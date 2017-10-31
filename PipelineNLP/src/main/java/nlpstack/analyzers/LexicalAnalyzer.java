package nlpstack.analyzers;

import nlpstack.annotations.AnnotatedChart;
import nlpstack.annotations.AnnotatedString;
import java.util.stream.Stream;

public interface LexicalAnalyzer {
    Stream<AnnotatedChart> tokenize(Stream<AnnotatedString> input);
}
