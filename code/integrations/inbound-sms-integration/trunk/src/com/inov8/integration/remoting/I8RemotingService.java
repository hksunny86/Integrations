package com.inov8.integration.remoting;

import com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean;

public interface I8RemotingService {
	public InboundSMSServiceBean checkBalance(InboundSMSServiceBean messageVO);
	public InboundSMSServiceBean miniStatement(InboundSMSServiceBean messageVO);
}
