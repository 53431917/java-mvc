package org.hyperledger.fabric.chaincode.test;



import org.hyperledger.fabric.chaincode.annotation.FabricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


@FabricService
public class FabricServiceTest {  
    private static Logger log = LoggerFactory.getLogger(FabricServiceTest.class);
   
    private  String name = "service" ;

    public FabricServiceTest(){

    }

    public String calculate(){
        log.info("calculate....");
        return this.name;
    }
}  