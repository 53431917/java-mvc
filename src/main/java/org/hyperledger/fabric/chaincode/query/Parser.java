package org.hyperledger.fabric.chaincode.query;


import java.io.IOException;
import java.io.Reader;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.query.common.EnumSelector;
import org.hyperledger.fabric.chaincode.query.model.QueryStatements;

public interface Parser {

    /**
     * 获取couchdb selector语法.
     * 程序运行期编译的selector不缓存.动态查询.
     * @param queryName
     * @param paramMap 参数。
     * @return
     */
    public abstract String getSelectorBysql(String sql, Map<String, Object> paramMap);

    /**
     * 获取couchdb selector语法.
     * 根据文件中的quries语句获取couchdb语法。
     * @param queryName
     * @param paramMap 参数。
     * @return
     */
    public abstract EnumMap<EnumSelector, Object> getSelector(String queryName, Map<String, Object> paramMap);

    public abstract List<String> getAllQueries() throws IOException;


    public abstract Map<String, QueryStatements> getQueryStatementMap();
    
    public Index createIndexCompiler(QueryStatements queryStatment);
    public QueryStatements parse(Reader reader) throws IOException ;
}