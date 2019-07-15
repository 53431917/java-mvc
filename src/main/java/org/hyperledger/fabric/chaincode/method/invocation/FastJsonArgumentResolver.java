package org.hyperledger.fabric.chaincode.method.invocation;


import java.beans.Introspector;
import java.lang.reflect.Type;
import java.util.Map;

import org.hyperledger.fabric.chaincode.annotation.FabricParam;
import org.hyperledger.fabric.chaincode.controller.DefaultController;
import org.hyperledger.fabric.chaincode.entity.validate.EntityFieldValidateFactory;
import org.hyperledger.fabric.chaincode.entity.validate.IEntityFieldValidate;
import org.hyperledger.fabric.chaincode.request.IRequest;
import org.hyperledger.fabric.chaincode.util.SpringContextUtil;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FastJsonArgumentResolver implements HandlerMethodArgumentResolver {
  



  private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
   // return parameter.hasParameterAnnotation(BaasParam.class);
    return true;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter,IRequest request) throws Exception {
    JSONObject j = request.getParameter();
    String parameterName = parameter.getGenericParameterType().getTypeName();
    String shortParameterName = Introspector.decapitalize(ClassUtils.getShortName(parameterName));   
    FabricParam bassParam = (FabricParam)  parameter.getParameterAnnotation(FabricParam.class);
    if (bassParam != null) {
    	if ("".equals(bassParam.value())) {
              throw new Exception("解析参数错误，FabricParam注解的value值不能为空。"+ parameter.getAnnotatedElement());
          }
    	shortParameterName = bassParam.value();
    }
   
    Type paramType = parameter.getGenericParameterType();
    //DefaultController 单独处理内置功能。取到对应的实体类型。
    String className = parameter.getMethod().getDeclaringClass().getName();
    if (DefaultController.class.getName().equals(className)
    		&& "java.lang.Object".equals(paramType.getTypeName())) {
       //  String function = request.getFunc();
        //String beanName = function.substring(0,function.lastIndexOf("."));
         String resource = request.getResource();
        // String beanName = resource.substring(0,resource.lastIndexOf("."));
         DefaultController entityObject = (DefaultController) SpringContextUtil.getBean(resource);
         String entityClassName = entityObject.getEntityClass().getName();
         paramType = Class.forName(entityClassName);
    }
    Object resultObject = null;
    if (j != null){
    	resultObject = j.getObject(shortParameterName, paramType);
    }
    if (bassParam != null && bassParam.required()) {
        if (resultObject == null) {   
            throw new Exception("Parse parameter failed，the BaasParam annotation's required  is true but the parameter is null."+ parameter.getAnnotatedElement());
         }
      //检查参数是否合规则
        IEntityFieldValidate entityFieldValidate = EntityFieldValidateFactory.getInstance().getEntityFieldValidate();
        entityFieldValidate.checkField(resultObject, false);
      }
    
    return resultObject;
  }

  public static void main(String[] args) {
    JSONObject j = JSON.parseObject(JSON_OBJ_STR);
    Map m = j.toJavaObject(Map.class);
    System.out.print(m);
  }
}
