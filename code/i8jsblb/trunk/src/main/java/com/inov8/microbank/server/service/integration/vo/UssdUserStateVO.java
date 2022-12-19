package com.inov8.microbank.server.service.integration.vo;

public class UssdUserStateVO {
	
	
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
	
	private String CAMT;
	private String TAMT;
	private String TPAM;
	private String BAMT;
	private String A1CAMT;
	private String A2CAMT;
	private String TXAM;
	private boolean changePinRequired=false;
	
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
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
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
	public String getUserMsisdn() {
		return userMsisdn;
	}
	public void setUserMsisdn(String userMsisdn) {
		this.userMsisdn = userMsisdn;
	}
	public boolean isInputRequired() {
		return inputRequired;
	}
	public void setInputRequired(boolean inputRequired) {
		this.inputRequired = inputRequired;
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
	public String getCwTotalAmountForCustomer() {
		return cwTotalAmountForCustomer;
	}
	public void setCwTotalAmountForCustomer(String cwTotalAmountForCustomer) {
		this.cwTotalAmountForCustomer = cwTotalAmountForCustomer;
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
	public boolean isChangePinRequired() {
		return changePinRequired;
	}
	public void setChangePinRequired(boolean changePinRequired) {
		this.changePinRequired = changePinRequired;
	}

}
