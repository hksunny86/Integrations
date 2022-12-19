package com.inov8.microbank.server.service.integration.vo;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.io.Serializable;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;

/**
 * 
 * @author Kashif Bashir
 *
 */

public class DonationPaymentVO implements BillPaymentVO, Serializable {

	private static final long serialVersionUID = 6219262453169387336L;
	
	public DonationPaymentVO() {}
	
	private String customerId;
	private String customerMobileNumber;
	private Double amount;
	private String paidDate;
	private String transactionCode;
	private String recepientName;
	private Long appUserId;

	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getRecepientName() {
		return recepientName;
	}

	public void setRecepientName(String recepientName) {
		this.recepientName = recepientName;
	}

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public static String createParameterTag(String name, String value) {
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		
		.append(name)
		
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		
		.append(value)
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE);		
		
		return strBuilder.toString();
	}	
	
	public String responseXML() {
		
		StringBuilder responseXML = new StringBuilder();
	
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_TXAM, amount.toString()));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_CUST_CODE, this.customerId));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN, this.customerMobileNumber));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, transactionCode));
		
		return responseXML.toString();
	}

	@Override
	public void setResponseCode(String responseCode) {
		
	}

	@Override
	public String getResponseCode() {
		return null;
	}

	@Override
	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper) {
		
		
		this.setAmount((Double.valueOf((String)baseWrapper.getObject(CommandFieldConstants.KEY_TXAM))));
		this.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		this.setBillAmount((Double.valueOf((String)baseWrapper.getObject(CommandFieldConstants.KEY_TXAM))));
		this.setConsumerNo((String)baseWrapper.getObject(CommandFieldConstants.KEY_TXAM));
		this.setCustomerId((String)baseWrapper.getObject(CommandFieldConstants.KEY_TXAM));
		this.setCustomerMobileNumber((String)baseWrapper.getObject(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN));
		this.setPaidDate(new java.util.Date().toString());		
		
		return this;
	}

	@Override
	public void validateVO(ProductVO productVO) throws FrameworkCheckedException {
		
	}

	@Override
	public Double getCurrentBillAmount() {
		return null;
	}

	@Override
	public Double getBillAmount() {
		return null;
	}

	@Override
	public void setBillAmount(Double billAmount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConsumerNo(String consumerNo) {
		
	}

	@Override
	public String getConsumerNo() {
		return null;
	}

}