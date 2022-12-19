package com.inov8.microbank.webapp.action.allpaymodule;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

public class AllpayRetailerAccountSearchController extends BaseSearchController {
	private AllpayRetailerAccountManager allpayRetailerAccountManager;
	protected AllpayRetailerAccountSearchController() {
		super.setFilterSearchCommandClass(RetailerContactListViewModel.class);
	}



	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		RetailerContactListViewModel retailerContactModel = (RetailerContactListViewModel) model;

		if(UserUtils.getCurrentUser().getAppUserTypeId().equals(7L)){
			retailerContactModel.setSoId(UserUtils.getCurrentUser().getMnoUserIdMnoUserModel().getMnoId());
		}

		searchBaseWrapper.setBasePersistableModel(retailerContactModel);
		if (sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("username", SortingOrder.ASC);
		}
		else if( sortingOrderMap.containsKey( "headString" ) )
		{
			SortingOrder sortingOrder = sortingOrderMap.remove( "headString" );
			sortingOrderMap.put( "head", sortingOrder );
		}

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.allpayRetailerAccountManager.searchAccount(searchBaseWrapper);

		return new ModelAndView(super.getSearchView(), "retailerContactModelList",
				searchBaseWrapper.getCustomList().getResultsetList());
	}




	public AllpayRetailerAccountManager getAllpayRetailerAccountManager() {
		return allpayRetailerAccountManager;
	}



	public void setAllpayRetailerAccountManager(
			AllpayRetailerAccountManager allpayRetailerAccountManager) {
		this.allpayRetailerAccountManager = allpayRetailerAccountManager;
	}

}
