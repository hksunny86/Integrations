package com.inov8.microbank.disbursement.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;

public interface BulkDisbursementsFileInfoDAO extends BaseDAO<BulkDisbursementsFileInfoModel, Long> {

    int updateDisbursementFileStatus(Long fileInfoId, Integer status);

    Object getDisbursementFileSettlementStatus(String batchNumber);

    int cancelBatch(Long disbursementFileInfoId);

    public int updateDisbursementFileStatusAndApprove(String batchNumber, Integer status, String isApproved) throws FrameworkCheckedException;

}