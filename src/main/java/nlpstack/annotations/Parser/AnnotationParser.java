package nlpstack.annotations.Parser;

import nlpstack.annotations.StringWithAnnotations;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;

public class AnnotationParser {
    public static void main(String[] args) throws Exception {
        grammarTest();
    }

    public static LinkedList<StringWithAnnotations> parse(String input) {
        AnnotationParserLexer lexer = new AnnotationParserLexer(
                new ANTLRInputStream(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AnnotationParserParser parser = new AnnotationParserParser(tokens);
        ParseTree tree = parser.init();
        TopLevelVisitor visitor = new TopLevelVisitor();
        return visitor.visit(tree);
    }

    public static LinkedList<StringWithAnnotations> parse(File input) throws IOException {
        AnnotationParserLexer lexer = new AnnotationParserLexer(
                new ANTLRInputStream(new FileReader(input)));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AnnotationParserParser parser = new AnnotationParserParser(tokens);
        ParseTree tree = parser.init();
        TopLevelVisitor visitor = new TopLevelVisitor();
        return visitor.visit(tree);
    }

    static void grammarTest() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String line = reader.readLine();
            AnnotationParserLexer lexer = new AnnotationParserLexer(
                    new ANTLRInputStream(line));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            AnnotationParserParser parser = new AnnotationParserParser(tokens);
            ParseTree tree = parser.init();
            System.out.println(tree.toStringTree(parser));
        }
    }
}
