package lexicalanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dk.brics.automaton.RunAutomaton;
import implementations.conffile.ConfFile;
import implementations.filereaders.FSALoader;
import implementations.lexicalutils.Arc;
import implementations.lexicalutils.ArcParsing;
import implementations.lexicalutils.AutomatonUtils;
import implementations.lexicalutils.Tokenization;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestTokenization {
    private String TEST_PATH = "src/test/java/lexicalanalyzer/";
    private RunAutomaton wordFSA;
    private RunAutomaton separatorFSA;
    private RunAutomaton invisibleCharacterPattern;
    private RunAutomaton eosSeparatorPattern;

    @Before
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfFile conf = mapper.readValue(new File(TEST_PATH + "conf.yml"), ConfFile.class);
        FSALoader fsaLoader = new FSALoader();
        wordFSA = new RunAutomaton(fsaLoader.loadFromFile(conf.lexicalConf.wordFSAPath));
        separatorFSA = new RunAutomaton(fsaLoader.loadFromFile(conf.lexicalConf.separatorFSAPath));
        invisibleCharacterPattern = new RunAutomaton(FSALoader.parseRegex(conf.lexicalConf.invisibleCharacterRegex));
        eosSeparatorPattern = new RunAutomaton(FSALoader.parseRegex(conf.lexicalConf.eosSeparatorRegex));
    }

    @Test
    public void testRunAutomaton() throws Exception {
        List<Pair<Integer, Integer>> arcs = AutomatonUtils.runAutomatonAllMatch(wordFSA, "hello", 0);
        assertEquals(1, arcs.size());
        assertEquals((Integer) 0, arcs.get(0).getLeft());
        assertEquals((Integer) 4, arcs.get(0).getRight());

        arcs = AutomatonUtils.runAutomatonAllMatch(wordFSA, "processing", 0);
        assertEquals(2, arcs.size());

        assertEquals((Integer) 0, arcs.get(0).getLeft());
        assertEquals((Integer) 0, arcs.get(1).getLeft());
        assertEquals((Integer) 6, arcs.get(0).getRight());
        assertEquals((Integer) 9, arcs.get(1).getRight());
    }

    @Test
    public void testFailedLexicalAnalysis() throws IOException {
        String input = "Unknown words";
        ArcParsing arcParsing = ArcParsing.parseArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        for (Set<Arc> arc : arcParsing)
            assertEquals(true, arc.isEmpty());

        assertEquals(-1, arcParsing.getFurthestReachingPosition());

        List<List<String>> tokenizedSentences = Tokenization.tokenizeFromArcs(arcParsing, input);
        assertEquals(0, tokenizedSentences.size());
    }


    @Test
    public void testPartiallyFailedLexicalAnalysis() throws IOException {
        String input = "Smith unknown words";
        ArcParsing arcParsing = ArcParsing.parseArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        assertEquals(1, arcParsing.getArcsStartingAt(0).size());
        assertEquals(1, arcParsing.getArcsStartingAt(5).size());
        for (int i = 1; i < 5; i++)
            assertEquals(true, arcParsing.getArcsStartingAt(i).isEmpty());
        for (int i = 6; i < input.length(); i++)
            assertEquals(true, arcParsing.getArcsStartingAt(i).isEmpty());

        List<List<String>> tokenizedSentences = Tokenization.tokenizeFromArcs(arcParsing, input);
        assertEquals(1, tokenizedSentences.size());
        assertEquals(2, tokenizedSentences.get(0).size());
        assertEquals("Smith", tokenizedSentences.get(0).get(0));
        assertEquals(" ", tokenizedSentences.get(0).get(1));
    }

    @Test
    public void testMakeSpacesInTokens() throws IOException {
        String input = "green card";
        ArcParsing arcParsing = ArcParsing.parseArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        arcParsing.filterImpossibleTokenization();
        List<List<String>> tokenizedSentences = Tokenization.tokenizeFromArcs(arcParsing, input);
        List<List<String>> tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcParsing, tokenizedSentences,
                invisibleCharacterPattern, input);
        assertEquals(1, tokenizedSentencesWithoutSpaces.size());
        assertEquals(3, tokenizedSentencesWithoutSpaces.get(0).size());
        String[] t1 = {"green", " ", "card"};
        assertEquals(Arrays.asList(t1), tokenizedSentencesWithoutSpaces.get(0));


        input = "machine learning";
        arcParsing = ArcParsing.parseArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        assertEquals(1, arcParsing.getArcsStartingAt(0).size());
        for (int i = 1 ; i < input.length() ; i++)
            assertEquals(0, arcParsing.getArcsStartingAt(i).size());
        tokenizedSentences = Tokenization.tokenizeFromArcs(arcParsing, input);
        assertEquals(1, tokenizedSentences.size());
        assertEquals(1, tokenizedSentences.get(0).size());
        tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcParsing, tokenizedSentences,
                invisibleCharacterPattern, input);
        assertEquals(1, tokenizedSentencesWithoutSpaces.size());
        assertEquals(3, tokenizedSentencesWithoutSpaces.get(0).size());
        String[] t2 = {"machine", " ", "learning"};
        assertEquals(Arrays.asList(t2), tokenizedSentencesWithoutSpaces.get(0));
        assertEquals(new Arc(Pair.of(7, 7), input, Arc.Type.INVISIBLE_SEP),
                arcParsing.getArcsStartingAt(7).iterator().next());

        input = "green, card";
        arcParsing = ArcParsing.parseArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        tokenizedSentences = Tokenization.tokenizeFromArcs(arcParsing, input);
        tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcParsing, tokenizedSentences,
                invisibleCharacterPattern, input);
        String[] t3 = {"green", ",", " ", "card"};
        assertEquals(Arrays.asList(t3), tokenizedSentencesWithoutSpaces.get(0));
        assertEquals(new Arc(Pair.of(5, 6), input, Arc.Type.VISIBLE_SEP),
                arcParsing.getArcsStartingAt(5).iterator().next());
        assertEquals(new Arc(Pair.of(6, 6), input, Arc.Type.INVISIBLE_SEP),
                arcParsing.getArcsStartingAt(6).iterator().next());


        input = "very long \t \t token  with \t multiple \n spaces";
        arcParsing = ArcParsing.parseArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        tokenizedSentences = Tokenization.tokenizeFromArcs(arcParsing, input);
        tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcParsing, tokenizedSentences,
                invisibleCharacterPattern, input);
        String[] t4 = {"very", " ", "long", " \t \t " , "token", "  ", "with", " \t ", "multiple", " \n " ,"spaces"};
        assertEquals(Arrays.asList(t4), tokenizedSentencesWithoutSpaces.get(0));
    }

    @Test
    public void testMakeSpacesInTokensEndOfSentence() throws IOException {
        String input = "hello? hello.";
        ArcParsing arcParsing = ArcParsing.parseArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        arcParsing.filterImpossibleTokenization();
        List<List<String>> tokenizedSentences = Tokenization.tokenizeFromArcs(arcParsing, input);
        List<List<String>> tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcParsing, tokenizedSentences,
                invisibleCharacterPattern, input);
        assertEquals(2, tokenizedSentencesWithoutSpaces.size());
        String[] t1 = {"hello", "?", " "};
        String[] t2 = {"hello", "."};
        assertEquals(Arrays.asList(t1), tokenizedSentencesWithoutSpaces.get(0));
        assertEquals(Arrays.asList(t2), tokenizedSentencesWithoutSpaces.get(1));
    }
}
