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
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;


public class BBToCoreVO implements BillPaymentVO
{	

    protected String mobileNo;

    protected Double amount;
    protected String accountNumber;
    protected String customerName;
    protected String consumerNo;
    protected String receiptNo;
    protected Long appUserId;
    protected Long customerId;
    protected String mfsId;
    protected Double balance;
    protected Double senderBalance;
    private String customerMobileNo;
    private String accountTitle;
    
    
    
    public Double getBalance()
	{
		return balance;
	}

	public void setBalance(Double balance)
	{
		this.balance = balance;
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

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
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
			.append(CommandFieldConstants.KEY_CORE_ACC_NO)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.accountNumber)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)
			
			/*.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_RECEPIENT_NAME)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.customerName)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)*/
			
			
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
		if( baseWrapper.getObject( CommandFieldConstants.KEY_AGENT_MOBILE ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_AGENT_MOBILE ).toString().trim();
		      ((BBToCoreVO)productVO).setMsisdn( mobileNo );
		}

		if( baseWrapper.getObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE ).toString().trim();
		      ((BBToCoreVO)productVO).setCustomerMobileNo( mobileNo );
		      ((BBToCoreVO)productVO).setConsumerNo(mobileNo);
		
		}else if( baseWrapper.getObject( CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE ) != null ){
			
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE ).toString().trim();
		      ((BBToCoreVO)productVO).setCustomerMobileNo( mobileNo );
		      ((BBToCoreVO)productVO).setConsumerNo(mobileNo);
		}

		if( baseWrapper.getObject( CommandFieldConstants.KEY_CORE_ACC_NO ) != null )
		{
			String accountNo = baseWrapper.getObject( CommandFieldConstants.KEY_CORE_ACC_NO ).toString().trim();
		      ((BBToCoreVO)productVO).setAccountNumber(accountNo);
		}
		
		
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) != null )
		{
		    try
			{
				((BBToCoreVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ).toString().trim()) );
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
					((BBToCoreVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
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

//		if( ((BBToCoreVO)productVO).getMsisdn() == null )
//			throw new FrameworkCheckedException("Mobile number is not provided.");
//		if( ((BBToCoreVO)productVO).getMsisdn().length() != 11 )
//			throw new FrameworkCheckedException("Mobile number is invalid.");
		
		ValidationErrors validationErrors = new ValidationErrors(); 
//		validationErrors = ValidatorWrapper.doNumeric(((BBToCoreVO)productVO).getMsisdn(), validationErrors, "Mobile number");
		
//		if( validationErrors.hasValidationErrors() )
//		{
//			throw new FrameworkCheckedException("Mobile number is invalid.");
//		}
		
		if( ((BBToCoreVO)productVO).getAmount() == null || ((BBToCoreVO)productVO).getAmount() == 0 )
			throw new FrameworkCheckedException("Bill amount is not provided.");
		
//		Product Limits should be checked instead	
//		if( ((BBToCoreVO)productVO).getAmount() < 10 || ((BBToCoreVO)productVO).getAmount() > 999999 )
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

	public Double getSenderBalance() {
		return senderBalance;
	}

	public void setSenderBalance(Double senderBalance) {
		this.senderBalance = senderBalance;
	}

	public String getCustomerMobileNo() {
		return customerMobileNo;
	}

	public void setCustomerMobileNo(String customerMobileNo) {
		this.customerMobileNo = customerMobileNo;
	}

	public String getAccountTitle() {
		return accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}
	

}
