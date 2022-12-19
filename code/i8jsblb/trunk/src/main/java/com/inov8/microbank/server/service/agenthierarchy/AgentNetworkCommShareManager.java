package com.inov8.microbank.server.service.agenthierarchy;

import java.util.List;
import java.util.Map;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommShareViewModel;
public interface AgentNetworkCommShareManager 
{
	public Map<Long, Double> findAgentHierarchySharesByProductIdAndDistributorLevelId(Long productId, Long distributorLevelId);
	
	/*added by atif hussain*/
	BaseWrapper saveDistributorCommissionShare(BaseWrapper baseWrapper);
	BaseWrapper getDistributorCommissionShare(BaseWrapper baseWrapper);
	SearchBaseWrapper loadDistributorCommissionShare(SearchBaseWrapper searchBaseWrapper);
	SearchBaseWrapper loadDistributorCommissionShareDetail(SearchBaseWrapper searchBaseWrapper);
	List<DistributorCommShareViewModel> findHierarchyCommissoinSharesByAppUserIdAndProductId(Long appUserId, Long productId);
}
