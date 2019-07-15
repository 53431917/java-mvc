package org.hyperledger.fabric.chaincode.query.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hyperledger.fabric.chaincode.query.Index;
import org.hyperledger.fabric.chaincode.query.model.Expression;
import org.hyperledger.fabric.chaincode.query.model.IndexModel;
import org.hyperledger.fabric.chaincode.query.model.OrderbyClause;
import org.hyperledger.fabric.chaincode.query.model.OrderbyExpression;
import org.hyperledger.fabric.chaincode.query.model.QueryStatements;


/**
 * 生成couchdb索引.CouchDB 的 "sort":[{"age": "asc"},{"id": "asc"}
 * 多个sort条件时必须全部为"asc"或"desc"
 *
 */
public class IndexCompiler implements Index {
    
    private QueryStatements queryStatment;
    
    public IndexCompiler(QueryStatements queryStatment) {
        this.queryStatment = queryStatment;
    }
    
    //全部组合索引，安装链码之前需要。
    /* (non-Javadoc)
     * @see org.hyperledger.fabric.chaincode.query.impl.Index#getAllIndexModel()
     */
    @Override
    public List<IndexModel> getAllIndexModel() {
        List<String> fieldsList = getExpressionFields();
        fieldsList .addAll(getOrderFields());
        fieldsList = fieldsList.stream().map(s -> "_" + s).distinct().collect(Collectors.toList());
        fieldsList.sort((s1,s2)-> s1.compareTo(s2));
        //根据fields组合生成多个IndexModel
        //fieldsList.add(0, "_@type");
        List<String> composeList = new ArrayList<>();
        compose(fieldsList, composeList);
        List<IndexModel> resList = new ArrayList<>();
        for (String s : composeList) {
            s = "@type" + s;
            String[] compseArray = s.split("_");
            IndexModel indexModel = createIndexModel();
            createPair(indexModel, Arrays.asList(compseArray));
            resList.add(indexModel);
        }
        return resList;
    }
    
    private IndexModel createIndexModel(){
        IndexModel indexModel = new IndexModel();
        indexModel.setType("json");
        //indexModel.createFieldPair("@type");
        return indexModel;
    }
    
    private void createPair(IndexModel indexModel, List<String> list){
        for (String s : list) {
            indexModel.createFieldPair(s);
        }
        String name =  String.join("", list);
        indexModel.setName(name);
        indexModel.setDdoc(name + "Doc");
    }
    
/*    public IndexModel getByParam(Map<String, Object> map) {
        IndexModel indexModel = createIndexModel();
        List<String> list = new ArrayList<>();
        map.forEach((k,v) -> list.add(k));
        list.sort((s1,s2) -> s1.compareTo(s2)); 
        createPair(indexModel, list);
        return indexModel;
    }
    */
    private List<String> getOrderFields() {
        List<String> resList = new ArrayList<>();
        if (queryStatment.getOrderbyClause() != null) {            
            List<OrderbyExpression> orderList =  queryStatment.getOrderbyClause().getOrderbyExList();
            for (OrderbyExpression oe : orderList) {
                resList.add(oe.getProperName());
            }
        }
        return resList;
    }
    
    private List<String> getExpressionFields() {
        List<String> resList = new ArrayList<>();
        if (queryStatment.getWhereClause() != null && queryStatment.getWhereClause().getExpression() != null) {
            Expression expression = queryStatment.getWhereClause().getExpression();
            vistExpression(expression, resList);
        }
        return resList;
    }
    
    
    private void vistExpression(Expression expression, List<String> list) {

        if (expression.getPreExpression() != null) {
             vistExpression(expression.getPreExpression(), list);
        }
        if (expression.getSuffixExpression() != null) {
             vistExpression(expression.getSuffixExpression() , list);

        }
         
        String propName = expression.getPropName();
        if (propName != null) {
            list.add(propName);
        }

}
    
    
    protected IndexModel vistIndex() {
        IndexModel indexModel = new IndexModel();
        indexModel.setType("json");
        
        indexModel.createFieldPair("@type");
        
        if (queryStatment.getWhereClause() != null && queryStatment.getWhereClause().getExpression() != null) {
            Expression expression = queryStatment.getWhereClause().getExpression();
            vistExpression(expression, indexModel);
        }
       
        if (queryStatment.getOrderbyClause() != null) {            
            visitOrderbyClause(queryStatment.getOrderbyClause(), indexModel);
        }
        
        StringBuilder nameBuilder = new StringBuilder();
      
        List<Map<String, String>> list =  indexModel.getIndex().get("fields");
        for (Map<String, String> map : list) {
            map.forEach((k, v) -> {
                nameBuilder.append(k);  
            }); 
        }
        indexModel.setName(nameBuilder.toString());
        indexModel.setDdoc(nameBuilder.toString() + "Doc");
        
  
        return indexModel;
    }
  
    
    private void vistExpression(Expression expression, IndexModel indexModel) {

        if (expression.getPreExpression() != null) {
             vistExpression(expression.getPreExpression(), indexModel);
        }
        if (expression.getSuffixExpression() != null) {
             vistExpression(expression.getSuffixExpression() , indexModel);

        }
         
        String propName = expression.getPropName();
        if (propName != null) {
            indexModel.createFieldPair(propName);
        }

}
    
    
    private void visitOrderbyClause(OrderbyClause orderbyClause, IndexModel indexModel) {
        List<OrderbyExpression> list =  orderbyClause.getOrderbyExList();
        for (OrderbyExpression oe : list) {
            indexModel.createFieldPair(oe.getProperName());
        }
    }
 

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("w2");
        list.add("w1");
        list.add("a2");
        list.add("a1");
        list.sort((s1,s2)-> s1.compareTo(s2));
        List<String> resList = new ArrayList<String>();
        compose(list, resList);
        System.out.println(" compose ");
    }
    
    private static void compose(List<String> list1, List<String> resList) {
            if (list1.isEmpty()) {
                return;
            }
            List<String> list = copy(list1);
            String first = list.remove(0);
            resList.add(first);
            one(first, list, resList);
            compose(list, resList);
            
    }
        
        /**
         * 去掉数组第一位进行递归  1,2,3,4-> 12,13,14->123,124 -> 1234
         * @param list
         */
        public  static void one (String first, List<String> pList, List<String> resList){
           if (pList.isEmpty()) {
               return;
           }
           List<String> list = copy(pList); //浅copy一下. 保留原数组长度不变.
           for (int i = 0; i < list.size(); i++) {
               resList.add(first + list.get(i));
           }
           first = first + list.remove(0);
           
           one(first, list, resList);
              
        }

        public static List<String> copy(List<String> list) {
            List<String> r = new ArrayList<>();
            for(String s : list) {
                r.add(s);
            }
            return r;
        }
    
            
}
