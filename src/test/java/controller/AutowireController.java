package controller;

import org.hyperledger.fabric.chaincode.annotation.FabricController;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.service.IRepositorService;
import org.hyperledger.fabric.chaincode.test.Context;
import org.springframework.beans.factory.annotation.Autowired;

@FabricController
public class AutowireController {
  
    @Autowired
    private IRepositorService<Context> contextEvidenceService;
  @FabricMapping
  public String autoWireTest(){
    return "";
  }
  
  
  
}
