package org.hyperledger.fabric.chaincode.query.model;


public class OrderbyExpression {
    
    private String properName;
    
    private String orderStr;

    public String getProperName() {
        return properName;
    }

    public void setProperName(String properName) {
        this.properName = properName;
    }

    public String getOrderStr() {
        return orderStr;
    }

    public void setOrderStr(String orderStr) {
        this.orderStr = orderStr;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.properName != null) {
            sb.append(this.properName);
        }
        if (this.orderStr != null) {
            sb.append(" ");
            sb.append(this.orderStr);
        }
        return sb.toString();
    }
    
    

}
