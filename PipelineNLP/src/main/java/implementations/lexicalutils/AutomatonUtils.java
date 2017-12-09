package implementations.lexicalutils;

import dk.brics.automaton.RunAutomaton;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;

public class AutomatonUtils {
    /**
     * Returns all the prefixes that can be match on s, in increasing order.
     * A pair (a, b) is the string that starts at position a, and ends at position b (included)
     *
     * @param automaton     The automaton to use to find the prefixes
     * @param s             The string on which we want to find the prefixes
     * @param startPosition The position in s where the prefixes are looked for
     * @return List of pairs of the matched prefixes starting at position startPosition
     */
    static public List<Pair<Integer, Integer>> runAutomatonAllMatch(RunAutomaton automaton, String s, Integer startPosition) {
        LinkedList<Pair<Integer, Integer>> exploredStatesWithStringPosition = new LinkedList<>();
        LinkedList<Pair<Integer, Integer>> out = new LinkedList<>();

        exploredStatesWithStringPosition.add(Pair.of(automaton.getInitialState(), startPosition));

        while (!exploredStatesWithStringPosition.isEmpty()) {
            Pair<Integer, Integer> currentStateAndStringPosition = exploredStatesWithStringPosition.poll();

            int nextState = automaton.step(
                    currentStateAndStringPosition.getLeft(),
                    s.charAt(currentStateAndStringPosition.getRight())
            );

            if (nextState != -1 && automaton.isAccept(nextState)) {
                out.add(Pair.of(startPosition, currentStateAndStringPosition.getRight()));
            }

            if (nextState != -1 && currentStateAndStringPosition.getRight() < s.length() - 1)
                exploredStatesWithStringPosition.add(
                        Pair.of(
                                nextState,
                                currentStateAndStringPosition.getRight() + 1
                        )
                );
        }

        return out;
    }

    /**
     * Returns the longest prefix on s starting at position startPosition
     * @param automaton The automaton to use to find the prefixe
     * @param s The string on which we want to find the prefixes
     * @param startPosition The position in s where the prefixes are looked for
     * @return The longest matching prefix as a pair (startPosition, endPosition)
     */
    static public Pair<Integer, Integer> runAutomatonLongestMatch(RunAutomaton automaton, String s, Integer startPosition) {
        int length = automaton.run(s, startPosition);
        if (length == -1 || length == 0)
            return null;
        else
            return Pair.of(startPosition, startPosition + length - 1);
    }
}
