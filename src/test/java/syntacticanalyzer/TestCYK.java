package syntacticanalyzer;

import implementations.filereaders.GrammarLoader;
import implementations.syntacticutils.*;
import nlpstack.communication.Chart;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCYK {
    static final String TEST_PATH = "src/test/java/syntacticanalyzer/";

    @Test
    public void testSimpleCase() {
        Grammar grammar = new Grammar();
        grammar.addRule(Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP"), RealNonTerminal.of("V")));
        grammar.addRule(Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP")));
        grammar.addRule(Rule.from(RealNonTerminal.of("NP"), RealNonTerminal.of("Det"), RealNonTerminal.of("N")));
        grammar.addRule(Rule.from(RealNonTerminal.of("Det"), Terminal.of("the")));
        grammar.addRule(Rule.from(RealNonTerminal.of("N"), Terminal.of("string")));

        Chart<String, String> chart = Chart.getEmptyChart("the", "string");
        CYK cyk = new CYK(new BinaryGrammar(grammar), chart);
        cyk.runCYK();
        assertEquals(1, chart.getRule(1, 1).size());
        assertEquals(1, chart.getRule(1, 2).size());
        assertEquals(2, chart.getRule(2, 1).size());
    }

    @Test
    public void testComplicatedCase() throws Exception {
        GrammarLoader grammarLoader = new GrammarLoader();
        BinaryGrammar grammar = new BinaryGrammar(grammarLoader.loadFromFile(TEST_PATH + "grammar_example.cfg"));

        // a normalized grammar has only two elements on the right side
        for (Rule rule : grammar.getAllRules())
            assertTrue(rule.getRight().length == 1 || rule.getRight().length == 2);

        // a normalized grammar is not changed
        assertEquals(grammar, new BinaryGrammar(grammar));

        Chart<String, String> chart = Chart.getEmptyChart("Time", "flies", "like", "an", "arrow");
        chart.addRule(1, 1, "N");
        chart.addRule(1, 1, "V");
        chart.addRule(1, 2, "N");
        chart.addRule(1, 2, "V");
        chart.addRule(1, 3, "Conj");
        chart.addRule(1, 3, "Prep");
        chart.addRule(1, 4, "Det");
        chart.addRule(1, 5, "N");

        CYK cykAlgo = new CYK(grammar, chart);
        cykAlgo.runCYK();
        assertEquals(4, chart.getRule(5, 1).size());
        assertTrue(chart.getRule(5, 1).contains("S"));
    }
}
