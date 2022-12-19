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



public class CreditCardPaymentVO implements BillPaymentVO
{	    
    protected String NIC;
    protected String cardNumber;    
    protected String accountNumber;
    protected String accountType;
    protected String accountCurrency;
    protected Double transactionAmount;
    protected String transactionCurrency;
    protected String MPin;
    protected String responseCode;
    protected String transactionCode;
    private String mfsId;
    
	protected Double dueAmount;
	protected Double minimumDueAmount;
	protected Double outstandingDueAmount;
	protected Date dueDate;
	
    
    
	
	public String getMfsId()
	{
		return mfsId;
	}

	
	public void setMfsId(String mfsId)
	{
		this.mfsId = mfsId;
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
			.append(CommandFieldConstants.KEY_CREDIT_CARD_NO)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.cardNumber)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE);
		  
		  return responseXML.toString();
	}

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper)
	{
		if( baseWrapper.getObject( CommandFieldConstants.KEY_CREDIT_CARD_NO ) != null )
		{
			String cardNo = baseWrapper.getObject( CommandFieldConstants.KEY_CREDIT_CARD_NO ).toString().trim();
		      ((CreditCardPaymentVO)productVO).setCardNumber( cardNo );
		      ((CreditCardPaymentVO)productVO).setConsumerNo(cardNo);
		}
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null ){
		      ((CreditCardPaymentVO)productVO).setTransactionAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
		}
		
		return (ProductVO)productVO;
	}

	public void setResponseCode(String responseCode)
	{
		this.responseCode = responseCode;		
	}

	public void validateVO(ProductVO productVO) throws FrameworkCheckedException
	{
		if( ((CreditCardPaymentVO)productVO).getCardNumber() == null )
			throw new FrameworkCheckedException("Credit Card number is not provided.");
//		if( ((CreditCardPaymentVO)productVO).getTransactionAmount() == null || ((CreditCardPaymentVO)productVO).getTransactionAmount() == 0 )
//			throw new FrameworkCheckedException("Transaction amount is not provided.");		
	}

	public Double getBillAmount()
	{
		return this.transactionAmount;
	}

	public String getConsumerNo()
	{		
		return this.cardNumber ;
	}

	public Double getCurrentBillAmount()
	{
		return this.transactionAmount;
	}

	public void setBillAmount(Double billAmount)
	{
		this.transactionAmount = billAmount ;		
	}

	public void setConsumerNo(String consumerNo)
	{
				
	}

	public String getResponseCode()
	{		
		return this.responseCode;
	}

	
	public String getAccountCurrency()
	{
		return accountCurrency;
	}

	
	public void setAccountCurrency(String accountCurrency)
	{
		this.accountCurrency = accountCurrency;
	}

	
	public String getAccountNumber()
	{
		return accountNumber;
	}

	
	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	
	public String getAccountType()
	{
		return accountType;
	}

	
	public void setAccountType(String accountType)
	{
		this.accountType = accountType;
	}

	
	public String getCardNumber()
	{
		return cardNumber;
	}

	
	public void setCardNumber(String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	
	public String getMPin()
	{
		return MPin;
	}

	
	public void setMPin(String pin)
	{
		MPin = pin;
	}

	
	public String getNIC()
	{
		return NIC;
	}

	
	public void setNIC(String nic)
	{
		NIC = nic;
	}

	
	public Double getTransactionAmount()
	{
		return transactionAmount;
	}

	
	public void setTransactionAmount(Double transactionAmount)
	{
		this.transactionAmount = transactionAmount;
	}

	
	public String getTransactionCurrency()
	{
		return transactionCurrency;
	}

	
	public void setTransactionCurrency(String transactionCurrency)
	{
		this.transactionCurrency = transactionCurrency;
	}

	
	public String getTransactionCode()
	{
		return transactionCode;
	}

	
	public void setTransactionCode(String transactionCode)
	{
		this.transactionCode = transactionCode;
	}


	public Double getDueAmount() {
		return dueAmount;
	}


	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}


	public Double getMinimumDueAmount() {
		return minimumDueAmount;
	}


	public void setMinimumDueAmount(Double minimumDueAmount) {
		this.minimumDueAmount = minimumDueAmount;
	}


	public Double getOutstandingDueAmount() {
		return outstandingDueAmount;
	}


	public void setOutstandingDueAmount(Double outstandingDueAmount) {
		this.outstandingDueAmount = outstandingDueAmount;
	}


	public Date getDueDate() {
		return dueDate;
	}


	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	


	


	

	

}
