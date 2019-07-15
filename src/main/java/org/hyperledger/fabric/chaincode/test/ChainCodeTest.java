package org.hyperledger.fabric.chaincode.test;


import org.hyperledger.fabric.chaincode.boot.Application;
import org.hyperledger.fabric.chaincode.util.CouchdbIndexUtil;

public class ChainCodeTest extends Application{
    
    /*
     * .
     * 开发模式测试用。 使用步骤 1，peer node start --peer-chaincodedev=true
     * 2， 安装链码命令install链码
     * 3, debug启动本程序
     * 4,初始化链码命令初始化
     * 
     */
    public static void main(String args[]){
    	/*
    	 注意打包前如果有queries文件，需要先创建索引
    	 Before this chaincode package, create index fisrt.
    	 CouchdbIndexUtil cUtil = new CouchdbIndexUtil();
	     cUtil.creatProjecIndex(null);
	        */
    	// META-INF目录copy到src的同一级目录 ，初始化链码时才能创建索引
        String[] args2 = new String[]{"--peerAddress", "10.122.3.47:7052", "-i", "test:1.0"};
        //debug模式启动时创建索引，避免富查询语句无法使用。 couchdb url  链码文档数据库名
        final String[] couchArg = new String[]{"http://10.122.3.47:5984", "mychannel_test"};
        CouchdbIndexUtil cUtil = new CouchdbIndexUtil();
        //cUtil.creatProjecIndex(null);
        cUtil.creatProjecIndex(couchArg);
        ChainCodeTest chainCodeTest = new ChainCodeTest();
        chainCodeTest.start(args2); 
        
        
       // ApplicationContext t = SpringContextUtil.getApplicationContext();
    }
    
    

}
