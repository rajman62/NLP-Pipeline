package implementations;

import com.google.common.collect.Multiset;
import dk.brics.automaton.RunAutomaton;
import implementations.conffile.LexicalConf;
import implementations.filereaders.FSALoader;
import implementations.fomawrapper.FomaWrapper;
import implementations.lexicalutils.Arc;
import implementations.lexicalutils.ArcParsing;
import implementations.lexicalutils.Tokenization;
import nlpstack.analyzers.LexicalAnalyzer;
import nlpstack.annotations.ReservedTags;
import nlpstack.annotations.LexicalChart;
import nlpstack.annotations.StringSegment;
import nlpstack.communication.Chart;
import nlpstack.communication.ErrorLogger;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class DefaultLexicalAnalyzer extends LexicalAnalyzer {
    private RunAutomaton wordFSA;
    private RunAutomaton separatorFSA;
    private RunAutomaton invisibleCharacterPattern;
    private RunAutomaton eosSeparatorPattern;
    private FomaWrapper foma;
    private ErrorLogger errorLogger;

    // enum used in getCharts(List<StringSegment> input)
    private enum LastSegmentType {
        END, MIDDLE
    }


    public DefaultLexicalAnalyzer(LexicalConf conf, ErrorLogger errorLogger) throws Exception {
        FSALoader fsaLoader = new FSALoader();
        wordFSA = new RunAutomaton(fsaLoader.loadFromFile(conf.wordFSAPath));
        separatorFSA = new RunAutomaton(fsaLoader.loadFromFile(conf.separatorFSAPath));
        invisibleCharacterPattern = new RunAutomaton(FSALoader.parseRegex(conf.invisibleCharacterRegex));
        eosSeparatorPattern = new RunAutomaton(FSALoader.parseRegex(conf.eosSeparatorRegex));
        foma = new FomaWrapper(conf.FomaBinPath, conf.FomaConfPath, errorLogger);
        this.errorLogger = errorLogger;
    }

    @Override
    public Stream<LexicalChart> apply(List<StringSegment> stringSegments) {
        try {
            return getCharts(stringSegments).stream().map(LexicalChart::fromChart);
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    /**
     * Using the output of get arcs and the tokenizer, this function uses foma to fill in charts
     *
     * @param arcParsing output of parseArcs
     * @param tokensList  output of tokenizeFromArcs
     * @return list of charts that can be passed later to a CFG parser
     * @throws IOException if there is a problem communicating with foma
     */
    public List<Chart<String, String>> getCharts(ArcParsing arcParsing, List<List<String>> tokensList, StringSegment stringSegment) throws IOException {
        int inputPosition = 0;

        if (arcParsing.getFurthestReachingPosition() == -1 ||
                arcParsing.length() == 0 || tokensList.size() == 0)
            return new ArrayList<>();

        // tokenPositions indicates where we can ignore tokens (the places where tokenPositions is false)
        boolean tokenPositions[] = new boolean[arcParsing.getFurthestReachingPosition() + 1];
        for (int i = 0 ; i < tokenPositions.length ; i++)
            tokenPositions[i] = true;
        for (List<String> s : tokensList) {
            for (String token : s) {
                for (Arc arc : arcParsing.getArcsStartingAt(inputPosition))
                    if (!arc.isVisible())
                        for (int i = arc.getStart(); i <= arc.getEnd(); i++)
                            tokenPositions[i] = false;
                inputPosition += token.length();
            }
        }

        // mapping arcs to positions in chart
        HashMap<Arc, Integer> mappingToStartPos = new HashMap<>();
        HashMap<Arc, Integer> mappingToLength = new HashMap<>();

        HashMap<Integer, Set<Pair<Arc, Integer>>> bufferGetLength = new HashMap<>();
        inputPosition = 0;
        int tokenPos = 0;

        for (List<String> s : tokensList) {
            tokenPos = 1;
            for (String token : s) {
                if (tokenPositions[inputPosition]) {
                    for (Arc arc : arcParsing.getArcsStartingAt(inputPosition)) {
                        mappingToStartPos.put(arc, tokenPos);

                        if (!bufferGetLength.containsKey(arc.getEnd() + 1))
                            bufferGetLength.put(arc.getEnd() + 1, new HashSet<>());

                        bufferGetLength.get(arc.getEnd() + 1).add(Pair.of(arc, tokenPos));
                    }
                    tokenPos += 1;
                }
                inputPosition += token.length();
                if (bufferGetLength.containsKey(inputPosition)) {
                    for (Pair<Arc, Integer> pair : bufferGetLength.get(inputPosition))
                        mappingToLength.put(pair.getLeft(), tokenPos - pair.getRight());

                    bufferGetLength.remove(inputPosition);
                }
            }
        }
        if (bufferGetLength.containsKey(inputPosition)) {
            for (Pair<Arc, Integer> pair : bufferGetLength.get(inputPosition))
                mappingToLength.put(pair.getLeft(), tokenPos - pair.getRight());
        }

        // creating and filling charts
        LinkedList<Chart<String, String>> out = new LinkedList<>();
        inputPosition = 0;
        for (List<String> s : tokensList) {
            List<String> sCopy = new ArrayList<>(s);
            int position2 = inputPosition;
            Iterator<String> it = sCopy.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (!tokenPositions[position2])
                    it.remove();
                position2 += next.length();
            }
            Chart<String, String> chart = Chart.getEmptyChart(sCopy);

            for (String token : s) {
                if (tokenPositions[inputPosition]) {
                    for (Arc arc : arcParsing.getArcsStartingAt(inputPosition)) {
                        List<String> possibleTags = getTags(arc.getString());
                        if (!possibleTags.isEmpty()) {
                            for (String tag : possibleTags)
                                chart.addRule(mappingToLength.get(arc), mappingToStartPos.get(arc), tag);
                        } else {
                            errorLogger.lexicalError(
                                    String.format("\"%s\" not recognized by the transducer", arc.getString()),
                                    stringSegment);
                            chart.addRule(mappingToLength.get(arc), mappingToStartPos.get(arc), ReservedTags.UNKNOWN);
                        }
                    }
                }
                inputPosition += token.length();
            }

            out.add(chart);
        }

        // checking the last arc to see if the last chart is a complete sentence
        if (arcParsing.getFurthestReachingArcSet().stream().anyMatch(Arc::isEOS))
            out.getLast().setCompleteSentence(true);
        else
            out.getLast().setCompleteSentence(false);

        return out;
    }

    /**
     * Simply executes the whole getChart pipeline (parseArcs, tokenizeFromArcs, markSpacesInTokens, getCharts).
     * Errors are logged to the ErrorLogger
     *
     * @param input the string to extract charts from
     * @return list of charts that can be passed later to a CFG parser
     * @throws IOException if there is a problem communicating with foma
     */
    public List<Chart<String, String>> getCharts(String input, StringSegment stringSegment) throws IOException {
        if (input.equals(""))
            errorLogger.lexicalError("Empty StringSegment", stringSegment);
        ArcParsing arcsParsing = ArcParsing.parseArcs(input, wordFSA, separatorFSA, invisibleCharacterPattern, eosSeparatorPattern);

        if (arcsParsing.getFurthestReachingPosition() < input.length() - 1) {
            if (arcsParsing.getFurthestReachingPosition() == -1) {
                errorLogger.lexicalError("No possible tokenization", stringSegment);
                return new ArrayList<>();
            } else {
                errorLogger.lexicalError(
                        String.format(
                                "Partial tokenization that stopped at position %d",
                                arcsParsing.getFurthestReachingPosition()),
                        stringSegment);
            }
        }
        arcsParsing.filterImpossibleTokenization();
        List<List<String>> tokenization = Tokenization.tokenizeFromArcs(arcsParsing, input);
        tokenization = Tokenization.markSpacesInTokens(arcsParsing, tokenization, invisibleCharacterPattern, input);
        return getCharts(arcsParsing, tokenization, stringSegment);
    }

    /**
     * @param input the input returned by the annotation parser: the content of StringWithAnnotations
     * @return initialized charts that can be passed later to a CFG parser
     * @throws IOException if there is a problem communicating with foma
     */
    public List<Chart<String, String>> getCharts(List<StringSegment> input) throws IOException {
        LinkedList<Chart<String, String>> out = new LinkedList<>();
        LastSegmentType lastSegmentType = LastSegmentType.END;

        for (StringSegment stringSegment : input) {
            if (!stringSegment.isAnnotated()) {
                List<Chart<String, String>> charts = getCharts(stringSegment.getNoneAnnotatedString(), stringSegment);
                charts.removeIf(x -> x.getSize() == 0);

                if (!charts.isEmpty() && !out.isEmpty() && lastSegmentType.equals(LastSegmentType.MIDDLE)) {
                    out.set(out.size() - 1, combineCharts(out.getLast(), charts.get(0)));
                    if (charts.size() == 1) {
                        if (charts.get(charts.size() - 1).isCompleteSentence())
                            lastSegmentType = LastSegmentType.END;
                        else
                            lastSegmentType = LastSegmentType.MIDDLE;
                    }
                    charts.remove(0);
                }

                if (!charts.isEmpty()) {
                    if (charts.get(charts.size() - 1).isCompleteSentence())
                        lastSegmentType = LastSegmentType.END;
                    else
                        lastSegmentType = LastSegmentType.MIDDLE;

                    out.addAll(charts);
                }

            } else {
                if (!stringSegment.getAnnotatedTag().equals(ReservedTags.EOS)) {
                    if (lastSegmentType.equals(LastSegmentType.MIDDLE)) {
                        out.set(out.size() - 1,
                                combineCharts(
                                        out.get(out.size() - 1),
                                        chartFromAnnotatedString(stringSegment.getAnnotation()))
                        );
                    } else {
                        out.add(chartFromAnnotatedString(stringSegment.getAnnotation()));
                        lastSegmentType = LastSegmentType.MIDDLE;
                    }
                } else {
                    out.set(out.size() - 1,
                            combineCharts(
                                    out.get(out.size() - 1),
                                    chartFromAnnotatedString(stringSegment.getAnnotation()))
                    );
                    lastSegmentType = LastSegmentType.END;
                }
            }
        }

        return out;
    }

    private Chart<String, String> chartFromAnnotatedString(Pair<String, String> annotation) {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add(annotation.getLeft());
        Chart<String, String> chart = Chart.getEmptyChart(tokens);
        chart.addRule(1, 1, annotation.getRight());
        return chart;
    }

    private Chart<String, String> combineCharts(Chart<String, String> chart1, Chart<String, String> chart2) {
        List<String> tokens = chart1.getTokens();
        tokens.addAll(chart2.getTokens());

        Chart<String, String> out = Chart.getEmptyChart(tokens);

        for (Triple<Integer, Integer, Multiset<String>> el : chart1) {
            for (String tag : el.getRight())
                out.addRule(el.getLeft(), el.getMiddle(), tag);
        }

        int size1 = chart1.getSize();

        for (Triple<Integer, Integer, Multiset<String>> el : chart2) {
            for (String tag : el.getRight())
                out.addRule(el.getLeft(), size1 + el.getMiddle(), tag);
        }

        return out;
    }

    private List<String> getTags(String word) throws IOException {
        return foma.applyUp(word).stream()
                .map(x -> {
                    String[] list = x.split("\\+");
                    return list[list.length - 1];
                }).collect(toList());
    }
}