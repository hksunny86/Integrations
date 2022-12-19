package com.inov8.microbank.server.service.agentbvsstate.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.AgentBvsStatModel;

public interface AgentBvsStatDAO extends BaseDAO<AgentBvsStatModel, Long> {

    AgentBvsStatModel loadAgentBvsStatByAgentId(Long agentId) throws FrameworkCheckedException;

}
