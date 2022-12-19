package com.inov8.microbank.server.dao.agentgroup;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.model.portal.reportmodule.WHTAgentReportViewModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;

public interface TaggedAgentsViewDAO extends BaseDAO<TaggedAgentsListViewModel, Long>{
	
	public List<TaggedAgentsListViewModel> findAppUsersByType(Long appUserType);

//	public CustomList<AgentTaggingViewModel> findByExample(
//			AgentTaggingViewModel agentTaggingViewModel);
	 List<TaggedAgentsListViewModel> loadTaggedagentReport(TaggedAgentsListViewModel exampleInstance, PagingHelperModel pagingHelperModel, DateRangeHolderModel dateRangeHolderModel) throws DataAccessException;

}
