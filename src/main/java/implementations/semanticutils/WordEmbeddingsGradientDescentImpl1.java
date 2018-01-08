package implementations.semanticutils;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.nd4j.linalg.api.ndarray.INDArray;
import scala.Tuple2;
import scala.Tuple3;

import java.util.*;

public class WordEmbeddingsGradientDescentImpl1<C, W> {
    private int k;
    private float gamma;
    private float lambdaW;
    private float lambdaC;
    private int maxIter;
    private JavaSparkContext sc;

    public WordEmbeddingsGradientDescentImpl1(int k, float gamma, float lambdaW, float lambdaC, int maxIter, JavaSparkContext sc) {
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

        trainData.wordTrainSet.cache();
        trainData.contextTrainSet.cache();
        trainData.wordNegativeSamplesTrainSet.cache();
        trainData.contextNegativeSamplesTrainSet.cache();

        wordVectors.cache();
        contextVectors.cache();

        int numPartitionWords = (int) Math.ceil(((double) trainData.numberOfWords) / 100.0);
        wordVectors.repartition(numPartitionWords);
        trainData.wordTrainSet.repartition(numPartitionWords);
        trainData.wordNegativeSamplesTrainSet.repartition(numPartitionWords);

        int numPartitionContexts = (int) Math.ceil(((double) trainData.numberOfContexts) / 100.0);
        contextVectors.repartition(numPartitionContexts);
        trainData.contextTrainSet.repartition(numPartitionContexts);
        trainData.contextNegativeSamplesTrainSet.repartition(numPartitionContexts);

        float lambdaW2 = lambdaW;
        float lambdaC2 = lambdaC;

        for (int i = 0; i < maxIter; i++) {
            Map<IDContext, INDArray> contextToVecMap = contextVectors.collectAsMap();
            Broadcast<Map<IDContext, INDArray>> broadcastedContextIDToVecMap = sc.broadcast(contextToVecMap);

            JavaPairRDD<IDWord, INDArray> newWordVectors = wordVectors
                    .leftOuterJoin(trainData.wordTrainSet)
                    .leftOuterJoin(trainData.wordNegativeSamplesTrainSet)
                    .mapToPair(x -> new Tuple2<>(x._1, new Tuple3<>(x._2._1._1, x._2._1._2, x._2._2))) // <IDWord, <Vc, trainSet, negativeSampleTrainSet>>
                    .mapToPair(x -> gradientCalculations.gradientDescentStep(broadcastedContextIDToVecMap, x, lambdaW2));

            newWordVectors.cache();

            Map<IDWord, INDArray> wordToVecMap = newWordVectors.collectAsMap();
            Broadcast<Map<IDWord, INDArray>> broadcastedWordIDToVecMap = sc.broadcast(wordToVecMap);

            wordVectors = newWordVectors;

            JavaPairRDD<IDContext, INDArray> newContextVectors = contextVectors
                    .leftOuterJoin(trainData.contextTrainSet)
                    .leftOuterJoin(trainData.contextNegativeSamplesTrainSet)
                    .mapToPair(x -> new Tuple2<>(x._1, new Tuple3<>(x._2._1._1, x._2._1._2, x._2._2))) // <IDContext, <Vc, trainSet, negativeSampleTrainSet>>
                    .mapToPair(x -> gradientCalculations.gradientDescentStep(broadcastedWordIDToVecMap, x, lambdaC2));

            newContextVectors.cache();

            if (i % 10 == 0) {
                System.out.println(String.format(
                        "%s - iteration %d training error: %f",
                        (new Date()).toString(),
                        i,
                        Model.getTrainLoss(trainData, newContextVectors, broadcastedWordIDToVecMap, sc)
                ));
            }

            // contextVectors.unpersist();
            // wordVectors.unpersist();
            contextVectors = newContextVectors;

            broadcastedContextIDToVecMap.unpersist();
            broadcastedWordIDToVecMap.unpersist();
        }

        trainData.wordTrainSet.unpersist();
        trainData.contextTrainSet.unpersist();
        trainData.wordNegativeSamplesTrainSet.unpersist();
        trainData.contextNegativeSamplesTrainSet.unpersist();
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