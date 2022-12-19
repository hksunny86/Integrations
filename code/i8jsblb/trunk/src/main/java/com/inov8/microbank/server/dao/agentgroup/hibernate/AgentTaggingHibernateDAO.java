package com.inov8.microbank.server.dao.agentgroup.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AgentTaggingModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentGroupVOModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.server.dao.agentgroup.AgentTaggingDAO;

public class AgentTaggingHibernateDAO extends BaseHibernateDAO<AgentTaggingModel, Long, AgentTaggingDAO> implements AgentTaggingDAO {

	
	public List <AgentTaggingViewModel> getGroupTitle ( ){
		String hql = "select distinct  groupTitle from AgentTaggingViewModel";
		//String hql = "from AppUserModel au where au.passwordChangeRequired <> 1";
		List <AgentTaggingViewModel> users = getHibernateTemplate().find(hql);
		return users;
	}
	
	
}
