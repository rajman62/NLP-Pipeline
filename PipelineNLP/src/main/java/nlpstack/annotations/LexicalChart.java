package nlpstack.annotations;

import nlpstack.communication.Chart;

public class LexicalChart {
    private SyntacticChart synChart;
    private Chart chart;

    private LexicalChart(Chart chart, SyntacticChart synChart) {
        this.chart = chart;
        this.synChart = synChart;
    }

    public static LexicalChart fromChart(Chart chart) {
        return new LexicalChart(chart, null);
    }

    public static LexicalChart fromAnnotation(SyntacticChart synChart) {
        return new LexicalChart(null, synChart);
    }

    public SyntacticChart getAnnotatedSyntacticChart() {
        return synChart;
    }

    public Chart getChart() {
        return chart;
    }

    public boolean isAnnotated() {
        return chart == null;
    }
}
