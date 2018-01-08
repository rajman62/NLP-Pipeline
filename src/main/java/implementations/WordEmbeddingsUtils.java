package implementations;

import com.scalified.tree.TraversalAction;
import com.scalified.tree.TreeNode;
import implementations.filereaders.ConlluReader;
import implementations.filereaders.conlluobjects.Sentence;
import implementations.filereaders.conlluobjects.Word;
import implementations.semanticutils.StringContext;
import implementations.semanticutils.TrainData;
import implementations.semanticutils.WordEmbeddingsGradientDescentImpl1;
import implementations.sparkutils.SparkSetup;
import org.apache.commons.cli.*;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class WordEmbeddingsUtils {
    private static String HELP_LONG = "help";
    private static String HELP_SHORT = "h";
    private static String TRAIN_WORD_EMBEDDINGS = "train";
    private static String OUTPUT = "o";
    private static String PROG_NAME = "WordEmbeddingsUtils";

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
            if (commandLine.hasOption(TRAIN_WORD_EMBEDDINGS)) {
                trainWordEmbeddings(commandLine.getOptionValue(TRAIN_WORD_EMBEDDINGS));
            }
        }
    }

    private static Options setupCliInterface() {
        Options cliInterface = new Options();
        OptionGroup mainCommand = new OptionGroup();
        mainCommand.addOption(Option.builder(TRAIN_WORD_EMBEDDINGS)
                .desc("Trains word embeddings on the given conllu file")
                .hasArg().argName("conllu_file").build());

        cliInterface.addOptionGroup(mainCommand);

        cliInterface.addOption(HELP_SHORT, HELP_LONG, false,
                "print this message");

        cliInterface.addOption(Option.builder(OUTPUT)
                .desc("Specifies the output file for the extract commands")
                .hasArg().argName("output_file").build());

        return cliInterface;
    }

    private static void trainWordEmbeddings(String conlluFilePath) {
        Long seed = 1000L;

        JavaSparkContext sc = (new SparkSetup()).get();

        WordEmbeddingsGradientDescentImpl1<StringContext, String> wordEmbeddings = new WordEmbeddingsGradientDescentImpl1<>(
                5, 0.001f, 0.0001f, 0.0001f, 100, sc);

        System.out.println(String.format("%s - loading conllu file...", (new Date()).toString()));
        JavaRDD<Tuple2<StringContext, String>> trainSet = sc.parallelize(getTrainData(conlluFilePath));
        TrainData<StringContext, String> trainData = new TrainData<>(trainSet, 4, seed);

        System.out.println(
                String.format(
                        "%s - loaded %d pairs <Word, Context> with %d unique words and %d unique contexts",
                        (new Date()).toString(),
                        trainData.trainSetSize, trainData.numberOfWords, trainData.numberOfContexts)
        );

        wordEmbeddings.train(trainData, Objects.hash(seed, 1));
    }

    private static List<Tuple2<StringContext, String>> getTrainData(String pathToConllu) {
        ConlluReader reader = new ConlluReader(pathToConllu);
        List<Tuple2<StringContext, String>> trainData = new LinkedList<>();

        for (Sentence sent : reader) {
            TreeNode<Word> tree = sent.getTree();
            tree.traversePreOrder(new TraversalAction<TreeNode<Word>>() {
                @Override
                public void perform(TreeNode<Word> wordTreeNode) {
                    List<Word> words = wordTreeNode.subtrees().stream().map(TreeNode::data)
                            .sorted(Comparator.comparingInt(o -> o.id)).collect(toList());
                    List<String> context = words.stream().map(x -> x.upostag).collect(toList());
                    Word head = wordTreeNode.data();
                    if (!context.isEmpty())
                        trainData.add(new Tuple2<>(new StringContext(context), String.format("%s+%s", head.lemma, head.upostag)));
                }

                @Override
                public boolean isCompleted() {
                    return false;
                }
            });
        }

        return trainData;
    }

    private static String mapWordToString(Word word) {
        return String.format("%s+%s", word.lemma, word.upostag);
    }
}