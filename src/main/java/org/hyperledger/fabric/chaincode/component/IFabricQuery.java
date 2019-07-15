package org.hyperledger.fabric.chaincode.component;


import java.util.List;
import java.util.Map;

public interface IFabricQuery {


    /**
     * 根据运行期query查询,不支持sort.+
     * 缓存query。
     * @param sql
     * @param paramMap
     * @param clazz
     * @return
     */
    public <T> List<T> query(String sql, Map<String, Object> paramMap,
            Class<T> clazz);


    /**
     * 根据运行期query分页查询.不支持sort.
     * 缓存query. 
     * @param sql
     * @param paramMap
     * @param pageSize
     * @param bookmark
     * @param clazz
     * @return
     */
    public <T> Map<String, Object> queryWithPagination(String sql,
            Map<String, Object> paramMap, int pageSize, String bookmark,
            Class<T> clazz);
    
    
     /**
     * 根据静态文件query语句查询.支持sort.
     * 缓存query。
     * @param sql
     * @param paramMap
     * @param clazz 返回类型
     * @return
     */
    public <T> List<T> queryByTagName(String sqlName, Map<String, Object> paramMap,
            Class<T> clazz);
    
    
    /**
     * 根据静态文件query语句分页查询,支持sort.
     * 缓存query。
     * @param sql
     * @param paramMap
     * @param clazz 返回类型
     * @return
     */
    public <T> Map<String, Object> queryByTagNameWithPagination(String sqlName,
            Map<String, Object> paramMap, Class<T> clazz);

    public <T> Map<String, Object> queryByTagNameWithPagination(String sqlName,
            Map<String, Object> paramMap, Integer size, String bookmark, Class<T> clazz);
    /**
    * 根据静态文件query查询.返回未知类型.
    * @param sqlName
    * @param paramMap
    * @return
    */
   public Object queryObjectByTagName(String sqlName, Map<String, Object> paramMap) ;



}
