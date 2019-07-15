package org.hyperledger.fabric.chaincode.test;



import org.hyperledger.fabric.chaincode.annotation.FabricEntity;
import org.hyperledger.fabric.chaincode.annotation.FabricId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


@FabricEntity(value="entity")
public class FabricEntityTest {  
    private static Logger log = LoggerFactory.getLogger(FabricEntityTest.class);
    private String cust = "asdf";

    //@BaasField(required=true,rule="胡咧咧的正则")
    private  int age = 0;
    
    @FabricId
    private int id = 0;
    

    private boolean b;
    
    
    
  

    public FabricEntityTest(){

    }
    
    







    public String getCust() {
      return cust;
    }



    public void setCust(String cust) {
      this.cust = cust;
    }



    public int getId() {
      return id;
    }



    public void setId(int id) {
      this.id = id;
    }



    public void setAge(int age) {
      this.age = age;
    }



    public int getAge(){
        log.info("asdfasdf");
        return this.age;
    }



    public boolean getB() {
        return b;
    }



    public void setB(boolean b) {
        this.b = b;
    }
    
    
}  