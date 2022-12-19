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


public class P2PVO implements BillPaymentVO
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
			.append(CommandFieldConstants.KEY_RECEPIENT_NAME)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.customerName)		
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
		      ((P2PVO)productVO).setMsisdn( mobileNo );
		      ((P2PVO)productVO).setConsumerNo(mobileNo);
		}
		else if( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ).toString().trim();
		      ((P2PVO)productVO).setMsisdn( mobileNo );
		      ((P2PVO)productVO).setConsumerNo(mobileNo);
		}
		
		
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) != null )
		{
		    try
			{
				((P2PVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ).toString().trim()) );
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
					((P2PVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
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
		if( ((P2PVO)productVO).getMsisdn() == null )
			throw new FrameworkCheckedException("Mobile number is not provided.");
		if( ((P2PVO)productVO).getMsisdn().length() != 11 )
			throw new FrameworkCheckedException("Mobile number is invalid.");
		
		ValidationErrors validationErrors = new ValidationErrors(); 
		validationErrors = ValidatorWrapper.doNumeric(((P2PVO)productVO).getMsisdn(), validationErrors, "Mobile number");
		
		if( validationErrors.hasValidationErrors() )
		{
			throw new FrameworkCheckedException("Mobile number is invalid.");
		}
		
		if( ((P2PVO)productVO).getAmount() == null || ((P2PVO)productVO).getAmount() == 0 )
			throw new FrameworkCheckedException("Bill amount is not provided.");
		
//		Product Limits should be checked instead	
//		if( ((P2PVO)productVO).getAmount() < 10 || ((P2PVO)productVO).getAmount() > 999999 )
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
	

}
