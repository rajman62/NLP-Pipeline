package implementations.lexicalutils;

import org.apache.commons.lang3.tuple.Pair;

public class Arc {
    public enum Type {TOKEN, SEP, ENDSEP}

    private Pair<Integer, Integer> arc;
    private String input;
    private Type type;

    public Arc(Pair<Integer, Integer> arc, String input, Type type) {
        this.arc = arc;
        this.input = input;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getString() {
        return input.substring(arc.getLeft(), arc.getRight() + 1);
    }

    public int getStart() {
        return arc.getLeft();
    }

    public int getEnd() {
        return arc.getRight();
    }

    @Override
    public String toString() {
        return String.format("'%s', %s, %d to %d", getString(), type.toString(), arc.getLeft(), arc.getRight());
    }
}
