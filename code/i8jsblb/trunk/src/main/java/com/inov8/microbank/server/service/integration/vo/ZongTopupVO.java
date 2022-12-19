package com.inov8.microbank.server.service.integration.vo;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.StringUtil;

public class ZongTopupVO implements BillPaymentVO
{
	

	protected String mobileNumber;
    protected String packageType;
    protected String customerName;
    protected Double minimumAmount;
    protected Double totalAmount;
    protected Double billAmount;
    protected String responseCode;
    protected String responseDescription;
    protected String transactionId;
    protected String transactionTime;
    protected String transactionDate;
    
    protected Long serialNumber;
    

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

  

	public String responseXML()
	{
		StringBuilder responseXML = new StringBuilder();
		  		  
		  responseXML		  
			
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_MOB_NO)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.mobileNumber)		
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
			.append(CommandFieldConstants.KEY_MOB_NO_TYPE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append( this.packageType )		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)	;
		  
		  return responseXML.toString();
	}

	public void setConsumerNo(String consumerNo)
	{
		this.mobileNumber = consumerNo; 
		
	}

	public String getResponseCode()
	{		
		return responseCode;
	}

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper)
	{
		if( baseWrapper.getObject( CommandFieldConstants.KEY_MOB_NO ) != null )
		{
		    String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_MOB_NO ).toString().trim();
            mobileNo = this.formatWithoutZero(mobileNo);
            ((ZongTopupVO)productVO).setConsumerNo( mobileNo );
		}
		else if( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ).toString().trim();
		      ((ZongTopupVO)productVO).setConsumerNo( mobileNo );
		}
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) != null )
		{
		      ((ZongTopupVO)productVO).setBillAmount( Integer.parseInt(baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ).toString().trim()) );
		      ((ZongTopupVO)productVO).setCurrentBillAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ).toString().trim()) );
		
		}
		if( baseWrapper.getObject( CommandFieldConstants.KEY_BILL_AMOUNT ) == null )
		{	
			if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null )		
			{
				((ZongTopupVO)productVO).setCurrentBillAmount( Double.parseDouble(baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim()) );
				((ZongTopupVO)productVO).setBillAmount(Double.parseDouble(baseWrapper.getObject( "TXAM").toString().trim()) );
			}
		}
		
		return (ZongTopupVO)productVO;
	}

	public void setResponseCode(String responseCode)
	{
		this.responseCode = responseCode ;		
	}

	public void validateVO(ProductVO productVO) throws FrameworkCheckedException
	{
		if( ((ZongTopupVO)productVO).getConsumerNo() == null )
			throw new FrameworkCheckedException("Mobile number is not provided.");
		if( ((ZongTopupVO)productVO).getBillAmount() == null || ((ZongTopupVO)productVO).getBillAmount() == 0 )
			throw new FrameworkCheckedException("Bill amount is not provided.");		
	}
	

	
	
	
	
	public String getCustomerName()
	{
		return customerName;
	}
	
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public Double getBillAmount()
	{
		return billAmount;
	}
	
	public String getConsumerNo()
	{		
		return mobileNumber;
	}

	public Double getCurrentBillAmount()
	{		
		return minimumAmount;
	}
	
	public void setCurrentBillAmount( Double billAmount )
	{		
		minimumAmount = billAmount ;
	}

	public void setBillAmount(Double billAmount)
	{
		this.billAmount = billAmount ;
	}
	
	public void setBillAmount(Integer billAmount)
	{
//		this.billAmount = billAmount ;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	public String getPackageType()
	{
		return packageType;
	}

	public void setPackageType(String packageType)
	{
		this.packageType = packageType;
	}

	public Double getMinimumAmount()
	{
		return minimumAmount;
	}

	public void setMinimumAmount(Double minimumAmount)
	{
		this.minimumAmount = minimumAmount;
	}

	public Double getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public String getResponseDescription()
	{
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription)
	{
		this.responseDescription = responseDescription;
	}

	public String getTransactionId()
	{
		return transactionId;
	}

	public void setTransactionId(String transactionId)
	{
		this.transactionId = transactionId;
	}

	public String getTransactionTime()
	{
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime)
	{
		this.transactionTime = transactionTime;
	}

	public String getTransactionDate()
	{
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate)
	{
		this.transactionDate = transactionDate;
	}
		
	private String formatWithoutZero(String cellNumber)
    {
        if(null != cellNumber && !"".equalsIgnoreCase(cellNumber))
        {
//          mobNum = mobNum.replaceFirst("0", "0092");
            if (cellNumber.startsWith("+92") && cellNumber.length() == 13) {
                
                cellNumber = cellNumber.substring(3);
                
            } else if (cellNumber.startsWith("0092") && cellNumber.length() == 14 && StringUtil.isInteger(cellNumber)) {
                cellNumber = cellNumber.substring(4);
            } else if (cellNumber.startsWith("3") && cellNumber.length() == 10 && StringUtil.isInteger(cellNumber)) {
                
            } else if (cellNumber.startsWith("92") && cellNumber.length() == 12 && StringUtil.isInteger(cellNumber)) {
                cellNumber = cellNumber.substring(2);
            } else if (cellNumber.startsWith("03") && cellNumber.length() == 11 && StringUtil.isInteger(cellNumber)) {
                cellNumber = cellNumber.substring(1);
            }
            
        }
        
        return cellNumber;
    }

}
