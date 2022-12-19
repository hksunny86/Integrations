package com.inov8.microbank.webapp.action.allpaymodule;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserHistoryViewModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

public class HandlerDetailsController extends AdvanceFormController {
	
	private AppUserManager appUserManager;
	private HandlerManager handlerManager;
	private RetailerContactManager retailerContactManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private AllpayRetailerAccountManager allpayRetailerAccountManager;
	private AccountControlManager 		accountControlManager;

	public HandlerDetailsController() {
		setCommandName("retailerContactListViewFormModel");
		setCommandClass(RetailerContactListViewFormModel.class);
	}

	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		Long handlerId = ServletRequestUtils.getLongParameter(httpServletRequest, "handlerId");
		logger.info("Start of HandlerDetailsController.loadFormBackingObject() for HANDLER_ID = " + handlerId);
		RetailerContactListViewFormModel retailerContactListViewFormModel = new RetailerContactListViewFormModel();
			
		if (handlerId != null) {
		    	
		   	httpServletRequest.setAttribute("handlerId", handlerId);

		   	HandlerModel handlerModel = new HandlerModel(handlerId);
		    	
		   	BaseWrapper baseWrapper = new BaseWrapperImpl();
		   	baseWrapper.setBasePersistableModel(handlerModel);
		   	baseWrapper = handlerManager.loadHandler(baseWrapper);
		   	handlerModel = (HandlerModel) baseWrapper.getBasePersistableModel();
		    		    	
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setHandlerId(handlerId);
			appUserModel = appUserManager.getAppUserModel(appUserModel);

			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(handlerModel.getRetailerContactId());
			searchBaseWrapper.setBasePersistableModel(retailerContactModel);
			/*RetailerContactListViewModel retailerContactListViewModel = new RetailerContactListViewModel();
			retailerContactListViewModel.setRetailerContactId(handlerModel.getRetailerContactId());
			searchBaseWrapper.setBasePersistableModel(retailerContactListViewModel);
			searchBaseWrapper = this.retailerContactManager.loadRetailerContactListView(searchBaseWrapper);
			retailerContactListViewModel = (RetailerContactListViewModel) searchBaseWrapper.getBasePersistableModel();*/

			searchBaseWrapper = this.retailerContactManager.loadRetailerContact(searchBaseWrapper);
			retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();

			SmartMoneyAccountModel smartMoneyAccount = new SmartMoneyAccountModel();
			smartMoneyAccount.setHandlerId(handlerId);

			baseWrapper.setBasePersistableModel(smartMoneyAccount);
			
			baseWrapper = this.smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
			
			smartMoneyAccount = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
			
			if(smartMoneyAccount != null && smartMoneyAccount.getSmartMoneyAccountId() != null){
				httpServletRequest.setAttribute("smAccountId", smartMoneyAccount.getSmartMoneyAccountId());
			}
			if(retailerContactModel.getHead()){
				httpServletRequest.setAttribute("headRetailer", "true");
			}
			if(appUserModel != null){
				String cnic = appUserModel.getNic();
				httpServletRequest.setAttribute("appUserId", appUserModel.getAppUserId());
				UserDeviceAccountsModel userDeviceAccountsModel = this.allpayRetailerAccountManager.getUserDeviceAccountsModel(String.valueOf(appUserModel.getAppUserId()));
				if(userDeviceAccountsModel != null){
					httpServletRequest.setAttribute("mfsId", userDeviceAccountsModel.getUserId());
					httpServletRequest.setAttribute("deviceAccId", userDeviceAccountsModel.getUserDeviceAccountsId());
					httpServletRequest.setAttribute("deviceAccEnabled", userDeviceAccountsModel.getAccountEnabled());
					httpServletRequest.setAttribute("deviceAccLocked", userDeviceAccountsModel.getAccountLocked());
					httpServletRequest.setAttribute("credentialsExpired", userDeviceAccountsModel.getCredentialsExpired());
					
					String statusDetails = appUserManager.getStatusDetails(appUserModel.getAppUserId(),
															userDeviceAccountsModel.getUpdatedOn(),
															userDeviceAccountsModel.getAccountLocked(),
															userDeviceAccountsModel.getAccountEnabled()
															);
					httpServletRequest.setAttribute("statusDetails", statusDetails);
					httpServletRequest.setAttribute("blacklisted", accountControlManager.isCnicBlacklisted(cnic));
                    httpServletRequest.setAttribute("isEnabledAgentWeb", handlerManager.isAgentWebEnabled(handlerId));
                    httpServletRequest.setAttribute("isEnabledDebitCardFee", handlerManager.isDebitCardFeeEnabled(handlerId));
					httpServletRequest.setAttribute("username", appUserModel.getUsername());
				}

				retailerContactListViewFormModel.setRetailerContactId(handlerModel.getRetailerContactId());
				retailerContactListViewFormModel.setDob(appUserModel.getDob());
				retailerContactListViewFormModel.setUsername(appUserModel.getUsername());
				retailerContactListViewFormModel.setPassword(appUserModel.getPassword());
				retailerContactListViewFormModel.setFirstName(appUserModel.getFirstName());
				retailerContactListViewFormModel.setLastName(appUserModel.getLastName());
				retailerContactListViewFormModel.setMobileNumber(appUserModel.getMobileNo());
				retailerContactListViewFormModel.setCnicNo(appUserModel.getNic());
				retailerContactListViewFormModel.setCnicExpiryDate(appUserModel.getNicExpiryDate());
				retailerContactListViewFormModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
				retailerContactListViewFormModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
			      if(null!=appUserModel.getClosedByAppUserModel()){
			    	  retailerContactListViewFormModel.setClosedBy(appUserModel.getClosedByAppUserModel().getUsername());
			      }
			      retailerContactListViewFormModel.setClosedOn(appUserModel.getClosedOn());
			      if(null!=appUserModel.getSettledByAppUserModel()){
			    	  retailerContactListViewFormModel.setSettledBy(appUserModel.getSettledByAppUserModel().getUsername());
			      }
			      retailerContactListViewFormModel.setSettledOn(appUserModel.getSettledOn());
			      retailerContactListViewFormModel.setClosingComments(appUserModel.getClosingComments());
			      retailerContactListViewFormModel.setSettlementComments(appUserModel.getSettlementComments());
			      
			      String nic = appUserModel.getNic();
			      List<AppUserHistoryViewModel> appUserHistoryViewModelList = searchAppUserHistoryView(nic);
			      httpServletRequest.setAttribute("appUserHistoryViewModelList", appUserHistoryViewModelList);
			}
	    	  Collection<RetailerContactAddressesModel> retailerContactAddresses = retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList();
	    	  if(retailerContactAddresses != null && retailerContactAddresses.size() > 0){
	    		  for(RetailerContactAddressesModel retAdd : retailerContactAddresses){
	    			  AddressModel addressModel = retAdd.getAddressIdAddressModel();
	    			  if(retAdd.getAddressTypeId() == 3){
	    				  if(addressModel.getCityId() != null){
	    					  retailerContactListViewFormModel.setCityVillage(addressModel.getCityId());
	    					  retailerContactListViewFormModel.setPrintCityVillageName(addressModel.getCityIdCityModel().getName());
	    				  }
	    				  if(addressModel.getDistrictId() != null){
	    					  retailerContactListViewFormModel.setDistrictTehsilTown(addressModel.getDistrictId());
	    					  retailerContactListViewFormModel.setPrintDistrictTehsilTownName(addressModel.getDistrictIdDistrictModel().getName());
	    				  }
	    				  if(addressModel.getPostalOfficeId() != null){
	    					  retailerContactListViewFormModel.setPostOffice(addressModel.getPostalOfficeId());
	    					  retailerContactListViewFormModel.setPrintPostOfficeName(addressModel.getPostalOfficeIdPostalOfficeModel().getName());
	    				  }
	    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
	    					  retailerContactListViewFormModel.setShopNo(addressModel.getHouseNo()); 
	    				  }
	    				  if(addressModel.getStreetNo() != null && !addressModel.getStreetNo().isEmpty()){
	    					  retailerContactListViewFormModel.setMarketPlaza(addressModel.getStreetNo()); 
	    				  }
	    				  if(addressModel.getNtn() != null && !addressModel.getNtn().isEmpty()){
	    					  retailerContactListViewFormModel.setNtnNumber(addressModel.getNtn()); 
	    				  }
	    				  if(addressModel.getNearestLandMark() != null && !addressModel.getNearestLandMark().isEmpty()){
	    					  retailerContactListViewFormModel.setNearestLandmark(addressModel.getNearestLandMark()); 
	    				  }
	    			  }
	    		  }
	    	  }
		      
	    	  //httpServletRequest.setAttribute("pageMfsId", (String)appUserModel.getUsername());
	    	  AppUserModel user = UserUtils.getCurrentUser();
	    	  httpServletRequest.setAttribute("appUserTypeId", user.getAppUserTypeId());
			logger.info("End of HandlerDetailsController.loadFormBackingObject() for HANDLER_ID = " + handlerId);
	    	  return retailerContactListViewFormModel;
		}else{
			if (log.isDebugEnabled()){
				log.debug("id is null....creating new instance of Model");
			}
			this.setFormView("p_handleraccountdetails");
			logger.info("End of HandlerDetailsController.loadFormBackingObject() for HANDLER_ID = " + handlerId);
			return new RetailerContactListViewFormModel();
		}
		
		
		
	}
	
	private List<AppUserHistoryViewModel> searchAppUserHistoryView(String nic) throws FrameworkCheckedException
	{
		nic = nic.substring(nic.indexOf("-") + 1);
		
		///Check for Closed Settled Account///
		if(nic.contains("_")){
			int endIndex = nic.indexOf("_");
			nic = nic.substring(0, endIndex);
		}
		///End Check for Closed Settled Account///
		
		List<AppUserHistoryViewModel> appUserHistoryViewModelList = null;
		AppUserHistoryViewModel appUserHistoryViewModel = new AppUserHistoryViewModel();
		appUserHistoryViewModel.setNic(nic);

		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(appUserHistoryViewModel);

		LinkedHashMap<String,SortingOrder> sortingOrderMap = new LinkedHashMap<>();
		sortingOrderMap.put("createdOn", SortingOrder.ASC);
		wrapper.setSortingOrderMap(sortingOrderMap);

		wrapper = appUserManager.searchAppUserHistoryView(wrapper);
		CustomList<AppUserHistoryViewModel> customList = wrapper.getCustomList();
		if(customList != null)
		{
			appUserHistoryViewModelList = customList.getResultsetList();
		}
		return appUserHistoryViewModelList;
	}

	
	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		return new HashMap();
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public AllpayRetailerAccountManager getAllpayRetailerAccountManager() {
		return allpayRetailerAccountManager;
	}

	public void setAllpayRetailerAccountManager(AllpayRetailerAccountManager allpayRetailerAccountManager) {
		this.allpayRetailerAccountManager = allpayRetailerAccountManager;
	}
	
	public RetailerContactManager getRetailerContactManager() {
		return retailerContactManager;
	}

	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, BindException arg3) throws Exception {
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, BindException arg3) throws Exception {
		return null;
	}
	
	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}
	
	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}


	public void setAccountControlManager(AccountControlManager accountControlManager) {
		this.accountControlManager = accountControlManager;
	}
	
}