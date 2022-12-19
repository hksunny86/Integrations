package com.inov8.microbank.server.service.agentlocationstat.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AgentLocationStatModel;

public interface AgentLocationStatDAO extends BaseDAO<AgentLocationStatModel,Long> {
    AgentLocationStatModel loadAgentLocationStatByAgentId(Long agentId) throws FrameworkCheckedException;
}
