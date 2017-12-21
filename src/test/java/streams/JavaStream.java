package streams;

import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class JavaStream {
    @Test
    public void javaBuilderTest(){
        Stream.Builder<String> streamBuilder = Stream.builder();
        streamBuilder.add("Hello");
        streamBuilder.add("world");
        Stream<String> stream = streamBuilder.build();
        List<String> out = stream.map(x -> x.toLowerCase()).collect(toList());
        assertEquals("hello", out.get(0));
        assertEquals("world", out.get(1));
    }
}
