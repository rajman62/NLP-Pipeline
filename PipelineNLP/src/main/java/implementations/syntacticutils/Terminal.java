package implementations.syntacticutils;

public class Terminal extends Sigma {
    private Terminal(String name) {
        this.name = name;
    }

    public static Terminal of(String name) {
        return new Terminal(name);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isNonTerminal() {
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Terminal && name.equals(((Terminal) other).name);
    }

    @Override
    public String toString() {
        return name;
    }
}
