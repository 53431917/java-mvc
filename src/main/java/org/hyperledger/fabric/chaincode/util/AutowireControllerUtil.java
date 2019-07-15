package org.hyperledger.fabric.chaincode.util;

import java.util.Map;

import org.hyperledger.fabric.chaincode.annotation.FabricEntity;
import org.hyperledger.fabric.chaincode.controller.DefaultController;
import org.hyperledger.fabric.chaincode.controller.QueryController;
import org.hyperledger.fabric.chaincode.query.DefaultParserHandlers;
import org.hyperledger.fabric.chaincode.query.Parser;
import org.hyperledger.fabric.chaincode.query.model.QueryStatements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * 注入默认的DefaultController和QueryController控制器.
 *
 */
@Component
public class AutowireControllerUtil implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(AutowireControllerUtil.class);
    


  /**
   * .
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    // TODO Auto-generated method stub
      autowireEntityController();
      autoWireQueriesController();
  }
  
  
  private void autoWireQueriesController() throws Exception {
     
      Parser dbQuerParser =  DefaultParserHandlers.COUCHDBPARSER.getParser();
      Map<String, QueryStatements> map = dbQuerParser.getQueryStatementMap();
      map.forEach((k,v) -> {
          autoWireQueryController(v);
      });
  }
  
  
  private void autoWireQueryController(QueryStatements s) {
      ApplicationContext context = SpringContextUtil.getApplicationContext();
      QueryController querController = new QueryController();
      
      String autowireBeanName =
              ClassUtils.getUserClass(querController).getName();
      SpringContextUtil.registerBean(s.getQueryName(), autowireBeanName);
      System.out.print("");
  }
  
  private void autowireEntityController()  throws Exception {
      ApplicationContext context = SpringContextUtil.getApplicationContext();
      String[] entityBeanNames =
              context.getBeanNamesForAnnotation(FabricEntity.class);
      for (String beaName : entityBeanNames) {
        Object object =  context.getBean(beaName);
        Class<?> entityClass = object.getClass();
        autowireController(entityClass);
        System.out.print("");
      }
  }

  /**
   * .
   * @param clazz
   * @throws Exception
   */
  private void autowireController(final Class<?> clazz) throws Exception {
    // TODO Auto-generated method stub
    String name = clazz.getName();
    DefaultController autowireController = new DefaultController();
    //autowireController.setEntityClassName(name);
    String autowireBeanName =
            ClassUtils.getUserClass(autowireController).getName();
    SpringContextUtil.registerBean(name, autowireBeanName);
    autowireController = (DefaultController) SpringContextUtil.getBean(name);
    autowireController.setEntityClass(clazz);

  }

}
