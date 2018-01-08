package implementations.sparkutils;

import com.esotericsoftware.kryo.Kryo;
import org.apache.spark.SparkConf;
import org.apache.spark.serializer.KryoSerializer;


public class KryoSerializerWithNd4jSupport extends KryoSerializer {
    public KryoSerializerWithNd4jSupport(SparkConf conf) {
        super(conf);
    }

    @Override
    public Kryo newKryo() {
        Kryo kryo = super.newKryo();
        kryo.register(org.nd4j.linalg.cpu.nativecpu.NDArray.class, new Nd4jSerializer());
        return kryo;
    }
}
