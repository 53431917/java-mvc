// Generated from CouchDB.g4 by ANTLR 4.7
package org.hyperledger.fabric.chaincode.query.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CouchDBParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, STRING=3, SELECT=4, QUERY=5, DESCRIPTION=6, WHERE=7, BY=8, 
		ORDER=9, AND=10, OR=11, ASC=12, DESC=13, LIMIT=14, BOOKMARK=15, CONTAINS=16, 
		TRUE=17, FALSE=18, FROM=19, IN=20, EQ=21, GTE=22, GT=23, LT=24, LTE=25, 
		NE=26, LEFT_PAREN=27, RIGHT_PAREN=28, Identifier=29, DECIMAL_VALUE=30, 
		INTEGER_VALUE=31, SIMPLE_COMMENT=32, BRACKETED_EMPTY_COMMENT=33, BRACKETED_COMMENT=34, 
		WS=35;
	public static final int
		RULE_logicalOperator = 0, RULE_comparisonOperator = 1, RULE_propName = 2, 
		RULE_descriptionClause = 3, RULE_queries = 4, RULE_qualifiedName = 5, 
		RULE_queryClause = 6, RULE_selectClause = 7, RULE_selectFields = 8, RULE_fromClause = 9, 
		RULE_whereClause = 10, RULE_expression = 11, RULE_value = 12, RULE_orderByClause = 13, 
		RULE_orderByExpression = 14, RULE_limitClause = 15, RULE_bookmarkClause = 16;
	public static final String[] ruleNames = {
		"logicalOperator", "comparisonOperator", "propName", "descriptionClause", 
		"queries", "qualifiedName", "queryClause", "selectClause", "selectFields", 
		"fromClause", "whereClause", "expression", "value", "orderByClause", "orderByExpression", 
		"limitClause", "bookmarkClause"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'.'", "','", null, "'SELECT'", "'query'", "'description'", "'WHERE'", 
		"'BY'", "'ORDER'", "'AND'", "'OR'", "'ASC'", "'DESC'", "'LIMIT'", "'BOOKMARK'", 
		"'CONTAINS'", "'TRUE'", "'FALSE'", "'FROM'", "'IN'", "'=='", "'>='", "'>'", 
		"'<'", "'<='", "'!='", "'('", "')'", null, null, null, null, "'/**/'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "STRING", "SELECT", "QUERY", "DESCRIPTION", "WHERE", 
		"BY", "ORDER", "AND", "OR", "ASC", "DESC", "LIMIT", "BOOKMARK", "CONTAINS", 
		"TRUE", "FALSE", "FROM", "IN", "EQ", "GTE", "GT", "LT", "LTE", "NE", "LEFT_PAREN", 
		"RIGHT_PAREN", "Identifier", "DECIMAL_VALUE", "INTEGER_VALUE", "SIMPLE_COMMENT", 
		"BRACKETED_EMPTY_COMMENT", "BRACKETED_COMMENT", "WS"
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
	public String getGrammarFileName() { return "CouchDB.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CouchDBParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class LogicalOperatorContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(CouchDBParser.AND, 0); }
		public TerminalNode OR() { return getToken(CouchDBParser.OR, 0); }
		public LogicalOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterLogicalOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitLogicalOperator(this);
		}
	}

	public final LogicalOperatorContext logicalOperator() throws RecognitionException {
		LogicalOperatorContext _localctx = new LogicalOperatorContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_logicalOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			_la = _input.LA(1);
			if ( !(_la==AND || _la==OR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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

	public static class ComparisonOperatorContext extends ParserRuleContext {
		public TerminalNode EQ() { return getToken(CouchDBParser.EQ, 0); }
		public TerminalNode GTE() { return getToken(CouchDBParser.GTE, 0); }
		public TerminalNode GT() { return getToken(CouchDBParser.GT, 0); }
		public TerminalNode LT() { return getToken(CouchDBParser.LT, 0); }
		public TerminalNode LTE() { return getToken(CouchDBParser.LTE, 0); }
		public TerminalNode NE() { return getToken(CouchDBParser.NE, 0); }
		public ComparisonOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterComparisonOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitComparisonOperator(this);
		}
	}

	public final ComparisonOperatorContext comparisonOperator() throws RecognitionException {
		ComparisonOperatorContext _localctx = new ComparisonOperatorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_comparisonOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(36);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQ) | (1L << GTE) | (1L << GT) | (1L << LT) | (1L << LTE) | (1L << NE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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

	public static class PropNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CouchDBParser.Identifier, 0); }
		public PropNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterPropName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitPropName(this);
		}
	}

	public final PropNameContext propName() throws RecognitionException {
		PropNameContext _localctx = new PropNameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_propName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			match(Identifier);
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

	public static class DescriptionClauseContext extends ParserRuleContext {
		public TerminalNode DESCRIPTION() { return getToken(CouchDBParser.DESCRIPTION, 0); }
		public TerminalNode STRING() { return getToken(CouchDBParser.STRING, 0); }
		public DescriptionClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterDescriptionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitDescriptionClause(this);
		}
	}

	public final DescriptionClauseContext descriptionClause() throws RecognitionException {
		DescriptionClauseContext _localctx = new DescriptionClauseContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_descriptionClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(DESCRIPTION);
			setState(41);
			match(STRING);
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

	public static class QueriesContext extends ParserRuleContext {
		public QueryClauseContext queryClause() {
			return getRuleContext(QueryClauseContext.class,0);
		}
		public FromClauseContext fromClause() {
			return getRuleContext(FromClauseContext.class,0);
		}
		public DescriptionClauseContext descriptionClause() {
			return getRuleContext(DescriptionClauseContext.class,0);
		}
		public SelectClauseContext selectClause() {
			return getRuleContext(SelectClauseContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public OrderByClauseContext orderByClause() {
			return getRuleContext(OrderByClauseContext.class,0);
		}
		public LimitClauseContext limitClause() {
			return getRuleContext(LimitClauseContext.class,0);
		}
		public BookmarkClauseContext bookmarkClause() {
			return getRuleContext(BookmarkClauseContext.class,0);
		}
		public QueriesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queries; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterQueries(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitQueries(this);
		}
	}

	public final QueriesContext queries() throws RecognitionException {
		QueriesContext _localctx = new QueriesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_queries);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			queryClause();
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESCRIPTION) {
				{
				setState(44);
				descriptionClause();
				}
			}

			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SELECT) {
				{
				setState(47);
				selectClause();
				}
			}

			setState(50);
			fromClause();
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(51);
				whereClause();
				}
			}

			setState(55);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(54);
				orderByClause();
				}
			}

			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(57);
				limitClause();
				}
			}

			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==BOOKMARK) {
				{
				setState(60);
				bookmarkClause();
				}
			}

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

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(CouchDBParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(CouchDBParser.Identifier, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitQualifiedName(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(Identifier);
			setState(68);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(64);
				match(T__0);
				setState(65);
				match(Identifier);
				}
				}
				setState(70);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class QueryClauseContext extends ParserRuleContext {
		public TerminalNode QUERY() { return getToken(CouchDBParser.QUERY, 0); }
		public TerminalNode Identifier() { return getToken(CouchDBParser.Identifier, 0); }
		public QueryClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterQueryClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitQueryClause(this);
		}
	}

	public final QueryClauseContext queryClause() throws RecognitionException {
		QueryClauseContext _localctx = new QueryClauseContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_queryClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(QUERY);
			setState(72);
			match(Identifier);
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

	public static class SelectClauseContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(CouchDBParser.SELECT, 0); }
		public SelectFieldsContext selectFields() {
			return getRuleContext(SelectFieldsContext.class,0);
		}
		public SelectClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterSelectClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitSelectClause(this);
		}
	}

	public final SelectClauseContext selectClause() throws RecognitionException {
		SelectClauseContext _localctx = new SelectClauseContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_selectClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(SELECT);
			setState(75);
			selectFields();
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

	public static class SelectFieldsContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(CouchDBParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(CouchDBParser.Identifier, i);
		}
		public SelectFieldsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectFields; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterSelectFields(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitSelectFields(this);
		}
	}

	public final SelectFieldsContext selectFields() throws RecognitionException {
		SelectFieldsContext _localctx = new SelectFieldsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_selectFields);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			match(Identifier);
			setState(82);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(78);
				match(T__1);
				setState(79);
				match(Identifier);
				}
				}
				setState(84);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class FromClauseContext extends ParserRuleContext {
		public TerminalNode FROM() { return getToken(CouchDBParser.FROM, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public FromClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterFromClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitFromClause(this);
		}
	}

	public final FromClauseContext fromClause() throws RecognitionException {
		FromClauseContext _localctx = new FromClauseContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_fromClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(FROM);
			setState(86);
			qualifiedName();
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

	public static class WhereClauseContext extends ParserRuleContext {
		public ExpressionContext whereExpr;
		public TerminalNode WHERE() { return getToken(CouchDBParser.WHERE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitWhereClause(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(WHERE);
			setState(89);
			((WhereClauseContext)_localctx).whereExpr = expression(0);
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

	public static class ExpressionContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(CouchDBParser.LEFT_PAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(CouchDBParser.RIGHT_PAREN, 0); }
		public PropNameContext propName() {
			return getRuleContext(PropNameContext.class,0);
		}
		public TerminalNode CONTAINS() { return getToken(CouchDBParser.CONTAINS, 0); }
		public TerminalNode IN() { return getToken(CouchDBParser.IN, 0); }
		public LogicalOperatorContext logicalOperator() {
			return getRuleContext(LogicalOperatorContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(92);
				qualifiedName();
				setState(93);
				comparisonOperator();
				setState(94);
				value();
				}
				break;
			case 2:
				{
				setState(96);
				match(LEFT_PAREN);
				setState(97);
				expression(0);
				setState(98);
				match(RIGHT_PAREN);
				}
				break;
			case 3:
				{
				setState(100);
				propName();
				setState(101);
				match(CONTAINS);
				setState(102);
				match(LEFT_PAREN);
				setState(103);
				expression(0);
				setState(104);
				match(RIGHT_PAREN);
				}
				break;
			case 4:
				{
				setState(106);
				qualifiedName();
				setState(107);
				match(IN);
				setState(108);
				value();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(118);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExpressionContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expression);
					setState(112);
					if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
					setState(113);
					logicalOperator();
					setState(114);
					expression(6);
					}
					} 
				}
				setState(120);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(CouchDBParser.STRING, 0); }
		public TerminalNode TRUE() { return getToken(CouchDBParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(CouchDBParser.FALSE, 0); }
		public TerminalNode DECIMAL_VALUE() { return getToken(CouchDBParser.DECIMAL_VALUE, 0); }
		public TerminalNode INTEGER_VALUE() { return getToken(CouchDBParser.INTEGER_VALUE, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << TRUE) | (1L << FALSE) | (1L << DECIMAL_VALUE) | (1L << INTEGER_VALUE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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

	public static class OrderByClauseContext extends ParserRuleContext {
		public TerminalNode ORDER() { return getToken(CouchDBParser.ORDER, 0); }
		public TerminalNode BY() { return getToken(CouchDBParser.BY, 0); }
		public List<OrderByExpressionContext> orderByExpression() {
			return getRuleContexts(OrderByExpressionContext.class);
		}
		public OrderByExpressionContext orderByExpression(int i) {
			return getRuleContext(OrderByExpressionContext.class,i);
		}
		public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterOrderByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitOrderByClause(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(ORDER);
			setState(124);
			match(BY);
			setState(125);
			orderByExpression();
			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(126);
				match(T__1);
				setState(127);
				orderByExpression();
				}
				}
				setState(132);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class OrderByExpressionContext extends ParserRuleContext {
		public PropNameContext propName() {
			return getRuleContext(PropNameContext.class,0);
		}
		public TerminalNode STRING() { return getToken(CouchDBParser.STRING, 0); }
		public TerminalNode ASC() { return getToken(CouchDBParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(CouchDBParser.DESC, 0); }
		public OrderByExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterOrderByExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitOrderByExpression(this);
		}
	}

	public final OrderByExpressionContext orderByExpression() throws RecognitionException {
		OrderByExpressionContext _localctx = new OrderByExpressionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_orderByExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			propName();
			setState(138);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EOF:
			case T__1:
			case ASC:
			case DESC:
			case LIMIT:
			case BOOKMARK:
				{
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ASC || _la==DESC) {
					{
					setState(134);
					_la = _input.LA(1);
					if ( !(_la==ASC || _la==DESC) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				}
				break;
			case STRING:
				{
				setState(137);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
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

	public static class LimitClauseContext extends ParserRuleContext {
		public TerminalNode LIMIT() { return getToken(CouchDBParser.LIMIT, 0); }
		public TerminalNode STRING() { return getToken(CouchDBParser.STRING, 0); }
		public TerminalNode INTEGER_VALUE() { return getToken(CouchDBParser.INTEGER_VALUE, 0); }
		public LimitClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limitClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterLimitClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitLimitClause(this);
		}
	}

	public final LimitClauseContext limitClause() throws RecognitionException {
		LimitClauseContext _localctx = new LimitClauseContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_limitClause);
		try {
			setState(144);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(140);
				match(LIMIT);
				setState(141);
				match(STRING);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(142);
				match(LIMIT);
				setState(143);
				match(INTEGER_VALUE);
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

	public static class BookmarkClauseContext extends ParserRuleContext {
		public TerminalNode BOOKMARK() { return getToken(CouchDBParser.BOOKMARK, 0); }
		public TerminalNode STRING() { return getToken(CouchDBParser.STRING, 0); }
		public BookmarkClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bookmarkClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).enterBookmarkClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CouchDBListener ) ((CouchDBListener)listener).exitBookmarkClause(this);
		}
	}

	public final BookmarkClauseContext bookmarkClause() throws RecognitionException {
		BookmarkClauseContext _localctx = new BookmarkClauseContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_bookmarkClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(BOOKMARK);
			setState(147);
			match(STRING);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 11:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 5);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3%\u0098\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\5\6\60\n\6\3\6\5\6\63\n\6"+
		"\3\6\3\6\5\6\67\n\6\3\6\5\6:\n\6\3\6\5\6=\n\6\3\6\5\6@\n\6\3\7\3\7\3\7"+
		"\7\7E\n\7\f\7\16\7H\13\7\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\7\nS\n\n"+
		"\f\n\16\nV\13\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\rq\n\r\3\r\3\r\3\r"+
		"\3\r\7\rw\n\r\f\r\16\rz\13\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\7\17\u0083"+
		"\n\17\f\17\16\17\u0086\13\17\3\20\3\20\5\20\u008a\n\20\3\20\5\20\u008d"+
		"\n\20\3\21\3\21\3\21\3\21\5\21\u0093\n\21\3\22\3\22\3\22\3\22\2\3\30\23"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"\2\6\3\2\f\r\3\2\27\34\5\2\5"+
		"\5\23\24 !\3\2\16\17\2\u0096\2$\3\2\2\2\4&\3\2\2\2\6(\3\2\2\2\b*\3\2\2"+
		"\2\n-\3\2\2\2\fA\3\2\2\2\16I\3\2\2\2\20L\3\2\2\2\22O\3\2\2\2\24W\3\2\2"+
		"\2\26Z\3\2\2\2\30p\3\2\2\2\32{\3\2\2\2\34}\3\2\2\2\36\u0087\3\2\2\2 \u0092"+
		"\3\2\2\2\"\u0094\3\2\2\2$%\t\2\2\2%\3\3\2\2\2&\'\t\3\2\2\'\5\3\2\2\2("+
		")\7\37\2\2)\7\3\2\2\2*+\7\b\2\2+,\7\5\2\2,\t\3\2\2\2-/\5\16\b\2.\60\5"+
		"\b\5\2/.\3\2\2\2/\60\3\2\2\2\60\62\3\2\2\2\61\63\5\20\t\2\62\61\3\2\2"+
		"\2\62\63\3\2\2\2\63\64\3\2\2\2\64\66\5\24\13\2\65\67\5\26\f\2\66\65\3"+
		"\2\2\2\66\67\3\2\2\2\679\3\2\2\28:\5\34\17\298\3\2\2\29:\3\2\2\2:<\3\2"+
		"\2\2;=\5 \21\2<;\3\2\2\2<=\3\2\2\2=?\3\2\2\2>@\5\"\22\2?>\3\2\2\2?@\3"+
		"\2\2\2@\13\3\2\2\2AF\7\37\2\2BC\7\3\2\2CE\7\37\2\2DB\3\2\2\2EH\3\2\2\2"+
		"FD\3\2\2\2FG\3\2\2\2G\r\3\2\2\2HF\3\2\2\2IJ\7\7\2\2JK\7\37\2\2K\17\3\2"+
		"\2\2LM\7\6\2\2MN\5\22\n\2N\21\3\2\2\2OT\7\37\2\2PQ\7\4\2\2QS\7\37\2\2"+
		"RP\3\2\2\2SV\3\2\2\2TR\3\2\2\2TU\3\2\2\2U\23\3\2\2\2VT\3\2\2\2WX\7\25"+
		"\2\2XY\5\f\7\2Y\25\3\2\2\2Z[\7\t\2\2[\\\5\30\r\2\\\27\3\2\2\2]^\b\r\1"+
		"\2^_\5\f\7\2_`\5\4\3\2`a\5\32\16\2aq\3\2\2\2bc\7\35\2\2cd\5\30\r\2de\7"+
		"\36\2\2eq\3\2\2\2fg\5\6\4\2gh\7\22\2\2hi\7\35\2\2ij\5\30\r\2jk\7\36\2"+
		"\2kq\3\2\2\2lm\5\f\7\2mn\7\26\2\2no\5\32\16\2oq\3\2\2\2p]\3\2\2\2pb\3"+
		"\2\2\2pf\3\2\2\2pl\3\2\2\2qx\3\2\2\2rs\f\7\2\2st\5\2\2\2tu\5\30\r\buw"+
		"\3\2\2\2vr\3\2\2\2wz\3\2\2\2xv\3\2\2\2xy\3\2\2\2y\31\3\2\2\2zx\3\2\2\2"+
		"{|\t\4\2\2|\33\3\2\2\2}~\7\13\2\2~\177\7\n\2\2\177\u0084\5\36\20\2\u0080"+
		"\u0081\7\4\2\2\u0081\u0083\5\36\20\2\u0082\u0080\3\2\2\2\u0083\u0086\3"+
		"\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\35\3\2\2\2\u0086"+
		"\u0084\3\2\2\2\u0087\u008c\5\6\4\2\u0088\u008a\t\5\2\2\u0089\u0088\3\2"+
		"\2\2\u0089\u008a\3\2\2\2\u008a\u008d\3\2\2\2\u008b\u008d\7\5\2\2\u008c"+
		"\u0089\3\2\2\2\u008c\u008b\3\2\2\2\u008d\37\3\2\2\2\u008e\u008f\7\20\2"+
		"\2\u008f\u0093\7\5\2\2\u0090\u0091\7\20\2\2\u0091\u0093\7!\2\2\u0092\u008e"+
		"\3\2\2\2\u0092\u0090\3\2\2\2\u0093!\3\2\2\2\u0094\u0095\7\21\2\2\u0095"+
		"\u0096\7\5\2\2\u0096#\3\2\2\2\20/\62\669<?FTpx\u0084\u0089\u008c\u0092";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}