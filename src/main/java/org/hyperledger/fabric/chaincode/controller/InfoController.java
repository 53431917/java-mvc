package org.hyperledger.fabric.chaincode.controller;


import java.beans.Introspector;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hyperledger.fabric.chaincode.annotation.FabricController;
import org.hyperledger.fabric.chaincode.annotation.FabricParam;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * 初始化controller,获取合约中的全部controller信息.
 *
 *
 */
//@DependsOn({ "autowireControllerUtil" })
//@BaasController
//@Component
public class InfoController implements InitializingBean {
    /**
     * .
     */
    private static Logger logger = LoggerFactory
            .getLogger(InfoController.class);

    /**
     * controller信息.
     */
    private List<Map<String, Object>> infoList = new ArrayList<>();

    /**
     * controller信息.
     * @return
     */
    @FabricMapping
    public List<Map<String, Object>> getInfo() {
        return infoList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        ApplicationContext context = SpringContextUtil.getApplicationContext();
        // String[] controllerNames =
        // context.getBeanNamesForType(DefaultController.class);
        String[] controllerNames = context
                .getBeanNamesForAnnotation(FabricController.class);

        for (String controllerName : controllerNames) {
            if ("infoController".equals(controllerName)) {
                continue;
            }
            Object handler = context.getBean(controllerName);
            Map<String, Object> map =
                    getControllerInfo(handler, controllerName);
            infoList.add(map);

        }
    /*    String infoListStr = JSON.toJSONString(infoList,
                SerializerFeature.WriteMapNullValue);
        System.out.println(infoListStr);*/
    }

    /**
     * 获取单个Controller信息.
     * @param controller
     * @param beanName
     * @return
     * @throws Exception
     */
    private Map<String, Object> getControllerInfo(final Object controller,
            final String beanName) throws Exception {
        Method[] mehtods = controller.getClass().getMethods();

        Map<String, Object> methodMap = new TreeMap<>();

        for (Method method : mehtods) {
            FabricMapping baasTrans = method.getAnnotation(FabricMapping.class);
            if (baasTrans == null) {
                continue;
            }

            String methodName = baasTrans.value();

            if ("".equals(methodName)) {
                methodName = method.getName();
            }

            Parameter[] parameters = method.getParameters();
            // 参数名和对象.
            Map<String, Object> parameterMap = new TreeMap<>();
            for (Parameter p : parameters) {

                FabricParam baasParam = p.getAnnotation(FabricParam.class);
                String parameterName = p.getName();
                if (baasParam != null) {
                    parameterName = baasParam.value();
                }
                Object object = null;
                Type type = p.getParameterizedType();
                if (DefaultController.class.getName().equals(
                        controller.getClass().getName())
                        && "java.lang.Object".equals(type.getTypeName())) {
                    DefaultController defaultController =
                                        (DefaultController) controller;
                    String entityName = defaultController.getClass().getName();
                    Class<?> entityClass = defaultController.getClass();
                    parameterName = Introspector.decapitalize(ClassUtils
                            .getShortName(entityName));
                    object = entityClass.newInstance();
                } else {
                    if (!baseType(type.getTypeName())) {
                        object = ((Class) type).newInstance();
                    }
                }

                parameterMap.put(parameterName, object);
            }
            if (!parameterMap.isEmpty()) {
                methodMap.put(beanName + "." + methodName, parameterMap);
            } else {
                methodMap.put(beanName + "." + methodName, null);
            }
        }
        return methodMap;
    }

    /**
     * 属性是否java基本类型.
     * @param className
     * @return
     */
    public boolean baseType(final String className) {
        String[] baseTypeNames = {"boolean", "byte", "char", "short", "int",
                "long", "float" };
        for (String name : baseTypeNames) {
            if (className.startsWith(name)) {
                return true;
            }
        }
        return false;
    }

}
