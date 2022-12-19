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
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;


public class RetailPaymentVO implements BillPaymentVO
{	

    protected String mobileNo;

    protected Double amount;
    protected String accountNumber;
    protected String customerName;
    protected String consumerNo;
    protected String receiptNo;
    protected Long appUserId;
    protected Long retailerId;
    protected String mfsId;
    protected String retailerName;
    protected Double balance;
    protected Date transactionDateTime;
    protected Long customerAccountTypeId;
    protected String receiverMobile;
    protected Double recipientAccountBalance;
    
    public Double getBalance()
	{
		return balance;
	}

	public void setBalance(Double balance)
	{
		this.balance = balance;
	}

	public Long getRetailerId()
	{
		return retailerId;
	}

	public void setRetailerId(Long retailerId)
	{
		this.retailerId = retailerId;
	}

	public String getRetailerName()
	{
		return retailerName;
	}

	public void setRetailerName(String retailerName)
	{
		this.retailerName = retailerName;
	}

	public String getMfsId()
	{
		return mfsId;
	}

	public void setMfsId(String mfsId)
	{
		this.mfsId = mfsId;
	}

	public Long getAppUserId()
	{
		return appUserId;
	}

	public void setAppUserId(Long appUserId)
	{
		this.appUserId = appUserId;
	}

	public String getMobileNo()
	{
		return mobileNo;
	}

	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	public String getReceiptNo()
	{
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo)
	{
		this.receiptNo = receiptNo;
	}

	public Double getBillAmount()
	{		
		return amount;
	}

	public String getConsumerNo()
	{		
		return consumerNo;
	}

	public Double getCurrentBillAmount()
	{	
		return amount;
	}

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
			.append(CommandFieldConstants.KEY_MOB_NO)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.mobileNo)		
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
			.append(CommandFieldConstants.KEY_RP_NAME)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.customerName)		
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
			.append("RETAILER")
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.retailerName)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)
			
			
				;
		  
		  return responseXML.toString();
	}

	public void setBillAmount(Double billAmount)
	{
		this.amount = billAmount;
		
	}

	public void setConsumerNo(String consumerNo)
	{
		this.consumerNo = consumerNo; 
		
	}

	public String getResponseCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper)
	{
		if( baseWrapper.getObject( CommandFieldConstants.KEY_MOB_NO ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_MOB_NO ).toString().trim();
		      ((RetailPaymentVO)productVO).setMsisdn( mobileNo );
		      ((RetailPaymentVO)productVO).setConsumerNo(mobileNo);
		}
		else if( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ).toString().trim();
		      ((RetailPaymentVO)productVO).setMsisdn( mobileNo );
		      ((RetailPaymentVO)productVO).setConsumerNo(mobileNo);
		}
		
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_REC_MSISDN ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_REC_MSISDN ).toString().trim();
		      ((RetailPaymentVO)productVO).setReceiverMobile( mobileNo );
		}
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) != null )
		{
		    try
			{
				((RetailPaymentVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ).toString().trim()) );
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) == null )
		{	
			if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null )
			{
				try
				{
					((RetailPaymentVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
				}
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
		if( ((RetailPaymentVO)productVO).getMsisdn() == null )
			throw new FrameworkCheckedException("Mobile number is not provided.");
		if( ((RetailPaymentVO)productVO).getMsisdn().length() != 11 )
			throw new FrameworkCheckedException("Mobile number is invalid.");
		
		ValidationErrors validationErrors = new ValidationErrors(); 
		validationErrors = ValidatorWrapper.doNumeric(((RetailPaymentVO)productVO).getMsisdn(), validationErrors, "Mobile number");
		
		if( validationErrors.hasValidationErrors() )
		{
			throw new FrameworkCheckedException("Mobile number is invalid.");
		}
		
		if( ((RetailPaymentVO)productVO).getAmount() == null || ((RetailPaymentVO)productVO).getAmount() == 0 )
			throw new FrameworkCheckedException("Bill amount is not provided.");
		
//		Product Limits should be checked instead	
//		if( ((RetailPaymentVO)productVO).getAmount() < 10 || ((RetailPaymentVO)productVO).getAmount() > 999999 )
//			throw new FrameworkCheckedException("Bill amount should be between 10 and 999999.");
	}
	

	public String getAccountNumber()
	{
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}
	
	
	
	public Double getAmount()
	{
		return amount;
	}
	
	public void setAmount(Double amount)
	{
		this.amount = amount;
	}
	
	public String getCustomerName()
	{
		return customerName;
	}
	
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}
	
	
	
	public String getMsisdn()
	{
		return mobileNo;
	}
	
	public void setMsisdn(String msisdn)
	{
		this.mobileNo = msisdn;
	}

	public Date getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}

	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public Double getRecipientAccountBalance() {
		return recipientAccountBalance;
	}

	public void setRecipientAccountBalance(Double recipientAccountBalance) {
		this.recipientAccountBalance = recipientAccountBalance;
	}
	

}
