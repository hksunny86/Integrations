package com.inov8.microbank.server.dao.velocitymodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.VelocityRuleModel;
import com.inov8.microbank.common.model.velocitymodule.VelocityRuleViewModel;

public interface VelocityRuleViewDAO extends BaseDAO<VelocityRuleViewModel, Long> {
	public List<VelocityRuleViewModel> loadVelocityRules(VelocityRuleViewModel velocityRuleViewModel);
}