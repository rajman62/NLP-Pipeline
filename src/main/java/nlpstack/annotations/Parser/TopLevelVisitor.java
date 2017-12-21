package nlpstack.annotations.Parser;

import nlpstack.annotations.LexicalChart;
import nlpstack.annotations.StringWithAnnotations;
import nlpstack.annotations.SyntacticChart;

import java.util.ArrayList;


public class TopLevelVisitor extends AnnotationParserBaseVisitor<LinkedList<StringWithAnnotations>> {
    @Override
    public LinkedList<StringWithAnnotations> visitInit(AnnotationParserParser.InitContext ctx) {
        return visit(ctx.toplevel());
    }

    @Override
    public LinkedList<StringWithAnnotations> visitInputStringThenEOF(AnnotationParserParser.InputStringThenEOFContext ctx) {
        return visitInputString(ctx.inputString());
    }

    @Override
    public LinkedList<StringWithAnnotations> visitAnnotationThenEOF(AnnotationParserParser.AnnotationThenEOFContext ctx) {
        return visit(ctx.annotation());
    }

    @Override
    public LinkedList<StringWithAnnotations> visitAnnotationThenToplevel(AnnotationParserParser.AnnotationThenToplevelContext ctx) {
        LinkedList<StringWithAnnotations> annotation = visit(ctx.annotation());
        LinkedList<StringWithAnnotations> topLevel = visit(ctx.toplevel());
        annotation.appendAtTail(topLevel);
        return annotation;
    }

    @Override
    public LinkedList<StringWithAnnotations> visitInputStringThenAnnotation(AnnotationParserParser.InputStringThenAnnotationContext ctx) {
        LinkedList<StringWithAnnotations> inputString = visitInputString(ctx.inputString());
        LinkedList<StringWithAnnotations> annotation = visit(ctx.annotation());
        inputString.appendAtTail(annotation);
        return inputString;
    }

    @Override
    public LinkedList<StringWithAnnotations> visitInputStringThenAnnotationThenToplevel(AnnotationParserParser.InputStringThenAnnotationThenToplevelContext ctx) {
        LinkedList<StringWithAnnotations> inputString = visitInputString(ctx.inputString());
        LinkedList<StringWithAnnotations> annotation = visit(ctx.annotation());
        LinkedList<StringWithAnnotations> topLevel = visit(ctx.toplevel());
        inputString.appendAtTail(annotation);
        inputString.appendAtTail(topLevel);
        return inputString;
    }

    LinkedList<StringWithAnnotations> visitInputString(AnnotationParserParser.InputStringContext ctx) {
        StringWithAnnotations out = StringWithAnnotations.fromStringSegment(
                (new InputStringVisitor()).visit(ctx).toArrayList()
        );
        return new LinkedList<>(out);
    }

    @Override
    public LinkedList<StringWithAnnotations> visitLexicalAnnotation(AnnotationParserParser.LexicalAnnotationContext ctx) {
        StringWithAnnotations annotation =
                StringWithAnnotations.fromLexicalAnnotation(
                        LexicalChart.fromChart(
                                (new ChartVisitor()).visitChart(ctx.chart())
                        )
                );
        return new LinkedList<>(annotation);
    }

    @Override
    public LinkedList<StringWithAnnotations> visitSyntacticAnnotation(AnnotationParserParser.SyntacticAnnotationContext ctx) {
        StringWithAnnotations annotation =
                StringWithAnnotations.fromSyntacticAnnotation(
                        new SyntacticChart((new ChartVisitor()).visitChart(ctx.chart()))
                );
        return new LinkedList<>(annotation);
    }
}
