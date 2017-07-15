package LexicalAnalyzer.Tests;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.BasicOperations;
import LexicalAnalyzer.FSA.RegExp;
import LexicalAnalyzer.FSA.SepSpecification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by MeryemMhamdi on 4/29/17.
 */
public class ImplicitExtended {
    public static void main(String args[]) {

        try {
            // 1. LOAD THE ALREADY FILLED tokFSA FROM THE FILE
            FileInputStream in = new FileInputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/tokFSA.ser");
            Automaton automaton = new Automaton();
            Automaton tokFSA = automaton.load(in);

            //tokFSA = new RegExp("abat achor king|abat|achor|king|abat achor|achor king").toAutomaton();
            //tokFSA = new RegExp("Nov[.] [1-29]+(th)?|[@]|U[.]S[.]|Pierre|The|length|is|meters|Let|go").toAutomaton();
            Automaton digit = new RegExp("[0-9]+|&|[\\%]|[\\$]").toAutomaton();
            Automaton percent = new RegExp("(([0-9]+)|([0-9]+)[.]([0-9]+))([\\%])").toAutomaton();
            Automaton dollar = new RegExp("([\\$])(([0-9]+)|([0-9]+)[.]([0-9]+)|([0-9]+)([,]([0-9]+))+)").toAutomaton();
            Automaton us_dollar = new RegExp("(US)([\\$])(([0-9]+)|([0-9]+)[.]([0-9]+)|([0-9]+)([,]([0-9]+))+)").toAutomaton();
            Automaton fraction1 = new RegExp("([0-9]+)[.]([0-9]+)").toAutomaton(); //([0-9]+)(.)([0-9]+)
            Automaton fraction2 = new RegExp("([0-9]+)([,]([0-9]+))+").toAutomaton();
            Automaton hour = new RegExp("([0-23]):([00:59])").toAutomaton();
            Automaton alpha = new RegExp("[a-zA-Z]+").toAutomaton();
            //tokFSA = tokFSA.union(digit).union(fraction1).union(fraction2).union(hour).union(alpha).union(dollar).union(us_dollar).union(percent);


            // 2. LOAD THE ALREADY FILLED sepFSA FROM THE FILE
            Automaton sepFSA = new RegExp(" |\\!|\\?|[.]|[.][.][.]|'|,|;|:|[\"]|[\\(]|[\\)]").toAutomaton();

            HashMap<String,SepSpecification> specifications = new HashMap<String,SepSpecification>();

            specifications.put(" ",new SepSpecification(false,false));
            specifications.put("!",new SepSpecification(true,true));
            specifications.put("?",new SepSpecification(true,true));
            specifications.put(".",new SepSpecification(true,true));
            specifications.put(";",new SepSpecification(true,false));
            specifications.put(":",new SepSpecification(true,false));
            specifications.put("...",new SepSpecification(true,true));
            specifications.put("'",new SepSpecification(true,false));
            specifications.put("\"",new SepSpecification(true,false));
            specifications.put(",",new SepSpecification(true,false));
            specifications.put("$",new SepSpecification(true,false));
            specifications.put("(",new SepSpecification(true,false));
            specifications.put(")",new SepSpecification(true,false));

            System.out.println(tokFSA.run("Nov. 29"));

            ArrayList<String>sentences = new ArrayList<String>();

            try(Stream<Path> paths = Files.walk(Paths.get("/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Datasets/treebank/RawCorpus"))) {
                paths.forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {
                        BufferedReader br = null;
                        try {
                            if (Files.isRegularFile(filePath) && !filePath.toString().equals("/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Datasets/treebank/RawCorpus/.DS_Store")) {
                                br = new BufferedReader(new FileReader(filePath.toString()));
                                StringBuilder sb = new StringBuilder();
                                String line = br.readLine();
                                while (line != null) {
                                    sb.append(line);
                                    sb.append(System.lineSeparator());
                                    line = br.readLine();
                                }
                                String everything = sb.toString();
                                //System.out.println(everything);

                                for (int o = 1; o < everything.split("\n+").length; o++) {
                                    String sentence = everything.split("\n+")[o];
                                    if (sentence.charAt(sentence.length()-1)==' ') {
                                        sentences.add(sentence.substring(0, sentence.length() - 2));
                                    }else {
                                        sentences.add(sentence);
                                    }
                                    //br.close();
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            /* Storing the sentences*/
            FileOutputStream fos = new FileOutputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/sentencesList.ser");
            ObjectOutputStream s = new ObjectOutputStream(fos);
            s.writeObject(sentences);
            s.flush();

            // 3. APPLY THE TRAVERSAL OF THE AUTOMATON ON THE LIST OF SENTENCES

            System.out.println("Number of sentences: "+sentences.size()); // 1747 sentences
            String test = "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29." ;//; // "; //abat achor king // //The meters. is go //The length is 5.2 meters... Let's go!

            /*
            System.out.println("******************Printing the Token Automaton**********************");
            System.out.println(tokFSA.toString());
            System.out.println("**************************************************************");
            System.out.println("******************Printing the Sep Automaton**********************");
            System.out.println(sepFSA.toString());
            System.out.println("**************************************************************");
            */

            ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>> charts = tokFSA.traverseExtendedSolution(tokFSA, sepFSA, specifications, test);


            System.out.println("=====================Printing Chart================="+charts.size());
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

            /*

            for(int i=charts.get(0).size()-1;i>=0;i--){
                //for(int j=0;j<charts.get(0).get(i).size();j++){
                    System.out.println(charts.get(0).get(i));
                //}
            }
            */


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
