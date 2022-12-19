package com.inov8.microbank.server.service.integration.vo;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.StringUtil;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class LescoBillSaleStubVO
    implements BillPaymentVO
{



String consumerNo, billingMonth, name, address ;
  Date dueDate ;
  Double billAmount, billAmountAfterDate,commissionAmount,totalAmount,txAmount ;
  String responseCode ;
  private String pin;
  
  public Double getCurrentBillAmount()
	{
		return this.billAmount;
	}
  
  public String responseXML()
  {
	  return "" ;	  
  }

  public void setResponseCode(String responseCode)
  {
    this.responseCode = responseCode;
  }

  public void setPin(String pin)
  {
    this.pin = pin;
  }

  public String getResponseCode()
  {
    return responseCode;
  }

  public String getPin()
  {
    return pin;
  }


  public void setAddress(String address)
  {
    this.address = address;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setDueDate(Date dueDate)
  {
    this.dueDate = dueDate;
  }

  public void setConsumerNo(String consumerNo)
  {
    this.consumerNo = consumerNo;
  }

  public void setBillingMonth(String billingMonth)
  {
    this.billingMonth = billingMonth;
  }

  public void setBillAmount(Double billAmount)
  {
    this.billAmount = billAmount;
  }

  public void setBillAmountAfterDate(Double billAmountAfterDate)
  {
    this.billAmountAfterDate = billAmountAfterDate;
  }

  public String getAddress()
  {
    return address;
  }

  public String getConsumerNo()
  {
    return consumerNo;
  }

  public Date getDueDate()
  {
    return dueDate;
  }

  public String getName()
  {
    return name;
  }

  public String getBillingMonth()
  {
    return billingMonth;
  }

  public Double getBillAmountAfterDate()
  {
    return billAmountAfterDate;
  }

  public Double getBillAmount()
  {
    return billAmount;
  }

public Double getCommissionAmount()
{
	return commissionAmount;
}

public void setCommissionAmount(Double commissionAmount)
{
	this.commissionAmount = commissionAmount;
}

public Double getTotalAmount()
{
	return totalAmount;
}

public void setTotalAmount(Double totalAmount)
{
	this.totalAmount = totalAmount;
}

public Double getTxAmount()
{
	return txAmount;
}

      public void setTxAmount(Double txAmount)
      {
        this.txAmount = txAmount;
      }

      public ProductVO populateVO( ProductVO billPaymentVO, BaseWrapper baseWrapper )
      {

    	  LescoBillSaleStubVO lescoBillSaleStubVO = ((LescoBillSaleStubVO)billPaymentVO);
    	  
        if( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ) != null ) {
        	lescoBillSaleStubVO.setConsumerNo( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ).toString().trim());
        }
    	
        if(StringUtil.isNullOrEmpty(lescoBillSaleStubVO.getConsumerNo())) {
        
        	lescoBillSaleStubVO.setConsumerNo( baseWrapper.getObject( CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER ).toString().trim());
    	}
        
        return (ProductVO)lescoBillSaleStubVO;
      }

      public void validateVO( ProductVO billPaymentVO )throws FrameworkCheckedException
      {

        if( ((LescoBillSaleStubVO)billPaymentVO).getConsumerNo() != null )
        {
          if( "".equals(((LescoBillSaleStubVO)billPaymentVO).getConsumerNo()))
            throw new FrameworkCheckedException("Consumer Reference number is not provided.");
        }
        else
          throw new FrameworkCheckedException("Consumer Reference number is not provided.");
      }


}
