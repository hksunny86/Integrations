package com.inov8.microbank.server.dao.agentgroup;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AgentTaggingChildrenModel;

public interface AgentTaggingChildrenDAO extends BaseDAO<AgentTaggingChildrenModel, Long> {

	public void deleteAgentTaggingChildrenList(Long agentTaggingId);
}
