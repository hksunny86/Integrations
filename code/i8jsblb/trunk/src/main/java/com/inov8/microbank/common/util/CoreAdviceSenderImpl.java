package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class CoreAdviceSenderImpl implements CoreAdviceSender {

	private JmsProducer jmsProducer;
	
	@Override
	public void send(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException {
		
		this.jmsProducer.produce(middlewareAdviceVO, DestinationConstants.CORE_ADVICE_DESTINATION);
	}

	@Override
	public void sendForAccOpening(MiddlewareAdviceVO middlewareAdviceVO, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

		this.jmsProducer.produce(middlewareAdviceVO, DestinationConstants.CORE_ADVICE_DESTINATION);
	}

	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}
	
}
