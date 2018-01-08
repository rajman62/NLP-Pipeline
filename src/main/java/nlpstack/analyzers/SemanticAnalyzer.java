package nlpstack.analyzers;

import nlpstack.annotations.SyntacticChart;
import nlpstack.communication.Chart;
import nlpstack.communication.WordEmbeddings;

import java.util.function.Function;
import java.util.stream.Stream;


public interface SemanticAnalyzer extends Function<Chart, Chart> {
    WordEmbeddings findEmbeddings(Stream<SyntacticChart> chartStream);
}
