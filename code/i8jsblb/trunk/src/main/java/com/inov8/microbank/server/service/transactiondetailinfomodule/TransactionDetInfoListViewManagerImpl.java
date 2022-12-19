package com.inov8.microbank.server.service.transactiondetailinfomodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel;
import com.inov8.microbank.common.model.transactiondetailinfomodule.TransactionDetInfoListViewModel;
import com.inov8.microbank.server.dao.commissionmodule.CommissionTransactionDAO;
import com.inov8.microbank.server.dao.failurelogmodule.AuditLogDAO;
import com.inov8.microbank.server.dao.transactiondetailinfomodule.AllPayTransactionInfoListViewDAO;
import com.inov8.microbank.server.dao.transactiondetailinfomodule.TransactionDetInfoListViewDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailDAO;

public class TransactionDetInfoListViewManagerImpl implements
		TransactionDetInfoListViewManager {

	  private TransactionDetInfoListViewDAO transactionDetInfoListViewDAO; 
	  private AllPayTransactionInfoListViewDAO allPayTransactionInfoListViewDAO;
	  private TransactionCodeDAO transactionCodeDAO ;
	  private TransactionDetailDAO transactionDetailDAO ;
	  private TransactionDAO transactionDAO ;
	  private CommissionTransactionDAO commissionTransactionDAO ;
	  private AuditLogDAO auditLogDAO ;
	  
	  

	public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
		this.auditLogDAO = auditLogDAO;
	}

	public void setCommissionTransactionDAO(
			CommissionTransactionDAO commissionTransactionDAO) {
		this.commissionTransactionDAO = commissionTransactionDAO;
	}

	public void setTransactionCodeDAO(TransactionCodeDAO transactionCodeDAO) {
		this.transactionCodeDAO = transactionCodeDAO;
	}

	public void setTransactionDAO(TransactionDAO transactionDAO) {
		this.transactionDAO = transactionDAO;
	}

	public void setTransactionDetailDAO(TransactionDetailDAO transactionDetailDAO) {
		this.transactionDetailDAO = transactionDetailDAO;
	}

	public void setTransactionDetInfoListViewDAO(
			TransactionDetInfoListViewDAO transactionDetInfoListViewDAO) {
		this.transactionDetInfoListViewDAO = transactionDetInfoListViewDAO;
	}

	public SearchBaseWrapper loadTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchBaseWrapper searchAllTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<TransactionDetInfoListViewModel> list = this.transactionDetInfoListViewDAO.findByExample(
				(TransactionDetInfoListViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchAllAllPayTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<AllpayTransInfoListViewModel> list = this.allPayTransactionInfoListViewDAO.findByExample(
				(AllpayTransInfoListViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	public CustomList<AllpayTransInfoListViewModel> searchAgentTransactionHistory(SearchBaseWrapper searchBaseWrapper, String mfsId) throws FrameworkCheckedException {
		
		return this.allPayTransactionInfoListViewDAO.searchAllpayTransInfoListViewModel(searchBaseWrapper, mfsId);
	}
	
	/**
	 * added by atif hussain
	 */
	public SearchBaseWrapper searchHandlerTransactionHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<AllpayTransInfoListViewModel>	customList	=	this.allPayTransactionInfoListViewDAO.findByExample((AllpayTransInfoListViewModel)searchBaseWrapper.getBasePersistableModel() , searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel() );
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}

	
	public SearchBaseWrapper searchTransactionCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<TransactionCodeModel> list = this.transactionCodeDAO.findByExample(
				(TransactionCodeModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper searchTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<TransactionModel> list = this.transactionDAO.findByExample(
				(TransactionModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper searchTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<TransactionDetailModel> list = this.transactionDetailDAO.findByExample(
				(TransactionDetailModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper searchCommissionTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<CommissionTransactionModel> list = this.commissionTransactionDAO.findByExample(
				(CommissionTransactionModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper searchAuditLog(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<AuditLogModel> list = this.auditLogDAO.findByExample(
				(AuditLogModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public void setAllPayTransactionInfoListViewDAO(AllPayTransactionInfoListViewDAO allPayTransactionInfoListViewDAO)
	{
		this.allPayTransactionInfoListViewDAO = allPayTransactionInfoListViewDAO;
	}


}
