package nlpstack;

import nlpstack.analyzers.*;

public interface Configuration {
    void parse(String confFilePath) throws Exception;

    LexicalAnalyzer getLexicalAnalyzer();

    SyntacticAnalyzer getSyntacticAnalyzer();

    SemanticAnalyzer getSemanticAnalyzer();
}
