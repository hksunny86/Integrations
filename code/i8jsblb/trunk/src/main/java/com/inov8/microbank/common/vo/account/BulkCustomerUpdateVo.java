package com.inov8.microbank.common.vo.account;

public class BulkCustomerUpdateVo
{
	private Long customerAccountTypeId;
	
	private Long segmentId;

	private String cnic;

	public BulkCustomerUpdateVo()
	{

	}

	public Long getCustomerAccountTypeId()
	{
		return customerAccountTypeId;
	}
	
	public void setCustomerAccountTypeId(Long customerAccountTypeId)
	{
		this.customerAccountTypeId = customerAccountTypeId;
	}
	
	public Long getSegmentId()
	{
		return segmentId;
	}
	
	public void setSegmentId(Long segmentId)
	{
		this.segmentId = segmentId;
	}

	public String getCnic()
	{
		return cnic;
	}

	public void setCnic(String cnic)
	{
		this.cnic = cnic;
	}

}
