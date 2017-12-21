package nlpstack.analyzers;

import nlpstack.annotations.SyntacticChart;
import nlpstack.communication.Chart;
import nlpstack.communication.Occurences;

import java.util.function.Function;
import java.util.stream.Stream;


public interface SemanticAnalyzer extends Function<Chart, Chart> {
    Occurences findOccurrences(Stream<SyntacticChart> chartStream);
}
