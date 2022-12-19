package com.inov8.microbank.server.service.agentbvsstate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AgentBvsStatModel;

public interface AgentBvsStatManager{

    AgentBvsStatModel saveOrUpdate(AgentBvsStatModel agentBvsStatModel) throws FrameworkCheckedException;

}
