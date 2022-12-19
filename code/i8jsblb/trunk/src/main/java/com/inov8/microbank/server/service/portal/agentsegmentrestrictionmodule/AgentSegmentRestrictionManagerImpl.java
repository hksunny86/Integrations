package com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionDAO;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AgentSegmentRestrictionManagerImpl implements AgentSegmentRestrictionManager {
    private AgentSegmentRestrictionDAO agentSegmentRestrictionDAO;
    private AgentSegmentRestriction agentSegmentRestriction;
    @Override
    public BaseWrapper createAgentSegmentRestriction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
         agentSegmentRestriction=(AgentSegmentRestriction) baseWrapper.getBasePersistableModel();
         agentSegmentRestriction =agentSegmentRestrictionDAO.saveOrUpdate(agentSegmentRestriction);
         baseWrapper.setBasePersistableModel(agentSegmentRestriction);
        return baseWrapper;
    }

    @Override
    public Boolean checkAgentSegmentRestriction(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        Boolean result=null;

        result=agentSegmentRestrictionDAO.checkAgentSegmentRestriction(baseWrapper);

        return result;
    }

    @Override
    public AgentSegmentRestrictionDAO agentSegmentRestrictionDAO() {
        return agentSegmentRestrictionDAO;
    }

    public void setAgentSegmentRestrictionDAO(AgentSegmentRestrictionDAO agentSegmentRestrictionDAO) {
        this.agentSegmentRestrictionDAO = agentSegmentRestrictionDAO;
    }
}
