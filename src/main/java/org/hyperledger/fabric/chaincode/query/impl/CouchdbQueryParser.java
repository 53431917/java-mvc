package org.hyperledger.fabric.chaincode.query.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.hyperledger.fabric.chaincode.query.Index;
import org.hyperledger.fabric.chaincode.query.Parser;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBLexer;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBParser;
import org.hyperledger.fabric.chaincode.query.common.EnumSelector;
import org.hyperledger.fabric.chaincode.query.model.IndexModel;
import org.hyperledger.fabric.chaincode.query.model.QueryStatements;
import org.hyperledger.fabric.chaincode.query.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CouchdbQueryParser implements Parser {
    
    private static Parser couchdbQueryParser
                                     = new CouchdbQueryParser();
    
    private static Logger logger = LoggerFactory.getLogger(CouchdbQueryParser.class);
    
    
    /**
     * key:queryName, value: SelectStatment.
     */
    private Map<String, QueryStatements> queryStatementMap = new ConcurrentHashMap<>();
    
   // private Map<QueryStatements, IndexModel> queryStatementsIndexMap = new ConcurrentHashMap<>();
    
    //private Set<IndexModel> indexModelSet = new HashSet<>();
    
    

    /**
     * key: query name, value : query
     */
    private Map<String, String> queryMap = new HashMap<>();
   
    
    private CouchdbQueryParser(){
        try {
            List<String> queries = getAllQueries();
            for (String query : queries) {
                QueryStatements queryStatements  =   getQueryStatements(query);
                //IndexModel indexModel = this.getIndex(queryStatements);
                //indexModelSet.add(indexModel);
                //queryStatementsIndexMap.put(queryStatements, indexModel);
                queryStatementMap.put(queryStatements.getQueryName(), queryStatements);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            if (logger.isErrorEnabled()) {
                logger.error("Load query files error:", e);
            }
            throw new RuntimeException(e);
        }
    }
    
    
    /* (non-Javadoc)
     * @see org.hyperledger.fabric.chaincode.query.impl.Parser#getSelectorBysql(java.lang.String, java.util.Map)
     */
    @Override
    public String getSelectorBysql(String sql, Map<String, Object> paramMap) {
        QueryStatements queryStatements = this.getQueryStatements(sql + " ");
        QueryCompiler qc = new QueryCompiler(queryStatements);
        Map<String,Object> map = qc.visit();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("selector", map);
        String resultStr = JSON.toJSONString(resultMap,SerializerFeature.DisableCircularReferenceDetect);
        String selectorStr = parseSelectValue(resultStr, paramMap);
        return selectorStr;
    }
    
    
    /* (non-Javadoc)
     * @see org.hyperledger.fabric.chaincode.query.impl.Parser#getSelector(java.lang.String, java.util.Map)
     */
    @Override
    public EnumMap<EnumSelector, Object> getSelector(String queryName, Map<String, Object> paramMap) {
        
       if (paramMap == null ) {
           if (logger.isErrorEnabled()) {
               logger.error("The parameter paramMap is null or empty.");
           }
           throw new RuntimeException("The parameter paramMap is null or empty.");
       }
       
       QueryStatements queryStatements = queryStatementMap.get(queryName);
       if (queryStatements == null) {
           if (logger.isErrorEnabled()) {
               logger.error("Can't find the query statement by name :" + queryName);
           }
           throw new RuntimeException("Can't find the query statement by name :" + queryName);
       }
        Map<String,Object> resultMap = new HashMap<>();
        
           
        QueryStatements newQueryStatements = null;
        String newQueryName = this.getQueryName(queryStatements, paramMap);
        if (queryStatementMap.get(newQueryName) != null) {
            newQueryStatements = queryStatementMap.get(newQueryName);
        } else {
            //根据不为空的字段名生成一个新的qury 并且使用原来的索引
            newQueryStatements = createNewQueryStatements(queryStatements, paramMap);
        }
        QueryCompiler qc = new QueryCompiler(newQueryStatements);
        Map<String,Object> map = qc.visit();
        resultMap.put("selector", map);
        resultMap.put("sort", map.remove("sort"));
        if (map.get("fields") != null) {
            resultMap.put("fields", map.remove("fields"));
        }
        
      //添加索引
        IndexCompiler indexCompiler = new IndexCompiler(newQueryStatements);
        IndexModel indexModel = indexCompiler.vistIndex();
        List<String> list = new ArrayList<>(2);
        list.add("_design/" + indexModel.getDdoc());
        list.add(indexModel.getName());
        resultMap.put("use_index", list);
        String resultStr = JSON.toJSONString(resultMap,SerializerFeature.DisableCircularReferenceDetect);
       
        
        String selectorStr = parseSelectValue(resultStr, paramMap);
        EnumMap<EnumSelector, Object> enumMap = new EnumMap<>(EnumSelector.class);
        enumMap.put(EnumSelector.SELECTOR, selectorStr);
        
        if (newQueryStatements.getBookmarkClause() != null) {
            String bookMark = newQueryStatements.getBookmarkClause().getBookMark();
            if (bookMark.lastIndexOf("#") == bookMark.length()-1 && bookMark.indexOf("#") == 0) {
                bookMark = bookMark.substring(1, bookMark.length()-1); 
            } 
            enumMap.put(EnumSelector.BOOKMARK, paramMap.get(bookMark));
        }
        
        if (newQueryStatements.getLimitClause() != null) {
            String size = newQueryStatements.getLimitClause().getSize();
            if (size.lastIndexOf("#") == size.length()-1 && size.indexOf("#") == 0) {
                size = size.substring(1, size.length()-1);  
                enumMap.put(EnumSelector.SIZE, paramMap.get(size));
            } else {
                enumMap.put(EnumSelector.SIZE, Integer.parseInt(size));
            }
            
           
        }
  
        return enumMap;
    }
    
    private String parseSelectValue(String selectorStr,
            Map<String, Object> paramMap) {
        // TODO Auto-generated method stub

        String resStr = selectorStr;
        for (Map.Entry<String, Object> m : paramMap.entrySet()) {
            if (m.getValue()==null) {
                continue;
            }
            if (m.getValue() instanceof String) {
                resStr = resStr.replace("#" + m.getKey() + "#", m.getValue().toString());
            } else if (m.getValue() instanceof List) {
                resStr = resStr.replace("\"#" + m.getKey() + "#\"", JSON.toJSONString(m.getValue()));
            } else {
                resStr = resStr.replace("\"#" + m.getKey() + "#\"", m.getValue().toString());
            }
            
        }
        return resStr;
    }



    /**
     * 
     * @param queryStatements
     * @param paramMap 如果是参数是空 则重新命名，原名加All.以保留原文件中的query
     * @return
     */
    private String getQueryName(QueryStatements queryStatements,  Map<String, Object> paramMap) {
        List<String> list = new ArrayList<>();
        
        paramMap.forEach(( k,v) -> {
            if (v != null) {
                list.add(k);
            } 
        });
        if (paramMap.isEmpty()) {
            list.add("All");
        }
        list.sort((s1,s2)-> s1.compareTo(s2));
        list.add(0, queryStatements.getQueryName());
        String joined = list.stream().collect(Collectors.joining("", "", ""));
        return joined;
    }
    
    private QueryStatements createNewQueryStatements(
            QueryStatements queryStatements, Map<String, Object> paramMap) {
        String jsonQueryStatements = JSON.toJSONString(queryStatements,SerializerFeature.WriteClassName);
        QueryStatements newQueryStatements = JSON.parseObject(jsonQueryStatements, QueryStatements.class);
        //为新生成的Query设置新的名字
        
        
        //删除 where条件
        for (Value v : newQueryStatements.getValueList()) {
            //变量
            StringBuilder sb = new StringBuilder(v.getValue().toString());
            if (sb.lastIndexOf("#") == sb.length()-1 && sb.indexOf("#") == 0) {
                sb.deleteCharAt(sb.length()-1);
                sb.deleteCharAt(0);
                Object mapValue = paramMap.get(sb.toString());
                if (mapValue == null && newQueryStatements.getWhereClause() != null) {
                    newQueryStatements.getWhereClause().removeByValue(sb.toString());
                } 
               
               
            }
        }

        
        if (newQueryStatements.getWhereClause() != null) {
            newQueryStatements.getWhereClause().removeByNullExpressionAndValue();
        }
        
        String  newQqueryStr =  newQueryStatements.toString();
        
        System.out.println(newQqueryStr);
        newQueryStatements = this.getQueryStatements(newQqueryStr+" ");
        String queryNameBuilder = this.getQueryName(newQueryStatements, paramMap);
        newQueryStatements.setQueryName(queryNameBuilder);
        if (queryStatementMap.get(newQueryStatements.getQueryName())  != null) {
             if (logger.isErrorEnabled()) {
                   logger.error("Query statment " + newQueryStatements.getQueryName() + "is existed in cache map.");
               }
               throw new RuntimeException("Query statment " + newQueryStatements.getQueryName() + "is existed in cache map.");
        }
        queryStatementMap.put(newQueryStatements.getQueryName(), newQueryStatements);
        return newQueryStatements;
    }

    


    public static Parser getInstance(){
        return couchdbQueryParser;
    }
    
    private Resource[] getQueryResources() throws IOException {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + "/**/*.queries";
        Resource[] resources = resourcePatternResolver
                .getResources(packageSearchPath);
        return resources;
    }
    

    
   
    
    private  QueryStatements getQueryStatements(String sql) {
        StringReader b = new StringReader(sql);
        QueryStatements queryStatements = null;
        try {
            queryStatements = parse(b);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Transfer to CouchDB   syntax error:",e);
            }
            throw new RuntimeException(e);
        }
        return queryStatements;
    }
    


    /* (non-Javadoc)
     * @see org.hyperledger.fabric.chaincode.query.impl.Parser#getAllQueries()
     */
    @Override
    public List<String> getAllQueries() throws IOException {
        Resource[] resources = getQueryResources();
        List<String> allSqlList = new ArrayList<>();
        for (Resource r : resources) {
            BufferedReader br = new BufferedReader(new InputStreamReader(r.getInputStream()));
            StringBuilder sb = null;
            String str = null;
            while ((str = br.readLine()) != null) {
                if ("".equals(str.trim())) {
                    continue;
                }
                if (str.trim().indexOf("query") == 0) {
                    // 一个查询语开头
                    if (sb != null) {
                        allSqlList.add(sb.toString());
                    }

                    sb = new StringBuilder();
                    sb.append(str + " ");
                } else {
                    sb.append(str +  " ");
                }

            }

            if (sb != null) {
                String sqlStr = sb.toString();
                StringReader b = new StringReader(sqlStr);
                allSqlList.add(sb.toString());
            }
            br.close();
        }
        for(String s : allSqlList) {
            validSql(s);
        }
        return allSqlList;
    }
    
    private void validSql(String sqlStr) {
        int leftCurvesLength = sqlStr.length() - sqlStr.replace("(", "").length();
        int rightCurvesLength = sqlStr.length() - sqlStr.replace(")", "").length();
        if (leftCurvesLength != rightCurvesLength) {
            if (logger.isErrorEnabled()) {
                logger.error("Parse query error：", sqlStr);
            }
            throw new RuntimeException("\"(\" and \")\"  is always paired。");
        }
    }


    public QueryStatements parse(Reader reader) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(reader);

        CouchDBLexer lexer = new CouchDBLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CouchDBParser parse = new CouchDBParser(tokens);

        ParseTreeWalker walker = new ParseTreeWalker();


        ParseTree tree = parse.queries();
        QueryStatements queryStatements = new QueryStatements();
        CouchDBListener queryListener = new CouchDBListener(queryStatements);
        walker.walk(queryListener, tree);
       
        return queryStatements;
    }



    public Map<String, QueryStatements> getQueryStatementMap() {
        return queryStatementMap;
    }


    @Override
    public Index createIndexCompiler(QueryStatements queryStatment) {
        // TODO Auto-generated method stub
        Index i = new IndexCompiler(queryStatment);
        return  i;
    }

}
