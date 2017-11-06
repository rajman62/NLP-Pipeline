package implementations;

import implementations.filereaders.ConlluReader;
import org.apache.commons.cli.*;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class ConlluUtils {
    private static String HELP_LONG = "help";
    private static String HELP_SHORT = "h";
    private static String STATS = "stats";
    private static String PROG_NAME = "ConlluUtils";

    public static void main(String[] args) {
        Options cliInterface = setupCliInterface();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(cliInterface, args);
        } catch (ParseException exp) {
            System.out.println("Error parsing command line: " + exp.getMessage());
            System.exit(-1);
        }

        if (commandLine.hasOption(HELP_LONG)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(PROG_NAME, cliInterface);
        } else {
            if (commandLine.hasOption(STATS)) {
                printStats(commandLine.getOptionValue(STATS));
            }
        }
    }

    private static Options setupCliInterface() {
        Options cliInterface = new Options();
        OptionGroup mainCommand = new OptionGroup();
        mainCommand.addOption(Option.builder(STATS)
                .desc("Output tags used and stats on: number of sentences, words and lemmas")
                .hasArg().argName("conllu_file").build());

        cliInterface.addOptionGroup(mainCommand);

        cliInterface.addOption(HELP_SHORT, HELP_LONG, false,
                "print this message");

        return cliInterface;
    }

    private static void printStats(String conlluPath) {
        ConlluReader reader = new ConlluReader(conlluPath);
        try {
            reader.calculateStats();
        } catch(IllegalArgumentException exp) {
            System.out.println(exp.getMessage());
            System.exit(-1);
        }

        List<String> uposttag = reader.getUPostTags().stream()
                .map(x -> "'".concat(x).concat("'")).collect(toList());
        List<String> xposttag = reader.getXPostTags().stream()
                .map(x -> "'".concat(x).concat("'")).collect(toList());

        uposttag.sort(String::compareTo);
        xposttag.sort(String::compareTo);

        System.out.println(String.format("Stats on %s:", conlluPath));
        System.out.println(String.format("Sentences: %d, Forms: %d, Lemmas: %d, UPostTags: %d, XPostTags: %d",
                reader.getNbSentences(), reader.getNbForms(), reader.getNbLemmas(),
                uposttag.size(), xposttag.size()));

        System.out.println(String.format("uposttags: %s",
                String.join(", ", uposttag)));
        System.out.println(String.format("xposttags: %s",
                String.join(", ", xposttag)));
    }
}
