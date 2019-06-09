package org.hyperledger.fabric.chaincode.request.impl;

import java.util.List;

import org.hyperledger.fabric.chaincode.query.CouchdbQueryParser;
import org.hyperledger.fabric.chaincode.request.IRequest;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 包装请求参数.
 * @author  
 *
 */
public class RequestImpl implements IRequest {
  
  private static Logger logger = LoggerFactory
      .getLogger(RequestImpl.class);
  
  private ChaincodeStub stub;
  
  private JSONObject paramObject;
  
  private String func;
  
  private String resource;
  
  public  RequestImpl(ChaincodeStub stub) {
        this.stub = stub;
        func = stub.getFunction();
        String[] funcArray = func.split("\\.");
        if (funcArray.length <= 1){
        	if (logger.isErrorEnabled()) {
                logger.error("Parse func error："+func);
            }
        	  throw new RuntimeException("Method name must be the formatter \"beanName.methodName\" .");
        }
        //func形式是 spring controller 的 beanName + "." + controller的mehtod name
        resource = func.replace("." + funcArray[funcArray.length-1], "");
        func = funcArray[funcArray.length-1];
       
        List<String> paramList = this.getParameters();
        if (!paramList.isEmpty()) {
            String param0 = paramList.get(0);
            if (param0 != null) {
              try{
              paramObject = JSONObject.parseObject(param0);
              } catch (JSONException e) {
                
                if (logger.isErrorEnabled()) {
                    logger.error("解析json串错误："+param0, e);
                }
                throw new RuntimeException("参数必须是一个json对象格式的字符串。");
                
              }
            }
        }
      
  }



  
  /**
   * @see IRequest.
   * @return.
   */
  @Override
  public String getFunc() {
     return this.func;
  }




  @Override
  public List<String> getParameters() {
    // TODO Auto-generated method stub
    return  stub.getParameters();
  }




  @Override
  public JSONObject getParameter() {
    // TODO Auto-generated method stub
    return this.paramObject;
  }




@Override
public String getResource() {
    // TODO Auto-generated method stub
    return this.resource;
}
  

  


}
