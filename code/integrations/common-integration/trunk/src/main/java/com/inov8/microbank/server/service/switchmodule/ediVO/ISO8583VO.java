package com.inov8.microbank.server.service.switchmodule.ediVO;

import java.io.Serializable;
import java.util.Date;

public class ISO8583VO implements Serializable{
	private String receivingAcctNo = null ;
	private String receivingAcctType = "00" ;
	private String processingCode = null ;
	private String amount = "0.0" ;
	private String transDateTime = null ;
	private String sysTraceAuditNo = null ;
	private String merchantType = null ;
	private String posEntryMode = null ;
	private String localTransDate = null ;
	private String localTransTime = null ;
	private String settlementDate = null ;
	private String acquirInstCode = null ;
	private String track2Data = null ;
	private String cardAcceptorId = null ;
	private String cardAcceptorName = null ;
	private String responseCode = null ;
	private String payingAccNo = null ;
	private String payingAccType = "00" ;	
	private String transactionCode = null ;
	private String microbankTransCodeId = null ;
	private String transactionType = null ;
	private String utilityCompanyCode = null ;
	private String consumerNo = null ;
	private String transTime = null ;
	private String additionalAmount = null ;
	private String sourceAccount = null ;
	private Date requestDate = null ;
	private String tPin = null;
	private Boolean isTPinValid = Boolean.TRUE; 
	
	private byte [] messageBytes = null ;
	private Boolean orphanResponse = new Boolean(false) ;
	private Boolean isValid = Boolean.TRUE;
	private Long vendorId = null;
	private String expDescription = null ;
	private String MTI = null ;
	private Date date = null ;
	
	private String mfsId;
	  
	
	/**
	 * 
	 */
	private String currencyCode = null ;
	
	
	 void reset () {
	 receivingAcctNo = null ;
	 receivingAcctType = "00" ;
	 processingCode = null ;
	 amount = "0.0" ;
	 transDateTime = null ;
	 sysTraceAuditNo = null ;
	 merchantType = null ;
	 posEntryMode = null ;
	 localTransDate = null ;
	 localTransTime = null ;
	 settlementDate = null ;
	 acquirInstCode = null ;
	 track2Data = null ;
	  cardAcceptorId = null ;
	  cardAcceptorName = null ;
	  responseCode = null ;
	  payingAccNo = null ;
	  payingAccType = "00" ;	
	  transactionCode = null ;
	  microbankTransCodeId = null ;
	  transactionType = null ;
	  utilityCompanyCode = null ;
	  consumerNo = null ;
	  transTime = null ;
	  tPin = null;
	  isTPinValid = Boolean.TRUE;
	  messageBytes = null ;
	  orphanResponse = new Boolean(false) ;
      isValid = Boolean.TRUE;
      vendorId = null;
	  currencyCode = null ;
		
	}
	
	public String getAcquirInstCode() {
		return acquirInstCode;
	}
	public void setAcquirInstCode(String acquirInstCode) {
		this.acquirInstCode = acquirInstCode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCardAcceptorId() {
		return cardAcceptorId;
	}
	public void setCardAcceptorId(String cardAcceptorId) {
		this.cardAcceptorId = cardAcceptorId;
	}
	public String getCardAcceptorName() {
		return cardAcceptorName;
	}
	public void setCardAcceptorName(String cardAcceptorName) {
		this.cardAcceptorName = cardAcceptorName;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getLocalTransDate() {
		return localTransDate;
	}
	public void setLocalTransDate(String localTransDate) {
		this.localTransDate = localTransDate;
	}
	public String getMerchantType() {
		return merchantType;
	}
	public void setMerchantType(String mechantType) {
		this.merchantType = mechantType;
	}
	public String getPosEntryMode() {
		return posEntryMode;
	}
	public void setPosEntryMode(String posEntryMode) {
		this.posEntryMode = posEntryMode;
	}
	public String getMicrobankTransCodeId() {
		return microbankTransCodeId;
	}
	public void setMicrobankTransCodeId(String microbankTransCodeId) {
		this.microbankTransCodeId = microbankTransCodeId;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getProcessingCode() {
		return processingCode;
	}
	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}
	public String getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}
	public String getSysTraceAuditNo() {
		return sysTraceAuditNo;
	}
	public void setSysTraceAuditNo(String sysTraceAuditNo) {
		this.sysTraceAuditNo = sysTraceAuditNo;
	}
	public String getTrack2Data() {
		return track2Data;
	}
	public void setTrack2Data(String track2Data) {
		this.track2Data = track2Data;
	}
	public String getTransDateTime() {
		return transDateTime;
	}
	public void setTransDateTime(String transDateTime) {
		this.transDateTime = transDateTime;
	}
	public String getLocalTransTime() {
		return localTransTime;
	}
	public void setLocalTransTime(String localTransTime) {
		this.localTransTime = localTransTime;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public String getPayingAccNo() {
		return payingAccNo;
	}
	public void setPayingAccNo(String payingAccNo) {
		this.payingAccNo = payingAccNo;
	}
	public String getReceivingAcctNo() {
		return receivingAcctNo;
	}
	public void setReceivingAcctNo(String receivingAcctNo) {
		this.receivingAcctNo = receivingAcctNo;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getConsumerNo() {
		return consumerNo;
	}
	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}
	public String getUtilityCompanyCode() {
		return utilityCompanyCode;
	}
	public void setUtilityCompanyCode(String utilityCompanyCode) {
		this.utilityCompanyCode = utilityCompanyCode;
	}
	public String getPayingAccType() {
		return payingAccType;
	}
	public void setPayingAccType(String payingAccType) {
		this.payingAccType = payingAccType;
	}
	public String getReceivingAcctType() {
		return receivingAcctType;
	}
	public void setReceivingAcctType(String receivingAcctType) {
		this.receivingAcctType = receivingAcctType;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public byte[] getMessageBytes() {
		return messageBytes;
	}
	public void setMessageBytes(byte[] messageBytes) {
		this.messageBytes = messageBytes;
	}
	
	public Boolean isOrphanResponse()
	{
		return orphanResponse;
	}
	
	public void setOrphanResponse(Boolean orphanResponse)
	{
		this.orphanResponse = orphanResponse;
	}
	
	public Boolean getIsValid()
	{
		return isValid;
	}
	public void setIsValid(Boolean isValid)
	{
		this.isValid = isValid;
	}
	
	public Long getVendorId()
	{
		return vendorId;
	}
	
	public void setVendorId(Long vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getExpDescription() {
		return expDescription;
	}

	public void setExpDescription(String expDescription) {
		this.expDescription = expDescription;
	}

	public String getMTI() {
		return MTI;
	}

	public void setMTI(String mti) {
		MTI = mti;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAdditionalAmount() {
		return additionalAmount;
	}

	public void setAdditionalAmount(String additionalAmount) {
		this.additionalAmount = additionalAmount;
	}

	public String getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	

	public Boolean getIsTPinValid()
	{
		return isTPinValid;
	}

	public void setIsTPinValid(Boolean isTPinValid)
	{
		this.isTPinValid = isTPinValid;
	}

	public String getTPin()
	{
		return tPin;
	}

	public void setTPin(String tPin)
	{
		this.tPin = tPin;
	}

	
	public String getMfsId()
	{
		return mfsId;
	}

	
	public void setMfsId(String mfsId)
	{
		this.mfsId = mfsId;
	}

}
