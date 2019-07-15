package org.hyperledger.fabric.chaincode.test;

import org.hyperledger.fabric.chaincode.annotation.FabricEntity;


@FabricEntity
public class Context  extends AbstractContext{
    private String textContext ;

    public String getTextContext() {
        return textContext;
    }

    public void setTextContext(String textContext) {
        this.textContext = textContext;
    }
    
    
}
