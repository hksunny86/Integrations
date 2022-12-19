package com.inov8.microbank.common.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class SmsSenderImpl implements SmsSender
{
	protected static Log logger = LogFactory.getLog(SmsSenderImpl.class);
	private JmsProducer jmsProducer;
	
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

	public void sendDelayed(SmsMessage smsMessage) throws FrameworkCheckedException
	{		
		this.jmsProducer.produce(smsMessage, DestinationConstants.DELAYED_SMS_DESTINATION);
	}

	public void setJmsProducer(JmsProducer jmsProducer)
	{
		this.jmsProducer = jmsProducer;
	}	
}
