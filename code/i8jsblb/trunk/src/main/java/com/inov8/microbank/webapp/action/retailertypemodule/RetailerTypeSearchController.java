package com.inov8.microbank.webapp.action.retailertypemodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.retailermodule.RetailerTypeListViewModel;
import com.inov8.microbank.server.service.retailertypemodule.RetailerTypeManager;


public class RetailerTypeSearchController extends BaseSearchController {

	private RetailerTypeManager retailerTypeManager;
	
	public RetailerTypeSearchController()
	  {
	    super.setFilterSearchCommandClass(RetailerTypeListViewModel.class);

	  }
	
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest request, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		RetailerTypeListViewModel retailerTypeListViewModel=(RetailerTypeListViewModel)object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setBasePersistableModel(retailerTypeListViewModel);
		if(sortingOrderMap.isEmpty())
		    sortingOrderMap.put("name", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper=this.retailerTypeManager.searchRetailerType(searchBaseWrapper);
		
		
		return new ModelAndView(getSearchView(), "retailerTypeList", searchBaseWrapper.getCustomList().getResultsetList());
	}


	public void setRetailerTypeManager(RetailerTypeManager retailerTypeManager) {
		this.retailerTypeManager = retailerTypeManager;
	}


}
