package com.inov8.microbank.server.dao.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BillStatusModel;


public interface BillStatusDAO extends BaseDAO<BillStatusModel,Long> {
    public void DeleteFromProcessing(String consumerNo, String producCode)throws  FrameworkCheckedException;
    public void AddToProcessing(String consumerNo, String productCode) throws FrameworkCheckedException;
    public boolean CheckBillStatus(String consumerNo, String productCode) throws FrameworkCheckedException;
}
