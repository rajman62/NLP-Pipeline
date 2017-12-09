package implementations.lexicalutils;

import dk.brics.automaton.RunAutomaton;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Tokenization {
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

    static public Pair<Integer, Integer> runAutomatonLonguestMatch(RunAutomaton automaton, String s, Integer startPosition) {
        int length = automaton.run(s, startPosition);
        if (length == -1 || length == 0)
            return null;
        else
            return Pair.of(startPosition, startPosition + length - 1);
    }

    /**
     * An arc is a space in the string that can be accepted by one of the automatons (wordFSA, separatorFSA, eosSeparatorFSA)
     * They are characterised by a start and a end position in the input string
     *
     * @param input String to extract arcs from
     * @return a list of arcs
     */
    static public List<Set<Arc>> getArcs(String input, RunAutomaton wordFSA, RunAutomaton separatorFSA,
                                         RunAutomaton invisibleCharacterPattern, RunAutomaton eosSeparatorPattern) {
        LinkedList<Integer> stack = new LinkedList<>();

        List<Set<Arc>> out = new ArrayList<>(input.length());
        for (int i = 0; i < input.length(); i++) {
            out.add(i, new HashSet<>());
        }

        Pair<Integer, Integer> pSep = runAutomatonLonguestMatch(separatorFSA, input, 0);
        if (pSep != null) {
            stack.add(pSep.getRight() + 1);
            out.get(pSep.getLeft()).add(Arc.sepFromPatterns(pSep, input, invisibleCharacterPattern, eosSeparatorPattern));
            if (input.length() == pSep.getRight() + 1)
                return out;
        } else
            stack.add(0);

        while (!stack.isEmpty()) {
            Integer pos = stack.poll();
            for (Pair<Integer, Integer> pToken : runAutomatonAllMatch(wordFSA, input, pos)) {
                if (pToken.getRight() == input.length() - 1)
                    out.get(pToken.getLeft()).add(new Arc(pToken, input, Arc.Type.TOKEN));
                else {
                    pSep = runAutomatonLonguestMatch(separatorFSA, input, pToken.getRight() + 1);
                    if (pSep != null) {
                        out.get(pToken.getLeft()).add(new Arc(pToken, input, Arc.Type.TOKEN));
                        out.get(pSep.getLeft()).add(
                                Arc.sepFromPatterns(pSep, input, invisibleCharacterPattern, eosSeparatorPattern)
                        );
                        if (pSep.getRight() + 1 < input.length())
                            stack.add(pSep.getRight() + 1);
                    }
                }
            }
        }

        return out;
    }

    /**
     * Any arc that points to a position where no other arcs exist cannot make a valid tokenization later, so we filter
     * them out.
     */
    public static void filterImpossibleTokenization(List<Set<Arc>> arcs, Integer from) {
        for (int i = from; i >= 0; i--) {
            arcs.get(i).removeIf(a -> a.getEnd() < from && arcs.get(a.getEnd() + 1).size() == 0);
        }
    }

    /**
     * tokenizeFromArcs uses the arcs calculated in getArcs to return a list of sentences. Each sentence is a list of string,
     * where each string is either a separator, an eos, or a part of a token (wordFsa). These unit serve as the basis
     * for the chart
     *
     * @param arcsInInput the output of the getArcs function
     * @param input       the string to apply tokenization on
     * @return the tokenized input
     */
    static public List<List<String>> tokenizeFromArcs(List<Set<Arc>> arcsInInput, String input) {
        // buffer storing the position where tokens can be extracted
        boolean[] cut = new boolean[arcsInInput.size()];
        int arcLength = getFurthestReachingArcPosition(arcsInInput);
        if (arcLength == -1)
            return new ArrayList<>();
        else
            cut[arcLength] = true;

        // buffer storing where eos are
        // no arcs should jump over an eos, in that case we have an ambiguity
        boolean[] eos = new boolean[arcsInInput.size()];

        // filling in cut and eos
        for (Set<Arc> set : arcsInInput) {
            for (Arc arc : set) {
                cut[arc.getEnd()] = true;

                if (arc.isEOS())
                    for (int i = arc.getStart(); i <= arc.getEnd(); i++)
                        eos[i] = true;

            }
        }

        // filtering out unsafe eos
        for (Set<Arc> set : arcsInInput) {
            for (Arc arc : set) {
                if (!arc.isEOS())
                    for (int i = arc.getStart(); i <= arc.getEnd(); i++)
                        eos[i] = false;
            }
        }

        // finding subTokens (the smallest unit in the chart)
        // subTokensList represents a list of sentences
        // subTokens is a buffer to find the next sentence
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


    public static List<List<String>> markSpacesInTokens(List<Set<Arc>> arcs, List<List<String>> tokenizedSentences,
                                                        RunAutomaton invisibleCharacters, String input) {
        int tokenPos = 0;
        List<List<String>> out = new LinkedList<>();

        for (List<String> s : tokenizedSentences) {
            ArrayList<String> sentence = new ArrayList<>();
            out.add(sentence);
            for (String token : s) {
                int i = 0;
                int lastMatchPosition = 0;
                while (i < token.length()) {
                    Pair<Integer, Integer> match = runAutomatonLonguestMatch(invisibleCharacters, token, i);
                    if (match != null) {
                        if (lastMatchPosition != match.getLeft())
                            sentence.add(token.substring(lastMatchPosition, match.getLeft()));
                        sentence.add(token.substring(match.getLeft(), match.getRight() + 1));
                        arcs.get(match.getLeft() + tokenPos).add(
                                new Arc(
                                        Pair.of(match.getLeft() + tokenPos, match.getRight() + tokenPos),
                                        input,
                                        Arc.Type.INVISIBLE_SEP
                                ));
                        i = match.getRight() + 1;
                        lastMatchPosition = match.getRight() + 1;
                    } else
                        i += 1;
                }
                tokenPos += token.length();
                if (lastMatchPosition != token.length())
                    sentence.add(token.substring(lastMatchPosition, token.length()));
            }
        }

        return out;
    }

    public static Set<Arc> getFurthestReachingArcs(List<Set<Arc>> arcsInInput) {
        Set<Arc> out = new HashSet<>();
        int max = -1;
        for (Set<Arc> arcSet : arcsInInput) {
            for (Arc arc : arcSet) {
                if (arc.getEnd() > max) {
                    out.clear();
                    out.add(arc);
                    max = arc.getEnd();
                } else if (arc.getEnd() == max) {
                    out.add(arc);
                }
            }
        }
        return out;
    }

    public static int getFurthestReachingArcPosition(List<Set<Arc>> arcsInInput) {
        Optional<Integer> out = arcsInInput.stream().flatMap(Set::stream).map(Arc::getEnd)
                .max(Comparator.comparingInt(x -> x));
        return out.orElse(-1);
    }
}
