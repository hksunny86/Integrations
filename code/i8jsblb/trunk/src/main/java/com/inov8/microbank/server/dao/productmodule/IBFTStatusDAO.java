package com.inov8.microbank.server.dao.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.IBFTStatusModel;

public interface IBFTStatusDAO extends BaseDAO<IBFTStatusModel,Long> {
    public void DeleteFromProcessing(String stan, String reqTime)throws FrameworkCheckedException;
    public void AddToProcessing(String stan, String reqTime) throws FrameworkCheckedException;
    public boolean CheckIBFTStatus(String stan, String reqTime) throws FrameworkCheckedException;
}
