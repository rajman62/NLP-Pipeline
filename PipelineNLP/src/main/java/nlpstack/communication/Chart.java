package nlpstack.communication;


import com.google.common.collect.Multiset;


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

    public void addRule(int length, int pos, String tag) {
        chart.get(length - 1).get(pos - 1).add(tag);
    }

    public Multiset<String> getRule(int length, int pos) {
        return chart.get(length - 1).get(pos - 1);
    }

    public String getToken(int pos) {
        return tokens.get(pos - 1);
    }
}
