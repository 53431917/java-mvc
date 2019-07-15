package org.hyperledger.fabric.chaincode.method;


import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.chaincode.core.HandlerAdapter;
import org.hyperledger.fabric.chaincode.method.invocation.FastJsonArgumentResolver;
import org.hyperledger.fabric.chaincode.method.invocation.HandlerMethod;
import org.hyperledger.fabric.chaincode.method.invocation.HandlerMethodArgumentResolver;
import org.hyperledger.fabric.chaincode.method.invocation.HandlerMethodArgumentResolverComposite;
import org.hyperledger.fabric.chaincode.method.invocation.InvocableHandlerMethod;
import org.hyperledger.fabric.chaincode.request.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class RequestMappingHandlerAdapter implements InitializingBean, HandlerAdapter {
  private static Logger logger = LoggerFactory
      .getLogger(RequestMappingHandlerAdapter.class);
  private HandlerMethodArgumentResolverComposite argumentResolvers;

  /* (non-Javadoc).
   * @see org.hyperledger.fabric.chaincode.core.HandlerAdapter#supports(org.hyperledger.fabric.chaincode.request.IRequest)
   */
  @Override
  public boolean supports(Object handler) {
    // TODO Auto-generated method stub
    return true;
  }

  /* (non-Javadoc).
   * @see org.hyperledger.fabric.chaincode.core.HandlerAdapter#handle(org.hyperledger.fabric.chaincode.request.IRequest)
   */
  @Override
  public Object handle(IRequest request,  Object handler) {
    // TODO Auto-generated method stub
    try {
      checkRequest(request);
      HandlerMethod handlerMethod  = (HandlerMethod) handler;
      InvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
      if (this.argumentResolvers != null) {
        invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
      }

      Object object = invocableMethod.invokeForRequest(request);
      return object;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      if (logger.isErrorEnabled()) {
        logger.error("Invoke the method "+handler.getClass().getName() + " error:" ,e);
        
      }
      throw new RuntimeException(e);
      
    }
   
   
  }
  
  
  private InvocableHandlerMethod createInvocableHandlerMethod(
      HandlerMethod handlerMethod) {
    // TODO Auto-generated method stub
      return new InvocableHandlerMethod(handlerMethod);
  }

  protected final void checkRequest(IRequest request) throws Exception {
    // Check whether we should support the request method.
   
  }

  @Override
  public void afterPropertiesSet() {

    if (this.argumentResolvers == null) {
      List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
      this.argumentResolvers = new HandlerMethodArgumentResolverComposite()
          .addResolvers(resolvers);
    }

  }

  private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
    List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    // Annotation-based argument resolution
    resolvers.add(new FastJsonArgumentResolver());

    return resolvers;
  }



}
