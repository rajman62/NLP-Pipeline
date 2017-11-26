package lexicalanalyzer;

import dk.brics.automaton.Automaton;
import implementations.filereaders.FSALoader;
import implementations.DefaultLexicalAnalyzer;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

public class TestLexicalAnalyzer {

    static final String TEST_PATH = "src/test/java/lexicalanalyzer/";

    @Test
    public void testRunAutomaton() throws Exception {
        FSALoader fsaLoader = new FSALoader();
        Automaton fsa = fsaLoader.loadFromFile(TEST_PATH + "word_fsa_example.txt");

        List<Pair<Integer, Integer>> arcs = DefaultLexicalAnalyzer.runAutomaton(fsa, "hello", 0);
        assertEquals(1, arcs.size());
        assertEquals((Integer) 0, arcs.get(0).getLeft());
        assertEquals((Integer) 4, arcs.get(0).getRight());

        arcs = DefaultLexicalAnalyzer.runAutomaton(fsa, "processing", 0);
        assertEquals(2, arcs.size());

        assertEquals((Integer) 0, arcs.get(0).getLeft());
        assertEquals((Integer) 0, arcs.get(1).getLeft());
        assertEquals((Integer) 6, arcs.get(0).getRight());
        assertEquals((Integer) 9, arcs.get(1).getRight());
    }
}
