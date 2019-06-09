// Generated from CouchDB.g4 by ANTLR 4.7
package org.hyperledger.fabric.chaincode.query.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CouchDBParser}.
 */
public interface CouchDBListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperator(CouchDBParser.LogicalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperator(CouchDBParser.LogicalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(CouchDBParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(CouchDBParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#propName}.
	 * @param ctx the parse tree
	 */
	void enterPropName(CouchDBParser.PropNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#propName}.
	 * @param ctx the parse tree
	 */
	void exitPropName(CouchDBParser.PropNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#descriptionClause}.
	 * @param ctx the parse tree
	 */
	void enterDescriptionClause(CouchDBParser.DescriptionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#descriptionClause}.
	 * @param ctx the parse tree
	 */
	void exitDescriptionClause(CouchDBParser.DescriptionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#queries}.
	 * @param ctx the parse tree
	 */
	void enterQueries(CouchDBParser.QueriesContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#queries}.
	 * @param ctx the parse tree
	 */
	void exitQueries(CouchDBParser.QueriesContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(CouchDBParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(CouchDBParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#queryClause}.
	 * @param ctx the parse tree
	 */
	void enterQueryClause(CouchDBParser.QueryClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#queryClause}.
	 * @param ctx the parse tree
	 */
	void exitQueryClause(CouchDBParser.QueryClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#selectClause}.
	 * @param ctx the parse tree
	 */
	void enterSelectClause(CouchDBParser.SelectClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#selectClause}.
	 * @param ctx the parse tree
	 */
	void exitSelectClause(CouchDBParser.SelectClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#selectFields}.
	 * @param ctx the parse tree
	 */
	void enterSelectFields(CouchDBParser.SelectFieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#selectFields}.
	 * @param ctx the parse tree
	 */
	void exitSelectFields(CouchDBParser.SelectFieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(CouchDBParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(CouchDBParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(CouchDBParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(CouchDBParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(CouchDBParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(CouchDBParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(CouchDBParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(CouchDBParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(CouchDBParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(CouchDBParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrderByExpression(CouchDBParser.OrderByExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrderByExpression(CouchDBParser.OrderByExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(CouchDBParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(CouchDBParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CouchDBParser#bookmarkClause}.
	 * @param ctx the parse tree
	 */
	void enterBookmarkClause(CouchDBParser.BookmarkClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CouchDBParser#bookmarkClause}.
	 * @param ctx the parse tree
	 */
	void exitBookmarkClause(CouchDBParser.BookmarkClauseContext ctx);
}