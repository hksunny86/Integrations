package com.inov8.microbank.common.model.portal.authorizationreferencedata;

public class ResendSmsRefDataModel {
	
	private Long transactionId;
	private String resendSmsStrategy;
	private String appUserId;
	private String transactionCode;
	
	public ResendSmsRefDataModel(Long transactionId,String resendSmsStrategy, String appUserId,String transactionCode) {

		this.transactionId = transactionId;
		this.resendSmsStrategy = resendSmsStrategy;
		this.appUserId = appUserId;
		this.transactionCode = transactionCode;
	}
	
	
	public ResendSmsRefDataModel() {
	}


	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String getResendSmsStrategy() {
		return resendSmsStrategy;
	}
	public void setResendSmsStrategy(String resendSmsStrategy) {
		this.resendSmsStrategy = resendSmsStrategy;
	}
	public String getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
}
