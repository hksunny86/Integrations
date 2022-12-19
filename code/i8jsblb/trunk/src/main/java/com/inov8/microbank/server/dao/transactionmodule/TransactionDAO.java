package com.inov8.microbank.server.dao.transactionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionModel;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface TransactionDAO extends BaseDAO<TransactionModel, Long> {
	public List getTransactionsByCriteria(Long distributorId, Long productId, Boolean isSettled, Boolean isPosted);
    TransactionModel loadTxAndTxDetailModel( Long transactionId ) throws FrameworkCheckedException;
    public Integer updateTransactionProcessingStatus(Long transactionProcessingStatus, List<Long> transactionId);
	public List<Object[]> getDonationTransactionList(Long trnsactionType, Long supProcessingStatusId, Long serviceId) throws FrameworkCheckedException;
    public MiniTransactionModel loadMiniTransactionByTransactionCode(String transactionCode);
}
