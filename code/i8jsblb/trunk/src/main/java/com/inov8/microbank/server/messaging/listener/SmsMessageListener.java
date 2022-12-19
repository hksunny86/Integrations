package com.inov8.microbank.server.messaging.listener;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.RealTimeSmsSender;

public class SmsMessageListener implements MessageListener
{
	private RealTimeSmsSender realTimeSmsSender;
	protected static Log logger	= LogFactory.getLog(SmsMessageListener.class);

	public void onMessage(Message message)
	{
		if (message instanceof ObjectMessage)
		{

			try
			{
				Object obj = ((ObjectMessage) message).getObject();
				if (null != obj)
				{
					if (obj instanceof SmsMessage)
					{
						if(logger.isDebugEnabled())
						{
//							logger.debug("Handing sms over to sender...");
						}
						this.realTimeSmsSender.send((SmsMessage) obj);
					}
					else if(obj instanceof ArrayList<?>)
					{
					    ArrayList<SmsMessage> smsMessageList = (ArrayList<SmsMessage>) obj;
					    long start = System.currentTimeMillis();
					    for( SmsMessage smsMessage: smsMessageList )
					    {
					        try
                            {
                                this.realTimeSmsSender.send(smsMessage);
                            }
                            catch( Exception e )
                            {
                                logger.error( "Agent Mobile Number: " + smsMessage.getMobileNo() + " SMS Text: " + smsMessage.getMessageText(), e );
                            }
					    }
					    long end = System.currentTimeMillis();
					    logger.info( "Time(ms) taken to push " + smsMessageList.size() +" SMS to kannel:  " + (end-start) );
					}
				}
			}
			catch (JMSException ex)
			{
				throw new RuntimeException(ex.getMessage(),ex);
			}
			catch (Exception ex)
			{
				throw new RuntimeException(ex.getMessage(),ex);
			}
		}

	}

	public void setRealTimeSmsSender(RealTimeSmsSender realTimeSmsSender)
	{
		this.realTimeSmsSender = realTimeSmsSender;
	}

}
