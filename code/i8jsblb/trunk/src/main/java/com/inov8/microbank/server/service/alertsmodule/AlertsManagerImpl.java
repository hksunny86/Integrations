package com.inov8.microbank.server.service.alertsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AlertsConfigModel;
import com.inov8.microbank.common.model.AlertsRecipientsModel;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.server.dao.alertsmodule.AlertsConfigDAO;
import com.inov8.microbank.server.dao.alertsmodule.AlertsRecipientsDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

public class AlertsManagerImpl implements AlertsManager {

	private AlertsConfigDAO alertsConfigDAO;
	private AlertsRecipientsDAO alertsRecipientsDAO;
	private ActionLogManager actionLogManager;

	public void setAlertsConfigDAO(AlertsConfigDAO alertsConfigDAO) {
		this.alertsConfigDAO = alertsConfigDAO;
	}
	
	public void setAlertsRecipientsDAO(AlertsRecipientsDAO alertsRecipientsDAO) {
		this.alertsRecipientsDAO = alertsRecipientsDAO;
	}
	
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
	
	@Override
	public SearchBaseWrapper searchAlertsConfigList(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		CustomList<AlertsConfigModel> customList = null;
		AlertsConfigModel model = (AlertsConfigModel) wrapper.getBasePersistableModel();
	    customList= alertsConfigDAO.findByExample(model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap());
	    wrapper.setCustomList( customList );
		return wrapper;
	}
	
	@Override
	public SearchBaseWrapper loadAlertsConfig(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		AlertsConfigModel model = (AlertsConfigModel) alertsConfigDAO.findByPrimaryKey(wrapper.getBasePersistableModel().getPrimaryKey());
		wrapper.setBasePersistableModel(model);
		return wrapper;
	}
	
	@Override
	public BaseWrapper saveAlertsConfig(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		AlertsConfigModel model = (AlertsConfigModel)baseWrapper.getBasePersistableModel();	
		model = this.alertsConfigDAO.saveOrUpdate(model);		
		baseWrapper.setBasePersistableModel(model);
		actionLogModel.setCustomField1(model.getAlertsConfigId().toString());
		actionLogModel.setCustomField11(model.getAlertName());
	    this.actionLogManager.completeActionLog(actionLogModel);
		return baseWrapper;	
	}
	
	@Override
	public SearchBaseWrapper searchAlertsRecepientsList(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		AlertsRecipientsModel model = (AlertsRecipientsModel) wrapper.getBasePersistableModel();
	    CustomList<AlertsRecipientsModel> customList = null;
	    customList= alertsRecipientsDAO.findByExample(model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap());
	    wrapper.setCustomList( customList );
		return wrapper;
	}
	
	@Override
	public SearchBaseWrapper loadAlertsRecepient(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		AlertsRecipientsModel model = (AlertsRecipientsModel) alertsRecipientsDAO.findByPrimaryKey(wrapper.getBasePersistableModel().getPrimaryKey());
		wrapper.setBasePersistableModel(model);
		return wrapper;
	}
	
	@Override
	public BaseWrapper saveAlertRecipient(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		AlertsRecipientsModel alertsRecipientsModel = (AlertsRecipientsModel)baseWrapper.getBasePersistableModel();	
		alertsRecipientsModel = this.alertsRecipientsDAO.saveOrUpdate(alertsRecipientsModel) ;		
		baseWrapper.setBasePersistableModel(alertsRecipientsModel);
		actionLogModel.setCustomField1(alertsRecipientsModel.getAlertsRecipientsId().toString());
		actionLogModel.setCustomField11(alertsRecipientsModel.getName());
	    this.actionLogManager.completeActionLog(actionLogModel);
		return baseWrapper;	
	}

}
