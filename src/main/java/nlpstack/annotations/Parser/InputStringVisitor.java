package nlpstack.annotations.Parser;

import nlpstack.annotations.StringSegment;
import org.apache.commons.lang3.tuple.Pair;


public class InputStringVisitor extends AnnotationParserBaseVisitor<LinkedList<StringSegment>> {
    @Override
    public LinkedList<StringSegment> visitSimpleStringThenEnd(AnnotationParserParser.SimpleStringThenEndContext ctx) {
        return visit(ctx.simpleString());
    }

    @Override
    public LinkedList<StringSegment> visitWordAnnotationThenEnd(AnnotationParserParser.WordAnnotationThenEndContext ctx) {
        return visit(ctx.wordAnnotation());
    }

    @Override
    public LinkedList<StringSegment> visitWordAnnotationThenInputString(AnnotationParserParser.WordAnnotationThenInputStringContext ctx) {
        LinkedList<StringSegment> wordAnnotation = visit(ctx.wordAnnotation());
        LinkedList<StringSegment> inputString = visit(ctx.inputString());
        wordAnnotation.appendAtTail(inputString);
        return wordAnnotation;
    }

    @Override
    public LinkedList<StringSegment> visitSimpleStringThenWordAnnotation(AnnotationParserParser.SimpleStringThenWordAnnotationContext ctx) {
        LinkedList<StringSegment> simpleString = visit(ctx.simpleString());
        LinkedList<StringSegment> wordAnnotation = visit(ctx.wordAnnotation());
        simpleString.appendAtTail(wordAnnotation);
        return simpleString;
    }

    @Override
    public LinkedList<StringSegment> visitSimpleStringThenWordAnnotationThenInputString(AnnotationParserParser.SimpleStringThenWordAnnotationThenInputStringContext ctx) {
        LinkedList<StringSegment> simpleString = visit(ctx.simpleString());
        LinkedList<StringSegment> wordAnnotation = visit(ctx.wordAnnotation());
        LinkedList<StringSegment> inputString = visit(ctx.inputString());
        simpleString.appendAtTail(wordAnnotation);
        simpleString.appendAtTail(inputString);
        return simpleString;
    }

    @Override
    public LinkedList<StringSegment> visitSimpleString(AnnotationParserParser.SimpleStringContext ctx) {
        return new LinkedList<>(StringSegment.fromString(
                ctx.getText()
                        .replace("\\\\", "\\").replace("\\#", "#")
        ));
    }

    @Override
    public LinkedList<StringSegment> visitWordAnnotation(AnnotationParserParser.WordAnnotationContext ctx) {
        StringSegment segment = (StringSegment.fromAnnotatedWord(
                Pair.of(
                        ctx.word().getText()
                                .replace("\\\\", "\\").replace("\\\"", "\""),
                        ctx.tag().getText()
                                .replace("\\\\", "\\").replace("\\\"", "\"")
                )
        ));
        return new LinkedList<>(segment);
    }
}
