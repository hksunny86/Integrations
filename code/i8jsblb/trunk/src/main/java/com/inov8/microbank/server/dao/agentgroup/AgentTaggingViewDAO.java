package com.inov8.microbank.server.dao.agentgroup;

import java.util.List;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.agentgroup.AgentGroupVOModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;

public interface AgentTaggingViewDAO extends BaseDAO<AgentTaggingViewModel, Long> {

	public List <AgentTaggingViewModel> getGroupTitle( );

	
	
}
