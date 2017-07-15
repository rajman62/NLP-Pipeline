package LexicalAnalyzer.Tests;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.BasicOperations;
import LexicalAnalyzer.FSA.RegExp;

import java.util.ArrayList;

/**
 * Created by MeryemMhamdi on 4/10/17.
 */
public class ExplicitExample {

    public static void main(String args[]){
        RegExp verbs = new RegExp("eat|abat|achor|go|eat go|abat achor|abat achor king|achor king|read|open|work");
        RegExp nouns = new RegExp("1|king|queen|man|woman");
        RegExp adj = new RegExp("easy|quick|amusing");
        RegExp digit = new RegExp("[0-9]+");
        RegExp fraction = new RegExp("([0-9]+)\\.([0-9]+)");
        RegExp sep = new RegExp(";|,| |:|?|!|\\.");


        RegExp start = new RegExp("S");

        RegExp endSep = new RegExp(".|E");

        RegExp end = new RegExp("E");
        RegExp endNonSep = sep.makeUnion(sep,end);



        RegExp nonsep1 = verbs.makeUnion(verbs,nouns);
        RegExp nonsep = verbs.makeUnion(nonsep1,adj);
        nonsep = verbs.makeUnion(nonsep,digit);
        nonsep = verbs.makeUnion(nonsep,fraction);

        RegExp nonSepEntry1 = nonsep.makeConcatenation(start,nonsep);
        RegExp nonSepEntry = nonSepEntry1.makeConcatenation(nonSepEntry1,endNonSep);

        RegExp sepEntry1 = sep.makeConcatenation(start,sep);
        RegExp sepEntry = sepEntry1.makeConcatenation(sepEntry1,endSep);


        RegExp lexicon = sepEntry.makeUnion(sepEntry,nonSepEntry);
        Automaton a = lexicon.toAutomaton();

        String test = "king.";

        //System.out.println("******************Printing the Automaton**********************");
        //System.out.println(a.toString());
        //System.out.println("**************************************************************");

        ArrayList<ArrayList<BasicOperations.Edge>> chart = a.traverseExplicitSolution(test);

        System.out.println("=====================Printing Chart=================");
        for (int k=chart.size()-1;k>=0;k--){
            String result = "[";
            for (int l=0;l<chart.get(k).size();l++){
                String edge ;
                if (chart.get(k).get(l)==null){
                    edge = "";
                }
                else {
                    edge = chart.get(k).get(l).toString();
                }
                if (l == chart.get(k).size()-1){
                    result = result + edge;
                }
                else {
                    result = result + edge + ",";
                }
            }
            System.out.println(result+"]");
        }
        System.out.println("==========================================================");


    }
}
