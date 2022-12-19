package com.inov8.microbank.server.service.agentlocationstat;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AgentLocationStatModel;
import com.inov8.microbank.server.service.agentlocationstat.dao.AgentLocationStatDAO;

public class AgentLocationStatManagerImpl implements AgentLocationStatManager{
    private AgentLocationStatDAO agentLocationStatDAO;
    @Override
    public AgentLocationStatModel saveOrUpdate(AgentLocationStatModel agentBvsStatModel) throws FrameworkCheckedException {
        return this.agentLocationStatDAO.saveOrUpdate(agentBvsStatModel);
    }

    @Override
    public AgentLocationStatModel getAgentLocationStat(AgentLocationStatModel agentLocationStatModel) throws FrameworkCheckedException {
         agentLocationStatModel = this.agentLocationStatDAO.loadAgentLocationStatByAgentId(agentLocationStatModel.getAgentId());
         return agentLocationStatModel;
    }

    public void setAgentLocationStatDAO(AgentLocationStatDAO agentLocationStatDAO) {
        this.agentLocationStatDAO = agentLocationStatDAO;
    }
}
