package com.inov8.microbank.server.service.postedtransactionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TransactionLogModel;

public interface PostedTransactionsManager {
	public SearchBaseWrapper searchPostedTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public Boolean resetPostedTransaction(Long transactionLogId, String resolutionType);
	public TransactionLogModel loadTransactionDetail(Long transactionLogId) throws FrameworkCheckedException;
}
