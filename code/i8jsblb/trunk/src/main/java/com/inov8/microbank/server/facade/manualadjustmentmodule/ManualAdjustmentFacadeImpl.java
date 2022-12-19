package com.inov8.microbank.server.facade.manualadjustmentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.ManualAdjustmentModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.server.facade.CoreAdviceQueingPreProcessor;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;

import java.util.List;

//import com.inov8.microbank.common.model.BulkManualAdjustmentModel;


public class ManualAdjustmentFacadeImpl implements ManualAdjustmentFacade{
	
	private ManualAdjustmentManager manualAdjustmentManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
	private CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor;
		
	@Override
	public SearchBaseWrapper loadManualAdjustments(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			return manualAdjustmentManager.loadManualAdjustments(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public SearchBaseWrapper loadBulkManualAdjustments(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			return manualAdjustmentManager.loadBulkManualAdjustments(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public String fetchCoreAccountTitle(String accNo) throws FrameworkCheckedException {
		try {
			return manualAdjustmentManager.fetchCoreAccountTitle(accNo);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
		this.manualAdjustmentManager = manualAdjustmentManager;
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
	public void makeOlaToOlaTransfer(ManualAdjustmentModel manualAdjustmentModel, Long authorizationId) throws Exception, WorkFlowException, FrameworkCheckedException {
		try {
			manualAdjustmentManager.makeOlaToOlaTransfer(manualAdjustmentModel, authorizationId);
			
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(manualAdjustmentModel.getTransactionCodeId());
			
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	@Override
	public void makeBBToCoreTransfer(ManualAdjustmentModel manualAdjustmentModel, Long authorizationId) throws Exception, FrameworkCheckedException {
		try {
			manualAdjustmentManager.makeBBToCoreTransfer(manualAdjustmentModel, authorizationId);
			
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(manualAdjustmentModel.getTransactionCodeId());
			coreAdviceQueingPreProcessor.loadAndForwardAdviceToQueue(manualAdjustmentModel.getTransactionCodeId());
			
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	@Override
	public void makeCoreToBBTransfer(ManualAdjustmentModel manualAdjustmentModel, Long authorizationId) throws Exception, FrameworkCheckedException {
		try {
			manualAdjustmentManager.makeCoreToBBTransfer(manualAdjustmentModel, authorizationId);
			
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(manualAdjustmentModel.getTransactionCodeId());
			
		} catch (Exception ex) {
			//In-case of Core Debit if 220 or 404 response is received from RDV, then send Debit Reversal Advice
			coreAdviceQueingPreProcessor.loadAndForwardAdviceToQueue(manualAdjustmentModel.getTransactionCodeId());

			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	@Override
	public void makeOracleFinancialSettlement(ManualAdjustmentModel manualAdjustmentModel, Long authorizationId) throws Exception, FrameworkCheckedException {
		manualAdjustmentManager.makeOracleFinancialSettlement(manualAdjustmentModel, authorizationId);
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
	public void createBulkAdjustments(List<BulkManualAdjustmentModel> dis)
			throws FrameworkCheckedException {
		try {
			manualAdjustmentManager.createBulkAdjustments(dis);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void pushBulkManualAdjustmentToQueue(BulkManualAdjustmentRefDataModel model) throws Exception {
		try {
			manualAdjustmentManager.pushBulkManualAdjustmentToQueue(model);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}



	public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}

	public void setCoreAdviceQueingPreProcessor(CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor) {
		this.coreAdviceQueingPreProcessor = coreAdviceQueingPreProcessor;
	}

/*<<<<<<< .mine
	@Override
	public void createBulkAdjustments(List<BulkManualAdjustmentModel> dis)
			throws FrameworkCheckedException {
		try {
			manualAdjustmentManager.createBulkAdjustments(dis);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

=======*/
	public List<BulkManualAdjustmentModel> loadBulkManualAdjustmentModelList(SearchBaseWrapper searchBaseWrapper) throws Exception{
		try {
			return manualAdjustmentManager.loadBulkManualAdjustmentModelList(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public Boolean updateBulkAdjustmentErrorMessage(Long bulkManualAdjustmentId, String errorMessage) throws Exception{
		try {
			return manualAdjustmentManager.updateBulkAdjustmentErrorMessage(bulkManualAdjustmentId, errorMessage);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	@Override
	public void updateIsApprovedForBatch(Long batchId , String bulkManualAdjustmentId []) throws Exception {
		try{
			manualAdjustmentManager.updateIsApprovedForBatch(batchId , bulkManualAdjustmentId);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	@Override
	public String accountNumberHealthCheck(BBAccountsViewModel model, String fromTo) {
		return manualAdjustmentManager.accountNumberHealthCheck(model, fromTo);
	}

	@Override
	public TransactionCodeModel loadTransactionCodeModelByPrimaryKey(Long transactionCodeId) throws Exception {
		return manualAdjustmentManager.loadTransactionCodeModelByPrimaryKey(transactionCodeId);
	}

}
