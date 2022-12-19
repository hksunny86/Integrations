package com.inov8.microbank.server.dao.portal.agentmerchantdetailmodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AgentMerchantDetailModel;

/**
 * @author rizwan.munir
 *
 */
public interface AgentMerchantDetailDAO  extends BaseDAO<AgentMerchantDetailModel, Long>{

	List<AgentMerchantDetailModel> getAgentMerchantDetailModel(
			AgentMerchantDetailModel agentMerchantDetailModel);

}
