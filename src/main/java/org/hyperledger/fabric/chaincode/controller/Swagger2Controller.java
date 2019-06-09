package org.hyperledger.fabric.chaincode.controller;

import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.util.Json;

import java.util.Map;

import org.hyperledger.fabric.chaincode.annotation.FabricController;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.util.CustomReader;
import org.hyperledger.fabric.chaincode.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 初始化controller,获取合约中的全部controller信息.
 * 可以根据返回的json 供swagger-ui 使用，做rest风格的提交节点.
 * @author hechang
 *
 */
@DependsOn({ "autowireControllerUtil" })
@FabricController
@Component
public class Swagger2Controller implements InitializingBean {
    /**
     * .
     */
    private static Logger logger = LoggerFactory
            .getLogger(Swagger2Controller.class);

    /**
     * controller信息.
     */
    private String json;

    /**
     * controller信息.
     * 
     * @return
     */
    @FabricMapping
    public String getSwaggerInfo() {
        return json;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Info info = new Info().title("Swagger Server").version("1.1.0")
                .description("Fabric java blockchain service.").termsOfService("")
                .contact(new Contact())
              ;
        Swagger swagger = new Swagger().info(info);
        CustomReader reader = new CustomReader(swagger);

        ApplicationContext context = SpringContextUtil.getApplicationContext();
        String[] controllerNames = context
                .getBeanNamesForAnnotation(FabricController.class);
        for (String controllerName : controllerNames) {
            
            if (controllerName.equals("defaultController")
                    || controllerName.equals("queryController")) {
                continue;
            }
            
            Object bean = context.getBean(controllerName);
            Class cls = bean.getClass();
            reader.read(cls, controllerName );
        }
        try {
            Map<String, Model> m = swagger.getDefinitions();
         /*   System.out.println(m);
            System.out.println(new String(Json.pretty().writeValueAsBytes(
                    swagger.getDefinitions())));*/
            String json = new String(Json.pretty().writeValueAsBytes(
                    reader.getSwagger()));
            
            json = json.replace("$$", "");
            
            logger.info(String.format( json));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        /*
         * String infoListStr = JSON.toJSONString(infoList,
         * SerializerFeature.WriteMapNullValue);
         * System.out.println(infoListStr);
         */
    }

   
 
}
