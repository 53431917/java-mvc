package org.hyperledger.fabric.chaincode.component.impl;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.component.IFabricQuery;
import org.hyperledger.fabric.chaincode.query.DefaultParserHandlers;
import org.hyperledger.fabric.chaincode.query.Parser;
import org.hyperledger.fabric.chaincode.query.common.EnumSelector;
import org.hyperledger.fabric.chaincode.util.ThreadLocalUtils;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.QueryResultsIteratorWithMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class FabricQuery implements IFabricQuery{
    
    private static Logger logger = LoggerFactory.getLogger(FabricQuery.class);

    Parser dbQuerParser =  DefaultParserHandlers.COUCHDBPARSER.getParser();

   /**
    * .
    * @param clazz
    * @param selector
    * @return
    */
    private <T> List<T> getQueryResult(Class<T> clazz, String selector) {
        ChaincodeStub stub = (ChaincodeStub) ThreadLocalUtils.get("stub");
      
        QueryResultsIterator<KeyValue> resultIterator = stub.getQueryResult(selector);
        List<T> list = new ArrayList<>();
        Iterator<KeyValue> iterator = resultIterator.iterator();
        while (iterator.hasNext()) {
          KeyValue kv = iterator.next();
          String v = kv.getStringValue();
          T t = (T) JSON.parseObject(v, clazz);
          list.add(t);
        }
        return list;
    }
    




    private <T> Map<String, Object> getQueryResultWithPagination(int pageSize,
            String bookmark, Class<T> clazz, String selector) {
        ChaincodeStub stub = (ChaincodeStub) ThreadLocalUtils.get("stub");
        QueryResultsIteratorWithMetadata<KeyValue> resultIterator =
                stub.getQueryResultWithPagination(selector, pageSize, bookmark);
        String resultBookmark = resultIterator.getMetadata().getBookmark();
        List<T> list = new ArrayList<>();
        Iterator<KeyValue> iterator = resultIterator.iterator();
        while (iterator.hasNext()) {
          KeyValue kv = iterator.next();
          String v = kv.getStringValue();
          T t = (T) JSON.parseObject(v, clazz);
          list.add(t);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("bookmark", resultBookmark);
        map.put("result", list);
        return map;
    }

    @Override
    public <T> List<T> queryByTagName(String sqlName,
            Map<String, Object> paramMap, Class<T> clazz) {
        // TODO Auto-generated method stub
        EnumMap<EnumSelector, Object> enumMap =
                           dbQuerParser.getSelector(sqlName, paramMap);
        String selector = (String) enumMap.get(EnumSelector.SELECTOR); 
        if (logger.isInfoEnabled()) {
            logger.info("Couchdb  syntax query :\n" + selector);
        }
        List<T> list = getQueryResult(clazz, selector);
        return list;
        
    }

    @Override
    public <T> Map<String, Object> queryByTagNameWithPagination(String sqlName,
            Map<String, Object> paramMap, Class<T> clazz) {
        // TODO Auto-generated method stub
        EnumMap<EnumSelector, Object> enumMap =
                           dbQuerParser.getSelector(sqlName, paramMap);
        String selector = (String) enumMap.get(EnumSelector.SELECTOR);
        if (logger.isInfoEnabled()) {
            logger.info("Couchdb  syntax query :\n" + selector);
        }
        
        Integer size = (Integer) enumMap.get(EnumSelector.SIZE);
        if (size == null) {
            size = 10;
        }
     
        String bookmark = (String) enumMap.get(EnumSelector.BOOKMARK);
        if (bookmark == null) {
        	bookmark = "";
        }
        
        Map<String, Object> map = getQueryResultWithPagination(size,
                bookmark, clazz, selector);
        return map;
    }
    
    
    @Override
    public <T> Map<String, Object> queryByTagNameWithPagination(String sqlName,
            Map<String, Object> paramMap, Integer size, String bookmark, Class<T> clazz) {
        EnumMap<EnumSelector, Object> enumMap =
                           dbQuerParser.getSelector(sqlName, paramMap);
        String selector = (String) enumMap.get(EnumSelector.SELECTOR);
        if (logger.isInfoEnabled()) {
            logger.info("Couchdb  syntax query :\n" + selector);
        }
       if (size == null) {
           size = 10;
       }
        if (bookmark == null) {
            bookmark = "";
        }
        Map<String, Object> map = getQueryResultWithPagination(size,
                bookmark, clazz, selector);
        return map;
    }







	@Override
	public <T> List<T> query(String sql, Map<String, Object> paramMap,
			Class<T> clazz) {
		// TODO Auto-generated method stub
		String selector = dbQuerParser.getSelectorBysql(sql, paramMap);
        if (logger.isInfoEnabled()) {
            logger.info("Couchdb  syntax query :\n" + selector);
        }
        List<T> list = getQueryResult(clazz, selector);
        return list;
	}



	@Override
	public <T> Map<String, Object> queryWithPagination(String sql,
			Map<String, Object> paramMap, int pageSize, String bookmark,
			Class<T> clazz) {
		// TODO Auto-generated method stub
        String selector = dbQuerParser.getSelectorBysql(sql, paramMap);
        if (logger.isInfoEnabled()) {
            logger.info("Couchdb  syntax query :\n" + selector);
        }
        Map<String, Object> map = getQueryResultWithPagination(pageSize,
                bookmark, clazz, selector);
        return map;
	}

    public Object queryObjectByTagName(String sqlName, Map<String, Object> paramMap) {
        EnumMap<EnumSelector, Object> enumMap =
                dbQuerParser.getSelector(sqlName, paramMap);
        
        if (enumMap.get(EnumSelector.BOOKMARK) != null && enumMap.get(EnumSelector.SIZE) != null) {
            return queryByTagNameWithPagination(sqlName, paramMap, Map.class);
        } 
        
        return queryByTagName(sqlName, paramMap, Map.class);
    }

}
