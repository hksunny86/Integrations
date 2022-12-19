package com.inov8.microbank.server.service.agentlocationstat.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;


import com.inov8.microbank.common.model.AgentLocationStatModel;
import com.inov8.microbank.server.service.agentlocationstat.dao.AgentLocationStatDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class AgentLocationStatHibernateDAO extends BaseHibernateDAO<AgentLocationStatModel,Long, AgentLocationStatDAO> implements  AgentLocationStatDAO {
    @Override
    public AgentLocationStatModel loadAgentLocationStatByAgentId(Long agentId) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(AgentLocationStatModel.class);
        criteria.add( Restrictions.eq("agentId", agentId));
        List<AgentLocationStatModel> list = getHibernateTemplate().findByCriteria(criteria);

        AgentLocationStatModel agentLocationStatModel = null;

        if(list != null && !list.isEmpty()) {
            agentLocationStatModel = list.get(0);
        }

        return agentLocationStatModel;

    }
}
