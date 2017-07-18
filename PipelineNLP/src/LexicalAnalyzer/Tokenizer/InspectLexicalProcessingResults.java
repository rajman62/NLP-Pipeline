package LexicalAnalyzer.Tokenizer;

import SyntacticAnalyzer.DependencyConLL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import LexicalAnalyzer.FSA.BasicOperations;

/**
 * @author MeryemMhamdi
 * @date 6/5/17.
 */
public class InspectLexicalProcessingResults {
	/******************************************************************************************************************/
	/**
	 * LOCATION FILES TO BE CHANGED
	 */
    private static String PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Datasets/UDC/";
	private static String MORPHOLOGY_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/Morphological Analysis/UDC/Train/";
	
	private static String TOKENIZATION_PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results/Tokenization Analysis/UDC/Train/";

	private static String PATH_INFO_CHARTS = MORPHOLOGY_PATH_FOLDER+ "udc_MorphologyResults.ser";

	private static String TREEBANK_PATH_FOLDER = PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER+"parsedDependenciesConLL_train.ser";
	private static String TREEBANK_PATH_FOLDER_TEST = PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER+"parsedDependenciesConLL_test.ser";
	
	public static String TREEBANK_CHARTS = TOKENIZATION_PATH_FOLDER+ "udc_tokenizationCharts_treebank.ser";
	private static String PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
			"/Tokenization Analysis/UDC/Train/";
	private static String PATH_INPUT_SENTENCES_STREAM = PATH_FOLDER + "udc_treebank_sentencesList.ser";

	private static String PATH_OUTPUT_INDICES_FALSE = PATH_FOLDER + "treebank_indicesFALSE.ser";

	private static String PATH_OUTPUT_INDICES_NO_CHARTS = PATH_FOLDER + "treebank_indicesNOCHARTS.ser";

	private static String PATH_OUTPUT_TRUE_CHARTS = PATH_FOLDER + "trueCharts.ser";

	private static String PATH_OUTPUT_TRUE_SENTENCES = PATH_FOLDER + "trueSentences.ser";

	private static String PATH_OUTPUT_TRUE_PARSING_DEPENDENCIES = PARSED_DEPENDENCIES_TREEBANK_PATH_FOLDER + "parsedDependenciesConLL_train_true.conllu";


	private static String PATH_OUTPUT_FALSE_CHARTS_STREAM = PATH_FOLDER + "falseChartsSentences.ser";

	private static String PATH_OUTPUT_FALSE_CHARTS_TXT = PATH_FOLDER + "falseChartsSentences.txt";

	private static String NO_CHARTS_STREAM = PATH_FOLDER + "noChartsSentences.ser";

	private static String NO_CHARTS_TXT = PATH_FOLDER + "noChartsSentences.txt";

	/******************************************************************************************************************/

    public static void main (String [] args){
        try {
        	FileInputStream in = new FileInputStream(TREEBANK_CHARTS);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts = (ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>) stream.readObject();
			
			in = new FileInputStream(TREEBANK_PATH_FOLDER);
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();


			in = new FileInputStream(PATH_INPUT_SENTENCES_STREAM);
			stream = new ObjectInputStream(in);
			ArrayList<String> sentences = (ArrayList<String>) stream.readObject();



			int count_0 = 0;
			int count_1_true = 0;
			int count_1_false = 0;
			int count_more = 0;
			ArrayList<Integer> count_0List = new ArrayList<Integer>();
			ArrayList<Integer> count_moreList = new ArrayList<Integer>();
			ArrayList<Integer> count_1_trueList = new ArrayList<Integer>();
			ArrayList<Integer> count_1_falseList = new ArrayList<Integer>();

			ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>> charts1True = new ArrayList<ArrayList<ArrayList<ArrayList<BasicOperations.Edge>>>>();
			ArrayList<String> noCharts = new ArrayList<String>();
			ArrayList<String> sentences1False = new ArrayList<String>();
			ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLLTrue = new ArrayList<ArrayList<DependencyConLL>> ();
			ArrayList<String> trueSentences = new ArrayList<String>();

			for (int i=0;i<charts.size();i++){ //
				if (charts.get(i).size() ==0){
					count_0 = count_0 + 1;
					count_0List.add(i);
					noCharts.add(sentences.get(i));
				} else if (charts.get(i).size()>1){
					count_more = count_more + 1;
					count_moreList.add(i);
				} else if (charts.get(i).size()==1){
					ArrayList<Boolean> existsList = new ArrayList<Boolean>();
					int k = 0;
					int level = 0;
					if (parsedDependenciesConLL.get(i).size()<=charts.get(i).get(0).size()) {
						for (int j = 0; j < parsedDependenciesConLL.get(i).size(); j++) {
							boolean exists = false;
							if (level + j < charts.get(i).get(0).size()) {
								System.out.println("i= " + i + "j= " + j + "level= " + level + " charts.get(i).get(0).size()= " + charts.get(i).get(0).size());
								System.out.println("parsedDependenciesConLL.get(i).get(j).getForm()= " + parsedDependenciesConLL.get(i).get(j).getForm());
								String word = sentences.get(i).substring(charts.get(i).get(0).get(0).get(j + level).getStartIndex(), charts.get(i).get(0).get(0).get(j + level).getEndIndex());
								System.out.println("word=" + word);
								int length = charts.get(i).get(0).get(k).size();
								k = 1;
								if (word.equals(parsedDependenciesConLL.get(i).get(j).getForm())) {
									exists = true;
								} else if (charts.get(i).get(0).size()>1){
									length = charts.get(i).get(0).get(1).size();
									while (!word.equals(parsedDependenciesConLL.get(i).get(j).getForm()) && k < length && j + level < length) {
										System.out.println("k= " + k + " j= " + j + "length= " + length + " level= " + level);
										System.out.println("HERE");
										if (charts.get(i).get(0).get(k).get(j + level) != null) {
											word = sentences.get(i).substring(charts.get(i).get(0).get(k).get(j + level).getStartIndex()
													, charts.get(i).get(0).get(k).get(j + level).getEndIndex());
											if (word.equals(parsedDependenciesConLL.get(i).get(j).getForm())) {
												exists = true;
												level = level + k;
											}
											System.out.println("word= " + word);
										}
										k = k + 1;
										length = charts.get(i).get(0).get(k).size();
									}
								}
							}
							existsList.add(exists);
						}
						if (!existsList.contains(false)) {
							count_1_true = count_1_true + 1;
							count_1_trueList.add(i);
							charts1True.add(charts.get(i));
							trueSentences.add(sentences.get(i));
							parsedDependenciesConLLTrue.add(parsedDependenciesConLL.get(i));

						} else {
							count_1_false = count_1_false + 1;
							count_1_falseList.add(i);
							sentences1False.add(sentences.get(i));
						}
					} else {
						count_1_false = count_1_false + 1;
						count_1_falseList.add(i);
						sentences1False.add(sentences.get(i));
					}
				}
			}

			System.out.println("count_0= "+count_0+ " at=> "+count_0List);
			System.out.println("count_1_true= "+count_1_true+ " at=> "+count_1_trueList);
			System.out.println("count_1_false= "+count_1_false+ " at=> "+count_1_falseList);
			System.out.println("count_more= "+count_more+ " at=> "+count_moreList);

			FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_TRUE_CHARTS);
			ObjectOutputStream outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(charts1True);
			outputStream.flush();

			//

			fos = new FileOutputStream(PATH_OUTPUT_TRUE_SENTENCES);
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(trueSentences);
			outputStream.flush();

			fos = new FileOutputStream(PATH_OUTPUT_TRUE_PARSING_DEPENDENCIES);
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(parsedDependenciesConLLTrue);
			outputStream.flush();

			//

			fos = new FileOutputStream(PATH_OUTPUT_FALSE_CHARTS_STREAM);
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(sentences1False);
			outputStream.flush();

			fos = new FileOutputStream(PATH_OUTPUT_INDICES_FALSE);
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(count_1_falseList);
			outputStream.flush();

			BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_OUTPUT_FALSE_CHARTS_TXT));
			for (int i=0;i<sentences1False.size();i++){
				writer.write(sentences1False.get(i)+"\n\n");
			}
			writer.close();


			fos = new FileOutputStream(NO_CHARTS_STREAM);
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(noCharts);
			outputStream.flush();

			fos = new FileOutputStream(PATH_OUTPUT_INDICES_NO_CHARTS);
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(count_0List);
			outputStream.flush();

			writer = new BufferedWriter(new FileWriter(NO_CHARTS_TXT));
			for (int i=0;i<noCharts.size();i++){
				writer.write(noCharts.get(i)+"\n\n");
			}
			writer.close();



			/*
            FileInputStream in = new FileInputStream(TREEBANK_PATH_FOLDER);
            ObjectInputStream stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();


			in = new FileInputStream(TREEBANK_PATH_FOLDER_TEST);
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLLTest = (ArrayList<ArrayList<DependencyConLL>>) stream.readObject();

		
			in = new FileInputStream(PATH_INFO_CHARTS);
            stream = new ObjectInputStream(in);
            ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>> chartsInfo = (ArrayList<ArrayList<ArrayList<Map<String,ArrayList<String>>>>>) stream.readObject();

			ArrayList<Integer> lengths = new ArrayList<Integer>();
			int sum = 0;
			int max = 0;
			int min = 1200000000;
			int maxIndex = 0;
			int minIndex = 0;
			for (int i=0;i<chartsInfo.size();i++){
				lengths.add(chartsInfo.get(i).get(0).size());
				sum = sum + chartsInfo.get(i).get(0).size();
				if (chartsInfo.get(i).get(0).size() >= max ){
					max = chartsInfo.get(i).get(0).size();
					maxIndex = i;
				}
				if (chartsInfo.get(i).get(0).size() <= min && chartsInfo.get(i).get(0).size()!=0 ){
					min = chartsInfo.get(i).get(0).size();
					minIndex = i;
				}
            }
			System.out.println("Number of Tokenized Sentences: "+chartsInfo.size());
            System.out.println("Maximum is: "+max + " at index= "+maxIndex);
			System.out.println("Minimum is: "+min + " at index= "+minIndex);
			System.out.println("Average is: "+sum/chartsInfo.size());
			
			sum = 0;
			max = 0;
			min = 1200000000;
			maxIndex = 0;
			minIndex = 0;
			ArrayList<String> distinctWords = new ArrayList<String>();
			for (int i=0;i<parsedDependenciesConLL.size();i++){
				lengths.add(parsedDependenciesConLL.get(i).size());
				sum = sum + parsedDependenciesConLL.get(i).size();
				if (parsedDependenciesConLL.get(i).size() >= max){
					max = parsedDependenciesConLL.get(i).size();
					maxIndex = i;
				}
				if (parsedDependenciesConLL.get(i).size() <= min && parsedDependenciesConLL.get(i).size()!=0){
					min = parsedDependenciesConLL.get(i).size();
					minIndex = i;
				}
				for (int j=0;j<parsedDependenciesConLL.get(i).size();j++){
					if (!distinctWords.contains(parsedDependenciesConLL.get(i).get(j).getForm())){
						distinctWords.add(parsedDependenciesConLL.get(i).get(j).getForm());
					}
				}
            }
            
            for (int i=0;i<parsedDependenciesConLLTest.size();i++){
            	for (int j=0;j<parsedDependenciesConLLTest.get(i).size();j++){
					if (!distinctWords.contains(parsedDependenciesConLLTest.get(i).get(j).getForm())){
						distinctWords.add(parsedDependenciesConLLTest.get(i).get(j).getForm());
					}
				}
			}
            
			System.out.println("Number of Tokenized Sentences at treebank: "+parsedDependenciesConLL.size());
			System.out.println("Sum at treebank is: "+sum);
			System.out.println("Number of Distinct Words at treebank is: "+distinctWords.size());
            System.out.println("Maximum at treebank is: "+max + " at index= "+maxIndex);
			System.out.println("Minimum at treebank is: "+min + " at index= "+minIndex);
			System.out.println("Average at treebank is: "+sum/parsedDependenciesConLL.size());
			
			*/

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
