package implementations.semanticutils;

import java.util.Random;
import scala.Serializable;
import scala.Tuple2;

import java.util.Iterator;

class SamplingExpensionIterator<T> implements Iterator<Tuple2<T, Long>>, Serializable {
    private Random randomGenerator;
    private Iterator<Tuple2<T, Double>> iterator;
    private double numberOfWords;
    private double negativeSamplingPerWord;

    SamplingExpensionIterator(Random randomGenerator, Iterator<Tuple2<T, Double>> iterator,
                              long numberOfWords, long negativeSamplingPerWord) {
        this.randomGenerator = randomGenerator;
        this.iterator = iterator;
        this.numberOfWords = (double) numberOfWords;
        this.negativeSamplingPerWord = (double) negativeSamplingPerWord;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Tuple2<T, Long> next() {
        Tuple2<T, Double> t = iterator.next();
        double expensionNumber = t._2 * numberOfWords * negativeSamplingPerWord;
        double integerPart = Math.floor(expensionNumber);
        double leftOverPart = expensionNumber - integerPart;
        if (randomGenerator.nextDouble() < leftOverPart)
            return new Tuple2<>(t._1, ((long) integerPart) + 1);
        else
            return new Tuple2<>(t._1, ((long) integerPart));
    }
}
