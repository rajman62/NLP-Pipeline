package implementations.syntacticutils;

public abstract class Sigma {
    protected String name;

    public abstract boolean isTerminal();
    public abstract boolean isNonTerminal();

    public String getName() {
        return name;
    }
}
