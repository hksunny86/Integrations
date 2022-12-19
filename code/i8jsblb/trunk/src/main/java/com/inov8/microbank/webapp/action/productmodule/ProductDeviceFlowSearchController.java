package com.inov8.microbank.webapp.action.productmodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.server.service.productdeviceflowmodule.ProductDeviceFlowManager;

public class ProductDeviceFlowSearchController extends BaseSearchController{
    private ProductDeviceFlowManager productDeviceFlowManager;
	
    public ProductDeviceFlowSearchController() {
		super.setFilterSearchCommandClass(ProductDeviceFlowListViewModel.class);
	}
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest request, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		ProductDeviceFlowListViewModel productDeviceFlowListViewModel = (ProductDeviceFlowListViewModel) model;
		searchBaseWrapper.setBasePersistableModel(productDeviceFlowListViewModel);
		if(sortingOrderMap.isEmpty())
		    sortingOrderMap.put("productName", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.productDeviceFlowManager.searchProductDeviceFlowListView(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "productDeviceFlowListViewModelList",
		                            searchBaseWrapper.getCustomList().getResultsetList());
	}
	public void setProductDeviceFlowManager(
			ProductDeviceFlowManager productDeviceFlowManager) {
		this.productDeviceFlowManager = productDeviceFlowManager;
	}

}
