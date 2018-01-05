package implementations.semanticutils;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.nd4j.linalg.api.ndarray.INDArray;
import scala.Tuple3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Model<C, W> {
    Map<C, INDArray> contextVectors;
    Map<W, INDArray> wordVectors;

    public Model(Map<C, INDArray> contextVectors, Map<W, INDArray> wordVectors) {
        this.contextVectors = contextVectors;
        this.wordVectors = wordVectors;
    }

    public static <C, W> double getTrainLoss(TrainData<C, W> trainData,
                                             JavaPairRDD<IDContext, INDArray> contextVectors,
                                             Broadcast<Map<IDWord, INDArray>> word2VecMapping,
                                             JavaSparkContext sc) {
        return contextVectors
                .join(trainData.contextTrainSet)
                .join(trainData.contextNegativeSamplesTrainSet)
                .map(x -> new Tuple3<>(x._2._1._1, x._2._1._2, x._2._2))
                .mapToDouble(
                        x -> {
                            Map<IDWord, INDArray> word2vec = word2VecMapping.getValue();
                            return loss(x._1(), x._2().stream().map(word2vec::get).collect(Collectors.toList()))
                                    +
                                    loss(x._1().neg(), x._3().stream().map(word2vec::get).collect(Collectors.toList()));
                        })
                .sum();
    }

    public static <C, W> double getTrainLoss(TrainData<C, W> trainData,
                                             JavaPairRDD<IDContext, INDArray> contextVectors,
                                             JavaPairRDD<IDWord, INDArray> wordVectors,
                                             JavaSparkContext sc) {
        Map<IDWord, INDArray> word2VecMapping = wordVectors.collectAsMap();
        Broadcast<Map<IDWord, INDArray>> b = sc.broadcast(word2VecMapping);
        double out = getTrainLoss(trainData, contextVectors, b, sc);
        b.unpersist();
        return out;
    }

    public static float loss(INDArray v1, List<INDArray> v2) {
        float out = 0;
        for (INDArray v : v2)
            out += Math.log(sigmoid(v1, v));
        return out;
    }

    private static float sigmoid(INDArray v1, INDArray v2) {
        return (float) (1.0 / (1.0 + Math.exp(-v1.transpose().mmul(v2).getFloat(0))));
    }
}
