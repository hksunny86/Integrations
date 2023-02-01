package com.inov8.microbank.common.util;

import java.util.ArrayList;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class NonTransactedSmsSenderImpl implements SmsSender
{
	protected static Log logger = LogFactory.getLog(NonTransactedSmsSenderImpl.class);
	private JmsProducer jmsProducer;
	private ESBAdapter esbAdapter;
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.common.util.SmsSender#send(com.inov8.microbank.common.model.messagemodule.SmsMessage)
	 */
	public void send(SmsMessage smsMessage) throws FrameworkCheckedException {		
		
		try {

			this.jmsProducer.produce(smsMessage, DestinationConstants.OUTGOING_SMS_DESTINATION);
			
		} catch (Exception e) {

			logger.error(e);
			throw new WorkFlowException(MessageUtil.getMessage(TransactionConstantsInterface.GENERIC_ERROR_MESSAGE));
		}
	}

	@Override
	public void send( ArrayList<SmsMessage> smsMessageList ) throws FrameworkCheckedException
	{
	    try
	    {
            this.jmsProducer.produce(smsMessageList, DestinationConstants.OUTGOING_SMS_DESTINATION);
            
        }
	    catch (Exception e)
	    {
            logger.error(e);
            throw new WorkFlowException(MessageUtil.getMessage(TransactionConstantsInterface.GENERIC_ERROR_MESSAGE));
        }
	}
	public void pushNotification(SmsMessage smsMessage) throws FrameworkCheckedException {
		SwitchWrapper sWrapper = new SwitchWrapperImpl();
		I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
		requestVO.setMobileNumber(smsMessage.getMobileNo());
		requestVO.setTitle(smsMessage.getTitle());
		requestVO.setMessage(smsMessage.getMessageText());
		requestVO.setMessageType(smsMessage.getMessageType());
		requestVO.setRequestType(I8SBConstants.RequestType_SendPushNotification);
		sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
		this.esbAdapter.makeI8SBCall(sWrapper);
	}

	public void sendDelayed(SmsMessage smsMessage) throws FrameworkCheckedException
	{		
		this.jmsProducer.produce(smsMessage, DestinationConstants.DELAYED_SMS_DESTINATION);
	}

	public void setJmsProducer(JmsProducer jmsProducer)
	{
		this.jmsProducer = jmsProducer;
	}
	public void setEsbAdapter(ESBAdapter esbAdapter) {
		this.esbAdapter = esbAdapter;
	}
}
