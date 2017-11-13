package nlpstack.analyzers;

import nlpstack.annotations.LexicalChart;
import nlpstack.annotations.StringWithAnnotations;
import nlpstack.annotations.StringSegment;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class LexicalAnalyzer implements Function<List<StringSegment>, Stream<LexicalChart>> {

    public Stream<LexicalChart> tokenize(Stream<StringWithAnnotations> input) {
        return input.flatMap(x -> {
           if (x.isAnnotated())
               return Stream.<LexicalChart>builder().add(x.getAnnotatedLexicalChart()).build();
           else
               return this.apply(x.getStrings());
        });
    }
}
