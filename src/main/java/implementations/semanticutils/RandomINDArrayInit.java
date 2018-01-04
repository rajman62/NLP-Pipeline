package implementations.semanticutils;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.factory.Nd4j;
import scala.Tuple2;

import java.util.Iterator;

class RandomINDArrayInit<T> implements Iterator<Tuple2<T, INDArray>> {
    private Random randomGenerator;
    private Iterator<T> iterator;
    private int[] shape;
    private double min;
    private double max;

    RandomINDArrayInit(Random randomGenerator, Iterator<T> iterator, int[] shape, double min, double max) {
        this.randomGenerator = randomGenerator;
        this.iterator = iterator;
        this.shape = shape;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Tuple2<T, INDArray> next() {
        return new Tuple2<>(iterator.next(), Nd4j.rand(shape, min, max, randomGenerator));
    }
}
