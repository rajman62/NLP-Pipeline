package SyntacticAnalyzer;

import java.util.ArrayList;

/**
 * Created by MeryemMhamdi on 4/21/17.
 */
public class Variable {
    private NonTerminal nonTerminal;
    private ArrayList<NonTerminal> variables;

    public Variable(NonTerminal nonTerminal, ArrayList<NonTerminal> variables){
        this.nonTerminal = nonTerminal;
        this.variables = variables;
    }
    public NonTerminal getNonTerminal(){
        return this.nonTerminal;
    }
    public ArrayList<NonTerminal> getVariables(){
        return this.variables;
    }
    public void setNonTerminal(NonTerminal nonTerminal){
        this.nonTerminal = nonTerminal;
    }
    public void setVariables(ArrayList<NonTerminal> variables){
        this.variables = variables;
    }
    public String toString(){
        String result="[";
        for (int i=0;i<this.getVariables().size()-1;i++){
            result = result + this.getVariables().get(i).getXpostag()+":"+this.getVariables().get(i).getDeprel()+",";
        }
        return this.nonTerminal.getXpostag()+":"+this.nonTerminal.getDeprel() + " = "+result+this.getVariables().get(this.getVariables().size()-1)+"]";
    }

}
