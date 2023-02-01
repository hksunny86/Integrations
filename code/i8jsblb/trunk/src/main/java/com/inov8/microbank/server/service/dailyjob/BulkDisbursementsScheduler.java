package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_TX_AMOUNT;
import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.fop.fo.properties.SrcMaker;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.BulkDisbursementType;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.disbursementmodule.BulkDisbursementDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.bulkdisbursements.BulkDisbursementsManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.transactionmodule.TransactionCodeGenerator;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.thoughtworks.xstream.XStream;

@SuppressWarnings("all")
public class BulkDisbursementsScheduler {
	private static Logger logger = Logger.getLogger(BulkDisbursementsScheduler.class);

	private BulkDisbursementDAO bulkDisbursementDao;
	private BulkDisbursementsManager bulkDisbursementsManager;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private MessageSource messageSource;
	private SmsSender smsSender;
	TransactionModuleManager transactionManager;

	public void init() {
		try {
			activateDailyBulkDisbursements(BulkDisbursementType.SALARY_DISBURSEMENT, new Date());
		} catch (FrameworkCheckedException e) {
			logger.debug("*** BULK DISBURSEMENT IS FAILED DUE TO SYSTEM ERROR ***");
			e.printStackTrace();
		}
	}

	public void activateDailyBulkDisbursements(BulkDisbursementType disbursementType, Date end) throws FrameworkCheckedException {
		logger.info("********** STARTING BULK DISBURSEMENT *****************");
		// TODO: 1. Find salary type disbursements within the date range
		// provided from BulkDisbursementsModel.
		// 2. Make sum of all & perform Phoenix FT from employer account to
		// pool account.
		// 3. Transfer salary for each individual account.
		// 4. bypass credit limit, if there is any for salary transfer.
		// 5. Send SMS notification at salary transfer successful.

//		StakeholderBankInfoModel sourceAccount = null;
//		
//		if (BulkDisbursementType.SALARY_DISBURSEMENT == disbursementType) {
//			sourceAccount = getAccount(PoolAccountConstantsInterface.ARMY_BULK_SALARY_DISBURSEMENT_ACCOUNT);
//		} else if (BulkDisbursementType.FUEL_DISBURSEMENT == disbursementType) {
//			sourceAccount = getAccount(PoolAccountConstantsInterface.FUEL_DISBURSEMENT_ACCOUNT);
//		}
		
		StakeholderBankInfoModel disbursmentCoreAccount = getAccount(PoolAccountConstantsInterface.BULK_DISBURSEMENT_POOL_ACCOUNT_CORE);
		
		StakeholderBankInfoModel disbursmentOLAAccount = getAccount(PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA);
		
		Integer[] types = {disbursementType.getValue()};
		
		if( disbursmentCoreAccount.getAccountNo() != null )
		{
			//Find disbursements which are unposted at Phoenix and obviously unsettled at BB
			List<BulkDisbursementsModel> unpostedAccounts = bulkDisbursementDao.findDueDisbursement(ProductConstantsInterface.BULK_DISBURSEMENT, types, end, Boolean.FALSE, Boolean.FALSE);

			if (CollectionUtils.isNotEmpty(unpostedAccounts))
			{
				logger.info(unpostedAccounts.size() + " unposted disbursement job(s) found.");
//				Double totalDisbursableAmount = bulkDisbursementDao.findTotalDueDisbursement(ProductConstantsInterface.BULK_DISBURSEMENT, disbursementType.getValue(), end, Boolean.FALSE, Boolean.FALSE);
//				totalDisbursableAmount = Double.valueOf(Formatter.formatDouble(totalDisbursableAmount));
//				if (totalDisbursableAmount != null)
//				{
//					if (disbursmentPoolAccount.getAccountNo() == null)
//					{
//						logger.info("Bulk Disbursement Pool account not found. can't proceed bulk disbursement at Phoenix");
//					}
//					else
//					{
//						 Fund Transfer on Phoenix from sourceAccount to customerPoolAccount
						try
						{
							Map debitAccountsMap = new HashMap<String,Double>();
							Map bulkDisbursmentsMap = new HashMap<String,List<BulkDisbursementsModel>>();
							List<BulkDisbursementsModel> list = null;
							String scrAccNo = "";
							for (BulkDisbursementsModel bulkDisbursementsModel : unpostedAccounts) {
								scrAccNo = bulkDisbursementsModel.getSourceACNo();
								
								bulkDisbursementsModel.setPosted(Boolean.TRUE);
								bulkDisbursementsModel.setPostedOn(new Date());
								
								if(bulkDisbursmentsMap.get(scrAccNo) == null){
									list = new ArrayList<BulkDisbursementsModel>();
									list.add(bulkDisbursementsModel);
									bulkDisbursmentsMap.put(scrAccNo,list);
								}else{
									list = (List<BulkDisbursementsModel>)bulkDisbursmentsMap.get(scrAccNo);
									list.add(bulkDisbursementsModel);
									bulkDisbursmentsMap.put(scrAccNo, list);
								}
								debitAccountsMap.put(scrAccNo, ((debitAccountsMap.get(scrAccNo)!=null)?(Double)debitAccountsMap.get(scrAccNo):0) + bulkDisbursementsModel.getAmount());
							}
							
							
							//Mark POSTED as true and Core FT for all disbursements 
							logger.info("Total source Accounts:" + debitAccountsMap.size());
							
							Iterator<String> itr = debitAccountsMap.keySet().iterator();
							while(itr.hasNext()){
								String accountNo = itr.next();
								
								StakeholderBankInfoModel shBankInfo = new StakeholderBankInfoModel();
								shBankInfo.setAccountNo(accountNo);
								
								Double amount = (Double) debitAccountsMap.get(accountNo);
								List<BulkDisbursementsModel> bulkDisbursmentsList = (List<BulkDisbursementsModel>)bulkDisbursmentsMap.get(accountNo);
								
								logger.info("@@@ Leg1: Summary Before Transfer Funds... \n "+
											"@@@ Unposted disbursements size:" + bulkDisbursmentsList.size()+"\n"+
											"@@@ Source Account number:" + accountNo+"\n"+
											"@@@ Total Ammount :" + amount+"\n"
											);
								
								WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
								bulkDisbursementsManager.makeDebitCreditAccount(accountNo,disbursmentCoreAccount.getAccountNo(),amount, bulkDisbursmentsList,wrapper);
							}
						}
						catch (Exception e)
						{
							logger.error(e.getMessage(),e);
						}
//					}
				}
			}
			else
			{
				logger.info("No unposted disbursement jobs found.");
			}

			//Find due disbursements which are posted at Phoenix but unsettled at BB
			List<BulkDisbursementsModel> unsettledBulkDisbursementsModels = bulkDisbursementDao.findDueDisbursement(ProductConstantsInterface.BULK_DISBURSEMENT, types, end, Boolean.TRUE, Boolean.FALSE);

			if (CollectionUtils.isNotEmpty(unsettledBulkDisbursementsModels))
			{
				logger.info(unsettledBulkDisbursementsModels.size() + " unsettled disbursement job(s) found.");

				for (BulkDisbursementsModel disbursement : unsettledBulkDisbursementsModels)
				{
					try
					{
						String salaryDisbursementNotification = this.messageSource.getMessage("SALARY.DISBURSEMENT.NOTIFICATION",
								new Object[] { disbursement.getMobileNo(), Formatter.formatDouble(disbursement.getAmount()), disbursement.getDescription() }, null);
						
						WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
						bulkDisbursementsManager.makeTransferFund(disbursmentOLAAccount, disbursement, salaryDisbursementNotification, workFlowWrapper);
						
						SmsMessage smsMessage = new SmsMessage(disbursement.getMobileNo(), salaryDisbursementNotification);
						this.smsSender.send(smsMessage);
						smsMessage.setMessageText(salaryDisbursementNotification);
						smsMessage.setMobileNo(disbursement.getMobileNo());
						smsMessage.setMessageType("ZINDIGI");
						smsMessage.setTitle("Bulk Sallery Disbursement");
						this.smsSender.pushNotification(smsMessage);
					}
					catch (Exception e)
					{
						logger.error("Exception occurred while tring to Settle customer salary : " + disbursement.getMobileNo(), e);
					}
				}

			}
			else
			{
				logger.info("No unsettled disbursement jobs found.");
			}
//		}
//		else
//		{
//			logger.info("Bulk disbursement Account not found. can't proceed bulk disbursement at Middleware and BB.");
//		}
	}

	public StakeholderBankInfoModel getAccount(Long key) {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setPrimaryKey(key);
		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		try {
			stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper)
					.getBasePersistableModel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stakeholderBankInfoModel;
	}

	public void setBulkDisbursementDao(BulkDisbursementDAO bulkDisbursementDao) {
		this.bulkDisbursementDao = bulkDisbursementDao;
	}

	public void setBulkDisbursementsManager(BulkDisbursementsManager bulkDisbursementsManager) {
		this.bulkDisbursementsManager = bulkDisbursementsManager;
	}

	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}
	public void setTransactionManager(TransactionModuleManager transactionManager) {
		this.transactionManager = transactionManager;
	}


}
