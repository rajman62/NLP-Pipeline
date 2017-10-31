package nlpstack.streams;

import java.util.function.Function;

public interface Stream<T> extends Iterable<T> {
    <U> Stream<U> map(Function<T, U> mapFun);

    void execute();
}
