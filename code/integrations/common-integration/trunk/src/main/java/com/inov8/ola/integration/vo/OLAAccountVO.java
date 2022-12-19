package com.inov8.ola.integration.vo;

/**
 * Project Name: 			Commons-Integration	
 * @author 					Jawwad Farooq
 * Creation Date: 			November 2008  			
 * Description:				
 */

import java.util.*;
import java.io.Serializable;



public class OLAAccountVO implements Serializable 
{
  
   private Long accountId;
   private Long accountNumber;
   private Double balance;
   private Date createdOn;
   private Date updatedOn;
   private Long statusId;

	public Long getAccountId()
	{
		return accountId;
	}

	public void setAccountId(Long accountId)
	{
		this.accountId = accountId;
	}

	public Long getAccountNumber()
	{
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	public Double getBalance()
	{
		return balance;
	}

	public void setBalance(Double balance)
	{
		this.balance = balance;
	}

	public Date getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	public Long getStatusId()
	{
		return statusId;
	}

	public void setStatusId(Long statusId)
	{
		this.statusId = statusId;
	}

	public Date getUpdatedOn()
	{
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn)
	{
		this.updatedOn = updatedOn;
	}

   
   
}
