package com.inov8.microbank.webapp.action.portal.complaintmodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ComplaintCategoryModel;
import com.inov8.microbank.common.model.ComplaintReportModel;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;

public class ComplaintSearchController extends BaseFormSearchController{
    
	private ComplaintManager complaintManager;
    private ReferenceDataManager referenceDataManager;
    
	public ComplaintSearchController() {
		super.setCommandName("complaintReportModel");
		super.setCommandClass(ComplaintReportModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
	    ComplaintCategoryModel categoryModel = new ComplaintCategoryModel();
	    categoryModel.setIsActive(true);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(categoryModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ComplaintCategoryModel> categoryModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null){
	    	categoryModelList = referenceDataWrapper.getReferenceDataList();
	    } 
	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("categoryModelList", categoryModelList);
	    
	    referenceDataMap.put("currentAssigneeList", this.complaintManager.loadAssigneeList());
	    referenceDataMap.put("assigneel0List", this.complaintManager.loadL0AssigneeList());
	    referenceDataMap.put("assigneel1List", this.complaintManager.loadL1AssigneeList());
	    referenceDataMap.put("assigneel2List", this.complaintManager.loadL2AssigneeList());
	    referenceDataMap.put("assigneel3List", this.complaintManager.loadL3AssigneeList());
	    
//	    /**
//	     * code fragment to load reference data for Concern Priority
//	     */
//	    ConcernPriorityModel concernPriorityModel = new ConcernPriorityModel();
//	    referenceDataWrapper = new ReferenceDataWrapperImpl(
//	    		concernPriorityModel, "name", SortingOrder.ASC);
//	    referenceDataManager.getReferenceData(referenceDataWrapper);
//	    List<ConcernPriorityModel> concernPriorityModelList = null;
//	    if (referenceDataWrapper.getReferenceDataList() != null)
//	    {
//	    	concernPriorityModelList = referenceDataWrapper.getReferenceDataList();
//	    }
//	    referenceDataMap.put("concernPriorityModelList", concernPriorityModelList);
//	    
//	    /**
//	     * code fragment to load reference data for Concern Status
//	     */
//	    ConcernStatusModel concernStatusModel = new ConcernStatusModel();
//	    referenceDataWrapper = new ReferenceDataWrapperImpl(
//	    		concernStatusModel, "name", SortingOrder.ASC);
//	    referenceDataManager.getReferenceData(referenceDataWrapper);
//	    List<ConcernStatusModel> concernStatusModelList = null;
//	    if (referenceDataWrapper.getReferenceDataList() != null)
//	    {
//	    	concernStatusModelList = referenceDataWrapper.getReferenceDataList();
//	    }
//	    referenceDataMap.put("concernStatusModelList", concernStatusModelList);
//	    
//	    /**
//	     * code fragment to load reference data for Concern Partner
//	     */
//	    ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
//	    referenceDataWrapper = new ReferenceDataWrapperImpl(
//	    		concernPartnerModel, "name", SortingOrder.ASC);
//	    referenceDataManager.getReferenceData(referenceDataWrapper);
//	    List<ConcernPartnerModel> concernPartnerModelList = null;
//	    
//	    if(referenceDataWrapper.getReferenceDataList() != null){
//	    	concernPartnerModelList = referenceDataWrapper.getReferenceDataList();
//	    }
//	    
//	    
//	    referenceDataMap.put("concernPartnerModelList", concernPartnerModelList);
	    return referenceDataMap;

	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ComplaintReportModel complaintReportModel = (ComplaintReportModel) model;
		
		searchBaseWrapper.setBasePersistableModel(complaintReportModel);
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", complaintReportModel.getLoggedFrom(),
				complaintReportModel.getLoggedTo());
		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		if(sortingOrderMap.isEmpty())
	    {
	       sortingOrderMap.put("createdOn", SortingOrder.DESC);
	    }
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		
		CustomList<ComplaintReportModel> list = this.complaintManager.searchComplaintReportList(searchBaseWrapper);
		
		return new ModelAndView(super.getSuccessView(), "complaintModelList", list.getResultsetList());

	}
	
	public ComplaintManager getComplaintManager() {
		return complaintManager;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
}