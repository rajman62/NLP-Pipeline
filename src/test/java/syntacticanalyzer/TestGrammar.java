package syntacticanalyzer;

import implementations.syntacticutils.AbstractNonTerminal;
import implementations.syntacticutils.RealNonTerminal;
import implementations.syntacticutils.Rule;
import implementations.syntacticutils.Terminal;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class TestGrammar {
    @Test
    public void testCorrectHashingOfRules() {
        AbstractNonTerminal abstractNonTerminal1 = new AbstractNonTerminal();
        AbstractNonTerminal abstractNonTerminal2 = abstractNonTerminal1;
        AbstractNonTerminal abstractNonTerminal3 = new AbstractNonTerminal();
        Rule rule1 = Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP"), Terminal.of("the"), abstractNonTerminal1);
        Rule rule2 = Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP"), Terminal.of("the"), abstractNonTerminal2);
        Rule rule3 = Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP"), Terminal.of("the"), abstractNonTerminal3);
        Rule rule4 = Rule.from(RealNonTerminal.of("X"), RealNonTerminal.of("NP"), Terminal.of("the"), abstractNonTerminal1);
        Rule rule5 = Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP"), Terminal.of("th"), abstractNonTerminal1);

        assertTrue(rule1.equals(rule2));
        assertTrue(abstractNonTerminal1.equals(abstractNonTerminal2));
        assertFalse(abstractNonTerminal1.equals(abstractNonTerminal3));
        assertEquals(abstractNonTerminal1.hashCode(), abstractNonTerminal2.hashCode());
        assertNotEquals(abstractNonTerminal1.hashCode(), abstractNonTerminal3.hashCode());
        assertFalse(rule3.equals(rule1));
        assertFalse(rule1.equals(rule4));
        assertFalse(rule1.equals(rule5));

        HashSet<Rule> set = new HashSet<>();
        set.add(rule1);
        set.add(rule2);
        set.add(rule3);
        set.add(rule4);
        set.add(rule5);
        assertEquals(4, set.size());
    }

    @Test
    public void testCorrectPrintingOfRules() {
        AbstractNonTerminal abstractNonTerminal1 = new AbstractNonTerminal();
        Rule rule1 = Rule.from(RealNonTerminal.of("S"), RealNonTerminal.of("NP"), Terminal.of("the"), abstractNonTerminal1);
        rule1.toString();
    }
}
