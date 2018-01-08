package implementations.sparkutils;

import implementations.WordEmbeddingsUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.function.Supplier;

public class SparkSetup implements Supplier<JavaSparkContext> {

    @Override
    public JavaSparkContext get() {
        String master = "local[3]";
        SparkConf conf = new SparkConf()
                .setAppName(WordEmbeddingsUtils.class.getName())
                .set("spark.executor.memory", "6g")
                .setMaster(master)
                .set("spark.serializer", "implementations.sparkutils.KryoSerializerWithNd4jSupport");
        return new JavaSparkContext(conf);
    }
}
