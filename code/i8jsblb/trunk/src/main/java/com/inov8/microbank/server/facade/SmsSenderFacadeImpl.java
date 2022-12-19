/**
 * 
 */
package com.inov8.microbank.server.facade;

import java.util.ArrayList;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.FailedSmsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 4, 2007
 * Creation Time: 			8:22:05 PM
 * Description:				
 */
public class SmsSenderFacadeImpl implements SmsSenderService
{
	private SmsSenderService smsSenderService;

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.smssendermodule.SmsSenderService#resendSms(java.lang.Long)
	 */
	public void resendSms(Long transactionId) throws FrameworkCheckedException
	{
		smsSenderService.resendSms(transactionId);
	}

	@Override
    public void resendSmsUsingStrategy( Long transactionId, String resendSmsStrategy ) throws FrameworkCheckedException
    {
	    smsSenderService.resendSmsUsingStrategy( transactionId, resendSmsStrategy );
	}
	
	@Override
    public void resendSmsUsingStrategy(BaseWrapper baseWrapper) throws FrameworkCheckedException
    {
	    smsSenderService.resendSmsUsingStrategy(baseWrapper);
	}

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.smssendermodule.SmsSenderService#resendSms(java.lang.Long, java.lang.String)
	 */
	public void resendSms(Long transactionId, String mobileNumber)
			throws FrameworkCheckedException
	{
		smsSenderService.resendSms(transactionId, mobileNumber);

	}

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.smssendermodule.SmsSenderService#sendSms(java.lang.String, java.lang.String)
	 */
	public void sendSms(String mobileNumber, String textToSms)
			throws FrameworkCheckedException
	{
		smsSenderService.sendSms(mobileNumber, textToSms);
	}

	public void resendCashWithdrawalSms(Long transactionId)
			throws FrameworkCheckedException {

		smsSenderService.resendCashWithdrawalSms(transactionId);
	}

	public void resendCashPaymentSms(Long transactionId, Boolean isCash2Cash)
			throws FrameworkCheckedException {
		
		smsSenderService.resendCashPaymentSms(transactionId,isCash2Cash);
	}

	public void setSmsSenderService(SmsSenderService smsSenderService)
    {
        this.smsSenderService = smsSenderService;
    }

	public void sendSmsList(ArrayList<SmsMessage> smsMessageList) throws FrameworkCheckedException {
	}

	@Override
	public void saveFailedSms(SmsMessage smsMessage)
			throws FrameworkCheckedException {
		
		smsSenderService.saveFailedSms(smsMessage);
	}
	
	@Override
	public void makeFailedSmsRetry(FailedSmsModel failedSmsModel) throws FrameworkCheckedException{
		smsSenderService.makeFailedSmsRetry(failedSmsModel);
	}	
}
