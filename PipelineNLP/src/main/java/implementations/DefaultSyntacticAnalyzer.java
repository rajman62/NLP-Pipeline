package implementations;

import implementations.conffile.SyntacticConf;
import nlpstack.analyzers.SyntacticAnalyzer;
import nlpstack.annotations.AnnotatedChart;

import java.util.stream.Stream;

public class DefaultSyntacticAnalyzer implements SyntacticAnalyzer {
    public DefaultSyntacticAnalyzer(SyntacticConf conf) {

    }

    @Override
    public Stream<AnnotatedChart> parse(Stream<AnnotatedChart> input) {
        return null;
    }
}
