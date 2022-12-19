package com.inov8.microbank.webapp.action.portal.adminusermodule;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;

public class AdminUserManagementSearchController extends BaseSearchController{
	private UserManagementManager userManagementManager;
	
	public AdminUserManagementSearchController() {
	    super.setFilterSearchCommandClass(UserManagementListViewModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, 
			                        Object model, 
			                        HttpServletRequest req, 
			                        LinkedHashMap<String, SortingOrder> sortingOrder) 
	throws Exception {
		UserManagementListViewModel userManagementListViewModel = (UserManagementListViewModel)model; 
		
		userManagementListViewModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        if(sortingOrder.isEmpty()){
        	sortingOrder.put("username", SortingOrder.ASC);
        }

		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		
		searchBaseWrapper.setSortingOrderMap(sortingOrder);
		searchBaseWrapper.setBasePersistableModel(userManagementListViewModel);
		searchBaseWrapper = this.userManagementManager.searchUsers(searchBaseWrapper);
		List list = searchBaseWrapper.getCustomList().getResultsetList();
		ModelAndView modelAndView = new ModelAndView(getSearchView(), "userManagementListViewModelList",
				                        searchBaseWrapper.getCustomList().getResultsetList());
		return modelAndView;
	}

	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}
}
