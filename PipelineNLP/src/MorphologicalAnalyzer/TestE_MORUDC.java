package MorphologicalAnalyzer;

import SyntacticAnalyzer.DependencyConLL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/** STEP 2: Debugging EMOR by checking its output against treebank words
 * Created by MeryemMhamdi on 7/13/17.
 */
public class TestE_MORUDC {

    private static String FST_MOR = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Morphological Analysis/SFST/src/fst-mor1";
    private static String EMOR_LOCATION = "/Users/MeryemMhamdi/Desktop/NLP-Pipeline/Morphology/E-MOR/morph.a";
    private static String PARSED_TREEBANK = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms" +
            "/Datasets/UDC/parsedDependenciesConLL_train_true.ser";

    private static String STREAM_MAPPINGS = "/Users/MeryemMhamdi/Desktop/NLP-Pipeline/PipelineNLP/src/MorphologicalAnalyzer/tagsMappings.ser";

    public static void main (String [] args) {

        try {


            /**
             * 1. Loading the Sentences
             */
            FileInputStream in = new FileInputStream(PARSED_TREEBANK);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsed_conllu = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();

            in = new FileInputStream(STREAM_MAPPINGS);
            stream = new ObjectInputStream(in);
            HashMap<String,String> mappingTags = (HashMap<String,String>) stream.readObject();


            /**
             *  2. Applying the Transducer to get the canonical form of every surface form in the chart
             */

            for (int i=0;i<parsed_conllu.size();i++) {
                for (int j = 0; j < parsed_conllu.get(i).size(); j++) {
                    String word = parsed_conllu.get(i).get(j).getForm();
                    String tag = parsed_conllu.get(i).get(j).getXPosTag();
                    String lemma = parsed_conllu.get(i).get(j).getLemma();
                    ArrayList<String> wordTagsList = new ArrayList<String>();
                    String mapping = "<" + tag + ">";
                    for (String key: mappingTags.keySet()){
                        if (mappingTags.get(key).equals("<" + tag + ">")){
                            mapping = key;
                        }
                    }
                    String lemmaTag = lemma + mapping;
                    wordTagsList.add(lemmaTag);

                    Process process = new ProcessBuilder(FST_MOR, EMOR_LOCATION, word).start();
                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    ArrayList<String> cannonicalForms = new ArrayList<String>();
                    String line = br.readLine();
                    while (line != null && line.length() > 1) {
                        cannonicalForms.add(line.toString());
                        line = br.readLine();
                    }
                    boolean flag = false;
                    for (String wordTag: wordTagsList){
                        if (cannonicalForms.contains(wordTag)){
                            flag = true;
                        }
                    }
                    if (flag == false){
                       // System.out.println("WAS NOT FOUND!!!!!");
                        for (String wordTag: wordTagsList){
                            System.out.println(word+">>>>"+wordTag);
                        }
                    }
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
