package com.inov8.microbank.webapp.action.allpaymodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.distributormodule.DistributorContactListViewModel;
import com.inov8.microbank.server.service.AllpayModule.AllpayDistributorAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class AllpayDistributorAccountSearchController extends
		BaseSearchController {
 private AllpayDistributorAccountManager allpayDistributorAccountManager;
 private ReferenceDataManager referenceDataManager;
 private AppUserManager appUserManager;
	protected AllpayDistributorAccountSearchController() {
		super.setFilterSearchCommandClass(DistributorContactListViewModel.class);
	}
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object,
			HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		  searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		  DistributorContactListViewModel distributorContactListViewModel = (
		        DistributorContactListViewModel)object;
		  searchBaseWrapper.setBasePersistableModel(distributorContactListViewModel);
		  if(sortingOrderMap.isEmpty())
		      sortingOrderMap.put("username", SortingOrder.ASC);
		  searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		  searchBaseWrapper = this.allpayDistributorAccountManager.searchAccount(searchBaseWrapper);

		  return new ModelAndView(getSearchView(), "distributorContactList",
		                            searchBaseWrapper.getCustomList().getResultsetList());
	  }
	public AllpayDistributorAccountManager getAllpayDistributorAccountManager() {
		return allpayDistributorAccountManager;
	}
	public void setAllpayDistributorAccountManager(
			AllpayDistributorAccountManager allpayDistributorAccountManager) {
		this.allpayDistributorAccountManager = allpayDistributorAccountManager;
	}
	public AppUserManager getAppUserManager() {
		return appUserManager;
	}
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
