package semanticanalyzer;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import static org.junit.Assert.*;

public class Nd4jSimpleTests {
    @Test
    public void simpleMatrixManipulation() {
        INDArray nd = Nd4j.create(new float[]{1, 2, 3, 4}, new int[]{4, 1});
        INDArray out = nd.transpose().mmul(nd);
        assertFalse(nd.isScalar());
        assertTrue(out.isScalar());
        assertEquals(30.0f, out.getFloat(0), 0.0001);
    }

    @Test
    public void simpleVectorOperations() {
        INDArray nd1 = Nd4j.create(new float[]{1, 2, 3, 4}, new int[]{4, 1});
        INDArray nd2 = nd1.mul(-0.5);
        assertNotEquals(nd1, nd2);
        INDArray nd3 = nd1.muli(-1.5);
        assertEquals(nd1.hashCode(), nd3.hashCode());
        assertTrue(nd1.equals(nd3));
    }

    @Test
    public void transformTest() {
        INDArray nd1 = Nd4j.create(new float[]{(float)Math.exp(1), (float)Math.exp(2)}, new int[]{2, 1});
        INDArray nd2 = Transforms.log(nd1, true);
        assertNotEquals(nd1, nd2);
        nd2 = Transforms.log(nd1, false);
        assertEquals(nd1, nd2);
        assertEquals(1.0f, nd1.getFloat(0), 0.001);
        assertEquals(2.0f, nd1.getFloat(1), 0.001);
    }
}
