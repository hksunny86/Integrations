package com.inov8.microbank.server.dao.commissionmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.util.TransactionProductEnum;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface CommissionTransactionDAO extends BaseDAO<CommissionTransactionModel, Long> {
	
	public Boolean updateCommissionTransaction(Boolean isSettled, Boolean isPosted, Object[] params, TransactionProductEnum productEnum, Integer legNumber);
	public void updateCommissionTransactionSettled(Long transactionDetailId);
}
