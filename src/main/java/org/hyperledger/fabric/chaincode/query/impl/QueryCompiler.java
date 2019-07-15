package org.hyperledger.fabric.chaincode.query.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.query.model.Expression;
import org.hyperledger.fabric.chaincode.query.model.FromClause;
import org.hyperledger.fabric.chaincode.query.model.OrderbyClause;
import org.hyperledger.fabric.chaincode.query.model.OrderbyExpression;
import org.hyperledger.fabric.chaincode.query.model.QueryStatements;
import org.hyperledger.fabric.chaincode.query.model.SelectClause;

public class QueryCompiler {

    private QueryStatements queryStatment;

    private Map<String, String> operatorMap = new HashMap<>();
    {
        operatorMap.put("==", "$eq");
        operatorMap.put(">=", "$gte");
        operatorMap.put(">", "$gt");
        operatorMap.put("<", "$lt");
        operatorMap.put("<=", "$lte");
        operatorMap.put("!=", "$ne");
        operatorMap.put("AND", "$and");
        operatorMap.put("OR", "$or");
        operatorMap.put("IN", "$in");

    }

    public QueryCompiler(QueryStatements queryStatment) {
        this.queryStatment = queryStatment;
    }

    protected Map<String, Object> visit() {
        
        Map<String,Object> map = new HashMap<>();
        
        Map<String,Object> selectMap = visitSelect(queryStatment.getSelectClause());
        map.putAll(selectMap);
        
        Map<String,Object> fromMap = visitFrom(queryStatment.getFromClause());
        map.putAll(fromMap);
        
        if (queryStatment.getWhereClause() != null && queryStatment.getWhereClause().getExpression() != null){
        	Expression expression = queryStatment.getWhereClause().getExpression();
            Map<String, Object> expressionMap = vistExpression(expression);
            map.putAll(expressionMap);
        }  
        
        if (queryStatment.getOrderbyClause() != null) {
        	Map<String, Object> orderbyMap = visitOrderbyClause(queryStatment.getOrderbyClause());
            map.putAll(orderbyMap);
        }
        
        
        return map;
    }
    
    private Map<String,Object> visitOrderbyClause(OrderbyClause orderbyClause) {
        Map<String, Object> map = new HashMap<>();
        if (orderbyClause != null && !orderbyClause.getOrderbyExList().isEmpty()) {
            List<Map<String,String>> orderList = new ArrayList<>();
            for (OrderbyExpression oe : orderbyClause.getOrderbyExList()) {
                Map<String,String> orderMap = new HashMap<>();
                orderMap.put(oe.getProperName(), oe.getOrderStr().toLowerCase());
                orderList.add(orderMap);
            }
            map.put("sort", orderList);
        }
        
        return map;
    }
    
    private Map<String,Object> visitSelect(SelectClause selectClause) {
        Map<String, Object> map = new HashMap<>();
        if (selectClause != null ) {
            map.put("fields", selectClause.getProperNameList());
        }
        
        return map;
    }
    
    private Map<String,Object> visitFrom(FromClause fromClause) {
        Map<String, Object> map = new HashMap<>();
        map.put("@type", fromClause.getFullEntityName());
        return map;
    }

    private Map<String, Object> vistExpression(Expression expression) {

        // 跳过(、)
        if (expression.getSuffixCurves() != null
                || expression.getPreCurves() != null) {
            if (expression.getPreExpression() != null) {
                return vistExpression(expression.getPreExpression());
            }
            if (expression.getSuffixExpression() != null) {
                return vistExpression(expression.getSuffixExpression());
            }
        }  
        
        Map<String, Object> map = new HashMap<>();

        // 叶子节点
        if (expression.getCompareOperator() != null) {
            map = visitConditionOperator(expression);
        }
        String logicOperator = expression.getLogicOperator();
        if (logicOperator != null) {
            // 有孩子节点

            if ("AND".equals(logicOperator) || "OR".equals(logicOperator)) {
                map = visitArrayCombinationOperator(expression);
            } else if ("CONTAINS".equals(logicOperator)) {
              //  map = visitContains(expression);
                throw new RuntimeException("The condition Expression only support \"AND\"、\"OR\"。");
            } else {
                throw new RuntimeException("The condition Expression only support \"AND\"、\"OR\"。");
            }

        }

        return map;

    }

    private Map<String, Object> visitArrayCombinationOperator(
            Expression expression) {
        Map<String, Object> result = null;
        Map<String, Object> leftMap = vistExpression(expression
                .getPreExpression());
        Map<String, Object> rightMap = vistExpression(expression
                .getSuffixExpression());

        if ("AND".equals(expression.getLogicOperator())) {
            result = eliminateAND(leftMap, rightMap);
        } else {
            // OR
            result = new HashMap<>();
            String operator = this.operatorMap.get(expression
                    .getLogicOperator());
            List<Map<String, Object>> valueList = new ArrayList<>();
            valueList.add(leftMap);
            valueList.add(rightMap);
            result.put(operator, valueList);
        }
        return result;
    }

    private Map<String, Object> eliminateAND(Map<String, Object> pre,
            Map<String, Object> suffix) {
        Map<String, Object> result = new HashMap<>();
        if (!((pre.containsKey("$or") || pre.containsKey("OR"))
                && (suffix.containsKey("$or") || suffix.containsKey("OR")))) {
            result.putAll(pre);

            suffix.forEach((k, v) -> {
                if (result.containsKey(k)) {
                    Map<String, Object> merged = eliminateAND(
                            (Map<String, Object>) result.get(k),
                            (Map<String, Object>) suffix.get(k));
                    result.remove(k);
                    result.put(k,merged);
                } else {
                    result.put(k, v);
                }
            });
        } else {
            List<Map<String, Object>> listValue = new ArrayList<>();
            listValue.add(pre);
            listValue.add(suffix);
            result.put("$and", listValue);
        }
        return result;
    }

    // 操作符返回。
    private Map<String, Object> visitConditionOperator(Expression expression) {
        Map<String, Object> map = new HashMap<>();
        String propName = expression.getPropName();
        String operator = expression.getCompareOperator();
        operator = this.operatorMap.get(operator);
        if (operator == null) {
            throw new RuntimeException("Thre operator only support ==、>=、>、<=、<、!= 。");

        }
        Map<String, Object> valueMap = new HashMap<>();
        Object value =  expression.getValue().getValue();
        valueMap.put(operator, value);
       
        map.put(propName, valueMap);
        return map;
    }
    
    
 // 操作符返回。
    private Map<String, Object> visitContains(Expression expression) {
        String propName = expression.getPropName();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> leftMap = vistExpression(expression
                .getPreExpression());
        Map<String, Object> rightMap = vistExpression(expression.getSuffixExpression());
        return map;
    }

   /* 
    visitContainsOperator(ast, parameters) {
        const method = 'visitContainsOperator';
        LOG.entry(method, ast, parameters);

        // Visit the left and right sides of the expression.
        let left = this.visit(ast.left, parameters);
        let right = this.visit(ast.right, parameters);

        // Grab the left hand side of this expression.
        const leftIsIdentifier = (ast.left.type === 'Identifier' && typeof left !== 'function');
        const leftIsMemberExpression = (ast.left.type === 'MemberExpression');
        const leftIsVariable = leftIsIdentifier || leftIsMemberExpression;

        // Grab the right hand side of this expression.
        const rightIsIdentifier = (ast.right.type === 'Identifier' && typeof right !== 'function');
        const rightIsMemberExpression = (ast.right.type === 'MemberExpression');
        const rightIsVariable = rightIsIdentifier || rightIsMemberExpression;

        // Ensure the arguments are valid.
        if (leftIsVariable) {
            // This is OK.
        } else if (rightIsVariable) {
            // This is OK, but swap the arguments.
            const temp = left;
            left = right;
            right = temp;
        } else {
            throw new Error(`The operator ${ast.operator} requires a property name`);
        }

        // Check to see if we have a selector, in which case this is an $elemMatch.
        let operator = '$all';
        if (!Array.isArray(right) && typeof right === 'object') {
            operator = '$elemMatch';
        }

        // We have to coerce the right hand side into an array for an $all.
        if (operator === '$all' && !Array.isArray(right)) {
            if (typeof right === 'function') {
                const originalRight = right;
                right = () => {
                    const value = originalRight();
                    if (Array.isArray(value)) {
                        return value;
                    } else {
                        return [ value ];
                    }
                };
            } else {
                right = [ right ];
            }
        }

        // Build the Mango selector for this operator.
        const result = {};
        result[left] = {};
        const property = {
            enumerable: true,
            configurable: true
        };
        if (typeof right === 'function') {
            property.get = right;
        } else {
            property.value = right;
        }
        Object.defineProperty(result[left], operator, property);

        LOG.exit(method, result);
        return result;
    }
    
    
    
    {
    "selector": {
        "\\$class": "org.acme.sample.SampleAsset",
        "\\$registryType": "Asset",
        "\\$registryId": "org.acme.sample.SampleAsset",
        "meows": {
            "$elemMatch": {
                "$or": [{
                    "woof": {
                        "$eq": "foo"
                    }
                }, {
                    "woof": {
                        "$eq": "noo"
                    }
                }]
            }
        }
    }
}



{
    "selector": {
        "\\$class": "org.acme.sample.SampleAsset",
        "\\$registryType": "Asset",
        "\\$registryId": "org.acme.sample.SampleAsset",
        "meows": {
            "$elemMatch": {
                "woof": {
                    "$eq": {
                        "$and": ["foo", "noo"]
                    }
                }
            }
        }
    }
}



  query Q19 {
            description: "Simple Historian Query"
            statement:
                SELECT org.acme.sample.SampleAsset
                    WHERE (meows CONTAINS ((woof == "foo") OR (woof == "noo")))
*/
}
