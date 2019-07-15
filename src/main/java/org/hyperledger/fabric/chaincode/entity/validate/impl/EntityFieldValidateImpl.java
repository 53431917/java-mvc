package org.hyperledger.fabric.chaincode.entity.validate.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hyperledger.fabric.chaincode.annotation.FabricField;
import org.hyperledger.fabric.chaincode.annotation.FabricId;
import org.hyperledger.fabric.chaincode.entity.validate.IEntityFieldValidate;
import org.hyperledger.fabric.chaincode.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity属性校验.
 *
 */
public class EntityFieldValidateImpl implements IEntityFieldValidate {
    private static Logger logger = LoggerFactory
            .getLogger(EntityFieldValidateImpl.class);
  /*(*
   * (non-Javadoc)
   * @see org.hyperledger.fabric.chaincode.entity.validate
   * .IEntityFieldValidate#checkField(java.lang.Object, boolean)
   */
  @Override
  public void checkField(final Object t, final boolean checkBaasId)
                        throws Exception {

    // 如果是基础属性则返回
    if (t == null || baseType(t.getClass().getName())) {
      return;
    } else {
      //map list 数组
      collection(checkBaasId, t, t.getClass());

    }

  //  Field[] fields = t.getClass().getDeclaredFields();
    Field[] fields = ReflectUtil.getAllFields(t);
    int countBaasId = 0;
    for (Field field : fields) {
      field.setAccessible(true);
      Object o = field.get(t);
      if (checkBaasId) {
        // 检查BaasId注解
        FabricId baasId = (FabricId) field.getAnnotation(FabricId.class);
        if (baasId != null) {
          countBaasId++;
          // 检查BaasId注解的属性的值合法。
          checkFiledValueNull(t, field);
        }
      }

      // 检查BaasField
      FabricField baasField = (FabricField) field.getAnnotation(FabricField.class);
      if (baasField != null && baasField.required()) {
        if (baseType(field)) {
          //基本类型
          checkBaasField(t, field, baasField);
        } else {
          //复杂类型或集合类型
          Class c = field.getType();
          checkFiledValueNull(t, field);
          if (List.class.isAssignableFrom(c) || Set.class.isAssignableFrom(c)
              || Map.class.isAssignableFrom(c)) {
            //集合类型
            collection(checkBaasId, o, c);
          }
          //Bean类型
          checkField(o, checkBaasId);
        }
      }

    }
    if (checkBaasId && (countBaasId > 1 || countBaasId == 0)) {
        if (logger.isErrorEnabled()) {
            logger.error("Need a @BaasId annotation.");   
        }
      throw new RuntimeException("需要一个BaasId注解。");
    }
  }

  /**
   * 检查常用集合容器的Entity.
   * @param checkBaasId
   * @param o
   * @param c
   * @throws Exception
   */
  private void collection(final boolean checkBaasId, final Object o,
      final Class c) throws Exception {
    if (c.isArray()) {
      // 如果属性是数组

      int len = Array.getLength(o);

      for (int i = 0; i < len; i++) {
        Object objectInArray = Array.get(o, i);

        checkField(objectInArray, checkBaasId);
      }

    } else if (List.class.isAssignableFrom(c)) {
      //如果属性是List
        List list = (List) o;
        for (Object objectInList : list) {
          checkField(objectInList, checkBaasId);
        }
    } else if (Set.class.isAssignableFrom(c)) {
      //如果属性是Set
      Set set = (Set) o;
      for (Object objectInList : set) {
        checkField(objectInList, checkBaasId);
      }
    } else if (Map.class.isAssignableFrom(c)) {
      //如果属性是Map
      Map map = (Map) o;
      for (Object objectInMap: map.values()) {
        checkField(objectInMap, checkBaasId);
      }
   }
  }

  /**
   * @BaasField的required值检查.
   * @param t
   * @param field
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public void checkFiledValueNull(final Object t, final Field field)
      throws IllegalArgumentException, IllegalAccessException {
    Object o = field.get(t);
    if (o == null || "".equals(o.toString())) {
      if (logger.isErrorEnabled()) {
            logger.error(t.getClass().getName() + "." + field.getName()
                    + " property value can't be null.");   
        }
      throw new RuntimeException(t.getClass().getName() + "." + field.getName()
          + "的属性值不能为空。");
    }

  }

  /**
   * 检查Entity属性.
   * @param t
   * @param field
   * @param baasField
   * @throws Exception
   */
  public void checkBaasField(final Object t, final Field field,
               final FabricField baasField)      throws Exception {
    checkFiledValueNull(t, field);

    if (!"".equals(baasField.rule())) {
      checkvalidRule(t, field, baasField);
    }

  }

  /**
   * 检查Entity属性正则表达式校验规则.
   * @param t
   * @param field
   * @param baasField
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public void checkvalidRule(final Object t, final Field field,
      final FabricField baasField) throws Exception {
    String rule = baasField.rule();
    Object o = field.get(t);
    Pattern pattern = Pattern.compile(rule);
    // 忽略大小写的写法
    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(o.toString());
    // 字符串是否与正则表达式相匹配
    boolean rs = matcher.matches();
    if (!rs) {
        if (logger.isErrorEnabled()) {
            logger.error("Property is invalid. " +  field.getName() 
                    + " Valid rule :" + rule + ". Value:" + o.toString() );   
        }
      throw new RuntimeException("属性校验错误。属性名:" + field.getName() + "校验规则:"
          + rule + "。值:" + o.toString());
    }

  }

  /**
   * 属性是否java基本类型.
   * @param className
   * @return
   */
  public boolean baseType(final String className) {
    String[] baseTypeNames = {"boolean", "byte", "char", "short", "int",
        "long", "float", "double", "java.util.Date",
        "java.lang.String", "java.lang.Integer", "java.lang.Double",
        "java.lang.Boolean", "java.lang.Float", "java.lang.Byte",
        "java.lang.Character", "java.lang.Short", "java.lang.Long",
        "java.sql.Date", "java.sql.Timestamp", "java.sql.Time" };
    for (String name : baseTypeNames) {
      if (className.startsWith(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * .
   * @param field
   * @return
   */
  public boolean baseType(final Field field) {

    Class<?> fieldClass = field.getType();

    return baseType(fieldClass.getName());

  }
}
