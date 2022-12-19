package com.inov8.microbank.tax.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.tax.model.FEDRuleModel;

import java.util.List;

public interface FEDRuleDAO extends BaseDAO<FEDRuleModel, Long> {

	void deleteFedRules() throws FrameworkCheckedException;

	List<FEDRuleModel> loadAllActiveRulesByServiceId(FEDRuleModel model) throws FrameworkCheckedException;

}
