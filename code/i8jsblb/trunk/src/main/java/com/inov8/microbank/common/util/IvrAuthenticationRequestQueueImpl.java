package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.ola.integration.vo.OLAVO;

public class IvrAuthenticationRequestQueueImpl implements IvrAuthenticationRequestQueue {

	private JmsProducer jmsProducer;
	
	@Override
	public void sentAuthenticationRequest(IvrRequestDTO dto) throws FrameworkCheckedException {
		
		this.jmsProducer.produce(dto, DestinationConstants.IVR_AUTHENTICATION_DESTINATION);
	}

	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}
	
}
