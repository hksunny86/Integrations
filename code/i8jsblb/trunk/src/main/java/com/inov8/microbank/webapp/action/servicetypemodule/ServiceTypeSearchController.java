package com.inov8.microbank.webapp.action.servicetypemodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.servicemodule.ServiceTypeListViewModel;
import com.inov8.microbank.server.service.servicetypemodule.ServiceTypeManager;


public class ServiceTypeSearchController extends BaseSearchController {

	private ServiceTypeManager serviceTypeManager;
	
	public ServiceTypeSearchController()
	  {
	    super.setFilterSearchCommandClass(ServiceTypeListViewModel.class);
	  }
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest request, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		ServiceTypeListViewModel serviceTypeListViewModel=(ServiceTypeListViewModel)object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setBasePersistableModel(serviceTypeListViewModel);
		if(sortingOrderMap.isEmpty())
		sortingOrderMap.put("name", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper=this.serviceTypeManager.searchServiceType(searchBaseWrapper);
		
		return new ModelAndView(getSearchView(), "serviceTypeList", searchBaseWrapper.getCustomList().getResultsetList());
	}

	public void setServiceTypeManager(ServiceTypeManager serviceTypeManager) {
		this.serviceTypeManager = serviceTypeManager;
	}



}
