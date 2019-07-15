package org.hyperledger.fabric.chaincode.query;


import java.util.List;

import org.hyperledger.fabric.chaincode.query.model.IndexModel;

public interface Index {

    //全部组合索引，安装链码之前需要。
    public abstract List<IndexModel> getAllIndexModel();

}