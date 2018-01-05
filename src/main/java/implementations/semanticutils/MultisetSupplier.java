package implementations.semanticutils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.function.Supplier;

public class MultisetSupplier<T> implements Supplier<Multiset<T>> {
    @Override
    public Multiset<T> get() {
        return HashMultiset.create();
    }
}
