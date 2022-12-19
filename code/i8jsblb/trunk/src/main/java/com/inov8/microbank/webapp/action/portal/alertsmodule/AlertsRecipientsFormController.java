package com.inov8.microbank.webapp.action.portal.alertsmodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AlertsConfigModel;
import com.inov8.microbank.common.model.AlertsRecipientsModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.alertsmodule.AlertsManager;

public class AlertsRecipientsFormController extends AdvanceFormController{
    
	private AlertsManager alertsManager;
    private ReferenceDataManager referenceDataManager;
    
	public AlertsRecipientsFormController() {
		setCommandName("alertsRecipientsModel");
		setCommandClass(AlertsRecipientsModel.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
		AlertsConfigModel alertsConfigModel = new AlertsConfigModel();
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(alertsConfigModel, "alertName", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<AlertsConfigModel> alertsConfigModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null){
	    	alertsConfigModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("alertsConfigModelList", alertsConfigModelList);
	    return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String alertsRecipientsId = ServletRequestUtils.getStringParameter(req, "alertsRecipientsId");
		AlertsRecipientsModel model = new AlertsRecipientsModel();
		if (!GenericValidator.isBlankOrNull(alertsRecipientsId) ) {
			SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			model.setAlertsRecipientsId(new Long(alertsRecipientsId));
			wrapper.setBasePersistableModel(model);
			wrapper = this.alertsManager.loadAlertsRecepient(wrapper);
			model = (AlertsRecipientsModel) wrapper.getBasePersistableModel();
		}
		return model;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		Date nowDate = new Date();
		AlertsRecipientsModel alertsRecipientsModel = (AlertsRecipientsModel) model;
		alertsRecipientsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		alertsRecipientsModel.setCreatedOn(nowDate);
		alertsRecipientsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		alertsRecipientsModel.setUpdatedOn(nowDate);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(alertsRecipientsModel);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.CREATE_ALERT_RECIPIENT_USECASE_ID));
		try{
			baseWrapper = this.alertsManager.saveAlertRecipient(baseWrapper);
			alertsRecipientsModel = (AlertsRecipientsModel) baseWrapper.getBasePersistableModel();
			
			String msg = super.getText("alertrecipient.add.success", req.getLocale());
			this.saveMessage(req, msg);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			super.saveMessage(req, super.getText("alertrecipient.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		}
		catch (Exception fce)
		{	
			fce.printStackTrace();
			this.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_alertsrecepientssearch.html?actionId=2"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		Date nowDate = new Date();
		AlertsRecipientsModel model = null;
		AlertsRecipientsModel tempModel = null;
		try{
			Long alertsRecipientsId = new Long(ServletRequestUtils.getStringParameter(req, "alertsRecipientsId"));			
			model = new AlertsRecipientsModel();
			tempModel = new AlertsRecipientsModel();
			model = (AlertsRecipientsModel) obj;
			model.setAlertsRecipientsId(alertsRecipientsId);
			wrapper.setBasePersistableModel(model);
			wrapper = this.alertsManager.loadAlertsRecepient(wrapper);
			tempModel = (AlertsRecipientsModel) wrapper.getBasePersistableModel();
			
			model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			model.setUpdatedOn(nowDate);
			model.setCreatedOn(tempModel.getCreatedOn());
			model.setCreatedByAppUserModel(tempModel.getCreatedByAppUserModel());
			model.setVersionNo(tempModel.getVersionNo());
			baseWrapper.setBasePersistableModel(model);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_ALERT_RECIPIENT_USECASE_ID));
		    baseWrapper = this.alertsManager.saveAlertRecipient(baseWrapper);
		   
		    String msg = super.getText("alertrecipient.update.success", req.getLocale());
			this.saveMessage(req, msg);
		}catch(FrameworkCheckedException exception){
			exception.printStackTrace();
			super.saveMessage(req, super.getText("alertrecipient.update.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		} 
		
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_alertsrecepientssearch.html?actionId=2"));
		return modelAndView;
	}
	
	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setAlertsManager(AlertsManager alertsManager) {
		this.alertsManager = alertsManager;
	}

}