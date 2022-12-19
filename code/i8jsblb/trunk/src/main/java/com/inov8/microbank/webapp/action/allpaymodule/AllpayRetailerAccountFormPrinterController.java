package com.inov8.microbank.webapp.action.allpaymodule;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.olamodule.OLAManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class AllpayRetailerAccountFormPrinterController extends AdvanceFormController {
	private RetailerContactManager retailerContactManager;
	private AllpayRetailerAccountManager allpayRetailerAccountManager;
	private ReferenceDataManager referenceDataManager;	
	private PartnerGroupManager partnerGroupManager;
	private AppUserManager appUserManager;
	private FinancialInstitution olaVeriflyFinancialInstitution;
	private OLAManager olaManager;	
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private ActionLogManager actionLogManager;
	private SmartMoneyAccountDAO smartMoneyAccountDAO;
	private Long id;

	
	public AllpayRetailerAccountFormPrinterController() {
		setCommandName("retailerContactListViewFormModel");
		setCommandClass(RetailerContactListViewFormModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "retailerContactId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			RetailerContactListViewFormModel retailerContactListViewFormModel = new RetailerContactListViewFormModel();
			RetailerContactListViewModel retailerContactListViewModel = new RetailerContactListViewModel();
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactListViewModel.setRetailerContactId(id);
			retailerContactModel.setRetailerContactId(id);
			searchBaseWrapper.setBasePersistableModel(retailerContactListViewModel);
			
			searchBaseWrapper = this.retailerContactManager.loadRetailerContactListView(searchBaseWrapper);
			retailerContactListViewModel = (RetailerContactListViewModel) searchBaseWrapper
					.getBasePersistableModel();
			// setting the area id //
			Double balance =retailerContactListViewModel.getBalance();
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			String bal =myFormatter.format(balance);
			httpServletRequest.setAttribute("balance", bal);
			retailerContactListViewModel.setBalance(Double.valueOf(bal));
			httpServletRequest.setAttribute("areaIdRef", retailerContactListViewModel.getAreaId());
			searchBaseWrapper = this.retailerContactManager.loadRetailerContact(searchBaseWrapper);

			retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
			AppUserModel appUserModel = new AppUserModel();
			appUserModel = appUserManager
					.getUser(String.valueOf(retailerContactListViewModel.getAppUserId()));
			
			AppUserPartnerGroupModel model = this.retailerContactManager.getAppUserPartnetGroupByAppUserId(appUserModel.getAppUserId());
			if(model != null){
			Collection<AppUserPartnerGroupModel> appUserPartnetGroupList = new ArrayList <AppUserPartnerGroupModel>();
			appUserPartnetGroupList.add(model);
			appUserModel.setAppUserIdAppUserPartnerGroupModelList(appUserPartnetGroupList);
			}
			
			RetailerModel retailerModel = new RetailerModel();
			httpServletRequest.setAttribute("retailerId", retailerContactListViewModel.getRetailerId());
			retailerModel.setAreaId( retailerContactListViewModel.getAreaId() ) ;			 
			 ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(retailerModel);
			
			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<RetailerModel> retailerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				retailerModelList = referenceDataWrapper.getReferenceDataList();
			}
			httpServletRequest.setAttribute("retailerModelList", retailerModelList);
			
			//according to new agent account form
			retailerContactListViewFormModel.setUsername(appUserModel.getUsername());
			retailerContactListViewFormModel.setPassword(appUserModel.getPassword());
			retailerContactListViewFormModel.setFirstName(appUserModel.getFirstName());
			retailerContactListViewFormModel.setLastName(appUserModel.getLastName());
			retailerContactListViewFormModel.setMobileNumber(retailerContactModel.getZongMsisdn());
			retailerContactListViewFormModel.setCnicNo(getNicWithHyphins(appUserModel.getNic()));
			retailerContactListViewFormModel.setCnicExpiryDate(appUserModel.getNicExpiryDate());
			
			retailerContactListViewFormModel.setAreaId(retailerContactModel.getAreaId());
			retailerContactListViewFormModel.setPrintAreaName(retailerContactModel.getAreaIdAreaModel().getName());
			retailerContactListViewFormModel.setRetailerId(retailerContactModel.getRetailerId());
			retailerContactListViewFormModel.setPrintRetailerName(retailerContactModel.getRetailerIdRetailerModel().getName());
			retailerContactListViewFormModel.setHead((retailerContactModel.getHead() == null || retailerContactModel.getHead() == false) ? true : null);
			retailerContactListViewFormModel.setApplicationNo(retailerContactModel.getApplicationNo());
			retailerContactListViewFormModel.setZongMsisdn(appUserModel.getMobileNo());
			retailerContactListViewFormModel.setAccountOpeningDate(retailerContactModel.getAccountOpeningDate());
			retailerContactListViewFormModel.setNtnNo(retailerContactModel.getNtn());
			retailerContactListViewFormModel.setContactNo(retailerContactModel.getContactNo());
			retailerContactListViewFormModel.setLandLineNo(retailerContactModel.getLandLineNo());
			retailerContactListViewFormModel.setBusinessName(retailerContactModel.getBusinessName());
			retailerContactListViewFormModel.setNatureOfBusiness(retailerContactModel.getNatureOfBusinessId());
			retailerContactListViewFormModel.setPrintNatureOfBusinessName(retailerContactModel.getNatureOfBusinessIdNatureOfBusinessModel().getName());
			
			Collection<AppUserPartnerGroupModel> appUserPartnerGroupModelList = appUserModel.getAppUserIdAppUserPartnerGroupModelList();
			if(appUserPartnerGroupModelList != null && appUserPartnerGroupModelList.size() > 0){
				for( AppUserPartnerGroupModel appUserPartnerGroupModel : appUserPartnerGroupModelList){
					if(appUserPartnerGroupModel.getAppUserPartnerGroupId() != null){
						retailerContactListViewFormModel.setPartnerGroupId(appUserPartnerGroupModel.getAppUserPartnerGroupId());
						retailerContactListViewFormModel.setPrintPartnerGroupName(appUserPartnerGroupModel.getRelationPartnerGroupIdPartnerGroupModel().getName());
					}
				}
			}
						
			
//			BeanUtils.copyProperties(retailerContactListViewFormModel, retailerContactListViewModel);
//			BeanUtils.copyProperties(retailerContactListViewFormModel, appUserModel);
//
//			retailerContactListViewFormModel.setHead(retailerContactModel.getHead());
//			retailerContactListViewFormModel.setComments( retailerContactModel.getComments() ) ;
//			retailerContactListViewFormModel.setDob( appUserModel.getDob() ) ;			
//
//			retailerContactListViewFormModel.setBalance(retailerContactModel.getBalance());
			
			
			
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

	    	  return retailerContactListViewFormModel;
				
			
//			//-----------mera code----------
//			retailerContactListViewFormModel.setAccountNick(loadSmartMoneyAccountModel(id).getName());
//			//step 2 ---load data for allpay id account nick
//			ActionLogModel actionLogModel = this.logBeforeAction(ServletRequestUtils.getLongParameter(
//					httpServletRequest, PortalConstants.KEY_ACTION_ID),
//					PortalConstants.OLA_SEARCH_ACCOUNT, appUserModel
//							.getAppUserId());
//			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
//			OLAVO olaVo = new OLAVO ();
//			SwitchWrapper sWrapper = new SwitchWrapperImpl ();			
//			olaVo.setCnic(appUserModel.getNic()	);	
//			
//			sWrapper.setBankId(this.allpayRetailerAccountManager.getOlaBankMadal().getBankId());
//			sWrapper.setOlavo(olaVo);
//			 try
//			{
//				sWrapper= olaVeriflyFinancialInstitution.getAccountInfo(sWrapper);
//				
//				 logAfterAction(actionLogModel,String.valueOf(appUserModel.getAppUserId()));
//				 olaVo =sWrapper.getOlavo();
//				 retailerContactListViewFormModel.setFatherName(olaVo.getFatherName());
//				 retailerContactListViewFormModel.setMiddleName(olaVo.getMiddleName());
//				 retailerContactListViewFormModel.setLandlineNumber(olaVo.getLandlineNumber());
//				 retailerContactListViewFormModel.setAccountStatusId(olaVo.getStatusId());
//				 retailerContactListViewFormModel.setOldNic(olaVo.getCnic());
//				 //System.out.println("nic at ola is :"+olaVo.getCnic());
//				appUserModel = getAppUserModel (id);
//				UserDeviceAccountsModel userDeviceAccountsModel =   getUserDeviceAccountModel(appUserModel);
//				retailerContactListViewFormModel.setCommissioned(userDeviceAccountsModel.getCommissioned());
//				retailerContactListViewFormModel.setAllpayId(userDeviceAccountsModel.getUserId());///get allpay id
//				
//				
//				retailerContactListViewFormModel.setPartnerGroupId(this.retailerContactManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));
//				return retailerContactListViewFormModel;
//
//			}
//			catch (WorkFlowException e)
//			{
//				if( e.getMessage().equals(WorkFlowErrorCodeConstants.DELETED_ACCOUNT) )
//				{
//					super.saveMessage(httpServletRequest, "This account has been deleted from OLA Bank.");
//					this.setFormView(this.getSuccessView());
//				}
//				else if( e.getMessage().equals(WorkFlowErrorCodeConstants.INACTIVE_ACCOUNT) )
//				{
//					super.saveMessage(httpServletRequest, "This account has been disabled in OLA Bank.");
//					this.setFormView(this.getSuccessView());
//				}
//				
//			}
		}
		else
		{
			
			if (log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}
			this.setFormView("allpayRetailerAccountForm");
			return new RetailerContactListViewFormModel();
		}
		
//		return new RetailerContactListViewFormModel();
	}

	
	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws Exception {
		
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView(getSuccessView() );
		return modelAndView;
		
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartnerGroupManager getPartnerGroupManager() {
		return partnerGroupManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public RetailerContactManager getRetailerContactManager() {
		return retailerContactManager;
	}

	public void setRetailerContactManager(
			RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}

	public ActionLogManager getActionLogManager() {
		return actionLogManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public AllpayRetailerAccountManager getAllpayRetailerAccountManager() {
		return allpayRetailerAccountManager;
	}

	public void setAllpayRetailerAccountManager(
			AllpayRetailerAccountManager allpayRetailerAccountManager) {
		this.allpayRetailerAccountManager = allpayRetailerAccountManager;
	}

	public OLAManager getOlaManager() {
		return olaManager;
	}

	public void setOlaManager(OLAManager olaManager) {
		this.olaManager = olaManager;
	}

	public FinancialInstitution getOlaVeriflyFinancialInstitution() {
		return olaVeriflyFinancialInstitution;
	}

	public void setOlaVeriflyFinancialInstitution(
			FinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public SmartMoneyAccountDAO getSmartMoneyAccountDAO() {
		return smartMoneyAccountDAO;
	}

	public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
		this.smartMoneyAccountDAO = smartMoneyAccountDAO;
	}

	public UserDeviceAccountsDAO getUserDeviceAccountsDAO() {
		return userDeviceAccountsDAO;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}
	
	private String getNicWithHyphins(String nic){
    	String nicWithHyphins = "";
    	if(nic != null && !nic.isEmpty() && nic.length() == 13){
    		String firstPart = nic.substring(0, 5);
    		String secondPart = nic.substring(5, 12);
    		String thirdPart = nic.substring(12);
    		
    		nicWithHyphins = firstPart + "-" +secondPart + "-"+ thirdPart;
    	}
    	return nicWithHyphins;
    }

}
