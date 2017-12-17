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
    public String getString() {
        throw new IllegalStateException("GhostNonTerminal cannot be used as a standard non terminal");
    }
}
