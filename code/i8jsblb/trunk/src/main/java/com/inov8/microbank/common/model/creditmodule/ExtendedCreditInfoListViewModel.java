package com.inov8.microbank.common.model.creditmodule;
/**
 * 
 * @author Maqsood Shahzad
 *
 */

public class ExtendedCreditInfoListViewModel extends CreditInfoListViewModel
{

	private Long distributorId;
	private Long retailerId;
	
	@javax.persistence.Transient
	public Long getDistributorId()
	{
		return distributorId;
	}
	public void setDistributorId(Long distributorId)
	{
		this.distributorId = distributorId;
	}
	@javax.persistence.Transient
	public Long getRetailerId()
	{
		return retailerId;
	}
	public void setRetailerId(Long retailerId)
	{
		this.retailerId = retailerId;
	}
}
