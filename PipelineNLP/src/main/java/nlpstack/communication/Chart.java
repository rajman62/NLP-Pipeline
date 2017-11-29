package nlpstack.communication;


import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;


import java.util.ArrayList;
import java.util.List;

public class Chart {
    private List<String> tokens;
    private List<List<Multiset<String>>> chart;
    private int size;

    public Chart(List<String> tokens, List<List<Multiset<String>>> chart) {
        this.tokens = tokens;
        this.chart = chart;
        size = tokens.size();
    }

    public static Chart getEmptyChart(List<String> tokens) {
        int n = tokens.size();
        List<List<Multiset<String>>> chart = new ArrayList<>(n);
        for (int i = 0 ; i < n ; i++) {
            chart.add(i, new ArrayList<>(n-i));
            for(int j = 0 ; j < n-i ; j++)
                chart.get(i).add(j, HashMultiset.create());
        }

        return new Chart(tokens, chart);
    }

    public void addRule(int length, int pos, String tag) {
        chart.get(length - 1).get(pos - 1).add(tag);
    }

    public Multiset<String> getRule(int length, int pos) {
        return chart.get(length - 1).get(pos - 1);
    }

    public String getToken(int pos) {
        return tokens.get(pos - 1);
    }

    public List<String> getTokens() {
        return new ArrayList<>(tokens);
    }
}
