package implementations.semanticutils;

import org.nd4j.linalg.api.ndarray.INDArray;
import java.util.Random;
import org.nd4j.linalg.factory.Nd4j;
import scala.Serializable;
import scala.Tuple2;

import java.util.Iterator;

class RandomINDArrayInit<T> implements Iterator<Tuple2<T, INDArray>>, Serializable {
    private Random randomGenerator;
    private Iterator<T> iterator;
    private int rows;
    private int columns;
    private float min;
    private float max;

    RandomINDArrayInit(Random randomGenerator, Iterator<T> iterator, int rows, int columns, float min, float max) {
        this.randomGenerator = randomGenerator;
        this.iterator = iterator;
        this.rows = rows;
        this.columns = columns;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Tuple2<T, INDArray> next() {
        return new Tuple2<>(iterator.next(), randArray());
    }

    private INDArray randArray() {
        INDArray out = Nd4j.zeros(rows, columns);
        for (int i = 0 ; i < rows ; i++)
            for (int j = 0 ; j < columns ; j++)
                out.put(i, j, randomGenerator.nextFloat()*(max-min) + min);
        return out;
    }
}
