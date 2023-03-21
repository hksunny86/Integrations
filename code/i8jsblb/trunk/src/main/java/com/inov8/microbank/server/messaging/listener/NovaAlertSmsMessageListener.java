package com.inov8.microbank.server.messaging.listener;

import com.inov8.microbank.common.model.messagemodule.NovaAlertMessage;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.RealTimeSmsSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.Date;

public class NovaAlertSmsMessageListener extends SmsMessageListener
{
	private RealTimeSmsSender realTimeSmsSender;

	protected static Log logger	= LogFactory.getLog(NovaAlertSmsMessageListener.class);

	@Override
	public void onMessage(Message message)
	{
		if (message instanceof ObjectMessage)
		{

			try
			{
				Object obj = ((ObjectMessage) message).getObject();
				if (null != obj)
				{
					if (obj instanceof NovaAlertMessage)
					{
						if(logger.isDebugEnabled())
						{
//							logger.debug("Handing sms over to sender...");
						}
						this.realTimeSmsSender.novaAlertSMS((NovaAlertMessage) obj);
					}
					else if(obj instanceof ArrayList<?>)
					{
						ArrayList<NovaAlertMessage> smsMessageList = (ArrayList<NovaAlertMessage>) obj;
						long start = System.currentTimeMillis();
						for( NovaAlertMessage smsMessage: smsMessageList )
						{
							try
							{
								this.realTimeSmsSender.novaAlertSMS(smsMessage);
							}
							catch( Exception e )
							{
								logger.error( "Agent Mobile Number: " + smsMessage.getMobileNo(), e );
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
