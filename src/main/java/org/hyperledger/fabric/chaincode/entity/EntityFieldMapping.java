package org.hyperledger.fabric.chaincode.entity;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.hyperledger.fabric.chaincode.annotation.FabricEntity;
import org.hyperledger.fabric.chaincode.annotation.FabricField;
import org.hyperledger.fabric.chaincode.annotation.FabricId;
import org.hyperledger.fabric.chaincode.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

public class EntityFieldMapping implements InitializingBean{

  private static Logger logger = LoggerFactory
      .getLogger(EntityFieldMapping.class);
  
  private ApplicationContext applicationContext = SpringContextUtil
      .getApplicationContext();
  
  private final MappingRegistry mappingRegistry = new MappingRegistry();
  
  public void initEntityField() {
  
    String[] beanNames = applicationContext.getBeanNamesForAnnotation(FabricEntity.class);

    for (String beanName : beanNames) {
        detectEntityField(beanName);
      }
  }
  
  private void detectEntityField(String beanName) {
    // TODO Auto-generated method stub
   
    mappingRegistry.register(beanName);
  }

  
  
  class MappingRegistry {

    private final Map<String, Map<String, List<String>>> mappingLookup = new LinkedHashMap<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * Return all mappings and handler methods. Not thread-safe.
     * 
     * @see #acquireReadLock()
     */
    public Map<String,  Map<String, List<String>>> getMappings() {
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

    public void register(String beanName) {
      Object entityObject = applicationContext.getBean(beanName);
      this.readWriteLock.writeLock().lock();
      try {
        Map<String, List<String>> fieldMap = new HashMap<>();
        Field[] fields = entityObject.getClass().getDeclaredFields();
        List<String> baasIdList = new ArrayList<>();
        List<String> baasFieldList = new ArrayList<>();
        for (Field field : fields) {
          FabricId baasId = (FabricId) field.getAnnotation(FabricId.class);
          if (baasId != null) {
            baasIdList.add(field.getName());
          }
          
          FabricField baasField = (FabricField) field.getAnnotation(FabricField.class);
          if (baasField != null) {
            baasFieldList.add(field.getName());
          }
        }
        fieldMap.put("baasId", baasIdList);
        fieldMap.put("baasField", baasFieldList);
        
        this.mappingLookup.put(entityObject.getClass().getName(), fieldMap);

      } finally {
        this.readWriteLock.writeLock().unlock();
      }
    }

   

    public void unregister(String beanName) {
      this.readWriteLock.writeLock().lock();
      try {

        this.mappingLookup.remove(beanName);
      } finally {
        this.readWriteLock.writeLock().unlock();
      }
    }
  }


  @Override
  public void afterPropertiesSet() throws Exception {
    // TODO Auto-generated method stub
    
  }
}
