package org.hyperledger.fabric.chaincode.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.annotation.FabricEntity;
import org.hyperledger.fabric.chaincode.annotation.FabricId;
import org.hyperledger.fabric.chaincode.util.ReflectUtil;
import org.hyperledger.fabric.chaincode.util.SpringContextUtil;
import org.hyperledger.fabric.chaincode.util.ThreadLocalUtils;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 存储服务抽像类.
 * @author hechang
 *
 */
public class AbstractBaseRepository  {

    private static Logger logger = LoggerFactory
            .getLogger(AbstractBaseRepository.class);

  /**
   * 从spring容器中查找@BaasEntity的Class.
   * 效率高且可以验证className是否合法.
   * @param className
   * @return
   */
  protected Class<?> getClass(String className){
   if (className == null) {
     throw new RuntimeException("className is null。");
   }
   Map<String, Object> map = SpringContextUtil.getApplicationContext()
                          .getBeansWithAnnotation(FabricEntity.class);
   if (map.isEmpty()) {
     if (logger.isErrorEnabled()) {
         logger.error("Can't find @BaasEntity annotation in " + className);   
     }
     throw new RuntimeException("未找到@BaasEntity注解的实体类。");
   }
   final List<Class<?>> list = new ArrayList<>(1);
   map.forEach((k, v) -> {
     String objectClassName = v.getClass().getName();
     if (className.equals(objectClassName)){
      list.add(v.getClass());
     }
   });
   if (list.isEmpty()) {
       if (logger.isErrorEnabled()) {
           logger.error("Entity  " + className + " have not registerd.");   
       }
       
     throw new RuntimeException("实体类型" + className + "没用注册。");
   }
    return list.get(0);
  }

  /**
   * 组合键.
   * @param key
   * @param className
   * @return
   */
  protected <T> CompositeKey createComposerKey(String key, Class<T> clazz) {
    String className = clazz.getName();
    ChaincodeStub stub = ThreadLocalUtils.get("stub");
    CompositeKey ck = stub.createCompositeKey(className, key.toString());
    return ck;
  }
  
  /**
   * .
   * @return
   */
  public ChaincodeStub getChaincodeStub(){
    ChaincodeStub stub = ThreadLocalUtils.get("stub");
    return stub;
  }
  
  /**
   * .
   * @param clazz
   * @param iterator
   * @return
   */
  protected <T> List<T> transKeyValue(final Class<T> clazz,
      Iterator<KeyValue> iterator) {
    List<T> list = new ArrayList<>();
    while (iterator.hasNext()) {
      KeyValue kv = iterator.next();
      String v = kv.getStringValue();
      T t = (T) JSON.parseObject(v, clazz);
      list.add(t);
    }
    return list;
  }


  
  protected <T> String getBaasIdValue(T t) {
      Class<?> clazz = this.getClass(t.getClass().getName());
      
     // Field[] fields = clazz.getDeclaredFields();
      Field[] fields = ReflectUtil.getAllFields(t);
      for (Field field : fields) {
          FabricId baasId = (FabricId) field.getAnnotation(FabricId.class);
          if (baasId != null) {
              field.setAccessible(true);
              try {
                Object o = field.get(t);
                return o.toString();
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
          }
      }
      return null;
  }
}
