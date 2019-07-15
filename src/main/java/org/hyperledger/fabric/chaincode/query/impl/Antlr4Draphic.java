package org.hyperledger.fabric.chaincode.query.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintException;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;

/** Run a lexer/parser combo, optionally printing tree string or generating
 *  postscript file. Optionally taking input file.
 *
 *  $ java org.antlr.v4.runtime.misc.TestRig GrammarName startRuleName
 *        [-tree]
 *        [-tokens] [-gui] [-ps file.ps]
 *        [-trace]
 *        [-diagnostics]
 *        [-SLL]
 *        [input-filename(s)]
 */
public class Antlr4Draphic {
    public static final String LEXER_START_RULE_NAME = "tokens";

    protected String grammarName;
    protected String startRuleName;
    protected  List<InputStream> inputFiles = new ArrayList<InputStream>();
    protected boolean printTree = true;
    protected boolean gui = false;
    protected String psFile = null;
    protected boolean showTokens = false;
    protected boolean trace = false;
    protected boolean diagnostics = false;
    protected String encoding = null;
    protected boolean SLL = false;

    public Antlr4Draphic(String[] args, List<InputStream> inputFiles) throws Exception {
        
        if ( args.length < 2 ) {
            System.err.println("java org.antlr.v4.runtime.misc.TestRig GrammarName startRuleName\n" +
                    "  [-tokens] [-tree] [-gui] [-ps file.ps] [-encoding encodingname]\n" +
                    "  [-trace] [-diagnostics] [-SLL]\n"+
                    "  [input-filename(s)]");
            System.err.println("Use startRuleName='tokens' if GrammarName is a lexer grammar.");
            System.err.println("Omitting input-filename makes rig read from stdin.");
            return;
        }
        this.inputFiles = inputFiles;
        int i=0;
        grammarName = args[i];
        i++;
        startRuleName = args[i];
        i++;
        while ( i<args.length ) {
            String arg = args[i];
            i++;
       
            if ( arg.equals("-tree") ) {
                printTree = true;
            }
            if ( arg.equals("-gui") ) {
                gui = true;
            }
            if ( arg.equals("-tokens") ) {
                showTokens = true;
            }
            else if ( arg.equals("-trace") ) {
                trace = true;
            }
            else if ( arg.equals("-SLL") ) {
                SLL = true;
            }
            else if ( arg.equals("-diagnostics") ) {
                diagnostics = true;
            }
            else if ( arg.equals("-encoding") ) {
                if ( i>=args.length ) {
                    System.err.println("missing encoding on -encoding");
                    return;
                }
                encoding = args[i];
                i++;
            }
            else if ( arg.equals("-ps") ) {
                if ( i>=args.length ) {
                    System.err.println("missing filename on -ps");
                    return;
                }
                psFile = args[i];
                i++;
            }
        }
        
 
    }

  

    public void process() throws Exception {
        //System.out.println("exec "+grammarName+" "+startRuleName);
        String lexerName = grammarName+"Lexer";
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class<? extends Lexer> lexerClass = null;
        try {
            lexerClass = cl.loadClass(lexerName).asSubclass(Lexer.class);
        }
        catch (java.lang.ClassNotFoundException cnfe) {
            System.err.println("1: Can't load "+lexerName+" as lexer or parser");
            return;
        }

        Constructor<? extends Lexer> lexerCtor = lexerClass.getConstructor(CharStream.class);
        Lexer lexer = lexerCtor.newInstance((CharStream)null);

        Class<? extends Parser> parserClass = null;
        Parser parser = null;
        if ( !startRuleName.equals(LEXER_START_RULE_NAME) ) {
            String parserName = grammarName+"Parser";
            parserClass = cl.loadClass(parserName).asSubclass(Parser.class);
            if ( parserClass==null ) {
                System.err.println("Can't load "+parserName);
            }
            Constructor<? extends Parser> parserCtor = parserClass.getConstructor(TokenStream.class);
            parser = parserCtor.newInstance((TokenStream)null);
        }

        if ( inputFiles.size()==0 ) {
            InputStream is = System.in;
            Reader r;
            if ( encoding!=null ) {
                r = new InputStreamReader(is, encoding);
            }
            else {
                r = new InputStreamReader(is);
            }

            process(lexer, parserClass, parser, is, r);
            return;
        }
        for (InputStream inputFile : inputFiles) {
            InputStream is = System.in;
            if ( inputFile!=null ) {
                is = inputFile;
            }
            Reader r;
            if ( encoding!=null ) {
                r = new InputStreamReader(is, encoding);
            }
            else {
                r = new InputStreamReader(is);
            }

            if ( inputFiles.size()>1 ) {
                System.err.println(inputFile);
            }
            process(lexer, parserClass, parser, is, r);
        }
    }

    protected void process(Lexer lexer, Class<? extends Parser> parserClass, Parser parser, InputStream is, Reader r) throws IOException, IllegalAccessException, InvocationTargetException, PrintException {
        try {
            ANTLRInputStream input = new ANTLRInputStream(r);
            lexer.setInputStream(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            tokens.fill();

            if ( showTokens ) {
                for (Object tok : tokens.getTokens()) {
                    System.out.println(tok);
                }
            }

            if ( startRuleName.equals(LEXER_START_RULE_NAME) ) return;

            if ( diagnostics ) {
                parser.addErrorListener(new DiagnosticErrorListener());
                parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
            }

            if ( printTree || gui || psFile!=null ) {
                parser.setBuildParseTree(true);
            }

            if ( SLL ) { // overrides diagnostics
                parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
            }

            parser.setTokenStream(tokens);
            parser.setTrace(trace);
            //parser.setErrorHandler(new BailErrorStrategy());

            try {
                Method startRule = parserClass.getMethod(startRuleName);
                ParserRuleContext tree = (ParserRuleContext)startRule.invoke(parser, (Object[])null);

                if ( printTree ) {
                    System.out.println(tree.toStringTree(parser));
                }
                if ( gui ) {
                    //tree.inspect(tree,parser);
                    Trees.inspect(tree, parser);
                }
                if ( psFile!=null ) {
                    //tree.save(parser, psFile); // Generate postscript
                }
            }
            catch (NoSuchMethodException nsme) {
                System.err.println("No method for rule "+startRuleName+" or it has arguments");
            }
        }
        finally {
            if ( r!=null ) r.close();
            if ( is!=null ) is.close();
        }
    }

    @SuppressWarnings("unused")
    private static class BailErrorStrategy extends DefaultErrorStrategy {
        /** Instead of recovering from exception e, rethrow it wrapped
         * in a generic RuntimeException so it is not caught by the
         * rule function catches. Exception e is the "cause" of the
         * RuntimeException.
         */
        @Override
        public void recover(Parser recognizer, RecognitionException e) {
            throw new RuntimeException(e);
        }
        /** Make sure we don't attempt to recover inline; if the parser
         * successfully recovers, it won't throw an exception.
         */
        @Override
        public Token recoverInline(Parser recognizer)
                throws RecognitionException
        {
            throw new RuntimeException(new InputMismatchException(recognizer));
        }
        /** Make sure we don't attempt to recover from problems in subrules. */
        @Override
        public void sync(Parser recognizer) { }
    }
}