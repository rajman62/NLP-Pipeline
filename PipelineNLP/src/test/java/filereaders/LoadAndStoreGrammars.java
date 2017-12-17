package filereaders;

import implementations.filereaders.GrammarLoader;
import implementations.syntacticutils.Grammar;
import implementations.syntacticutils.NonTerminal;
import implementations.syntacticutils.RealNonTerminal;
import implementations.syntacticutils.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class LoadAndStoreGrammars {
    static final String TEST_PATH = "src/test/java/filereaders/";

    @Test
    public void loadGrammar() throws Exception {
        GrammarLoader grammarLoader = new GrammarLoader();
        Grammar grammar = grammarLoader.loadFromFile(TEST_PATH + "grammar_example.cfg");

        assertEquals(8, grammar.getAllRules().size());
        assertEquals(5, grammar.getMultiRightSideRules().size());
        assertEquals(3, grammar.getSingleRightSideRules().size());

        assertTrue(grammar.getAllRules().contains(
                Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP"), RealNonTerminal.of("VP"))
        ));
        assertTrue(grammar.getAllRules().contains(
                Rule.from(RealNonTerminal.of("/.:"), RealNonTerminal.of("$µ"))
        ));
        assertTrue(grammar.getAllRules().contains(
                Rule.from(
                        RealNonTerminal.of("X"),
                        RealNonTerminal.of("V"),
                        RealNonTerminal.of("W"),
                        RealNonTerminal.of("X"),
                        RealNonTerminal.of("Y"),
                        RealNonTerminal.of("Z")
                )
        ));
    }
}
