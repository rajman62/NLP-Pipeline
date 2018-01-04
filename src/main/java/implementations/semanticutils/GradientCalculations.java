package implementations.semanticutils;

import org.apache.spark.broadcast.Broadcast;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GradientCalculations implements Serializable {
    private float gamma;

    public GradientCalculations(float gamma) {
        this.gamma = gamma;
    }

    public <T> INDArray gradientDescent(INDArray vc, List<T> train,
                                    Broadcast<Map<T, INDArray>> broadcastedMap) {
        INDArray newVC = vc.dup();
        Map<T, INDArray> wordVectorMapping = broadcastedMap.getValue();
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

    public <T> INDArray negativeSampleGradientDescent(INDArray vc, List<T> train,
                                                      Broadcast<Map<T, INDArray>> broadcastedMap) {
        INDArray newVC = vc.dup();
        Map<T, INDArray> wordVectorMapping = broadcastedMap.getValue();
        for(T idWord : train) {
            newVC.addi(gradient(vc, wordVectorMapping.get(idWord).neg()).muli(-gamma));
        }

        return newVC;
    }
}
