package org.hyperledger.fabric.chaincode.component;


import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.shim.ChaincodeStub;

public interface IPrivateRepository {

    <T> void savePvtData(String collection, T t);


    <T> void update(String collection, T t);


    <T> void saveOrUpdate(String collection, T t);

    <T> T get(String collection, T t);

    <T> List<T> getAll(String collection, Class<T> clazz);

    <T> void delete(String collection, T t);


   
    String getTxId();

    String getChannelId();

    /**
     * 按queries文件中的查询语句查询，不支持分页
     * @param collection
     * @param query
     * @param paramMap
     * @param clazz
     * @return
     */
    <T> List<T> query(String collection, String query, Map<String, Object> paramMap, Class<T> clazz);
 
    ChaincodeStub getChaincodeStub();
}
