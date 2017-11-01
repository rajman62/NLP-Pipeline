package implementations;

import implementations.conffile.LexicalConf;
import nlpstack.analyzers.LexicalAnalyzer;
import nlpstack.annotations.AnnotatedChart;
import nlpstack.annotations.AnnotatedString;

import java.util.stream.Stream;

public class DefaultLexicalAnalyzer implements LexicalAnalyzer {

    public DefaultLexicalAnalyzer(LexicalConf conf) {

    }

    @Override
    public Stream<AnnotatedChart> tokenize(Stream<AnnotatedString> input) {
        return null;
    }
}
