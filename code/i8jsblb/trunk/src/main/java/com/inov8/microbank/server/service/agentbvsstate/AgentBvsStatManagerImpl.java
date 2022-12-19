package com.inov8.microbank.server.service.agentbvsstate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AgentBvsStatModel;

import com.inov8.microbank.server.service.agentbvsstate.dao.AgentBvsStatDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AgentBvsStatManagerImpl  implements AgentBvsStatManager{
    protected final Log logger = LogFactory.getLog(AgentBvsStatManagerImpl.class);
    private AgentBvsStatDAO agentBvsStatDAO;

    @Override
    public AgentBvsStatModel saveOrUpdate(AgentBvsStatModel agentBvsStatModel) throws FrameworkCheckedException {
        return agentBvsStatDAO.saveOrUpdate(agentBvsStatModel);
    }


    public void setAgentBvsStatDAO(AgentBvsStatDAO agentBvsStatDAO) {
        this.agentBvsStatDAO = agentBvsStatDAO;
    }
}
