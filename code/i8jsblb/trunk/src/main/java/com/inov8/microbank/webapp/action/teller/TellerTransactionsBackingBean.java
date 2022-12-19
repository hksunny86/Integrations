package com.inov8.microbank.webapp.action.teller;

import com.inov8.microbank.common.model.AppUserModel;

public class TellerTransactionsBackingBean {
	
	public static final String BEAN_NAME = "tellerTransactionsBackingBean";
	private static final long serialVersionUID = 1L;

	private String userName = null;
	private String userType = null; ;
	private String totalAmount = null;
	private String commisttionAmount = null;
	private String cnic = null;
	private String amountFormated = null;
	private String senderMobileNumber = null;
	private String transactionCode = null;
	private String transactionDate = null;
	private String transactionTime = null;	
	private String productName = null;

	private AppUserModel appUserModel;
	private Double amount;
	private Long productId;

	public TellerTransactionsBackingBean() {
		
		this.appUserModel = new AppUserModel();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getAmountFormated() {
		return amountFormated;
	}

	public void setAmountFormated(String amountFormated) {
		this.amountFormated = amountFormated;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCommisttionAmount() {
		return commisttionAmount;
	}

	public void setCommisttionAmount(String commisttionAmount) {
		this.commisttionAmount = commisttionAmount;
	}

	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public AppUserModel getAppUserModel() {
		return appUserModel;
	}

	public void setAppUserModel(AppUserModel appUserModel) {
		this.appUserModel = appUserModel;
	}
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public String getSenderMobileNumber() {
		return senderMobileNumber;
	}

	public void setSenderMobileNumber(String senderMobileNumber) {
		this.senderMobileNumber = senderMobileNumber;
	}
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
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
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}