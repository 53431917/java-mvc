package org.hyperledger.fabric.chaincode.controller;


import java.util.Map;

import org.hyperledger.fabric.chaincode.annotation.FabricController;
import org.hyperledger.fabric.chaincode.annotation.FabricParam;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.component.IFabricQuery;
import org.springframework.beans.factory.annotation.Autowired;

@FabricController
public class QueryController {

  @Autowired
  private IFabricQuery fabricQuery;
    
  @FabricMapping
  public Object query(
		  @FabricParam(value = "queryName", required = true) String queryName, 
		  @FabricParam(value = "map", required = true) 
		  Map<String, Object> map) {
       Object o = fabricQuery.queryObjectByTagName(queryName, map);
       return o;
   }
}
