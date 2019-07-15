package org.hyperledger.fabric.chaincode.component;


import java.util.List;

/**
 * 实体类存存储，可以注入到其它类.
 * 
 *
 */
public interface IFabricRepository {

    /**
     * 保存实体类,类中BaasId对应的属性不能为空.
     * 
     * @param t
     */
    public <T> void save(T t);

    /**
     * 根据实体类更新状态库.
     * 
     * @param t
     */
    public <T> void update(T t);

    /**
     * 根据实体类查询状态，类中BaasId对应的属性不能为空.
     * 
     * @param t
     * @return
     */
    public <T> T get(T t);

    /**
     * 根据key和实体类名查询实体.
     * 
     * @param key
     *            key是可以通过toString()方法转成字符串.
     * @param className
     *            完整的实体类名.
     * @return
     */
    public <T> T get(String key, Class<T> clazz);

    /*
     * public <T> List<T> getStateByRange(String startKey, String endKey,
     * Class<T> clazz);
     */

    /**
     * 根据实体类名称查询所有的实体.
     * 
     * @param className
     * @return
     */
    public <T> List<T> getAll(Class<T> clazz);

    /**
     * 根据实体模型中的BaasId删除状态.
     * 
     * @param t
     */
    public <T> void delete(T t);

    /**
     * 根据key和实体类名称删除状态.
     * 
     * @param t
     */
    public <T> void delete(String key, Class<T> clazz);

    /**
     * .
     * 
     * @return txID.
     */
    public String getTxId();

    /**
     * 返回通道名.
     * 
     * @return
     */
    public String getChannelId();

    /**
     * 查询历史记录.
     * 
     * @param <T>
     * @param key
     * @param className
     * @return List<String>
     */
    public <T> List<String> getHistoryForKey(String key, Class<T> clazz);

    /**
     * 保存或更新对象.
     * @param o
     */
    public <T> void saveOrUpdate(T t);

}
