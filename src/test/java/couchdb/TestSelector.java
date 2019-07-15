package couchdb;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.query.DefaultParserHandlers;
import org.hyperledger.fabric.chaincode.query.Parser;
import org.hyperledger.fabric.chaincode.query.common.EnumSelector;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = AppMain.class)

public class TestSelector {
    
    
    @Test
    public void getNewSelector() {
        Parser c =  DefaultParserHandlers.COUCHDBPARSER.getParser();
        Map<String, Object> p = new HashMap<>();
        //p.put("beginAge", 10);
        //p.put("lastAge", 20);
        //p.put("b", true);
        //p.put("cust", "cust01");
        //p.put("bookmark", "adf");
        p.put("gid", "1");
        p.put("bookmark", "");
        p.put("limit", 10);
        
        List<String> inAgeList = new ArrayList<>();
        inAgeList.add("in1");
        inAgeList.add("in2");
        p.put("inAge",inAgeList);
        EnumMap<EnumSelector, Object> selector = c.getSelector("querContext", p);
        String str = (String) selector.get(EnumSelector.SELECTOR);
        System.out.println(str);
        System.out.println(selector.get(EnumSelector.BOOKMARK));
        System.out.println(selector.get(EnumSelector.SIZE));
        
        
        selector = c.getSelector("querContext", p);
        System.out.println(selector);
    }
	
	@Test
	public void getSelector() {
	    Parser c =  DefaultParserHandlers.COUCHDBPARSER.getParser();
		Map<String, Object> p = new HashMap<>();
		//p.put("beginAge", 10);
	    //p.put("lastAge", 20);
		//p.put("b", true);
		//p.put("cust", "cust01");
		//p.put("bookmark", "adf");
		p.put("limit", 10);
		EnumMap<EnumSelector, Object> selector = c.getSelector("querContext", p);
		String str = (String) selector.get(EnumSelector.SELECTOR);
		System.out.println(str);
		System.out.println(selector.get(EnumSelector.BOOKMARK));
		System.out.println(selector.get(EnumSelector.SIZE));
		
		
		selector = c.getSelector("querContext", p);
	}
	
	//@Test 
	public void testDynamicSql() {
	        Parser c =  DefaultParserHandlers.COUCHDBPARSER.getParser();
		   
		    StringBuilder sb = new StringBuilder();
	        sb.append("query testDynamicQuery ");
	        sb.append("FROM org.hyperledger.fabric.chaincode.test.FabricEntityTest ");
	        sb.append("WHERE id >= 30 AND id < 40 ");
	        String selector = c.getSelectorBysql(sb.toString(), new HashMap<String, Object>());
	        System.out.println(selector);
	}
	
	
	//@Test 
	public void testDynamicSqlParm() {
	       Parser c =  DefaultParserHandlers.COUCHDBPARSER.getParser();
		   
		    StringBuilder sb = new StringBuilder();
	        sb.append("query testDynamicQuery ");
	        sb.append("SELECT org.hyperledger.fabric.chaincode.test.FabricEntityTest ");
	        sb.append("WHERE id >= #id# AND f == #f# AND b == #b# ");
	        Map<String, Object> p = new HashMap<String, Object>();
	        p.put("id", 40);
	        p.put("b", true);
	        String selector = c.getSelectorBysql(sb.toString(), p);
	        System.out.println(selector);
	}

}
