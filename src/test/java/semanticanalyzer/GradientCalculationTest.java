package semanticanalyzer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import implementations.semanticutils.GradientCalculations;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class GradientCalculationTest {
    @Test
    public void contextGradientCalculationTest() {
        GradientCalculations gradientCalculations = new GradientCalculations(0.5f, 10);
        INDArray nd1 = Nd4j.create(new float[]{1, 1, 1, 1}, new int[]{4, 1});

        Multiset<Integer> train = HashMultiset.create();
        train.add(1);
        train.add(2);

        Map<Integer, INDArray> mapping = new HashMap<>();
        mapping.put(1, nd1.add(0.5));
        mapping.put(2, nd1.add(-0.5));


        INDArray result = gradientCalculations.positiveSampleGradient(nd1, train, mapping);
        INDArray expectedResult = Nd4j.create(
                new float[]{0.9683448f, 0.9683448f, 0.9683448f, 0.9683448f},
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
