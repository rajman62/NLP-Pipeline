package implementations.syntacticutils;

public class AbstractNonTerminal extends NonTerminal {
    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isNonTerminal() {
        return true;
    }

    @Override
    public String getName() {
        throw new IllegalStateException("AbstractNonTerminal is an not a named terminal");
    }

    @Override
    public String toString() {
        return String.format("AbstractNonTerminal@%d", hashCode());
    }
}
