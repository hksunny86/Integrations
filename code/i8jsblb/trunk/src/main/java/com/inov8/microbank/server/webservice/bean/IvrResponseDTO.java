package com.inov8.microbank.server.webservice.bean;

public class IvrResponseDTO {
	
	private String customerMobileNo;
	private String transactionId;
	private String agentMobileNo;
	private Double amount;
	private Double charges;
	private String pin;
	private String agentId;
	private Long productId;
	private Integer retryCount;
	private Boolean isPinAuthenticated;
	private Boolean isCredentialsExpired;
	private String newPin;
	private String handlerMobileNo;
	
	
	public String getCustomerMobileNo() {
		return customerMobileNo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public String getAgentMobileNo() {
		return agentMobileNo;
	}
	public Double getAmount() {
		return amount;
	}
	public Double getCharges() {
		return charges;
	}
	public String getPin() {
		return pin;
	}
	public void setCustomerMobileNo(String customerMobileNo) {
		this.customerMobileNo = customerMobileNo;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public void setCharges(Double charges) {
		this.charges = charges;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public Boolean getIsPinAuthenticated() {
		return isPinAuthenticated;
	}
	public Boolean getIsCredentialsExpired() {
		return isCredentialsExpired;
	}
	public void setIsPinAuthenticated(Boolean isPinAuthenticated) {
		this.isPinAuthenticated = isPinAuthenticated;
	}
	public void setIsCredentialsExpired(Boolean isCredentialsExpired) {
		this.isCredentialsExpired = isCredentialsExpired;
	}
	public String getAgentId() {
		return agentId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getNewPin() {
		return newPin;
	}
	public void setNewPin(String newPin) {
		this.newPin = newPin;
	}
	public String getHandlerMobileNo() {
		return handlerMobileNo;
	}
	public void setHandlerMobileNo(String handlerMobileNo) {
		this.handlerMobileNo = handlerMobileNo;
	}

}
