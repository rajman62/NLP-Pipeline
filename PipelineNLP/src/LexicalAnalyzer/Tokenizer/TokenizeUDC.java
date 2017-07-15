package LexicalAnalyzer.Tokenizer;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.BasicOperations;
import LexicalAnalyzer.FSA.SepSpecification;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * STEP 5:
 * This class tokenizes UDC raw corpus using tokFSA and sepFSA given as inputs and provides as an output the list of
 * tokenized sentences and tokenization charts as determined by EOS specification in the algorithm defined in BasicOperations.
 * INPUT ===> raw treebank
 *            tokFSA
 *            sepFSA
 * OUTPUT ===> tokenized sentences
 *             tokenization charts
 * Created by MeryemMhamdi on 5/30/17.
 */
public class TokenizeUDC {
    private static String PATH_INPUT_TREEBANK = "/Users/MeryemMhamdi/Google Drive/Semester Project/" +
            "3 Implementation & Algorithms/Datasets/UDC/en-ud-train.txt";
    private static String PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Train/";
    private static String PATH_INPUT_TOK_FSA = PATH_FOLDER+"UDCtokFSA.ser";
    private static String PATH_INPUT_SEP_FSA = PATH_FOLDER+"UDCsepFSA.ser";
    private static String PATH_OUTPUT_SENTENCES = PATH_FOLDER+"udc_sentencesList.ser";
    private static String PATH_OUTPUT_CHARTS = PATH_FOLDER+"udc_tokenizationCharts.ser";
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


            String [] tests = {"..."};
            for (int i=0;i<tests.length;i++) {
                if (sepFSA.run(tests[i])) {
                    System.out.println("url FOUND");
                } else {
                    System.out.println("url NOT FOUND");
                }
            }



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

            // 2. READ FROM DOCUMENTS IN THE FOLDER OF PENN TREEBANK
            ArrayList<String> sentences = new ArrayList<String>();

            BufferedReader br = new BufferedReader(new FileReader(PATH_INPUT_TREEBANK));
            String line = br.readLine();
            while (line != null) {
                sentences.add(line);
                line = br.readLine();
            }

            /* Storing the sentences*/
            FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_SENTENCES);
            ObjectOutputStream s = new ObjectOutputStream(fos);
            s.writeObject(sentences);
            s.flush();

            // 3. APPLY THE TRAVERSAL OF THE AUTOMATON ON THE LIST OF SENTENCES
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts = new ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>();

            System.out.println("SENTENCES SIZE:"+sentences.size());

            int start = 0;
            int end = sentences.size();
            //String sentence = "*** Bush also nominated A. Noel Anketell Kramer for a 15-year term as associate judge of the District of Columbia Court of Appeals, replacing John Montague Steadman.";
                    //" *** Bush also nominated A. Noel Anketell Kramer for a 15-year term as associate " +
                    //"judge of the District of Columbia Court of Appeals, replacing John Montague Steadman.";
            for (int i=start;i<end;i++) { //sentences.size()
                //if (i%100==0){
                System.out.println("\nSENTENCE TO BE TOKENIZED>>> "+sentences.get(i));
                //}
                ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>> subCharts = tokFSA.traverseExtendedSolution(tokFSA, sepFSA, specifications,sentences.get(i).toString());
                ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>> filteredSubCharts = new ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>();
                for (int l=0;l<subCharts.size();l++){
                    if (subCharts.get(l).get(0).size()>0){
                        filteredSubCharts.add(subCharts.get(l));
                    }
                }
                charts.add(filteredSubCharts);
                System.out.println("NUMBER OF SUBCHARTS==>"+filteredSubCharts.size());
                if (true) { //subCharts.size()==1

                    for (int k=0;k<filteredSubCharts.size();k++){
                        System.out.println("CHART "+k+"=> ");
                        for (int l=filteredSubCharts.get(k).size()-1;l>=0;l--){
                            String result = "[";
                            for (int o=0;o<filteredSubCharts.get(k).get(l).size();o++) {
                                String edge;
                                if (filteredSubCharts.get(k).get(l).get(o) == null) {
                                    edge = "";
                                } else {
                                    edge = filteredSubCharts.get(k).get(l).get(o).toString();
                                }
                                if (o < filteredSubCharts.get(k).get(l).size()-1 ) {
                                    result = result + edge+",";
                                } else {
                                    result = result + edge;
                                }
                            }
                            System.out.println(result+"]");
                        }
                    }
                    System.out.println("==========================================================");
                }
                for (int k=0;k<filteredSubCharts.size();k++){
                    System.out.println("i= "+i+" TOKENIZED SENTENCE>>>> "
                            +sentences.get(i).substring(filteredSubCharts.get(k).get(0).get(0).getStartIndex(),filteredSubCharts.get(k).get(0).get(filteredSubCharts.get(k).get(0).size()-1).getEndIndex()));
                }

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

            fos = new FileOutputStream(PATH_OUTPUT_CHARTS);
            s = new ObjectOutputStream(fos);
            s.writeObject(charts);
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
