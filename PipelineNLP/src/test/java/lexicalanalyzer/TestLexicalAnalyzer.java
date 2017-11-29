package lexicalanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dk.brics.automaton.Automaton;
import implementations.conffile.ConfFile;
import implementations.filereaders.FSALoader;
import implementations.DefaultLexicalAnalyzer;

import static org.junit.Assert.assertEquals;

import implementations.lexicalutils.Arc;
import nlpstack.communication.Chart;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TestLexicalAnalyzer {

    private String TEST_PATH = "src/test/java/lexicalanalyzer/";
    private ConfFile conf;
    private DefaultLexicalAnalyzer lexicalAnalyzer;

    @Before
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        conf = mapper.readValue(new File(TEST_PATH + "conf.yml"), ConfFile.class);
        lexicalAnalyzer = new DefaultLexicalAnalyzer(conf.lexicalConf);
    }

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

        arcs = lexicalAnalyzer.getArcs("M. Smith. Mr. Smith.");
        assertEquals(2, arcs.get(0).size());
    }

    @Test
    public void testSubTokensAreExtractedCorrectly() throws IOException {
        List<Chart> charts = lexicalAnalyzer.getCharts("M. Smith. Mr. Smith.");
        assertEquals(2, charts.size());
        String[] t1 = {"M", ".", "Smith"};
        String[] t2 = {"Mr.", "Smith"};
        assertEquals(Arrays.asList(t1), charts.get(0).getTokens());
        assertEquals(Arrays.asList(t2), charts.get(1).getTokens());
    }
}
