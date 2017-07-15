package LexicalAnalyzer.Tokenizer;

import SyntacticAnalyzer.DependencyConLL;
import java.io.*;
import java.util.ArrayList;


/**
 * Created by MeryemMhamdi on 6/6/17.
 */
public class ExtractTrueTreebankSentences {
    private static String PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Datasets/UDC/";
    private static String TREEBANK_PATH_FOLDER = PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER + "parsedDependenciesConLL_train.ser";

    private static String PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Train/";
    private static String PATH_OUTPUT_SENTENCES_STREAM = PATH_FOLDER + "udc_treebank_sentencesList.ser";
    private static String PATH_OUTPUT_SENTENCES_TXT = PATH_FOLDER + "udc_treebank_sentencesList.txt";

    public static void main(String[] args) {
        try {

            // 2. Reading the stored true sentences from the treebank
            FileInputStream in = new FileInputStream(TREEBANK_PATH_FOLDER);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();

            ArrayList<String> treebankSentencesList = new ArrayList<>();
            for (int i = 0; i < parsedDependenciesConLL.size(); i++) {
                String sentence = "";
                for (int j = 0; j < parsedDependenciesConLL.get(i).size() - 1; j++) {
                    sentence = sentence + parsedDependenciesConLL.get(i).get(j).getForm() + " ";
                }
                sentence = sentence + parsedDependenciesConLL.get(i).get(parsedDependenciesConLL.get(i).size() - 1).getForm();
                System.out.println(i+"=>"+sentence);
                treebankSentencesList.add(sentence);
            }
            System.out.println("SIZE=>"+treebankSentencesList.size());

            BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_OUTPUT_SENTENCES_TXT));
            for (int i=0;i<treebankSentencesList.size();i++){
                writer.write(treebankSentencesList.get(i));
            }
            writer.close();

            FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_SENTENCES_STREAM);
            ObjectOutputStream s = new ObjectOutputStream(fos);
            s.writeObject(treebankSentencesList);
            s.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidClassException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
