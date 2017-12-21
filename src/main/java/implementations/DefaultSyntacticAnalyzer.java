package implementations;

import implementations.conffile.SyntacticConf;
import implementations.filereaders.GrammarLoader;
import implementations.syntacticutils.BinaryGrammar;
import implementations.syntacticutils.CYK;
import implementations.syntacticutils.Grammar;
import nlpstack.analyzers.SyntacticAnalyzer;
import nlpstack.annotations.LexicalChart;
import nlpstack.annotations.SyntacticChart;
import nlpstack.communication.Chart;

public class DefaultSyntacticAnalyzer extends SyntacticAnalyzer {
    BinaryGrammar grammar;

    public DefaultSyntacticAnalyzer(SyntacticConf conf) throws Exception {
        grammar = new BinaryGrammar((new GrammarLoader()).loadFromFile(conf.grammarPath));
    }

    @Override
    public SyntacticChart apply(LexicalChart lexicalChart) {
        Chart<String, String> chart = lexicalChart.getChart();
        (new CYK(grammar, chart)).runCYK();
        return new SyntacticChart(chart);
    }
}
