package com.inov8.microbank.common.vo.ussd;

import com.inov8.microbank.common.model.AppUserModel;

import java.io.Serializable;
import java.util.Date;




public class UserState implements Serializable {
	
	private static final long serialVersionUID = -6734300286235599004L;


	private long appUserTypeID;
	private String msisdn;

	private String userMsisdn;
	private String pin;
	private String newPin;
	private String confirmNewPin;
	private double amount;
	private String commandCode;

	private int senderID;
	private long screenCode;
	private long previousScreenCode;
	private boolean inputRequired;
	private String cwTotalAmountForCustomer;
	private String executionRequired;
	private String validInputs; 
	private String cutomInput;
	private String transactionCode;//PIN
	private String customerBalance;
	private String agentBalance;
	private String transactionID;//ID
	private String transactionDate;//CW Transaction Date of initiation
	private String transactionTime;//CW Transaction Time of initiation
	private String validDate;
	private String validTime;
	private String deductionAmount;
	private String fedAmount;
	private String agentName;
	private String customerName;
	private String customerCNIC;
	private String walkinCustomerCNIC;
	private String walkinCustomerMSISDN;
	private String billPaymentConsumerNumber;
	private String utilityCompanyID;
	private String utilityBillType;
	private String utilityCompanyName;
	private String utilityBillMonth;
	private String utilityBillAmount;
	private String utilitySubscriber;
	private String commissionAmount;
	private String transactionProcessingAmount;
	private String billAmount;
	private String transactionAmount;
	private String recipientName;
	private String totalAmount;
	private String bankId;
	private String accId;
	private Long ProductID;
	
	private String CAMT;
	private String TAMT;
	private String TPAM;
	private String BAMT;
	private String A1CAMT;
	private String A2CAMT;
	private String TXAM;
	private boolean changePinRequired=false;
	private Long UserStateModelId=null;
	private Date creationDate;
	private int invalidPinAttempts=0;
	private String zongMSISDN=null;
//	@Embedded
	AppUserModelVO appUserModel=null;
//	@Embedded
	UserDeviceAccountsModelVO userDeviceAccountsModel=null;

	AppUserModel customerAppUserModel = null;
	
	private String walkinSenderCNIC;
	private String walkinSenderMSISDN;
	private String walkinReceiverCNIC;
	private String walkinReceiverMSISDN;
	private Boolean isAccountToCashLeg2;
	private Boolean isCashToCashLeg2;
	private Long actionLogId;
	private long transactionCodeId;
	private Boolean isAgentFirstLogin;
	private String commissionOption;
	private String myCommissionStartDate;
	private String myCommnissionEndDate;
	private String myCommissionAmount;
	private String transferAmount;
	
	private String donationAmount;
	private String customerId;
	private String donationCompanyId;
	private Boolean commissionInclusive;
	private Boolean isBulkPaymentLeg2;
	private String idpPaymentType;
	private String creditCardNumber;
	private String creditCardAmount;
	private String minAmount;
	private String lastBillAmount;
    private String accountTypeId;
    private String jCashAccountId;
    private String loginPIN;
    private String challanID;
    private String receiverAccountNo;
    private String coreAccountID;
    private String coreAccountTitle;
    private String billDueDate;
    private String isBillOverDue;
    private String isBillPaid;
    private String paymentMode;
    private String userOTP;
    private String govPaymentType;
    private boolean isPrepaidLoad;

	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}	
	public String getDonationAmount() {
		return donationAmount;
	}
	public void setDonationAmount(String donationAmount) {
		this.donationAmount = donationAmount;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getDonationCompanyId() {
		return donationCompanyId;
	}
	public void setDonationCompanyId(String donationCompanyId) {
		this.donationCompanyId = donationCompanyId;
	}
	public String getCommissionOption() {
		return commissionOption;
	}
	public void setCommissionOption(String commissionOption) {
		this.commissionOption = commissionOption;
	}
	public Boolean getIsAgentFirstLogin() {
		return isAgentFirstLogin;
	}
	public void setIsAgentFirstLogin(Boolean isAgentFirstLogin) {
		this.isAgentFirstLogin = isAgentFirstLogin;
	}
	public Boolean getIsCashToCashLeg2() {
		return isCashToCashLeg2;
	}
	public void setIsCashToCashLeg2(Boolean isCashToCashLeg2) {
		this.isCashToCashLeg2 = isCashToCashLeg2;
	}
	public long getAppUserTypeID() {
		return appUserTypeID;
	}
	public void setAppUserTypeID(long appUserTypeID) {
		this.appUserTypeID = appUserTypeID;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getUserMsisdn() {
		return userMsisdn;
	}
	public void setUserMsisdn(String userMsisdn) {
		this.userMsisdn = userMsisdn;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getNewPin() {
		return newPin;
	}
	public void setNewPin(String newPin) {
		this.newPin = newPin;
	}
	public String getConfirmNewPin() {
		return confirmNewPin;
	}
	public void setConfirmNewPin(String confirmNewPin) {
		this.confirmNewPin = confirmNewPin;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCommandCode() {
		return commandCode;
	}
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	public int getSenderID() {
		return senderID;
	}
	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	public long getScreenCode() {
		return screenCode;
	}
	public void setScreenCode(long screenCode) {
		this.screenCode = screenCode;
	}
	public boolean isInputRequired() {
		return inputRequired;
	}
	public void setInputRequired(boolean inputRequired) {
		this.inputRequired = inputRequired;
	}
	public String getCwTotalAmountForCustomer() {
		return cwTotalAmountForCustomer;
	}
	public void setCwTotalAmountForCustomer(String cwTotalAmountForCustomer) {
		this.cwTotalAmountForCustomer = cwTotalAmountForCustomer;
	}
	public String getExecutionRequired() {
		return executionRequired;
	}
	public void setExecutionRequired(String executionRequired) {
		this.executionRequired = executionRequired;
	}
	public String getValidInputs() {
		return validInputs;
	}
	public void setValidInputs(String validInputs) {
		this.validInputs = validInputs;
	}
	public String getCutomInput() {
		return cutomInput;
	}
	public void setCutomInput(String cutomInput) {
		this.cutomInput = cutomInput;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getCustomerBalance() {
		return customerBalance;
	}
	public void setCustomerBalance(String customerBalance) {
		this.customerBalance = customerBalance;
	}
	public String getAgentBalance() {
		return agentBalance;
	}
	public void setAgentBalance(String agentBalance) {
		this.agentBalance = agentBalance;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public String getDeductionAmount() {
		return deductionAmount;
	}
	public void setDeductionAmount(String deductionAmount) {
		this.deductionAmount = deductionAmount;
	}
	public String getFedAmount() {
		return fedAmount;
	}
	public void setFedAmount(String fedAmount) {
		this.fedAmount = fedAmount;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerCNIC() {
		return customerCNIC;
	}
	public void setCustomerCNIC(String customerCNIC) {
		this.customerCNIC = customerCNIC;
	}
	public String getWalkinCustomerCNIC() {
		return walkinCustomerCNIC;
	}
	public void setWalkinCustomerCNIC(String walkinCustomerCNIC) {
		this.walkinCustomerCNIC = walkinCustomerCNIC;
	}
	public String getWalkinCustomerMSISDN() {
		return walkinCustomerMSISDN;
	}
	public void setWalkinCustomerMSISDN(String walkinCustomerMSISDN) {
		this.walkinCustomerMSISDN = walkinCustomerMSISDN;
	}
	public String getBillPaymentConsumerNumber() {
		return billPaymentConsumerNumber;
	}
	public void setBillPaymentConsumerNumber(String billPaymentConsumerNumber) {
		this.billPaymentConsumerNumber = billPaymentConsumerNumber;
	}
	public String getUtilityCompanyID() {
		return utilityCompanyID;
	}
	public void setUtilityCompanyID(String utilityCompanyID) {
		this.utilityCompanyID = utilityCompanyID;
	}
	public String getUtilityBillType() {
		return utilityBillType;
	}
	public void setUtilityBillType(String utilityBillType) {
		this.utilityBillType = utilityBillType;
	}
	public String getUtilityCompanyName() {
		return utilityCompanyName;
	}
	public void setUtilityCompanyName(String utilityCompanyName) {
		this.utilityCompanyName = utilityCompanyName;
	}
	public String getUtilityBillMonth() {
		return utilityBillMonth;
	}
	public void setUtilityBillMonth(String utilityBillMonth) {
		this.utilityBillMonth = utilityBillMonth;
	}
	public String getUtilityBillAmount() {
		return utilityBillAmount;
	}
	public void setUtilityBillAmount(String utilityBillAmount) {
		this.utilityBillAmount = utilityBillAmount;
	}
	public String getUtilitySubscriber() {
		return utilitySubscriber;
	}
	public void setUtilitySubscriber(String utilitySubscriber) {
		this.utilitySubscriber = utilitySubscriber;
	}
	public String getCommissionAmount() {
		return commissionAmount;
	}
	public void setCommissionAmount(String commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	public String getTransactionProcessingAmount() {
		return transactionProcessingAmount;
	}
	public void setTransactionProcessingAmount(String transactionProcessingAmount) {
		this.transactionProcessingAmount = transactionProcessingAmount;
	}
	public String getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getCAMT() {
		return CAMT;
	}
	public void setCAMT(String cAMT) {
		CAMT = cAMT;
	}
	public String getTAMT() {
		return TAMT;
	}
	public void setTAMT(String tAMT) {
		TAMT = tAMT;
	}
	public String getTPAM() {
		return TPAM;
	}
	public void setTPAM(String tPAM) {
		TPAM = tPAM;
	}
	public String getBAMT() {
		return BAMT;
	}
	public void setBAMT(String bAMT) {
		BAMT = bAMT;
	}
	public String getA1CAMT() {
		return A1CAMT;
	}
	public void setA1CAMT(String a1camt) {
		A1CAMT = a1camt;
	}
	public String getA2CAMT() {
		return A2CAMT;
	}
	public void setA2CAMT(String a2camt) {
		A2CAMT = a2camt;
	}
	public String getTXAM() {
		return TXAM;
	}
	public void setTXAM(String tXAM) {
		TXAM = tXAM;
	}
	public boolean isChangePinRequired() {
		return changePinRequired;
	}
	public void setChangePinRequired(boolean changePinRequired) {
		this.changePinRequired = changePinRequired;
	}
	
	public String getCashoutSMS()
	{
		String sms = this.getCommandCode()+" "+this.getMsisdn()+" "+this.getAmount()+" "+this.getPin();
		return sms;
	}
	
	public String getPayCashSMS()
	{
		String sms = this.getCommandCode()+" "+this.getPin()+" "+this.getTransactionCode()+" "+this.getMsisdn();
		return sms;
	}
	/*public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}*/
	public AppUserModelVO getAppUserModel() {
		return appUserModel;
	}
	public void setAppUserModel(AppUserModelVO appUserModel) {
		this.appUserModel = appUserModel;
	}
	public UserDeviceAccountsModelVO getUserDeviceAccountsModel() {
		return userDeviceAccountsModel;
	}
	public void setUserDeviceAccountsModel(UserDeviceAccountsModelVO userDeviceAccountsModel) {
		this.userDeviceAccountsModel = userDeviceAccountsModel;
	}
	public Long getUserStateModelId() {
		return UserStateModelId;
	}
	public void setUserStateModelId(Long userStateModelId) {
		UserStateModelId = userStateModelId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public long getPreviousScreenCode() {
		return previousScreenCode;
	}
	public void setPreviousScreenCode(long previousScreenCode) {
		this.previousScreenCode = previousScreenCode;
	}
	public int getInvalidPinAttempts() {
		return invalidPinAttempts;
	}
	public void setInvalidPinAttempts(int invalidPinAttempts) {
		this.invalidPinAttempts = invalidPinAttempts;
	}
	public String getZongMSISDN() {
		return zongMSISDN;
	}
	public void setZongMSISDN(String zongMSISDN) {
		this.zongMSISDN = zongMSISDN;
	}
	public String getWalkinSenderCNIC() {
		return walkinSenderCNIC;
	}
	public void setWalkinSenderCNIC(String walkinSenderCNIC) {
		this.walkinSenderCNIC = walkinSenderCNIC;
	}
	public String getWalkinSenderMSISDN() {
		return walkinSenderMSISDN;
	}
	public void setWalkinSenderMSISDN(String walkinSenderMSISDN) {
		this.walkinSenderMSISDN = walkinSenderMSISDN;
	}
	public String getWalkinReceiverCNIC() {
		return walkinReceiverCNIC;
	}
	public void setWalkinReceiverCNIC(String walkinReceiverCNIC) {
		this.walkinReceiverCNIC = walkinReceiverCNIC;
	}
	public String getWalkinReceiverMSISDN() {
		return walkinReceiverMSISDN;
	}
	public void setWalkinReceiverMSISDN(String walkinReceiverMSISDN) {
		this.walkinReceiverMSISDN = walkinReceiverMSISDN;
	}
	public Boolean getIsAccountToCashLeg2() {
		return isAccountToCashLeg2;
	}
	public void setIsAccountToCashLeg2(Boolean isAccountToCashLeg2) {
		this.isAccountToCashLeg2 = isAccountToCashLeg2;
	}
	public Long getActionLogId() {
		return actionLogId;
	}
	public void setActionLogId(Long actionLogId) {
		this.actionLogId = actionLogId;
	}
	public long getTransactionCodeId() {
		return transactionCodeId;
	}
	public void setTransactionCodeId(long transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}
	public String getMyCommissionStartDate() {
		return myCommissionStartDate;
	}
	public void setMyCommissionStartDate(String myCommissionStartDate) {
		this.myCommissionStartDate = myCommissionStartDate;
	}
	public String getMyCommnissionEndDate() {
		return myCommnissionEndDate;
	}
	public void setMyCommnissionEndDate(String myCommnissionEndDate) {
		this.myCommnissionEndDate = myCommnissionEndDate;
	}
	public String getAgentCommissionAmount() {
		return myCommissionAmount;
	}
	public void setAgentCommissionAmount(String agentCommissionAmount) {
		this.myCommissionAmount = agentCommissionAmount;
	}
	public Boolean isCommissionInclusive() {
		return commissionInclusive;
	}
	public void setCommissionInclusive(Boolean commissionInclusive) {
		this.commissionInclusive = commissionInclusive;
	}
	public Boolean getIsBulkPaymentLeg2() {
		return isBulkPaymentLeg2;
	}
	public void setIsBulkPaymentLeg2(Boolean isBulkPaymentLeg2) {
		this.isBulkPaymentLeg2 = isBulkPaymentLeg2;
	}
	public String getIdpPaymentType() {
		return idpPaymentType;
	}
	public void setIdpPaymentType(String idpPaymentType) {
		this.idpPaymentType = idpPaymentType;
	}
	
	public String getCreditCardAmount() {
		return creditCardAmount;
	}
	public void setCreditCardAmount(String creditCardAmount) {
		this.creditCardAmount = creditCardAmount;
	}
	public String getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(String minAmount) {
		this.minAmount = minAmount;
	}
	public String getLastBillAmount() {
		return lastBillAmount;
	}
	public void setLastBillAmount(String lastBillAmount) {
		this.lastBillAmount = lastBillAmount;
	}

    public String getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(String accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

	public String getjCashAccountId() {
		return jCashAccountId;
	}

	public void setjCashAccountId(String jCashAccountId) {
		this.jCashAccountId = jCashAccountId;
	}

	public String getLoginPIN() {
		return loginPIN;
	}

	public void setLoginPIN(String loginPIN) {
		this.loginPIN = loginPIN;
	}

	public String getChallanID() {
		return challanID;
	}

	public void setChallanID(String challanID) {
		this.challanID = challanID;
	}

	public String getReceiverAccountNo() {
		return receiverAccountNo;
	}

	public void setReceiverAccountNo(String receiverAccountNo) {
		this.receiverAccountNo = receiverAccountNo;
	}

	public String getCoreAccountID() {
		return coreAccountID;
	}

	public void setCoreAccountID(String coreAccountID) {
		this.coreAccountID = coreAccountID;
	}

	public String getCoreAccountTitle() {
		return coreAccountTitle;
	}

	public void setCoreAccountTitle(String coreAccountTitle) {
		this.coreAccountTitle = coreAccountTitle;
	}

	public String getBillDueDate() {
		return billDueDate;
	}

	public void setBillDueDate(String billDueDate) {
		this.billDueDate = billDueDate;
	}

	public String getIsBillOverDue() {
		return isBillOverDue;
	}

	public void setIsBillOverDue(String isBillOverDue) {
		this.isBillOverDue = isBillOverDue;
	}

	public String getIsBillPaid() {
		return isBillPaid;
	}

	public void setIsBillPaid(String isBillPaid) {
		this.isBillPaid = isBillPaid;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public AppUserModel getCustomerAppUserModel() {
		return customerAppUserModel;
	}

	public void setCustomerAppUserModel(AppUserModel customerAppUserModel) {
		this.customerAppUserModel = customerAppUserModel;
	}

	public String getUserOTP() {
		return userOTP;
	}

	public void setUserOTP(String userOTP) {
		this.userOTP = userOTP;
	}

	public Long getProductID() {
		return ProductID;
	}

	public void setProductID(Long productID) {
		ProductID = productID;
	}

	public boolean isPrepaidLoad() {
		return isPrepaidLoad;
	}

	public void setPrepaidLoad(boolean prepaidLoad) {
		isPrepaidLoad = prepaidLoad;
	}

	public String getGovPaymentType() {
		return govPaymentType;
	}

	public void setGovPaymentType(String govPaymentType) {
		this.govPaymentType = govPaymentType;
	}
}
