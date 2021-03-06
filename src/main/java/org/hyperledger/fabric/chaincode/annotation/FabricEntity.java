package org.hyperledger.fabric.chaincode.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 控制器类注解.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FabricEntity {
  /**
   * .
   * @return
   */
  String value() default "";

}
