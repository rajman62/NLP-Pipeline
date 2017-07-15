/**
 * OPTIONAL: Helper Class for Graph based Representation of Output (alternative to the adopted Chart Based Representation)
 * Used in @see LexicalAnalyzer.FSA.BasicOperations#method()
 * @author Meryem M'hamdi
 * @date March 2017
 */
package LexicalAnalyzer.FSA;

public class Edge{
    public Node start;
    public Node end;

    public Edge(Node start, Node end){
        this.start = start;
        this.end = end;
    }

    public String toString(){
        return "Edge from: "+start.toString()+" to: "+end.toString();
    }
}