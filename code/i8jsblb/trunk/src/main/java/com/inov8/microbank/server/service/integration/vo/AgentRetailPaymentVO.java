package com.inov8.microbank.server.service.integration.vo;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;


public class AgentRetailPaymentVO implements ProductVO
{	

    protected String customerMobileNo;
	protected Double transactionAmount;
    protected String customerName;

    protected Long customerAppUserId;
    protected Long customerId;
    protected String customerMfsId;
    protected Double balance;
    protected Date transactionDateTime;
    
	public String responseXML()
	{
		StringBuilder responseXML = new StringBuilder();
		  		  
		  responseXML		  
			
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_CUSTOMER_MOBILE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.customerMobileNo)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)
			
			
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_CUSTOMER_NAME)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.customerName)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE);
		  
		  return responseXML.toString();
	}


	public String getResponseCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper)
	{
		if( baseWrapper.getObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE ).toString().trim();
		      ((AgentRetailPaymentVO)productVO).setCustomerMobileNo(mobileNo );
		}
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null )
		{
		    try
			{
				((AgentRetailPaymentVO)productVO).setTransactionAmount(Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		
		return (ProductVO)productVO;
	}

	public void setResponseCode(String responseCode)
	{
		// TODO Auto-generated method stub
		
	}

	public void validateVO(ProductVO productVO) throws FrameworkCheckedException
	{
		if( ((AgentRetailPaymentVO)productVO).getCustomerMobileNo() == null ){
			throw new FrameworkCheckedException("Mobile number is not provided.");
		}
		
		if( ((AgentRetailPaymentVO)productVO).getTransactionAmount() == null || ((AgentRetailPaymentVO)productVO).getTransactionAmount() == 0 )
			throw new FrameworkCheckedException("Amount is not provided.");
		
	}
	
    public String getCustomerMobileNo() {
		return customerMobileNo;
	}


	public void setCustomerMobileNo(String customerMobileNo) {
		this.customerMobileNo = customerMobileNo;
	}


	public Double getTransactionAmount() {
		return transactionAmount;
	}


	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public Long getCustomerAppUserId() {
		return customerAppUserId;
	}


	public void setCustomerAppUserId(Long customerAppUserId) {
		this.customerAppUserId = customerAppUserId;
	}


	public Long getCustomerId() {
		return customerId;
	}


	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}


	public String getCustomerMfsId() {
		return customerMfsId;
	}


	public void setCustomerMfsId(String customerMfsId) {
		this.customerMfsId = customerMfsId;
	}


	public Double getBalance() {
		return balance;
	}


	public void setBalance(Double balance) {
		this.balance = balance;
	}


	public Date getTransactionDateTime() {
		return transactionDateTime;
	}


	public void setTransactionDateTime(Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}



}
