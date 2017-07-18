package SyntacticAnalyzer;

import java.io.Serializable;
import java.util.ArrayList;

/** Class to encapsulate a rule of the context free grammar in CNF or nonCNF (A -> B C ... Z)
 * where A is leftHandSide and the list B C ... Z is rightHandSide
 * @author MeryemMhamdi
 * @date 5/14/17.
 */
public class GrammaticalRule implements Serializable {
    private NonTerminal leftHandSide;
    private ArrayList<NonTerminal> rightHandSide;

    public GrammaticalRule(NonTerminal leftHandSide, ArrayList<NonTerminal> rightHandSide){
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    public NonTerminal getLeftHandSide(){
        return this.leftHandSide;
    }
    public ArrayList<NonTerminal> getRightHandSide(){
        return this.rightHandSide;
    }
    public void setLeftHandSide(NonTerminal leftHandSide){
        this.leftHandSide = leftHandSide;
    }
    public void setRightHandSide(ArrayList<NonTerminal> rightHandSide){
        this.rightHandSide = rightHandSide;
    }

    public void addToRightHandSide(NonTerminal nonTerminal){
        this.rightHandSide.add(nonTerminal);
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof GrammaticalRule) {
            GrammaticalRule rule = (GrammaticalRule) obj;
            if (this.getRightHandSide().size() != rule.getRightHandSide().size()) {
                return false;
            }
            if (!this.getLeftHandSide().equals(rule.getLeftHandSide())) {
                return false;
            }
            for (int i = 0; i < this.getRightHandSide().size(); i++) {
                if (!this.getRightHandSide().get(i).equals(rule.getRightHandSide().get(i))) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        int hash = 134;
        hash = (int) (hash * 17 + this.getLeftHandSide().getId());
        for (int i=0;i<this.getRightHandSide().size();i++) {
            hash = (int) (hash * 31 + this.getLeftHandSide().getId());
        }
        return hash;
    }

    public String toString(){
        String right = "";
        for (int i=0; i<this.getRightHandSide().size()-1;i++){
            right = right + this.getRightHandSide().get(i) + ",";
        }
        right = right + this.getRightHandSide().get(this.getRightHandSide().size()-1);
        return this.getLeftHandSide() + "=> " + right;
    }
}
