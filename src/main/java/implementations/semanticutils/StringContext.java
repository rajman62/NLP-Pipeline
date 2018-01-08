package implementations.semanticutils;

import java.io.Serializable;
import java.util.List;

public class StringContext implements Serializable {
    private List<String> tags;

    public StringContext(List<String> tags) {
        tags.sort(String::compareTo);
        this.tags = tags;
    }

    @Override
    public int hashCode() {
        return tags.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof StringContext && tags.equals(((StringContext) o).tags);
    }
}
