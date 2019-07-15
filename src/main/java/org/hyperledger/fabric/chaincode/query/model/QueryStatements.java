package org.hyperledger.fabric.chaincode.query.model;


import java.util.ArrayList;
import java.util.List;

public class QueryStatements {
    
    private String queryName;
    
    private String description;
    
    private SelectClause selectClause;
    
    private FromClause fromClause;
    
    private WhereClause whereClause;
    
    private OrderbyClause orderbyClause;
    
    private LimitClause limitClause;
    
    private BookmarkClause bookmarkClause;
    
    private List<Value> valueList = new ArrayList<>();
 
    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectClause getSelectClause() {
        return selectClause;
    }

    public void setSelectClause(SelectClause selectClause) {
        this.selectClause = selectClause;
    }

    
    
    public FromClause getFromClause() {
        return fromClause;
    }

    public void setFromClause(FromClause fromClause) {
        this.fromClause = fromClause;
    }

    public WhereClause getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(WhereClause whereClause) {
        this.whereClause = whereClause;
    }

    public OrderbyClause getOrderbyClause() {
        return orderbyClause;
    }

    public void setOrderbyClause(OrderbyClause orderbyClause) {
        this.orderbyClause = orderbyClause;
    }

    public LimitClause getLimitClause() {
        return limitClause;
    }

    public void setLimitClause(LimitClause limitClause) {
        this.limitClause = limitClause;
    }

    public BookmarkClause getBookmarkClause() {
        return bookmarkClause;
    }

    public void setBookmarkClause(BookmarkClause bookmarkClause) {
        this.bookmarkClause = bookmarkClause;
    }
    
    public void addValue(Value v) {
        this.valueList.add(v);
    }
    
    
    public List<Value> getValueList() {
        return valueList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (queryName !=null) {
        	sb.append("query ");
        	sb.append(queryName);
        	sb.append(" ");
        }
        
        if (selectClause != null) {
            sb.append(selectClause.toString());
        }       
        
        sb.append(fromClause.toString());
        
        if (whereClause != null) {
            sb.append(whereClause.toString());
        }
        if (orderbyClause !=null) {
            sb.append(orderbyClause.toString());
        }
        
        if (limitClause != null) {
            sb.append(limitClause.toString());
        }
        
        if (bookmarkClause != null) {
            sb.append(bookmarkClause.toString());
        }
        
        return sb.toString();
    }

}
