package com.inov8.microbank.webapp.action.agenthierarchy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.AccountPurposeModel;
import com.inov8.microbank.common.model.AccountReasonModel;
import com.inov8.microbank.common.model.BusinessTypeModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.CountryModel;
import com.inov8.microbank.common.model.CustomerTypeModel;
import com.inov8.microbank.common.model.FundSourceModel;
import com.inov8.microbank.common.model.Level3AccountModel;
import com.inov8.microbank.common.model.OccupationModel;
import com.inov8.microbank.common.model.ProfessionModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.TransactionModeModel;
import com.inov8.microbank.common.model.portal.changeaccnickmodule.ChangeAccountNickListViewModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.userdeviceaccountmodule.UserDeviceAccountListViewModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MaritalStatusEnum;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.TitleEnum;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.facade.portal.level3account.Level3AccountFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;

public class Level3ApplicantController extends AdvanceFormController{
	private AppUserManager appUserManager;
	private MfsAccountManager mfsAccountManager;
	private ReferenceDataManager referenceDataManager;
	private LinkPaymentModeManager linkPaymentModeManager;
	private UserDeviceAccountListViewManager userDeviceAccountListViewManager;
	private CommonCommandManager commonCommandManager;
	private Level3AccountFacade level3AccountFacade;
	private AgentHierarchyManager agentHierarchyManager;

	public Level3ApplicantController() {
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
		String cnic = ServletRequestUtils.getStringParameter(req, "idNumber");
		Long index = ServletRequestUtils.getLongParameter(req, "index");
		ApplicantDetailModel selectedApplicantDetailModel = new ApplicantDetailModel();
		HttpSession session = req.getSession();
		Level3AccountModel level3AccountModel = (Level3AccountModel) session.getAttribute("accountModelLevel3");
		
		if(level3AccountModel != null)
		{
			for(ApplicantDetailModel model : level3AccountModel.getApplicantDetailModelList())
			{
				if (cnic != null && cnic.equals(model.getIdNumber()))
				{
					selectedApplicantDetailModel = model;
					selectedApplicantDetailModel.setIndex(index);
					break;
				}
			}
		}
		
		level3AccountModel.setApplicantDetailModelEditMode(selectedApplicantDetailModel);
		if(cnic == null){
			level3AccountModel.setActionId(PortalConstants.ACTION_CREATE);
			level3AccountModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID));
		}else{
			level3AccountModel.setActionId(PortalConstants.ACTION_UPDATE);
			level3AccountModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID));
		}

		session.setAttribute("accountModelLevel3", level3AccountModel);
		return level3AccountModel;
	} 

	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {

		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		Long id = null;
	    if (null != appUserId && appUserId.trim().length() > 0)
	    {
	      id = new Long(EncryptionUtil.decryptWithDES(appUserId));
	      
	      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	      ChangeAccountNickListViewModel changeAccountNickListViewModel = new ChangeAccountNickListViewModel();
	      changeAccountNickListViewModel.setAppUserId(id);
	      changeAccountNickListViewModel.setBankId(CommissionConstantsInterface.BANK_ID);
	      searchBaseWrapper.setBasePersistableModel(changeAccountNickListViewModel);
	      searchBaseWrapper = linkPaymentModeManager.searchAccounts(searchBaseWrapper);
	      CustomList clist = searchBaseWrapper.getCustomList();
	      List accountsList = clist.getResultsetList();
	      if(!accountsList.isEmpty()){
	    	  req.setAttribute("accountExists", "true");
	      }
	      
          UserDeviceAccountListViewModel userDeviceAccountListViewModel = new UserDeviceAccountListViewModel();
          userDeviceAccountListViewModel.setAppUserId(id);
	      searchBaseWrapper.setBasePersistableModel(userDeviceAccountListViewModel);
	      searchBaseWrapper = this.userDeviceAccountListViewManager.searchUserDeviceAccount(searchBaseWrapper);
	      if(searchBaseWrapper.getCustomList().getResultsetList().size() > 0){
	    	  userDeviceAccountListViewModel = (UserDeviceAccountListViewModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);  
	          if(userDeviceAccountListViewModel.getAccountLocked() == true){
	        	  req.setAttribute("accountLocked", "true");
	          }
	      }
	    }
		
		Map referenceDataMap = new HashMap();
		
		List<LabelValueBean> mailingAddressTypeList = new ArrayList<LabelValueBean>();
	    LabelValueBean addressType = new LabelValueBean("Resident", "1");
	    mailingAddressTypeList.add(addressType);
	    addressType = new LabelValueBean("Office/Business", "2");
	    mailingAddressTypeList.add(addressType);
	    referenceDataMap.put("mailingAddressTypeList", mailingAddressTypeList);
	    
	    List<LabelValueBean> residentialStatusList = new ArrayList<LabelValueBean>();
	    LabelValueBean residentialStatus = new LabelValueBean("Resident", "1");
	    residentialStatusList.add(residentialStatus);
	    residentialStatus = new LabelValueBean("Non-Resident", "0");
	    residentialStatusList.add(residentialStatus);
	    referenceDataMap.put("residentialStatusList", residentialStatusList);
	    
	    List<LabelValueBean> titleList = new ArrayList<LabelValueBean>();
	    LabelValueBean title = new LabelValueBean("Mr.", "1");

	    for (TitleEnum value : TitleEnum.values())
		{
			title = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
			titleList.add(title);
		}
	    
	    referenceDataMap.put("titleList", titleList);
	    
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
	    
		List<LabelValueBean> maritalStatusList = new ArrayList<LabelValueBean>();
		for (MaritalStatusEnum value : MaritalStatusEnum.values())
		{
			LabelValueBean ownerShipType = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
			maritalStatusList.add(ownerShipType);
		}
		referenceDataMap.put("maritalStatusList", maritalStatusList);	    
	    
	    ProfessionModel professionModel = new ProfessionModel();
	    ReferenceDataWrapper professionDataWrapper = new ReferenceDataWrapperImpl(professionModel, "name", SortingOrder.ASC);
	    professionDataWrapper.setBasePersistableModel(professionModel);
	    try
	    {
	      referenceDataManager.getReferenceData(professionDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    List<ProfessionModel> professionList = null;
	    if (professionDataWrapper.getReferenceDataList() != null)
	    {
	    	professionList = professionDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("professionList", professionList);
	    
	    CustomerTypeModel customerTypeModel = new CustomerTypeModel();
	    ReferenceDataWrapper customerTypeDataWrapper = new ReferenceDataWrapperImpl(customerTypeModel, "name", SortingOrder.ASC);
	    customerTypeDataWrapper.setBasePersistableModel(customerTypeModel);
	    try
	    {
	      referenceDataManager.getReferenceData(customerTypeDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    List<CustomerTypeModel> customerTypeList = null;
	    if (customerTypeDataWrapper.getReferenceDataList() != null)
	    {
	    	customerTypeList = customerTypeDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("customerTypeList", customerTypeList);
	    
	    FundSourceModel fundSourceModel = new FundSourceModel();
	    ReferenceDataWrapper fundSourceDataWrapper = new ReferenceDataWrapperImpl(fundSourceModel, "fundSourceId", SortingOrder.ASC);
	    fundSourceDataWrapper.setBasePersistableModel(fundSourceModel);
	    try
	    {
	      referenceDataManager.getReferenceData(fundSourceDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    List<FundSourceModel> fundSourceList = null;
	    if (fundSourceDataWrapper.getReferenceDataList() != null)
	    {
	    	fundSourceList = fundSourceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("fundSourceList", fundSourceList);
	    
	    OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
	    customerAccountTypeModel.setActive(true);
	    customerAccountTypeModel.setIsCustomerAccountType(Boolean.FALSE); //added by Turab
	    ReferenceDataWrapper customerAccountTypeDataWrapper = new ReferenceDataWrapperImpl(customerAccountTypeModel, "name", SortingOrder.ASC);
	    customerAccountTypeDataWrapper.setBasePersistableModel(customerAccountTypeModel);
	    try
	    {
	      referenceDataManager.getReferenceData(customerAccountTypeDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    CopyOnWriteArrayList<OlaCustomerAccountTypeModel> customerAccountTypeList = null;
	    if (customerAccountTypeDataWrapper.getReferenceDataList() != null)
	    {
	    	customerAccountTypeList = new CopyOnWriteArrayList<OlaCustomerAccountTypeModel>(customerAccountTypeDataWrapper.getReferenceDataList());
	    	removeSpecialAccountTypes(customerAccountTypeList);
	    }
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
	    
	    List<LabelValueBean> residanceStatusList = new ArrayList<LabelValueBean>();
	    LabelValueBean residanceStatus = new LabelValueBean("Permanent", "P");
	    residanceStatusList.add(residanceStatus);
	    residanceStatus = new LabelValueBean("Temporary", "T");
	    residanceStatusList.add(residanceStatus);
	    referenceDataMap.put("residanceStatusList", residanceStatusList);
	    
	    List<LabelValueBean> binaryOpt = new ArrayList<LabelValueBean>();
	    LabelValueBean opt = new LabelValueBean("Yes", "1");
	    binaryOpt.add(opt);
	    opt = new LabelValueBean("No", "0");
	    binaryOpt.add(opt);
	    referenceDataMap.put("binaryOpt", binaryOpt);
	    
	    Long[] regStateList = {RegistrationStateConstantsInterface.DECLINE,RegistrationStateConstantsInterface.DISCREPANT,RegistrationStateConstantsInterface.VERIFIED};
	    CustomList<RegistrationStateModel> regStates= commonCommandManager.getRegistrationStateByIds(regStateList);
	    referenceDataMap.put("regStateList", regStates.getResultsetList());
	    
	    CountryModel countryModel = new CountryModel();
	    ReferenceDataWrapper countryReferenceDataWrapper = new ReferenceDataWrapperImpl(countryModel, "name", SortingOrder.ASC);
	    countryReferenceDataWrapper.setBasePersistableModel(countryModel);
	    try
	    {
	      referenceDataManager.getReferenceData(countryReferenceDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    TransactionModeModel transactionModeModel = new TransactionModeModel();
	    ReferenceDataWrapper transactionModeReferenceDataWrapper = new ReferenceDataWrapperImpl(transactionModeModel, "transactionModeId", SortingOrder.ASC);
	    transactionModeReferenceDataWrapper.setBasePersistableModel(transactionModeModel);
	    try
	    {
	      referenceDataManager.getReferenceData(transactionModeReferenceDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    List<TransactionModeModel> transactionModeList = null;
	    if (transactionModeReferenceDataWrapper.getReferenceDataList() != null)
	    {
	    	transactionModeList = transactionModeReferenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("transactionModeList", transactionModeList);

	    
	    AccountPurposeModel accountPurposeModel = new AccountPurposeModel();
	    ReferenceDataWrapper accountPurposeReferenceDataWrapper = new ReferenceDataWrapperImpl(accountPurposeModel, "name", SortingOrder.ASC);
	    accountPurposeReferenceDataWrapper.setBasePersistableModel(accountPurposeModel);
	    try
	    {
	      referenceDataManager.getReferenceData(accountPurposeReferenceDataWrapper);
	    }
	    catch (Exception e)
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
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    List<BusinessTypeModel> businessTypeList = null;
	    if (businessTypeReferenceDataWrapper.getReferenceDataList() != null)
	    {
	    	businessTypeList = businessTypeReferenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("businessTypeList", businessTypeList);
	    
	    AccountReasonModel accountReasonModel = new AccountReasonModel();
	    ReferenceDataWrapper accountReasonReferenceDataWrapper = new ReferenceDataWrapperImpl(accountReasonModel, "accountReasonId", SortingOrder.ASC);
	    accountReasonReferenceDataWrapper.setBasePersistableModel(accountReasonModel);
	    try
	    {
	      referenceDataManager.getReferenceData(accountReasonReferenceDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    List<AccountReasonModel> accountReasonList = null;
	    if (accountReasonReferenceDataWrapper.getReferenceDataList() != null)
	    {
	    	accountReasonList = accountReasonReferenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("accountReasonList", accountReasonList);
		
	    Map<Object,String> publicFigureOpt = new LinkedHashMap<Object,String>();
	    publicFigureOpt.put(true,"Yes");
		publicFigureOpt.put(false,"No");
		referenceDataMap.put("publicFigureOpt", publicFigureOpt);
		
		OccupationModel occupationModel = new OccupationModel();
	    ReferenceDataWrapper occupationReferenceDataWrapper = new ReferenceDataWrapperImpl(occupationModel, "name", SortingOrder.ASC);
	    occupationReferenceDataWrapper.setBasePersistableModel(occupationModel);
	    try
	    {
	      referenceDataManager.getReferenceData(occupationReferenceDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    List<OccupationModel> occupationList = null;
	    if (occupationReferenceDataWrapper.getReferenceDataList() != null)
	    {
	    	occupationList = occupationReferenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("occupationList", occupationList);
		
	    CityModel cityModel = new CityModel();
	    ReferenceDataWrapper cityDataWrapper = new ReferenceDataWrapperImpl(cityModel, "name", SortingOrder.ASC);
	    cityDataWrapper.setBasePersistableModel(cityModel);
	    try
	    {
	      referenceDataManager.getReferenceData(cityDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    List<CityModel> cityList = null;
	    if (cityDataWrapper.getReferenceDataList() != null)
	    {
	    	cityList = cityDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("cityList", cityList);
		
	    
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
	    
	    return referenceDataMap;
	}
    
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{

		Level3AccountModel accountModelModel = (Level3AccountModel) obj;
		HttpSession session = req.getSession();
		Level3AccountModel sessionAccountModelLevel3 = (Level3AccountModel) session.getAttribute("accountModelLevel3");
		sessionAccountModelLevel3.setApplicantDetailModelEditMode(accountModelModel.getApplicantDetailModelEditMode());

		// sessionAccountModelLevel3.getApplicantDetailModelList().add(accountModelModel.getApplicantDetailModelEditMode());

		int idx = -1;
		ApplicantDetailModel model = null;
		if (sessionAccountModelLevel3 != null && sessionAccountModelLevel3.getApplicantDetailModelEditMode() != null)
		{
			boolean isExist = false;
			for (ApplicantDetailModel applicantDetailModel : sessionAccountModelLevel3.getApplicantDetailModelList())
			{
				if (applicantDetailModel.getIdNumber() != null
						&& applicantDetailModel.getIdNumber().equals(sessionAccountModelLevel3.getApplicantDetailModelEditMode().getIdNumber()))
				{
					idx = sessionAccountModelLevel3.getApplicantDetailModelList().indexOf(applicantDetailModel);
					model = sessionAccountModelLevel3.getApplicantDetailModelEditMode();
					isExist = true;
					break;
				}
			}

			long index	=	(long)sessionAccountModelLevel3.getApplicantDetailModelList().size()+1;
			accountModelModel.getApplicantDetailModelEditMode().setIndex(index);
			
			if (!isExist)
			{
				if (sessionAccountModelLevel3.getApplicantDetailModelEditMode().getIdNumber() != null)
				{
					sessionAccountModelLevel3.getApplicantDetailModelList().add(sessionAccountModelLevel3.getApplicantDetailModelEditMode());
				}
			} else
			{
				sessionAccountModelLevel3.setApplicantDetailModelList(this.updateApplicantDetailList(
						sessionAccountModelLevel3.getApplicantDetailModelList(), idx, model));
			}

		}

		//super.saveMessage(req, "Applicant Information are updated");
		session.setAttribute("accountModelLevel3", sessionAccountModelLevel3);

		Map<String, Object> modelMap = new HashMap<>(2);
		modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		ModelAndView modelAndView = new ModelAndView(getSuccessView(), modelMap);
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		
		Level3AccountModel accountModelModel = (Level3AccountModel) obj;
		int index	=	accountModelModel.getApplicantDetailModelEditMode().getIndex().intValue()-1;
		
		HttpSession session = req.getSession();
		Level3AccountModel sessionAccountModelLevel3 = (Level3AccountModel) session.getAttribute("accountModelLevel3");
		if(sessionAccountModelLevel3 != null && sessionAccountModelLevel3.getApplicantDetailModelEditMode()!=null)
		{
			sessionAccountModelLevel3.getApplicantDetailModelList().set(index, accountModelModel.getApplicantDetailModelEditMode());

		}
	
		session.setAttribute("accountModelLevel3", sessionAccountModelLevel3);
		
		Map<String,Object> modelMap = new HashMap<>(2);
		modelMap.put(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		ModelAndView modelAndView = new ModelAndView(getSuccessView(), modelMap);
		return modelAndView;
	}

	/**
	 * @param retailerContactModel
	 * @throws FrameworkCheckedException
	 */
	private List<RetailerContactAddressesModel> findRetailContactAddresses( RetailerContactModel retailerContactModel) throws FrameworkCheckedException
	{
		  List<RetailerContactAddressesModel> retailerContactAddressesModelList = null;
		  SearchBaseWrapper retailerContactAddressesWrapper = new SearchBaseWrapperImpl();
		  RetailerContactAddressesModel retailerContactAddressesModel = new RetailerContactAddressesModel();
		  retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		  retailerContactAddressesWrapper.setBasePersistableModel(retailerContactAddressesModel);
		  retailerContactAddressesWrapper = agentHierarchyManager.findRetailContactAddresses(retailerContactAddressesWrapper);
		  if(retailerContactAddressesWrapper.getCustomList() != null)
		  {
			  retailerContactAddressesModelList = retailerContactAddressesWrapper.getCustomList().getResultsetList();
		  }
		  return retailerContactAddressesModelList;
	}

	private void removeSpecialAccountTypes(CopyOnWriteArrayList<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList)
	{
	    //Iterator<OlaCustomerAccountTypeModel> it = olaCustomerAccountTypeModelList.iterator();
	    //So far only one special account type exists which is SETTLEMENT (id = 3L) 
	     for (OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList) {
	           if(model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT
	              || model.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER){
	                olaCustomerAccountTypeModelList.remove(model);
	           }
	    }
    }
	
	private List<ApplicantDetailModel> updateApplicantDetailList(List<ApplicantDetailModel> list, int index, ApplicantDetailModel model){
		list.remove(index);
		list.add(model);
		return list;
	}
	public void setAppUserManager(AppUserManager appUserManager)
	{
		this.appUserManager = appUserManager;
	}
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager)
	{
		this.mfsAccountManager = mfsAccountManager;
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
}
