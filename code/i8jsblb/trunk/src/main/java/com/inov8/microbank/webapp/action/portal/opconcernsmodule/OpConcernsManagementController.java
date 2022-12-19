package com.inov8.microbank.webapp.action.portal.opconcernsmodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ConcernCategoryModel;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.ConcernPriorityModel;
import com.inov8.microbank.common.model.ConcernStatusModel;
import com.inov8.microbank.common.model.portal.concernmodule.ConcernsParentListViewModel;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class OpConcernsManagementController extends BaseFormSearchController {
	private ConcernManager concernManager;
	private ReferenceDataManager referenceDataManager;

	public OpConcernsManagementController() {
		super.setCommandName("concernsParentListViewModel");
		super.setCommandClass(ConcernsParentListViewModel.class);
	}
    
	
	
	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		/** 
	     * code fragment to load reference data for ConcernPartner Type
	     */
	    ConcernCategoryModel concernCategoryModel = new ConcernCategoryModel();
	    concernCategoryModel.setActive(true);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernCategoryModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ConcernCategoryModel> concernCategoryModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	concernCategoryModelList = referenceDataWrapper.getReferenceDataList();
	    } 
	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("concernCategoryModelList", concernCategoryModelList);

	    /**
	     * code fragment to load reference data for Concern Priority
	     */
	    ConcernPriorityModel concernPriorityModel = new ConcernPriorityModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernPriorityModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ConcernPriorityModel> concernPriorityModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	concernPriorityModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("concernPriorityModelList", concernPriorityModelList);
	    
	    /**
	     * code fragment to load reference data for Concern Status
	     */
	    ConcernStatusModel concernStatusModel = new ConcernStatusModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernStatusModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ConcernStatusModel> concernStatusModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	concernStatusModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("concernStatusModelList", concernStatusModelList);
	    
	    /**
	     * code fragment to load reference data for Concern Partner
	     */
	    ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		concernPartnerModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ConcernPartnerModel> concernPartnerModelList = null;
	    
	    if(referenceDataWrapper.getReferenceDataList() != null){
	    	concernPartnerModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    
	    
	    referenceDataMap.put("concernPartnerModelList", concernPartnerModelList);
	    return referenceDataMap;
	}



	@Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrder) throws Exception {
		ConcernsParentListViewModel concernsParentListViewModel = (ConcernsParentListViewModel)model;


		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        if(sortingOrder.isEmpty()){
        	sortingOrder.put("updatedOn", SortingOrder.DESC);
        }

		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		searchBaseWrapper.setSortingOrderMap(sortingOrder);
		searchBaseWrapper.setBasePersistableModel(concernsParentListViewModel);
		searchBaseWrapper = this.concernManager.searchConcernsParentList(searchBaseWrapper);
		List list = searchBaseWrapper.getCustomList().getResultsetList();
		ModelAndView modelAndView = new ModelAndView(super.getSuccessView(), "concernModelList",
				                        searchBaseWrapper.getCustomList().getResultsetList());
		return modelAndView;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}


}
