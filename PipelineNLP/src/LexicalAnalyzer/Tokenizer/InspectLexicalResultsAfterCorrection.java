package LexicalAnalyzer.Tokenizer;

import LexicalAnalyzer.FSA.BasicOperations;
import SyntacticAnalyzer.DependencyConLL;

import java.io.*;
import java.util.ArrayList;

/**
 * @author MeryemMhamdi
 * @date 6/11/17.
 */
public class InspectLexicalResultsAfterCorrection {

    /******************************************************************************************************************/
    /**
     * LOCATION FILES TO BE CHANGED
     */

    /**
     * Input Golden Truth
     */
    private static String PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project" +
            "/3 Implementation & Algorithms/Datasets/UDC/";
    private static String TREEBANK_PATH_FOLDER = PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER+"parsedDependenciesConLL_train.ser";



    /**
     * Tokenized
     */
    private static String TOKENIZATION_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/" +
            "Tokenization Analysis/UDC/Train/";
    public static String TREEBANK_CHARTS = TOKENIZATION_PATH_FOLDER+ "udc_tokenizationCharts_CORRECTION_FALSE.ser";
    private static String PATH_OUTPUT_INDICES = TOKENIZATION_PATH_FOLDER + "treebank_indicesFALSE.ser";

    /**
     *
     */

    private static String PATH_INPUT_SENTENCES_STREAM = TOKENIZATION_PATH_FOLDER + "falseChartsSentences.ser";

    private static String PATH_OUTPUT_INDICES_FALSE = TOKENIZATION_PATH_FOLDER + "treebank_indicesFALSE_CORRECTION_FALSE.ser";

    private static String PATH_OUTPUT_INDICES_NO_CHARTS = TOKENIZATION_PATH_FOLDER + "treebank_indicesNOCHARTS_CORRECTION_FALSE.ser";

    private static String PATH_OUTPUT_INDICES_TRUE = TOKENIZATION_PATH_FOLDER + "treebank_indicesTRUE_CORRECTION_FALSE.ser";

    private static String PATH_OUTPUT_TRUE_CHARTS = TOKENIZATION_PATH_FOLDER + "trueCharts_CORRECTION_FALSE.ser";

    private static String PATH_OUTPUT_TRUE_SENTENCES = TOKENIZATION_PATH_FOLDER + "trueSentences_CORRECTION_FALSE.ser";

    private static String PATH_OUTPUT_TRUE_PARSING_DEPENDENCIES = PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER +
            "parsedDependenciesConLL_train_true_CORRECTION_FALSE.conllu";


    private static String PATH_OUTPUT_FALSE_CHARTS_STREAM = TOKENIZATION_PATH_FOLDER + "falseChartsSentences_CORRECTION_FALSE.ser";

    private static String PATH_OUTPUT_FALSE_CHARTS_TXT = TOKENIZATION_PATH_FOLDER + "falseChartsSentences_CORRECTION_FALSE.txt";

    private static String NO_CHARTS_STREAM = TOKENIZATION_PATH_FOLDER + "noChartsSentences_CORRECTION_FALSE.ser";

    private static String NO_CHARTS_TXT = TOKENIZATION_PATH_FOLDER + "noChartsSentences_CORRECTION_FALSE.txt";

    /******************************************************************************************************************/

    public static void main (String [] args){
        try {
            FileInputStream in = new FileInputStream(TREEBANK_CHARTS);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts = (ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>) stream.readObject();

            in = new FileInputStream(TREEBANK_PATH_FOLDER);
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();


            in = new FileInputStream(PATH_INPUT_SENTENCES_STREAM);
            stream = new ObjectInputStream(in);
            ArrayList<String> sentences = (ArrayList<String>) stream.readObject();

            in = new FileInputStream(PATH_OUTPUT_INDICES);
            stream = new ObjectInputStream(in);
            ArrayList<Integer> indices = (ArrayList<Integer>) stream.readObject();



            int count_0 = 0;
            int count_1_true = 0;
            int count_1_false = 0;
            int count_more = 0;
            ArrayList<Integer> count_0List = new ArrayList<Integer>();
            ArrayList<Integer> count_moreList = new ArrayList<Integer>();
            ArrayList<Integer> count_1_trueList = new ArrayList<Integer>();
            ArrayList<Integer> count_1_falseList = new ArrayList<Integer>();

            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts1True = new ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>();
            ArrayList<String> noCharts = new ArrayList<String>();
            ArrayList<String> sentences1False = new ArrayList<String>();
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLLTrue = new ArrayList<ArrayList<DependencyConLL>> ();
            ArrayList<String> trueSentences = new ArrayList<String>();

            for (int i=0;i<charts.size();i++){ //
                if (charts.get(i).size() ==0){
                    count_0 = count_0 + 1;
                    count_0List.add(i);
                    noCharts.add(sentences.get(i));
                } else if (charts.get(i).size()>1){
                    count_more = count_more + 1;
                    count_moreList.add(i);
                } else if (charts.get(i).size()==1){
                    ArrayList<Boolean> existsList = new ArrayList<Boolean>();
                    int k = 0;
                    int level = 0;
                    if (parsedDependenciesConLL.get(indices.get(i)).size()<=charts.get(i).get(0).size()) {
                        for (int j = 0; j < parsedDependenciesConLL.get(indices.get(i)).size(); j++) {
                            boolean exists = false;
                            if (level + j < charts.get(i).get(0).size()) {
                                System.out.println("i= " + i + "j= " + j + "level= " + level + " charts.get(i).get(0).size()= " + charts.get(i).get(0).size());
                                System.out.println("parsedDependenciesConLL.get(i).get(j).getForm()= " + parsedDependenciesConLL.get(indices.get(i)).get(j).getForm());
                                String word = sentences.get(i).substring(charts.get(i).get(0).get(0).get(j + level).getStartIndex(), charts.get(i).get(0).get(0).get(j + level).getEndIndex());
                                System.out.println("word=" + word);
                                int length = charts.get(i).get(0).get(k).size();
                                k = 1;
                                if (word.equals(parsedDependenciesConLL.get(indices.get(i)).get(j).getForm())) {
                                    exists = true;
                                } else if (charts.get(i).get(0).size()>1){
                                    length = charts.get(i).get(0).get(1).size();
                                    while (!word.equals(parsedDependenciesConLL.get(indices.get(i)).get(j).getForm()) && k < length && j + level < length) {
                                        System.out.println("k= " + k + " j= " + j + "length= " + length + " level= " + level);
                                        System.out.println("HERE");
                                        if (charts.get(i).get(0).get(k).get(j + level) != null) {
                                            word = sentences.get(i).substring(charts.get(i).get(0).get(k).get(j + level).getStartIndex()
                                                    , charts.get(i).get(0).get(k).get(j + level).getEndIndex());
                                            if (word.equals(parsedDependenciesConLL.get(indices.get(i)).get(j).getForm())) {
                                                exists = true;
                                                level = level + k;
                                            }
                                            System.out.println("word= " + word);
                                        }
                                        k = k + 1;
                                        length = charts.get(i).get(0).get(k).size();
                                    }
                                }
                            }
                            existsList.add(exists);
                        }
                        if (!existsList.contains(false)) {
                            count_1_true = count_1_true + 1;
                            count_1_trueList.add(i);
                            charts1True.add(charts.get(i));
                            trueSentences.add(sentences.get(i));
                            parsedDependenciesConLLTrue.add(parsedDependenciesConLL.get(indices.get(i)));

                        } else {
                            count_1_false = count_1_false + 1;
                            count_1_falseList.add(i);
                            sentences1False.add(sentences.get(i));
                        }
                    } else {
                        count_1_false = count_1_false + 1;
                        count_1_falseList.add(i);
                        sentences1False.add(sentences.get(i));
                    }
                }
            }

            System.out.println("count_0= "+count_0+ " at=> "+count_0List);
            System.out.println("count_1_true= "+count_1_true+ " at=> "+count_1_trueList);
            System.out.println("count_1_false= "+count_1_false+ " at=> "+count_1_falseList);
            System.out.println("count_more= "+count_more+ " at=> "+count_moreList);

            FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_TRUE_CHARTS);
            ObjectOutputStream outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(charts1True);
            outputStream.flush();

            //

            fos = new FileOutputStream(PATH_OUTPUT_INDICES_TRUE);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(count_1_trueList);
            outputStream.flush();

            //

            fos = new FileOutputStream(PATH_OUTPUT_TRUE_SENTENCES);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(trueSentences);
            outputStream.flush();

            fos = new FileOutputStream(PATH_OUTPUT_TRUE_PARSING_DEPENDENCIES);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(parsedDependenciesConLLTrue);
            outputStream.flush();

            //

            fos = new FileOutputStream(PATH_OUTPUT_FALSE_CHARTS_STREAM);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(sentences1False);
            outputStream.flush();

            fos = new FileOutputStream(PATH_OUTPUT_INDICES_FALSE);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(count_1_falseList);
            outputStream.flush();

            BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_OUTPUT_FALSE_CHARTS_TXT));
            for (int i=0;i<sentences1False.size();i++){
                writer.write(sentences1False.get(i)+"\n\n");
            }
            writer.close();


            fos = new FileOutputStream(NO_CHARTS_STREAM);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(noCharts);
            outputStream.flush();

            fos = new FileOutputStream(PATH_OUTPUT_INDICES_NO_CHARTS);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(count_0List);
            outputStream.flush();

            writer = new BufferedWriter(new FileWriter(NO_CHARTS_TXT));
            for (int i=0;i<noCharts.size();i++){
                writer.write(noCharts.get(i)+"\n\n");
            }
            writer.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
