package com.inov8.microbank.server.service.agentgroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inov8.microbank.common.model.*;
import com.inov8.microbank.server.dao.agentgroup.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentGroupVOModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.agentgroup.AgentTransferRuleVo;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.server.dao.accountholder.AccountHolderDAO;

public class AgentTaggingManagerImpl implements AgentTaggingManager {

	@Autowired
	private AgentTaggingDAO agentTaggingDAO;
	@Autowired
	private TaggedAgentsViewDAO taggedAgentsDAO;
	@Autowired
	private AgentTaggingChildrenDAO agentTaggingChildrenDAO;
	@Autowired
	private AgentTaggingViewDAO agentTaggingViewDAO;
	private AppUserManager appUserManager;
	@Autowired
	private ActionLogManager actionLogManager;
	@Autowired
	private AgentTransferRuleDAO agentTransferRuleDAO;
	@Autowired
	private HandlerManager handlerManager;
	@Autowired
	private  AccountHolderDAO  accountHolderDAO;
	@Autowired
	private TransactionDetailMasterDAO transactionDetailMasterDAO;

	private AgentGroupChildrenViewDAO agentGroupChildrenViewDAO;

	public void setTransactionDetailMasterDAO(
			TransactionDetailMasterDAO transactionDetailMasterDAO) {
		this.transactionDetailMasterDAO = transactionDetailMasterDAO;
	}

	private RetailerContactManager retailerContactManager;
	protected static Log logger	= LogFactory.getLog(AgentTaggingManagerImpl.class);
	


	public AgentTaggingViewModel loadAgentTaggingViewModel(Long id) {
		
		AgentTaggingViewModel agentTaggingViewModel = agentTaggingViewDAO.findByPrimaryKey(id);
		
		return agentTaggingViewModel;
	}


    @Override
	public SearchBaseWrapper sAgentTaggingViewModel(SearchBaseWrapper searchBaseWrapper) {
		
		CustomList<AgentTaggingViewModel> customList = agentTaggingViewDAO.findByExample((AgentTaggingViewModel)searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper.getPagingHelperModel(),null);
		
		searchBaseWrapper.setCustomList(customList);
	    return searchBaseWrapper;
	}


	public List<AgentTaggingModel> getAgentTaggingModel(AgentTaggingModel agentTaggingModel) {
		
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		exampleHolder.setEnableLike(Boolean.FALSE);
		
		CustomList<AgentTaggingModel> customList = agentTaggingDAO.findByExample(agentTaggingModel, null, null, exampleHolder);
		
		return customList.getResultsetList();
	}


	public AgentTaggingModel getAgentTaggingModel(Long agentTaggingId) {
		
		AgentTaggingModel agentTaggingModel = agentTaggingDAO.findByPrimaryKey(agentTaggingId);
		
		return agentTaggingModel;
	}

	
	public AgentTaggingModel updateAgentTaggingModel(AgentTaggingModel agentTaggingModel, HttpServletRequest httpServletRequest) throws FrameworkCheckedException{
		
		deleteAgentTaggingChildrenBy(agentTaggingModel.getAgentTaggingId());
		
		saveAgent(agentTaggingModel, httpServletRequest);
		
		return agentTaggingModel;
	}

	
	public AgentTaggingModel updateAgentTaggingModel(AgentTaggingModel agentTaggingModel, String childString) throws FrameworkCheckedException{
		
		deleteAgentTaggingChildrenBy(agentTaggingModel.getAgentTaggingId());
		
		saveAgent(agentTaggingModel, childString);
		
		return agentTaggingModel;
	}
	

	public AgentTaggingModel saveAgentTaggingModel(AgentTaggingModel agentTaggingModel) {
		
		agentTaggingDAO.saveOrUpdate(agentTaggingModel);
		
		return agentTaggingModel;
	}
	
	
	public void saveAgentTaggingChildrenList(List<AgentTaggingChildrenModel> list) {
		
		if(list != null && list.size()>0) {
			agentTaggingChildrenDAO.saveOrUpdateCollection(list);
		}
	}
	
	public boolean isTitleUnique(Long agentTaggingId, String groupTitle) {
		boolean result = true;
		try{
			if(StringUtil.isNullOrEmpty(groupTitle)){
				return result;
			}
			
			AgentTaggingModel exampleModel = new AgentTaggingModel();
			exampleModel.setGroupTitle(groupTitle);
			List<AgentTaggingModel> list = getAgentTaggingModel(exampleModel);
			if(list != null && list.size() > 0){
				if(agentTaggingId == null){ // create scenario
					result = false;
				}else{ //update scenario
					for(AgentTaggingModel dbModel : list) {					
						if(dbModel.getAgentTaggingId().longValue() != agentTaggingId.longValue()) {
							result = false;
							break;
						}
					}
				}
			}
		}catch(Exception ex){
			logger.error("Exception while checking duplication of Group Title for agentTaggingId:"+agentTaggingId+" groupTitle:"+groupTitle , ex);
		}
		return result;
	}

	private void saveAgent(AgentTaggingModel agentTaggingModel, HttpServletRequest httpServletRequest, String childString) throws FrameworkCheckedException{

		saveAgentTaggingModel(agentTaggingModel);
		
		List<AgentTaggingChildrenModel> agentTaggingChildrenList = null;
		
		
		if(childString == null && httpServletRequest != null) {

			agentTaggingChildrenList = getAgentTaggingChildrenList(httpServletRequest, agentTaggingModel);
		
		} else if(childString != null && httpServletRequest == null)  {

			agentTaggingChildrenList = getAgentTaggingChildrenList(childString, agentTaggingModel);
		}
		
		
		if(agentTaggingChildrenList !=null && !agentTaggingChildrenList.isEmpty()) {

			saveAgentTaggingChildrenList(agentTaggingChildrenList);
		}
	}


	@Override
	public void saveAgent(AgentTaggingModel agentTaggingModel, HttpServletRequest httpServletRequest) throws FrameworkCheckedException{
		
		this.saveAgent(agentTaggingModel, httpServletRequest, null);
	}	


	@Override
	public void saveAgent(AgentTaggingModel agentTaggingModel, String childString) throws FrameworkCheckedException{
		
		this.saveAgent(agentTaggingModel, null, childString);
	}	


	@Override
	public AgentTaggingModel createAgentTaggingModel(AgentGroupVOModel agentGroupVOModel) {
		
		AgentTaggingModel agentTaggingModel = null;
		
		if(agentGroupVOModel.getGroupId() != null) {

			agentTaggingModel = getAgentTaggingModel(agentGroupVOModel.getGroupId());
			agentTaggingModel.setGroupTitle(agentGroupVOModel.getGroupTitle());
			agentTaggingModel.setParrentId(agentGroupVOModel.getParrentId());
			agentTaggingModel.setStatus(agentGroupVOModel.getStatus());
			agentTaggingModel.setAppUserId(agentGroupVOModel.getAppUserId());
			
		} else {
			
			agentTaggingModel = new AgentTaggingModel();
			agentTaggingModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			agentTaggingModel.setCreatedOn(new Date());
			agentTaggingModel.setGroupTitle(agentGroupVOModel.getGroupTitle());
			agentTaggingModel.setParrentId(agentGroupVOModel.getParrentId());
			agentTaggingModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			agentTaggingModel.setUpdatedOn(new Date());
			agentTaggingModel.setStatus(agentGroupVOModel.getStatus());
			agentTaggingModel.setAppUserId(agentGroupVOModel.getAppUserId());
		}
		
		return agentTaggingModel;
	}
	
	
	
	public List<AgentTaggingChildrenModel> getAgentTaggingChildrenList(HttpServletRequest httpServletRequest, AgentTaggingModel agentTaggingModel) {
		
		Integer totalRows = Integer.parseInt((String) httpServletRequest.getParameter("totalRows"));
			
		List<AgentTaggingChildrenModel> list = new ArrayList<AgentTaggingChildrenModel>(0);
		
		for(int rowNum = 1; rowNum<=totalRows; rowNum++) {

			String appUserID = "appUserID_"+rowNum;
			String USER_ID = "childId_"+rowNum;

			String appUserId = httpServletRequest.getParameter(appUserID);
			String userId = httpServletRequest.getParameter(USER_ID);

			if(!StringUtil.isNullOrEmpty(appUserId) && !StringUtil.isNullOrEmpty(userId)) {

				list.add(new AgentTaggingChildrenModel(Long.valueOf(appUserId), agentTaggingModel.getAgentTaggingId(), userId));
			}	
		}
		
		return list;
	}
	
	
	
	public List<AgentTaggingChildrenModel> getAgentTaggingChildrenList(String childString, AgentTaggingModel agentTaggingModel) {
				
		List<AgentTaggingChildrenModel> list = new ArrayList<AgentTaggingChildrenModel>(0);
		
		childString = childString.replace("|", ":");
		String[] rows = childString.split(":");

		for(String row : rows) {
			
			String[] cols = row.split(";");

			String appUserId = cols[4];
			String userId = cols[5];
				
			if(!StringUtil.isNullOrEmpty(appUserId) && !StringUtil.isNullOrEmpty(userId)) {

				list.add(new AgentTaggingChildrenModel(Long.valueOf(appUserId), agentTaggingModel.getAgentTaggingId(), userId));
			}
		}
		
		return list;
	}
	
	public void deleteAgentTaggingChildrenBy(Long agentTaggingId) {
		
		agentTaggingChildrenDAO.deleteAgentTaggingChildrenList(agentTaggingId);
	}
	
	
	
	public List<AgentTaggingChildrenModel> loadAgentTaggingChildrenList(AgentTaggingChildrenModel agentTaggingChildrenModel) {
		
		List<AgentTaggingChildrenModel> list = new ArrayList<AgentTaggingChildrenModel>(0);
		
		try {
			
			CustomList<AgentTaggingChildrenModel> customList = agentTaggingChildrenDAO.findByExample(agentTaggingChildrenModel);
			
			list.addAll(customList.getResultsetList());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public String getAgentTaggingChildrenList(AgentTaggingChildrenModel agentTaggingChildrenModel) {
		AgentGroupChildrenViewModel agentGroupChildrenViewModel = new AgentGroupChildrenViewModel();
		agentGroupChildrenViewModel.setAgentGroupTaggingId(agentTaggingChildrenModel.getAgentTaggingId());
		List<AgentGroupChildrenViewModel> list = agentGroupChildrenViewDAO.findByExample(agentGroupChildrenViewModel).getResultsetList();
		StringBuilder xml = new StringBuilder("");
		if(list != null  && !list.isEmpty())
		{
			logger.info("Agent Childred Tagging List Size :: " + list.size() + " at time :: " + new Date());
			for(AgentGroupChildrenViewModel model : list) {
				xml.append(model.getFirstName());
				xml.append(" ");
				xml.append(model.getLastName());
				xml.append(";");
				xml.append(model.getBusinessName());
				xml.append(";");
				xml.append(model.getMobileNo());
				xml.append(";");
				xml.append(model.getcNic());
				xml.append(";");
				xml.append(model.getAppUserId());
				xml.append(";");
				xml.append(model.getUserId());
				xml.append(";");
				xml.append("");//_agentTaggingChildrenModel.getAgentTaggingChildrenId()
				xml.append("|");
			}
		}
		logger.info("Agent Childred Tagging XML :: " + xml.toString() + " at time :: " + new Date());
		return xml.toString();
	}
	
	
	public BaseWrapper saveAgentTransferRules(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		agentTransferRuleDAO.deleteAllExistingRecords();
		
		AgentTransferRuleVo agentTransferRuleVo = (AgentTransferRuleVo) baseWrapper.getObject(AgentTransferRuleVo.class.getSimpleName());
		agentTransferRuleDAO.saveOrUpdateCollection(agentTransferRuleVo.getAgentTransferRuleModelList());
		
		this.actionLogManager.completeActionLogRequiresNewTransaction(actionLogModel);
		
		return baseWrapper;
	}
	
	public void deleteAllAgentTransferRules(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		agentTransferRuleDAO.deleteAllExistingRecords();
		
		this.actionLogManager.completeActionLogRequiresNewTransaction(actionLogModel);
		
	}
	
	public List<AgentTransferRuleModel> getAllAgentTransferRules() throws FrameworkCheckedException{
		
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("transferTypeId", SortingOrder.ASC);
        sortingOrderMap.put("senderGroupId", SortingOrder.ASC);
        sortingOrderMap.put("receiverGroupId", SortingOrder.ASC);
        sortingOrderMap.put("rangeStarts", SortingOrder.ASC);

		CustomList<AgentTransferRuleModel> customList = agentTransferRuleDAO.findAll(sortingOrderMap);
		if(customList != null && customList.getResultsetList() != null && customList.getResultsetList().size() > 0){
			return customList.getResultsetList();
		}else{
			return new ArrayList<AgentTransferRuleModel>(0);
		}
		
	}
	
public List<AgentTaggingViewModel> searchAgentTaggingViewModel(AgentTaggingViewModel agentTaggingViewModel) {
		
		CustomList<AgentTaggingViewModel> customList = agentTaggingViewDAO.findByExample(agentTaggingViewModel);
		
		return customList.getResultsetList();
	}

	
	public List<AgentTaggingViewModel> getAllGroupTitle()
	{
		
		List<AgentTaggingViewModel> groupTitleList= agentTaggingViewDAO.getGroupTitle();
		return groupTitleList;
	}
	
	public SearchBaseWrapper getTaggedAgentDetail(SearchBaseWrapper searchBaseWrapper)
	  {
	    CustomList<AgentTaggingChildrenModel> agentTaggingChildrenList = agentTaggingChildrenDAO.findByExample((AgentTaggingChildrenModel) searchBaseWrapper.getBasePersistableModel(), null);
	    		
	    searchBaseWrapper.setCustomList(agentTaggingChildrenList);
	    return searchBaseWrapper;
	  }
	
	public SearchBaseWrapper getAccountHolderModel(SearchBaseWrapper searchBaseWrapper)
	  {
	    CustomList<AccountHolderModel> accountHolderModelList = accountHolderDAO.findByExample((AccountHolderModel) searchBaseWrapper.getBasePersistableModel(), null);
	    		
	    searchBaseWrapper.setCustomList(accountHolderModelList);
	    return searchBaseWrapper;
	  }
	
	@Override
	public List<TransactionDetailMasterModel> getTaggedAgentTransactionDetailList(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		TransactionDetailMasterModel model = new TransactionDetailMasterModel();
		model = (TransactionDetailMasterModel) searchBaseWrapper.getBasePersistableModel();
		List<TransactionDetailMasterModel> list = transactionDetailMasterDAO.getTaggedAgentTransactionDetailList(searchBaseWrapper);
		return list;
	}
	
	public SearchBaseWrapper getAgentTaggingChildren(SearchBaseWrapper wrapper)
	{
		CustomList<AgentTaggingChildrenModel> modelList= agentTaggingChildrenDAO.findByExample((AgentTaggingChildrenModel) wrapper.getBasePersistableModel(),null);
		 if(modelList!=null)
		 {
		wrapper.setCustomList(modelList);
		 }
		 return wrapper;
	}
	


	public void setAccountHolderDAO(AccountHolderDAO accountHolderDAO) {
		this.accountHolderDAO = accountHolderDAO;
	}


	public boolean checkIfAgentTransferRuleExists(Long agentTaggingId) throws FrameworkCheckedException{
		return agentTransferRuleDAO.checkIfAgentTransferRuleExists(agentTaggingId);
	}

	public void setAgentTaggingDAO(AgentTaggingDAO agentTaggingDAO) {
		this.agentTaggingDAO = agentTaggingDAO;
	}
	
	public void setAgentTaggingChildrenDAO(AgentTaggingChildrenDAO agentTaggingChildrenDAO) {
		this.agentTaggingChildrenDAO = agentTaggingChildrenDAO;
	}

	public void setAgentTaggingViewDAO(AgentTaggingViewDAO agentTaggingViewDAO) {
		this.agentTaggingViewDAO = agentTaggingViewDAO;
	}
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
	
	public void setAgentTransferRuleDAO(AgentTransferRuleDAO agentTransferRuleDAO) {
		this.agentTransferRuleDAO = agentTransferRuleDAO;
	}
	
	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}

	


	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}

	public void setAgentGroupChildrenViewDAO(AgentGroupChildrenViewDAO agentGroupChildrenViewDAO) {
		this.agentGroupChildrenViewDAO = agentGroupChildrenViewDAO;
	}
}