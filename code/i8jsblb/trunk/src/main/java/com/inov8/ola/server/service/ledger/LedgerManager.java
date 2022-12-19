package com.inov8.ola.server.service.ledger;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.ola.integration.vo.LedgerModel;


public interface LedgerManager 
{
			
	public BaseWrapper saveLedgerEntry(BaseWrapper baseWrapper) throws Exception;
	public BaseWrapper loadLedgerEntry(BaseWrapper baseWrapper) throws Exception;
	public Double getDailyConsumedBalance(Long accountId,Long transactionTypeId,Date date) throws Exception;
	public Double getConsumedBalanceByDateRange(Long accountId,Long transactionTypeId,Date startDate,Date endDate) throws Exception;
	public List<LedgerModel> getLedgerModelByDateRangeAndAccountId(Long accountId,Date fromDate, Date toDate) throws Exception;
	public List<LedgerModel> getLedgerModelByAccountId(Long accountId,Integer noOfTransactions) throws Exception;
	public void deleteLedgerEntry(BaseWrapper baseWrapper) throws Exception;
	public BaseWrapper loadLedgerModelByPK(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public Date getLastTrxDate(Long accountId, Long transactionTypeId,Long handlerId) throws Exception;
	public Double getConsumedBalanceByDateRangeForDormancy(Long accountId,Long transactionTypeId,Date startDate,Date endDate) throws Exception;
	public List<com.inov8.integration.common.model.LedgerModel> loadLedgerEntries(BaseWrapper baseWrapper) throws FrameworkCheckedException;

}
