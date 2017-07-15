package SyntacticAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 *  A projective, rule-based, dependency parser.  A ProjectiveDependencyParser
 *  is created with a DependencyGrammar, a set of productions specifying
 *  word-to-word dependency relations.  The parse() method will then
 *  return the set of all parses, in tree representation, for a given input
 *  sequence of tokens.  Each parse must meet the requirements of the both
 *  the grammar and the projectivity constraint which specifies that the
 *  branches of the dependency tree are not allowed to cross.  Alternatively,
 *  this can be understood as stating that each parent node and its children
 *  in the parse tree form a continuous substring of the input sequence.
 *
 * @author MeryemMhamdi
 * @date 5/9/17
 */
public class ProjectiveDependencyParser {
    ArrayList<ArrayList<String>> tokensList; // INPUT
    /* The 2 dimensional table for the CYK algorithm */
    private static ArrayList<Set<DependencySpan>[][]> cykCharts = new ArrayList<Set<DependencySpan>[][]>();

    private DependencyGrammar grammar;

    private ArrayList<String> labels;

    static private String grammarFile = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/PipelineNLP/src/SyntacticAnalyzer/dependencyGrammar.txt";

    /*
    public ProjectiveDependencyParser(ArrayList<ArrayList<ArrayList<String>>> tokenizationCharts){
        grammar = new DependencyGrammar(grammarFile);
        initializeTokensList(tokenizationCharts);
        for (int i=0; i<tokensList.size();i++){
            Set<DependencySpan>[][] cykChart  = parse(tokensList.get(i));
            cykCharts.add(cykChart);
        }

    }*/

    public ProjectiveDependencyParser(ArrayList<String>  tokens){
        grammar = new DependencyGrammar(grammarFile);
        labels = grammar.getLabels();
        Set<DependencySpan>[][] cykChart  = parse(tokens);
        cykCharts.add(cykChart);
    }


    public void initializeTokensList(ArrayList<ArrayList<ArrayList<String>>> tokenizationCharts) {
        for (int i=0;i<tokenizationCharts.size();i++){
            // Iterate over the tokenization chart for each sentence
            for (int j=0;j<tokenizationCharts.get(i).size();j++){
                ArrayList<String> tokens = new ArrayList<String>();
                for (int k=0;k<tokenizationCharts.get(i).get(j).size();k++){
                    tokens.add(tokenizationCharts.get(i).get(j).get(k));
                }
                tokensList.add(tokens);
            }
        }
    }
    /**
     * Performs a projective dependency parse on the list of tokens using
     a chart-based, span-concatenation algorithm similar to Eisner (1996).

     */
    public Set<DependencySpan>[][] parse(ArrayList<String> tokens){
        Set<DependencySpan> [][] cykChart = new HashSet[tokens.size()+1][tokens.size()+1];
        int [] arcs = {-1};
        String [] tags = {"ROOT"};
        for (int i=0;i<tokens.size()+1;i++){
            for (int j=0;j<tokens.size()+1;j++){
                cykChart[i][j] = new HashSet();
                if (i == j + 1){
                    System.out.println("INITIALIZING CYKCHART i= "+i+" j= "+j);
                    cykChart[i][j].add(new DependencySpan(i-1,i,i-1,arcs,tags));
                }
            }

        }

        for (int i=1; i<tokens.size()+1;i++){
            for (int j=i-2;j>-1;j--){
                for (int k=i-1;k>j;k--){
                    System.out.println("i= "+i+" j= "+j+" k= "+k+" cykChart[k][j]= "+cykChart[k][j]+" cykChart[i][k]= "+cykChart[i][k]);
                    for (DependencySpan span1 : cykChart[k][j]){
                        for (DependencySpan span2 : cykChart[i][k]){
                            for (DependencySpan newSpan: this.concatenate(span1,span2,tokens)){
                                cykChart[i][j].add(newSpan);
                            }

                        }
                    }
                }
            }
        }
        cykCharts.add(cykChart);
        return cykChart;

    }

    /**
     * Concatenates the two spans in whichever way possible.  This
     includes rightward concatenation (from the leftmost word of the
     leftmost span to the rightmost word of the rightmost span) and
     leftward concatenation (vice-versa) between adjacent spans.  Unlike
     Eisner's presentation of span concatenation, these spans do not
     share or pivot on a particular word/word-index.

     * @param span1
     * @param span2
     * @return A set of new spans formed through concatenation.
     */
    public Set<DependencySpan> concatenate (DependencySpan span1, DependencySpan span2, ArrayList<String> tokens){
        Set<DependencySpan> spans = new HashSet<DependencySpan>();
        DependencySpan tempSpan;
        if (span1.getStartIndex() == span2.getStartIndex()){
            System.out.println("Error: Mismatched spans - replace this with thrown error");
        }
        if (span1.getStartIndex() > span2.getStartIndex()){
            tempSpan = span1;
            span1 = span2;
            span2 = tempSpan;
        }
        // Adjacent rightward covered concatenation
        int[] newArcs =  new int [span1.getArcs().length+span2.getArcs().length];
        for (int i=0;i<span1.getArcs().length;i++){
            newArcs[i] = span1.getArcs()[i];
        }
        for (int i=span1.getArcs().length;i<span1.getArcs().length+span2.getArcs().length;i++){
            newArcs[i] = span2.getArcs()[i-span1.getArcs().length];
        }

        String[] newTags =  new String [span1.getTags().length+span2.getTags().length];
        for (int i=0;i<span1.getTags().length;i++){
            newTags[i] = span1.getTags()[i];
        }
        for (int i=span1.getTags().length;i<span1.getTags().length+span2.getTags().length;i++){
            newTags[i] = span2.getTags()[i-span1.getTags().length];
        }

        String head = tokens.get(span1.getHeadIndex());

        String dependent = tokens.get(span2.getHeadIndex());

        System.out.println("Performing rightward/leftward cover with head= "+head+" dependent= "+dependent);

        if (grammar.containsElement(head,dependent)!=-1){
            System.out.println("RIGHTWARD HERE >>>"+labels.get(grammar.containsElement(head,dependent)));
            // Performing rightward cover from head of span 1 to head of span 2
            newArcs[span2.getHeadIndex()-span1.getStartIndex()] = span1.getHeadIndex();
            newTags[span2.getHeadIndex()-span1.getStartIndex()] = labels.get(grammar.containsElement(head,dependent));
            spans.add(new DependencySpan(span1.getStartIndex(),span2.getEndIndex(),span1.getHeadIndex(),newArcs,newTags));
        }
        // Performing leftward covered concatenation from head of span 2 to head of span 1
        newArcs =  new int [span1.getArcs().length+span2.getArcs().length];
        for (int i=0;i<span1.getArcs().length;i++){
            newArcs[i] = span1.getArcs()[i];
        }
        for (int i=span1.getArcs().length;i<span1.getArcs().length+span2.getArcs().length;i++){
            newArcs[i] = span2.getArcs()[i-span1.getArcs().length];
        }

        head = tokens.get(span2.getHeadIndex());

        dependent = tokens.get(span1.getHeadIndex());

        if (grammar.containsElement(head,dependent)!=-1){
            System.out.println("LEFTWARD HERE >>>"+labels.get(grammar.containsElement(head,dependent)));
            newArcs[span1.getHeadIndex()-span1.getStartIndex()] = span2.getHeadIndex();
            newTags[span1.getHeadIndex()-span1.getStartIndex()] = labels.get(grammar.containsElement(head,dependent));
            spans.add(new DependencySpan(span1.getStartIndex(),span2.getEndIndex(),span2.getHeadIndex(),newArcs,newTags));
        }


        return spans;
    }

    public static void main (String args[]){

        ArrayList<String> tokens = new ArrayList<String>();
        tokens.add("the");
        tokens.add("price");
        tokens.add("of");
        tokens.add("the");
        tokens.add("stock");
        tokens.add("fell");

        ProjectiveDependencyParser projectiveDependencyParser = new ProjectiveDependencyParser(tokens);

        Set<DependencySpan> [][] parsingChart = cykCharts.get(0);

        //System.out.println(parsingChart[tokens.size()-1][0]);

        System.out.println("***************************************");

        System.out.println(parsingChart[tokens.size()][0]);

        System.out.println("*************PRINTING TREES IN CONLL FORMAT*********************");
        for (DependencySpan parse: parsingChart[tokens.size()][0]){
            String conll_format = "";
            for (int i=0;i<tokens.size();i++){
                int position = i + 1;
                conll_format = conll_format + "(" +String.valueOf(position) + "," + tokens.get(i) + ", null, null, null, "+String.valueOf(parse.getArcs()[i]+1)+ ","+String.valueOf(parse.getTags()[i])+", -, -) \n";
            }
            System.out.println(conll_format);
        }

        System.out.println("****************PRINTING TREES IN CONLL FORMAT***********************");

        System.out.println("*************PRINTING THE TREES*********************");
        Graph graph  = new Graph();
        for (DependencySpan parse: parsingChart[tokens.size()][0]) {
            String conll_format = "";
            for (int i = 0; i < tokens.size(); i++) {
                Node dependent = new Node(i,tokens.get(i));
                Node head ;
                if (parse.getArcs()[i]>-1) {
                    head = new Node(i, tokens.get(parse.getArcs()[i]));
                } else {
                    head = new Node(i, "ROOT");
                }
                graph.addNode(dependent);
                graph.addNode(head);
                graph.addEdge(dependent,head);
            }
        }

        graph.printGraph();



        /*
        for(int i=parsingChart.length-1;i>=0;i--){
            String level = "[";
            for(int j=0;j<parsingChart[i].length;j++){
                if (j<parsingChart[i].length-1) {
                    level = level + parsingChart[i][j] + ",";
                }else{
                    level = level + parsingChart[i][j] + "]";
                }
            }
            System.out.println(level);
        }
        */

    }

}
