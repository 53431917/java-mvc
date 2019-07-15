package org.hyperledger.fabric.chaincode.core;


import org.hyperledger.fabric.chaincode.request.IRequest;

public interface HandlerAdapter {
  
  public boolean supports(Object handler);

  public abstract Object handle(IRequest request, Object handler);


}