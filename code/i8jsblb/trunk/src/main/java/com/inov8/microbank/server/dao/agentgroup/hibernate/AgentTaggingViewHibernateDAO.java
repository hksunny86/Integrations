package com.inov8.microbank.server.dao.agentgroup.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.server.dao.agentgroup.AgentTaggingViewDAO;

public class AgentTaggingViewHibernateDAO extends BaseHibernateDAO<AgentTaggingViewModel, Long, AgentTaggingViewDAO> implements AgentTaggingViewDAO {

	
	public List <AgentTaggingViewModel> getGroupTitle ( ){
		String hql = "select distinct  groupTitle from AgentTaggingViewModel";
		//String hql = "from AppUserModel au where au.passwordChangeRequired <> 1";
		List <AgentTaggingViewModel> users = getHibernateTemplate().find(hql);
		return users;
	}
}
