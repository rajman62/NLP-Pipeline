package implementations.lexicalutils;

import dk.brics.automaton.RunAutomaton;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class ArcParsing implements Iterable<Set<Arc>> {
    private int furthestReachingPosition;
    private Set<Arc> furthestReachingArcSet;
    private List<Set<Arc>> arcStartPositions;


    private ArcParsing(List<Set<Arc>> arcStartPositions, Set<Arc> furthestReachingArcSet, int furthestReachingPosition) {
        this.arcStartPositions = arcStartPositions;
        this.furthestReachingArcSet = furthestReachingArcSet;
        this.furthestReachingPosition = furthestReachingPosition;
    }

    /**
     * An arc is a space in the string that can be accepted by one of the automaton: wordFSA or separatorFSA.
     * Arcs are characterised by a start and a end position in the input string. An Arc can have four different type:
     * TOKEN, VISIBLE_SEP, INVISIBLE_SEP, VISIBLE_EOS, INVISIBLE_EOS
     *
     * EOS means End Of Sentence, SEP stands for separator. And EOS or a SEP can be visible or invisible. If one is
     * invisible, getCharts will not put it in the chart.
     *
     * @param input                     String to extract arcs from
     * @param wordFSA                   FSA containing the wrd lexicon
     * @param separatorFSA              FSA containing the separator lexicon
     * @param invisibleCharacterPattern pattern of all invisible characters
     * @param eosSeparatorPattern       pattern of all eos separators
     * @return an ArcParsing
     */
    static public ArcParsing parseArcs(String input, RunAutomaton wordFSA, RunAutomaton separatorFSA,
                                       RunAutomaton invisibleCharacterPattern, RunAutomaton eosSeparatorPattern) {
        LinkedList<Integer> stack = new LinkedList<>();

        int furthestReachingPosition = -1;
        Set<Arc> furthestReachingArcSet = new HashSet<>();

        List<Set<Arc>> out = new ArrayList<>(input.length());
        for (int i = 0; i < input.length(); i++) {
            out.add(i, new HashSet<>());
        }

        Pair<Integer, Integer> pSep = AutomatonUtils.runAutomatonLongestMatch(separatorFSA, input, 0);
        if (pSep != null) {
            stack.add(pSep.getRight() + 1);
            Arc arc = Arc.sepFromPatterns(pSep, input, invisibleCharacterPattern, eosSeparatorPattern);
            out.get(pSep.getLeft()).add(arc);
            if (input.length() == pSep.getRight() + 1) {
                furthestReachingPosition = pSep.getRight();
                furthestReachingArcSet.add(arc);
                return new ArcParsing(out, furthestReachingArcSet, furthestReachingPosition);
            }
        } else
            stack.add(0);

        while (!stack.isEmpty()) {
            Integer pos = stack.poll();
            for (Pair<Integer, Integer> pToken : AutomatonUtils.runAutomatonAllMatch(wordFSA, input, pos)) {
                if (pToken.getRight() == input.length() - 1) {
                    Arc arc = new Arc(pToken, input, Arc.Type.TOKEN);
                    out.get(pToken.getLeft()).add(arc);
                    furthestReachingPosition = updateFurthestReachingArcs(arc, furthestReachingArcSet,
                            furthestReachingPosition);
                } else {
                    pSep = AutomatonUtils.runAutomatonLongestMatch(separatorFSA, input, pToken.getRight() + 1);
                    if (pSep != null) {
                        out.get(pToken.getLeft()).add(new Arc(pToken, input, Arc.Type.TOKEN));
                        Arc arc = Arc.sepFromPatterns(pSep, input, invisibleCharacterPattern, eosSeparatorPattern);
                        out.get(pSep.getLeft()).add(arc);
                        furthestReachingPosition = updateFurthestReachingArcs(arc, furthestReachingArcSet,
                                furthestReachingPosition);

                        if (pSep.getRight() + 1 < input.length())
                            stack.add(pSep.getRight() + 1);
                    }
                }
            }
        }

        return new ArcParsing(out, furthestReachingArcSet, furthestReachingPosition);
    }

    static private int updateFurthestReachingArcs(Arc arc, Set<Arc> furthestReachingArcSet, int furthestReachingPosition) {
        if (arc.getEnd() > furthestReachingPosition) {
            furthestReachingArcSet.clear();
            furthestReachingArcSet.add(arc);
            furthestReachingPosition = arc.getEnd();
        } else if (arc.getEnd() == furthestReachingPosition) {
            furthestReachingArcSet.add(arc);
        }

        return furthestReachingPosition;
    }

    public int getFurthestReachingPosition() {
        return furthestReachingPosition;
    }

    public Set<Arc> getFurthestReachingArcSet() {
        return furthestReachingArcSet;
    }

    public Set<Arc> getArcsStartingAt(int pos) {
        return arcStartPositions.get(pos);
    }

    public int length() {
        return arcStartPositions.size();
    }

    /**
     * Any arc that points to a position where no other arcs exist cannot make a valid tokenization later, so we filter
     * them out.
     */
    public void filterImpossibleTokenization() {
        for (int i = furthestReachingPosition; i >= 0; i--) {
            arcStartPositions.get(i).removeIf(
                    a -> a.getEnd() < furthestReachingPosition && arcStartPositions.get(a.getEnd() + 1).size() == 0
            );
        }
    }

    @Override
    public Iterator<Set<Arc>> iterator() {
        return arcStartPositions.iterator();
    }
}
