package nlpstack.analyzers;

import nlpstack.annotations.LexicalChart;
import nlpstack.annotations.SyntacticChart;

import java.util.function.Function;
import java.util.stream.Stream;


public abstract class SyntacticAnalyzer implements Function<LexicalChart, SyntacticChart> {

    public Stream<SyntacticChart> parse(Stream<LexicalChart> sentenceStream) {
        return sentenceStream.map(x -> {
            if (x.isAnnotated())
                return x.getAnnotatedSyntacticChart();
            else
                return this.apply(x);
        });
    }
}
