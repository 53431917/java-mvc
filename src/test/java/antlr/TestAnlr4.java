package antlr;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hyperledger.fabric.chaincode.query.Antlr4Draphic;
import org.hyperledger.fabric.chaincode.query.CouchdbQueryParser;
import org.hyperledger.fabric.chaincode.query.antlr.CouchDBParser;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.alibaba.fastjson.JSON;

public class TestAnlr4 {

	
    public void test() throws IOException {
        Resource[] resources = getResources();
        CouchdbQueryParser c = CouchdbQueryParser.getInstance();
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
    //  q.test();
    
    
        
        
    }
    
 
    

   

}
