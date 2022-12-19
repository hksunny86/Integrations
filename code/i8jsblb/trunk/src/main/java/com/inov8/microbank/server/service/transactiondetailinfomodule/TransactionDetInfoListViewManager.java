package com.inov8.microbank.server.service.transactiondetailinfomodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel;

public interface TransactionDetInfoListViewManager {

	
	public SearchBaseWrapper searchAllTransaction(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	
	public SearchBaseWrapper searchAllAllPayTransaction(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public CustomList<AllpayTransInfoListViewModel> searchAgentTransactionHistory(SearchBaseWrapper searchBaseWrapper, String mfsId) throws FrameworkCheckedException;
	
	public SearchBaseWrapper loadTransaction(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	public SearchBaseWrapper searchTransactionCode(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	
	public SearchBaseWrapper searchTransaction(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	
	public SearchBaseWrapper searchTransactionDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	
	public SearchBaseWrapper searchCommissionTransaction(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	
	public SearchBaseWrapper searchAuditLog(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;
	
	/*added by atifhussain*/
	public SearchBaseWrapper searchHandlerTransactionHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
