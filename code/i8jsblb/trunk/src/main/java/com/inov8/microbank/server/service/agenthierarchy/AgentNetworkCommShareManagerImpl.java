package com.inov8.microbank.server.service.agenthierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AgentNeworkCommShareModel;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommShareViewModel;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareDetailModel;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.DistributorCommShareViewDAO;
import com.inov8.microbank.server.dao.commissionmodule.AgentNetworkCommShareDAO;
import com.inov8.microbank.server.dao.commissionmodule.DistributorCommissionShareDAO;
import com.inov8.microbank.server.dao.commissionmodule.DistributorCommissionShareDetailDAO;

public class AgentNetworkCommShareManagerImpl implements AgentNetworkCommShareManager{

	private AgentNetworkCommShareDAO agentNetworkCommShareDAO;
	private DistributorCommissionShareDAO distributorCommissionShareDAO;
	private DistributorCommissionShareDetailDAO distributorCommissionShareDetailDAO;

	
	private DistributorCommShareViewDAO distributorCommShareViewDAO;

	@Override
	public Map<Long, Double> findAgentHierarchySharesByProductIdAndDistributorLevelId(Long productId, Long distributorLevelId) {
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		
		AgentNeworkCommShareModel model = new AgentNeworkCommShareModel();
		model.setProductId(productId);
		model.setSharingDistributorLevelId(distributorLevelId);
		Map<Long, Double> hierarchyShareMap = new HashMap<Long, Double>(); 
		CustomList<AgentNeworkCommShareModel> resultList = agentNetworkCommShareDAO.findByExample(model, null, null, exampleHolder);
		
		if(resultList.getResultsetList() != null && resultList.getResultsetList().size() > 0)
		{
			for (Iterator<AgentNeworkCommShareModel> iterator = resultList.getResultsetList().iterator(); iterator.hasNext();) {
				AgentNeworkCommShareModel commShareModel = (AgentNeworkCommShareModel) iterator.next();
				hierarchyShareMap.put(commShareModel.getDistributorLevelId(), commShareModel.getCommissionShare());
			}
			
//			return resultList.getResultsetList();
		}
		
		return null;
	}
	
	@Override
	public BaseWrapper getDistributorCommissionShare(
			BaseWrapper baseWrapper) {
		
		DistributorCommissionShareModel	model	=	(DistributorCommissionShareModel) baseWrapper.getBasePersistableModel();
		model=distributorCommissionShareDAO.findByPrimaryKey(model.getDistributorCommissionShareId());
		baseWrapper.setBasePersistableModel(model);
		return baseWrapper;
	}
	
	@Override
	public BaseWrapper saveDistributorCommissionShare(BaseWrapper baseWrapper) {
		try {
			DistributorCommissionShareModel model = (DistributorCommissionShareModel) baseWrapper
					.getBasePersistableModel();
			distributorCommissionShareDAO.createDistributorCommissionShare(model);

			Collection<DistributorCommissionShareDetailModel> list = (Collection<DistributorCommissionShareDetailModel>) baseWrapper
					.getObject("DistributorCommissionShareDetailModelList");

			for (DistributorCommissionShareDetailModel dtlModel : list) {
				dtlModel.setDistributorCommissionShareIdModel(model);
			}
			
			distributorCommissionShareDetailDAO.deleteDistributorCommShareDtlModel(model.getDistributorCommissionShareId());
			list	=	distributorCommissionShareDetailDAO.saveOrUpdateCollection(list);

			baseWrapper.setBasePersistableModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseWrapper;
	}
	
	@Override
	public List<DistributorCommShareViewModel> findHierarchyCommissoinSharesByAppUserIdAndProductId(Long appUserId, Long productId){
		List<DistributorCommShareViewModel> commissionsList = new ArrayList<DistributorCommShareViewModel>();
		DistributorCommShareViewModel model = new DistributorCommShareViewModel();
		model.setAppUserId(appUserId);
		model.setProductId(productId);
		commissionsList = distributorCommShareViewDAO.loadDistributorCommissionSharesList(model);
		if (commissionsList.isEmpty()) {//Load default hierarchy shares with product id NULL and given appUserID
			List<DistributorCommShareViewModel> resultList = distributorCommShareViewDAO.loadDistributorCommissionSharesList(model);
			if (CollectionUtils.isNotEmpty(resultList)) {
				commissionsList = resultList;
			}
		}
		
		return commissionsList;
	}

	public SearchBaseWrapper loadDistributorCommissionShare(SearchBaseWrapper searchBaseWrapper) 
	{
		DistributorCommissionShareModel model = (DistributorCommissionShareModel) searchBaseWrapper.getBasePersistableModel();
		
		CustomList<DistributorCommissionShareModel> list =	null;
		
		list =	distributorCommissionShareDAO.searchDistributorCommissionShare(model.getDistributorId(), model.getRegionId(), model.getProductId(), model.getCurrentDistributorLevelId());
		
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	@Override
	public SearchBaseWrapper loadDistributorCommissionShareDetail(
			SearchBaseWrapper searchBaseWrapper) {
		ExampleConfigHolderModel configHolderModel=new ExampleConfigHolderModel();
		configHolderModel.setMatchMode(MatchMode.EXACT);
		configHolderModel.setEnableLike(Boolean.FALSE);
		configHolderModel.setIgnoreCase(Boolean.FALSE);
		
		DistributorCommissionShareDetailModel model = (DistributorCommissionShareDetailModel) searchBaseWrapper.getBasePersistableModel();
		CustomList<DistributorCommissionShareDetailModel> list =	distributorCommissionShareDetailDAO.findByExample(model,null,null,configHolderModel);
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	public void setDistributorCommShareViewDAO(
			DistributorCommShareViewDAO distributorCommShareViewDAO) {
		this.distributorCommShareViewDAO = distributorCommShareViewDAO;
	}

	public void setDistributorCommissionShareDetailDAO(
			DistributorCommissionShareDetailDAO distributorCommissionShareDetailDAO) {
		this.distributorCommissionShareDetailDAO = distributorCommissionShareDetailDAO;
	}
	
	
	public void setAgentNetworkCommShareDAO(
			AgentNetworkCommShareDAO agentNetworkCommShareDAO) {
		this.agentNetworkCommShareDAO = agentNetworkCommShareDAO;
	}
	
	public void setDistributorCommissionShareDAO(
			DistributorCommissionShareDAO distributorCommissionShareDAO) {
		this.distributorCommissionShareDAO = distributorCommissionShareDAO;
	}
}