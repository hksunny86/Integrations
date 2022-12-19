package com.inov8.microbank.webapp.action.portal.forgotpinmodule;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.forgotpinmodule.ForgotpinListViewModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.server.service.portal.forgotpinmodule.ForgotpinManager;

public class ForgotPinUserSearchController extends BaseFormSearchController

{

	private ForgotpinManager forgotPinManager;

	public ForgotPinUserSearchController() {
		super.setCommandName("forgotpinListViewModel");
		super.setCommandClass(ForgotpinListViewModel.class);

	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,
			HttpServletResponse response, Object object,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		ForgotpinListViewModel forgotpinListViewModel = (ForgotpinListViewModel) object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		forgotpinListViewModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		
        if(sortingOrderMap.isEmpty()){
    		sortingOrderMap.put("firstName", SortingOrder.ASC);
    		sortingOrderMap.put("lastName", SortingOrder.ASC);
        }
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		searchBaseWrapper.setBasePersistableModel(forgotpinListViewModel);
		searchBaseWrapper = this.forgotPinManager
				.searchForgotPinUser(searchBaseWrapper);

		return new ModelAndView(getSuccessView(), "mfsRecord",
				searchBaseWrapper.getCustomList().getResultsetList());

	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {

		return null;
	}

	public void setForgotPinManager(ForgotpinManager forgotPinManager) {
		this.forgotPinManager = forgotPinManager;
	}

}
