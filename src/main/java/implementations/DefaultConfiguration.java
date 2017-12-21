package implementations;

import implementations.conffile.ConfFile;
import nlpstack.Configuration;
import nlpstack.analyzers.LexicalAnalyzer;
import nlpstack.analyzers.SemanticAnalyzer;
import nlpstack.analyzers.SyntacticAnalyzer;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import nlpstack.communication.ErrorLogger;

import java.io.File;


public class DefaultConfiguration implements Configuration {
    private DefaultLexicalAnalyzer lexicalAnalyzer = null;
    private DefaultSyntacticAnalyzer syntacticAnalyzer = null;

    @Override
    public void parse(String confFilePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfFile conf = mapper.readValue(new File(confFilePath), ConfFile.class);
        ErrorLogger errorLogger = new ErrorLogger();
        lexicalAnalyzer = new DefaultLexicalAnalyzer(conf.lexicalConf, errorLogger);
        syntacticAnalyzer = new DefaultSyntacticAnalyzer(conf.syntacticConf);
    }

    @Override
    public LexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }

    @Override
    public SyntacticAnalyzer getSyntacticAnalyzer() {
        return syntacticAnalyzer;
    }

    @Override
    public SemanticAnalyzer getSemanticAnalyzer() {
        return null;
    }
}
