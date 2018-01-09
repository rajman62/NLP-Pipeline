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
            Tuple2<T, Tuple3<INDArray, Optional<Multiset<W>>, Optional<Multiset<W>>>> x,
            float lambda) {

        INDArray vp;
        INDArray vn;

        Map<W, INDArray> map = broadcastedMap.value();

        if (x._2._2().isPresent())
            vp = positiveSampleGradient(x._2._1(), x._2._2().get(), map);
        else
            vp = zeroVector;


        if (x._2._3().isPresent())
            vn = negativeSampleGradient(x._2._1(), x._2._3().get(), map);
        else
            vn = zeroVector;

        return new Tuple2<>(x._1, vp.addi(x._2._1()).addi(vn).addi(x._2._1().mul(-lambda)));
    }


    public <T> INDArray positiveSampleGradient(INDArray vc, Multiset<T> train,
                                               Map<T, INDArray> wordVectorMapping) {
        float n = train.size();
        INDArray newVC = zeroVector.dup();
        for(Multiset.Entry<T> id : train.entrySet()) {
            newVC.addi(gradient(vc, wordVectorMapping.get(id.getElement())).muli(id.getCount()));
        }

        return newVC.muli(gamma/n);
    }

    public INDArray positiveSampleGradient(INDArray v1, INDArray v2,
                                               Integer count) {
        return gradient(v1, v2).muli(count).muli(gamma);
    }

    public INDArray negativeSampleGradient(INDArray v1, INDArray v2,
                                               Integer count) {
        return gradient(v1, v2.neg()).muli(count).muli(gamma);
    }

    private INDArray gradient(INDArray v1, INDArray v2) {
        float dotProd = v1.transpose().mmul(v2).getFloat(0);
        double expNegDotProd = Math.exp(-dotProd);
        double scalar = expNegDotProd/(1 + expNegDotProd);
        return v2.mul(scalar);
    }

    public <T> INDArray negativeSampleGradient(INDArray vc, Multiset<T> train,
                                               Map<T, INDArray> wordVectorMapping) {
        float n = train.size();
        INDArray newVC = zeroVector.dup();
        for(T id : train) {
            newVC.addi(gradient(vc, wordVectorMapping.get(id).neg()).muli(train.count(id)));
        }

        return newVC.muli(gamma/n);
    }
}
