package implementations;

import com.scalified.tree.TraversalAction;
import com.scalified.tree.TreeNode;
import implementations.filereaders.ConlluReader;
import implementations.filereaders.conlluobjects.Sentence;
import implementations.filereaders.conlluobjects.Word;
import org.apache.commons.cli.*;

import static java.util.stream.Collectors.toList;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class ConlluUtils {
    private static String HELP_LONG = "help";
    private static String HELP_SHORT = "h";
    private static String STATS = "stats";
    private static String EXTRACT_LEXEC = "extractLexec";
    private static String EXTRACT_GRAMMAR = "extractGrammar";
    private static String OUTPUT = "o";
    private static String PROG_NAME = "ConlluUtils";

    public static void main(String[] args) throws IOException {
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
            } else if (commandLine.hasOption(EXTRACT_LEXEC)) {
                if (!commandLine.hasOption(OUTPUT))
                    System.out.println(String.format("-%s has to be specified for -%s", OUTPUT, EXTRACT_LEXEC));
                else
                    extractLexec(commandLine.getOptionValue(EXTRACT_LEXEC), commandLine.getOptionValue(OUTPUT));
            } else if (commandLine.hasOption(EXTRACT_GRAMMAR)) {
                if (!commandLine.hasOption(OUTPUT))
                    System.out.println(String.format("-%s has to be specified for -%s", OUTPUT, EXTRACT_LEXEC));
                else
                    extractGrammar(commandLine.getOptionValue(EXTRACT_GRAMMAR), commandLine.getOptionValue(OUTPUT));
            }
        }
    }

    private static Options setupCliInterface() {
        Options cliInterface = new Options();
        OptionGroup mainCommand = new OptionGroup();
        mainCommand.addOption(Option.builder(STATS)
                .desc("Outputs tags used and stats on: number of sentences, words and lemmas")
                .hasArg().argName("conllu_file").build());

        mainCommand.addOption(Option.builder(EXTRACT_LEXEC)
                .desc("Extracts a lexec from a conllu file")
                .hasArg().argName("conllu_file").build());

        mainCommand.addOption(Option.builder(EXTRACT_GRAMMAR)
                .desc("Extracts a grammar from a conllu file")
                .hasArg().argName("conllu_file").build());

        cliInterface.addOptionGroup(mainCommand);

        cliInterface.addOption(HELP_SHORT, HELP_LONG, false,
                "print this message");

        cliInterface.addOption(Option.builder(OUTPUT)
                .desc("Specifies the output file for the extract commands")
                .hasArg().argName("output_file").build());

        return cliInterface;
    }

    private static void printStats(String conlluPath) {
        ConlluReader reader = new ConlluReader(conlluPath);
        try {
            reader.calculateStats();
        } catch (IllegalArgumentException exp) {
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

    private static void extractLexec(String conlluPath, String outputFileName) throws IOException {
        ConlluReader reader = new ConlluReader(conlluPath);
        HashSet<String> lexicon = new HashSet<>();
        HashSet<String> tags = new HashSet<>();

        Pattern wordPattern = Pattern.compile("^[a-zA-Z]+$");

        for (Sentence sent : reader) {
            for (Word word : sent) {
                if (wordPattern.matcher(word.lemma).matches() && wordPattern.matcher(word.form).matches()) {
                    lexicon.add(String.format("%s+%s:%s #;", word.lemma, word.upostag, word.form));
                    tags.add(String.format("+%s", word.upostag));
                }
            }
        }

        ArrayList<String> sortedLexicon = new ArrayList<>(lexicon);
        sortedLexicon.sort(String::compareTo);
        ArrayList<String> sortedTags = new ArrayList<>(tags);
        sortedTags.sort(String::compareTo);

        FileWriter output = new FileWriter(outputFileName);

        output.write(String.format("Multichar_Symbols %s\n\n", String.join(" ", sortedTags)));
        output.write("LEXICON Root\n\nExtractedLexicon ;\n\nLEXICON ExtractedLexicon\n\n");

        for (String line : sortedLexicon)
            output.write(line.concat("\n"));

        output.close();
    }

    private static void extractGrammar(String conlluPath, String outputFileName) throws IOException {
        ConlluReader reader = new ConlluReader(conlluPath);
        Set<String> ruleSet = new HashSet<>();
        Set<String> startRuleSet = new HashSet<>();

        for (Sentence sent : reader) {
            TreeNode<Word> tree = sent.getTree();
            tree.traversePreOrder(new TraversalAction<TreeNode<Word>>() {
                @Override
                public void perform(TreeNode<Word> wordTreeNode) {
                    List<Word> words = wordTreeNode.subtrees().stream().map(TreeNode::data).collect(toList());
                    words.add(wordTreeNode.data());
                    words.sort(Comparator.comparingInt(o -> o.id));
                    String left = wordTreeNode.data().upostag;
                    String right = String.join(" ", words.stream().map(x -> x.upostag).collect(toList()));

                    // condition to avoid useless rules
                    if (!left.equals(right)) {
                        if (wordTreeNode.isRoot())
                            startRuleSet.add(String.format("S -> %s", right));
                        else
                            ruleSet.add(String.format("%s -> %s", left, right));
                    }
                }

                @Override
                public boolean isCompleted() {
                    return false;
                }
            });
        }

        ArrayList<String> startRuleSetSorted = new ArrayList<>(startRuleSet);
        startRuleSetSorted.sort(String::compareTo);

        ArrayList<String> ruleSetSorted = new ArrayList<>(ruleSet);
        ruleSetSorted.sort(String::compareTo);

        FileWriter output = new FileWriter(outputFileName);

        for (String line : startRuleSetSorted)
            output.write(line.concat("\n"));

        output.write("\n");

        for (String line : ruleSetSorted)
            output.write(line.concat("\n"));

        output.close();
    }
}
