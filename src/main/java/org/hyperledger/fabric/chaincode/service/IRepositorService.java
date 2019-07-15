package org.hyperledger.fabric.chaincode.service;


import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.annotation.FabricService;
import org.hyperledger.fabric.chaincode.component.IFabricRepository;
import org.hyperledger.fabric.chaincode.component.impl.FabricRepository;
import org.hyperledger.fabric.chaincode.entity.PageBean;
import org.hyperledger.fabric.chaincode.test.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public  interface IRepositorService<T>    {



	  /*
     * 保存实体类,类中BaasId对应的属性不能为空.
     * 
     * @param t
     */
    public  String save(T t);

    /*
     * 根据实体类更新状态库.
     * 
     * @param t
     */
    public  String update(T t);

    /*
     * 根据实体类查询状态，类中BaasId对应的属性不能为空.
     * 
     * @param t
     * @return
     */
    public  T get(T t);
    /*
     * 根据key和实体类名查询实体.
     * 
     * @param key
     *            key是可以通过toString()方法转成字符串.
     * @param className
     *            完整的实体类名.
     * @return
     */
    public  T get(String key);

    
    
     

    /*
     * 根据实体类名称查询所有的实体.
     * 
     * @param className
     * @return
     */
    public  List<T> getAll();

    /*
     * 根据实体模型中的BaasId删除状态.
     * 
     * @param t
     */
    public  String delete(T t);

    /*
     * 根据key和实体类名称删除状态.
     * 
     * @param t
     */
    public  String delete(String key);
    /*
     * .
     * 
     * @return txID.
     */
    public String getTxId();

    /*
     * 返回通道名.
     * 
     * @return
     */
    public String getChannelId();

    /*
     * 查询历史记录.
     * 
     * @param 
     * @param key
     * @param className
     * @return List<String>
     */
    public  List<String> getHistoryForKey(String key);

    /*
     * 保存或更新对象.
     * @param o
     */
    public  void saveOrUpdate(T t);

    /**
     * 从queries文件查询，分页.
     * @param tagName
     * @param pageBean
     * @return
     */
    public <M> PageBean<M, T> queryByPageBean(String tagName, PageBean<M, T> pageBean);

    /**
     * 从queries文件查询,不分页.
     * @param tagName
     * @param paramMap
     * @return
     */
    public <T> List<T> queryByTagName(String tagName, Map<String, Object> paramMap);
}
