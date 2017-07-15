package SemanticAnalyzer;

import LexicalAnalyzer.FSA.Automaton;
import SyntacticAnalyzer.SmallConLL;

import java.io.*;
import java.util.*;

/**
 * Created by MeryemMhamdi on 5/19/17.
 */
public class CoOccurrenceMatrix {
    private ArrayList<String> NAVALemmas;
    private HashMap<ArrayList<String>,Double> counts;
    private HashMap<ArrayList<String>,Double> PMIs;

    public CoOccurrenceMatrix(){
        NAVALemmas = new ArrayList<String> ();
        counts = new HashMap<ArrayList<String>,Double> ();
        PMIs = new HashMap<ArrayList<String>,Double> ();
    }
    public boolean isNAVA(String pos){
        String [] navaArray = {"JJ","JJR","JJS","NN","NNS","NP","NPS","RB","RBR","RBS","VB","VBD","VBG","VBN","VBP","VBZ","VH"
                ,"VHG","VHN","VHP","VHZ","VV","VVD","VVG","VVN","VVP","VVZ"};
        List<String> navaPOS = Arrays.asList(navaArray);
        if (navaPOS.contains(pos)){
            return true;
        }
        else {
            return false;
        }
    }
    public static void main(String args[]){
        CoOccurrenceMatrix oc = new CoOccurrenceMatrix();

        // Output Matrix
        FileInputStream in = null;
        try {
            // Loading the parsed
            in = new FileInputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/dependenciesCYK1stPass.ser");
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<SmallConLL>> dependencies = (ArrayList<ArrayList<SmallConLL>>)stream.readObject();

            in = new FileInputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/wordsCount.ser");
            stream = new ObjectInputStream(in);
            HashMap<String,Integer> wordCount = (HashMap<String,Integer>)stream.readObject();

            for (int i=0;i<dependencies.size();i++){
                for (int j=0;j<dependencies.get(i).size();j++){
                    String head = dependencies.get(i).get(j).getHead();
                    String posHead = dependencies.get(i).get(j).getPosHead();
                    String dep = dependencies.get(i).get(j).getDep();
                    String posDep = dependencies.get(i).get(j).getPosDep();

                    if (oc.isNAVA(posHead) && !oc.NAVALemmas.contains(head)){
                        oc.NAVALemmas.add(head);
                    }
                    if (oc.isNAVA(posDep) && !oc.NAVALemmas.contains(dep)){
                        oc.NAVALemmas.add(dep);
                    }
                    if (oc.isNAVA(posHead) && oc.isNAVA(posDep)){
                        ArrayList<String> key = new ArrayList<String>();
                        key.add(head);
                        key.add(dep);
                        if (!oc.counts.containsKey(key)){
                            oc.counts.put(key,1.0);
                        }else {
                            oc.counts.put(key,oc.counts.get(key)+1.0);
                        }
                    }
                }

            }

            System.out.println(wordCount);

            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/semantic_similarities.txt"));

            List<Double> scores = new ArrayList<Double>();
            for (ArrayList<String> words: oc.counts.keySet()){
                double counts=1;
                String result = "";
                for (String word: words){
                    if (!wordCount.containsKey(word)){
                        System.out.println(word);
                    }else {
                        counts = counts * wordCount.get(word);
                    }
                    result = result + word + " ";
                }
                double pmi = oc.counts.get(words)/counts;
                System.out.println("PMI="+pmi);
                oc.PMIs.put(words, pmi);
                result = result + pmi;
                writer.write(result+"\n");
                scores.add(pmi);
            }
            writer.close();

            //System.out.println(oc.PMIs);
            System.out.println(scores);
            int minIndex =  scores.indexOf(Collections.min(scores));
            int maxIndex =  scores.indexOf(Collections.max(scores));

            System.out.println("minimum score => "+scores.get(minIndex)+" maximum score => "+scores.get(maxIndex));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
