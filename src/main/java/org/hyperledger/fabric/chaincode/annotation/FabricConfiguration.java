package org.hyperledger.fabric.chaincode.annotation;


import java.lang.annotation.*;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Configuration //组合@Configuration元注解
@ComponentScan ({"org.hyperledger.fabric.chaincode" }) //组合@ComponentScan元注解
public @interface FabricConfiguration {
   String[] value() default{};
}
