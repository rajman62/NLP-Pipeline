package SyntacticAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<Node> nodes;

    public Graph(){
        nodes = new ArrayList<Node>();
    }
    public List<Node> getNodes(){
        return this.nodes;
    }

    public void addEdge(Node start, Node end){
        if (!nodes.contains(start)){
            nodes.add(start);
        }
        if(!nodes.contains(end)){
            nodes.add(end);
        }
        Edge edge = new Edge(start,end);
        start.addConnection(end);
    }
    public void addNode(Node node){
        nodes.add(node);
    }
    public void printGraph(){
        Node start;
        for (int i=0;i<nodes.size();i++) {
            System.out.println("Node: "+nodes.get(i).getValue());
            for (int j = 0; j < nodes.get(i).getConnections().size(); j++) {
                System.out.println("-> "+nodes.get(i).getConnections().get(j).getValue());
            }
        }
    }

    public ArrayList<ArrayList<String>> traverseGraph(){
        ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
        Node start = nodes.get(0);
        for (int j = 0; j < start.getConnections().size(); j++) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(start.getConnections().get(j).getValue());
        }
        return lists;
    }


}
