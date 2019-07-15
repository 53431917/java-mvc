package controller;

import org.hyperledger.fabric.chaincode.util.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;



/**
 * 测试自定义扫描注入.
 * org.hyperledger.fabric.chaincode
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppMain.class)
public class AutowrieBeanTests {

  /**
   * .
   * @throws Exception 
   */
  @Test
  public void testAutowire() throws Exception {
    ApplicationContext context = SpringContextUtil.getApplicationContext();

    
    AutowireController autowireControler1 = (AutowireController) SpringContextUtil.getBean("autowireController");
    autowireControler1.autoWireTest();
    
  
     
  }
  
 
}
