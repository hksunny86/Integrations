package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.switchmodule.ediVO.ISO8583VO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.ola.integration.vo.OLAVO;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author Jawwad Farooq
 */

public class SwitchController
{
  private FailureLogManager auditLogModule;
  private PostedTransactionReportFacade postedTransactionReportModule;
  

private XStream xstream;
  private SwitchFactory switchFactory;

  public SwitchController()
  {

  }

  /**
   * Checks the balance of the account.
   * @param accountInfo SwitchWrapper
   * @return SwitchWrapper
   * @throws Exception
   */
  public SwitchWrapper checkBalance(SwitchWrapper accountInfo) throws WorkFlowException, FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    switchObj.checkBalance(accountInfo);

    return accountInfo;
  }
  
  public SwitchWrapper titleFetch(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException
  {
    Switch switchObj = getSwitch(wrapper);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    switchObj.titleFetch(wrapper);

    return wrapper;
  }
  
  public SwitchWrapper settleCommission(SwitchWrapper accountInfo) throws WorkFlowException, FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    switchObj.settleCommission(accountInfo);

    return accountInfo;
  }
  
  public SwitchWrapper settleInov8Commission(SwitchWrapper accountInfo) throws WorkFlowException, FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    switchObj.settleInov8Commission(accountInfo);

    return accountInfo;
  }
  
  
  public SwitchWrapper changePIN(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    accountInfo = switchObj.changePIN(accountInfo);

    return accountInfo;
  }
 

  public SwitchWrapper customerAccountRelationshipInquiry(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    switchObj.customerAccountRelationshipInquiry(accountInfo);

    return accountInfo;
  }
  
  public SwitchWrapper customerProfile(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    switchObj.customerProfile(accountInfo);

    return accountInfo;
  }

  
  public SwitchWrapper fetchTitle(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    accountInfo = switchObj.titleFetch(accountInfo);

    return accountInfo;
  }
  
  public SwitchWrapper generatePIN(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    accountInfo = switchObj.generatePIN(accountInfo);

    return accountInfo;
  }
  
  public SwitchWrapper miniStatement(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    accountInfo = switchObj.miniStatement(accountInfo);

    return accountInfo;
  }
  
  public SwitchWrapper changeDeliveryChannel(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    accountInfo = switchObj.changeDeliveryChannel(accountInfo);

    return accountInfo;
  }
  public SwitchWrapper getAllOlaAccounts(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    accountInfo = switchObj.getAllOlaAccounts(accountInfo);

    return accountInfo;
  }
  
  public SwitchWrapper getAllAccountsWithStats(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    accountInfo = switchObj.getAllAccountsWithStats(accountInfo);

    return accountInfo;
  }
  
  
  public SwitchWrapper getAllAccountsStatsWithRange(SwitchWrapper accountInfo) throws FrameworkCheckedException
  {
    Switch switchObj = getSwitch(accountInfo);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    accountInfo = switchObj.getAllAccountsStatsWithRange(accountInfo);

    return accountInfo;
  }

  

  /**
   * Performs the transaction on the Bank's system(Switch).
   * @param transactionsSwitchWrapper SwitchWrapper
   * @return SwitchWrapper
   * @throws Exception
   */
  public SwitchWrapper transaction(SwitchWrapper transactionsSwitchWrapper,
                                   CommissionWrapper commissionWrapper) throws
      FrameworkCheckedException
  {

    Switch switchObj = getSwitch(transactionsSwitchWrapper);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
/*
    try
    {
      transactionsSwitchWrapper = switchObj.checkBalance(transactionsSwitchWrapper);

      if (transactionsSwitchWrapper.getBalance() <
          transactionsSwitchWrapper.getTransactionTransactionModel().getTotalAmount().doubleValue())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.INSUFFICIENT_BALANCE);
      }
    }
    catch (WorkFlowException ex1)
    {
      if( !ex1.getMessage().equalsIgnoreCase( WorkFlowErrorCodeConstants.CHK_BAL_SERVICE_NOT_AVAILABLE ) )
        throw ex1;
    }
*/
    return switchObj.transaction(transactionsSwitchWrapper);

  }


  public SwitchWrapper debit(SwitchWrapper transactionsSwitchWrapper, CommissionWrapper commissionWrapper)
			throws FrameworkCheckedException
	{
		Switch switchObj = getSwitch(transactionsSwitchWrapper);
		((SwitchProcessor) switchObj).setAuditLogModuleFacade(auditLogModule);
		((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
		((SwitchProcessor) switchObj).setXstream(xstream);		
		return switchObj.debit(transactionsSwitchWrapper);
	}

  public SwitchWrapper debitAccount(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
 
	  	Switch switchObj = getSwitch(switchWrapper);
		((SwitchProcessor) switchObj).setAuditLogModuleFacade(auditLogModule);
		((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
		((SwitchProcessor) switchObj).setXstream(xstream);		
		return switchObj.debitAccount(switchWrapper);
  }
  
  public SwitchWrapper transactionToSettleComm(SwitchWrapper transactionsSwitchWrapper,
                                   CommissionRateModel commissionRateModel) throws
      FrameworkCheckedException
  {
	  /*
	   * Commented By Maqsood Shahzad(20070809)
	   * 
    Switch switchObj = getSwitch(transactionsSwitchWrapper);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
    
    */

//    transactionsSwitchWrapper = switchObj.checkBalance( transactionsSwitchWrapper ) ;

//    if( transactionsSwitchWrapper.getBalance() < commissionRateModel.getRate() )
//    {
//      throw new WorkFlowException(WorkFlowErrorCodeConstants.INSUFFICIENT_BALANCE);
//    }
//    switchObj.transaction(transactionsSwitchWrapper);

    return transactionsSwitchWrapper;
  }



  /**
   * Rollbacks the transaction on the Bank's system(Switch).
   * @param transactions SwitchWrapper
   * @return SwitchWrapper
   */
  public SwitchWrapper rollback(SwitchWrapper transactions) throws FrameworkCheckedException
  {
	  // Added by Maqsood Shahzad (13-06-2007)
	  ISO8583VO iso8583VO = new ISO8583VO();
	  iso8583VO.setTransactionCode(transactions.getWorkFlowWrapper().getTransactionCodeModel().getCode());
	  iso8583VO.setTransactionType(SwitchConstants.PURCHASE_TRANS);
	  Switch switchObj = getSwitch(transactions);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
	  transactions.setISO8583VO(iso8583VO);
	  switchObj.rollback(transactions);
    return transactions;
  }
  public SwitchWrapper createAccount(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException, Exception
  {
	  
	  
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  switchObj.createAccount(wrapper);
      return wrapper;
  }
  public SwitchWrapper debitCreditAccount(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException
  {
	  
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
	  wrapper = switchObj.debitCreditAccount(wrapper);
      return wrapper;
  }
  
  public SwitchWrapper creditAccountAdvice(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException
  {
	  
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
	  wrapper = switchObj.creditAccountAdvice(wrapper);
      return wrapper;
  }
  
  public SwitchWrapper updateAccountInfo(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException
  {
	  
	  
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  wrapper = switchObj.updateAccountInfo(wrapper);
      return wrapper;
  }
  
  public SwitchWrapper changeAccountDetails(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException{
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  wrapper = switchObj.changeAccountDetails(wrapper);
      return wrapper;
  }
    
  public SwitchWrapper getAccountInfo(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException
  {
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  wrapper = switchObj.getAccountInfo(wrapper);
      return wrapper;
  }
  public SwitchWrapper creditTransfer(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException
  {
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  wrapper = switchObj.creditTransfer(wrapper);
      return wrapper;
  }
  public SwitchWrapper getLedger(SwitchWrapper wrapper) throws Exception
  {
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
	  wrapper = switchObj.getLedger(wrapper);
      return wrapper;
  }
  
  public SwitchWrapper billInquiry(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
	  
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
	  wrapper = switchObj.billInquiry(wrapper);
      return wrapper;
  }
  
  public SwitchWrapper billPayment(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
	  Switch switchObj = getSwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
	  wrapper = switchObj.billPayment(wrapper);
      return wrapper;
  }

  	
	public SwitchWrapper pushBillPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		
		  Switch switchObj = getSwitch(switchWrapper);
		  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
		  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
		  switchWrapper = switchObj.pushBillPayment(switchWrapper);
	      return switchWrapper;
	}   
  
	public SwitchWrapper sendReversalAdvice(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
		Switch switchObj = getSwitch(wrapper);
		((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
		((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
		wrapper = switchObj.sendReversalAdvice(wrapper);
		return wrapper;
	}

	public SwitchWrapper sendCreditAdvice(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
		Switch switchObj = getSwitch(wrapper);
		((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
		((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
		wrapper = switchObj.sendCreditAdvice(wrapper);
		return wrapper;
	}

public SwitchWrapper billPaymentViaNADRA(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
	  Switch switchObj = getNADRASwitch(wrapper);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
	  wrapper = switchObj.payBill(wrapper);
    return wrapper;
}
  
public SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException, Exception
{


	Switch switchObj = getSwitch(wrapper);
	((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	switchObj.verifyWalkinCustomerThroughputLimits(wrapper);
	return wrapper;
}

public SwitchWrapper saveWalkinCustomerLedgerEntry(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException, Exception
{


	Switch switchObj = getSwitch(wrapper);
	((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	switchObj.saveWalkinCustomerLedgerEntry(wrapper);
	return wrapper;
}


  public Map<Long, String> getStatusCodes() throws Exception
  {
	  return null;
  }
  
  /**
   * Rollbacks the walkin customer ledger entry
   * @param transactions SwitchWrapper
   * @return SwitchWrapper
   * @author mudassar.hanif
   */
  public SwitchWrapper rollbackWalkinCustomer(SwitchWrapper transactions)
  {
	  ISO8583VO iso8583VO = new ISO8583VO();
	  iso8583VO.setTransactionCode(transactions.getWorkFlowWrapper().getTransactionCodeModel().getCode());
	  iso8583VO.setTransactionType(SwitchConstants.PURCHASE_TRANS);
	  Switch switchObj = getSwitch(transactions);
	  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	  transactions.setISO8583VO(iso8583VO);
	  switchObj.rollbackWalkinCustomer(transactions);
    return transactions;
  }

  public SwitchWrapper updateLedger(SwitchWrapper wrapper)throws WorkFlowException, FrameworkCheckedException
  {


  	Switch switchObj = getSwitch(wrapper);
  	((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
  	switchObj.updateLedger(wrapper);
  	return wrapper;
  }


  
  private Switch getNADRASwitch(SwitchWrapper wrapper)
  {
	  try
	  {
	    Class switchClass = Class.forName("com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.NADRA.NADRASwitchImpl");
		
		Class[] argsClass = new Class[] {ApplicationContext.class };
//		Object[] argsVals = new Object[] {this.ctx};

		Constructor constructor = switchClass.getConstructor() ;
		return  (Switch)constructor.newInstance() ;
	  }
	  catch (Exception ex)
		{
			ex.printStackTrace();
			throw new WorkFlowException(ex.getMessage(), ex);
		}		
	    
  }
  

  /**
   * Gets the Switch's implementation class
   * @param SwitchWrapper SwitchWrapper
   * @return Switch
   * @throws Exception
   */
  private Switch getSwitch(SwitchWrapper SwitchWrapper) throws WorkFlowException
  {
    // Following line should be replaced. There must be a call to the SwitchFactory
    //    return new SwitchImpl();
    /**
     * @todo ****** UN-COMMENT THE FOLLOWING LINE OF CODE ********
     */

    return switchFactory.getSwitch( SwitchWrapper ) ;
  }

//  private AuditLogModel auditLogBeforeCall(SwitchWrapper switchWrapper) throws
//      WorkFlowException
//  {
//    AuditLogModel auditLogModel = new AuditLogModel();
//    auditLogModel.setTransactionStartTime( new Timestamp( new java.util.Date().getTime() ) );
//    auditLogModel.setIntegrationModuleId( IntegrationModuleConstants.SWITCH_MODULE );
//    if( switchWrapper.getTransactionTransactionModel() != null && switchWrapper.getTransactionTransactionModel().getTransactionCodeIdTransactionCodeModel() != null)
//      auditLogModel.setTransactionCodeId( switchWrapper.getTransactionTransactionModel().getTransactionCodeIdTransactionCodeModel().getTransactionCodeId() );
////    auditLogModel.setBeforeObjectImage( this.xstream.toXML( switchWrapper )  );
//    auditLogModel.setActionLogId( ThreadLocalActionLog.getActionLogId() );
//    BaseWrapper baseWrapper = new BaseWrapperImpl() ;
//    baseWrapper.setBasePersistableModel( auditLogModel );
//
//    return (AuditLogModel)this.auditLogModule.saveLogRequiresNewTransaction( baseWrapper ).getBasePersistableModel() ;
//  }
//
//  private void auditLogAfterCall( AuditLogModel auditLogModel, SwitchWrapper switchWrapper ) throws
//      WorkFlowException
//  {
////    auditLogModel.setAfterObjectImage( this.xstream.toXML( switchWrapper )  );
//    auditLogModel.setTransactionEndTime( new Timestamp( new java.util.Date().getTime() ) );
//    BaseWrapper baseWrapper = new BaseWrapperImpl() ;
//    baseWrapper.setBasePersistableModel( auditLogModel );
//
//    this.auditLogModule.saveLogRequiresNewTransaction( baseWrapper ) ;
//  }

  public void setXstream(XStream xstream)
  {
    this.xstream = xstream;
  }

  public void setAuditLogModule(FailureLogManager auditLogModuleFacade)
  {
    this.auditLogModule = auditLogModuleFacade;
  }

  public void setSwitchFactory(SwitchFactory switchFactory)
  {
    this.switchFactory = switchFactory;
  }

  public void setPostedTransactionReportModule( PostedTransactionReportFacade postedTransactionReportModule )
  {
      this.postedTransactionReportModule = postedTransactionReportModule;
  }

public SwitchWrapper getPhoenixTransactions(SwitchWrapper switchWrapper) throws Exception {

	switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
	switchWrapper.setPaymentModeId(6L);
	CustomerAccount custAcct = new CustomerAccount();
	switchWrapper.setCustomerAccount(custAcct);

	Switch switchObj = getSwitch(switchWrapper);
    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
    ((SwitchProcessor)switchObj).setXstream( xstream );
	
    return switchObj.getPhoenixTransactions(switchWrapper);
}
	public SwitchWrapper reverseFundTransfer(SwitchWrapper switchWrapper) throws Exception {
		switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
		switchWrapper.setPaymentModeId(6L);
		CustomerAccount custAcct = new CustomerAccount();
		switchWrapper.setCustomerAccount(custAcct);
	
		Switch switchObj = getSwitch(switchWrapper);
	    ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
	    ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
		 ((SwitchProcessor)switchObj).setXstream( xstream );
		
	    return switchObj.reverseFundTransfer(switchWrapper);
	}

	  public SwitchWrapper creditCardBillPayment(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException{
		  Switch switchObj = getSwitch(wrapper);
		  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
		  ((SwitchProcessor)switchObj).setPostedTransactionReportFacade( postedTransactionReportModule );
		  wrapper = switchObj.creditCardBillPayment(wrapper);
	      return wrapper;
	  }
	  
	  public SwitchWrapper getAccountTitle(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException
	  {
		  Switch switchObj = getSwitch(wrapper);
		  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
		  wrapper = switchObj.getAccountTitle(wrapper);
	      return wrapper;
	  }
	  
	public SwitchWrapper verifyLimits(SwitchWrapper wrapper) throws Exception {
			
		  Switch switchObj = getSwitch(wrapper);
		  ((SwitchProcessor)switchObj).setAuditLogModuleFacade( auditLogModule );
		  wrapper = switchObj.verifyLimits(wrapper);
	      return wrapper;
	}
}
