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
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;


public class AgentToAgentTransferVO implements ProductVO
{	

	protected String agentName;
    protected String agentCNIC;
    protected String agentMobileNo;
    protected Double amount;
	
    protected Long appUserId;
    protected Long agentId;
    protected String mfsId;
    protected Double balance;
    
    
    
    public Double getBalance()
	{
		return balance;
	}

	public void setBalance(Double balance)
	{
		this.balance = balance;
	}

	public String getMfsId()
	{
		return mfsId;
	}

	public void setMfsId(String mfsId)
	{
		this.mfsId = mfsId;
	}

	public Long getAppUserId()
	{
		return appUserId;
	}

	public void setAppUserId(Long appUserId)
	{
		this.appUserId = appUserId;
	}

	public Long getAgentId()
	{
		return agentId;
	}

	public void setAgentId(Long agentId)
	{
		this.agentId = agentId;
	}

	public Double getBillAmount()
	{		
		return amount;
	}

	public Double getCurrentBillAmount()
	{	
		return amount;
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
			.append(CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.agentMobileNo)		
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
			.append(CommandFieldConstants.KEY_RECEPIENT_NAME)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)		
			.append(this.agentName)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)
			
			
				;
		  
		  return responseXML.toString();
	}

	public void setBillAmount(Double billAmount)
	{
		this.amount = billAmount;
		
	}

	public String getResponseCode()
	{
		return null;
	}

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper)
	{
		if( baseWrapper.getObject( CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE ) != null )
		{
			String mobileNo = baseWrapper.getObject( CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE ).toString().trim();
		      ((AgentToAgentTransferVO)productVO).setAgentMobileNo( mobileNo );
		}else if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null ){
			String amount = baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim();
		      ((AgentToAgentTransferVO)productVO).setAmount(Double.parseDouble(amount));
		}
		
		return (ProductVO)productVO;
	}

	public void setResponseCode(String responseCode)
	{
		// TODO Auto-generated method stub
		
	}

	public void validateVO(ProductVO productVO) throws FrameworkCheckedException
	{
		if( ((AgentToAgentTransferVO)productVO).getAgentMobileNo() == null )
			throw new FrameworkCheckedException("Recipient Agent Mobile number is not provided.");
		if( ((AgentToAgentTransferVO)productVO).getAgentMobileNo().length() != 11 )
			throw new FrameworkCheckedException("Recipient Agent Mobile number is invalid.");
		
		ValidationErrors validationErrors = new ValidationErrors(); 
		validationErrors = ValidatorWrapper.doNumeric(((AgentToAgentTransferVO)productVO).getAgentMobileNo(), validationErrors, "Mobile number");
		
		if( validationErrors.hasValidationErrors() )
		{
			throw new FrameworkCheckedException("Recipient Agent Mobile number is invalid.");
		}
		
	}
	

	public Double getAmount()
	{
		return amount;
	}
	
	public void setAmount(Double amount)
	{
		this.amount = amount;
	}
	
	public String getAgentName()
	{
		return agentName;
	}
	
	public void setCustomerName(String agentName)
	{
		this.agentName = agentName;
	}
	
    public String getAgentCNIC() {
		return agentCNIC;
	}

	public void setAgentCNIC(String agentCNIC) {
		this.agentCNIC = agentCNIC;
	}

    public String getAgentMobileNo() {
		return agentMobileNo;
	}

	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

}
