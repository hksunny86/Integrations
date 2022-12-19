package com.inov8.microbank.server.messaging.listener;

import java.util.Date;

import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DelayedSmsMessageListener extends SmsMessageListener
{
	private String delay;
	protected static Log logger	= LogFactory.getLog(DelayedSmsMessageListener.class);

	@Override
	public void onMessage(Message message)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of onMessage of DelayedSmsMessageListener");
		}
		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Before going to sleep "+new Date());
			}
			Thread.sleep(Long.valueOf(delay));
			if(logger.isDebugEnabled())
			{
				logger.debug("Back from sleep "+new Date());
			}
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onMessage(message);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of onMessage of DelayedSmsMessageListener");
		}

	}

	public void setDelay(String delay)
	{
		this.delay = delay;
	}


	

}
