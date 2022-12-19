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
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;

/**
 * 
 * @author Kashif Bashir
 *
 */

public class BulkBillPaymentVO implements BillPaymentVO, Serializable {

	private static final long serialVersionUID = 6219262453169387336L;
	public static final String BulkBillPaymentVO = "BulkBillPaymentVO";
	
	private String consumerNumber;
	private String mobileNumber;
	private Long productId;
	private Double billAmount;

	private String transactionCode;
	private String supProcessingStatus;
	private String productName;

	public BulkBillPaymentVO() {}
	
	public BulkBillPaymentVO(String consumerNumber, String mobileNumber, Long productId, Double billAmount) {
		
		this.consumerNumber = consumerNumber;
		this.mobileNumber = mobileNumber;
		this.productId = productId;
		this.billAmount = billAmount;
		this.supProcessingStatus = "FAILED";
	}

	public String getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}	

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_TXAM, billAmount.toString()));
		
		return responseXML.toString();
	}

	@Override
	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper) {
		
		
		this.setBillAmount((Double.valueOf((String)baseWrapper.getObject(CommandFieldConstants.KEY_TXAM))));
		this.setConsumerNo((String)baseWrapper.getObject(CommandFieldConstants.KEY_TXAM));
		this.setMobileNumber((String)baseWrapper.getObject(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN));
		
		return this;
	}

	@Override
	public void validateVO(ProductVO productVO) throws FrameworkCheckedException {
		
	}

	@Override
	public Double getBillAmount() {
		return this.billAmount;
	}

	@Override
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	@Override
	public void setConsumerNo(String consumerNo) {
		this.consumerNumber = consumerNo;
	}

	@Override
	public String getConsumerNo() {
		return this.consumerNumber;
	}
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getSupProcessingStatus() {
		return supProcessingStatus;
	}

	public void setSupProcessingStatus(String supProcessingStatus) {
		this.supProcessingStatus = supProcessingStatus;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public void setResponseCode(String responseCode) {
		
	}

	@Override
	public String getResponseCode() {

		return null;
	}

	@Override
	public Double getCurrentBillAmount() {

		return null;
	}
}