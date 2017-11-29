package filereaders;

import org.junit.Test;
import org.leibnizcenter.cfg.category.Category;
import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;
import org.leibnizcenter.cfg.earleyparser.Parser;
import org.leibnizcenter.cfg.earleyparser.scan.TokenNotInLexiconException;
import org.leibnizcenter.cfg.grammar.Grammar;
import org.leibnizcenter.cfg.token.Tokens;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoadAndStoreGrammars {
    private Grammar<String> grammar = Grammar.parse(Paths.get("src/test/java/filereaders/grammar_example.cfg"), Charset.forName("UTF-8"));

    public LoadAndStoreGrammars() throws IOException {
    }

    @Test
    public void loadGrammar() throws IOException {
        Parser<String> parser = new Parser<>(grammar);
        NonTerminal S = Category.nonTerminal("S");
        assertTrue(parser.recognize(S, Tokens.tokenize("the heavy heave")) > 0);
    }

    @Test(expected = TokenNotInLexiconException.class)
    public void missingTerminal() throws IOException {
        Parser<String> parser = new Parser<>(grammar);
        NonTerminal S = Category.nonTerminal("S");
        parser.recognize(S, Tokens.tokenize("the man heavy heave"));
    }

    @Test(expected = RuntimeException.class)
    public void noParseTree() throws IOException {
        Parser<String> parser = new Parser<>(grammar);
        NonTerminal S = Category.nonTerminal("S");
        assertEquals(0.0, parser.recognize(S, Tokens.tokenize("heavy the heavy heave")), 0.001);
        parser.getViterbiParseWithScore(S, Tokens.tokenize("heavy the heavy heave"));
    }
}
