package com.inov8.microbank.server.service.alertsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;


public interface AlertsManager {
	public SearchBaseWrapper searchAlertsConfigList(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper loadAlertsConfig(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
	public BaseWrapper saveAlertsConfig(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchAlertsRecepientsList(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper loadAlertsRecepient(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
	public BaseWrapper saveAlertRecipient(BaseWrapper baseWrapper) throws FrameworkCheckedException;
}
