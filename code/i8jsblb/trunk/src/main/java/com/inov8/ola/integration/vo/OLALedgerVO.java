package com.inov8.ola.integration.vo;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class OLALedgerVO implements Serializable
{

	private Date fromDate;
	private Date toDate;
	private String NIC;
	List<LedgerModel> ledger;
	
	
	public Date getFromDate()
	{
		return fromDate;
	}
	
	public void setFromDate(Date fromDate)
	{
		this.fromDate = fromDate;
	}
	
	public List<LedgerModel> getLedger()
	{
		return ledger;
	}
	
	public void setLedger(List<LedgerModel> ledger)
	{
		this.ledger = ledger;
	}
	
	public String getNIC()
	{
		return NIC;
	}
	
	public void setNIC(String nic)
	{
		NIC = nic;
	}
	
	public Date getToDate()
	{
		return toDate;
	}
	
	public void setToDate(Date toDate)
	{
		this.toDate = toDate;
	}

}
