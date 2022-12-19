package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class JMSAgentSenderImpl implements JMSAgentSender
{

	private JmsProducer jmsProducer;
	
	@Override
	public void send(RetailerContactListViewFormModel agentFormModel) throws FrameworkCheckedException
	{
		System.out.println("before sending message to jms");
		this.jmsProducer.produce(agentFormModel, DestinationConstants.AGENT_DESTINATION);
		System.out.println("after sending message to jms");
	}

	public void setJmsProducer(JmsProducer jmsProducer)
	{
		this.jmsProducer = jmsProducer;
	}
	
}
