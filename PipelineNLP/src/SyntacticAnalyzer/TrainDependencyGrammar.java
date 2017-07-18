package SyntacticAnalyzer;

import java.io.*;
import java.util.*;

/**
 * Class to extract Dependencies grammatical rules from treebank
 * @author MeryemMhamdi
 * @date 5/10/17.
 */
public class TrainDependencyGrammar {
    /******************************************************************************************************************/
    /**
     * LOCATION FILES TO BE CHANGED
     */
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
    /******************************************************************************************************************/


    public ArrayList<GrammaticalRule> grammar;
    public Map<String,ArrayList<String>> oneSidedRules;

    /**
     * Constructor
     */
    public TrainDependencyGrammar(){
        grammar = new ArrayList<GrammaticalRule>();
        oneSidedRules = new HashMap<String,ArrayList<String>>();
    }

    /** Extract dependencies treebank in CONLLU format and save them in a convenient format for further processing
     *
     * @param file path to file containing conLLu dependencies
     * @return listDependenciesConLL list of dependencies
     */
    public ArrayList<ArrayList<DependencyConLL>> parseDependencyConLL(String file){
        ArrayList<ArrayList<DependencyConLL>> listDependenciesConLL = new ArrayList<ArrayList<DependencyConLL>>();
        ArrayList<DependencyConLL> dependenciesConLL = new ArrayList<DependencyConLL>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null){
                if (line.length()==0){
                    listDependenciesConLL.add(dependenciesConLL);
                    dependenciesConLL = new ArrayList<DependencyConLL>();
                }
                else {
                    if (Character.isDigit(line.charAt(0))) {
                        String[] conLLElements = line.split("\\t", -1);
                        int head;
                        if (conLLElements[6].equals("_")){
                            head = -2;
                        } else {
                            head = Integer.parseInt(conLLElements[6]);
                        }
                        DependencyConLL dependencyConLL = new DependencyConLL(Double.parseDouble(conLLElements[0]),
                                conLLElements[1], conLLElements[2], conLLElements[3], conLLElements[4],
                                conLLElements[5], head, conLLElements[7], conLLElements[8], conLLElements[9]);
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

    /**
     *
     * @param grammaticalRules
     * @param dependenciesConLL
     * @return
     */
    public boolean checkForProjectivity(HashMap<DependencyNode,ArrayList<DependencyNode>> grammaticalRules
            , ArrayList<DependencyConLL> dependenciesConLL){
        boolean flag = true;
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

        if (paths.size()==truePaths.size()) {
            for (int i = 0; i < truePaths.size(); i++) {
                if (Double.compare(truePaths.get(i), paths.get(i))!=0){
                    flag = false;
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public static ArrayList<Double> swapList(ArrayList<Double> list1, ArrayList<Double> list2){
        ArrayList<Double> tmpList = new ArrayList<Double>(list1);
        list1 = new ArrayList<Double>();
        list1.addAll(list2);
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

        TrainDependencyGrammar dg = new TrainDependencyGrammar();
        try {
            FileInputStream in = new FileInputStream(PATH_TRAIN_CONLL);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = (ArrayList<ArrayList<DependencyConLL>>)
                    stream.readObject();


            String number = "CNF";
            int start = 0;
            int end = parsedDependenciesConLL.size();


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

                ArrayList<GrammaticalRule> grammaticalRules = dg.buildGrammaticalRules(parsedDependenciesConLL.get(k));

                if (grammaticalRules!=null) {
                    count = count + 1;
                    projectiveSentences.add(parsedDependenciesConLL.get(k));
                    projectiveIndices.add(k);
                    System.out.println(grammaticalRules.toString());

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
                String rule = dg.grammar.get(i).getLeftHandSide().getXpostag() + ":"
                             + dg.grammar.get(i).getLeftHandSide().getDeprel() + "->";
                for (int j = 0; j < dg.grammar.get(i).getRightHandSide().size(); j++) {
                    rule = rule + dg.grammar.get(i).getRightHandSide().get(j).getXpostag()
                            + ":" + dg.grammar.get(i).getRightHandSide().get(j).getDeprel() + " ";
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

    }

}
