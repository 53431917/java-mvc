package org.hyperledger.fabric.chaincode.query.model;

public class WhereClause {
    
    private String name = "WHERE";
    
    private Expression expression;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
    
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    
    	if (expression != null) {
    		sb.append(" ");
        	sb.append(name);
        	sb.append(" ");
    		sb.append(expression.toString());
    	}
        return sb.toString();
    }
    
    
    public void removeByValue(String value) {
    	if (expression != null ) {
    		if (expression.getValue() != null && 
    				("#" + value + "#").equals(expression.getValue().getValue().toString())) {
    			expression = null;
    		} else {
    			expression.removeByValue(value);
    		}
    		
    	}
    
    }
    
	public void removeByNullExpressionAndValue(){
		if (expression != null) {
            expression.removeByNullExpressionAndValue();
        }
    	if (expression != null && (expression.getValue() == null
    			&& expression.getPreExpression() == null
    			&& expression.getSuffixExpression() == null)) {
    		expression = null;   			
    			
    	} 
   
	}

    
}
