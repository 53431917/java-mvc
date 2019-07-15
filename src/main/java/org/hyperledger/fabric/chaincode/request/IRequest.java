package org.hyperledger.fabric.chaincode.request;


import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface IRequest {
  

  
  /**
   * .
   * @return 返回要调用的方法名称
   */
  public String getFunc(); 
  
  /**
   * .
   * @return 返回要调用的资源名称。是一个spring管理的bean的名称.
   */
  public String getResource(); 
  
  /**
   * .返回字符串参数数组.
   * @return .
   */
  public List<String> getParameters();
  
  
  /**
   * 返回JSONObject参数对象，可以为空。
   * @return .
   */
  public JSONObject getParameter();


}
