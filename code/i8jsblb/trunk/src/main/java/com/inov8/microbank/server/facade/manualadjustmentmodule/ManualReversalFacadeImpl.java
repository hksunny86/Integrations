package com.inov8.microbank.server.facade.manualadjustmentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkReversalRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.vo.transactionreversal.ManualReversalVO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualReversalManager;

import java.io.IOException;
import java.util.List;


public class ManualReversalFacadeImpl implements ManualReversalManager{

	private ManualReversalManager manualReversalManager;
	private ManualAdjustmentManager manualAdjustmentManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
		
	
	@Override
	public void makeReversal(ManualReversalVO manualReversalVO) throws Exception, WorkFlowException, FrameworkCheckedException {
		try {
			manualReversalManager.makeReversal(manualReversalVO);
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(manualReversalVO.getTransactionCode());
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}
	
	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setManualReversalManager(ManualReversalManager manualReversalManager) {
		this.manualReversalManager = manualReversalManager;
	}

	public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}

	@Override
	public TransactionDetailMasterModel getTransactionDetailMasterModel(String trxCode) throws FrameworkCheckedException {
		try {
			return manualReversalManager.getTransactionDetailMasterModel(trxCode);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void createBulkReversal(List<BulkAutoReversalModel> dis) throws FrameworkCheckedException {
		try {
			manualReversalManager.createBulkReversal(dis);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void validateTransactionCode(String trxCode) throws FrameworkCheckedException {
		try {
			manualAdjustmentManager.validateTransactionCode(trxCode);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public BBAccountsViewModel getBBAccountsViewModel(BBAccountsViewModel model) throws FrameworkCheckedException {
		try {
			return manualAdjustmentManager.getBBAccountsViewModel(model);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public String accountNumberHealthCheck(BBAccountsViewModel model, String fromTo) {
		return null;
	}

	public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
		this.manualAdjustmentManager = manualAdjustmentManager;
	}

	@Override
	public List<BulkAutoReversalModel> loadBulkReversalModelList(SearchBaseWrapper searchBaseWrapper) throws Exception {
		try {
			return manualReversalManager.loadBulkReversalModelList(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void updateIsApprovedForBatch(Long batchId, String[] bulkAdjustmentId) throws Exception {

		try{
			manualReversalManager.updateIsApprovedForBatch(batchId , bulkAdjustmentId);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}

	}

	@Override
	public void pushBulkReversalToQueue(BulkReversalRefDataModel bulkReversalRefDataModel) throws Exception {

		try {
			manualReversalManager.pushBulkReversalToQueue(bulkReversalRefDataModel);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION);
		}

	}

	@Override
	public SearchBaseWrapper loadBulkAutoReversals(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			return manualReversalManager.loadBulkAutoReversals(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
}
