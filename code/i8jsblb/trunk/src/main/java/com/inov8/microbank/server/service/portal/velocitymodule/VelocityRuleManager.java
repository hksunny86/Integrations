package com.inov8.microbank.server.service.portal.velocitymodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.microbank.common.model.VelocityRuleModel;

public interface VelocityRuleManager {

	BaseWrapper saveOrUpdate(BaseWrapper baseWrapper)throws FrameworkCheckedException;
	VelocityRuleModel loadVelocityRuleModel(Long limitRuleId)throws FrameworkCheckedException;
	SearchBaseWrapper searchVelocityRule(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException;
	List<VelocityRuleModel> findByCriteria(VelocityRuleModel velocityRuleModelTemp) throws FrameworkCheckedException;
}
