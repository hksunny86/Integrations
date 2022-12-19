package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.FrameworkException;
import org.joda.time.DateTime;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.commissionmodule.CommissionStakeholderDAO;
import com.inov8.microbank.server.dao.commissionmodule.hibernate.CommissionTransactionHibernateDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.transactionmodule.TransactionCodeGenerator;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;

@SuppressWarnings("all")
public class CommissionDayEndSchedulerImpl implements CommissionDayEndScheduler {
	private static Logger logger = Logger.getLogger(CommissionDayEndSchedulerImpl.class);

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
	private SupplierBankInfoManager supplierBankInfoManager;
	
	public void init() {
		DateTime dt = new DateTime();
		try {
			execute(dt.minusDays(1).toDate(), dt.plusDays(1).toDate());
		} catch (FrameworkException e) {
			logger.info("*** COMMISSION TRANSFER FAILED ***");
			e.printStackTrace();
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
	}

	public void execute(Date start, Date end) throws FrameworkCheckedException, FrameworkException {
		logger.info("********** STARTING COMMISSION SETTLEMENT *****************");

		DateTime startDateTime = new DateTime(start);
		// REMOVING TIME INFORMATION & MAKING DATE'S START.
		startDateTime = startDateTime.withTime(0, 0, 0, 0);
		DateTime endDateTime = new DateTime(end);
		// REMOVING TIME INFORMATION & MAKING DATE'S START.
		endDateTime = endDateTime.withTime(0, 0, 0, 0);

		List<Object> parentResult = commissionTransactionHibernateDAO.findTotalUnpostedCommission();
		
		//Cash to cash commission trx entries with posted = 0 and settled = 0
		List<Object> c2cParentResult = commissionTransactionHibernateDAO.findCashToCashTotalUnpostedCommission();
		List<Object> donationParentResult = commissionTransactionHibernateDAO.findDonationPaymentTotalUnpostedCommission();
		List<Object> dawatEIslamiParentResult = commissionTransactionHibernateDAO.findDawatEIslamiDonationPaymentTotalUnpostedCommission();
		List<Object> zongMLParentResult = commissionTransactionHibernateDAO.findZongMLPaymentTotalUnpostedCommission();
		List<Object> bulkPaymentParentResult = commissionTransactionHibernateDAO.findBulkLPaymentTotalUnpostedCommission();
		
	if ((CollectionUtils.isNotEmpty(parentResult) && parentResult.get(0) != null && parentResult.get(1) != null)
				|| (CollectionUtils.isNotEmpty(c2cParentResult) && c2cParentResult.get(0) != null && c2cParentResult.get(1) != null)
				|| (CollectionUtils.isNotEmpty(donationParentResult) && donationParentResult.get(0) != null && donationParentResult.get(1) != null)
				|| (CollectionUtils.isNotEmpty(dawatEIslamiParentResult) && dawatEIslamiParentResult.get(0) != null && dawatEIslamiParentResult.get(1) != null)
				|| (CollectionUtils.isNotEmpty(zongMLParentResult) && zongMLParentResult.get(0) != null && zongMLParentResult.get(1) != null)
				|| (CollectionUtils.isNotEmpty(bulkPaymentParentResult) && bulkPaymentParentResult.get(0) != null && bulkPaymentParentResult.get(1) != null)){

			StakeholderBankInfoModel customerPoolAccount = getAccount(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
			StakeholderBankInfoModel c2cSundaryAccount = getAccount(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID);
			StakeholderBankInfoModel commissionPoolAccount = getAccount(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
			StakeholderBankInfoModel bulkPaymentSundaryAccount = getAccount(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
			
//			StakeholderBankInfoModel donationSundryAccount = getAccount(PoolAccountConstantsInterface.DONATION_COLLECTION_SUNDRY_ACCOUNT_ID);
			SupplierBankInfoModel apotheCareSupplierInfo = getSupplierBankInfoModel(SupplierConstants.APOTHECARE_SUPPLIER_ID);
			SupplierBankInfoModel dawatEIslamiSupplierInfo = getSupplierBankInfoModel(SupplierConstants.DAWAT_E_ISLAMI_SUPPLIER_ID);
			SupplierBankInfoModel telcoSupplierInfo = getSupplierBankInfoModel(SupplierConstants.TELCO_SUPPLIER_ID);
			
			
			if (commissionPoolAccount == null || customerPoolAccount == null || c2cSundaryAccount == null || apotheCareSupplierInfo == null
					|| dawatEIslamiSupplierInfo == null || telcoSupplierInfo == null || bulkPaymentSundaryAccount == null) {
				logger.error("*** COMMISSION TRANSFER CAN'T PROCEED, POOL ACCOUNT(s) NOT FOUND");
				return;
			}

			boolean isC2CFTSuccessful = false;
			boolean isDonationFTSuccessful = false;
			boolean isDawatEIslamiFTSuccessful = false;
			boolean isMlFTSuccessful = false;
			boolean isFTSuccessful = false;
			boolean isBulkPaymentFTSuccessful = false;
			
			Double c2cTotalUnpostedCommission = 0d;
			Double donationTotalUnpostedCommission = 0d;
			Double totalUnpostedCommission = 0d;
			List<Long> unpostedcommissionTransactionIds = new ArrayList<Long>();
			List<Long> C2CUnpostedcommissionTransactionIds = new ArrayList<Long>();
			List<Long> donationUnpostedcommissionTransactionIds = new ArrayList<Long>();
			
			// Start - Phoenix total unposted commission transaction for all other transactions
			if (CollectionUtils.isNotEmpty(parentResult) && parentResult.get(0) != null && parentResult.get(1) != null) {
				try {
					double amount = (Double) parentResult.get(0);
					isFTSuccessful = debitCreditPhoenixAccounts(customerPoolAccount.getAccountNo(), commissionPoolAccount.getAccountNo(), amount);
				} catch (Exception e) {
					isFTSuccessful = false;
					logger.error("*** [CommissionDayEndSchedulerImpl] Fund Transfer failed from Customer Pool A/C to Commisison Recon Pool A/C. Exception: " + e.getMessage());
				}
				
				if (isFTSuccessful){
					//Update Amount for total Unposted Commission
					totalUnpostedCommission = (Double) parentResult.get(0);
					
					//Update IDs of total unposted Commission
					unpostedcommissionTransactionIds = (List<Long>) parentResult.get(1);
				}
				
			}
			
			//Now make FT for Cash To Cash unposted commission.[C2C Sundry to Commission Recon Pool Account]
			if (CollectionUtils.isNotEmpty(c2cParentResult) && c2cParentResult.get(0) != null && c2cParentResult.get(1) != null) {
				logger.info("*** [CommissionDayEndSchedulerImpl] Going to make FT from C2C Sundry to Commission Recon Pool Account ***");
				try {
					double amount = (Double) c2cParentResult.get(0);
					isC2CFTSuccessful = debitCreditPhoenixAccounts(c2cSundaryAccount.getAccountNo(), commissionPoolAccount.getAccountNo(), amount);
				}catch (Exception e) {
					isC2CFTSuccessful = false;
					logger.error("*** [CommissionDayEndSchedulerImpl] Fund Transfer failed from C2C Sundry A/C to Commisison Recon Pool A/C. Exception: " + e.getMessage());
				}
				
				if (isC2CFTSuccessful){
					//Update Amount for total Unposted Commission
					c2cTotalUnpostedCommission = (Double) c2cParentResult.get(0);
					totalUnpostedCommission += c2cTotalUnpostedCommission;
					
					//Update IDs of total unposted Commission
					C2CUnpostedcommissionTransactionIds = (List<Long>) c2cParentResult.get(1);
					unpostedcommissionTransactionIds.addAll(C2CUnpostedcommissionTransactionIds);
				}
				
			}
			
			//Now make FT for Donation trx unposted commission.[Supplier Core Account(SupplierBankInfo) to Commission Recon Pool Account]
			if (CollectionUtils.isNotEmpty(donationParentResult) && donationParentResult.get(0) != null && donationParentResult.get(1) != null) {
				logger.info("*** [CommissionDayEndSchedulerImpl] Going to make FT from Supplier Core Account(SupplierBankInfo) to Commission Recon Pool Account ***");
				try {
					double amount = (Double) donationParentResult.get(0);
					isDonationFTSuccessful = debitCreditPhoenixAccounts(apotheCareSupplierInfo.getAccountNo(), commissionPoolAccount.getAccountNo(), amount);
				}catch (Exception e) {
					isDonationFTSuccessful = false;
					logger.error("*** [CommissionDayEndSchedulerImpl] Fund Transfer failed from Supplier Core Account(SupplierBankInfo) to Commisison Recon Pool A/C. Exception: " + e.getMessage());
				}
				
				if (isDonationFTSuccessful){
					//Update Amount for total Unposted Commission
					donationTotalUnpostedCommission = (Double) donationParentResult.get(0);
					totalUnpostedCommission += donationTotalUnpostedCommission;
					
					//Update IDs of total unposted Commission
					donationUnpostedcommissionTransactionIds = (List<Long>) donationParentResult.get(1);
					unpostedcommissionTransactionIds.addAll(donationUnpostedcommissionTransactionIds);
				}
				
			}
			
			//Now make FT for Donation trx unposted commission.[Supplier Core Account(SupplierBankInfo) to Commission Recon Pool Account]
			if (CollectionUtils.isNotEmpty(dawatEIslamiParentResult) && dawatEIslamiParentResult.get(0) != null && dawatEIslamiParentResult.get(1) != null) {
				logger.info("*** [CommissionDayEndSchedulerImpl] Going to make FT from Dawat E Islami Core Account(SupplierBankInfo) to Commission Recon Pool Account ***");
				try {
					double amount = (Double) dawatEIslamiParentResult.get(0);
					isDawatEIslamiFTSuccessful = debitCreditPhoenixAccounts(dawatEIslamiSupplierInfo.getAccountNo(), commissionPoolAccount.getAccountNo(), amount);
				}catch (Exception e) {
					isDawatEIslamiFTSuccessful = false;
					logger.error("*** [CommissionDayEndSchedulerImpl] Fund Transfer failed from Supplier Core Account(SupplierBankInfo) to Commisison Recon Pool A/C. Exception: " + e.getMessage());
				}
				
				if (isDawatEIslamiFTSuccessful){
					//Update Amount for total Unposted Commission
					donationTotalUnpostedCommission = (Double) dawatEIslamiParentResult.get(0);
					totalUnpostedCommission += donationTotalUnpostedCommission;
					
					//Update IDs of total unposted Commission
					donationUnpostedcommissionTransactionIds = (List<Long>) dawatEIslamiParentResult.get(1);
					unpostedcommissionTransactionIds.addAll(donationUnpostedcommissionTransactionIds);
				}
				
			}
			
			//Now make FT for Donation trx unposted commission.[Supplier Core Account(SupplierBankInfo) to Commission Recon Pool Account]
			if (CollectionUtils.isNotEmpty(zongMLParentResult) && zongMLParentResult.get(0) != null && zongMLParentResult.get(1) != null) {
				logger.info("*** [CommissionDayEndSchedulerImpl] Going to make FT from Supplier Core Account(SupplierBankInfo) to Commission Recon Pool Account ***");
				try {
					double amount = (Double) zongMLParentResult.get(0);
					isMlFTSuccessful = debitCreditPhoenixAccounts(telcoSupplierInfo.getAccountNo(), commissionPoolAccount.getAccountNo(), amount);
				}catch (Exception e) {
					isMlFTSuccessful = false;
					logger.error("*** [CommissionDayEndSchedulerImpl] Fund Transfer failed from Supplier Core Account(SupplierBankInfo) to Commisison Recon Pool A/C. Exception: " + e.getMessage());
				}
				
				if (isMlFTSuccessful){
					//Update Amount for total Unposted Commission
					Double mlTotalUnpostedCommission = (Double) zongMLParentResult.get(0);
					totalUnpostedCommission += mlTotalUnpostedCommission;
					
					//Update IDs of total unposted Commission
					List<Long> mlUnpostedcommissionTransactionIds = (List<Long>) zongMLParentResult.get(1);
					unpostedcommissionTransactionIds.addAll(mlUnpostedcommissionTransactionIds);
				}
				
			}
			
			//Now make FT for Donation trx unposted commission.[Supplier Core Account(SupplierBankInfo) to Commission Recon Pool Account]
			if (CollectionUtils.isNotEmpty(bulkPaymentParentResult) && bulkPaymentParentResult.get(0) != null && bulkPaymentParentResult.get(1) != null) {
				logger.info("*** [CommissionDayEndSchedulerImpl] Going to make FT from Bulk Payment Sundry Account to Commission Recon Pool Account ***");
				try {
					double amount = (Double) bulkPaymentParentResult.get(0);
					isBulkPaymentFTSuccessful = debitCreditPhoenixAccounts(bulkPaymentSundaryAccount.getAccountNo(), commissionPoolAccount.getAccountNo(), amount);
				}catch (Exception e) {
					isBulkPaymentFTSuccessful = false;
					logger.error("*** [CommissionDayEndSchedulerImpl] Fund Transfer failed from Bulk Payment Sundry Account to Commisison Recon Pool A/C. Exception: " + e.getMessage());
				}
				
				if (isBulkPaymentFTSuccessful){
					//Update Amount for total Unposted Commission
					donationTotalUnpostedCommission = (Double) bulkPaymentParentResult.get(0);
					totalUnpostedCommission += donationTotalUnpostedCommission;
					
					//Update IDs of total unposted Commission
					donationUnpostedcommissionTransactionIds = (List<Long>) bulkPaymentParentResult.get(1);
					unpostedcommissionTransactionIds.addAll(donationUnpostedcommissionTransactionIds);
				}
				
			}
			
			// IF PARENET FT SUCCESSFUL UPDATE, RECORDS POSTED = 1
			if (isFTSuccessful || isC2CFTSuccessful || isDonationFTSuccessful || isDawatEIslamiFTSuccessful || isMlFTSuccessful || isBulkPaymentFTSuccessful) {

				// Starts - Commission Mirror Account
				StakeholderBankInfoModel commissionMirrorAccount = getAccount(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
				this.doDebitMirrorAccount(totalUnpostedCommission, commissionMirrorAccount);
				// Ends - - Commission Mirror Account
				
				//this is already done in c2c tx. so no need to 
				/*StakeholderBankInfoModel c2cSundryOLAAccount = getAccount(PoolAccountConstantsInterface.CASH_TO_CASH_SUNDRY_ACCOUNT_ID);
				this.doDebitMirrorAccount(c2cTotalUnpostedCommission, c2cSundryOLAAccount);*/
				
				if (unpostedcommissionTransactionIds != null) {
					for (Long commissionTransactionModelId : unpostedcommissionTransactionIds) {
						try {
							logger.info("Updated CommissionTransaction to POSTED = 1 for commTrxId: " + commissionTransactionModelId);
							this.commissionTransactionHibernateDAO.updateCommissionTransactionPosted(commissionTransactionModelId, SCHEDULER_APP_USER_ID);
							
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("Commission Scheduler - Update CommissionTransaction Failed for commTrxId: " + commissionTransactionModelId);
							
						}
					}
				}
			}

			// NOW FIND ALL COMMISSION TRANSACTION WHICH WERE POSTED TO PHONIX
			// BUT NOT SETTLETED.
			List<Object[]> childResult = commissionTransactionHibernateDAO.findCommissionStakeholders();
			for (Object[] stakeHolderInfo : childResult) {
				Long commissionStakeholderId = (Long) stakeHolderInfo[0];
				Double totalCommission = (Double) stakeHolderInfo[1];
				String commissionPartnerAccount = null;

				try {
					List<StakeholderBankInfoModel> stakeHolderAccountInfo = commissionTransactionHibernateDAO
							.findCommissionStakeholderBankAccount(commissionStakeholderId);
					if (stakeHolderAccountInfo != null && stakeHolderAccountInfo.size() > 0) {
						StakeholderBankInfoModel bankInfoModel = stakeHolderAccountInfo.get(0);
						commissionPartnerAccount = bankInfoModel.getAccountNo();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (commissionPoolAccount == null || commissionPartnerAccount == null){
					logger.error(commissionPoolAccount == null ? "commissionPoolAccount " : "commissionPartnerAccount " + " Not Found.");
					continue;
				}
				
				// CALL PHOENIX FUND TRANSFER TO SETTLE COMMISION FROM POOL
				// TO PARTNER'S ACCOUNT.
				try {
					SwitchWrapper switchWrapper = new SwitchWrapperImpl();
					IntegrationMessageVO integrationMessageVO = new PhoenixIntegrationMessageVO();
					switchWrapper.setIntegrationMessageVO(integrationMessageVO);

					switchWrapper.setFromAccountNo(commissionPoolAccount.getAccountNo());
					switchWrapper.setFromAccountType("20");
					switchWrapper.setFromCurrencyCode("586");

					switchWrapper.setToAccountNo(commissionPartnerAccount);
					switchWrapper.setToAccountType("20");
					switchWrapper.setToCurrencyCode("586");

					DecimalFormat Currency = new DecimalFormat("#0.00");
					String howmuch = Currency.format(totalCommission);

					switchWrapper.setTransactionAmount(totalCommission);
					switchWrapper.setCurrencyCode("586");

					switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
					switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
					AppUserModel appUserModel = new AppUserModel();
					appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
					ThreadLocalAppUser.setAppUserModel(appUserModel);
					switchWrapper.setBankId(50110L);
					switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

					try {

						switchWrapper = switchController.debitCreditAccount(switchWrapper);
						
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

					String responseCode = switchWrapper.getIntegrationMessageVO().getResponseCode();
					if (StringUtils.isNotEmpty(responseCode) && responseCode.equals("00")) {
						List<Long> commissionTransactionIds = commissionTransactionHibernateDAO.findCommissionTransactions(commissionStakeholderId);
						if (commissionTransactionIds != null) {
							for (Long id : commissionTransactionIds) {
								Long commissionTransactionId = id;
								boolean resultStatus = commissionTransactionHibernateDAO.updateCommissionTransaction(commissionTransactionId, SCHEDULER_APP_USER_ID);
								logger.info("COMMISSION SETTLETED FOR commissionTransactionId = " + commissionTransactionId);
							}
						}
						logger.info("COMMISSION SETTLETED FOR STAKEHOLDERID = " + commissionStakeholderId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			logger.info("********** NO COMMISSION SETTLEMENT JOB FOUND *****************");
		}
	}
	
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

	private boolean doDebitMirrorAccount(Double amount, StakeholderBankInfoModel accountInfoModel) {
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
		logger.info("CommissionDayEndScheduler Going to make debitCredit tx from account " + fromAccountNo + " to Account: " + toAccountNo);
		switchWrapperMain = switchController.debitCreditAccount(switchWrapperMain);
		
		//if FT is unsuccessful, update success flag.
		if (switchWrapperMain.getIntegrationMessageVO().getResponseCode() == null || ! switchWrapperMain.getIntegrationMessageVO().getResponseCode().equals("00")){
			logger.error("[CommissionDayEndSchedulerImpl] Failed to perform FT. Response Code:" + switchWrapperMain.getIntegrationMessageVO().getResponseCode());
			success = false;
		}

		return success;
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

	public SupplierBankInfoManager getSupplierBankInfoManager() {
		return supplierBankInfoManager;
	}

	public void setSupplierBankInfoManager(
			SupplierBankInfoManager supplierBankInfoManager) {
		this.supplierBankInfoManager = supplierBankInfoManager;
	}
}
