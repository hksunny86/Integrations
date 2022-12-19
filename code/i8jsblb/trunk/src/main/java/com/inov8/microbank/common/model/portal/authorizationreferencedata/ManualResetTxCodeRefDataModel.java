package com.inov8.microbank.common.model.portal.authorizationreferencedata;


public class ManualResetTxCodeRefDataModel {
	
	private String transactionId;
	private String oneTimePin;
	private String comments;
	
	
	public ManualResetTxCodeRefDataModel() {		
	}
	
	public ManualResetTxCodeRefDataModel(String transactionId,String oneTimePin, String comments) {
		this.transactionId = transactionId;
		this.oneTimePin = oneTimePin;
		this.comments = comments;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getOneTimePin() {
		return oneTimePin;
	}
	public void setOneTimePin(String oneTimePin) {
		this.oneTimePin = oneTimePin;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}
