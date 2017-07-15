package LexicalAnalyzer.FSA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @param <String>
 *          Object's type in the tree.
 */
public class Tree {

    private Node head;

    private ArrayList<Tree> leafs = new ArrayList<Tree>();

    private Tree parent = null;

    private HashMap<Node, Tree> locate = new HashMap<Node, Tree>();

    public Tree(Node node) {
        this.head = node;
        locate.put(head, this);
    }

    public void addLeaf(Node root, Node leaf) {
        if (locate.containsKey(root)) {
            locate.get(root).addLeaf(leaf);
        }
        else {
            addLeaf(root).addLeaf(leaf);
        }
    }

    public Tree addLeaf(Node leaf) {
        Tree t = new Tree(leaf);
        leafs.add(t);
        t.parent = this;
        t.locate = this.locate;
        locate.put(leaf, t);
        return t;
    }

    public Tree setAsParent(Node parentRoot) {
        Tree t = new Tree(parentRoot);
        t.leafs.add(this);
        this.parent = t;
        t.locate = this.locate;
        t.locate.put(head, this);
        t.locate.put(parentRoot, t);
        return t;
    }


    public Node getHead() {
        return this.head;
    }

    public Tree getTree(Node element) {
        return locate.get(element);
    }

    public Tree getParent() {
        return parent;
    }

    public Collection<Node> getSuccessors(Node root) {
        Collection<Node> successors = new ArrayList<Node>();
        Tree tree = getTree(root);
        if (null != tree) {
            for (Tree leaf : tree.leafs) {
                successors.add(leaf.head);
            }
        }
        return successors;
    }

    public Collection<Tree> getSubTrees() {
        return leafs;
    }

    public static Collection<Node> getSuccessors(Node of, Collection<Tree> in) {
        for (Tree tree : in) {
            if (tree.locate.containsKey(of)) {
                return tree.getSuccessors(of);
            }
        }
        return new ArrayList<Node>();
    }
    public String toString() {
        return printTree(0);
    }

    private String printTree(int increment) {
        String s;
        String inc = " ";
        for (int i = 0; i < increment; ++i) {
            inc = inc.concat(" ");
        }
        s = inc.concat(head.getValue());
        for (Tree child : leafs) {
            s = s.concat("\n");
            s = s.concat(child.printTree(increment+indent));
        }
        return s;
    }
    private static final int indent = 2;


}