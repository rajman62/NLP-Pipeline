// Generated from /home/marc/Documents/prog/NLP-Pipeline/src/main/java/nlpstack/annotations/Parser/AnnotationParser.g4 by ANTLR 4.7
package nlpstack.annotations.Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AnnotationParserParser}.
 */
public interface AnnotationParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#init}.
	 * @param ctx the parse tree
	 */
	void enterInit(AnnotationParserParser.InitContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#init}.
	 * @param ctx the parse tree
	 */
	void exitInit(AnnotationParserParser.InitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inputStringThenEOF}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void enterInputStringThenEOF(AnnotationParserParser.InputStringThenEOFContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inputStringThenEOF}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void exitInputStringThenEOF(AnnotationParserParser.InputStringThenEOFContext ctx);
	/**
	 * Enter a parse tree produced by the {@code annotationThenEOF}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationThenEOF(AnnotationParserParser.AnnotationThenEOFContext ctx);
	/**
	 * Exit a parse tree produced by the {@code annotationThenEOF}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationThenEOF(AnnotationParserParser.AnnotationThenEOFContext ctx);
	/**
	 * Enter a parse tree produced by the {@code annotationThenToplevel}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationThenToplevel(AnnotationParserParser.AnnotationThenToplevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code annotationThenToplevel}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationThenToplevel(AnnotationParserParser.AnnotationThenToplevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inputStringThenAnnotation}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void enterInputStringThenAnnotation(AnnotationParserParser.InputStringThenAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inputStringThenAnnotation}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void exitInputStringThenAnnotation(AnnotationParserParser.InputStringThenAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inputStringThenAnnotationThenToplevel}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void enterInputStringThenAnnotationThenToplevel(AnnotationParserParser.InputStringThenAnnotationThenToplevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inputStringThenAnnotationThenToplevel}
	 * labeled alternative in {@link AnnotationParserParser#toplevel}.
	 * @param ctx the parse tree
	 */
	void exitInputStringThenAnnotationThenToplevel(AnnotationParserParser.InputStringThenAnnotationThenToplevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleStringThenEnd}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStringThenEnd(AnnotationParserParser.SimpleStringThenEndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleStringThenEnd}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStringThenEnd(AnnotationParserParser.SimpleStringThenEndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wordAnnotationThenEnd}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void enterWordAnnotationThenEnd(AnnotationParserParser.WordAnnotationThenEndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wordAnnotationThenEnd}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void exitWordAnnotationThenEnd(AnnotationParserParser.WordAnnotationThenEndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wordAnnotationThenInputString}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void enterWordAnnotationThenInputString(AnnotationParserParser.WordAnnotationThenInputStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wordAnnotationThenInputString}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void exitWordAnnotationThenInputString(AnnotationParserParser.WordAnnotationThenInputStringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleStringThenWordAnnotation}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStringThenWordAnnotation(AnnotationParserParser.SimpleStringThenWordAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleStringThenWordAnnotation}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStringThenWordAnnotation(AnnotationParserParser.SimpleStringThenWordAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleStringThenWordAnnotationThenInputString}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStringThenWordAnnotationThenInputString(AnnotationParserParser.SimpleStringThenWordAnnotationThenInputStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleStringThenWordAnnotationThenInputString}
	 * labeled alternative in {@link AnnotationParserParser#inputString}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStringThenWordAnnotationThenInputString(AnnotationParserParser.SimpleStringThenWordAnnotationThenInputStringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lexicalAnnotationCase}
	 * labeled alternative in {@link AnnotationParserParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterLexicalAnnotationCase(AnnotationParserParser.LexicalAnnotationCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lexicalAnnotationCase}
	 * labeled alternative in {@link AnnotationParserParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitLexicalAnnotationCase(AnnotationParserParser.LexicalAnnotationCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code syntacticAnnotationCase}
	 * labeled alternative in {@link AnnotationParserParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterSyntacticAnnotationCase(AnnotationParserParser.SyntacticAnnotationCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code syntacticAnnotationCase}
	 * labeled alternative in {@link AnnotationParserParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitSyntacticAnnotationCase(AnnotationParserParser.SyntacticAnnotationCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#lexicalAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterLexicalAnnotation(AnnotationParserParser.LexicalAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#lexicalAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitLexicalAnnotation(AnnotationParserParser.LexicalAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#syntacticAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterSyntacticAnnotation(AnnotationParserParser.SyntacticAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#syntacticAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitSyntacticAnnotation(AnnotationParserParser.SyntacticAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#wordAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterWordAnnotation(AnnotationParserParser.WordAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#wordAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitWordAnnotation(AnnotationParserParser.WordAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#word}.
	 * @param ctx the parse tree
	 */
	void enterWord(AnnotationParserParser.WordContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#word}.
	 * @param ctx the parse tree
	 */
	void exitWord(AnnotationParserParser.WordContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#simpleString}.
	 * @param ctx the parse tree
	 */
	void enterSimpleString(AnnotationParserParser.SimpleStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#simpleString}.
	 * @param ctx the parse tree
	 */
	void exitSimpleString(AnnotationParserParser.SimpleStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#chart}.
	 * @param ctx the parse tree
	 */
	void enterChart(AnnotationParserParser.ChartContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#chart}.
	 * @param ctx the parse tree
	 */
	void exitChart(AnnotationParserParser.ChartContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#chartToken}.
	 * @param ctx the parse tree
	 */
	void enterChartToken(AnnotationParserParser.ChartTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#chartToken}.
	 * @param ctx the parse tree
	 */
	void exitChartToken(AnnotationParserParser.ChartTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#chartTag}.
	 * @param ctx the parse tree
	 */
	void enterChartTag(AnnotationParserParser.ChartTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#chartTag}.
	 * @param ctx the parse tree
	 */
	void exitChartTag(AnnotationParserParser.ChartTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#token}.
	 * @param ctx the parse tree
	 */
	void enterToken(AnnotationParserParser.TokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#token}.
	 * @param ctx the parse tree
	 */
	void exitToken(AnnotationParserParser.TokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#tag}.
	 * @param ctx the parse tree
	 */
	void enterTag(AnnotationParserParser.TagContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#tag}.
	 * @param ctx the parse tree
	 */
	void exitTag(AnnotationParserParser.TagContext ctx);
	/**
	 * Enter a parse tree produced by {@link AnnotationParserParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(AnnotationParserParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link AnnotationParserParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(AnnotationParserParser.NumberContext ctx);
}