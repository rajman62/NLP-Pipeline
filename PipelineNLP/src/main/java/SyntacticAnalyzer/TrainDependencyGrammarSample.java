package SyntacticAnalyzer;

import java.io.*;
import java.util.ArrayList;

/** Training and Saving Dependency Grammar on a smaller random sample of the dataset
 * @author MeryemMhamdi
 * @date 6/11/17.
 */
public class TrainDependencyGrammarSample {
    /******************************************************************************************************************/
    /**
     * LOCATION FILES TO BE CHANGED
     */
    private static String OUTPUT_PATH_FOLDER = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/Big Data/";
    private static String DATASET_LOCATION = "/Users/MeryemMhamdi/Google Drive/Semester Project" +
            "/3 Implementation & Algorithms/Datasets/UDC/";
    private static String PATH_TRAIN_CONLL =  DATASET_LOCATION + "parsedDependenciesConLL_train_true.conllu";
    private static String PATH_LIST_PROJECTIVE_TRAIN = OUTPUT_PATH_FOLDER+ "projective_indices_train.ser";
    private static String PATH_LIST_PROJECTIVE_TEST = OUTPUT_PATH_FOLDER+ "projective_indices_test.ser";

    private static String PATH_PROJECTIVE_TRAIN_SENTENCES = OUTPUT_PATH_FOLDER+"sentences_projective_train.ser";
    private static String PATH_PROJECTIVE_TEST_SENTENCES = OUTPUT_PATH_FOLDER+"sentences_projective_test.ser";

    private static String PATH_DEP_GRAMMAR_STREAM = OUTPUT_PATH_FOLDER+"Dependency_Grammar_TRAIN_ROOT.ser";
    private static String PATH_DEP_GRAMMAR_TXT = OUTPUT_PATH_FOLDER+"Dependency_Grammar_TRAIN_ROOT.txt";

    private static  String PATH_ONE_SIDED_STREAM = OUTPUT_PATH_FOLDER+"One_sided_rules_TRAIN_ROOT.ser";
    private static  String PATH_ONE_SIDED_TXT = OUTPUT_PATH_FOLDER+"One_sided_rules_TRAIN_ROOT.txt";
    /******************************************************************************************************************/

    public static void main(String args[]) {

        TrainDependencyGrammar dg = new TrainDependencyGrammar();

        try {
            FileInputStream in = new FileInputStream(PATH_TRAIN_CONLL);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL =
                    (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();

            System.out.println("parsedDependenciesConLL.size()= " + parsedDependenciesConLL.size());

            in = new FileInputStream(PATH_LIST_PROJECTIVE_TRAIN);
            stream = new ObjectInputStream(in);
            ArrayList<Integer> trainIndices = (ArrayList<Integer>) stream.readObject();


            in = new FileInputStream(PATH_LIST_PROJECTIVE_TEST);
            stream = new ObjectInputStream(in);
            ArrayList<Integer> testIndices = (ArrayList<Integer>) stream.readObject();

            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLLTEST =  new ArrayList<ArrayList<DependencyConLL>>();

            for (int testIndex: testIndices){
                parsedDependenciesConLLTEST.add(parsedDependenciesConLL.get(testIndex));
            }

            int count = 0;

            ArrayList<ArrayList<DependencyConLL>> projectiveSentences = new ArrayList<ArrayList<DependencyConLL>>();
            for (int k: trainIndices) { //
                System.out.println("Progress is=>" + k);

                ArrayList<GrammaticalRule> grammaticalRules = dg.buildGrammaticalRules(parsedDependenciesConLL.get(k));

                if (grammaticalRules != null) {
                    count = count + 1;
                    projectiveSentences.add(parsedDependenciesConLL.get(k));
                    System.out.println(grammaticalRules.toString());

                    // CONVERTING RULES WITH MORE THAN TWO RIGHT HANDSIDES
                    for (int j = 0; j < grammaticalRules.size(); j++) {
                        NonTerminal left = grammaticalRules.get(j).getLeftHandSide();
                        ArrayList<NonTerminal> right = grammaticalRules.get(j).getRightHandSide();
                        if (right.size() > 2) {
                            boolean flag = false;
                            boolean first = true;
                            NonTerminal headDuplicate = left;
                            for (int r = 0; r < right.size(); r++) {
                                if (Double.compare(right.get(r).getId(), left.getId()) == 0) {
                                    headDuplicate = right.get(r);
                                }
                            }

                            for (int r = 0; r < right.size(); r++) {
                                if (Double.compare(right.get(r).getId(), left.getId()) == 0) {
                                    flag = true;
                                } else {
                                    if (flag == true) {
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

                        } else if (right.size() == 2) {
                            GrammaticalRule grammaticalRule = new GrammaticalRule(left, right);
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
                }
            }


            // SAVING TRAIN PROJECTIVE SENTENCES
            FileOutputStream fos = new FileOutputStream(PATH_PROJECTIVE_TRAIN_SENTENCES);
            ObjectOutputStream outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(projectiveSentences);
            outputStream.flush();

            // SAVING TEST PROJECTIVE SENTENCES
            fos = new FileOutputStream(PATH_PROJECTIVE_TEST_SENTENCES);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(parsedDependenciesConLLTEST);
            outputStream.flush();


            // SAVING THE GRAMMAR IN A STREAM FILE
            fos = new FileOutputStream(PATH_DEP_GRAMMAR_STREAM);
            outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(dg.grammar);
            outputStream.flush();

            // SAVING THE GRAMMAR IN A TEXT FILE
            BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_DEP_GRAMMAR_TXT));

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


            System.out.println("The number of projective sentences is=> " + count);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
