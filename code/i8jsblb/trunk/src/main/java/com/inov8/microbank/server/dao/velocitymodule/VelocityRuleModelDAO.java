package com.inov8.microbank.server.dao.velocitymodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.VelocityRuleModel;

public interface VelocityRuleModelDAO extends BaseDAO<VelocityRuleModel, Long> {

	List<VelocityRuleModel> findByCriteria(VelocityRuleModel velocityRuleModel);
	String update(VelocityRuleModel velocityRuleModel);
}
