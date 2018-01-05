package implementations.semanticutils;

import com.google.common.collect.Multiset;
import org.apache.spark.api.java.Optional;
import org.apache.spark.broadcast.Broadcast;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import scala.Tuple2;
import scala.Tuple3;

import java.io.Serializable;
import java.util.Map;

public class GradientCalculations implements Serializable {
    private float gamma;
    private INDArray zeroVector;

    public GradientCalculations(float gamma, int k) {
        this.gamma = gamma;
        zeroVector = Nd4j.zeros(k, 1);
    }


    public <T, W> Tuple2<T, INDArray> gradientDescentStep(
            Broadcast<Map<W, INDArray>> broadcastedMap,
            Tuple2<T, Tuple3<INDArray, Optional<Multiset<W>>, Optional<Multiset<W>>>> x) {

        INDArray vp;
        INDArray vn;

        Map<W, INDArray> map = broadcastedMap.value();

        int tpSize, tnSize;

        if (x._2._2().isPresent()) {
            tpSize = x._2._2().get().size();
            vp = positiveSampleGradient(x._2._1(), x._2._2().get(), map);
        }
        else {
            tpSize = 0;
            vp = zeroVector;
        }

        if (x._2._3().isPresent()) {
            tnSize = x._2._3().get().size();
            vn = negativeSampleGradient(x._2._1(), x._2._3().get(), map);
        }
        else {
            tnSize = 0;
            vn = zeroVector;
        }

        vp.divi(tpSize + tnSize);
        vn.divi(tpSize + tnSize);

        return new Tuple2<>(x._1, vp.addi(x._2._1()).addi(vn));
    }


    public <T> INDArray positiveSampleGradient(INDArray vc, Multiset<T> train,
                                               Map<T, INDArray> wordVectorMapping) {
        INDArray newVC = zeroVector.dup();
        for(T id : train) {
            newVC.addi(gradient(vc, wordVectorMapping.get(id)).muli(train.count(id)));
        }

        return newVC.muli(-gamma);
    }

    private INDArray gradient(INDArray v1, INDArray v2) {
        float dotProd = v1.transpose().mmul(v2).getFloat(0);
        double expNegDotProd = Math.exp(-dotProd);
        double scalar = expNegDotProd/(1 + expNegDotProd);
        return v2.mul(scalar);
    }

    public <T> INDArray negativeSampleGradient(INDArray vc, Multiset<T> train,
                                               Map<T, INDArray> wordVectorMapping) {
        INDArray newVC = zeroVector.dup();
        for(T id : train) {
            newVC.addi(gradient(vc, wordVectorMapping.get(id).neg()).muli(train.count(id)));
        }

        return newVC.muli(-gamma);
    }
}
