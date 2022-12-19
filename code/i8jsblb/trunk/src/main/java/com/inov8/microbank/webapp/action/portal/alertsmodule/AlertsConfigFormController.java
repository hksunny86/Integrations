package com.inov8.microbank.webapp.action.portal.alertsmodule;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.atomikos.logging.Logger;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AlertsConfigModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.alertsmodule.AlertsManager;

public class AlertsConfigFormController extends AdvanceFormController{
    
	private AlertsManager alertsManager;
    private ReferenceDataManager referenceDataManager;
    
	public AlertsConfigFormController() {
		setCommandName("alertsConfigModel");
		setCommandClass(AlertsConfigModel.class);
	}
	
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
	    return null;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String alertsConfigId = ServletRequestUtils.getStringParameter(req, "alertsConfigId");
		AlertsConfigModel model = new AlertsConfigModel();
		if ( !GenericValidator.isBlankOrNull(alertsConfigId) ) {
			SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			model.setAlertsConfigId(new Long(alertsConfigId));
			wrapper.setBasePersistableModel(model);
			wrapper = this.alertsManager.loadAlertsConfig(wrapper);
			model = (AlertsConfigModel) wrapper.getBasePersistableModel();
			return model;
		}
		return model;
	}
	
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		ModelAndView modelAndView = new ModelAndView( new RedirectView("home.html"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		Date nowDate = new Date();
		AlertsConfigModel model = null;
		AlertsConfigModel tempModel = null;
		try{
			Long alertsConfigId = new Long(ServletRequestUtils.getStringParameter(req, "alertsConfigId"));			
			model = new AlertsConfigModel();
			tempModel = new AlertsConfigModel();
			model = (AlertsConfigModel) obj;
			model.setAlertsConfigId(alertsConfigId);
			wrapper.setBasePersistableModel(model);
			wrapper = this.alertsManager.loadAlertsConfig(wrapper);
			tempModel = (AlertsConfigModel) wrapper.getBasePersistableModel();
			model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			model.setUpdatedOn(nowDate);
			model.setCreatedOn(tempModel.getCreatedOn());
			model.setCreatedByAppUserModel(tempModel.getCreatedByAppUserModel());
			model.setVersionNo(tempModel.getVersionNo());
			baseWrapper.setBasePersistableModel(model);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_ALERT_CONFIG_USECASE_ID));
		    baseWrapper = this.alertsManager.saveAlertsConfig(baseWrapper);
		   
		    String msg = super.getText("alertconfig.update.success", req.getLocale());
			this.saveMessage(req, msg);
		}catch(FrameworkCheckedException exception){
			exception.printStackTrace();
			super.saveMessage(req, super.getText("alertconfig.update.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		} 
		catch (Exception fce)
		{		
			this.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
		
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_alertconfigsearch.html?actionId=2"));
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