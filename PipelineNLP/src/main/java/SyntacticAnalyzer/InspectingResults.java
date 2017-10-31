package SyntacticAnalyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

/** Checking the results QUANTITATIVELY by calculating the following statistics:
 *    - The number of charts with 0 trees
 *    - The number of charts with 1 trees correct
 *    - The number of charts with 1 trees false
 *    - The number of charts with more trees correct
 *    - The number of charts with more trees false
 *    - Minimum number of trees
 *    - Average number of trees per chart
 *    - Maximum number of trees
 * @author MeryemMhamdi
 * @date 6/4/17.
 */
public class InspectingResults {
    /******************************************************************************************************************/
    /**
     * LOCATION FILES TO BE CHANGED
     */
    private static String DATASET_LOCATION = "/Users/MeryemMhamdi/Google Drive/Semester Project" +
            "/3 Implementation & Algorithms/Datasets/UDC/";
    private static String SYNTAX_INPUT_PATH_FOLDER = "/Users/MeryemMhamdi/EPFL/Spring2017/SemesterProject/Results/Big Data/";

    private static String PATH_TRAIN_DEPENDENCIES =  DATASET_LOCATION + "parsedDependenciesConLL_train_true.conllu";
    private static String OUTPUT_CYK_CHARTS_STREAM = SYNTAX_INPUT_PATH_FOLDER +"udc_parsingCharts1stPass_TEST_NEW.ser";
    private static String PATH_LIST_PROJECTIVE_TRAIN = SYNTAX_INPUT_PATH_FOLDER+ "projective_indices_test.ser";
    /******************************************************************************************************************/

    public static void main (String [] args){
        try {
            System.out.println("LOADING TRUE DEPENDENCIES");
            FileInputStream in = new FileInputStream(PATH_TRAIN_DEPENDENCIES);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> trueDependencies =
                    (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();


            System.out.println("LOADING CYK CHARTS");
            in = new FileInputStream(OUTPUT_CYK_CHARTS_STREAM);
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<NonTerminal> [][]> parsingCharts =
                    (ArrayList<ArrayList<NonTerminal> [][]>) stream.readObject();
            CYK cyk = new CYK();

            in = new FileInputStream(PATH_LIST_PROJECTIVE_TRAIN);
            stream = new ObjectInputStream(in);
            ArrayList<Integer> indices = (ArrayList<Integer>) stream.readObject();


            int true_charts = 0;
            int progress =0;
            int start = 0;
            int sum = 0;
            ArrayList<Integer> treesNumbers = new ArrayList<Integer> ();
            ArrayList<Boolean> resultsFlags = new ArrayList<Boolean> ();
            for (int partial=start;partial<start+100;partial++) {
                int i = indices.get(partial);
                for (int j=0;j<trueDependencies.get(i).size();j++){
                    System.out.println(trueDependencies.get(i).get(j).getForm());
                }
                int length = parsingCharts.get(progress).length;
                ArrayList<NonTerminal> top = parsingCharts.get(progress)[length-1][0];
                int numberOfTrees = 0;
                for (NonTerminal root: top){
                    if (root.getDeprel().equals("root")){
                        numberOfTrees++;
                    }
                }
                treesNumbers.add(numberOfTrees);
                sum = sum + numberOfTrees;


                boolean result = cyk.checkExistenceofTrueChart(parsingCharts.get(partial-start),trueDependencies.get(i));
                System.out.println("i= "+i+ " result= "+result);
                resultsFlags.add(result);
                if (result ==true){
                    true_charts ++;
                }
                progress++;
            }
            double average = sum/treesNumbers.size();
            System.out.println("The number of sentences for which there exists the true trees:"+ true_charts);
            System.out.println("Maximum number of trees:"+ Collections.max(treesNumbers));
            int min = 0;

            System.out.println("Average number of trees:"+ average);
            int number0 = 0;
            int number1 = 0;
            int number1false = 0;
            int moreCorrect = 0;
            int moreFalse = 0;
            for (int i=0;i<treesNumbers.size();i++) {
                if (treesNumbers.get(i)==0){
                    number0++;
                }
                if (treesNumbers.get(i)==1 && resultsFlags.get(i)==true){
                    number1++;
                }
                if (treesNumbers.get(i)==1 && resultsFlags.get(i)==false){
                    number1false++;
                }
                if(treesNumbers.get(i)>1 && resultsFlags.get(i)==true){
                    moreCorrect++;
                }
                if(treesNumbers.get(i)>1 && resultsFlags.get(i)==false){
                    moreFalse++;
                }

            }
            System.out.println("The number of 0 trees:"+number0);
            System.out.println("The number of 1 trees correct:"+number1);
            System.out.println("The number of 1 trees false:"+number1false);
            System.out.println("The number of more trees correct:"+moreCorrect);
            System.out.println("The number of more trees false:"+moreFalse);
            System.out.println("Minimum number of trees:"+ Collections.min(treesNumbers));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




    }
}
