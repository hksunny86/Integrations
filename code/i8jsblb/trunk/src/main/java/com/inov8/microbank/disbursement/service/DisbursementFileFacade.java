package com.inov8.microbank.disbursement.service;

import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;

/**
 * Created by AtieqRe on 2/16/2017.
 */
public interface DisbursementFileFacade {
    DisbursementFileInfoViewModel findDisbursementInfoViewModel(Long pk);

    Object getDisbursementFileSettlementStatus(String batchNumber);

    DisbursementFileInfoViewModel processBatch(Long bulkDisbursementFileInfoId) throws Exception;

    int cancelBatch(Long disbursementFileInfoId) throws Exception;

    int updateDisbursementFileStatus(Long disbursementFileInfoId, Integer status) throws Exception;

    void saveWalkInCustomers(DisbursementFileInfoViewModel infoViewModel, Long loggedInUserId) throws Exception;

    CustomList<BulkDisbursementsModel> findBulkDibursementsByBatchNumber(BulkDisbursementsModel bulkDisbursementsModel);
}
