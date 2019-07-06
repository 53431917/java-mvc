package org.hyperledger.fabric.chaincode.controller;

import java.util.List;

import org.hyperledger.fabric.chaincode.annotation.FabricController;
import org.hyperledger.fabric.chaincode.annotation.FabricParam;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.component.IFabricRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 默认控制器，根据Entity注解生成默认接口.
 *
 */
@FabricController
public class DefaultController {

    /**
     * .
     */
    private Class<?> entityClass;

    /**
     * .
     */
    @Autowired
    private IFabricRepository fabricRepository;

    /**
     * .
     * @param o
     */
    private void checkObject(final Object o) {
        if (!o.getClass().getName().equals(entityClass.getName())) {
            throw new RuntimeException("参数类型必须匹配"
                        + entityClass.getName() + "。");
        }
    }

    /**
     * 保存Entity.
     * @param o
     * @return .
     */
    @FabricMapping
    public String save(@FabricParam(value = "object", required = true)
                        final Object o) {
        checkObject(o);
        fabricRepository.save(o);
        return fabricRepository.getTxId();

    }

    /**
     * 删除实体.
     * @param o
     * @return .
     */
    @FabricMapping
    public String delete(@FabricParam(value = "object", required = true)
                          final Object o) {
        checkObject(o);
        fabricRepository.delete(o);
        return fabricRepository.getTxId();

    }

    /**
     * 根据key删除Entity.
     * @param key
     * @return
     */
    @FabricMapping
    public String deleteByKey(
            @FabricParam(value = "key", required = true) final String key) {
        fabricRepository.delete(key, entityClass);
        return fabricRepository.getTxId();

    }

    /**
     * 更新实体.
     * @param o
     * @return
     */
    @FabricMapping
    public String update(@FabricParam(value = "object", required = true)
                         final Object o) {
        checkObject(o);
        fabricRepository.update(o);
        return fabricRepository.getTxId();

    }
    
    
    /**
     * 保存或更新实体.
     * @param o
     * @return
     */
    @FabricMapping
    public String saveOrUpdate(@FabricParam(value = "object", required = true)
                         final Object o) {
        checkObject(o);
        fabricRepository.saveOrUpdate(o);
        return fabricRepository.getTxId();

    }

    /**
     * 根据key获取Entity.
     * @param key
     * @return
     */
    @FabricMapping
    public Object getByKey(@FabricParam(value = "key", required = true)
                            final String key) {
        Object o = fabricRepository.get(key, entityClass);
        return o;
    }

    /**
     * 根据Entity的BaasId注解获取Entity.
     * @param o
     * @return
     */
    @FabricMapping
    public Object get(@FabricParam(value = "object", required = true)
                      final Object o) {
        checkObject(o);
         Object resObject = fabricRepository.get(o);
         return resObject;
    }

    /**
     * 获取全部实体.
     * @return
     */
    @FabricMapping
    public List getAll() {
         List list = fabricRepository.getAll(entityClass);
         return list;
       // return new ArrayList<String>();
    }
    /**
     * 根据key获取历史记录.
     * @param key
     * @return
     */
    @FabricMapping
    public  List<String> getHistory(@FabricParam(value = "key", required = true)
                            final String key) {
        List<String> historyList =
                fabricRepository.getHistoryForKey(key, entityClass);
        return historyList;
    }

    /**
     * .
     * @return .
     */
    public Class<?> getEntityClass() {
        return entityClass;
    }

    /**
     * .
     * @param entityClassName.
     */
    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    /*
     * private String getClassAnnotationValue(){ BaasController baasController =
     * getClass().getAnnotation(BaasController.class); String value =
     * baasController.value(); return value; }
     */
}
