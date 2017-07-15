package LexicalAnalyzer.Tokenizer;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.BasicOperations;
import LexicalAnalyzer.FSA.SepSpecification;
import SyntacticAnalyzer.DependencyConLL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MeryemMhamdi on 6/7/17.
 */
public class TokenizeTreebank {
    private static String PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Train/";
    private static String PATH_INPUT_TOK_FSA = PATH_FOLDER+"UDCtokFSA.ser";
    private static String PATH_INPUT_SEP_FSA = PATH_FOLDER+"UDCsepFSA.ser";
    private static String PATH_OUTPUT_SENTENCES_STREAM = PATH_FOLDER + "noChartsSentences.ser";

    private static String PATH_OUTPUT_CHARTS_TXT = PATH_FOLDER+"udc_tokenizationCharts_CORRECTION_NO_CHARTS.txt";
    private static String PATH_OUTPUT_CHARTS_STREAM = PATH_FOLDER+"udc_tokenizationCharts_CORRECTION_NO_CHARTS.ser";

    public static void main(String[] args) {
        try {
            // 1.1. LOAD THE ALREADY FILLED LEXICON FROM THE FILE
            FileInputStream in = new FileInputStream(PATH_INPUT_TOK_FSA);
            Automaton automaton = new Automaton();
            Automaton tokFSA = automaton.load(in);


            // 1.2. Define Separator FSA

            FileInputStream in1 = new FileInputStream(PATH_INPUT_SEP_FSA);
            Automaton automaton1 = new Automaton();
            Automaton sepFSA = automaton1.load(in1);

            HashMap<String,SepSpecification> specifications = new HashMap<String,SepSpecification>();

            specifications.put("_",new SepSpecification(true,false));
            specifications.put("-",new SepSpecification(true,false));
            specifications.put("<",new SepSpecification(true,false));
            specifications.put(">",new SepSpecification(true,false));
            specifications.put("…",new SepSpecification(true,true));  // TRUE
            specifications.put(" ",new SepSpecification(false,false));
            specifications.put("‘",new SepSpecification(true,false));
            specifications.put("’",new SepSpecification(true,false));
            specifications.put("“",new SepSpecification(true,false));
            specifications.put("”",new SepSpecification(true,false));
            specifications.put("”",new SepSpecification(true,false));
            specifications.put("?",new SepSpecification(true,true)); // TRUE
            specifications.put("!",new SepSpecification(true,true)); // TRUE
            specifications.put(":",new SepSpecification(true,false));
            specifications.put(",",new SepSpecification(true,false));
            specifications.put("'",new SepSpecification(true,false));
            specifications.put("\"",new SepSpecification(true,false));
            specifications.put("`",new SepSpecification(true,false));
            specifications.put("`",new SepSpecification(true,false));
            specifications.put(".",new SepSpecification(true,true)); // TRUE
            specifications.put("‘",new SepSpecification(true,false));
            specifications.put(")",new SepSpecification(true,false));
            specifications.put("/",new SepSpecification(true,false));
            specifications.put(",",new SepSpecification(true,false));
            specifications.put("'",new SepSpecification(true,false));
            specifications.put(";",new SepSpecification(true,true)); // TRUE
            specifications.put("|",new SepSpecification(true,true)); // TRUE
            specifications.put("(",new SepSpecification(true,false));
            specifications.put("{",new SepSpecification(true,false));
            specifications.put("}",new SepSpecification(true,false));
            specifications.put("[",new SepSpecification(true,false));
            specifications.put("]",new SepSpecification(true,true)); // TRUE

            /*
            // 2. LOADING THE SENTENCES
            FileInputStream fin = new FileInputStream(PATH_OUTPUT_SENTENCES_STREAM);
            ObjectInputStream s = new ObjectInputStream(fin);
            ArrayList<String> treebankSentences = (ArrayList<String>) s.readObject();
            */

            // 3. APPLY THE TRAVERSAL OF THE AUTOMATON ON THE LIST OF SENTENCES
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts = new ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>();

            //System.out.println("SIZE=>"+treebankSentences.size());
            String sentence = "Web posted at : 3:29 p.m. EST ( 2029 GMT )";
            for (int i=0;i<1;i++){ //treebankSentences.size()
                //System.out.println("SENTENCE: "+i+"=> "+treebankSentences.get(i));
                ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>> subCharts
                        = tokFSA.traverseShortSolution(tokFSA, sepFSA, specifications,sentence.toString());//treebankSentences.get(i)
                ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>> filteredSubCharts = new ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>();
                for (int l=0;l<subCharts.size();l++){
                    if (subCharts.get(l).get(0).size()>0){
                        filteredSubCharts.add(subCharts.get(l));
                    }
                }
                charts.add(filteredSubCharts);
                for (int k=0;k<filteredSubCharts.size();k++){
                    System.out.println("i= "+i+" TOKENIZED SENTENCE>>>> " +sentence.substring(filteredSubCharts.get(k).get(0).get(0)
                            .getStartIndex(),filteredSubCharts.get(k).get(0).get(filteredSubCharts.get(k).get(0).size()-1).getEndIndex()));//treebankSentences.get(i)
                }

            }

            // 4. STORING IN TEXT FILE
            //BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_OUTPUT_CHARTS_TXT));
            System.out.println("=====================Printing Charts=================");
            for (int k=0;k<charts.size();k++){
                //writer.write("SENTENCE: "+k+"=> "+treebankSentences.get(k)+"\n");//treebankSentences.get(k)+"\n");
                //System.out.println("SENTENCE: "+k+"=> "+treebankSentences.get(k)+"\n");//treebankSentences.get(k));
                for (int o=0;o<charts.get(k).size();o++) {
                    //writer.write("CHART " + o + "=> ");
                    System.out.println("CHART " + o + "=> "+"\n");
                    for (int l = charts.get(k).get(o).size() - 1; l >= 0; l--) {
                        String result = "[";
                        for (int i = 0; i < charts.get(k).get(o).get(l).size(); i++) {
                            String edge = "";
                            if (charts.get(k).get(o).get(l).get(i) == null) {
                                edge = "";
                            } else {
                                if (charts.get(k).get(o).get(l).get(i).getIsKnown()) {
                                    edge = sentence.substring(charts.get(k).get(o).get(l).get(i)
                                            .getStartIndex(), charts.get(k).get(o).get(l).get(i).getEndIndex()); //treebankSentences.get(k)
                                }
                                //treebankSentences.get(k).substring(charts.get(k).get(o).get(l).get(i)
                                // .getStartIndex(),charts.get(k).get(o).get(l).get(i).getEndIndex());
                            }
                            if (i < charts.get(k).get(o).get(l).size() - 1) {
                                result = result + edge + ",";
                            } else {
                                result = result + edge;
                            }
                        }
                        //writer.write(result + "]"+"\n");
                        System.out.println(result + "]");
                    }
                }
            }
            //writer.close();
            System.out.println("==========================================================");

            /*
            // 5. STORING IN STREAM
            FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_CHARTS_STREAM);
            ObjectOutputStream outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(charts);
            outputStream.flush();
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
