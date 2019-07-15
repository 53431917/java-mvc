package org.hyperledger.fabric.chaincode.query.model;


public class LimitClause {
    
    private String name = " LIMIT ";

    public String  size ;

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String toString() {
       return name + this.size;
    }
}
