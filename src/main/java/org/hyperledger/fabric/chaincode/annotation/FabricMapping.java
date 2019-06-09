package org.hyperledger.fabric.chaincode.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;


/**
 * 控制器内的方法定义注解。有该注解的方法才可以被调用.
 * @author hechang
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FabricMapping {
  /**
   * .
   * @return
   */
  String value() default "";
  
  /**
   * 取值post、delete、put、get.指出该方法应该是用query(get)调用还是invoke(其它)调用.
   * @return
   */
  String method() default "";
  
  
  
  
}
