package com.inov8.microbank.tax.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.tax.TargetedWhtDetailReportModel;
import com.inov8.microbank.server.service.portal.taxreportmodule.TaxReportManager;


public class TargetedWhtDetailController extends BaseFormSearchController {
	
	private TaxReportManager taxReportManager;
	
	TargetedWhtDetailController(){
		this.setCommandClass(TargetedWhtDetailReportModel.class);
		this.setCommandName("targetedWhtDetailReportModel");
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, PagingHelperModel arg3,
			LinkedHashMap<String, SortingOrder> arg4) throws Exception {

		TargetedWhtDetailReportModel targetedWhtDetailReportModel = (TargetedWhtDetailReportModel) arg2;
		List<TargetedWhtDetailReportModel> targetedWhtDetailReportModelList = new ArrayList<>();
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(arg3);
		searchBaseWrapper.setBasePersistableModel(targetedWhtDetailReportModel);
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn" , targetedWhtDetailReportModel.getStartDate() , targetedWhtDetailReportModel.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		if(arg4.isEmpty()){
			arg4.put("createdOn", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(arg4);
		searchBaseWrapper = taxReportManager.findTargetedWhtDetail(searchBaseWrapper);
		
		if(searchBaseWrapper.getCustomList() !=null){
			if(searchBaseWrapper.getCustomList().getResultsetList().size() > 0){
				targetedWhtDetailReportModelList = searchBaseWrapper.getCustomList().getResultsetList();
			}
		}
		
		
		return new ModelAndView(getSuccessView(),"resultList",targetedWhtDetailReportModelList);
	}

	public void setTaxReportManager(TaxReportManager taxReportManager) {
		this.taxReportManager = taxReportManager;
	}

}
