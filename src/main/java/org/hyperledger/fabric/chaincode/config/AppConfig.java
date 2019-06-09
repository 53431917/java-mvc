package org.hyperledger.fabric.chaincode.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 注解配置类，配置文件路径.
 * @author  
 *
 */
//@Configuration
//@ComponentScan(basePackages = "com.websystique.spring")
//@PropertySource(value = { "classpath:application.properties" })
public class AppConfig {
   

     /**
     * .
     * PropertySourcesPlaceHolderConfigurer Bean only required for
     * @Value("{}") annotations.
     * Remove this bean if you are not using @Value
     * annotations for injecting properties.
     * @return .
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer
                          propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
