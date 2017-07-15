package MorphologicalAnalyzer;
import LexicalAnalyzer.FSA.BasicOperations;

import java.io.*;
import java.lang.ProcessBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MeryemMhamdi on 4/10/17.
 */
public class InfoLexicon {

    public printOutput getStreamWrapper(InputStream is, String type) {
        return new printOutput(is, type);
    }

    public static void main(String[] args) {
        String emor_location = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/EMOR/emor.a";
        String inflection_location = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Morphological Analysis/Inflections/inflection.a"; // Extension of EMOR
        String fst_mor = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Morphological Analysis/SFST/src/fst-mor1";
        try {
            // 1. Loading the Sentences
            FileInputStream in = new FileInputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/udc_sentencesList.ser");

            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<String> sentences =  (ArrayList<String>)stream.readObject();

            // 2. Loading the tags
            FileInputStream in1 = new FileInputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/tagsMap.ser");
            ObjectInputStream stream1 = new ObjectInputStream(in1);
            HashMap<String,String> mappingPennTreebankEMOR =  (HashMap<String,String>)stream1.readObject();

            // 3. Load the Tokenization Charts
            in = new FileInputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/udc_tokenizationCharts.ser");
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts =  (ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>)stream.readObject();


            // 4. Applying the Transducer to get the canonical form of every surface form in the chart

            /** 4.1. Traversing the list of charts **/

            ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> chartsInfo =  new ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> ();

            System.out.println("SIZE of SENTENCES==> "+sentences.size()+ " SIZE of Charts=> "+charts.size());
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
                                    Process process = new ProcessBuilder(fst_mor, emor_location, word).start();
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
                                    if (list.size() < 1) {
                                        Process process_ext = new ProcessBuilder(fst_mor, inflection_location, word).start();
                                        InputStream is_ext = process_ext.getInputStream();
                                        InputStreamReader isr_ext = new InputStreamReader(is_ext);
                                        BufferedReader br_ext = new BufferedReader(isr_ext);
                                        line = br.readLine();
                                        while (line != null && line.length() > 1) {
                                            list.add(line.toString());
                                            line = br_ext.readLine();
                                        }
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

            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/udc_MorphologyResults.txt"));

            int counter=0;
            int sent = 0;
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

            /** 4.2 Saving the output of Morphological Analysis **/
            FileOutputStream fos = new FileOutputStream("/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/udc_morphologyAnalysis.ser");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(chartsInfo);
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private class printOutput extends Thread {
        InputStream is = null;

        printOutput(InputStream is, String type) {
            this.is = is;
        }

        public void run() {
            String s = null;
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                while ((s = br.readLine()) != null) {
                    System.out.println(s);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}

 /*
Runtime rt = Runtime.getRuntime();
InfoLexicon infoLex = new InfoLexicon();
printOutput errorReported, outputMessage;

try {
    Process proc = rt.exec("fst-mor /Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/EMOR/emor.a");
    errorReported = infoLex.getStreamWrapper(proc.getErrorStream(), "ERROR");
    outputMessage = infoLex.getStreamWrapper(proc.getInputStream(), "OUTPUT");
    errorReported.start();
    outputMessage.start();

} catch (IOException e) {
    e.printStackTrace();
}*/
