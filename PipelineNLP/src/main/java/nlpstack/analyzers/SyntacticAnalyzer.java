package nlpstack.analyzers;

import nlpstack.annotations.AnnotatedChart;
import java.util.stream.Stream;

public interface SyntacticAnalyzer {
    Stream<AnnotatedChart> parse(Stream<AnnotatedChart> input);
}
