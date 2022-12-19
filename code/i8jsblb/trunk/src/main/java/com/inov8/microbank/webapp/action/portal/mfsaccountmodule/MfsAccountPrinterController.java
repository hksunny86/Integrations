package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;


import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CustomerAddressesModel;
import com.inov8.microbank.common.model.CustomerFundSourceModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;

public class MfsAccountPrinterController extends AdvanceFormController{

	private MfsAccountManager mfsAccountManager;
	private FinancialIntegrationManager financialIntegrationManager;
	private ReferenceDataManager 		referenceDataManager;

	public MfsAccountPrinterController() {
		setCommandName("mfsAccountModel");
	    setCommandClass(MfsAccountModel.class);
	}


	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		BankModel bankModel = new BankModel();
		bankModel.setBankId(CommissionConstantsInterface.BANK_ID);
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
		boolean veriflyRequired = abstractFinancialInstitution.isVeriflyRequired();
		req.setAttribute("veriflyRequired", veriflyRequired);
		
		Long id = null;
	    if (null != appUserId && appUserId.trim().length() > 0)
	    {
	      id = new Long(EncryptionUtil.decryptWithDES(appUserId));	
	      BaseWrapper baseWrapper = new BaseWrapperImpl();
	      AppUserModel appUserModel = new AppUserModel();
          appUserModel.setAppUserId(id);
          baseWrapper.setBasePersistableModel(appUserModel);
	      this.mfsAccountManager.logPrintAction(id);
	      baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
	      appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
	      
	      MfsAccountModel mfsAccountModel = new MfsAccountModel();

	      //*** START - check if customer is created by agent??
	      AppUserModel createdByAppUserModel = appUserModel.getCreatedByAppUserModel();
	      if(createdByAppUserModel != null && 
	    		  createdByAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()){
		      UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(createdByAppUserModel.getAppUserId(),null);
	    	  if(deviceAccountModel != null && deviceAccountModel.getUserDeviceAccountsId() != null){
		    	  mfsAccountModel.setAllpayId(deviceAccountModel.getUserId());
	    	  }
	      }
	      //*** END - check if customer is created by agent

	      mfsAccountModel.setAddress1(appUserModel.getAddress1());
	      mfsAccountModel.setAddress2(appUserModel.getAddress2());
	      mfsAccountModel.setFirstName(appUserModel.getFirstName());
	      mfsAccountModel.setLastName(appUserModel.getLastName());
	      mfsAccountModel.setCity(appUserModel.getCity());
	      mfsAccountModel.setState(appUserModel.getState());
	      mfsAccountModel.setZongNo(appUserModel.getMobileNo());
	      mfsAccountModel.setAppUserId(appUserModel.getAppUserId());
	      mfsAccountModel.setConnectionType(appUserModel.getMobileTypeId());
	      
	      mfsAccountModel.setNic(getNicWithHyphins(appUserModel.getNic()));
	      mfsAccountModel.setNicExpiryDate(appUserModel.getNicExpiryDate());
	      mfsAccountModel.setState(appUserModel.getState());
	      mfsAccountModel.setCountry(appUserModel.getCountry());
	      	      
	      mfsAccountModel.setDialingCode( appUserModel.getMotherMaidenName() ) ;
	      
	      mfsAccountModel.setDob( appUserModel.getDob() );
	      mfsAccountModel.setSearchFirstName(ServletRequestUtils.getStringParameter(req, "searchFirstName"));
	      mfsAccountModel.setSearchLastName(ServletRequestUtils.getStringParameter(req, "searchLastName"));
	      mfsAccountModel.setSearchMfsId(ServletRequestUtils.getStringParameter(req, "searchMfsId"));
	      mfsAccountModel.setSearchNic(ServletRequestUtils.getStringParameter(req, "searchNic"));
	      
	      
	      CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
	      if(customerModel != null){
		      UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(id,DeviceTypeConstantsInterface.MOBILE);
	    	  if(deviceAccountModel != null && deviceAccountModel.getUserDeviceAccountsId() != null){
	    		  // Set Device Account ID
	    		  mfsAccountModel.setCustomerId(deviceAccountModel.getUserId());
	    		  //req.setAttribute("deviceAccId", deviceAccountModel.getUserDeviceAccountsId());
	    		  //req.setAttribute("deviceAccEnabled", deviceAccountModel.getAccountEnabled());
	    		  //req.setAttribute("deviceAccLocked", deviceAccountModel.getAccountLocked());
	    	  }

	    	  
	    	  
	    	  mfsAccountModel.setApplicationNo(customerModel.getApplicationN0());
	    	  mfsAccountModel.setApplicationDate(customerModel.getCreatedOn());
	    	  mfsAccountModel.setName(customerModel.getName());
	    	  mfsAccountModel.setFatherHusbandName(customerModel.getFatherHusbandName());
	    	  
	    	  if(customerModel.getRelationAskari().equals(1)){
	    		  mfsAccountModel.setAskariRelation(true);
	    	  }else{
	    		  mfsAccountModel.setAskariRelation(false);
	    	  }
	    	  if(customerModel.getRelationZong().equals(1)){
	    		  mfsAccountModel.setZongRelation(true);
	    	  }else{
	    		  mfsAccountModel.setZongRelation(false);
	    	  }
	    	  if(customerModel.getGender().equals("M")){
	    		  mfsAccountModel.setGender("Male");
	    	  }else{
	    		  mfsAccountModel.setGender("Female");
	    	  }
	    	  mfsAccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
	    	  mfsAccountModel.setCustomerAccountName(customerModel.getCustomerTypeIdCustomerTypeModel().getName());
	    	  mfsAccountModel.setEmail(customerModel.getEmail());
	    	  mfsAccountModel.setContactNo(customerModel.getContactNo());
	    	  mfsAccountModel.setLandLineNo(customerModel.getLandLineNo());
	    	  mfsAccountModel.setMobileNo(customerModel.getMobileNo());
	    	  mfsAccountModel.setRefferedBy(customerModel.getReferringName1());
	    	  mfsAccountModel.setFundSourceNarration(customerModel.getFundsSourceNarration());
	    	  mfsAccountModel.setLanguageId(customerModel.getLanguageId());
	    	  mfsAccountModel.setLanguageName(customerModel.getLanguageIdLanguageModel().getName());
	    	  mfsAccountModel.setSegmentId(customerModel.getSegmentId());
	    	  mfsAccountModel.setSegmentName(customerModel.getSegmentIdSegmentModel().getName());
	    	  mfsAccountModel.setCustomerTypeId(customerModel.getCustomerTypeId());
	    	  mfsAccountModel.setTypeOfCustomerName(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
	    	  mfsAccountModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
	    	  mfsAccountModel.setEmployeeID(customerModel.getEmployeeId());
	    	  mfsAccountModel.setFundSourceOthers(customerModel.getOtherFundSource());
	    	  
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
			  	mfsAccountModel.setFundSourceName(fundSourceName);
	    	  
	    	  // Populating Address Fields
	    	  Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
	    	  if(customerAddresses != null && customerAddresses.size() > 0){
	    		  for(CustomerAddressesModel custAdd : customerAddresses){
	    			  AddressModel addressModel = custAdd.getAddressIdAddressModel();
	    			  if(custAdd.getAddressTypeId() == 1){
	    				  if(addressModel.getCityId() != null){
	    					  mfsAccountModel.setPresentAddCityId(addressModel.getCityId());
	    					  mfsAccountModel.setPresentHomeAddCityName(addressModel.getCityIdCityModel().getName());
	    				  }else if(addressModel.getOtherCity() != null && !addressModel.getOtherCity().isEmpty()){
	    					  mfsAccountModel.setPresentCityOthers(addressModel.getOtherCity());  
	    				  }
	    				  if(addressModel.getDistrictId() != null){
	    					  mfsAccountModel.setPresentAddDistrictId(addressModel.getDistrictId());
	    					  mfsAccountModel.setPresentHomeAddDistrictName(addressModel.getDistrictIdDistrictModel().getName());
	    				  }else if(addressModel.getOtherDistrict() != null && !addressModel.getOtherDistrict().isEmpty()){
	    					  mfsAccountModel.setPresentDistOthers(addressModel.getOtherDistrict());  
	    				  }
	    				  if(addressModel.getPostalOfficeId() != null){
	    					  mfsAccountModel.setPresentAddPostalOfficeId(addressModel.getPostalOfficeId());
	    					  mfsAccountModel.setPresentHomeAddPostalOfficeName(addressModel.getPostalOfficeIdPostalOfficeModel().getName());
	    				  }else if(addressModel.getOtherPostalOffice() != null && !addressModel.getOtherPostalOffice().isEmpty()){
	    					  mfsAccountModel.setPresentPostalOfficeOthers(addressModel.getOtherPostalOffice());  
	    				  }
	    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
	    					  mfsAccountModel.setPresentAddHouseNo(addressModel.getHouseNo()); 
	    				  }
	    				  if(addressModel.getStreetNo() != null && !addressModel.getStreetNo().isEmpty()){
	    					  mfsAccountModel.setPresentAddStreetNo(addressModel.getStreetNo()); 
	    				  }

	    			  }else if(custAdd.getAddressTypeId() == 2){
	    				  if(addressModel.getCityId() != null){
	    					  mfsAccountModel.setPermanentAddCityId(addressModel.getCityId());
	    					  mfsAccountModel.setPermanentHomeAddCityName(addressModel.getCityIdCityModel().getName());
	    				  }else if(addressModel.getOtherCity() != null && !addressModel.getOtherCity().isEmpty()){
	    					  mfsAccountModel.setPermanentAddCityOthers(addressModel.getOtherCity());  
	    				  }
	    				  if(addressModel.getDistrictId() != null){
	    					  mfsAccountModel.setPermanentAddDistrictId(addressModel.getDistrictId());
	    					  mfsAccountModel.setPermanentHomeAddDistrictName(addressModel.getDistrictIdDistrictModel().getName());
	    				  }else if(addressModel.getOtherDistrict() != null && !addressModel.getOtherDistrict().isEmpty()){
	    					  mfsAccountModel.setPermanentDistOthers(addressModel.getOtherDistrict());  
	    				  }
	    				  if(addressModel.getPostalOfficeId() != null){
	    					  mfsAccountModel.setPermanentAddPostalOfficeId(addressModel.getPostalOfficeId());
	    					  mfsAccountModel.setPermanentHomeAddPostalOfficeName(addressModel.getPostalOfficeIdPostalOfficeModel().getName());
	    				  }else if(addressModel.getOtherPostalOffice() != null && !addressModel.getOtherPostalOffice().isEmpty()){
	    					  mfsAccountModel.setPermanentPostalOfficeOthers(addressModel.getOtherPostalOffice());  
	    				  }
	    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
	    					  mfsAccountModel.setPermanentAddHouseNo(addressModel.getHouseNo()); 
	    				  }
	    				  if(addressModel.getStreetNo() != null && !addressModel.getStreetNo().isEmpty()){
	    					  mfsAccountModel.setpermanentAddStreetNo(addressModel.getStreetNo()); 
	    				  }
	    			  }
	    		  }
	    	  }
	    	  
	    	  
	    	  
	    	  
	      }
	      
	      req.setAttribute("pageMfsId", (String)baseWrapper.getObject("userId"));
	      // for the logging process
	      mfsAccountModel.setActionId(PortalConstants.ACTION_UPDATE);
	      mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID));
	      
	      return mfsAccountModel;
	    }
	    else
	    {
	        MfsAccountModel mfsAccountModel = new MfsAccountModel();
	        // for the logging process
	        mfsAccountModel.setActionId(PortalConstants.ACTION_CREATE);
	        mfsAccountModel.setUsecaseId(new Long(PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID));
	    	return mfsAccountModel;
	    }
		
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
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}
	
	
	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		if (referenceDataManager != null) {
			this.referenceDataManager = referenceDataManager;
		}
	}

}
