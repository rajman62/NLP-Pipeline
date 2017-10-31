package nlpstack.analyzers;

import nlpstack.annotations.AnnotatedChart;
import nlpstack.annotations.AnnotatedString;
import nlpstack.streams.Stream;

public interface LexicalAnalyzer {
    Stream<AnnotatedChart> tokenize(Stream<AnnotatedString> input);
}
