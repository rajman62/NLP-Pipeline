package lexicalanalyzer;

import implementations.lexicalutils.Arc;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashSet;
import java.util.Set;

public class TestArcs {
    @Test
    public void testHashcodeAndEqual() {
        String input = "my input";
        Arc arc1 = new Arc(Pair.of(3, 7), input, Arc.Type.TOKEN);
        Arc arc2 = new Arc(Pair.of(3, 7), input, Arc.Type.TOKEN);
        Arc arc3 = new Arc(Pair.of(2, 2), input, Arc.Type.INVISIBLE_SEP);
        Set<Arc> set = new HashSet<>();
        set.add(arc1);
        set.add(arc2);
        assertEquals(arc1, arc2);
        assertEquals(1, set.size());
        set.add(arc3);
        assertNotEquals(arc2, arc3);
        assertEquals(2, set.size());
    }
}
