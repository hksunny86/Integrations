package com.inov8.microbank.server.facade.portal.transactiondetail;

import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.CashTransactionViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.SenderRedeemViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionCashReversalViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionSummaryTextViewModel;
import com.inov8.microbank.server.service.portal.transactiondetail.TransactionDetailManager;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Asad Hayat
 * @version 1.0
 */
public class TransactionDetailFacadeImpl implements TransactionDetailFacade {

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	private TransactionDetailManager transactionDetailManager;

	public SearchBaseWrapper searchTransactionDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return transactionDetailManager
					.searchTransactionDetail(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public SearchBaseWrapper searchTransactionDetail( SearchBaseWrapper searchBaseWrapper,
	                                                  String mfsId ) throws FrameworkCheckedException
	{
	    try
        {
	        return transactionDetailManager.searchTransactionDetail( searchBaseWrapper, mfsId );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	public SearchBaseWrapper loadTransactionDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return transactionDetailManager
					.loadTransactionDetail(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper createOrUpdateIssue(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		try {
			return transactionDetailManager.createOrUpdateIssue(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public BaseWrapper createOrUpdateTransaction(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		try {
			return transactionDetailManager
					.createOrUpdateTransaction(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	public BaseWrapper loadTransaction(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return transactionDetailManager.loadTransaction(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public List<TransactionSummaryTextViewModel> populateBBTransactionSummary(String csvTransIDs)
		throws FrameworkCheckedException {
		try {
			return transactionDetailManager.populateBBTransactionSummary(csvTransIDs);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchMiniTransactionViewList(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return transactionDetailManager.searchMiniTransactionViewList(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public CustomList<TransactionAllViewModel> searchTransactionAllView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException {
		try {
			return transactionDetailManager.searchTransactionAllView( searchBaseWrapper );
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public List<?> findWalkInCustomerTransactions(String cnic,TransactionAllViewModel transactionAllViewModel, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap,DateRangeHolderModel dateRangeHolderModel)
    {
        return transactionDetailManager.findWalkInCustomerTransactions(cnic,transactionAllViewModel, pagingHelperModel, sortingOrderMap,dateRangeHolderModel);
    }

	public List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids )
	{
		return transactionDetailManager.loadSupplierProcessingStatuses( ids );
	}

	public CustomList<TransactionCashReversalViewModel> searchTransactionCashReversalView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
	{
		try
		{
			return transactionDetailManager.searchTransactionCashReversalView( searchBaseWrapper );
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.FIND_ACTION );
		}
	}

	public CustomList<SenderRedeemViewModel> searchSenderRedeemView( SearchBaseWrapper searchBaseWrapper )
				throws FrameworkCheckedException {
		
		try{
			return transactionDetailManager.searchSenderRedeemView(searchBaseWrapper);
		}catch (Exception ex){
			throw this.frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.FIND_ACTION );
		}
		
	}
	
		public CustomList<CashTransactionViewModel> searchCashTransactionView( SearchBaseWrapper searchBaseWrapper )
				throws FrameworkCheckedException {
		
		try{
			return transactionDetailManager.searchCashTransactionView(searchBaseWrapper);
		}catch (Exception ex){
			throw this.frameworkExceptionTranslator.translate( ex, FrameworkExceptionTranslator.FIND_ACTION );
		}
		
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setTransactionDetailManager(
			TransactionDetailManager transactionDetailManager) {
		this.transactionDetailManager = transactionDetailManager;
	}

	public BaseWrapper modifyPINSentMiniTransToExpired( BaseWrapper baseWrapper ) throws FrameworkCheckedException {
		try {
			return transactionDetailManager.modifyPINSentMiniTransToExpired( baseWrapper );
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}
	public BaseWrapper updateMiniTransactionModel(BaseWrapper baseWrapper)throws FrameworkCheckedException{
		try {
			return transactionDetailManager.updateMiniTransactionModel(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	@Override
	public MiniTransactionModel LoadMiniTransactionModel(String transactionCode) throws FrameworkCheckedException {
		
			return transactionDetailManager.LoadMiniTransactionModel(transactionCode);
		
	}
	public SearchBaseWrapper searchTransactionCodeHtrViewModel(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
		try {
			return transactionDetailManager.searchTransactionCodeHtrViewModel(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
}
