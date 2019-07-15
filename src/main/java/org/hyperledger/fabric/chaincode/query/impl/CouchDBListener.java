package org.hyperledger.fabric.chaincode.query.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBBaseListener;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBParser;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBParser.ExpressionContext;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBParser.PropNameContext;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBParser.SelectFieldsContext;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBParser.ValueContext;
import org.hyperledger.fabric.chaincode.query.model.BookmarkClause;
import org.hyperledger.fabric.chaincode.query.model.Expression;
import org.hyperledger.fabric.chaincode.query.model.FromClause;
import org.hyperledger.fabric.chaincode.query.model.LimitClause;
import org.hyperledger.fabric.chaincode.query.model.OrderbyClause;
import org.hyperledger.fabric.chaincode.query.model.OrderbyExpression;
import org.hyperledger.fabric.chaincode.query.model.QueryStatements;
import org.hyperledger.fabric.chaincode.query.model.SelectClause;
import org.hyperledger.fabric.chaincode.query.model.Value;
import org.hyperledger.fabric.chaincode.query.model.WhereClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CouchDBListener extends CouchDBBaseListener {
    private static Logger logger = LoggerFactory.getLogger(CouchDBListener.class);
    
    private QueryStatements queryStatment;
    
    public CouchDBListener(QueryStatements queryStatment) {
        this.queryStatment = queryStatment;
    }
    
   public void enterQueries(CouchDBParser.QueriesContext ctx) { 
        if (ctx.queryClause() == null || ctx.queryClause().getText() == null) {
        	if (logger.isErrorEnabled()) {
        		logger.error("No queries found.");
        	}
            throw new RuntimeException("No queries found.");
          
        }
        //selectStatment.setQueryName(ctx.selectStatement()..getText());
    }
   
 
   public void enterQueryClause(CouchDBParser.QueryClauseContext ctx) { 
	   if (ctx.Identifier() == null || ctx.Identifier().getText() == null) {
		   if (logger.isErrorEnabled()) {
       		logger.error("Query name can not be null.");
       	}
           throw new RuntimeException("Query name can not be null.");
       }
	   queryStatment.setQueryName( ctx.Identifier().getText());
   }
   
   
   public void enterDescriptionClause(CouchDBParser.DescriptionClauseContext ctx) { 
	   if (ctx.STRING() != null) {
		   queryStatment.setDescription(ctx.STRING().getText());
       }
   }
   
   public void enterSelectClause(CouchDBParser.SelectClauseContext ctx) { 
	   SelectClause selectClause = new SelectClause();
	   
      
	  if (ctx.selectFields() != null) {
	       
	       SelectFieldsContext sc =  ctx.selectFields();
	       if (sc == null) {
	           if (logger.isErrorEnabled()) {
	               logger.error("The fields must be followed by SELECT . ");
	           }
	           
	           throw new RuntimeException("The fields must be followed by SELECT . "); 
	       }
	       
	       List<TerminalNode> list = sc.Identifier();
	       List<String> propNameList = list.stream().map(t -> t.toString()).collect(Collectors.toList());
	       selectClause.setProperNameList(propNameList);
	   } else {
	       if (logger.isErrorEnabled()) {
               logger.error("Can't parse the SELECT . ");
           }
           
           throw new RuntimeException("Can't parse the SELECT . "); 
	   }
	
      // String desc = strList.stream().collect(Collectors.joining("  ", "", ""));
       queryStatment.setSelectClause(selectClause);
       
   }
   
   public void enterFromClause(CouchDBParser.FromClauseContext ctx) {
       FromClause fromClause = new FromClause();
     
       
      
       String str =  ctx.qualifiedName().getText();
       
       if (str == null || "".equals(str.trim())) {
           if (logger.isErrorEnabled()) {
               logger.error("The Object full name is null :" + str);
           }
           
           throw new RuntimeException("The Object full name is null :" + str);
       }
       try {
          Class.forName(str);
  
         
       } catch (ClassNotFoundException e) {
           // TODO Auto-generated catch block
           if (logger.isErrorEnabled()) {
               logger.error("Can't find the Object in query "
                             + fromClause.getName() + " : " + str);
           }
           throw new RuntimeException(str,e);
       }
       fromClause.setFullEntityName(str);
      // String desc = strList.stream().collect(Collectors.joining("  ", "", ""));
       queryStatment.setFromClause(fromClause);
   }
  
   public void enterWhereClause(CouchDBParser.WhereClauseContext ctx) { 
	   
	   WhereClause whereClause = new WhereClause();
       whereClause.setName(ctx.WHERE().getText());
       Expression  e = createExpression(ctx.expression());
       whereClause.setExpression(e);
       queryStatment.setWhereClause(whereClause);
   }
  
   
   private Expression createExpression(ExpressionContext l) {
       if (l == null) {
           return null;
       }
       Expression e = new Expression();
       
       if (l.LEFT_PAREN() != null && l.LEFT_PAREN().getText() != null) {
         e.setPreCurves(l.LEFT_PAREN().getText() );
       }
       e.setPropName(l.qualifiedName() == null ? null : l.qualifiedName().getText());
       e.setCompareOperator(l.comparisonOperator() == null ? null : l.comparisonOperator().getText());
       
       //IN 算作比较符.
       if (l.IN() != null) {
           e.setCompareOperator("IN");
       }
       
       if (l.RIGHT_PAREN() != null && l.RIGHT_PAREN().getText() != null) {
              e.setSuffixCurves(l.RIGHT_PAREN().getText());
       }
       
       

       ValueContext valueContext =  l.value();
       if (valueContext != null) {
           Value v = new Value();
           if (valueContext.STRING() !=null) {
               StringBuilder sb = new StringBuilder(valueContext.STRING().getText().trim());
               if (sb.lastIndexOf("'") == sb.length()-1 && sb.indexOf("'") == 0) {
                   sb.deleteCharAt(sb.length()-1);
                   sb.deleteCharAt(0);
                   v.setPreCharacter("'");
                   v.setSuffixCharacter("'");
               }  else {
                   v.setValue(sb.toString());
               }
              
           } else if (valueContext.TRUE() != null)  {
               v.setValue(Boolean.TRUE); 
           } else if (valueContext.FALSE() != null) {
        	   v.setValue(Boolean.FALSE); 
           } else if (valueContext.DECIMAL_VALUE() !=null) {
               v.setValue(new Double(valueContext.DECIMAL_VALUE().getText()));
           } else {
               v.setValue(new Integer(valueContext.INTEGER_VALUE().getText()));
           }
      /*    if (!valueContext.VALUEADORN().isEmpty()){
              v.setPreCharacter(valueContext.VALUEADORN(0).getText());
              v.setSuffixCharacter(valueContext.VALUEADORN(1).getText());
          }*/
          this.queryStatment.addValue(v);
          e.setValue(v); 
       }
       
       
       e.setLogicOperator(l.logicalOperator() == null ? null : l.logicalOperator().getText());
       
       //递归 Expression
       if (!l.expression().isEmpty()) {
           Expression preExpression = createExpression(l.expression(0));
           e.setPreExpression(preExpression);
           if (l.expression().size() > 1) {
               Expression suffixExpression = createExpression(l.expression(1));
               e.setSuffixExpression(suffixExpression);
           }
       }
      
       return e;
   }
    
    
    public void enterOrderByClause(CouchDBParser.OrderByClauseContext ctx) { 
    	 if (ctx == null) {
             return;
         }
         OrderbyClause orderbyClause = new OrderbyClause();
         queryStatment.setOrderbyClause(orderbyClause);
         
        
    }
    
    @Override 
    public void enterOrderByExpression(CouchDBParser.OrderByExpressionContext ctx) {
    	   OrderbyClause orderbyClause = queryStatment.getOrderbyClause();
           OrderbyExpression orderbyExpression = new OrderbyExpression();
           orderbyExpression.setProperName(ctx.propName().getText());
           
           if (ctx.STRING() != null) {
               orderbyExpression.setOrderStr(ctx.STRING().getText());
           } else {
               orderbyExpression.setOrderStr(ctx.stop.getText());
           }
          
           orderbyClause.getOrderbyExList().add(orderbyExpression);
        
    }

    public void enterLimitClause(CouchDBParser.LimitClauseContext ctx) {    
        LimitClause limitClause = new LimitClause();
        if (ctx.INTEGER_VALUE() != null) {
            limitClause.setSize(ctx.INTEGER_VALUE().getText().trim());
        } else {
            limitClause.setSize(ctx.STRING().getText().trim());
        }
       
        queryStatment.setLimitClause(limitClause);
    }
    
    
    @Override
    public void enterBookmarkClause(CouchDBParser.BookmarkClauseContext ctx) {
        BookmarkClause bookmarkClause = new BookmarkClause();
        bookmarkClause.setBookMark(ctx.STRING().getText() == null
        		? "" : ctx.STRING().getText().trim());
        queryStatment.setBookmarkClause(bookmarkClause);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void visitErrorNode(@NotNull ErrorNode node) {
          throw new RuntimeException(node.toStringTree());
    }
}
