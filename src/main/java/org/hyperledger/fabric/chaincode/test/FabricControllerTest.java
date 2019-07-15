package org.hyperledger.fabric.chaincode.test;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.annotation.FabricController;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.annotation.FabricParam;
import org.hyperledger.fabric.chaincode.component.IFabricQuery;
import org.hyperledger.fabric.chaincode.component.IFabricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@FabricController(value="controller")
public class FabricControllerTest {  
    private static Logger log = LoggerFactory.getLogger(FabricControllerTest.class);
    @Value("${jdbc.username}") 
    private String cust;

    private  int age ;
    
    @Autowired
    private IFabricRepository baseRepository;
    
    @Autowired
    private IFabricQuery query;
    
    @Autowired
    private FabricServiceTest fabricServiceTest;

    
    
    public FabricControllerTest(){

    }

    @FabricMapping(method="query")
    public String paramTest(@FabricParam(value="map1") Map<String, Object> map1){
        log.info("asdfasdf");
        return this.cust;
    }
    
  @FabricMapping
    public List<FabricEntityTest> paramTest2(@FabricParam(value="name") List<FabricEntityTest> list){
       // log.info(name);
        log.info("asdfasdf");
        return list;
    }
    
    
    @FabricMapping
    public String paramTest3(FabricEntityTest baasEntityTest,
            @FabricParam(value="baasEntityTest2") FabricEntityTest baasEntityTest2){
       // log.info(name);
        log.info("asdfasdf");
        return this.cust;
    }
    
    

    
    
    

    
  @FabricMapping
    public String getCust(){
        log.info("getCust was invoked...");
        return this.cust;
    }
    
    
    @FabricMapping
    public String testIntParma(@FabricParam(value="t") int t){
        log.info("testIntParma was invoked:" + t);
        return this.cust;
    }
    
    
    /*
    invoke this method
    peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test     -c '{"Args":["controller.testBooleanParam","{\"t\":true}"]}'
    
    */
    @FabricMapping
    public String testBooleanParam(@FabricParam(value="t") boolean t){
        log.info("testBooleanParam was invoked:" + t);
        return this.cust;
    }
    
    /*
    invoke this method
    peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n chaincodetest 
    -c '{"Args":["controller.saveBaasEntityTest"]}'
    
    */
    @FabricMapping
    public String saveBaasEntityTest(){
    
        for( int i = 0; i < 100 ; i++) {
            FabricEntityTest bt = new FabricEntityTest();
            bt.setCust("cust"+i);
            bt.setAge(i);
            bt.setId(i);
            bt.setB(i%2 == 0);
            baseRepository.save(bt);
        }
        return this.cust;
    }
    
    
    @FabricMapping(method="get")
    public List<FabricEntityTest>  queryAll(){
        List<FabricEntityTest> list = baseRepository.getAll(FabricEntityTest.class);
        return list;
    }
    
    @FabricMapping(method="get")
    public List<FabricEntityTest>  testDynamicQuery1(){
        StringBuilder sb = new StringBuilder();
        sb.append("query testDynamicQuery ");
        sb.append("SELECT org.hyperledger.fabric.chaincode.test.FabricEntityTest ");
        sb.append("WHERE id >= 35 AND id < 40 ");
        List<FabricEntityTest> list = query.query(sb.toString(), new HashMap<String, Object>(),FabricEntityTest.class);
        return list;
    }
    
  
   /*
    invoke this method
    peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["controller.testDynamicQueryWithPagination","{\"map\":{\"beginAge\":0,\"bookmark\":\"\",\"b\":true,\"lastAge\":10,\"limit\":5}}"]}'
    
    
    */
    @FabricMapping(method="get")
    public Map<String, Object>  testDynamicQueryWithPagination(){
        StringBuilder sb = new StringBuilder();
        sb.append("query testDynamicQuery ");
        sb.append("SELECT org.hyperledger.fabric.chaincode.test.FabricEntityTest ");
        sb.append("WHERE id >=30 AND id < 40 ");
        Map<String, Object> map = query.queryWithPagination(sb.toString(),new HashMap<String, Object>(),2,"",FabricEntityTest.class);
        return map;
    }
    
    @FabricMapping(method="get")
    public Map<String, Object>  testQueryByTagNameWithPagination(
    		@FabricParam(value="map", required = true) Map<String, Object> map){
        
        Map<String, Object> resMap = query.queryByTagNameWithPagination("testDynamicQuery", map, FabricEntityTest.class);
        return resMap;
    }
    


    
    public void say(){
        System.out.println("Say...");
    }
}  