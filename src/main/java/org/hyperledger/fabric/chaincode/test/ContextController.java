package org.hyperledger.fabric.chaincode.test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.chaincode.annotation.FabricController;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.annotation.FabricParam;
import org.hyperledger.fabric.chaincode.entity.PageBean;
import org.hyperledger.fabric.chaincode.service.IRepositorService;
import org.springframework.beans.factory.annotation.Autowired;

@FabricController
public class ContextController {

/*   
   peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.save","{\"context\":{\"gid\":\"1\",\"textContext\":\"textContext1\",\"cust\":\"cust1\",\"b\":true,\"age\":1 }}"]}'
    
    
    peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.save","{\"context\":{\"gid\":\"3\",\"textContext\":\"textContext6\",\"cust\":\"cust3\",\"b\":true,\"age\":3 }}"]}'
    
    peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.save","{\"context\":{\"gid\":\"5\",\"textContext\":\"textContext6\",\"cust\":\"cust5\",\"b\":true,\"age\":5 }}"]}'
    

   peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.save","{\"context\":{\"gid\":\"7\",\"textContext\":\"textContext6\",\"cust\":\"cust7\",\"b\":true,\"age\":7 }}"]}'
      
   peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.save","{\"context\":{\"gid\":\"8\",\"textContext\":\"textContext8\",\"cust\":\"cust8\",\"b\":true,\"age\":8 }}"]}'
      
   peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.save","{\"context\":{\"gid\":\"9\",\"textContext\":\"textContext9\",\"cust\":\"cust9\",\"b\":true,\"age\":9 }}"]}'
    */  
    @Autowired
    private IRepositorService<Context> contextService;
    //  peer chaincode invoke -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.save","{\"context\":{\"gid\":\"4\",\"textContext\":\"textContext4\"}}"]}'
   
    @FabricMapping
    public String save(@FabricParam(value="context") Context context) {
        contextService.save(context);
        return contextService.getTxId();
    }
    /*
      peer chaincode query -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.queryContextByPageBean","{\"pageBean\": {\"size\": 10, \"bookmark\": \"\",\"object\": {\"gid\": \"1\"} }}"]}'
     */
    @FabricMapping
    public PageBean<Context, Context> queryContextByPageBean(@FabricParam(value="pageBean", required=true )
                  PageBean<Context, Context> pageBean) {
        pageBean = contextService.queryByPageBean("querContext", pageBean);
        return pageBean;
    }
    
    
    /*
    peer chaincode query -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.queryContextAllFieldsByPageBean","{\"pageBean\": {\"size\": 10, \"bookmark\": \"\",\"object\": {\"gid\": \"1\"} }}"]}'
   */
  @FabricMapping
  public PageBean<Context, Context> queryContextAllFieldsByPageBean(@FabricParam(value="pageBean", required=true )
                PageBean<Context, Context> pageBean) {
      pageBean = contextService.queryByPageBean("querContextAllFields", pageBean);
      return pageBean;
  }
    
 // peer chaincode query -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["contextController.queryContextByIn","{\"map\":{\"inAge\":[9,3]}}"]}'
  
  @FabricMapping
  public List<Context> queryContextByIn(@FabricParam(value="map", required=true )
                Map<String, Object> map) {
      List<Context> list = contextService.queryByTagName("querContext4",map);
      return list;
  }
  
  @FabricMapping
  public List<Context> queryContext(@FabricParam(value="map", required=true )
                Map<String, Object> map) {
      List<Context> list = contextService.queryByTagName("querContext",map);
      return list;
  }
    
  
  public static void instanceActualTypeArguments(Type type) throws Exception{  
      System.out.println("该类型是"+ type);  
      // 参数化类型  
      if ( type instanceof ParameterizedType ) {  
          Type[] typeArguments = ((ParameterizedType)type).getActualTypeArguments();  
          for (int i = 0; i < typeArguments.length; i++) {  
              // 类型变量  
              if(typeArguments[i] instanceof TypeVariable){  
                  System.out.println("第" + (i+1) +  "个泛型参数类型是类型变量" + typeArguments[i] + "，无法实例化。");  
              }   
              // 通配符表达式  
              else if(typeArguments[i] instanceof WildcardType){  
                  System.out.println("第" + (i+1) +  "个泛型参数类型是通配符表达式" + typeArguments[i] + "，无法实例化。");  
              }  
              // 泛型的实际类型，即实际存在的类型  
              else if(typeArguments[i] instanceof Class){  
                  System.out.println("第" + (i+1) +  "个泛型参数类型是:" + typeArguments[i] + "，可以直接实例化对象");  
              }  
          }  
      // 参数化类型数组或类型变量数组  
      } else if ( type instanceof GenericArrayType) {  
          System.out.println("该泛型类型是参数化类型数组或类型变量数组，可以获取其原始类型。");  
          Type componentType = ((GenericArrayType)type).getGenericComponentType();  
          // 类型变量  
          if(componentType instanceof TypeVariable){  
              System.out.println("该类型变量数组的原始类型是类型变量" + componentType + "，无法实例化。");  
          }   
          // 参数化类型，参数化类型数组或类型变量数组  
          // 参数化类型数组或类型变量数组也可以是多维的数组，getGenericComponentType()方法仅仅是去掉最右边的[]  
          else {  
              // 递归调用方法自身  
              instanceActualTypeArguments(componentType);  
          }  
      } else if( type instanceof TypeVariable){  
          System.out.println("该类型是类型变量");  
      }else if( type instanceof WildcardType){  
          System.out.println("该类型是通配符表达式");  
      } else if( type instanceof Class ){  
          System.out.println("该类型不是泛型类型");  
      } else {  
          throw new Exception();  
      }  
  }  
    
    public static void main(String args[]) throws Exception{
        IRepositorService i = new ContextService();
        
        Method[] ms =  ContextService.class.getMethods();
        for(Method m : ms) {
            if(m.getName().equals("update")){
                Type[] genericParameterTypes = m.getGenericParameterTypes();  
                for (int j = 0; j < genericParameterTypes.length; j++) {  
                    System.out.println("该方法的第" + (j+1) + "个参数：");  
                    instanceActualTypeArguments(genericParameterTypes[j]);  
                }  
            }
        }
        
        

       
        Context t = new Context();
       // PageBean<Context,Context> p = new PageBean<Context, Context>();
      //  i.queryByPageBean("tat", p);
    }
    /*
            链码根据queries文件自动生成的查询
    peer chaincode query -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["querContext.query","{\"queryName\":\"querContext\",\"map\": {\"gid\": \"1\"} }"]}'
    
     peer chaincode query -o orderer.example.com:7050   -C mychannel -n test  -c '{"Args":["querContextAllFields.query","{\"queryName\":\"querContextAllFields\",\"map\": {\"gid\": \"1\",\"limit\":40,\"bookmark\":\"\"} }"]}'
  
    */

}
