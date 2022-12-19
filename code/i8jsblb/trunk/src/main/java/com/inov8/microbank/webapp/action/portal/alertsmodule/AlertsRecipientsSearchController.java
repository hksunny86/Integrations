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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AlertsConfigModel;
import com.inov8.microbank.common.model.AlertsRecipientsModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.service.alertsmodule.AlertsManager;

public class AlertsRecipientsSearchController extends BaseFormSearchController{
    
	private AlertsManager alertsManager;
    private ReferenceDataManager referenceDataManager;
    
	public AlertsRecipientsSearchController() {
		setCommandName("alertsRecipientsModel");
		setCommandClass(AlertsRecipientsModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest arg0) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(3);
		AlertsConfigModel alertsConfigModel = new AlertsConfigModel();
		alertsConfigModel.setIsActive(true);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(alertsConfigModel, "alertName", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<AlertsConfigModel> alertsConfigModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null){
	    	alertsConfigModelList = referenceDataWrapper.getReferenceDataList();
	    } 
	    referenceDataMap.put("alertsConfigModelList", alertsConfigModelList);
	    
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
		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("alertsRecipientsId", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		AlertsRecipientsModel alertsRecipientsModel = (AlertsRecipientsModel) model;
		searchBaseWrapper.setBasePersistableModel(alertsRecipientsModel);
		searchBaseWrapper = this.alertsManager.searchAlertsRecepientsList(searchBaseWrapper);
		CustomList<AlertsRecipientsModel> list = searchBaseWrapper.getCustomList();
		return new ModelAndView(this.getSuccessView(), "alertsRecipientsModelList",  list.getResultsetList());

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