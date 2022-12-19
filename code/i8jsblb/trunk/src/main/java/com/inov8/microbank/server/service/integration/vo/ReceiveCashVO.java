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
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.commandmodule.ReceiveCashCommand;

public class ReceiveCashVO implements ProductVO {

	private String productId = null;
	private String accountId = null;
	private String deviceTypeId = null;
	private String bankId = null;
	private Double txAmount = 0.0;	
	private Double commissionAmount = 0.0;	
	private Double totalAmount = 0.0;
	private Double balance = null;

	private ProductModel productModel;

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

	
	public Double getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(Double txAmount) {
		this.txAmount = txAmount;
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
	}

	public String getResponseCode() {
		
		return null;
	}

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper) {

		BaseCommand command = new ReceiveCashCommand();	

		ReceiveCashVO receiveMoneyVO = (ReceiveCashVO) productVO;
		
    	String _txAmount = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
    	
    	if(!StringUtil.isNullOrEmpty(_txAmount)) {

        	txAmount = Double.valueOf(_txAmount);	
    	}	
		productId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		deviceTypeId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		bankId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		
		toString();
		
		receiveMoneyVO.setProductId(productId);
		receiveMoneyVO.setAccountId(accountId);
		receiveMoneyVO.setDeviceTypeId(deviceTypeId);
		receiveMoneyVO.setTxAmount(txAmount);
		
		return receiveMoneyVO;
	}

	public void validateVO(ProductVO productVO) throws FrameworkCheckedException {
	
		
	}
	
	public ProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
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
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, Formatter.formatDouble(totalAmount)));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, Formatter.formatDouble(txAmount)));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, Formatter.formatDouble(commissionAmount)));
		responseXML.append(createParameterTag(CommandFieldConstants.KEY_DED_AMT, Formatter.formatDouble(commissionAmount)));
		
		return responseXML.toString();
	}
	
	public String toString() {
		
		StringBuilder toString = new StringBuilder();
		
		toString.append("productId : "+productId);
		toString.append(", accountId : "+accountId);
		toString.append(", deviceTypeId : "+deviceTypeId);
		toString.append(", bankId : "+bankId);
		toString.append(", txAmount : "+txAmount);
		
		System.out.println(toString.toString());
		
		return toString.toString();
	}
	
}
