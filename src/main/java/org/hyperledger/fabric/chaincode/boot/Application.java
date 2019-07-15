package org.hyperledger.fabric.chaincode.boot;


import org.hyperledger.fabric.chaincode.annotation.FabricScan;
import org.hyperledger.fabric.chaincode.annotation.FabricConfiguration;
import org.hyperledger.fabric.chaincode.core.Dispatcher;
import org.hyperledger.fabric.chaincode.request.IRequest;
import org.hyperledger.fabric.chaincode.request.impl.RequestImpl;
import org.hyperledger.fabric.chaincode.util.ThreadLocalUtils;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
@FabricConfiguration
@FabricScan ({"org.hyperledger.fabric.chaincode" }) //测试用
public class Application extends ChaincodeBase{
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    @Override
    public Response init(ChaincodeStub stub) {
        // TODO Auto-generated method stub
        return newSuccessResponse();
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        ThreadLocalUtils.set("stub", stub);
        IRequest request = new RequestImpl(stub);
        Dispatcher dispatcher = Dispatcher.getInstance();
        Object object = null;
        try {
            object= dispatcher.doDispatch(request);
            if (object == null) {
                return newSuccessResponse();
            }
           
           String returnObject = JSON.toJSONString(object,
                    SerializerFeature.WriteMapNullValue);
            return newSuccessResponse("success", returnObject.getBytes());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (logger.isErrorEnabled()) {
                logger.error("Invoke chaincode failed:",e);
            }
            return newErrorResponse(e);
        }
       
    }
    
    public void start(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        Class<?> clazz = this.getClass();
        AbstractApplicationContext  context = new AnnotationConfigApplicationContext(clazz);
        super.start(args);
    }
    
    public static void main(String args[]){
       
        Application chainCodeTest = new Application();
        chainCodeTest.start(args);
       // ApplicationContext t = SpringContextUtil.getApplicationContext();
    }

}
