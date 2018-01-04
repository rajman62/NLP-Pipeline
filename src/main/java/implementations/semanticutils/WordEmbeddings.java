package implementations.semanticutils;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.DefaultRandom;
import scala.Tuple2;
import scala.Tuple3;

import java.util.*;

public class WordEmbeddings<C, W> {
    private int k;
    private float gamma;
    private float lambdaW;
    private float lambdaC;
    private int maxIter;
    private JavaSparkContext sc;
    private int negativeSamplingPerWord;

    public WordEmbeddings(int k, float gamma, float lambdaW, float lambdaC, int maxIter, int negativeSamplingPerWord, JavaSparkContext sc) {
        this.k = k;
        this.gamma = gamma;
        this.lambdaW = lambdaW;
        this.lambdaC = lambdaC;
        this.maxIter = maxIter;
        this.negativeSamplingPerWord = negativeSamplingPerWord;
        this.sc = sc;
    }

    public void train(JavaRDD<Tuple2<C, W>> trainSet, long seed) {
        TrainData<C, W> trainData = new TrainData<>(trainSet, negativeSamplingPerWord, sc, seed);

        JavaPairRDD<IDContext, INDArray> contextVectors = initContextVectors(seed + 1L, trainData.contextsZippedWithID.map(x -> x._2));
        JavaPairRDD<IDWord, INDArray> wordVectors = initWordVectors(seed + 2L, trainData.wordsZippedWithID.map(x -> x._2));

        GradientCalculations gradientCalculations = new GradientCalculations(gamma, k);

        for (int i = 0; i < maxIter; i++) {
            Map<IDContext, INDArray> contextToVecMap = contextVectors.collectAsMap();
            Broadcast<Map<IDContext, INDArray>> broadcastedContextIDToVecMap = sc.broadcast(contextToVecMap);

            wordVectors = wordVectors
                    .leftOuterJoin(trainData.wordTrainSet)
                    .leftOuterJoin(trainData.wordNegativeSamplesTrainSet)
                    .mapToPair(x -> new Tuple2<>(x._1, new Tuple3<>(x._2._1._1, x._2._1._2, x._2._2))) // <IDWord, <Vc, trainSet, negativeSampleTrainSet>>
                    .mapToPair(x -> gradientCalculations.gradientDescentStep(broadcastedContextIDToVecMap, x));

            Map<IDWord, INDArray> wordToVecMap = wordVectors.collectAsMap();
            Broadcast<Map<IDWord, INDArray>> broadcastedWordIDToVecMap = sc.broadcast(wordToVecMap);

            contextVectors = contextVectors
                    .leftOuterJoin(trainData.contextTrainSet)
                    .leftOuterJoin(trainData.contextNegativeSamplesTrainSet)
                    .mapToPair(x -> new Tuple2<>(x._1, new Tuple3<>(x._2._1._1, x._2._1._2, x._2._2))) // <IDContext, <Vc, trainSet, negativeSampleTrainSet>>
                    .mapToPair(x -> gradientCalculations.gradientDescentStep(broadcastedWordIDToVecMap, x));

            System.out.println(String.format(
                    "Iteration %d training error: %f",
                    i,
                    Model.getTrainLoss(trainData, contextVectors, wordVectors, sc)
            ));
        }

        trainData.wordTrainSet.unpersist();
        trainData.contextTrainSet.unpersist();
        trainData.wordNegativeSamplesTrainSet.unpersist();
        trainData.contextNegativeSamplesTrainSet.unpersist();
    }

    private <W2> JavaPairRDD<W2, INDArray> initWordVectors(long seed, JavaRDD<W2> words) {
        return words.mapPartitionsWithIndex(
                (index, iterWords) ->
                        new RandomINDArrayInit<>(
                                new DefaultRandom(Objects.hash(seed, index)),
                                iterWords,
                                new int[]{k, 1},
                                -1.0,
                                1.0),
                false
        ).mapToPair(x -> x);
    }

    private <C2> JavaPairRDD<C2, INDArray> initContextVectors(long seed, JavaRDD<C2> contexts) {
        return contexts.mapPartitionsWithIndex(
                (index, iterContext) ->
                        new RandomINDArrayInit<>(
                                new DefaultRandom(Objects.hash(seed, index)),
                                iterContext,
                                new int[]{k, 1},
                                -1.0,
                                1.0),
                false
        ).mapToPair(x -> x);
    }
}