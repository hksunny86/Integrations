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

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;

/**
 * 
 * @author Jawwad Farooq
 *
 */

public class CashInVO implements ProductVO, Serializable
{
	
  
	protected Double transactionAmount;
	protected Double transactionProcessingAmount;
    protected Double totalAmount;
    
    protected String agentMobileNo;
    
    protected String customerMobileNo;
    protected Long customerAppUserId;
	protected Long customerId;
	protected String customerMFSID;
    protected Double customerBalance;
	protected String customerCNIC;
	protected String customerName;
    
	protected Double agentBalance;

  private static final long serialVersionUID = 6219262453169387336L;
  
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
			.append(CommandFieldConstants.KEY_CUSTOMER_MOBILE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.customerMobileNo)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE);
		  
		  return responseXML.toString();
	}
  
  public String getResponseCode()
  {	
	return null;
  }

  public ProductVO populateVO( ProductVO productVO, BaseWrapper baseWrapper )
  {		CashInVO cashInVO = (CashInVO) productVO;
	
  if( baseWrapper.getObject( CommandFieldConstants.KEY_PROD_ID ) != null 
		  &&  baseWrapper.getObject( CommandFieldConstants.KEY_PROD_ID ).equals(ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.toString())){
		
	  	if( baseWrapper.getObject( CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE ) != null ){
			String value = baseWrapper.getObject( CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE ).toString().trim();
			cashInVO.setCustomerMobileNo( value );
		}
  }else{
		if( baseWrapper.getObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE ) != null ){
			String value = baseWrapper.getObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE ).toString().trim();
			cashInVO.setCustomerMobileNo( value );
		}
  }
	
	if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null ){
		String value = baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim();
		try{
			cashInVO.setTransactionAmount( Double.parseDouble(value) );
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
	}
	
	return cashInVO;
}

  public void setResponseCode(String responseCode)
  {
	// TODO Auto-generated method stub	
  }

  public void validateVO(ProductVO productVO) throws FrameworkCheckedException
  {
	  if( ((CashInVO)productVO).getCustomerMobileNo() == null || ((CashInVO)productVO).getCustomerMobileNo().equals("") ){
			throw new FrameworkCheckedException("Mobile number is not provided.");	
	  }	

	  if( ((CashInVO)productVO).getTransactionAmount() == null || ((CashInVO)productVO).getTransactionAmount().equals("") ){
			throw new FrameworkCheckedException("Amount is not provided.");	
	  }	

  }

  public CashInVO()
  {
  }


  
	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Double getTransactionProcessingAmount() {
		return transactionProcessingAmount;
	}

	public void setTransactionProcessingAmount(Double transactionProcessingAmount) {
		this.transactionProcessingAmount = transactionProcessingAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAgentMobileNo() {
		return agentMobileNo;
	}

	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}

	public String getCustomerMobileNo() {
		return customerMobileNo;
	}

	public void setCustomerMobileNo(String customerMobileNo) {
		this.customerMobileNo = customerMobileNo;
	}

	public Long getCustomerAppUserId() {
		return customerAppUserId;
	}

	public void setCustomerAppUserId(Long customerAppUserId) {
		this.customerAppUserId = customerAppUserId;
	}

	public String getCustomerMFSID() {
		return customerMFSID;
	}

	public void setCustomerMFSID(String customerMFSID) {
		this.customerMFSID = customerMFSID;
	}

	public Double getCustomerBalance() {
		return customerBalance;
	}

	public void setCustomerBalance(Double customerBalance) {
		this.customerBalance = customerBalance;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Double getAgentBalance() {
		return agentBalance;
	}

	public void setAgentBalance(Double agentBalance) {
		this.agentBalance = agentBalance;
	}
    
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCNIC() {
		return customerCNIC;
	}

	public void setCustomerCNIC(String customerCNIC) {
		this.customerCNIC = customerCNIC;
	}
}





