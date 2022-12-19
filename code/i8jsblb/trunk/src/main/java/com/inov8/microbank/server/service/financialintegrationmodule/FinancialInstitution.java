package com.inov8.microbank.server.service.financialintegrationmodule;

import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;

public interface FinancialInstitution
{
	public boolean isVeriflyRequired() throws FrameworkCheckedException,Exception;
	public boolean isVeriflyLite() throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper changePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper markAsDeleted(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper activatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper deactivatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper deleteAccount(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper generatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper modifyAccountInfo(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper resetPin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper changeAccountNick(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper generateOneTimePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public VeriflyBaseWrapper verifyOneTimePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException,Exception;
	
	public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	
	public SwitchWrapper titleFetch(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException;
	
	public SwitchWrapper checkBalanceForAccountVerification(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	
	public SwitchWrapper transaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	public SwitchWrapper debit(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	
	public SwitchWrapper rollback(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	
	public SwitchWrapper customerAccountRelationshipInquiry(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	
	//Method for MiniStatement
	public SwitchWrapper miniStatement(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	public SwitchWrapper creditTransfer(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	
	//public SwitchWrapper generateThirdPartyPIN(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception;
	
	//Method for Generate Pin
	
	//Method for Credit Card Account Balance
	
	public boolean isIVRChannelActive(String accountNo, String accountType, String currency,String NIC) throws Exception;
	public boolean isMobileChannelActive(String accountType, String NIC) throws Exception;
	
	public String getAccountTitle(String accountNo, String accountType, String currency,String NIC) throws Exception;
	
	public boolean activateDeliveryChannel(String accountType, String NIC) throws Exception;
	public boolean deActivateDeliveryChannel(String accountType, String NIC) throws Exception;
	
	public VeriflyBaseWrapper verifyPin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception;
	public SwitchWrapper createAccount(SwitchWrapper wrapper) throws WorkFlowException,FrameworkCheckedException, Exception;
	  public SwitchWrapper debitCreditAccount(SwitchWrapper wrapper) throws
	  WorkFlowException,FrameworkCheckedException, Exception;
	  public SwitchWrapper getAllOlaAccounts(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException;
	  public SwitchWrapper updateAccountInfo(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException;
	  public SwitchWrapper getAccountInfo(SwitchWrapper switchWrapper ) throws WorkFlowException, FrameworkCheckedException;
	  public Map<Long, String> getStatusCodes() throws Exception;
	  public SwitchWrapper settleInov8Commission(SwitchWrapper switchWrapper ) throws WorkFlowException, FrameworkCheckedException, Exception;
	  public SwitchWrapper getAllAccountsWithStats(SwitchWrapper switchWrapper) throws FrameworkCheckedException;
	  
	  public SwitchWrapper verifyPIN(SwitchWrapper switchWrapper ) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException;
	  public SwitchWrapper getLedger(SwitchWrapper wrapper) throws Exception;
	  public SwitchWrapper changeAccountDetails(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException;
	  public SwitchWrapper creditAccountAdvice(SwitchWrapper wrapper) throws WorkFlowException,FrameworkCheckedException, Exception;
	  public AccountInfoModel getAccountInfoModelBySmartMoneyAccount(SmartMoneyAccountModel smartMoneyAccountModel, Long customerId, Long trxCodeId) throws Exception;
	  public SwitchWrapper settleAccountOpeningCommission(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException, Exception;
	  public SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper switchWrapper ) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException;

}
