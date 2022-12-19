/**
 * 
 */
package com.inov8.microbank.server.service.switchmodule;

import java.io.Serializable;
import java.util.List;

import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

/**
 * Project Name: 			Financial-Integration	
 * @author Imran Sarwar
 * Creation Date: 			Nov 5, 2007
 * Creation Time: 			1:24:26 PM
 * Description:				
 */
public interface IntegrationMessageVO extends Serializable
{
	public String getPaymentGatewayCode();
	public void setPaymentGatewayCode(String code);
	public String getMicrobankTransactionCode();
	public void setMicrobankTransactionCode(String transactionCode);
	public String getSystemTraceAuditNumber();
	public String getTransmissionDateAndTime();
	public String getMessageAsEdi();
	public String getRetrievalReferenceNumber();
	public List<CustomerAccount> getCustomerAccounts();
	public String getSecureVerificationData();
	public void setSecureVerificationData(String secureVerificationData);
	public String getResponseCode();
	public String getIvrChannelStatus();	
	public String getMobileChannelStatus();
	public long getTimeOutInterval();
	public void setTimeOutInterval( long timeOut);
}
