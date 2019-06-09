package org.hyperledger.fabric.chaincode.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.component.IFabricQuery;
import org.hyperledger.fabric.chaincode.component.IFabricRepository;
import org.hyperledger.fabric.chaincode.entity.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RepositorService<T> implements IRepositorService<T>   {
    private static Logger log = LoggerFactory.getLogger(RepositorService.class);
	@Autowired
	protected  IFabricRepository fabricRepository;
	
	
	@Autowired
    protected  IFabricQuery fabricQuery;
	
	protected Class<?> clazz ;
	
	public RepositorService(){
	   // System.out.println(type[0].getClass().getName());
	    Type t = this.getClass().getGenericSuperclass();
        ParameterizedType pType= (ParameterizedType) t;//ParameterizedType是Type的子接口
        Type[] types = pType.getActualTypeArguments();
        clazz = (Class<?>) types[0];
	}

	public IFabricRepository getFabricRepository() {
		return fabricRepository;
	}

	public void setFabricRepository(IFabricRepository fabricRepository) {
		this.fabricRepository = fabricRepository;
	}
	
	
	  /**
     * 保存实体类,类中BaasId对应的属性不能为空.
     * 
     * @param t
     */
    public  String save(T t) {
        fabricRepository.save(t);
        return fabricRepository.getTxId();
    }

    /**
     * 根据实体类更新状态库.
     * 
     * @param t
     */
    public  String update(T t) {
        fabricRepository.update(t);
        return fabricRepository.getTxId();
    }

    /**
     * 根据实体类查询状态，类中BaasId对应的属性不能为空.
     * 
     * @param t
     * @return
     */
    public  T get(T t) {
        return fabricRepository.get(t);
    }

    /**
     * 根据key和实体类名查询实体.
     * 
     * @param key
     *            key是可以通过toString()方法转成字符串.
     * @param className
     *            完整的实体类名.
     * @return
     */
    public  T get(String key) {
        return (T) fabricRepository.get(key, clazz);
    }

    
    
     

    /**
     * 根据实体类名称查询所有的实体.
     * 
     * @param className
     * @return
     */
    public  List<T> getAll() {
        return (List<T>) fabricRepository.getAll(clazz);
    }

    /**
     * 根据实体模型中的BaasId删除状态.
     * 
     * @param t
     */
    public  String delete(T t) {
        fabricRepository.delete(t);
        return fabricRepository.getTxId();
    }

    /**
     * 根据key和实体类名称删除状态.
     * 
     * @param t
     */
    public  String delete(String key) {
        fabricRepository.delete(key, clazz);
        return fabricRepository.getTxId();
    }

    /**
     * .
     * 
     * @return txID.
     */
    public String getTxId() {
        return fabricRepository.getTxId();
    }

    /**
     * 返回通道名.
     * 
     * @return
     */
    public String getChannelId() {
        return fabricRepository.getChannelId();
    }

    /**
     * 查询历史记录.
     * 
     * @param 
     * @param key
     * @param className
     * @return List<String>
     */
    public  List<String> getHistoryForKey(String key) {
        return fabricRepository.getHistoryForKey(key, clazz);
    }

    /**
     * 保存或更新对象.
     * @param o
     */
    public  void saveOrUpdate(T t) {
         fabricRepository.saveOrUpdate(t);
    }

    /*(*
     * (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.service.IRepositorService#queryByPageBean(java.lang.String, org.hyperledger.fabric.chaincode.entity.PageBean)
     */
    public <M> PageBean<M, T> queryByPageBean(String tagName, PageBean<M, T> pageBean) {
        
        //Type 是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
        // TODO Auto-generated method stub
        Object o = pageBean.getObject();
        Map paramMap = null;
        if (o instanceof Map) {
            paramMap = (Map) o;
        } else {
            paramMap = transBean2Map(o);
        }
        
        
        Map<String, Object> resMap = fabricQuery.queryByTagNameWithPagination(tagName, paramMap,pageBean.getSize(), pageBean.getBookmark(), clazz);
             
        pageBean.setBookmark((String) resMap.get("bookmark"));
        pageBean.setRecordList((List<T>) resMap.get("result"));
        return pageBean;
    }
    
    /*(*
     * (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.service.IRepositorService#queryByTagName(java.lang.String, java.util.Map)
     */
    public <T> List<T> queryByTagName(String tagName, Map<String, Object> paramMap) {
        return (List<T>) fabricQuery.queryByTagName(tagName, paramMap, clazz);
    }
    
    
    protected static Map<String, Object> transBean2Map(Object obj) {

        if(obj == null){
            return null;
        }        
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            log.error("Trans bean to map error:", e);
            throw new RuntimeException(e);
        }

        return map;

    }
    

}
