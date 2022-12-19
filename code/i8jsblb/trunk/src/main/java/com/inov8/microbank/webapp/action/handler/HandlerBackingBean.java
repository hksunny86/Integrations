
package com.inov8.microbank.webapp.action.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.verifly.common.model.AccountInfoModel;

/**
 * @author Kashif Bashir
 *
 */

public class HandlerBackingBean implements Serializable {

	public static final String BEAN_NAME = "handlerBackingBean";
	private static final long serialVersionUID = 1L;

	private AppUserModel appUserModel = new AppUserModel();
	private HandlerModel handlerModel = new HandlerModel();
	private UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
	private AccountInfoModel accountInfoModel = new AccountInfoModel();
	private SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel(); 

	private List<DistributorModel> distributorList = new ArrayList<DistributorModel>(0);
	private List<SelectItem> regionList = new ArrayList<SelectItem>(0);
	private List<SelectItem> retailerList = new ArrayList<SelectItem>(0);
	private List<SelectItem> productCatalogList = new ArrayList<SelectItem>(0);
	private List<SelectItem> customerAccountTypeList = new ArrayList<SelectItem>(0);

	private Long distributorId;
	private Long regionId;
	private Long retailerContactId;
	private String password; 
	private String plainLoginPin;
	private String plainTransactionPin;
	private Long productCatalogId;
	private String agentName;
	private String agentMfsId;
	
	public Long getProductCatalogId() {
		return productCatalogId;
	}

	public void setProductCatalogId(Long productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	public String getPlainLoginPin() {
		return plainLoginPin;
	}

	public void setPlainLoginPin(String plainLoginPin) {
		this.plainLoginPin = plainLoginPin;
	}

	public String getPlainTransactionPin() {
		return plainTransactionPin;
	}

	public void setPlainTransactionPin(String plainTransactionPin) {
		this.plainTransactionPin = plainTransactionPin;
	}


	private Long customerAccountTypeId;
	private Boolean readOnly = Boolean.FALSE;
	
	
	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}

	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Long getRetailerContactId() {
		return retailerContactId;
	}

	public void setRetailerContactId(Long retailerContactId) {
		this.retailerContactId = retailerContactId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public HandlerBackingBean() {

	}
		
	public List<SelectItem> getRetailerList() {
		return retailerList;
	}

	public void setRetailerList(List<SelectItem> retailerList) {
		this.retailerList = retailerList;
	}

	public List<SelectItem> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<SelectItem> regionList) {
		this.regionList = regionList;
	}

	public List<DistributorModel> getDistributorList() {
		return distributorList;
	}

	public void setDistributorList(List<DistributorModel> distributorList) {
		this.distributorList = distributorList;
	}

	public AppUserModel getAppUserModel() {
		return appUserModel;
	}

	public void setAppUserModel(AppUserModel appUserModel) {
		this.appUserModel = appUserModel;
	}
	public HandlerModel getHandlerModel() {
		return handlerModel;
	}

	public void setHandlerModel(HandlerModel handlerModel) {
		this.handlerModel = handlerModel;
	}
	public List<SelectItem> getProductCatalogList() {
		return productCatalogList;
	}

	public void setProductCatalogList(List<SelectItem> productCatalogList) {
		this.productCatalogList = productCatalogList;
	}

	public UserDeviceAccountsModel getUserDeviceAccountsModel() {
		return userDeviceAccountsModel;
	}
	public void setUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
		this.userDeviceAccountsModel = userDeviceAccountsModel;
	}

	public AccountInfoModel getAccountInfoModel() {
		return accountInfoModel;
	}

	public void setAccountInfoModel(AccountInfoModel accountInfoModel) {
		this.accountInfoModel = accountInfoModel;
	}

	public SmartMoneyAccountModel getSmartMoneyAccountModel() {
		return smartMoneyAccountModel;
	}

	public void setSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
		this.smartMoneyAccountModel = smartMoneyAccountModel;
	}

	public List<SelectItem> getCustomerAccountTypeList() {
		return customerAccountTypeList;
	}

	public void setCustomerAccountTypeList(List<SelectItem> customerAccountTypeList) {
		this.customerAccountTypeList = customerAccountTypeList;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentMfsId() {
		return agentMfsId;
	}

	public void setAgentMfsId(String agentMfsId) {
		this.agentMfsId = agentMfsId;
	}
}