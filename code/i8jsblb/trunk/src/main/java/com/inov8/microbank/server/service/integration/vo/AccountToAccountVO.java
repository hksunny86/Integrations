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


public class AccountToAccountVO implements ProductVO
{	
	
	protected Double transactionAmount;
	protected Double transactionProcessingAmount;
    protected Double totalAmount;
    
    protected String agentMobileNo;
    
    protected String senderCustomerMobileNo;
    protected Long senderAppUserId;
	protected Long senderCustomerId;
    protected String senderMFSID;
    protected Double senderCustomerBalance;

	protected String recipientCustomerMobileNo;
    protected Long recipientAppUserId;
    protected Long recipientCustomerId;
	protected String recipientName;
    protected String recipientMFSID;
    protected Double recipientCustomerBalance;
    
	protected Double agentBalance;
    
    
    

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
			.append(this.senderCustomerMobileNo)		
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE);
		  
		  return responseXML.toString();
	}

	

	public ProductVO populateVO(ProductVO productVO, BaseWrapper baseWrapper)
	{
		AccountToAccountVO accToAccVO = (AccountToAccountVO) productVO;
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE ) != null ){
			String value = baseWrapper.getObject( CommandFieldConstants.KEY_CUSTOMER_MOBILE ).toString().trim();
			accToAccVO.setSenderCustomerMobileNo( value );
		}
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE ) != null ){
			String value = baseWrapper.getObject( CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE ).toString().trim();
			accToAccVO.setRecipientCustomerMobileNo( value );
		}
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_AGENT_MOBILE ) != null ){
			String value = baseWrapper.getObject( CommandFieldConstants.KEY_AGENT_MOBILE ).toString().trim();
			accToAccVO.setAgentMobileNo( value );
		}
		
		if( baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ) != null ){
			String value = baseWrapper.getObject( CommandFieldConstants.KEY_TX_AMOUNT ).toString().trim();
			try{
				accToAccVO.setTransactionAmount( Double.parseDouble(value) );
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		
		return accToAccVO;
	}

	public void validateVO(ProductVO productVO) throws FrameworkCheckedException {
		AccountToAccountVO accToAccVO = (AccountToAccountVO) productVO;
		
		if(accToAccVO.getSenderCustomerMobileNo() == null){
			throw new FrameworkCheckedException("Sender mobile number is not provided.");
		}
		
		if(accToAccVO.getRecipientCustomerMobileNo() == null){
			throw new FrameworkCheckedException("Recipient mobile number is not provided.");
		}
		
		//this code is commented due customer initiated transactions
/*		if(accToAccVO.getAgentMobileNo() == null){
			throw new FrameworkCheckedException("Agent mobile number is not provided.");
		}*/
		
		if(accToAccVO.getTransactionAmount() == null || accToAccVO.getTransactionAmount() == 0){
			throw new FrameworkCheckedException("Transaction amount is not provided.");
		}
		
	}
	
	public String getResponseCode()
	{
		return null;
	}
	
	public void setResponseCode(String responseCode)
	{
		// TODO Auto-generated method stub
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

	public String getSenderCustomerMobileNo() {
		return senderCustomerMobileNo;
	}

	public void setSenderCustomerMobileNo(String senderCustomerMobileNo) {
		this.senderCustomerMobileNo = senderCustomerMobileNo;
	}

	public Double getSenderCustomerBalance() {
		return senderCustomerBalance;
	}

	public void setSenderCustomerBalance(Double senderCustomerBalance) {
		this.senderCustomerBalance = senderCustomerBalance;
	}

	public Double getAgentBalance() {
		return agentBalance;
	}

	public void setAgentBalance(Double agentBalance) {
		this.agentBalance = agentBalance;
	}

    public String getRecipientCustomerMobileNo() {
		return recipientCustomerMobileNo;
	}



	public void setRecipientCustomerMobileNo(String recipientCustomerMobileNo) {
		this.recipientCustomerMobileNo = recipientCustomerMobileNo;
	}



	public Long getRecipientAppUserId() {
		return recipientAppUserId;
	}



	public void setRecipientAppUserId(Long recipientAppUserId) {
		this.recipientAppUserId = recipientAppUserId;
	}



	public Long getRecipientCustomerId() {
		return recipientCustomerId;
	}



	public void setRecipientCustomerId(Long recipientCustomerId) {
		this.recipientCustomerId = recipientCustomerId;
	}



	public String getRecipientName() {
		return recipientName;
	}



	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}



	public String getRecipientMFSID() {
		return recipientMFSID;
	}



	public void setRecipientMFSID(String recipientMFSID) {
		this.recipientMFSID = recipientMFSID;
	}
	
    public Long getSenderAppUserId() {
		return senderAppUserId;
	}

	public void setSenderAppUserId(Long senderAppUserId) {
		this.senderAppUserId = senderAppUserId;
	}

	public Long getSenderCustomerId() {
		return senderCustomerId;
	}

	public void setSenderCustomerId(Long senderCustomerId) {
		this.senderCustomerId = senderCustomerId;
	}


    public String getSenderMFSID() {
		return senderMFSID;
	}

	public void setSenderMFSID(String senderMFSID) {
		this.senderMFSID = senderMFSID;
	}

    public Double getRecipientCustomerBalance() {
		return recipientCustomerBalance;
	}

	public void setRecipientCustomerBalance(Double recipientCustomerBalance) {
		this.recipientCustomerBalance = recipientCustomerBalance;
	}


}
