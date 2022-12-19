package com.inov8.microbank.server.facade;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class JmsProducerFacadeImpl implements JmsProducerFacade
{
	private final Log logger = LogFactory.getLog(this.getClass());

	private JmsProducer jmsProducer;

	

	public void produce(Serializable obj, String destinationName) throws FrameworkCheckedException
	{
		try
		{
			this.jmsProducer.produce(obj, destinationName);
		}
		catch (Exception ex)
		{
			logger.warn("Exception occurred while publishing message: " + ex.getMessage());
			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}

	}

	public void setJmsProducer(JmsProducer jmsProducer)
	{
		this.jmsProducer = jmsProducer;
	}
}
