package org.hyperledger.fabric.chaincode.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;


/**
 * 定义实体类内部ID,ID会保存为数据库的键/复合键.
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FabricField {
  /**
   * .
   * @return
   */
  boolean required() default false;
  /**
   * 如果 validRule不为空将忽略required()值.
   * @return
   */
  String rule() default "";
}
