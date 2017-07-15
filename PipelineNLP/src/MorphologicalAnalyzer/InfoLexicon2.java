package MorphologicalAnalyzer;

import LexicalAnalyzer.FSA.BasicOperations;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MeryemMhamdi on 6/2/17.
 */
public class InfoLexicon2 {
    private static String PATH_EMOR = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/EMOR/emor.a";
    private static String PATH_INFLECTION = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/Inflections/inflection.a"; // Extension of EMOR
    private static String PATH_FST_MOR = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Morphological Analysis/SFST/src/fst-mor1";

    private static String INPUT_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Train/";
    private static String OUTPUT_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Morphological Analysis/UDC/Train/";
    private static String PATH_SENTENCES = INPUT_PATH_FOLDER+"trueSentences.ser";
    private static String PATH_TOK_CHARTS= INPUT_PATH_FOLDER+"trueCharts.ser";
    private static String PATH_EMOR_MAP = OUTPUT_PATH_FOLDER+"emor_map.ser";
    private static String PATH_TAGS_MAP = OUTPUT_PATH_FOLDER+"tagsMap.ser";
    private static String PATH_OUTPUT_MOR_CHARTS_STREAM = OUTPUT_PATH_FOLDER+"true_Treebank_MorphologyResults.ser";
    private static String PATH_OUTPUT_MOR_CHARTS_TXT = OUTPUT_PATH_FOLDER+"true_Treebank_MorphologyResults.txt";

    public static void main(String[] args) {
         try {
            // 1.1. Loading the Sentences
            FileInputStream in = new FileInputStream(PATH_SENTENCES);

            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<String> sentences =  (ArrayList<String>)stream.readObject();

            // 1.2.

            FileInputStream in2 = new FileInputStream(PATH_EMOR_MAP);

            ObjectInputStream stream2 = new ObjectInputStream(in2);
            Map<String,ArrayList<String>> emorMapAll =  (Map<String,ArrayList<String>>)stream2.readObject();

            // 1.3. Loading the tags
            FileInputStream in1 = new FileInputStream(PATH_TAGS_MAP);
            ObjectInputStream stream1 = new ObjectInputStream(in1);
            HashMap<String,String> mappingPennTreebankEMOR =  (HashMap<String,String>)stream1.readObject();

            // 2. Load the Tokenization Charts
            in = new FileInputStream(PATH_TOK_CHARTS);
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts =  (ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>)stream.readObject();


            // 3. Applying the Transducer to get the canonical form of every surface form in the chart

            /** 3.1. Traversing the list of charts **/

            ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> chartsInfo =  new ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> ();

            System.out.println("SIZE of SENTENCES==> "+sentences.size()+ " SIZE of Charts=> "+charts.size());
            int count = 0;
            for (int i=0;i<charts.size();i++) { //sentences.size()
                for (int j=0;j<charts.get(i).size();j++) {

                    if (i % 10 == 0) {
                        System.out.println(i);
                        //System.out.println(sentences.get(i));
                    }
                    //System.out.println("s="+s+"i="+i);
                    //System.out.println("charts.get(i).get(j).get(0).size()= "+charts.get(i).get(j).get(0).size());
                    ArrayList<ArrayList<Map<String, ArrayList<String>>>> chartInfo = new ArrayList<ArrayList<Map<String, ArrayList<String>>>>();
                    for (int k = 0; k < charts.get(i).get(j).get(0).size() ; k++) {
                        ArrayList<Map<String, ArrayList<String>>> subList = new ArrayList<Map<String, ArrayList<String>>>();
                        for (int l = 0; l < charts.get(i).get(j).get(0).size()-k; l++) {
                            BasicOperations.Edge edge = charts.get(i).get(j).get(k).get(l);
                            Map<String, ArrayList<String>> canonicalForms = new HashMap<String, ArrayList<String>>();
                            if (edge != null && edge.getIsKnown() == true) {
                                //System.out.println("Edge toString:" + edge.toString());
                                String word = sentences.get(i).substring(edge.getStartIndex(), edge.getEndIndex());
                                if (!word.equals(" ")) {

                                    ArrayList<String> list = new ArrayList<String>();
                                    if (emorMapAll.containsKey(word)) {
                                        list = emorMapAll.get(word);
                                    } else {
                                        System.out.println(word);
                                        count ++;
                                    }
                                    if (list.size() >= 1) {
                                        canonicalForms.put(word, list);
                                        subList.add(canonicalForms);
                                    }
                                }
                            } else {
                                subList.add(canonicalForms);
                            }
                        }
                        chartInfo.add(subList);
                    }
                    chartsInfo.add(chartInfo);
                }
            }

            System.out.println("Number of unmmaped words=> "+count);
            BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_OUTPUT_MOR_CHARTS_TXT));

            int counter=0;
            int sent = 0;
            System.out.println("=====================Printing Info Charts=================");
            for (int k=0;k<chartsInfo.size();k++){
                writer.write("SENTENCE " +counter + ": "+sentences.get(k)+"\n");
                writer.write("INFO CHART => ");
                for (int l=chartsInfo.get(k).size()-1;l>=0;l--){
                    String result = "[";
                    for (int i=0;i<chartsInfo.get(k).get(l).size();i++) {
                        String wordInfo;
                        if (chartsInfo.get(k).get(l).get(i) == null) {
                            wordInfo = "";
                        } else {
                            wordInfo = chartsInfo.get(k).get(l).get(i).toString();
                        }
                        if (i < chartsInfo.get(k).get(l).size()-1 ) {
                            result = result + wordInfo+",";
                        } else {
                            result = result + wordInfo;
                        }
                    }
                    writer.write(result+"]\n");
                }
                counter ++;
            }
            System.out.println("==========================================================");
            writer.close();

            /** 3.2 Saving the output of Morphological Analysis **/
            FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_MOR_CHARTS_STREAM);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(chartsInfo);
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
