package com.inov8.microbank.webapp.action.portal.usermanagementmodule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementListViewModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;

public class UserManagementSearchController extends BaseSearchController{
	
	private UserManagementManager userManagementManager;
	
	public UserManagementSearchController() {
	    super.setFilterSearchCommandClass(UserManagementListViewModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, 
			                        Object model, 
			                        HttpServletRequest req, 
			                        LinkedHashMap<String, SortingOrder> sortingOrder) 
	throws Exception {
		UserManagementListViewModel userManagementListViewModel = (UserManagementListViewModel)model; 
			                            
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        if(sortingOrder.isEmpty()){
        	sortingOrder.put("username", SortingOrder.ASC);
        }
        else if(sortingOrder.containsKey("accountStatus")){
			SortingOrder order = sortingOrder.remove("accountStatus");
			sortingOrder.put("accountEnabled", order);
		}

		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		
		searchBaseWrapper.setSortingOrderMap(sortingOrder);
		searchBaseWrapper.setBasePersistableModel(userManagementListViewModel);
		searchBaseWrapper = this.userManagementManager.searchUsers(searchBaseWrapper);
		List<UserManagementListViewModel> list = searchBaseWrapper.getCustomList().getResultsetList();
		
		List<UserManagementListViewModel> filteredList = new ArrayList<UserManagementListViewModel>();
		if (list != null) {
			// hardcoded for Askari pilot
			for(UserManagementListViewModel userModel: list){
				if(!userModel.getUsername().equalsIgnoreCase("ROOT_ADMIN")){
					filteredList.add(userModel);
				}
			}
		}
		
		String restrictedPartnerGroup	=	MessageUtil.getMessage("PARTNER_GROUP.SUPER_ADMIN");
		
		if(restrictedPartnerGroup==null){
			throw new FrameworkCheckedException("Please set PARTNER_GROUP.SUPER_ADMIN in ApplicationResource.properties");
		}
		
		AppUserModel user = UserUtils.getCurrentUser();
		req.setAttribute("currentAppUserId", user.getAppUserId());
		req.setAttribute("allowAccessOfCurrentUser", this.userManagementManager.isAppUserInPartnerGroup(user.getAppUserId(), Long.parseLong(restrictedPartnerGroup)));
		req.setAttribute("restrictedUserPartnerGroup", Long.parseLong(restrictedPartnerGroup));
		
		ModelAndView modelAndView = new ModelAndView(getSearchView(), "userManagementListViewModelList",
				filteredList);
		return modelAndView;
	}

	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}
    
}
