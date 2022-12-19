package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

public class BillAuthorizationSenderImpl implements BillAuthorizationSender {

	private JmsProducer jmsProducer;
	
	@Override
	public void send(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO) throws FrameworkCheckedException {
		
		this.jmsProducer.produce(phoenixIntegrationMessageVO, DestinationConstants.PHOENIX_BILL_PAYMENT_DESTINATION);
	}

	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}
	
}
