package SyntacticAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/** UNIT TEST to check the correctness of CYK on a simple example
 * Created by MeryemMhamdi on 6/14/17.
 */
public class InspectingResultsExample {
    private HashMap<NonTerminal, ArrayList<ArrayList<NonTerminal>>> variables;

    public ArrayList<NonTerminal>[][] buildParsingChart(ArrayList<NonTerminal>[][] parsingChart) {

        System.out.println("Running CYK====>");
        int length = parsingChart.length;
        System.out.println("length of parsing Chart= "+length);

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

        // Applying 1 nonterminal on right hand side rules:
        //for (int i=0;i<length;i++) {
            //for (int k = 0; k < length; k++) {
                Set<NonTerminal> nonTerminals = variables.keySet();
                for (NonTerminal nonTerminal : nonTerminals) {
                    ArrayList<ArrayList<NonTerminal>> vars = variables.get(nonTerminal);
                    for(int var=0;var<vars.size();var++) {
                        ArrayList<NonTerminal> values = variables.get(nonTerminal).get(var);
                        if (values.size()==1) {
                            if (parsingChart[length-1][0].contains((values.get(0)))) {
                                parsingChart[length-1][0].add(new NonTerminal(parsingChart[length-1][0].get(0).getId(),
                                        "root",nonTerminal.getXpostag(),
                                        nonTerminal.getDeprel(),length-1, parsingChart[length-1][0].indexOf(values.get(0)), -1));
                            }
                        }
                    }
               // }
            //}
        }
        return parsingChart;
    }

    public boolean checkExistenceofTrueChart (ArrayList<NonTerminal> [][] parsingChart, ArrayList<DependencyConLL> parsedDependenciesConLL) {

        boolean exists = false;
        ArrayList<ArrayList<ArrayList<DependencyConLL>>> dependenciesAllList = new ArrayList<ArrayList<ArrayList<DependencyConLL>>>(); // list of list of dependencyConLLs for all sentences
        /**
         * Traversing the chart
         */
        System.out.println("parsingChart.length= "+parsingChart.length);
        for (int i = parsingChart.length - 1; i >= 0; i--) {
            for (int j = 0; j < parsingChart[i].length - 1; j++) {
                //System.out.println("SIZE is=>"+parsingChart[i][j].size());
                for (int k = 0; k < parsingChart[i][j].size(); k++) {
                    // For each element in the cell in the chart, traverse to check if one of the paths leads to a dependency that exists
                    int i_right_1 = parsingChart[i][j].get(k).getPointer();
                    int i_right_2 = i - parsingChart[i][j].get(k).getPointer() - 1;
                    int right1Index = parsingChart[i][j].get(k).getRight1Index();
                    System.out.println(parsingChart[i][j].get(k));
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
                                    System.out.println("ROOT REMOVED DEPENDENCY>>> "+dependencyConLL.getId());
                                    parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
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
                                        System.out.println("REMOVED DEPENDENCY>>> "+dependencyConLL.getId());
                                        parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                        //k = parsingChart[i][j].size();
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
                                        System.out.println("REMOVED DEPENDENCY>>> "+dependencyConLL.getId());
                                        parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                        //k = parsingChart[i][j].size();
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
                                    System.out.println("REMOVED DEPENDENCY>>> "+dependencyConLL.getId());
                                    parsedDependenciesConLL.remove(parsedDependenciesConLL.indexOf(dependencyConLL));
                                    //k = parsingChart[i][j].size();
                                }
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

    public static void main (String [] args){
        InspectingResultsExample is = new InspectingResultsExample();
        ArrayList<NonTerminal>[][] parsingChart = new ArrayList[5][];

        for (int i = 0; i < 5; ++i)
        {
            parsingChart[i] = new ArrayList[5];
            for (int j = 0; j < 5; ++j) {
                parsingChart[i][j] = new ArrayList<NonTerminal>();
            }
        }
        parsingChart[0][0].add(new NonTerminal(1,"the", "DT", "det", -1, -1, -1));
        parsingChart[0][1].add(new NonTerminal(2,"cat", "NN", "*", -1, -1, -1));
        parsingChart[0][1].add(new NonTerminal(2,"cat", "NN", "nsubj", -1, -1, -1));
        parsingChart[0][2].add(new NonTerminal(3,"eat", "VBG", "*", -1, -1, -1));
        parsingChart[0][3].add(new NonTerminal(4,"the", "DT", "det", -1, -1, -1));
        parsingChart[0][4].add(new NonTerminal(5,"mouse", "NNS", "*", -1, -1, -1));
        parsingChart[0][4].add(new NonTerminal(5,"mouse", "NNS", "dobj", -1, -1, -1));

        is.variables = new HashMap<NonTerminal, ArrayList<ArrayList<NonTerminal>>>();
        ArrayList<ArrayList<NonTerminal>> rightHandSides= new ArrayList<ArrayList<NonTerminal>>();
        ArrayList<NonTerminal> rightHand = new ArrayList<NonTerminal>();
        rightHand.add(new NonTerminal(1,"the", "DT", "det", -1, -1, -1));
        rightHand.add(new NonTerminal(2,"cat", "NN", "*", -1, -1, -1));
        rightHandSides.add(rightHand);

        is.variables.put(new NonTerminal(2,"cat", "NN", "nsubj", -1, -1, -1),rightHandSides);

        rightHandSides= new ArrayList<ArrayList<NonTerminal>>();
        rightHand = new ArrayList<NonTerminal>();
        rightHand.add(new NonTerminal(4,"the", "DT", "det", -1, -1, -1));
        rightHand.add(new NonTerminal(5,"mouse", "NNS", "*", -1, -1, -1));
        rightHandSides.add(rightHand);

        is.variables.put(new NonTerminal(5,"mouse", "NNS", "dobj", -1, -1, -1),rightHandSides);


        rightHandSides= new ArrayList<ArrayList<NonTerminal>>();
        rightHand = new ArrayList<NonTerminal>();
        rightHand.add(new NonTerminal(4,"ate", "VBG", "*", -1, -1, -1));
        rightHand.add(new NonTerminal(5,"mouse", "NNS", "dobj", -1, -1, -1));
        rightHandSides.add(rightHand);

        rightHand = new ArrayList<NonTerminal>();
        rightHand.add(new NonTerminal(2,"cat", "NN", "nsubj", -1, -1, -1));
        rightHand.add(new NonTerminal(4,"ate", "VBG", "*", -1, -1, -1));
        rightHandSides.add(rightHand);

        is.variables.put(new NonTerminal(4,"ate", "VBG", "*", -1, -1, -1),rightHandSides);
        /*

        rightHandSides= new ArrayList<ArrayList<NonTerminal>>();
        rightHand = new ArrayList<NonTerminal>();
        rightHand.add(new NonTerminal(4,"ate", "VBG", "*", -1, -1, -1));
        rightHand.add(new NonTerminal(5,"mouse", "NNS", "dobj", -1, -1, -1));
        rightHandSides.add(rightHand);

        rightHand = new ArrayList<NonTerminal>();
        rightHand.add(new NonTerminal(2,"cat", "NN", "nsubj", -1, -1, -1));
        rightHand.add(new NonTerminal(4,"ate", "VBG", "*", -1, -1, -1));
        rightHandSides.add(rightHand);

        is.variables.put(new NonTerminal(0,"root", "ROOT", "root", -1, -1, -1),rightHandSides);

        */

        rightHandSides= new ArrayList<ArrayList<NonTerminal>>();
        rightHand = new ArrayList<NonTerminal>();
        rightHand.add(new NonTerminal(4,"ate", "VBG", "*", -1, -1, -1));
        rightHandSides.add(rightHand);

        is.variables.put(new NonTerminal(0,"root", "ROOT", "root", -1, -1, -1),rightHandSides);


        CYK cyk = new CYK();
        parsingChart = is.buildParsingChart(parsingChart);

        cyk.printChart(parsingChart);

        ArrayList<DependencyConLL> trueDependency = new ArrayList<DependencyConLL>();
        trueDependency.add(new DependencyConLL(1,"The","the","DT","DT","-",2,"det","-","-"));
        trueDependency.add(new DependencyConLL(2,"cat","cat","NN","NN","-",3,"nsubj","-","-"));
        trueDependency.add(new DependencyConLL(3,"ate","eat","VBG","VBG","-",0,"root","-","-"));
        trueDependency.add(new DependencyConLL(4,"the","the","DT","DT","-",5,"det","-","-"));
        trueDependency.add(new DependencyConLL(5,"mice","mouse","NNS","NNS","-",3,"dobj","-","-"));

        boolean result = is.checkExistenceofTrueChart(parsingChart,trueDependency);

        System.out.println("result is=> "+result);
    }
}
