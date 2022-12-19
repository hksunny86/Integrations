package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class DebitPaymentReqQueueImpl implements DebitPaymentRequestQueue {
    private JmsProducer jmsProducer;

    @Override
    public void sentWalletRequest(MiddlewareAdviceVO messageVO) throws FrameworkCheckedException {
        this.jmsProducer.produce(messageVO, DestinationConstants.DEDIT_PAYMENT_DESTINATION);

    }

    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }
}
