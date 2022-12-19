package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.ola.integration.vo.OLAVO;

public interface CreditAccountQueueSender {

	void send(OLAVO olaVO) throws FrameworkCheckedException;
}