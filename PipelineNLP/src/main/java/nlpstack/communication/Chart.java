package nlpstack.communication;


import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import static java.util.stream.Collectors.toList;


import java.util.*;

public class Chart implements Iterable<Triple<Integer, Integer, Multiset<String>>> {
    private List<String> tokens;
    private List<List<Multiset<String>>> chart;
    private HashMap<Pair<Integer, Integer>, Multiset<String>> iterationIndex;
    private int size;
    private boolean isCompleteSentence = true;

    private Chart(List<String> tokens, List<List<Multiset<String>>> chart) {
        this.tokens = tokens;
        this.chart = chart;
        size = tokens.size();
        iterationIndex = new HashMap<>(2 * ((int) Math.ceil(size / 0.75)));
    }

    public static Chart getEmptyChart(List<String> tokens) {
        int n = tokens.size();
        List<List<Multiset<String>>> chart = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            chart.add(i, new ArrayList<>(n - i));
            for (int j = 0; j < n - i; j++)
                chart.get(i).add(j, HashMultiset.create());
        }

        return new Chart(tokens, chart);
    }

    public void addRule(int length, int pos, String tag) {
        Multiset<String> multiset = chart.get(length - 1).get(pos - 1);
        multiset.add(tag);
        iterationIndex.put(Pair.of(length, pos), multiset);
    }

    public Multiset<String> getRule(int length, int pos) {
        return chart.get(length - 1).get(pos - 1);
    }

    public int getSize() {
        return size;
    }

    public String getToken(int pos) {
        return tokens.get(pos - 1);
    }

    /**
     * Returns the base tokens of the chart. The returned list is a copy, it will not create side effects if modified.
     *
     * @return The list of tokens
     */
    public List<String> getTokens() {
        return new ArrayList<>(tokens);
    }

    @Override
    public Iterator<Triple<Integer, Integer, Multiset<String>>> iterator() {
        return iterationIndex.keySet().stream()
                .map(x -> Triple.of(x.getLeft(), x.getRight(), iterationIndex.get(x)))
                .collect(toList()).iterator();
    }

    public boolean isCompleteSentence() {
        return isCompleteSentence;
    }

    public void setCompleteSentence(boolean completeSentence) {
        isCompleteSentence = completeSentence;
    }

    @Override
    public String toString() {
        String out = "";
        Optional<Integer> potentialMaxLength = iterationIndex.values().stream()
                .map(x -> sum(x).length()).max(Comparator.comparingInt(x -> x));
        if (!potentialMaxLength.isPresent())
            return "Empty";

        int maxLength = Math.max(
                getTokens().stream().map(String::length).max(Comparator.comparingInt(x -> x)).get(),
                potentialMaxLength.get());

        for (int i = getSize() - 1; i >= 0; i--) {
            out = out.concat(
                    String.join(" | ",
                            chart.get(i).stream()
                                    .map(x -> String.format("%1$-" + maxLength + "s", sum(x)))
                                    .collect(toList())
                    ));
            out = out.concat("\n");
        }
        out = out.concat(
                String.join(" | ",
                        getTokens().stream()
                                .map(x -> String.format("%1$-" + maxLength + "s", x))
                                .collect(toList())
                ));

        return out;
    }

    private String sum(Multiset<String> tagSet) {
        return String.join(" ", tagSet.entrySet().stream().map(Multiset.Entry::getElement).collect(toList()));
    }
}
