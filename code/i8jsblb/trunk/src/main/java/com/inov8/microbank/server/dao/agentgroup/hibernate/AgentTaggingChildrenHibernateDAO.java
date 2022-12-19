package com.inov8.microbank.server.dao.agentgroup.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AgentTaggingChildrenModel;
import com.inov8.microbank.server.dao.agentgroup.AgentTaggingChildrenDAO;

public class AgentTaggingChildrenHibernateDAO extends BaseHibernateDAO<AgentTaggingChildrenModel, Long, AgentTaggingChildrenDAO> implements AgentTaggingChildrenDAO {

	public void deleteAgentTaggingChildrenList(Long agentTaggingId) {
		
		Session session = super.getSession();
		StringBuilder queryStr = new StringBuilder();
		queryStr.append("delete from com.inov8.microbank.common.model.AgentTaggingChildrenModel model ");
		queryStr.append("where model.agentTaggingId =:agentTaggingId");

		Query query = session.createQuery(queryStr.toString());
		query.setParameter("agentTaggingId", agentTaggingId);
		query.executeUpdate();
	}
}
