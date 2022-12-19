package com.inov8.microbank.webapp.action.portal.complaintmodule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.ComplaintReportModel;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;

public class ComplaintBillPaySearchController extends BaseSearchController{
    
	private ComplaintManager complaintManager;
    private ReferenceDataManager referenceDataManager;
    
	public ComplaintBillPaySearchController() {
		super.setFilterSearchCommandClass(ComplaintReportModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest req, LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception {
		List<ComplaintReportModel> list  = new ArrayList<ComplaintReportModel>(0);
		String consumerNo = req.getParameter("consumerno");
		
		if(consumerNo != null && consumerNo.length() > 0){
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	
			ComplaintReportModel complaintReportModel = (ComplaintReportModel) model;
			complaintReportModel.setConsumerNo(consumerNo);
			searchBaseWrapper.setBasePersistableModel(complaintReportModel);
	
			if(sortingOrderMap.isEmpty()){
		       sortingOrderMap.put("createdOn", SortingOrder.DESC);
		    }
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
			list = this.complaintManager.searchComplaintByConsumerNo(searchBaseWrapper);
		}
		
		if(list !=null && !list.isEmpty() ){
			pagingHelperModel.setTotalRecordsCount(list.size());
		}else{
			pagingHelperModel.setTotalRecordsCount(0);
		}
		
		return new ModelAndView(super.getSearchView(), "complaintModelList", list);

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