package SyntacticAnalyzer;


/** Class to encapsulate an item of a cell in probabilistic CYK chart with information how to recover what produced it
 * namely the position and order of the items that produces it (the right hand sides) that led to the left hand side
 * @author MeryemMhamdi
 * @date 5/18/17.
 */

public class ProbabilisticNonTerminal {
    private String lemma;
    private String xpostag;
    private String deprel;
    private double score;
    private int pointer; // index of the ith of the leftmost righthandside
    private int right1Index; // index of Probabilistic nonterminal in the array list of the cell containing the 1st righthandside
    private int right2Index; // index of Probabilistic nonterminal in the array list of the cell containing the 2st righthandside

    public ProbabilisticNonTerminal(String lemma, String xpostag,String deprel,double score,int pointer, int right1Index, int right2Index){
        this.lemma = lemma;
        this.xpostag = xpostag;
        this.deprel = deprel;
        this.score = score;
        this.pointer = pointer;
        this.right1Index = right1Index;
        this.right2Index = right2Index;
    }

    public String getLemma(){return this.lemma;}

    public String getXpostag (){
        return this.xpostag;
    }

    public String getDeprel(){
        return this.deprel;
    }

    public double getScore(){
        return this.score;
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

    public void setLemma(String lemma){this.lemma = lemma;}

    public void setXpostag(String xpostag){
        this.xpostag = xpostag;
    }

    public void setDeprel (String deprel){
        this.deprel = deprel;
    }

    public void setScore (double score){
        this.score = score;
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
        } else {
            return false;
        }
    }

    public String toString(){
        return this.lemma + ":" + this.xpostag + ":" + this.deprel + ":score=" +this.score + ":pointer=" +
                this.pointer + ":right1Index="+this.right1Index+":right2Index="+this.right2Index;
    }

}
