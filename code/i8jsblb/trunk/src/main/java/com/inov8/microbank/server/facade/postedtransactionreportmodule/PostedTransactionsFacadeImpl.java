package com.inov8.microbank.server.facade.postedtransactionreportmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TransactionLogModel;
import com.inov8.microbank.server.service.postedtransactionmodule.PostedTransactionsManager;

public class PostedTransactionsFacadeImpl implements PostedTransactionsFacade
{
	private PostedTransactionsManager postedTransactionsManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}


	public void setPostedTransactionsManager ( PostedTransactionsManager postedTransactionsManager) 
	{
		this.postedTransactionsManager = postedTransactionsManager;
	}


	@Override
	public SearchBaseWrapper searchPostedTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException{
		try{
			return postedTransactionsManager.searchPostedTransactions(searchBaseWrapper);
	    }catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	}


	@Override
	public Boolean resetPostedTransaction(Long transactionLogId, String resolutionType) {
		return postedTransactionsManager.resetPostedTransaction(transactionLogId, resolutionType);
	}


	@Override
	public TransactionLogModel loadTransactionDetail(Long transactionLogId)
			throws FrameworkCheckedException {
		try{
			return postedTransactionsManager.loadTransactionDetail(transactionLogId);
	    }catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,this.frameworkExceptionTranslator.FIND_ACTION);
	    }
	}


}
