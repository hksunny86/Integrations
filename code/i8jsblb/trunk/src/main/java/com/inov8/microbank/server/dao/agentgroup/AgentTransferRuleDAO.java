package com.inov8.microbank.server.dao.agentgroup;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AgentTransferRuleModel;

public interface AgentTransferRuleDAO extends BaseDAO<AgentTransferRuleModel, Long>{
	
	public void deleteAllExistingRecords() throws FrameworkCheckedException;
	
	public AgentTransferRuleModel findAgentTransferRule(Long deviceTypeId, Double transactionAmount, Long senderAppUserId, Long recipientAppUserId) throws FrameworkCheckedException;
	public boolean checkIfAgentTransferRuleExists(Long agentTaggingId) throws FrameworkCheckedException;

}
