package implementations.lexicalutils;

import dk.brics.automaton.RunAutomaton;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

public class Arc {
    public enum Type {TOKEN, VISIBLE_SEP, INVISIBLE_SEP, VISIBLE_EOS, INVISIBLE_EOS}

    private Pair<Integer, Integer> arc;
    private String input;
    private Type type;

    public Arc(Pair<Integer, Integer> arc, String input, Type type) {
        this.arc = arc;
        this.input = input;
        this.type = type;
    }

    public static Arc sepFromPatterns(Pair<Integer, Integer> arc, String input,
                                      RunAutomaton invisibleCharacterPattern, RunAutomaton eosSeparatorPattern) {
        String subString = input.substring(arc.getLeft(), arc.getRight() + 1);
        if (invisibleCharacterPattern.run(subString)) {
            if (eosSeparatorPattern.run(subString))
                return new Arc(arc, input, Type.INVISIBLE_EOS);
            else
                return new Arc(arc, input, Type.INVISIBLE_SEP);
        } else {
            if (eosSeparatorPattern.run(subString))
                return new Arc(arc, input, Type.VISIBLE_EOS);
            else
                return new Arc(arc, input, Type.VISIBLE_SEP);
        }
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

    public boolean isEOS() {
        return type.equals(Arc.Type.INVISIBLE_EOS) || type.equals(Arc.Type.VISIBLE_EOS);
    }

    public boolean isVisible() {
        return type.equals(Type.TOKEN) || type.equals(Type.VISIBLE_SEP) || type.equals(Type.VISIBLE_EOS);
    }

    @Override
    public String toString() {
        return String.format("'%s', %s, %d to %d", getString(), type.toString(), arc.getLeft(), arc.getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(arc, type, input);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Arc) {
            Arc arc2 = (Arc) other;
            return type.equals(arc2.type) && arc.equals(arc2.arc) && input.equals(arc2.input);
        }
        return false;
    }
}
