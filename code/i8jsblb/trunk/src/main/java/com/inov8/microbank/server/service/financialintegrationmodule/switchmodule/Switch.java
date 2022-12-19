package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.statuscheckmodule.IntegrationModuleStatus;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface Switch
    extends IntegrationModuleStatus
{

  public SwitchWrapper checkBalance(SwitchWrapper accountInfo) throws WorkFlowException, FrameworkCheckedException;

  public SwitchWrapper transaction(SwitchWrapper transactions ) throws
      WorkFlowException, FrameworkCheckedException;
  
  public SwitchWrapper titleFetch(SwitchWrapper transactions ) throws
  WorkFlowException, FrameworkCheckedException;
  
  public SwitchWrapper customerProfile(SwitchWrapper transactions ) throws
  WorkFlowException, FrameworkCheckedException;


  public SwitchWrapper customerAccountRelationshipInquiry(SwitchWrapper switchWrapper) throws
  WorkFlowException, FrameworkCheckedException;
  
  public SwitchWrapper miniStatement(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
    
  public SwitchWrapper changeDeliveryChannel(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper generatePIN(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper changePIN(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  
  
  public SwitchWrapper rollback(SwitchWrapper transactions) throws
      WorkFlowException, FrameworkCheckedException;
  
  public SwitchWrapper debit(SwitchWrapper transactions ) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper createAccount(SwitchWrapper wrapper) throws
  WorkFlowException,FrameworkCheckedException,Exception;
  public SwitchWrapper debitCreditAccount(SwitchWrapper wrapper) throws
  WorkFlowException,FrameworkCheckedException;
  public SwitchWrapper getAllOlaAccounts(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper updateAccountInfo(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper getAccountInfo(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public Map<Long, String> getStatusCodes() throws Exception;
  public SwitchWrapper creditTransfer(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper settleCommission(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper settleInov8Commission(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper getAllAccountsWithStats(SwitchWrapper switchWrapper) throws FrameworkCheckedException;
  public SwitchWrapper getAllAccountsStatsWithRange(SwitchWrapper switchWrapper) throws FrameworkCheckedException;
  public SwitchWrapper getLedger(SwitchWrapper switchWrapper )throws WorkFlowException,FrameworkCheckedException, Exception;
  public SwitchWrapper billPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper pushBillPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper billInquiry(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper wrapper) throws  WorkFlowException,FrameworkCheckedException,Exception;
  public SwitchWrapper saveWalkinCustomerLedgerEntry(SwitchWrapper wrapper) throws  WorkFlowException,FrameworkCheckedException,Exception;
  public SwitchWrapper getPhoenixTransactions(SwitchWrapper wrapper) throws Exception;
  public SwitchWrapper rollbackWalkinCustomer(SwitchWrapper transactions) throws  WorkFlowException;
  public SwitchWrapper updateLedger(SwitchWrapper switchWrapper) throws  WorkFlowException,FrameworkCheckedException;
  public SwitchWrapper reverseFundTransfer(SwitchWrapper switchWrapper) throws Exception;
  public SwitchWrapper payBill(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper changeAccountDetails(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper creditCardBillPayment(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper creditAccountAdvice(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper debitAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper getAccountTitle(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper sendReversalAdvice(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
  public SwitchWrapper sendCreditAdvice(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;	
  public SwitchWrapper verifyLimits(SwitchWrapper switchWrapper) throws Exception ;
}
