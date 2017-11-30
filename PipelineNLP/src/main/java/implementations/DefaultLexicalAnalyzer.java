package implementations;

import com.google.common.collect.Multiset;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import implementations.conffile.LexicalConf;
import implementations.filereaders.FSALoader;
import implementations.fomawrapper.FomaWrapper;
import implementations.lexicalutils.Arc;
import nlpstack.analyzers.LexicalAnalyzer;
import nlpstack.analyzers.ReservedTags;
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
    private Automaton wordFSA;
    private Automaton separatorFSA;
    private Automaton eosSeparatorFSA;
    private FomaWrapper foma;
    private ErrorLogger errorLogger;

    // enum used in getCharts(List<StringSegment> input)
    private enum LastSegmentType {
        END, MIDDLE
    }


    public DefaultLexicalAnalyzer(LexicalConf conf, ErrorLogger errorLogger) throws Exception {
        FSALoader fsaLoader = new FSALoader();
        wordFSA = fsaLoader.loadFromFile(conf.wordFSAPath);
        separatorFSA = fsaLoader.loadFromFile(conf.separatorFSAPath);
        eosSeparatorFSA = fsaLoader.loadFromFile(conf.eosSeparatorFSAPath);
        foma = new FomaWrapper(conf.FomaBinPath, conf.FomaConfPath);
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
     * Returns all the prefixes that can be match on s, in increasing order.
     * A pair (a, b) is the string that starts at position a, and ends at position b (included)
     *
     * @param automaton     The automaton to use to find the prefixes
     * @param s             The string on which we want to find the prefixes
     * @param startPosition The position in s where the prefixes are looked for
     * @return List of pairs of the matched prefixes starting at position startPosition
     */
    static public List<Pair<Integer, Integer>> runAutomaton(Automaton automaton, String s, Integer startPosition) {
        LinkedList<Pair<State, Integer>> exploredStatesWithStringPosition = new LinkedList<>();
        LinkedList<Pair<Integer, Integer>> out = new LinkedList<>();

        Set<State> nextStates = new HashSet<>();
        exploredStatesWithStringPosition.add(Pair.of(automaton.getInitialState(), startPosition));

        while (!exploredStatesWithStringPosition.isEmpty()) {
            Pair<State, Integer> currentStateAndStringPosition = exploredStatesWithStringPosition.poll();

            nextStates.clear();

            currentStateAndStringPosition.getLeft().step(
                    s.charAt(currentStateAndStringPosition.getRight()),
                    nextStates
            );

            for (State nState : nextStates) {
                if (nState.isAccept()) {
                    out.add(Pair.of(startPosition, currentStateAndStringPosition.getRight()));
                }

                if (currentStateAndStringPosition.getRight() < s.length() - 1)
                    exploredStatesWithStringPosition.add(
                            Pair.of(
                                    nState,
                                    currentStateAndStringPosition.getRight() + 1
                            )
                    );
            }
        }

        return out;
    }

    /**
     * An arc is a space in the string that can be accepted by one of the automatons (wordFSA, separatorFSA, eosSeparatorFSA)
     * They are characterised by a start and a end position in the input string
     *
     * @param input String to extract arcs from
     * @return a list of arcs
     */
    public List<Set<Arc>> getArcs(String input) {
        LinkedList<Integer> stack = new LinkedList<>();
        HashSet<Integer> processedPositions = new HashSet<>();
        // we have to use 'positionsAddedToStack' because 'stack.contains' is a O(n) operation
        HashSet<Integer> positionsAddedToStack = new HashSet<>();

        List<Set<Arc>> out = new ArrayList<>(input.length());
        for (int i = 0; i < input.length(); i++) {
            out.add(i, new HashSet<>());
        }

        stack.add(0);

        while (!stack.isEmpty()) {
            arcDiscoveryAlgorithmStep(separatorFSA, input, stack.peek(), stack, Arc.Type.SEP, out,
                    processedPositions, positionsAddedToStack);
            arcDiscoveryAlgorithmStep(eosSeparatorFSA, input, stack.peek(), stack, Arc.Type.ENDSEP, out,
                    processedPositions, positionsAddedToStack);
            arcDiscoveryAlgorithmStep(wordFSA, input, stack.peek(), stack, Arc.Type.TOKEN, out,
                    processedPositions, positionsAddedToStack);
            processedPositions.add(stack.pop());
        }

        return out;
    }

    /**
     * @param automaton the automaton that will try to read a new token (can be multiple)
     * @param input     the input string to tokenize
     * @param position  the position to start at in input
     * @param stack     the stack the will be filled with the end of the sequences found by the automaton
     * @param type      the type Arc.Type that should be used when when instantiating and adding arcs to out
     * @param out       the output that will be returned by getArcs
     */
    private void arcDiscoveryAlgorithmStep(Automaton automaton, String input, int position, LinkedList<Integer> stack,
                                           Arc.Type type, List<Set<Arc>> out, HashSet<Integer> processedPositions, HashSet<Integer> positionsAddedToStack) {
        List<Pair<Integer, Integer>> prefixes = runAutomaton(automaton, input, position);
        for (Pair<Integer, Integer> prefix : prefixes) {
            int stackPos = prefix.getRight() + 1;
            if (stackPos < input.length() && !processedPositions.contains(stackPos) && !positionsAddedToStack.contains(stackPos)) {
                stack.add(stackPos);
                positionsAddedToStack.add(stackPos);
            }
            out.get(position).add(new Arc(prefix, input, type));
        }
    }

    /**
     * Any arc that points to a position where no other arcs exist cannot make a valid tokenization later, so we filter
     * them out.
     */
    public void filterImpossibleTokenization(List<Set<Arc>> out, Integer from) {
        for (int i = from - 1; i >= 0; i--) {
            out.get(i).removeIf(a -> a.getEnd() + 1 < out.size() && out.get(a.getEnd() + 1).size() == 0);
        }
    }

    /**
     * tokenize uses the arcs calculated in getArcs to return a list of sentences. Each sentence is a list of string,
     * where each string is either a separator, an eos, or a part of a token (wordFsa). These unit serve as the basis
     * for the chart
     *
     * @param arcsInInput the output of the getArcs function
     * @param input       the string to apply tokenization on
     * @return the tokenized input
     */
    public List<List<String>> tokenize(List<Set<Arc>> arcsInInput, String input) {
        // buffer storing the position where tokens can be extracted
        boolean[] cut = new boolean[arcsInInput.size()];
        cut[arcsInInput.size() - 1] = true;

        // buffer storing where eos are
        // no arcs should jump over an eos, in that case we have an ambiguity
        boolean[] eos = new boolean[arcsInInput.size()];

        // filling in cut and eos
        for (Set<Arc> set : arcsInInput) {
            for (Arc arc : set) {
                cut[arc.getEnd()] = true;

                if (arc.getType().equals(Arc.Type.ENDSEP))
                    for (int i = arc.getStart(); i <= arc.getEnd(); i++)
                        eos[i] = true;
            }
        }

        // filtering out unsafe eos
        for (Set<Arc> set : arcsInInput) {
            for (Arc arc : set) {
                if (!arc.getType().equals(Arc.Type.ENDSEP))
                    for (int i = arc.getStart(); i <= arc.getEnd(); i++)
                        eos[i] = false;
            }
        }

        // finding subTokens (the smallest unit in the chart)
        List<List<String>> subTokensList = new LinkedList<>();
        ArrayList<String> subTokens = new ArrayList<>();
        int start = 0;
        int i = 0;
        boolean sentenceFinished = false;

        while (i < arcsInInput.size()) {
            if (eos[i]) {
                i++;
                while (i < eos.length && eos[i])
                    i++;
                subTokens.add(input.substring(start, i));
                start = i;
                subTokensList.add(subTokens);
                subTokens = new ArrayList<>();
                sentenceFinished = true;
            } else if (i < cut.length && cut[i]) {
                i++;
                subTokens.add(input.substring(start, i));
                start = i;
                sentenceFinished = false;
            } else {
                i++;
            }
        }

        if (!sentenceFinished)
            subTokensList.add(subTokens);

        return subTokensList;
    }

    /**
     * Using the output of get arcs and the tokenizer, this function uses foma to fill in charts
     *
     * @param arcsInInput output of getArcs
     * @param tokensList  output of tokenize
     * @return list of charts that can be passed later to a CFG parser
     * @throws IOException if there is a problem communicating with foma
     */
    public List<Chart> getCharts(List<Set<Arc>> arcsInInput, List<List<String>> tokensList, StringSegment stringSegment) throws IOException {
        int inputPosition = 0;

        if (arcsInInput.size() == 0 || tokensList.size() == 0)
            return new ArrayList<>();

        // tokenPositions indicates where we can ignore tokens
        boolean tokenPositions[] = new boolean[arcsInInput.size()];
        for (List<String> s : tokensList) {
            for (String token : s) {
                for (Arc arc : arcsInInput.get(inputPosition))
                    if (arc.getType().equals(Arc.Type.TOKEN))
                        for (int i = arc.getStart(); i <= arc.getEnd(); i++)
                            tokenPositions[i] = true;
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
                    for (Arc arc : arcsInInput.get(inputPosition)) {
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
        LinkedList<Chart> out = new LinkedList<>();
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
            Chart chart = Chart.getEmptyChart(sCopy);

            for (String token : s) {
                if (tokenPositions[inputPosition]) {
                    for (Arc arc : arcsInInput.get(inputPosition)) {
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
        Pair<Integer, Set<Arc>> p = getLastArcSet(arcsInInput);
        Set<Arc> lastArcSet = p.getRight();
        int lastArcPosition = p.getLeft();
        if (lastArcSet != null
                && lastArcSet.stream().allMatch(x -> x.getType().equals(Arc.Type.ENDSEP))
                && !tokenPositions[lastArcPosition]) {
            out.getLast().setCompleteSentence(true);
        } else
            out.getLast().setCompleteSentence(false);


        return out;
    }

    private Pair<Integer, Set<Arc>> getLastArcSet(List<Set<Arc>> arcsInInput) {
        ListIterator<Set<Arc>> li = arcsInInput.listIterator(arcsInInput.size());
        int lastArcPosition = arcsInInput.size();
        while (li.hasPrevious()) {
            Set<Arc> arcs = li.previous();
            lastArcPosition -= 1;
            if (!arcs.isEmpty())
                return Pair.of(lastArcPosition, arcs);
        }
        return Pair.of(0, null);
    }

    /**
     * Simply executes the whole getChart pipeline (getArcs, tokenize, getCharts). Unlike the other methods, errors are
     * logged to the ErrorLogger
     *
     * @param input the string to extract charts from
     * @return list of charts that can be passed later to a CFG parser
     * @throws IOException if there is a problem communicating with foma
     */
    public List<Chart> getCharts(String input, StringSegment stringSegment) throws IOException {
        if (input.equals(""))
            errorLogger.lexicalError("Empty StringSegment", stringSegment);
        List<Set<Arc>> arcsInInput = getArcs(input);
        Pair<Integer, Set<Arc>> p = getLastArcSet(arcsInInput);
        if (p.getLeft() < input.length() - 1) {
            if (p.getLeft() == 0) {
                errorLogger.lexicalError("No possible tokenization", stringSegment);
                return new ArrayList<>();
            } else {
                errorLogger.lexicalError(
                        String.format("Partial tokenization that stopped at position %d", p.getLeft()), stringSegment
                );
            }
        }
        filterImpossibleTokenization(arcsInInput, p.getLeft());
        return getCharts(arcsInInput, tokenize(arcsInInput, input), stringSegment);
    }

    /**
     * @param input the input returned by the annotation parser: the content of StringWithAnnotations
     * @return initialized charts that can be passed later to a CFG parser
     * @throws IOException if there is a problem communicating with foma
     */
    public List<Chart> getCharts(List<StringSegment> input) throws IOException {
        LinkedList<Chart> out = new LinkedList<>();
        LastSegmentType lastSegmentType = LastSegmentType.END;

        for (StringSegment stringSegment : input) {
            if (!stringSegment.isAnnotated()) {
                List<Chart> charts = getCharts(stringSegment.getNoneAnnotatedString(), stringSegment);
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
                } else
                    lastSegmentType = LastSegmentType.END;
            }
        }

        return out;
    }

    private Chart chartFromAnnotatedString(Pair<String, String> annotation) {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add(annotation.getLeft());
        Chart chart = Chart.getEmptyChart(tokens);
        chart.addRule(1, 1, annotation.getRight());
        return chart;
    }

    private Chart combineCharts(Chart chart1, Chart chart2) {
        List<String> tokens = chart1.getTokens();
        tokens.addAll(chart2.getTokens());

        Chart out = Chart.getEmptyChart(tokens);

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