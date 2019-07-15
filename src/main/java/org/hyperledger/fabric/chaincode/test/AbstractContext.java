package org.hyperledger.fabric.chaincode.test;


import org.hyperledger.fabric.chaincode.annotation.FabricId;



public class AbstractContext {
    
    @FabricId
    private String gid;
    private String name;
    
    private String type;
    private String owner  ;

    private String memo  ;    
    
    private boolean b;
    
    private int age;
    
    private String cust;

    public String getGid() {
        return gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMemo() {
        return memo;
    }
    public boolean getB() {
        return b;
    }
    public void setB(boolean b) {
        this.b = b;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getCust() {
        return cust;
    }
    public void setCust(String cust) {
        this.cust = cust;
    }
 
    
}
