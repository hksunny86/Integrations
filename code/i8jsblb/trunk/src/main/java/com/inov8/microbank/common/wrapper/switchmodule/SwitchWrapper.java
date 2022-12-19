package com.inov8.microbank.common.wrapper.switchmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.PhoenixTransactionVO;
import com.inov8.microbank.server.service.switchmodule.ediVO.ISO8583VO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.ola.integration.vo.LedgerModel;
import com.inov8.ola.integration.vo.OLALedgerVO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;

import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: inov8 Limited
 * </p>
 * 
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface SwitchWrapper extends BaseWrapper {
	
	
	public void setCustomerPoolAccountNumber(String customerPool);
	
	public String getCustomerPoolAccountNumber();

	public void setPaymentModePaymentModeModel(PaymentModeModel paymentModePaymentModeModel);

	public void setTransactionTransactionModel(TransactionModel transactionTransactionModel);

	public void setBankBankModel(BankModel bankBankModel);

	public void setInvoiceType(String invoiceType);

	public void setSwitchSwitchModel(SwitchModel switchSwitchModel);

	public PaymentModeModel getPaymentModePaymentModeModel();

	public TransactionModel getTransactionTransactionModel();

	public BankModel getBankBankModel();

	public String getInvoiceType();

	public SwitchModel getSwitchSwitchModel();

	public double getBalance();

	public void setBalance(double balance);
	
	
	public double getAgentBalance();

	public void setAgentBalance(double balance);

	public void setResponseCode(String responseCode);

	public String getResponseCode();

	public void setStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel);

	public StakeholderBankInfoModel getStakeholderBankInfoModel();

	public void setAccountInfoModel(AccountInfoModel accountInfoModel);

	public AccountInfoModel getAccountInfoModel();

	/*********************** ASKARI INTEGRATION ****************** */
	// USED FOR FROM ACCOUNT
	public String getFromAccountNo();

	public void setFromAccountNo(String attr);

	public String getFromAccountType();

	public void setFromAccountType(String attr);

	public String getFromCurrencyCode();

	public void setFromCurrencyCode(String attr);

	// USED FOR TO ACCOUNT
	public String getToAccountNo();

	public void setToAccountNo(String attr);

	public String getToAccountType();

	public void setToAccountType(String attr);

	public String getToCurrencyCode();

	public void setToCurrencyCode(String attr);
	
	//OTHER PARAMETERS

	public Double getTransactionAmount();

	public void setTransactionAmount(Double value);

	public String getCurrencyCode();

	public void setCurrencyCode(String attr);
	
	public void setUtilityCompanyId(String utilityCompanyId);

	public void setConsumerNumber(String consumerNumber);

	public void setAmountPaid(String amountPaid);


	// ASKARI PHOENIX INTEGRATIN - BILLING SERVICE
	// REQUEST PARAMETERS
	
	public String getUtilityCompanyId();

	public String getConsumerNumber();

	public String getAmountPaid();

	// BILL INQUIRY RESPONSE
	public String getSubscriberName();

	public String getBillingMonth();

	public String getDueDatePayableAmount();

	public String getPaymentDueDate();

	public String getPaymentAfterDueDate();

	public String getBillStatus();

	public String getPaymentAuthResponseId();

	public String getNetCED();

	public String getNetWithholdingTAX();

	public void setSubscriberName(String attr);

	public void setBillingMonth(String attr);

	public void setDueDatePayableAmount(String attr);

	public void setPaymentDueDate(String attr);

	public void setPaymentAfterDueDate(String attr);

	public void setBillStatus(String attr);

	public void setPaymentAuthResponseId(String attr);

	public void setNetCED(String attr);

	public void setNetWithholdingTAX(String attr);

	/*********************** ASKARI INTEGRATION ****************** */

	public void setWorkFlowWrapper(WorkFlowWrapper workFlowWrapper);

	public WorkFlowWrapper getWorkFlowWrapper();

	public void setBankId(Long bankId);

	public void setPaymentModeId(Long paymentModeId);

	public Long getBankId();

	public Long getPaymentModeId();

	public void setCommissionWrapper(CommissionWrapper commissionWrapper);

	public void setVeriflyBaseWrapper(VeriflyBaseWrapper veriflyBaseWrapper);

	public CommissionWrapper getCommissionWrapper();

	public VeriflyBaseWrapper getVeriflyBaseWrapper();

	public void setCommissionRateModel(CommissionRateModel commissionRateModel);

	public CommissionRateModel getCommissionRateModel();

	public void setISO8583VO(ISO8583VO iso8583VO);

	public void setIntegrationMessageVO(IntegrationMessageVO integrationMessageVO);
	
	public void setMiddlewareIntegrationMessageVO(MiddlewareMessageVO middlewareMessageVO);
	
	public MiddlewareMessageVO getMiddlewareIntegrationMessageVO();

	public IntegrationMessageVO getIntegrationMessageVO();

	public ISO8583VO getISO8583VO();

	public OLAVO getOlavo();

	public void setOlavo(OLAVO olavo);

	public OLALedgerVO getLedgerVO();

	public void setLedgerVO(OLALedgerVO olaLedgerVO);

	public List<LedgerModel> getLedgerModelList();

	public void setLedgerModelList(List<LedgerModel> list);

	public void setOlaAccountsList(List<OLAVO> list);

	public List<OLAVO> getOlaAccountsList();

	public void setOlaAccountsWithStatsHashMap(HashMap<String, OLAVO> list);

	public HashMap<String, OLAVO> getOlaAccountsWithStatsHashMap();

	public void setCustomerAccount(CustomerAccount customerAccount);

	public CustomerAccount getCustomerAccount();

	public double getAmountDue();

	public void setAmountDue(double amountDue);

	public double getCreditLimit();

	public void setCreditLimit(double creditLimit);

	public Date getDueDate();

	public void setDueDate(Date dueDate);

	public double getMinAmountDue();

	public void setMinAmountDue(double minAmountDue);

	public void setDiscountAmount(double discountAmount);

	public double getDiscountAmount();

	public boolean getCommissioned();

	public void setCommissioned(boolean commissioned);

	public SmartMoneyAccountModel getOlaCommissionSMA();

	public void setOlaCommissionSMA(SmartMoneyAccountModel olaCommissionSMA);

	public AppUserModel getCommissionAppUserModel();

	public void setCommissionAppUserModel(AppUserModel commissionAppUserModel);

	public String getCommissionStakeHolderType();

	public void setCommissionStakeHolderType(String commissionStakeHolderType);

	public SmartMoneyAccountModel getSmartMoneyAccountModel();

	public void setSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel);

	public boolean isAccountToCashLeg2();
	public boolean isCashToCashLeg2();
	public void setIsAccountToCashLeg2(boolean isAccountToCashLeg2);
	public void setIsCashToCashLeg2(boolean isCashToCashLeg2);
	// public void setEncryptionKey(String encryptionKey);
	// public String getEncryptionKey();

	public PhoenixTransactionVO getPhoenixTransactionVO();

	public void setPhoenixTransactionVO(PhoenixTransactionVO phoenixTransactionVO);

	public List<PhoenixTransactionVO> getPhoenixTransactionList();

	public void setPhoenixTransactionList(List<PhoenixTransactionVO> phoenixTransactionList);

	public Double getInclusiveChargesApplied();

	public void setInclusiveChargesApplied(Double inclusiveCharges);

	public int getFtOrder();

	public void setFtOrder(int ftOrder);

	public Long getTransactionTypeId();

	public void setTransactionTypeId(Long transactionTypeId);

	public boolean getSkipAccountInfoLoading();

	public void setSkipAccountInfoLoading(boolean skipAccountInfoLoading);
	
	public String getSenderCNIC();

	public void setSenderCNIC(String senderCNIC);
	
	public String getUtilityCompanyCategoryId();

	public void setUtilityCompanyCategoryId(String utilityCompanyCategoryId);
	public Long getIntgTransactionTypeId();
	public void setIntgTransactionTypeId(Long intgTransactionTypeId) ;
	
	public String getToAccountBB();
	public void setToAccountBB(String toAccountBB);
	public String getFromAccountBB();
	public void setFromAccountBB(String fromAccountBB);
	public boolean getSkipPostedTrxEntry();
	public void setSkipPostedTrxEntry(boolean skipPostedTrxEntry);

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO();
    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO);
    public I8SBSwitchControllerResponseVO getI8SBSwitchControllerResponseVO();
    public void setI8SBSwitchControllerResponseVO(I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO);
}
