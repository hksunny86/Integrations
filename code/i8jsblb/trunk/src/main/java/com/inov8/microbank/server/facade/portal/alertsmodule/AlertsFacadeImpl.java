package com.inov8.microbank.server.facade.portal.alertsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.alertsmodule.AlertsManager;

public class AlertsFacadeImpl implements AlertsFacade {

	private AlertsManager alertsManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	@Override
	public SearchBaseWrapper searchAlertsConfigList(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			return alertsManager.searchAlertsConfigList(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public SearchBaseWrapper loadAlertsConfig(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			return alertsManager.loadAlertsConfig(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public BaseWrapper saveAlertsConfig(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return alertsManager.saveAlertsConfig(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}
	
	@Override
	public SearchBaseWrapper searchAlertsRecepientsList(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			return alertsManager.searchAlertsRecepientsList(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public SearchBaseWrapper loadAlertsRecepient(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			return alertsManager.loadAlertsRecepient(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public BaseWrapper saveAlertRecipient(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try{
			return alertsManager.saveAlertRecipient(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}
	
	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setAlertsManager(AlertsManager alertsManager) {
		this.alertsManager = alertsManager;
	}
	
}
