package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

public interface BillAuthorizationSender {

	void send(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO) throws FrameworkCheckedException;
}