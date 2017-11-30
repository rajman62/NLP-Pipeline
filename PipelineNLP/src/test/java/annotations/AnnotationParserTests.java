package annotations;

import nlpstack.annotations.Parser.AnnotationParser;
import nlpstack.annotations.StringSegment;
import nlpstack.annotations.StringWithAnnotations;
import nlpstack.communication.Chart;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class AnnotationParserTests {
    @Test
    public void testSimpleStringParsing() {
        List<StringWithAnnotations> test = AnnotationParser.parse("hello world").toArrayList();
        assertEquals(1, test.size());
        assertEquals(false, test.get(0).isAnnotated());
        assertEquals(1, test.get(0).getStrings().size());
        StringSegment segment = test.get(0).getStrings().get(0);
        assertEquals(false, segment.isAnnotated());
        assertEquals("hello world", segment.getNoneAnnotatedString());
    }

    @Test
    public void testSimpleStringParsingWithEscapeCharacters() {
        List<StringWithAnnotations> test = AnnotationParser.parse("hello \\# \\\\ world").toArrayList();
        assertEquals(1, test.size());
        StringSegment segment = test.get(0).getStrings().get(0);
        assertEquals(false, segment.isAnnotated());
        assertEquals("hello # \\ world", segment.getNoneAnnotatedString());
    }

    @Test
    public void testWordAnnotationParsing() {
        List<StringWithAnnotations> test = AnnotationParser.parse("#[\"hello\", \"world\"]").toArrayList();
        assertEquals(1, test.size());
        assertEquals(false, test.get(0).isAnnotated());
        assertEquals(1, test.get(0).getStrings().size());
        StringSegment segment = test.get(0).getStrings().get(0);
        assertEquals(true, segment.isAnnotated());
        Pair<String, String> annotatedWord = segment.getAnnotation();
        assertEquals("hello", annotatedWord.getLeft());
        assertEquals("world", annotatedWord.getRight());
    }

    @Test
    public void testLexicalAnnotationParsing() {
        List<StringWithAnnotations> test =
                AnnotationParser.parse("#[lex: (some)(tokens) : (1, 1, \"N\") (1, 2, \"V\")]").toArrayList();
        assertEquals(1, test.size());
        assertEquals(true, test.get(0).isAnnotated());
        Chart chart = test.get(0).getAnnotatedLexicalChart().getChart();
        assertEquals(1, chart.getRule(1, 1).count("N"));
        assertEquals(0, chart.getRule(1, 1).count("V"));
        assertEquals(1, 2, chart.getRule(1, 2).count("V"));
    }

    @Test
    public void testSyntacticAnnotationParsing() {
        List<StringWithAnnotations> test =
                AnnotationParser.parse("#[syn: (some)(tokens) : (1, 1, \"N\") (1, 2, \"V\")]").toArrayList();
        assertEquals(1, test.size());
        assertEquals(true, test.get(0).isAnnotated());
        assertEquals(true, test.get(0).getAnnotatedLexicalChart().isAnnotated());
        Chart chart = test.get(0).getAnnotatedLexicalChart().getAnnotatedSyntacticChart().getChart();
        assertEquals(1, chart.getRule(1, 1).count("N"));
        assertEquals(0, chart.getRule(1, 1).count("V"));
        assertEquals(1, 2, chart.getRule(1, 2).count("V"));
    }

    @Test
    public void testReturnCharacterInString() {
        List<StringWithAnnotations> test = AnnotationParser.parse(
                "#[syn: (a)(chart) : (1, 1, \"DET\") (1, 2, \"N\") (2, 1, \"NP\")]")
                .toArrayList();
    }

    @Test
    public void completeTest() throws IOException {
        List<StringWithAnnotations> test =
                AnnotationParser.parse(new File("src/test/java/annotations/full_annotation_example.txt")).toArrayList();
        assertEquals(2, test.size());
        List<StringSegment> segments = test.get(0).getStrings();
        assertEquals(3, segments.size());
        assertEquals("This is an example annotation with ", segments.get(0).getNoneAnnotatedString());
        assertEquals(Pair.of("annotated", "V"), segments.get(1).getAnnotation());
        assertEquals(
                " words.\r\n" +
                        "Annotation can also contain full lexical and syntactic charts for sentences:\r\n",
                segments.get(2).getNoneAnnotatedString());
        Chart chart = test.get(1).getAnnotatedLexicalChart().getAnnotatedSyntacticChart().getChart();
        assertEquals("a", chart.getToken(1));
        assertEquals("chart", chart.getToken(2));
        assertEquals(1, chart.getRule(1, 1).count("DET"));
        assertEquals(1, chart.getRule(1, 1).entrySet().size());
        assertEquals(1, chart.getRule(1, 2).count("N"));
        assertEquals(1, chart.getRule(1, 1).entrySet().size());
        assertEquals(1, chart.getRule(2, 1).count("NP"));
        assertEquals(1, chart.getRule(2, 1).entrySet().size());
    }
}
