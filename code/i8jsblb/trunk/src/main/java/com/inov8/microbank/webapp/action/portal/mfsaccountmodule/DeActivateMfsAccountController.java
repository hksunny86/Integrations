package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

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
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;

public class DeActivateMfsAccountController extends BaseFormSearchController{

	private MfsAccountManager mfsAccountManager;
	
	public DeActivateMfsAccountController() {
		setCommandName("userInfoListViewModel");
	    setCommandClass(UserInfoListViewModel.class);
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {
		return null;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest req, 
			                        HttpServletResponse res, 
			                        Object model, 
			                        PagingHelperModel pagingHelperModel, 
			                        LinkedHashMap<String, SortingOrder> sortingOrderMap) 
	                          throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		UserInfoListViewModel userInfoListViewModel = (UserInfoListViewModel) model;
		searchBaseWrapper.setBasePersistableModel(userInfoListViewModel);
		
        if(sortingOrderMap.isEmpty()){
    		sortingOrderMap.put("firstName", SortingOrder.ASC);
    		sortingOrderMap.put("lastName", SortingOrder.ASC);
        }

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.mfsAccountManager.searchUserInfo(searchBaseWrapper);
		return new ModelAndView(getSuccessView(), "userInfoListViewModelList",
				searchBaseWrapper.getCustomList().getResultsetList());
	}


}
