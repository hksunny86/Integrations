package com.inov8.microbank.webapp.action.agenthierarchy;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.NokDetailVOModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.facade.RetailerFacade;
import com.inov8.microbank.server.facade.portal.level3account.Level3AccountFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.microbank.server.facade.portal.usermanagementmodule.UserManagementFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Level3AccountController extends AdvanceAuthorizationFormController
{ 
	private final static Logger log = Logger.getLogger(Level3AccountController.class);
	
	private UserManagementFacade userManagementFacade; 
	private MfsAccountFacade mfsAccountFacade;
	private ReferenceDataManager referenceDataManager;
	private LinkPaymentModeManager linkPaymentModeManager;
	private UserDeviceAccountListViewManager userDeviceAccountListViewManager;
	private CommonCommandManager commonCommandManager;
	private Level3AccountFacade level3AccountFacade;
	private AccountFacade accountFacade;
	private AgentHierarchyManager agentHierarchyManager;
	private RetailerFacade retailerFacade;
	private SmartMoneyAccountManager	smartMoneyAccountManager;
	private EncryptionHandler encryptionHandler;
	private DistributorManager distributorManager;
	
	public Level3AccountController()
	{
		setCommandName("level3AccountModel");
		setCommandClass(Level3AccountModel.class);
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception
	{
		String initAppFormNumber = ServletRequestUtils.getStringParameter(req, "initAppFormNumber");
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit",false);
		Level3AccountModel accountModel = new Level3AccountModel();
		HttpSession session = req.getSession();
		session.removeAttribute("accountModelLevel3");
		
		AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		Level3AccountModel level3AccountModelAuth = null;
		
		if(isReSubmit){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if((actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()) && isReSubmit){
				throw new FrameworkCheckedException("illegal operation performed");
			}
	 	
			XStream xstream = new XStream();
			level3AccountModelAuth = (Level3AccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
		    req.setAttribute("pageMfsId", level3AccountModelAuth.getMfsId());
			
			return level3AccountModelAuth;
		}
		
		if (null == initAppFormNumber || initAppFormNumber.trim().length() == 0)
		{
			//error
		}else{
			BaseWrapper	baseWrapper=new BaseWrapperImpl();
			agentMerchantDetailModel.setInitialAppFormNo(initAppFormNumber);
			baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
			mfsAccountFacade.findAgentMerchantByInitialAppFormNo(baseWrapper);
			
			agentMerchantDetailModel=(AgentMerchantDetailModel) baseWrapper.getBasePersistableModel();
			//
			accountModel.setEmployeeID(agentMerchantDetailModel.getEmpId());
			accountModel.setEmployeeName(agentMerchantDetailModel.getEmpName());
			
			Long[] acTypeId = new Long[]{agentMerchantDetailModel.getAcLevelQualificationId()};
			List<OlaCustomerAccountTypeModel> acTypeModelList = mfsAccountFacade.loadCustomerACTypes(acTypeId);
			
			if(acTypeModelList!=null && acTypeModelList.size()>0)
			{
				OlaCustomerAccountTypeModel acTypeModel = acTypeModelList.get(0);
				accountModel.setAccounttypeName(acTypeModel.getName()); 
			}
			baseWrapper = new BaseWrapperImpl();
			DistributorModel distributorModel = new DistributorModel();
			
			if(agentMerchantDetailModel.getDistributorId()!=null)
	  	    {
				distributorModel.setDistributorId(agentMerchantDetailModel.getDistributorId());
		  	    baseWrapper.setBasePersistableModel(distributorModel);
		  	    baseWrapper = distributorManager.loadDistributor(baseWrapper);
		  	    distributorModel = (DistributorModel) baseWrapper.getBasePersistableModel();
		  	    accountModel.setDistributorName(distributorModel.getName()); 
	  	    }
			retailerContactModel.setInitialAppFormNo(initAppFormNumber);
			searchBaseWrapper.setBasePersistableModel(retailerContactModel);
			retailerFacade.loadRetailerByInitialAppFormNo(searchBaseWrapper);
			
			if (searchBaseWrapper.getCustomList() == null || searchBaseWrapper.getCustomList().getResultsetList().size() == 0)
			{
				accountModel.setInitialAppFormNo(initAppFormNumber);
				accountModel.setBusinessName(agentMerchantDetailModel.getBusinessName());
				accountModel.setAccountOpeningDate(new Date());
				accountModel.setAcOwnershipDetailModelList(new ArrayList<ACOwnershipDetailModel>());
				accountModel.getAcOwnershipDetailModelList().add(new ACOwnershipDetailModel());
				accountModel.setActionId(PortalConstants.ACTION_CREATE);
				accountModel.setUsecaseId(new Long(PortalConstants.CREATE_L3_USECASE_ID));
				return accountModel;
			}
		}
		
		retailerContactModel = (RetailerContactModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
		accountModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		//Account Relationship Information
		accountModel.setInitialAppFormNo(initAppFormNumber);
		accountModel.setAccountPurposeId(retailerContactModel.getAccountPurposeId());
		accountModel.setAcNature(retailerContactModel.getAcNature());
		accountModel.setAcTitle(retailerContactModel.getName());
		accountModel.setCurrencyId(retailerContactModel.getCurrency());
		accountModel.setTaxRegimeId(retailerContactModel.getTaxRegimeId());
		accountModel.setFed(retailerContactModel.getFed());
		accountModel.setBvsEnable(retailerContactModel.getBvsEnable());
		//Business Information
		accountModel.setBusinessName(agentMerchantDetailModel.getBusinessName());
		accountModel.setCommencementDate(retailerContactModel.getCommencementDate());
		accountModel.setIncorporationDate(retailerContactModel.getIncorporationDate());
		accountModel.setSecpRegNo(retailerContactModel.getSecpRegNo());
		accountModel.setSecpRegDate(retailerContactModel.getSecpRegDate());
		accountModel.setNtn(retailerContactModel.getNtn());
		accountModel.setSalesTaxRegNo(retailerContactModel.getSalesTaxRegNo());
		accountModel.setMembershipNoTradeBody(retailerContactModel.getMembershipNoTradeBody());
		accountModel.setTradeBody(retailerContactModel.getTradeBody());
		accountModel.setBusinessTypeId(retailerContactModel.getBusinessTypeId());
		accountModel.setBusinessNatureId(retailerContactModel.getBusinessNatureId());
		accountModel.setLocationTypeId(retailerContactModel.getLocationTypeId());
		accountModel.setLocationSizeId(retailerContactModel.getLocationSizeId());
		accountModel.setEstSince(retailerContactModel.getEstSince());
		accountModel.setLandLineNo(retailerContactModel.getLandLineNo());
		if(retailerContactModel.getGeoLocationIdGeoLocationModel()!=null){
			accountModel.setLatitude(retailerContactModel.getGeoLocationIdGeoLocationModel().getLatitude());
			accountModel.setLongitude(retailerContactModel.getGeoLocationIdGeoLocationModel().getLongitude());
		}
		
		accountModel.setRegStateComments(retailerContactModel.getRegStateComments());
		accountModel.setCustomerAccountTypeId(retailerContactModel.getOlaCustomerAccountTypeModelId());
		accountModel.setCreatedOn(retailerContactModel.getCreatedOn());
		accountModel.setSalary(retailerContactModel.getSalary());
		accountModel.setBuisnessIncome(retailerContactModel.getBusinessIncome());
		accountModel.setOtherIncome(retailerContactModel.getOtherIncome());
		accountModel.setCustomerTypeId(retailerContactModel.getCustomerTypeId());
		accountModel.setFundsSourceId(retailerContactModel.getFundSourceId());
		accountModel.setTransactionModeId(retailerContactModel.getTransactionModeId());
		accountModel.setOtherTransactionMode(retailerContactModel.getOtherTransactionMode());
		accountModel.setAccountReason(retailerContactModel.getAccountReasonId());
		accountModel.setScreeningPerformed(retailerContactModel.isScreeningPerformed());
		accountModel.setComments(retailerContactModel.getNokComments());

		//NOK
		NokDetailVOModel nokDetailVOModel = new NokDetailVOModel();
		nokDetailVOModel.setNokName(retailerContactModel.getNokName());
		nokDetailVOModel.setNokRelationship(retailerContactModel.getNokRelationship());
		nokDetailVOModel.setNokMobile(retailerContactModel.getNokMobile());
		nokDetailVOModel.setNokMobile(retailerContactModel.getNokMobile());
		nokDetailVOModel.setNokIdNumber(retailerContactModel.getNokIdNumber());
		nokDetailVOModel.setNokIdType(retailerContactModel.getNokIdType());
		accountModel.setNokDetailVOModel(nokDetailVOModel);

		AppUserModel appUserModel = new AppUserModel();
		appUserModel = userManagementFacade.getAppUserByRetailer(retailerContactModel.getRetailerContactId());
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		accountModel.setMobileNo(appUserModel.getMobileNo());
		accountModel.setAppUserId(appUserModel.getAppUserId());
		accountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
		accountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
		accountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
		accountModel.setMfsId(appUserModel.getUsername());
		accountModel.setFiler(appUserModel.getFiler());
		if (null != appUserModel.getClosedByAppUserModel())
		{
			accountModel.setClosedBy(appUserModel.getClosedByAppUserModel().getUsername());
		}
		accountModel.setClosedOn(appUserModel.getClosedOn());
		if (null != appUserModel.getSettledByAppUserModel())
		{
			accountModel.setSettledBy(appUserModel.getSettledByAppUserModel().getUsername());
		}
		accountModel.setSettledOn(appUserModel.getSettledOn());
		accountModel.setClosingComments(appUserModel.getClosingComments());
		accountModel.setSettlementComments(appUserModel.getSettlementComments());
		accountModel.setSearchFirstName(ServletRequestUtils.getStringParameter(req, "searchFirstName"));
		accountModel.setSearchLastName(ServletRequestUtils.getStringParameter(req, "searchLastName"));
		accountModel.setSearchMfsId(ServletRequestUtils.getStringParameter(req, "searchMfsId"));
		accountModel.setSearchNic(ServletRequestUtils.getStringParameter(req, "searchNic"));
		
		try
		{
			UserDeviceAccountsModel	userDeviceAccountsModel=	mfsAccountFacade.getDeviceAccountByAppUserId(appUserModel.getAppUserId(), DeviceTypeConstantsInterface.ALL_PAY );
			
			if (!this.linkPaymentModeManager.isFirstAccountOtherThanOla(userDeviceAccountsModel.getUserId())){
				
				SmartMoneyAccountModel smartMoneyAccountModel	=	smartMoneyAccountManager.getSMALinkedWithCoreAccount(retailerContactModel.getRetailerContactId());
				if(smartMoneyAccountModel!=null)
				{
					String accountNumner=level3AccountFacade.getLinkedCoreAccountNo(appUserModel.getUsername());
					accountModel.setCoreAccountNumber(encryptionHandler.decrypt(accountNumner));
					accountModel.setCoreAccountTitle(smartMoneyAccountModel.getName());
					accountModel.setAccountLinked(Boolean.TRUE);
				}
			}
		} catch (Exception ex)
		{
			log.warn("Exception while getting customer info from OLA: " + ex.getStackTrace());
		}

		ApplicantDetailModel applicantModel = new ApplicantDetailModel();
		applicantModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
		accountModel.setApplicant1DetailModel(mfsAccountFacade.loadApplicantDetail(applicantModel));
		
		applicantModel = new ApplicantDetailModel();
		applicantModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
		accountModel.setApplicantDetailModelList(mfsAccountFacade.loadApplicantDetails(applicantModel));

		long index=0;
  
		for(ApplicantDetailModel model : accountModel.getApplicantDetailModelList()){
			model.setIndex(index);
			index++;
		}
		
		// Populating Address Fields
		List<RetailerContactAddressesModel> retailerContactAddresses = findRetailContactAddresses(retailerContactModel);
		if (retailerContactAddresses != null && retailerContactAddresses.size() > 0)
		{
			for (RetailerContactAddressesModel retailerContactAdd : retailerContactAddresses)
			{
				AddressModel addressModel = retailerContactAdd.getAddressIdAddressModel();
				
				if(retailerContactAdd.getApplicantDetailId() == null && retailerContactAdd.getApplicantTypeId()==null)
				{
					//BUSINESS_ADDRESS
					if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.BUSINESS_ADDRESS.longValue()
							&& retailerContactAdd.getApplicantDetailId()==null 
							&& retailerContactAdd.getApplicantTypeId()==null
							&& retailerContactAdd.getRetailerContactId()==retailerContactModel.getRetailerContactId().longValue()){
						
						accountModel.setBusinessAddress(addressModel.getStreetAddress());
						accountModel.setBusinessPostalCode(addressModel.getPostalCode());
						accountModel.setBusinessAddCity(addressModel.getCityId());
					}
					//CORRESSPONDANCE_ADDRESS
					if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.CORRESSPONDANCE_ADDRESS.longValue()
							&& retailerContactAdd.getApplicantDetailId()==null 
							&& retailerContactAdd.getApplicantTypeId()==null
							&& retailerContactAdd.getRetailerContactId()==retailerContactModel.getRetailerContactId().longValue()){
						
						accountModel.setCorresspondanceAddress(addressModel.getStreetAddress());
						accountModel.setCorresspondancePostalCode(addressModel.getPostalCode());
						accountModel.setCorresspondanceAddCity(addressModel.getCityId());
					}
				}
				else if(retailerContactAdd.getApplicantTypeId()==ApplicantTypeConstants.APPLICANT_TYPE_1.longValue()){
					//APPLICANT_TYPE_1 BUSINESS_ADDRESS
					if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.BUSINESS_ADDRESS.longValue()
							&& retailerContactAdd.getApplicantDetailId()==accountModel.getApplicant1DetailModel().getApplicantDetailId().longValue() 
							&& retailerContactAdd.getApplicantTypeId()==ApplicantTypeConstants.APPLICANT_TYPE_1.longValue()
							&& retailerContactAdd.getRetailerContactId()==retailerContactModel.getRetailerContactId().longValue()){
						
						accountModel.getApplicant1DetailModel().setBuisnessAddress(addressModel.getStreetAddress());
						accountModel.getApplicant1DetailModel().setBuisnessCity(addressModel.getCityId());
					}
					//APPLICANT_TYPE_1 PRESENT_HOME_ADDRESS
					if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.PRESENT_HOME_ADDRESS.longValue()
							&& retailerContactAdd.getApplicantDetailId()==accountModel.getApplicant1DetailModel().getApplicantDetailId().longValue() 
							&& retailerContactAdd.getApplicantTypeId()==ApplicantTypeConstants.APPLICANT_TYPE_1.longValue()
							&& retailerContactAdd.getRetailerContactId()==retailerContactModel.getRetailerContactId().longValue()){
						
						accountModel.getApplicant1DetailModel().setResidentialAddress(addressModel.getStreetAddress());
						accountModel.getApplicant1DetailModel().setResidentialCity(addressModel.getCityId());
					}
					//APPLICANT_TYPE_1 NOK_ADDRESS
					if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.NOK_ADDRESS.longValue()
							&& retailerContactAdd.getApplicantDetailId()==accountModel.getApplicant1DetailModel().getApplicantDetailId() .longValue()
							&& retailerContactAdd.getApplicantTypeId()==ApplicantTypeConstants.APPLICANT_TYPE_1.longValue()
							&& retailerContactAdd.getRetailerContactId()==retailerContactModel.getRetailerContactId().longValue()){
						
						accountModel.getNokDetailVOModel().setNokMailingAdd(addressModel.getStreetAddress());
					}
				}
				else if (retailerContactAdd.getApplicantTypeId().longValue() == ApplicantTypeConstants.APPLICANT_TYPE_2)
				{
					if(retailerContactAdd.getAddressTypeId().longValue() == AddressTypeConstants.PRESENT_HOME_ADDRESS)
					{
						if(addressModel.getCityId() != null)
						{
							for(ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList()){
								if(retailerContactAdd.getApplicantDetailId()!= null && applicant2DetailModel.getApplicantDetailId().equals(retailerContactAdd.getApplicantDetailId())){
									applicant2DetailModel.setResidentialCity(addressModel.getCityId());
								}
    				  		}
						}
	    				if(addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty())
	    				{
	    					for(ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList())
	    					{
	    						if(retailerContactAdd.getApplicantDetailId()!= null && applicant2DetailModel.getApplicantDetailId().equals(retailerContactAdd.getApplicantDetailId()))
	    						{
	    							applicant2DetailModel.setResidentialAddress(addressModel.getStreetAddress()); 
	    						}
	    					}
	    				}
					}
    				else if(retailerContactAdd.getAddressTypeId().longValue() == AddressTypeConstants.BUSINESS_ADDRESS)
    				{
    					if(addressModel.getCityId() != null)
    					{
    						for(ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList())
    						{
    							if(retailerContactAdd.getApplicantDetailId()!= null && applicant2DetailModel.getApplicantDetailId().equals(retailerContactAdd.getApplicantDetailId()))
    							{
    							  applicant2DetailModel.setBuisnessCity(addressModel.getCityId());
    							}
    						}
    					}
    					if(addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty())
    					{
    						for(ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList())
    						{
    							if(retailerContactAdd.getApplicantDetailId()!= null && applicant2DetailModel.getApplicantDetailId().equals(retailerContactAdd.getApplicantDetailId())){
    								{
    									applicant2DetailModel.setBuisnessAddress(addressModel.getStreetAddress());
    								}
    							}
    						}
    					}
    				}
				}
			}
		}
		req.setAttribute("pageMfsId", (String) baseWrapper.getObject("userId"));

		
		SearchBaseWrapper	searchWrapper = new SearchBaseWrapperImpl();
		ACOwnershipDetailModel queryAccountOwnerShipDetailModel = new ACOwnershipDetailModel();
		queryAccountOwnerShipDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		queryAccountOwnerShipDetailModel.setIsDeleted(Boolean.FALSE);
		searchWrapper.setBasePersistableModel(queryAccountOwnerShipDetailModel);
		accountModel.setAcOwnershipDetailModelList(mfsAccountFacade.loadL3AccountOwnerShipDetails(searchWrapper));

		if( CollectionUtils.isEmpty(accountModel.getAcOwnershipDetailModelList() ))
		{
    		accountModel.getAcOwnershipDetailModelList().add(new ACOwnershipDetailModel());
		}
		
		accountModel.setVerisysDone(retailerContactModel.getVerisysDone());
		accountModel.setRegStateComments(retailerContactModel.getRegStateComments());
		accountModel.setAccountOpeningDate(retailerContactModel.getAccountOpeningDate());
		accountModel.setActionId(PortalConstants.ACTION_UPDATE);
		accountModel.setUsecaseId(new Long(PortalConstants.UPDATE_L3_USECASE_ID));
		return accountModel;
	}

	private List<ApplicantDetailModel> updateApplicantDetailList(List<ApplicantDetailModel> list, int index, ApplicantDetailModel model)
	{
		list.remove(index);
		list.add(model);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception
	{
		Map referenceDataMap = new HashMap();

		CustomerTypeModel customerTypeModel = new CustomerTypeModel();
		ReferenceDataWrapper customerTypeDataWrapper = new ReferenceDataWrapperImpl(customerTypeModel, "name", SortingOrder.ASC);
		customerTypeDataWrapper.setBasePersistableModel(customerTypeModel);
		try
		{
			referenceDataManager.getReferenceData(customerTypeDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<CustomerTypeModel> customerTypeList = null;
		if (customerTypeDataWrapper.getReferenceDataList() != null)
		{
			customerTypeList = customerTypeDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("customerTypeList", customerTypeList);

		TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
		ReferenceDataWrapper taxRegimeDataWrapper = new ReferenceDataWrapperImpl(taxRegimeModel, "taxRegimeId", SortingOrder.ASC);
		taxRegimeDataWrapper.setBasePersistableModel(taxRegimeModel);
		try
		{
			referenceDataManager.getReferenceData(taxRegimeDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<TaxRegimeModel> taxRegimeList = null;
		if (taxRegimeDataWrapper.getReferenceDataList() != null)
		{
			taxRegimeList = taxRegimeDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("taxRegimeList", taxRegimeList);

		CityModel cityModel = new CityModel();
		ReferenceDataWrapper cityDataWrapper = new ReferenceDataWrapperImpl(cityModel, "cityId", SortingOrder.ASC);
		cityDataWrapper.setBasePersistableModel(cityModel);
		try
		{
			referenceDataManager.getReferenceData(cityDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<CityModel> cityList = null;
		if (cityDataWrapper.getReferenceDataList() != null)
		{
			cityList = cityDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("cityList", cityList);

		AccountNature accountType = new AccountNature();
		ReferenceDataWrapper accountNatureDataWrapper = new ReferenceDataWrapperImpl(accountType, "accountNatureId", SortingOrder.ASC);
		accountNatureDataWrapper.setBasePersistableModel(accountType);
		try
		{
			referenceDataManager.getReferenceData(accountNatureDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<AccountNature> accountTypeList = null;
		if (accountNatureDataWrapper.getReferenceDataList() != null)
		{
			accountTypeList = accountNatureDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("accountTypeList", accountTypeList);

		List<OlaCustomerAccountTypeModel> customerAccountTypeList = accountFacade.searchParentOlaCustomerAccountTypes(
				CustomerAccountTypeConstants.SETTLEMENT, CustomerAccountTypeConstants.WALK_IN_CUSTOMER);

		HttpSession session = req.getSession();
		session.removeAttribute("accountModelLevel3");
		referenceDataMap.put("customerAccountTypeList", customerAccountTypeList);

		List<LabelValueBean> accountNatureList = new ArrayList<LabelValueBean>();
		LabelValueBean accountNature = new LabelValueBean("Single", "S");
		accountNatureList.add(accountNature);
		accountNature = new LabelValueBean("Joint", "J");
		accountNatureList.add(accountNature);
		referenceDataMap.put("accountNatureList", accountNatureList);

		List<LabelValueBean> genderList = new ArrayList<LabelValueBean>();
		LabelValueBean gender = new LabelValueBean("Male", "M");
		genderList.add(gender);
		gender = new LabelValueBean("Female", "F");
		genderList.add(gender);
		gender = new LabelValueBean("Khwaja Sira", "K");
		genderList.add(gender);
		referenceDataMap.put("genderList", genderList);

	    List<LabelValueBean> binaryOpt = new ArrayList<LabelValueBean>();
	    LabelValueBean opt = new LabelValueBean("Yes", "1");
	    binaryOpt.add(opt);
	    opt = new LabelValueBean("No", "0");
	    binaryOpt.add(opt);
	    referenceDataMap.put("binaryOpt", binaryOpt);
		
		List<LabelValueBean> mailingAddTypeList = new ArrayList<LabelValueBean>();
		LabelValueBean mailingAddType = new LabelValueBean("Residence", "1");
		mailingAddTypeList.add(mailingAddType);
		mailingAddType = new LabelValueBean("Office/Business", "0");
		mailingAddTypeList.add(mailingAddType);
		referenceDataMap.put("mailingAddTypeList", mailingAddTypeList);

		List<LabelValueBean> residanceStatusList = new ArrayList<LabelValueBean>();
		LabelValueBean residanceStatus = new LabelValueBean("Resident", "1");
		residanceStatusList.add(residanceStatus);
		residanceStatus = new LabelValueBean("Non Resident", "0");
		residanceStatusList.add(residanceStatus);
		referenceDataMap.put("residanceStatusList", residanceStatusList);

		Long[] regStateList = { RegistrationStateConstantsInterface.DECLINE, RegistrationStateConstantsInterface.DISCREPANT,
				RegistrationStateConstantsInterface.VERIFIED };
		CustomList<RegistrationStateModel> regStates = commonCommandManager.getRegistrationStateByIds(regStateList);
		referenceDataMap.put("regStateList", regStates.getResultsetList());

		CountryModel countryModel = new CountryModel();
		ReferenceDataWrapper countryReferenceDataWrapper = new ReferenceDataWrapperImpl(countryModel, "name", SortingOrder.ASC);
		countryReferenceDataWrapper.setBasePersistableModel(countryModel);
		try
		{
			referenceDataManager.getReferenceData(countryReferenceDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}

		TransactionModeModel transactionModeModel = new TransactionModeModel();
		ReferenceDataWrapper transactionModeReferenceDataWrapper = new ReferenceDataWrapperImpl(transactionModeModel, "transactionModeId",
				SortingOrder.ASC);
		transactionModeReferenceDataWrapper.setBasePersistableModel(transactionModeModel);
		try
		{
			referenceDataManager.getReferenceData(transactionModeReferenceDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}

		List<TransactionModeModel> transactionModeList = null;
		if (transactionModeReferenceDataWrapper.getReferenceDataList() != null)
		{
			transactionModeList = transactionModeReferenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("transactionModeList", transactionModeList);

		BusinessNatureModel businessNatureModel = new BusinessNatureModel();
		ReferenceDataWrapper businessNatureDataWrapper = new ReferenceDataWrapperImpl(businessNatureModel, "businessNatureId", SortingOrder.ASC);
		businessNatureDataWrapper.setBasePersistableModel(businessNatureModel);
		try
		{
			referenceDataManager.getReferenceData(businessNatureDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<BusinessNatureModel> businessNatureList = null;
		if (businessNatureDataWrapper.getReferenceDataList() != null)
		{
			businessNatureList = businessNatureDataWrapper.getReferenceDataList();
		}
		
		Map<String,List<BusinessNatureModel>> businessNatureMap = new HashMap<String,List<BusinessNatureModel>>();
		
		List<BusinessNatureModel> modellist = null;
		for(BusinessNatureModel bnModel : businessNatureList){
			modellist = businessNatureMap.get(bnModel.getMerchantGroup());
			if(modellist != null){
				modellist.add(bnModel);
			}else{
				modellist = new ArrayList<BusinessNatureModel>();
				modellist.add(bnModel);
			}
			
			businessNatureMap.put(bnModel.getMerchantGroup(),modellist);
		}
		
		//Sorting the keys
		Map<String,List<BusinessNatureModel>> treeMap = new TreeMap<String,List<BusinessNatureModel>>(businessNatureMap);
		
		referenceDataMap.put("businessNatureList", treeMap);
		
		LocationTypeModel locationTypeModel = new LocationTypeModel();
		ReferenceDataWrapper locationTypeDataWrapper = new ReferenceDataWrapperImpl(locationTypeModel, "locationTypeId", SortingOrder.ASC);
		locationTypeDataWrapper.setBasePersistableModel(locationTypeModel);
		try
		{
			referenceDataManager.getReferenceData(locationTypeDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<LocationTypeModel> locationTypeList = null;
		if (locationTypeDataWrapper.getReferenceDataList() != null)
		{
			locationTypeList = locationTypeDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("locationTypeList", locationTypeList);

		ReferenceDataWrapper locationSizeDataWrapper = new ReferenceDataWrapperImpl(new LocationSizeModel(), "locationSizeId", SortingOrder.ASC);
		try
		{
			referenceDataManager.getReferenceData(locationSizeDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<LocationSizeModel> locationSizeList = null;
		if (locationSizeDataWrapper.getReferenceDataList() != null)
		{
			locationSizeList = locationSizeDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("locationSizeList", locationSizeList);

		AccountPurposeModel accountPurposeModel = new AccountPurposeModel();
		ReferenceDataWrapper accountPurposeReferenceDataWrapper = new ReferenceDataWrapperImpl(accountPurposeModel, "name", SortingOrder.ASC);
		accountPurposeReferenceDataWrapper.setBasePersistableModel(accountPurposeModel);
		try
		{
			referenceDataManager.getReferenceData(accountPurposeReferenceDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}

		List<AccountPurposeModel> accountPurposeList = null;
		if (accountPurposeReferenceDataWrapper.getReferenceDataList() != null)
		{
			accountPurposeList = accountPurposeReferenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("accountPurposeList", accountPurposeList);

		BusinessTypeModel businessTypeModel = new BusinessTypeModel();
		ReferenceDataWrapper businessTypeReferenceDataWrapper = new ReferenceDataWrapperImpl(businessTypeModel, "businessTypeId", SortingOrder.ASC);
		businessTypeReferenceDataWrapper.setBasePersistableModel(businessTypeModel);
		try
		{
			referenceDataManager.getReferenceData(businessTypeReferenceDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}

		List<BusinessTypeModel> corporateBusinessTypeList = null;
		List<BusinessTypeModel> individualBusinessTypeList = null;

		if (businessTypeReferenceDataWrapper.getReferenceDataList() != null)
		{
			corporateBusinessTypeList = businessTypeReferenceDataWrapper.getReferenceDataList();

			individualBusinessTypeList = new ArrayList<BusinessTypeModel>();
			Iterator<BusinessTypeModel> businessTypeIterator = corporateBusinessTypeList.iterator();

			while (businessTypeIterator.hasNext())
			{

				businessTypeModel = businessTypeIterator.next();
				if (businessTypeModel.getCategory().equalsIgnoreCase(BusinessTypeCategoryConstants.INDIVIDUAL))
				{
					individualBusinessTypeList.add(businessTypeModel);
					businessTypeIterator.remove();
				}
			}
		}
		referenceDataMap.put("corporateBusinessTypeList", corporateBusinessTypeList);
		referenceDataMap.put("individualBusinessTypeList", individualBusinessTypeList);

		OccupationModel occupationModel = new OccupationModel();
		ReferenceDataWrapper occupationReferenceDataWrapper = new ReferenceDataWrapperImpl(occupationModel, "name", SortingOrder.ASC);
		occupationReferenceDataWrapper.setBasePersistableModel(occupationModel);
		try
		{
			referenceDataManager.getReferenceData(occupationReferenceDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}

		List<OccupationModel> occupationList = null;
		if (occupationReferenceDataWrapper.getReferenceDataList() != null)
		{
			occupationList = occupationReferenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("occupationList", occupationList);

		ProfessionModel professionModel = new ProfessionModel();
		ReferenceDataWrapper professionReferenceDataWrapper = new ReferenceDataWrapperImpl(professionModel, "professionId", SortingOrder.ASC);
		professionReferenceDataWrapper.setBasePersistableModel(professionModel);
		try
		{
			referenceDataManager.getReferenceData(professionReferenceDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}

		List<ProfessionModel> professionList = null;
		if (professionReferenceDataWrapper.getReferenceDataList() != null)
		{
			professionList = professionReferenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("professionList", professionList);

		List<LabelValueBean> labelValueBeanList = new ArrayList<LabelValueBean>();
		LabelValueBean labelValueBean = new LabelValueBean();
		labelValueBean.setLabel("Business");
		labelValueBean.setValue("0");
		labelValueBeanList.add(labelValueBean);

		labelValueBean = new LabelValueBean();
		labelValueBean.setLabel("Residential");
		labelValueBean.setValue("1");
		labelValueBeanList.add(labelValueBean);
		referenceDataMap.put("mailingAddressTypeList", labelValueBeanList);

		Calendar cal = Calendar.getInstance();
		int currentYear = cal.get(Calendar.YEAR);
		currentYear	=	currentYear	+	5;
		List<LabelValueBean> yearList = new ArrayList<LabelValueBean>();
		for (Integer startYear = 1900; startYear <= currentYear; startYear++)
		{
			LabelValueBean year = new LabelValueBean(startYear.toString(), startYear.toString());
			yearList.add(year);
		}
		referenceDataMap.put("yearList", yearList);

		List<LabelValueBean> titleList = new ArrayList<LabelValueBean>();
		for (TitleEnum value : TitleEnum.values())
		{
			LabelValueBean title = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
			titleList.add(title);
		}
		referenceDataMap.put("titleList", titleList);

		List<LabelValueBean> idTypeList = new ArrayList<>();
		for (IDDocumentTypeEnum value : IDDocumentTypeEnum.values())
		{
			LabelValueBean idType = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
			idTypeList.add(idType);
		}
		referenceDataMap.put("idTypeList", idTypeList);

		List<LabelValueBean> maritalStatusList = new ArrayList<LabelValueBean>();
		for (MaritalStatusEnum value : MaritalStatusEnum.values())
		{
			LabelValueBean maritalStatus = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
			maritalStatusList.add(maritalStatus);
		}
		referenceDataMap.put("maritalStatusList", maritalStatusList);

		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(new CurrencyCodeModel(), "name", SortingOrder.ASC);
		try
		{
			referenceDataWrapper = referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		referenceDataMap.put("currencyList", referenceDataWrapper.getReferenceDataList());

		referenceDataWrapper = new ReferenceDataWrapperImpl(new BusinessTypeModel(), "name", SortingOrder.ASC);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		
		List<BusinessTypeModel> businessTypeList = referenceDataWrapper.getReferenceDataList();
		
		Map<String,List<BusinessTypeModel>> businessTypeMap = new HashMap<String,List<BusinessTypeModel>>();
		
		List<BusinessTypeModel> businessTypeModelList = null;
		for(BusinessTypeModel btModel : businessTypeList){
			businessTypeModelList = businessTypeMap.get(btModel.getCategory());
			if(businessTypeModelList != null){
				businessTypeModelList.add(btModel);
			}else{
				businessTypeModelList = new ArrayList<BusinessTypeModel>();
				businessTypeModelList.add(btModel);
			}
			
			businessTypeMap.put(btModel.getCategory(),businessTypeModelList);
		}
		
		//Sorting the keys
		Map<String,List<BusinessTypeModel>> businessTypeTreeMap = new TreeMap<String,List<BusinessTypeModel>>(businessTypeMap);
		

		referenceDataMap.put("businessTypeList", businessTypeTreeMap);

		
		List<LabelValueBean> ownerShipTypeList = new ArrayList<LabelValueBean>();
	    LabelValueBean ownerShipType = new LabelValueBean("Partner", "1");
	    ownerShipTypeList.add(ownerShipType);
	    ownerShipType = new LabelValueBean("Director", "2");
	    ownerShipTypeList.add(ownerShipType);
	    ownerShipType = new LabelValueBean("Guardian", "3");
	    ownerShipTypeList.add(ownerShipType);
	    ownerShipType = new LabelValueBean("Shareholder", "4");
	    ownerShipTypeList.add(ownerShipType);
	    ownerShipType = new LabelValueBean("Signatory", "5");
	    ownerShipTypeList.add(ownerShipType);
	    ownerShipType = new LabelValueBean("Member", "6");
	    ownerShipTypeList.add(ownerShipType);
	    referenceDataMap.put("ownerShipTypeList", ownerShipTypeList);
	    
	    List<LabelValueBean> documentTypeList = new ArrayList<LabelValueBean>();
	    LabelValueBean documentType = new LabelValueBean("CNIC", "1");
	    documentTypeList.add(documentType);
	    documentType = new LabelValueBean("NICOP", "2");
	    documentTypeList.add(documentType);
	    documentType = new LabelValueBean("Passport", "3");
	    documentTypeList.add(documentType);
	    documentType = new LabelValueBean("NARA", "4");
	    documentTypeList.add(documentType);
	    documentType = new LabelValueBean("POC", "5");
	    documentTypeList.add(documentType);
	    documentType = new LabelValueBean("SNIC", "6");
	    documentTypeList.add(documentType);
	    referenceDataMap.put("documentTypeList", documentTypeList);
	    
	    List<LabelValueBean> screeningList = new ArrayList<LabelValueBean>();
	    LabelValueBean screening = new LabelValueBean("Match", "1");
	    screeningList.add(screening);
	    screening = new LabelValueBean("Not Match", "0");
	    screeningList.add(screening);
	    referenceDataMap.put("screeningList", screeningList);

	    List<LabelValueBean> nadraVerList = new ArrayList<LabelValueBean>();
	    LabelValueBean nadraVer = new LabelValueBean("Positive", "1");
	    nadraVerList.add(nadraVer);
	    nadraVer = new LabelValueBean("Negative", "0");
	    nadraVerList.add(nadraVer);
	    referenceDataMap.put("nadraVerList", nadraVerList);
	    
	    List<LabelValueBean> filerList = new ArrayList<LabelValueBean>();
	    LabelValueBean filer = new LabelValueBean("Filer", "1");
	    filerList.add(filer);
	    filer = new LabelValueBean("Non Filer", "0");
	    filerList.add(filer);
	    referenceDataMap.put("filerList", filerList);
	    
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		String mfsAccountId = "";
		HttpSession session = req.getSession();
		Level3AccountModel accountModel = (Level3AccountModel) obj;
		Level3AccountModel sessionAccountModel = (Level3AccountModel) session.getAttribute("accountModelLevel3");
				
		try
		{
			if (sessionAccountModel != null)
			{
				accountModel.setApplicantDetailModelList(sessionAccountModel.getApplicantDetailModelList());
			}
			if (!this.agentHierarchyManager.isMobileNumUnique(accountModel.getApplicant1DetailModel().getMobileNo(), accountModel.getAppUserId()))
			{
				this.saveMessage(req, "Applicant1 Mobile No already exists.");
				Map<String, Object> modelMap = new HashMap<>(2);
				modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
				return super.showForm(req, res, errors,modelMap); 
			}
			
			//Commented By Hassan on 4/12/15 - To Allow an Applicant in other account to become retailer.
			
			/*this.level3AccountFacade.isIdDocumentNumberAlreadyExist(accountModel.getInitialAppFormNo(),
					accountModel.getApplicant1DetailModel().getIdType()
					, accountModel.getApplicant1DetailModel().getIdNumber());*/
			
			/*Commented By Hassan on 4/12/15 - To Allow Applicant to be NOK in other account.*/
			
			/*this.level3AccountFacade.isIdDocumentNumberAlreadyExist(accountModel.getInitialAppFormNo(),
					accountModel.getNokDetailVOModel().getNokIdType(), accountModel.getNokDetailVOModel().getNokIdNumber());*/
			
			if(!validateApplicantUniqueInfo(accountModel, req, res, errors))
			{
				this.saveMessage(req, "Detail of applicants cannot be same (ID Document Number, Mobile Number)");
				Map<String, Object> modelMap = new HashMap<>(2);
				modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
				return super.showForm(req, res, errors,modelMap); 
			}
			
			//****************************************************************************************************************************
			//									Check if cnic is blacklisted
			//****************************************************************************************************************************
			if(accountModel.getApplicant1DetailModel()!= null && accountModel.getApplicant1DetailModel().getIdNumber() != null && this.commonCommandManager.isCnicBlacklisted(accountModel.getApplicant1DetailModel().getIdNumber()))
			{
				this.saveMessage(req, "ID Document Number is Black Listed");
				Map<String, Object> modelMap = new HashMap<>(2);
				modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
				return super.showForm(req, res, errors,modelMap); 
			}
			//****************************************************************************************************************************
			
			accountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
			accountModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
			accountModel.setCurrencyId(1L);
			baseWrapper.putObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY, accountModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, accountModel.getActionId());
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, accountModel.getUsecaseId());
			baseWrapper = this.level3AccountFacade.createLevel3Account(baseWrapper);
			mfsAccountId = accountModel.getMfsId();
			accountModel=(Level3AccountModel) baseWrapper.getObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY);

		} catch (FrameworkCheckedException exception)
		{
			String msg = exception.getMessage();
			if ("MobileNumUniqueException".equals(msg))
			{
				this.saveMessage(req, super.getText("newMfsAccount.mobileNumNotUnique", req.getLocale()));
			} 
			else if ("Duplicate_Applicant_Nok_Id".equals(msg))
			{
				this.saveMessage(req, super.getText("Applicant 1 NOK ID Document Number already exist.", req.getLocale()));
			} 
			else if ("Duplicate_Applicant_Id".equals(msg))
			{
				this.saveMessage(req, super.getText("Applicant 1 ID Document Number already exist.", req.getLocale()));
			}
			else if ("NICUniqueException".equals(msg))
			{
				this.saveMessage(req, super.getText("newMfsAccount.nicNotUnique", req.getLocale()));
			}
			else if("GenPinServiceDown".equals(msg)){
				this.saveMessage(req, "Generate pin service is not available. Please try again later.");
			}
			else
			{
				this.saveMessage(req, super.getText("6075", req.getLocale()));
			}
			
			accountModel.setRetailerContactId(null);
			accountModel.setAppUserId(null);
			accountModel.getApplicant1DetailModel().setApplicantDetailId(null);
			
			Map<String, Object> modelMap = new HashMap<>(2);
			modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());

			return super.showForm(req, res, errors,modelMap);
		}
		catch (Exception exception)
		{	
			accountModel.setRetailerContactId(null);
			accountModel.setAppUserId(null);
			accountModel.getApplicant1DetailModel().setApplicantDetailId(null);
			
			Map<String, Object> modelMap = new HashMap<>(2);
			modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());

			return super.showForm(req, res, errors,modelMap);
		}

		this.saveMessage(req, super.getText("level3Account.recordSaveSuccessful", new Object[] { mfsAccountId }, req.getLocale()));

		Map<String, Object> modelMap = new HashMap<>(2);
		modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
		ModelAndView modelAndView = new ModelAndView("redirect:p_l3accountopeningform.html", modelMap);
		session.removeAttribute("accountModelLevel3");
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		String mfsAccountId = "";
		Long appUserId = null;
		HttpSession session = req.getSession();

		Level3AccountModel accountModel = (Level3AccountModel) obj;
		Level3AccountModel sessionAccountModel = (Level3AccountModel) session.getAttribute("accountModelLevel3");
		try
		{
			if (sessionAccountModel != null)
			{
				accountModel.setApplicantDetailModelList(sessionAccountModel.getApplicantDetailModelList());
			}

			if(!onUpdateValidation(sessionAccountModel, req))
			{
				Map<String, Object> modelMap = new HashMap<>(2);
				modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
				accountModel.setRegistrationStateId(getUpdatedRegisterationState(accountModel.getRetailerContactId()));
				return super.showForm(req, res, errors,modelMap); 
			}
			
			//Commented By Hassan on 4/12/15 - To Allow an Applicant in other account to become retailer.
			//Commented By Hassan on 4/12/15 - To Allow Applicant to be NOK in other account.
			
			/*this.level3AccountFacade.isIdDocumentNumberAlreadyExist(accountModel.getInitialAppFormNo(),
					accountModel.getApplicant1DetailModel().getIdType()
					, accountModel.getApplicant1DetailModel().getIdNumber());
			
			this.level3AccountFacade.isIdDocumentNumberAlreadyExist(accountModel.getInitialAppFormNo(),
					accountModel.getNokDetailVOModel().getNokIdType(), accountModel.getNokDetailVOModel().getNokIdNumber());*/
			
			if(!validateApplicantUniqueInfo(sessionAccountModel, req, res, errors)){
				this.saveMessage(req, "Detail of applicants cannot be same (ID Document Number, Mobile Number)");
				Map<String, Object> modelMap = new HashMap<>(2);
				modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
				accountModel.setRegistrationStateId(getUpdatedRegisterationState(accountModel.getRetailerContactId()));
				return super.showForm(req, res, errors,modelMap); 
			}
			
			SearchBaseWrapper searchWrapper = new SearchBaseWrapperImpl();
			ACOwnershipDetailModel acOwnerShipDetailModel = new ACOwnershipDetailModel();
			acOwnerShipDetailModel.setIsDeleted(false);
			acOwnerShipDetailModel.setRetailerContactId(sessionAccountModel.getRetailerContactId());
			searchWrapper.setBasePersistableModel(acOwnerShipDetailModel);
			List<ACOwnershipDetailModel> existingOwners = mfsAccountFacade.loadL3AccountOwnerShipDetails(searchWrapper);
			
			baseWrapper.putObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY, accountModel);
			baseWrapper.putObject("existingOwners", (Serializable) existingOwners);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, accountModel.getActionId());
			baseWrapper = this.level3AccountFacade.updateLevel3Account(baseWrapper);
			appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
			mfsAccountId = accountModel.getMfsId();
		} 
		catch (FrameworkCheckedException exception)
		{
			String msg = exception.getMessage();
			if ("MobileNumUniqueException".equals(msg))
			{
				this.saveMessage(req, super.getText("newMfsAccount.mobileNumNotUnique", req.getLocale()));
			} 
			else if ("Duplicate_Applicant_Nok_Id".equals(msg))
			{
				this.saveMessage(req, "Applicant 1 NOK ID Document Number already exist.");
			} 
			else if ("Duplicate_Applicant_Id".equals(msg))
			{
				this.saveMessage(req, "Applicant 1 ID Document Number already exist.");
			}
			else if ("NICUniqueException".equals(msg))
			{
				this.saveMessage(req, super.getText("newMfsAccount.nicNotUnique", req.getLocale()));
			}
			else if ("AccountNumberAlreadyLinked".equals(msg))
			{
				this.saveMessage(req, "The Account Number is already Linked. Kindly Select Another Account Number and Try Again.");
			}
			else
			{
				this.saveMessage(req, super.getText("6075", req.getLocale()));
			}

			accountModel.setRegistrationStateId(getUpdatedRegisterationState(accountModel.getRetailerContactId()));
			
			Map<String, Object> modelMap = new HashMap<>(2);
			modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());

			return super.showForm(req, res, errors,modelMap);
		}
		catch (Exception exception)
		{
			this.saveMessage(req, super.getText("6075", req.getLocale()));
			accountModel.setRegistrationStateId(getUpdatedRegisterationState(accountModel.getRetailerContactId()));
			
			Map<String, Object> modelMap = new HashMap<>(2);
			modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());

			return super.showForm(req, res, errors,modelMap);
		}

		this.saveMessage(req, super.getText("level3Account.recordUpdatedSuccessful", new Object[] { mfsAccountId }, req.getLocale()));

		Map<String, Object> modelMap = new HashMap<>(2);
		modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
		ModelAndView modelAndView = new ModelAndView("redirect:p_l3accountopeningform.html", modelMap);
		session.removeAttribute("accountModelLevel3");
		return modelAndView;
	}
	
	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		HttpSession session = request.getSession();
		Level3AccountModel accountModel = (Level3AccountModel) command;
		accountModel.setScreeningPerformed(false);
		
		Level3AccountModel sessionAccountModel = (Level3AccountModel) session.getAttribute("accountModelLevel3");
		if(sessionAccountModel!= null){
			accountModel.setApplicantDetailModelList(sessionAccountModel.getApplicantDetailModelList());
		}
		ModelAndView modelAndView=null;
		AppUserModel appUserModel = null;

		Long appUserId=null;
		String appUserIdStr = ServletRequestUtils.getStringParameter(request, "appUserId"); 
		String mfsAccountId = "";
		Long customerAccountTypeId=null;
		
		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(request, "resubmitRequest",false);
		Long actionAuthorizationId = null;
		if(resubmitRequest)
			actionAuthorizationId=ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");

		
		if(!validateApplicantUniqueInfo(accountModel, request, response, errors))
		{
			this.saveMessage(request, "Detail of applicants cannot be same (ID Document Number, Mobile Number)");
			Map<String, Object> modelMap = new HashMap<>(2);
			modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());

			if(accountModel.getUsecaseId().longValue()!= PortalConstants.CREATE_L3_USECASE_ID) {
				accountModel.setRegistrationStateId(getUpdatedRegisterationState(accountModel.getRetailerContactId()));
			}
			return super.showForm(request, response, errors,modelMap);
		}
		//***************************************************************************************************************************
		//									Check if cnic is blacklisted
		//***************************************************************************************************************************
		       
		if(accountModel.getApplicant1DetailModel()!= null && accountModel.getApplicant1DetailModel().getIdNumber() != null && this.commonCommandManager.isCnicBlacklisted(accountModel.getApplicant1DetailModel().getIdNumber()))
		{
			this.saveMessage(request, "ID Document Number is Black Listed");
			Map<String, Object> modelMap = new HashMap<>(2);
			modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
			return super.showForm(request, response, errors,modelMap); 
		}
		
		//***************************************************************************************************************************
		
		if(accountModel.getUsecaseId().longValue()== PortalConstants.CREATE_L3_USECASE_ID)
		{
			accountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
			accountModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
			accountModel.setCreatedOn(new Date());
		}
		else
		{
			if(!onUpdateValidation(sessionAccountModel, request)){
				Map<String, Object> modelMap = new HashMap<>(2);
				modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
				return super.showForm(request, response, errors,modelMap); 
			}
			
			if(null!=appUserIdStr  && appUserIdStr.trim().length() > 0){
				if (!resubmitRequest)
					appUserId = accountModel.getAppUserId();
				else
					appUserId = Long.parseLong(appUserIdStr) ;
			}
			
			if(null!=accountModel.getCoreAccountNumber()){
				if(level3AccountFacade.isCoreAccountLinkedToOtherAgent(encryptionHandler.encrypt(accountModel.getCoreAccountNumber()), 
						accountModel.getRetailerContactId())){
					this.saveMessage(request, "Account number is already linked to other account.");
					Map<String, Object> modelMap = new HashMap<>(2);
					modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
					accountModel.setRegistrationStateId(getUpdatedRegisterationState(accountModel.getRetailerContactId()));
					return super.showForm(request, response, errors,modelMap); 
				}
			}
			
			customerAccountTypeId = ServletRequestUtils.getLongParameter(request, "customerAccountTypeId");
			appUserModel = new AppUserModel();
			appUserModel.setAppUserId(appUserId);
			baseWrapper.setBasePersistableModel(appUserModel);
			appUserModel = (AppUserModel)baseWrapper.getBasePersistableModel();
			if(appUserModel.getNic() == null){
			   	request.setAttribute("nicNullInDB", "true");
			}
			
			accountModel.setAppUserId(appUserId);
			accountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
			accountModel.setCustomerAccountTypeId(customerAccountTypeId);
			accountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
		}
		
		try
		{								
			XStream xstream = new XStream();
			
			Level3AccountModel level3AccountModelAuth = (Level3AccountModel) accountModel.clone();
		
			for (ApplicantDetailModel iterable_element : level3AccountModelAuth.getApplicantDetailModelList())
			{
				iterable_element.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				iterable_element.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				iterable_element.setRetailerContactId(level3AccountModelAuth.getRetailerContactId());
			}
			
			String refDataModelString= xstream.toXML(level3AccountModelAuth);
			String oldRefDataModelString = null;
		
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(accountModel.getUsecaseId());
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(accountModel.getUsecaseId(),new Long(0));
			
			if(accountModel.getUsecaseId().longValue()!= PortalConstants.CREATE_L3_USECASE_ID)
			{
				Level3AccountModel oldModel = populateCurrentInfoModel(level3AccountModelAuth);
		    	oldRefDataModelString = xstream.toXML(oldModel); 
			}
			
			
			if(nextAuthorizationLevel.intValue()<1){
				
				AppUserModel aum = new AppUserModel();
				if(usecaseModel.getUsecaseId().longValue()== PortalConstants.CREATE_L3_USECASE_ID && accountModel.getApplicant1DetailModel() != null){
					aum.setMobileNo(accountModel.getApplicant1DetailModel().getMobileNo());
					aum.setNic(accountModel.getApplicant1DetailModel().getIdNumber());
					this.mfsAccountFacade.isUniqueCNICMobile(aum,baseWrapper);
				}
				
				baseWrapper.putObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY, accountModel);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, accountModel.getActionId());
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, accountModel.getUsecaseId());
				
			    if(accountModel.getUsecaseId().longValue()== PortalConstants.CREATE_L3_USECASE_ID){
			    	baseWrapper = this.level3AccountFacade.createLevel3Account(baseWrapper);
			    	accountModel=(Level3AccountModel) baseWrapper.getObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY);
			    	mfsAccountId = accountModel.getMfsId();
					appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
			    }
			    else
			    {
			    	baseWrapper = this.level3AccountFacade.updateLevel3Account(baseWrapper);    	 
			    }
			    				
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString,oldRefDataModelString,usecaseModel,actionAuthorizationId,request);
				
				if(accountModel.getUsecaseId().longValue()== PortalConstants.UPDATE_L3_USECASE_ID){
					
					this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale() )
							+"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
					modelAndView = new ModelAndView(new RedirectView("p_l3accountsmanagement.html") );
				}
				else
				{
					this.saveMessage(request, super.getText("newMfsAccount.recordSaveSuccessful",new Object[]{mfsAccountId} ,request.getLocale() )
							+"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);

					Map<String, Object> modelMap = new HashMap<>(2);
					modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					modelMap.put("initAppFormNumber", accountModel.getInitialAppFormNo());
					modelAndView = new ModelAndView("redirect:p_l3accountopeningform.html", modelMap);
					session.removeAttribute("accountModelLevel3");
					return modelAndView;
				}
			}
			else
			{			
				AppUserModel aum = new AppUserModel();
				if(usecaseModel.getUsecaseId().longValue()== PortalConstants.CREATE_L3_USECASE_ID && accountModel.getApplicant1DetailModel() != null){
					aum.setMobileNo(accountModel.getApplicant1DetailModel().getMobileNo());
					aum.setNic(accountModel.getApplicant1DetailModel().getIdNumber());
					this.mfsAccountFacade.isUniqueCNICMobile(aum,baseWrapper);
				}

				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,"", refDataModelString,oldRefDataModelString,usecaseModel.getUsecaseId(),accountModel.getInitialAppFormNo(),resubmitRequest,actionAuthorizationId,request);
				this.saveMessage(request,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
				modelAndView = new ModelAndView(new RedirectView("p_l3accountsmanagement.html?actionId=3") );
			}       
		}
		catch (FrameworkCheckedException exception)
		{	
			String msg = exception.getMessage();
			String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};
			
			if("MobileNumUniqueException".equals(msg)) {
				this.saveMessage(request, super.getText("newMfsAccount.mobileNumNotUnique2", args, request.getLocale() ));
			} 
			else if("NICUniqueException".equals(msg)){
				this.saveMessage(request, super.getText("newMfsAccount.nicNotUnique2", args, request.getLocale() ));
			} 
			else
			{
				this.saveMessage(request, msg);
			}
	        return super.showForm(request, response, errors);
		}
		catch (Exception exception)
		{	
			exception.printStackTrace();
			this.saveMessage(request, MessageUtil.getMessage("6075"));
	        return super.showForm(request, response, errors);
		}
		
		return modelAndView;
	}

	
	private Long getUpdatedRegisterationState(Long retailerContactId) throws FrameworkCheckedException
	{
		AppUserModel	newAppUser=userManagementFacade.getAppUserByRetailer(retailerContactId);
		return newAppUser.getRegistrationStateId();
	}
	
	
	private List<RetailerContactAddressesModel> findRetailContactAddresses(RetailerContactModel retailerContactModel)
			throws FrameworkCheckedException
	{
		List<RetailerContactAddressesModel> retailerContactAddressesModelList = null;
		SearchBaseWrapper retailerContactAddressesWrapper = new SearchBaseWrapperImpl();
		RetailerContactAddressesModel retailerContactAddressesModel = new RetailerContactAddressesModel();
		retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		retailerContactAddressesWrapper.setBasePersistableModel(retailerContactAddressesModel);
		retailerContactAddressesWrapper = agentHierarchyManager.findRetailContactAddresses(retailerContactAddressesWrapper);
		if (retailerContactAddressesWrapper.getCustomList() != null)
		{
			retailerContactAddressesModelList = retailerContactAddressesWrapper.getCustomList().getResultsetList();
		} 
		return retailerContactAddressesModelList;
	}

	private void removeSpecialAccountTypes(CopyOnWriteArrayList<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList)
	{
		// Iterator<OlaCustomerAccountTypeModel> it =
		// olaCustomerAccountTypeModelList.iterator();
		// So far only one special account type exists which is SETTLEMENT (id =
		// 3L)
		for (OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList)
		{
			if (model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT
					|| model.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER)
			{
				olaCustomerAccountTypeModelList.remove(model);
			}
		}
	}

	private boolean validateApplicantUniqueInfo(Level3AccountModel accountModel, HttpServletRequest req,HttpServletResponse res, BindException errors)
	{
		String applicant1ID	=	accountModel.getApplicant1DetailModel().getIdNumber();
		String applicant1Mobile	=	accountModel.getApplicant1DetailModel().getMobileNo();
		
		List<ApplicantDetailModel> list1= accountModel.getApplicantDetailModelList();
		List<ApplicantDetailModel> list2= accountModel.getApplicantDetailModelList();
		
		if(list1==null)
			return true;
		
		ApplicantDetailModel model1	= null;
		for (int x = 0; x < list1.size(); x++)
		{
			model1	=	list1.get(x);
			
			if(model1.getIdNumber().equals(applicant1ID) || model1.getMobileNo().equals(applicant1Mobile))
			{
				return false;
			}
		}

		ApplicantDetailModel model2	= null;
		
		for (int x = 0; x < list1.size(); x++)
		{
			model1	=	list1.get(x);
			for (int y = x+1; y < list2.size(); y++)
			{
				model2	=	list1.get(y);
				
				if(model1.getIdNumber().equals(model2.getIdNumber()) || model1.getMobileNo().equals(model2.getMobileNo()))
				{
					return false;
				}
				
			}
		}

		return true;
	}
	
	private boolean onCreateValidation(Level3AccountModel accountModel, HttpServletRequest req){
		boolean flag=true;
		return flag;
	}
	
	private boolean onUpdateValidation(Level3AccountModel accountModel, HttpServletRequest req){
		boolean flag=true;
		if(accountModel != null && accountModel.getRegistrationStateId()==null)
		{
			this.saveMessage(req, "Registration State: is required.");
			flag=false;
		}
		return flag;
	}
	
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setLinkPaymentModeManager(LinkPaymentModeManager linkPaymentModeManager)
	{
		this.linkPaymentModeManager = linkPaymentModeManager;
	}

	public void setUserDeviceAccountListViewManager(UserDeviceAccountListViewManager userDeviceAccountListViewManager)
	{
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}

	public void setCommonCommandManager(CommonCommandManager commonCommandManager)
	{
		this.commonCommandManager = commonCommandManager;
	}

	public void setLevel3AccountFacade(Level3AccountFacade level3AccountFacade)
	{
		this.level3AccountFacade = level3AccountFacade;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager)
	{
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public void setAccountFacade(AccountFacade accountFacade)
	{
		this.accountFacade = accountFacade;
	}

	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade)
	{
		this.mfsAccountFacade = mfsAccountFacade;
	}

	public void setRetailerFacade(RetailerFacade retailerFacade)
	{
		this.retailerFacade = retailerFacade;
	}

	public void setUserManagementFacade(UserManagementFacade userManagementFacade)
	{
		this.userManagementFacade = userManagementFacade;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
	{
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setEncryptionHandler(EncryptionHandler encryptionHandler)
	{
		this.encryptionHandler = encryptionHandler;
	}

	public DistributorManager getDistributorManager() {
		return distributorManager;
	}

	public void setDistributorManager(DistributorManager distributorManager) {
		if (distributorManager != null) {
			this.distributorManager = distributorManager;
		}
	}
	
	private Level3AccountModel populateCurrentInfoModel(Level3AccountModel model) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		Level3AccountModel accountModel = new Level3AccountModel();
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setInitialAppFormNo(model.getInitialAppFormNo());
		searchBaseWrapper.setBasePersistableModel(retailerContactModel);

		retailerFacade.loadRetailerByInitialAppFormNo(searchBaseWrapper);
      
		if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
		{
			retailerContactModel = (RetailerContactModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
			accountModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			//Account Relationship Information
		
			accountModel.setInitialAppFormNo(model.getInitialAppFormNo());
			accountModel.setAccountPurpose(retailerContactModel.getAccountPurposeIdAccountPurposeModel().getName());
			accountModel.setAcNature(retailerContactModel.getAcNature());
			accountModel.setAcTitle(retailerContactModel.getName());
			accountModel.setCurrencyId(retailerContactModel.getCurrency());
			accountModel.setTaxRegimeName(retailerContactModel.getTaxRegimeIdTaxRegimeModel().getName());
			accountModel.setFed(retailerContactModel.getFed());
			//Business Information
			accountModel.setBusinessName(retailerContactModel.getBusinessName());
			accountModel.setCommencementDate(retailerContactModel.getCommencementDate());
			accountModel.setIncorporationDate(retailerContactModel.getIncorporationDate());
			accountModel.setSecpRegNo(retailerContactModel.getSecpRegNo());
			accountModel.setSecpRegDate(retailerContactModel.getSecpRegDate());
			accountModel.setNtn(retailerContactModel.getNtn());
			accountModel.setSalesTaxRegNo(retailerContactModel.getSalesTaxRegNo());
			accountModel.setMembershipNoTradeBody(retailerContactModel.getMembershipNoTradeBody());
			accountModel.setTradeBody(retailerContactModel.getTradeBody());
			accountModel.setBusinessNatureName(retailerContactModel.getBusinessNatureIdBusinessNatureModel().getName());
			accountModel.setLocationTypeName(retailerContactModel.getLocationTypeIdLocationTypeModel().getName());
			accountModel.setLocationSizeName(retailerContactModel.getLocationSizeIdLocationSizeModel().getName());
			accountModel.setEstSince(retailerContactModel.getEstSince());
			accountModel.setLandLineNo(retailerContactModel.getLandLineNo());
			accountModel.setRegStateComments(retailerContactModel.getRegStateComments());
			accountModel.setCustomerAccountTypeId(retailerContactModel.getOlaCustomerAccountTypeModelId());
			accountModel.setCreatedOn(retailerContactModel.getCreatedOn());
			accountModel.setSalary(retailerContactModel.getSalary());
			accountModel.setBuisnessIncome(retailerContactModel.getBusinessIncome());
			accountModel.setOtherIncome(retailerContactModel.getOtherIncome());
			accountModel.setCustomerTypeId(retailerContactModel.getCustomerTypeId());
			accountModel.setFundsSourceId(retailerContactModel.getFundSourceId());
			accountModel.setTransactionModeId(retailerContactModel.getTransactionModeId());
			accountModel.setOtherTransactionMode(retailerContactModel.getOtherTransactionMode());
			accountModel.setAccountReason(retailerContactModel.getAccountReasonId());
			accountModel.setScreeningPerformed(retailerContactModel.isScreeningPerformed());
			accountModel.setComments(retailerContactModel.getNokComments());
			accountModel.setAccountOpeningDate(model.getAccountOpeningDate());
			accountModel.setAccounttypeName(retailerContactModel.getOlaCustomerAccountTypeModel().getName());
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			DistributorModel distributorModel = new DistributorModel();
	  	    distributorModel.setDistributorId(retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
	  	    baseWrapper.setBasePersistableModel(distributorModel);
	  	    baseWrapper = distributorManager.loadDistributor(baseWrapper);
	  	    distributorModel = (DistributorModel) baseWrapper.getBasePersistableModel();
	  	    accountModel.setDistributorName(distributorModel.getName()); 
			
			//NOK
			NokDetailVOModel nokDetailVOModel = new NokDetailVOModel();
			nokDetailVOModel.setNokName(retailerContactModel.getNokName());
			nokDetailVOModel.setNokRelationship(retailerContactModel.getNokRelationship());
			nokDetailVOModel.setNokMobile(retailerContactModel.getNokMobile());
			nokDetailVOModel.setNokMobile(retailerContactModel.getNokMobile());
			nokDetailVOModel.setNokIdNumber(retailerContactModel.getNokIdNumber());
			nokDetailVOModel.setNokIdType(retailerContactModel.getNokIdType());
			accountModel.setNokDetailVOModel(nokDetailVOModel);
			
			AppUserModel appUserModel = new AppUserModel();
			appUserModel = userManagementFacade.getAppUserByRetailer(retailerContactModel.getRetailerContactId());
			
			accountModel.setMobileNo(appUserModel.getMobileNo());
			accountModel.setAppUserId(appUserModel.getAppUserId());
			accountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
			accountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
			accountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
			accountModel.setMfsId(appUserModel.getUsername());
			
			try {
				UserDeviceAccountsModel	userDeviceAccountsModel=	mfsAccountFacade.getDeviceAccountByAppUserId(appUserModel.getAppUserId(), DeviceTypeConstantsInterface.ALL_PAY );
				
				if (!this.linkPaymentModeManager.isFirstAccountOtherThanOla(userDeviceAccountsModel.getUserId())){
					
					SmartMoneyAccountModel smartMoneyAccountModel	=	smartMoneyAccountManager.getSMALinkedWithCoreAccount(retailerContactModel.getRetailerContactId());
					if(smartMoneyAccountModel!=null) {
						String accountNumner=level3AccountFacade.getLinkedCoreAccountNo(appUserModel.getUsername());
						accountModel.setCoreAccountNumber(encryptionHandler.decrypt(accountNumner));
						accountModel.setCoreAccountTitle(smartMoneyAccountModel.getName());
						accountModel.setAccountLinked(Boolean.TRUE);
					}
				}
			} catch (Exception ex) {
				log.warn("Exception while getting customer info from OLA: " + ex.getStackTrace());
			}
			
			
			ApplicantDetailModel applicantModel = new ApplicantDetailModel();
			applicantModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
			accountModel.setApplicant1DetailModel(mfsAccountFacade.loadApplicantDetail(applicantModel));
			accountModel.getApplicant1DetailModel().setCreatedByAppUserModel(new AppUserModel(2L));
			accountModel.getApplicant1DetailModel().setUpdatedByAppUserModel(new AppUserModel(2L));
			accountModel.getApplicant1DetailModel().setRetailerContactModel(null);
			
			model.getApplicant1DetailModel();
			
			BusinessTypeModel businessTypeModel = new BusinessTypeModel();
			businessTypeModel = level3AccountFacade.loadBusinessTypeModel(retailerContactModel.getBusinessTypeId());
		    accountModel.setBusinessTypeName(businessTypeModel.getName()); 
			
		   /* if(null != accountModel.getApplicant1DetailModel().getProfession()){
		  	    ProfessionModel professionModel = new ProfessionModel();
		  	    professionModel = level3AccountFacade.loadProfessionModel(accountModel.getApplicant1DetailModel().getProfession());
		  	    accountModel.getApplicant1DetailModel().setProfessionName(professionModel.getName()); 
		    }*/
		    
		   /* if(null != accountModel.getApplicant1DetailModel().getOccupation())
		    {
		  	    OccupationModel occupationModel = new OccupationModel(); 
		  	    occupationModel = level3AccountFacade.loadOccupationModel(accountModel.getApplicant1DetailModel().getOccupation());
		  	    accountModel.getApplicant1DetailModel().setOccupationName(occupationModel.getName()); 
		    }
		    
		    if(null != accountModel.getApplicant1DetailModel().getBirthPlace())
		    {
		    	CityModel birthCityModel = new CityModel();
			    birthCityModel = level3AccountFacade.loadCityModel(Long.parseLong(accountModel.getApplicant1DetailModel().getBirthPlace()));
			    accountModel.getApplicant1DetailModel().setBirthPlaceName(birthCityModel.getName());
		    }*/
			
			/*SearchBaseWrapper	searchWrapper = new SearchBaseWrapperImpl();
			ACOwnershipDetailModel queryAccountOwnerShipDetailModel = new ACOwnershipDetailModel();
			queryAccountOwnerShipDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			queryAccountOwnerShipDetailModel.setIsDeleted(Boolean.FALSE);
			searchWrapper.setBasePersistableModel(queryAccountOwnerShipDetailModel);
			List<ACOwnershipDetailModel> aCOwnershipDetailModelList = mfsAccountFacade.loadL3AccountOwnerShipDetails(searchWrapper);
			if(aCOwnershipDetailModelList.isEmpty()){
				aCOwnershipDetailModelList.add(new ACOwnershipDetailModel());
			}
			accountModel.setAcOwnershipDetailModelList(aCOwnershipDetailModelList);*/
			
			// Populating Address Fields
			List<RetailerContactAddressesModel> retailerContactAddresses = findRetailContactAddresses(retailerContactModel);
			if (retailerContactAddresses != null && retailerContactAddresses.size() > 0)
			{
				for (RetailerContactAddressesModel retailerContactAdd : retailerContactAddresses)
				{
					AddressModel addressModel = retailerContactAdd.getAddressIdAddressModel();
					if (null == retailerContactAdd.getApplicantTypeId())
					{
						if (addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty())
						{
							accountModel.getNokDetailVOModel().setNokMailingAdd(addressModel.getStreetAddress());
						}
						if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.CORRESSPONDANCE_ADDRESS.longValue()
								&& retailerContactAdd.getApplicantDetailId()==null){
							
							accountModel.setCorresspondanceAddress(addressModel.getStreetAddress());
							accountModel.setCorresspondancePostalCode(addressModel.getPostalCode());
							
							if(addressModel.getCityId()!=null){
								CityModel corrCityModel = new CityModel();
								corrCityModel = level3AccountFacade.loadCityModel(addressModel.getCityId());
							    accountModel.setCorresspondanceAddCityName(corrCityModel.getName());
							}
						}
						if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.BUSINESS_ADDRESS.longValue()
								&& retailerContactAdd.getApplicantTypeId()==null){
							
							accountModel.setBusinessAddress(addressModel.getStreetAddress());
							accountModel.setBusinessPostalCode(addressModel.getPostalCode());
							
							CityModel corrCityModel = new CityModel();
							corrCityModel = level3AccountFacade.loadCityModel(addressModel.getCityId());
						    accountModel.setBusinessAddCityName(corrCityModel.getName());
						}
					} else if (retailerContactAdd.getApplicantTypeId().longValue() == ApplicantTypeConstants.APPLICANT_TYPE_1) {
						if (retailerContactAdd.getAddressTypeId().longValue() == AddressTypeConstants.PRESENT_HOME_ADDRESS)
						{
							if (addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty()) {
								accountModel.getApplicant1DetailModel().setResidentialAddress(addressModel.getStreetAddress());
							}
							if(null != addressModel.getCityId()){
								CityModel corrCityModel = new CityModel();
								corrCityModel = level3AccountFacade.loadCityModel(addressModel.getCityId());
							    accountModel.getApplicant1DetailModel().setResidentialCityName(corrCityModel.getName());
							}
						} else if (retailerContactAdd.getAddressTypeId().longValue() == AddressTypeConstants.BUSINESS_ADDRESS) {
							if (addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty()) {
								accountModel.getApplicant1DetailModel().setBuisnessAddress(addressModel.getStreetAddress());
							}
							if(null != addressModel.getCityId()){
								CityModel corrCityModel = new CityModel();
								corrCityModel = level3AccountFacade.loadCityModel(addressModel.getCityId());
							    accountModel.getApplicant1DetailModel().setBuisnessCityName(corrCityModel.getName());
							}
						}
					}
				}
			}
		}
      
      /////Load Reference Data
      
      Map referenceDataMap = new HashMap();
	  try{  
		    Long[] regStateList = {RegistrationStateConstantsInterface.RQST_RCVD,RegistrationStateConstantsInterface.DECLINE,RegistrationStateConstantsInterface.DISCREPANT,RegistrationStateConstantsInterface.VERIFIED};
		    List<RegistrationStateModel> regStates= commonCommandManager.getRegistrationStateByIds(regStateList).getResultsetList();
		    
		    for (RegistrationStateModel registrationStateModel : regStates) {
				if(null!=accountModel.getRegistrationStateId() && (accountModel.getRegistrationStateId().longValue()==registrationStateModel.getRegistrationStateId().longValue()))
					accountModel.setRegStateName(registrationStateModel.getName());
			}
	  }
	  catch(Exception e){
		  logger.error(e.getMessage(), e);
		  throw new Exception();
	  }
      
      return accountModel;
	} 
}
