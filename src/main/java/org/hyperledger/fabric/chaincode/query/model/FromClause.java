package org.hyperledger.fabric.chaincode.query.model;


public class FromClause {
    
    private String name = "FROM";
    
    private String fullEntityName ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullEntityName() {
        return fullEntityName;
    }

    public void setFullEntityName(String fullEntityName) {
        this.fullEntityName = fullEntityName;
    }
    
    public String toString(){
        return this.name+" " + this.fullEntityName;
    }
}
