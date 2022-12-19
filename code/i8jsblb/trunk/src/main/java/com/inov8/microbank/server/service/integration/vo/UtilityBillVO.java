package com.inov8.microbank.server.service.integration.vo;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.io.Serializable;
import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;

/**
 * 
 * @author Jawwad Farooq
 *
 */

public class UtilityBillVO implements BillPaymentVO, Serializable
{
	
  private String companyCode;
  private String companyName;
  private String customerName;
  private String customerAddress;
  private String billingMonth;
  private String consumerNo;
  private String phoneNo;
  private Double paidAmount;
  private Double billAmount=0D;
  private Double lateBillAmount=0D;
  private Date dueDate;
  private String paidDate;
  private boolean billPaid;
  private String mfsId;
  private String transactionCode;
  
  private static final long serialVersionUID = 6219262453169387336L;
  
  public String responseXML()
  {
	  StringBuilder responseXML = new StringBuilder();
	  /*
	  
	  responseXML
	  
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_CONS_REF_NO)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.consumerNo)		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_CUST_CODE)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.consumerNo)		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)
		
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_BILL_DATE_OVERDUE)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append( this.isBillOverDue())		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)	
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_BILL_AMOUNT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append( this.getCurrentBillAmount() )		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)	
	  
	  		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(Formatter.formatNumbers(this.getCurrentBillAmount()))		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)	
		
			
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_LATE_BILL_AMT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.lateBillAmount)		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)	
		
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_ACTUAL_BILL_AMT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.billAmount)		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_FORMATED_ACTUAL_BILL_AMT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(Formatter.formatNumbers(this.billAmount))		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)

		
			
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_FORMATED_LATE_BILL_AMT)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(Formatter.formatNumbers(this.lateBillAmount))		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)	
	  
	  		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_BILL_PAID)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.billPaid == false ? 0 : 1)		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE) 
		
		
//		.append(TAG_SYMBOL_OPEN)
//		.append(TAG_PARAM)
//		.append(TAG_SYMBOL_SPACE)
//		.append(ATTR_PARAM_NAME)
//		.append(TAG_SYMBOL_EQUAL)
//		.append(TAG_SYMBOL_QUOTE)
//		.append(CommandFieldConstants.KEY_BILL_DATE)
//		.append(TAG_SYMBOL_QUOTE)
//		.append(TAG_SYMBOL_CLOSE)		
//		.append(this.billingMonth)		
//		.append(TAG_SYMBOL_OPEN)
//		.append(TAG_SYMBOL_SLASH)
//		.append(TAG_PARAM)
//		.append(TAG_SYMBOL_CLOSE)
//	  
//	  		
//		.append(TAG_SYMBOL_OPEN)
//		.append(TAG_PARAM)
//		.append(TAG_SYMBOL_SPACE)
//		.append(ATTR_PARAM_NAME)
//		.append(TAG_SYMBOL_EQUAL)
//		.append(TAG_SYMBOL_QUOTE)
//		.append(CommandFieldConstants.KEY_FORMATED_BILL_DATE)
//		.append(TAG_SYMBOL_QUOTE)
//		.append(TAG_SYMBOL_CLOSE)		
//		.append(Formatter.formatDate(this.billingMonth))		
//		.append(TAG_SYMBOL_OPEN)
//		.append(TAG_SYMBOL_SLASH)
//		.append(TAG_PARAM)
//		.append(TAG_SYMBOL_CLOSE)
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_BILL_DATE)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.billingMonth)		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)
			
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_LATE_BILL_DATE)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(this.dueDate)		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE)
		
				
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_FORMATED_LATE_BILL_DATE)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		.append(Formatter.formatDate(this.dueDate))		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE);
	  
	  */
	  return responseXML.toString();
	  
  }
  
  public String getResponseCode()
  {	
	return null;
  }

  public ProductVO populateVO( ProductVO billPaymentVO, BaseWrapper baseWrapper )
  {
  	UtilityBillVO utilityBillVO = ((UtilityBillVO)billPaymentVO);
  	
    if( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ) != null ) {
    	
    	utilityBillVO.setConsumerNo( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ).toString().trim());
    }
	
    if(StringUtil.isNullOrEmpty(utilityBillVO.getConsumerNo())) {
    
    	utilityBillVO.setConsumerNo( baseWrapper.getObject( CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER ).toString().trim());
	}
      
    return (ProductVO)utilityBillVO;
  }

  public void setResponseCode(String responseCode)
  {
	// TODO Auto-generated method stub	
  }

  public void validateVO(ProductVO productVO) throws FrameworkCheckedException
  {
	  if( ((UtilityBillVO)productVO).getConsumerNo() == null || ((UtilityBillVO)productVO).getConsumerNo().equals("") )
			throw new FrameworkCheckedException("Reference number is not provided.");	
  }

  public UtilityBillVO()
  {
  }

  public void setBillAmount(Double billAmount)
  {
    this.billAmount = billAmount;
  }

  public void setBillingMonth(String billingMonth)
  {
    this.billingMonth = billingMonth;
  }

  public void setPhoneNo(String phoneNo)
  {
    this.phoneNo = phoneNo;
  }

  public void setPaidDate(String paidDate)
  {
    this.paidDate = paidDate;
  }

  public void setLateBillAmount(Double lateBillAmount)
  {
    this.lateBillAmount = lateBillAmount;
  }

  public void setDueDate(Date dueDate)
  {
    this.dueDate = dueDate;
  }

  public void setCustomerName(String customerName)
  {
    this.customerName = customerName;
  }

  public void setConsumerNo(String consumerNo)
  {
    this.consumerNo = consumerNo;
  }

  public void setCompanyName(String companyName)
  {
    this.companyName = companyName;
  }

  public void setPaidAmount(Double paidAmount)
  {
    this.paidAmount = paidAmount;
  }

  public void setCustomerAddress(String customerAddress)
  {
    this.customerAddress = customerAddress;
  }

  public void setCompanyCode(String companyCode)
  {
    this.companyCode = companyCode;
  }

  public Double getBillAmount()
  {
    return (this.billAmount == null ? 0 : billAmount ) ;
  }

  public String getBillingMonth()
  {
    return billingMonth;
  }

  public String getCompanyCode()
  {
    return companyCode;
  }

  public String getCompanyName()
  {
    return companyName;
  }

  public String getConsumerNo()
  {
    return consumerNo;
  }

  public String getCustomerAddress()
  {
    return customerAddress;
  }

  public String getCustomerName()
  {
    return customerName;
  }

  public Date getDueDate()
  {
    return dueDate;
  }

  public String getPhoneNo()
  {
    return phoneNo;
  }

  public String getPaidDate()
  {
    return paidDate;
  }

  public Double getLateBillAmount()
  {
    return (lateBillAmount == null ? 0 : lateBillAmount) ;
  }

  public Double getPaidAmount()
  {
    return paidAmount;
  }


public boolean isBillPaid()
{
	return billPaid;
}


public void setBillPaid(boolean billPaid)
{
	this.billPaid = billPaid;
}

public Double getCurrentBillAmount()
{
	if(this.getBillingMonth() == null || this.getBillingMonth().equalsIgnoreCase("") )
	{
		//throw new WorkFlowException(WorkFlowErrorCodeConstants.INTEGRATION_ERROR);		
	}
	
	if(this.getDueDate() != null && DateUtils.getDayEndDate(this.getDueDate()).after( new Date() ))	
		return this.getBillAmount();
	else
		return this.getLateBillAmount();
	
}

public String isBillOverDue()
{
	if( this.getDueDate() == null || this.getBillingMonth() == null || this.getBillingMonth().equalsIgnoreCase("") )
	{
		throw new WorkFlowException(WorkFlowErrorCodeConstants.INTEGRATION_ERROR);		
	}
	
	//if( this.getDueDate().after( new Date() ) )
	if( DateUtils.getDayEndDate(this.getDueDate()).after( new Date() ))	
		return "0";
	else
		return "1";
	
}


public String getMfsId()
{
	return mfsId;
}


public void setMfsId(String mfsId)
{
	this.mfsId = mfsId;
}


public String getTransactionCode()
{
	return transactionCode;
}


public void setTransactionCode(String transactionCode)
{
	this.transactionCode = transactionCode;
}

}





