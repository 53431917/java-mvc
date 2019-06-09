package org.hyperledger.fabric.chaincode.query.model;

public class Value {
    
    private String preCharacter;
    
    private Object value;
    
    private String suffixCharacter;

    public String getPreCharacter() {
        return preCharacter;
    }

    public void setPreCharacter(String preCharacter) {
        this.preCharacter = preCharacter;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getSuffixCharacter() {
        return suffixCharacter;
    }

    public void setSuffixCharacter(String suffixCharacter) {
        this.suffixCharacter = suffixCharacter;
    }
    
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (preCharacter != null) {
            sb.append(preCharacter);
        }
        if (value != null) {
            sb.append(value.toString());
        }
        if (suffixCharacter != null) {
            sb.append(suffixCharacter);
        }
        
        
        
        return sb.toString();
    }

}
