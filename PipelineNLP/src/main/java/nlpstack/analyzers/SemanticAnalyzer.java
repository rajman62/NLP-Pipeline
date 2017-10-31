package nlpstack.analyzers;

import nlpstack.annotations.AnnotatedChart;
import nlpstack.annotations.AnnotatedOccurrences;
import nlpstack.streams.Stream;

public interface SemanticAnalyzer {
    AnnotatedOccurrences findOccurrences(Stream<AnnotatedChart> input);
}
