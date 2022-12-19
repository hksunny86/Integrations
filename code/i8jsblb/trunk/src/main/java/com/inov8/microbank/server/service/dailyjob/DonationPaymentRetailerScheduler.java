package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.PoolAccountConstantsInterface.DONATION_COLLECTION_SUNDRY_ACCOUNT_ID;
import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;

public class DonationPaymentRetailerScheduler extends QuartzJobBean {
	
	private final SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("EEE.MMMMM.dd.yyyy.hh:mm:ss.aaa");
	private static Logger logger = Logger.getLogger(DonationPaymentRetailerScheduler.class);
	
	private StakeholderBankInfoManager stakeholderBankInfoManager = null;
	private TransactionModuleManager transactionModuleManager = null;
	private SupplierBankInfoManager supplierBankInfoManager = null;
	private SwitchController switchController = null;


	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		long start = System.currentTimeMillis();

		logger.info(":-Started Donation Payment Retailer Scheduler");
		
		try {

			process();

		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		
		logger.info(":-Ended Donation.Payment. Retailer Scheduler Took : "+ ((System.currentTimeMillis() - start)/1000) + " Second(s), Next Date/Time to Fire will : "+simpleDateFormat.format(context.getNextFireTime()));
	}

	
	private void process() throws FrameworkCheckedException {
		
		List<Object[]> transactionSupplierList = transactionModuleManager.getDonationTransactionList(TransactionTypeConstantsInterface.DONATION_PAYMENT_TX, SupplierProcessingStatusConstants.BANK_SETTLEMENT_PENDING, ServiceTypeConstantsInterface.SERVICE_TYPE_DONATION_RETAILER_PAYMENT);
		
		Double supplierAmountCurrent = 0.0D;
		ProductModel supplierProductModel = new ProductModel();
		supplierProductModel.setSupplierId(0L);
		
		StakeholderBankInfoModel stakeholderBankInfoModel = getStakeholderBankInfoModel(DONATION_COLLECTION_SUNDRY_ACCOUNT_ID);
		
		List<TransactionModel> transactionModelList = new ArrayList<TransactionModel>(0);
		
		int totalTransaction = transactionSupplierList.size()-1;
		int i = 0;
		
		for(Object[] transactionSupplier : transactionSupplierList) {
				
			TransactionModel transactionModel = (TransactionModel) transactionSupplier[0];
			ProductModel productModel = (ProductModel) transactionSupplier[1];
			
			if(supplierProductModel.getSupplierId().longValue() == productModel.getSupplierId().longValue()) {
					
				supplierAmountCurrent += (transactionModel.getTotalAmount() - transactionModel.getTotalCommissionAmount());
				supplierProductModel = productModel;
					
				transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
					
				transactionModelList.add(transactionModel);
				
				if(totalTransaction == i) {
					
					if(supplierAmountCurrent.doubleValue() > 0.0D) {
							
						debitCreditAccount(supplierProductModel, supplierAmountCurrent, transactionModelList, stakeholderBankInfoModel);
					}		
						
				}
				
			} else {
				
				if(supplierAmountCurrent.doubleValue() > 0.0D) {
				
					debitCreditAccount(supplierProductModel, supplierAmountCurrent, transactionModelList, stakeholderBankInfoModel);
				}
					
				transactionModelList.clear();
				supplierAmountCurrent = 0.0D;
				supplierProductModel.setSupplierId(0L);
					
				supplierProductModel = productModel;
				supplierAmountCurrent += (transactionModel.getTotalAmount() - transactionModel.getTotalCommissionAmount());
				transactionModelList.add(transactionModel);
					
				if(totalTransaction == i) {
						
					if(supplierAmountCurrent.doubleValue() > 0.0D) {
							
						debitCreditAccount(supplierProductModel, supplierAmountCurrent, transactionModelList, stakeholderBankInfoModel);
					}		
						
				} else if(i<totalTransaction) {
						
					ProductModel _productModel = (ProductModel)(transactionSupplierList.get(i+1))[1];
						
					if(_productModel.getSupplierId().longValue() != supplierProductModel.getSupplierId().longValue()) {

						debitCreditAccount(supplierProductModel, supplierAmountCurrent, transactionModelList, stakeholderBankInfoModel);
					}
				}
			}
			
			i++;
		}		
	}
	
	
	/**
	 * @param supplierProductModel
	 * @param supplierAmountCurrent
	 * @param transactionModelList
	 */
	private void debitCreditAccount(ProductModel supplierProductModel, Double supplierAmountCurrent, List<TransactionModel> transactionModelList, StakeholderBankInfoModel stakeholderBankInfoModel) {

		Date currentDate = new Date();
		
		try {
			
			SwitchWrapper switchWrapper = getSwitchWrapper(supplierProductModel, supplierAmountCurrent, stakeholderBankInfoModel);
			
			ThreadLocalAppUser.setAppUserModel(switchWrapper.getWorkFlowWrapper().getAppUserModel());		
			
			switchController.debitCreditAccount(switchWrapper);
			
			logger.info(switchWrapper.getIntegrationMessageVO().getResponseCode());
			
			if(switchWrapper.getIntegrationMessageVO().getResponseCode().equals("00")) {
				
				for(TransactionModel transactionModel : transactionModelList) {
					transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
					WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
					workFlowWrapper.setTransactionModel(transactionModel);
					workFlowWrapper.getTransactionModel().setCreatedOn(currentDate);
					workFlowWrapper.getTransactionModel().setUpdatedOn(currentDate);
					transactionModuleManager.saveTransaction(workFlowWrapper);
				}
				
			}
			
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	}	
	
	
	/**
	 * @param supplierProductModel
	 * @param supplierAmountCurrent
	 * @return
	 * @throws FrameworkCheckedException 
	 */
	private SwitchWrapper getSwitchWrapper(ProductModel supplierProductModel, Double supplierAmountCurrent, StakeholderBankInfoModel stakeholderBankInfoModel) throws FrameworkCheckedException {
		
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		
		SupplierBankInfoModel supplierBankInfoModel = getSupplierBankInfoModel(supplierProductModel.getSupplierId());					
		
		DeviceTypeModel deviceTypeModel = new DeviceTypeModel();	
		deviceTypeModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);
		
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
		workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());
		workFlowWrapper.setSupplierBankInfoModel(supplierBankInfoModel);
		workFlowWrapper.setAppUserModel(appUserModel);
		workFlowWrapper.setProductModel(supplierProductModel);
		
		transactionModuleManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);		
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
		
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		switchWrapper.setTransactionAmount(supplierAmountCurrent);

		switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());

		switchWrapper.setFromAccountNo(stakeholderBankInfoModel.getAccountNo());	
		switchWrapper.setFromAccountType("20");
		switchWrapper.setFromCurrencyCode("586");

		switchWrapper.setToAccountNo(supplierBankInfoModel.getAccountNo());
		switchWrapper.setToAccountType("20");
		switchWrapper.setToCurrencyCode("586");

		switchWrapper.setCurrencyCode("586");		

		logger.info("From Sundry Account # "+stakeholderBankInfoModel.getAccountNo());
		logger.info("To Supplier Account # "+supplierBankInfoModel.getAccountNo());
		logger.info("Supplier ID # "+supplierProductModel.getSupplierId());
		logger.info("Transaction Amount "+supplierAmountCurrent);
		
		return switchWrapper;
	}
	
	
	/**
	 * @param supplierId
	 * @return
	 */
	private SupplierBankInfoModel getSupplierBankInfoModel(Long supplierId) {
	
		SupplierBankInfoModel supplierBankInfoModel = null;
		
		try {
		
			SupplierBankInfoModel _supplierBankInfoModel = new SupplierBankInfoModel();
			_supplierBankInfoModel.setSupplierId(supplierId);
			
			BaseWrapper _baseWrapper = new BaseWrapperImpl();
			_baseWrapper.setBasePersistableModel(_supplierBankInfoModel);
			
			BaseWrapper baseWrapper = this.supplierBankInfoManager.loadSupplierBankInfo(_baseWrapper);
			
			supplierBankInfoModel = (SupplierBankInfoModel) baseWrapper.getBasePersistableModel();
			
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}

		return supplierBankInfoModel;
	}
	
	
	/**
	 * @return
	 */
	private StakeholderBankInfoModel getStakeholderBankInfoModel(Long stakeholderBankInfoId) {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		StakeholderBankInfoModel sundaryStakeholderBankInfoModel = new StakeholderBankInfoModel();
		sundaryStakeholderBankInfoModel.setStakeholderBankInfoId(stakeholderBankInfoId);
		searchBaseWrapper.setBasePersistableModel(sundaryStakeholderBankInfoModel);
		
		StakeholderBankInfoModel _sundaryStakeholderBankInfoModel = null;
		
		try {

			_sundaryStakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
		
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}	
		
		return _sundaryStakeholderBankInfoModel;
	}


	
	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}
	public void setTransactionModuleManager(TransactionModuleManager transactionModuleManager) {
		this.transactionModuleManager = transactionModuleManager;
	}
	public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
		this.supplierBankInfoManager = supplierBankInfoManager;
	}
	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
}