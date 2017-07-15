package SyntacticAnalyzer;

//for number in "1" "2" "3" "4" "5" "6" "7" "8" "9" "10" "11" "12" "13" "14" "15" "16" "17" "18" "19" "20" "21" "22" "23" "24" "25" "26" "27" "28" "29" "30" "31" "32" "33" "34" "35" "36" "37" "38" "39" "40" "41" "42" "43" "44" "45" "46" "47" "48" "49" "50" "51" "52" "53" "54" "55" "56" "57" "58" "59" "60" "61" "62" "63";
// do for start in 0 200 400 600 800 1000 1200 1400 1600 1800 2000 2200 2400 2600 2800 3000 3200 3400 3600 3800 4000 4200 4400 4600 4800 5000 5200 5400 5600 5800 6000 6200 6400 6600 6800 7000 7200 7400 7600 7800 8000 8200 8400 8600 8800 9000 9200 9400 9600 9800 10000 10200 10400 10600 10800 11000 11200 11400 11600 11800 12000 12200 12400
// ; do for end in 200 400 600 800 1000 1200
// 1400 1600 1800 2000 2200 2400 2600 2800 3000
// 3200 3400 3600 3800 4000 4200 4400 4600 4800
// 5000 5200 5400 5600 5800 6000 6200 6400 6600
// 6800 7000 7200 7400 7600 7800 8000 8200 8400
// 8600 8800 9000 9200 9400 9600 9800 10000 10200
// 10400 10600 10800 11000 11200 11400 11600 11800
// 12000 12200 12400 12543; do
// printf "java SyntacticAnalyzer/DependencyGraph %s %04d %04d" $number $start $end | "sh"; done; done; done;

//for number in 0 200 400 600 800 1000 1200 1400 1600 1800 2000 2200 2400 2600 2800 3000 3200 3400 3600 3800 4000 4200 4400 4600 4800 5000 5200 5400 5600 5800 6000 6200 6400 6600 6800 7000 7200 7400 7600 7800 8000 8200 8400 8600 8800 9000 9200 9400 9600 9800 10000 10200 10400 10600 10800 11000 11200 11400 11600 11800 12000 12200; do printf "java SyntacticAnalyzer/DependencyGraph %04d %04d %04d" $number $number $number| "sh"; done;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Dependencies Structures written in ConLL format
 * @author MeryemMhamdi
 * @date 5/10/17.
 */
public class DependencyGraph {
    public ArrayList<GrammaticalRule> grammar;
    public Map<String,ArrayList<String>> oneSidedRules;
    private static String OUTPUT_PATH_FOLDER1 = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Syntactic Analysis/UDC/Train/";
    private static String OUTPUT_PATH_FOLDER = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/Big Data/";
    private static String DATASET_LOCATION = "/Users/MeryemMhamdi/Google Drive/Semester Project" +
            "/3 Implementation & Algorithms/Datasets/UDC/";
    private static String PATH_TRAIN_CONLL =  DATASET_LOCATION + "parsedDependenciesConLL_train_true.conllu";

    private static String PATH_DEP_GRAMMAR_STREAM = OUTPUT_PATH_FOLDER+"Dependency_Grammar_CNF.ser";
    private static String PATH_DEP_GRAMMAR_TXT = OUTPUT_PATH_FOLDER+"Dependency_Grammar_CNF.txt";

    private static  String PATH_ONE_SIDED_STREAM = OUTPUT_PATH_FOLDER+"One_sided_rules_CNF.ser";
    private static  String PATH_ONE_SIDED_TXT = OUTPUT_PATH_FOLDER+"One_sided_rules_CNF.txt";

    private static String PATH_PROJECTIVE = OUTPUT_PATH_FOLDER+ "parsed_sentences_projective.ser";
    private static String PATH_LIST_PROJECTIVE_TRAIN = OUTPUT_PATH_FOLDER+ "projective_indices_train.ser";
    private static String PATH_LIST_PROJECTIVE_TEST = OUTPUT_PATH_FOLDER+ "projective_indices_test.ser";
    private static String PATH_LIST_PROJECTIVE_TRAIN_TXT = OUTPUT_PATH_FOLDER+ "projective_indices_train.txt";
    private static String PATH_LIST_PROJECTIVE_TEST_TXT = OUTPUT_PATH_FOLDER+ "projective_indices_test.txt";
    private static String PATH_NON_PROJECTIVE = OUTPUT_PATH_FOLDER+ "parsed_sentences_non_projective.ser";


    public DependencyGraph(){
        grammar = new ArrayList<GrammaticalRule>();
        oneSidedRules = new HashMap<String,ArrayList<String>>();
    }
    public ArrayList<ArrayList<DependencyConLL>> parseDependencyConLL(String file){
        ArrayList<ArrayList<DependencyConLL>> listDependenciesConLL = new ArrayList<ArrayList<DependencyConLL>>();
        ArrayList<DependencyConLL> dependenciesConLL = new ArrayList<DependencyConLL>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null){
                if (line.length()==0){
                    //System.out.println("DETECTING NEWLINE");
                    listDependenciesConLL.add(dependenciesConLL);
                    dependenciesConLL = new ArrayList<DependencyConLL>();
                }
                else {
                    if (Character.isDigit(line.charAt(0))) {
                        //System.out.println("DETECTING DEPENDENCY LINE");
                        String[] conLLElements = line.split("\\t", -1);
                        int head;
                        if (conLLElements[6].equals("_")){
                            head = -2;
                        } else {
                            head = Integer.parseInt(conLLElements[6]);
                        }
                        DependencyConLL dependencyConLL = new DependencyConLL(Double.parseDouble(conLLElements[0]), conLLElements[1], conLLElements[2]
                                , conLLElements[3], conLLElements[4], conLLElements[5], head, conLLElements[7], conLLElements[8], conLLElements[9]);
                        dependenciesConLL.add(dependencyConLL);
                    }
                }

                line = br.readLine();
            }
            listDependenciesConLL.add(dependenciesConLL);
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listDependenciesConLL;
    }
    public DependencyNode[][] transformTree(ArrayList<DependencyConLL> dependenciesConLL){
        int length = dependenciesConLL.size();
        DependencyNode[][] cykChart = new DependencyNode[length][length];
        HashMap<Double,Double> dependenciesEdges = new HashMap<Double,Double>();

        for (int i =0; i<dependenciesConLL.size();i++){
            double id = dependenciesConLL.get(i).getId();
            double head = dependenciesConLL.get(i).getHead();
            String lemma = dependenciesConLL.get(i).getLemma();
            String xpostag = dependenciesConLL.get(i).getXPosTag();
            String deprel = dependenciesConLL.get(i).getDeprel();
            dependenciesEdges.put(id,head);
            cykChart[0][i] = new DependencyNode(id,lemma,xpostag,"*",-1);
        }

        // Looking for dependencies to construct a tree the CYK way
        int count = 1;
        for (int i = 1; i < length; i = i + 1) {
            count = 1;
            for (int j = 0; j < length - i ; j = j + count) {
                count = 1;
                for (int k = 0; k <= i-1 ; k = k + 1) {
                    // Looking for Rightward Dependency Rule
                    if (cykChart[i - k - 1][j]!=null && cykChart[k][i + j - k]!=null) {
                        if (dependenciesEdges.get(cykChart[i - k - 1][j].getId()) == cykChart[k][i + j - k].getId()) {
                            //System.out.println("Checking for Rightward Dependency Rule");
                            double id = cykChart[k][i + j - k].getId();
                            String lemma = cykChart[k][i + j - k].getLemma();
                            String xpostag = cykChart[k][i + j - k].getXpostag();
                            String deprel = cykChart[k][i + j - k].getDeprel();
                            int pointer;
                            if (j<i+j-k){
                                pointer = i - k - 1;
                            } else {
                                pointer = k;
                            }
                            cykChart[i][j] = new DependencyNode(id,lemma,xpostag,deprel,pointer);
                            // Update the dependency of the dependent
                            deprel = dependenciesConLL.get((int)Math.round(Math.floor(cykChart[i - k - 1][j].getId()-1))).getDeprel();
                            cykChart[i - k - 1][j].setDeprel(deprel);

                            count = count + i;
                        } else if (dependenciesEdges.get(cykChart[k][i + j - k].getId()) == cykChart[i - k - 1][j].getId()) {
                            //System.out.println("Checking for Leftward Dependency Rule");
                            double id = cykChart[i - k - 1][j].getId();
                            String lemma = cykChart[i - k - 1][j].getLemma();
                            String xpostag = cykChart[i - k - 1][j].getXpostag();
                            String deprel = cykChart[i - k - 1][j].getDeprel();
                            int pointer;
                            if (j<i+j-k){
                                pointer = i - k - 1;
                            } else {
                                pointer = k;
                            }
                            cykChart[i][j] = new DependencyNode(id,lemma,xpostag,deprel,pointer);
                            //System.out.println("ADDING NEW CONTENT "+cykChart[i][j].getLemma() +"to i= "+i+" j= "+j);
                            //System.out.println("cykChart[k][i + j - k].getId()= "+cykChart[k][i + j - k].getId());
                            deprel = dependenciesConLL.get((int)Math.round(Math.floor(cykChart[k][i + j - k].getId()-1))).getDeprel();
                            cykChart[k][i + j - k].setDeprel(deprel);
                            count = count + i;
                        }
                    }
                }
            }
        }
        return cykChart;
    }

    public boolean checkForProjectivity(HashMap<DependencyNode,ArrayList<DependencyNode>> grammaticalRules
            , ArrayList<DependencyConLL> dependenciesConLL){
        boolean flag = true;
        System.out.println("grammaticalRules= "+grammaticalRules);
        HashMap<Double,ArrayList<Double>> doublePaths = new HashMap<Double,ArrayList<Double>>();
        for (DependencyNode key: grammaticalRules.keySet()) {
            for (DependencyNode node : grammaticalRules.get(key)) {
                if (!doublePaths.containsKey(key.getId())) {
                    ArrayList<Double> values = new ArrayList<Double>();
                    values.add(node.getId());
                    doublePaths.put(key.getId(),values);
                } else {
                    ArrayList<Double> values = doublePaths.get(key.getId());
                    values.add(node.getId());
                    doublePaths.put(key.getId(),values);
                }
            }
        }
        ArrayList<Double> paths = new ArrayList<Double>();
        ArrayList<Double> swapPaths = new ArrayList<Double>();
        paths.add(Double.valueOf(0));
        ArrayList<Double> truePaths = new ArrayList<Double>();
        ArrayList<Double> visited = new ArrayList<Double>();

        for(int i=0;i<dependenciesConLL.size();i++){
            truePaths.add(dependenciesConLL.get(i).getId());
        }
        //System.out.println("doublePaths= "+doublePaths);
        //System.out.println("grammaticalRules.size()= "+grammaticalRules.size());
        while (visited.size()<grammaticalRules.size()){
            for (int i = 0; i < paths.size(); i++) {
                if (doublePaths.containsKey(paths.get(i)) && !visited.contains(paths.get(i))) {
                    visited.add(paths.get(i));
                    for (Double element : doublePaths.get(paths.get(i))) {
                        swapPaths.add(element);
                    }
                } else {
                    swapPaths.add(paths.get(i));
                }
            }

            paths = swapList(paths,swapPaths);
            swapPaths = new ArrayList<Double>();
        }
        //System.out.println("paths=> "+truePaths);
        //System.out.println("truePaths=> "+paths);

        if (paths.size()==truePaths.size()) {
            for (int i = 0; i < truePaths.size(); i++) {
                if (Double.compare(truePaths.get(i), paths.get(i))!=0){
                    //System.out.println("FALSE "+paths.get(i)+ " "+truePaths.get(i));
                    flag = false;
                }
            }
        } else {
            flag = false;
        }
        //System.out.println("FLAG=> "+flag);
        return flag;
    }

    public static ArrayList<Double> swapList(ArrayList<Double> list1, ArrayList<Double> list2){
        ArrayList<Double> tmpList = new ArrayList<Double>(list1);
        list1 = new ArrayList<Double>();
        list1.addAll(list2);
        /*
        list2 = new ArrayList<Double>();
        list2.addAll(tmpList);
        */
        return list1;
    }

    public ArrayList<GrammaticalRule> buildGrammaticalRules(ArrayList<DependencyConLL> dependenciesConLL){
        ArrayList<GrammaticalRule> grammaticalRules = new ArrayList<GrammaticalRule>();
        HashMap<DependencyNode,ArrayList<DependencyNode>> rules = new HashMap<DependencyNode,ArrayList<DependencyNode>>();
        HashMap<Double,DependencyNode> dependents = new HashMap<Double,DependencyNode>();
        Stack<Double> visited = new Stack<>();
        Stack<Integer> visitedIndices = new Stack<>();

        for (int i=0;i<dependenciesConLL.size();i++) {
            if (dependenciesConLL.get(i).getHead() == 0) {
                DependencyNode node = new DependencyNode(dependenciesConLL.get(i).getId(), dependenciesConLL.get(i).getLemma()
                        , dependenciesConLL.get(i).getXPosTag(), "*");
                DependencyNode head = new DependencyNode(dependenciesConLL.get(i).getHead(), "ROOT", "root", "root");
                if (!rules.containsKey(head)) {
                    ArrayList<DependencyNode> dependencyNodes = new ArrayList<DependencyNode>();
                    dependencyNodes.add(node);
                    rules.put(head, dependencyNodes);
                }
                visited.add(dependenciesConLL.get(i).getId());
                visitedIndices.add(i);
            }
        }
        while(visited.size()>0){
            ArrayList<Integer> children = new ArrayList<Integer>();
            double id = visited.pop();
            int index = visitedIndices.pop();
            for (int j=0;j<dependenciesConLL.size();j++){
                if (dependenciesConLL.get(j).getHead()==id){
                    visited.add(dependenciesConLL.get(j).getId());
                    visitedIndices.add(j);
                    children.add(j);
                }
            }
            DependencyNode head;
            if (dependents.containsKey(id)) {
                String rel = dependents.get(id).getDeprel();
                head = new DependencyNode(id, dependenciesConLL.get(index).getLemma()
                        , dependenciesConLL.get(index).getXPosTag(), rel);
            } else {
                head = new DependencyNode(id, dependenciesConLL.get(index).getLemma()
                        , dependenciesConLL.get(index).getXPosTag(), "*");
            }
            DependencyNode duplicateHead = new DependencyNode(id, dependenciesConLL.get(index).getLemma()
                    , dependenciesConLL.get(index).getXPosTag(), "*");
            for (int k=0;k<children.size();k++) {
                int child = children.get(k);
                DependencyNode node = new DependencyNode(dependenciesConLL.get(child).getId(),dependenciesConLL.get(child).getLemma()
                        ,dependenciesConLL.get(child).getXPosTag(),dependenciesConLL.get(child).getDeprel());
                if (!rules.containsKey(head)) {
                    dependents.put(dependenciesConLL.get(child).getId(), node);
                    ArrayList<DependencyNode> dependencyNodes = new ArrayList<>();
                    dependencyNodes.add(node);
                    dependencyNodes.add(duplicateHead);
                    Collections.sort(dependencyNodes, new DependencyNodeComparer());
                    rules.put(head, dependencyNodes);
                } else {
                    ArrayList<DependencyNode> dependencyNodes = rules.get(head);
                    dependents.put(dependenciesConLL.get(child).getId(), node);
                    dependencyNodes.add(node);
                    Collections.sort(dependencyNodes, new DependencyNodeComparer());
                    rules.put(head, dependencyNodes);
                }
            }
        }
        //System.out.println("dependents= "+dependents);
        if (checkForProjectivity(rules,dependenciesConLL)){
            for (DependencyNode key: rules.keySet()){
                ArrayList<NonTerminal> nonTerminals = new ArrayList<NonTerminal>();
                for (DependencyNode value: rules.get(key)) {
                    NonTerminal nonTerminal = new NonTerminal(value.getId(),value.getLemma(),value.getXpostag(),value.getDeprel());
                    nonTerminals.add(nonTerminal);
                }
                grammaticalRules.add(new GrammaticalRule(new NonTerminal(key.getId(),key.getLemma(),key.getXpostag(),key.getDeprel()),nonTerminals));
            }
            return grammaticalRules;
        } else {
            return null;
        }
    }
    public static void main (String args[]){

        DependencyGraph dg = new DependencyGraph();
        //ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = dg.parseDependencyConLL(PATH_TRAIN_CONLL);

        try {
            FileInputStream in = new FileInputStream(PATH_TRAIN_CONLL);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();


            String number = "oldCNF";//Integer.parseInt(args[0]); //"15";
            int start = 0;//Integer.parseInt(args[1]);//3400;
            int end = parsedDependenciesConLL.size();//Integer.parseInt(args[2])+200; //+ 200;//3600;

            //System.out.println("number=> "+number+" start=> "+start+" end=> "+end);

            /*

            parsedDependenciesConLL = new ArrayList<ArrayList<DependencyConLL>>();
            ArrayList<DependencyConLL> testdependency = new ArrayList<DependencyConLL>();
            testdependency.add(new DependencyConLL(1,"The","the","DET","DT","-",2,"det","-","-"));
            testdependency.add(new DependencyConLL(2,"cat","cat","NOUN","NN","-",3,"nsubj","-","-"));
            testdependency.add(new DependencyConLL(3,"ate","eat","VERB","VBD","-",0,"root","-","-"));
            testdependency.add(new DependencyConLL(4,"the","the","DET","DT","-",5,"det","-","-"));
            testdependency.add(new DependencyConLL(5,"mice","mouse","NOUN","NNS","-",3,"dobj","-","-"));
            testdependency.add(new DependencyConLL(6,"in","in","SCONJ","IN","-",8,"case","-","-"));
            testdependency.add(new DependencyConLL(7,"the","the","DET","DT","-",8,"det","-","-"));
            testdependency.add(new DependencyConLL(8,"garden","garden","NOUN","NN","-",3,"nmod","-","-"));
            parsedDependenciesConLL.add(testdependency);

            */


            System.out.println("parsedDependenciesConLL.size()= "+parsedDependenciesConLL.size());


            ArrayList<ArrayList<DependencyConLL>> projectiveSentences = new ArrayList<ArrayList<DependencyConLL>>();
            ArrayList<ArrayList<DependencyConLL>> nonProjectiveSentences = new ArrayList<ArrayList<DependencyConLL>>();

            int count = 0;
            String indices = "";
            ArrayList<Integer> projectiveIndices = new ArrayList<Integer>();

            for (int k=0;k< parsedDependenciesConLL.size();k++) { //
                if (k%10==0){
                    System.out.println("Progress is=>"+k);
                }
                //DependencyNode[][] dependencyChart = dg.transformTree(parsedDependenciesConLL.get(k));

                ArrayList<GrammaticalRule> grammaticalRules = dg.buildGrammaticalRules(parsedDependenciesConLL.get(k));

                if (grammaticalRules!=null) {
                    count = count + 1;
                    projectiveSentences.add(parsedDependenciesConLL.get(k));
                    projectiveIndices.add(k);
                    System.out.println(grammaticalRules.toString());

                    /*
                    for (int g=0;g<grammaticalRules.size();g++){
                        if (!dg.grammar.contains(new GrammaticalRule(grammaticalRules.get(g).getLeftHandSide(), grammaticalRules.get(g).getRightHandSide()))) {
                            dg.grammar.add(new GrammaticalRule(grammaticalRules.get(g).getLeftHandSide(), grammaticalRules.get(g).getRightHandSide()));
                        }
                    }*/

                    // CONVERTING RULES WITH MORE THAN TWO RIGHT HANDSIDES
                    for (int j = 0; j < grammaticalRules.size(); j++) {
                        NonTerminal left = grammaticalRules.get(j).getLeftHandSide();
                        ArrayList<NonTerminal> right = grammaticalRules.get(j).getRightHandSide();
                        if (right.size()>2){
                            boolean flag = false;
                            boolean first = true;
                            NonTerminal headDuplicate = left;
                            for (int r=0;r<right.size();r++){
                                if (Double.compare(right.get(r).getId(),left.getId())==0){
                                    headDuplicate = right.get(r);
                                }
                            }

                            for (int r=0;r<right.size();r++){
                                if (Double.compare(right.get(r).getId(),left.getId())==0){
                                    flag = true;
                                } else {
                                    if (flag==true){
                                        ArrayList<NonTerminal> newRightHandSides = new ArrayList<>();
                                        newRightHandSides.add(headDuplicate);
                                        newRightHandSides.add(right.get(r));
                                        if (first) {
                                            GrammaticalRule grammaticalRule = new GrammaticalRule(left, newRightHandSides);
                                            if (!dg.grammar.contains(grammaticalRule)) {
                                                dg.grammar.add(grammaticalRule);
                                            }
                                        } else {
                                            GrammaticalRule grammaticalRule = new GrammaticalRule(headDuplicate, newRightHandSides);
                                            if (!dg.grammar.contains(grammaticalRule)) {
                                                dg.grammar.add(grammaticalRule);
                                            }
                                        }

                                    } else {
                                        ArrayList<NonTerminal> newRightHandSides = new ArrayList<>();
                                        newRightHandSides.add(right.get(r));
                                        newRightHandSides.add(headDuplicate);
                                        if (first) {
                                            GrammaticalRule grammaticalRule = new GrammaticalRule(left, newRightHandSides);
                                            if (!dg.grammar.contains(grammaticalRule)) {
                                                dg.grammar.add(grammaticalRule);
                                            }
                                        } else {
                                            GrammaticalRule grammaticalRule = new GrammaticalRule(headDuplicate, newRightHandSides);
                                            if (!dg.grammar.contains(grammaticalRule)) {
                                                dg.grammar.add(grammaticalRule);
                                            }
                                        }
                                    }
                                    first = false;
                                }
                            }

                        } else if (right.size()==2) {
                            GrammaticalRule grammaticalRule = new GrammaticalRule(left,right);
                            if (!dg.grammar.contains(grammaticalRule)) {
                                dg.grammar.add(grammaticalRule);
                            }
                        }
                    }

                    for (int j = 0; j < grammaticalRules.size(); j++) {
                        if (Double.compare(grammaticalRules.get(j).getLeftHandSide().getId(),0.0)==0 ) {
                            NonTerminal leftRoot = grammaticalRules.get(j).getLeftHandSide();
                            NonTerminal rightRoot= grammaticalRules.get(j).getRightHandSide().get(0);
                            ArrayList<NonTerminal> rightHandSide = new ArrayList<NonTerminal>();
                            rightHandSide.add(rightRoot);
                            GrammaticalRule grammaticalRule = new GrammaticalRule(leftRoot, rightHandSide);
                            System.out.println("ROOT");
                            System.out.println(grammaticalRule.toString());
                            dg.grammar.add(grammaticalRule);
                        }
                    }

                    /*
                    // CONVERTING ONE RULE OF ROOT TO CNF
                    for (int j = 0; j < grammaticalRules.size(); j++) {
                        if (Double.compare(grammaticalRules.get(j).getLeftHandSide().getId(),0.0)==0 ) {
                            NonTerminal leftRoot = grammaticalRules.get(j).getLeftHandSide();
                            NonTerminal rightRoot= grammaticalRules.get(j).getRightHandSide().get(0);
                            for (int i = 0; i < dg.grammar.size(); i++) {
                                if (dg.grammar.get(i).getLeftHandSide().equals(rightRoot)) {
                                    ArrayList<NonTerminal> rightHandSide = dg.grammar.get(i).getRightHandSide();
                                    GrammaticalRule grammaticalRule = new GrammaticalRule(leftRoot, rightHandSide);
                                    if (!dg.grammar.contains(grammaticalRule)) {
                                        dg.grammar.add(grammaticalRule);
                                    }
                                }

                            }
                        }
                    }*/
                    // Constructing One Sided Rules
                    for (int i = 0; i < dg.grammar.size(); i++) {
                        String pos = dg.grammar.get(i).getLeftHandSide().getXpostag();
                        String deprel = dg.grammar.get(i).getLeftHandSide().getDeprel();
                        ArrayList<String> deps;
                        if (!dg.oneSidedRules.containsKey(pos)) {
                            deps = new ArrayList<String>();
                            deps.add(deprel);
                        } else {
                            deps = dg.oneSidedRules.get(pos);
                            if (!deps.contains(deprel)) {
                                deps.add(deprel);
                                dg.oneSidedRules.put(pos, deps);
                            }
                        }

                        for (int j = 0; j < dg.grammar.get(i).getRightHandSide().size(); j++) {
                            pos = dg.grammar.get(i).getRightHandSide().get(j).getXpostag();
                            deprel = dg.grammar.get(i).getRightHandSide().get(j).getDeprel();
                            if (!dg.oneSidedRules.containsKey(pos)) {
                                deps = new ArrayList<String>();
                                deps.add(deprel);
                            } else {
                                deps = dg.oneSidedRules.get(pos);
                                if (!deps.contains(deprel)) {
                                    deps = dg.oneSidedRules.get(pos);
                                    deps.add(deprel);
                                }
                            }
                            dg.oneSidedRules.put(pos, deps);
                        }
                    }
                }else {
                        nonProjectiveSentences.add(parsedDependenciesConLL.get(k));
                        indices = indices + " "+ String.valueOf(k);
                }
            }

            System.out.println("INDICES OF NON-PROJECTIVE SENTENCES ==> " + indices);

            // RANDOMLY SHUFFLE TEH LIST OF PROJECTIVE INDICES
            Collections.shuffle(projectiveIndices);

            ArrayList<Integer> projectiveSample = new ArrayList<>();
            int s = 0;
            while (projectiveSample.size()<500){
                int index = projectiveIndices.get(s);
                System.out.println(index);
                if (parsedDependenciesConLL.get(index).size()<=23) {
                    projectiveSample.add(index);
                }
                s++;
            }

            Collections.shuffle(projectiveSample);
            ArrayList<Integer> trainProjectiveSample = new ArrayList<>();
            for (s=0;s<400;s++){
                System.out.println(projectiveSample.get(s));
                trainProjectiveSample.add(projectiveSample.get(s));
            }

            ArrayList<Integer> testProjectiveSample = new ArrayList<>();
            for (s=400;s<500;s++){
                System.out.println(projectiveSample.get(s));
                testProjectiveSample.add(projectiveSample.get(s));
            }

            // SAVING PROJECTIVE SENTENCES
            FileOutputStream fos = new FileOutputStream(PATH_PROJECTIVE);
            ObjectOutputStream outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(projectiveSentences);
            outputStream.flush();

            fos = new FileOutputStream(PATH_LIST_PROJECTIVE_TRAIN);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(trainProjectiveSample);
            outputStream.flush();

            BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_LIST_PROJECTIVE_TRAIN_TXT));
            for (int i=0;i<trainProjectiveSample.size();i++){
                writer.write(trainProjectiveSample.get(i)+"\n");
            }
            writer.close();

            fos = new FileOutputStream(PATH_LIST_PROJECTIVE_TEST);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(testProjectiveSample);
            outputStream.flush();

            writer = new BufferedWriter(new FileWriter(PATH_LIST_PROJECTIVE_TEST_TXT));
            for (int i=0;i<testProjectiveSample.size();i++){
                writer.write(testProjectiveSample.get(i)+"\n");
            }
            writer.close();

            // SAVING NONPROJECTIVE SENTENCES
            fos = new FileOutputStream(PATH_NON_PROJECTIVE);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(nonProjectiveSentences);
            outputStream.flush();


            // SAVING THE GRAMMAR IN A STREAM FILE
            fos = new FileOutputStream(PATH_DEP_GRAMMAR_STREAM);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(dg.grammar);
            outputStream.flush();

            // SAVING THE GRAMMAR IN A TEXT FILE
            writer = new BufferedWriter(new FileWriter(PATH_DEP_GRAMMAR_TXT));

            for (int i = 0; i < dg.grammar.size(); i++) {
                String rule = dg.grammar.get(i).getLeftHandSide().getXpostag() + ":" + dg.grammar.get(i).getLeftHandSide().getDeprel() + "->";
                for (int j = 0; j < dg.grammar.get(i).getRightHandSide().size(); j++) {
                    rule = rule + dg.grammar.get(i).getRightHandSide().get(j).getXpostag() + ":" + dg.grammar.get(i).getRightHandSide().get(j).getDeprel() + " ";
                }
                writer.write(rule + "\n");

            }
            writer.close();



            // SAVING ONE SIDED RULES IN A STREAM FILE
            fos = new FileOutputStream(PATH_ONE_SIDED_STREAM);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(dg.oneSidedRules);
            outputStream.flush();

            // SAVING ONE SIDED RULES IN A TEXT FILE
            writer = new BufferedWriter(new FileWriter(PATH_ONE_SIDED_TXT));
            for (String key : dg.oneSidedRules.keySet()) {
                String result = key + ":";
                for (int j = 0; j < dg.oneSidedRules.get(key).size() - 1; j++) {
                    result = result + dg.oneSidedRules.get(key).get(j) + ",";
                }
                result = result + dg.oneSidedRules.get(key).get(dg.oneSidedRules.get(key).size() - 1);
                writer.write(result + "\n");
            }
            writer.close();


            System.out.println("The number of projective sentences is=> "+count);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        /*

                if (dependencyChart.length>0) {
                    if (dependencyChart[dependencyChart.length - 1][0] != null) {
                        System.out.println(k);
                        count = count + 1;
                        projectiveSentences.add(parsedDependenciesConLL.get(k));

                        // Building the grammar from one element of tree structure of the CYK chart
                        //System.out.println("Building the grammar>>>>>>");

                        for (int i = dependencyChart.length - 1; i > 0; i--) {
                            for (int j = 0; j < dependencyChart.length - 1; j++) {
                                if (dependencyChart[i][j] != null) {
                                    NonTerminal leftHandSide = new NonTerminal(-1, "", dependencyChart[i][j].getXpostag(), dependencyChart[i][j].getDeprel(), -1, -1, -1); // Head of the relationship
                                    int i_right_1 = dependencyChart[i][j].getPointer();
                                    int i_right_2 = i - dependencyChart[i][j].getPointer() - 1;
                                    int j_right_2 = j + dependencyChart[i][j].getPointer() + 1;

                                    //System.out.println("dependencyChart[i][j].getLemma()= " + dependencyChart[i][j].getLemma() + " i_right_1= " + i_right_1 + " j= " + j + " i_right_2= " + i_right_2 + " j_right_2= " + j_right_2);
                                    NonTerminal rightHandSide1 = new NonTerminal(-1, "", dependencyChart[i_right_1][j].getXpostag(), dependencyChart[i_right_1][j].getDeprel(), -1, -1, -1);
                                    NonTerminal rightHandSide2 = new NonTerminal(-1, "", dependencyChart[i_right_2][j_right_2].getXpostag(), dependencyChart[i_right_2][j_right_2].getDeprel(), -1, -1, -1);
                                    ArrayList<NonTerminal> rightHandSide = new ArrayList<NonTerminal>();
                                    rightHandSide.add(rightHandSide1);
                                    rightHandSide.add(rightHandSide2);
                                    if (!dg.grammar.contains(new GrammaticalRule(leftHandSide, rightHandSide))) {
                                        dg.grammar.add(new GrammaticalRule(leftHandSide, rightHandSide));
                                    }
                                }
                            }
                        }



         */

        // Printing the dependency chart
                /*
                for (int i = dependencyChart.length - 1; i >= 0; i--) {
                    String result = "[";
                    for (int j = 0; j < dependencyChart.length - 1; j++) {
                        if (dependencyChart[i][j] == null) {
                            result = result + ",";
                        } else {
                            result = result + dependencyChart[i][j].getLemma() + " : " + dependencyChart[i][j].getDeprel() + " : " + dependencyChart[i][j].getPointer() + ",";
                        }
                    }
                    if (dependencyChart[i][dependencyChart.length - 1] == null) {
                        result = result + "]";
                    } else {
                        result = result + dependencyChart[i][dependencyChart.length - 1].getLemma() + " : " + dependencyChart[i][dependencyChart.length - 1].getDeprel() + " : " + dependencyChart[i][dependencyChart.length - 1].getPointer() + "]";
                    }
                    System.out.println(result);
                }
                */


            /*
            for (int l=0;l<parsedDependenciesConLL.size();l++){
                DependencyNode[][] chart = dg.transformTree(parsedDependenciesConLL.get(l));

                // Printing the dependency chart
                for (int i=chart.length-1;i>=0;i--){
                    String result = "[";
                    for (int j=0;j<chart.length-1;j++){
                        if (chart[i][j]==null){
                            result = result +",";
                        } else {
                            result = result + chart[i][j].getLemma() + ",";
                        }
                    }
                    if (chart[i][chart.length-1]==null){
                        result = result +"]";
                    } else {
                        result = result + chart[i][chart.length-1].getLemma()+ "]";
                    }
                    System.out.println(result);
                }
                // Transform to grammar
                // Add to Overall Grammar
            }
            */


    }

}
