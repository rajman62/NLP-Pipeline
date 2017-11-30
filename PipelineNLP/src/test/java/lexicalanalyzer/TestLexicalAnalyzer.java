package lexicalanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dk.brics.automaton.Automaton;
import implementations.conffile.ConfFile;
import implementations.filereaders.FSALoader;
import implementations.DefaultLexicalAnalyzer;

import static org.junit.Assert.assertEquals;

import implementations.lexicalutils.Arc;
import nlpstack.annotations.Parser.AnnotationParser;
import nlpstack.annotations.StringSegment;
import nlpstack.annotations.StringWithAnnotations;
import nlpstack.communication.Chart;
import nlpstack.communication.ErrorLogger;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class TestLexicalAnalyzer {

    private String TEST_PATH = "src/test/java/lexicalanalyzer/";
    private ConfFile conf;
    private DefaultLexicalAnalyzer lexicalAnalyzer;
    private ErrorLogger errorLogger = mock(ErrorLogger.class);

    @Before
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        conf = mapper.readValue(new File(TEST_PATH + "conf.yml"), ConfFile.class);
        lexicalAnalyzer = new DefaultLexicalAnalyzer(conf.lexicalConf, errorLogger);
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
    public void testLoadingAndRunningArcsDiscovery() throws Exception {
        List<Set<Arc>> arcs = lexicalAnalyzer.getArcs("Mr. Smith is processing data.");
        lexicalAnalyzer.filterImpossibleTokenization(arcs, arcs.size() - 1);

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
    public void testFailedLexicalAnalysis() throws IOException {
        String input = "Unknown words";
        List<Set<Arc>> arcs = lexicalAnalyzer.getArcs(input);
        for (Set<Arc> arc : arcs)
            assertEquals(true, arc.isEmpty());

        List<List<String>> tokenizedSentences = lexicalAnalyzer.tokenize(arcs, input);
        assertEquals(1, tokenizedSentences.size());
        assertEquals(1, tokenizedSentences.get(0).size());
        assertEquals(input, tokenizedSentences.get(0).get(0));

        reset(errorLogger);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<StringSegment> segments = ArgumentCaptor.forClass(StringSegment.class);
        List<Chart> charts = lexicalAnalyzer.getCharts(input, StringSegment.fromString(input));
        assertEquals(0, charts.size());
        verify(errorLogger, times(1)).lexicalError(message.capture(), segments.capture());
    }


    /**
     * Partial parsing should extract as much as possible
     */
    @Test
    public void testPartiallyFailedLexicalAnalysis() throws IOException {
        String input = "Smith unknown words";
        List<Set<Arc>> arcs = lexicalAnalyzer.getArcs(input);
        assertEquals(1, arcs.get(0).size());
        assertEquals(1, arcs.get(5).size());
        for (int i = 1; i < 5; i++)
            assertEquals(true, arcs.get(i).isEmpty());
        for (int i = 6; i < input.length(); i++)
            assertEquals(true, arcs.get(i).isEmpty());

        List<List<String>> tokenizedSentences = lexicalAnalyzer.tokenize(arcs, input);
        assertEquals(1, tokenizedSentences.size());
        assertEquals(3, tokenizedSentences.get(0).size());
        assertEquals("Smith", tokenizedSentences.get(0).get(0));
        assertEquals(" ", tokenizedSentences.get(0).get(1));
        assertEquals("unknown words", tokenizedSentences.get(0).get(2));

        reset(errorLogger);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<StringSegment> segments = ArgumentCaptor.forClass(StringSegment.class);
        List<Chart> charts = lexicalAnalyzer.getCharts(input, StringSegment.fromString(input));
        verify(errorLogger, times(2)).lexicalError(message.capture(), segments.capture());
        assertEquals(1, charts.size());
        String[] t = {"Smith"};
        assertEquals(Arrays.asList(t), charts.get(0).getTokens());
    }

    @Test
    public void testSubTokensAreExtractedCorrectly() throws IOException {
        reset(errorLogger);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<StringSegment> segments = ArgumentCaptor.forClass(StringSegment.class);

        String input = "M. Smith. Mr. Smith.";
        List<Chart> charts = lexicalAnalyzer.getCharts(input, StringSegment.fromString(input));
        assertEquals(2, charts.size());
        String[] t1 = {"M", ".", "Smith"};
        String[] t2 = {"Mr.", "Smith"};
        assertEquals(Arrays.asList(t1), charts.get(0).getTokens());
        assertEquals(Arrays.asList(t2), charts.get(1).getTokens());

        // None of the words are in the transducers, so each one of them should call errorLogger saying foma
        // couldn't get the corresponding tag
        verify(errorLogger, times(6)).lexicalError(message.capture(), segments.capture());
    }

    @Test
    public void testSimpleSentence() throws IOException {
        String input = "the green card of bob.";
        List<Chart> charts = lexicalAnalyzer.getCharts(input, StringSegment.fromString(input));
        String[] t = {"the", "green", " ", "card", "of", "bob"};
        assertEquals(1, charts.size());
    }

    @Test
    public void testStringSegmentsAreProcessedCorrectly() throws IOException {
        reset(errorLogger);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<StringSegment> segments = ArgumentCaptor.forClass(StringSegment.class);

        List<StringWithAnnotations> test = AnnotationParser.parse(
                "the #[\"great\", \"Adj\"] green card of bob. #[\"Super\", \"Adj\"] #[\"great\", \"Adj\"] green card."
        ).toArrayList();
        assertEquals(1, test.size());
        List<StringSegment> stringSegments = test.get(0).getStrings();
        List<Chart> charts = lexicalAnalyzer.getCharts(stringSegments);
        assertEquals(2, charts.size());

        verify(errorLogger, times(0)).lexicalError(message.capture(), segments.capture());
    }
}
