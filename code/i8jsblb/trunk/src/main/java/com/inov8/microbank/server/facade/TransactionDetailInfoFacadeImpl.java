package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel;
import com.inov8.microbank.server.service.transactiondetailinfomodule.TransactionDetInfoListViewManager;

public class TransactionDetailInfoFacadeImpl implements
		TransactionDetailInfoFacade {

	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private TransactionDetInfoListViewManager transactionDetInfoListViewManager; 
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setTransactionDetInfoListViewManager(
			TransactionDetInfoListViewManager transactionDetInfoListViewManager) {
		this.transactionDetInfoListViewManager = transactionDetInfoListViewManager;
	}

	public SearchBaseWrapper loadTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			this.transactionDetInfoListViewManager.loadTransaction(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);

		}

		return searchBaseWrapper;


	}

	public SearchBaseWrapper searchTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
	
		try {
			this.transactionDetInfoListViewManager.searchTransaction(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
		// TODO Auto-generated method stub

	}

	public SearchBaseWrapper searchAllTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			this.transactionDetInfoListViewManager.searchAllTransaction(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper searchAllAllPayTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.transactionDetInfoListViewManager.searchAllAllPayTransaction(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}
	


	public CustomList<AllpayTransInfoListViewModel> searchAgentTransactionHistory(SearchBaseWrapper searchBaseWrapper, String mfsId) throws FrameworkCheckedException {
		
		CustomList<AllpayTransInfoListViewModel> customList = null;
		try
		{
			customList = this.transactionDetInfoListViewManager.searchAgentTransactionHistory(searchBaseWrapper, mfsId);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
		return customList;
	}	

	public SearchBaseWrapper searchCommissionTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			this.transactionDetInfoListViewManager.searchCommissionTransaction(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchTransactionCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			this.transactionDetInfoListViewManager.searchTransactionCode(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			this.transactionDetInfoListViewManager.searchTransactionDetail(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchAuditLog(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			this.transactionDetInfoListViewManager.searchAuditLog(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	@Override
	public SearchBaseWrapper searchHandlerTransactionHistory(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			this.transactionDetInfoListViewManager.searchHandlerTransactionHistory(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}
}
