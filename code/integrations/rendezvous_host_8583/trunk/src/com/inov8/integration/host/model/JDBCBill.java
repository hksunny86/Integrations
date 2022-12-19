package com.inov8.integration.host.model;


public class JDBCBill {
	private String utilityCompanyId;
	private String consumerNumber;
	private String subscriberName;
	private String billingMonth;
	private String dueDatePayableAmount;
	private String paymentDueDate;
	private String paymentAfterDueDate;
	private String billStatus;
	private String paymentAuthResponseId;
	private String netCED;
	private String netWithholdingTAX;
	private String responseCode;
	private long ttlRequest;

	public String getUtilityCompanyId() {
		return utilityCompanyId;
	}

	public void setUtilityCompanyId(String utilityCompanyId) {
		this.utilityCompanyId = utilityCompanyId;
	}

	public String getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
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

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public long getTtlRequest() {
		return ttlRequest;
	}

	public void setTtlRequest(long ttlRequest) {
		this.ttlRequest = ttlRequest;
	}
}
