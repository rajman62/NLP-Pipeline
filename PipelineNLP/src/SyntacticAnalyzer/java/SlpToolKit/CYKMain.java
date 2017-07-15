package SyntacticAnalyzer.java.SlpToolKit;

import java.io.IOException;

/**
 * Created by MeryemMhamdi on 5/1/17.
 */
public class CYKMain {
    public static void main(String args[]) {

        // Importing the grammar from the file

        TextualLexicon lexicon = new TextualLexicon();
        Grammar myGrammar = new Grammar(lexicon, "Phrase");
        try{
            myGrammar.importFromASCII("/Users/MeryemMhamdi/Google Drive/Semester Project/3 Implementation & Algorithms/Syntactic Analysis/");
            System.out.println("getNumberOfRules()="+myGrammar.getNumberOfRules());
            //SyntacticAnalyzer mySA = new SyntacticAnalyzer(myGrammar);
            //PChart myPChart = mySA.analyze("La petite brise la glace");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ChartElement myElement = myPChart.element[1][2];
    }
}
