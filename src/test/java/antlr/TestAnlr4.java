package antlr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.chaincode.query.DefaultParserHandlers;
import org.hyperledger.fabric.chaincode.query.Parser;
import org.hyperledger.fabric.chaincode.query.impl.Antlr4Draphic;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class TestAnlr4 {

	
    public void test() throws IOException {
        Resource[] resources = getResources();
        Parser c =  DefaultParserHandlers.COUCHDBPARSER.getParser();
        for (Resource r : resources) {
            BufferedReader br = Files.newBufferedReader(Paths.get(r.getURI()),
                    Charset.defaultCharset());
            StringBuilder sb = null;
            String str = null;
            while ((str = br.readLine()) != null) {
                if ("".equals(str.trim())) {
                    continue;
                }
                if (str.indexOf("query") >= 0) {
                    // 一个查询语开头
                    if (sb != null) {
                        StringReader b = new StringReader(sb.toString());
                       
                        // InputStreamReader b = new InputStreamReader
                        // (r.getInputStream());
                        c.parse(b);
                    }

                    sb = new StringBuilder();
                    sb.append(str + " ");
                } else {
                    sb.append(str +  " ");
                }

            }

            if (sb != null) {
                String sqlStr = sb.toString();
                int leftCurvesLength = sqlStr.length() - sqlStr.replace("(", "").length();
                int rightCurvesLength = sqlStr.length() - sqlStr.replace(")", "").length();
                if (leftCurvesLength != rightCurvesLength) {
                    throw new RuntimeException("( 、 )必须成对匹配。");
                }
                StringReader b = new StringReader(sqlStr);
               
                c.parse(b);
            }

        }

        // String t =
        // ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(""));
        // System.out.println(t);
    }

    
    public void testQuer() throws Exception {
        List<InputStream> list = new ArrayList<>();
        Resource[] resources = getResources();
        for (Resource r : resources) {
            list.add(r.getInputStream());
        }
        String[] arg = { "org.hyperledger.fabric.chaincode.query.antlr.CouchDB",
                "queries", "-gui" };
        Antlr4Draphic test = new Antlr4Draphic(arg, list);
        test.process();
    }
    
    
    public Resource[] getResources() throws IOException {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + '/' + "**/*.queries";
        Resource[] resources = resourcePatternResolver
                .getResources(packageSearchPath);
        return resources;
    }
    
    public static void main(String[] arg) throws Exception {
         TestAnlr4 q = new TestAnlr4();
         q.testQuer(); //用这个测试，queries文件中每一行必须空格
         //q.test();
    
    }
    
 
    

   

}
