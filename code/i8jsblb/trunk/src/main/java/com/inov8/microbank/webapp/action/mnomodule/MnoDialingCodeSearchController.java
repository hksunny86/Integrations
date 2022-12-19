package com.inov8.microbank.webapp.action.mnomodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.mnomodule.MnoDialingCodeListViewModel;
import com.inov8.microbank.server.service.mnomodule.MnoDialingCodeManager;

public class MnoDialingCodeSearchController extends BaseSearchController {

	 private MnoDialingCodeManager mnoDialingCodeManager;
	
	 public MnoDialingCodeSearchController()
	  {
	    super.setFilterSearchCommandClass(MnoDialingCodeListViewModel.class);
	  }

	 public void setMnoDialingCodeManager(
			MnoDialingCodeManager mnoDialingCodeManager) {
		this.mnoDialingCodeManager = mnoDialingCodeManager;
	}
		protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	    MnoDialingCodeListViewModel mnoDialingCodeListViewModel = (MnoDialingCodeListViewModel) model;
	    if(sortingOrderMap.isEmpty())
	        sortingOrderMap.put("name", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper.setBasePersistableModel(mnoDialingCodeListViewModel);
	    searchBaseWrapper = this.mnoDialingCodeManager.searchMnoDialingCode(searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "mnoDialingCodeModelList",
	                            searchBaseWrapper.getCustomList().
	                            getResultsetList());
	}

}
