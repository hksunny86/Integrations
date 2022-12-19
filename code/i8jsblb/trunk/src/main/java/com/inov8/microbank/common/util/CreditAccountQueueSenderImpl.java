package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.ola.integration.vo.OLAVO;

public class CreditAccountQueueSenderImpl implements CreditAccountQueueSender {

	private JmsProducer jmsProducer;
	
	@Override
	public void send(OLAVO olaVO) throws FrameworkCheckedException {
		
		this.jmsProducer.produce(olaVO, DestinationConstants.CREDIT_ACCOUNT_DESTINATION);
	}

	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}
	
}
