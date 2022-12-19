package com.inov8.microbank.server.dao.transactionmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.TransactionDetailModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface TransactionDetailDAO
    extends BaseDAO<TransactionDetailModel, Long>
{
	TransactionDetailModel loadTransactionDetailModel(String cnic, Long productId, Long supProcessingStatus);
	int findPendingIdpCnicTransactions(String cnic, Long productId, Long supProcessingStatus);
	TransactionDetailModel loadAccOpeningTransactionDetailModel(Long customerId);
}
