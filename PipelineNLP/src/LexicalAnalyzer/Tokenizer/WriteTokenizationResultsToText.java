package LexicalAnalyzer.Tokenizer;

import LexicalAnalyzer.FSA.BasicOperations;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * STEP 6:
 * This class writes back the results of tokenization charts to a text file with the sentence preceeding each chart
 * Created by MeryemMhamdi on 5/3/17.
 */
public class WriteTokenizationResultsToText {
    private static String PATH_FOLDER =  "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Test/";
    private static String PATH_INPUT_TOKEN_CHARTS = PATH_FOLDER+"udc_tokenizationCharts.ser";
    private static String PATH_INPUT_SENTENCES = PATH_FOLDER + "udc_sentencesList.ser";
    private static String PATH_OUTPUT_TOKEN_CHARTS_TXT = PATH_FOLDER +"udc_tokenizationResults.txt";
    public static void main(String [] args){
        try {
            // 1. LOADING THE CHARTS
            FileInputStream in = new FileInputStream(PATH_INPUT_TOKEN_CHARTS);
            ObjectInputStream os = new ObjectInputStream(in);
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts = (ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>) os.readObject();


            // 2. LOADING THE SENTENCES
            in = new FileInputStream(PATH_INPUT_SENTENCES);
            os = new ObjectInputStream(in);
            ArrayList<String> sentences = (ArrayList<String>) os.readObject();

            System.out.println(sentences.size());

            BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_OUTPUT_TOKEN_CHARTS_TXT));

            int counter = 0;
            System.out.println("=====================Printing Charts=================");
            for (int k = 0; k < charts.size(); k++) {
                writer.write("SENTENCE: "+sentences.get(k)+"\n");
                writer.write("CHART " + counter + "=>\n");
                for (int l = 0; l < charts.get(k).size() ; l++) {
                    String result = "[";
                    for (int i = charts.get(k).get(l).size()-1; i >=0 ; i--) {
                        String edge;
                        if (charts.get(k).get(l).get(i) == null) {
                            edge = "";
                        } else {
                            edge = charts.get(k).get(l).get(i).toString();
                        }
                        if (i < charts.get(k).get(l).size() - 1) {
                            result = result + edge + ",";
                        } else {
                            result = result + edge + "\n";
                        }
                    }
                    writer.write(result + "]"+"\n");
                }
                counter ++;
            }
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
