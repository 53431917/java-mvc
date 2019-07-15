package org.hyperledger.fabric.chaincode.method;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.hyperledger.fabric.chaincode.annotation.FabricController;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.core.HandlerExecutionChain;
import org.hyperledger.fabric.chaincode.core.HandlerMapping;
import org.hyperledger.fabric.chaincode.method.invocation.HandlerMethod;
import org.hyperledger.fabric.chaincode.request.IRequest;
import org.hyperledger.fabric.chaincode.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * 注入controller后把controller的方法映射出来
 * 
 *
 */
public class HandlerMethodMapping implements HandlerMapping, InitializingBean{

  private static Logger logger = LoggerFactory
      .getLogger(HandlerMethodMapping.class);

  private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";
  private final MappingRegistry mappingRegistry = new MappingRegistry();
  
  private Object defaultHandler;

  public void initHandlerMethods() {
    ApplicationContext applicationContext = SpringContextUtil
        .getApplicationContext();
    String[] beanNames = applicationContext.getBeanNamesForType(Object.class);

    for (String beanName : beanNames) {
      if (!beanName.startsWith(SCOPED_TARGET_NAME_PREFIX)) {
        Class<?> beanType = null;
        try {
          beanType = applicationContext.getType(beanName);
        } catch (Throwable ex) {
          // An unresolvable bean type, probably from a lazy bean - let's ignore
          // it.
          if (logger.isDebugEnabled()) {
            logger.debug("Could not resolve target class for bean with name '"
                + beanName + "'", ex);
          }
        }
        if (beanType != null && isHandler(beanType)) {
          detectHandlerMethods(beanName);
        }
      }
    }
    handlerMethodsInitialized(getHandlerMethods());
  }
  
  
  public  HandlerExecutionChain getHandler(IRequest request) throws Exception {
    Object handler = getHandlerInternal(request);
    if (handler == null) {
      handler = getDefaultHandler();
    }
    if (handler == null) {
      if (logger.isErrorEnabled()) {
          logger.error("Get the invoke method name error,check the method name is valid.");
          //logger.error("获取调用方法名为空,请检查方法名是否正确。");
      }
      return null;
    }
    // Bean name or resolved handler?
    if (handler instanceof String) {
      String handlerName = (String) handler;
      handler = SpringContextUtil.getApplicationContext().getBean(handlerName);
    }

    HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);
   
    return executionChain;
  }
  
  
  protected HandlerExecutionChain getHandlerExecutionChain(Object handler, IRequest request) {
    HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ?
        (HandlerExecutionChain) handler : new HandlerExecutionChain(handler));

   
    //chain.addInterceptor(interceptor);
       
    return chain;
  }
  
  protected HandlerMethod getHandlerInternal(IRequest request) throws Exception {

    this.mappingRegistry.acquireReadLock();
    try {
      
     
     return mappingRegistry.getMappings().get(request.getResource() + "." + request.getFunc());
    }
    finally {
      this.mappingRegistry.releaseReadLock();
    }
  }

  
  public Object getDefaultHandler() {
    return this.defaultHandler;
  }

  protected <T> void handlerMethodsInitialized(
      Map<T, HandlerMethod> handlerMethods) {
  }

  /**
   * Return a (read-only) map with all mappings and HandlerMethod's.
   * 
   * @param <T>
   */
  public Map<String, HandlerMethod> getHandlerMethods() {
    this.mappingRegistry.acquireReadLock();
    try {
      return Collections.unmodifiableMap(this.mappingRegistry.getMappings());
    } finally {
      this.mappingRegistry.releaseReadLock();
    }
  }

  protected boolean isHandler(Class<?> beanType) {
    // return (AnnotatedElementUtils.hasAnnotation(beanType,
    // BaasController.class) ||
    // AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));

    return (AnnotatedElementUtils.hasAnnotation(beanType, FabricController.class));
  }

  /**
   * Look for handler methods in a handler.
   * 
   * @param <T>
   * @param handler
   *          the bean name of a handler or a handler instance
   */
  protected <T> void detectHandlerMethods(final Object handler) {
    ApplicationContext applicationContext = SpringContextUtil
        .getApplicationContext();
    Class<?> handlerType = (handler instanceof String ? applicationContext
        .getType((String) handler) : handler.getClass());

    if (handlerType != null) {

      Set<Method> annotatedMethods = getAnnotatedMethodsInBaseType(handlerType);

     // Map<Method, T> methods = null;

      // MethodIntrospector.selectMethods(userType,
      // (MethodIntrospector.MetadataLookup<T>) method ->
      // getMappingForMethod(method, userType));

      annotatedMethods.forEach(method -> {
        if (Modifier.isPrivate(method.getModifiers())) {
          throw new IllegalStateException(String.format(
              "Private method can't have a @BaasTans annotation.", method.getName(), method
                  .getDeclaringClass().getSimpleName()));
        }
        registerHandlerMethod(handler, method);
      });

      /*
       * methods.forEach((key, mapping) -> { if
       * (Modifier.isPrivate(key.getModifiers())) { throw new
       * IllegalStateException(String.format( "私有方法不能暴露为@BaasTans注解。",
       * key.getName(), key.getDeclaringClass().getSimpleName())); }
       * registerHandlerMethod(handler, key); });
       */
    }
  }

  static Set<Method> getAnnotatedMethodsInBaseType(Class<?> baseType) {
    boolean ifcCheck = baseType.isInterface();
    if (ifcCheck && ClassUtils.isJavaLanguageInterface(baseType)) {
      return Collections.emptySet();
    }

    Set<Method> annotatedMethods = null;

    Method[] methods = (ifcCheck ? baseType.getMethods() : baseType
        .getDeclaredMethods());
    for (Method baseMethod : methods) {
      try {
        // Public methods on interfaces (including interface hierarchy),
        // non-private (and therefore overridable) methods on base classes
        if ((ifcCheck || !Modifier.isPrivate(baseMethod.getModifiers()))
            && hasSearchableAnnotations(baseMethod)) {
          if (annotatedMethods == null) {
            annotatedMethods = new HashSet<>();
          }
          annotatedMethods.add(baseMethod);
        }
      } catch (Throwable ex) {
        if (logger.isErrorEnabled()) {
          logger.error("Get " + baseType.toString() + " inner method error.", ex);
        }
        return null;
      }
    }
    if (annotatedMethods == null) {
      annotatedMethods = Collections.emptySet();
    }
    return annotatedMethods;
  }

  private static boolean hasSearchableAnnotations(Method ifcMethod) {
    Annotation[] anns = ifcMethod.getAnnotations();
    if (anns.length == 0) {
      return false;
    }
    if (anns.length == 1) {
      Class<?> annType = anns[0].annotationType();
      return (annType != Nullable.class && annType != Deprecated.class);
    }
    return true;
  }

  protected void registerHandlerMethod(Object handler, Method method) {
    this.mappingRegistry.register(handler, method);
  }

  class MappingRegistry {

    private final Map<String, HandlerMethod> mappingLookup = new LinkedHashMap<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * Return all mappings and handler methods. Not thread-safe.
     * 
     * @see #acquireReadLock()
     */
    public Map<String, HandlerMethod> getMappings() {
      return this.mappingLookup;
    }

    /**
     * Acquire the read lock when using getMappings and getMappingsByUrl.
     */
    public void acquireReadLock() {
      this.readWriteLock.readLock().lock();
    }

    /**
     * Release the read lock after using getMappings and getMappingsByUrl.
     */
    public void releaseReadLock() {
      this.readWriteLock.readLock().unlock();
    }

    public void register(Object handler, Method method) {
      this.readWriteLock.writeLock().lock();
      try {
        HandlerMethod handlerMethod = createHandlerMethod(handler, method);
        FabricMapping baasTansAnnotation = (FabricMapping) method.getAnnotation(FabricMapping.class);
        String mappingKey = baasTansAnnotation.value();
        if ("".equals(mappingKey.trim())) {
          mappingKey = method.getName();
        }
        
        //String className = handlerMethod.getMethod().getDeclaringClass().getName();
       // mappingKey = className + "." +  mappingKey;
        Object object = handlerMethod.getBean();
        mappingKey = object.toString() + "." +  mappingKey;
        
        if (mappingLookup.containsKey(mappingKey)) {
          //AnnotationUtils.getAnnotationAttributes(annotation)
          throw new java.lang.IllegalArgumentException(
                  "Method name is duplicate or method annotaion is duplicate,"
                  + "please get a different annotaion:" + mappingKey);
        }
        this.mappingLookup.put(mappingKey, handlerMethod);

      } finally {
        this.readWriteLock.writeLock().unlock();
      }
    }

    protected HandlerMethod createHandlerMethod(Object handler, Method method) {
      HandlerMethod handlerMethod;
      if (handler instanceof String) {
        String beanName = (String) handler;

        ApplicationContext applicationContext = SpringContextUtil
            .getApplicationContext();

        handlerMethod = new HandlerMethod(beanName,
            applicationContext.getAutowireCapableBeanFactory(), method);
      } else {
        handlerMethod = new HandlerMethod(handler, method);
      }
      return handlerMethod;
    }

    public void unregister(String mapping) {
      this.readWriteLock.writeLock().lock();
      try {

        this.mappingLookup.remove(mapping);
      } finally {
        this.readWriteLock.writeLock().unlock();
      }
    }
  }
  
  
  public void afterPropertiesSet() {
    initHandlerMethods();
  }

}
