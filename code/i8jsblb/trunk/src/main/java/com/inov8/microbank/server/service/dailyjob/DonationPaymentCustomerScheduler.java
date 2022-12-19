package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID;
import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;

public class DonationPaymentCustomerScheduler extends QuartzJobBean {
	
	private final SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("EEE.MMMMM.dd.yyyy.hh:mm:ss.aaa");
	private static Logger logger = Logger.getLogger(DonationPaymentCustomerScheduler.class);

	private AbstractFinancialInstitution olaVeriflyFinancialInstitution = null;
	private StakeholderBankInfoManager stakeholderBankInfoManager = null;
	private TransactionModuleManager transactionModuleManager = null;
	private SupplierBankInfoManager supplierBankInfoManager = null;
	private SwitchController switchController = null;
	private TransactionDetailMasterManager transactionDetailMasterManager;


	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		long start = System.currentTimeMillis();

		logger.info(":-Started Donation Payment Customer Scheduler");
		
		try {

			process();

		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		
		logger.info(":-Ended Donation.Payment. Customer Scheduler Took : "+ ((System.currentTimeMillis() - start)/1000) + " Second(s), Next Date/Time to Fire will : "+simpleDateFormat.format(context.getNextFireTime()));
	}

	
	/**
	 * @throws FrameworkCheckedException
	 * @ Scheduler will settle collection amount of each supplier from customer pool account to supplier(collection entity)'s core bank account.
	 * 
	 */
	private void process() throws FrameworkCheckedException {
		
		List<Object[]> transactionSupplierList = transactionModuleManager.getDonationTransactionList(TransactionTypeConstantsInterface.DONATION_PAYMENT_TX, SupplierProcessingStatusConstants.BANK_SETTLEMENT_PENDING, ServiceTypeConstantsInterface.SERVICE_TYPE_DONATION_CUSTOMER_PAYMENT);
		
		Double supplierAmountCurrent = 0.0D;
		ProductModel supplierProductModel = new ProductModel();
		supplierProductModel.setSupplierId(0L);
		
		StakeholderBankInfoModel stakeholderBankInfoModel = getStakeholderBankInfoModel(CUSTOMER_POOL_ACCOUNT_ID);
		
		CopyOnWriteArrayList<TransactionModel> transactionModelList = new CopyOnWriteArrayList<TransactionModel>();
		
		int totalTransaction = transactionSupplierList.size()-1;
		int i = 0;
		
		for(Object[] transactionSupplier : transactionSupplierList) {
				
			TransactionModel transactionModel = (TransactionModel) transactionSupplier[0];
			ProductModel productModel = (ProductModel) transactionSupplier[1];
			
			if(supplierProductModel.getSupplierId().longValue() == productModel.getSupplierId().longValue()) {
					
				supplierAmountCurrent += (transactionModel.getTotalAmount() - transactionModel.getTotalCommissionAmount());
				supplierProductModel = productModel;
					
				transactionModelList.add(transactionModel);
				
				if(totalTransaction == i) {
					
					if(supplierAmountCurrent.doubleValue() > 0.0D) {
//							
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
	private void debitCreditAccount(ProductModel supplierProductModel, Double supplierAmountCurrent, CopyOnWriteArrayList<TransactionModel> transactionModelList, StakeholderBankInfoModel stakeholderBankInfoModel) {
	
		try {
			
			SwitchWrapper switchWrapper = getSwitchWrapper(supplierProductModel, supplierAmountCurrent, stakeholderBankInfoModel);
			switchWrapper.setToAccountNo(switchWrapper.getWorkFlowWrapper().getSupplierBankInfoModel().getAccountNo());
			
			SwitchWrapper _switchWrapper = switchWrapper;
			_switchWrapper.getWorkFlowWrapper().setSupplierBankInfoModel((SupplierBankInfoModel)switchWrapper.getObject(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.toString()));
			
			ThreadLocalAppUser.setAppUserModel(switchWrapper.getWorkFlowWrapper().getAppUserModel());		
			
			switchController.debitCreditAccount(switchWrapper);
			
			logger.info(switchWrapper.getIntegrationMessageVO().getResponseCode());
			
			if(switchWrapper.getIntegrationMessageVO().getResponseCode().equals("00")) {
				
				List<Long> transactionIdList = new ArrayList<Long>(0);
				
				for(TransactionModel transactionModel : transactionModelList) {
					
					TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
					transactionCodeModel.setTransactionCodeId(transactionModel.getTransactionCodeId());
					
					BaseWrapper baseWrapper = new BaseWrapperImpl(); 
					baseWrapper.setBasePersistableModel(transactionCodeModel);
					baseWrapper = (BaseWrapper) this.transactionModuleManager.loadTransactionCode(baseWrapper);
					transactionCodeModel = (TransactionCodeModel) baseWrapper.getBasePersistableModel();
					
					_switchWrapper.getWorkFlowWrapper().setTransactionCodeModel(transactionCodeModel);
					_switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
					
					this.olaVeriflyFinancialInstitution.donationTransactionDr(_switchWrapper);
					
					if(_switchWrapper.getIntegrationMessageVO().getResponseCode().equals("00")) {
						
						transactionIdList.add(transactionModel.getTransactionId());
					}
					
//					saveTransactionModel(transactionModel);
				}
				
				this.saveTransactionList(transactionIdList);
				
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
		
		SupplierBankInfoModel supplierBankInfoModelCore = getSupplierBankInfoModel(supplierProductModel.getSupplierId(), PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);				
		SupplierBankInfoModel supplierBankInfoModelBBA = getSupplierBankInfoModel(supplierProductModel.getSupplierId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);				
		
		DeviceTypeModel deviceTypeModel = new DeviceTypeModel();	
		deviceTypeModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);	

		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
		workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());
		workFlowWrapper.setSupplierBankInfoModel(supplierBankInfoModelCore);
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
		switchWrapper.putObject(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.toString(), supplierBankInfoModelBBA);
		
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		switchWrapper.setTransactionAmount(Double.valueOf(Formatter.formatDouble(supplierAmountCurrent)));

		switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());

		switchWrapper.setFromAccountNo(stakeholderBankInfoModel.getAccountNo());	
		switchWrapper.setFromAccountType("20");
		switchWrapper.setFromCurrencyCode("586");
		switchWrapper.setToAccountType("20");
		switchWrapper.setToCurrencyCode("586");

		switchWrapper.setCurrencyCode("586");		

		logger.info("From POOL Account # "+stakeholderBankInfoModel.getAccountNo());
		logger.info("To Supplier Core Account # "+supplierBankInfoModelCore.getAccountNo());
		logger.info("Supplier ID # "+supplierProductModel.getSupplierId());
		logger.info("Transaction Amount "+supplierAmountCurrent);
		logger.info("Supplier BB Account #"+supplierBankInfoModelBBA.getAccountNo());
		
		return switchWrapper;
	}
	
	
	/**
	 * @param supplierId
	 * @return
	 */
	private SupplierBankInfoModel getSupplierBankInfoModel(Long supplierId, Long paymentModeId) {
	
		SupplierBankInfoModel supplierBankInfoModel = null;
		
		try {
		
			SupplierBankInfoModel _supplierBankInfoModel = new SupplierBankInfoModel();
			_supplierBankInfoModel.setSupplierId(supplierId);
			_supplierBankInfoModel.setPaymentModeId(paymentModeId);
			
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
	 * @param transactionIdList
	 */
	private void saveTransactionList(List<Long> transactionIdList) {

		int i = transactionModuleManager.updateTransactionProcessingStatus(SupplierProcessingStatusConstants.COMPLETED, transactionIdList);

		if(i == transactionIdList.size()){
			try {
				transactionDetailMasterManager.updateTransactionProcessingStatus(transactionIdList, SupplierProcessingStatusConstants.COMPLETED, SupplierProcessingStatusConstants.COMPLETE_NAME);
			} catch (Exception e) {
				logger.error("Donation Payment Scheduler encountered an error while updating Transaction Detail Master table for Transcation status(Complete).");
				e.printStackTrace();
			}
		}
		
		logger.info("[Donation Payment Scheduler] Number of Transactions Updated as SPS " + i);
	}
	
	
	/**
	 * @param transactionModel
	 * @throws FrameworkCheckedException
	 */
	private void saveTransactionModel(TransactionModel transactionModel) throws FrameworkCheckedException {

		Date currentDate = new Date();
		
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setTransactionModel(transactionModel);
		workFlowWrapper.getTransactionModel().setUpdatedOn(currentDate);
		transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		
		transactionModuleManager.saveTransaction(workFlowWrapper);
		
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
	public void setOlaVeriflyFinancialInstitution(AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}


	public void setTransactionDetailMasterManager(
			TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}	
}