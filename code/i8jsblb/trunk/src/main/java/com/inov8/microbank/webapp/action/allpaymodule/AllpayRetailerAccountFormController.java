package com.inov8.microbank.webapp.action.allpaymodule;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.DistrictModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.NatureOfBusinessModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.PostalOfficeModel;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.DistributorConstants;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.olamodule.OLAManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class AllpayRetailerAccountFormController extends AdvanceFormController {
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
//	private Long id;

	
	public AllpayRetailerAccountFormController() {
		setCommandName("retailerContactListViewFormModel");
		setCommandClass(RetailerContactListViewFormModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		Long retailerContactId = ServletRequestUtils.getLongParameter(httpServletRequest, "retailerContactId");
		if (null != retailerContactId && retailerContactId.longValue() != 0L)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			RetailerContactListViewFormModel retailerContactListViewFormModel = new RetailerContactListViewFormModel();
			RetailerContactListViewModel retailerContactListViewModel = new RetailerContactListViewModel();
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactListViewModel.setRetailerContactId(retailerContactId);
			retailerContactModel.setRetailerContactId(retailerContactId);
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
			retailerContactListViewFormModel.setAppUserId(appUserModel.getAppUserId());
			retailerContactListViewFormModel.setRetailerContactId(retailerContactId);
			retailerContactListViewFormModel.setUsername(appUserModel.getUsername());
			retailerContactListViewFormModel.setPassword(appUserModel.getPassword());
			retailerContactListViewFormModel.setFirstName(appUserModel.getFirstName());
			retailerContactListViewFormModel.setLastName(appUserModel.getLastName());
			retailerContactListViewFormModel.setCnicNo(appUserModel.getNic());
			retailerContactListViewFormModel.setCnicExpiryDate(appUserModel.getNicExpiryDate());
			retailerContactListViewFormModel.setZongMsisdn(retailerContactModel.getZongMsisdn());
			
			retailerContactListViewFormModel.setAreaId(retailerContactModel.getAreaId());
			retailerContactListViewFormModel.setRetailerId(retailerContactModel.getRetailerId());
			retailerContactListViewFormModel.setHead((retailerContactModel.getHead() == null || retailerContactModel.getHead() == false) ? true : null);
			retailerContactListViewFormModel.setApplicationNo(retailerContactModel.getApplicationNo());
			retailerContactListViewFormModel.setMobileNumber(appUserModel.getMobileNo());
			retailerContactListViewFormModel.setAccountOpeningDate(retailerContactModel.getAccountOpeningDate());
			retailerContactListViewFormModel.setNtnNo(retailerContactModel.getNtn());
			retailerContactListViewFormModel.setContactNo(retailerContactModel.getContactNo());
			retailerContactListViewFormModel.setLandLineNo(retailerContactModel.getLandLineNo());
			retailerContactListViewFormModel.setBusinessName(retailerContactModel.getBusinessName());
			retailerContactListViewFormModel.setNatureOfBusiness(retailerContactModel.getNatureOfBusinessId());
			Collection<AppUserPartnerGroupModel> appUserPartnerGroupModelList = appUserModel.getAppUserIdAppUserPartnerGroupModelList();
			if(appUserPartnerGroupModelList != null && appUserPartnerGroupModelList.size() > 0){
				for( AppUserPartnerGroupModel appUserPartnerGroupModel : appUserPartnerGroupModelList){
					if(appUserPartnerGroupModel.getAppUserPartnerGroupId() != null){
						retailerContactListViewFormModel.setPartnerGroupId(appUserPartnerGroupModel.getPartnerGroupId());
					}
				}
			}
//			retailerModel.setAreaId(retailerContactModel.getAreaId() ) ;
//			retailerModel.setActive(true);
//			referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
//			referenceDataWrapper.setBasePersistableModel(retailerModel);
//
//			referenceDataManager.getReferenceData(referenceDataWrapper);
//			
//			if (referenceDataWrapper.getReferenceDataList() != null)
//			{
//				retailerModelList = referenceDataWrapper.getReferenceDataList();
//			}
//			httpServletRequest.setAttribute("retailerModelList", retailerModelList);
				
			
	
//			PartnerModel partnerModel = new PartnerModel();
//			partnerModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
//			if(retailerModelList!=null && retailerModelList.size()>0)
//			{
//				
//				
//				partnerModel.setRetailerId(retailerContactListViewFormModel.getRetailerId());
//		
//				
//				
//			}else {
//				Long retailerId = (Long)httpServletRequest.getAttribute("retailerId");
//				if (retailerId == null || "".equals(retailerId)){
//					try {
//						retailerId = Long.valueOf(httpServletRequest.getParameter("retailerId"));
//					} catch (Exception e) {
//					
//					}
//				}
//				partnerModel.setRetailerId(retailerId);
//			}
//			
//			
//			referenceDataWrapper = new ReferenceDataWrapperImpl(
//					partnerModel, "name", SortingOrder.ASC);
//			referenceDataWrapper.setBasePersistableModel(partnerModel);
//			try {
//			referenceDataManager.getReferenceData(referenceDataWrapper);
//			} catch (FrameworkCheckedException ex1) {
//				ex1.getStackTrace();
//			}
//			List<PartnerModel> partnerModelList = null;
//			if (referenceDataWrapper.getReferenceDataList() != null) 
//			{
//				partnerModelList = referenceDataWrapper.getReferenceDataList();
//			}
//			
//			PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
//			List<PartnerGroupModel> partnerGroupModelList = null;
//			if(partnerModelList!=null && partnerModelList.size()>0)
//			{
//				partnerGroupModel.setActive(true);
//				partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
//				partnerGroupModelList =partnerGroupManager.getPartnerGroups(partnerGroupModel,true);
//			}
//			httpServletRequest.setAttribute("partnerGroupModelList", partnerGroupModelList);
			
			
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
	    				  }
	    				  if(addressModel.getDistrictId() != null){
	    					  retailerContactListViewFormModel.setDistrictTehsilTown(addressModel.getDistrictId());
	    				  }
	    				  if(addressModel.getPostalOfficeId() != null){
	    					  retailerContactListViewFormModel.setPostOffice(addressModel.getPostalOfficeId());
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

	private SmartMoneyAccountModel loadSmartMoneyAccountModel (Long retailerContactId){
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		smartMoneyAccountModel.setBankId(this.allpayRetailerAccountManager.getOlaBankMadal().getBankId());
		smartMoneyAccountModel.setRetailerContactId(retailerContactId);
		List moneyMadel = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel).getResultsetList();
		if (moneyMadel.size() == 0){
			return new SmartMoneyAccountModel ();
		}
		return (SmartMoneyAccountModel)moneyMadel.get(0);		
	}
	private UserDeviceAccountsModel getUserDeviceAccountModel(AppUserModel appUserModel) {
		
		
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		CustomList list = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
		int size = list.getResultsetList().size();
		if (size != 0)
			userDeviceAccountsModel=(UserDeviceAccountsModel)list.getResultsetList().get(0);
		return userDeviceAccountsModel;
	
		
	}

	private AppUserModel getAppUserModel(Long id2) {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	
		AppUserModel appUserModel=new AppUserModel();
		appUserModel.setRetailerContactId(id2);
		searchBaseWrapper.setBasePersistableModel(appUserModel);
		try {
			searchBaseWrapper  = (SearchBaseWrapper)appUserManager.searchAppUser(searchBaseWrapper);
		appUserModel = (AppUserModel)	searchBaseWrapper.getBasePersistableModel();
			return appUserModel;
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appUserModel;
		
		
	}

	private ActionLogModel logBeforeAction(Long actionId, Long useCaseId,
			Long appUserId) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId(actionId);
		actionLogModel.setUsecaseId(useCaseId);
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the
		// process
		// is
		// starting
		actionLogModel.setCustomField1(String.valueOf(appUserId));
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
		return actionLogModel;
	}

	private void logAfterAction(ActionLogModel actionLogModel, String appUserId)
			throws FrameworkCheckedException {
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // the
		// process
		// is
		// starting

		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLog(baseWrapperActionLog);
	}
	
	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws Exception {
		if (log.isDebugEnabled())
		{
			log.debug("Inside reference data");
		}

		/**
		 * code fragment to load reference data  for Distributor
		 *
		 */

		DistributorModel distributorModel = new DistributorModel();
		List<RetailerModel> retailerModelList = null;
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name",
				SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(distributorModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);

		List<DistributorModel> distributorModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			distributorModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("distributorModelList", distributorModelList);


		/**
		 * code fragment to load reference data for Area
		 */

		AreaModel areaModel = new AreaModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(areaModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(areaModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<AreaModel> areaModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			areaModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("areaModelList", areaModelList);
		
		/**
		 * code fragment to load reference data for Nature of Business
		 */
		
		NatureOfBusinessModel natureOfBusinessModel = new NatureOfBusinessModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(natureOfBusinessModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(natureOfBusinessModel);
		
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<NatureOfBusinessModel> natureOfBusinessModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			natureOfBusinessModelList = referenceDataWrapper.getReferenceDataList();
		}
		
		referenceDataMap.put("natureOfBusinessModelList", natureOfBusinessModelList);
		
		// end of loading of Nature of Business
		
		DistrictModel districtModel = new DistrictModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(districtModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(districtModel);
		
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<DistrictModel> districtModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			districtModelList = referenceDataWrapper.getReferenceDataList();
		}
		
		referenceDataMap.put("districtModelList", districtModelList);
		
		
		CityModel cityModel = new CityModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(cityModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(cityModel);
		
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<CityModel> cityModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			cityModelList = referenceDataWrapper.getReferenceDataList();
		}
		
		referenceDataMap.put("cityModelList", cityModelList);
		
		
		PostalOfficeModel postalOfficeModel = new PostalOfficeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(postalOfficeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(postalOfficeModel);
		
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<PostalOfficeModel> postalOfficeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			postalOfficeModelList = referenceDataWrapper.getReferenceDataList();
		}
		
		referenceDataMap.put("postalOfficeModelList", postalOfficeModelList);
		

		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		//paymentModeModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(mobileTypeModel, "name", SortingOrder.ASC);

		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (FrameworkCheckedException ex1)
		{
			ex1.getStackTrace();
		}
		List<MobileTypeModel> mobileTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);

		
		/**
		 * code fragment to load reference data for Retailer
		 */
		
		//if (null == id)
		//{
		
		RetailerModel retailerModel = new RetailerModel();

		try
		{
			Long areaIdRef = ServletRequestUtils.getLongParameter(httpServletRequest, "areaId");

			if (areaIdRef == null)
			{
				areaIdRef = (Long) httpServletRequest.getAttribute("areaIdRef");
				retailerModel.setAreaId(areaIdRef);
			}
			else
			{
				retailerModel.setAreaId(areaIdRef);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		
		if (retailerModel.getAreaId() ==null)
		{
			
			retailerModel.setAreaId( ((AreaModel)areaModelList.get(0)).getAreaId() ) ;
//			retailerModel.setAreaId(Long.parseLong(httpServletRequest.getParameter("areaId")));
		}
//		else
//		{
//			if (areaModelList.size()>0)
//			{
//		
//		
//		}
//		}
		if(retailerModel.getAreaId() != null){
			retailerModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(retailerModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				retailerModelList = referenceDataWrapper.getReferenceDataList();
			}
			referenceDataMap.put("retailerModelList", retailerModelList);
				}

		//}
		
		
		
		
		PartnerModel partnerModel = new PartnerModel();
		partnerModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		if(retailerModelList!=null && retailerModelList.size()>0)
		{
			
			
			Long retailerId = (Long)httpServletRequest.getAttribute("retailerId");
			if (retailerId == null || "".equals(retailerId)){
				try {
					retailerId = Long.valueOf(httpServletRequest.getParameter("retailerId"));
				} catch (RuntimeException e) {
					
				}
			}
			if(null!=retailerId)
			{
				
				partnerModel.setRetailerId(retailerId);
			}
			else
			{
				partnerModel.setRetailerId(retailerModelList.get(0).getRetailerId());	
			}
	
			
			
		}else {
			Long retailerId = (Long)httpServletRequest.getAttribute("retailerId");
			if (retailerId == null || "".equals(retailerId)){
				try {
					retailerId = Long.valueOf(httpServletRequest.getParameter("retailerId"));
				} catch (Exception e) {
				
				}
			}
			partnerModel.setRetailerId(retailerId);
		}
		
		
		referenceDataWrapper = new ReferenceDataWrapperImpl(
				partnerModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(partnerModel);
		try {
		referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PartnerModel> partnerModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) 
		{
			partnerModelList = referenceDataWrapper.getReferenceDataList();
		}
		
		PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		List<PartnerGroupModel> partnerGroupModelList = null;
		if(partnerModelList!=null && partnerModelList.size()>0)
		{
			partnerGroupModel.setActive(true);
			partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
			partnerGroupModelList =partnerGroupManager.getPartnerGroups(partnerGroupModel,true);
		}
		referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		
		try {
			RetailerContactListViewFormModel madel = (RetailerContactListViewFormModel) object;
			if(madel.getHead() != null){
				madel.setHead(null);
			}
			else{
				madel.setHead(true);
			}
			
			String nicWithoutHyphins = madel.getCnicNo().replace("-", "");
			madel.setCnicNo(nicWithoutHyphins);
			
			allpayRetailerAccountManager.createAccount(httpServletRequest,
					httpServletResponse, object, bindException);
			httpServletRequest.getSession().setAttribute("messages", new ArrayList());
			String olaAccountNo =(String) httpServletRequest.getAttribute("olaAccountNo");
			String allpayId= ((RetailerContactListViewFormModel)object).getAllpayId();
			Long retailerContactId= ((RetailerContactListViewFormModel)object).getRetailerContactId();
			String message = "New Agent with ID: "+allpayId+" successfully created.";
			super.saveMessage(httpServletRequest,message);
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView()+"&retailerContactId="+retailerContactId);
			
	        return modelAndView;
		} catch (Exception e) {
			RetailerContactListViewFormModel exMadel = (RetailerContactListViewFormModel) object;
			exMadel.setCnicNo(exMadel.getCnicNo());
			((RetailerContactListViewFormModel)object).setHead((exMadel.getHead() == null || exMadel.getHead() == false) ? true : null);
			((RetailerContactListViewFormModel)object).setRetailerContactId(null);
			httpServletRequest.getSession().setAttribute("messages", new ArrayList());
			//if (e instanceof  MobileNumUniqueException)
			String exceptionMessage =(String) httpServletRequest.getAttribute("exceptionMessage");
			String olaExceptionMessage= (String)httpServletRequest.getAttribute("olaExceptionMessage");
			
			super.saveMessage(httpServletRequest,
            "Record could not be saved.");
			if (DistributorConstants.HEAD_CHK.equals(e.getMessage())){
				super.saveMessage(httpServletRequest, e.getMessage());
			}
			if ("Account No already exists.".equals(e.getMessage())){
				super.saveMessage(httpServletRequest, "Account number created by OLA service already exists in Verifly.");
			}
			if ("NIC already exisits in the OLA accounts".equals(olaExceptionMessage)){
				super.saveMessage(httpServletRequest, olaExceptionMessage);
			}
				if ("MobileNumUniqueException".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,
		            "MSISDN already exists.");
				}else if("UserNameUniqueException".equals(exceptionMessage)) {
					super.saveMessage(httpServletRequest,
		            "Username already exists.");
				}else if("HeadUniqueException".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,
		            "Franchise/Branch Agent is already defined.");
				}
				if (null != httpServletRequest.getAttribute("UK_USER_DEVICE_TYPE")){
					super.saveMessage(httpServletRequest,(String)
							httpServletRequest.getAttribute("UK_USER_DEVICE_TYPE"));
				}
				
				return super.showForm(httpServletRequest, httpServletResponse, bindException);
		}		
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		try {
			httpServletRequest.setAttribute("isUpdate", "y");
			RetailerContactListViewFormModel model = (RetailerContactListViewFormModel) object;
			if(model.getHead() != null){
				model.setHead(null);
			}else{
				model.setHead(true);
			}
			allpayRetailerAccountManager.updateAccount(httpServletRequest, httpServletResponse, object, bindException);
			httpServletRequest.getSession().setAttribute("messages", new ArrayList());
			super.saveMessage(httpServletRequest,
            "Record updated successfully.");
			Long retailerContactId= model.getRetailerContactId();
			ModelAndView modelAndView = new ModelAndView("redirect:allpayRetailerAccountForm.html?actionId=3&retailerContactId="+retailerContactId);
			return modelAndView;
		} catch (Exception e) {
			//reset the head checkbox
			((RetailerContactListViewFormModel)object).setHead(Boolean.valueOf(httpServletRequest.getParameter("originalHeadValue")));
			
			
			httpServletRequest.getSession().setAttribute("messages", new ArrayList());
			//if (e instanceof  MobileNumUniqueException)
			String exceptionMessage =(String) httpServletRequest.getAttribute("exceptionMessage");	
			String olaExceptionMessage= (String)httpServletRequest.getAttribute("olaExceptionMessage");
			super.saveMessage(httpServletRequest,
            "Record could not be saved.");		
			if ("NIC already exisits in the OLA accounts".equals(olaExceptionMessage)){
				super.saveMessage(httpServletRequest, olaExceptionMessage);
			}
				if ("MobileNumUniqueException".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,
		            "Mobile number already exists.");
				}else if("UserNameUniqueException".equals(exceptionMessage)) {
					super.saveMessage(httpServletRequest,
		            "Username already exists.");
				}else if("HeadUniqueException".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,
		            "Head contact  is already defined.");
				}
				if (null != httpServletRequest.getAttribute("UK_USER_DEVICE_TYPE")){
					super.saveMessage(httpServletRequest,(String)
							httpServletRequest.getAttribute("UK_USER_DEVICE_TYPE"));
				}
				return super.showForm(httpServletRequest, httpServletResponse, bindException);
		}		

	
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

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
