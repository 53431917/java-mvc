package org.hyperledger.fabric.chaincode.query.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class IndexModel {

    private Map<String,List<Map<String,String>>> index = new HashMap<>();
    
    {
        List<Map<String,String>> list = new ArrayList<>();
        index.put("fields", list);
    }
    
    private String name;
    
    private String ddoc;
    
    private String type;

    public Map<String, List<Map<String, String>>> getIndex() {
        return index;
    }

    public void setIndex(Map<String, List<Map<String, String>>> index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDdoc() {
        return ddoc;
    }

    public void setDdoc(String ddoc) {
        this.ddoc = ddoc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void createFieldPair(String name) {
        
        for (Map<String,String> map : getIndex().get("fields")) {
            //防重复
           if (map.containsKey(name)) {
               return;
           }
        }
        
        Map<String,String> map = new HashMap(1);
        map.put(name, "desc");
        getIndex().get("fields").add(map);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ddoc == null) ? 0 : ddoc.hashCode());
        result = prime * result + ((index == null) ? 0 : index.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IndexModel other = (IndexModel) obj;
        if (ddoc == null) {
            if (other.ddoc != null)
                return false;
        } else if (!ddoc.equals(other.ddoc))
            return false;
        if (index == null) {
            if (other.index != null)
                return false;
        } else if (!index.equals(other.index))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
    
 /*   public String getJSONString() {
        String jsonStr = JSON.toJSONString(index);
        return jsonStr;
    }
    */
    
}
