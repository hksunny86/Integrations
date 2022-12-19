package com.inov8.microbank.server.service.switchmodule.ola;

/**
 * Project Name: 			Commons-Integration	
 * @author 					Jawwad Farooq
 * Creation Date: 			November 2008  			
 * Description:				
 */

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inov8.ola.integration.vo.LedgerModel;
import com.inov8.ola.integration.vo.OLALedgerVO;
import com.inov8.ola.integration.vo.OLAVO;


public interface OLASwitch
{
	OLAVO transaction( OLAVO olaVO ) throws Exception;
	OLAVO createAccount( OLAVO olaVO ) throws Exception;
	OLAVO changeAccountStatus( OLAVO olaVO ) throws Exception;
	List<OLAVO> getAllAccounts( OLAVO olaVO ) throws Exception;
	OLAVO deleteAccount( OLAVO olaVO ) throws Exception;
	Map<Long, String> getStatusCodes() throws Exception;
	OLAVO getAccountInfo(OLAVO olaVO) throws Exception;	
	OLAVO checkBalance( OLAVO olaVO ) throws Exception;
	OLAVO reversal( OLAVO olaVO ) throws Exception;
	OLAVO creditTransfer(OLAVO olaVO) throws Exception;
	List<LedgerModel> getLegder( OLALedgerVO ledgerVO ) throws Exception;
//	List<LedgerModel> getLegderTransactions( OLALedgerVO ledgerVO,Integer noOfTransactions) throws Exception;
	HashMap<String,OLAVO> getAllAccountsWithStats(Date date) throws Exception;
	HashMap<String,OLAVO> getAllAccountsStatsWithRange(Date startDate, Date endDate ) throws Exception;
	OLAVO verifyLimits(OLAVO olaVO) throws Exception;
	OLAVO verifyWalkinCustomerThroughputLimits(OLAVO olaVO) throws Exception;
	OLAVO saveWalkinCustomerLedgerEntry(OLAVO olaVO) throws Exception;
	OLAVO rollbackWalkinCustomer(OLAVO olaVO) throws Exception;
	OLAVO updateLedger(OLAVO olaVO) throws Exception;
	OLAVO changeAccountDetails( OLAVO olaVO ) throws Exception;
	OLAVO getAccountTitle(OLAVO olavo)throws Exception;
    OLAVO makeWalletTransactionReversal(OLAVO olavo)throws Exception;
}
