package org.hyperledger.fabric.chaincode.core;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.chaincode.request.IRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

public class HandlerExecutionChain {


  /**
   * HandlerMethod实例.
   */
  private  Object handler;


  private List<HandlerInterceptor> interceptorList;

  private int interceptorIndex = -1;
  
  public HandlerExecutionChain(Object handler) {
    this(handler, (HandlerInterceptor[]) null);
  }
  
  public HandlerExecutionChain(Object handler,  HandlerInterceptor... interceptors) {
    if (handler instanceof HandlerExecutionChain) {
      HandlerExecutionChain originalChain = (HandlerExecutionChain) handler;
      this.handler = originalChain.getHandler();
      this.interceptorList = new ArrayList<>();
      CollectionUtils.mergeArrayIntoCollection(originalChain.getInterceptors(), this.interceptorList);
      CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
    }
    else {
      this.handler = handler;
    }
  }
  
  
  boolean applyPreHandle(IRequest request) throws Exception {
    List<HandlerInterceptor> interceptors = getInterceptors();
    if (!ObjectUtils.isEmpty(interceptors)) {
      for (int i = 0; i < interceptors.size(); i++) {
        HandlerInterceptor interceptor = interceptors.get(i);
        if (!interceptor.preHandle(request,  this.handler)) {
         // triggerAfterCompletion(request,  null);
          return false;
        }
        this.interceptorIndex = i;
      }
    }
    return true;
  }
  
  
  void applyPostHandle(IRequest request)
      throws Exception {

    List<HandlerInterceptor> interceptors = getInterceptors();
    if (!ObjectUtils.isEmpty(interceptors)) {
      for (int i = interceptors.size() - 1; i >= 0; i--) {
        HandlerInterceptor interceptor = interceptors.get(i);
        interceptor.postHandle(request, this.handler);
      }
    }
  }
 
  
  public Object getHandler() {
    return this.handler;
  }
  
  public List<HandlerInterceptor> getInterceptors() {
   
    return this.interceptorList;
  }

}
