package com.inov8.microbank.app.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.app.service.AppManager;
import com.inov8.microbank.app.vo.AppInfoVO;
import com.inov8.microbank.app.vo.AppVO;

import java.util.List;


public class AppFacadeImpl implements AppFacade {

    private AppManager appManager;
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    @Override
    public AppVO validateAppVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            this.appManager.validateAppVersion(baseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_ACTION);
        }
        return null;
    }

    @Override
    public List<AppInfoVO> loadAppInfoList(Long appUserTypeId) throws FrameworkCheckedException {
        try{
            return appManager.loadAppInfoList(appUserTypeId);
        }

        catch (Exception ex) {
            throw frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public String getDefaultAppURL(Long appUserTypeId) throws FrameworkCheckedException {
        try{
            return appManager.getDefaultAppURL(appUserTypeId);
        }

        catch (Exception ex) {
            throw frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    public BaseWrapper createApplicationVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            this.appManager.createApplicationVersion(baseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.INSERT_ACTION);
        }
        return baseWrapper;
    }

    public SearchBaseWrapper loadApplicationVersion(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        try {
            this.appManager.loadApplicationVersion(searchBaseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
        }
        return searchBaseWrapper;
    }

    public BaseWrapper loadApplicationVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            this.appManager.loadApplicationVersion(baseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
        }
        return baseWrapper;
    }

    public SearchBaseWrapper searchApplicationVersion(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        try {
            this.appManager.searchApplicationVersion(searchBaseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex,
                    this.frameworkExceptionTranslator.FIND_ACTION);
        }
        return searchBaseWrapper;
    }

    public BaseWrapper updateApplicationVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            this.appManager.updateApplicationVersion(baseWrapper);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex,
                    this.frameworkExceptionTranslator.INSERT_ACTION);
        }
        return baseWrapper;
    }

    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }

    public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator) {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

	@Override
	public void sendSMSToUsers(String userName, String pin,
			boolean isConsumerApp) {		
		this.appManager.sendSMSToUsers(userName, pin, isConsumerApp);
	}

}
