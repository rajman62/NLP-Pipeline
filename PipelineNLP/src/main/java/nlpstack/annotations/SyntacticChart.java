package nlpstack.annotations;

import nlpstack.communication.Chart;

public class SyntacticChart {
    private Chart chart;

    public SyntacticChart(Chart chart){
        this.chart = chart;
    }


    public Chart getChart() {
        return chart;
    }
}
