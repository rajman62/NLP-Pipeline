package nlpstack.annotations;

import java.util.List;

public class StringWithAnnotations {
    private List<StringSegment> string;
    private LexicalChart lexChart;
    private SyntacticChart synChart;

    private StringWithAnnotations(List<StringSegment> string, LexicalChart lexChart, SyntacticChart synChart) {
        this.string = string;
        this.lexChart = lexChart;
        this.synChart = synChart;
    }

    public static StringWithAnnotations fromStringSegment(List<StringSegment> string) {
        return new StringWithAnnotations(string, null, null);
    }

    public static StringWithAnnotations fromLexicalAnnotation(LexicalChart chart) {
        return new StringWithAnnotations(null, chart, null);
    }

    public static StringWithAnnotations fromSyntacticAnnotation(SyntacticChart chart) {
        return new StringWithAnnotations(null, null, chart);
    }

    public LexicalChart getAnnotatedLexicalChart() {
         if (synChart != null)
             return LexicalChart.fromAnnotation(synChart);
         else
             return lexChart;
    }

    public List<StringSegment> getStrings() {
        return string;
    }

    public boolean isAnnotated() {
        return string == null;
    }
}
