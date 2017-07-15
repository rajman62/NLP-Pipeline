/**
 * To run this:
 *
 * javac -sourcepath src -d build/dk src/dk/brics/automaton/TokenizePennTreebank.java
 * java -cp build/dk dk.brics.automaton.TokenizePennTreebank
 *
 */

package LexicalAnalyzer.PTB;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.BasicOperations;
import LexicalAnalyzer.FSA.RegExp;
import LexicalAnalyzer.FSA.SepSpecification;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.util.stream.Stream;

public class TokenizePennTreebank {
    public static void main(String[] args) {
        try {
            // 1.1. LOAD THE ALREADY FILLED LEXICON FROM THE FILE
            FileInputStream in = new FileInputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/tokFSA.ser");
            Automaton automaton = new Automaton();
            Automaton tokFSA = automaton.load(in);


            // 1.2. LOAD THE ALREADY FILLED sepFSA FROM THE FILE
            Automaton sepFSA = new RegExp(" |\\!|\\?|[.]|[.][.][.]|'|,|;|:|[\"]|[\\(]|[\\)]|[\\{]|[\\}]|[\\]]|[\\[]").toAutomaton();
            HashMap<String,SepSpecification> specifications = new HashMap<String,SepSpecification>();

            specifications.put(" ",new SepSpecification(false,false));
            specifications.put("!",new SepSpecification(true,true));
            specifications.put("?",new SepSpecification(true,true));
            specifications.put(".",new SepSpecification(true,true));
            specifications.put(";",new SepSpecification(true,true));
            specifications.put(":",new SepSpecification(true,false));
            specifications.put("...",new SepSpecification(true,true));
            specifications.put("'",new SepSpecification(true,false));
            specifications.put("\"",new SepSpecification(true,false));
            specifications.put(",",new SepSpecification(true,false));
            specifications.put("$",new SepSpecification(true,false));
            specifications.put("(",new SepSpecification(true,false));
            specifications.put(")",new SepSpecification(true,false));
            specifications.put("{",new SepSpecification(true,false));
            specifications.put("}",new SepSpecification(true,false));
            specifications.put("[",new SepSpecification(true,false));
            specifications.put("]",new SepSpecification(true,false));

            // 2. READ FROM DOCUMENTS IN THE FOLDER OF PENN TREEBANK
            ArrayList<String>sentences = new ArrayList<String>();

            BufferedReader br = new BufferedReader(new FileReader("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/Sentences.txt"));
            String line = br.readLine();
            while (line != null) {
                sentences.add(line);
                line = br.readLine();
            }

            /* Storing the sentences*/
            FileOutputStream fos = new FileOutputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/sentencesList.ser");
            ObjectOutputStream s = new ObjectOutputStream(fos);
            s.writeObject(sentences);
            s.flush();

            // 3. APPLY THE TRAVERSAL OF THE AUTOMATON ON THE LIST OF SENTENCES
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts = new ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>();

            //System.out.println("Number of sentences: "+sentences.size()); // 1745 sentences

            ArrayList<String> new_sentences = new ArrayList<String>();

            int start = 0;
            int end = sentences.size();
            for (int i=start;i<end;i++) { //sentences.size()
                if (i%100==0){
                    System.out.println(i);
                }
                ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>> subCharts = tokFSA.traverseExtendedSolution(tokFSA, sepFSA, specifications,sentences.get(i));//sentences.get(i).toString());
                charts.add(subCharts);
                /*
                if (subCharts.size()!=1) { //
                    System.out.println("i=> "+i + " SENTENCE=>"+sentences.get(i)+"\n >>>>>>>>>>>> subCharts.size()= " +subCharts.size());
                    System.out.println("=====================Printing SUBCharts=================");
                    for (int k=0;k<subCharts.size();k++){
                        System.out.println("CHART "+k+"=> ");
                        for (int l=subCharts.get(k).size()-1;l>=0;l--){
                            String result = "[";
                            for (int o=0;o<subCharts.get(k).get(l).size();o++) {
                                String edge;
                                if (subCharts.get(k).get(l).get(o) == null) {
                                    edge = "";
                                } else {
                                    edge = subCharts.get(k).get(l).get(o).toString();
                                }
                                if (i < subCharts.get(k).get(l).size()-1 ) {
                                    result = result + edge+",";
                                } else {
                                    result = result + edge;
                                }
                            }
                            System.out.println(result+"]");
                        }
                    }
                    System.out.println("==========================================================");
                }*/
            }

            /*

            System.out.println("=====================Printing Charts=================");
            for (int k=0;k<charts.size();k++){
                System.out.println("CHART "+k+"=> ");
                for (int l=charts.get(k).size()-1;l>=0;l--){
                    String result = "[";
                    for (int i=0;i<charts.get(k).get(l).size();i++) {
                        String edge;
                        if (charts.get(k).get(l).get(i) == null) {
                            edge = "";
                        } else {
                            edge = charts.get(k).get(l).get(i).toString();
                        }
                        if (i < charts.get(k).get(l).size()-1 ) {
                            result = result + edge+",";
                        } else {
                            result = result + edge;
                        }
                    }
                    System.out.println(result+"]");
                }
            }
            System.out.println("==========================================================");
            */

            // 4. SAVING INTO FOR LATER REUSE IN LEMMATIZATION PART

            fos = new FileOutputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/tokenizationCharts.ser");
            s = new ObjectOutputStream(fos);
            s.writeObject(charts);
            s.flush();

            fos = new FileOutputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/new_sentences.ser");
            s = new ObjectOutputStream(fos);
            s.writeObject(new_sentences);
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

