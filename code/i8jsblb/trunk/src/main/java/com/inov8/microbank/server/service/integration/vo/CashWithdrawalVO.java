package com.inov8.microbank.server.service.integration.vo;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.commandmodule.CashToCashCommand;

public class CashWithdrawalVO implements ProductVO {

	private String productId;
	private String deviceTypeId;
	private String customerMobileNo, customerName;
	private String retailerName, mfsId;
	private Long retailerId, appUserId;
	private String agentMobileNo;
	private Double withdrawalAmount;
	private String encryptionType, CNIC, agentPIN;
	private String commissionAmount, processingAmount, totalAmount;
	private Double customerBalance, agentBalance;
	
	@Override
	public void setResponseCode(String responseCode) {
		
	}

	@Override
	public String getResponseCode() {
		return null;
	}

	@Override
	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper) {
		BaseCommand command = new CashToCashCommand();	

		if( baseWrapper.getObject( CommandFieldConstants.KEY_TXAM ) != null )
		{
	    	String txAmount = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
	    	withdrawalAmount = Double.valueOf(txAmount);
		}
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_TAMT ) != null )
		{
			totalAmount = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
		}
		
    	deviceTypeId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
	    
		customerMobileNo = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        agentMobileNo = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        
        if( baseWrapper.getObject( CommandFieldConstants.KEY_ENCRYPTION_TYPE ) != null )
		{
        	encryptionType = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		}
        
        CNIC= command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        agentPIN = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
    	commissionAmount = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAMT);
    	processingAmount = command.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
    	
		return this;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append("[CashWithdrawalVO.toString] : ");
		toString.append("productId : "+productId);
		toString.append(", deviceTypeId : "+deviceTypeId);
		toString.append(", agentMobileNo : "+agentMobileNo);
		toString.append(", customerMobileNo : "+customerMobileNo);
		toString.append(", encryptionType : "+encryptionType);
		toString.append(", CNIC : "+CNIC);
		toString.append(", agentPIN : "+agentPIN);
		toString.append(", commissionAmount : "+commissionAmount);
		toString.append(", processingAmount : "+processingAmount);
		toString.append(", totalAmount : "+totalAmount);
		
		return toString.toString();
	}
	
	@Override
	public void validateVO(ProductVO productVO)
			throws FrameworkCheckedException {
	}

	public String getCustomerMobileNo() {
		return customerMobileNo;
	}

	public void setCustomerMobileNo(String customerMobileNo) {
		this.customerMobileNo = customerMobileNo;
	}

	public String getAgentMobileNo() {
		return agentMobileNo;
	}

	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}

	public Double getWithdrawalAmount() {
		return withdrawalAmount;
	}

	public void setWithdrawalAmount(Double withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getEncryptionType() {
		return encryptionType;
	}

	public void setEncryptionType(String encryptionType) {
		this.encryptionType = encryptionType;
	}

	public String getCNIC() {
		return CNIC;
	}

	public void setCNIC(String cNIC) {
		CNIC = cNIC;
	}

	public String getAgentPIN() {
		return agentPIN;
	}

	public void setAgentPIN(String agentPIN) {
		this.agentPIN = agentPIN;
	}

	public String getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(String commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public String getProcessingAmount() {
		return processingAmount;
	}

	public void setProcessingAmount(String processingAmount) {
		this.processingAmount = processingAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getCustomerBalance() {
		return customerBalance;
	}

	public void setCustomerBalance(Double customerBalance) {
		this.customerBalance = customerBalance;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public String getMfsId() {
		return mfsId;
	}

	public void setMfsId(String mfsId) {
		this.mfsId = mfsId;
	}

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public void setRetailerId(Long retailerId) {
		this.retailerId = retailerId;
	}

	public Long getRetailerId() {
		return retailerId;
	}

	public Double getAgentBalance() {
		return agentBalance;
	}

	public void setAgentBalance(Double agentBalance) {
		this.agentBalance = agentBalance;
	}

}
