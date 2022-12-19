package com.inov8.microbank.server.dao.operatinghoursmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.OperatingHoursRuleModel;

import java.util.List;

public interface OperatingHoursRuleModelDAO extends BaseDAO<OperatingHoursRuleModel,Long> {
    List<OperatingHoursRuleModel> findByCriteria(OperatingHoursRuleModel operatingHoursRuleModel);
    List<OperatingHoursRuleModel> loadOperatingHoursRules(OperatingHoursRuleModel operatingHoursRuleModel);
    String update(OperatingHoursRuleModel operatingHoursRuleModel);
}
