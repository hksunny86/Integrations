package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class WalletIncomingReqQueueImpl implements WalletIncomingRequestQueue {
    private JmsProducer jmsProducer;

    @Override
    public void sentWalletRequest(MiddlewareAdviceVO messageVO) throws FrameworkCheckedException {
        this.jmsProducer.produce(messageVO, DestinationConstants.WALLET_TO_CORE_DESTINATION);

    }

    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }
}
