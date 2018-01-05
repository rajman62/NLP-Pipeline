package implementations.semanticutils;

import java.io.Serializable;

class IDWord implements Serializable {
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
        return o instanceof IDWord && value.equals(((IDWord)o).value);
    }
}
