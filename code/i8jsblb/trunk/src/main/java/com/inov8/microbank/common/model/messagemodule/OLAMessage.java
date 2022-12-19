package com.inov8.microbank.common.model.messagemodule;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			Microbank
 * Creation Date: 			November 2008  			
 * Description:				
 */

import java.io.Serializable;
import java.util.Date;

import com.inov8.ola.integration.vo.OLAVO;



public class OLAMessage implements Serializable
{
	private String serviceURL ;
	private OLAVO olaVO ;
	private Long actionLogId ;
	private Long transactionCodeId;
	private Long allpayCommTransId;
	private Boolean nationalDistributor;
	private Boolean distributor;
	private Boolean retailer;
	private Boolean commissionSettled = false;
	private Date transactionDateTime;
	private Long customerAccountTypeId;
	
	
	
	
	public OLAMessage()
	{
		this.retailer = false;
		this.distributor = false;
		this.nationalDistributor = false;
	}

	public String getServiceURL()
	{
		return serviceURL;
	}
	
	public void setServiceURL(String serviceURL)
	{
		this.serviceURL = serviceURL;
	}
	
	public OLAVO getOlaVO()
	{
		return olaVO;
	}

	public void setOlaVO(OLAVO olaVO)
	{
		this.olaVO = olaVO;
	}

	
	public Long getActionLogId()
	{
		return actionLogId;
	}

	
	public void setActionLogId(Long actionLogId)
	{
		this.actionLogId = actionLogId;
	}

	
	public Long getTransactionCodeId()
	{
		return transactionCodeId;
	}

	
	public void setTransactionCodeId(Long transactionCodeId)
	{
		this.transactionCodeId = transactionCodeId;
	}

	public Long getAllpayCommTransId()
	{
		return allpayCommTransId;
	}

	public void setAllpayCommTransId(Long allpayCommTransId)
	{
		this.allpayCommTransId = allpayCommTransId;
	}

	public Boolean isNationalDistributor()
	{
		return nationalDistributor;
	}

	public void setNationalDistributor(Boolean nationalDistributor)
	{
		this.nationalDistributor = nationalDistributor;
	}

	public Boolean isDistributor()
	{
		return distributor;
	}

	public void setDistributor(Boolean distributor)
	{
		this.distributor = distributor;
	}

	public Boolean isRetailer()
	{
		return retailer;
	}

	public void setRetailer(Boolean retailer)
	{
		this.retailer = retailer;
	}

	public Boolean getCommissionSettled()
	{
		return commissionSettled;
	}

	public void setCommissionSettled(Boolean commissionSettled)
	{
		this.commissionSettled = commissionSettled;
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
}
