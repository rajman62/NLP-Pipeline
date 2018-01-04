package implementations.semanticutils;

class IDWord {
    private Long value;

    IDWord(Long value) {
        this.value = value;
    }

    long getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return value.equals(o);
    }
}
