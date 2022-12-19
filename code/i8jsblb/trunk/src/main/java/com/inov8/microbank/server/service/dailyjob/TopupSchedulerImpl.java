package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;
import static com.inov8.microbank.common.util.SchedulerConstantsInterface.TOPUP_EXEC_FAIL;
import static com.inov8.microbank.common.util.SchedulerConstantsInterface.TOPUP_EXEC_SUCCESS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.FrameworkException;
import org.joda.time.DateTime;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TopupSchedulerExecModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.transactionmodule.hibernate.TransactionHibernateDAO;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.topupmodule.TopupManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.verifly.common.model.AccountInfoModel;

public class TopupSchedulerImpl {
	private static Logger logger = Logger.getLogger(TopupSchedulerImpl.class);

	private TransactionHibernateDAO transactionHibernateDAO;
	private SwitchController switchController;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private TopupManager topupManager;
	private PostedTransactionReportFacade postedTransactionReportFacade;
	private TransactionModuleManager transactionManager;
	 
	public void init() {
		DateTime dt = new DateTime();
		try {
			execute(dt.minusDays(1).toDate(), dt.plusDays(1).toDate());
		} catch (FrameworkException e) {
			logger.error("*** TOPUP SCHEDULER DISBURSEMENT FAILED *** Exception:" + e.getMessage());
			e.printStackTrace();
		} catch (FrameworkCheckedException e) {
			logger.error("*** TOPUP SCHEDULER DISBURSEMENT FAILED *** Exception:" + e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			logger.error("*** TOPUP SCHEDULER DISBURSEMENT FAILED *** Exception:" + e.getMessage());
			e.printStackTrace();
		}finally{
			logger.info("[TopupSchedulerImpl.execute] Completed Execution Flow. Now exiting Schdeuler..");
			ThreadLocalAppUser.remove();
		}
	}

	@SuppressWarnings("unchecked")
	public void execute(Date start, Date end) throws FrameworkCheckedException, FrameworkException {
		logger.info("************ STARTING TOPUP DISBURSEMENT TO PHOENIX ******************");

		/*
		 * Step 1. Load all transactions with status = complete and Date = today.
		 * */
		
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstantsInterface.COMPLETED);
		transactionModel.setCreatedOn(new Date());
		List<Object> resultList = transactionHibernateDAO.getTransactionsByCriteria(2510727L, SupplierProcessingStatusConstantsInterface.COMPLETED, new Date());
		Double amount = resultList.get(0) == null ? null : ((Double)resultList.get(0));
		List<TransactionModel> transactionsList = resultList.get(1) == null ? null : ((List<TransactionModel>)resultList.get(1));
		
		if( ! CollectionUtils.isEmpty(transactionsList) && null != amount){
			logger.info("[TopupSchedulerImpl.execute] "+ transactionsList.size() +" topup entries found for date:" + new Date());
			
			AppUserModel appUserModelMain = new AppUserModel();
			appUserModelMain.setAppUserId(SCHEDULER_APP_USER_ID);
			TopupSchedulerExecModel todayTopupExecModel = new TopupSchedulerExecModel();
			todayTopupExecModel.setAmount(amount);
			todayTopupExecModel.setCreatedByAppUserModel(appUserModelMain);
			todayTopupExecModel.setCreatedOn(new Date());
			todayTopupExecModel.setUpdatedByAppUserModel(appUserModelMain);
			todayTopupExecModel.setUpdatedOn(new Date());
			todayTopupExecModel.setStatus(TOPUP_EXEC_FAIL); 
			
			WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
			wrapper.setTransactionCodeModel(new TransactionCodeModel());
			wrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(wrapper);
			
			todayTopupExecModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
			
			topupManager.saveTopupExecModelRequiresNewTransaction(todayTopupExecModel);
			
		}else{
			logger.info("[TopupSchedulerImpl.execute] No transactions found for disbursement on Dated:" + transactionModel.getCreatedOn() + ". Loading previous entries now...");
		}
		
		/*
		 * Step 2. Load Entries for TopupExecModel with status = FAIL or Response Code != '00'.
		 * */
		TopupSchedulerExecModel model = new TopupSchedulerExecModel();
		model.setStatus(TOPUP_EXEC_FAIL);
		
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		sBaseWrapper.setBasePersistableModel(model);
		List<TopupSchedulerExecModel> unpostedTopupModelsList = new ArrayList<TopupSchedulerExecModel>();
		try {
			sBaseWrapper = topupManager.loadTopupExecModelList(sBaseWrapper);
			unpostedTopupModelsList = (List<TopupSchedulerExecModel>)(sBaseWrapper.getCustomList().getResultsetList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(! CollectionUtils.isEmpty(unpostedTopupModelsList)){
			logger.info("[TopupSchedulerImpl.execute] "+ (unpostedTopupModelsList.size()) +" topup entries found in Topup_Scheduler_Exec for FT");
			
			StakeholderBankInfoModel customePoolAccount = getAccount(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
			StakeholderBankInfoModel topupPoolAccount = getAccount(PoolAccountConstantsInterface.TOPUP_POOL_ID);

			/*
			 * Step 3. Check if this entry is already disbursed to phoenix but response code is not previously received timely.
			 * */
			//First, Get failed transactions Transaction code IDs and compare it with posted transaction report to check if FT is successful there...
			List<Long> trxCodeList = new ArrayList<Long>();
			for(TopupSchedulerExecModel topupModel : unpostedTopupModelsList) {
				if(topupModel.getRrn() == null && topupModel.getResponseCode() != null && ! topupModel.getResponseCode().equals("00")){//
					trxCodeList.add(topupModel.getTransactionCodeId());
					logger.info("[TopupSchedulerImpl.execute] TopupModelID:" + topupModel.getTopupSchedulerExecId() + " Response Code:"+ topupModel.getResponseCode() +" needs to be disbursed which was previously failed on " + topupModel.getUpdatedOn());
				}
			}
			
			//Get Response codes against RRN from Posted Transaction Report (synonym used for querying integration db.)
			Map<Long, String> rrnCodeMap = new HashMap<Long, String>();
			if(! CollectionUtils.isEmpty(trxCodeList)){
				rrnCodeMap = topupManager.getAllDisputedEntries(trxCodeList);
			}
			
			for(TopupSchedulerExecModel topupModel : unpostedTopupModelsList) {
				
				//check this entry is already settled but response code not updated in i8. if yes then update its response code and status and save
				if(topupModel.getResponseCode() != null && ! topupModel.getResponseCode().equals("00")){
					String responseCode = rrnCodeMap.get(topupModel.getTransactionCodeId());
					if(responseCode != null && responseCode.equals("00")){//when phoenix successfully processed transaction but i8 doesn't know it. verify phoenix response code.
						topupModel.setResponseCode("00");
						topupModel.setStatus(TOPUP_EXEC_SUCCESS);
						topupModel.setUpdatedOn(new Date());
						
						logger.info("[TopupSchedulerImpl.execute] TopupModelID:" + topupModel.getTopupSchedulerExecId() + " RRN:"+ topupModel.getRrn() +" was already disbursed successfully to Phoenix but i8 was out of sync with it. Now Updating Status to SUCCESS.");
						topupManager.saveTopupExecModel(topupModel);
						
						logger.info("[TopupSchedulerImpl.execute] Saving Posted Transaction Entries for TopupModelID:" + topupModel.getTopupSchedulerExecId() + " with RRN:"+ topupModel.getRrn());
						savePostedTransactionEntries(topupModel, customePoolAccount.getAccountNo(), topupPoolAccount.getAccountNo());
						continue;
					}
				}
				
				WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
				wrapper.setTransactionCodeModel(new TransactionCodeModel());
				wrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(wrapper);
				
				topupModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
				
				
				Double tAmount = topupModel.getAmount();//
				SwitchWrapper switchWrapper = null;
				try {
					switchWrapper = debitCreditPhoenixAccounts(customePoolAccount.getAccountNo(), topupPoolAccount.getAccountNo(), tAmount, wrapper.getTransactionCodeModel());
				} catch (Exception e) {
					topupModel.setStatus(TOPUP_EXEC_FAIL);
//					String rrn = (switchWrapper == null ? null : switchWrapper.getIntegrationMessageVO().getRetrievalReferenceNumber());
//					String resCode = (switchWrapper == null ? null : switchWrapper.getIntegrationMessageVO().getResponseCode());
//					topupModel.setRrn(rrn);
					topupModel.setResponseCode("-1");
					
					topupManager.saveTopupExecModelRequiresNewTransaction(topupModel);
					logger.error("[TopupSchedulerImpl.execute] TopupModelID:" + topupModel.getTopupSchedulerExecId() + " RRN:"+ topupModel.getRrn() +"  failed with response code: " + topupModel.getResponseCode());
					
					throw new FrameworkCheckedException("[TopupSchedulerImpl.execute] TopupModelID:" + topupModel.getTopupSchedulerExecId() + " RRN:"+ topupModel.getRrn() +"  failed with response code: " + topupModel.getResponseCode());
				}
				
				if (switchWrapper.getIntegrationMessageVO().getResponseCode() != null && switchWrapper.getIntegrationMessageVO().getResponseCode().equals("00")){
					topupModel.setStatus(TOPUP_EXEC_SUCCESS);
					topupModel.setRrn(switchWrapper.getIntegrationMessageVO().getRetrievalReferenceNumber());
					topupModel.setResponseCode(switchWrapper.getIntegrationMessageVO().getResponseCode());
					topupModel.setUpdatedOn(new Date());
					
				}
				
				logger.info("[TopupSchedulerImpl.execute] TopupModelID:" + topupModel.getTopupSchedulerExecId() + "  RRN:"+ topupModel.getRrn() +" saved after successful FT of amount: " + topupModel.getAmount());
				topupManager.saveTopupExecModel(topupModel);
	
				savePostedTransactionEntries(topupModel, customePoolAccount.getAccountNo(), topupPoolAccount.getAccountNo());
			}
			
		}else{
			logger.info("[TopupSchedulerImpl.execute] No Previously Unposted/Failed transactions found for disbursement");
		}
		
	}
	
	private void savePostedTransactionEntries(TopupSchedulerExecModel topupModel, String fromAccountNo, String toAccountNo) throws FrameworkCheckedException{
		
		List<Object> dataList = transactionHibernateDAO.getTransactionsByDate(2510727L, SupplierProcessingStatusConstantsInterface.COMPLETED, topupModel.getCreatedOn());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		
		//Create posted transactions for all entries.
		for (Object object : dataList) {
			Object[] objArray = (Object[])object;
			
			TransactionModel transaction = (TransactionModel)objArray[0];
			TransactionCodeModel transactionCode = (TransactionCodeModel)objArray[1];
			
			PostedTransactionReportModel postedTransaction = new PostedTransactionReportModel();
			
			postedTransaction.setAmount(transaction.getTransactionAmount());
			postedTransaction.setConsumerNo(transaction.getCustomerMobileNo());
			postedTransaction.setCreatedBy(transaction.getCreatedBy());
			postedTransaction.setCreatedOn(new Timestamp(transaction.getCreatedOn().getTime()));
			postedTransaction.setFromAccount(fromAccountNo);
			postedTransaction.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE);
			postedTransaction.setProductId(2510727L);
			postedTransaction.setRefCode(topupModel.getRrn());
			postedTransaction.setResponseCode("00");
			postedTransaction.setToAccount(toAccountNo);
			postedTransaction.setTransactionCodeId(transactionCode.getTransactionCodeId());

			baseWrapper.setBasePersistableModel(postedTransaction);
			logger.info("[TopupSchedulerImpl.execute] Saving PostedTransaction entry for transactionId:" + transaction.getTransactionId() + ". performed on Date:" + transaction.getCreatedOn() + " with RRN:" + topupModel.getRrn());
			baseWrapper = postedTransactionReportFacade.createOrUpdatePostedTransactionRequiresNewTransaction(baseWrapper);
			
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

	
	
	private SwitchWrapper debitCreditPhoenixAccounts(String fromAccountNo, String toAccountNo, Double transactionAmount, TransactionCodeModel transactionCodeModel) throws FrameworkCheckedException {
		
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
		switchWrapperMain.getWorkFlowWrapper().setTransactionCodeModel(transactionCodeModel);
		ProductModel product = new ProductModel();
		product.setProductId(2510727L);
		switchWrapperMain.getWorkFlowWrapper().setProductModel(product);
		AppUserModel appUserModelMain = new AppUserModel();
		appUserModelMain.setAppUserId(SCHEDULER_APP_USER_ID);
		ThreadLocalAppUser.setAppUserModel(appUserModelMain);
		switchWrapperMain.setBankId(50110L);
		switchWrapperMain.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		logger.info("topupExecScheduler Going to make debitCredit tx from account " + fromAccountNo + " to Account: " + toAccountNo);
		switchWrapperMain = switchController.debitCreditAccount(switchWrapperMain);
		
		/*if (switchWrapperMain.getIntegrationMessageVO().getResponseCode() == null || ! switchWrapperMain.getIntegrationMessageVO().getResponseCode().equals("00")){
			rrn = switchWrapperMain.getIntegrationMessageVO().getRetrievalReferenceNumber();
		}*/

		return switchWrapperMain;
	}


	public SwitchController getSwitchController() {
		return switchController;
	}

	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}

	public TransactionHibernateDAO getTransactionHibernateDAO() {
		return transactionHibernateDAO;
	}

	public void setTransactionHibernateDAO(
			TransactionHibernateDAO transactionHibernateDAO) {
		this.transactionHibernateDAO = transactionHibernateDAO;
	}

	public TopupManager getTopupManager() {
		return topupManager;
	}

	public void setTopupManager(TopupManager topupManager) {
		this.topupManager = topupManager;
	}

	public PostedTransactionReportFacade getPostedTransactionReportFacade() {
		return postedTransactionReportFacade;
	}

	public void setPostedTransactionReportFacade(
			PostedTransactionReportFacade postedTransactionReportFacade) {
		this.postedTransactionReportFacade = postedTransactionReportFacade;
	}

	public void setTransactionManager(TransactionModuleManager transactionManager) {
		this.transactionManager = transactionManager;
	}


}
