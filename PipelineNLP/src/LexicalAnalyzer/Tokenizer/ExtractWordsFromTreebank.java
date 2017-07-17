package LexicalAnalyzer.Tokenizer;

import LexicalAnalyzer.FSA.WordTag;
import SyntacticAnalyzer.DependencyConLL;
import SyntacticAnalyzer.TrainDependencyGrammar;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * STEP 1:
 * This class reads a conLL format file and extracts words from it
 * and builds token FSA based on that
 * INPUT===> DATASET file in CONLLU format
 * OUTPUT===> list of word lemma tags (text + serialized object)
 *            list of distinct entries (text + serialized object)
 * Created by MeryemMhamdi on 5/24/17.
 */
public class ExtractWordsFromTreebank {

    private static String DATASET_LOCATION = "/Users/MeryemMhamdi/Google Drive/Semester Project" +
            "/3 Implementation & Algorithms/Datasets/UDC/en-ud-train.conllu";
    private static String PATH_OUTPUT_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Train/";
    private static String PATH_OUTPUT_STREAM_WORDS = PATH_OUTPUT_FOLDER + "UDCWordLemmaTag.ser";
    private static String PATH_OUTPUT_TEXT_WORDS = PATH_OUTPUT_FOLDER + "UDCWordLemmaTag.txt";
    private static String PATH_OUTPUT_STREAM_DISTINCT_WORDS = PATH_OUTPUT_FOLDER + "UDCDistinctWords.ser";
    private static String PATH_OUTPUT_TXT_DISTINCT_WORDS = PATH_OUTPUT_FOLDER + "UDCDistinctWords.txt";

    private String dependencyFile;
    private ArrayList<WordTag> distinctWords;
    public ExtractWordsFromTreebank(String conllFile){
        this.dependencyFile = conllFile;
    }
    public void saveToSerFile(ArrayList<WordTag> distinctWords) throws IOException {
        FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_STREAM_WORDS);
        ObjectOutputStream s = new ObjectOutputStream(fos);
        s.writeObject(distinctWords);
        s.flush();
    }
    public void writeToTxtFile(ArrayList<WordTag> distinctWords) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_OUTPUT_TEXT_WORDS));
        for (int i=0;i<distinctWords.size();i++){
            writer.write(distinctWords.get(i).toString() + "\n");
        }
        writer.close();
    }
    public void saveDistinctWordsToSerFile(ArrayList<String> distinctWords) throws IOException {
        ArrayList<String> words = new ArrayList<String>();
        for (int i=0;i<words.size();i++){
            words.add(distinctWords.get(i));
        }
        FileOutputStream fos = new FileOutputStream(PATH_OUTPUT_STREAM_DISTINCT_WORDS);
        ObjectOutputStream s = new ObjectOutputStream(fos);
        s.writeObject(words);
        s.flush();
    }
    public void writeDistinctWordsToTxt(ArrayList<String> distinctWords) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_OUTPUT_TXT_DISTINCT_WORDS));
        for (int i=0;i<distinctWords.size();i++){
            writer.write(distinctWords.get(i) + "\n");
        }
        writer.close();
    }
    public static void main (String [] args){
        ArrayList<WordTag> wordList = new ArrayList<WordTag>();
        TrainDependencyGrammar dg = new TrainDependencyGrammar();
        ExtractWordsFromTreebank createFSAforUDP = new ExtractWordsFromTreebank(DATASET_LOCATION);
        ArrayList<ArrayList<DependencyConLL>> parsedDependenciesConLL = dg.parseDependencyConLL(createFSAforUDP.dependencyFile);
        System.out.println("NUMBER OF SENTENCES ===>"+parsedDependenciesConLL.size());
        for (int i=0;i<parsedDependenciesConLL.size();i++){
            for (int j=0;j<parsedDependenciesConLL.get(i).size();j++) {
                wordList.add(new WordTag(parsedDependenciesConLL.get(i).get(j).getForm(),parsedDependenciesConLL.get(i).get(j).getLemma(),
                        parsedDependenciesConLL.get(i).get(j).getXPosTag()));
            }
        }
        System.out.println("NUMBER OF WORDS ===>"+wordList.size());
        createFSAforUDP.distinctWords = new ArrayList<>(new HashSet(wordList));
        ArrayList<String> distincts = new ArrayList<String>();
        for (int i=0;i<createFSAforUDP.distinctWords.size();i++){
            String word = createFSAforUDP.distinctWords.get(i).getWord();
            if (!distincts.contains(word)){
                distincts.add(word);
            }
        }
        System.out.println("NUMBER OF DISTINCT WORDS ===>"+distincts.size());
        try {
            createFSAforUDP.saveToSerFile(createFSAforUDP.distinctWords);
            createFSAforUDP.writeToTxtFile(createFSAforUDP.distinctWords);

            createFSAforUDP.saveDistinctWordsToSerFile(distincts);
            createFSAforUDP.writeDistinctWordsToTxt(distincts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
