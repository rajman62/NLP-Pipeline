package implementations;

import dk.brics.automaton.Automaton;
import implementations.conffile.LexicalConf;
import implementations.filereaders.FSALoader;
import nlpstack.analyzers.LexicalAnalyzer;
import nlpstack.annotations.AnnotatedChart;
import nlpstack.annotations.AnnotatedString;

import java.util.stream.Stream;

public class DefaultLexicalAnalyzer implements LexicalAnalyzer {
    private Automaton wordFSA;
    private Automaton separatorFSA;
    private FSALoader fsaLoader = new FSALoader();

    DefaultLexicalAnalyzer(LexicalConf conf) throws Exception {
        wordFSA = fsaLoader.loadFromFile(conf.wordFSAPath);
        separatorFSA = fsaLoader.loadFromFile(conf.separatorFSAPath);
    }

    @Override
    public Stream<AnnotatedChart> tokenize(Stream<AnnotatedString> input) {
        return null;
    }
}
