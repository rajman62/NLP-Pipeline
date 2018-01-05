package implementations.semanticutils;

import com.google.common.collect.Streams;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class TrainData<C, W> {
    public long trainSetSize;
    public long numberOfWords;
    public long numberOfContexts;
    public JavaRDD<Tuple2<C, W>> trainSet;
    public JavaPairRDD<IDContext, List<IDWord>> contextTrainSet;
    public JavaPairRDD<IDWord, List<IDContext>> wordTrainSet;
    public JavaPairRDD<IDWord, List<IDContext>> wordNegativeSamplesTrainSet;
    public JavaPairRDD<IDContext, List<IDWord>> contextNegativeSamplesTrainSet;
    public JavaPairRDD<C, IDContext> contextsZippedWithID;
    public JavaPairRDD<W, IDWord> wordsZippedWithID;

    public TrainData(JavaRDD<Tuple2<C, W>> trainSetRDD, int negativeSamplingPerWord, Long seed) {
        this.trainSet = trainSetRDD;
        this.trainSetSize = trainSetRDD.count();

        JavaRDD<C> contextsUnique = trainSet.map(x -> x._1).distinct();
        JavaRDD<W> wordsUnique = trainSet.map(x -> x._2).distinct();
        numberOfWords = wordsUnique.count();
        numberOfContexts = contextsUnique.count();

        // map words and contexts to integers
        contextsZippedWithID = contextsUnique.zipWithIndex()
                .mapToPair(x -> new Tuple2<>(x._1, new IDContext(x._2)));
        wordsZippedWithID = wordsUnique.zipWithIndex()
                .mapToPair(x -> new Tuple2<>(x._1, new IDWord(x._2)));

        // create train sets
        JavaPairRDD<IDContext, IDWord> contextTrainSetIDMapping = trainSet
                .mapToPair(x -> x)
                .join(contextsZippedWithID)
                .mapToPair(x -> x._2)
                .join(wordsZippedWithID)
                .mapToPair(x -> x._2);

        JavaPairRDD<IDWord, IDContext> wordTrainSetIdMapping = contextTrainSetIDMapping.mapToPair(x -> new Tuple2<>(x._2, x._1));

        contextTrainSet = contextTrainSetIDMapping
                .groupBy(x -> x._1)
                .mapToPair(x -> new Tuple2<>(x._1, Streams.stream(x._2).map(y -> y._2).collect(toList())));

        wordTrainSet = wordTrainSetIdMapping
                .groupBy(x -> x._1)
                .mapToPair(x -> new Tuple2<>(x._1, Streams.stream(x._2).map(y -> y._2).collect(toList())));

        // getting the distribution of contexts
        JavaPairRDD<IDContext, Long> histogramContextRDD =
                contextTrainSetIDMapping
                        .mapToPair(x -> new Tuple2<>(x._1, 1L))
                        .reduceByKey((i1, i2) -> i1 + i2);

        // get negative samples
        wordNegativeSamplesTrainSet =
                negativeSampling(
                        histogramContextRDD,
                        seed,
                        negativeSamplingPerWord,
                        trainSetSize,
                        numberOfWords
                );

        contextNegativeSamplesTrainSet = wordNegativeSamplesTrainSet
                .flatMap(x -> x._2.stream().map(y -> new Tuple2<>(y, x._1)).iterator())
                .groupBy(x -> x._1)
                .mapToPair(x -> new Tuple2<>(x._1, Streams.stream(x._2).map(y -> y._2).collect(toList())));
    }


    private JavaPairRDD<IDWord, List<IDContext>> negativeSampling(JavaPairRDD<IDContext, Long> histogramContextRDD,
                                                                  long seed, int negativeSamplingPerWord,
                                                                  long trainSetSize, long numberOfWords) {
        double n = (double) trainSetSize;
        double z = histogramContextRDD.mapToDouble(x -> Math.pow(((double) x._2) / n, 0.75)).sum();

        JavaPairRDD<IDContext, Double> contextProbability = histogramContextRDD
                .mapToPair(x -> new Tuple2<>(x._1, Math.pow(((double) x._2) / n, 0.75) / z));

        JavaRDD<IDContext> expendedContext = contextProbability
                .mapPartitionsWithIndex((index, iteratorContext) ->
                        new SamplingExpensionIterator<>(
                                new Random(Objects.hash(seed, index)),
                                iteratorContext,
                                numberOfWords,
                                negativeSamplingPerWord
                        ), true)
                .flatMap(x -> Collections.nCopies(Math.toIntExact(x._2), x._1).iterator());

        double w = 1.0 / ((double) expendedContext.count());
        double[] weights = new double[Math.toIntExact(numberOfWords)];
        for (int i = 0; i < weights.length; i++)
            weights[i] = w;

        // now we spread the expended context randomly on the words, and remove the pairs that are part of the train set
        return expendedContext
                .mapPartitionsWithIndex((index, iteratorContext) -> {
                    Random random = new Random(Objects.hash(seed, index, 1));
                    int max = Math.toIntExact(numberOfWords);
                    return Streams.stream(iteratorContext)
                            .map(x -> new Tuple2<>(new IDWord((long) random.nextInt(max)), x)).iterator();
                }, false)
                .groupBy(x -> x._1)
                .mapToPair(x -> new Tuple2<>(x._1, Streams.stream(x._2).map(y -> y._2).collect(toList())))
                .join(wordTrainSet)
                .mapToPair(x -> {
                    List<IDContext> newList = new ArrayList<>(x._2._1);
                    newList.removeAll(x._2._2);
                    return new Tuple2<>(x._1, newList);
                });
    }
}
