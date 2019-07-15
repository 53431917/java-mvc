package org.hyperledger.fabric.chaincode.query;


import org.hyperledger.fabric.chaincode.query.impl.CouchdbQueryParser;
public enum DefaultParserHandlers implements ParserFactory{

    COUCHDBPARSER(() -> CouchdbQueryParser.getInstance());
  
    private final ParserFactory pf;
    
    
    DefaultParserHandlers(ParserFactory pf) {
        this.pf = pf;
    }

    @Override
    public Parser getParser() {
        // TODO Auto-generated method stub
        return pf.getParser();
    }

}
