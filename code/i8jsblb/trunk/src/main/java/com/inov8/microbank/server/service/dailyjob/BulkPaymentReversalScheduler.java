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
public class BulkPaymentReversalScheduler {
	private static Logger logger = Logger.getLogger(BulkPaymentReversalScheduler.class.getSimpleName());

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
		logger.info("*********** BOOTING UP BULK PAYMENT REVERSAL SCHEDULER RC-1.96 ***********");
		String expiryDate = messageSource.getMessage("BULKPAYMENT.EXPIRYTIME", null, null);
		Integer days = Integer.parseInt(expiryDate);
		logger.info("BULKPAYMENT.EXPIRYTIME: " + days +" day(s)");
		DateTime dateTime = new DateTime().withTime(0, 0, 0, 0).minusDays(days);
		logger.info("FINDING TRX OLDER THAN: " + dateTime);
		execute(dateTime.toDate());
	}

	public void execute(Date expiryDays) {
		List<Long> reversedTransactionIds = new ArrayList<Long>();
		List<TransactionModel> transactions = orphanTransactionDAO.findOrphanTransactionsForBulkPayment(expiryDays, SupplierProcessingStatusConstants.IN_PROGRESS);
		if ( ! CollectionUtils.isEmpty(transactions)) {
			logger.info("*********** BULK PAYMENT REVERSAL -> " + transactions.size() + " JOB(S) FOUND ***********");
			
			StakeholderBankInfoModel olaSundaryAccount = new StakeholderBankInfoModel();
			StakeholderBankInfoModel unclaimedOlaSundry = null;
			boolean dataLoaded = false;
			try {
				olaSundaryAccount = getAccount(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
				unclaimedOlaSundry = getAccount(PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_OLA_ACCOUNT);
				dataLoaded = true;
			} catch (FrameworkCheckedException e) {
				logger.error("[BulkPaymentReversalScheduler.execute] Error while loading FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID:" + PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID , e);
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
						
						txAmount = transaction.getTotalAmount();
						logger.info("[BulkPaymentReversalScheduler] Going to process Transction ID:" + transactionCode + " Customer Mobile: " + transaction.getCustomerMobileNo() + " Total Amount: " + txAmount);
						
						result = orphanTransactionManager.reverseBulkPaymentOrphanTransaction(transaction, olaSundaryAccount, unclaimedOlaSundry);
						
						creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(transactionCode);

					}catch(Exception ex){
						logger.error("[BulkPaymentReversalScheduler.execute] unable to reverse OrphanTransaction transactionCodeId:"+transaction.getTransactionCodeId(),ex);
					}
					
					if(result){
						totalReversedAmount += txAmount;
						reversedTransactionIds.add(transaction.getTransactionId());
					}
				}
			
				logger.info("[BulkPaymentReversalScheduler] Successfully processed Transction IDs:" + reversedTransactionIds + " Total Processed Amount: " + totalReversedAmount);
				
			}
			
			if( ! CollectionUtils.isEmpty(reversedTransactionIds)){
				try {
					transactionDetailMasterManager.updateTransactionProcessingStatus(reversedTransactionIds, SupplierProcessingStatusConstants.UNCLAIMED, SupplierProcessingStatusConstants.UNCLAIMED_NAME);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else{
			logger.info("*********** BULK PAYMENT REVERSAL. No candidate Transaction FOUND ***********");
		}
	}

	public StakeholderBankInfoModel getAccount(Long key) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setPrimaryKey(key);
		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
		return stakeholderBankInfoModel;
	}
	
	public BulkPaymentReversalScheduler() {

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
