package org.hyperledger.fabric.chaincode.util;


import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * .
 */
@Component
public class SpringContextUtil implements ApplicationContextAware  , BeanDefinitionRegistryPostProcessor {
    
  private static ApplicationContext context;
  
  private static BeanDefinitionRegistry beanDefinitionRegistry;
   
    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext contex)
            throws BeansException {
        this.context=contex;
    }
    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }
    
    public static <T> T getBean(Class<T> requiredType){
        return  context.getBean(requiredType);
    }
    
    public <T> T getBean(String name, Class<T> requiredType) {
      return context.getBean(name, requiredType);
    }
     
    public static String getMessage(String key){
        return context.getMessage(key, null, Locale.getDefault());
    }
 
    public static ApplicationContext  getApplicationContext(){
      return context;
    }
    @Override
    public void postProcessBeanFactory(
        ConfigurableListableBeanFactory beanFactory) throws BeansException {
      // TODO Auto-generated method stub
      
    }
    @Override
    public void postProcessBeanDefinitionRegistry(
        BeanDefinitionRegistry registry) throws BeansException {
      // TODO Auto-generated method stub
      this.beanDefinitionRegistry = registry;
    }
    
    public BeanDefinitionRegistry getBeanDefinitionRegistry(){
      return this.beanDefinitionRegistry;
    }
    
    public static void registerBean(String beanId,String className) {
      // get the BeanDefinitionBuilder
      BeanDefinitionBuilder beanDefinitionBuilder =
      BeanDefinitionBuilder.genericBeanDefinition(className);
      
      
      // get the BeanDefinition
      BeanDefinition beanDefinition=beanDefinitionBuilder.getBeanDefinition();
      // register the bean
      beanDefinitionRegistry.registerBeanDefinition(beanId,beanDefinition);
  }

}
