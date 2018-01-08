package implementations.semanticutils;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.nd4j.linalg.api.ndarray.INDArray;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple5;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class WordEmbeddingsGradientDescentImpl2<C, W> {
    private int k;
    private float gamma;
    private float lambdaW;
    private float lambdaC;
    private int maxIter;
    private JavaSparkContext sc;

    public WordEmbeddingsGradientDescentImpl2(int k, float gamma, float lambdaW, float lambdaC, int maxIter, JavaSparkContext sc) {
        this.k = k;
        this.gamma = gamma;
        this.lambdaW = lambdaW;
        this.lambdaC = lambdaC;
        this.maxIter = maxIter;
        this.sc = sc;
    }

    public void train(TrainData<C, W> trainData, long seed) {
        JavaPairRDD<IDContext, INDArray> contextVectors = initContextVectors(seed + 1L, trainData.contextsZippedWithID.map(x -> x._2));
        JavaPairRDD<IDWord, INDArray> wordVectors = initWordVectors(seed + 2L, trainData.wordsZippedWithID.map(x -> x._2));

        GradientCalculations gradientCalculations = new GradientCalculations(gamma, k);

        JavaPairRDD<IDWord, Tuple2<IDContext, Integer>> positiveSamples =
                trainData.wordTrainSet.flatMapToPair(
                        x -> x._2.entrySet().stream()
                                .map(y -> new Tuple2<>(x._1, new Tuple2<>(y.getElement(), y.getCount())))
                                .iterator()
                );

        JavaPairRDD<IDWord, Tuple2<IDContext, Integer>> negativeSamples =
                trainData.wordNegativeSamplesTrainSet.flatMapToPair(
                        x -> x._2.entrySet().stream()
                                .map(y -> new Tuple2<>(x._1, new Tuple2<>(y.getElement(), y.getCount())))
                                .iterator()
                );

        positiveSamples.cache();
        negativeSamples.cache();
        wordVectors.cache();
        contextVectors.cache();

        int numPartitionWords = (int) Math.ceil(((double) trainData.numberOfWords) / 100.0);
        wordVectors.repartition(numPartitionWords);

        int numPartitionContexts = (int) Math.ceil(((double) trainData.numberOfContexts) / 100.0);
        contextVectors.repartition(numPartitionContexts);

        float lambdaW2 = lambdaW;
        float lambdaC2 = lambdaC;

        for (int i = 0; i < maxIter; i++) {
            // tuple 3 corresponds to IDWord, VectorWord, couple count
            JavaPairRDD<IDContext, Tuple3<IDWord, INDArray, Integer>> rdd1 = wordVectors
                    .join(positiveSamples)
                    .mapToPair(x -> new Tuple2<>(x._2._2._1, new Tuple3<>(x._1, x._2._1, x._2._2._2)));
            JavaRDD<Tuple5<IDWord, INDArray, IDContext, INDArray, Integer>> fullPositiveTrain = contextVectors
                    .join(rdd1)
                    .map(x -> new Tuple5<>(x._2._2._1(), x._2._2._2(), x._1, x._2._1, x._2._2._3()));


            fullPositiveTrain.cache();


            JavaPairRDD<IDContext, Tuple3<IDWord, INDArray, Integer>> rdd2 = wordVectors
                    .join(negativeSamples)
                    .mapToPair(x -> new Tuple2<>(x._2._2._1, new Tuple3<>(x._1, x._2._1, x._2._2._2)));
            JavaRDD<Tuple5<IDWord, INDArray, IDContext, INDArray, Integer>> fullNegativeTrain = contextVectors
                    .join(rdd2)
                    .map(x -> new Tuple5<>(x._2._2._1(), x._2._2._2(), x._1, x._2._1, x._2._2._3()));


            fullNegativeTrain.cache();


            // calculating loss
            JavaDoubleRDD positiveSampleLoss = fullPositiveTrain
                    .mapToDouble(x -> Math.log(Model.sigmoid(x._2(), x._4())*x._5()));
            JavaDoubleRDD negativeSampleLoss = fullNegativeTrain
                    .mapToDouble(x -> Math.log(Model.sigmoid(x._2().neg(), x._4())*x._5()));

            System.out.println(String.format(
                    "%s - iteration %d training error: %f",
                    (new Date()).toString(),
                    i,
                    positiveSampleLoss.union(negativeSampleLoss).sum()
            ));


            JavaPairRDD<IDWord, INDArray> positiveGradientWord = fullPositiveTrain
                    .mapToPair(x -> new Tuple2<>(x._1(), new Tuple2<>(
                            gradientCalculations.positiveSampleGradient(x._2(), x._4(), x._5()),
                            x._5())))
                    .reduceByKey((x, y) -> new Tuple2<>(x._1.add(y._1), x._2+y._2))
                    .mapToPair(x -> new Tuple2<>(x._1, x._2._1.div((float) x._2._2)));


            JavaPairRDD<IDContext, INDArray> positiveGradientContext = fullPositiveTrain
                    .mapToPair(x -> new Tuple2<>(x._3(), new Tuple2<>(
                            gradientCalculations.positiveSampleGradient(x._4(), x._2(), x._5()),
                            x._5())))
                    .reduceByKey((x, y) -> new Tuple2<>(x._1.add(y._1), x._2+y._2))
                    .mapToPair(x -> new Tuple2<>(x._1, x._2._1.div((float) x._2._2)));



            JavaPairRDD<IDWord, INDArray> negativeGradientWord = fullNegativeTrain
                    .mapToPair(x -> new Tuple2<>(x._1(), new Tuple2<>(
                            gradientCalculations.negativeSampleGradient(x._2(), x._4(), x._5()),
                            x._5())))
                    .reduceByKey((x, y) -> new Tuple2<>(x._1.add(y._1), x._2+y._2))
                    .mapToPair(x -> new Tuple2<>(x._1, x._2._1.div((float) x._2._2)));

            JavaPairRDD<IDContext, INDArray> negativeGradientContext = fullNegativeTrain
                    .mapToPair(x -> new Tuple2<>(x._3(), new Tuple2<>(
                            gradientCalculations.negativeSampleGradient(x._4(), x._2(), x._5()),
                            x._5())))
                    .reduceByKey((x, y) -> new Tuple2<>(x._1.add(y._1), x._2+y._2))
                    .mapToPair(x -> new Tuple2<>(x._1, x._2._1.div((float) x._2._2)));



            // updating wordVectors and contextVectors
            wordVectors = wordVectors.join(positiveGradientWord).join(negativeGradientWord)
                    .mapToPair(x -> new Tuple2<>(x._1, x._2._1._1.add(x._2._1._2).addi(x._2._2)));
            contextVectors = contextVectors.join(positiveGradientContext).join(negativeGradientContext)
                    .mapToPair(x -> new Tuple2<>(x._1, x._2._1._1.add(x._2._1._2).addi(x._2._2)));


            wordVectors.cache();
            contextVectors.cache();
        }
    }

    private <W2> JavaPairRDD<W2, INDArray> initWordVectors(long seed, JavaRDD<W2> words) {
        int newK = k;
        return words.mapPartitionsWithIndex(
                (index, iterWords) ->
                        new RandomINDArrayInit<>(
                                new Random(Objects.hash(seed, index)),
                                iterWords,
                                newK,
                                1,
                                -1.0f,
                                1.0f),
                true
        ).mapToPair(x -> x);
    }

    private <C2> JavaPairRDD<C2, INDArray> initContextVectors(long seed, JavaRDD<C2> contexts) {
        int newK = k;
        return contexts.mapPartitionsWithIndex(
                (index, iterContext) ->
                        new RandomINDArrayInit<>(
                                new Random(Objects.hash(seed, index)),
                                iterContext,
                                newK,
                                1,
                                -1.0f,
                                1.0f),
                true
        ).mapToPair(x -> x);
    }
}
