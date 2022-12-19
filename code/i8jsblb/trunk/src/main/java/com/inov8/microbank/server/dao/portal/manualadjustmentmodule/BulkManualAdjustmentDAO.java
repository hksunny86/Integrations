package com.inov8.microbank.server.dao.portal.manualadjustmentmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

public interface BulkManualAdjustmentDAO extends BaseDAO<BulkManualAdjustmentModel, Long>{

	Boolean updateBulkManualAdjustment(Long bulkManualAdjustmentId, String transactionCode, String errorMessage, Boolean isProcessed, Boolean isApproved, String coreAccTitle, String fromToAccType) throws Exception;
	//void updateIsApprovedForBatch(Long batchId,String bulkAdjustmentID []) throws Exception;
		
}
