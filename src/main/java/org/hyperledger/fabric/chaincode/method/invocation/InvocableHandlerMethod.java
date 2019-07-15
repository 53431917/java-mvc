package org.hyperledger.fabric.chaincode.method.invocation;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.hyperledger.fabric.chaincode.request.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class InvocableHandlerMethod extends HandlerMethod {

  private static Logger logger = LoggerFactory.getLogger(InvocableHandlerMethod.class);
  
  private HandlerMethodArgumentResolverComposite argumentResolvers = new HandlerMethodArgumentResolverComposite();

  private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();


  public InvocableHandlerMethod(HandlerMethod handlerMethod) {
    super(handlerMethod);
  }


  public InvocableHandlerMethod(Object bean, Method method) {
    super(bean, method);
  }


  public InvocableHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes)
      throws NoSuchMethodException {

    super(bean, methodName, parameterTypes);
  }




 
  public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers) {
    this.argumentResolvers = argumentResolvers;
  }

  
  public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
    this.parameterNameDiscoverer = parameterNameDiscoverer;
  }



  public Object invokeForRequest(IRequest request) throws Exception {

    Object[] args = getMethodArgumentValues(request);
    if (logger.isTraceEnabled()) {
      logger.trace("Invoking '" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
          "' with arguments " + Arrays.toString(args));
    }
    Object returnValue = doInvoke(args);
    if (logger.isTraceEnabled()) {
      logger.trace("Method [" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
          "] returned [" + returnValue + "]");
    }
    return returnValue;
  }

  
  private Object[] getMethodArgumentValues( IRequest request) throws Exception {

    MethodParameter[] parameters = getMethodParameters();
    Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      MethodParameter parameter = parameters[i];
      parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
      args[i] = resolveProvidedArgument(parameter);
      if (args[i] != null) {
        continue;
      }
      if (this.argumentResolvers.supportsParameter(parameter)) {
        try {
          args[i] = this.argumentResolvers.resolveArgument(parameter, request);
          continue;
        }
        catch (Exception ex) {
          if (logger.isDebugEnabled()) {
            logger.debug(getArgumentResolutionErrorMessage("Failed to resolve", i), ex);
          }
          throw ex;
        }
      }
      if (args[i] == null) {
        throw new IllegalStateException("Could not resolve method parameter at index " +
            parameter.getParameterIndex() + " in " + parameter.getExecutable().toGenericString() +
            ": " + getArgumentResolutionErrorMessage("No suitable resolver for", i));
      }
    }
    return args;
  }

  private String getArgumentResolutionErrorMessage(String text, int index) {
    Class<?> paramType = getMethodParameters()[index].getParameterType();
    return text + " argument " + index + " of type '" + paramType.getName() + "'";
  }


  private Object resolveProvidedArgument(MethodParameter parameter,  Object... providedArgs) {
    if (providedArgs == null) {
      return null;
    }
    for (Object providedArg : providedArgs) {
      if (parameter.getParameterType().isInstance(providedArg)) {
        return providedArg;
      }
    }
    return null;
  }


  protected Object doInvoke(Object... args) throws Exception {
    ReflectionUtils.makeAccessible(getBridgedMethod());
    Object actualBean = null;
    if ("java.lang.String".equals(getBean().getClass().getName())){
      actualBean = this.getBeanFactory().getBean(getBean().toString());
    }
   
    try {
      if (actualBean != null) {
        return getBridgedMethod().invoke(actualBean, args);
      } else {
        return getBridgedMethod().invoke(getBean(), args);
      }
     
    }
    catch (IllegalArgumentException ex) {
      assertTargetBean(getBridgedMethod(), getBean(), args);
      String text = (ex.getMessage() != null ? ex.getMessage() : "Illegal argument");
      throw new IllegalStateException(getInvocationErrorMessage(text, args), ex);
    }
    catch (InvocationTargetException ex) {
      // Unwrap for HandlerExceptionResolvers ...
      Throwable targetException = ex.getTargetException();
      if (targetException instanceof RuntimeException) {
        throw (RuntimeException) targetException;
      }
      else if (targetException instanceof Error) {
        throw (Error) targetException;
      }
      else if (targetException instanceof Exception) {
        throw (Exception) targetException;
      }
      else {
        String text = getInvocationErrorMessage("Failed to invoke handler method", args);
        throw new IllegalStateException(text, targetException);
      }
    }
  }


  private void assertTargetBean(Method method, Object targetBean, Object[] args) {
    Class<?> methodDeclaringClass = method.getDeclaringClass();
    Class<?> targetBeanClass = targetBean.getClass();
    if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
      String text = "The mapped handler method class '" + methodDeclaringClass.getName() +
          "' is not an instance of the actual controller bean class '" +
          targetBeanClass.getName() + "'. If the controller requires proxying " +
          "(e.g. due to @Transactional), please use class-based proxying.";
      throw new IllegalStateException(getInvocationErrorMessage(text, args));
    }
  }

  private String getInvocationErrorMessage(String text, Object[] resolvedArgs) {
    StringBuilder sb = new StringBuilder(getDetailedErrorMessage(text));
    sb.append("Resolved arguments: \n");
    for (int i = 0; i < resolvedArgs.length; i++) {
      sb.append("[").append(i).append("] ");
      if (resolvedArgs[i] == null) {
        sb.append("[null] \n");
      }
      else {
        sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
        sb.append("[value=").append(resolvedArgs[i]).append("]\n");
      }
    }
    return sb.toString();
  }

  /**
   * Adds HandlerMethod details such as the bean type and method signature to the message.
   * @param text error message to append the HandlerMethod details to
   */
  protected String getDetailedErrorMessage(String text) {
    StringBuilder sb = new StringBuilder(text).append("\n");
    sb.append("HandlerMethod details: \n");
    sb.append("Controller [").append(getBeanType().getName()).append("]\n");
    sb.append("Method [").append(getBridgedMethod().toGenericString()).append("]\n");
    return sb.toString();
  }
  


}
