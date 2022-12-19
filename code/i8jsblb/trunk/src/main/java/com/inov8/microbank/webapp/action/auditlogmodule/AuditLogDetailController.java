package com.inov8.microbank.webapp.action.auditlogmodule;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.server.service.auditlogmodule.AuditLogListViewManager;

public class AuditLogDetailController extends AbstractController{
    
	AuditLogListViewManager auditLogListViewManager;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// TODO Auto-generated method stub
		Map auditLogDetailMap = new HashMap();
		Long auditLogId = ServletRequestUtils.getLongParameter(req, "auditLogId");
		AuditLogModel auditLogModel = new AuditLogModel();
		auditLogModel.setAuditLogId(auditLogId);
		auditLogModel.setPrimaryKey(auditLogId);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(auditLogModel);
		baseWrapper = this.auditLogListViewManager.loadAuditLog(baseWrapper);
		auditLogModel = (AuditLogModel)baseWrapper.getBasePersistableModel();
		auditLogDetailMap.put("auditLogModel", auditLogModel);
		return new ModelAndView("auditlogdetail", auditLogDetailMap);
	}

	public void setAuditLogListViewManager(
			AuditLogListViewManager auditLogListViewManager) {
		this.auditLogListViewManager = auditLogListViewManager;
	}
    
}
