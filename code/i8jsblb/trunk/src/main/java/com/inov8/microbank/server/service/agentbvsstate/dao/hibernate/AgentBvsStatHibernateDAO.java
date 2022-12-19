package com.inov8.microbank.server.service.agentbvsstate.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.AgentBvsStatModel;
import com.inov8.microbank.server.service.agentbvsstate.dao.AgentBvsStatDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class AgentBvsStatHibernateDAO extends BaseHibernateDAO<AgentBvsStatModel, Long, AgentBvsStatDAO> implements AgentBvsStatDAO {


    @Override
    public AgentBvsStatModel loadAgentBvsStatByAgentId(Long agentId) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(AgentBvsStatModel.class);
        criteria.add( Restrictions.eq("agentId", agentId));
        List<AgentBvsStatModel> list = getHibernateTemplate().findByCriteria(criteria);

        AgentBvsStatModel appUserModel = null;

        if(list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }

        return appUserModel;
    }
}
