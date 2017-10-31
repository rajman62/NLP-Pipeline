package nlpstack;

import nlpstack.analyzers.*;
import nlpstack.annotations.*;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        Options cliInterface = setupCliInterface();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(cliInterface, args);

            if (commandLine.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("nlpstack", cliInterface);
            }

        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }

    static Options setupCliInterface() {
        Options cliInterface = new Options();

        OptionGroup mainCommande = new OptionGroup();
        mainCommande.addOption(new Option("lexical", "reads a file or stdin and outputs the tokenized sentences in charts"));
        mainCommande.addOption(new Option("syntactic", "reads a file or stdin and outputs the syntactic trees of the sentences"));
        mainCommande.addOption(new Option("semantic", "reads a file or stdin and outputs the occurrences"));

        cliInterface.addOptionGroup(mainCommande);
        cliInterface.addOption("help", "prints the arguments");

        return cliInterface;
    }

    static void lexicalAnalyzer(Stream<AnnotatedString> input, LexicalAnalyzer analyzer) {
        Stream<AnnotatedChart> sentenceStream = analyzer.tokenize(input);
        for (AnnotatedChart sentence : sentenceStream.collect(toList())) {
            System.out.println(sentence.toString());
        }
    }

    static void syntacticAnalyzer(Stream<AnnotatedString> input, LexicalAnalyzer lexicalAnalyzer, SyntacticAnalyzer syntacticAnalyzer) {
        Stream<AnnotatedChart> sentenceStream = lexicalAnalyzer.tokenize(input);
        Stream<AnnotatedChart> chartStream = syntacticAnalyzer.parse(sentenceStream);
        for (AnnotatedChart chart : chartStream.collect(toList())) {
            System.out.println(chart.toString());
        }
    }

    static void semanticAnalyzer(Stream<AnnotatedString> input, LexicalAnalyzer lexicalAnalyzer,
                                 SyntacticAnalyzer syntacticAnalyzer, SemanticAnalyzer semanticAnalyzer) {
        Stream<AnnotatedChart> sentenceStream = lexicalAnalyzer.tokenize(input);
        Stream<AnnotatedChart> chartStream = syntacticAnalyzer.parse(sentenceStream);
        AnnotatedOccurrences occurrences = semanticAnalyzer.findOccurrences(chartStream);
        System.out.println(occurrences.toString());
    }
}
