package com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;

public interface AgentSegmentRestrictionDAO extends BaseDAO<AgentSegmentRestriction,Long> {

    public Boolean checkAgentSegmentRestriction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

}
