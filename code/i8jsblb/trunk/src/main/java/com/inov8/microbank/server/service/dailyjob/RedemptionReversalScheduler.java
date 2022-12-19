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
public class RedemptionReversalScheduler {
	private static Logger logger = Logger.getLogger(RedemptionReversalScheduler.class.getSimpleName());

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
		logger.info("*********** STARTING REDEMPTION REVERSAL SCHEDULER  ***********");
		String bulkPaymentExpiry = messageSource.getMessage("BULKPAYMENT.EXPIRYTIME", null, null);
		String c2cExpiry = messageSource.getMessage("CASH2CASH.EXPIRYTIME", null, null);
		String a2cExpiry= messageSource.getMessage("ACC2CASH.EXPIRYTIME", null, null);
		
		DateTime bulkPaymentExpiryDate = calculateExpiryTime(bulkPaymentExpiry);
		DateTime c2cExpiryDate = calculateExpiryTime(c2cExpiry);
		DateTime a2cExpriyDate = calculateExpiryTime(a2cExpiry);
				
		execute(bulkPaymentExpiryDate.toDate(), c2cExpiryDate.toDate(), a2cExpriyDate.toDate());
	}

	public void execute(Date bulkPaymentExpiryDate, Date c2cExpiryDate, Date a2cExpiryDate) {
		List<Long> reversedTransactionIds = new ArrayList<Long>();
		List<TransactionModel> bulkPaymentTransactions = orphanTransactionDAO.findOrphanTransactions(ProductConstantsInterface.BULK_PAYMENT, bulkPaymentExpiryDate, SupplierProcessingStatusConstants.IN_PROGRESS);
		List<TransactionModel> cashToCashTransactions = orphanTransactionDAO.findOrphanTransactions(ProductConstantsInterface.CASH_TRANSFER, c2cExpiryDate, SupplierProcessingStatusConstants.IN_PROGRESS);
		List<TransactionModel> accountToCashTransactions = orphanTransactionDAO.findOrphanTransactions(ProductConstantsInterface.ACCOUNT_TO_CASH, a2cExpiryDate, SupplierProcessingStatusConstants.IN_PROGRESS);
		List<TransactionModel> transactions = mergeTransactions(bulkPaymentTransactions, cashToCashTransactions, accountToCashTransactions);
		
		
		if ( ! CollectionUtils.isEmpty(transactions)) {
			logger.info("*********** REDEMPTION REVERSAL -> " + transactions.size() + " JOB(S) FOUND ***********");
			
			StakeholderBankInfoModel olaSundaryAccount = new StakeholderBankInfoModel();
			StakeholderBankInfoModel unclaimedOlaSundry = null;
			boolean dataLoaded = false;
			try {
				olaSundaryAccount = getAccount(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
				unclaimedOlaSundry = getAccount(PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_OLA_ACCOUNT);
				dataLoaded = true;
			} catch (FrameworkCheckedException e) {
				logger.error("[execute] Error while loading FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID:" + PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID , e);
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
						logger.info("Going to process Transction ID:" + transactionCode + " Customer Mobile: " + transaction.getCustomerMobileNo() + " Transaction Amount: " + txAmount);
						
						result = orphanTransactionManager.makeTransactionsUnclaimed(transaction, olaSundaryAccount, unclaimedOlaSundry);
						
						creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(transactionCode);

					}catch(Exception ex){
						logger.error("unable to reverse OrphanTransaction transactionCodeId:"+transaction.getTransactionCodeId(),ex);
					}
					
					if(result){
						totalReversedAmount += txAmount;
						reversedTransactionIds.add(transaction.getTransactionId());
					}
				}
			
				logger.info("Successfully processed Transction IDs:" + reversedTransactionIds + " Total Processed Amount: " + totalReversedAmount);
				
			}
			
			if( ! CollectionUtils.isEmpty(reversedTransactionIds)){
				try {
					transactionDetailMasterManager.updateTransactionProcessingStatus(reversedTransactionIds, SupplierProcessingStatusConstants.UNCLAIMED, SupplierProcessingStatusConstants.UNCLAIMED_NAME);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else{
			logger.info("*********** No candidate Transaction FOUND ***********");
		}
	}
	
	private List<TransactionModel> mergeTransactions(List<TransactionModel> bulkPaymentTransactions, List<TransactionModel> cashToCashTransactions, List<TransactionModel> accountToCashTransactions){
		List<TransactionModel> transactions = new ArrayList<>();
		if ( ! CollectionUtils.isEmpty(bulkPaymentTransactions)) {
			logger.info("Bulk Payment Transactions: " + transactions.size());
			transactions.addAll(bulkPaymentTransactions);
		}
		
		if ( ! CollectionUtils.isEmpty(cashToCashTransactions)) {
			logger.info("Cash to Cash Transactions: " + transactions.size());
			transactions.addAll(cashToCashTransactions);
		}
		
		if ( ! CollectionUtils.isEmpty(accountToCashTransactions)) {
			logger.info("Account to Cash Transactions: " + transactions.size());
			transactions.addAll(accountToCashTransactions);
		}
		
		return transactions;
	}

	private DateTime calculateExpiryTime(String expiryDays) { 
		Integer days = Integer.parseInt(expiryDays);
		logger.info(expiryDays + ": " + days +" day(s)");
		DateTime dateTime = new DateTime().withTime(0, 0, 0, 0).minusDays(days);
		logger.info(expiryDays + ". Finding transactions before : " + dateTime);	
		
		return dateTime;
	}
	
	public StakeholderBankInfoModel getAccount(Long key) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setPrimaryKey(key);
		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
		return stakeholderBankInfoModel;
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
