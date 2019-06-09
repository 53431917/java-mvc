package org.hyperledger.fabric.chaincode.query;

import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.query.model.Expression;
import org.hyperledger.fabric.chaincode.query.model.IndexModel;
import org.hyperledger.fabric.chaincode.query.model.OrderbyClause;
import org.hyperledger.fabric.chaincode.query.model.OrderbyExpression;
import org.hyperledger.fabric.chaincode.query.model.QueryStatements;


/**
 * 生成couchdb索引.CouchDB 的 "sort":[{"age": "asc"},{"id": "asc"}
 * 多个sort条件时必须全部为"asc"或"desc"
 * @author hechang
 *
 */
public class IndexCompiler {
    
    private QueryStatements queryStatment;
    
    public IndexCompiler(QueryStatements queryStatment) {
        this.queryStatment = queryStatment;
    }
    
    protected IndexModel vistIndex() {
        IndexModel indexModel = new IndexModel();
        indexModel.setType("json");
        
        indexModel.createFieldPair("@type");
       
        if (queryStatment.getOrderbyClause() != null) {            
            visitOrderbyClause(queryStatment.getOrderbyClause(), indexModel);
        }
        
      StringBuilder nameBuilder = new StringBuilder();
      /*  String fullName = queryStatment.getSelectClause().getFullEntityName();
        fullName = fullName.replace(".", "-");
        nameBuilder.append(fullName);
        */
        List<Map<String, String>> list =  indexModel.getIndex().get("fields");
        for (Map<String, String> map : list) {
            map.forEach((k, v) -> {
                nameBuilder.append(k);  
            }); 
        }
        indexModel.setName(nameBuilder.toString());
        indexModel.setDdoc(nameBuilder.toString() + "Doc");
        
     /*   String name = queryStatment.getQueryName();
        indexModel.setName(name);
        indexModel.setDdoc(name + "Doc");*/
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


    
    
}
