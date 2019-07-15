package org.hyperledger.fabric.chaincode.core;


import org.hyperledger.fabric.chaincode.request.IRequest;

public interface HandlerMapping {

  public HandlerExecutionChain getHandler(IRequest request) throws Exception;
}
