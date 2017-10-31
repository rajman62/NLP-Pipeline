package nlpstack.analyzers;

import nlpstack.annotations.AnnotatedChart;
import nlpstack.streams.Stream;

public interface SyntacticAnalyzer {
    Stream<AnnotatedChart> parse(Stream<AnnotatedChart> input);
}
