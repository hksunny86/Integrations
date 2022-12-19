package com.inov8.microbank.server.service.dailyjob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.MessageSource;
import org.springframework.util.CollectionUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.server.dao.dailyjob.OrphanTransactionDAO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.orphantransaction.OrphanTransactionManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionCodeGenerator;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;

@SuppressWarnings("all")
public class OrphanTransactionReversalScheduler {
	private static Logger logger = Logger.getLogger(OrphanTransactionReversalScheduler.class.getSimpleName());

	private SwitchController switchController;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
	private AppUserManager appUserManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private TransactionCodeGenerator transactionCodeGenerator;
	private TransactionModuleManager transactionManager;
	private ActionLogManager actionLogManager;
	private OrphanTransactionDAO orphanTransactionDAO;
	private CommissionManager commissionManager;
	private MessageSource messageSource;
	private SmsSender smsSender;
	private TransactionDetailMasterManager transactionDetailMasterManager;
	private OrphanTransactionManager orphanTransactionManager;
	private SettlementManager settlementManager;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
	 
	public void init() {
		logger.info("*********** BOOTING UP ACCOUNT 2 CASH REVERSAL SCHEDULER RC-1.96 ***********");
		String expiryDate = messageSource.getMessage("ACC2CASH.EXPIRYTIME", null, null);
		Integer days = Integer.parseInt(expiryDate);
		logger.info("ACC2CASH.EXPIRYTIME: " + days +" day(s)");
		DateTime dateTime = new DateTime().withTime(0, 0, 0, 0).minusDays(days);
		logger.info("FINDING TRX OLDER THAN: " + dateTime);
		execute(dateTime.toDate());
	}

	public void execute(Date expiryDays) {
		List<Long> reversedTransactionIds = new ArrayList<Long>();
		List<TransactionModel> transactions = orphanTransactionDAO.findOrphanTransactions(ProductConstantsInterface.ACCOUNT_TO_CASH, expiryDays, SupplierProcessingStatusConstants.IN_PROGRESS);
		if ( ! CollectionUtils.isEmpty(transactions)) {
			logger.info("*********** ACCOUNT 2 CASH REVERSAL -> " + transactions.size() + " JOB(S) FOUND ***********");
			
			StakeholderBankInfoModel olaSundaryAccount = new StakeholderBankInfoModel();
			boolean dataLoaded = false;
			try {
				olaSundaryAccount = getAccount(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
				dataLoaded = true;
			} catch (FrameworkCheckedException e) {
				logger.error("[OrphanTransactionReversalScheduler.execute] Error while loading ACCOUNT_TO_CASH_SUNDRY_ACCOUNT_ID:" + PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID , e);
			}
			
			boolean result = false;
			Double txAmount = 0D;
			Double totalReversedAmount = 0D;
			
			if(dataLoaded){
				
				for (TransactionModel transaction : transactions) {
					String transactionCode = transaction.getTransactionCodeIdTransactionCodeModel().getCode();
					try{
						TransactionDetailModel transactionDetailModel = null;
						List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transaction.getTransactionIdTransactionDetailModelList();
						if (txdetails != null) {
							transactionDetailModel = txdetails.get(0);
						}
						
						
						txAmount = transactionDetailModel.getActualBillableAmount();
						logger.info("[OrphanTransactionReversalScheduler] Going to process Transction ID:" + transactionCode + " Customer Mobile: " + transaction.getCustomerMobileNo() + " Transaction Amount: " + txAmount);
						
						result = orphanTransactionManager.reverseOrphanTransaction(transaction,olaSundaryAccount,txAmount);
						
						creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(transactionCode);
						
					}catch(Exception ex){
						logger.error("[OrphanTransactionReversalScheduler.execute] unable to reverse OrphanTransaction transactionCodeId:"+transaction.getTransactionCodeId(),ex);
					}
					
					if(result){
						totalReversedAmount += txAmount;
						reversedTransactionIds.add(transaction.getTransactionId());
					}
				}
			
				logger.info("[OrphanTransactionReversalScheduler] Successfully processed Transction IDs:" + reversedTransactionIds + " Total Processed Amount: " + totalReversedAmount);
				
			}
			
			if( ! CollectionUtils.isEmpty(reversedTransactionIds)){
				try {
					transactionDetailMasterManager.updateTransactionProcessingStatus(reversedTransactionIds, SupplierProcessingStatusConstants.UNCLAIMED, SupplierProcessingStatusConstants.UNCLAIMED_NAME);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else{
			logger.info("*********** ACCOUNT 2 CASH REVERSAL. No candidate Transaction FOUND ***********");
		}
	}
/*
	public double reverseTransaction(String notificationMessage, Double amount, TransactionModel transactionModel) {
		transactionModel.setTotalAmount(amount);
		double balance = 0;
		String mobileNo = transactionModel.getCustomerMobileNo();
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		sBaseWrapper.setBasePersistableModel(appUserModel);
		try {
			sBaseWrapper = appUserManager.searchAppUserByMobile(sBaseWrapper);
			if (null != sBaseWrapper.getCustomList() && null != sBaseWrapper.getCustomList().getResultsetList()
					&& sBaseWrapper.getCustomList().getResultsetList().size() > 0) {
				appUserModel = (AppUserModel) sBaseWrapper.getCustomList().getResultsetList().get(0);
			}
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
			smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			baseWrapper = smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
			if (null != baseWrapper.getBasePersistableModel()) {
				smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
			}
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setTransactionModel(transactionModel);

			workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());

			workFlowWrapper.setAccountInfoModel(new AccountInfoModel());
			workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setCustomerModel(smartMoneyAccountModel.getCustomerIdCustomerModel());
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);

			OLAVO olavo = new OLAVO();
			olavo.setReasonId(OLATransactionReasonsInterface.ROLLBACK_WALKIN_CUSTOMER);
			switchWrapper.setOlavo(olavo);
			switchWrapper = olaVeriflyFinancialInstitution.debitWithoutPin(switchWrapper);

			if (switchWrapper.getOlavo().getResponseCode().equals("00")) {
				balance = switchWrapper.getOlavo().getBalance();
			}

		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return balance;
	}
*/
	private double findStakeholderCommission(long txdetailId, Long stakeholderId) {
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		CommissionTransactionModel commissoinTxModel = new CommissionTransactionModel();
		commissoinTxModel.setCommissionStakeholderId(stakeholderId);
		commissoinTxModel.setTransactionDetailId(txdetailId);

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(commissoinTxModel);
		try {
			searchBaseWrapper = commissionManager.getCommissionTransactionModel(searchBaseWrapper);
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}

		commissoinTxModel = (CommissionTransactionModel) searchBaseWrapper.getBasePersistableModel();
		if (commissoinTxModel != null)
			return commissoinTxModel.getCommissionAmount();
		return 0;
	}
	
	public StakeholderBankInfoModel getAccount(Long key) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setPrimaryKey(key);
		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
		return stakeholderBankInfoModel;
	}
/*
	public SwitchWrapper transferFund(StakeholderBankInfoModel customerPoolAccount, StakeholderBankInfoModel sundaryAccount, Double amount, TransactionModel transaction) {

		if (sundaryAccount == null || customerPoolAccount == null) {
			logger.info("*** POOL(s) ACCOUNT NOT FOUND");
			return null;
		}

		// Start - Phoenix
		SwitchWrapper switchWrapperMain = new SwitchWrapperImpl();
		IntegrationMessageVO integrationMessageVOMain = new PhoenixIntegrationMessageVO();
		switchWrapperMain.setIntegrationMessageVO(integrationMessageVOMain);

		switchWrapperMain.setFromAccountNo(sundaryAccount.getAccountNo());
		switchWrapperMain.setFromAccountType("20");
		switchWrapperMain.setFromCurrencyCode("586");

		switchWrapperMain.setToAccountNo(customerPoolAccount.getAccountNo());
		switchWrapperMain.setToAccountType("20");
		switchWrapperMain.setToCurrencyCode("586");

		switchWrapperMain.setTransactionAmount(amount);
		switchWrapperMain.setCurrencyCode("586");

		switchWrapperMain.setWorkFlowWrapper(new WorkFlowWrapperImpl());
		switchWrapperMain.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
		AppUserModel appUserModelMain = new AppUserModel();
		appUserModelMain.setAppUserId(SCHEDULER_APP_USER_ID);
		ThreadLocalAppUser.setAppUserModel(appUserModelMain);
		switchWrapperMain.setBankId(50110L);
		switchWrapperMain.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		
		switchWrapperMain.getWorkFlowWrapper().setTransactionCodeModel(transaction.getRelationTransactionCodeIdTransactionCodeModel());
		switchWrapperMain.getWorkFlowWrapper().setProductModel(new ProductModel());
		 Long productId = null;
		 List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transaction.getTransactionIdTransactionDetailModelList();
	        if(txdetails != null && !txdetails.isEmpty() )
	        {
	        	TransactionDetailModel transactionDetailModel = txdetails.get(0);
	        	productId =transactionDetailModel.getProductId();
	        }
	        
		switchWrapperMain.getWorkFlowWrapper().getProductModel().setProductId(productId);
		
		try {
			switchWrapperMain = switchController.debitCreditAccount(switchWrapperMain);
		} catch (WorkFlowException e) {
			e.printStackTrace();
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		return switchWrapperMain;
	}

	public boolean olaDebitAccount(Double amount, StakeholderBankInfoModel accountInfoModel, TransactionModel transactionModel) {
		boolean result = false;
		AppUserModel appUserModel = new AppUserModel();
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		sBaseWrapper.setBasePersistableModel(appUserModel);
		try {
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

			OLAVO olavo = new OLAVO();
			olavo.setBalance(amount);
			olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
			olavo.setPayingAccNo(accountInfoModel.getAccountNo());
			olavo.setMicrobankTransactionCode(transactionModel.getTransactionCodeId().toString());
			olavo.setCustomerAccountTypeId(3L);
			olavo.setReasonId(OLATransactionReasonsInterface.ROLLBACK_WALKIN_CUSTOMER);

			SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
			newSwitchWrapper.setBankId(50110L);
			newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			newSwitchWrapper.setOlavo(olavo);
			newSwitchWrapper.setWorkFlowWrapper(workFlowWrapper);
			TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
			transactionCodeModel.setTransactionCodeId(transactionModel.getTransactionCodeId());
			newSwitchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
			newSwitchWrapper.getWorkFlowWrapper().setTransactionCodeModel(transactionCodeModel);
			switchWrapper = switchController.debit(newSwitchWrapper, null);

			if (switchWrapper.getOlavo().getResponseCode().equals("00")) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean creditAccountOLA(TransactionModel transactionModel, double txamount) {
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		AppUserModel appUserModel = new AppUserModel();
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		sBaseWrapper.setBasePersistableModel(appUserModel);
		try {
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
			smartMoneyAccountModel.setCustomerId(transactionModel.getCustomerId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			baseWrapper = smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
			if (null != baseWrapper.getBasePersistableModel()) {
				smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
			}
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setAccountInfoModel(new AccountInfoModel());
			workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setCustomerModel(smartMoneyAccountModel.getCustomerIdCustomerModel());
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);

			workFlowWrapper.setTransactionModel(transactionModel);
			OLAVO olavo = new OLAVO();
			olavo.setReasonId(-1L);
			olavo.setBalance(txamount);
			olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
			olavo.setMicrobankTransactionCode(transactionModel.getTransactionCodeId().toString());
			olavo.setCustomerAccountTypeId(3L);
			switchWrapper.setOlavo(olavo);
			switchWrapper = olaVeriflyFinancialInstitution.debitWithoutPin(switchWrapper);

		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (switchWrapper.getOlavo().getResponseCode() != null && switchWrapper.getOlavo().getResponseCode().equals("00")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean olaCreditAccount(Double amount, StakeholderBankInfoModel accountInfoModel, TransactionModel transactionModel) {
		boolean result = false;
		AppUserModel appUserModel = new AppUserModel();
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		sBaseWrapper.setBasePersistableModel(appUserModel);
		try {
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

			OLAVO olavo = new OLAVO();
			olavo.setBalance(amount);
			olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
			olavo.setPayingAccNo(accountInfoModel.getAccountNo());
			olavo.setMicrobankTransactionCode(transactionModel.getTransactionCodeId().toString());
			olavo.setCustomerAccountTypeId(3L);
			olavo.setReasonId(-1L);

			SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
			newSwitchWrapper.setBankId(50110L);
			newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			newSwitchWrapper.setOlavo(olavo);
			newSwitchWrapper.setWorkFlowWrapper(workFlowWrapper);
			TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
			transactionCodeModel.setTransactionCodeId(transactionModel.getTransactionCodeId());
			newSwitchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
			newSwitchWrapper.getWorkFlowWrapper().setTransactionCodeModel(transactionCodeModel);
			switchWrapper = switchController.debit(newSwitchWrapper, null);

			if (switchWrapper.getOlavo().getResponseCode().equals("00")) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void actionLogBeforeStart(ActionLogModel actionLogModel) {

		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
		if (actionLogModel.getActionLogId() != null) {
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}

	private void actionLogAfterEnd(ActionLogModel actionLogModel) {
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
		actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
		insertActionLogRequiresNewTransaction(actionLogModel);
	}

	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try {
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		} catch (Exception ex) {
			logger.error("Exception occurred while processing", ex);

		}
		return actionLogModel;
	}
*/
	public OrphanTransactionReversalScheduler() {

	}

	public SwitchController getSwitchController() {
		return switchController;
	}

	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}

	public StakeholderBankInfoManager getStakeholderBankInfoManager() {
		return stakeholderBankInfoManager;
	}

	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}

	public AbstractFinancialInstitution getOlaVeriflyFinancialInstitution() {
		return olaVeriflyFinancialInstitution;
	}

	public void setOlaVeriflyFinancialInstitution(AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public SmartMoneyAccountManager getSmartMoneyAccountManager() {
		return smartMoneyAccountManager;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public TransactionCodeGenerator getTransactionCodeGenerator() {
		return transactionCodeGenerator;
	}

	public void setTransactionCodeGenerator(TransactionCodeGenerator transactionCodeGenerator) {
		this.transactionCodeGenerator = transactionCodeGenerator;
	}

	public TransactionModuleManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(TransactionModuleManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public ActionLogManager getActionLogManager() {
		return actionLogManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public OrphanTransactionDAO getOrphanTransactionDAO() {
		return orphanTransactionDAO;
	}

	public void setOrphanTransactionDAO(OrphanTransactionDAO orphanTransactionDAO) {
		this.orphanTransactionDAO = orphanTransactionDAO;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public CommissionManager getCommissionManager() {
		return commissionManager;
	}

	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}

	public void setOrphanTransactionManager(
			OrphanTransactionManager orphanTransactionManager) {
		this.orphanTransactionManager = orphanTransactionManager;
	}
	
	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}


	public void setTransactionDetailMasterManager(
			TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}

	public void setCreditAccountQueingPreProcessor(
			CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}
}
