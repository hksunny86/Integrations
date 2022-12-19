/**
 * 
 */
package com.inov8.microbank.webapp.action.allpayweb.formbean;

import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_CAMT;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_CSCD;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_CUST_CODE;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_DEVICE_TYPE_ID;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_PIN;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_PROD_ID;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_STATEMENTS;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_TXAM;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_WALKIN_SENDER_CNIC;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.inov8.microbank.webapp.action.allpayweb.utils.AgentWeb;

/**
 * @author kashefbasher
 *
 */
public class AgentWebFormBean implements Serializable {

	public final static String CLASS_NAME = "agentWebFormBean";
	
	private static final long serialVersionUID = 1L;

	public AgentWebFormBean() {}

	private String bankPin = null;
	private Long deviceTypeId = null;
	private String customerId = null;
	private Long amount = null;
	private String walkInSenderMobileNumber = null;
	private String walkInSenderCnic = null;
	private Long productId = null;
	private String productName = null;
	private String commissionAmountTotal = null;
	private String consumerNumber = null;
	private String miscInfo = null;


	private ConcurrentHashMap<String, Object> parameterMap = new ConcurrentHashMap<String, Object>(0);
	
	@AgentWeb(parameterName=KEY_DEVICE_TYPE_ID) 
    public Long getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	@AgentWeb(parameterName=KEY_PIN) 
	public String getBankPin() {
		return bankPin;
	}
	public void setBankPin(String bankPin) {
		this.bankPin = bankPin;
	}
	
	@AgentWeb(parameterName=KEY_CUST_CODE) 
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@AgentWeb(parameterName=KEY_TXAM) 
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	
	@AgentWeb(parameterName=KEY_WALKIN_SENDER_MSISDN) 
	public String getWalkInSenderMobileNumber() {
		return walkInSenderMobileNumber;
	}
	public void setWalkInSenderMobileNumber(String walkInSenderMobileNumber) {
		this.walkInSenderMobileNumber = walkInSenderMobileNumber;
	}

	@AgentWeb(parameterName=KEY_WALKIN_SENDER_CNIC) 
	public String getWalkInSenderCnic() {
		return walkInSenderCnic;
	}
	public void setWalkInSenderCnic(String walkInSenderCnic) {
		this.walkInSenderCnic = walkInSenderCnic;
	}
	
	public ConcurrentHashMap<String, Object> getParameterMap() {
		return parameterMap;
	}
	public void setParameterMap(ConcurrentHashMap<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}
	@AgentWeb(parameterName=KEY_PROD_ID) 
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	@AgentWeb(parameterName="PNAME") 
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	@AgentWeb(parameterName=KEY_CAMT) 
	public String getCommissionAmountTotal() {
		return commissionAmountTotal;
	}
	public void setCommissionAmountTotal(String commissionAmountTotal) {
		this.commissionAmountTotal = commissionAmountTotal;
	}

	@AgentWeb(parameterName=KEY_CSCD) 
	public String getConsumerNumber() {
		return consumerNumber;
	}
	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}
	
	@AgentWeb(parameterName=KEY_STATEMENTS) 
	public String getMiscInfo() {
		return miscInfo;
	}
	public void setMiscInfo(String miscInfo) {
		this.miscInfo = miscInfo;
	}

}
