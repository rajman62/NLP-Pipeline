package nlpstack;

import nlpstack.analyzers.LexicalAnalyzer;
import nlpstack.analyzers.SemanticAnalyzer;
import nlpstack.analyzers.SyntacticAnalyzer;
import nlpstack.annotations.AnnotatedChart;
import nlpstack.annotations.AnnotatedOccurrences;
import nlpstack.annotations.AnnotatedString;
import nlpstack.streams.Stream;

public class Main {
    public static void main(String[] args) {

    }

    public static void lexicalAnalyzer(Stream<AnnotatedString> input, LexicalAnalyzer analyzer) {
        Stream<AnnotatedChart> sentenceStream = analyzer.tokenize(input);
        for(AnnotatedChart sentence : sentenceStream) {
            System.out.println(sentence.toString());
        }
    }

    public static void syntacticAnalyzer(Stream<AnnotatedString> input, LexicalAnalyzer lexicalAnalyzer, SyntacticAnalyzer syntacticAnalyzer) {
        Stream<AnnotatedChart> sentenceStream = lexicalAnalyzer.tokenize(input);
        Stream<AnnotatedChart> chartStream = syntacticAnalyzer.parse(sentenceStream);
        for(AnnotatedChart chart : chartStream) {
            System.out.println(chart.toString());
        }
    }

    public static void semanticAnalyzer(Stream<AnnotatedString> input, LexicalAnalyzer lexicalAnalyzer,
                                        SyntacticAnalyzer syntacticAnalyzer, SemanticAnalyzer semanticAnalyzer) {
        Stream<AnnotatedChart> sentenceStream = lexicalAnalyzer.tokenize(input);
        Stream<AnnotatedChart> chartStream = syntacticAnalyzer.parse(sentenceStream);
        AnnotatedOccurrences occurrences = semanticAnalyzer.findOccurrences(chartStream);
        System.out.println(occurrences.toString());
    }
}
