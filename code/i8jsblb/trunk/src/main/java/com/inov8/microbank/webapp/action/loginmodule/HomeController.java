package com.inov8.microbank.webapp.action.loginmodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.home.HomeSearchModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;

/**
 * <p>Title: Microbank Demo</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 */

public class HomeController extends BaseFormSearchController
//    implements Controller 
{
	private MfsAccountManager mfsAccountManager;
	private AllpayRetailerAccountManager allpayRetailerAccountManager;
	private HandlerManager handlerManager;
  public HomeController()
  {
	  
	  setCommandName("homeSearchModel");
	  setCommandClass(HomeSearchModel.class);
  }
/*
  public ModelAndView handleRequest(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) throws
      Exception
  {
/*	AppUserModel user = UserUtils.getCurrentUser();
	AppRoleModel role = null;
	for (Iterator iter = user.getRoles().iterator(); iter.hasNext();)
	{
		role = (AppRoleModel)iter.next();
		if(role.getName().equalsIgnoreCase("admin"))
			return new ModelAndView("admin/home");
	}
	
		return new ModelAndView("home");
  }*/



@Override
protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception {
	
	AppUserModel user = UserUtils.getCurrentUser();
	Map<String,Object> referenceDataMap = new HashMap<String,Object>();
	referenceDataMap.put("appUserTypeId", user.getAppUserTypeId());
	//in case of retailer set mfs id - for Transaction history button
	if(user.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()){
		UserDeviceAccountsModel deviceAccountModel = mfsAccountManager.getDeviceAccountByAppUserId(user.getAppUserId(),null);
		if(deviceAccountModel != null && deviceAccountModel.getUserId() != null){
			referenceDataMap.put("mfsId", deviceAccountModel.getUserId());
		}
		if(user.getRelationRetailerContactIdRetailerContactModel().getHead()){
			referenceDataMap.put("isHeadRetailer", true);
		}
	}
    if(user.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {
        UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(user.getAppUserId(), null);
        if(deviceAccountModel != null && deviceAccountModel.getUserId() != null) {
            referenceDataMap.put("mfsId", deviceAccountModel.getUserId());
        }
    }

	return referenceDataMap;
}
@Override
protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> map) throws Exception {

	pagingHelperModel.setTotalRecordsCount(0);
	String customerOrAgent = httpServletRequest.getParameter("customerOrAgent");
	if(customerOrAgent != null && !customerOrAgent.isEmpty()){
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		HomeSearchModel homeSearchModel = (HomeSearchModel)model;
		if(customerOrAgent.equals("Customer")){
			UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();			
			if(homeSearchModel.getSearchCriteria()){
				userInfoListViewModel.setMobileNo(homeSearchModel.getCustomerSearch());
			}else{
				userInfoListViewModel.setNic(homeSearchModel.getCustomerSearch().replace("-", ""));
			}
//			userInfoListViewModel.setAccountEnabled(true);
			searchBaseWrapper.setBasePersistableModel(userInfoListViewModel);
			searchBaseWrapper = this.mfsAccountManager.searchUserInfo(searchBaseWrapper);
			if(searchBaseWrapper.getBasePersistableModel() != null){
				CustomList<UserInfoListViewModel> customerInfoListViewModels = searchBaseWrapper.getCustomList();
				if(customerInfoListViewModels.getResultsetList() != null && customerInfoListViewModels.getResultsetList().size() > 0){
					UserInfoListViewModel customer = customerInfoListViewModels.getResultsetList().get(0);
					ModelAndView modelAndView = null;
					if(customer.getCustomerAccountTypeId()==1 || customer.getCustomerAccountTypeId() == 2){
						modelAndView =  new ModelAndView("redirect:p_mnomfsaccountdetails.html?appUserId="+EncryptionUtil.encryptWithDES(""+customer.getAppUserId())+"&actionId=2");
					}else{
						modelAndView =  new ModelAndView("redirect:p_level2accountdetails.html?appUserId="+EncryptionUtil.encryptWithDES(""+customer.getAppUserId())+"&actionId=2");
					}
					return modelAndView;
				}else{
					this.saveMessage(httpServletRequest, "No Customer Found.");
					new ModelAndView("home");
				}
			}
			
		}else if(customerOrAgent.equals("Agent")){
			  RetailerContactListViewModel retailerContactModel = new RetailerContactListViewModel();
			  if(homeSearchModel.getSearchCriteria()){
				  retailerContactModel.setMobileNo(homeSearchModel.getAgentSearch());
				}else{
					retailerContactModel.setNic(homeSearchModel.getAgentSearch().replace("-", ""));
				}
			  searchBaseWrapper.setBasePersistableModel(retailerContactModel);
			  
			  searchBaseWrapper = this.allpayRetailerAccountManager.searchAccount(searchBaseWrapper);
			  if(searchBaseWrapper.getBasePersistableModel() != null){
					CustomList<RetailerContactListViewModel> retailerContactListViewModels = searchBaseWrapper.getCustomList();
					if(retailerContactListViewModels.getResultsetList() != null && retailerContactListViewModels.getResultsetList().size() > 0){
						RetailerContactListViewModel agent = retailerContactListViewModels.getResultsetList().get(0);
						RedirectView redirectView = new RedirectView("allpayRetailerAccountDetails.html?retailerContactId="+agent.getRetailerContactId()+"&actionId=2");
						redirectView.setExposeModelAttributes(false);
						ModelAndView modelAndView =  new ModelAndView(redirectView);
						return modelAndView;
					}else{
						RetailerContactListViewModel agent = retailerContactListViewModels.getResultsetList().get(0);
						this.saveMessage(httpServletRequest, "No Agent Found.");
						return new ModelAndView("welcomescreen.aw?UID="+agent.getUsername());
					}
				}
		}else if(customerOrAgent.equals("Handler")){
				HandlerSearchViewModel handlerSearchViewModel = new HandlerSearchViewModel();
			  if(homeSearchModel.getSearchCriteria()){
				  handlerSearchViewModel.setContactNo(homeSearchModel.getHandlerSearch());
				}else{
					handlerSearchViewModel.setCnic(homeSearchModel.getHandlerSearch().replace("-", ""));
				}
			  searchBaseWrapper.setBasePersistableModel(handlerSearchViewModel);
			  
			  searchBaseWrapper = this.handlerManager.searchHandlerView(searchBaseWrapper);
			  if(searchBaseWrapper.getBasePersistableModel() != null){
					CustomList<HandlerSearchViewModel> handlerSearchViewModels = searchBaseWrapper.getCustomList();
					if(handlerSearchViewModels.getResultsetList() != null && handlerSearchViewModels.getResultsetList().size() > 0){
						HandlerSearchViewModel handler = handlerSearchViewModels.getResultsetList().get(0);
						RedirectView redirectView = new RedirectView("p_handleraccountdetails.html?handlerId="+handler.getHandlerId()+"&actionId=2");
						redirectView.setExposeModelAttributes(false);
						ModelAndView modelAndView =  new ModelAndView(redirectView);
						return modelAndView;
					}else{
						this.saveMessage(httpServletRequest, "No Handler Found.");
						return new ModelAndView("home");
					}
				}
		}
	}
	return new ModelAndView("home");
}

public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
	this.mfsAccountManager = mfsAccountManager;
}

public void setAllpayRetailerAccountManager(
		AllpayRetailerAccountManager allpayRetailerAccountManager) {
	this.allpayRetailerAccountManager = allpayRetailerAccountManager;
}


public void setHandlerManager(HandlerManager handlerManager) {
	this.handlerManager = handlerManager;
}
}
