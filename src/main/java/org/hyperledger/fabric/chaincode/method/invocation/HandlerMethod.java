package org.hyperledger.fabric.chaincode.method.invocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

public class HandlerMethod {
  private static Logger log = LoggerFactory.getLogger(HandlerMethod.class);
  
  
  /** Logger that is available to subclasses */

  private  Object bean;

  private  BeanFactory beanFactory;

  private  Class<?> beanType;

  private  Method method;
  
  /**
   * 
   */
  private  final Method bridgedMethod;


  private  MethodParameter[] parameters;

  /**
   * Create an instance from a bean instance and a method.
   */
  public HandlerMethod(Object bean, Method method) {
    this.bean = bean;
    this.beanFactory = null;
    this.method = method;
    this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
    this.parameters = initMethodParameters();
    
  }

  /**
   * Create an instance from a bean instance, method name, and parameter types.
   * @throws NoSuchMethodException when the method cannot be found
   */
  public HandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
    this.bean = bean;
    this.beanFactory = null;
    this.method = bean.getClass().getMethod(methodName, parameterTypes);
    this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
    this.parameters = initMethodParameters();
  }

  /**
   * Create an instance from a bean name, a method, and a {@code BeanFactory}.
   * The method {@link #createWithResolvedBean()} may be used later to
   * re-create the {@code HandlerMethod} with an initialized bean.
   */
  public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
  
    this.beanFactory = beanFactory;
    this.bean = beanName;
    this.beanType = beanFactory.getType(beanName);
    if (beanType == null) {
      throw new IllegalStateException("Cannot resolve bean type for bean with name '" + beanName + "'");
    }
    this.method = method;
    this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
    this.parameters = initMethodParameters();
  }

  /**
   * Copy constructor for use in subclasses.
   */
  protected HandlerMethod(HandlerMethod handlerMethod) {
    this.bean = handlerMethod.bean;
    this.beanFactory = handlerMethod.beanFactory;
    this.beanType = handlerMethod.beanType;
    this.method = handlerMethod.method;
    this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
    this.parameters = handlerMethod.parameters;
  }

  /**
   * Re-create HandlerMethod with the resolved handler.
   */
  private HandlerMethod(HandlerMethod handlerMethod, Object handler) {
    this.bean = handler;
    this.beanFactory = handlerMethod.beanFactory;
    this.beanType = handlerMethod.beanType;
    this.method = handlerMethod.method;
    this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
    this.parameters = handlerMethod.parameters;
  }


  private MethodParameter[] initMethodParameters() {

    int count = this.method.getParameterTypes().length;
    MethodParameter[] result = new MethodParameter[count];
    for (int i = 0; i < count; i++) {
      MethodParameter parameter = new MethodParameter(method,i);
      GenericTypeResolver.resolveParameterType(parameter, this.beanType);
      result[i] = parameter;
    }
    return result;
  }




  /**
   * Return the bean for this handler method.
   */
  public Object getBean() {
    return this.bean;
  }

  /**
   * Return the method for this handler method.
   */
  public Method getMethod() {
    return this.method;
  }
  
  protected Method getBridgedMethod() {
    return this.bridgedMethod;
  }


  /**
   * This method returns the type of the handler for this handler method.
   * <p>Note that if the bean type is a CGLIB-generated class, the original
   * user-defined class is returned.
   */
  public Class<?> getBeanType() {
    return this.beanType;
  }


  /**
   * Return the method parameters for this handler method.
   */
  public MethodParameter[] getMethodParameters() {
    return this.parameters;
  }





  /**
   * Return a single annotation on the underlying method traversing its super methods
   * if no annotation can be found on the given method itself.
   * <p>Also supports <em>merged</em> composed annotations with attribute
   * overrides as of Spring Framework 4.2.2.
   * @param annotationType the type of annotation to introspect the method for
   * @return the annotation, or {@code null} if none found
   * @see AnnotatedElementUtils#findMergedAnnotation
   */
  public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
    return AnnotatedElementUtils.findMergedAnnotation(this.method, annotationType);
  }

  /**
   * Return whether the parameter is declared with the given annotation type.
   * @param annotationType the annotation type to look for
   * @since 4.3
   * @see AnnotatedElementUtils#hasAnnotation
   */
  public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
    return AnnotatedElementUtils.hasAnnotation(this.method, annotationType);
  }


  /**
   * If the provided instance contains a bean name rather than an object instance,
   * the bean name is resolved before a {@link HandlerMethod} is created and returned.
   */
  public HandlerMethod createWithResolvedBean() {
    Object handler = this.bean;
    if (this.bean instanceof String) {
      String beanName = (String) this.bean;
      handler = this.beanFactory.getBean(beanName);
    }
    return new HandlerMethod(this, handler);
  }

  /**
   * Return a short representation of this handler method for log message purposes.
   * @since 4.3
   */
  public String getShortLogMessage() {
    int args = this.method.getParameterCount();
    return getBeanType().getName() + "#" + this.method.getName() + "[" + args + " args]";
  }


  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof HandlerMethod)) {
      return false;
    }
    HandlerMethod otherMethod = (HandlerMethod) other;
    return (this.bean.equals(otherMethod.bean) && this.method.equals(otherMethod.method));
  }

  @Override
  public int hashCode() {
    return (this.bean.hashCode() * 31 + this.method.hashCode());
  }

  @Override
  public String toString() {
    return this.method.toGenericString();
  }

  public BeanFactory getBeanFactory() {
    return beanFactory;
  }

  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }


  

}

