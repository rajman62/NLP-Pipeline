package LexicalAnalyzer.FSA;
import java.util.List;
import java.util.ArrayList;

public class Node{
    private int key;
    private String value;
    public List<Node> connections;

    public void addConnection(Node node){
        connections.add(node);
    }

    public List<Node> getConnections(){
        return connections;
    }
    public Node(int key, String value){
        this.key = key;
        this.value = value;
        this.connections = new ArrayList<Node>();
    }

    public int getKey(){
        return this.key;
    }
    public String getValue(){
        return this.value;
    }

    public void setKey(int key){
        this.key = key;
    }
    public void setValue(String value){
        this.value = value;
    }

    public String toString(){
        return "Key: "+this.key + " Value: "+this.value;
    }
}