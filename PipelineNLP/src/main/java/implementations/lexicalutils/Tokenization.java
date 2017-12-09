package implementations.lexicalutils;

import dk.brics.automaton.RunAutomaton;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Tokenization {
    /**
     * tokenizeFromArcs uses the arcs calculated in parseArcs to return a list of sentences. Each sentence is a list of string,
     * where each string is either a separator, an eos, or a part of a token (wordFsa). These unit serve as the basis
     * for the chart
     *
     * @param arcParsing the output of the parseArcs function
     * @param input      the string to apply tokenization on
     * @return the tokenized input
     */
    static public List<List<String>> tokenizeFromArcs(ArcParsing arcParsing, String input) {
        // buffer storing the position where tokens can be extracted
        boolean[] cut = new boolean[arcParsing.getFurthestReachingPosition() + 1];
        if (arcParsing.getFurthestReachingPosition() == -1)
            return new ArrayList<>();
        else
            cut[arcParsing.getFurthestReachingPosition()] = true;

        // buffer storing where eos are
        // no arcs should jump over an eos, in that case we have an ambiguity
        boolean[] eos = new boolean[arcParsing.getFurthestReachingPosition() + 1];

        // filling in cut and eos
        for (Set<Arc> set : arcParsing) {
            for (Arc arc : set) {
                cut[arc.getEnd()] = true;

                if (arc.isEOS())
                    for (int i = arc.getStart(); i <= arc.getEnd(); i++)
                        eos[i] = true;

            }
        }

        // filtering out unsafe eos
        for (Set<Arc> set : arcParsing) {
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

        while (i < arcParsing.getFurthestReachingPosition() + 1) {
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
     * markSpacesInTokens finds the places in tokens where invisibleCharacters fit. It adds an arc in arcParing to note
     * them, and returns an updated version of the tokenization.
     *
     * @param arcParsing the output of the parseArcs function
     * @param tokenizedSentences the output of tokenizeFromArcs
     * @param invisibleCharacters the pattern of invisible characters
     * @param input the initial input string
     * @return updated version of the tokenization (where invisibleCharacters match are now their own token)
     */
    public static List<List<String>> markSpacesInTokens(ArcParsing arcParsing, List<List<String>> tokenizedSentences,
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
                    Pair<Integer, Integer> match = AutomatonUtils.runAutomatonLongestMatch(invisibleCharacters, token, i);
                    if (match != null) {
                        if (lastMatchPosition != match.getLeft())
                            sentence.add(token.substring(lastMatchPosition, match.getLeft()));
                        sentence.add(token.substring(match.getLeft(), match.getRight() + 1));
                        arcParsing.getArcsStartingAt(match.getLeft() + tokenPos).add(
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
}
