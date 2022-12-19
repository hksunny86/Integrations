package com.inov8.microbank.server.service.manualadjustmentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkReversalRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.vo.transactionreversal.ManualReversalVO;

import java.io.IOException;
import java.util.List;


public interface ManualReversalManager {
	void makeReversal(ManualReversalVO manualReversalVO) throws Exception;
	TransactionDetailMasterModel getTransactionDetailMasterModel(String trxCode) throws FrameworkCheckedException;

	public void createBulkReversal(List<BulkAutoReversalModel> dis) throws IOException, FrameworkCheckedException;

	public void validateTransactionCode(String trxCode) throws FrameworkCheckedException;

	public BBAccountsViewModel getBBAccountsViewModel(BBAccountsViewModel model) throws FrameworkCheckedException;

	public String accountNumberHealthCheck(BBAccountsViewModel model, String fromTo);

	List<BulkAutoReversalModel> loadBulkReversalModelList(SearchBaseWrapper searchBaseWrapper) throws Exception;

	//Check This bulkAdjustmentId//
	void updateIsApprovedForBatch(Long batchId , String bulkAdjustmentId []) throws Exception;

	void pushBulkReversalToQueue(BulkReversalRefDataModel bulkReversalRefDataModel) throws Exception;

	SearchBaseWrapper loadBulkAutoReversals( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

}
