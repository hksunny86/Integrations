package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.FrameworkException;
import org.joda.time.DateTime;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.commissionmodule.CommissionStakeholderDAO;
import com.inov8.microbank.server.dao.commissionmodule.hibernate.CommissionTransactionHibernateDAO;
import com.inov8.microbank.server.dao.commissionmodule.hibernate.FranchiseCommissionViewHibernateDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.transactionmodule.TransactionCodeGenerator;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.thoughtworks.xstream.XStream;

@SuppressWarnings("all")
public class FranchiseCommissionSchedulerImpl implements CommissionDayEndScheduler {
	private static Logger logger = Logger.getLogger(FranchiseCommissionSchedulerImpl.class);

	private CommissionStakeholderDAO stakeholderDAO;
	private CommissionTransactionHibernateDAO commissionTransactionHibernateDAO;
	private FranchiseCommissionViewHibernateDAO franchiseCommissionHibernateDAO;
	private SwitchController switchController;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
	private AppUserManager appUserManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private TransactionCodeGenerator transactionCodeGenerator;
	private TransactionModuleManager transactionManager;
	private ActionLogManager actionLogManager;

	public void init() {
		DateTime dt = new DateTime();
		try {
			execute(dt.minusDays(1).toDate(), dt.plusDays(1).toDate());
		} catch (FrameworkException e) {
			logger.info("*** FRANCHISE COMMISSION TRANSFER FAILED ***");
			e.printStackTrace();
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}finally{
			ThreadLocalAppUser.remove();
		}
	}

	public void execute(Date start, Date end) throws FrameworkCheckedException, FrameworkException {
		logger.info("********** STARTING FRANCHISE COMMISSION SETTLEMENT *****************");

		DateTime startDateTime = new DateTime(start);
		// REMOVING TIME INFORMATION & MAKING DATE'S START.
		startDateTime = startDateTime.withTime(0, 0, 0, 0);
		DateTime endDateTime = new DateTime(end);
		// REMOVING TIME INFORMATION & MAKING DATE'S START.
		endDateTime = endDateTime.withTime(0, 0, 0, 0);

//		Leg 1 - Account to Cash - find all Franchise 2 entries with Status = Complete and posted = null and settled = null
		List<Object> a2cparentResult = franchiseCommissionHibernateDAO.findTotalUnpostedCommission(50010L, 50038L);
		List<Object> c2cparentResult = franchiseCommissionHibernateDAO.findTotalUnpostedCommission(50011L, 50038L);
		List<Object> bulkPaymentparentResult = franchiseCommissionHibernateDAO.findTotalUnpostedCommission(ProductConstantsInterface.BULK_PAYMENT, 50038L);

		StakeholderBankInfoModel commissionPoolAccount = getAccount(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);

		int postedSettledZeroRecords = 0;
		
		if (CollectionUtils.isNotEmpty(a2cparentResult) && a2cparentResult.get(0) != null && a2cparentResult.get(1) != null) {

			
			StakeholderBankInfoModel a2cPhoenixSundryAccount = getAccount(PoolAccountConstantsInterface.CREDIT_TRANSFER_SUNDRY_ACCOUNT_ID);//Account to cash phoenix sundary account

			//Account to Cash - Franchise 2 commissions are posted to Recon Account
			postSundryToReconAccount(a2cparentResult, a2cPhoenixSundryAccount, commissionPoolAccount, PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);//Account to cash OLA sundary account
			
			postedSettledZeroRecords += a2cparentResult.size();
		}

		if (CollectionUtils.isNotEmpty(c2cparentResult) && c2cparentResult.get(0) != null && c2cparentResult.get(1) != null) {

			StakeholderBankInfoModel c2cPhoenixSundryAccount = getAccount(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID);//Cash to cash phoenix sundary account

			//Cash to Cash - Franchise 2 commissions are posted to Recon Account
			postSundryToReconAccount(c2cparentResult, c2cPhoenixSundryAccount, commissionPoolAccount, PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);//Cash to cash OLA sundary account

			postedSettledZeroRecords += c2cparentResult.size();
			
		}

		if (CollectionUtils.isNotEmpty(bulkPaymentparentResult) && bulkPaymentparentResult.get(0) != null && bulkPaymentparentResult.get(1) != null) {

			StakeholderBankInfoModel bulkPaymentSundryAccount = getAccount(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID);//Bulk Payment phoenix sundary account

			//Cash to Cash - Franchise 2 commissions are posted to Recon Account
			postSundryToReconAccount(bulkPaymentparentResult, bulkPaymentSundryAccount, commissionPoolAccount, PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);//Bulk Payment OLA sundary account

			postedSettledZeroRecords += bulkPaymentparentResult.size();
			
		}

		
		logger.info("************************"+ postedSettledZeroRecords +" Records found with Posted = 0 and Settled = 0 ***********************");

			/***********************************************************************************************************************************\
			* Leg 2 																															 *
			* settle commission from Recon Account to respective Franchise Satkeholders accounts.										 		 *
			* 																																	 *
			\************************************************************************************************************************************/
		
			/* 1. Find Head Agents(Franchises) and their respective cumulative commissions with posted = 1 and settled = 0*/
			List<Object[]> childResult = franchiseCommissionHibernateDAO.findCommissionStakeholders();
			if(! CollectionUtils.isEmpty(childResult) && childResult.size() > 0){
				logger.info("************************"+ childResult.size() +" Records found with Posted = 1 and Settled = 0 *****************");

				for (Object[] stakeHolderInfo : childResult) {
//					Long commStakeholderId = (Long) stakeHolderInfo[0];
					Long smartMoneyAccountId = (Long) stakeHolderInfo[0];
					Double totalCommission = (Double) stakeHolderInfo[1];
					String commissionPartnerAccount = null;
	
					try {
						
						SmartMoneyAccountModel franchiseSmaModel = new SmartMoneyAccountModel();
						franchiseSmaModel.setSmartMoneyAccountId(smartMoneyAccountId);
						
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						baseWrapper.setBasePersistableModel(franchiseSmaModel);
						baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
						franchiseSmaModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
						
						if (franchiseSmaModel == null){
							logger.info("FRANCHISE COMMISSION SKIPPING THIS FRANCHISE AGENT...\nREASON: No records found for Smart Money Account ID: " + smartMoneyAccountId);
							continue;
						}
						
						AppUserModel franchiseAppUserModel = new AppUserModel();
						franchiseAppUserModel.setRetailerContactId(franchiseSmaModel.getRetailerContactId());
						
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(franchiseAppUserModel);
						BaseWrapper appUserBaseWrapper = appUserManager.searchAppUser(searchBaseWrapper);
						franchiseAppUserModel = (AppUserModel)appUserBaseWrapper.getBasePersistableModel();
						
						AccountInfoModel accountInfoModel = new AccountInfoModel();
						accountInfoModel.setAccountNick(franchiseSmaModel.getName());
						accountInfoModel.setCustomerId(franchiseAppUserModel.getAppUserId());
						
						LogModel logModel = new LogModel();
						logModel.setCreatdByUserId(franchiseAppUserModel.getAppUserId());
						logModel.setCreatedBy(franchiseAppUserModel.getFirstName());
						logModel.setTransactionCodeId(null);
						
						ActionLogModel actionLogModel = new ActionLogModel();
						XStream xstream = new XStream();
						try {
							Long appUserId = SCHEDULER_APP_USER_ID;
							xstream.toXML("");
							actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
							actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(""),
									XPathConstants.actionLogInputXMLLocationSteps));
							this.actionLogBeforeStart(actionLogModel);
						} catch (Exception e) {
							e.printStackTrace();
	
						}
						
						VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
						
						veriflyBaseWrapper.setBasePersistableModel(franchiseSmaModel);
						veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
						veriflyBaseWrapper.setLogModel(logModel);
						logger.info("FranchiseScheduler going to hit verifly to verifly credentials of Franchise AppUser : " + franchiseAppUserModel.getAppUserId());
						AppUserModel appUserModelMain = new AppUserModel();
						appUserModelMain.setAppUserId(SCHEDULER_APP_USER_ID);
						ThreadLocalAppUser.setAppUserModel(appUserModelMain);//SET Scheduler appUser in Threadlocal for PostedTransactions create/update.
						
						veriflyBaseWrapper = olaVeriflyFinancialInstitution.verifyCredentials(veriflyBaseWrapper);
						boolean success = veriflyBaseWrapper.isErrorStatus();
						
						if(success){
							
							accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
							commissionPartnerAccount = accountInfoModel.getAccountNo();
							
						}else{
							logger.info("FRANCHISE COMMISSION SKIPPING THIS FRANCHISE AGENT...\nREASON: " + veriflyBaseWrapper.getErrorMessage());
							logger.info("Details: \nAccountInfoModel : " + " Acct Nick: " + accountInfoModel.getAccountNick() + " CustomerId: " + accountInfoModel.getCustomerId() + " SMA RetContactId: " + franchiseSmaModel.getRetailerContactId() + " smartMoneyAcctId: " + smartMoneyAccountId + (veriflyBaseWrapper.getAccountInfoModel() == null  ? " null" : veriflyBaseWrapper.getAccountInfoModel().getAccountInfoId()));
							logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~End Details");
							continue;
						}
	
					} catch (Exception ex) {
						ex.printStackTrace();
					}finally{
						ThreadLocalAppUser.remove();
					}
	
					if (commissionPoolAccount != null && commissionPartnerAccount != null){
						
						// CALL PHOENIX FUND TRANSFER TO SETTLE COMMISION FROM POOL TO Head Agent's(Franchise's) ACCOUNT.
						try {
							
							boolean success = debitCreditPhoenixAccounts(commissionPoolAccount.getAccountNo(), commissionPartnerAccount, totalCommission);
		
							if (success) {
								List<Long> commissionTransactionIds = franchiseCommissionHibernateDAO.findUnsettledCommissionTransactionIds();
								
								if (commissionTransactionIds != null) {
									
									for (Long commissionTransactionId : commissionTransactionIds) {
										
										boolean resultStatus = commissionTransactionHibernateDAO.updateCommissionTransaction(commissionTransactionId, SCHEDULER_APP_USER_ID);
										
										/*if(logger.isDebugEnabled()){
											logger.debug("COMMISSION SETTLETED FOR commissionTransactionId = " + commissionTransactionId);
										}*/
									}
									
									logger.info("COMMISSION SETTLETED FOR commissionTransactionId = " + commissionTransactionIds);
								}
								
									logger.info("COMMISSION SETTLETED FOR SMART_MONEY_ACCOUNT_ID = " + smartMoneyAccountId);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}finally{
							ThreadLocalAppUser.remove();
						}
					}
				}

		} else {
			logger.info("*********** NO FRANCHISE COMMISSION SETTLEMENT JOB FOUND *****************");
		}
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

	public StakeholderBankInfoManager getStakeholderBankInfoManager() {
		return stakeholderBankInfoManager;
	}

	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}

	public boolean doDebitCommissionMirrorAccount(Double amount, StakeholderBankInfoModel accountInfoModel) {
		boolean result = false;
		AppUserModel appUserModel = new AppUserModel();
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		sBaseWrapper.setBasePersistableModel(appUserModel);
		try {
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			try {
				appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
				ThreadLocalAppUser.setAppUserModel(appUserModel);
				workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);
				switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}

			TransactionModel transactionModel = new TransactionModel();
			transactionModel.setTotalAmount(amount);
			transactionModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
			transactionModel.setTransactionCodeId(workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
			transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			transactionModel.setCustomerId(null);
			// transactionModel.setMfsId(appUserModel.getUsername());
			transactionModel.setTransactionTypeId(TransactionTypeConstantsInterface.COMMISSION_SETTLEMENT);
			// transactionModel.setCustomerMobileNo(MSISDN);
			transactionModel.setBankResponseCode(switchWrapper.getResponseCode());
			transactionModel.setTotalAmount(amount);
			transactionModel.setTotalCommissionAmount(0.0);
			transactionModel.setTransactionAmount(amount);

			transactionModel.setIssue(false);
			transactionModel.setSupProcessingStatusId(1L);
			transactionModel.setBankAccountNo(accountInfoModel.getAccountNo());
			transactionModel.setDiscountAmount(0.0);

			transactionModel.setCreatedOn(new Date());
			transactionModel.setUpdatedOn(new Date());

			transactionModel.setCreatedBy(accountInfoModel.getCreatedByAppUserModel().getAppUserId());
			transactionModel.setUpdatedBy(accountInfoModel.getCreatedByAppUserModel().getAppUserId());
			transactionModel.setBankAccountNo(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));
			transactionModel.setProcessingSwitchId(8L);
			transactionModel.setProcessingBankId(50110L);

			TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
			transactionDetailModel.setTransactionId(transactionModel.getTransactionId());
			// transactionDetailModel.setConsumerNo(MSISDN);
			transactionDetailModel.setActualBillableAmount(amount);
			transactionDetailModel.setSettled(true);
			transactionDetailModel.setProductId(2510734L); //TODO: 
			transactionDetailModel.setActualBillableAmount(amount);
			transactionDetailModel.setSettled(true);
			transactionModel.setConfirmationMessage("COMMISSION_SETTLEMENT");
			transactionModel.setNotificationMobileNo("COMM_SETT");
//			transactionDetailModel.setCustomField2(switchWrapper.getAccountInfoModel().getAccountNo());
//			transactionDetailModel.setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId().toString());

			transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);
			workFlowWrapper.setTransactionModel(transactionModel);

			OLAVO olavo = new OLAVO();
			olavo.setBalance(amount);
			olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
			olavo.setPayingAccNo(accountInfoModel.getAccountNo());
			olavo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
			olavo.setCustomerAccountTypeId(3L); //TODO:

			olavo.setReasonId(-1L);

			SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
			newSwitchWrapper.setBankId(50110L);
			newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			newSwitchWrapper.setOlavo(olavo);
			newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());
			switchWrapper = switchController.debit(newSwitchWrapper, null);

			if (switchWrapper.getOlavo().getResponseCode().equals("00")) {
				transactionManager.saveTransaction(workFlowWrapper);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private void postSundryToReconAccount(List<Object> parentResult, StakeholderBankInfoModel phoenixSundryAccount, StakeholderBankInfoModel commissionPoolAccount, Long sundaryAccountId) throws FrameworkCheckedException {

		if (commissionPoolAccount == null || phoenixSundryAccount == null) {
			logger.info("*** FRANCHISE COMMISSION TRANSFER CAN'T PROCEED, POOL/SUNDRRY ACCOUNT NOT FOUND");
			return;
		}

		Double totalUnpostedCommission = (Double) parentResult.get(0);

		// Start - Phoenix total unposted commission transaction.
		boolean success = debitCreditPhoenixAccounts(phoenixSundryAccount.getAccountNo(), commissionPoolAccount.getAccountNo(), totalUnpostedCommission);
		// Ends - Phoenix totalunposted FT Transaction.

		if (! success){
			return;
		}

		// IF PARENET FT SUCCESSFUL UPDATE, RECORDS POSTED = 1
		// Starts - Commission Mirror Account
		StakeholderBankInfoModel sundryAccount = getAccount(sundaryAccountId);
		this.doDebitCommissionMirrorAccount(totalUnpostedCommission, sundryAccount);
		// Ends - - Commission Mirror Account

		List<Long> unpostedcommissionTransactionIds = (List<Long>) parentResult.get(1);

		if (unpostedcommissionTransactionIds != null) {
			for (Long commissionTransactionModelId : unpostedcommissionTransactionIds) {
				this.franchiseCommissionHibernateDAO.updateCommissionTransactionPostedSettled(commissionTransactionModelId, SCHEDULER_APP_USER_ID);
			}
		}
	}
	
	private boolean debitCreditPhoenixAccounts(String fromAccountNo, String toAccountNo, Double transactionAmount) throws FrameworkCheckedException {
		boolean success = true;
		
		SwitchWrapper switchWrapperMain = new SwitchWrapperImpl();
		IntegrationMessageVO integrationMessageVOMain = new PhoenixIntegrationMessageVO();
		switchWrapperMain.setIntegrationMessageVO(integrationMessageVOMain);

		switchWrapperMain.setFromAccountNo(fromAccountNo);
		switchWrapperMain.setFromAccountType("20");
		switchWrapperMain.setFromCurrencyCode("586");

		switchWrapperMain.setToAccountNo(toAccountNo);
		switchWrapperMain.setToAccountType("20");
		switchWrapperMain.setToCurrencyCode("586");

		switchWrapperMain.setTransactionAmount(transactionAmount);
		switchWrapperMain.setCurrencyCode("586");

		switchWrapperMain.setWorkFlowWrapper(new WorkFlowWrapperImpl());
		switchWrapperMain.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
		AppUserModel appUserModelMain = new AppUserModel();
		appUserModelMain.setAppUserId(SCHEDULER_APP_USER_ID);
		ThreadLocalAppUser.setAppUserModel(appUserModelMain);
		switchWrapperMain.setBankId(50110L);
		switchWrapperMain.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		logger.info("FranchiseCommissionScheduler Going to make debitCredit tx from account " + fromAccountNo + " to Account: " + toAccountNo);
		switchWrapperMain = switchController.debitCreditAccount(switchWrapperMain);
		
		//if FT is unsuccessful, update success flag.
		if (switchWrapperMain.getIntegrationMessageVO().getResponseCode() == null || ! switchWrapperMain.getIntegrationMessageVO().getResponseCode().equals("00")){
			success = false;
		}

		return success;
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

	public FranchiseCommissionViewHibernateDAO getFranchiseCommissionHibernateDAO() {
		return franchiseCommissionHibernateDAO;
	}

	public void setFranchiseCommissionHibernateDAO(
			FranchiseCommissionViewHibernateDAO franchiseCommissionHibernateDAO) {
		this.franchiseCommissionHibernateDAO = franchiseCommissionHibernateDAO;
	}

	public CommissionTransactionHibernateDAO getCommissionTransactionHibernateDAO() {
		return commissionTransactionHibernateDAO;
	}

	public void setCommissionTransactionHibernateDAO(CommissionTransactionHibernateDAO commissionTransactionHibernateDAO) {
		this.commissionTransactionHibernateDAO = commissionTransactionHibernateDAO;
	}

	public CommissionStakeholderDAO getStakeholderDAO() {
		return stakeholderDAO;
	}

	public void setStakeholderDAO(CommissionStakeholderDAO stakeholderDAO) {
		this.stakeholderDAO = stakeholderDAO;
	}

	public SwitchController getSwitchController() {
		return switchController;
	}

	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}

}
