package SyntacticAnalyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by MeryemMhamdi on 6/4/17.
 */
public class InspectingResults1 {
    private static String OUTPUT_PATH_FOLDER = "C:\\Users\\mmhamdi\\Desktop\\BigData\\";
    private static String PATH_TRAIN_DEPENDENCIES = OUTPUT_PATH_FOLDER+"parsedDependenciesConLL_Part1.ser";
    private static String OUTPUT_CYK_CHARTS_STREAM = OUTPUT_PATH_FOLDER;
    public static void main (String [] args){
        try {
			/*
            System.out.println("LOADING TRUE DEPENDENCIES");
            FileInputStream in = new FileInputStream(PATH_TRAIN_DEPENDENCIES);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> trueDependencies = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();
			*/

			int count = 0;
			int sum = 0;
			for (int i=0;i<=6600;i=i+200){
				System.out.println("LOADING CYK CHARTS");
				FileInputStream in1 = new FileInputStream(OUTPUT_CYK_CHARTS_STREAM +"udc_parsingCharts1stPass_"+i+".ser");
				ObjectInputStream stream1 = new ObjectInputStream(in1);
				ArrayList<ArrayList<NonTerminal> [][]> parsingCharts = (ArrayList<ArrayList<NonTerminal> [][]>) stream1.readObject();

				for (int j=0;j<parsingCharts.size();j++){
					sum = sum +1;
					if (parsingCharts.get(j)[parsingCharts.get(j).length-1][0].size()>1){
						count = count +1;
					}
				}
			}
			System.out.println("The total number of charts:"+sum);
			System.out.println("The count of sentences containing parsing:"+count);
			
			
			/*
            CYK cyk = new CYK();

            int true_charts = 0;
            for (int i=0;i<parsingCharts.size();i++){
                cyk.printChart(parsingCharts.get(i));
                boolean result = cyk.checkExistenceofTrueChart(parsingCharts.get(i),trueDependencies.get(i));
                System.out.println("i= "+i+ " result= "+result);
                if (result ==true){
                    true_charts ++;
                }
            }*/
            //System.out.println("The number of sentences for which there exists the true trees:"+ true_charts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




    }
}
