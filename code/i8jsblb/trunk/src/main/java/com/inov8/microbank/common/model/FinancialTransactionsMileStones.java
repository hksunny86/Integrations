package com.inov8.microbank.common.model;

import java.util.HashMap;

public class FinancialTransactionsMileStones
{
	private boolean isCustomerBankAccountDebitted = false;
	private HashMap isCommissionsSettled = new HashMap();
	private boolean isBillPaid = false;
	private boolean productDispensed = false;
	
	
	public boolean isBillPaid()
	{
		return isBillPaid;
	}
	public void setBillPaid(boolean isBillPaid)
	{
		this.isBillPaid = isBillPaid;
	}
	public HashMap getIsCommissionsSettled()
	{
		return isCommissionsSettled;
	}
	public void setIsCommissionsSettled(HashMap isCommissionSettled)
	{
		this.isCommissionsSettled = isCommissionSettled;
	}
	public boolean isCustomerBankAccountDebitted()
	{
		return isCustomerBankAccountDebitted;
	}
	public void setCustomerBankAccountDebitted(boolean isCustomerBankAccountDebitted)
	{
		this.isCustomerBankAccountDebitted = isCustomerBankAccountDebitted;
	} 
	public void addToIsCommissionSettled(Object key, Object value)
	{
		this.isCommissionsSettled.put(key,value);
	}
	
	public void removeFromIsCommissionSettled(Object object)
	{
		this.isCommissionsSettled.remove(object);
	}
	public Object getIsCommissionSettled(Object key)
	{
		return this.isCommissionsSettled.get(key);
	}
	public boolean isProductDispensed()
	{
		return productDispensed;
	}
	public void setProductDispensed(boolean productDispensed)
	{
		this.productDispensed = productDispensed;
	}

}
