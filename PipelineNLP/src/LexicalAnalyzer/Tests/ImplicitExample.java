package LexicalAnalyzer.Tests;

import LexicalAnalyzer.FSA.Automaton;
import LexicalAnalyzer.FSA.BasicOperations;
import LexicalAnalyzer.FSA.RegExp;

import java.util.ArrayList;

/**
 * Created by MeryemMhamdi on 4/19/17.
 */
public class ImplicitExample {
    public static void main(String args[]){

        RegExp verbs = new RegExp("eat|abat|achor|go|eat go|abat achor|abat achor king|achor king|read|open|work");
        RegExp nouns = new RegExp("1|king|queen|man|woman");
        RegExp adj = new RegExp("easy|quick|amusing");
        RegExp digit = new RegExp("[0-9]+");
        RegExp fraction = new RegExp("([0-9]+)\\.([0-9]+)");
        RegExp sep = new RegExp(";|,| |:");
        //|?|!|\.|...|; |, |: |? |! |!!|!!!|??|???|( )+

        RegExp nonsep1 = verbs.makeUnion(verbs,nouns);
        RegExp nonsep = verbs.makeUnion(nonsep1,adj);
        nonsep = verbs.makeUnion(nonsep,digit);
        nonsep = verbs.makeUnion(nonsep,fraction);


        Automaton sepAutomaton = sep.toAutomaton();
        Automaton nonSepAutomaton = nonsep.toAutomaton();


        String test = "abat achor king";

        /*
        System.out.println("******************Printing the Non Sep Automaton**********************");
        System.out.println(nonSepAutomaton.toString());
        System.out.println("**************************************************************");

        System.out.println("******************Printing the Sep Automaton**********************");
        System.out.println(sepAutomaton.toString());
        System.out.println("**************************************************************");
        */

        ArrayList<BasicOperations.Edge> indices = sepAutomaton.traverseImplicitSolution(nonSepAutomaton,sepAutomaton,test);

        /*
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
        */


        for (int i=0;i<indices.size();i++){
            System.out.println(indices.get(i));
        }


    }
}
