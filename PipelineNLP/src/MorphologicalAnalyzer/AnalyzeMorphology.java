package MorphologicalAnalyzer;
import LexicalAnalyzer.FSA.BasicOperations;

import java.io.*;
import java.lang.ProcessBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** STEP 3: Applying EMOR on tokenization charts to map words in their surface forms to their cannonical representations
 * @author MeryemMhamdi
 * @date 4/10/17.
 */
public class AnalyzeMorphology {

    private static String EMOR_LOCATION = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/E-MOR/morph.a";
    private static String FST_MOR = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Morphological Analysis/SFST/src/fst-mor1";
    private static String UDC_SENTENCES = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/Tokenization Analysis/UDC/Train/udc_sentencesList.ser";
    private static String TOK_CHARTS = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/Tokenization Analysis/UDC/Train/udc_tokenizationCharts.ser";
    private static String TXT_MORPHOLOGY_RESULTS = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/udc_MorphologyResults_DEBUG.txt";
    private static String STREAM_MORPHOLOGY_RESULTS = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/udc_morphologyAnalysis_DEBUG.ser";

    public static void main(String[] args) {
        try {
            // 1. Loading the Sentences
            FileInputStream in = new FileInputStream(UDC_SENTENCES);

            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<String> sentences =  (ArrayList<String>)stream.readObject();

            // 2. Load the Tokenization Charts
            in = new FileInputStream(TOK_CHARTS);
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts =  (ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>)stream.readObject();


            // 3. Applying the Transducer to get the canonical form of every surface form in the chart

            /** 3.1. Traversing the list of charts **/

            ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> chartsInfo =  new ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> ();

            System.out.println("SIZE of SENTENCES==> "+sentences.size()+ " SIZE of Charts=> "+charts.size());
            for (int i=0;i<charts.size();i++) {
                for (int j=0;j<charts.get(i).size();j++) {
                    ArrayList<ArrayList<Map<String, ArrayList<String>>>> chartInfo = new ArrayList<ArrayList<Map<String, ArrayList<String>>>>();
                    for (int k = 0; k < charts.get(i).get(j).get(0).size() ; k++) {
                        ArrayList<Map<String, ArrayList<String>>> subList = new ArrayList<Map<String, ArrayList<String>>>();
                        for (int l = 0; l < charts.get(i).get(j).get(0).size()-k; l++) {
                            BasicOperations.Edge edge = charts.get(i).get(j).get(k).get(l);
                            Map<String, ArrayList<String>> canonicalForms = new HashMap<String, ArrayList<String>>();
                            if (edge != null && edge.getIsKnown() == true) {
                                String word = sentences.get(i).substring(edge.getStartIndex(), edge.getEndIndex());
                                if (!word.equals(" ")) {
                                    Process process = new ProcessBuilder(FST_MOR, EMOR_LOCATION, word).start();
                                    InputStream is = process.getInputStream();
                                    InputStreamReader isr = new InputStreamReader(is);
                                    BufferedReader br = new BufferedReader(isr);
                                    String line;
                                    ArrayList<String> list = new ArrayList<String>();
                                    line = br.readLine();
                                    while (line != null && line.length() > 1) {
                                        list.add(line.toString());
                                        line = br.readLine();
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

            BufferedWriter writer = new BufferedWriter(new FileWriter(TXT_MORPHOLOGY_RESULTS));

            int counter=0;
            System.out.println("=====================Printing Info Charts=================");
            for (int k=0;k<chartsInfo.size();k++){
                writer.write("INFO CHART "+counter+"=> ");
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
            FileOutputStream fos = new FileOutputStream(STREAM_MORPHOLOGY_RESULTS);
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
