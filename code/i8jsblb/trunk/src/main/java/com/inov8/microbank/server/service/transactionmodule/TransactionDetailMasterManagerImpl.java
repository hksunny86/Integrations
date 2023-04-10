package com.inov8.microbank.server.service.transactionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: The pupose of this class is to Insert and update the the record
 * in Transaction and Transaction Detail Master
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: inov8 Limited
 * </p>
 * 
 * @author Syed Ahmad Bilal
 * @version 1.0
 */
public class TransactionDetailMasterManagerImpl implements TransactionDetailMasterManager
{
	private TransactionDetailMasterDAO transactionDetailMasterDAO;
	private AppUserManager appUserManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private ProductManager productManager;
	private DeviceTypeManager deviceTypeManager;

	protected final transient Log logger = LogFactory.getLog(TransactionDetailMasterManagerImpl.class);

	@Override
	public BaseWrapper saveTransactionDetailMasterRequiresNewTransaction(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		TransactionDetailMasterModel transactionDetailMasterModel = (TransactionDetailMasterModel)baseWrapper.getBasePersistableModel();
		transactionDetailMasterModel = transactionDetailMasterDAO.saveOrUpdate(transactionDetailMasterModel);
		baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
		return baseWrapper;
	}
	
	public void saveTransactionDetailMaster(TransactionDetailMasterModel transactionDetailMasterModel) throws FrameworkCheckedException
	{
		transactionDetailMasterDAO.saveOrUpdate(transactionDetailMasterModel);
	}

	
	public BaseWrapper loadTransactionDetailMasterModel(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		TransactionDetailMasterModel transactionDetailMasterModel = (TransactionDetailMasterModel)baseWrapper.getBasePersistableModel();

		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.TRUE);
		exampleHolder.setMatchMode(MatchMode.EXACT);

		CustomList<TransactionDetailMasterModel> list = this.transactionDetailMasterDAO.findByExample(transactionDetailMasterModel, null, null, exampleHolder);
		
		if(null != list.getResultsetList() && list.getResultsetList().size() > 0 ) {
			
			transactionDetailMasterModel = list.getResultsetList().get(0);		
			baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
		}
		
		return baseWrapper;
	}

	@Override
	public TransactionDetailMasterModel loadTransactionDetailMasterModel(String transactionCode) throws FrameworkCheckedException {
		TransactionDetailMasterModel tdm = new TransactionDetailMasterModel();
		tdm.setTransactionCode(transactionCode);

//		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
//		exampleHolder.setEnableLike(Boolean.TRUE);
//		exampleHolder.setMatchMode(MatchMode.EXACT);
//
		tdm = this.transactionDetailMasterDAO.findTrxByTransactionCodeAndStatus(transactionCode);

		return tdm;
	}

	public BaseWrapper loadAndLockTransactionDetailMasterModel(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return transactionDetailMasterDAO.loadAndLockTransactionDetailMasterModel(baseWrapper);
	}

	public String loadSmartMoneyAccountNickBySmartMoneyAccountId(Long smartMoneyAccountId) throws FrameworkCheckedException{
		logger.info("[TransactionDetailMasterManagerImpl.loadSmartMoneyAccountNickBySmartMoneyAccountId] smartMoneyAccountId: " + smartMoneyAccountId);
		String nick = null;
		SmartMoneyAccountModel smaModel = new SmartMoneyAccountModel();
		smaModel.setSmartMoneyAccountId(smartMoneyAccountId);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(smaModel);
		
		baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
		
		if(baseWrapper != null && baseWrapper.getBasePersistableModel() != null){
			smaModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
			nick = smaModel.getName();
		}
		
		return nick;
	}
	
	public String getAgentMobileByRetailerContactId(Long retailerContactId) throws FrameworkCheckedException{
		logger.info("[TransactionDetailMasterManagerImpl.getAgentMobileByRetailerContactId] RetailerContactId: " + retailerContactId);
		String mobileNo = null;
		AppUserModel example = new AppUserModel();
		example.setRetailerContactId(retailerContactId);
		example = appUserManager.getAppUserModel(example);
		if (example != null) {
			mobileNo = example.getMobileNo();
		}
		
		return mobileNo;
	}
	
	public String loadAgentId(Long retailerContactId) throws FrameworkCheckedException {
		logger.info("[TransactionDetailMasterManagerImpl.loadAgentId] RetailerContactId: " + retailerContactId);
		String userId = null;
		AppUserModel appUser = new AppUserModel();
		appUser.setRetailerContactId(retailerContactId);
		appUser = appUserManager.getAppUserModel(appUser);
		if (appUser != null && appUser.getAppUserId() != null) {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			UserDeviceAccountsModel deviceAccountModel = new UserDeviceAccountsModel();
			deviceAccountModel.setAppUserId(appUser.getAppUserId());
			baseWrapper.setBasePersistableModel(deviceAccountModel);
			baseWrapper = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			deviceAccountModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
			if(deviceAccountModel != null && deviceAccountModel.getUserId() != null){
				userId = deviceAccountModel.getUserId();
			}
		}
		
		return userId;
	}
	
	public String loadUserIdByMobileNo(String mobileNo) throws FrameworkCheckedException {
		logger.info("[TransactionDetailMasterManagerImpl.loadAgentId] Mobile NO: " + mobileNo);
		String userId = null;
		AppUserModel appUser = new AppUserModel();
		appUser.setMobileNo(mobileNo);
		appUser = appUserManager.getAppUserModel(appUser);
		if (appUser != null && appUser.getAppUserId() != null) {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			UserDeviceAccountsModel deviceAccountModel = new UserDeviceAccountsModel();
			deviceAccountModel.setAppUserId(appUser.getAppUserId());
			baseWrapper.setBasePersistableModel(deviceAccountModel);
			baseWrapper = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			deviceAccountModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
			if(deviceAccountModel != null && deviceAccountModel.getUserId() != null){
				userId = deviceAccountModel.getUserId();
			}
		}
		
		return userId;
	}
	
	public AppUserModel getAppUserByMobile(String mobileNO) throws FrameworkCheckedException{
		logger.info("[TransactionDetailMasterManagerImpl.getAppUserByMobile] mobile No:" + mobileNO);
		AppUserModel appUserModel = new AppUserModel();
		appUserModel = appUserManager.loadAppUserByMobileByQuery(mobileNO);
		
		return appUserModel;
	}
	
	public ProductModel getProductModelByProductId(Long productId) throws FrameworkCheckedException{
		logger.info("[TransactionDetailMasterManagerImpl.getProductModelByProductId] product Id: " + productId);
		ProductModel productModel = new ProductModel();
		productModel.setProductId(productId);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(productModel);
		baseWrapper = productManager.loadProduct(baseWrapper);
		productModel = (ProductModel)baseWrapper.getBasePersistableModel();
		if(productModel != null && productModel.getProductId() != null){
			SupplierModel supplierModel = productModel.getSupplierIdSupplierModel();
			logger.debug("supplier ID:" + supplierModel.getSupplierId());
		}
		return productModel;
	}
	
	public String getDeviceTypeNameById(Long deviceTypeId) throws FrameworkCheckedException{
		String name = null;
		DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
		deviceTypeModel.setDeviceTypeId(deviceTypeId);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(deviceTypeModel);
		
		deviceTypeManager.loadDeviceType(baseWrapper);
		deviceTypeModel = (DeviceTypeModel)baseWrapper.getBasePersistableModel();
		if(deviceTypeModel != null && deviceTypeModel.getName() != null){
			name = deviceTypeModel.getName();
		}
		
		return name;
	}
	
	public Integer updateTransactionProcessingStatus(List<Long> transactionId, Long transactionProcessingStatus, String processingStatusName){
		return transactionDetailMasterDAO.updateTransactionProcessingStatus(transactionId, transactionProcessingStatus, processingStatusName);
	}
	
	public Integer updateTransactionDetailMasterForLeg2(TransactionDetailMasterModel model){
		return transactionDetailMasterDAO.updateTransactionDetailMasterForLeg2(model);
	}

	public void updateTransactionDetailMaster(TransactionDetailMasterModel tdm) throws FrameworkCheckedException
	{
	 this.transactionDetailMasterDAO.saveOrUpdate(tdm) ;

	}

	@Override
	public int findAgentPendingTrxByCNIC(String agentCNIC) {
		return transactionDetailMasterDAO.findAgentPendingTrxByCNIC(agentCNIC);
	}

	@Override
	public int getCustomerChallanCount(String mobileNo) {
		return transactionDetailMasterDAO.getCustomerChallanCount(mobileNo);
	}

	@Override
	public long getPaidChallan(String consumerNo,String productCode) {
		return transactionDetailMasterDAO.getPaidChallan(consumerNo,productCode);
	}

	@Override
	public TransactionDetailMasterModel loadTransactionDetailMasterModelByRRN(String rrn) throws FrameworkCheckedException {
		TransactionDetailMasterModel tdm = new TransactionDetailMasterModel();
		tdm.setFonepayTransactionCode(rrn);

//		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
//		exampleHolder.setEnableLike(Boolean.TRUE);
//		exampleHolder.setMatchMode(MatchMode.EXACT);
//
		tdm = this.transactionDetailMasterDAO.loadTDMbyRRN(rrn);

		return tdm;
	}

	@Override
	public TransactionDetailMasterModel loadTDMByReserved2(String reserved2) throws FrameworkCheckedException {
		TransactionDetailMasterModel tdm = new TransactionDetailMasterModel();
		tdm.setReserved2(reserved2);

//		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
//		exampleHolder.setEnableLike(Boolean.TRUE);
//		exampleHolder.setMatchMode(MatchMode.EXACT);
//
		tdm = this.transactionDetailMasterDAO.loadTDMbyReserved2(reserved2);

		return tdm;
	}

	@Override
	public TransactionDetailMasterModel loadTDMbyMobileNumber(String mobileNo, String productId) throws FrameworkCheckedException {
		TransactionDetailMasterModel tdm = new TransactionDetailMasterModel();
		tdm = this.transactionDetailMasterDAO.loadTDMbyMobileNumber(mobileNo, productId);

		return tdm;
	}

	@Override
	public TransactionDetailMasterModel loadCurrentTDMbyMobileNumber(String mobileNo, String productId) throws FrameworkCheckedException {
		TransactionDetailMasterModel tdm = new TransactionDetailMasterModel();
		tdm = this.transactionDetailMasterDAO.loadCurrentTDMbyMobileNumber(mobileNo, productId);

		return tdm;
	}

	@Override
	public TransactionDetailMasterModel loadTDMbyProductId(String mobileNo, String productId) throws FrameworkCheckedException {
		TransactionDetailMasterModel tdm = new TransactionDetailMasterModel();
		tdm = this.transactionDetailMasterDAO.loadTDMbyProductId(mobileNo, productId);

		return tdm;
	}

//	@Override
//	public List<TransactionDetailMasterModel> loadTDMbyMobileandDateRange(String mobileNo, String startDate, String endDate) throws FrameworkCheckedException {
//		TransactionDetailMasterModel tdm = new TransactionDetailMasterModel();
//		tdm = this.transactionDetailMasterDAO.loadTDMbyMobileandDateRange(mobileNo, startDate, endDate);
//
//		return tdm;
//	}

	public void setTransactionDetailMasterDAO(
			TransactionDetailMasterDAO transactionDetailMasterDAO) {
		this.transactionDetailMasterDAO = transactionDetailMasterDAO;
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

	@Override
	public BaseWrapper saveTransactionDetailMaster(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		TransactionDetailMasterModel transactionDetailMasterModel = (TransactionDetailMasterModel)baseWrapper.getBasePersistableModel();
		transactionDetailMasterModel = transactionDetailMasterDAO.saveOrUpdate(transactionDetailMasterModel);
		baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
		return baseWrapper;
	}

}