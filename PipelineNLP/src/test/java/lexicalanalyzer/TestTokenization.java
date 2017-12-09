package lexicalanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dk.brics.automaton.RunAutomaton;
import implementations.conffile.ConfFile;
import implementations.filereaders.FSALoader;
import implementations.lexicalutils.Arc;
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
        List<Pair<Integer, Integer>> arcs = Tokenization.runAutomatonAllMatch(wordFSA, "hello", 0);
        assertEquals(1, arcs.size());
        assertEquals((Integer) 0, arcs.get(0).getLeft());
        assertEquals((Integer) 4, arcs.get(0).getRight());

        arcs = Tokenization.runAutomatonAllMatch(wordFSA, "processing", 0);
        assertEquals(2, arcs.size());

        assertEquals((Integer) 0, arcs.get(0).getLeft());
        assertEquals((Integer) 0, arcs.get(1).getLeft());
        assertEquals((Integer) 6, arcs.get(0).getRight());
        assertEquals((Integer) 9, arcs.get(1).getRight());
    }

    @Test
    public void testLoadingAndRunningArcsDiscovery() throws Exception {
        List<Set<Arc>> arcs = Tokenization.getArcs("Mr. Smith is processing data.", wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        Tokenization.filterImpossibleTokenization(arcs, arcs.size() - 1);

        assertEquals(1, arcs.get(0).size());
        Arc arc = arcs.get(0).iterator().next();
        assertEquals("Mr.", arc.getString());
        assertEquals(Arc.Type.TOKEN, arc.getType());

        assertEquals(0, arcs.get(1).size());
        assertEquals(0, arcs.get(2).size());

        assertEquals(1, arcs.get(3).size());
        arc = arcs.get(3).iterator().next();
        assertEquals(" ", arc.getString());
        assertEquals(Arc.Type.INVISIBLE_SEP, arc.getType());

        // checks that filterImpossibleTokenization worked
        assertEquals(1, arcs.get(13).size());
        arc = arcs.get(13).iterator().next();
        assertEquals("processing", arc.getString());

        arcs = Tokenization.getArcs("M. Smith. Mr. Smith.", wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        assertEquals(2, arcs.get(0).size());
    }


    @Test
    public void testFailedLexicalAnalysis() throws IOException {
        String input = "Unknown words";
        List<Set<Arc>> arcs = Tokenization.getArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        for (Set<Arc> arc : arcs)
            assertEquals(true, arc.isEmpty());

        List<List<String>> tokenizedSentences = Tokenization.tokenizeFromArcs(arcs, input);
        assertEquals(0, tokenizedSentences.size());
    }


    @Test
    public void testPartiallyFailedLexicalAnalysis() throws IOException {
        String input = "Smith unknown words";
        List<Set<Arc>> arcs = Tokenization.getArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        assertEquals(1, arcs.get(0).size());
        assertEquals(1, arcs.get(5).size());
        for (int i = 1; i < 5; i++)
            assertEquals(true, arcs.get(i).isEmpty());
        for (int i = 6; i < input.length(); i++)
            assertEquals(true, arcs.get(i).isEmpty());

        List<List<String>> tokenizedSentences = Tokenization.tokenizeFromArcs(arcs, input);
        assertEquals(1, tokenizedSentences.size());
        assertEquals(2, tokenizedSentences.get(0).size());
        assertEquals("Smith", tokenizedSentences.get(0).get(0));
        assertEquals(" ", tokenizedSentences.get(0).get(1));
    }

    @Test
    public void testMakeSpacesInTokens() throws IOException {
        String input = "green card";
        List<Set<Arc>> arcs = Tokenization.getArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        Tokenization.filterImpossibleTokenization(arcs, input.length() - 1);
        List<List<String>> tokenizedSentences = Tokenization.tokenizeFromArcs(arcs, input);
        List<List<String>> tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcs, tokenizedSentences,
                invisibleCharacterPattern, input);
        assertEquals(1, tokenizedSentencesWithoutSpaces.size());
        assertEquals(3, tokenizedSentencesWithoutSpaces.get(0).size());
        String[] t1 = {"green", " ", "card"};
        assertEquals(Arrays.asList(t1), tokenizedSentencesWithoutSpaces.get(0));


        input = "machine learning";
        arcs = Tokenization.getArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        assertEquals(1, arcs.get(0).size());
        for (int i = 1 ; i < input.length() ; i++)
            assertEquals(0, arcs.get(i).size());
        tokenizedSentences = Tokenization.tokenizeFromArcs(arcs, input);
        assertEquals(1, tokenizedSentences.size());
        assertEquals(1, tokenizedSentences.get(0).size());
        tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcs, tokenizedSentences,
                invisibleCharacterPattern, input);
        assertEquals(1, tokenizedSentencesWithoutSpaces.size());
        assertEquals(3, tokenizedSentencesWithoutSpaces.get(0).size());
        String[] t2 = {"machine", " ", "learning"};
        assertEquals(Arrays.asList(t2), tokenizedSentencesWithoutSpaces.get(0));
        assertEquals(new Arc(Pair.of(7, 7), input, Arc.Type.INVISIBLE_SEP), arcs.get(7).iterator().next());

        input = "green, card";
        arcs = Tokenization.getArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        tokenizedSentences = Tokenization.tokenizeFromArcs(arcs, input);
        tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcs, tokenizedSentences,
                invisibleCharacterPattern, input);
        String[] t3 = {"green", ",", " ", "card"};
        assertEquals(Arrays.asList(t3), tokenizedSentencesWithoutSpaces.get(0));
        assertEquals(new Arc(Pair.of(5, 6), input, Arc.Type.VISIBLE_SEP), arcs.get(5).iterator().next());
        assertEquals(new Arc(Pair.of(6, 6), input, Arc.Type.INVISIBLE_SEP), arcs.get(6).iterator().next());


        input = "very long \t \t token  with \t multiple \n spaces";
        arcs = Tokenization.getArcs(input, wordFSA, separatorFSA,
                invisibleCharacterPattern, eosSeparatorPattern);
        tokenizedSentences = Tokenization.tokenizeFromArcs(arcs, input);
        tokenizedSentencesWithoutSpaces = Tokenization.markSpacesInTokens(arcs, tokenizedSentences,
                invisibleCharacterPattern, input);
        String[] t4 = {"very", " ", "long", " \t \t " , "token", "  ", "with", " \t ", "multiple", " \n " ,"spaces"};
        assertEquals(Arrays.asList(t4), tokenizedSentencesWithoutSpaces.get(0));
    }
}
