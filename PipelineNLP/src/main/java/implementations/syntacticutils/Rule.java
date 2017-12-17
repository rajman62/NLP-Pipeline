package implementations.syntacticutils;


import java.util.Arrays;
import java.util.Objects;

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
        String strRight = String.join(", ", Arrays.stream(right).map(Sigma::toString).collect(toList()));
        return String.format("%s -> %s", left.toString(), strRight);
    }

    @Override
    public int hashCode() {
        // +1 is just meant to give it a different hash from the NonTerminals
        return Objects.hash(left, Arrays.hashCode(right));
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Rule) {
            Rule rule2 = (Rule) other;
            return left.equals(rule2.left) && Arrays.equals(right, rule2.right);
        }
        else
            return false;
    }
}
