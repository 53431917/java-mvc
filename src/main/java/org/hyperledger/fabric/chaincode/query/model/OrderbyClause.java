package org.hyperledger.fabric.chaincode.query.model;

import java.util.ArrayList;
import java.util.List;

public class OrderbyClause {

    private String name = "ORDER BY";
    
    private List<OrderbyExpression> orderbyExList = new ArrayList<>(5);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderbyExpression> getOrderbyExList() {
        return orderbyExList;
    }

    public void setOrderbyExList(List<OrderbyExpression> orderbyExList) {
        this.orderbyExList = orderbyExList;
    }
    
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" "+name + " ");
        orderbyExList.forEach( (OrderbyExpression o) -> {
           
            sb.append(o.toString());
            sb.append(", ");
        });
        if (sb.charAt(sb.length()-2) == ',') {
        	sb.deleteCharAt((sb.length() - 1));
        	sb.deleteCharAt((sb.length() - 1));
        }
        return sb.toString();
    }
    
}
