package nlpstack;

import com.google.common.collect.Multiset;
import implementations.DefaultConfiguration;
import nlpstack.analyzers.*;
import nlpstack.annotations.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

import nlpstack.annotations.Parser.AnnotationParser;
import nlpstack.communication.WordEmbeddings;
import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Options cliInterface = setupCliInterface();
        Configuration configuration = new DefaultConfiguration();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(cliInterface, args);
        } catch (ParseException exp) {
            System.out.println("Error parsing command line: " + exp.getMessage());
            System.exit(-1);
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

            if (commandLine.hasOption(CliArguments.LEXICAL_ANALYZER)) {
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    System.out.print(">>> ");
                    String line = input.readLine();
                    if (line == null)
                        break;
                    if (line.equals(""))
                        continue;

                    try {
                        long startTime = System.nanoTime();

                        List<LexicalChart> sentenceList = lexicalAnalyzer(
                                AnnotationParser.parse(line).toArrayList().stream(),
                                configuration.getLexicalAnalyzer()
                        );

                        long endTime = System.nanoTime();

                        for (LexicalChart sentence : sentenceList) {
                            System.out.println(sentence.toString());
                            System.out.println();
                        }

                        System.out.println(String.format("Total execution time: %fms\n\n", ((double) (endTime - startTime)) / 1000000.0));
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            } else if (commandLine.hasOption(CliArguments.SYNTACTIC_ANALYZER)) {
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    System.out.print(">>> ");
                    String line = input.readLine();
                    if (line == null)
                        break;
                    if (line.equals(""))
                        continue;


                    try {
                        // good test sentence:
                        // My name is Bob, I live in a big city, my parents are nice, I love big cities.
                        long startTime = System.nanoTime();

                        List<SyntacticChart> charts = syntacticAnalyzer(
                                AnnotationParser.parse(line).toArrayList().stream(),
                                configuration.getLexicalAnalyzer(),
                                configuration.getSyntacticAnalyzer()
                        );

                        for (SyntacticChart chart : charts) {
                            System.out.println(chart.toString());
                            int numberOfParsedChart = 0;
                            Multiset<String> multiSet = chart.getChart().getRule(chart.getChart().getSize(), 1);
                            if (multiSet.contains("S"))
                                numberOfParsedChart = multiSet.count("S");
                            System.out.println(String.format("\n%d full charts.\n", numberOfParsedChart));
                        }

                        long endTime = System.nanoTime();

                        System.out.println(String.format("Total execution time: %fms\n\n", ((double) (endTime - startTime)) / 1000000.0));
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }
        }
    }

    static private Options setupCliInterface() {
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

    static private List<LexicalChart> lexicalAnalyzer(Stream<StringWithAnnotations> input, LexicalAnalyzer analyzer) {
        Stream<LexicalChart> sentenceStream = analyzer.tokenize(input);
        return sentenceStream.collect(toList());
    }

    static private List<SyntacticChart> syntacticAnalyzer(Stream<StringWithAnnotations> input, LexicalAnalyzer lexicalAnalyzer, SyntacticAnalyzer syntacticAnalyzer) {
        Stream<LexicalChart> sentenceStream = lexicalAnalyzer.tokenize(input);
        Stream<SyntacticChart> chartStream = syntacticAnalyzer.parse(sentenceStream);
        return chartStream.collect(toList());
    }

    static private void semanticAnalyzer(Stream<StringWithAnnotations> input, LexicalAnalyzer lexicalAnalyzer,
                                 SyntacticAnalyzer syntacticAnalyzer, SemanticAnalyzer semanticAnalyzer) {
        Stream<LexicalChart> sentenceStream = lexicalAnalyzer.tokenize(input);
        Stream<SyntacticChart> chartStream = syntacticAnalyzer.parse(sentenceStream);
        WordEmbeddings occurrences = semanticAnalyzer.findEmbeddings(chartStream);
        System.out.println(occurrences.toString());
    }
}
