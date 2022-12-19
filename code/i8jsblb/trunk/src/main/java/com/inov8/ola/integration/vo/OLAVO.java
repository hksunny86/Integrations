package com.inov8.ola.integration.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.inov8.framework.common.model.SortingOrder;




public class OLAVO implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6848266438004001194L;
	private Double balance ;
	private String authCode;
    private String mfsId;
	private String microbankTransactionCode;
	private String payingAccNo;
	private String receivingAccNo;
	private String responseCode;
	private String transactionTypeId;
	private Date transactionDateTime;
	private String address;
	private String mobileNumber;
	private Long accountHolderId;
	private String firstName;
	private String lastName;
	private String middleName;
	private String cnic;
	private String fatherName;
	private Boolean active;
	private Boolean deleted;
	private String landlineNumber;
	private Date dob;
	private Long reasonId;
	private Long statusId ;
	private Long accountId ;
	private String statusName;
	
	private Long balanceDisbursed;
	private Long balanceReceived;
	private String endDayBalance;
	private String startDayBalance;
	private Date statsDate;
	private Long dailyAccountStatsId;
	
	private Date statsStartDate;
	private Date statsEndDate;
	private boolean isBillPayment;
	private Long customerAccountTypeId;
	private boolean requiresNewTrx;
	private boolean requiresNoCrypto;
	
	private Long receivingAccountId;
	private Double toBalanceAfterTransaction;
	private Double transactionAmount;
	private Double commissionAmount;
	private Double transactionProcessingAmount;
	private Double totalAmount;
	private Double fromBalanceAfterTransaction;
	private Long ledgerId;
	private Boolean isReversal;
	private Long productId;
	private Long fromSegmentId;
	private Long toSegmentId;
	private Boolean isCreditOnly = Boolean.FALSE;
	private Long accountCreditQueueLogId = null;

	private LinkedHashMap<String, SortingOrder> sortingOrderMap ;
	private List<OLAInfo> debitAccountList = new ArrayList<OLAInfo>(0);
	private List<OLAInfo> creditAccountList = new ArrayList<OLAInfo>(0); ;
	private Boolean excludeLimit;
	private Boolean isCreditPush = Boolean.FALSE;
	private Boolean isDebitPush = Boolean.FALSE;
    private Boolean isCustomerAccountType = Boolean.FALSE;
	private Long handlerId;
	private Long handlerAccountTypeId;
	private Boolean handlerExcludeLimit;
	private Long category;
	private Long commissionType;
	private Boolean isViaQueue = Boolean.FALSE;
	private boolean excludeInProcessTx = Boolean.FALSE;
	private Map<String, Object> responseCodeMap = new HashMap<>(2);
	private Boolean checkIfLimitApplicable;
	private List<OLAInfo> accountList = new ArrayList<OLAInfo>(0);
	private String deviceTypeId;
	private String receiverMobileNumber;
	private String receiverCnic;
	private boolean isNegativeBalanceAllowed = false;


	// for agent assisted transactions
	private Double agentBalanceAfterTransaction;
	
	public Date getStatsStartDate()
	{
		return statsStartDate;
	}

	public void setStatsStartDate(Date statsStartDate)
	{
		this.statsStartDate = statsStartDate;
	}

	public Date getStatsEndDate()
	{
		return statsEndDate;
	}

	public void setStatsEndDate(Date statsEndDate)
	{
		this.statsEndDate = statsEndDate;
	}

	public Long getAccountHolderId()
	{
		return accountHolderId;
	}
	
	public void setAccountHolderId(Long accountHolderId)
	{
		this.accountHolderId = accountHolderId;
	}
	
	public Boolean getActive()
	{
		return active;
	}
	
	public void setActive(Boolean active)
	{
		this.active = active;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getAuthCode()
	{
		return authCode;
	}
	
	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}
	
	public Double getBalance()
	{
		return balance;
	}
	
	public void setBalance(Double balance)
	{
		this.balance = balance;
	}
	
	public String getCnic()
	{
		return cnic;
	}
	
	public void setCnic(String cnic)
	{
		this.cnic = cnic;
	}
	
	public Boolean getDeleted()
	{
		return deleted;
	}
	
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}
	
	public Date getDob()
	{
		return dob;
	}
	
	public void setDob(Date dob)
	{
		this.dob = dob;
	}
	
	public String getFatherName()
	{
		return fatherName;
	}
	
	public void setFatherName(String fatherName)
	{
		this.fatherName = fatherName;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getMfsId()
	{
		return mfsId;
	}
	
	public void setMfsId(String mfsId)
	{
		this.mfsId = mfsId;
	}
	
	public String getMicrobankTransactionCode()
	{
		return microbankTransactionCode;
	}
	
	public void setMicrobankTransactionCode(String microbankTransactionCode)
	{
		this.microbankTransactionCode = microbankTransactionCode;
	}
	
	public String getLandlineNumber()
	{
		return landlineNumber;
	}
	
	public void setLandlineNumber(String landlineNumber)
	{
		this.landlineNumber = landlineNumber;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getMiddleName()
	{
		return middleName;
	}
	
	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}
	
	public String getMobileNumber()
	{
		return mobileNumber;
	}
	
	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}
	
	public String getPayingAccNo()
	{
		return payingAccNo;
	}
	
	public void setPayingAccNo(String payingAccNo)
	{
		this.payingAccNo = payingAccNo;
	}
	
	public Long getReasonId()
	{
		return reasonId;
	}
	
	public void setReasonId(Long reasonId)
	{
		this.reasonId = reasonId;
	}
	
	public String getReceivingAccNo()
	{
		return receivingAccNo;
	}
	
	public void setReceivingAccNo(String receivingAccNo)
	{
		this.receivingAccNo = receivingAccNo;
	}
	
	public String getResponseCode()
	{
		return responseCode;
	}
	
	public void setResponseCode(String responseCode)
	{
		this.responseCode = responseCode;
	}
	
	public Date getTransactionDateTime()
	{
		return transactionDateTime;
	}
	
	public void setTransactionDateTime(Date transactionDateTime)
	{
		this.transactionDateTime = transactionDateTime;
	}
	
	public String getTransactionTypeId()
	{
		return transactionTypeId;
	}
	
	public void setTransactionTypeId(String transactionTypeId)
	{
		this.transactionTypeId = transactionTypeId;
	}

	
	public Long getStatusId()
	{
		return statusId;
	}

	
	public void setStatusId(Long statusId)
	{
		this.statusId = statusId;
	}

	
	public Long getAccountId()
	{
		return accountId;
	}

	
	public void setAccountId(Long accountId)
	{
		this.accountId = accountId;
	}

	
	public LinkedHashMap<String, SortingOrder> getSortingOrderMap()
	{
		return sortingOrderMap;
	}

	
	public void setSortingOrderMap(LinkedHashMap<String, SortingOrder> sortingOrderMap)
	{
		this.sortingOrderMap = sortingOrderMap;
	}

	
	public String getStatusName()
	{
		return statusName;
	}

	
	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}

	public Long getBalanceDisbursed()
	{
		return balanceDisbursed;
	}

	public void setBalanceDisbursed(Long balanceDisbursed)
	{
		this.balanceDisbursed = balanceDisbursed;
	}

	public Long getBalanceReceived()
	{
		return balanceReceived;
	}

	public void setBalanceReceived(Long balanceReceived)
	{
		this.balanceReceived = balanceReceived;
	}

	public String getEndDayBalance()
	{
		return endDayBalance;
	}

	public void setEndDayBalance(String endDayBalance)
	{
		this.endDayBalance = endDayBalance;
	}

	public String getStartDayBalance()
	{
		return startDayBalance;
	}

	public void setStartDayBalance(String startDayBalance)
	{
		this.startDayBalance = startDayBalance;
	}

	public Date getStatsDate()
	{
		return statsDate;
	}

	public void setStatsDate(Date statsDate)
	{
		this.statsDate = statsDate;
	}

	public Long getDailyAccountStatsId()
	{
		return dailyAccountStatsId;
	}

	public void setDailyAccountStatsId(Long dailyAccountStatsId)
	{
		this.dailyAccountStatsId = dailyAccountStatsId;
	}

	@Deprecated
	public boolean getIsBillPayment() {
		return isBillPayment;
	}

	@Deprecated
	public void setIsBillPayment(boolean isBillPayment) {
		this.isBillPayment = isBillPayment;
	}

	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}

	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}

	public boolean getRequiresNewTrx() {
		return requiresNewTrx;
	}

	public void setRequiresNewTrx(boolean requiresNewTrx) {
		this.requiresNewTrx = requiresNewTrx;
	}

	public boolean getRequiresNoCrypto() {
		return requiresNoCrypto;
	}

	public void setRequiresNoCrypto(boolean requiresNoCrypto) {
		this.requiresNoCrypto = requiresNoCrypto;
	}
	public Long getReceivingAccountId() {
		return receivingAccountId;
	}

	public void setReceivingAccountId(Long recievingAccountId) {
		this.receivingAccountId = recievingAccountId;
	}

	public Double getToBalanceAfterTransaction() {
		return toBalanceAfterTransaction;
	}

	public void setToBalanceAfterTransaction(Double toBalanceAfterTransaction) {
		this.toBalanceAfterTransaction = toBalanceAfterTransaction;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Double getFromBalanceAfterTransaction() {
		return fromBalanceAfterTransaction;
	}

	public void setFromBalanceAfterTransaction(Double fromBalanceAfterTransaction) {
		this.fromBalanceAfterTransaction = fromBalanceAfterTransaction;
	}

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public Boolean getIsReversal() {
		return (isReversal==null)?false:isReversal;
	}

	public void setIsReversal(Boolean isReversal) {
		this.isReversal = isReversal;
	}

	public Long getFromSegmentId() {
		return fromSegmentId;
	}

	public void setFromSegmentId(Long fromSegmentId) {
		this.fromSegmentId = fromSegmentId;
	}

	public Long getToSegmentId() {
		return toSegmentId;
	}

	public void setToSegmentId(Long toSegmentId) {
		this.toSegmentId = toSegmentId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public List<OLAInfo> getCreditAccountList() {
		return creditAccountList;
	}

	public void setCreditAccountList(List<OLAInfo> creditAccountList) {
		this.creditAccountList = creditAccountList;
	}

	public List<OLAInfo> getDebitAccountList() {
		return debitAccountList;
	}

	public void setDebitAccountList(List<OLAInfo> debitAccountList) {
		this.debitAccountList = debitAccountList;
	}

	public Boolean getExcludeLimit() {
		return excludeLimit;
	}

	public void setExcludeLimit(Boolean excludeLimit) {
		this.excludeLimit = excludeLimit;
	}
	
	public Boolean getIsCreditPush() {
		return isCreditPush;
	}

	public void setIsCreditPush(Boolean isCreditPush) {
		this.isCreditPush = isCreditPush;
	}
	
	public Boolean getIsDebitPush() {
		return isDebitPush;
	}

	public void setIsDebitPush(Boolean isDebitPush) {
		this.isDebitPush = isDebitPush;
	}

	public Double getAgentBalanceAfterTransaction() {
		return agentBalanceAfterTransaction;
	}

	public void setAgentBalanceAfterTransaction(Double agentBalanceAfterTransaction) {
		this.agentBalanceAfterTransaction = agentBalanceAfterTransaction;
	}

	
	public Boolean getIsCreditOnly() {
		return isCreditOnly;
	}

	public void setIsCreditOnly(Boolean isCreditOnly) {
		this.isCreditOnly = isCreditOnly;
	}
	public Boolean getIsCustomerAccountType() {
		return isCustomerAccountType;
	}

	public void setIsCustomerAccountType(Boolean isCustomerAccountType) {
		this.isCustomerAccountType = isCustomerAccountType;
	}
	
	public Long getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}
	
	public Long getHandlerAccountTypeId() {
		return handlerAccountTypeId;
	}

	public void setHandlerAccountTypeId(Long handlerAccountTypeId) {
		this.handlerAccountTypeId = handlerAccountTypeId;
	}
	
	public Boolean getHandlerExcludeLimit() {
		return handlerExcludeLimit;
	}

	public void setHandlerExcludeLimit(Boolean handlerExcludeLimit) {
		this.handlerExcludeLimit = handlerExcludeLimit;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public Long getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(Long commissionType) {
		this.commissionType = commissionType;
	}

	public Long getAccountCreditQueueLogId() {
	    return accountCreditQueueLogId;
	}

	public void setAccountCreditQueueLogId(Long accountCreditQueueLogId) {
	    this.accountCreditQueueLogId = accountCreditQueueLogId;
	}

	public Boolean getIsViaQueue() {
		return isViaQueue;
	}

	public void setIsViaQueue(Boolean isViaQueue) {
		this.isViaQueue = isViaQueue;
	}
	
	public List<OLAInfo> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<OLAInfo> accountList) {
		this.accountList = accountList;
	}
	
	
	public Map<String, Object> getResponseCodeMap() {
		return responseCodeMap;
	}

	public void setResponseCodeMap(Map<String, Object> responseCodeMap) {
		this.responseCodeMap = responseCodeMap;
	}
	
	public Boolean getCheckIfLimitApplicable(){
		return checkIfLimitApplicable;
	}

	public void setCheckIfLimitApplicable(Boolean checkIfLimitApplicable){
		this.checkIfLimitApplicable = checkIfLimitApplicable;
	}

	public boolean isExcludeInProcessTx() {
		return excludeInProcessTx;
	}

	public void setExcludeInProcessTx(boolean excludeInProcessTx) {
		this.excludeInProcessTx = excludeInProcessTx;
	}

	public String getDeviceTypeId() { return deviceTypeId; }

	public void setDeviceTypeId(String deviceTypeId) { this.deviceTypeId = deviceTypeId; }

	public Double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(Double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public Double getTransactionProcessingAmount() {
		return transactionProcessingAmount;
	}

	public void setTransactionProcessingAmount(Double transactionProcessingAmount) {
		this.transactionProcessingAmount = transactionProcessingAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getReceiverMobileNumber() {
		return receiverMobileNumber;
	}

	public void setReceiverMobileNumber(String receiverMobileNumber) {
		this.receiverMobileNumber = receiverMobileNumber;
	}

	public String getReceiverCnic() {
		return receiverCnic;
	}

	public void setReceiverCnic(String receiverCnic) {
		this.receiverCnic = receiverCnic;
	}

	public boolean isNegativeBalanceAllowed() {
		return isNegativeBalanceAllowed;
	}

	public void setNegativeBalanceAllowed(boolean negativeBalanceAllowed) {
		isNegativeBalanceAllowed = negativeBalanceAllowed;
	}
}
