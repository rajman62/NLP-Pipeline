package semanticanalyzer;

import implementations.semanticutils.GradientCalculations;
import org.apache.spark.broadcast.Broadcast;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.exception.ND4JException;
import org.nd4j.linalg.factory.Nd4j;
import scala.reflect.ClassTag;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradientCalculationTest {
    @Test
    public void contextGradientCalculationTest() {
        GradientCalculations gradientCalculations = new GradientCalculations(0.5f);
        INDArray nd1 = Nd4j.create(new float[]{1, 1, 1, 1}, new int[]{4, 1});

        List<Integer> train = new ArrayList<>();
        train.add(1);
        train.add(2);

        Map<Integer, INDArray> mapping = new HashMap<>();
        mapping.put(1, nd1.add(0.5));
        mapping.put(2, nd1.add(-0.5));

        ClassTag<Map<Integer, INDArray>> classTag = scala.reflect.ClassTag$.MODULE$.apply(mapping.getClass());

        Broadcast<Map<Integer, INDArray>> b = new Broadcast<Map<Integer, INDArray>>(0L, classTag) {
            @Override
            public Map<Integer, INDArray> getValue() {
                return mapping;
            }

            @Override
            public void doUnpersist(boolean b) {

            }

            @Override
            public void doDestroy(boolean b) {

            }
        };

        INDArray result = gradientCalculations.gradientDescent(nd1, train, b);
        INDArray expectedResult = Nd4j.create(
                new float[]{0.9683448f,  0.9683448f,  0.9683448f,  0.9683448f},
                new int[]{4, 1});
        assertTrue(
                String.format("Expected %s but got %s", expectedResult.toString(), result.toString()),
                result.equalsWithEps(expectedResult, 0.0001)
        );

        // we check the original vectors were not changed
        assertTrue(nd1.equalsWithEps(Nd4j.create(new float[]{1, 1, 1, 1}, new int[]{4, 1}), 0.0001));
        assertTrue(mapping.get(1).equalsWithEps(nd1.add(0.5), 0.0001));
        assertTrue(mapping.get(2).equalsWithEps(nd1.add(-0.5), 0.0001));
    }
}
