package org.hyperledger.fabric.chaincode.core;

import org.hyperledger.fabric.chaincode.request.IRequest;

public interface HandlerInterceptor {
  
  default boolean preHandle(IRequest request, Object handler) throws Exception {

    return true;
  }

  default void postHandle(IRequest request, Object handler) throws Exception {
  }

  default void afterCompletion(IRequest request, Object handler)
      throws Exception {
  }

}
