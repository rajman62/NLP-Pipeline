package implementations.syntacticutils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import nlpstack.communication.Chart;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CYK {
    private Grammar grammar;
    private Chart<String, String> chart;

    public CYK(Grammar grammar, Chart<String, String> chart) {
        this.grammar = grammar;
        this.chart = chart;
    }

    static public Grammar adaptGrammarForCYK(Grammar grammar) {
        Grammar out = new Grammar();
        for (Rule rule : grammar) {
            NonTerminal left = rule.getLeft();
            Sigma[] right = rule.getRight();

            if (right.length == 1)
                out.addRule(rule);

            else if (right.length >= 2) {
                int i;
                for (i = 0; i < right.length - 2; i++) {
                    AbstractNonTerminal newLeft = new AbstractNonTerminal();
                    Sigma[] newRight = new Sigma[2];
                    newRight[0] = right[i];
                    newRight[1] = newLeft;
                    out.addRule(new Rule(left, newRight));
                    left = newLeft;
                }

                out.addRule(new Rule(left, Arrays.copyOfRange(right, right.length - 2, right.length)));
            }
        }
        return out;
    }

    public void runCYK() {
        // copy the tokens from the chart to workChart
        List<String> tokens = chart.getTokens();
        ArrayList<Terminal> newTokens = new ArrayList<>(tokens.size());
        for (String tok : tokens)
            newTokens.add(Terminal.of(tok));

        // copy tags from the chart to workChart
        Chart<NonTerminal, Terminal> workChart = Chart.getEmptyChart(newTokens);
        for (Triple<Integer, Integer, Multiset<String>> tags : chart) {
            for (String t : tags.getRight())
                workChart.addRule(tags.getLeft(), tags.getMiddle(), RealNonTerminal.of(t), tags.getRight().count(t));
        }

        // executing CYK on the first line
        for (int pos = 1 ; pos <= workChart.getSize() ; pos++) {
            for (Rule rule : grammar.getSingleRightSideRules()) {
                if (workChart.getToken(pos).equals(rule.getRight()[0]))
                    addNewRule(workChart, 1, pos, rule.getLeft(), 1);
            }
            fixPoint(workChart, 1, pos);
        }

        // executing CYK on the other lines
        for (int len = 2; len <= workChart.getSize(); len++)
            for (int pos = 1; pos < workChart.getSize(); pos++) {
                for (int i = 1; i < len; i++) {
                    Multiset<NonTerminal> m1;
                    Terminal t1 = null;
                    Multiset<NonTerminal> m2;
                    Terminal t2 = null;
                    m1 = workChart.getRule(i, pos);
                    if (i == 1)
                        t1 = workChart.getToken(pos);
                    m2 = workChart.getRule(len - i, pos + i);
                    if (i == len - 1)
                        t2 = workChart.getToken(pos + i);

                    for (Rule rule : grammar.getMultiRightSideRules()) {
                        if (t1 != null && t1.equals(rule.getRight()[0]) && t2 != null && t2.equals(rule.getRight()[1]))
                            addNewRule(workChart, len, pos, rule.getLeft(), 1);

                        else if (t1 != null && t1.equals(rule.getRight()[0]) && m2.contains(rule.getRight()[1]))
                            addNewRule(workChart, len, pos, rule.getLeft(), m2.count(rule.getRight()[1]));

                        else if (m1.contains(rule.getRight()[0]) && t2 != null && t2.equals(rule.getRight()[1]))
                            addNewRule(workChart, len, pos, rule.getLeft(), m1.count(rule.getRight()[1]));

                        else if (m1.contains(rule.getRight()[0]) && m2.contains(rule.getRight()[1]))
                            addNewRule(workChart, len, pos, rule.getLeft(), Math.min(m1.count(rule.getRight()[0]), m2.count(rule.getRight()[1])));
                    }
                }

                fixPoint(workChart, len, pos);
            }
    }

    private void addNewRule(Chart<NonTerminal, Terminal> workChart, int length, int pos, NonTerminal t, int count) {
        if (t instanceof RealNonTerminal)
            chart.addRule(length, pos, t.getString(), count);
        workChart.addRule(length, pos, t, count);
    }

    /**
     * Fix Point develops single hand sided rules on the cell (len, pos) until no new rule are added.
     * @param workChart the chart to apply fix point to
     * @param len the cell
     * @param pos the cell
     */
    private void fixPoint(Chart<NonTerminal, Terminal> workChart, int len, int pos) {
        Multiset<NonTerminal> previousMultiSet = HashMultiset.create();
        Multiset<NonTerminal> elToProcess = HashMultiset.create();
        Multiset<NonTerminal> elAdded = HashMultiset.create();
        copyMultiSet(workChart.getRule(len, pos), elToProcess);
        do {
            copyMultiSet(workChart.getRule(len, pos), previousMultiSet);
            for (Rule rule : grammar.getSingleRightSideRules()) {
                if (elToProcess.contains(rule.getRight()[0]) && rule.getRight()[0].isNonTerminal()) {
                    addNewRule(workChart, len, pos, rule.getLeft(), elToProcess.count(rule.getRight()[0]));
                    elAdded.add(rule.getLeft(), elToProcess.count(rule.getRight()[0]));
                }
            }
            Multiset<NonTerminal> tmp = elToProcess;
            elToProcess = elAdded;
            elAdded = tmp;
            elAdded.clear();
        } while (!previousMultiSet.equals(workChart.getRule(len, pos)));
    }

    private static <T> void copyMultiSet(Multiset<T> in, Multiset<T> out) {
        out.clear();
        for (T t : in)
            out.add(t, in.count(t));
    }
}
