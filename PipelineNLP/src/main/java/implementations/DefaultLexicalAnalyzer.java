package implementations;

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
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class DefaultLexicalAnalyzer extends LexicalAnalyzer {
    private Automaton wordFSA;
    private Automaton separatorFSA;
    private Automaton eosSeparatorFSA;
    private FomaWrapper foma;

    public DefaultLexicalAnalyzer(LexicalConf conf) throws Exception {
        FSALoader fsaLoader = new FSALoader();
        wordFSA = fsaLoader.loadFromFile(conf.wordFSAPath);
        separatorFSA = fsaLoader.loadFromFile(conf.separatorFSAPath);
        eosSeparatorFSA = fsaLoader.loadFromFile(conf.eosSeparatorFSAPath);
        foma = new FomaWrapper(conf.FomaBinPath, conf.FomaConfPath);
    }

    @Override
    public Stream<LexicalChart> apply(List<StringSegment> stringSegments) {
        return null;
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

        filterImpossibleTokenization(out);

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
    private void filterImpossibleTokenization(List<Set<Arc>> out) {
        for (int i = out.size() - 2; i >= 0; i--) {
            out.get(i).removeIf(a -> a.getEnd() + 1 < out.size() && out.get(a.getEnd() + 1).size() == 0);
        }
    }

    /**
     * tokenize uses the arcs calculated in getArcs to return a list of sentences. Each sentence is a list of string,
     * where each string is either a separator, an eos, or a part of a token (wordFsa). These unit serve as the basis
     * for the chart
     */
    public List<List<String>> tokenize(List<Set<Arc>> out, String input) {
        // buffer storing the position where tokens can be extracted
        boolean[] cut = new boolean[out.size() - 1];

        // buffer storing where eos are
        // no arcs should jump over an eos, in that case we have an ambiguity
        boolean[] eos = new boolean[out.size()];

        // filling in cut and eos
        for (Set<Arc> set : out) {
            for (Arc arc : set) {
                if (arc.getEnd() < out.size() - 1)
                    cut[arc.getEnd()] = true;

                if (arc.getType().equals(Arc.Type.ENDSEP))
                    for (int i = arc.getStart(); i <= arc.getEnd(); i++)
                        eos[i] = true;
            }
        }

        // filtering out unsafe eos
        for (Set<Arc> set : out) {
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

        while (i < out.size()) {
            if (eos[i]) {
                i++;
                while (i < eos.length && eos[i])
                    i++;
                subTokens.add(input.substring(start, i));
                start = i;
                subTokensList.add(subTokens);
                subTokens = new ArrayList<>();
            } else if (cut[i]) {
                i++;
                subTokens.add(input.substring(start, i));
                start = i;
            } else {
                i++;
            }
        }

        return subTokensList;
    }


    public List<Chart> getCharts(List<Set<Arc>> arcsInInput, String input) throws IOException {
        List<List<String>> tokensList = tokenize(arcsInInput, input);
        int inputPosition = 0;

        // tokenPositions indicates to us where we can ignore tokens
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
                        if (arc.getType().equals(Arc.Type.TOKEN)) {
                            mappingToStartPos.put(arc, tokenPos);

                            if (!bufferGetLength.containsKey(arc.getEnd() + 1))
                                bufferGetLength.put(arc.getEnd() + 1, new HashSet<>());

                            bufferGetLength.get(arc.getEnd() + 1).add(Pair.of(arc, tokenPos));
                        }
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
            while(it.hasNext()){
                String next = it.next();
                if (!tokenPositions[position2])
                    it.remove();
                position2 += next.length();
            }
            Chart chart = Chart.getEmptyChart(sCopy);

            for (String token : s) {
                for (Arc arc : arcsInInput.get(inputPosition)) {
                    if (arc.getType().equals(Arc.Type.TOKEN)) {
                        List<String> possibleTags = foma.applyUp(arc.getString());
                        if (possibleTags.size() > 0)
                            for (String tag : possibleTags)
                                chart.addRule(mappingToLength.get(arc), mappingToStartPos.get(arc), tag);
                        else
                            chart.addRule(mappingToLength.get(arc), mappingToStartPos.get(arc), ReservedTags.UNKNOWN);
                    }
                }
                inputPosition += token.length();
            }

            out.add(chart);
        }

        return out;
    }

    public List<Chart> getCharts(String input) throws IOException {
        return getCharts(getArcs(input), input);
    }
}