/**
 * 
 */
package com.inov8.microbank.server.service.smssendermodule;

import java.util.ArrayList;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.FailedSmsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 4, 2007
 * Creation Time: 			5:40:29 PM
 * Description:				
 */
public interface SmsSenderService
{

	public abstract void sendSms(String mobileNumber, String textToSms)
			throws FrameworkCheckedException;

	public abstract void resendSms(Long transactionId)
			throws FrameworkCheckedException;

	public abstract void resendSmsUsingStrategy(Long transactionId, String resendSmsStrategy) throws FrameworkCheckedException;
	
	public void resendSmsUsingStrategy(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	
	public abstract void resendSms(Long transactionId, String mobileNumber)
			throws FrameworkCheckedException;

	public abstract void resendCashWithdrawalSms(Long transactionId)
			throws FrameworkCheckedException;
	
	public void resendCashPaymentSms(Long transactionId,Boolean isCash2Cash)
			throws FrameworkCheckedException;

	public void sendSmsList(ArrayList<SmsMessage> smsMessageList)
			throws FrameworkCheckedException;
	
	public void saveFailedSms(SmsMessage smsMessage) 
			throws FrameworkCheckedException;

	public void makeFailedSmsRetry(FailedSmsModel failedSmsModel) 
			throws FrameworkCheckedException;
}