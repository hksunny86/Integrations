package com.inov8.microbank.server.facade;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.bulkdisbursements.BulkDisbursementsManager;

public class BulkDisbursementsFacadeImpl implements BulkDisbursementsFacade{
	
	private BulkDisbursementsManager bulkDisbursementsManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;

	@Override
	public BulkDisbursementsModel saveOrUpdateBulkDisbursement( BulkDisbursementsModel bulkDisbursementsModel) throws FrameworkCheckedException
	{
		try
		{
			return bulkDisbursementsManager.saveOrUpdateBulkDisbursement(bulkDisbursementsModel);
		}
		catch (Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	public void createOrUpdateBulkDisbursements(List<BulkDisbursementsModel> dis) throws FrameworkCheckedException{
		
		try{
			bulkDisbursementsManager.createOrUpdateBulkDisbursements(dis);
		}
		catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
	    }
		
	}

	@Override
	public void createOrUpdateBulkPayments(List<BulkDisbursementsModel> bulkPaymentList) throws FrameworkCheckedException
	{
		bulkDisbursementsManager.createOrUpdateBulkPayments(bulkPaymentList);
	}

	@Override
	public void update(BulkDisbursementsModel model) throws FrameworkCheckedException
	{
		try
		{
			bulkDisbursementsManager.update(model);
		}
		catch(Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	@Override
	public void makeDebitCreditAccount(String sourceAccount, String targetAccount, Double totalDisbursableAmount, List<BulkDisbursementsModel> bulkDisbursementsModels, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		try
		{
			bulkDisbursementsManager.makeDebitCreditAccount(sourceAccount, targetAccount, totalDisbursableAmount, bulkDisbursementsModels, workFlowWrapper);
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(workFlowWrapper.getTransactionCodeModel().getCode());
		}
		catch(Exception e)
		{
			//In-case of Core Debit if 220 or 404 response is received from RDV, then send Debit Reversal Advice
			if(workFlowWrapper.getTransactionCodeModel() != null){
				try {
					coreAdviceQueingPreProcessor.loadAndForwardAdviceToQueue(workFlowWrapper.getTransactionCodeModel().getCode());
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	@Override
	public void makeTransferFund(StakeholderBankInfoModel sourceAccount, BulkDisbursementsModel bulkDisbursementsModel, String notificationMessage, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		try
		{
			bulkDisbursementsManager.makeTransferFund(sourceAccount, bulkDisbursementsModel, notificationMessage, workFlowWrapper);
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(workFlowWrapper.getTransactionCodeModel().getCode());
		}
		catch(Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public BulkDisbursementsManager getBulkDisbursementsManager() {
		return bulkDisbursementsManager;
	}

	public void setBulkDisbursementsManager(
			BulkDisbursementsManager bulkDisbursementsManager) {
		this.bulkDisbursementsManager = bulkDisbursementsManager;
	}

	public FrameworkExceptionTranslator getFrameworkExceptionTranslator() {
		return frameworkExceptionTranslator;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	@Override
	public SearchBaseWrapper searchBulkDisbursements(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try
		{
			wrapper = bulkDisbursementsManager.searchBulkDisbursements(wrapper);
		}
		catch(Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
		}
		return wrapper;
	}
	
	@Override
	public SearchBaseWrapper searchBulkPayments(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try
		{
			wrapper = bulkDisbursementsManager.searchBulkPayments(wrapper);
		}
		catch(Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
		}
		return wrapper;
	}

	@Override
	public SearchBaseWrapper loadBulkDisbursement(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try
		{
			wrapper = bulkDisbursementsManager.loadBulkDisbursement(wrapper);
		}
		catch(Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
		}
		return wrapper;
	}

	@Override
	public BaseWrapper updateBulkDisbursement(BaseWrapper wrapper) throws FrameworkCheckedException {
		try
		{
			wrapper = bulkDisbursementsManager.updateBulkDisbursement(wrapper);
		}
		catch(Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.UPDATE_ACTION);
		}
		return wrapper;
	}

	@Override
	public List<LabelValueBean> loadBankUsersList() throws FrameworkCheckedException {
		try {
			return bulkDisbursementsManager.loadBankUsersList();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void saveOrUpdateCollection(List<BulkDisbursementsModel> bulkDisbursementsModels) throws FrameworkCheckedException {
		
		bulkDisbursementsManager.saveOrUpdateCollection(bulkDisbursementsModels);
	}


	@Override
	public String postCoreFundTransfer(String fromAccountNumber, String toAccountNumber, Double amount, Long productId, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		try {
			return bulkDisbursementsManager.postCoreFundTransfer(fromAccountNumber, toAccountNumber, amount, productId, workFlowWrapper);
		} catch (Exception ex) {
			//In-case of Core Debit if 220 or 404 response is received from RDV, then send Debit Reversal Advice
			if(workFlowWrapper.getTransactionCodeModel() != null){
				try {
					coreAdviceQueingPreProcessor.loadAndForwardAdviceToQueue(workFlowWrapper.getTransactionCodeModel().getCode());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	public void setCoreAdviceQueingPreProcessor(CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor) {
		this.coreAdviceQueingPreProcessor = coreAdviceQueingPreProcessor;
	}

	public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}
}
