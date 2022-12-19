package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.jms.JmsProducer;

/**
 * Created by Attique on 9/6/2018.
 */
public class ThirdPartyCashOutAdviceSenderImpl implements  ThirdPartyCashOutAdviceSender {

    private JmsProducer jmsProducer;

    @Override
    public void send(SwitchWrapper switchWrapper) throws FrameworkCheckedException {

        this.jmsProducer.produce(switchWrapper, DestinationConstants.THIRD_PARTY_CASH_OUT_ADVICE_DESTINATION);
    }

    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

}
