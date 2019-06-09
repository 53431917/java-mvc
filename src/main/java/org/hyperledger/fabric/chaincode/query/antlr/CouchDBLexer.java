// Generated from CouchDB.g4 by ANTLR 4.7
package org.hyperledger.fabric.chaincode.query.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CouchDBLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "LETTER", "LETTERORDIGIT", "DIGIT", "ESC", "STRING", "SELECT", 
		"QUERY", "DESCRIPTION", "WHERE", "BY", "ORDER", "AND", "OR", "ASC", "DESC", 
		"LIMIT", "BOOKMARK", "CONTAINS", "TRUE", "FALSE", "FROM", "IN", "EQ", 
		"GTE", "GT", "LT", "LTE", "NE", "LEFT_PAREN", "RIGHT_PAREN", "Identifier", 
		"DECIMAL_VALUE", "INTEGER_VALUE", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
		"BRACKETED_COMMENT", "WS"
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


	public CouchDBLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CouchDB.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2%\u013f\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\3\2\3\2\3\3\3\3\3\4"+
		"\3\4\3\5\3\5\5\5Z\n\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7c\n\7\3\b\3\b\3\b"+
		"\7\bh\n\b\f\b\16\bk\13\b\3\b\3\b\3\b\3\b\3\b\7\br\n\b\f\b\16\bu\13\b\3"+
		"\b\3\b\5\by\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\37"+
		"\3\37\3\37\3 \3 \3!\3!\3\"\3\"\7\"\u00f4\n\"\f\"\16\"\u00f7\13\"\3#\6"+
		"#\u00fa\n#\r#\16#\u00fb\3#\3#\7#\u0100\n#\f#\16#\u0103\13#\3#\3#\6#\u0107"+
		"\n#\r#\16#\u0108\5#\u010b\n#\3$\6$\u010e\n$\r$\16$\u010f\3%\3%\3%\3%\7"+
		"%\u0116\n%\f%\16%\u0119\13%\3%\5%\u011c\n%\3%\5%\u011f\n%\3%\3%\3&\3&"+
		"\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\7\'\u012f\n\'\f\'\16\'\u0132\13\'"+
		"\3\'\3\'\3\'\3\'\3\'\3(\6(\u013a\n(\r(\16(\u013b\3(\3(\5is\u0130\2)\3"+
		"\3\5\4\7\2\t\2\13\2\r\2\17\5\21\6\23\7\25\b\27\t\31\n\33\13\35\f\37\r"+
		"!\16#\17%\20\'\21)\22+\23-\24/\25\61\26\63\27\65\30\67\319\32;\33=\34"+
		"?\35A\36C\37E G!I\"K#M$O%\3\2\t\6\2&&C\\aac|\3\2\62;\5\2C\\aac|\7\2&&"+
		"\62;C\\aac|\4\2\f\f\17\17\3\2--\5\2\13\f\17\17\"\"\2\u014d\2\3\3\2\2\2"+
		"\2\5\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"+
		"\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"+
		"\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\3Q\3\2\2\2\5S"+
		"\3\2\2\2\7U\3\2\2\2\tY\3\2\2\2\13[\3\2\2\2\rb\3\2\2\2\17x\3\2\2\2\21z"+
		"\3\2\2\2\23\u0081\3\2\2\2\25\u0087\3\2\2\2\27\u0093\3\2\2\2\31\u0099\3"+
		"\2\2\2\33\u009c\3\2\2\2\35\u00a2\3\2\2\2\37\u00a6\3\2\2\2!\u00a9\3\2\2"+
		"\2#\u00ad\3\2\2\2%\u00b2\3\2\2\2\'\u00b8\3\2\2\2)\u00c1\3\2\2\2+\u00ca"+
		"\3\2\2\2-\u00cf\3\2\2\2/\u00d5\3\2\2\2\61\u00da\3\2\2\2\63\u00dd\3\2\2"+
		"\2\65\u00e0\3\2\2\2\67\u00e3\3\2\2\29\u00e5\3\2\2\2;\u00e7\3\2\2\2=\u00ea"+
		"\3\2\2\2?\u00ed\3\2\2\2A\u00ef\3\2\2\2C\u00f1\3\2\2\2E\u010a\3\2\2\2G"+
		"\u010d\3\2\2\2I\u0111\3\2\2\2K\u0122\3\2\2\2M\u0129\3\2\2\2O\u0139\3\2"+
		"\2\2QR\7\60\2\2R\4\3\2\2\2ST\7.\2\2T\6\3\2\2\2UV\t\2\2\2V\b\3\2\2\2WZ"+
		"\5\7\4\2XZ\t\3\2\2YW\3\2\2\2YX\3\2\2\2Z\n\3\2\2\2[\\\t\3\2\2\\\f\3\2\2"+
		"\2]^\7^\2\2^c\7$\2\2_`\7^\2\2`c\7^\2\2ac\7)\2\2b]\3\2\2\2b_\3\2\2\2ba"+
		"\3\2\2\2c\16\3\2\2\2di\7)\2\2eh\5\r\7\2fh\13\2\2\2ge\3\2\2\2gf\3\2\2\2"+
		"hk\3\2\2\2ij\3\2\2\2ig\3\2\2\2jl\3\2\2\2ki\3\2\2\2lm\7)\2\2my\7\"\2\2"+
		"ns\7%\2\2or\5\r\7\2pr\13\2\2\2qo\3\2\2\2qp\3\2\2\2ru\3\2\2\2st\3\2\2\2"+
		"sq\3\2\2\2tv\3\2\2\2us\3\2\2\2vw\7%\2\2wy\7\"\2\2xd\3\2\2\2xn\3\2\2\2"+
		"y\20\3\2\2\2z{\7U\2\2{|\7G\2\2|}\7N\2\2}~\7G\2\2~\177\7E\2\2\177\u0080"+
		"\7V\2\2\u0080\22\3\2\2\2\u0081\u0082\7s\2\2\u0082\u0083\7w\2\2\u0083\u0084"+
		"\7g\2\2\u0084\u0085\7t\2\2\u0085\u0086\7{\2\2\u0086\24\3\2\2\2\u0087\u0088"+
		"\7f\2\2\u0088\u0089\7g\2\2\u0089\u008a\7u\2\2\u008a\u008b\7e\2\2\u008b"+
		"\u008c\7t\2\2\u008c\u008d\7k\2\2\u008d\u008e\7r\2\2\u008e\u008f\7v\2\2"+
		"\u008f\u0090\7k\2\2\u0090\u0091\7q\2\2\u0091\u0092\7p\2\2\u0092\26\3\2"+
		"\2\2\u0093\u0094\7Y\2\2\u0094\u0095\7J\2\2\u0095\u0096\7G\2\2\u0096\u0097"+
		"\7T\2\2\u0097\u0098\7G\2\2\u0098\30\3\2\2\2\u0099\u009a\7D\2\2\u009a\u009b"+
		"\7[\2\2\u009b\32\3\2\2\2\u009c\u009d\7Q\2\2\u009d\u009e\7T\2\2\u009e\u009f"+
		"\7F\2\2\u009f\u00a0\7G\2\2\u00a0\u00a1\7T\2\2\u00a1\34\3\2\2\2\u00a2\u00a3"+
		"\7C\2\2\u00a3\u00a4\7P\2\2\u00a4\u00a5\7F\2\2\u00a5\36\3\2\2\2\u00a6\u00a7"+
		"\7Q\2\2\u00a7\u00a8\7T\2\2\u00a8 \3\2\2\2\u00a9\u00aa\7C\2\2\u00aa\u00ab"+
		"\7U\2\2\u00ab\u00ac\7E\2\2\u00ac\"\3\2\2\2\u00ad\u00ae\7F\2\2\u00ae\u00af"+
		"\7G\2\2\u00af\u00b0\7U\2\2\u00b0\u00b1\7E\2\2\u00b1$\3\2\2\2\u00b2\u00b3"+
		"\7N\2\2\u00b3\u00b4\7K\2\2\u00b4\u00b5\7O\2\2\u00b5\u00b6\7K\2\2\u00b6"+
		"\u00b7\7V\2\2\u00b7&\3\2\2\2\u00b8\u00b9\7D\2\2\u00b9\u00ba\7Q\2\2\u00ba"+
		"\u00bb\7Q\2\2\u00bb\u00bc\7M\2\2\u00bc\u00bd\7O\2\2\u00bd\u00be\7C\2\2"+
		"\u00be\u00bf\7T\2\2\u00bf\u00c0\7M\2\2\u00c0(\3\2\2\2\u00c1\u00c2\7E\2"+
		"\2\u00c2\u00c3\7Q\2\2\u00c3\u00c4\7P\2\2\u00c4\u00c5\7V\2\2\u00c5\u00c6"+
		"\7C\2\2\u00c6\u00c7\7K\2\2\u00c7\u00c8\7P\2\2\u00c8\u00c9\7U\2\2\u00c9"+
		"*\3\2\2\2\u00ca\u00cb\7V\2\2\u00cb\u00cc\7T\2\2\u00cc\u00cd\7W\2\2\u00cd"+
		"\u00ce\7G\2\2\u00ce,\3\2\2\2\u00cf\u00d0\7H\2\2\u00d0\u00d1\7C\2\2\u00d1"+
		"\u00d2\7N\2\2\u00d2\u00d3\7U\2\2\u00d3\u00d4\7G\2\2\u00d4.\3\2\2\2\u00d5"+
		"\u00d6\7H\2\2\u00d6\u00d7\7T\2\2\u00d7\u00d8\7Q\2\2\u00d8\u00d9\7O\2\2"+
		"\u00d9\60\3\2\2\2\u00da\u00db\7K\2\2\u00db\u00dc\7P\2\2\u00dc\62\3\2\2"+
		"\2\u00dd\u00de\7?\2\2\u00de\u00df\7?\2\2\u00df\64\3\2\2\2\u00e0\u00e1"+
		"\7@\2\2\u00e1\u00e2\7?\2\2\u00e2\66\3\2\2\2\u00e3\u00e4\7@\2\2\u00e48"+
		"\3\2\2\2\u00e5\u00e6\7>\2\2\u00e6:\3\2\2\2\u00e7\u00e8\7>\2\2\u00e8\u00e9"+
		"\7?\2\2\u00e9<\3\2\2\2\u00ea\u00eb\7#\2\2\u00eb\u00ec\7?\2\2\u00ec>\3"+
		"\2\2\2\u00ed\u00ee\7*\2\2\u00ee@\3\2\2\2\u00ef\u00f0\7+\2\2\u00f0B\3\2"+
		"\2\2\u00f1\u00f5\t\4\2\2\u00f2\u00f4\t\5\2\2\u00f3\u00f2\3\2\2\2\u00f4"+
		"\u00f7\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6D\3\2\2\2"+
		"\u00f7\u00f5\3\2\2\2\u00f8\u00fa\5\13\6\2\u00f9\u00f8\3\2\2\2\u00fa\u00fb"+
		"\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd"+
		"\u0101\7\60\2\2\u00fe\u0100\5\13\6\2\u00ff\u00fe\3\2\2\2\u0100\u0103\3"+
		"\2\2\2\u0101\u00ff\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u010b\3\2\2\2\u0103"+
		"\u0101\3\2\2\2\u0104\u0106\7\60\2\2\u0105\u0107\5\13\6\2\u0106\u0105\3"+
		"\2\2\2\u0107\u0108\3\2\2\2\u0108\u0106\3\2\2\2\u0108\u0109\3\2\2\2\u0109"+
		"\u010b\3\2\2\2\u010a\u00f9\3\2\2\2\u010a\u0104\3\2\2\2\u010bF\3\2\2\2"+
		"\u010c\u010e\5\13\6\2\u010d\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u010d"+
		"\3\2\2\2\u010f\u0110\3\2\2\2\u0110H\3\2\2\2\u0111\u0112\7/\2\2\u0112\u0113"+
		"\7/\2\2\u0113\u0117\3\2\2\2\u0114\u0116\n\6\2\2\u0115\u0114\3\2\2\2\u0116"+
		"\u0119\3\2\2\2\u0117\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011b\3\2"+
		"\2\2\u0119\u0117\3\2\2\2\u011a\u011c\7\17\2\2\u011b\u011a\3\2\2\2\u011b"+
		"\u011c\3\2\2\2\u011c\u011e\3\2\2\2\u011d\u011f\7\f\2\2\u011e\u011d\3\2"+
		"\2\2\u011e\u011f\3\2\2\2\u011f\u0120\3\2\2\2\u0120\u0121\b%\2\2\u0121"+
		"J\3\2\2\2\u0122\u0123\7\61\2\2\u0123\u0124\7,\2\2\u0124\u0125\7,\2\2\u0125"+
		"\u0126\7\61\2\2\u0126\u0127\3\2\2\2\u0127\u0128\b&\2\2\u0128L\3\2\2\2"+
		"\u0129\u012a\7\61\2\2\u012a\u012b\7,\2\2\u012b\u012c\3\2\2\2\u012c\u0130"+
		"\n\7\2\2\u012d\u012f\13\2\2\2\u012e\u012d\3\2\2\2\u012f\u0132\3\2\2\2"+
		"\u0130\u0131\3\2\2\2\u0130\u012e\3\2\2\2\u0131\u0133\3\2\2\2\u0132\u0130"+
		"\3\2\2\2\u0133\u0134\7,\2\2\u0134\u0135\7\61\2\2\u0135\u0136\3\2\2\2\u0136"+
		"\u0137\b\'\2\2\u0137N\3\2\2\2\u0138\u013a\t\b\2\2\u0139\u0138\3\2\2\2"+
		"\u013a\u013b\3\2\2\2\u013b\u0139\3\2\2\2\u013b\u013c\3\2\2\2\u013c\u013d"+
		"\3\2\2\2\u013d\u013e\b(\2\2\u013eP\3\2\2\2\25\2Ybgiqsx\u00f5\u00fb\u0101"+
		"\u0108\u010a\u010f\u0117\u011b\u011e\u0130\u013b\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}