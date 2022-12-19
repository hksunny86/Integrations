package com.inov8.microbank.server.dao.safrepo;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.VCFileModel;
import com.inov8.microbank.common.model.WalletSafRepoModel;

import java.util.Date;
import java.util.List;

public interface VCFileDAO extends BaseDAO<VCFileModel,Long> {
    List<VCFileModel> loadAllToBeProcessedRecords(String transactionType) throws FrameworkCheckedException;
    void updateVCFileModel(VCFileModel model) throws FrameworkCheckedException;
    public Double getTotalBalance(String transactionType)throws Exception;

}
