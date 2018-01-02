package semanticanalyzer;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Nd4jSimpleTests {
    @Test
    public void simpleMatrixManipulation() {
        INDArray nd = Nd4j.create(new float[]{1, 2, 3, 4}, new int[]{4, 1});
        INDArray out = nd.transpose().mmul(nd);
        assertFalse(nd.isScalar());
        assertTrue(out.isScalar());
        assertEquals(30.0f, out.getFloat(0), 0.0001);
    }
}
