package implementations.semanticutils;

import org.apache.spark.api.java.Optional;
import org.apache.spark.broadcast.Broadcast;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import scala.Tuple2;
import scala.Tuple3;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GradientCalculations implements Serializable {
    private float gamma;
    private INDArray zeroVector;

    public GradientCalculations(float gamma, int k) {
        this.gamma = gamma;
        zeroVector = Nd4j.zeros(k, 1);
    }


    public <T, W> Tuple2<T, INDArray> gradientDescentStep(Broadcast<Map<W, INDArray>> broadcastedMap, Tuple2<T, Tuple3<INDArray, Optional<List<W>>, Optional<List<W>>>> x) {
        INDArray vp;
        INDArray vn;

        if (x._2._2().isPresent())
            vp = gradient(x._2._1(), x._2._2().get(), broadcastedMap);
        else
            vp = zeroVector;

        if (x._2._3().isPresent())
            vn = negativeSampleGradient(x._2._1(), x._2._3().get(), broadcastedMap);
        else
            vn = zeroVector;

        return new Tuple2<>(x._1, vp.addi(x._2._1()).addi(vn));
    }


    public <T> INDArray gradient(INDArray vc, List<T> train,
                                 Broadcast<Map<T, INDArray>> broadcastedMap) {
        INDArray newVC = vc.dup();
        Map<T, INDArray> wordVectorMapping = broadcastedMap.value();
        for(T idWord : train) {
            newVC.addi(gradient(vc, wordVectorMapping.get(idWord)).muli(-gamma));
        }

        return newVC;
    }

    private INDArray gradient(INDArray v1, INDArray v2) {
        float dotProd = v1.transpose().mmul(v2).getFloat(0);
        double expNegDotProd = Math.exp(-dotProd);
        double scalar = expNegDotProd/(1 + expNegDotProd);
        return v2.mul(scalar);
    }

    public <T> INDArray negativeSampleGradient(INDArray vc, List<T> train,
                                               Broadcast<Map<T, INDArray>> broadcastedMap) {
        INDArray newVC = vc.dup();
        Map<T, INDArray> wordVectorMapping = broadcastedMap.value();
        for(T idWord : train) {
            newVC.addi(gradient(vc, wordVectorMapping.get(idWord).neg()).muli(-gamma));
        }

        return newVC;
    }
}
