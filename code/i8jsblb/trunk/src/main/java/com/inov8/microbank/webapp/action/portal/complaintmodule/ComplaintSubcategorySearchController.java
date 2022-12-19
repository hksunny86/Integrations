package com.inov8.microbank.webapp.action.portal.complaintmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.BasePersistableModel;
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
import com.inov8.microbank.common.model.ComplaintSubcategoryViewModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;

public class ComplaintSubcategorySearchController extends BaseFormSearchController{
    
	private ComplaintManager complaintManager;
    private ReferenceDataManager referenceDataManager;
    
	public ComplaintSubcategorySearchController() {
		super.setCommandName("complaintSubcategoryViewModel");
		super.setCommandClass(ComplaintSubcategoryViewModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest arg0) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(3);

	    ComplaintCategoryModel categoryModel = new ComplaintCategoryModel();
	    categoryModel.setIsActive(true);
	    categoryModel.setIsAuto(false);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(categoryModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ComplaintCategoryModel> categoryModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null){
	    	categoryModelList = referenceDataWrapper.getReferenceDataList();
	    } 
	    referenceDataMap.put("categoryModelList", categoryModelList);
	    referenceDataMap.put("assigneel0List", this.complaintManager.loadL0AssigneeList());
	    referenceDataMap.put("assigneel1List", this.complaintManager.loadL1AssigneeList());
	    referenceDataMap.put("assigneel2List", this.complaintManager.loadL2AssigneeList());
	    referenceDataMap.put("assigneel3List", this.complaintManager.loadL3AssigneeList());
		
	    Map<Object,String> statusList = new LinkedHashMap<Object,String>();
		statusList.put(true,"Active");
		statusList.put(false,"Inactive");
		referenceDataMap.put("statusList", statusList);
	    
	    return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("complaintSubcategoryId", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		ComplaintSubcategoryViewModel complaintSubcategoryViewModel = (ComplaintSubcategoryViewModel) model;
		searchBaseWrapper.setBasePersistableModel(complaintSubcategoryViewModel);
		List<ComplaintSubcategoryViewModel> list = this.complaintManager.searchComplaintSubcategoryList(searchBaseWrapper);

		if( list != null && !list.isEmpty() )
		{
		    pagingHelperModel.setTotalRecordsCount( list.size() );
		}
		else
		{
		    pagingHelperModel.setTotalRecordsCount( 0 );
		}
		
		return new ModelAndView(this.getSuccessView(), "complaintSubcategoryModelList", list);
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