package com.inov8.microbank.webapp.action.portal.usecasemodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;

public class UsecaseSearchController extends BaseSearchController {
		
	private UsecaseFacade usecaseFacade;
	private UserManagementManager userManagementManager;

	public UsecaseSearchController() {
		super.setFilterSearchCommandClass(UsecaseModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception{
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		UsecaseModel usecaseModel = (UsecaseModel) model;

		searchBaseWrapper.setBasePersistableModel(usecaseModel);

		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put("name", SortingOrder.ASC);
		}
		else if(sortingOrderMap.containsKey("authorizationStatus")){
			SortingOrder	sortingOrder	=	sortingOrderMap.remove("authorizationStatus");
			sortingOrderMap.put("isAuthorizationEnable", sortingOrder);
		}
		
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CustomList<UsecaseModel> list = this.usecaseFacade.searchAuthorizationEnableUsecase(searchBaseWrapper).getCustomList();

		String superAdminPartnerGroup = MessageUtil.getMessage("PARTNER_GROUP.SUPER_ADMIN");
		if(StringUtil.isNullOrEmpty(superAdminPartnerGroup)){
			throw new FrameworkCheckedException("Please set PARTNER_GROUP.SUPER_ADMIN in ApplicationResource.properties");
		}
		boolean isSuperAdmin = this.userManagementManager.isAppUserInPartnerGroup(UserUtils.getCurrentUser().getAppUserId(), Long.parseLong(superAdminPartnerGroup));
		httpServletRequest.setAttribute("isSuperAdmin",isSuperAdmin);
		logger.info("**** isSuperAdmin:"+isSuperAdmin+" --> LoggedIn AppUserId:"+UserUtils.getCurrentUser().getAppUserId());
		
		return new ModelAndView( getSearchView(), "usecaseModelList", list.getResultsetList());
	}
	
	public void setUsecaseFacade(UsecaseFacade usecaseFacade) {
		this.usecaseFacade = usecaseFacade;
	}
	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}
}
