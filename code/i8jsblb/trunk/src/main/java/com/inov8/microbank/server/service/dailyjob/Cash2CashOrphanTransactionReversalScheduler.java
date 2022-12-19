package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.MessageSource;
import org.springframework.util.CollectionUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.OLATransactionReasonsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.commissionmodule.CommissionStakeholderDAO;
import com.inov8.microbank.server.dao.commissionmodule.hibernate.CommissionTransactionHibernateDAO;
import com.inov8.microbank.server.dao.dailyjob.OrphanTransactionDAO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.orphantransaction.OrphanTransactionManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.transactionmodule.TransactionCodeGenerator;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;

@SuppressWarnings("all")
public class Cash2CashOrphanTransactionReversalScheduler {
	private static Logger logger = Logger.getLogger(Cash2CashOrphanTransactionReversalScheduler.class);

	private CommissionStakeholderDAO stakeholderDAO;
	private CommissionTransactionHibernateDAO commissionTransactionHibernateDAO;
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
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;	
	
	public void init() {
		logger.info("*********** BOOTING UP CASH 2 CASH REVERSAL SCHEDULER RC-1.95 ***********");
		String expiryDate = messageSource.getMessage("CASH2CASH.EXPIRYTIME", null, null);
		Integer days = Integer.parseInt(expiryDate);
		logger.info("CASH2CASH.EXPIRYTIME: " + days +" day(s)");
		DateTime dateTime = new DateTime().withTime(0, 0, 0, 0).minusDays(days);
		logger.info("FINDING TRX OLDER THAN: " + dateTime);
		execute(dateTime.toDate());
	}

	public void execute(Date expiryDays) {
		List<TransactionModel> transactions = orphanTransactionDAO.findOrphanTransactions(ProductConstantsInterface.CASH_TRANSFER, expiryDays, SupplierProcessingStatusConstants.IN_PROGRESS);
		String receiverCNIC = null;
		String customerMobileNo = null;
		List<Long> reversedTransactionIds = new ArrayList<Long>();
		StakeholderBankInfoModel unclaimedOlaSundry = null;
		StakeholderBankInfoModel fundTransferOLASundaryAccount = new StakeholderBankInfoModel();
		
		if ( ! CollectionUtils.isEmpty(transactions)) {
			logger.info(" *********** CASH 2 CASH REVERSAL -> " + transactions.size() + " JOB(S) FOUND ***********");
			
			boolean dataLoaded = false;
			try {
				fundTransferOLASundaryAccount = getAccount(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
				unclaimedOlaSundry = getAccount(PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_OLA_ACCOUNT);

				dataLoaded = true;
			} catch (Exception e) {
				logger.error("[OrphanTransactionReversalScheduler.execute] Error while loading CASH_TO_CASH_SUNDRY_ACCOUNT_ID:" + PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID , e);
			}
			
			Double txAmount = 0D;
			Double totalReversedAmount = 0D;
			
			if(dataLoaded){
				
				for (TransactionModel transaction : transactions) {

					boolean result = false;
					
					try{
						String transactionCode = transaction.getTransactionCodeIdTransactionCodeModel().getCode();
						
						TransactionDetailModel transactionDetailModel = null;
						List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transaction.getTransactionIdTransactionDetailModelList();
						if (txdetails != null) {
							transactionDetailModel = txdetails.get(0);
						}
						
						TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
						transactionDetailMasterModel.setTransactionCodeId(transaction.getTransactionCodeId());
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
						
						this.transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
						transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();

						txAmount = transactionDetailMasterModel.getSundryAmount();
						
						if(txAmount==null || txAmount == 0D) {
							logger.error("Found "+txAmount+" value to run C2C Orphan Transaction.");
							continue;
						}						
						
						logger.info("[OrphanTransactionReversalScheduler] Going to process Transction ID:" + transaction.getTransactionCodeIdTransactionCodeModel().getCode() + " Walkin Customer Mobile: " + transactionDetailModel.getCustomField6() + " Transaction Amount: " + txAmount);
						
						result = orphanTransactionManager.reverseC2COrphanTransaction(transactionDetailMasterModel, transaction, fundTransferOLASundaryAccount, unclaimedOlaSundry);
						
						creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(transactionCode);
						
					}catch(Exception ex){
						logger.error("[OrphanTransactionReversalScheduler.execute] unable to reverse OrphanTransaction transactionCodeId: "+transaction.getTransactionCodeId(),ex);
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
			logger.info("*********** CASH 2 CASH REVERSAL. No candidate Transaction FOUND ***********");
		}
	}

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
	
	public StakeholderBankInfoModel getAccount(Long key) {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setPrimaryKey(key);
		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		try {
			stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper)
					.getBasePersistableModel();
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		return stakeholderBankInfoModel;
	}

	public Cash2CashOrphanTransactionReversalScheduler() {

	}

	public CommissionStakeholderDAO getStakeholderDAO() {
		return stakeholderDAO;
	}

	public void setStakeholderDAO(CommissionStakeholderDAO stakeholderDAO) {
		this.stakeholderDAO = stakeholderDAO;
	}

	public CommissionTransactionHibernateDAO getCommissionTransactionHibernateDAO() {
		return commissionTransactionHibernateDAO;
	}

	public void setCommissionTransactionHibernateDAO(CommissionTransactionHibernateDAO commissionTransactionHibernateDAO) {
		this.commissionTransactionHibernateDAO = commissionTransactionHibernateDAO;
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

	public void setTransactionDetailMasterManager(
			TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}

	public void setOrphanTransactionManager(
			OrphanTransactionManager orphanTransactionManager) {
		this.orphanTransactionManager = orphanTransactionManager;
	}

	public void setCreditAccountQueingPreProcessor(
			CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}
}
