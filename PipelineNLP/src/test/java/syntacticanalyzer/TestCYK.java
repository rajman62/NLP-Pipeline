package syntacticanalyzer;

import implementations.syntacticutils.*;
import nlpstack.communication.Chart;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestCYK {
    @Test
    public void testSimpleCase() {
        Grammar grammar = new Grammar();
        grammar.addRule(Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP"), RealNonTerminal.of("V")));
        grammar.addRule(Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP")));
        grammar.addRule(Rule.from(RealNonTerminal.of("NP"), RealNonTerminal.of("Det"), RealNonTerminal.of("N")));
        grammar.addRule(Rule.from(RealNonTerminal.of("Det"), Terminal.of("the")));
        grammar.addRule(Rule.from(RealNonTerminal.of("N"), Terminal.of("string")));

        Chart<String, String> chart = Chart.getEmptyChart("the", "string");
        CYK cyk = new CYK(grammar, chart);
        cyk.runCYK();
        assertEquals(1, chart.getRule(1, 1).size());
        assertEquals(1, chart.getRule(1, 2).size());
        assertEquals(2, chart.getRule(2, 1).size());
    }
}
