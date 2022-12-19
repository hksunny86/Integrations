package com.inov8.microbank.server.service.agentlocationstat;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AgentLocationStatModel;
import sun.management.Agent;


public interface AgentLocationStatManager {
    AgentLocationStatModel saveOrUpdate(AgentLocationStatModel agentBvsStatModel) throws FrameworkCheckedException;

    AgentLocationStatModel getAgentLocationStat(AgentLocationStatModel agentLocationStatModel) throws  FrameworkCheckedException;
}
