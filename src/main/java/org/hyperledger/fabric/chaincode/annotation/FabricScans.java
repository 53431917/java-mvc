package org.hyperledger.fabric.chaincode.annotation;



import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import org.springframework.context.annotation.Import;


/**
 * .
 * @author  
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@Import(FabricScannerRegistrar.RepeatingRegistrar.class)
public @interface FabricScans {
  /**
   * .
   * @return
   */
  FabricScan[] value();
}
