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
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

public class AllpayRetailerDetailsController extends AdvanceFormController {
	private RetailerContactManager retailerContactManager;
	private AllpayRetailerAccountManager allpayRetailerAccountManager;
	private AppUserManager appUserManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private AccountControlManager 	accountControlManager;
	
	public AllpayRetailerDetailsController() {
		setCommandName("retailerContactListViewFormModel");
		setCommandClass(RetailerContactListViewFormModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		
		Long id = ServletRequestUtils.getLongParameter(httpServletRequest, "retailerContactId");
		
		if (null != id){
	    	boolean veriflyRequired = true;
	    	try{
				BaseWrapper baseWrapperBank = new BaseWrapperImpl();
				BankModel bankModel = new BankModel();
				bankModel.setBankId(CommissionConstantsInterface.BANK_ID);
				baseWrapperBank.setBasePersistableModel(bankModel);
		    	AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
	    		veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
	    	}catch(Exception ex){
	    		ex.printStackTrace();
	    	}   
	    	httpServletRequest.setAttribute("veriflyRequired", veriflyRequired);

			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			RetailerContactListViewFormModel retailerContactListViewFormModel = new RetailerContactListViewFormModel();
			RetailerContactListViewModel retailerContactListViewModel = new RetailerContactListViewModel();
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactListViewModel.setRetailerContactId(id);
			retailerContactModel.setRetailerContactId(id);
			searchBaseWrapper.setBasePersistableModel(retailerContactListViewModel);
			searchBaseWrapper = this.retailerContactManager.loadRetailerContactListView(searchBaseWrapper);
			retailerContactListViewModel = (RetailerContactListViewModel) searchBaseWrapper.getBasePersistableModel();

			searchBaseWrapper = this.retailerContactManager.loadRetailerContact(searchBaseWrapper);
			retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
			AppUserModel appUserModel = appUserManager.getUser(String.valueOf(retailerContactListViewModel.getAppUserId()));

			SmartMoneyAccountModel smartMoneyAccount = this.smartMoneyAccountManager.getSMAccountByRetailerContactId(id);
			if(smartMoneyAccount != null && smartMoneyAccount.getSmartMoneyAccountId() != null){
				httpServletRequest.setAttribute("smAccountId", smartMoneyAccount.getSmartMoneyAccountId());
			}
			if(retailerContactModel.getHead()){
				httpServletRequest.setAttribute("headRetailer", "true");
			}
			if(appUserModel != null){
				httpServletRequest.setAttribute("registerationStateId", appUserModel.getRegistrationStateId());
				String cnic = appUserModel.getNic();
				httpServletRequest.setAttribute("appUserId", appUserModel.getAppUserId());
				UserDeviceAccountsModel userDeviceAccountsModel = this.allpayRetailerAccountManager.getUserDeviceAccountsModel(String.valueOf(appUserModel.getAppUserId()));
				if(userDeviceAccountsModel != null){
					httpServletRequest.setAttribute("mfsId", userDeviceAccountsModel.getUserId());
					httpServletRequest.setAttribute("deviceAccId", userDeviceAccountsModel.getUserDeviceAccountsId());
					httpServletRequest.setAttribute("deviceAccEnabled", userDeviceAccountsModel.getAccountEnabled());
					httpServletRequest.setAttribute("deviceAccLocked", userDeviceAccountsModel.getAccountLocked());
					httpServletRequest.setAttribute("credentialsExpired", userDeviceAccountsModel.getCredentialsExpired());
					httpServletRequest.setAttribute("blacklisted", accountControlManager.isCnicBlacklisted(cnic));
					httpServletRequest.setAttribute("isEnabledAgentWeb", accountControlManager.isAgentWebEnabled(id));
					httpServletRequest.setAttribute("isEnabledAgentUSSD", accountControlManager.isAgentUSSDEnabled(id));
					//Debit Card Enabled
					httpServletRequest.setAttribute("isEnabledDebitCardFee", accountControlManager.isDebitCardFeeEnabled(id));

					String statusDetails = appUserManager.getStatusDetails(appUserModel.getAppUserId(),
							userDeviceAccountsModel.getUpdatedOn(),
							userDeviceAccountsModel.getAccountLocked(),
							userDeviceAccountsModel.getAccountEnabled()
							);
					httpServletRequest.setAttribute("statusDetails", statusDetails);

				}
				retailerContactListViewFormModel.setRetailerContactId(id);
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
				retailerContactListViewFormModel.setAccountStatusId(appUserModel.getAccountStateId());
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
			      Long appUserId = appUserModel.getAppUserId();
			      List<AppUserHistoryViewModel> appUserHistoryViewModelList = searchAppUserHistoryView(appUserId,nic);
			      httpServletRequest.setAttribute("appUserHistoryViewModelList", appUserHistoryViewModelList);

			}
	    	  Collection<RetailerContactAddressesModel> retailerContactAddresses = retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList();
	    	  if(retailerContactAddresses != null && retailerContactAddresses.size() > 0){
	    		  for(RetailerContactAddressesModel retAdd : retailerContactAddresses){
	    			  AddressModel addressModel = retAdd.getAddressIdAddressModel();
	    			  if(retAdd.getAddressTypeId() == 5){
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
	    				  if(addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty()){
	    					  retailerContactListViewFormModel.setAddress1(addressModel.getStreetAddress());
	    				  }
	    			  }
	    		  }
	    	  }
		      
	    	  //httpServletRequest.setAttribute("pageMfsId", (String)appUserModel.getUsername());
	    	  return retailerContactListViewFormModel;
		}else{
			if (log.isDebugEnabled()){
				log.debug("id is null....creating new instance of Model");
			}
			this.setFormView("allpayRetailerAccountForm");
			return new RetailerContactListViewFormModel();
		}
		
	}

	private List<AppUserHistoryViewModel> searchAppUserHistoryView(Long appUserId,String nic) throws FrameworkCheckedException
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
		appUserHistoryViewModel.setAppUserId(appUserId);

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

	public void setAllpayRetailerAccountManager(
			AllpayRetailerAccountManager allpayRetailerAccountManager) {
		this.allpayRetailerAccountManager = allpayRetailerAccountManager;
	}
	
	public RetailerContactManager getRetailerContactManager() {
		return retailerContactManager;
	}

	public void setRetailerContactManager(
			RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}
	
	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setAccountControlManager(AccountControlManager accountControlManager) {
		this.accountControlManager = accountControlManager;
	}
	

}
