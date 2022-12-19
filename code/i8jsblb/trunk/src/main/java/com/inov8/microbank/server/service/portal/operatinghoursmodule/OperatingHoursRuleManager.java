package com.inov8.microbank.server.service.portal.operatinghoursmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.OperatingHoursRuleModel;



import java.util.List;

public interface OperatingHoursRuleManager {
    BaseWrapper saveOrUpdate(BaseWrapper baseWrapper)throws FrameworkCheckedException;
    OperatingHoursRuleModel loadOperatingHoursRuleModel(Long operatingHoursRuleId) throws FrameworkCheckedException;
    //VelocityRuleModel loadVelocityRuleModel(Long limitRuleId)throws FrameworkCheckedException;
    SearchBaseWrapper searchOperatingHoursRule(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException;
    List<OperatingHoursRuleModel> findByCriteria(OperatingHoursRuleModel operatingHoursRuleModeltemp) throws FrameworkCheckedException;
    //List<VelocityRuleModel> findByCriteria(VelocityRuleModel velocityRuleModelTemp) throws FrameworkCheckedException;
}
