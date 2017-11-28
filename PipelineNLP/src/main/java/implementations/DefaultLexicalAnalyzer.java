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
    private FSALoader fsaLoader = new FSALoader();

    public DefaultLexicalAnalyzer(LexicalConf conf) throws Exception {
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
     * @param input String to extract arcs from
     * @return a list of arcs
     */
    public List<Set<Arc>> getArcs(String input) {
        // for every word sequence found in string, the position after the sequence is added to wordPosStack
        LinkedList<Integer> wordPosStack = new LinkedList<>();
        // sepPosStack and eosPosStack work in the same way as wordPosStack
        LinkedList<Integer> sepPosStack = new LinkedList<>();
        LinkedList<Integer> eosPosStack = new LinkedList<>();

        List<Set<Arc>> out = new ArrayList<>(input.length());
        for (int i = 0; i < input.length(); i++) {
            out.add(i, new HashSet<>());
        }

        wordPosStack.add(0);
        sepPosStack.add(0);

        while (!wordPosStack.isEmpty() || !sepPosStack.isEmpty() || !eosPosStack.isEmpty()) {
            if (!wordPosStack.isEmpty()) {
                arcDiscoveryAlgorithmStep(separatorFSA, input, wordPosStack.peek(), sepPosStack, Arc.Type.SEP, out);
                arcDiscoveryAlgorithmStep(eosSeparatorFSA, input, wordPosStack.peek(), eosPosStack, Arc.Type.ENDSEP, out);
                wordPosStack.pop();
            }

            if (!sepPosStack.isEmpty()) {
                arcDiscoveryAlgorithmStep(wordFSA, input, sepPosStack.pop(), wordPosStack, Arc.Type.TOKEN, out);
            }

            if (!eosPosStack.isEmpty()) {
                arcDiscoveryAlgorithmStep(wordFSA, input, eosPosStack.pop(), wordPosStack, Arc.Type.TOKEN, out);
            }
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
    private void arcDiscoveryAlgorithmStep(Automaton automaton, String input, int position, LinkedList<Integer> stack, Arc.Type type, List<Set<Arc>> out) {
        List<Pair<Integer, Integer>> prefixes = runAutomaton(automaton, input, position);
        for (Pair<Integer, Integer> prefix : prefixes) {
            if (prefix.getRight() + 1 < input.length()) {
                stack.add(prefix.getRight() + 1);
            }
            out.get(position).add(new Arc(prefix, input, type));
        }
    }

    /**
     * Any arc that points to a position where no other arcs exist cannot be a valid tokenization.
     * This method remove these cases.
     */
    private void filterImpossibleTokenization(List<Set<Arc>> out) {
        for (int i = out.size() - 2; i >= 0; i--) {
            out.get(i).removeIf(a -> a.getEnd() + 1 < out.size() && out.get(a.getEnd() + 1).size() == 0);
        }
    }

    public Chart getChart() {
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
}