/**
 * 
 */
package com.inov8.microbank.server.service;

import java.io.Serializable;
import java.util.List;

import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

/**
 * Project Name: Commons-Integration
 * @author Jawwad Farooq Creation Date: July 7, 2009 
 */


public abstract class PaymentServiceVO implements IntegrationMessageVO
{
	public abstract String getPaymentGatewayCode();
	public abstract void setPaymentGatewayCode(String code);
	public abstract String getMicrobankTransactionCode();
	public abstract void setMicrobankTransactionCode(String transactionCode);
	public abstract String getSystemTraceAuditNumber();
	public abstract String getTransmissionDateAndTime();
	public abstract String getRetrievalReferenceNumber();
	public abstract String getResponseCode();
	public abstract long getTimeOutInterval();
	public abstract void setTimeOutInterval( long timeOut);
	public List<CustomerAccount> getCustomerAccounts()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String getIvrChannelStatus()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String getMessageAsEdi()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String getMobileChannelStatus()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String getSecureVerificationData()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public void setSecureVerificationData(String secureVerificationData)
	{
		// TODO Auto-generated method stub
		
	}
}
