package com.inov8.ola.integration.vo;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.io.Serializable;
import java.util.Date;


public class LedgerModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9042669411336390708L;
	private Double amount ;
	private Long customerId;
	private Long transactionTypeId;
	private Long reasonId;
	private Date transactionDateTime ;
	private Double customerBalanceAfterTx;
	private Double customerBalanceBeforeTx;
	private String microbankTransactionCode;
	private Double debitAmount;
	private Double creditAmount;
	private String summary;
	
	public Double getAmount()
	{
		return amount;
	}
	
	public void setAmount(Double amount)
	{
		this.amount = amount;
	}
	
	public Double getCustomerBalanceAfterTx()
	{
		return customerBalanceAfterTx;
	}
	
	public void setCustomerBalanceAfterTx(Double customerBalanceAfterTx)
	{
		this.customerBalanceAfterTx = customerBalanceAfterTx;
	}
	
	public Double getCustomerBalanceBeforeTx()
	{
		return customerBalanceBeforeTx;
	}
	
	public void setCustomerBalanceBeforeTx(Double customerBalanceBeforeTx)
	{
		this.customerBalanceBeforeTx = customerBalanceBeforeTx;
	}
	
	public Long getCustomerId()
	{
		return customerId;
	}
	
	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public Long getTransactionTypeId()
	{
		return transactionTypeId;
	}

	public void setTransactionTypeId(Long transactionTypeId)
	{
		this.transactionTypeId = transactionTypeId;
	}

	public Long getReasonId()
	{
		return reasonId;
	}

	public void setReasonId(Long reasonId)
	{
		this.reasonId = reasonId;
	}

	public String getMicrobankTransactionCode()
	{
		return microbankTransactionCode;
	}
	
	public void setMicrobankTransactionCode(String microbankTransactionCode)
	{
		this.microbankTransactionCode = microbankTransactionCode;
	}
	
	public Date getTransactionDateTime()
	{
		return transactionDateTime;
	}
	
	public void setTransactionDateTime(Date transactionDateTime)
	{
		this.transactionDateTime = transactionDateTime;
	}

	public Double getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}

	public Double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
