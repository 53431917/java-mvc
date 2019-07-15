package org.hyperledger.fabric.chaincode.entity;


import java.io.Serializable;
import java.util.List;

import org.hyperledger.fabric.chaincode.annotation.FabricField;
import org.hyperledger.fabric.chaincode.service.IRepositorService;
import org.hyperledger.fabric.chaincode.service.RepositorService;
import org.hyperledger.fabric.chaincode.test.Context;
import org.hyperledger.fabric.chaincode.test.ContextService;

public class PageBean<M, T> implements Serializable
{
    private static final long serialVersionUID = 5107501468420045382L;
    private Integer size; 
    private String bookmark;
    
    //map or javaBean
    @FabricField(required=true)
    private M object;
    private List<T> recordList;
    public Integer getSize() {
        return size;
    }
    public void setSize(Integer size) {
        this.size = size;
    }
    public String getBookmark() {
        return bookmark;
    }
    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    
  
    public M getObject() {
        return object;
    }
    public void setObject(M object) {
        this.object = object;
    }
    public List<T> getRecordList() {
        return recordList;
    }
    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    } 
    


}