package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.ACOwnershipDetailModel;
import com.inov8.microbank.common.model.AccountNature;
import com.inov8.microbank.common.model.AccountPurposeModel;
import com.inov8.microbank.common.model.AccountReasonModel;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.BusinessTypeModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.CountryModel;
import com.inov8.microbank.common.model.CustomerAddressesModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.CustomerTypeModel;
import com.inov8.microbank.common.model.FundSourceModel;
import com.inov8.microbank.common.model.KYCModel;
import com.inov8.microbank.common.model.OccupationModel;
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.common.model.ProfessionModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.model.TransactionModeModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.changeaccnickmodule.ChangeAccountNickListViewModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.AdditionalDetailVOModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.BusinessDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.Level2AccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.NokDetailVOModel;
import com.inov8.microbank.common.model.userdeviceaccountmodule.UserDeviceAccountListViewModel;
import com.inov8.microbank.common.util.ACOwnershipTypeEnum;
import com.inov8.microbank.common.util.AddressTypeConstants;
import com.inov8.microbank.common.util.ApplicantTypeConstants;
import com.inov8.microbank.common.util.BusinessTypeCategoryConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IDDocumentTypeEnum;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MaritalStatusEnum;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.TitleEnum;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.thoughtworks.xstream.XStream;

public class Level2AccountController extends AdvanceAuthorizationFormController{

	private MfsAccountManager mfsAccountManager;
	private ReferenceDataManager referenceDataManager;
	private LinkPaymentModeManager linkPaymentModeManager;
	private FinancialIntegrationManager financialIntegrationManager;
	private UserDeviceAccountListViewManager userDeviceAccountListViewManager;
	private CommonCommandManager commonCommandManager;

	public Level2AccountController() {
		setCommandName("level2AccountModel");
	    setCommandClass(Level2AccountModel.class);
	}
	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		String initialAppFormNo = ServletRequestUtils.getStringParameter(req, "iaf");
		Long id = null;
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit",false);
		Level2AccountModel level2AccountModelAuth = null;
		
		if(isReSubmit){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if((actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()) && isReSubmit){
				throw new FrameworkCheckedException("illegal operation performed");
			}
	 	
			XStream xstream = new XStream();
			level2AccountModelAuth = (Level2AccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
		    req.setAttribute("pageMfsId", level2AccountModelAuth.getMfsId());
		    if(null != level2AccountModelAuth.getAppUserId()){
		    	req.setAttribute("appUserId", EncryptionUtil.encryptWithDES(level2AccountModelAuth.getAppUserId().toString()));
		    }
			
			return level2AccountModelAuth;
		}
		
		if (null != appUserId && appUserId.trim().length() > 0)
	    {
			id = new Long(EncryptionUtil.decryptWithDES(appUserId));
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(id);
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
			appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
			
			baseWrapper = new BaseWrapperImpl();
			CustomerModel customer = new CustomerModel();
			customer.setCustomerId(appUserModel.getCustomerId());
			baseWrapper.setBasePersistableModel(customer);
			baseWrapper = mfsAccountManager.loadCustomer(baseWrapper);
			customer = (CustomerModel) baseWrapper.getBasePersistableModel();
			initialAppFormNo = customer.getInitialApplicationFormNumber();
	    }
		
		
		KYCModel queryKYC = new KYCModel();
		List<KYCModel> kycModelList = new ArrayList<KYCModel>(0);
		SearchBaseWrapper searchWrapper = new SearchBaseWrapperImpl();
		if(null != initialAppFormNo && initialAppFormNo.trim().length() > 0 ){
			queryKYC.setInitialAppFormNo(initialAppFormNo);
			queryKYC.setAcType(CustomerAccountTypeConstants.LEVEL_2);
			searchWrapper.setBasePersistableModel(queryKYC);
			kycModelList = mfsAccountManager.findKycModel(searchWrapper);
		}
		
		if(CollectionUtils.isEmpty(kycModelList)){
			//throw new Exception("No KYC form found with given Initial application form number. Please save first KYC Form with this application form number.");
			super.saveMessage(req, "Invalid Initial Application Form Number.");
			Level2AccountModel errorModel = new Level2AccountModel();
			errorModel.getAcOwnershipDetailModelList().add(new ACOwnershipDetailModel());
			errorModel.setInitialAppFormNo("");
			return errorModel;
		}

		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		BankModel bankModel = new BankModel();
		bankModel.setBankId(CommissionConstantsInterface.BANK_ID);
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
		boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
		req.setAttribute("veriflyRequired", veriflyRequired);
		HttpSession session = req.getSession();
		Level2AccountModel sessionAccountModelLevel2 = (Level2AccountModel) session.getAttribute("accountModelLevel2");
	
		session.removeAttribute("accountModelLevel2");

		
	    if (null != initialAppFormNo && initialAppFormNo.trim().length() > 0 /*null != appUserId && appUserId.trim().length() > 0*/)
	    {
	    	BaseWrapper baseWrapper = new BaseWrapperImpl();
	    	CustomerModel customer = new CustomerModel();
	    	customer.setInitialApplicationFormNumber(initialAppFormNo);
	    	baseWrapper.setBasePersistableModel(customer);
	    	baseWrapper = mfsAccountManager.searchCustomerByInitialAppFormNo(baseWrapper);
	    	AppUserModel customerAppUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
	      
	    	//id = new Long(EncryptionUtil.decryptWithDES(appUserId));
	    	id = customerAppUserModel.getAppUserId();
	    	
	    	if(id == null){
	    		Level2AccountModel accountModel = new Level2AccountModel();
	  	      //Add 1 object to make sure that table on screen has atleast one row
	  	    	if( CollectionUtils.isEmpty(accountModel.getAcOwnershipDetailModelList() ))
	  			{
	  	    		accountModel.getAcOwnershipDetailModelList().add(new ACOwnershipDetailModel());
	  			}
	  	    	if(CollectionUtils.isNotEmpty(kycModelList)){
	  		    	KYCModel kycModel = kycModelList.get(0);
	  				accountModel.setEmpId(kycModel.getCreatedByAppUserModel().getEmployeeId());
	  				accountModel.setEmployeeName(kycModel.getCreatedByAppUserModel().getFirstName() + " " + kycModel.getCreatedByAppUserModel().getLastName());
	  				accountModel.setKycOpeningDate(kycModel.getCreatedOn());
	  				accountModel.setInitialAppFormNo(kycModel.getInitialAppFormNo());
	  		      }
	  	        // for the logging process
	  	        accountModel.setActionId(PortalConstants.ACTION_CREATE);
	  	        accountModel.setUsecaseId(new Long(PortalConstants.CREATE_L2_USECASE_ID));
	  	    	return accountModel;
	    	}
	      
	    	if(sessionAccountModelLevel2 != null && id != null){
				if(!id.equals(sessionAccountModelLevel2.getAppUserId())){//this is old session object remove it and initialize to null
					session.removeAttribute("accountModelLevel2");
					sessionAccountModelLevel2 = null;
				}
			}	
	      
	      baseWrapper = new BaseWrapperImpl();
	      AppUserModel appUserModel = new AppUserModel();
          appUserModel.setAppUserId(id);
          baseWrapper.setBasePersistableModel(appUserModel);
	      baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
	      
	      appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
	      Level2AccountModel accountModel = new Level2AccountModel();
	      if(CollectionUtils.isNotEmpty(kycModelList)){
	    	KYCModel kycModel = kycModelList.get(0);
			accountModel.setEmpId(kycModel.getCreatedByAppUserModel().getEmployeeId());
			accountModel.setEmployeeName(kycModel.getCreatedByAppUserModel().getFirstName() + " " + kycModel.getCreatedByAppUserModel().getLastName()) ;
			accountModel.setKycOpeningDate(kycModel.getCreatedOn());
			accountModel.setInitialAppFormNo(kycModel.getInitialAppFormNo());
	      }
			
	      accountModel.setMobileNo(appUserModel.getMobileNo());
	      accountModel.setAppUserId(appUserModel.getAppUserId());
	      accountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
	      accountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
	      accountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
	      accountModel.setMfsId(appUserModel.getUsername());
	      if(null!=appUserModel.getClosedByAppUserModel())
	      {
		      accountModel.setClosedBy(appUserModel.getClosedByAppUserModel().getUsername());
	      }
	      accountModel.setClosedOn(appUserModel.getClosedOn());
	      if(null!=appUserModel.getSettledByAppUserModel())
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
	      
	      
	      try{
	    	  OLAVO olaVo = this.mfsAccountManager.getAccountInfoFromOLA(appUserModel.getNic(),bankModel.getBankId());
		      if(olaVo != null){
		    	  accountModel.setAccountNo(olaVo.getPayingAccNo());
		      }
	      }catch(Exception ex){
	    	  log.warn("Exception while getting customer info from OLA: "+ex.getStackTrace());
	      }
	      
	      CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
	      accountModel.setCustomerId(customerModel.getCustomerId().toString());
	      accountModel.setFed(customerModel.getFed());
	      accountModel.setTaxRegimeId(customerModel.getTaxRegimeId());
	      accountModel.setIsVeriSysDone(customerModel.getVerisysDone());
	      accountModel.setCustomerAccountName(customerModel.getName());
	      searchWrapper = new SearchBaseWrapperImpl();
	      ACOwnershipDetailModel queryAccountOwnerShipDetailModel = new ACOwnershipDetailModel();
	      queryAccountOwnerShipDetailModel.setCustomerId(customerModel.getCustomerId());
	      queryAccountOwnerShipDetailModel.setIsDeleted(Boolean.FALSE);
	      searchWrapper.setBasePersistableModel(queryAccountOwnerShipDetailModel);
	      accountModel.setAcOwnershipDetailModelList(mfsAccountManager.loadAccountOwnerShipDetails(searchWrapper));
	      
	      if(customerModel != null){
	    	  
	    	  ApplicantDetailModel applicantModel = new ApplicantDetailModel();
	    	  applicantModel.setCustomerId(customerModel.getCustomerId());
	    	  
	    	  applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
	    	  accountModel.setApplicant1DetailModel(mfsAccountManager.loadApplicantDetail(applicantModel));
	    	  if(accountModel.getApplicant1DetailModel() != null){
	    		  Long idType = accountModel.getApplicant1DetailModel().getIdType();
	    		  for (IDDocumentTypeEnum value : IDDocumentTypeEnum.values())
	    		    {
	    			  if(null != idType && value.getIndex() == idType.longValue()){
	    				  accountModel.getApplicant1DetailModel().setIdTypeName(value.getName());
	    			  }
	    		    }
	    	  }
	    	  
	      	  applicantModel = new ApplicantDetailModel();
	    	  applicantModel.setCustomerId(customerModel.getCustomerId());
	    	  applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
	    	  accountModel.setApplicantDetailModelList(mfsAccountManager.loadApplicantDetails(applicantModel));

	    	  long index=1;
	    	  
	    	  for(ApplicantDetailModel model : accountModel.getApplicantDetailModelList()){
	    		  Long idType = model.getIdType();
	    		  model.setIndex(index);
	    		  index++;
	    		  
	    		  for (IDDocumentTypeEnum value : IDDocumentTypeEnum.values())
	    		    {
		    			  
	    			  if(null != idType && value.getIndex() == idType.longValue()){
	    				  model.setIdTypeName(value.getName());
	    			  }
	    		    }
	    	  }
	    	  
	    	  
	    	 
		      
		      
		      //Get Product Catalog Id
		      UserDeviceAccountsModel userDeviceModel = mfsAccountManager.getDeviceAccountByAppUserId(appUserModel.getAppUserId(), DeviceTypeConstantsInterface.ALL_PAY);
		      Long catalogId = null;
		      if(userDeviceModel != null){
		    	  catalogId = userDeviceModel.getProdCatalogId();  
		      }
		      

	    	  BusinessDetailModel businessModel = new BusinessDetailModel();
	    	  businessModel.setCustomerId(customerModel.getCustomerId());
	    	  accountModel.setBusinessDetailModel(mfsAccountManager.loadBusinessDetail(businessModel));

	    	  accountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
	    	  accountModel.setAccounttypeName(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
	    	  accountModel.setMobileNo(customerModel.getMobileNo());
	    	  accountModel.setAcNature(customerModel.getAcNature());
	    	  accountModel.setAccountNature(customerModel.getAccountNature());
	    	  accountModel.setTypeOfAccount(customerModel.getTypeOfAccount());
	    	  accountModel.setSegmentId(customerModel.getSegmentId());
	    	  accountModel.setCurrency(customerModel.getCurrency());
	    	  accountModel.setProductCatalogId(catalogId);
	    	  
	    	  accountModel.setOccupation(customerModel.getOccupation());
	    	  accountModel.setCustomerTypeId(customerModel.getCustomerTypeId());		
	    	  accountModel.setRegStateComments(customerModel.getRegStateComments());
	    	  accountModel.setCreatedOn(customerModel.getCreatedOn());
	    	  accountModel.setBusinessTypeId(customerModel.getBusinessTypeId());
	    	  accountModel.setNokDetailVOModel(new NokDetailVOModel());
	    	  accountModel.getNokDetailVOModel().setNokName(customerModel.getNokName());
	    	  accountModel.getNokDetailVOModel().setNokContactNo(customerModel.getNokContactNo());
	    	  accountModel.getNokDetailVOModel().setNokRelationship(customerModel.getNokRelationship());  
	    	  accountModel.getNokDetailVOModel().setNokMobile(customerModel.getNokMobile());
	    	  accountModel.getNokDetailVOModel().setNokIdType(customerModel.getNokIdType());
	    	  accountModel.getNokDetailVOModel().setNokIdNumber(customerModel.getNokIdNumber());
	    	  accountModel.setComments(customerModel.getComments());
	    	  accountModel.setAdditionalDetailVOModel(new AdditionalDetailVOModel());
	    	  accountModel.getAdditionalDetailVOModel().setSecpRegNo(customerModel.getSecpRegNo());
	    	  accountModel.getAdditionalDetailVOModel().setIncorporationDate(customerModel.getIncorporationDate());
	    	  accountModel.getAdditionalDetailVOModel().setMembershipNoTradeBody(customerModel.getMembershipNoTradeBody());
	    	  accountModel.getAdditionalDetailVOModel().setSalesTaxRegNo(customerModel.getSalesTaxRegNo());
	    	  accountModel.getAdditionalDetailVOModel().setRegistrationPlace(customerModel.getRegistrationPlace());
	    	  accountModel.setCnicSeen(customerModel.getIsCnicSeen());
	    	  accountModel.setScreeningPerformed(customerModel.isScreeningPerformed());
	    	  accountModel.setModeOfTx(customerModel.getTransactionModeId()+"");
	    	  accountModel.setAccountPurposeId(customerModel.getAccountPurposeId());
	    	  accountModel.setAccountReason(customerModel.getAccountReasonId()); //added by turab 01-05-2015
	    	  
	    	  // Populating Address Fields
	    	  Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
	    	  if(customerAddresses != null && customerAddresses.size() > 0){
	    		  for(CustomerAddressesModel custAdd : customerAddresses){
	    			  AddressModel addressModel = custAdd.getAddressIdAddressModel();
	    			  	if(null == custAdd.getApplicantTypeId()){
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  accountModel.getNokDetailVOModel().setNokMailingAdd(addressModel.getHouseNo()); 
		    				  }
	    		  		}else if(custAdd.getApplicantTypeId() == 1L){
	    				  if(custAdd.getAddressTypeId() == 1){
		    				  if(addressModel.getCityId() != null){
		    					  accountModel.getApplicant1DetailModel().setResidentialCity(addressModel.getCityId());
		    				  }
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  accountModel.getApplicant1DetailModel().setResidentialAddress(addressModel.getHouseNo()); 
		    				  }
		    			  }else if(custAdd.getAddressTypeId() == 3){
		    				  if(addressModel.getCityId() != null){
		    					  accountModel.getApplicant1DetailModel().setBuisnessCity(addressModel.getCityId());
		    				  }
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  accountModel.getApplicant1DetailModel().setBuisnessAddress(addressModel.getHouseNo()); 
		    				  }
		    			  }
	    			  } else if(custAdd.getApplicantTypeId().longValue() == ApplicantTypeConstants.APPLICANT_TYPE_2){
	    				  if(custAdd.getAddressTypeId().longValue() == AddressTypeConstants.PRESENT_HOME_ADDRESS){
		    				  if(addressModel.getCityId() != null){
		    					  for(ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList()){
		    						  if(custAdd.getApplicantDetailId()!= null && applicant2DetailModel.getApplicantDetailId().equals(custAdd.getApplicantDetailId())){
		    							  applicant2DetailModel.setResidentialCity(addressModel.getCityId());
		    						  }
		    				  		}
		    				  }
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  for(ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList()){
		    						  if(custAdd.getApplicantDetailId()!= null && applicant2DetailModel.getApplicantDetailId().equals(custAdd.getApplicantDetailId()))
		    							  applicant2DetailModel.setResidentialAddress(addressModel.getHouseNo()); 
		    					  }
		    				  }
		    			  }else if(custAdd.getAddressTypeId().longValue() == AddressTypeConstants.BUSINESS_ADDRESS){
		    				  if(addressModel.getCityId() != null){
		    					  for(ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList()){
		    						  if(custAdd.getApplicantDetailId()!= null && applicant2DetailModel.getApplicantDetailId().equals(custAdd.getApplicantDetailId())){
		    							  applicant2DetailModel.setBuisnessCity(addressModel.getCityId());
		    						  }
		    					  }
		    				  }
		    				  if(addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty()){
		    					  for(ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList()){
		    						  if(custAdd.getApplicantDetailId()!= null && applicant2DetailModel.getApplicantDetailId().equals(custAdd.getApplicantDetailId())){
		    							 applicant2DetailModel.setBuisnessAddress(addressModel.getStreetAddress());
		    						  }
		    					  }
		    				  }
		    			  }
	    			  }
	    			  /*else if(custAdd.getApplicantTypeId() == 3L){
	    				  if(custAdd.getAddressTypeId() == 1){
		    				  if(addressModel.getPostalOfficeId() != null){
		    					  accountModel.getApplicant3DetailModel().setPresentAddPostalOfficeId(addressModel.getPostalOfficeId());
		    				  }
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  accountModel.getApplicant3DetailModel().setPresentAddHouseNo(addressModel.getHouseNo()); 
		    				  }
		    			  }else if(custAdd.getAddressTypeId() == 3){
		    				  if(addressModel.getPostalOfficeId() != null){
		    					  accountModel.getApplicant3DetailModel().setBuisnessPostalOfficeId(addressModel.getPostalOfficeId());
		    				  }
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  accountModel.getApplicant3DetailModel().setBuisnessAddress(addressModel.getHouseNo()); 
		    				  }
		    			  }
	    			  }*/
	    			  
	    		  }
	    	  }  	  
	      }
	      
	      req.setAttribute("pageMfsId", (String)baseWrapper.getObject("userId"));
	      req.setAttribute("appUserId", EncryptionUtil.encryptWithDES(accountModel.getAppUserId().toString()));

	      // for the logging process
	      accountModel.setActionId(PortalConstants.ACTION_UPDATE);
	      accountModel.setUsecaseId(new Long(PortalConstants.UPDATE_L2_USECASE_ID));
	      
	      return accountModel;
	    }
	    else
	    {
	        Level2AccountModel accountModel = new Level2AccountModel();
	      //Add 1 object to make sure that table on screen has atleast one row
	    	if( CollectionUtils.isEmpty(accountModel.getAcOwnershipDetailModelList() ))
			{
	    		accountModel.getAcOwnershipDetailModelList().add(new ACOwnershipDetailModel());
			}
	    	if(CollectionUtils.isNotEmpty(kycModelList)){
		    	KYCModel kycModel = kycModelList.get(0);
				accountModel.setEmpId(kycModel.getCreatedByAppUserModel().getEmployeeId());
				accountModel.setEmployeeName(kycModel.getCreatedByAppUserModel().getUsername());
				accountModel.setKycOpeningDate(kycModel.getCreatedOn());
				accountModel.setInitialAppFormNo(kycModel.getInitialAppFormNo());
		      }
	        // for the logging process
	        accountModel.setActionId(PortalConstants.ACTION_CREATE);
	        accountModel.setUsecaseId(new Long(PortalConstants.CREATE_L2_USECASE_ID));
	    	return accountModel;
	    }
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {

		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		Long id = null;
	    if (null != appUserId && appUserId.trim().length() > 0)
	    {
	      try{
    		  id = new Long(EncryptionUtil.decryptWithDES(appUserId)); //if id is coming in encrypted form
	      }catch(Exception e){//id was not in encrypted form, it was plain id
	    	  logger.info("id was not in encrypted form, it was plain id " + id);
	    	  id = new Long(appUserId);
	      }

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
	    
	    SegmentModel segmentModel = new SegmentModel();
	    segmentModel.setIsActive(true);
	    ReferenceDataWrapper segmentReferenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
	    segmentReferenceDataWrapper.setBasePersistableModel(segmentModel);
	    try
	    {
	      referenceDataManager.getReferenceData(segmentReferenceDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    List<SegmentModel> segmentList = null;
	    if (segmentReferenceDataWrapper.getReferenceDataList() != null)
	    {
	    	segmentList = segmentReferenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("segmentList", segmentList);
	    
	    
	    ProductCatalogModel productCatalog = new ProductCatalogModel();
        List<ProductCatalogModel> productCatalogList = null;
        List<ProductCatalogModel> custProductCatalogList = new ArrayList<ProductCatalogModel>() ;
        productCatalog.setActive(true);
        productCatalog.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        ReferenceDataWrapper customerCatalogDataWrapper = new ReferenceDataWrapperImpl(productCatalog, "name", SortingOrder.ASC);
        productCatalogList= (List<ProductCatalogModel>) referenceDataManager.getReferenceData(customerCatalogDataWrapper ).getReferenceDataList();
        //get only customer catalogs.
        if(null != productCatalogList){
        	for(int i = 0; i< productCatalogList.size(); i++){
            	if(productCatalogList.get(i).getAppUserTypeId().equals(UserTypeConstantsInterface.CUSTOMER)){
            		custProductCatalogList.add(productCatalogList.get(i));
            		//custProductCatalogList.add(i, productCatalogList.get(i));
            	}
            }
        }        
        referenceDataMap.put("productCatalogList",custProductCatalogList);
	    

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
	    customerAccountTypeModel.setIsCustomerAccountType(true); //added by Turab
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
	    	if(! CollectionUtils.isEmpty(customerAccountTypeList)){
            	//remove special account types from screen. like settlemnt account type is used for commission settlemnt in OLA and walkin customer. it needs to be removed
            	//because it is for system use only.
            	removeSpecialAccountTypes(customerAccountTypeList);
            }
	    	
	    }
	    referenceDataMap.put("customerAccountTypeList", customerAccountTypeList);

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
	    
	    List<LabelValueBean> genderList = new ArrayList<LabelValueBean>();
	    LabelValueBean gender = new LabelValueBean("Male", "M");
	    genderList.add(gender);
	    gender = new LabelValueBean("Female", "F");
	    genderList.add(gender);
	    gender = new LabelValueBean("Khwaja Sira", "K");
	    genderList.add(gender);
	    referenceDataMap.put("genderList", genderList);
	    
	    
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
	    for (TitleEnum value : TitleEnum.values())
	    {
	     LabelValueBean title = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
	     titleList.add(title);
	    }
	    referenceDataMap.put("titleList", titleList);
	    
	    
	    TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
	    ReferenceDataWrapper taxRegimeDataWrapper = new ReferenceDataWrapperImpl(taxRegimeModel, "name", SortingOrder.ASC);
	    taxRegimeDataWrapper.setBasePersistableModel(taxRegimeModel);
	    try
	    {
	      referenceDataManager.getReferenceData(taxRegimeDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    List<TaxRegimeModel> regimeList = null;
	    if (taxRegimeDataWrapper.getReferenceDataList() != null)
	    {
	    	regimeList = taxRegimeDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("regimeList", regimeList);
	    
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
	    
	    List<LabelValueBean> maritalStatusList = new ArrayList<LabelValueBean>();
	    for (MaritalStatusEnum value : MaritalStatusEnum.values())
	    {
	     LabelValueBean ownerShipType = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
	     maritalStatusList.add(ownerShipType);
	    }
	    referenceDataMap.put("maritalStatusList", maritalStatusList);
	    
	    List<LabelValueBean> ownerShipTypeList = new ArrayList<LabelValueBean>();
	    for (ACOwnershipTypeEnum value : ACOwnershipTypeEnum.values())
	    {
	     LabelValueBean ownerShipType = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
	     ownerShipTypeList.add(ownerShipType);
	    }
	    referenceDataMap.put("ownerShipTypeList", ownerShipTypeList);
	    
	    List<LabelValueBean> documentTypeList = new ArrayList<LabelValueBean>();
	    for (IDDocumentTypeEnum value : IDDocumentTypeEnum.values())
	    {
	     LabelValueBean documentType = new LabelValueBean(value.getName(), String.valueOf(value.getIndex()));
	     documentTypeList.add(documentType);
	    }
	    referenceDataMap.put("documentTypeList", documentTypeList);
	    
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
	    
	    Long[] regStateList = {RegistrationStateConstantsInterface.DECLINE,RegistrationStateConstantsInterface.DISCREPANT,RegistrationStateConstantsInterface.VERIFIED,RegistrationStateConstantsInterface.REJECTED};
	    CustomList<RegistrationStateModel> regStates= commonCommandManager.getRegistrationStateByIds(regStateList);
	    referenceDataMap.put("regStateList", regStates.getResultsetList());
	    
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
	    
	    List<BusinessTypeModel> corporateBusinessTypeList = null;
	    List<BusinessTypeModel> individualBusinessTypeList = null;
	    	
	    if (businessTypeReferenceDataWrapper.getReferenceDataList() != null)
	    {
	    	corporateBusinessTypeList = businessTypeReferenceDataWrapper.getReferenceDataList();
	    	
	    	individualBusinessTypeList=new ArrayList<BusinessTypeModel>();
	    	Iterator<BusinessTypeModel>	businessTypeIterator=corporateBusinessTypeList.iterator();
	    	
	    	while(businessTypeIterator.hasNext()){
	    		
	    		businessTypeModel=businessTypeIterator.next();
	    		if(businessTypeModel.getCategory().equalsIgnoreCase(BusinessTypeCategoryConstants.INDIVIDUAL))
	    		{
	    			individualBusinessTypeList.add(businessTypeModel);
	    			businessTypeIterator.remove();
	    		}
	    	}
	    }
	    referenceDataMap.put("corporateBusinessTypeList", corporateBusinessTypeList);
	    referenceDataMap.put("individualBusinessTypeList", individualBusinessTypeList);
	    
	    
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
		
		
	    return referenceDataMap;
	}
    
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		String mfsAccountId = "";
		Long appUserId = null;
		HttpSession session = req.getSession();
		Level2AccountModel accountModel = (Level2AccountModel) obj;
		try{
			
			if(!validate(accountModel, req)){
				accountModel.setCustomerId(null);
				accountModel.setAppUserId(null);
				accountModel.getApplicant1DetailModel().setApplicantDetailId(null);
		        return super.showForm(req, res, errors);
			}
			
			Date nowDate = new Date();
			
			Level2AccountModel sessionAccountModel = (Level2AccountModel) session.getAttribute("accountModelLevel2");
			if(sessionAccountModel!= null){
				accountModel.setApplicantDetailModelList(sessionAccountModel.getApplicantDetailModelList());
			}
	
			// check main applicant's mobile and id number with other applicants
			if (!this.isCustomerDetailsUnique(accountModel)) {
				super.saveMessage(req,
						"Detail of applicants cannot be same (ID Document Number, Mobile Number)");
				return super.showForm(req, res, errors);
			}
	
			accountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
			accountModel.setCurrency("586");
			accountModel.setCreatedOn(nowDate);
			accountModel.setScreeningPerformed(false);
			baseWrapper.putObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY, accountModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, accountModel.getActionId());
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, accountModel.getUsecaseId());
		    baseWrapper = this.mfsAccountManager.createLevel2Account(baseWrapper);
//		    customerPendingTrxManager.makeCustomerPendingTrx(accountModel.getApplicant1DetailModel().getNic());
		    mfsAccountId = (String)baseWrapper.getObject(PortalConstants.KEY_MFS_ACCOUNT_ID);
		    appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
		}catch(FrameworkCheckedException exception){
			
			String msg = exception.getMessage();
			String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};
			
			if("MobileNumUniqueException".equals(msg)) {				
				this.saveMessage(req, super.getText("newMfsAccount.mobileNumNotUnique2", args, req.getLocale() ));
			} else if("NICUniqueException".equals(msg)){
				this.saveMessage(req, super.getText("newMfsAccount.nicNotUnique2", args, req.getLocale() ));
			} else if(null != msg && msg.equalsIgnoreCase("Pay Shield Service is not available. Please try again later.")){
				this.saveMessage(req, msg);
			} else {
			    this.saveMessage(req, super.getText("newMfsAccount.unknown", req.getLocale() ));
			}
			
			accountModel.setCustomerId(null);
			accountModel.setAppUserId(null);
			accountModel.getApplicant1DetailModel().setApplicantDetailId(null);
			
	        return super.showForm(req, res, errors);
		}    
		catch (Exception exception)
		{	logger.error("Exception Occurred at Level2AccountController.onCreate",exception);
			accountModel.setCustomerId(null);
			accountModel.setAppUserId(null);
			accountModel.getApplicant1DetailModel().setApplicantDetailId(null);
			
			this.saveMessage(req, MessageUtil.getMessage("6075"));
	        return super.showForm(req, res, errors);
		}
		this.saveMessage(req, super.getText("newMfsAccount.recordSaveSuccessful",new Object[]{mfsAccountId} ,req.getLocale() ));
		
		String eappUserId  = EncryptionUtil.encryptWithDES(appUserId.toString());
		
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView()+"&"+PortalConstants.KEY_APP_USER_ID+"="+eappUserId);
		session.removeAttribute("accountModelLevel2");
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		AppUserModel appUserModel = null;
		HttpSession session = req.getSession();
		try{
			
			
			Level2AccountModel accountModel = (Level2AccountModel) obj;
			
			Long appUserId = accountModel.getAppUserId();/*new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(req, "appUserId")));*/
			Long customerAccountTypeId = ServletRequestUtils.getLongParameter(req, "customerAccountTypeId");
			appUserModel = new AppUserModel();
			appUserModel.setAppUserId(appUserId);
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
			appUserModel = (AppUserModel)baseWrapper.getBasePersistableModel();
			if(appUserModel.getNic() == null){
			   	req.setAttribute("nicNullInDB", "true");
			}
			
			Level2AccountModel sessionAccountModel = (Level2AccountModel) session.getAttribute("accountModelLevel2");
			
			if(sessionAccountModel!= null){
				accountModel.setApplicantDetailModelList(sessionAccountModel.getApplicantDetailModelList());
			}
			
			if(!validate(accountModel, req)){
				req.setAttribute("exceptionOccured", "true");
		        return super.showForm(req, res, errors);
			}
			
			// check main applicant's mobile and id number with other applicants
			if (!this.isCustomerDetailsUnique(accountModel)) {
				super.saveMessage(req,
						"Detail of applicants cannot be same (ID Document Number, Mobile Number)");
				return super.showForm(req, res, errors);
			}
	
			
			accountModel.setAppUserId(appUserId);
			accountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
			accountModel.setCustomerAccountTypeId(customerAccountTypeId);
			accountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
			
			SearchBaseWrapper searchWrapper = new SearchBaseWrapperImpl();
			ACOwnershipDetailModel acOwnerShipDetailModel = new ACOwnershipDetailModel();
			acOwnerShipDetailModel.setIsDeleted(false);
			acOwnerShipDetailModel.setCustomerId(appUserModel.getCustomerId());
			searchWrapper.setBasePersistableModel(acOwnerShipDetailModel);
			List<ACOwnershipDetailModel> existingOwners = mfsAccountManager.loadAccountOwnerShipDetails(searchWrapper);
			
			baseWrapper.putObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY, accountModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, accountModel.getActionId() );
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, accountModel.getUsecaseId() );
			
			baseWrapper.putObject("existingOwners", (Serializable) existingOwners);
		    baseWrapper = this.mfsAccountManager.updateLevel2Account(baseWrapper);
		    
		}catch(FrameworkCheckedException exception){
			req.setAttribute("exceptionOccured", "true");
			if(appUserModel.getUsername() != null)
			   req.setAttribute("pageMfsId", appUserModel.getUsername());
			
			String msg = exception.getMessage();
			String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};
			
			if("MobileNumUniqueException".equals(msg)) {				
				this.saveMessage(req, super.getText("newMfsAccount.mobileNumNotUnique2", args, req.getLocale() ));
			} else if("NICUniqueException".equals(msg)){
				this.saveMessage(req, super.getText("newMfsAccount.nicNotUnique2", args, req.getLocale() ));
			} else {
			    this.saveMessage(req, super.getText("newMfsAccount.unknown", req.getLocale() ));
			}
			
	        return super.showForm(req, res, errors);
		}
		catch(Exception exception){
			req.setAttribute("exceptionOccured", "true");
			if(appUserModel.getUsername() != null)
			   req.setAttribute("pageMfsId", appUserModel.getUsername());
			
			this.saveMessage(req,MessageUtil.getMessage("6075"));
	        return super.showForm(req, res, errors);
		}
		this.saveMessage(req, super.getText("newMfsAccount.recordUpdateSuccessful", req.getLocale() ));
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_pgsearchuserinfo.html?actionId=3") );
		session.removeAttribute("accountModelLevel2");    
		return modelAndView;
	}

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		HttpSession session = request.getSession();
		Level2AccountModel accountModel = (Level2AccountModel) command;
		
		Level2AccountModel sessionAccountModel = (Level2AccountModel) session.getAttribute("accountModelLevel2");
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

		if(accountModel.getUsecaseId().longValue()== PortalConstants.CREATE_L2_USECASE_ID)
		{
			accountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
			accountModel.setCreatedOn(new Date());
			if(!validate(accountModel, request)){
		        return super.showForm(request, response, errors);
			}
		}
		else
		{
			if(null!=appUserIdStr  && appUserIdStr.trim().length() > 0){
				if (!resubmitRequest)
					appUserId = accountModel.getAppUserId();
				else
					appUserId = Long.parseLong(appUserIdStr) ;
			}
			
			customerAccountTypeId = ServletRequestUtils.getLongParameter(request, "customerAccountTypeId");
			appUserModel = new AppUserModel();
			appUserModel.setAppUserId(appUserId);
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
			appUserModel = (AppUserModel)baseWrapper.getBasePersistableModel();
			if(appUserModel.getNic() == null){
			   	request.setAttribute("nicNullInDB", "true");
			}
			
			accountModel.setAppUserId(appUserId);
			accountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
			accountModel.setCustomerAccountTypeId(customerAccountTypeId);
			accountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
			
			if(!validate(accountModel, request)){
		        return super.showForm(request, response, errors);
			}
		}
		
		try
		{								
			XStream xstream = new XStream();
			
			Level2AccountModel level2AccountModelAuth = (Level2AccountModel) accountModel.clone();
			
			for(ApplicantDetailModel model : level2AccountModelAuth.getApplicantDetailModelList()){
				model.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				model.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			}

			String refDataModelString= xstream.toXML(level2AccountModelAuth);
		
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(accountModel.getUsecaseId());
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(accountModel.getUsecaseId(),new Long(0));
			
			if(nextAuthorizationLevel.intValue()<1){
				
			if(!this.isCustomerDetailsUnique(accountModel)){
				super.saveMessage(request, "Detail of applicants cannot be same (ID Document Number, Mobile Number)" );
				return super.showForm(request, response, errors);
				}
			
				
				baseWrapper.putObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY, accountModel);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, accountModel.getActionId());
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, accountModel.getUsecaseId());
				
			    if(accountModel.getUsecaseId().longValue()== PortalConstants.CREATE_L2_USECASE_ID){
			    	baseWrapper = this.mfsAccountManager.createLevel2Account(baseWrapper);
			    	mfsAccountId = (String)baseWrapper.getObject(PortalConstants.KEY_MFS_ACCOUNT_ID);
					appUserId = new Long(baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID).toString());
			    }
			    else
			    {
					SearchBaseWrapper searchWrapper = new SearchBaseWrapperImpl();
					ACOwnershipDetailModel acOwnerShipDetailModel = new ACOwnershipDetailModel();
					acOwnerShipDetailModel.setIsDeleted(false);
					acOwnerShipDetailModel.setCustomerId(Long.parseLong(sessionAccountModel.getCustomerId()));
					searchWrapper.setBasePersistableModel(acOwnerShipDetailModel);
					List<ACOwnershipDetailModel> existingOwners = mfsAccountManager.loadAccountOwnerShipDetails(searchWrapper);
					
					baseWrapper.putObject("existingOwners", (Serializable) existingOwners);
			    	baseWrapper = this.mfsAccountManager.updateLevel2Account(baseWrapper);
			    }
			    				
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString,null,usecaseModel,actionAuthorizationId,request);
				
				if(accountModel.getUsecaseId().longValue()== PortalConstants.UPDATE_L2_USECASE_ID){
					
					this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale() )
							+"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
					modelAndView = new ModelAndView(new RedirectView("p_pgsearchuserinfo.html?actionId=3") );
				}
				else
				{
					this.saveMessage(request, super.getText("newMfsAccount.recordSaveSuccessful",new Object[]{mfsAccountId} ,request.getLocale() )
							+"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
					String eappUserId  = EncryptionUtil.encryptWithDES(appUserId.toString());
					modelAndView = new ModelAndView(this.getSuccessView()+"&"+PortalConstants.KEY_APP_USER_ID+"="+eappUserId);
				}
			}
			else
			{	
				AppUserModel aum = new AppUserModel();
				if(usecaseModel.getUsecaseId().longValue()== PortalConstants.CREATE_L2_USECASE_ID && accountModel.getApplicant1DetailModel() != null){
					aum.setMobileNo(accountModel.getMobileNo());
					aum.setNic(accountModel.getApplicant1DetailModel().getIdNumber());
					this.mfsAccountManager.isUniqueCNICMobile(aum,baseWrapper);
					
					//check main applicant's mobile and id number with other applicants
					if(!this.isCustomerDetailsUnique(accountModel)){
								super.saveMessage(request, "Detail of applicants cannot be same (ID Document Number, Mobile Number)" );
								return super.showForm(request, response, errors);
						}
					}
				
				
				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,"", refDataModelString,null, usecaseModel.getUsecaseId(),accountModel.getInitialAppFormNo(),resubmitRequest,actionAuthorizationId,request);
				this.saveMessage(request,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
				modelAndView = new ModelAndView(new RedirectView("p_pgsearchuserinfo.html?actionId=3") );
			}
				
			////Saving Cutomer Pictures 
			CustomerModel customerModel =null;
			if(null!=appUserModel){////appUserModel is not null only in update scenario
				customerModel = appUserModel.getCustomerIdCustomerModel();
			}        
		}
		catch (FrameworkCheckedException exception)
		{	
			String msg = exception.getMessage();
			String[] args = {(String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME), (String) baseWrapper.getObject(PortalConstants.KEY_MFS_ID)};
			
			if("MobileNumUniqueException".equals(msg)) {
				this.saveMessage(request, super.getText("newMfsAccount.mobileNumNotUnique2", args, request.getLocale() ));
			} else if("NICUniqueException".equals(msg)){
				this.saveMessage(request, super.getText("newMfsAccount.nicNotUnique2", args, request.getLocale() ));
			} else {
			    this.saveMessage(request, msg);
			}
	        return super.showForm(request, response, errors);
		}
		catch (Exception exception)
		{	
			this.saveMessage(request, MessageUtil.getMessage("6075"));
	        return super.showForm(request, response, errors);
		}
		
		return modelAndView;
	}
    
	private void removeSpecialAccountTypes(CopyOnWriteArrayList<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList){

		// Iterator<OlaCustomerAccountTypeModel> it =
		// olaCustomerAccountTypeModelList.iterator();
		// So far only one special account type exists which is SETTLEMENT (id =
		// 3L)
		for (OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList) {
			if (model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.LEVEL_0
					|| model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.LEVEL_1
					|| model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT
					|| model.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER) {
				olaCustomerAccountTypeModelList.remove(model);
			}
		}
	}

	
	private boolean isCustomerDetailsUnique(Level2AccountModel accountModel){
		//add applicants id type and no.
		boolean result = true;
		List<String> list = new ArrayList<String>();
		for(ApplicantDetailModel model : accountModel.getApplicantDetailModelList()){
			list.add(model.getIdType()+":"+model.getIdNumber());
			list.add("mob:"+model.getMobileNo());
		}
		
		//add applicant 1 id type and no.
		list.add(accountModel.getApplicant1DetailModel().getIdType()+":"+accountModel.getApplicant1DetailModel().getIdNumber());
		if(accountModel.getApplicant1DetailModel()!= null && accountModel.getMobileNo() != null){
			list.add("mob:"+accountModel.getMobileNo());
		}
		if(accountModel.getNokDetailVOModel() != null && accountModel.getNokDetailVOModel().getNokIdType() != null){
			list.add(accountModel.getNokDetailVOModel().getNokIdType()+":"+accountModel.getNokDetailVOModel().getNokIdNumber());
		}
		if(accountModel.getNokDetailVOModel() != null && accountModel.getNokDetailVOModel().getNokContactNo() != null){
			list.add("mob:"+accountModel.getNokDetailVOModel().getNokContactNo());
		}
				
		//add account ownership id type and no.
		for(int i=0; i< accountModel.getAcOwnershipDetailModelList().size(); i++){
			list.add(accountModel.getAcOwnershipDetailModelList().get(i).getIdDocumentType()+":"+accountModel.getAcOwnershipDetailModelList().get(i).getIdDocumentNo());
		}
		
		//now check if there are two same entries then return false;
		
		for(int i=0; i< list.size(); i++){
			if(result==false)
				break;
			for(int j= i+1; j < list.size(); j++){
				if(list.get(i).equalsIgnoreCase(list.get(j))){
					result=false;
					break;
				}
			}
		}
		
		return result;
	}
	
	private boolean validate(Level2AccountModel accountModel, HttpServletRequest request)
	{
		boolean flag=true;
		if(accountModel.getAppUserId()!=null && accountModel.getRegistrationStateId()==null){
			this.saveMessage(request,"Registration State: is required.");
			flag=false;
		}
		//***************************************************************************************************************************
		//									Check if cnic is blacklisted
		//***************************************************************************************************************************       
		if(accountModel.getApplicant1DetailModel() != null && accountModel.getApplicant1DetailModel().getIdType().longValue()==IDDocumentTypeEnum.CNIC.getIndex() && accountModel.getApplicant1DetailModel().getIdNumber() != null && this.commonCommandManager.isCnicBlacklisted(accountModel.getApplicant1DetailModel().getIdNumber())){
			this.saveMessage(request, "Black Listed CNIC is provided.");
			flag=false;
		}		
		if(accountModel.getNokDetailVOModel() != null 
				&& accountModel.getNokDetailVOModel().getNokIdNumber() != null 
				&& accountModel.getNokDetailVOModel().getNokIdType() != null
				&& IDDocumentTypeEnum.CNIC.getIndex() == accountModel.getNokDetailVOModel().getNokIdType() 
				&& this.commonCommandManager.isCnicBlacklisted(accountModel.getNokDetailVOModel().getNokIdNumber())){
			this.saveMessage(request, "Black Listed Next of Kin CNIC is provided.");
			flag=false;
		}
		//***************************************************************************************************************************
		return flag;
	}
	

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}


	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setUserDeviceAccountListViewManager(
			UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}
	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		if (commonCommandManager != null) {
			this.commonCommandManager = commonCommandManager;
		}
	}
}
