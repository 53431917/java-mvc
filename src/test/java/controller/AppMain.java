package controller;

import java.util.Arrays;

import org.hyperledger.fabric.chaincode.annotation.FabricConfiguration;
import org.hyperledger.fabric.chaincode.annotation.FabricScan;
import org.hyperledger.fabric.chaincode.method.HandlerMethodMapping;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.MethodParameter;



@FabricConfiguration
@FabricScan({"controller" })
public class AppMain{

    public static void main(String args[]){
      
        AbstractApplicationContext  context = new AnnotationConfigApplicationContext(AppMain.class);
        
        HandlerMethodMapping hm = new HandlerMethodMapping();
        hm.initHandlerMethods();
        
        hm.getHandlerMethods().forEach((k,method) -> {
       
          MethodParameter[] methodParameters = method.getMethodParameters();
          Arrays.asList(methodParameters).forEach(mhp ->{
           String name =  mhp.getParameter().getName();
            System.out.println(mhp.getParameter().getName());
          });
          
        });
        
        
     /*   BaasControllerTest c3 = (BaasControllerTest) context.getBean("baasControllerTest");
        System.out.println(c3.getCust());
        
        System.out.println(c3.getService().getName());
        
        BaasEntityTest e3 = (BaasEntityTest) context.getBean("entity");
        System.out.println(e3.getAge());*/
        context.close();
    }
    
   /* @Bean
    public Entity1 getEntity1(){
      System.out.println(1);
      return new Entity1();
    }
*/
}
