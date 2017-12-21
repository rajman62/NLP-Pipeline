package nlpstack.communication;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import static java.util.stream.Collectors.toList;

import java.util.*;

public class Chart<R, T> implements Iterable<Triple<Integer, Integer, Multiset<R>>> {
    // records the tokens at the base of the chart
    private List<T> tokens;

    // records for each cell of the chart, what NonTerminal there are, and how many time they appear
    private List<List<Multiset<R>>> chart;

    // records all the present elements
    private HashMap<Pair<Integer, Integer>, Multiset<R>> iterationIndex;

    private int size;
    private boolean isCompleteSentence = true;

    private Chart(List<T> tokens, List<List<Multiset<R>>> chart) {
        this.tokens = tokens;
        this.chart = chart;
        size = tokens.size();
        iterationIndex = new HashMap<>(2 * ((int) Math.ceil(size / 0.75)));
    }

    public static <R, T> Chart<R, T> getEmptyChart(List<T> tokens) {
        int n = tokens.size();
        List<List<Multiset<R>>> chart = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            chart.add(new ArrayList<>(n - i));
            for (int j = 0; j < n - i; j++)
                chart.get(i).add(HashMultiset.create());
        }

        return new Chart<>(tokens, chart);
    }

    @SafeVarargs
    public static <R, T> Chart<R, T> getEmptyChart(T... tokens) {
        return getEmptyChart(new ArrayList<>(Arrays.asList(tokens)));
    }

    public void addRule(int length, int pos, R tag) {
        addRule(length, pos, tag, 1);
    }

    public void addRule(int length, int pos, R tag, int count) {
        Multiset<R> multiset = chart.get(length - 1).get(pos - 1);
        multiset.add(tag, count);
        iterationIndex.put(Pair.of(length, pos), multiset);
    }

    public Multiset<R> getRule(int length, int pos) {
        return chart.get(length - 1).get(pos - 1);
    }

    public int getSize() {
        return size;
    }

    public T getToken(int pos) {
        return tokens.get(pos - 1);
    }

    /**
     * Returns the base tokens of the chart. The returned list is not a copy, it will create side effects if modified.
     *
     * @return The list of tokens
     */
    public List<T> getTokens() {
        return tokens;
    }

    @Override
    public Iterator<Triple<Integer, Integer, Multiset<R>>> iterator() {
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
                .map(x -> sumRuleStrings(x).length()).max(Comparator.comparingInt(x -> x));
        if (!potentialMaxLength.isPresent())
            return "Empty";

        int maxLength = Math.max(
                getTokens().stream().map(T::toString).map(String::length).max(Comparator.comparingInt(x -> x)).get(),
                potentialMaxLength.get());

        for (int i = getSize() - 1; i >= 0; i--) {
            out = out.concat(
                    String.join(" | ",
                            chart.get(i).stream()
                                    .map(x -> String.format("%1$-" + maxLength + "s", sumRuleStrings(x)))
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

    private String sumRuleStrings(Multiset<R> tagSet) {
        return String.join(" ", tagSet.entrySet().stream().map(Multiset.Entry::getElement)
                .map(R::toString).collect(toList()));
    }
}
