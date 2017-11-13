package implementations;

import dk.brics.automaton.Automaton;
import implementations.conffile.LexicalConf;
import implementations.filereaders.FSALoader;
import nlpstack.analyzers.LexicalAnalyzer;
import nlpstack.annotations.LexicalChart;
import nlpstack.annotations.StringSegment;

import java.util.List;
import java.util.stream.Stream;

public class DefaultLexicalAnalyzer extends LexicalAnalyzer {
    private Automaton wordFSA;
    private Automaton separatorFSA;
    private FSALoader fsaLoader = new FSALoader();

    DefaultLexicalAnalyzer(LexicalConf conf) throws Exception {
        wordFSA = fsaLoader.loadFromFile(conf.wordFSAPath);
        separatorFSA = fsaLoader.loadFromFile(conf.separatorFSAPath);
    }

    @Override
    public Stream<LexicalChart> apply(List<StringSegment> stringSegments) {
        return null;
    }
}
