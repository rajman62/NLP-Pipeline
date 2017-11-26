package implementations;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import implementations.conffile.LexicalConf;
import implementations.filereaders.FSALoader;
import nlpstack.analyzers.LexicalAnalyzer;
import nlpstack.annotations.LexicalChart;
import nlpstack.annotations.StringSegment;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class DefaultLexicalAnalyzer extends LexicalAnalyzer {
    private Automaton wordFSA;
    private Automaton separatorFSA;
    private FSALoader fsaLoader = new FSALoader();

    DefaultLexicalAnalyzer(LexicalConf conf) throws Exception {
        wordFSA = fsaLoader.loadFromFile(conf.wordFSAPath);
        separatorFSA = fsaLoader.loadFromFile(conf.separatorFSAPath);
    }

    @Override
    public Stream<LexicalChart> apply(List<StringSegment> stringSegments) {
        return null;
    }

    public List<Pair<Integer, Integer>> getArcs(String input) {
        LinkedList<Integer> wordPosStack = new LinkedList<>();
        LinkedList<Integer> sepPosStack = new LinkedList<>();

        LinkedList<Pair<Integer, Integer>> wordTokens = new LinkedList<>();
        LinkedList<Pair<Integer, Integer>> sepTokens = new LinkedList<>();

        wordPosStack.add(0);
        sepPosStack.add(0);

        while (!wordPosStack.isEmpty() || !sepPosStack.isEmpty()) {
            if (!wordPosStack.isEmpty()) {
                List<Pair<Integer, Integer>> sepPrefix = runAutomaton(separatorFSA, input, wordPosStack.poll());
                for (Pair<Integer, Integer> i : sepPrefix) {
                    if (i.getRight() + 1 < input.length())
                        sepPosStack.add(i.getRight() + 1);
                    sepTokens.add(i);
                }
            }

            if (!sepPosStack.isEmpty()) {
                List<Pair<Integer, Integer>> wordPrefix = runAutomaton(wordFSA, input, sepPosStack.poll());
                for (Pair<Integer, Integer> i : wordPrefix) {
                    if (i.getRight() + 1 < input.length())
                        wordPosStack.add(i.getRight() + 1);
                    wordTokens.add(i);
                }
            }
        }

        return null;
    }

    /**
     * Returns all the prefixes that can be match on s, in increasing order.
     * A pair (a, b) is is the string that starts at position a, and ends at position b (included)
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