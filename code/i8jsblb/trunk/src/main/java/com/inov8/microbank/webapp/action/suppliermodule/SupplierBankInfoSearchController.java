package com.inov8.microbank.webapp.action.suppliermodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.suppliermodule.SupplierBankInfoListViewModel;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;

public class SupplierBankInfoSearchController extends BaseSearchController {
private SupplierBankInfoManager supplierBankInfoManager ;

public SupplierBankInfoSearchController()
{
  super.setFilterSearchCommandClass(SupplierBankInfoListViewModel.class);
}

public void setSupplierBankInfoManager(
		SupplierBankInfoManager supplierBankInfoManager) {
	this.supplierBankInfoManager = supplierBankInfoManager;
}
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	    SupplierBankInfoListViewModel 
	    supplierBankInfoListViewModel = (SupplierBankInfoListViewModel) model;
	    searchBaseWrapper.setBasePersistableModel(supplierBankInfoListViewModel);
	    if(sortingOrderMap.isEmpty())
	        sortingOrderMap.put("name", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.supplierBankInfoManager.searchSupplierBankInfo(
	        searchBaseWrapper);

	    return new ModelAndView(super.getSearchView(), "supplierBankInfoModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());

	}

}
