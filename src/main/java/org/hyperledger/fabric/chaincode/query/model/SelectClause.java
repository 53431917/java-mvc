package org.hyperledger.fabric.chaincode.query.model;


import java.util.List;



public class SelectClause {
    
    private String name = "SELECT";
    
    private boolean selectAll = false;
    
    private List<String> properNameList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public List<String> getProperNameList() {
        return properNameList;
    }

    public void setProperNameList(List<String> properNameList) {
        this.properNameList = properNameList;
    }
    
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        if (selectAll) {
            sb.append(" * ");
        } else {
           String props = String.join(",", properNameList);
           sb.append(props);
           sb.append(" ");
        }
   
        return sb.toString();
    }
    
   

}
