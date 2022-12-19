package com.inov8.microbank.server.service.integration.vo;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;

public class SkmtVO implements ProductVO, BillPaymentVO
{
	
	protected Double amount;
	private Double billAmount;
	

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(Double amount)
	{
		this.amount = amount;
	}

	public String getResponseCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper)
	{
		// TODO Auto-generated method stub
		if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) != null )
		{
		    try
			{
				((SkmtVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ).toString().trim()) );
				((SkmtVO)productVO).setBillAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ).toString().trim()) );
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
					((SkmtVO)productVO).setAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
					((SkmtVO)productVO).setBillAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
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
		// TODO Auto-generated method stub

	}

	public Double getBillAmount()
	{
		// TODO Auto-generated method stub
		return (this.billAmount == null ? 0 : billAmount ) ;
	}

	public String getConsumerNo()
	{
		// TODO Auto-generated method stub
		return "";
	}

	public Double getCurrentBillAmount()
	{
		// TODO Auto-generated method stub
		return this.getBillAmount();
	}

	public String responseXML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setBillAmount(Double billAmount)
	{
		// TODO Auto-generated method stub
		this.billAmount = billAmount;
		
	}

	public void setConsumerNo(String consumerNo)
	{
		// TODO Auto-generated method stub
		
	}

}
