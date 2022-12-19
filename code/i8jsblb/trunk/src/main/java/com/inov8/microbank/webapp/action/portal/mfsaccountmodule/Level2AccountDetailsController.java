package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserHistoryViewModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.CustomerAddressesModel;
import com.inov8.microbank.common.model.CustomerFundSourceModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.OccupationModel;
import com.inov8.microbank.common.model.ProfessionModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.AdditionalDetailVOModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.BusinessDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.Level2AccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.NokDetailVOModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.util.AccountStateConstantsInterface;
import com.inov8.microbank.common.util.ApplicantTypeConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IDDocumentTypeEnum;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.ledger.LedgerManager;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;

public class Level2AccountDetailsController extends AdvanceFormController{

	private MfsAccountManager 			mfsAccountManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private AppUserManager 				appUserManager;
    //added by turab
    private AccountManager 				accountManager;
    private LedgerManager 				ledgerManager;
    private LimitManager 				limitManager;
    private ReferenceDataManager 		referenceDataManager;

	public Level2AccountDetailsController() {
		setCommandName("level2AccountModel");
	    setCommandClass(Level2AccountModel.class);
	}
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		Double remainingDailyCreditLimit 	= 0.0;
		Double remainingDailyDebitLimit 	= 0.0;
		Double remainingMonthlyCreditLimit 	= 0.0;
		Double remainingMonthlyDebitLimit 	= 0.0;
		Double remainingYearlyCreditLimit 	= 0.0;
		Double remainingYearlyDebitLimit 	= 0.0;
		
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		BankModel bankModel = new BankModel();
		bankModel.setBankId(CommissionConstantsInterface.BANK_ID);
		baseWrapperBank.setBasePersistableModel(bankModel);

    	AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
    	boolean veriflyRequired = true;
    	try{
    		veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}   
    	req.setAttribute("veriflyRequired", veriflyRequired);

		 Long id = new Long(EncryptionUtil.decryptWithDES(appUserId));	
	     BaseWrapper baseWrapper = new BaseWrapperImpl();
	     AppUserModel appUserModel = new AppUserModel();
         appUserModel.setAppUserId(id);
         baseWrapper.setBasePersistableModel(appUserModel);
	     baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
	      
	     appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
	     Level2AccountModel level2AccountModel = new Level2AccountModel();

	      try{
	    	  OLAVO olaVo = this.mfsAccountManager.getAccountInfoFromOLA(appUserModel.getNic(),bankModel.getBankId());
		      if(olaVo != null){
			      level2AccountModel.setAccountNo(olaVo.getPayingAccNo());
//			      level2AccountModel.setAccountStatus(olaVo.getStatusName());
			      level2AccountModel.setAccountBalance(olaVo.getBalance());
		      }
	      }catch(Exception ex){
	    	  log.warn("Exception while getting customer info from OLA: "+ex.getStackTrace());
	      }

	      
		baseWrapper = new BaseWrapperImpl();
		CustomerModel customer = new CustomerModel();
		customer.setCustomerId(appUserModel.getCustomerId());
		baseWrapper.setBasePersistableModel(customer);
		baseWrapper = mfsAccountManager.loadCustomer(baseWrapper);
		customer = (CustomerModel) baseWrapper.getBasePersistableModel();
		String initialAppFormNo = customer.getInitialApplicationFormNumber();

		level2AccountModel.setInitialAppFormNo(initialAppFormNo);
		level2AccountModel.setMobileNo(appUserModel.getMobileNo());
		level2AccountModel.setAppUserId(appUserModel.getAppUserId());
		level2AccountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
		level2AccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
      level2AccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
      level2AccountModel.setMfsId(appUserModel.getUsername());
      
      if(null!=appUserModel.getClosedByAppUserModel()) {
    	  level2AccountModel.setClosedBy(appUserModel.getClosedByAppUserModel().getUsername());
      }
      level2AccountModel.setClosedOn(appUserModel.getClosedOn());
      
      if(null!=appUserModel.getSettledByAppUserModel()) {
    	  level2AccountModel.setSettledBy(appUserModel.getSettledByAppUserModel().getUsername());
      }
      
      level2AccountModel.setSettledOn(appUserModel.getSettledOn());
      level2AccountModel.setClosingComments(appUserModel.getClosingComments());
      level2AccountModel.setSettlementComments(appUserModel.getSettlementComments());
      level2AccountModel.setSearchFirstName(ServletRequestUtils.getStringParameter(req, "searchFirstName"));
      level2AccountModel.setSearchLastName(ServletRequestUtils.getStringParameter(req, "searchLastName"));
      level2AccountModel.setSearchMfsId(ServletRequestUtils.getStringParameter(req, "searchMfsId"));
      level2AccountModel.setSearchNic(ServletRequestUtils.getStringParameter(req, "searchNic"));
      
      
      try{
    	  OLAVO olaVo = this.mfsAccountManager.getAccountInfoFromOLA(appUserModel.getNic(),bankModel.getBankId());
	      if(olaVo != null){
	    	  level2AccountModel.setAccountNo(olaVo.getPayingAccNo());
	      }
      }catch(Exception ex){
    	  log.warn("Exception while getting customer info from OLA: "+ex.getStackTrace());
      }
      
      CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
      level2AccountModel.setCustomerId(customerModel.getCustomerId().toString());
      level2AccountModel.setFed(customerModel.getFed());
      level2AccountModel.setTaxRegimeName(customerModel.getTaxRegimeIdTaxRegimeModel() == null ? "" : customerModel.getTaxRegimeIdTaxRegimeModel().getName());
      level2AccountModel.setIsVeriSysDone(customerModel.getVerisysDone());
      level2AccountModel.setCustomerAccountName(customerModel.getName());
      
      if(customerModel != null){
    	  
    	  ApplicantDetailModel applicantModel = new ApplicantDetailModel();
    	  applicantModel.setCustomerId(customerModel.getCustomerId());
    	  
    	  applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
    	  level2AccountModel.setApplicant1DetailModel(mfsAccountManager.loadApplicantDetail(applicantModel));
    	  if(level2AccountModel.getApplicant1DetailModel() != null){
    		  Long idType = level2AccountModel.getApplicant1DetailModel().getIdType();
    		  for (IDDocumentTypeEnum value : IDDocumentTypeEnum.values())
    		    {
    			  if(null != idType && value.getIndex() == idType.longValue()){
    				  level2AccountModel.getApplicant1DetailModel().setIdTypeName(value.getName());
    			  }
    		    }
    	  }
    	  
		Map referenceDataMap = new HashMap();
	    OccupationModel occupationModel = new OccupationModel();
	    if(level2AccountModel.getApplicant1DetailModel().getOccupation()!=null)
	    {
	    	occupationModel.setOccupationId(level2AccountModel.getApplicant1DetailModel().getOccupation());
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
		    
		    if (occupationReferenceDataWrapper.getReferenceDataList() != null)
		    {
		    	occupationModel = (OccupationModel) occupationReferenceDataWrapper.getReferenceDataList().get(0);
		    }
		    level2AccountModel.getApplicant1DetailModel().setOccupationName(occupationModel.getName()); 
	    }
	    
    	  
	    ProfessionModel professionModel = new ProfessionModel();
	    if(level2AccountModel.getApplicant1DetailModel().getProfession()!=null)
	    {
		    professionModel.setProfessionId(level2AccountModel.getApplicant1DetailModel().getProfession());
		    ReferenceDataWrapper proffesionReferenceDataWrapper = new ReferenceDataWrapperImpl(professionModel, "name", SortingOrder.ASC);
		    proffesionReferenceDataWrapper.setBasePersistableModel(professionModel);
		    try
		    {
		      referenceDataManager.getReferenceData(proffesionReferenceDataWrapper);
		    }
		    catch (Exception e)
		    {
		    	logger.error(e.getMessage(), e);
		    }
		    
		    if (proffesionReferenceDataWrapper.getReferenceDataList() != null)
		    {
		    	professionModel = (ProfessionModel) proffesionReferenceDataWrapper.getReferenceDataList().get(0);
		    }
		    level2AccountModel.getApplicant1DetailModel().setProfessionName(professionModel.getName()); 
	    }
	    
	    CityModel birthCityModel = new CityModel();
	    
	    if(level2AccountModel.getApplicant1DetailModel().getBirthPlace()!=null)
	    {
	    	birthCityModel.setCityId(Long.parseLong(level2AccountModel.getApplicant1DetailModel().getBirthPlace()));
		    ReferenceDataWrapper birthPlaceReferenceDataWrapper = new ReferenceDataWrapperImpl(birthCityModel, "name", SortingOrder.ASC);
		    birthPlaceReferenceDataWrapper.setBasePersistableModel(birthCityModel);
		    try
		    {
		      referenceDataManager.getReferenceData(birthPlaceReferenceDataWrapper);
		    }
		    catch (Exception e)
		    {
		    	logger.error(e.getMessage(), e);
		    }
		    
		    if (birthPlaceReferenceDataWrapper.getReferenceDataList() != null)
		    {
		    	birthCityModel = (CityModel) birthPlaceReferenceDataWrapper.getReferenceDataList().get(0);
		    }
		    level2AccountModel.getApplicant1DetailModel().setBirthPlaceName(birthCityModel.getName());
	    	  
	    }
	    
	    CityModel residentialCityModel = new CityModel();
	    if(level2AccountModel.getApplicant1DetailModel().getResidentialCity()!=null)
	    {
		    residentialCityModel.setCityId(level2AccountModel.getApplicant1DetailModel().getResidentialCity());
		    ReferenceDataWrapper residentialCityReferenceDataWrapper = new ReferenceDataWrapperImpl(residentialCityModel, "name", SortingOrder.ASC);
		    residentialCityReferenceDataWrapper.setBasePersistableModel(residentialCityModel);
		    try
		    {
		      referenceDataManager.getReferenceData(residentialCityReferenceDataWrapper);
		    }
		    catch (Exception e)
		    {
		    	logger.error(e.getMessage(), e);
		    }
		    
		    if (residentialCityReferenceDataWrapper.getReferenceDataList() != null)
		    {
		    	residentialCityModel = (CityModel) residentialCityReferenceDataWrapper.getReferenceDataList().get(0);
		    }
		    level2AccountModel.getApplicant1DetailModel().setResidentialCityName(residentialCityModel.getName());
	    }
	    
	    if(level2AccountModel.getApplicant1DetailModel().getBuisnessCity()!=null)
	    {
		    CityModel businessCityModel = new CityModel();
		    businessCityModel.setCityId(level2AccountModel.getApplicant1DetailModel().getBuisnessCity());
		    ReferenceDataWrapper businessCityReferenceDataWrapper = new ReferenceDataWrapperImpl(residentialCityModel, "name", SortingOrder.ASC);
		    businessCityReferenceDataWrapper.setBasePersistableModel(businessCityModel);
		    try
		    {
		      referenceDataManager.getReferenceData(businessCityReferenceDataWrapper);
		    }
		    catch (Exception e)
		    {
		    	logger.error(e.getMessage(), e);
		    }
		    
		    if (businessCityReferenceDataWrapper.getReferenceDataList() != null)
		    {
		    	businessCityModel = (CityModel) businessCityReferenceDataWrapper.getReferenceDataList().get(0);
		    }
		    level2AccountModel.getApplicant1DetailModel().setBuisnessCityName(businessCityModel.getName());
		 }

    	  BusinessDetailModel businessModel = new BusinessDetailModel();
    	  businessModel.setCustomerId(customerModel.getCustomerId());
    	  level2AccountModel.setBusinessDetailModel(mfsAccountManager.loadBusinessDetail(businessModel));

    	  level2AccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
    	  level2AccountModel.setAccounttypeName(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
    	  level2AccountModel.setMobileNo(customerModel.getMobileNo());
    	  level2AccountModel.setAcNature(customerModel.getAcNature());
    	  level2AccountModel.setAccountNature(customerModel.getAccountNature());
    	  level2AccountModel.setTypeOfAccount(customerModel.getTypeOfAccount());
    	  level2AccountModel.setSegmentName(customerModel.getSegmentIdSegmentModel().getName());
    	  level2AccountModel.setCurrency(customerModel.getCurrency());
    	  
    	  
    	  level2AccountModel.setCustomerTypeId(customerModel.getCustomerTypeId());		
    	  level2AccountModel.setRegStateComments(customerModel.getRegStateComments());
    	  level2AccountModel.setCreatedOn(customerModel.getCreatedOn());
    	  level2AccountModel.setBusinessTypeId(customerModel.getBusinessTypeId());
    	  level2AccountModel.setNokDetailVOModel(new NokDetailVOModel());
    	  level2AccountModel.getNokDetailVOModel().setNokName(customerModel.getNokName());
    	  level2AccountModel.getNokDetailVOModel().setNokContactNo(customerModel.getNokContactNo());
    	  level2AccountModel.getNokDetailVOModel().setNokRelationship(customerModel.getNokRelationship());  
    	  level2AccountModel.getNokDetailVOModel().setNokMobile(customerModel.getNokMobile());
    	  level2AccountModel.getNokDetailVOModel().setNokIdType(customerModel.getNokIdType());
    	  level2AccountModel.getNokDetailVOModel().setNokIdNumber(customerModel.getNokIdNumber());
    	  level2AccountModel.setComments(customerModel.getComments());
    	  level2AccountModel.setAdditionalDetailVOModel(new AdditionalDetailVOModel());
    	  level2AccountModel.getAdditionalDetailVOModel().setSecpRegNo(customerModel.getSecpRegNo());
    	  level2AccountModel.getAdditionalDetailVOModel().setIncorporationDate(customerModel.getIncorporationDate());
    	  level2AccountModel.getAdditionalDetailVOModel().setMembershipNoTradeBody(customerModel.getMembershipNoTradeBody());
    	  level2AccountModel.getAdditionalDetailVOModel().setSalesTaxRegNo(customerModel.getSalesTaxRegNo());
    	  level2AccountModel.getAdditionalDetailVOModel().setRegistrationPlace(customerModel.getRegistrationPlace());
    	  level2AccountModel.setCnicSeen(customerModel.getIsCnicSeen());
    	  level2AccountModel.setScreeningPerformed(customerModel.isScreeningPerformed());
    	  level2AccountModel.setModeOfTx(customerModel.getTransactionModeId()+"");
    	  if(customerModel.getAccountPurposeIdAccountPurposeModel() != null){
    		  level2AccountModel.setAccountPurpose(customerModel.getAccountPurposeIdAccountPurposeModel().getName());
    	  }
    	  level2AccountModel.setAccountReason(customerModel.getAccountReasonId()); //added by turab 01-05-2015
    	  
    	  // Populating Address Fields
    	  Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
    	  if(customerAddresses != null && customerAddresses.size() > 0){
    		  for(CustomerAddressesModel custAdd : customerAddresses){
    			  AddressModel addressModel = custAdd.getAddressIdAddressModel();
    			  	if(null == custAdd.getApplicantTypeId()){
	    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
	    					  level2AccountModel.getNokDetailVOModel().setNokMailingAdd(addressModel.getHouseNo()); 
	    				  }
    		  		}else if(custAdd.getApplicantTypeId() == 1L){
    				  if(custAdd.getAddressTypeId() == 1){
	    				  if(addressModel.getCityId() != null){
	    					  level2AccountModel.getApplicant1DetailModel().setResidentialCity(addressModel.getCityId());
	    				  }
	    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
	    					  level2AccountModel.getApplicant1DetailModel().setResidentialAddress(addressModel.getHouseNo()); 
	    				  }
	    			  }else if(custAdd.getAddressTypeId() == 3){
	    				  if(addressModel.getCityId() != null){
	    					  level2AccountModel.getApplicant1DetailModel().setBuisnessCity(addressModel.getCityId());
	    				  }
	    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
	    					  level2AccountModel.getApplicant1DetailModel().setBuisnessAddress(addressModel.getHouseNo()); 
	    				  }
	    			  }
    			  }
    		  }
    	  }  	  
      }
		

	      level2AccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
	      level2AccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
	      if(null!=appUserModel.getClosedByAppUserModel())
	      {
		      level2AccountModel.setClosedBy(appUserModel.getClosedByAppUserModel().getUsername());
	      }
	      level2AccountModel.setClosedOn(appUserModel.getClosedOn());
	      if(null!=appUserModel.getSettledByAppUserModel())
	      {
	    	  level2AccountModel.setSettledBy(appUserModel.getSettledByAppUserModel().getUsername());
	      }
	      level2AccountModel.setSettledOn(appUserModel.getSettledOn());
	      level2AccountModel.setClosingComments(appUserModel.getClosingComments());
	      level2AccountModel.setSettlementComments(appUserModel.getSettlementComments());

	      String nic = appUserModel.getNic();
	      List<AppUserHistoryViewModel> appUserHistoryViewModelList = searchAppUserHistoryView(nic);
	      req.setAttribute("appUserHistoryViewModelList", appUserHistoryViewModelList);
	      
	      level2AccountModel.setSearchFirstName(ServletRequestUtils.getStringParameter(req, "searchFirstName"));
	      level2AccountModel.setSearchLastName(ServletRequestUtils.getStringParameter(req, "searchLastName"));
	      level2AccountModel.setSearchMfsId(ServletRequestUtils.getStringParameter(req, "searchMfsId"));
	      level2AccountModel.setSearchNic(ServletRequestUtils.getStringParameter(req, "searchNic"));

	      if(customerModel != null){
	    	  if(appUserModel.getAccountStateId() != null){
	    		  if(appUserModel.getAccountStateId().equals(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED)){
	    			  level2AccountModel.setAccountState("Closed");
	    		  }else if(appUserModel.getAccountStateId().equals(AccountStateConstantsInterface.ACCOUNT_STATE_COLD)){
	    			  level2AccountModel.setAccountState("Cold");
	    		  }else if(appUserModel.getAccountStateId().equals(AccountStateConstantsInterface.ACCOUNT_STATE_DECEASED)){
	    			  level2AccountModel.setAccountState("Deceased");
	    		  }else if(appUserModel.getAccountStateId().equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)){
	    			  level2AccountModel.setAccountState("Dormant");
	    		  }else if(appUserModel.getAccountStateId().equals(AccountStateConstantsInterface.ACCOUNT_STATE_HOT)){
	    			  level2AccountModel.setAccountState("Hot");
	    		  }else if(appUserModel.getAccountStateId().equals(AccountStateConstantsInterface.ACCOUNT_STATE_WARM)){
	    			  level2AccountModel.setAccountState("Warm");
	    		  }
	    	  }
	    	  
	    	  AccountModel accountModel = accountManager.getAccountModelByCNIC(level2AccountModel.getApplicant1DetailModel().getIdNumber());
	    	  if(accountModel != null){
		    	  Date currentDate = new Date();
		    	  Calendar cal = GregorianCalendar.getInstance();
		    	  //daily debit consumed
		    	  Double dailyDebitConsumed = ledgerManager.getDailyConsumedBalance(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, currentDate);
		    	  //daily credit consumed
		    	  Double dailyCreditConsumed = ledgerManager.getDailyConsumedBalance(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, currentDate);
		    	//monthly debit consumed
		          cal.setTime(new Date());
		          cal.set(Calendar.DAY_OF_MONTH, 1);
		          Date startDate = cal.getTime();

		          Double monthlyDebitConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, startDate, currentDate);
		          //monthly credit consumed
		          Double monthlyCreditConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, startDate, currentDate);
		          
		          //yearly debit consumed
		          cal.setTime(new Date());
		          cal.set(Calendar.DAY_OF_MONTH, 1);
		          cal.set(Calendar.MONTH,0);
		          startDate = cal.getTime();
		          
		          Double yearlyDebitConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, startDate, currentDate);
		          //yearly credit consumed
		          Double yearlyCreditConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, startDate, currentDate);
	    	  
		          List<LimitModel> limits = limitManager.getLimitsByCustomerAccountType(customerModel.getCustomerAccountTypeId());
	    	  
	    	  
	    	  for (LimitModel limitModel : limits) {
	    		  if( limitModel.getLimitTypeId().equals(LimitTypeConstants.DAILY) ){
	    			  if(limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)){
	    				  remainingDailyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - dailyCreditConsumed;
	    				  remainingDailyCreditLimit = remainingDailyCreditLimit < 0 ? 0 : remainingDailyCreditLimit;
	    			  }else{
	    				  remainingDailyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - dailyDebitConsumed;
	    				  remainingDailyDebitLimit = (remainingDailyDebitLimit < 0 ? 0 : remainingDailyDebitLimit);
	    			  }
	    		  }else if(limitModel.getLimitTypeId().equals(LimitTypeConstants.MONTHLY)){
	    			  if(limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)){
	    				  remainingMonthlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - monthlyCreditConsumed;
	    				  remainingMonthlyCreditLimit = remainingMonthlyCreditLimit < 0 ? 0 : remainingMonthlyCreditLimit;
	    			  }else{
	    				  remainingMonthlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - monthlyDebitConsumed;
	    				  remainingMonthlyDebitLimit = remainingMonthlyDebitLimit < 0 ? 0 : remainingMonthlyDebitLimit;
	    			  }
	    		  }else if(limitModel.getLimitTypeId().equals(LimitTypeConstants.YEARLY)){
	    			  if(limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)){
	    				  remainingYearlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - yearlyCreditConsumed;
	    				  remainingYearlyCreditLimit = remainingYearlyCreditLimit < 0 ? 0 : remainingYearlyCreditLimit;
	    			  }else{
	    				  remainingYearlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - yearlyDebitConsumed;
	    				  remainingYearlyDebitLimit = remainingYearlyDebitLimit < 0 ? 0 : remainingYearlyDebitLimit;
	    			  }
	    		  }
			}
	    	  
	    	  NumberFormat formatter = new DecimalFormat("#0.00");
	    	  level2AccountModel.setRemainingDailyDebitLimit(formatter.format(remainingDailyDebitLimit));
	    	  level2AccountModel.setRemainingDailyCreditLimit(formatter.format(remainingDailyCreditLimit));
	    	  level2AccountModel.setRemainingMonthlyCreditLimit(formatter.format(remainingMonthlyCreditLimit));
	    	  level2AccountModel.setRemainingMonthlyDebitLimit(formatter.format(remainingMonthlyDebitLimit));
	    	  level2AccountModel.setRemainingYearlyCreditLimit(formatter.format(remainingYearlyCreditLimit));
	    	  level2AccountModel.setRemainingYearlyDebitLimit(formatter.format(remainingYearlyDebitLimit));
    	  }else{
    		  super.saveMessage(req, "Could not retreive remaining limits for this customer");
    	  }
	    	  /*******************************************************end by turab***************************************************/
	    	  
	    	  SmartMoneyAccountModel smartMoneyAccount = this.mfsAccountManager.getSmartMoneyAccountByCustomerId(customerModel.getCustomerId());
	    	  if(smartMoneyAccount != null && smartMoneyAccount.getSmartMoneyAccountId() != null){
	    		  req.setAttribute("smAccountId", smartMoneyAccount.getSmartMoneyAccountId());
	    	  }
		      UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(id,DeviceTypeConstantsInterface.MOBILE);
	    	  if(deviceAccountModel != null && deviceAccountModel.getUserDeviceAccountsId() != null){
	    		  // Set Device Account ID
	    		  level2AccountModel.setCustomerId(deviceAccountModel.getUserId());
	    		  req.setAttribute("deviceAccId", deviceAccountModel.getUserDeviceAccountsId());
	    		  req.setAttribute("deviceAccEnabled", deviceAccountModel.getAccountEnabled());
	    		  req.setAttribute("deviceAccLocked", deviceAccountModel.getAccountLocked());
	    		  req.setAttribute("credentialsExpired", deviceAccountModel.getCredentialsExpired());
	    		  
	    		  String statusDetails = appUserManager.getStatusDetails(appUserModel.getAppUserId(),
	    				  deviceAccountModel.getUpdatedOn(),
	    				  deviceAccountModel.getAccountLocked(),
	    				  deviceAccountModel.getAccountEnabled()
	    				  );
	    		  req.setAttribute("statusDetails", statusDetails);

	    	  }
		      level2AccountModel.setApplicationNo(customerModel.getApplicationN0());
	    	  level2AccountModel.setApplicationDate(customerModel.getCreatedOn());
	    	  
	    	  if(customerModel.getGender() != null){
		    	  if(customerModel.getGender().equals("M")){
		    		  level2AccountModel.getApplicant1DetailModel().setGender("Male");
		    	  }else if(customerModel.getGender().equals("F")){
		    		  level2AccountModel.getApplicant1DetailModel().setGender("Female");
		    	  }else if(customerModel.getGender().equals("K")){
		    		  level2AccountModel.getApplicant1DetailModel().setGender("Khwaja Sira");
		    	  }
	    	  }
	    	  level2AccountModel.setAccounttypeName(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
	    	  if(customerModel.getCustomerTypeIdCustomerTypeModel()!=null){
	    		  level2AccountModel.setCustomerAccountName(customerModel.getCustomerTypeIdCustomerTypeModel().getName());
	    	  }
	    	  level2AccountModel.getApplicant1DetailModel().setEmail(customerModel.getEmail());
	    	  level2AccountModel.setCreatedOn(customerModel.getCreatedOn());
	    	  
	    	  UserInfoListViewModel userInfoListViewModel = new UserInfoListViewModel();
	    	  userInfoListViewModel.setAppUserId(appUserModel.getAppUserId());
		      BaseWrapper baseWrapperUserInfo = new BaseWrapperImpl();
		      baseWrapperUserInfo.setBasePersistableModel(userInfoListViewModel);
		      baseWrapperUserInfo = mfsAccountManager.searchUserInfoByPrimaryKey(baseWrapperUserInfo);
		      userInfoListViewModel = (UserInfoListViewModel) baseWrapperUserInfo.getBasePersistableModel();
		      if(userInfoListViewModel != null){
		    	  level2AccountModel.setCreatedBy(userInfoListViewModel.getAccountOpenedBy());
		      }
	    	  
		      level2AccountModel.setLocation(mfsAccountManager.getAreaByAppUserId(customerModel.getCreatedBy()));
		      
	    	  level2AccountModel.setMobileNo(customerModel.getMobileNo());
	    	  level2AccountModel.setRefferedBy(customerModel.getReferringName1());
	    	  if(customerModel.getFundSourceIdFundSourceModel()!=null){
	    		  level2AccountModel.setFundSourceNarration(customerModel.getFundSourceIdFundSourceModel().getName());
	    	  }
	    	  
	    	  level2AccountModel.setSegmentId(customerModel.getSegmentId());
	    	  if(customerModel.getSegmentIdSegmentModel()!=null){
	    		  level2AccountModel.setSegmentName(customerModel.getSegmentIdSegmentModel().getName());
	    	  }
	    	  level2AccountModel.setCustomerTypeId(customerModel.getCustomerTypeId());
	    	  if(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel()!=null){
	    		  level2AccountModel.setTypeOfCustomerName(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
	    	  }
	    	  level2AccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());

		    /***
		     * Populating Customer Source of Funds
		    */
		    	  
		    CustomerFundSourceModel customerFundSourceModel = new CustomerFundSourceModel();
		    customerFundSourceModel.setCustomerId(customerModel.getCustomerId());
		    ReferenceDataWrapper customerFundSourceReferenceDataWrapper = new ReferenceDataWrapperImpl(customerFundSourceModel, "customerFundSourceId", SortingOrder.ASC);
		    customerFundSourceReferenceDataWrapper.setBasePersistableModel(customerFundSourceModel);
		  	try {
		  		referenceDataManager.getReferenceData(customerFundSourceReferenceDataWrapper);
		  	} catch (Exception e) {
		  		logger.error(e.getMessage(), e);
		  	}
		  	    
		  	List<CustomerFundSourceModel> customerFundSourceList = null;
		  	if (customerFundSourceReferenceDataWrapper.getReferenceDataList() != null) {
		  		customerFundSourceList = customerFundSourceReferenceDataWrapper.getReferenceDataList();
		  	}
		  	String[] fundSourceName = new String[customerFundSourceList.size()];
		  	int i = 0;
		  	for(CustomerFundSourceModel customerFundSource : customerFundSourceList) {
		  		fundSourceName[i] = customerFundSource.getFundSourceIdFundSourceModel().getName();
		  		i++;
		  	}
	    	  
	    	  // Populating Address Fields
	    	  Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
	    	  if(customerAddresses != null && customerAddresses.size() > 0){
	    		  for(CustomerAddressesModel custAdd : customerAddresses){
	    			  AddressModel addressModel = custAdd.getAddressIdAddressModel();
	    			  	if(null == custAdd.getApplicantTypeId()){
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  level2AccountModel.getNokDetailVOModel().setNokMailingAdd(addressModel.getHouseNo()); 
		    				  }
	    		  		}else if(custAdd.getApplicantTypeId() == 1L){
	    				  if(custAdd.getAddressTypeId() == 1){
		    				  if(addressModel.getCityId() != null){
		    					  level2AccountModel.getApplicant1DetailModel().setResidentialCity(addressModel.getCityId());
		    				  }
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  level2AccountModel.getApplicant1DetailModel().setResidentialAddress(addressModel.getHouseNo()); 
		    				  }
		    			  }else if(custAdd.getAddressTypeId() == 3){
		    				  if(addressModel.getCityId() != null){
		    					  level2AccountModel.getApplicant1DetailModel().setBuisnessCity(addressModel.getCityId());
		    				  }
		    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
		    					  level2AccountModel.getApplicant1DetailModel().setBuisnessAddress(addressModel.getHouseNo()); 
		    				  }
		    			  }
	    			  }
	    		  }
	    	  } 	    	  
	      }
	      
	      req.setAttribute("pageMfsId", (String)baseWrapper.getObject("userId"));
	      return level2AccountModel;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {
		return null;
	}
    
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		ModelAndView modelAndView = new ModelAndView(getSuccessView() );
		    
		return modelAndView;
	}

	private List<AppUserHistoryViewModel> searchAppUserHistoryView(String nic) throws FrameworkCheckedException
	{
		nic = nic.substring(nic.indexOf("-") + 1);
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

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setLedgerManager(LedgerManager ledgerManager) {
		this.ledgerManager = ledgerManager;
	}

	public void setLimitManager(LimitManager limitManager) {
		this.limitManager = limitManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		if (referenceDataManager != null) {
			this.referenceDataManager = referenceDataManager;
		}
	}

}
