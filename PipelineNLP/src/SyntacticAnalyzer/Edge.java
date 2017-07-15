package SyntacticAnalyzer;

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