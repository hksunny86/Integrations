package com.inov8.microbank.server.service.agentgroup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AgentTaggingChildrenModel;
import com.inov8.microbank.common.model.AgentTaggingModel;
import com.inov8.microbank.common.model.AgentTransferRuleModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentGroupVOModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;

public interface AgentTaggingManager {

	public AgentTaggingViewModel loadAgentTaggingViewModel(Long id);
	public AgentTaggingModel saveAgentTaggingModel(AgentTaggingModel agentTaggingModel);
	public void saveAgentTaggingChildrenList(List<AgentTaggingChildrenModel> list);
	public void saveAgent(AgentTaggingModel agentTaggingModel, HttpServletRequest httpServletRequest) throws FrameworkCheckedException;
	public void saveAgent(AgentTaggingModel agentTaggingModel, String childString) throws FrameworkCheckedException;
	public List<AgentTaggingViewModel> searchAgentTaggingViewModel(AgentTaggingViewModel agentTaggingViewModel);
	public List<AgentTaggingModel> getAgentTaggingModel(AgentTaggingModel agentTaggingModel);
	public String getAgentTaggingChildrenList(AgentTaggingChildrenModel agentTaggingChildrenModel);
	public List<AgentTaggingChildrenModel> getAgentTaggingChildrenList(HttpServletRequest httpServletRequest, AgentTaggingModel agentTaggingModel);
	public AgentTaggingModel updateAgentTaggingModel(AgentTaggingModel agentTaggingModel, HttpServletRequest httpServletRequest)  throws FrameworkCheckedException;
	public AgentTaggingModel updateAgentTaggingModel(AgentTaggingModel agentTaggingModel, String childString) throws FrameworkCheckedException;
	public AgentTaggingModel getAgentTaggingModel(Long agentTaggingId);
	public List<AgentTaggingChildrenModel> loadAgentTaggingChildrenList(AgentTaggingChildrenModel agentTaggingChildrenModel);
	public AgentTaggingModel createAgentTaggingModel(AgentGroupVOModel agentGroupVOModel);
	public SearchBaseWrapper sAgentTaggingViewModel(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public boolean isTitleUnique(Long agentTaggingId, String groupTitle);
	
	//Agent Transfer Rules
	public BaseWrapper saveAgentTransferRules(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public List<AgentTransferRuleModel> getAllAgentTransferRules() throws FrameworkCheckedException;
	public void deleteAllAgentTransferRules(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public boolean checkIfAgentTransferRuleExists(Long agentTaggingId) throws FrameworkCheckedException;
	
	public List<AgentTaggingViewModel> getAllGroupTitle();
	public SearchBaseWrapper getTaggedAgentDetail(SearchBaseWrapper searchBaseWrapper);
	public SearchBaseWrapper getAccountHolderModel(SearchBaseWrapper searchBaseWrapper);
	
	public SearchBaseWrapper getAgentTaggingChildren(SearchBaseWrapper wrapper);
	List<TransactionDetailMasterModel> getTaggedAgentTransactionDetailList(
			SearchBaseWrapper wrapper) throws FrameworkCheckedException;

}