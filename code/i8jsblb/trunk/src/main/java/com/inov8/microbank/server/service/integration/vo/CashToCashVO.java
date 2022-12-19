package com.inov8.microbank.server.service.integration.vo;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.commandmodule.CashToCashCommand;

public class CashToCashVO implements ProductVO {

	private String productId = null;
	private String accountId = null;
	private String deviceTypeId = null;
	private String agentMobileNumber = null;
	private String bankId = null;
	private Long walkInCustomerId = null;
	private String walkinSenderMobile = null;
	private String recepientWalkinMobile = null;
	private String recepientWalkinCNIC = null;
	private String walkinSenderCNIC = null;	
	private Double txAmount = 0.0;	
	private Double commissionAmount = 0.0;	

	private Double totalAmount = 0.0;
	private String walkInCNIC = null;
	private Double balance = null;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getAgentMobileNumber() {
		return agentMobileNumber;
	}

	public void setAgentMobileNumber(String agentMobileNumber) {
		this.agentMobileNumber = agentMobileNumber;
	}

	public String getWalkinSenderCNIC() {
		return walkinSenderCNIC;
	}

	public void setWalkinSenderCNIC(String walkinSenderCNIC) {
		this.walkinSenderCNIC = walkinSenderCNIC;
	}

	public String getWalkinSenderMobile() {
		return walkinSenderMobile;
	}

	public void setWalkinSenderMobile(String walkinSenderMobile) {
		this.walkinSenderMobile = walkinSenderMobile;
	}

	public String getRecepientWalkinCNIC() {
		return recepientWalkinCNIC;
	}

	public void setRecepientWalkinCNIC(String recepientWalkinCNIC) {
		this.recepientWalkinCNIC = recepientWalkinCNIC;
	}

	public String getRecepientWalkinMobile() {
		return recepientWalkinMobile;
	}

	public void setRecepientWalkinMobile(String recepientWalkinMobile) {
		this.recepientWalkinMobile = recepientWalkinMobile;
	}

	public Long getWalkInCustomerId() {
		return walkInCustomerId;
	}

	public void setWalkInCustomerId(Long walkInCustomerId) {
		this.walkInCustomerId = walkInCustomerId;
	}	
	
	public Double getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(Double txAmount) {
		this.txAmount = txAmount;
	}

	public String getWalkInCNIC() {
		return walkInCNIC;
	}

	public void setWalkInCNIC(String walkInCNIC) {
		this.walkInCNIC = walkInCNIC;
	}
	
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(Double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	
	
	
	
	
	
	
	
	public void setResponseCode(String responseCode) {
		// TODO Auto-generated method stub

	}

	public String getResponseCode() {
		
		return null;
	}

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper) {

		BaseCommand command = new CashToCashCommand();	

		CashToCashVO cashToCashVO = (CashToCashVO) productVO;
		
    	String _txAmount = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
    	txAmount = Double.valueOf(_txAmount);
		
		productId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		deviceTypeId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		agentMobileNumber = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        recepientWalkinCNIC = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC);
		walkinSenderMobile = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN);
    	recepientWalkinMobile = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN);
		walkinSenderCNIC = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_CNIC);
		bankId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		
//		walkInCustomerId = ThreadLocalAppUser.getAppUserModel().getWalkinCustomerModel().getWalkinCustomerId();
//		walkInCNIC = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
		
		toString();
		
		cashToCashVO.setProductId(productId);
		cashToCashVO.setAccountId(accountId);
		cashToCashVO.setDeviceTypeId(deviceTypeId);
		cashToCashVO.setAgentMobileNumber(agentMobileNumber);
		cashToCashVO.setWalkInCustomerId(walkInCustomerId);
		cashToCashVO.setWalkinSenderCNIC(walkinSenderCNIC);
		cashToCashVO.setRecepientWalkinCNIC(recepientWalkinCNIC);
		cashToCashVO.setWalkinSenderMobile(walkinSenderMobile);
		cashToCashVO.setWalkInCustomerId(walkInCustomerId);	
		cashToCashVO.setTxAmount(txAmount);
		cashToCashVO.setWalkInCNIC(walkInCNIC);
		
		return this;
	}

	public void validateVO(ProductVO productVO) throws FrameworkCheckedException {
	
		
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

		responseXML.append(createParameterTag(CommandFieldConstants.KEY_CNIC, walkinSenderCNIC));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_CUST_CODE, recepientWalkinCNIC));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, Formatter.formatDouble(totalAmount)));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, Formatter.formatDouble(txAmount)));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, Formatter.formatDouble(commissionAmount)));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_DED_AMT, Formatter.formatDouble(commissionAmount)));
		
		return responseXML.toString();
	}
	
	public String toString() {
		
		StringBuilder toString = new StringBuilder();
		toString.append("[CashToCashVO.toString] : ");
		toString.append("productId : "+productId);
		toString.append(", accountId : "+accountId);
		toString.append(", deviceTypeId : "+deviceTypeId);
		toString.append(", agentMobileNumber : "+agentMobileNumber);
		toString.append(", walkinSenderCNIC : "+walkinSenderCNIC);
		toString.append(", walkinSenderMobile : "+walkinSenderMobile);
		toString.append(", recepientWalkinCNIC : "+recepientWalkinCNIC);
		toString.append(", recepientWalkinMobile : "+recepientWalkinMobile);
		toString.append(", bankId : "+bankId);
		toString.append(", walkInCustomerId : "+walkInCustomerId);
		toString.append(", walkInCNIC : "+walkInCNIC);
		
		System.out.println(toString.toString());
		
		return toString.toString();
	}
	
}
