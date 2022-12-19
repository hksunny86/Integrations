package com.inov8.ola.server.dao.limit;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.LimitRuleModel;


public interface LimitRuleDAO extends BaseDAO<LimitRuleModel, Long> {

	public boolean isLimitApplicable(LimitRuleModel limitRuleModel);

	public int findByCriteria(LimitRuleModel limitRuleModelTemp);
}
