package org.hyperledger.fabric.chaincode.component.impl;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.component.AbstractBaseRepository;
import org.hyperledger.fabric.chaincode.component.IPrivateRepository;
import org.hyperledger.fabric.chaincode.entity.validate.EntityFieldValidateFactory;
import org.hyperledger.fabric.chaincode.entity.validate.IEntityFieldValidate;
import org.hyperledger.fabric.chaincode.query.DefaultParserHandlers;
import org.hyperledger.fabric.chaincode.query.Parser;
import org.hyperledger.fabric.chaincode.query.common.EnumSelector;
import org.hyperledger.fabric.chaincode.util.ThreadLocalUtils;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Component
public class PrivateRepository extends AbstractBaseRepository implements IPrivateRepository{

    private <T> boolean exsitsPvtData(final String collection, final T t) {
        boolean res = true;
        final String keys = getBaasIdValue(t);
        final CompositeKey ck = createComposerKey(keys, t.getClass());
        final ChaincodeStub stub = getChaincodeStub();
        final String v = stub.getPrivateDataUTF8(collection, ck.toString());
        if (v == null || "".equals(v.trim())) {
            res = false;
        }
        return res;
    }
    
    private <T> void beforsave(final T t) throws Exception {
        // 检查t是否有@BaasEntity注解
        getClass(t.getClass().getName());
        final IEntityFieldValidate entityFieldValidate = EntityFieldValidateFactory.getInstance()
                .getEntityFieldValidate();
        entityFieldValidate.checkField((Object) t, false);
    }
    
    
    private <T> List<T> getQueryResult(final String collection, final Class<T> clazz, final String selector) {
        final ChaincodeStub stub = (ChaincodeStub) ThreadLocalUtils.get("stub");

        final QueryResultsIterator<KeyValue> resultIterator = stub.getPrivateDataQueryResult(collection, selector);

        return getListResult(clazz, resultIterator);
    }
    
    private <T> List<T> getListResult(final Class<T> clazz, final QueryResultsIterator<KeyValue> resultIterator) {
        final List<T> list = new ArrayList<>();
        final Iterator<KeyValue> iterator = resultIterator.iterator();
        while (iterator.hasNext()) {
            final KeyValue kv = iterator.next();
            final String v = kv.getStringValue();
            final T t = (T) JSON.parseObject(v, clazz);
            list.add(t);
        }
        return list;
    }
    
    @Override
    public <T> void savePvtData(String collection, T t) {
        // TODO Auto-generated method stub
        if (exsitsPvtData(collection, t)) {
            throw new RuntimeException("Save the object failed,the object existed.");
        }
        saveOrUpdate(collection, t);
    }

    @Override
    public <T> void update(String collection, T t) {
        // TODO Auto-generated method stub
        if (!exsitsPvtData(collection, t)) {
            throw new RuntimeException( "Update the object failed,the object is not existed.");
        }
        this.saveOrUpdate(collection, t);
    }

    @Override
    public <T> void saveOrUpdate(String collection, T t) {
        // TODO Auto-generated method stub
        try {
            beforsave(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final String jsonStr = JSON.toJSONString(t, SerializerFeature.WriteClassName,
                SerializerFeature.WriteDateUseDateFormat);
  
        final ChaincodeStub stub = getChaincodeStub();
        final String keys = super.getBaasIdValue(t);
        final CompositeKey ck = super.createComposerKey(keys, t.getClass());
        stub.putPrivateData(collection, ck.toString(), jsonStr);
    }

    @Override
    public <T> T get(String collection, T t) {
        // TODO Auto-generated method stub
        final String keys = super.getBaasIdValue(t);
        final CompositeKey ck = createComposerKey(keys, t.getClass());
        final ChaincodeStub stub = getChaincodeStub();
        final String v = stub.getPrivateDataUTF8(collection, ck.toString());
        if (v == null || "".equals(v.trim())) {
            return null;
        }
        final T m = (T) JSON.parseObject(v, t.getClass());
        return m;
    }

    @Override
    public <T> List<T> getAll(String collection, Class<T> clazz) {
        // TODO Auto-generated method stub
        final ChaincodeStub stub = getChaincodeStub();
        final CompositeKey ck1 = super.createComposerKey("", clazz);
        final QueryResultsIterator<KeyValue> queryResultsIterator = stub.getPrivateDataByPartialCompositeKey(
                collection, ck1.toString());
        final Iterator<KeyValue> iterator = queryResultsIterator.iterator();
        final List<T> list = transKeyValue(clazz, iterator);
        return list;
    }

    @Override
    public <T> void delete(String collection, T t) {
        // TODO Auto-generated method stub
        final String keys = super.getBaasIdValue(t);
        final CompositeKey ck = createComposerKey(keys, t.getClass());
        super.getChaincodeStub().delPrivateData(collection, ck.toString());
    }



    @Override
    public String getTxId() {
        // TODO Auto-generated method stub
        return getChaincodeStub().getTxId();
    }

    @Override
    public String getChannelId() {
        // TODO Auto-generated method stub
        return getChaincodeStub().getChannelId();
    }

    @Override
    public <T> List<T> query(String collection, String query, Map<String, Object> paramMap, Class<T> clazz) {
        // TODO Auto-generated method stub
        final Parser dbQuerParser =  DefaultParserHandlers.COUCHDBPARSER.getParser();
        final EnumMap<EnumSelector, Object> enumMap = dbQuerParser.getSelector(query, paramMap);
        final String selector = (String) enumMap.get(EnumSelector.SELECTOR);
        final List<T> list = getQueryResult(collection, clazz, selector);
        return list;
    }

    @Override
    public ChaincodeStub getChaincodeStub() {
        // TODO Auto-generated method stub
        return getChaincodeStub();
    }

}
