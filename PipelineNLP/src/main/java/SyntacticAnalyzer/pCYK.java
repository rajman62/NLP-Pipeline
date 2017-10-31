package SyntacticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/** Probabilistic Version of CYK
 * @author MeryemMhamdi
 * @date 5/18/17.
 */
public class pCYK {

    ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> chartsInfo =  new ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> ();

    /* The 2 dimensional table for the CYK algorithm */
    private static ArrayList<String>[][] parsingChart;

    /**
     * variables are in the form of (0 U 1)+
     * They are stored in the HashMap as (0 U 1)+ maps { (0 U 1)+, (0 U 1)+ }
     */
    private HashMap<NonTerminal, ArrayList<RightHandSide>> variables;


    /**
     * HashMap of the pos tags along with all possible deprels
     */
    private HashMap<String,ArrayList<String>> tagsDeprels;

    /* The start variable */
    private static String startVariable;


    /**
     * Constructs a Cyk object and initializes the HashMaps of the variables
     * and the terminals
     */
    public pCYK()
    {
        variables = new HashMap<NonTerminal, ArrayList<RightHandSide>>();
        tagsDeprels = new HashMap<String, ArrayList<String>>();
    }

    /**
     * Processes the grammar file and builds the HashMap of the list of terminals
     * and variables. Uses the Scanner object to read the grammar file.
     * @param file the string representing the path of the grammar file
     */
    public void processGrammarFile(String file)
    {
        ArrayList<ProbabilisticVariable> variablesList = new ArrayList<ProbabilisticVariable>();

        File grammarFile = null;
        Scanner scanner = null;
        try {
            grammarFile = new File(file);
            scanner = new Scanner(grammarFile);

            String[] line = scanner.nextLine().split("->");
            startVariable = line[0];
            System.out.println("startVariable: "+startVariable);
            do
            {
                String[] variable = line[0].split(":");
                String[] rest = line[1].split(" ");
                if (rest != null) {
                    double score;
                    ArrayList<ProbabilisticNonTerminal> nonTerminals = new ArrayList<ProbabilisticNonTerminal>();
                    if (rest.length == 1) {
                        String [] composition = rest[0].split("\\(")[0].split(":");
                        score = Double.valueOf(rest[0].split("\\(")[1].split("\\)")[0]);
                        ProbabilisticNonTerminal nonTerminal = new ProbabilisticNonTerminal("",
                                composition[0],composition[1],1,-1,-1,-1);
                        nonTerminals.add(nonTerminal);
                    } else {
                        String [] composition0 = rest[0].split(":");
                        score = Double.valueOf(rest[1].split("\\(")[1].split("\\)")[0]);
                        ProbabilisticNonTerminal rightHandSide0 = new ProbabilisticNonTerminal("",
                                composition0[0],composition0[1],1,-1,-1,-1);

                        String [] composition1 = rest[1].split("\\(")[0].split(":");
                        ProbabilisticNonTerminal rightHandSide1 = new ProbabilisticNonTerminal("",
                                composition1[0],composition1[1],1,-1,-1,-1);
                        nonTerminals.add(rightHandSide0);
                        nonTerminals.add(rightHandSide1);
                    }
                    RightHandSide rightHandSide = new RightHandSide(nonTerminals,score);
                    variablesList.add(new ProbabilisticVariable(new NonTerminal(-1,"",
                            variable[0],variable[1],-1,-1,-1), rightHandSide));
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

        System.out.println("variablesList: "+variablesList);

        /**
         * Converting the lists into HashMaps
         */
        for(int i=0;i<variablesList.size();i++){
            if(variables.containsKey(variablesList.get(i).getNonTerminal())){
                ArrayList<RightHandSide> values = variables.get(variablesList.get(i).getNonTerminal());
                values.add(variablesList.get(i).getVariables());
                variables.put(variablesList.get(i).getNonTerminal(),values);
            }else{
                ArrayList<RightHandSide> values = new ArrayList<RightHandSide>();
                values.add(variablesList.get(i).getVariables());
                variables.put(variablesList.get(i).getNonTerminal(),values);
            }
        }
        System.out.println("variables: "+variables);
    }

    /**
     * Processes the one sided rules file and extends all xpostags in the info chart with
     * all possible dependencies at the bottom chart
     */
    public void processOneSidedRules(String file)
    {
        ArrayList<Terminal> terminalsList = new ArrayList<Terminal>();
        ArrayList<Variable> variablesList = new ArrayList<Variable>();

        File oneSidedRulesFile = null;
        Scanner scanner = null;
        try
        {
            oneSidedRulesFile = new File(file);
            scanner = new Scanner(oneSidedRulesFile);

            // Read the one sided rules
            String[] line = scanner.nextLine().split(":");
            do
            {
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
                if (scanner.hasNextLine())
                    line = scanner.nextLine().split(":");
                else
                    line = null;
            } while (line != null);
            scanner.close();

            for (String key: tagsDeprels.keySet()) {
                String result = key+":";
                for (int j=0;j<tagsDeprels.get(key).size();j++) {
                    result = result + tagsDeprels.get(key).get(j)+ " ";
                }
                System.out.println(result);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    /**
     * Returns the chart that contains all parsing trees
     * @param parsingChart
     * @return
     */
    public ArrayList<ProbabilisticNonTerminal>[][] buildParsingChart( ArrayList<ProbabilisticNonTerminal>[][] parsingChart)
    {

        int length = parsingChart.length;

        // Applying 1 nonterminal on right hand side rules:
        for (int k=0;k<length;k++) {
            Set<NonTerminal> nonTerminals = variables.keySet();
            for (NonTerminal nonTerminal : nonTerminals) {
                ArrayList<RightHandSide> vars = variables.get(nonTerminal);
                for (int var = 0; var < vars.size(); var++) {
                    RightHandSide values = variables.get(nonTerminal).get(var);
                    if (values.getRightHandSides().size() == 1) {
                        if (parsingChart[0][k].contains((values.getRightHandSides().get(0)))) {
                            parsingChart[0][k].add(new ProbabilisticNonTerminal(parsingChart[0][k].get(0).getLemma(),
                                    nonTerminal.getXpostag(), nonTerminal.getDeprel(), values.getScore(),0,
                                    parsingChart[0][k].indexOf(values.getRightHandSides().get(0)),-1));
                            System.out.println("YAY RULE GOT REALLY APPLIED");
                        }
                    }
                }
            }
        }

        // Applying 2 nonterminals on right hand side rules:
        System.out.println("length:"+length);
        for (int i = 1; i < length; i++) {
            for (int j = 0; j < length - i ; j++) {
                for (int k = 0; k <= i-1 ; k++) {
                    System.out.println("\n\nnext iteration>> i= "+i+" j= "+j+" k= "+k);
                    Set<NonTerminal> nonTerminals = variables.keySet();
                    for (NonTerminal nonTerminal : nonTerminals) {
                        ArrayList<RightHandSide> vars = variables.get(nonTerminal);
                        for(int var=0;var<vars.size();var++) {
                            RightHandSide values = variables.get(nonTerminal).get(var);
                            if (values.getRightHandSides().size()==2) {
                                System.out.println("\nApplying rule=> " + nonTerminal + "->"
                                        + values.getRightHandSides().get(0).toString()
                                        + "," + values.getRightHandSides().get(1).toString());
                                System.out.println("parsingChart[i-k][j]:"+parsingChart[i-k-1][j]);
                                System.out.println("parsingChart[k][i+j-k]:"+parsingChart[k][i+j-k]);
                                if (parsingChart[i-k-1][j].contains((values.getRightHandSides().get(0)))
                                        && parsingChart[k][i+j-k].contains(values.getRightHandSides().get(1))){
                                    int pointer;
                                    if (j<i+j-k){
                                        pointer = i - k - 1;
                                    } else {
                                        pointer = k;
                                    }
                                    int right1Index = parsingChart[i-k-1][j].indexOf(values.getRightHandSides().get(0));
                                    int right2Index = parsingChart[k][i+j-k].indexOf(values.getRightHandSides().get(1));

                                    String lemma;
                                    if ((parsingChart[i-k-1][j].get(right1Index)).equals(nonTerminal)){
                                        lemma = parsingChart[i-k-1][j].get(right1Index).getLemma();
                                    } else {
                                        lemma = parsingChart[k][i+j-k].get(right2Index).getLemma();
                                    }
                                    double score = values.getScore()*parsingChart[i-k-1][j].get(right1Index).getScore()
                                            *parsingChart[k][i+j-k].get(right2Index).getScore();
                                    if (!parsingChart[i][j].contains(nonTerminal)) {
                                        System.out.println("FIRST TIME");
                                        parsingChart[i][j].add(new ProbabilisticNonTerminal(lemma,nonTerminal.getXpostag()
                                                ,nonTerminal.getDeprel(),score,pointer,right1Index, right2Index));
                                        System.out.println("YAY RULE GOT REALLY APPLIED");
                                    } else {
                                        System.out.println("UPDATING EXISTING");
                                        ProbabilisticNonTerminal updated = parsingChart[i][j].get(parsingChart[i][j]
                                                .indexOf(nonTerminal));
                                        if (score >updated.getScore()) {
                                            updated.setPointer(pointer);
                                            updated.setRight1Index(right1Index);
                                            updated.setRight2Index(right2Index);
                                            updated.setScore(score);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Applying 1 nonterminal on right hand side rules:
        for (int i=0;i<length;i++) {
            for (int k = 0; k < length; k++) {
                Set<NonTerminal> nonTerminals = variables.keySet();
                for (NonTerminal nonTerminal : nonTerminals) {
                    ArrayList<RightHandSide> vars = variables.get(nonTerminal);
                    for (int var = 0; var < vars.size(); var++) {
                        RightHandSide values = variables.get(nonTerminal).get(var);
                        if (values.getRightHandSides().size() == 1) {
                            if (parsingChart[i][k].contains((values.getRightHandSides().get(0)))) {
                                parsingChart[i][k].add(new ProbabilisticNonTerminal(parsingChart[i][k].get(0).getLemma()
                                        ,nonTerminal.getXpostag(), nonTerminal.getDeprel(),values.getScore(),i,
                                        parsingChart[i][k].indexOf(values.getRightHandSides().get(0)), -1));
                                System.out.println("YAY RULE GOT REALLY APPLIED");
                            }
                        }
                    }
                }
            }
        }

        return parsingChart;
    }

    public void printChart(ArrayList<ProbabilisticNonTerminal>[][] parsingChart){
        for (int i = parsingChart.length - 1; i >= 0; i--) {
            String level = "[";
            for (int j = 0; j < parsingChart[i].length; j++) {
                String result = "";
                for (int k=0;k<parsingChart[i][j].size();k++){
                    if (k == parsingChart[i][j].size()-1) {
                        result = result + parsingChart[i][j].get(k).getLemma()
                                + ":" + parsingChart[i][j].get(k).getXpostag()
                                + ":" + parsingChart[i][j].get(k).getDeprel()
                                + ":" + parsingChart[i][j].get(k).getScore()
                                + ":" + parsingChart[i][j].get(k).getPointer()
                                + ":" + parsingChart[i][j].get(k).getRight1Index()
                                + ":" + parsingChart[i][j].get(k).getRight2Index();
                    } else {
                        result = result + parsingChart[i][j].get(k).getLemma()
                                + ":" + parsingChart[i][j].get(k).getXpostag()
                                + ":" + parsingChart[i][j].get(k).getDeprel()
                                + ":" + parsingChart[i][j].get(k).getScore()
                                + ":" + parsingChart[i][j].get(k).getPointer()
                                + ":" + parsingChart[i][j].get(k).getRight1Index()
                                + ":" + parsingChart[i][j].get(k).getRight2Index()+ "|";
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


    public ArrayList<SmallConLL> constructDependenciesPairs(ArrayList<ProbabilisticNonTerminal>[][] parsingChart){
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

                        System.out.println("i= " + i + " j= " + j + " k= " + k + " i_right_1= " + i_right_1
                                + " i_right_2= " + i_right_2 + " j_right_2= " + j_right_2);
                        String head = parsingChart[i][j].get(k).getLemma();
                        String posHead = parsingChart[i][j].get(k).getXpostag();
                        String dep, posDep, rel;
                        if (parsingChart[i_right_1][j].get(right1Index).getLemma()
                                .equals(parsingChart[i][j].get(k).getLemma()) &&
                                parsingChart[i_right_1][j].get(right1Index).getXpostag()
                                .equals(parsingChart[i][j].get(k).getXpostag())) {
                            dep = parsingChart[i_right_2][j_right_2].get(right2Index).getLemma();
                            posDep = parsingChart[i_right_2][j_right_2].get(right2Index).getXpostag();
                            rel = parsingChart[i_right_2][j_right_2].get(right2Index).getDeprel();
                            dependencies.add(new SmallConLL(head, posHead, dep, posDep, rel));

                        } else if (parsingChart[i_right_2][j_right_2].get(right2Index).getLemma()
                                .equals(parsingChart[i][j].get(k).getLemma()) &&
                                parsingChart[i_right_2][j_right_2].get(right2Index).getXpostag()
                                .equals(parsingChart[i][j].get(k).getXpostag())){
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


    /**
     * Takes a given grammar file as the input and a given string to test
     * against that grammar.
     * @param args the list of the given command-line arguments consisting
     *             of the grammar file and the string to test, strictly in that
     *             order.
     */
    public static void main(String[] args) {

        /**
         * Load the tokenization chart from the file (EXAMPLE FOR NOW JUST FOR TESTING)
         */
        ArrayList<ArrayList<ArrayList<Map<String, ArrayList<String>>>>> chartsInfo = new ArrayList<ArrayList<ArrayList<Map<String, ArrayList<String>>>>>();

        ArrayList<ArrayList<Map<String, ArrayList<String>>>> chart = new ArrayList<ArrayList<Map<String, ArrayList<String>>>>();

        // Building the first row
        ArrayList<Map<String, ArrayList<String>>> rowChart = new ArrayList<Map<String, ArrayList<String>>>();
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

        ArrayList<String> cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("the<DT><pl>");
        map.put("the", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("cat<NN><pl>");
        map.put("cat", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("eat<VBD><pl>");
        map.put("ate", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("the<DT><pl>");
        map.put("the", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("mouse<NNS><pl>");
        map.put("mice", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("in<IN><pl>");
        map.put("in", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("the<DT><pl>");
        map.put("the", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("garden<NN><pl>");
        map.put("NN", cannonicalForms);
        rowChart.add(map);

        chart.add(rowChart);

        // Building the next row
        rowChart = new ArrayList<Map<String, ArrayList<String>>>();

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("the cat<NN><pl>");
        map.put("the cat", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        chart.add(rowChart);

        // Building the next row
        rowChart = new ArrayList<Map<String, ArrayList<String>>>();

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        chart.add(rowChart);

        // Building the next row
        rowChart = new ArrayList<Map<String, ArrayList<String>>>();

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        chart.add(rowChart);

        // Building the next row
        rowChart = new ArrayList<Map<String, ArrayList<String>>>();

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        chart.add(rowChart);

        // Building the next row
        rowChart = new ArrayList<Map<String, ArrayList<String>>>();

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        chart.add(rowChart);

        // Building the next row
        rowChart = new ArrayList<Map<String, ArrayList<String>>>();

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        chart.add(rowChart);

        // Building the next row
        rowChart = new ArrayList<Map<String, ArrayList<String>>>();

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        map = new HashMap<String, ArrayList<String>>();
        cannonicalForms = new ArrayList<String>();
        cannonicalForms.add("");
        map.put("", cannonicalForms);
        rowChart.add(map);

        chart.add(rowChart);

        chartsInfo.add(chart);

        // Printing the Test Info chart
        System.out.println("chart.size()=" + chart.size());

        System.out.println("Printing the Test info chart");

        for (int i = chart.size() - 1; i >= 0; i--) {
            String level = "[";
            for (int j = 0; j < chart.get(i).size() - 1; j++) {
                level = level + chart.get(i).get(j) + ',';
            }
            System.out.println(level + chart.get(i).get(chart.get(i).size() - 1) + "]");
        }

        pCYK pCYK = new pCYK();

        /**
         * Load the grammar file
         */
        pCYK.processGrammarFile("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/Dependency_PCFG.txt");

        /**
         * Load the one sided rules
         */
        pCYK.processOneSidedRules("/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/One_sided_rules.txt");



        ArrayList<ArrayList<ProbabilisticNonTerminal> [][]> parsingCharts = new ArrayList<ArrayList<ProbabilisticNonTerminal>[][]>();

        ArrayList<ArrayList<SmallConLL>> dependenciesList = new ArrayList();

        // Transform the info chart into a format that fits our CYK algorithm
        for (int index = 0; index < chartsInfo.size(); index++) {
            //ArrayList<ArrayList<Map<String, ArrayList<String>>>>
            // mouse<NNS><pl>
            ArrayList<ProbabilisticNonTerminal>[][] parsingChart = new ArrayList[chartsInfo.get(index).size()][];
            for (int i = 0; i < chartsInfo.get(index).size(); ++i)
            {
                parsingChart[i] = new ArrayList[chartsInfo.get(index).size()];
                for (int j = 0; j < chartsInfo.get(index).size(); ++j) {
                    parsingChart[i][j] = new ArrayList<ProbabilisticNonTerminal>();
                }
            }

            for (int sub=0;sub<chartsInfo.get(index).size();sub++){
                for (int subsub =0; subsub<chartsInfo.get(index).get(sub).size();subsub++){
                    for (String key: chartsInfo.get(index).get(sub).get(subsub).keySet()) {
                        for(int subsubsub = 0; subsubsub<chartsInfo.get(index).get(sub).get(subsub).get(key).size();subsubsub++){
                            String [] parts = chartsInfo.get(index).get(sub).get(subsub).get(key).get(subsubsub).split("<");
                            if (parts.length>=2){
                                String [] subParts = parts[1].split(">");
                                if (subParts.length>=1) {
                                    if (subParts[0]!=null) {
                                        for (String deprel : pCYK.tagsDeprels.get(subParts[0])) {
                                            parsingChart[sub][subsub].add(new ProbabilisticNonTerminal(parts[0]
                                                    ,subParts[0], deprel,1,-1,-1,-1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            parsingChart = pCYK.buildParsingChart(parsingChart);


            pCYK.printChart(parsingChart);

            ArrayList<SmallConLL> dependencies = pCYK.constructDependenciesPairs(parsingChart);



            for (int i=0;i<dependencies.size();i++){
                System.out.println(dependencies.get(i).toString());
            }

            dependenciesList.add(dependencies);
            parsingCharts.add(parsingChart);
        }

    }
}
