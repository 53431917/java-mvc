package org.hyperledger.fabric.chaincode.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FabricParam {
  
  /**
   * .
   * @return .
   */
  String value() default "";
  
  /**
   * .
   * @return .
   */
  boolean required() default false;

}
