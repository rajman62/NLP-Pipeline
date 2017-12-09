package lexicalanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RunAutomaton;
import implementations.conffile.ConfFile;
import implementations.filereaders.FSALoader;
import implementations.DefaultLexicalAnalyzer;

import static org.junit.Assert.assertEquals;

import implementations.lexicalutils.Arc;
import implementations.lexicalutils.Tokenization;
import nlpstack.annotations.Parser.AnnotationParser;
import nlpstack.annotations.StringSegment;
import nlpstack.annotations.StringWithAnnotations;
import nlpstack.communication.Chart;
import nlpstack.communication.ErrorLogger;
import org.apache.commons.lang3.SystemUtils;
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
    private DefaultLexicalAnalyzer lexicalAnalyzer;
    private ErrorLogger errorLogger = mock(ErrorLogger.class);

    @Before
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfFile conf = mapper.readValue(new File(TEST_PATH + "conf.yml"), ConfFile.class);
        if (SystemUtils.IS_OS_WINDOWS)
            conf.lexicalConf.FomaBinPath = conf.lexicalConf.FomaBinPath.concat(".exe");
        lexicalAnalyzer = new DefaultLexicalAnalyzer(conf.lexicalConf, errorLogger);
    }

    @Test
    public void testFailedLexicalAnalysis() throws IOException {
        String input = "Unknown words";
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
        String[] t1 = {"M", ".", "Smith", "."};
        String[] t2 = {"Mr.", "Smith", "."};
        assertEquals(Arrays.asList(t1), charts.get(0).getTokens());
        assertEquals(Arrays.asList(t2), charts.get(1).getTokens());

        // None of the words are in the transducers, so each one of them should call errorLogger saying foma
        // couldn't get the corresponding tag
        verify(errorLogger, times(7)).lexicalError(message.capture(), segments.capture());
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

        verify(errorLogger, times(1)).lexicalError(message.capture(), segments.capture());
    }

    @Test
    public void testSingleWord() throws IOException {
        List<Chart> charts = lexicalAnalyzer.getCharts("bob", StringSegment.fromString("bob"));
        assertEquals(1, charts.size());
        String[] t = {"bob"};
        assertEquals(Arrays.asList(t), charts.get(0).getTokens());
    }
}
