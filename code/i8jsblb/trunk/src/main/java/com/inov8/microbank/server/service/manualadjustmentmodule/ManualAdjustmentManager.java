package com.inov8.microbank.server.service.manualadjustmentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.ManualAdjustmentModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

import java.util.List;

//import com.inov8.microbank.common.model.BulkManualAdjustmentModel;


public interface ManualAdjustmentManager {
	SearchBaseWrapper loadManualAdjustments( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
	BBAccountsViewModel getBBAccountsViewModel(BBAccountsViewModel model) throws FrameworkCheckedException;
	void makeOlaToOlaTransfer(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws Exception, WorkFlowException, FrameworkCheckedException;
	void makeBBToCoreTransfer(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws Exception, FrameworkCheckedException;
	void makeCoreToBBTransfer(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws Exception, FrameworkCheckedException;
	void makeOracleFinancialSettlement(ManualAdjustmentModel manualAdjustmentModel, Long actionAuthorizationId) throws Exception, FrameworkCheckedException;
	void validateTransactionCode(String trxCode) throws FrameworkCheckedException;
	public void createBulkAdjustments(List<BulkManualAdjustmentModel> dis) throws Exception;
	void pushBulkManualAdjustmentToQueue(BulkManualAdjustmentRefDataModel bulkManualAdjustmentRefDataModel) throws Exception;
	List<BulkManualAdjustmentModel> loadBulkManualAdjustmentModelList(SearchBaseWrapper searchBaseWrapper) throws Exception;
	Boolean updateBulkAdjustmentErrorMessage(Long bulkManualAdjustmentId, String errorMessage) throws Exception;

	SearchBaseWrapper loadBulkManualAdjustments( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
	String fetchCoreAccountTitle(String accNo) throws FrameworkCheckedException;
	void updateIsApprovedForBatch(Long batchId , String bulkAdjustmentId []) throws Exception;
	public String accountNumberHealthCheck(BBAccountsViewModel model, String fromTo);

	TransactionCodeModel loadTransactionCodeModelByPrimaryKey(Long transactionCodeId) throws Exception;

}
