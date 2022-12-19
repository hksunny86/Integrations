package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.server.service.jms.JmsProducer;

import java.io.Serializable;

public class ReversalAdviceSenderImpl implements ReversalAdviceSender {

    private JmsProducer jmsProducer;

    @Override
    public void send(Object o) throws FrameworkCheckedException {
        this.jmsProducer.produce((Serializable) o, DestinationConstants.REVERSAL_ADVICE_QUEUE);
    }

    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }
}
