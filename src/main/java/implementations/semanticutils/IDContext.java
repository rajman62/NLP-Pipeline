package implementations.semanticutils;

import java.io.Serializable;

class IDContext implements Serializable {
    private Long value;

    IDContext(Long value) {
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
        return o instanceof IDContext && value.equals(((IDContext)o).value);
    }
}
