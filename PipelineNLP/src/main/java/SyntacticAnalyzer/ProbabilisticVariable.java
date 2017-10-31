package SyntacticAnalyzer;

/**
 * @author MeryemMhamdi
 * @date 5/18/17.
 */
public class ProbabilisticVariable {
    private NonTerminal nonTerminal;
    private RightHandSide variables;

    public ProbabilisticVariable(NonTerminal nonTerminal, RightHandSide variables){
        this.nonTerminal = nonTerminal;
        this.variables = variables;
    }
    public NonTerminal getNonTerminal(){
        return this.nonTerminal;
    }
    public RightHandSide getVariables(){
        return this.variables;
    }
    public void setNonTerminal(NonTerminal nonTerminal){
        this.nonTerminal = nonTerminal;
    }
    public void setVariables(RightHandSide variables){
        this.variables = variables;
    }
    public String toString(){
        String result="[";
        for (int i=0;i<this.getVariables().getRightHandSides().size()-1;i++){
            result = result + this.getVariables().getRightHandSides().get(i).getXpostag()
                    +":"+this.getVariables().getRightHandSides().get(i).getDeprel()
                    +this.getVariables().getScore()+",";
        }
        return this.nonTerminal.getXpostag()+":"+this.nonTerminal.getDeprel() + " = "+result+
                this.getVariables().getRightHandSides().get(this.getVariables().getRightHandSides().size()-1)+"]";
    }
}
