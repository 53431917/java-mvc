package org.hyperledger.fabric.chaincode.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hyperledger.fabric.chaincode.query.CouchdbQueryParser;
import org.hyperledger.fabric.chaincode.query.IndexCompiler;
import org.hyperledger.fabric.chaincode.query.model.IndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * create index
 *
 */
public class CouchdbIndexUtil {

	 private static Logger logger = LoggerFactory
	            .getLogger(CouchdbIndexUtil.class);

	    /**
	     * 根据项目工程中的queries文件创建couchdb需要的索引文件.
	     */
	    public void creatProjecIndex(String[] args) {
	        try {
	            CouchdbQueryParser c = CouchdbQueryParser.getInstance();
	            Set<IndexModel> set = new HashSet<>();
	            c.getQueryStatementMap().forEach((k, v) -> {
	                IndexCompiler indexCompiler = new IndexCompiler(v);
	                List<IndexModel> list = indexCompiler.getAllIndexModel();
	                set.addAll(list);
	            });
	            
	            /*
	             * for(IndexModel indexModel : list) {
	             * set.add(JSON.toJSONString(indexModel)); }
	             */
//	            URL url = this.getClass().getResource(
//	                    "/META-INF/statedb/couchdb/indexes");
	            String classPath = new File(CouchdbIndexUtil.class.getResource("/").getPath()).getCanonicalPath();
	            File directory = new File(Paths.get(classPath, "..", "..","src","main","resources", "META-INF", "statedb", "couchdb", "indexes").toUri());
	            String courseFile = directory.getCanonicalPath();
//	            File directory = new File("");
//	            String courseFile = directory.getCanonicalPath()
//	                    + "\\src\\main\\resources\\META-INF\\statedb\\couchdb\\indexes";

//	            directory = new File(courseFile);
	            System.out.println(directory.getCanonicalPath());
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }

	            deletefile(courseFile);
	            for (IndexModel indexModel : set) {
	                writeIndex(courseFile, indexModel);
	                if (args != null && args.length >=2 && args[0] != null && args[1] != null) {
	                    posetIndex(args,JSON.toJSONString(indexModel));
	                }
	                System.out.println(JSON.toJSONString(indexModel));
	            }
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 删除创建的索引文件.
	     * @param delpath
	     */
	    private void deletefile(String delpath) {
	        Path dir = Paths.get(delpath);
	        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	            for (Path e : stream) {

	                if (e.getFileName().toString().lastIndexOf(".json") > 0) {
	                    Files.delete(e);
	                }

	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    }

	    /**
	     * 写入索引文件.
	     * @param path
	     * @param indexModel
	     */
	    private void writeIndex(String path, IndexModel indexModel) {

	        try {
	            BufferedWriter writer = Files.newBufferedWriter(
	                    Paths.get(path, indexModel.getName() + ".json"),
	                    StandardCharsets.UTF_8);
	            writer.write(JSON.toJSONString(indexModel));
	            writer.flush();
	            writer.close();
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }

	    }
	    
	    
	    
	    public static void posetIndex(String[] args,String jsonBody) {
	        
	        try {
	            //创建连接
	            URL url = new URL(args[0]+"/"+args[1] + "/_index");
	            HttpURLConnection connection = (HttpURLConnection) url
	                    .openConnection();
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            connection.setRequestMethod("POST");
	            connection.setUseCaches(false);
	            connection.setInstanceFollowRedirects(true);
	            connection.setRequestProperty("Content-Type",
	                    "application/json");
	 
	            connection.connect();
	 
	            //POST请求
	            DataOutputStream out = new DataOutputStream(
	                    connection.getOutputStream());
	     
	 
	            out.writeBytes(jsonBody);
	            out.flush();
	            out.close();
	 
	            //读取响应
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String lines;
	            StringBuffer sb = new StringBuffer("");
	            while ((lines = reader.readLine()) != null) {
	                lines = new String(lines.getBytes(), "utf-8");
	                sb.append(lines);
	            }
	            System.out.println(sb);
	            reader.close();
	            // 断开连接
	            connection.disconnect();
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } 
	 
	    }


	    
	    public static void main(String[] args) {
	        CouchdbIndexUtil cUtil = new CouchdbIndexUtil();
	        cUtil.creatProjecIndex(args);
	    }


}
