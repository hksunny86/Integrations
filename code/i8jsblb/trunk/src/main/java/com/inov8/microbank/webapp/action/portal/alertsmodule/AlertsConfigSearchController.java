package com.inov8.microbank.webapp.action.portal.alertsmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AlertsConfigModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.service.alertsmodule.AlertsManager;

public class AlertsConfigSearchController extends BaseFormSearchController{
    
    
	private AlertsManager alertsManager;
    private ReferenceDataManager referenceDataManager;
    
	public AlertsConfigSearchController() {
		setCommandName("alertsConfigModel");
		setCommandClass(AlertsConfigModel.class);
	}

	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
		Map<Object,String> statusList = new LinkedHashMap<Object,String>();
		statusList.put(true,"Active");
		statusList.put(false,"Inactive");
		referenceDataMap.put("statusList", statusList);
	    return referenceDataMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		if(sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("alertsConfigId", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		AlertsConfigModel alertsConfigModel = (AlertsConfigModel) model;
		searchBaseWrapper.setBasePersistableModel(alertsConfigModel);
		searchBaseWrapper = this.alertsManager.searchAlertsConfigList(searchBaseWrapper);
		CustomList<AlertsConfigModel> list = searchBaseWrapper.getCustomList();
		return new ModelAndView(this.getSuccessView(), "alertsConfigModelList", list.getResultsetList());
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