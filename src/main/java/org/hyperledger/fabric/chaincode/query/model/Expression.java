package org.hyperledger.fabric.chaincode.query.model;


public class Expression {
	
	
    /**
     * 小括号.
     */
    private String preCurves;
    
    /**
     * 小括号.
     */
    private String suffixCurves;
    
    private String propName;
    
    private String compareOperator;
    
    private Value value;
    
    
    /**
     * 操作符. and、or.
     */
    private String logicOperator;
    
    private Expression preExpression;
    private Expression suffixExpression;
    public String getPreCurves() {
        return preCurves;
    }
    public void setPreCurves(String preCurves) {
        this.preCurves = preCurves;
    }
    public String getSuffixCurves() {
        return suffixCurves;
    }
    public void setSuffixCurves(String suffixCurves) {
        this.suffixCurves = suffixCurves;
    }
    public String getPropName() {
        return propName;
    }
    public void setPropName(String propName) {
        this.propName = propName;
    }
    public String getCompareOperator() {
        return compareOperator;
    }
    public void setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
    }
    public Value getValue() {
        return value;
    }
    public void setValue(Value value) {
        this.value = value;
    }
    public String getLogicOperator() {
        return logicOperator;
    }
    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }
    public Expression getPreExpression() {
        return preExpression;
    }
    public void setPreExpression(Expression preExpression) {
        this.preExpression = preExpression;
    }
    public Expression getSuffixExpression() {
        return suffixExpression;
    }
    public void setSuffixExpression(Expression suffixExpression) {
        this.suffixExpression = suffixExpression;
    }
    
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.preCurves != null) {
            sb.append(" " + this.preCurves);
        }
        
        if (this.preExpression != null) {
            sb.append(" " + this.preExpression.toString());            
        }
        
        if (this.logicOperator != null) {
            sb.append(" " + logicOperator);
        }
        
        if (this.suffixExpression != null) {
            sb.append(" " + suffixExpression.toString());
        }
        
        if (this.propName != null) {
            sb.append(" " + propName);
        }
        
        if (this.compareOperator != null) {
            sb.append(" " + this.compareOperator);
        }
        
        if (this.value != null) {
            sb.append(" " + this.value.toString());
        }
        
        if (this.suffixCurves != null) {
            sb.append(" " +this.suffixCurves);
        }
        
        return sb.toString();
    }
    
    
	public void removeByValue(String propValue) {
		// TODO Auto-generated method stub
	    
		
		if (preExpression != null) {
    		if (preExpression.getValue() != null && ("#" + propValue + "#")
    				.equals(preExpression.getValue().getValue().toString())) {
    			logicOperator = null;
    			preExpression = null;
    			
    			
    		} else {
    			preExpression.removeByValue(propValue);
    		}
    	}
		if (suffixExpression != null) {
    		if (suffixExpression.getValue() != null && ("#" + propValue + "#")
    				.equals(suffixExpression.getValue().getValue().toString())) {
    			suffixExpression = null;
    			logicOperator = null;
    		}  else {
    			suffixExpression.removeByValue(propValue);
    		}
    	}
		
	}
	
	public void removeByNullExpressionAndValue(){

		if (preExpression != null) {
    		preExpression.removeByNullExpressionAndValue();
    	}
    	if (suffixExpression != null) {
    		suffixExpression.removeByNullExpressionAndValue();
    	}
    	if ( preExpression != null && (preExpression.getValue() == null
    			&& preExpression.getPreExpression() == null
    			&& preExpression.getSuffixExpression() == null)) {
    	   preExpression = null;   			
    	   logicOperator = null;
    	} 
    	if (suffixExpression != null && (suffixExpression.getValue() == null
    			&& suffixExpression.getPreExpression() == null
    			&& suffixExpression.getSuffixExpression() == null)) {
    		  suffixExpression = null;   			
    		  logicOperator = null;
    	}
    	
	
    	
	}



}
