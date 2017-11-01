package nlpstack;

import implementations.DefaultConfiguration;
import nlpstack.analyzers.*;
import nlpstack.annotations.*;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        Options cliInterface = setupCliInterface();
        Configuration configuration = new DefaultConfiguration();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(cliInterface, args);
        } catch (ParseException exp) {
            System.out.println("Error parsing command line: " + exp.getMessage());
        }

        if (commandLine.hasOption(CliArguments.HELP_LONG)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("nlpstack", cliInterface);
        } else {
            if (commandLine.hasOption(CliArguments.CONFIGURATION_LONG)) {
                try {
                    configuration.parse(commandLine.getOptionValue(CliArguments.CONFIGURATION_LONG));
                } catch (Exception e) {
                    System.out.println("Error reading configuration: " + e.getMessage());
                }
            }
        }


    }

    static Options setupCliInterface() {
        Options cliInterface = new Options();

        OptionGroup mainCommand = new OptionGroup();
        mainCommand.addOption(new Option(CliArguments.LEXICAL_ANALYZER,
                "reads a file or stdin and outputs the tokenized sentences in charts"));
        mainCommand.addOption(new Option(CliArguments.SYNTACTIC_ANALYZER,
                "reads a file or stdin and outputs the syntactic trees of the sentences"));
        mainCommand.addOption(new Option(CliArguments.SEMANTIC_ANALYZER,
                "reads a file or stdin and outputs the occurrences"));

        cliInterface.addOptionGroup(mainCommand);
        cliInterface.addOption(CliArguments.HELP_SHORT, CliArguments.HELP_LONG, false,
                "print this message");
        cliInterface.addOption(CliArguments.CONFIGURATION_SHORT, CliArguments.CONFIGURATION_LONG, true,
                "file to pass to the configuration module");

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
