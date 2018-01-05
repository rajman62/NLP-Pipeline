package semanticanalyzer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class SparkIntegrationTest {
    static private JavaSparkContext sc;

    @BeforeClass
    static public void initSpark() {
        String master = "local[2]";
        SparkConf conf = new SparkConf()
                .setAppName(SparkIntegrationTest.class.getName())
                .setMaster(master);
        sc = new JavaSparkContext(conf);
    }

    @AfterClass
    static public void closeSpark() {
        sc.close();
    }

    @Test
    public void testSparkAndND4JCanBeUsedTogether() {
        List<INDArray> data = new LinkedList<>();
        for (int i = 0; i < 16; i++)
            data.add(Nd4j.rand(new int[]{4, 1}));
        JavaRDD<INDArray> rdd = sc.parallelize(data);
        assertEquals(16, rdd.count());
    }

    @Test
    public void testSparkWithMultiSet() {
        List<Multiset<Integer>> data = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            Multiset<Integer> toAdd = HashMultiset.create();
            toAdd.add(i);
            toAdd.add(i);
            data.add(toAdd);
        }
        JavaRDD<Multiset<Integer>> rdd = sc.parallelize(data);
        assertEquals(16L, rdd.count());
    }
}
