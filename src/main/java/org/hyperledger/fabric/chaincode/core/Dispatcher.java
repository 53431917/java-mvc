package org.hyperledger.fabric.chaincode.core;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.request.IRequest;
import org.hyperledger.fabric.chaincode.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class Dispatcher {
  private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
  
  public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
  
  public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";

  private static Dispatcher dispatcher = new Dispatcher();

  private List<HandlerMapping> handlerMappings;
  
  private List<HandlerAdapter> handlerAdapters;
  
  private Map<String, String> defaultStrategies = new HashMap<>();
  
  {
    defaultStrategies.put("org.hyperledger.fabric.chaincode.core.HandlerMapping",
        "org.hyperledger.fabric.chaincode.method.HandlerMethodMapping");
    defaultStrategies.put("org.hyperledger.fabric.chaincode.core.HandlerAdapter",
        "org.hyperledger.fabric.chaincode.method.RequestMappingHandlerAdapter");
    
  }

  private Dispatcher() {
    initStrategies();
   // Predicate<HandlerAdapter> p = s -> { return s.equals("");};

  }



  public static Dispatcher getInstance() {
    return dispatcher;
  }

  public Object doDispatch(IRequest request) throws Exception {
    HandlerExecutionChain mappedHandler = getHandler(request);
    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
    if (!mappedHandler.applyPreHandle(request)) {
    	 if (logger.isErrorEnabled()) {
             logger.trace("Process handlerInterceptor failed.");
           }
      throw new RuntimeException("HandlerInterceptor failed.");
    }
    
    // Actually invoke the handler.
    Object o = ha.handle(request, mappedHandler.getHandler());

    mappedHandler.applyPostHandle(request);
    return o;
  }
  
  protected HandlerExecutionChain getHandler(IRequest request) throws Exception {
    if (this.handlerMappings != null) {
      for (HandlerMapping hm : this.handlerMappings) {
        
        HandlerExecutionChain handler = hm.getHandler(request);
        if (handler != null) {
          return handler;
        }
      }
    }
    return null;
  }
  
  protected HandlerAdapter getHandlerAdapter(Object handler) throws Exception {
    if (this.handlerAdapters != null) {
      for (HandlerAdapter ha : this.handlerAdapters) {
        if (logger.isTraceEnabled()) {
          logger.trace("Testing handler adapter [" + ha + "]");
        }
        if (ha.supports(handler)) {
          return ha;
        }
      }
    }
    throw new RuntimeException("No adapter for handler [" + handler +
        "]: The Dispatcher configuration needs to include a HandlerAdapter that supports this handler");
  }


  protected void initStrategies() {
    initHandlerMappings();
    initHandlerAdapters();
  }

  private void initHandlerAdapters() {
    this.handlerAdapters = null;
    ApplicationContext context = SpringContextUtil.getApplicationContext();
      // Find all HandlerAdapters in the ApplicationContext, including ancestor contexts.
    Map<String, HandlerAdapter> matchingBeans =
          BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
    if (!matchingBeans.isEmpty()) {
        this.handlerAdapters = new ArrayList<>(matchingBeans.values());
        // We keep HandlerAdapters in sorted order.
        AnnotationAwareOrderComparator.sort(this.handlerAdapters);
      }
    else {
      try {
        HandlerAdapter ha = context.getBean(HANDLER_ADAPTER_BEAN_NAME, HandlerAdapter.class);
        this.handlerAdapters = Collections.singletonList(ha);
      }
      catch (NoSuchBeanDefinitionException ex) {
        // Ignore, we'll add a default HandlerAdapter later.
        if (this.logger.isDebugEnabled()) {
          logger.warn("Can't find HandlerAdapter in container,use the default HandlerAdapter.");
        }
      }
    }

    // Ensure we have at least some HandlerAdapters, by registering
    // default HandlerAdapters if no other adapters are found.
    if (this.handlerAdapters == null) {
      this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
      if (logger.isDebugEnabled()) {
        logger.debug("No HandlerAdapters found for dispatcher : using default");
      }
    }

  }

  private void initHandlerMappings() {
    // TODO Auto-generated method stub
    this.handlerMappings = null;
    ApplicationContext context = SpringContextUtil.getApplicationContext();
 
      // Find all HandlerMappings in the ApplicationContext, including ancestor contexts.
    Map<String, HandlerMapping> matchingBeans =
          BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
    if (!matchingBeans.isEmpty()) {
        this.handlerMappings = new ArrayList<>(matchingBeans.values());
        // We keep HandlerMappings in sorted order.
        AnnotationAwareOrderComparator.sort(this.handlerMappings);
      }
    // Ensure we have at least one HandlerMapping, by registering
    // a default HandlerMapping if no other mappings are found.
    if (this.handlerMappings == null) {
      this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
      if (logger.isDebugEnabled()) {
        logger.debug("No HandlerMappings found for dispatcher : using default");
      }
    }    

  }
  
  protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
    String key = strategyInterface.getName();
    String value = defaultStrategies.get(key);
    if (value != null) {
      String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
      List<T> strategies = new ArrayList<>(classNames.length);
      for (String className : classNames) {
        try {
          Class<?> clazz = ClassUtils.forName(className, Dispatcher.class.getClassLoader());
          Object strategy = createDefaultStrategy(context, clazz);
          strategies.add((T) strategy);
        }
        catch (ClassNotFoundException ex) {
          throw new BeanInitializationException(
              "Could not find DispatcherServlet's default strategy class [" + className +
              "] for interface [" + key + "]", ex);
        }
        catch (LinkageError err) {
          throw new BeanInitializationException(
              "Unresolvable class definition for DispatcherServlet's defaultstrategy class [" +
              className + "] for interface [" + key + "]", err);
        }
      }
      return strategies;
    }
    else {
      return new LinkedList<>();
    }
  }
  protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz) {
    return context.getAutowireCapableBeanFactory().createBean(clazz,AutowireCapableBeanFactory.AUTOWIRE_BY_NAME,true);
  }
  /**
   * bean.
   * @param clazz
   * @return
   */
  protected Object createDefaultStrategy(final Class<?> clazz) {
    return SpringContextUtil.getApplicationContext()
        .getAutowireCapableBeanFactory().createBean(clazz);
  }


}
