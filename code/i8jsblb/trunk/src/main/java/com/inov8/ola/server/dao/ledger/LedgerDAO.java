package com.inov8.ola.server.dao.ledger;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.LedgerModel;


public interface LedgerDAO extends BaseDAO<LedgerModel, Long> {

	public Double getDailyConsumedBalance(Long accountId,Long transactionTypeId,Date date,Long handlerId)throws Exception;
	public Double getConsumedBalanceByDateRange(Long accountId, Long transactionTypeId,Date startDate,Date endDate,Long handlerId)throws Exception;
	public List<LedgerModel> getLedgerModelByDateRangeAndAccountID(Date fromDate, Date toDate,Long accountId);
	public List<LedgerModel> getLedgerModelByAccountID(Long accountId,Integer noOfTransactions); 
	public Double getWalkinCustomerConsumedBalanceByDateRange(Long accountId,Long transactionTypeId,Date startDate,Date endDate, boolean excludeInProcessTx);
	public boolean markLedgerReversalEntries(Long ledgerId);
	public LedgerModel saveOrUpdate(LedgerModel ledgerModel);

	public Date getLastTrxDate(Long accountId, Long transactionTypeId,Long handlerId) throws Exception;

	public Double getConsumedBalanceByDateRangeForDormancy(Long accountId, Long transactionTypeId,Date startDate,Date endDate)throws Exception;

	public Double getConsumedBalanceByDateRangeReports(Long accountId, Long transactionTypeId, Date startDate, Date endDate, Long handlerId);
}
