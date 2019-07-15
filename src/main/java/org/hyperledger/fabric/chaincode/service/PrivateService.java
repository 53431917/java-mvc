package org.hyperledger.fabric.chaincode.service;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.component.IPrivateRepository;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PrivateService<T> implements IPrivateService<T> {

    private static Logger log = LoggerFactory.getLogger(PrivateService.class);
    @Autowired
    protected  IPrivateRepository privateRepository;
    
    private Class entityClazz = null;
    
    
    public PrivateService() {
        final Type type = this.getClass().getGenericSuperclass();
        final Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        this.entityClazz = (Class) params[0];
    }


    @Override
    public <T> void savePvtData(String collection, T t) {
        // TODO Auto-generated method stub
        privateRepository.savePvtData(collection, t);
        
    }


    @Override
    public <T> void update(String collection, T t) {
        // TODO Auto-generated method stub
        privateRepository.update(collection, t);
    }


    @Override
    public <T> void saveOrUpdate(String collection, T t) {
        // TODO Auto-generated method stub
        privateRepository.saveOrUpdate(collection, t);
    }


    @Override
    public <T> T get(String collection, T t) {
        // TODO Auto-generated method stub
       return privateRepository.get(collection, t);
    }


    @Override
    public <T> List<T> getAll(String collection, Class<T> clazz) {
        // TODO Auto-generated method stub
        return privateRepository.getAll(collection, clazz);
    }


    @Override
    public <T> void delete(String collection, T t) {
        // TODO Auto-generated method stub
         privateRepository.delete(collection, t);
    }


    @Override
    public String getTxId() {
        // TODO Auto-generated method stub
        return privateRepository.getTxId();
    }


    @Override
    public String getChannelId() {
        // TODO Auto-generated method stub
        return privateRepository.getChannelId();
    }


    @Override
    public <T> List<T> query(String collection, String query, Map<String, Object> paramMap, Class<T> clazz) {
        // TODO Auto-generated method stub
        return privateRepository.query(collection, query, paramMap, clazz);
    }


    @Override
    public ChaincodeStub getChaincodeStub() {
        // TODO Auto-generated method stub
        return privateRepository.getChaincodeStub();
    }


  
   
}
