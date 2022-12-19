package com.inov8.integration.inbound.sms.bean;

public class InboundSMSServiceBean {
	
	private String username;
	private String password;
	private String senderMSISDN;
	private String transactionID;
	private String smsText;
	private String responseCode;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSenderMSISDN() {
		return senderMSISDN;
	}
	public void setSenderMSISDN(String senderMSISDN) {
		this.senderMSISDN = senderMSISDN;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	
}
