// Generated from /home/marc/Documents/prog/NLP-Pipeline/src/main/java/nlpstack/annotations/Parser/AnnotationParser.g4 by ANTLR 4.7
package nlpstack.annotations.Parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AnnotationParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AnnotationParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit(AnnotationParserParser.InitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inputStringThenEOF}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputStringThenEOF(AnnotationParserParser.InputStringThenEOFContext ctx);
	/**
	 * Visit a parse tree produced by the {@code annotationThenEOF}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationThenEOF(AnnotationParserParser.AnnotationThenEOFContext ctx);
	/**
	 * Visit a parse tree produced by the {@code annotationThenToplevel}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationThenToplevel(AnnotationParserParser.AnnotationThenToplevelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inputStringThenAnnotation}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputStringThenAnnotation(AnnotationParserParser.InputStringThenAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inputStringThenAnnotationThenToplevel}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputStringThenAnnotationThenToplevel(AnnotationParserParser.InputStringThenAnnotationThenToplevelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleStringThenEnd}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStringThenEnd(AnnotationParserParser.SimpleStringThenEndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code wordAnnotationThenEnd}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWordAnnotationThenEnd(AnnotationParserParser.WordAnnotationThenEndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code wordAnnotationThenInputString}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWordAnnotationThenInputString(AnnotationParserParser.WordAnnotationThenInputStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleStringThenWordAnnotation}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStringThenWordAnnotation(AnnotationParserParser.SimpleStringThenWordAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleStringThenWordAnnotationThenInputString}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStringThenWordAnnotationThenInputString(AnnotationParserParser.SimpleStringThenWordAnnotationThenInputStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lexicalAnnotationCase}
	 * labeled alternative in {@link AnnotationParserParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLexicalAnnotationCase(AnnotationParserParser.LexicalAnnotationCaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code syntacticAnnotationCase}
	 * labeled alternative in {@link AnnotationParserParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSyntacticAnnotationCase(AnnotationParserParser.SyntacticAnnotationCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#lexicalAnnotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLexicalAnnotation(AnnotationParserParser.LexicalAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#syntacticAnnotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSyntacticAnnotation(AnnotationParserParser.SyntacticAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#wordAnnotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWordAnnotation(AnnotationParserParser.WordAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#word}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWord(AnnotationParserParser.WordContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#simpleString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleString(AnnotationParserParser.SimpleStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#chart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChart(AnnotationParserParser.ChartContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#chartToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChartToken(AnnotationParserParser.ChartTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#chartTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChartTag(AnnotationParserParser.ChartTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#token}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToken(AnnotationParserParser.TokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTag(AnnotationParserParser.TagContext ctx);
	/**
	 * Visit a parse tree produced by {@link AnnotationParserParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(AnnotationParserParser.NumberContext ctx);
}