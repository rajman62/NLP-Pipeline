// Generated from /home/marc/Documents/prog/NLP-Pipeline/src/main/java/nlpstack/annotations/Parser/AnnotationParser.g4 by ANTLR 4.7
package nlpstack.annotations.Parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AnnotationParserParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, TAGDELIMITER=3, ANNOTATIONCHARACTER=4, ANNOTATIONOPEN=5, 
		ANNOTATIONCLOSE=6, ESCAPECHARACTER=7, COLON=8, COMMA=9, OPENBRACKET=10, 
		CLOSEBRACKET=11, SPACE=12, DIGIT=13, CHARACTER=14;
	public static final int
		RULE_init = 0, RULE_toplevel = 1, RULE_inputString = 2, RULE_annotation = 3, 
		RULE_lexicalAnnotation = 4, RULE_syntacticAnnotation = 5, RULE_wordAnnotation = 6, 
		RULE_word = 7, RULE_simpleString = 8, RULE_chart = 9, RULE_chartToken = 10, 
		RULE_chartTag = 11, RULE_token = 12, RULE_tag = 13, RULE_number = 14;
	public static final String[] ruleNames = {
		"init", "toplevel", "inputString", "annotation", "lexicalAnnotation", 
		"syntacticAnnotation", "wordAnnotation", "word", "simpleString", "chart", 
		"chartToken", "chartTag", "token", "tag", "number"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'lex:'", "'syn:'", "'\"'", "'#'", "'['", "']'", "'\\'", "':'", 
		"','", "'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "TAGDELIMITER", "ANNOTATIONCHARACTER", "ANNOTATIONOPEN", 
		"ANNOTATIONCLOSE", "ESCAPECHARACTER", "COLON", "COMMA", "OPENBRACKET", 
		"CLOSEBRACKET", "SPACE", "DIGIT", "CHARACTER"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "AnnotationParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AnnotationParserParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class InitContext extends ParserRuleContext {
		public ToplevelContext toplevel() {
			return getRuleContext(ToplevelContext.class,0);
		}
		public TerminalNode EOF() { return getToken(AnnotationParserParser.EOF, 0); }
		public InitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitInit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitContext init() throws RecognitionException {
		InitContext _localctx = new InitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_init);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			toplevel();
			setState(31);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ToplevelContext extends ParserRuleContext {
		public ToplevelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_toplevel; }
	 
		public ToplevelContext() { }
		public void copyFrom(ToplevelContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class InputStringThenAnnotationThenToplevelContext extends ToplevelContext {
		public InputStringContext inputString() {
			return getRuleContext(InputStringContext.class,0);
		}
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public ToplevelContext toplevel() {
			return getRuleContext(ToplevelContext.class,0);
		}
		public InputStringThenAnnotationThenToplevelContext(ToplevelContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterInputStringThenAnnotationThenToplevel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitInputStringThenAnnotationThenToplevel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitInputStringThenAnnotationThenToplevel(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InputStringThenEOFContext extends ToplevelContext {
		public InputStringContext inputString() {
			return getRuleContext(InputStringContext.class,0);
		}
		public InputStringThenEOFContext(ToplevelContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterInputStringThenEOF(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitInputStringThenEOF(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitInputStringThenEOF(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InputStringThenAnnotationContext extends ToplevelContext {
		public InputStringContext inputString() {
			return getRuleContext(InputStringContext.class,0);
		}
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public InputStringThenAnnotationContext(ToplevelContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterInputStringThenAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitInputStringThenAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitInputStringThenAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnnotationThenToplevelContext extends ToplevelContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public ToplevelContext toplevel() {
			return getRuleContext(ToplevelContext.class,0);
		}
		public AnnotationThenToplevelContext(ToplevelContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterAnnotationThenToplevel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitAnnotationThenToplevel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitAnnotationThenToplevel(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnnotationThenEOFContext extends ToplevelContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public AnnotationThenEOFContext(ToplevelContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterAnnotationThenEOF(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitAnnotationThenEOF(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitAnnotationThenEOF(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ToplevelContext toplevel() throws RecognitionException {
		ToplevelContext _localctx = new ToplevelContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_toplevel);
		try {
			setState(45);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				_localctx = new InputStringThenEOFContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(33);
				inputString();
				}
				break;
			case 2:
				_localctx = new AnnotationThenEOFContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(34);
				annotation();
				}
				break;
			case 3:
				_localctx = new AnnotationThenToplevelContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(35);
				annotation();
				setState(36);
				toplevel();
				}
				break;
			case 4:
				_localctx = new InputStringThenAnnotationContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(38);
				inputString();
				setState(39);
				annotation();
				}
				break;
			case 5:
				_localctx = new InputStringThenAnnotationThenToplevelContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(41);
				inputString();
				setState(42);
				annotation();
				setState(43);
				toplevel();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InputStringContext extends ParserRuleContext {
		public InputStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputString; }
	 
		public InputStringContext() { }
		public void copyFrom(InputStringContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class WordAnnotationThenEndContext extends InputStringContext {
		public WordAnnotationContext wordAnnotation() {
			return getRuleContext(WordAnnotationContext.class,0);
		}
		public WordAnnotationThenEndContext(InputStringContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterWordAnnotationThenEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitWordAnnotationThenEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitWordAnnotationThenEnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpleStringThenWordAnnotationContext extends InputStringContext {
		public SimpleStringContext simpleString() {
			return getRuleContext(SimpleStringContext.class,0);
		}
		public WordAnnotationContext wordAnnotation() {
			return getRuleContext(WordAnnotationContext.class,0);
		}
		public SimpleStringThenWordAnnotationContext(InputStringContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterSimpleStringThenWordAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitSimpleStringThenWordAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitSimpleStringThenWordAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpleStringThenEndContext extends InputStringContext {
		public SimpleStringContext simpleString() {
			return getRuleContext(SimpleStringContext.class,0);
		}
		public SimpleStringThenEndContext(InputStringContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterSimpleStringThenEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitSimpleStringThenEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitSimpleStringThenEnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpleStringThenWordAnnotationThenInputStringContext extends InputStringContext {
		public SimpleStringContext simpleString() {
			return getRuleContext(SimpleStringContext.class,0);
		}
		public WordAnnotationContext wordAnnotation() {
			return getRuleContext(WordAnnotationContext.class,0);
		}
		public InputStringContext inputString() {
			return getRuleContext(InputStringContext.class,0);
		}
		public SimpleStringThenWordAnnotationThenInputStringContext(InputStringContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterSimpleStringThenWordAnnotationThenInputString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitSimpleStringThenWordAnnotationThenInputString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitSimpleStringThenWordAnnotationThenInputString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WordAnnotationThenInputStringContext extends InputStringContext {
		public WordAnnotationContext wordAnnotation() {
			return getRuleContext(WordAnnotationContext.class,0);
		}
		public InputStringContext inputString() {
			return getRuleContext(InputStringContext.class,0);
		}
		public WordAnnotationThenInputStringContext(InputStringContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterWordAnnotationThenInputString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitWordAnnotationThenInputString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitWordAnnotationThenInputString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputStringContext inputString() throws RecognitionException {
		InputStringContext _localctx = new InputStringContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_inputString);
		try {
			setState(59);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new SimpleStringThenEndContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(47);
				simpleString();
				}
				break;
			case 2:
				_localctx = new WordAnnotationThenEndContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(48);
				wordAnnotation();
				}
				break;
			case 3:
				_localctx = new WordAnnotationThenInputStringContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(49);
				wordAnnotation();
				setState(50);
				inputString();
				}
				break;
			case 4:
				_localctx = new SimpleStringThenWordAnnotationContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(52);
				simpleString();
				setState(53);
				wordAnnotation();
				}
				break;
			case 5:
				_localctx = new SimpleStringThenWordAnnotationThenInputStringContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(55);
				simpleString();
				setState(56);
				wordAnnotation();
				setState(57);
				inputString();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationContext extends ParserRuleContext {
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
	 
		public AnnotationContext() { }
		public void copyFrom(AnnotationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SyntacticAnnotationCaseContext extends AnnotationContext {
		public SyntacticAnnotationContext syntacticAnnotation() {
			return getRuleContext(SyntacticAnnotationContext.class,0);
		}
		public SyntacticAnnotationCaseContext(AnnotationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterSyntacticAnnotationCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitSyntacticAnnotationCase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitSyntacticAnnotationCase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LexicalAnnotationCaseContext extends AnnotationContext {
		public LexicalAnnotationContext lexicalAnnotation() {
			return getRuleContext(LexicalAnnotationContext.class,0);
		}
		public LexicalAnnotationCaseContext(AnnotationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterLexicalAnnotationCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitLexicalAnnotationCase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitLexicalAnnotationCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_annotation);
		try {
			setState(63);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new LexicalAnnotationCaseContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				lexicalAnnotation();
				}
				break;
			case 2:
				_localctx = new SyntacticAnnotationCaseContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(62);
				syntacticAnnotation();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LexicalAnnotationContext extends ParserRuleContext {
		public TerminalNode ANNOTATIONCHARACTER() { return getToken(AnnotationParserParser.ANNOTATIONCHARACTER, 0); }
		public TerminalNode ANNOTATIONOPEN() { return getToken(AnnotationParserParser.ANNOTATIONOPEN, 0); }
		public ChartContext chart() {
			return getRuleContext(ChartContext.class,0);
		}
		public TerminalNode ANNOTATIONCLOSE() { return getToken(AnnotationParserParser.ANNOTATIONCLOSE, 0); }
		public LexicalAnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lexicalAnnotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterLexicalAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitLexicalAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitLexicalAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LexicalAnnotationContext lexicalAnnotation() throws RecognitionException {
		LexicalAnnotationContext _localctx = new LexicalAnnotationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_lexicalAnnotation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			match(ANNOTATIONCHARACTER);
			setState(66);
			match(ANNOTATIONOPEN);
			setState(67);
			match(T__0);
			setState(68);
			chart();
			setState(69);
			match(ANNOTATIONCLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SyntacticAnnotationContext extends ParserRuleContext {
		public TerminalNode ANNOTATIONCHARACTER() { return getToken(AnnotationParserParser.ANNOTATIONCHARACTER, 0); }
		public TerminalNode ANNOTATIONOPEN() { return getToken(AnnotationParserParser.ANNOTATIONOPEN, 0); }
		public ChartContext chart() {
			return getRuleContext(ChartContext.class,0);
		}
		public TerminalNode ANNOTATIONCLOSE() { return getToken(AnnotationParserParser.ANNOTATIONCLOSE, 0); }
		public SyntacticAnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_syntacticAnnotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterSyntacticAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitSyntacticAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitSyntacticAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SyntacticAnnotationContext syntacticAnnotation() throws RecognitionException {
		SyntacticAnnotationContext _localctx = new SyntacticAnnotationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_syntacticAnnotation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(ANNOTATIONCHARACTER);
			setState(72);
			match(ANNOTATIONOPEN);
			setState(73);
			match(T__1);
			setState(74);
			chart();
			setState(75);
			match(ANNOTATIONCLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WordAnnotationContext extends ParserRuleContext {
		public TerminalNode ANNOTATIONCHARACTER() { return getToken(AnnotationParserParser.ANNOTATIONCHARACTER, 0); }
		public TerminalNode ANNOTATIONOPEN() { return getToken(AnnotationParserParser.ANNOTATIONOPEN, 0); }
		public List<TerminalNode> TAGDELIMITER() { return getTokens(AnnotationParserParser.TAGDELIMITER); }
		public TerminalNode TAGDELIMITER(int i) {
			return getToken(AnnotationParserParser.TAGDELIMITER, i);
		}
		public WordContext word() {
			return getRuleContext(WordContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(AnnotationParserParser.COMMA, 0); }
		public TagContext tag() {
			return getRuleContext(TagContext.class,0);
		}
		public TerminalNode ANNOTATIONCLOSE() { return getToken(AnnotationParserParser.ANNOTATIONCLOSE, 0); }
		public List<TerminalNode> SPACE() { return getTokens(AnnotationParserParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(AnnotationParserParser.SPACE, i);
		}
		public WordAnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wordAnnotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterWordAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitWordAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitWordAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WordAnnotationContext wordAnnotation() throws RecognitionException {
		WordAnnotationContext _localctx = new WordAnnotationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_wordAnnotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			match(ANNOTATIONCHARACTER);
			setState(78);
			match(ANNOTATIONOPEN);
			setState(82);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(79);
				match(SPACE);
				}
				}
				setState(84);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(85);
			match(TAGDELIMITER);
			setState(86);
			word();
			setState(87);
			match(TAGDELIMITER);
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(88);
				match(SPACE);
				}
				}
				setState(93);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(94);
			match(COMMA);
			setState(98);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(95);
				match(SPACE);
				}
				}
				setState(100);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(101);
			match(TAGDELIMITER);
			setState(102);
			tag();
			setState(103);
			match(TAGDELIMITER);
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(104);
				match(SPACE);
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(110);
			match(ANNOTATIONCLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WordContext extends ParserRuleContext {
		public List<TerminalNode> ANNOTATIONCHARACTER() { return getTokens(AnnotationParserParser.ANNOTATIONCHARACTER); }
		public TerminalNode ANNOTATIONCHARACTER(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONCHARACTER, i);
		}
		public List<TerminalNode> ANNOTATIONOPEN() { return getTokens(AnnotationParserParser.ANNOTATIONOPEN); }
		public TerminalNode ANNOTATIONOPEN(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONOPEN, i);
		}
		public List<TerminalNode> ANNOTATIONCLOSE() { return getTokens(AnnotationParserParser.ANNOTATIONCLOSE); }
		public TerminalNode ANNOTATIONCLOSE(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONCLOSE, i);
		}
		public List<TerminalNode> COLON() { return getTokens(AnnotationParserParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(AnnotationParserParser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AnnotationParserParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AnnotationParserParser.COMMA, i);
		}
		public List<TerminalNode> OPENBRACKET() { return getTokens(AnnotationParserParser.OPENBRACKET); }
		public TerminalNode OPENBRACKET(int i) {
			return getToken(AnnotationParserParser.OPENBRACKET, i);
		}
		public List<TerminalNode> CLOSEBRACKET() { return getTokens(AnnotationParserParser.CLOSEBRACKET); }
		public TerminalNode CLOSEBRACKET(int i) {
			return getToken(AnnotationParserParser.CLOSEBRACKET, i);
		}
		public List<TerminalNode> SPACE() { return getTokens(AnnotationParserParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(AnnotationParserParser.SPACE, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(AnnotationParserParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(AnnotationParserParser.DIGIT, i);
		}
		public List<TerminalNode> CHARACTER() { return getTokens(AnnotationParserParser.CHARACTER); }
		public TerminalNode CHARACTER(int i) {
			return getToken(AnnotationParserParser.CHARACTER, i);
		}
		public List<TerminalNode> ESCAPECHARACTER() { return getTokens(AnnotationParserParser.ESCAPECHARACTER); }
		public TerminalNode ESCAPECHARACTER(int i) {
			return getToken(AnnotationParserParser.ESCAPECHARACTER, i);
		}
		public List<TerminalNode> TAGDELIMITER() { return getTokens(AnnotationParserParser.TAGDELIMITER); }
		public TerminalNode TAGDELIMITER(int i) {
			return getToken(AnnotationParserParser.TAGDELIMITER, i);
		}
		public WordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_word; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterWord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitWord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitWord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WordContext word() throws RecognitionException {
		WordContext _localctx = new WordContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_word);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(126);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					{
					setState(112);
					match(ESCAPECHARACTER);
					setState(113);
					match(TAGDELIMITER);
					}
					}
					break;
				case 2:
					{
					setState(114);
					match(ANNOTATIONCHARACTER);
					}
					break;
				case 3:
					{
					setState(115);
					match(ANNOTATIONOPEN);
					}
					break;
				case 4:
					{
					setState(116);
					match(ANNOTATIONCLOSE);
					}
					break;
				case 5:
					{
					{
					setState(117);
					match(ESCAPECHARACTER);
					setState(118);
					match(ESCAPECHARACTER);
					}
					}
					break;
				case 6:
					{
					setState(119);
					match(COLON);
					}
					break;
				case 7:
					{
					setState(120);
					match(COMMA);
					}
					break;
				case 8:
					{
					setState(121);
					match(OPENBRACKET);
					}
					break;
				case 9:
					{
					setState(122);
					match(CLOSEBRACKET);
					}
					break;
				case 10:
					{
					setState(123);
					match(SPACE);
					}
					break;
				case 11:
					{
					setState(124);
					match(DIGIT);
					}
					break;
				case 12:
					{
					setState(125);
					match(CHARACTER);
					}
					break;
				}
				}
				setState(128); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ANNOTATIONCHARACTER) | (1L << ANNOTATIONOPEN) | (1L << ANNOTATIONCLOSE) | (1L << ESCAPECHARACTER) | (1L << COLON) | (1L << COMMA) | (1L << OPENBRACKET) | (1L << CLOSEBRACKET) | (1L << SPACE) | (1L << DIGIT) | (1L << CHARACTER))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleStringContext extends ParserRuleContext {
		public List<TerminalNode> TAGDELIMITER() { return getTokens(AnnotationParserParser.TAGDELIMITER); }
		public TerminalNode TAGDELIMITER(int i) {
			return getToken(AnnotationParserParser.TAGDELIMITER, i);
		}
		public List<TerminalNode> ANNOTATIONOPEN() { return getTokens(AnnotationParserParser.ANNOTATIONOPEN); }
		public TerminalNode ANNOTATIONOPEN(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONOPEN, i);
		}
		public List<TerminalNode> ANNOTATIONCLOSE() { return getTokens(AnnotationParserParser.ANNOTATIONCLOSE); }
		public TerminalNode ANNOTATIONCLOSE(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONCLOSE, i);
		}
		public List<TerminalNode> COLON() { return getTokens(AnnotationParserParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(AnnotationParserParser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AnnotationParserParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AnnotationParserParser.COMMA, i);
		}
		public List<TerminalNode> OPENBRACKET() { return getTokens(AnnotationParserParser.OPENBRACKET); }
		public TerminalNode OPENBRACKET(int i) {
			return getToken(AnnotationParserParser.OPENBRACKET, i);
		}
		public List<TerminalNode> CLOSEBRACKET() { return getTokens(AnnotationParserParser.CLOSEBRACKET); }
		public TerminalNode CLOSEBRACKET(int i) {
			return getToken(AnnotationParserParser.CLOSEBRACKET, i);
		}
		public List<TerminalNode> SPACE() { return getTokens(AnnotationParserParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(AnnotationParserParser.SPACE, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(AnnotationParserParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(AnnotationParserParser.DIGIT, i);
		}
		public List<TerminalNode> CHARACTER() { return getTokens(AnnotationParserParser.CHARACTER); }
		public TerminalNode CHARACTER(int i) {
			return getToken(AnnotationParserParser.CHARACTER, i);
		}
		public List<TerminalNode> ESCAPECHARACTER() { return getTokens(AnnotationParserParser.ESCAPECHARACTER); }
		public TerminalNode ESCAPECHARACTER(int i) {
			return getToken(AnnotationParserParser.ESCAPECHARACTER, i);
		}
		public List<TerminalNode> ANNOTATIONCHARACTER() { return getTokens(AnnotationParserParser.ANNOTATIONCHARACTER); }
		public TerminalNode ANNOTATIONCHARACTER(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONCHARACTER, i);
		}
		public SimpleStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterSimpleString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitSimpleString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitSimpleString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleStringContext simpleString() throws RecognitionException {
		SimpleStringContext _localctx = new SimpleStringContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_simpleString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(144);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(130);
					match(TAGDELIMITER);
					}
					break;
				case 2:
					{
					{
					setState(131);
					match(ESCAPECHARACTER);
					setState(132);
					match(ANNOTATIONCHARACTER);
					}
					}
					break;
				case 3:
					{
					setState(133);
					match(ANNOTATIONOPEN);
					}
					break;
				case 4:
					{
					setState(134);
					match(ANNOTATIONCLOSE);
					}
					break;
				case 5:
					{
					{
					setState(135);
					match(ESCAPECHARACTER);
					setState(136);
					match(ESCAPECHARACTER);
					}
					}
					break;
				case 6:
					{
					setState(137);
					match(COLON);
					}
					break;
				case 7:
					{
					setState(138);
					match(COMMA);
					}
					break;
				case 8:
					{
					setState(139);
					match(OPENBRACKET);
					}
					break;
				case 9:
					{
					setState(140);
					match(CLOSEBRACKET);
					}
					break;
				case 10:
					{
					setState(141);
					match(SPACE);
					}
					break;
				case 11:
					{
					setState(142);
					match(DIGIT);
					}
					break;
				case 12:
					{
					setState(143);
					match(CHARACTER);
					}
					break;
				}
				}
				setState(146); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TAGDELIMITER) | (1L << ANNOTATIONOPEN) | (1L << ANNOTATIONCLOSE) | (1L << ESCAPECHARACTER) | (1L << COLON) | (1L << COMMA) | (1L << OPENBRACKET) | (1L << CLOSEBRACKET) | (1L << SPACE) | (1L << DIGIT) | (1L << CHARACTER))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChartContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(AnnotationParserParser.COLON, 0); }
		public List<TerminalNode> SPACE() { return getTokens(AnnotationParserParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(AnnotationParserParser.SPACE, i);
		}
		public List<ChartTokenContext> chartToken() {
			return getRuleContexts(ChartTokenContext.class);
		}
		public ChartTokenContext chartToken(int i) {
			return getRuleContext(ChartTokenContext.class,i);
		}
		public List<ChartTagContext> chartTag() {
			return getRuleContexts(ChartTagContext.class);
		}
		public ChartTagContext chartTag(int i) {
			return getRuleContext(ChartTagContext.class,i);
		}
		public ChartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_chart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterChart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitChart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitChart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChartContext chart() throws RecognitionException {
		ChartContext _localctx = new ChartContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_chart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(148);
				match(SPACE);
				}
				}
				setState(153);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(161); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(154);
				chartToken();
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SPACE) {
					{
					{
					setState(155);
					match(SPACE);
					}
					}
					setState(160);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(163); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==OPENBRACKET );
			setState(165);
			match(COLON);
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(166);
				match(SPACE);
				}
				}
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(179); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(172);
				chartTag();
				setState(176);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SPACE) {
					{
					{
					setState(173);
					match(SPACE);
					}
					}
					setState(178);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(181); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==OPENBRACKET );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChartTokenContext extends ParserRuleContext {
		public TerminalNode OPENBRACKET() { return getToken(AnnotationParserParser.OPENBRACKET, 0); }
		public TokenContext token() {
			return getRuleContext(TokenContext.class,0);
		}
		public TerminalNode CLOSEBRACKET() { return getToken(AnnotationParserParser.CLOSEBRACKET, 0); }
		public ChartTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_chartToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterChartToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitChartToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitChartToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChartTokenContext chartToken() throws RecognitionException {
		ChartTokenContext _localctx = new ChartTokenContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_chartToken);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(OPENBRACKET);
			setState(184);
			token();
			setState(185);
			match(CLOSEBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChartTagContext extends ParserRuleContext {
		public TerminalNode OPENBRACKET() { return getToken(AnnotationParserParser.OPENBRACKET, 0); }
		public List<NumberContext> number() {
			return getRuleContexts(NumberContext.class);
		}
		public NumberContext number(int i) {
			return getRuleContext(NumberContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AnnotationParserParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AnnotationParserParser.COMMA, i);
		}
		public List<TerminalNode> TAGDELIMITER() { return getTokens(AnnotationParserParser.TAGDELIMITER); }
		public TerminalNode TAGDELIMITER(int i) {
			return getToken(AnnotationParserParser.TAGDELIMITER, i);
		}
		public TagContext tag() {
			return getRuleContext(TagContext.class,0);
		}
		public TerminalNode CLOSEBRACKET() { return getToken(AnnotationParserParser.CLOSEBRACKET, 0); }
		public List<TerminalNode> SPACE() { return getTokens(AnnotationParserParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(AnnotationParserParser.SPACE, i);
		}
		public ChartTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_chartTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterChartTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitChartTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitChartTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChartTagContext chartTag() throws RecognitionException {
		ChartTagContext _localctx = new ChartTagContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_chartTag);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			match(OPENBRACKET);
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(188);
				match(SPACE);
				}
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(194);
			number();
			setState(198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(195);
				match(SPACE);
				}
				}
				setState(200);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(201);
			match(COMMA);
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(202);
				match(SPACE);
				}
				}
				setState(207);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(208);
			number();
			setState(212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(209);
				match(SPACE);
				}
				}
				setState(214);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(215);
			match(COMMA);
			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(216);
				match(SPACE);
				}
				}
				setState(221);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(222);
			match(TAGDELIMITER);
			setState(223);
			tag();
			setState(224);
			match(TAGDELIMITER);
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(225);
				match(SPACE);
				}
				}
				setState(230);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(231);
			match(CLOSEBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TokenContext extends ParserRuleContext {
		public List<TerminalNode> TAGDELIMITER() { return getTokens(AnnotationParserParser.TAGDELIMITER); }
		public TerminalNode TAGDELIMITER(int i) {
			return getToken(AnnotationParserParser.TAGDELIMITER, i);
		}
		public List<TerminalNode> ANNOTATIONCHARACTER() { return getTokens(AnnotationParserParser.ANNOTATIONCHARACTER); }
		public TerminalNode ANNOTATIONCHARACTER(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONCHARACTER, i);
		}
		public List<TerminalNode> ANNOTATIONOPEN() { return getTokens(AnnotationParserParser.ANNOTATIONOPEN); }
		public TerminalNode ANNOTATIONOPEN(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONOPEN, i);
		}
		public List<TerminalNode> ANNOTATIONCLOSE() { return getTokens(AnnotationParserParser.ANNOTATIONCLOSE); }
		public TerminalNode ANNOTATIONCLOSE(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONCLOSE, i);
		}
		public List<TerminalNode> COLON() { return getTokens(AnnotationParserParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(AnnotationParserParser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AnnotationParserParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AnnotationParserParser.COMMA, i);
		}
		public List<TerminalNode> OPENBRACKET() { return getTokens(AnnotationParserParser.OPENBRACKET); }
		public TerminalNode OPENBRACKET(int i) {
			return getToken(AnnotationParserParser.OPENBRACKET, i);
		}
		public List<TerminalNode> SPACE() { return getTokens(AnnotationParserParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(AnnotationParserParser.SPACE, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(AnnotationParserParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(AnnotationParserParser.DIGIT, i);
		}
		public List<TerminalNode> CHARACTER() { return getTokens(AnnotationParserParser.CHARACTER); }
		public TerminalNode CHARACTER(int i) {
			return getToken(AnnotationParserParser.CHARACTER, i);
		}
		public List<TerminalNode> ESCAPECHARACTER() { return getTokens(AnnotationParserParser.ESCAPECHARACTER); }
		public TerminalNode ESCAPECHARACTER(int i) {
			return getToken(AnnotationParserParser.ESCAPECHARACTER, i);
		}
		public List<TerminalNode> CLOSEBRACKET() { return getTokens(AnnotationParserParser.CLOSEBRACKET); }
		public TerminalNode CLOSEBRACKET(int i) {
			return getToken(AnnotationParserParser.CLOSEBRACKET, i);
		}
		public TokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_token; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TokenContext token() throws RecognitionException {
		TokenContext _localctx = new TokenContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_token);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(247);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(233);
					match(TAGDELIMITER);
					}
					break;
				case 2:
					{
					setState(234);
					match(ANNOTATIONCHARACTER);
					}
					break;
				case 3:
					{
					setState(235);
					match(ANNOTATIONOPEN);
					}
					break;
				case 4:
					{
					setState(236);
					match(ANNOTATIONCLOSE);
					}
					break;
				case 5:
					{
					{
					setState(237);
					match(ESCAPECHARACTER);
					setState(238);
					match(ESCAPECHARACTER);
					}
					}
					break;
				case 6:
					{
					setState(239);
					match(COLON);
					}
					break;
				case 7:
					{
					setState(240);
					match(COMMA);
					}
					break;
				case 8:
					{
					setState(241);
					match(OPENBRACKET);
					}
					break;
				case 9:
					{
					{
					setState(242);
					match(ESCAPECHARACTER);
					setState(243);
					match(CLOSEBRACKET);
					}
					}
					break;
				case 10:
					{
					setState(244);
					match(SPACE);
					}
					break;
				case 11:
					{
					setState(245);
					match(DIGIT);
					}
					break;
				case 12:
					{
					setState(246);
					match(CHARACTER);
					}
					break;
				}
				}
				setState(249); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TAGDELIMITER) | (1L << ANNOTATIONCHARACTER) | (1L << ANNOTATIONOPEN) | (1L << ANNOTATIONCLOSE) | (1L << ESCAPECHARACTER) | (1L << COLON) | (1L << COMMA) | (1L << OPENBRACKET) | (1L << SPACE) | (1L << DIGIT) | (1L << CHARACTER))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TagContext extends ParserRuleContext {
		public List<TerminalNode> ANNOTATIONCHARACTER() { return getTokens(AnnotationParserParser.ANNOTATIONCHARACTER); }
		public TerminalNode ANNOTATIONCHARACTER(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONCHARACTER, i);
		}
		public List<TerminalNode> ANNOTATIONOPEN() { return getTokens(AnnotationParserParser.ANNOTATIONOPEN); }
		public TerminalNode ANNOTATIONOPEN(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONOPEN, i);
		}
		public List<TerminalNode> ANNOTATIONCLOSE() { return getTokens(AnnotationParserParser.ANNOTATIONCLOSE); }
		public TerminalNode ANNOTATIONCLOSE(int i) {
			return getToken(AnnotationParserParser.ANNOTATIONCLOSE, i);
		}
		public List<TerminalNode> COLON() { return getTokens(AnnotationParserParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(AnnotationParserParser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(AnnotationParserParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(AnnotationParserParser.COMMA, i);
		}
		public List<TerminalNode> OPENBRACKET() { return getTokens(AnnotationParserParser.OPENBRACKET); }
		public TerminalNode OPENBRACKET(int i) {
			return getToken(AnnotationParserParser.OPENBRACKET, i);
		}
		public List<TerminalNode> CLOSEBRACKET() { return getTokens(AnnotationParserParser.CLOSEBRACKET); }
		public TerminalNode CLOSEBRACKET(int i) {
			return getToken(AnnotationParserParser.CLOSEBRACKET, i);
		}
		public List<TerminalNode> SPACE() { return getTokens(AnnotationParserParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(AnnotationParserParser.SPACE, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(AnnotationParserParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(AnnotationParserParser.DIGIT, i);
		}
		public List<TerminalNode> CHARACTER() { return getTokens(AnnotationParserParser.CHARACTER); }
		public TerminalNode CHARACTER(int i) {
			return getToken(AnnotationParserParser.CHARACTER, i);
		}
		public List<TerminalNode> ESCAPECHARACTER() { return getTokens(AnnotationParserParser.ESCAPECHARACTER); }
		public TerminalNode ESCAPECHARACTER(int i) {
			return getToken(AnnotationParserParser.ESCAPECHARACTER, i);
		}
		public List<TerminalNode> TAGDELIMITER() { return getTokens(AnnotationParserParser.TAGDELIMITER); }
		public TerminalNode TAGDELIMITER(int i) {
			return getToken(AnnotationParserParser.TAGDELIMITER, i);
		}
		public TagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagContext tag() throws RecognitionException {
		TagContext _localctx = new TagContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_tag);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(265);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
				case 1:
					{
					{
					setState(251);
					match(ESCAPECHARACTER);
					setState(252);
					match(TAGDELIMITER);
					}
					}
					break;
				case 2:
					{
					setState(253);
					match(ANNOTATIONCHARACTER);
					}
					break;
				case 3:
					{
					setState(254);
					match(ANNOTATIONOPEN);
					}
					break;
				case 4:
					{
					setState(255);
					match(ANNOTATIONCLOSE);
					}
					break;
				case 5:
					{
					{
					setState(256);
					match(ESCAPECHARACTER);
					setState(257);
					match(ESCAPECHARACTER);
					}
					}
					break;
				case 6:
					{
					setState(258);
					match(COLON);
					}
					break;
				case 7:
					{
					setState(259);
					match(COMMA);
					}
					break;
				case 8:
					{
					setState(260);
					match(OPENBRACKET);
					}
					break;
				case 9:
					{
					setState(261);
					match(CLOSEBRACKET);
					}
					break;
				case 10:
					{
					setState(262);
					match(SPACE);
					}
					break;
				case 11:
					{
					setState(263);
					match(DIGIT);
					}
					break;
				case 12:
					{
					setState(264);
					match(CHARACTER);
					}
					break;
				}
				}
				setState(267); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ANNOTATIONCHARACTER) | (1L << ANNOTATIONOPEN) | (1L << ANNOTATIONCLOSE) | (1L << ESCAPECHARACTER) | (1L << COLON) | (1L << COMMA) | (1L << OPENBRACKET) | (1L << CLOSEBRACKET) | (1L << SPACE) | (1L << DIGIT) | (1L << CHARACTER))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberContext extends ParserRuleContext {
		public List<TerminalNode> DIGIT() { return getTokens(AnnotationParserParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(AnnotationParserParser.DIGIT, i);
		}
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AnnotationParserListener ) ((AnnotationParserListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AnnotationParserVisitor ) return ((AnnotationParserVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(269);
				match(DIGIT);
				}
				}
				setState(272); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==DIGIT );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\20\u0115\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\60\n\3\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4>\n\4\3\5\3\5\5\5B\n\5\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\7\bS\n\b\f\b\16\bV\13"+
		"\b\3\b\3\b\3\b\3\b\7\b\\\n\b\f\b\16\b_\13\b\3\b\3\b\7\bc\n\b\f\b\16\b"+
		"f\13\b\3\b\3\b\3\b\3\b\7\bl\n\b\f\b\16\bo\13\b\3\b\3\b\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\6\t\u0081\n\t\r\t\16\t\u0082"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\6\n\u0093\n\n"+
		"\r\n\16\n\u0094\3\13\7\13\u0098\n\13\f\13\16\13\u009b\13\13\3\13\3\13"+
		"\7\13\u009f\n\13\f\13\16\13\u00a2\13\13\6\13\u00a4\n\13\r\13\16\13\u00a5"+
		"\3\13\3\13\7\13\u00aa\n\13\f\13\16\13\u00ad\13\13\3\13\3\13\7\13\u00b1"+
		"\n\13\f\13\16\13\u00b4\13\13\6\13\u00b6\n\13\r\13\16\13\u00b7\3\f\3\f"+
		"\3\f\3\f\3\r\3\r\7\r\u00c0\n\r\f\r\16\r\u00c3\13\r\3\r\3\r\7\r\u00c7\n"+
		"\r\f\r\16\r\u00ca\13\r\3\r\3\r\7\r\u00ce\n\r\f\r\16\r\u00d1\13\r\3\r\3"+
		"\r\7\r\u00d5\n\r\f\r\16\r\u00d8\13\r\3\r\3\r\7\r\u00dc\n\r\f\r\16\r\u00df"+
		"\13\r\3\r\3\r\3\r\3\r\7\r\u00e5\n\r\f\r\16\r\u00e8\13\r\3\r\3\r\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\6\16\u00fa"+
		"\n\16\r\16\16\16\u00fb\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\6\17\u010c\n\17\r\17\16\17\u010d\3\20\6\20\u0111"+
		"\n\20\r\20\16\20\u0112\3\20\2\2\21\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36\2\2\2\u014f\2 \3\2\2\2\4/\3\2\2\2\6=\3\2\2\2\bA\3\2\2\2\nC\3\2\2\2"+
		"\fI\3\2\2\2\16O\3\2\2\2\20\u0080\3\2\2\2\22\u0092\3\2\2\2\24\u0099\3\2"+
		"\2\2\26\u00b9\3\2\2\2\30\u00bd\3\2\2\2\32\u00f9\3\2\2\2\34\u010b\3\2\2"+
		"\2\36\u0110\3\2\2\2 !\5\4\3\2!\"\7\2\2\3\"\3\3\2\2\2#\60\5\6\4\2$\60\5"+
		"\b\5\2%&\5\b\5\2&\'\5\4\3\2\'\60\3\2\2\2()\5\6\4\2)*\5\b\5\2*\60\3\2\2"+
		"\2+,\5\6\4\2,-\5\b\5\2-.\5\4\3\2.\60\3\2\2\2/#\3\2\2\2/$\3\2\2\2/%\3\2"+
		"\2\2/(\3\2\2\2/+\3\2\2\2\60\5\3\2\2\2\61>\5\22\n\2\62>\5\16\b\2\63\64"+
		"\5\16\b\2\64\65\5\6\4\2\65>\3\2\2\2\66\67\5\22\n\2\678\5\16\b\28>\3\2"+
		"\2\29:\5\22\n\2:;\5\16\b\2;<\5\6\4\2<>\3\2\2\2=\61\3\2\2\2=\62\3\2\2\2"+
		"=\63\3\2\2\2=\66\3\2\2\2=9\3\2\2\2>\7\3\2\2\2?B\5\n\6\2@B\5\f\7\2A?\3"+
		"\2\2\2A@\3\2\2\2B\t\3\2\2\2CD\7\6\2\2DE\7\7\2\2EF\7\3\2\2FG\5\24\13\2"+
		"GH\7\b\2\2H\13\3\2\2\2IJ\7\6\2\2JK\7\7\2\2KL\7\4\2\2LM\5\24\13\2MN\7\b"+
		"\2\2N\r\3\2\2\2OP\7\6\2\2PT\7\7\2\2QS\7\16\2\2RQ\3\2\2\2SV\3\2\2\2TR\3"+
		"\2\2\2TU\3\2\2\2UW\3\2\2\2VT\3\2\2\2WX\7\5\2\2XY\5\20\t\2Y]\7\5\2\2Z\\"+
		"\7\16\2\2[Z\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^`\3\2\2\2_]\3\2\2\2"+
		"`d\7\13\2\2ac\7\16\2\2ba\3\2\2\2cf\3\2\2\2db\3\2\2\2de\3\2\2\2eg\3\2\2"+
		"\2fd\3\2\2\2gh\7\5\2\2hi\5\34\17\2im\7\5\2\2jl\7\16\2\2kj\3\2\2\2lo\3"+
		"\2\2\2mk\3\2\2\2mn\3\2\2\2np\3\2\2\2om\3\2\2\2pq\7\b\2\2q\17\3\2\2\2r"+
		"s\7\t\2\2s\u0081\7\5\2\2t\u0081\7\6\2\2u\u0081\7\7\2\2v\u0081\7\b\2\2"+
		"wx\7\t\2\2x\u0081\7\t\2\2y\u0081\7\n\2\2z\u0081\7\13\2\2{\u0081\7\f\2"+
		"\2|\u0081\7\r\2\2}\u0081\7\16\2\2~\u0081\7\17\2\2\177\u0081\7\20\2\2\u0080"+
		"r\3\2\2\2\u0080t\3\2\2\2\u0080u\3\2\2\2\u0080v\3\2\2\2\u0080w\3\2\2\2"+
		"\u0080y\3\2\2\2\u0080z\3\2\2\2\u0080{\3\2\2\2\u0080|\3\2\2\2\u0080}\3"+
		"\2\2\2\u0080~\3\2\2\2\u0080\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0080"+
		"\3\2\2\2\u0082\u0083\3\2\2\2\u0083\21\3\2\2\2\u0084\u0093\7\5\2\2\u0085"+
		"\u0086\7\t\2\2\u0086\u0093\7\6\2\2\u0087\u0093\7\7\2\2\u0088\u0093\7\b"+
		"\2\2\u0089\u008a\7\t\2\2\u008a\u0093\7\t\2\2\u008b\u0093\7\n\2\2\u008c"+
		"\u0093\7\13\2\2\u008d\u0093\7\f\2\2\u008e\u0093\7\r\2\2\u008f\u0093\7"+
		"\16\2\2\u0090\u0093\7\17\2\2\u0091\u0093\7\20\2\2\u0092\u0084\3\2\2\2"+
		"\u0092\u0085\3\2\2\2\u0092\u0087\3\2\2\2\u0092\u0088\3\2\2\2\u0092\u0089"+
		"\3\2\2\2\u0092\u008b\3\2\2\2\u0092\u008c\3\2\2\2\u0092\u008d\3\2\2\2\u0092"+
		"\u008e\3\2\2\2\u0092\u008f\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0091\3\2"+
		"\2\2\u0093\u0094\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095"+
		"\23\3\2\2\2\u0096\u0098\7\16\2\2\u0097\u0096\3\2\2\2\u0098\u009b\3\2\2"+
		"\2\u0099\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u00a3\3\2\2\2\u009b\u0099"+
		"\3\2\2\2\u009c\u00a0\5\26\f\2\u009d\u009f\7\16\2\2\u009e\u009d\3\2\2\2"+
		"\u009f\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a4"+
		"\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u009c\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5"+
		"\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00ab\7\n"+
		"\2\2\u00a8\u00aa\7\16\2\2\u00a9\u00a8\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab"+
		"\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00b5\3\2\2\2\u00ad\u00ab\3\2"+
		"\2\2\u00ae\u00b2\5\30\r\2\u00af\u00b1\7\16\2\2\u00b0\u00af\3\2\2\2\u00b1"+
		"\u00b4\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b6\3\2"+
		"\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00ae\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7"+
		"\u00b5\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\25\3\2\2\2\u00b9\u00ba\7\f\2"+
		"\2\u00ba\u00bb\5\32\16\2\u00bb\u00bc\7\r\2\2\u00bc\27\3\2\2\2\u00bd\u00c1"+
		"\7\f\2\2\u00be\u00c0\7\16\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c3\3\2\2\2"+
		"\u00c1\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c4\3\2\2\2\u00c3\u00c1"+
		"\3\2\2\2\u00c4\u00c8\5\36\20\2\u00c5\u00c7\7\16\2\2\u00c6\u00c5\3\2\2"+
		"\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00cb"+
		"\3\2\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00cf\7\13\2\2\u00cc\u00ce\7\16\2\2"+
		"\u00cd\u00cc\3\2\2\2\u00ce\u00d1\3\2\2\2\u00cf\u00cd\3\2\2\2\u00cf\u00d0"+
		"\3\2\2\2\u00d0\u00d2\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d2\u00d6\5\36\20\2"+
		"\u00d3\u00d5\7\16\2\2\u00d4\u00d3\3\2\2\2\u00d5\u00d8\3\2\2\2\u00d6\u00d4"+
		"\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d9\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d9"+
		"\u00dd\7\13\2\2\u00da\u00dc\7\16\2\2\u00db\u00da\3\2\2\2\u00dc\u00df\3"+
		"\2\2\2\u00dd\u00db\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00e0\3\2\2\2\u00df"+
		"\u00dd\3\2\2\2\u00e0\u00e1\7\5\2\2\u00e1\u00e2\5\34\17\2\u00e2\u00e6\7"+
		"\5\2\2\u00e3\u00e5\7\16\2\2\u00e4\u00e3\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6"+
		"\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\u00e9\3\2\2\2\u00e8\u00e6\3\2"+
		"\2\2\u00e9\u00ea\7\r\2\2\u00ea\31\3\2\2\2\u00eb\u00fa\7\5\2\2\u00ec\u00fa"+
		"\7\6\2\2\u00ed\u00fa\7\7\2\2\u00ee\u00fa\7\b\2\2\u00ef\u00f0\7\t\2\2\u00f0"+
		"\u00fa\7\t\2\2\u00f1\u00fa\7\n\2\2\u00f2\u00fa\7\13\2\2\u00f3\u00fa\7"+
		"\f\2\2\u00f4\u00f5\7\t\2\2\u00f5\u00fa\7\r\2\2\u00f6\u00fa\7\16\2\2\u00f7"+
		"\u00fa\7\17\2\2\u00f8\u00fa\7\20\2\2\u00f9\u00eb\3\2\2\2\u00f9\u00ec\3"+
		"\2\2\2\u00f9\u00ed\3\2\2\2\u00f9\u00ee\3\2\2\2\u00f9\u00ef\3\2\2\2\u00f9"+
		"\u00f1\3\2\2\2\u00f9\u00f2\3\2\2\2\u00f9\u00f3\3\2\2\2\u00f9\u00f4\3\2"+
		"\2\2\u00f9\u00f6\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00f8\3\2\2\2\u00fa"+
		"\u00fb\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\33\3\2\2"+
		"\2\u00fd\u00fe\7\t\2\2\u00fe\u010c\7\5\2\2\u00ff\u010c\7\6\2\2\u0100\u010c"+
		"\7\7\2\2\u0101\u010c\7\b\2\2\u0102\u0103\7\t\2\2\u0103\u010c\7\t\2\2\u0104"+
		"\u010c\7\n\2\2\u0105\u010c\7\13\2\2\u0106\u010c\7\f\2\2\u0107\u010c\7"+
		"\r\2\2\u0108\u010c\7\16\2\2\u0109\u010c\7\17\2\2\u010a\u010c\7\20\2\2"+
		"\u010b\u00fd\3\2\2\2\u010b\u00ff\3\2\2\2\u010b\u0100\3\2\2\2\u010b\u0101"+
		"\3\2\2\2\u010b\u0102\3\2\2\2\u010b\u0104\3\2\2\2\u010b\u0105\3\2\2\2\u010b"+
		"\u0106\3\2\2\2\u010b\u0107\3\2\2\2\u010b\u0108\3\2\2\2\u010b\u0109\3\2"+
		"\2\2\u010b\u010a\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010b\3\2\2\2\u010d"+
		"\u010e\3\2\2\2\u010e\35\3\2\2\2\u010f\u0111\7\17\2\2\u0110\u010f\3\2\2"+
		"\2\u0111\u0112\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113\37"+
		"\3\2\2\2\36/=AT]dm\u0080\u0082\u0092\u0094\u0099\u00a0\u00a5\u00ab\u00b2"+
		"\u00b7\u00c1\u00c8\u00cf\u00d6\u00dd\u00e6\u00f9\u00fb\u010b\u010d\u0112";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}