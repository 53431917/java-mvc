package org.hyperledger.fabric.chaincode.component.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hyperledger.fabric.chaincode.component.AbstractBaseRepository;
import org.hyperledger.fabric.chaincode.component.IFabricRepository;
import org.hyperledger.fabric.chaincode.entity.validate.EntityFieldValidateFactory;
import org.hyperledger.fabric.chaincode.entity.validate.IEntityFieldValidate;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * .
 * 
 * @author  
 *
 */
@Component
public class FabricRepository extends AbstractBaseRepository implements
        IFabricRepository {

    /**
     * .
     */
    public FabricRepository() {

    }

    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component
     * .IBaseRepository#save(java.lang.Object)
     */
    @Override
    public <T> void save(final T t) {
        // TODO Auto-generated method stub

        Object object = this.get(t);
        if (object != null) {
            throw new RuntimeException("保存失败，对象已存在。");
        }
        saveOrUpdate(t);
    }

    /*
     * (* (non-Javadoc)
     * @see org.hyperledger.fabric.chaincode.component
     * .IBaseRepository#update(java.lang.Object)
     */
    @Override
    public <T> void update(final T t) {
        // TODO Auto-generated method stub
        Object object = this.get(t);
        if (object == null) {
            throw new RuntimeException("更新失败，对象不存在。");
        }

        this.saveOrUpdate(t);
    }
    
    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component.
     * IBaseRepository#saveOrUpdate(java.lang.Object)
     */
    @Override
    public <T> void saveOrUpdate(T t) {
        // TODO Auto-generated method stub
        try {
            beforsave(t);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
        
        String jsonStr = JSON.toJSONString(t,SerializerFeature.WriteClassName,SerializerFeature.WriteDateUseDateFormat);
       // jsonStr = jsonStr.replace("@type", "aType");
       // StringBuilder sb = new StringBuilder(jsonStr);
        //加上$class,方便couchdb查询.
       // sb.insert(1, "\"class\":\""+t.getClass().getName()+"\",");
        ChaincodeStub stub = getChaincodeStub();
        String key = super.getBaasIdValue(t);
        CompositeKey ck = stub.createCompositeKey(t.getClass().getName(), key);
        //CompositeKey k2 = stub.splitCompositeKey(ck.toString());
        stub.putStringState(ck.toString(),jsonStr);
    }

    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component
     * .IBaseRepository#get(java.lang.Object)
     */
    @Override
    public <T> T get(final T t) {
        String key = super.getBaasIdValue(t);
        CompositeKey ck = createComposerKey(key, t.getClass());
        ChaincodeStub stub = getChaincodeStub();
        String v = stub.getStringState(ck.toString());
        if (v == null || "".equals(v.trim())) {
            return null;
        }
        T m = (T) JSON.parseObject(v, t.getClass());
        return m;
    }

    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component
     * .IBaseRepository#get(java.lang.Object, java.lang.String)
     */
    @Override
    public <T> T get(final String key, final Class<T> clazz) {
        String className = clazz.getClass().getName();
        CompositeKey ck = createComposerKey(key, clazz);
        ChaincodeStub stub = getChaincodeStub();
        String v = stub.getStringState(ck.toString());
        if (v == null || "".equals(v.trim())) {
            return null;
        }
        T t = (T) JSON.parseObject(v, clazz);
        return t;
    }



    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component
     * .IBaseRepository#getAll(java.lang.String)
     */
    @Override
    public <T> List<T> getAll(final Class<T> clazz) {
        // TODO Auto-generated method stub
        ChaincodeStub stub = getChaincodeStub();
        CompositeKey ck1 = super.createComposerKey("", clazz);
        QueryResultsIterator<KeyValue> queryResultsIterator = stub
                .getStateByPartialCompositeKey(ck1.toString());
        Iterator<KeyValue> iterator = queryResultsIterator.iterator();
        List<T> list = transKeyValue(clazz, iterator);
        return list;
    }

    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component
     * .IBaseRepository#delete(java.lang.Object)
     */
    @Override
    public <T> void delete(final T t) {
        // TODO Auto-generated method stub
        String key = super.getBaasIdValue(t);
        this.delete(key, t.getClass());
    }

    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component
     * .IBaseRepository#delete(java.lang.Object, java.lang.String)
     */
    @Override
    public <T> void delete(final String key, final Class<T> clazz) {
        CompositeKey ck = createComposerKey(key, clazz);
        super.getChaincodeStub().delState(ck.toString());
    }

    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component
     * .IBaseRepository#getHistoryForKey(java.lang.Object, java.lang.String)
     */
    @Override
    public <T> List<String> getHistoryForKey(final String key,
            final Class<T> clazz) {
        CompositeKey ck = createComposerKey(key, clazz);
        ChaincodeStub stub = getChaincodeStub();
        QueryResultsIterator<KeyModification> queryIterator = stub
                .getHistoryForKey(ck.toString());
        Iterator<KeyModification> it = queryIterator.iterator();
        List<String> list = new ArrayList<>();
        while (it.hasNext()) {
            KeyModification km = it.next();
            String v = JSON.toJSONString(km);
            list.add(v);
        }
        return list;
    }

    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component .IBaseRepository#getTxId()
     */
    @Override
    public String getTxId() {
        return getChaincodeStub().getTxId();
    }

    /*
     * (* (non-Javadoc).
     * @see org.hyperledger.fabric.chaincode.component .IBaseRepository#getChannelId()
     */
    @Override
    public String getChannelId() {
        return getChaincodeStub().getChannelId();
    }

    private <T> void beforsave(T t) throws Exception {
        // 检查t是否有@BaasEntity注解
        getClass(t.getClass().getName());
        IEntityFieldValidate entityFieldValidate = EntityFieldValidateFactory
                .getInstance().getEntityFieldValidate();
        entityFieldValidate.checkField((Object) t, false);
    }

   

}
