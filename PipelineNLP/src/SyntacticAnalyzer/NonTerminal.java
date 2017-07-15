package SyntacticAnalyzer;

import java.io.Serializable;

/**
 * Created by MeryemMhamdi on 5/14/17.
 */
public class NonTerminal implements Serializable {
    private double id;
    private String lemma;
    private String xpostag;
    private String deprel;
    private int pointer; // index of the ith of the leftmost righthandside
    private int right1Index; // index of Probabilistic nonterminal in the array list of the cell containing the 1st righthandside
    private int right2Index; // index of Probabilistic nonterminal in the array list of the cell containing the 2st righthandside

    public NonTerminal(double id, String lemma, String xpostag,String deprel,int pointer, int right1Index, int right2Index){
        this.id = id;
        this.lemma = lemma;
        this.xpostag = xpostag;
        this.deprel = deprel;
        this.pointer = pointer;
        this.right1Index = right1Index;
        this.right2Index = right2Index;
    }
    public NonTerminal(double id, String lemma, String xpostag, String deprel){
        this.id = id;
        this.lemma = lemma;
        this.xpostag = xpostag;
        this.deprel = deprel;
    }
    public double getId(){return this.id;}
    public String getLemma(){return this.lemma;}
    public String getXpostag (){
        return this.xpostag;
    }

    public String getDeprel(){
        return this.deprel;
    }

    public int getPointer(){
        return this.pointer;
    }

    public int getRight1Index(){
        return this.right1Index;
    }

    public int getRight2Index(){
        return this.right2Index;
    }


    public void setLemma(String lemma){ this.lemma = lemma;}
    public void setId (double id){this.id = id;}
    public void setXpostag(String xpostag){
        this.xpostag = xpostag;
    }

    public void setDeprel (String deprel){
        this.deprel = deprel;
    }

    public void setPointer(int pointer){
        this.pointer = pointer;
    }

    public void setRight1Index(int right1Index){
        this.right1Index = right1Index;
    }

    public void setRight2Index (int right2Index){
        this.right2Index = right2Index;
    }


    @Override
    public boolean equals(Object obj){
        if (obj instanceof ProbabilisticNonTerminal) {
            ProbabilisticNonTerminal rule = (ProbabilisticNonTerminal) obj;

            if (!this.getXpostag().equals(rule.getXpostag())) {
                return false;
            }
            if (!this.getDeprel().equals(rule.getDeprel())) {
                return false;
            }
            return true;
        } else if (obj instanceof NonTerminal) {
            NonTerminal rule = (NonTerminal) obj;

            if (!this.getXpostag().equals(rule.getXpostag())) {
                return false;
            }
            if (!this.getDeprel().equals(rule.getDeprel())) {
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

    public String toString(){
        return this.id+ ":"+this.lemma + ":" + this.xpostag + ":" + this.deprel  + ":pointer=" +
                this.pointer + ":right1Index="+this.right1Index+":right2Index="+this.right2Index;
    }
}

