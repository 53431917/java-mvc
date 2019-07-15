package org.hyperledger.fabric.chaincode.method.invocation;


import org.hyperledger.fabric.chaincode.request.IRequest;
import org.springframework.core.MethodParameter;

public interface HandlerMethodArgumentResolver {


  boolean supportsParameter(MethodParameter parameter);

  Object resolveArgument(MethodParameter parameter,IRequest request) throws Exception;
}
