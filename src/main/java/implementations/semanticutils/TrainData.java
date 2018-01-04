package implementations.semanticutils;

import com.google.common.collect.Streams;
import org.apache.spark.api.java.AbstractJavaRDDLike;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.nd4j.linalg.api.rng.DefaultRandom;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class TrainData <C, W> {
    public long trainSetSize;
    public long numberOfWords;
    public JavaRDD<Tuple2<C, W>> trainSet;
    public JavaPairRDD<IDContext, List<IDWord>> contextTrainSet;
    public JavaPairRDD<IDWord, List<IDContext>> wordTrainSet;
    public JavaPairRDD<IDWord, List<IDContext>> wordNegativeSamplesTrainSet;
    public JavaPairRDD<IDContext, List<IDWord>> contextNegativeSamplesTrainSet;
    public JavaPairRDD<C, IDContext> contextsZippedWithID;
    public JavaPairRDD<W, IDWord> wordsZippedWithID;

    public TrainData(JavaRDD<Tuple2<C, W>> trainSetRDD, int negativeSamplingPerWord, JavaSparkContext sc, Long seed) {
        this.trainSet = trainSetRDD;
        trainSet.cache();

        JavaRDD<C> contextsUnique = trainSet.map(x -> x._1).distinct();
        JavaRDD<W> wordsUnique = trainSet.map(x -> x._2).distinct();

        wordsUnique.cache();

        numberOfWords = wordsUnique.count();

        // map words and contexts to integers
        contextsZippedWithID = contextsUnique.zipWithIndex()
                .mapToPair(x -> new Tuple2<>(x._1, new IDContext(x._2)));
        wordsZippedWithID = wordsUnique.zipWithIndex()
                .mapToPair(x -> new Tuple2<>(x._1, new IDWord(x._2)));

        wordsUnique.unpersist();

        contextsZippedWithID.cache();
        wordsZippedWithID.cache();

        // create train sets
        JavaPairRDD<IDContext, IDWord> contextTrainSetIDMapping = trainSet
                .mapToPair(x -> x)
                .join(contextsZippedWithID)
                .mapToPair(x -> x._2)
                .join(wordsZippedWithID)
                .mapToPair(x -> x._2);

        contextTrainSetIDMapping.cache();

        JavaPairRDD<IDWord, IDContext> wordTrainSetIdMapping = contextTrainSetIDMapping.mapToPair(x -> new Tuple2<>(x._2, x._1));

        contextTrainSet = contextTrainSetIDMapping
                .groupBy(x -> x._1)
                .mapToPair(x -> new Tuple2<>(x._1, Streams.stream(x._2).map(y -> y._2).collect(toList())));

        contextTrainSetIDMapping.unpersist();

        wordTrainSet = wordTrainSetIdMapping
                .groupBy(x -> x._1)
                .mapToPair(x -> new Tuple2<>(x._1, Streams.stream(x._2).map(y -> y._2).collect(toList())));

        // getting the distribution of contexts
        JavaPairRDD<IDContext, Long> histogramContextRDD = trainSet
                .mapToPair(x -> new Tuple2<>(x._1, x._2))
                .join(contextsZippedWithID)
                .mapToPair(x -> new Tuple2<>(x._2._2, 1L))
                .reduceByKey((i1, i2) -> i1 + i2);

        histogramContextRDD.cache();

        // get negative samples
        wordNegativeSamplesTrainSet =
                negativeSampling(
                        histogramContextRDD,
                        trainSetSize,
                        wordsZippedWithID.map(x -> x._2),
                        seed,
                        numberOfWords,
                        negativeSamplingPerWord,
                        sc
                );

        wordNegativeSamplesTrainSet.cache();
        histogramContextRDD.unpersist();

        contextNegativeSamplesTrainSet = wordNegativeSamplesTrainSet
                .flatMap(x -> x._2.stream().map(y -> new Tuple2<>(y, x._1)).iterator())
                .groupBy(x -> x._1)
                .mapToPair(x -> new Tuple2<>(x._1, Streams.stream(x._2).map(y -> y._2).collect(toList())));

        contextNegativeSamplesTrainSet.cache();
        contextTrainSet.cache();
        wordTrainSet.cache();
    }

    private <C2, W2> JavaPairRDD<W2, List<C2>> negativeSampling(JavaPairRDD<C2, Long> histogramContextRDD, long trainSetSize,
                                                                JavaRDD<W2> words, long seed, long numberOfWords,
                                                                int negativeSamplingPerWord, JavaSparkContext sc) {
        double n = (double) trainSetSize;
        double z = histogramContextRDD.mapToDouble(x -> Math.pow(((double) x._2) / n, 0.75)).sum();

        JavaPairRDD<C2, Double> contextProbability = histogramContextRDD
                .mapToPair(x -> new Tuple2<>(x._1, Math.pow(((double) x._2) / n, 0.75) / z));

        JavaRDD<C2> expendedContext = contextProbability
                .mapPartitionsWithIndex((index, iteratorContext) ->
                        new SamplingExpensionIterator<>(
                                new DefaultRandom(Objects.hash(seed, index)),
                                iteratorContext,
                                numberOfWords,
                                negativeSamplingPerWord
                        ), false)
                .flatMap(x -> Collections.nCopies(Math.toIntExact(x._2), x._1).iterator());

        double w = 1.0 / ((double) expendedContext.count());
        double[] weights = new double[Math.toIntExact(numberOfWords)];
        for (int i = 0; i < weights.length; i++)
            weights[i] = w;

        JavaRDD<C2>[] splitContexts = expendedContext.randomSplit(weights, Objects.hash(seed, trainSetSize));
        JavaRDD<List<C2>> splitContextsRDD = sc.parallelize(Arrays.asList(splitContexts)).map(AbstractJavaRDDLike::collect);
        return words.zip(splitContextsRDD);
    }
}
