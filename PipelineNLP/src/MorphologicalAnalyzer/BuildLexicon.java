package MorphologicalAnalyzer;

import SyntacticAnalyzer.DependencyConLL;

import java.io.*;
import java.util.*;

/** STEP 1: Extract the words lemmas and adds cannonical forms based on that to a lexicon file.
 * @author MeryemMhamdi
 * @date 7/9/17.
 */
public class BuildLexicon {
    /******************************************************************************************************************/
    /**
     * LOCATION FILES TO BE REPLACED
     */
    // TO BE REPLACED BY THE PATH TO THE PARSED DEPENDENCIES DATASET
    private static String PARSED_TREEBANK = "/Users/MeryemMhamdi/Google Drive/Semester Project" +
            "/3 Implementation & Algorithms" +
            "/Datasets/UDC/parsedDependenciesConLL_train_true.ser";

    // TO BE REPLACED BY THE PATH OF THE OUTPUT FILE
    private static String TO_BE_ADDED_LEXICON = "/Users/MeryemMhamdi/Desktop/lexicon.txt";
    /******************************************************************************************************************/
    public static void main (String [] args) {

        try {
            // 1. Loading the Sentences
            FileInputStream in = new FileInputStream(PARSED_TREEBANK);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsed_conllu = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();

            Set<String> lexicon = new HashSet<>();
            Set<String> nouns = new HashSet<>();
            Set<String> proper = new HashSet<>();
            Set<String> verbs = new HashSet<>();
            Set<String> adj = new HashSet<>();
            Set<String> adv = new HashSet<>();
            Set<String> others = new HashSet<>();
            Set<String> foreigns = new HashSet<>();

            Set<String> tags = new HashSet<String>();
            Set<String> nounsTags = new HashSet<String>(Arrays.asList("NNS","NN"));
            Set<String> properTags = new HashSet<String>(Arrays.asList("NNP","NNPS"));
            Set<String> verbsTags = new HashSet<String>(Arrays.asList("VBD", "VBG","VBN","VBP","VB","VBZ"));
            Set<String> adjTags = new HashSet<String>(Arrays.asList("JJS", "JJR","JJ"));
            Set<String> advTags = new HashSet<String>(Arrays.asList("RBS", "RBR","RB"));


            for (int i=0;i<parsed_conllu.size();i++){

                for (int j=0;j<parsed_conllu.get(i).size();j++) {
                    String tag = parsed_conllu.get(i).get(j).getXPosTag().toString();
                    if (nounsTags.contains(tag)) {
                        if (!nouns.contains(parsed_conllu.get(i).get(j).getLemma())) {
                            lexicon.add(parsed_conllu.get(i).get(j).getLemma() + "<N-reg>");
                            nouns.add(parsed_conllu.get(i).get(j).getLemma());
                        }
                    }
                    if (properTags.contains(tag)) {
                        if (!proper.contains(parsed_conllu.get(i).get(j).getLemma().toString())) {
                            lexicon.add(parsed_conllu.get(i).get(j).getLemma()+"<PropN-reg>");
                            proper.add(parsed_conllu.get(i).get(j).getLemma());
                        }
                    }

                    if (verbsTags.contains(tag)) {
                        if (!verbs.contains(parsed_conllu.get(i).get(j).getLemma())) {
                            lexicon.add(parsed_conllu.get(i).get(j).getLemma()+"<V-reg>");
                            verbs.add(parsed_conllu.get(i).get(j).getLemma());
                        }
                    }
                    if (adjTags.contains(tag)) {
                        if (!adj.contains(parsed_conllu.get(i).get(j).getLemma())) {
                            lexicon.add(parsed_conllu.get(i).get(j).getLemma()+"<A-reg>");
                            adj.add(parsed_conllu.get(i).get(j).getLemma());
                        }
                    }
                    if (advTags.contains(tag)) {
                        if (!adv.contains(parsed_conllu.get(i).get(j).getLemma())) {
                            lexicon.add(parsed_conllu.get(i).get(j).getLemma()+"<Adv-reg>");
                            adv.add(parsed_conllu.get(i).get(j).getLemma());
                        }
                    }
                    if (parsed_conllu.get(i).get(j).getXPosTag().equals("FW")) {
                        if (!foreigns.contains(parsed_conllu.get(i).get(j).getLemma())){
                            lexicon.add(parsed_conllu.get(i).get(j).getLemma()+"<foreign>");
                            foreigns.add(parsed_conllu.get(i).get(j).getLemma());
                        }
                    } if (!nounsTags.contains(tag) &&
                            !properTags.contains(tag) &&
                            !verbsTags.contains(tag) &&
                            !adjTags.contains(tag) &&
                            !advTags.contains(tag) &&
                            !parsed_conllu.get(i).get(j).getXPosTag().equals("FW")) {
                        if(!others.contains(parsed_conllu.get(i).get(j).getLemma()+ "<" + parsed_conllu.get(i).get(j).getXPosTag()+"-reg>")) {
                            lexicon.add(parsed_conllu.get(i).get(j).getLemma() + "<" +parsed_conllu.get(i).get(j).getXPosTag()+"-reg>");
                            others.add(parsed_conllu.get(i).get(j).getLemma()+ "<" +parsed_conllu.get(i).get(j).getXPosTag()+"-reg>");
                        }
                        tags.add(parsed_conllu.get(i).get(j).getXPosTag());
                    }
                }
            }


            BufferedWriter writer = new BufferedWriter(new FileWriter(TO_BE_ADDED_LEXICON));
            for (String wordTag : lexicon) {
                writer.write(wordTag + "\n");
            }
            writer.close();

            System.out.println("SIZE=> "+lexicon.size());
            String result = "";
            for (String tag: tags){
                //result = result + tag+"><";
                System.out.println( "<>:<"+tag+"-reg> <"+tag+">|\\");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
