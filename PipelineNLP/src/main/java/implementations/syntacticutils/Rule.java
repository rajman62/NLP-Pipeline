package implementations.syntacticutils;


import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Rule {
    private NonTerminal left;
    private Sigma[] right;

    public static Rule from(NonTerminal left, Sigma... right) {
        return new Rule(left, right);
    }

    public Rule(NonTerminal left, Sigma[] right) {
        this.left = left;
        this.right = right;
    }


    public NonTerminal getLeft() {
        return left;
    }

    public Sigma[] getRight() {
        return right;
    }

    public String toString() {
        String strRight = String.join(", ", Arrays.stream(right).map(Sigma::getString).collect(toList()));
        return String.format("%s -> %s",
                left.getString(), strRight);
    }
}
