package MorphologicalAnalyzer;

import LexicalAnalyzer.FSA.BasicOperations;
import SyntacticAnalyzer.DependencyConLL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MeryemMhamdi on 7/13/17.
 */
public class TestE_MORUDC {

    private static String OUTPUT_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Morphological Analysis/UDC/Train/";
    private static String PATH_TAGS_MAP = OUTPUT_PATH_FOLDER+"tagsMap.ser";

    public static void main (String [] args) {
        String fst_mor = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Morphological Analysis/SFST/src/fst-mor1";
        String emor_location = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms" +
                "/Morphological Analysis/E-MOR/morph.a";
        String PARSED_TREEBANK = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms" +
                "/Datasets/UDC/parsedDependenciesConLL_train_true.ser";
        try {

            /**
             * 1. Loading the Sentences
             */
            FileInputStream in = new FileInputStream(PARSED_TREEBANK);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsed_conllu = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();

            /**
             * 2. Reading the tags map
             */
            FileInputStream in1 = new FileInputStream(PATH_TAGS_MAP);
            ObjectInputStream stream1 = new ObjectInputStream(in1);
            HashMap<String,String> mappingPennTreebankEMOR =  (HashMap<String,String>)stream1.readObject();



            /**
             *  2. Applying the Transducer to get the canonical form of every surface form in the chart
             */



            for (int i=0;i<100;i++) { //parsed_conllu.size()
                for (int j = 0; j < parsed_conllu.get(i).size(); j++) {
                    String word = parsed_conllu.get(i).get(j).getForm();
                    String tag = parsed_conllu.get(i).get(j).getXPosTag();
                    String lemma = parsed_conllu.get(i).get(j).getLemma();
                    ArrayList<String> wordTagsList = new ArrayList<String>();
                    //System.out.println("Correct Word Tagging => ");

                    String lemmaTag = lemma + "<" + tag + ">";
                    wordTagsList.add(lemmaTag);
                    //System.out.println(lemmaTag);

                    Process process = new ProcessBuilder(fst_mor, emor_location, word).start();
                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    ArrayList<String> cannonicalForms = new ArrayList<String>();
                    String line = br.readLine();
                    //System.out.println("\nEMOR Word Tagging => ");
                    while (line != null && line.length() > 1) {
                        cannonicalForms.add(line.toString());
                        //System.out.println(line.toString());
                        line = br.readLine();
                    }
                    boolean flag = false;
                    for (String wordTag: wordTagsList){
                        if (cannonicalForms.contains(wordTag)){
                            flag = true;
                        }
                    }
                    if (flag == false){
                        System.out.println("WAS NOT FOUND!!!!!");
                        for (String wordTag: wordTagsList){
                            System.out.println(wordTag);
                        }
                    }
                    //System.out.println("\n********************\n");
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
