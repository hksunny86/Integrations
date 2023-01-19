package com.inov8.microbank.common.wrapper.switchmodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.common.model.TransactionModel;
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

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class SwitchWrapperImpl extends BaseWrapperImpl implements SwitchWrapper {

	private static final long serialVersionUID = 8438907198435824930L;

	private String customerPoolAccountNumber;
	private PaymentModeModel paymentModePaymentModeModel;
	private TransactionModel transactionTransactionModel;
	private BankModel bankBankModel;
	private SwitchModel switchSwitchModel;

	private String invoiceType;
	private double balance;
	private String responseCode;
	private StakeholderBankInfoModel stakeholderBankInfoModel;
	private AccountInfoModel accountInfoModel;
	private Long bankId;
	private Long paymentModeId;
	private WorkFlowWrapper workFlowWrapper;
	private CommissionWrapper commissionWrapper;
	private VeriflyBaseWrapper veriflyBaseWrapper;
	private CommissionRateModel commissionRateModel;
	private ISO8583VO iso8583VO;
	private IntegrationMessageVO integrationMessageVO;
	private CustomerAccount customerAccount;
	private SmartMoneyAccountModel olaCommissionSMA;
	private SmartMoneyAccountModel smartMoneyAccountModel;
	private OLAVO olavo;
	private List<OLAVO> list;
	private HashMap<String, OLAVO> olaAccountsWithStatsHashMap;
	private AppUserModel commissionAppUserModel;
	private String commissionStakeHolderType;

	// USED FOR FUND TRANSFER
	public String fromAccountNo;
	public String fromAccountType;
	public String fromCurrencyCode;
	// USED FOR TO ACCOUNT
	public String toAccountNo;
	public String toAccountType;
	public String toCurrencyCode;

	public Double transactionAmount;
	public String currencyCode;

	// BILL PAYMENT
	public String utilityCompanyId;
	public String utilityCompanyCategoryId;

	public String consumerNumber;
	public String amountPaid;
	// BILL INQUIRY RESPONSE
	public String subscriberName;
	public String billingMonth;
	public String dueDatePayableAmount;
	public String paymentDueDate;
	public String paymentAfterDueDate;
	public String billStatus;
	public String paymentAuthResponseId;
	public String netCED;
	public String netWithholdingTAX;

	private double creditLimit;
	private double amountDue;
	private double minAmountDue;
	private Date dueDate;
	private double discountAmount;
	private boolean commissioned = false;

	private OLALedgerVO ledgerVO;
	private List<LedgerModel> ledgerModelList;

	private double agentBalance;

	private PhoenixTransactionVO phoenixTransactionVO;
	private List<PhoenixTransactionVO> phoenixTransactionList;

	private boolean isAccountToCashLeg2;
	private boolean isCashToCashLeg2;
	private Double inclusiveCharges;
	private int ftOrder;
	private boolean skipAccountInfoLoading;
	private Long transactionTypeId = null;
	private MiddlewareMessageVO middlewareMessageVO;
	private String senderCNIC;
	private Long intgTransactionTypeId;

	private String toAccountBB;
	private String fromAccountBB;
	private boolean skipPostedTrxEntry;
	private boolean isOfflineBiller;

	private I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;
	private I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO;

	// private String encryptionKey;

	public SwitchWrapperImpl() {
	}

	public void setLedgerVO(OLALedgerVO olaLedgerVO) {
		this.ledgerVO = olaLedgerVO;
	}

	public void setLedgerModelList(List<LedgerModel> list) {
		this.ledgerModelList = list;
	}

	public void setPaymentModePaymentModeModel(PaymentModeModel paymentModePaymentModeModel) {
		this.paymentModePaymentModeModel = paymentModePaymentModeModel;
	}

	public void setTransactionTransactionModel(TransactionModel transactionTransactionModel) {
		this.transactionTransactionModel = transactionTransactionModel;
	}

	public void setBankBankModel(BankModel bankBankModel) {
		this.bankBankModel = bankBankModel;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public void setSwitchSwitchModel(SwitchModel switchSwitchModel) {
		this.switchSwitchModel = switchSwitchModel;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public void setStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
		this.stakeholderBankInfoModel = stakeholderBankInfoModel;
	}

	public void setAccountInfoModel(AccountInfoModel accountInfoModel) {
		this.accountInfoModel = accountInfoModel;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public void setWorkFlowWrapper(WorkFlowWrapper workFlowWrapper) {
		this.workFlowWrapper = workFlowWrapper;
	}

	public void setCommissionWrapper(CommissionWrapper commissionWrapper) {
		this.commissionWrapper = commissionWrapper;
	}

	public void setVeriflyBaseWrapper(VeriflyBaseWrapper veriflyBaseWrapper) {
		this.veriflyBaseWrapper = veriflyBaseWrapper;
	}

	public void setCommissionRateModel(CommissionRateModel commissionRateModel) {
		this.commissionRateModel = commissionRateModel;
	}

	public PaymentModeModel getPaymentModePaymentModeModel() {

		return paymentModePaymentModeModel;
	}

	public TransactionModel getTransactionTransactionModel() {
		return transactionTransactionModel;
	}

	public BankModel getBankBankModel() {
		return bankBankModel;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public SwitchModel getSwitchSwitchModel() {
		return switchSwitchModel;
	}

	public double getBalance() {
		return balance;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public StakeholderBankInfoModel getStakeholderBankInfoModel() {
		return stakeholderBankInfoModel;
	}

	public AccountInfoModel getAccountInfoModel() {
		return accountInfoModel;
	}

	public Long getBankId() {
		return bankId;
	}

	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public WorkFlowWrapper getWorkFlowWrapper() {
		return workFlowWrapper;
	}

	public CommissionWrapper getCommissionWrapper() {
		return commissionWrapper;
	}

	public VeriflyBaseWrapper getVeriflyBaseWrapper() {
		return veriflyBaseWrapper;
	}

	public CommissionRateModel getCommissionRateModel() {
		return commissionRateModel;
	}

	public void setISO8583VO(ISO8583VO iso8583VO) {
		this.iso8583VO = iso8583VO;
	}

	public ISO8583VO getISO8583VO() {
		return iso8583VO;
	}

	public IntegrationMessageVO getIntegrationMessageVO() {
		return this.integrationMessageVO;
	}

	public void setIntegrationMessageVO(IntegrationMessageVO integrationMessageVO) {
		this.integrationMessageVO = integrationMessageVO;
	}

	public CustomerAccount getCustomerAccount() {
		return this.customerAccount;
	}

	public void setCustomerAccount(CustomerAccount customerAccount) {
		this.customerAccount = customerAccount;

	}

	public double getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public double getMinAmountDue() {
		return minAmountDue;
	}

	public void setMinAmountDue(double minAmountDue) {
		this.minAmountDue = minAmountDue;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public void setCommissioned(boolean commissioned) {
		this.commissioned = commissioned;
	}

	public boolean getCommissioned() {
		// TODO Auto-generated method stub
		return commissioned;
	}

	public ISO8583VO getIso8583VO() {
		return iso8583VO;
	}

	public void setIso8583VO(ISO8583VO iso8583VO) {
		this.iso8583VO = iso8583VO;
	}

	public OLAVO getOlavo() {
		return olavo;
	}

	public void setOlavo(OLAVO olavo) {
		this.olavo = olavo;
	}

	public List<OLAVO> getOlaAccountsList() {
		// TODO Auto-generated method stub
		return this.list;
	}

	public void setOlaAccountsList(List<OLAVO> list) {
		this.list = list;

	}

	public SmartMoneyAccountModel getOlaCommissionSMA() {
		return olaCommissionSMA;
	}

	public void setOlaCommissionSMA(SmartMoneyAccountModel olaCommissionSMA) {
		this.olaCommissionSMA = olaCommissionSMA;
	}

	public AppUserModel getCommissionAppUserModel() {
		return commissionAppUserModel;
	}

	public void setCommissionAppUserModel(AppUserModel commissionAppUserModel) {
		this.commissionAppUserModel = commissionAppUserModel;
	}

	public String getCommissionStakeHolderType() {
		return commissionStakeHolderType;
	}

	public void setCommissionStakeHolderType(String commissionStakeHolderType) {
		this.commissionStakeHolderType = commissionStakeHolderType;
	}

	public HashMap<String, OLAVO> getOlaAccountsWithStatsHashMap() {
		return olaAccountsWithStatsHashMap;
	}

	public void setOlaAccountsWithStatsHashMap(HashMap<String, OLAVO> olaAccountsWithStatsHashMap) {
		this.olaAccountsWithStatsHashMap = olaAccountsWithStatsHashMap;
	}

	public SmartMoneyAccountModel getSmartMoneyAccountModel() {
		return smartMoneyAccountModel;
	}

	public void setSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
		this.smartMoneyAccountModel = smartMoneyAccountModel;
	}

	public OLALedgerVO getLedgerVO() {

		return ledgerVO;
	}

	public List<LedgerModel> getLedgerModelList() {

		return ledgerModelList;
	}

	public String getUtilityCompanyId() {
		return utilityCompanyId;
	}

	public void setUtilityCompanyId(String utilityCompanyId) {
		this.utilityCompanyId = utilityCompanyId;
	}

	public String getUtilityCompanyCategoryId() {
		return utilityCompanyCategoryId;
	}

	public void setUtilityCompanyCategoryId(String utilityCompanyCategoryId) {
		this.utilityCompanyCategoryId = utilityCompanyCategoryId;
	}

	public String getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public String getBillingMonth() {
		return billingMonth;
	}

	public void setBillingMonth(String billingMonth) {
		this.billingMonth = billingMonth;
	}

	public String getDueDatePayableAmount() {
		return dueDatePayableAmount;
	}

	public void setDueDatePayableAmount(String dueDatePayableAmount) {
		this.dueDatePayableAmount = dueDatePayableAmount;
	}

	public String getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public String getPaymentAfterDueDate() {
		return paymentAfterDueDate;
	}

	public void setPaymentAfterDueDate(String paymentAfterDueDate) {
		this.paymentAfterDueDate = paymentAfterDueDate;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getPaymentAuthResponseId() {
		return paymentAuthResponseId;
	}

	public void setPaymentAuthResponseId(String paymentAuthResponseId) {
		this.paymentAuthResponseId = paymentAuthResponseId;
	}

	public String getNetCED() {
		return netCED;
	}

	public void setNetCED(String netCED) {
		this.netCED = netCED;
	}

	public String getNetWithholdingTAX() {
		return netWithholdingTAX;
	}

	public void setNetWithholdingTAX(String netWithholdingTAX) {
		this.netWithholdingTAX = netWithholdingTAX;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public String getFromCurrencyCode() {
		return fromCurrencyCode;
	}

	public void setFromCurrencyCode(String fromCurrencyCode) {
		this.fromCurrencyCode = fromCurrencyCode;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public String getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getToCurrencyCode() {
		return toCurrencyCode;
	}

	public void setToCurrencyCode(String toCurrencyCode) {
		this.toCurrencyCode = toCurrencyCode;
	}

	public double getAgentBalance() {
		return agentBalance;
	}

	public void setAgentBalance(double agentBalance) {
		this.agentBalance = agentBalance;
	}

	public String getCustomerPoolAccountNumber() {
		return customerPoolAccountNumber;
	}

	public void setCustomerPoolAccountNumber(String customerPoolAccountNumber) {
		this.customerPoolAccountNumber = customerPoolAccountNumber;
	}

	public PhoenixTransactionVO getPhoenixTransactionVO() {
		return phoenixTransactionVO;
	}

	public void setPhoenixTransactionVO(PhoenixTransactionVO phoenixTransactionVO) {
		this.phoenixTransactionVO = phoenixTransactionVO;
	}

	public List<PhoenixTransactionVO> getPhoenixTransactionList() {
		return phoenixTransactionList;
	}

	public void setPhoenixTransactionList(
			List<PhoenixTransactionVO> phoenixTransactionList) {
		this.phoenixTransactionList = phoenixTransactionList;
	}
	public boolean isAccountToCashLeg2() {
		return isAccountToCashLeg2;
	}

	public void setIsAccountToCashLeg2(boolean isAccountToCashLeg2) {
		this.isAccountToCashLeg2 = isAccountToCashLeg2;

	}

	public boolean isCashToCashLeg2() {
		// TODO Auto-generated method stub
		return isCashToCashLeg2;
	}

	public void setIsCashToCashLeg2(boolean isCashToCashLeg2) {
		// TODO Auto-generated method stub
		this.isCashToCashLeg2=isCashToCashLeg2;
	}

	public Double getInclusiveChargesApplied(){
		return inclusiveCharges;
	}

	public void setInclusiveChargesApplied(Double inclusiveCharges){
		this.inclusiveCharges = inclusiveCharges;
	}

	public int getFtOrder() {
		return ftOrder;
	}

	public void setFtOrder(int ftOrder) {
		this.ftOrder = ftOrder;
	}

	public Long getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(Long transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public boolean getSkipAccountInfoLoading() {
		return skipAccountInfoLoading;
	}

	public void setSkipAccountInfoLoading(boolean skipAccountInfoLoading) {
		this.skipAccountInfoLoading = skipAccountInfoLoading;
	}

	@Override
	public void setMiddlewareIntegrationMessageVO(
			MiddlewareMessageVO middlewareMessageVO) {
		this.middlewareMessageVO = middlewareMessageVO;
	}

	@Override
	public MiddlewareMessageVO getMiddlewareIntegrationMessageVO() {
		return middlewareMessageVO;
	}

	public String getSenderCNIC() {
		return senderCNIC;
	}

	public void setSenderCNIC(String senderCNIC) {
		this.senderCNIC = senderCNIC;
	}

	public Long getIntgTransactionTypeId() {
		return intgTransactionTypeId;
	}

	public void setIntgTransactionTypeId(Long intgTransactionTypeId) {
		this.intgTransactionTypeId = intgTransactionTypeId;
	}
	public String getToAccountBB() {
		return toAccountBB;
	}

	public void setToAccountBB(String toAccountBB) {
		this.toAccountBB = toAccountBB;
	}

	public String getFromAccountBB() {
		return fromAccountBB;
	}

	public void setFromAccountBB(String fromAccountBB) {
		this.fromAccountBB = fromAccountBB;
	}

	public boolean getSkipPostedTrxEntry() {
		return skipPostedTrxEntry;
	}

	public void setSkipPostedTrxEntry(boolean skipPostedTrxEntry) {
		this.skipPostedTrxEntry = skipPostedTrxEntry;
	}

	public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
		return i8SBSwitchControllerRequestVO;
	}

	public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
		this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
	}

	public I8SBSwitchControllerResponseVO getI8SBSwitchControllerResponseVO() {
		return i8SBSwitchControllerResponseVO;
	}

	public void setI8SBSwitchControllerResponseVO(I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
		this.i8SBSwitchControllerResponseVO = i8SBSwitchControllerResponseVO;
	}

	@Override
	public Boolean getIsOfflineBiller() {
		return isOfflineBiller;
	}

	@Override
	public void setIsOfflineBiller(Boolean isOfflineBiller) {
		this.isOfflineBiller = isOfflineBiller;
	}

}
