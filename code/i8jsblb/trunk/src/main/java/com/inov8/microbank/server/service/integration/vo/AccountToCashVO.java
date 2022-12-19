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
import com.inov8.microbank.common.util.Formatter;

/**
 * 
 * @author Jawwad Farooq
 *
 */

public class AccountToCashVO implements ProductVO, Serializable
{
	
  
  private String consumerNo;
  private String phoneNo;
  private Double paidAmount;
  private Double billAmount;
  private String paidDate;
  private String mfsId;
  private String transactionCode;
  private String recepientName;
  private Long customerId;
  private Long appUserId;
  private Double txAmount;
  private String recipientWalkinMobile;
  private String recipientWalkinCNIC;
  private Double balance;
  
  private static final long serialVersionUID = 6219262453169387336L;
  
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
		.append(CommandFieldConstants.KEY_CONS_REF_NO)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.consumerNo)		
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
		.append(CommandFieldConstants.KEY_CUST_CODE)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.consumerNo)		
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
		.append(CommandFieldConstants.KEY_BILL_AMOUNT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append( this.getCurrentBillAmount() )		
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
		.append( this.getRecepientName() )		
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
		.append(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(Formatter.formatNumbers(this.getCurrentBillAmount()))		
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
		.append(CommandFieldConstants.KEY_ACTUAL_BILL_AMT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.billAmount)		
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
		.append(CommandFieldConstants.KEY_FORMATED_ACTUAL_BILL_AMT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(Formatter.formatNumbers(this.billAmount))		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE);
		
	  
	  
	  return responseXML.toString();
  }
  
  public String getResponseCode()
  {	
	return null;
  }

  public ProductVO populateVO( ProductVO acctToCashVO, BaseWrapper baseWrapper )
  {
    if( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ) != null ){
    	((AccountToCashVO)acctToCashVO).setConsumerNo( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ).toString().trim() );
    }
    
    if( baseWrapper.getObject( CommandFieldConstants.KEY_R_W_CNIC ) != null ){
    	((AccountToCashVO)acctToCashVO).setRecipientWalkinCNIC( baseWrapper.getObject( CommandFieldConstants.KEY_R_W_CNIC ).toString().trim() );
    }

    if( baseWrapper.getObject( CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN ) != null ){
    	((AccountToCashVO)acctToCashVO).setRecipientWalkinMobile( baseWrapper.getObject( CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN ).toString().trim() );
    }

    if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null )
	{
	    try
		{
			((AccountToCashVO)acctToCashVO).setTxAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
			
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
	}
    
    if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) != null )
	{
	    try
		{
			((AccountToCashVO)acctToCashVO).setBillAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ).toString().trim()) );
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
				((AccountToCashVO)acctToCashVO).setBillAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
	}
    /*if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) == null )
	{	
		if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null )
		{
			try
			{
				((AccountToCashVO)acctToCashVO).setBillAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
	}*/
    return (ProductVO)acctToCashVO;
  }

  public void setResponseCode(String responseCode)
  {
	// TODO Auto-generated method stub	
  }

  public void validateVO(ProductVO productVO) throws FrameworkCheckedException
  {
//	  if( ((AccountToCashVO)productVO).getConsumerNo() == null || ((AccountToCashVO)productVO).getConsumerNo().equals("") )
//			throw new FrameworkCheckedException("Mobile number is not provided.");	
  }

  public AccountToCashVO()
  {
  }

  public void setBillAmount(Double billAmount)
  {
    this.billAmount = billAmount;
  }

 

  public void setPhoneNo(String phoneNo)
  {
    this.phoneNo = phoneNo;
  }

  public void setPaidDate(String paidDate)
  {
    this.paidDate = paidDate;
  }

 

 
 

  public void setConsumerNo(String consumerNo)
  {
    this.consumerNo = consumerNo;
  }

 

  public void setPaidAmount(Double paidAmount)
  {
    this.paidAmount = paidAmount;
  }

  

  

  public Double getBillAmount()
  {
    return (this.billAmount == null ? 0 : billAmount ) ;
  }

 

  public String getConsumerNo()
  {
    return consumerNo;
  }

  

  
  public String getPhoneNo()
  {
    return phoneNo;
  }

  public String getPaidDate()
  {
    return paidDate;
  }

  
  public Double getPaidAmount()
  {
    return paidAmount;
  }



public String getMfsId()
{
	return mfsId;
}


public void setMfsId(String mfsId)
{
	this.mfsId = mfsId;
}


public String getTransactionCode()
{
	return transactionCode;
}


public void setTransactionCode(String transactionCode)
{
	this.transactionCode = transactionCode;
}

public Double getCurrentBillAmount()
{
	// TODO Auto-generated method stub
	return billAmount;
}

public String getRecepientName()
{
	return recepientName;
}

public void setRecepientName(String recepientName)
{
	this.recepientName = recepientName;
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

public Double getTxAmount() {
	return txAmount;
}

public void setTxAmount(Double txAmount) {
	this.txAmount = txAmount;
}

public String getRecipientWalkinMobile() {
	return recipientWalkinMobile;
}

public void setRecipientWalkinMobile(String recipientWalkinMobile) {
	this.recipientWalkinMobile = recipientWalkinMobile;
}

public String getRecipientWalkinCNIC() {
	return recipientWalkinCNIC;
}

public void setRecipientWalkinCNIC(String recipientWalkinCNIC) {
	this.recipientWalkinCNIC = recipientWalkinCNIC;
}

public Double getBalance() {
	return balance;
}

public void setBalance(Double balance) {
	this.balance = balance;
}

}





