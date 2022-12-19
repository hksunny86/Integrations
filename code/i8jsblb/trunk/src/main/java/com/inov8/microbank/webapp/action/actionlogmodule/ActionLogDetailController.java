package com.inov8.microbank.webapp.action.actionlogmodule;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

public class ActionLogDetailController extends AbstractController{

	ActionLogManager actionLogManager;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		Map actionLogDetailMap = new HashMap();
		Long actionLogId = ServletRequestUtils.getLongParameter(arg0, "actionLogId");
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionLogId(actionLogId);
		actionLogModel.setPrimaryKey(actionLogId);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		baseWrapper = this.actionLogManager.loadUserActionLog(baseWrapper);
		actionLogModel = (ActionLogModel)baseWrapper.getBasePersistableModel();
		actionLogDetailMap.put("actionLogModel", actionLogModel);
		
		return new ModelAndView("actionlogdetail", actionLogDetailMap);
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
    
}
