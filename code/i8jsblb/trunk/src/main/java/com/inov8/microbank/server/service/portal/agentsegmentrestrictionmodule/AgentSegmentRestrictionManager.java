package com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionDAO;

public interface AgentSegmentRestrictionManager {

    public BaseWrapper createAgentSegmentRestriction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public Boolean checkAgentSegmentRestriction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    AgentSegmentRestrictionDAO agentSegmentRestrictionDAO();



}
