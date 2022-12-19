package com.inov8.microbank.schedulers.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.server.facade.portal.level3account.Level3AccountFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;

public class SchedulerManagerImpl implements SchedulerManager {

    private MfsAccountFacade mfsAccountFacade;
    private Level3AccountFacade level3AccountFacade;
    @Override
    public BaseWrapper createAgentAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        mfsAccountFacade.createAgentMerchant(baseWrapper);
        ApplicantDetailModel applicantDetailModel = (ApplicantDetailModel) baseWrapper.getObject("applicantDetailModel");
        level3AccountFacade.createLevel3Account(baseWrapper);
        return baseWrapper;
    }

    public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade) {
        this.mfsAccountFacade = mfsAccountFacade;
    }

    public void setLevel3AccountFacade(Level3AccountFacade level3AccountFacade) {
        this.level3AccountFacade = level3AccountFacade;
    }
}
