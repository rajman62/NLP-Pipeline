package implementations.syntacticutils;

public class RealNonTerminal extends NonTerminal {
    private RealNonTerminal(String name) {
        this.name = name;
    }

    public static RealNonTerminal of(String name) {
        return new RealNonTerminal(name);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isNonTerminal() {
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + 1;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RealNonTerminal && name.equals(((RealNonTerminal) other).name);
    }

    @Override
    public String toString() {
        return name;
    }
}
