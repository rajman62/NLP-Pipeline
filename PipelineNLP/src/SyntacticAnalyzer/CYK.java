package SyntacticAnalyzer;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.BasicOperations;

import javax.lang.model.type.ArrayType;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by MeryemMhamdi on 4/19/17.
 */
public class CYK {

    /**
     * variables are in the form of (0 U 1)+
     * They are stored in the HashMap as (0 U 1)+ maps { (0 U 1)+, (0 U 1)+ }
     */
    private HashMap<NonTerminal, ArrayList<ArrayList<NonTerminal>>> variables;


    /**
     * HashMap of the pos tags along with all possible deprels
     */
    private Map<String,ArrayList<String>> tagsDeprels;

    private HashMap<String,String> EMORtoPenntags;

    private HashMap<String,Integer> wordCount;

    private static String INPUT_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Morphological Analysis/UDC/Train/";

    private static String OUTPUT_PATH_FOLDER1 = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Syntactic Analysis/UDC/Train/";

    private static String PATH_INFO_CHARTS = INPUT_PATH_FOLDER+ "true_Treebank_MorphologyResults.ser";
    private static String PATH_DISTINCT_TAGS = OUTPUT_PATH_FOLDER1 +"DistinctTags.txt";

    /**
     * GRAMMAR AND ONE SIDED RULES
     */
    private static String SYNTAX_INPUT_PATH_FOLDER = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/Big Data/";
    private static String PATH_INPUT_GRAMMAR = SYNTAX_INPUT_PATH_FOLDER+"Dependency_Grammar_TRAIN_ROOT.ser";
    private static  String PATH_INPUT_ONE_SIDED_RULES = SYNTAX_INPUT_PATH_FOLDER+"One_sided_rules_TRAIN_ROOT.ser";
    private static String PATH_LIST_PROJECTIVE_TRAIN = SYNTAX_INPUT_PATH_FOLDER+ "projective_indices_train.ser";

    /**
     * OUTPUT FILES
     */
    private static String OUTPUT_DEPENDENCIES_STREAM = SYNTAX_INPUT_PATH_FOLDER +"udc_dependenciesCYK1stPass_TRAIN_ROOT_1.ser";
    private static String OUTPUT_CYK_CHARTS_STREAM = SYNTAX_INPUT_PATH_FOLDER +"udc_parsingCharts1stPass_TRAIN_ROOT_1.ser";

    private static String DATASET_LOCATION = "/Users/MeryemMhamdi/Google Drive/Semester Project" +
            "/3 Implementation & Algorithms/Datasets/UDC/";

    private static String PATH_TRAIN_DEPENDENCIES =  DATASET_LOCATION + "parsedDependenciesConLL_train_true.conllu";

    private static String PATH_TIMES =  "/Users/MeryemMhamdi/Desktop/Times.txt";

    private static String PATH_LENGTHS =  "/Users/MeryemMhamdi/Desktop/Lengths.txt";





    /**
     * Constructs a Cyk object and initializes the HashMaps of the variables
     * and the terminals
     */
    public CYK() {
        variables = new HashMap<NonTerminal, ArrayList<ArrayList<NonTerminal>>>();
        tagsDeprels = new HashMap<String, ArrayList<String>>();
        EMORtoPenntags = new  HashMap<String,String>();
        wordCount = new HashMap<String,Integer> ();
    }

    /**
     * Processes the grammar file and builds the HashMap of the list of terminals
     * and variables. Uses the Scanner object to read the grammar file.
     * @param file the string representing the path of the grammar file
     */
    public void processGrammarFile(String file) {
        ArrayList<Variable> variablesList = new ArrayList<Variable>();
        try {
            File grammarFile = new File(file);
            Scanner scanner = new Scanner(grammarFile);

            String[] line = scanner.nextLine().split("->");
            do
            {
                String[] variable = line[0].split(":");
                String[] rest = line[1].split(" ");
                if (rest != null) {
                    ArrayList<NonTerminal> nonTerminals = new ArrayList<NonTerminal>();
                    if (rest.length == 1) {
                        String [] composition = rest[0].split(":");
                        NonTerminal rightHandSide = new NonTerminal(-1,"",composition[0],composition[1],-1,-1,-1);
                        nonTerminals.add(rightHandSide);
                    } else {
                        String [] composition0 = rest[0].split(":");
                        NonTerminal rightHandSide0 = new NonTerminal(-1,"",composition0[0],composition0[1],-1,-1,-1);

                        String [] composition1 = rest[1].split(":");
                        NonTerminal rightHandSide1 = new NonTerminal(-1,"",composition1[0],composition1[1],-1,-1,-1);
                        nonTerminals.add(rightHandSide0);
                        nonTerminals.add(rightHandSide1);
                    }
                    variablesList.add(new Variable(new NonTerminal(-1,"",variable[0],variable[1],-1,-1,-1), nonTerminals));
                }
                if (scanner.hasNextLine())
                    line = scanner.nextLine().split("->");
                else
                    line = null;
            } while (line != null);
            scanner.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        //System.out.println("variablesList: "+variablesList);

        /**
         * Converting the lists into HashMaps
         */
        for(int i=0;i<variablesList.size();i++){
            if(variables.containsKey(variablesList.get(i).getNonTerminal())){
                ArrayList<ArrayList<NonTerminal>> values = variables.get(variablesList.get(i).getNonTerminal());
                values.add(variablesList.get(i).getVariables());
                variables.put(variablesList.get(i).getNonTerminal(),values);
            }else{
                ArrayList<ArrayList<NonTerminal>> values = new ArrayList<ArrayList<NonTerminal>>();
                values.add(variablesList.get(i).getVariables());
                variables.put(variablesList.get(i).getNonTerminal(),values);
            }
        }
        //System.out.println("variables: "+variables);
    }

    /**
     * Processes the one sided rules file and extends all xpostags in the info chart with all possible dependencies at the bottom chart
     */
    public void processOneSidedRules(String file) {

        File oneSidedRulesFile = null;
        Scanner scanner = null;
        try
        {
            oneSidedRulesFile = new File(file);
            scanner = new Scanner(oneSidedRulesFile);

            // Read the one sided rules
            String scanned = scanner.nextLine();
            String[] line={};
            if (scanned.charAt(0)==':'){
                line[0] = ":";
                line[1] = scanned.substring(2,scanned.length());
            } else {
                line = scanned.split(":");
            }
            do {
                String [] deps = line[1].split(",");
                if (deps == null){
                    ArrayList<String> deprels = new ArrayList<String>();
                    deprels.add(line[1]);
                    tagsDeprels.put(line[0],deprels);
                } else {
                    ArrayList<String> deprels = new ArrayList<String>();
                    for (int i=0;i<deps.length;i++){
                        deprels.add(deps[i]);
                    }
                    tagsDeprels.put(line[0],deprels);
                }
                if (scanner.hasNextLine()) {
                    scanned = scanner.nextLine();
                    if (scanned.charAt(0) == ':') {
                        line[0] = ":";
                        line[1] = scanned.substring(2, scanned.length());
                    } else {
                        line = scanned.split(":");
                    }
                }
                else
                    line = null;
            } while (line != null);
            scanner.close();

            for (String key: tagsDeprels.keySet()) {
                String result = key+":";
                for (int j=0;j<tagsDeprels.get(key).size();j++) {
                    result = result + tagsDeprels.get(key).get(j)+ " ";
                }
                //System.out.println(result);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void processEmorToPennTags(String file){

        File oneSidedRulesFile = null;
        Scanner scanner = null;
        try
        {
            oneSidedRulesFile = new File(file);
            scanner = new Scanner(oneSidedRulesFile);

            // Read the one sided rules
            String scanned = scanner.nextLine();
            //System.out.println(scanned);
            String[] line = scanned.split("=");
            do
            {
                String [] emor = line[0].split(" ");
                String emorTag = "";
                for (int i=0;i<emor.length;i++){
                    emorTag = emorTag + "<" + emor[i]+">";
                }
                EMORtoPenntags.put(emorTag,line[1]);
                if (scanner.hasNextLine()){
                    scanned = scanner.nextLine();
                    //System.out.println(scanned);
                    line = scanned.split("=");
                }
                else
                    line = null;
            } while (line != null);
            scanner.close();

            EMORtoPenntags.put("<.>",".");
            EMORtoPenntags.put("<,>",",");
            EMORtoPenntags.put("<:>",":");
            EMORtoPenntags.put("<.>",".");
            /*
            for (String key: EMORtoPenntags.keySet()) {
                System.out.println(key+":"+EMORtoPenntags.get(key));
            }
            */
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Returns the chart that contains all parsing trees
     * @param parsingChart
     * @return
     */


    @SuppressWarnings("unchecked")

    public ArrayList<NonTerminal>[][] buildParsingChart(ArrayList<NonTerminal>[][] parsingChart) {

        //System.out.println("Running CYK====>");
        int length = parsingChart.length;
        //System.out.println("length of parsing Chart= "+length);

        // Applying 2 nonterminals on right hand side rules:
        //System.out.println("length:"+length);
        //System.out.println("Applying 2 nonterminals on right hand side rules");
        for (int i = 1; i < length; i++)
        {
            for (int j = 0; j < length - i ; j++)
            {
                for (int k = 0; k <= i-1 ; k++)
                {
                    //System.out.println("\n\nnext iteration>> i= "+i+" j= "+j+" k= "+k);
                    Set<NonTerminal> nonTerminals = variables.keySet();
                    for (NonTerminal nonTerminal : nonTerminals) {
                        ArrayList<ArrayList<NonTerminal>> vars = variables.get(nonTerminal);
                        for(int var=0;var<vars.size();var++) {
                            ArrayList<NonTerminal> values = variables.get(nonTerminal).get(var);
                            if (values.size()==2) {
                                //System.out.println("\nApplying rule=> " + nonTerminal + "->" + values.get(0).toString() + "," + values.get(1).toString());
                                //System.out.println("parsingChart[i-k][j]:"+parsingChart[i-k-1][j]);
                                //System.out.println("parsingChart[k][i+j-k]:"+parsingChart[k][i+j-k]);
                                //for (int o_right1=0;o_right1< parsingChart[i-k-1][j].size();o_right1++){
                                    //for (int o_right2=0;o_right2< parsingChart[k][i+j-k].size();o_right2++) {
                                        if (parsingChart[i - k - 1][j].contains(values.get(0))
                                                && parsingChart[k][i + j - k].contains(values.get(1))) {
                                            int pointer;
                                            if (j < i + j - k) {
                                                pointer = i - k - 1;
                                            } else {
                                                pointer = k;
                                            }

                                            int right1Index = parsingChart[i - k - 1][j].indexOf(values.get(0));
                                            int right2Index = parsingChart[k][i + j - k].indexOf(values.get(1));
                                            //int right1Index = o_right1;
                                            //int right2Index = o_right2;

                                            String lemma;
                                            double id;
                                            if ((parsingChart[i - k - 1][j].get(right1Index)).equals(nonTerminal)) {
                                                lemma = parsingChart[i - k - 1][j].get(right1Index).getLemma();
                                                id = parsingChart[i - k - 1][j].get(right1Index).getId();
                                            } else {
                                                lemma = parsingChart[k][i + j - k].get(right2Index).getLemma();
                                                id = parsingChart[k][i + j - k].get(right2Index).getId();
                                            }
                                            parsingChart[i][j].add(new NonTerminal(id, lemma, nonTerminal.getXpostag(), nonTerminal.getDeprel()
                                                    , pointer, right1Index, right2Index));
                                            // System.out.println("YAY RULE GOT REALLY APPLIED");
                                        }
                                    }
                               // }
                            //}
                        }
                    }
                }
            }
        }

        // Applying 1 nonterminal on right hand side rules:
        //for (int i=0;i<length;i++) {
            //for (int k = 0; k < length; k++) {
        Set<NonTerminal> nonTerminals = variables.keySet();
        for (NonTerminal nonTerminal : nonTerminals) {
            ArrayList<ArrayList<NonTerminal>> vars = variables.get(nonTerminal);
            for(int var=0;var<vars.size();var++) {
                ArrayList<NonTerminal> values = variables.get(nonTerminal).get(var);
                if (values.size()==1) {
                    //for (int o_right1 = 0; o_right1 < parsingChart[length - 1][0].size(); o_right1++) {
                        if (parsingChart[length - 1][0].contains(values.get(0))) {
                            int pointer = length - 1;
                            parsingChart[length - 1][0].add(new NonTerminal(parsingChart[length - 1][0].get(0).getId(),
                                    "root", nonTerminal.getXpostag(),
                                    nonTerminal.getDeprel(), pointer, parsingChart[pointer][0].indexOf(values.get(0)), -1));//
                        }
                   // }
                }
            }
        }
       // }
    //}
        return parsingChart;
    }
    public ArrayList<NonTerminal>[][] buildParsingChart1(ArrayList<NonTerminal>[][] parsingChart) {

        System.out.println("Running CYK====>");
        int length = parsingChart.length;
        System.out.println("length of parsing Chart= "+length);

        /*
        // Applying 1 nonterminal on right hand side rules:
        System.out.println("Applying 1 nonterminal on right hand side rules");
        for (int k=0;k<length;k++) {
            for (int i=0;i<length;i++) {
                Set<NonTerminal> nonTerminals = variables.keySet();
                for (NonTerminal nonTerminal : nonTerminals) {
                    ArrayList<ArrayList<NonTerminal>> vars = variables.get(nonTerminal);
                    for (int var = 0; var < vars.size(); var++) {
                        ArrayList<NonTerminal> values = variables.get(nonTerminal).get(var);
                        if (values.size() == 1) {
                            if (parsingChart[k][i].contains((values.get(0)))) {

                                parsingChart[k][i].add(new NonTerminal(parsingChart[k][i].get(0).getId()
                                        , parsingChart[k][i].get(0).getLemma(), nonTerminal.getXpostag(), nonTerminal.getDeprel(),
                                        0, parsingChart[k][i].indexOf(values.get(0)), -1));
                                //System.out.println("YAY RULE GOT REALLY APPLIED");
                            }
                        }
                    }
                }
            }
        }
        */
        // Applying 2 nonterminals on right hand side rules:
        //System.out.println("length:"+length);
        System.out.println("Applying 2 nonterminals on right hand side rules");
        for (int i = 1; i < length; i++)
        {
            for (int j = 0; j < length - i ; j++)
            {
                for (int k = 0; k <= i-1 ; k++)
                {
                    //System.out.println("\n\nnext iteration>> i= "+i+" j= "+j+" k= "+k);
                    Set<NonTerminal> nonTerminals = variables.keySet();
                    for (NonTerminal nonTerminal : nonTerminals) {
                        ArrayList<ArrayList<NonTerminal>> vars = variables.get(nonTerminal);
                        for(int var=0;var<vars.size();var++) {
                            ArrayList<NonTerminal> values = variables.get(nonTerminal).get(var);
                            if (values.size()==2) {
                                //System.out.println("\nApplying rule=> " + nonTerminal + "->" + values.get(0).toString() + "," + values.get(1).toString());
                                //System.out.println("parsingChart[i-k][j]:"+parsingChart[i-k-1][j]);
                                //System.out.println("parsingChart[k][i+j-k]:"+parsingChart[k][i+j-k]);
                                if (parsingChart[i-k-1][j].contains((values.get(0)))
                                        && parsingChart[k][i+j-k].contains(values.get(1))){
                                    int pointer;
                                    if (j<i+j-k){
                                        pointer = i - k - 1;
                                    } else {
                                        pointer = k;
                                    }

                                    int right1Index = parsingChart[i-k-1][j].indexOf(values.get(0));
                                    int right2Index = parsingChart[k][i+j-k].indexOf(values.get(1));

                                    String lemma;
                                    double id;
                                    if ((parsingChart[i-k-1][j].get(right1Index)).equals(nonTerminal)){
                                        lemma = parsingChart[i-k-1][j].get(right1Index).getLemma();
                                        id = parsingChart[i-k-1][j].get(right1Index).getId();
                                    } else {
                                        lemma = parsingChart[k][i+j-k].get(right2Index).getLemma();
                                        id =  parsingChart[k][i+j-k].get(right2Index).getId();
                                    }
                                    parsingChart[i][j].add(new NonTerminal(id,lemma,nonTerminal.getXpostag(),nonTerminal.getDeprel()
                                            ,pointer,right1Index,right2Index));
                                   // System.out.println("YAY RULE GOT REALLY APPLIED");
                                }
                            }
                        }
                    }
                }
            }
        }

        return parsingChart;
    }

    public void printChart(ArrayList<NonTerminal>[][] parsingChart){
        for (int i = parsingChart.length - 1; i >= 0; i--) {
            String level = "[";
            for (int j = 0; j < parsingChart[i].length; j++) {
                String result = "";
                for (int k=0;k<parsingChart[i][j].size();k++){
                    if (k == parsingChart[i][j].size()-1) {
                        result = result + parsingChart[i][j].get(k).getLemma() + ":" +parsingChart[i][j].get(k).getXpostag()
                                + ":" + parsingChart[i][j].get(k).getDeprel() + ":" + parsingChart[i][j].get(k).getPointer()
                                + ":" + parsingChart[i][j].get(k).getRight1Index() + ":" + parsingChart[i][j].get(k).getRight2Index() ;
                    } else {
                        result = result + parsingChart[i][j].get(k).getLemma() + ":" + parsingChart[i][j].get(k).getXpostag()
                                + ":" + parsingChart[i][j].get(k).getDeprel() + ":" + parsingChart[i][j].get(k).getPointer()
                                + ":" + parsingChart[i][j].get(k).getRight1Index() + ":" + parsingChart[i][j].get(k).getRight2Index() + "|";
                    }
                }
                if (j < parsingChart[i].length - 1) {

                    level = level + result + ",";
                } else {
                    level = level + result + "]";
                }
            }
            System.out.println(level);
        }

    }


    public ArrayList<SmallConLL> constructDependenciesPairs(ArrayList<NonTerminal>[][] parsingChart){
        ArrayList<SmallConLL> dependencies = new ArrayList<SmallConLL>();
        for (int i = parsingChart.length - 1; i >= 0; i--) {
            for (int j = 0; j < parsingChart[i].length-1; j++) {
                for (int k=0;k<parsingChart[i][j].size();k++){
                    int i_right_1 = parsingChart[i][j].get(k).getPointer();
                    int right1Index = parsingChart[i][j].get(k).getRight1Index();
                    if (i_right_1 != -1) {
                        int i_right_2 = i - parsingChart[i][j].get(k).getPointer() - 1;
                        int j_right_2 = j + parsingChart[i][j].get(k).getPointer() + 1;
                        int right2Index = parsingChart[i][j].get(k).getRight2Index();

                        //System.out.println("i= " + i + " j= " + j + " k= " + k + " i_right_1= " + i_right_1 + " i_right_2= " + i_right_2 + " j_right_2= " + j_right_2);
                        String head = parsingChart[i][j].get(k).getLemma();
                        String posHead = parsingChart[i][j].get(k).getXpostag();
                        String dep, posDep, rel;
                        if (parsingChart[i_right_1][j].get(right1Index).getLemma().equals(parsingChart[i][j].get(k).getLemma()) &&
                                parsingChart[i_right_1][j].get(right1Index).getXpostag().equals(parsingChart[i][j].get(k).getXpostag())) {
                            dep = parsingChart[i_right_2][j_right_2].get(right2Index).getLemma();
                            posDep = parsingChart[i_right_2][j_right_2].get(right2Index).getXpostag();
                            rel = parsingChart[i_right_2][j_right_2].get(right2Index).getDeprel();
                            dependencies.add(new SmallConLL(head, posHead, dep, posDep, rel));

                        } else if (parsingChart[i_right_2][j_right_2].get(right2Index).getLemma().equals(parsingChart[i][j].get(k).getLemma()) &&
                                parsingChart[i_right_2][j_right_2].get(right2Index).getXpostag().equals(parsingChart[i][j].get(k).getXpostag())){
                            dep = parsingChart[i_right_1][j].get(right1Index).getLemma();
                            posDep = parsingChart[i_right_1][j].get(right1Index).getXpostag();
                            rel = parsingChart[i_right_1][j].get(right1Index).getDeprel();
                            dependencies.add(new SmallConLL(head, posHead, dep, posDep, rel));
                        }

                    }

                }

            }
        }
        return dependencies;

    }


    public boolean checkExistenceofTrueChart (ArrayList<NonTerminal> [][] parsingChart, ArrayList<DependencyConLL> parsedDependenciesConLL) {

        boolean exists = false;
        ArrayList<Double> visitedDependencies = new ArrayList<Double>(); // list of list of dependencyConLLs for all sentences
        /**
         * Traversing the chart
         */
        System.out.println("parsingChart.length= "+parsingChart.length);
        for (int i = parsingChart.length - 1; i >= 0; i--) {
            for (int j = 0; j <= parsingChart[i].length - 1; j++) {
                //System.out.println("SIZE is=>"+parsingChart[i][j].size());
                for (int k = 0; k < parsingChart[i][j].size(); k++) {
                    // For each element in the cell in the chart, traverse to check if one of the paths leads to a dependency that exists
                    int i_right_1 = parsingChart[i][j].get(k).getPointer();
                    int i_right_2 = i - parsingChart[i][j].get(k).getPointer() - 1;
                    int right1Index = parsingChart[i][j].get(k).getRight1Index();
                    if (parsingChart[i][j].get(k).getLemma().equals("this")){
                        System.out.println(parsingChart[i][j].get(k));
                    }
                    if (i_right_2 == -1) {
                        //System.out.println("root");
                        String rel = parsingChart[i][j].get(k).getDeprel();
                        if (parsingChart[i_right_1][j].get(k).getLemma().equals(parsingChart[i][j].get(k).getLemma())){

                            String dep = parsingChart[i_right_1][j].get(right1Index).getLemma();
                            String posDep = parsingChart[i_right_1][j].get(right1Index).getXpostag();
                            double id = parsingChart[i][j].get(right1Index).getId();

                            if (rel.equals("root")) {
                                DependencyConLL dependencyConLL = new DependencyConLL(id, "", dep, "", posDep, "", 0, rel, "", "");
                                if (parsedDependenciesConLL.contains(dependencyConLL)) {
                                    // If one of them exists pop from the list of dependencies, set the one the pointers to the next
                                    //System.out.println("ROOT REMOVED DEPENDENCY>>> "+dependencyConLL.getId());
                                    if (!visitedDependencies.contains(dependencyConLL.getId())){
                                        visitedDependencies.add(dependencyConLL.getId());
                                    }
                                    //parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                    //k = parsingChart[i][j].size();
                                }
                            }
                        }

                    } else {
                        if (i_right_1 != -1) {

                            int j_right_2 = j + parsingChart[i][j].get(k).getPointer() + 1;
                            int right2Index = parsingChart[i][j].get(k).getRight2Index();

                            String head = parsingChart[i][j].get(k).getLemma();
                            String posHead = parsingChart[i][j].get(k).getXpostag();
                            double headId = parsingChart[i][j].get(k).getId();
                            String dep, posDep, rel;
                            double id;

                            /*
                            if (parsingChart[i][j].get(k).getLemma().equals("let") &&parsingChart[i][j].get(k).getXpostag().equals("VB")
                                    &&parsingChart[i][j].get(k).getDeprel().equals("*") && parsingChart[i][j].get(k).getPointer()==0) {
                                System.out.println(parsingChart[i_right_2][j].get(right2Index).getLemma());
                            }
                            */

                            if (parsingChart[i_right_1][j].get(right1Index).getLemma().equals(parsingChart[i][j].get(k).getLemma()) &&
                                    parsingChart[i_right_1][j].get(right1Index).getXpostag().equals(parsingChart[i][j].get(k).getXpostag())) {
                                dep = parsingChart[i_right_2][j_right_2].get(right2Index).getLemma();
                                posDep = parsingChart[i_right_2][j_right_2].get(right2Index).getXpostag();
                                rel = parsingChart[i_right_2][j_right_2].get(right2Index).getDeprel();
                                id = parsingChart[i_right_2][j_right_2].get(right2Index).getId();
                                if (!rel.equals("*")) {
                                    DependencyConLL dependencyConLL = new DependencyConLL(id, "", dep, "", posDep, "", headId, rel, "", "");
                                    /*if (dep.equals("I")){
                                        System.out.println(id);
                                    }*/
                                    if (parsedDependenciesConLL.contains(dependencyConLL)) {
                                        if (dep.equals("I")){
                                            System.out.println("REALLY HERE");
                                        }
                                        // If one of them exists pop from the list of dependencies, set the one the pointers to the next
                                        //System.out.println("REMOVED DEPENDENCY>>> "+dependencyConLL.getId()+" head= "+parsingChart[i][j].get(k).getId());
                                        //parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                        //k = parsingChart[i][j].size();
                                        if (!visitedDependencies.contains(dependencyConLL.getId())){
                                            visitedDependencies.add(dependencyConLL.getId());
                                        }
                                    }
                                }

                            } else if (parsingChart[i_right_2][j_right_2].get(right2Index).getLemma().equals(parsingChart[i][j].get(k).getLemma()) &&
                                    parsingChart[i_right_2][j_right_2].get(right2Index).getXpostag().equals(parsingChart[i][j].get(k).getXpostag())) {
                                dep = parsingChart[i_right_1][j].get(right1Index).getLemma();
                                posDep = parsingChart[i_right_1][j].get(right1Index).getXpostag();
                                rel = parsingChart[i_right_1][j].get(right1Index).getDeprel();
                                id = parsingChart[i_right_1][j].get(right1Index).getId();
                                if (dep.equals("he") && rel.equals("nmod:poss")){
                                    System.out.println("TRUTHHHHHH="+parsingChart[i_right_1][j].get(right1Index).getLemma());
                                    System.out.println("posDep=> "+posDep);
                                    System.out.println("rel=> "+rel);
                                    System.out.println("head=> "+headId);
                                }
                                if (!rel.equals("*")) {
                                    /*if (dep.equals("I")){
                                        System.out.println(id);
                                    }*/
                                    DependencyConLL dependencyConLL = new DependencyConLL(id, "", dep, "", posDep, "", headId, rel, "", "");
                                    if (parsedDependenciesConLL.contains(dependencyConLL)) {
                                        // If one of them exists pop from the list of dependencies, set the one the pointers to the next
                                        //System.out.println("REMOVED DEPENDENCY>>> "+dependencyConLL.getId()+" head= "+parsingChart[i][j].get(k).getId());
                                        //parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                        //k = parsingChart[i][j].size();
                                        if (!visitedDependencies.contains(dependencyConLL.getId())){
                                            visitedDependencies.add(dependencyConLL.getId());
                                        }
                                    }

                                }
                            }

                            dep = parsingChart[i_right_1][j].get(right1Index).getLemma();
                            posDep = parsingChart[i_right_1][j].get(right1Index).getXpostag();
                            headId = 0;
                            rel = parsingChart[i_right_1][j].get(right1Index).getDeprel();
                            id = parsingChart[i_right_1][j].get(right1Index).getId();
                            if (rel.equals("root")) {
                                /*if (dep.equals("I")){
                                    System.out.println(id);
                                }*/
                                DependencyConLL dependencyConLL = new DependencyConLL(id, "", dep, "", posDep, "", headId, rel, "", "");
                                if (parsedDependenciesConLL.contains(dependencyConLL)) {
                                    // If one of them exists pop from the list of dependencies, set the one the pointers to the next
                                    //System.out.println("REMOVED DEPENDENCY>>> "+dependencyConLL.getId()+" head= "+parsingChart[i][j].get(k).getId());
                                    //parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                    //k = parsingChart[i][j].size();
                                    if (!visitedDependencies.contains(dependencyConLL.getId())){
                                        visitedDependencies.add(dependencyConLL.getId());
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        for (int oo=0;oo<visitedDependencies.size();oo++) {
            System.out.println(visitedDependencies.get(oo));
        }
        System.out.println(visitedDependencies.size());
        System.out.println(parsedDependenciesConLL.size());
        if (Math.abs(parsedDependenciesConLL.size()-visitedDependencies.size())<2) {
            exists = true;
        }
        return exists;
    }
    public boolean checkExistenceofTrueChart1 (ArrayList<NonTerminal> [][] parsingChart, ArrayList<DependencyConLL> parsedDependenciesConLL) {

        boolean exists = false;
        ArrayList<ArrayList<ArrayList<DependencyConLL>>> dependenciesAllList = new ArrayList<ArrayList<ArrayList<DependencyConLL>>>(); // list of list of dependencyConLLs for all sentences
        /**
         * Traversing the chart
         */
        System.out.println("parsingChart.length= "+parsingChart.length);
        for (int i = parsingChart.length - 1; i >= 0; i--) {
            for (int j = 0; j < parsingChart[i].length - 1; j++) {
                for (int k = 0; k < parsingChart[i][j].size(); k++) {
                    // For each element in the cell in the chart, traverse to check if one of the paths leads to a dependency that exists
                    int i_right_1 = parsingChart[i][j].get(k).getPointer();
                    int right1Index = parsingChart[i][j].get(k).getRight1Index();
                    if (i_right_1 != -1) {

                        int i_right_2 = i - parsingChart[i][j].get(k).getPointer() - 1;
                        int j_right_2 = j + parsingChart[i][j].get(k).getPointer() + 1;
                        int right2Index = parsingChart[i][j].get(k).getRight2Index();

                        String head = parsingChart[i][j].get(k).getLemma();
                        String posHead = parsingChart[i][j].get(k).getXpostag();
                        double headId = parsingChart[i][j].get(k).getId();
                        String dep, posDep, rel;
                        double id;
                        if (parsingChart[i_right_1][j].get(right1Index).getLemma().equals(parsingChart[i][j].get(k).getLemma()) &&
                                parsingChart[i_right_1][j].get(right1Index).getXpostag().equals(parsingChart[i][j].get(k).getXpostag())) {
                            dep = parsingChart[i_right_2][j_right_2].get(right2Index).getLemma();
                            posDep = parsingChart[i_right_2][j_right_2].get(right2Index).getXpostag();
                            rel = parsingChart[i_right_2][j_right_2].get(right2Index).getDeprel();
                            id = parsingChart[i_right_2][j_right_2].get(right2Index).getId();
                            if (!rel.equals("*")) {
                                DependencyConLL dependencyConLL = new DependencyConLL(id, "", dep, "", posDep, "", headId, rel, "", "");
                                if (parsedDependenciesConLL.contains(dependencyConLL)) {
                                    // If one of them exists pop from the list of dependencies, set the one the pointers to the next
                                    parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                    k = parsingChart[i][j].size();
                                }
                            }

                        } else if (parsingChart[i_right_2][j_right_2].get(right2Index).getLemma().equals(parsingChart[i][j].get(k).getLemma()) &&
                                parsingChart[i_right_2][j_right_2].get(right2Index).getXpostag().equals(parsingChart[i][j].get(k).getXpostag())) {
                            dep = parsingChart[i_right_1][j].get(right1Index).getLemma();
                            posDep = parsingChart[i_right_1][j].get(right1Index).getXpostag();
                            rel = parsingChart[i_right_1][j].get(right1Index).getDeprel();
                            id = parsingChart[i_right_1][j].get(right1Index).getId();
                            if (!rel.equals("*")) {
                                DependencyConLL dependencyConLL = new DependencyConLL(id, "", dep, "", posDep, "", headId, rel, "", "");
                                if (parsedDependenciesConLL.contains(dependencyConLL)) {
                                    // If one of them exists pop from the list of dependencies, set the one the pointers to the next
                                    parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                    k = parsingChart[i][j].size();
                                }

                            }
                        }

                        dep = parsingChart[i_right_1][j].get(right1Index).getLemma();
                        posDep = parsingChart[i_right_1][j].get(right1Index).getXpostag();
                        headId = 0;
                        rel = parsingChart[i_right_1][j].get(right1Index).getDeprel();
                        id = parsingChart[i_right_1][j].get(right1Index).getId();
                        if (rel.equals("root")) {
                            DependencyConLL dependencyConLL = new DependencyConLL(id, "", dep, "", posDep, "", headId, rel, "", "");
                            if (parsedDependenciesConLL.contains(dependencyConLL)) {
                                // If one of them exists pop from the list of dependencies, set the one the pointers to the next
                                parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                k = parsingChart[i][j].size();
                            }
                        }
                    }
                }
            }

        }
        if (parsedDependenciesConLL.size()==0) {
            exists = true;
        }
       return exists;
    }

    /**
     * Takes a given grammar file as the input and a given string to test
     * against that grammar.
     * @param args the list of the given command-line arguments consisting
     *             of the grammar file and the string to test, strictly in that
     *             order.
     */

    private static String INPUT_MAPPINGS_STREAM = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/Big Data/mappings_indices.ser";

    //for number in 0 200 400 600 800 1000 1200 1400 1600 1800 2000 2200 2400 2600 2800 3000 3200 3400 3600 3800 4000 4200 4400 4600 4800 5000 5200 5400 5600 5800 6000 6200 6400 6600 6800 7000 7200 7400 7600 7800 8000 8200 8400 8600 8800 9000 9200 9400 9600 9800 10000 10200 10400 10600 10800 11000 11200 11400 11600 11800 12000 12200 12400; do printf "java SyntacticAnalyzer/CYK %04d" $number | "sh"; done;

    public static void main(String[] args) {



        ArrayList<Integer> notRunned = new ArrayList<>();
        try{

            CYK cyk = new CYK();


            FileInputStream in = new FileInputStream(PATH_TRAIN_DEPENDENCIES);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> trueDependencies = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();

            /**
             * 1.1. Loading the grammar from stream file
             */
            System.out.println("Load the grammar file");
            in = new FileInputStream(PATH_INPUT_GRAMMAR);
            stream = new ObjectInputStream(in);
            ArrayList<GrammaticalRule> grammar = (ArrayList<GrammaticalRule>) stream.readObject();

            /**
             * Converting the grammar list into HashMap
             */
            for(int i=0;i<grammar.size();i++){
                if(cyk.variables.containsKey(grammar.get(i).getLeftHandSide())){
                    ArrayList<ArrayList<NonTerminal>> values = cyk.variables.get(grammar.get(i).getLeftHandSide());
                    values.add(grammar.get(i).getRightHandSide());
                    cyk.variables.put(grammar.get(i).getLeftHandSide(),values);
                }else{
                    ArrayList<ArrayList<NonTerminal>> values = new ArrayList<ArrayList<NonTerminal>>();
                    values.add(grammar.get(i).getRightHandSide());
                    cyk.variables.put(grammar.get(i).getLeftHandSide(),values);
                }
            }

            /**
             * 1.2. Loading the one sided rules from stream file
             */
            System.out.println("Load the one sided rules");
            in = new FileInputStream(PATH_INPUT_ONE_SIDED_RULES);
            stream = new ObjectInputStream(in);
            cyk.tagsDeprels = (Map<String,ArrayList<String>>) stream.readObject();

            /**
             * 2. Loading the morphology chart from the file
             */
            System.out.println("Load the charts");
            in = new FileInputStream(PATH_INFO_CHARTS);
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> chartsInfo = (ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>>) stream.readObject();

            in = new FileInputStream(PATH_LIST_PROJECTIVE_TRAIN);
            stream = new ObjectInputStream(in);
            ArrayList<Integer> indices = (ArrayList<Integer>) stream.readObject();


            /**
             * 3. Loading the mapping from EMOR to Penn Treebank Tags
             */
            System.out.println("Load Distinct Tags");
            cyk.processEmorToPennTags(PATH_DISTINCT_TAGS);



            ArrayList<ArrayList<NonTerminal> [][]> parsingCharts = new ArrayList<ArrayList<NonTerminal>[][]>();

            ArrayList<ArrayList<SmallConLL>> dependenciesList = new ArrayList();


            /**
             * 4. Transform the info chart into a format that fits our CYK algorithm
             */
            System.out.println("Transform the info chart into a format that fits our CYK algorithm");

            int progress =0;
            ArrayList<Long> times = new ArrayList<Long>();
            ArrayList<Integer> lengths = new ArrayList<Integer>();
            for (int partial=0;partial<400;partial++) { //chartsInfo.size()
                progress++;
                int index = indices.get(partial);
                ArrayList<DependencyConLL> trueDependency = trueDependencies.get(index);
                System.out.println("PROGRESS BAR ===>"+progress+" trueDependency.size()==> "+trueDependency.size()
                        +" chartsInfo.get(index).size()= "+chartsInfo.get(index).size());
                ArrayList<NonTerminal>[][] parsingChart = new ArrayList[chartsInfo.get(index).size()][];
                for (int i = 0; i < chartsInfo.get(index).size(); ++i)
                {
                    parsingChart[i] = new ArrayList[chartsInfo.get(index).size()];
                    for (int j = 0; j < chartsInfo.get(index).size(); ++j) {
                        parsingChart[i][j] = new ArrayList<NonTerminal>();
                    }
                }

                for (int sub=0;sub<chartsInfo.get(index).size();sub++){
                    for (int subsub =0; subsub<chartsInfo.get(index).get(sub).size();subsub++){
                        for (String key: chartsInfo.get(index).get(sub).get(subsub).keySet()) {
                            for(int subsubsub = 0; subsubsub<chartsInfo.get(index).get(sub).get(subsub).get(key).size();subsubsub++){
                                String [] parts = chartsInfo.get(index).get(sub).get(subsub).get(key).get(subsubsub).split("<");
                                if (parts.length>=2){
                                    String emorTag = "";
                                    for (int ll=1;ll<parts.length;ll++){
                                        emorTag = emorTag + "<" + parts[ll];
                                    }
                                    // Increase the count for that word
                                    if (cyk.wordCount.containsKey(parts[0])){
                                        int count = cyk.wordCount.get(parts[0]);
                                        count = count + 1;
                                        cyk.wordCount.put(parts[0],count);
                                    } else {
                                        cyk.wordCount.put(parts[0],1);
                                    }

                                    //System.out.println("emorTag= "+emorTag);
                                    String pennTag;
                                    if (emorTag.equals("<Punct>")){
                                        if (!parts[0].equals("--")) {
                                            pennTag = parts[0];
                                        } else {
                                            pennTag = ":";
                                        }
                                    }else {
                                        if (cyk.EMORtoPenntags.containsKey(emorTag)) {
                                            pennTag = cyk.EMORtoPenntags.get(emorTag);
                                        } else {
                                            pennTag = emorTag;
                                        }
                                    }
                                    if (subsub<trueDependency.size()) {
                                        if (cyk.tagsDeprels.containsKey(pennTag) && trueDependency.get(subsub).getLemma().equals(parts[0])) {
                                            //System.out.println(pennTag+"something");
                                            for (String deprel : cyk.tagsDeprels.get(pennTag)) {
                                                parsingChart[sub][subsub].add(new NonTerminal(trueDependency.get(subsub).getId(), parts[0], pennTag, deprel, -1, -1, -1));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //cyk.printChart(parsingChart);

                /**
                 * 5. Running CYK
                 */

                int length = parsingChart.length;
                System.out.println("length of parsing Chart= "+length);

                //if (length>55){
                    //notRunned.add(index);

                //} else {

                //System.out.println("Running CYK");
                long startTime = System.currentTimeMillis();
                parsingChart = cyk.buildParsingChart(parsingChart);
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                times.add(totalTime);
                lengths.add(length);
                System.out.println("Total Running Time is=> " + totalTime);
                //cyk.printChart(parsingChart);



                //ArrayList<SmallConLL> dependencies = cyk.constructDependenciesPairs(parsingChart);
                //dependenciesList.add(dependencies);


                parsingCharts.add(parsingChart);
                Runtime.getRuntime().gc();
                //}

            }

            /**
             * 6. Saving Results
             */
            System.out.println("Saving Results");

            FileOutputStream fos = new FileOutputStream(OUTPUT_CYK_CHARTS_STREAM);
            ObjectOutputStream s = new ObjectOutputStream(fos);
            s.writeObject(parsingCharts);
            s.flush();

            fos = new FileOutputStream(SYNTAX_INPUT_PATH_FOLDER+"not_runned_train.ser");
            s = new ObjectOutputStream(fos);
            s.writeObject(notRunned);
            s.flush();



            BufferedWriter wr = new BufferedWriter(new FileWriter(PATH_TIMES));
            for (int i=0;i<times.size()-1;i++){
                wr.write(String.valueOf(times.get(i))+",");
            }
            wr.write(String.valueOf(times.get(times.size()-1))+",");
            wr.close();

            wr = new BufferedWriter(new FileWriter(PATH_LENGTHS));
            for (int i=0;i<lengths.size()-1;i++){
                wr.write(String.valueOf(lengths.get(i))+",");
            }
            wr.write(String.valueOf(lengths.get(lengths.size()-1))+",");
            wr.close();
            /*
            fos = new FileOutputStream(OUTPUT_DEPENDENCIES_STREAM);
            s = new ObjectOutputStream(fos);
            s.writeObject(dependenciesList);
            s.flush();
            */


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

            /*
             System.out.println("Load the grammar file");
            cyk.processGrammarFile(PATH_INPUT_GRAMMAR);


            System.out.println("Load the one sided rules");
            cyk.processOneSidedRules(PATH_INPUT_ONE_SIDED_RULES);
            */

             /*
            FileInputStream ins = new FileInputStream(INPUT_MAPPINGS_STREAM);
            ObjectInputStream is = new ObjectInputStream(ins);
            HashMap<Integer,ArrayList<Integer>> mappings = (HashMap<Integer,ArrayList<Integer>>)is.readObject();
      */

       /*
            fos = new FileOutputStream("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/wordsCount.ser");
            s = new ObjectOutputStream(fos);
            s.writeObject(cyk.wordCount);
            s.flush();
            */


         /*
                for (int i=0;i<dependencies.size();i++){
                    System.out.println(dependencies.get(i).toString());
                }*/