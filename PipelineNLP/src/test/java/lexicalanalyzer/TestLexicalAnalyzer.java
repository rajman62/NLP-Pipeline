package lexicalanalyzer;

import dk.brics.automaton.Automaton;
import implementations.conffile.LexicalConf;
import implementations.filereaders.FSALoader;
import implementations.DefaultLexicalAnalyzer;

import static org.junit.Assert.assertEquals;

import implementations.lexicalutils.Arc;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;
import java.util.Set;

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

    @Test
    public void testLoadingAndRunning() throws Exception {
        LexicalConf conf = new LexicalConf();
        conf.wordFSAPath = TEST_PATH + "word_fsa_example.txt";
        conf.separatorFSAPath = TEST_PATH + "sep_fsa_example.txt";
        conf.eosSeparatorFSAPath = TEST_PATH + "eos_fsa_example.txt";

        DefaultLexicalAnalyzer lexicalAnalyzer = new DefaultLexicalAnalyzer(conf);
        List<Set<Arc>> arcs = lexicalAnalyzer.getArcs("Mr. Smith is processing data.");

        assertEquals(1, arcs.get(0).size());
        Arc arc = arcs.get(0).iterator().next();
        assertEquals("Mr.", arc.getString());
        assertEquals(Arc.Type.TOKEN, arc.getType());

        assertEquals(0, arcs.get(1).size());
        assertEquals(0, arcs.get(2).size());

        assertEquals(1, arcs.get(3).size());
        arc = arcs.get(3).iterator().next();
        assertEquals(" ", arc.getString());
        assertEquals(Arc.Type.SEP, arc.getType());

        // checks that filterImpossibleTokenization worked
        assertEquals(1, arcs.get(13).size());
        arc = arcs.get(13).iterator().next();
        assertEquals("processing", arc.getString());
    }
}
