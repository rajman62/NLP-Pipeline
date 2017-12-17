package implementations.syntacticutils;

import com.google.common.collect.Multiset;
import nlpstack.communication.Chart;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class EarlyParser {
    /*
    TODO: Implement an Early Parser

    This is a first implementation that I stopped to implement CYK.

    Early parser is the same complexity, or lower than CYK. It would be better to have an Early Parser.
    The implementation is a little trickier than CYK though: How to deal with left recursive rules?
    CYK is also simpler to understand, it's a good tool for debugging purposes. It might be an idea to
    let the user choose between CYK and Early Parser in the conf file.


    private ArrayList<LinkedList<EarlyItem>> toBePredicted;
    private ArrayList<LinkedList<EarlyItem>> toBeScanned;
    private ArrayList<HashMap<String, EarlyItem>> preFilledElements;
    private Chart chart;
    private Grammar grammar;
    private String S;


    public void EarlyParser(Chart chart, Grammar grammar, String S) {
        this.chart = chart;
        this.grammar = grammar;
        this.S = S;

        toBePredicted = new ArrayList<>(chart.getSize());
        toBeScanned = new ArrayList<>(chart.getSize());
        preFilledElements = new ArrayList<>(chart.getSize());

        for (int i = 0; i < toBePredicted.size(); i++)
            toBePredicted.add(new LinkedList<>());
        for (int i = 0; i < toBeScanned.size(); i++)
            toBeScanned.add(new LinkedList<>());
        for (int i = 0; i < preFilledElements.size(); i++)
            preFilledElements.add(new HashMap<>());

        for (Triple<Integer, Integer, Multiset<String>> el : chart) {
            for (String ruleName : el.getRight()) {
                int i = el.getLeft() + el.getMiddle() - 1;
                int k = el.getMiddle() - 1;

                Sigma[] right = (Sigma[]) chart.getTokens()
                        .subList(k, i)
                        .stream().map(Terminal::new).toArray();

                preFilledElements.get(k).put(
                        ruleName,
                        new EarlyItem(new Rule(new NonTerminal(ruleName), right), i, i, k)
                );
            }
        }
    }

    public void fillChart() {
        LinkedList<EarlyItem> POut = new LinkedList<>();
        LinkedList<EarlyItem> SOut = new LinkedList<>();
        expandRule(new NonTerminal(S), 0, null, POut, SOut);
        toBePredicted.set(0, POut);
        toBeScanned.set(0, SOut);

        for (int i = 0; i <= chart.getSize(); i++) {
            while (!toBePredicted.get(i).isEmpty() || !toBeScanned.get(i).isEmpty()) {
                tryStepPredict(i);
                tryStepScan(i);
            }
        }
    }

    private void tryStepPredict(int pos) {
        LinkedList<EarlyItem> POut = new LinkedList<>();
        LinkedList<EarlyItem> SOut = new LinkedList<>();
        for (EarlyItem earlyItem : toBePredicted.get(pos)) {
            expandRule(earlyItem.rule.getLeft(), pos, earlyItem, POut, SOut);
            //if (preFilledElements.get(pos).contains())
        }
        toBePredicted.set(pos, POut);
        toBeScanned.get(pos).addAll(SOut);
    }

    private void tryStepScan(int pos) {
        LinkedList<EarlyItem> POut = new LinkedList<>();
        LinkedList<EarlyItem> SOut = new LinkedList<>();
        for (EarlyItem earlyItem : toBeScanned.get(pos)) {
            if (earlyItem.getDotElement().getString().equals(chart.getToken(pos + 1))) {
                earlyItem.dot += 1;
                earlyItem.i += 1;
                if (!tryCompletion(earlyItem)) {
                    if (earlyItem.nextIsNonTerminal())
                        POut.add(earlyItem);
                    else
                        SOut.add(earlyItem);
                }
            }
        }
        toBePredicted.get(pos).addAll(POut);
        toBeScanned.set(pos, SOut);
    }

    private boolean tryCompletion(EarlyItem earlyItem) {
        if (earlyItem.isComplete()) {
            chart.addRule(
                    earlyItem.i - earlyItem.k, earlyItem.k + 1,
                    earlyItem.rule.getLeft().getString()
            );
            if (earlyItem.parent != null) {
                EarlyItem parent = earlyItem.parent;
                EarlyItem newItem = new EarlyItem(parent.rule, parent.dot + 1, parent.i + 1, parent.k);
                newItem.parent = parent.parent;
                tryCompletion(newItem);
            }
            return true;
        }
        return false;
    }

    private void expandRule(NonTerminal from, int atPos, EarlyItem withParent,
                            LinkedList<EarlyItem> toBePredictedOut, LinkedList<EarlyItem> toBeScannedOut) {
        for (Rule rule : grammar.getRulesWithLeft(from)) {
            EarlyItem earlyItem = new EarlyItem(rule, 0, atPos, atPos);
            earlyItem.parent = withParent;
            if (earlyItem.nextIsNonTerminal())
                toBePredictedOut.add(earlyItem);
            else
                toBeScannedOut.add(earlyItem);
        }
    }
    */
}
