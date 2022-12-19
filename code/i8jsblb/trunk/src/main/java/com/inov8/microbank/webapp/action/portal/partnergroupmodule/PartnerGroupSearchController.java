package com.inov8.microbank.webapp.action.portal.partnergroupmodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.AppUserPartnerViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerGroupListViewModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;

public class PartnerGroupSearchController extends BaseSearchController  
{
	private PartnerGroupManager partnerGroupManager;
	private UserManagementManager userManagementManager;
	
	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

	public PartnerGroupSearchController() {

		super.setFilterSearchCommandClass(PartnerGroupListViewModel.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		PartnerGroupListViewModel partnerGroupListViewModel = (PartnerGroupListViewModel) object;
		
		if(!(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.INOV8) || UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.BANK)))
		{
			 Object partnerId = httpServletRequest.getSession(true).getAttribute(PortalConstants.KEY_PARTNER_ID);
			 if(partnerId != null)
			 {
				 partnerGroupListViewModel.setPartnerId((Long)partnerId); 
			 }else
			 {
				 BaseWrapper baseWrapperAUP = new BaseWrapperImpl();
				 AppUserPartnerViewModel  appUserPartnerViewModel = new AppUserPartnerViewModel();
				 appUserPartnerViewModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
				 baseWrapperAUP.setBasePersistableModel(appUserPartnerViewModel);
				 baseWrapperAUP = this.partnerGroupManager.loadAppUserPartner(baseWrapperAUP);
				 appUserPartnerViewModel = (AppUserPartnerViewModel)baseWrapperAUP.getBasePersistableModel();
				 partnerGroupListViewModel.setPartnerId(appUserPartnerViewModel.getPartnerId());
				 httpServletRequest.getSession(true).setAttribute(PortalConstants.KEY_PARTNER_ID, appUserPartnerViewModel.getPartnerId());
			 }
		}
		
		searchBaseWrapper.setBasePersistableModel(partnerGroupListViewModel);
		if(sortingOrderMap.isEmpty())
			sortingOrderMap.put("name", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.partnerGroupManager
				.searchPartnerGroup(searchBaseWrapper);

		String restrictedPartnerGroup	=	MessageUtil.getMessage("PARTNER_GROUP.SUPER_ADMIN");
		
		if(restrictedPartnerGroup==null){
			throw new FrameworkCheckedException("Please set PARTNER_GROUP.SUPER_ADMIN in ApplicationResource.properties");
		}
		
		AppUserModel	user	=	UserUtils.getCurrentUser();
		
		httpServletRequest.setAttribute("allowAccessOfCurrentUser", this.userManagementManager.isAppUserInPartnerGroup(user.getAppUserId(), Long.parseLong(restrictedPartnerGroup)));
		httpServletRequest.setAttribute("restrictedUserPartnerGroup", Long.parseLong(restrictedPartnerGroup));
		
		return new ModelAndView(getSearchView(), "partnerGroupModelList",
				searchBaseWrapper.getCustomList().getResultsetList());

	}

	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}	
}
