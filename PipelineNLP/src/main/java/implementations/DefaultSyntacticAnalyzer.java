package implementations;

import implementations.conffile.SyntacticConf;
import nlpstack.analyzers.SyntacticAnalyzer;
import nlpstack.annotations.LexicalChart;
import nlpstack.annotations.SyntacticChart;
import nlpstack.communication.Chart;
import nlpstack.communication.Occurences;

public class DefaultSyntacticAnalyzer extends SyntacticAnalyzer {
    public DefaultSyntacticAnalyzer(SyntacticConf conf) {

    }

    @Override
    public SyntacticChart apply(LexicalChart lexicalChart) {
        return null;
    }
}
