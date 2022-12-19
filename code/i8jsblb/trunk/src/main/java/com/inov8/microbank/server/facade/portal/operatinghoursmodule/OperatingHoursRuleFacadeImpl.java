package com.inov8.microbank.server.facade.portal.operatinghoursmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.OperatingHoursRuleModel;
import com.inov8.microbank.server.service.portal.operatinghoursmodule.OperatingHoursRuleManager;


import java.util.List;

public class OperatingHoursRuleFacadeImpl implements OperatingHoursRuleFacade {
    private OperatingHoursRuleManager operatingHoursRuleManager;
    private FrameworkExceptionTranslator frameworkExceptionTranslator;
    @Override
    public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try
        {
            return this.operatingHoursRuleManager.saveOrUpdate(baseWrapper);
        }
        catch (Exception e)
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION );
        }
    }

    @Override
    public OperatingHoursRuleModel loadOperatingHoursRuleModel(Long operatingHoursRuleId) throws FrameworkCheckedException {
        try
        {
            return this.operatingHoursRuleManager.loadOperatingHoursRuleModel(operatingHoursRuleId);
        }
        catch (Exception e)
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    @Override
    public SearchBaseWrapper searchOperatingHoursRule(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        try
        {
            return this.operatingHoursRuleManager.searchOperatingHoursRule(searchBaseWrapper);
        }
        catch (Exception e)
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }



    @Override
    public List<OperatingHoursRuleModel> findByCriteria(OperatingHoursRuleModel operatingHoursRuleModeltemp) throws FrameworkCheckedException {
        try
        {
            return this.operatingHoursRuleManager.findByCriteria(operatingHoursRuleModeltemp);
        }
        catch (Exception e)
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    public void setOperatingHoursRuleManager(OperatingHoursRuleManager operatingHoursRuleManager) {
        this.operatingHoursRuleManager = operatingHoursRuleManager;
    }

    public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator) {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }
}
