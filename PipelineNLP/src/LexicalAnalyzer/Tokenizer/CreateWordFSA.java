package LexicalAnalyzer.Tokenizer;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.RegExp;

import java.io.*;
import java.util.ArrayList;

/**
 * STEP 3:
 * This class creates wordFSA with pure word entries
 * @author MeryemMhamdi
 * @date 5/4/17.
 */
public class CreateWordFSA {
    /******************************************************************************************************************/
    /**
     * LOCATION FILES TO BE REPLACED
     */
    private static String PATH_FOLDER = "/Users/MeryemMhamdi/Google Drive/Semester Project/4 Results" +
            "/Tokenization Analysis/UDC/Test/";
    private static String CLEANED_TEXT_WORDS =PATH_FOLDER+ "REPLACEDUDCDistinctWords.txt";
    private static String PATH_WORD_FSA = PATH_FOLDER+ "UDCwordFSA.ser";
    /******************************************************************************************************************/

    public static void main(String args[]){
        try {
            ArrayList<String> cleanedWordEntries = new ArrayList<String>();
            /**
             * 1.1. Reading Words from wordsListCleaned
             */
            BufferedReader br = new BufferedReader(new FileReader(CLEANED_TEXT_WORDS));
            String line = br.readLine();

            while (line != null) {
                cleanedWordEntries.add(line);
                line = br.readLine();
            }

            /**
             * 1.2. Creating wordFSA
             */


            RegExp tok = new RegExp(cleanedWordEntries.get(0));
            Automaton wordFSA = tok.toAutomaton();

            for (int i = 1; i < cleanedWordEntries.size(); i++) {
                tok = new RegExp(cleanedWordEntries.get(i).toString());
                wordFSA = wordFSA.union(tok.toAutomaton());
            }
            FileOutputStream fos = new FileOutputStream(PATH_WORD_FSA);
            wordFSA.store(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
