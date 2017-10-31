package nlpstack.analyzers;

import nlpstack.annotations.AnnotatedChart;
import nlpstack.annotations.AnnotatedOccurrences;
import java.util.stream.Stream;

public interface SemanticAnalyzer {
    AnnotatedOccurrences findOccurrences(Stream<AnnotatedChart> input);
}
