package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_TX_AMOUNT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.PaymentTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.disbursementmodule.BulkDisbursementDAO;
import com.inov8.microbank.server.dao.mfsmodule.PaymentTypeDAO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.bulkdisbursements.BulkDisbursementsManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;

@SuppressWarnings("all")
public class BulkPaymentsScheduler {
	
	private static Logger logger = Logger.getLogger(BulkPaymentsScheduler.class);

	private StakeholderBankInfoModel bulkPaymentPoolCoreAccount = null;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private BulkDisbursementsManager bulkDisbursementsManager;
	private BulkDisbursementDAO bulkDisbursementDao;
	private CommandManager commandManager;
	private MessageSource messageSource;
	private Integer transactionChunk;
	private SmsSender smsSender;
	private PaymentTypeDAO paymentTypeDAO;
	Map<Integer, String> paymentTypeMap = new HashMap<Integer, String>();
	Integer[] paymentTypes;
	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
	private TransactionModuleManager transactionManager;
	private TransactionDetailMasterManager transactionDetailMasterManager;
	private ProductManager productManager;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;

	
	public void init() {
			
		Date currentDateTime = GregorianCalendar.getInstance(TimeZone.getDefault()).getTime();
		
		if(bulkPaymentPoolCoreAccount == null) {

			bulkPaymentPoolCoreAccount = this.getStakeholderBankInfoModel(PoolAccountConstantsInterface.BULK_DISBURSEMENT_POOL_ACCOUNT_CORE);
		}
		
	
		if (paymentTypes ==  null) {
			
			try {//TODO test types population..
				
				paymentTypes = getPaymentTypesArray();
				
			}catch (Exception e) {
				logger.error("[BulkPaymentScheduler.init] Error in loading Payment Type. ");
				logger.error(e.getMessage(),e);
				return;
			}
		}
		
		SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("EEE.MMMMM.dd.yyyy.hh:mm:ss.aaa");

		long start = System.currentTimeMillis();
		
		logger.info("\n\n:-Started Bulk Payment Scheduler on "+currentDateTime);
		
		int olaTrx = 0;
		
		try {
			
			doStart(currentDateTime);
		
		} catch (Exception e) {
		
			logger.debug("\n\n*** BULK Payment IS FAILED DUE TO SYSTEM ERROR ***" + e.getLocalizedMessage());
			e.printStackTrace();
		} 

		logger.info("\n\n:---Ended Bulk.Payment. Scheduler Took : "+ ((System.currentTimeMillis() - start)/1000) + " Second(s) for OLA Transactions : "+olaTrx);
	}

	
	/**
	 * @param currentDateTime
	 * @return
	 * @throws Exception 
	 */
	private List<BulkDisbursementsModel> postCoreIDPTransactions(Map<String, Double> creditAccountsMap, CopyOnWriteArrayList<BulkDisbursementsModel> bulkDisbursementsModelList) throws Exception {	
				
 		Set<String> keySet = creditAccountsMap.keySet();
 		
 		for (String sourceAccountNumber : keySet) {
 			
 			Double sumAmount = creditAccountsMap.get(sourceAccountNumber); 			
			
			if(sourceAccountNumber != null && bulkPaymentPoolCoreAccount != null && sumAmount != null) {
				WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
				String responseCode = bulkDisbursementsManager.postCoreFundTransfer(sourceAccountNumber, bulkPaymentPoolCoreAccount.getAccountNo(), sumAmount, ProductConstantsInterface.BULK_PAYMENT, wrapper);

				if("00".equals(responseCode)) {
				
					bulkDisbursementsManager.saveOrUpdateCollection(bulkDisbursementsModelList);
				
				} else {
					
					for(BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementsModelList) {
					
						if(bulkDisbursementsModel.getSourceACNo().equalsIgnoreCase(sourceAccountNumber)) {
							bulkDisbursementsModelList.remove(bulkDisbursementsModel);
						}
					}					
				}
			}
		}
		
		return bulkDisbursementsModelList;
	}
	
	
 	public void postCoreFundTransfer(String fromAccountNumber, String toAccountNumber, Double amount, Long productId) throws WorkFlowException, Exception {
					
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);		
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
		switchWrapper.setMiddlewareIntegrationMessageVO(new MiddlewareMessageVO());
		switchWrapper.setTransactionAmount(amount);
		
		switchWrapper.setFromAccountNo(fromAccountNumber);	
		switchWrapper.setFromAccountType("20");
		switchWrapper.setFromCurrencyCode("586");			
		
		switchWrapper.setToAccountNo(toAccountNumber);
		switchWrapper.setToAccountType("20");
		switchWrapper.setToCurrencyCode("586");
		
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());		
		workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);		
		workFlowWrapper.setTransactionModel(new TransactionModel());		

		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		
//		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
//		switchWrapper.setAccountInfoModel(accountInfoModelCore);
//		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
//		_workFlowWrapper.setAccountInfoModel(accountInfoModelCore);
//		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
//		_workFlowWrapper.putObject(CORE_TRANSFER_OUT_SWITCH, switchWrapper);

		/*
		 *** 	SENDING TO CORE BANKING.
		 */				
		logger.info("JS Core > SENDING CORE JS.");
		logger.info("JS Core > FROM ACCOUNT : "+switchWrapper.getFromAccountNo());
		logger.info("JS Core > TO ACCOUNT : "+switchWrapper.getToAccountNo());
		logger.info("JS Core > AMOUNT : "+switchWrapper.getTransactionAmount());
		
		
		switchWrapper = phoenixFinancialInstitution.creditAccountAdvice(switchWrapper);
		
//		logger.info("Core Balance = "+coreBalance+" - "+switchWrapper.getTransactionAmount()+" = "+ (olaBalance - switchWrapper.getTransactionAmount()));
		
//		coreBalance -= switchWrapper.getTransactionAmount();
	}
	
 	/**
 	 * @param sourceAccount
 	 * @param customerPoolAccount
 	 * @param totalAmount
 	 * @param bulkDisbursementsModelList
 	 * @throws FrameworkCheckedException
 	 */
 	public void rollBack(StakeholderBankInfoModel sourceAccount, StakeholderBankInfoModel customerPoolAccount, Double totalAmount, List<BulkDisbursementsModel> bulkDisbursementsModelList ) throws FrameworkCheckedException {
 		

		logger.error("\n\nROLLBACK .... Amount "+totalAmount);
 		
//		bulkDisbursementsManager.makeDebitCreditAccount(sundryIDPStakeholderBankInfo, null, totalAmount, bulkDisbursementsModelList, ProductConstantsInterface.BULK_PAYMENT);
 	}
	

// 	private final Long TOTAL_AMOUNT_CREDIT = -1L;
 	
	private Map<String, Double> createCreditAccountsMap(List<BulkDisbursementsModel> bulkDisbursementsModelList) {	
		
	    Map<String, Double> creditAccountsMap = new HashMap<String, Double>(0);
	    String sourceAccountNumber = "";
	    Double totalAmountCredit = 0.0D;
	    
	    for (BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementsModelList) {
	    		sourceAccountNumber = bulkDisbursementsModel.getSourceACNo();
	    		bulkDisbursementsModel.setPosted(Boolean.TRUE);
	    		bulkDisbursementsModel.setPostedOn(new Date());
	    		creditAccountsMap.put(sourceAccountNumber, ((creditAccountsMap.get(sourceAccountNumber)!=null)?(Double)creditAccountsMap.get(sourceAccountNumber):0) + bulkDisbursementsModel.getAmount());
	    
	    		totalAmountCredit += bulkDisbursementsModel.getAmount();
	    } 
	    
	    creditAccountsMap.put("TOTAL_CREDIT_AMOUNT", totalAmountCredit);
	    
	    return creditAccountsMap;
	}
 	
 	
 	private void doStart(Date currentDateTime) throws Exception {
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		List<BulkDisbursementsModel> _bulkDisbursementsModelList = bulkDisbursementDao.findDueDisbursement(ProductConstantsInterface.BULK_PAYMENT, paymentTypes, currentDateTime, Boolean.FALSE, Boolean.FALSE);
		
		CopyOnWriteArrayList<BulkDisbursementsModel> bulkDisbursementsModelList = new CopyOnWriteArrayList<BulkDisbursementsModel>(_bulkDisbursementsModelList);
		
		logger.info("\nTotal Transactions to be Settled Found "+bulkDisbursementsModelList.size());
		
		if(bulkDisbursementsModelList.size() == 0) {
			return;
		}
		
	    Map<String, Double> creditAccountsMap = createCreditAccountsMap(bulkDisbursementsModelList);

		if (CollectionUtils.isNotEmpty(bulkDisbursementsModelList)) {
			
			Double TOTAL_CREDIT_AMOUNT = creditAccountsMap.get("TOTAL_CREDIT_AMOUNT");
			creditAccountsMap.remove("TOTAL_CREDIT_AMOUNT");			
			
			postCoreIDPTransactions(creditAccountsMap, bulkDisbursementsModelList);
			bulkDisbursementsModelList.clear();
			creditAccountsMap.clear();
		}
		
		
		_bulkDisbursementsModelList = bulkDisbursementDao.findDueDisbursement(ProductConstantsInterface.BULK_PAYMENT, paymentTypes, currentDateTime, Boolean.TRUE, Boolean.FALSE);		
		bulkDisbursementsModelList.addAll(_bulkDisbursementsModelList);
		
		creditAccountsMap = createCreditAccountsMap(bulkDisbursementsModelList);		
		
		if (CollectionUtils.isNotEmpty(bulkDisbursementsModelList)) {
			
			Double TOTAL_CREDIT_AMOUNT = creditAccountsMap.get("TOTAL_CREDIT_AMOUNT");
			creditAccountsMap.remove("TOTAL_CREDIT_AMOUNT");			
			
			switchWrapper = settleOLAIDPTransactions(TOTAL_CREDIT_AMOUNT, bulkDisbursementsModelList);
		}
		
 		if (switchWrapper.getWorkFlowWrapper() != null) {
			loadAndForwardAccountToQueue(switchWrapper.getWorkFlowWrapper());
		}

		if (CollectionUtils.isNotEmpty(bulkDisbursementsModelList)) {			
			createThreads(bulkDisbursementsModelList, transactionChunk);
		}	    		
 	}
 	
	public void loadAndForwardAccountToQueue(final WorkFlowWrapper workFlowWrapper) throws InterruptedException{
		creditAccountQueingPreProcessor.startProcessing(workFlowWrapper);
	}

	/**
	 * @param disbursementType
	 * @param end
	 * @throws FrameworkCheckedException
	 */
 	
 	
	public SwitchWrapper settleOLAIDPTransactions(Double totalAmountCredit, CopyOnWriteArrayList<BulkDisbursementsModel> bulkDisbursementsModelList) throws FrameworkCheckedException {
    	
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.putObject(KEY_TX_AMOUNT, totalAmountCredit);		
		switchWrapper.setBankId(50110L);
//		switchWrapper.setFromAccountNo(bulkPaymentPoolCoreAccount.getAccountNo());
					
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());		
		workFlowWrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);		
		workFlowWrapper.setTransactionModel(new TransactionModel());		
		
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);

		switchWrapper = ((OLAVeriflyFinancialInstitutionImpl) olaVeriflyFinancialInstitution).bulkDisbursmentPayment(switchWrapper);
		
		if(switchWrapper.getOlavo().getResponseCode().equals("00")) {

			bulkDisbursementsManager.saveOrUpdateCollection(bulkDisbursementsModelList);

			saveTransactionData(switchWrapper, bulkDisbursementsModelList);			
			
		} else {
			
			for(BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementsModelList) {
				
				bulkDisbursementsModelList.remove(bulkDisbursementsModel);
			}
		}
		
		return switchWrapper;
	}

	
	
	private void saveTransactionData(SwitchWrapper switchWrapper, CopyOnWriteArrayList<BulkDisbursementsModel> bulkDisbursementsModelList) {

		ProductModel productModel = new ProductModel(ProductConstantsInterface.BULK_PAYMENT);
		
		try {
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(productModel);
			this.productManager.loadProduct(baseWrapper);
			productModel = (ProductModel) baseWrapper.getBasePersistableModel();
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return;
		}		
		
		TransactionModel transactionModel = switchWrapper.getWorkFlowWrapper().getTransactionModel();

		transactionModel.setTransactionCodeIdTransactionCodeModel(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel());
	    transactionModel.setConfirmationMessage(" _ ");
	    transactionModel.setTransactionAmount((Double)switchWrapper.getObject(KEY_TX_AMOUNT));
		transactionModel.setTotalAmount(transactionModel.getTransactionAmount());
		transactionModel.setTotalCommissionAmount(0d);
	    transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstantsInterface.COMPLETED);
		transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		transactionModel.setTransactionTypeId(TransactionTypeConstantsInterface.BULK_DISBURSEMENT);
		transactionModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
		transactionModel.setProcessingBankId(switchWrapper.getBankId());
		transactionModel.setDiscountAmount(0d);		
		transactionModel.setNotificationMobileNo("N/A");
		transactionModel.setCreatedBy(2L);
		transactionModel.setUpdatedBy(2L);
		transactionModel.setCreatedOn(new Date());
		transactionModel.setUpdatedOn(new Date());
		
	    TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
	    transactionDetailModel.setSettled(true);
	    transactionDetailModel.setProductId(ProductConstantsInterface.BULK_PAYMENT);
	    transactionDetailModel.setTransactionIdTransactionModel(transactionModel);
	    transactionDetailModel.setActualBillableAmount(transactionModel.getTransactionAmount());
		transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);
		
		TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel(true);
		transactionDetailMasterModel.setTransactionCode(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
		transactionDetailMasterModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
		transactionDetailMasterModel.setProductId(ProductConstantsInterface.BULK_PAYMENT);
		transactionDetailMasterModel.setProductName(productModel.getName());
		transactionDetailMasterModel.setSupplierId(productModel.getSupplierId());
		transactionDetailMasterModel.setProductName(productModel.getName());
		transactionDetailMasterModel.setProductCode(productModel.getProductCode()); 
		transactionDetailMasterModel.setBillType(productModel.getBillType());
		transactionDetailMasterModel.setSupplierId(productModel.getSupplierId());
		transactionDetailMasterModel.setSupplierName(productModel.getSupplierIdSupplierModel().getName());		
		transactionDetailMasterModel.setDeviceType(DeviceTypeConstantsInterface.DEVICE_TYPES_MAP.get(DeviceTypeConstantsInterface.BULK_DISBURSEMENT));
		transactionDetailMasterModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		transactionDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(transactionModel.getSupProcessingStatusId()));
		transactionDetailMasterModel.setTransactionAmount(transactionModel.getTransactionAmount());
		transactionDetailMasterModel.setTotalAmount(transactionModel.getTotalAmount());
		transactionDetailMasterModel.setCreatedOn(new Date());
		transactionDetailMasterModel.setUpdatedOn(new Date());
		
		
		try {
			
			BaseWrapper wrapper = new BaseWrapperImpl();
			wrapper.setBasePersistableModel(transactionModel);
			
			this.transactionManager.saveTransactionModel(wrapper);
			
			transactionModel = (TransactionModel) wrapper.getBasePersistableModel();
			switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
			
			transactionDetailMasterModel.setTransactionId(transactionModel.getTransactionId());
			transactionDetailMasterModel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
			
			transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);
			
			if(logger.isInfoEnabled()) {
				
				logger.info("Save Sum Transaction OLA | Relations");
				
				String trxCode = switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode();
				
				for(BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementsModelList) {
					
					logger.info("***"+trxCode +"("+switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount()+") --> "+ bulkDisbursementsModel.getTransactionCode() +" = "+ bulkDisbursementsModel.getAmount());
				}			
			}
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param bulkDisbursementsModelList
	 * @param transactionChunk
	 */
	public void createThreads(List<BulkDisbursementsModel> bulkDisbursementsModelList, Integer transactionChunk) {
		
		List<List<BulkDisbursementsModel>> disbursementIDPSettlementThreadList = new ArrayList<List<BulkDisbursementsModel>>(0); 
		
		if (CollectionUtils.isNotEmpty(bulkDisbursementsModelList)) {
	
			Integer numberOfThread = bulkDisbursementsModelList.size() / transactionChunk;
			Integer pendingNumberOfThread = bulkDisbursementsModelList.size() % transactionChunk;
			
			int start = 0;
			int end = transactionChunk;
			
			if(bulkDisbursementsModelList.size() <= transactionChunk) {
				numberOfThread = 1;
				end = bulkDisbursementsModelList.size();
			}
			
			for(int i=1; i <= numberOfThread; i++) {
				
				List<BulkDisbursementsModel> subList = bulkDisbursementsModelList.subList(start, end);
				
				disbursementIDPSettlementThreadList.add(subList);
				
				if(i != numberOfThread) {
					start = end;
					end = end + transactionChunk;
				} else {
					start = end;
					end = bulkDisbursementsModelList.size();
				}
			}
			
			if(pendingNumberOfThread > 0 && bulkDisbursementsModelList.size() > transactionChunk) {
				
				List<BulkDisbursementsModel> subList = bulkDisbursementsModelList.subList(start, end);
				disbursementIDPSettlementThreadList.add(subList);
			
			} else {
				
				pendingNumberOfThread = 0;
			}
			
			
			logger.info("\n\n\n*****************************************************************************************************************");
			
			logger.info("\nNumber of Transactions "+ bulkDisbursementsModelList.size() +"\nNumber of Defined Transaction Chunks per Thread "+(transactionChunk+"\nThreads Generated : ")+ (numberOfThread + ((pendingNumberOfThread > 0) ? 1: 0)));

			logger.info("*****************************************************************************************************************\n\n\n");
			
			int threadNumber = 0;
			CountDownLatch startSignal = new CountDownLatch(1);
		    CountDownLatch doneSignal = new CountDownLatch(disbursementIDPSettlementThreadList.size());

			for(List<BulkDisbursementsModel> chunkList : disbursementIDPSettlementThreadList) {
				
				BulkDisbursementIDPSettlementThread bulkDisbursementIDPSettlementThread = new BulkDisbursementIDPSettlementThread(threadNumber, commandManager, bulkDisbursementsModelList, bulkDisbursementDao,paymentTypeMap, startSignal, doneSignal);
				Thread thread = new Thread(bulkDisbursementIDPSettlementThread);
				thread.start();			
			}
		    try
		    {
		    	startSignal.countDown();// let all threads proceed
				doneSignal.await();// wait for all to finish
				logger.info("Bulk Payment: All threads completed.");
			}
		    catch (InterruptedException e)
		    {
				logger.error(e.getMessage(),e);
			}           
		}		
	}

	
	private Integer[] getPaymentTypesArray() throws FrameworkCheckedException {
		Integer[] types = null;
		try{
			CustomList<PaymentTypeModel> paymentTypesList = paymentTypeDAO.findAll();
			if (paymentTypesList != null && paymentTypesList.getResultsetList() != null && paymentTypesList.getResultsetList().size() > 0) {
				types = new Integer[paymentTypesList.getResultsetList().size()];
				int count =0;
				for (PaymentTypeModel typeModel : paymentTypesList.getResultsetList()) {
					paymentTypeMap.put(typeModel.getPaymentTypeId().intValue(), typeModel.getName());
					types[count++] = typeModel.getPaymentTypeId().intValue();
				}
			}
		}catch (Exception e) {
			logger.error("[BulkPaymentScheduler.getPaymentTypesArray] Exception occured while loading payment typs. Ex Message:	" + e.getMessage());
			throw new FrameworkCheckedException("Error in loading Payment Types");
		}
		
		return types;
	}
	
	
	/**
	 * @param stakeholderBankInfoId
	 * @return
	 */
	private StakeholderBankInfoModel getStakeholderBankInfoModel(Long stakeholderBankInfoId) {
		
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setPrimaryKey(stakeholderBankInfoId);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		
		try {
		
			stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
		
		} catch (FrameworkCheckedException e) {
			logger.error(e);
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
	
	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public void setTransactionChunk(Integer transactionChunk) {
		this.transactionChunk = transactionChunk;
	}
	public void setPaymentTypeDAO(PaymentTypeDAO paymentTypeDAO) {
		this.paymentTypeDAO = paymentTypeDAO;
	}
	public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}
	public void setOlaVeriflyFinancialInstitution(AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}
	public void setTransactionManager(TransactionModuleManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}
	public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}

}