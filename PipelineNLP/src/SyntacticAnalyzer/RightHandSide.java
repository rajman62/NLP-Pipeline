package SyntacticAnalyzer;

import java.util.ArrayList;

/**
 * Created by MeryemMhamdi on 5/18/17.
 */
public class RightHandSide {
    private ArrayList<ProbabilisticNonTerminal> rightHandSides;
    private double score;

    public RightHandSide(ArrayList<ProbabilisticNonTerminal> rightHandSides,double score){
        this.rightHandSides = rightHandSides;
        this.score = score;
    }

    public ArrayList<ProbabilisticNonTerminal> getRightHandSides(){
        return this.rightHandSides;
    }
    public double getScore(){
        return this.score;
    }

    public void setRightHandSides(ArrayList<ProbabilisticNonTerminal> rightHandSides){
        this.rightHandSides = rightHandSides;
    }

    public void setScore(double score){
        this.score = score;
    }

    @Override
    public String toString(){
        String result = "";
        for (int i=0;i<this.rightHandSides.size();i++){
            result = result + this.rightHandSides.get(i).getXpostag() + ":" + this.rightHandSides.get(i).getDeprel()+" ";
        }
        result = result+this.score;
        return result;
    }
}
