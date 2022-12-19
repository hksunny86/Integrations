package com.inov8.microbank.server.service.manualadjustmentmodule;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkReversalRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.server.dao.portal.manualadjustmentmodule.BulkReversalDAO;
import com.inov8.microbank.server.dao.safrepo.WalletSafRepoDAO;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ReasonConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierConstants;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.transactionreversal.ManualReversalVO;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.portal.bbaccountsview.BBAccountsViewDao;
import com.inov8.microbank.server.dao.portal.manualadjustmentmodule.ManualAdjustmentDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;

public class ManualReversalManagerImpl implements ManualReversalManager {

	private final Log logger = LogFactory.getLog(this.getClass());
	private ManualAdjustmentDAO manualAdjustmentDAO;
	private BBAccountsViewDao bBAccountsViewDao;
	private ActionLogManager actionLogManager;
	private SettlementManager settlementManager;
	private TransactionDetailMasterManager transactionDetailMasterManager;
	private SwitchController switchController;
	private TransactionModuleManager transactionModuleManager;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private WalletSafRepoDAO walletSafRepoDAO;
	private BulkReversalDAO bulkReversalDAO;
	private XmlMarshaller<BulkReversalRefDataModel> bulkAutoReversalModelXmlMarshaller;
	private JmsProducer jmsProducer;
	private TransactionCodeDAO transactionCodeDAO;

	public SearchBaseWrapper loadManualAdjustments(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<ManualAdjustmentModel>
		list = this.manualAdjustmentDAO.findByExample( (ManualAdjustmentModel)
				searchBaseWrapper.
				getBasePersistableModel(),
				searchBaseWrapper.
				getPagingHelperModel(),
				searchBaseWrapper.
				getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		if(list != null)
		{
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}
	
	public BBAccountsViewModel getBBAccountsViewModel(BBAccountsViewModel model) throws FrameworkCheckedException {
		CustomList<BBAccountsViewModel> modelList = new CustomList<BBAccountsViewModel>();
		modelList = this.bBAccountsViewDao.findByExample(model,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);		
		if(modelList.getResultsetList().size() > 0){
			model = modelList.getResultsetList().get(0);
		}
		return model;
	}
	
	public BBAccountsViewModel getBBAccountsViewModel(Long accountId) throws FrameworkCheckedException {
		
		BBAccountsViewModel bbAccountsViewModel = this.bBAccountsViewDao.findByPrimaryKey(accountId);

		return bbAccountsViewModel;
	}

	public void makeReversal(ManualReversalVO manualReversalVO) throws Exception {
		OLAVO olaVO = new OLAVO();
		List<OLAInfo> debitList = new ArrayList<OLAInfo>();
		List<OLAInfo> creditList = new ArrayList<OLAInfo>();
		
		WorkFlowWrapper workFlowWrapper = createOrLoadTransaction(manualReversalVO);
		
		logger.info("[Manual Reversal/Adjustment] processing trxID:" + workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode());

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		logger.info("Welcome");
		Long reasonId = ReasonConstants.MANUAL_ADJUSTMENT;
		Long category = TransactionConstantsInterface.MANUAL_ADJUSTMENT_CATEGORY_ID;
		if(manualReversalVO.getAdjustmentType() != null && manualReversalVO.getAdjustmentType().longValue() == 1){
			if(manualReversalVO.getProductId() != null && (manualReversalVO.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)
					|| manualReversalVO.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US)
					|| manualReversalVO.getProductId().equals(ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL)
					||manualReversalVO.getProductId().equals(ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL)
					||manualReversalVO.getProductId().equals(ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)
					|| manualReversalVO.getProductId().equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION)
					|| manualReversalVO.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)
					|| manualReversalVO.getProductId().equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION)
					|| manualReversalVO.getProductId().equals(ProductConstantsInterface.ACT_TO_ACT_CI)))
			{
				reasonId = ReasonConstants.REVERSAL;
				category = TransactionConstantsInterface.AUTO_REVERSAL_CATEGORY_ID;
			}
			else
			{
				reasonId = ReasonConstants.REVERSAL;
				category = TransactionConstantsInterface.REVERSAL_CATEGORY_ID;
			}
		}
		
		//preparing debit/credit list - start
		for(BbStatementAllViewModel debitCreditEntry: manualReversalVO.getFundTransferEntryList()){
			OLAInfo olaInfo = new OLAInfo();
			olaInfo.setReasonId(reasonId);
			olaInfo.setMicrobankTransactionCode(manualReversalVO.getTransactionCode());
			
			String accNoEncrypted = EncryptionUtil.encryptWithDES(debitCreditEntry.getAccountNumber());
			BBAccountsViewModel model = new BBAccountsViewModel();
			model.setAccountNumber(accNoEncrypted);
			BBAccountsViewModel bbAccountsViewModel = getBBAccountsViewModel(model);
			
			this.checkClosedAccount(bbAccountsViewModel);
			
			if(bbAccountsViewModel.getIsCustomerAccountType()){
				olaInfo.setIsAgent(false);
			}else{
				olaInfo.setIsAgent(true);
			}
			olaInfo.setCustomerAccountTypeId(bbAccountsViewModel.getAccountTypeId());
			olaInfo.setPayingAccNo(debitCreditEntry.getAccountNumber());
			olaInfo.setBalance(debitCreditEntry.getAmount());
			
			if(TransactionTypeConstants.DEBIT.longValue() == debitCreditEntry.getTransactionType()){
				olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
				debitList.add(olaInfo);
			}else{
				olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
				creditList.add(olaInfo);
			}
		}
		//preparing debit/credits - end
		
		olaVO.setDebitAccountList(debitList);
		olaVO.setCreditAccountList(creditList);
		olaVO.setCategory(category);

		switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
		switchWrapper.setOlavo(olaVO);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
		
		if(ProductConstantsInterface.IBFT.longValue() == workFlowWrapper.getProductModel().getProductId()){
			switchWrapper.setSkipPostedTrxEntry(true);
		}
		
		ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());
		
		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
		
		this.saveSettlementTransactionList(switchWrapper);
		
		if(manualReversalVO.getAdjustmentType().intValue() == 1){ // update transaction status to Reverse Completed
			logger.info("[Manual Reversal] Going to update Transaction status to Reverse Completed for trxID:" + workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode());
			workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSE_COMPLETED);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(workFlowWrapper.getTransactionModel());
			transactionModuleManager.updateTransaction(baseWrapper);
			
			workFlowWrapper.getTransactionDetailMasterModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSE_COMPLETED);
			workFlowWrapper.getTransactionDetailMasterModel().setProcessingStatusName(SupplierProcessingStatusConstants.REVERSE_COMPLETED_NAME);
			
			workFlowWrapper.getTransactionDetailMasterModel().setReversedByAppUserId(manualReversalVO.getInitiatorAppUserId());
			workFlowWrapper.getTransactionDetailMasterModel().setReversedByName(manualReversalVO.getInitiatorName());
			workFlowWrapper.getTransactionDetailMasterModel().setReversedDate(new Date());
		}

		if(workFlowWrapper.getTransactionModel().getNotificationMobileNo() == null) {
			workFlowWrapper.getTransactionModel().setNotificationMobileNo(workFlowWrapper.getTransactionDetailMasterModel().getSaleMobileNo());
		}
		workFlowWrapper.getTransactionDetailMasterModel().setUpdatedOn(new Date());
		workFlowWrapper.getTransactionDetailMasterModel().setReversedComments(manualReversalVO.getComments());
		transactionDetailMasterManager.saveTransactionDetailMaster(workFlowWrapper.getTransactionDetailMasterModel());

		WalletSafRepoModel walletSafRepoModel = new WalletSafRepoModel();

		WalletSafRepoModel wsrm = walletSafRepoDAO.loadWalletSafRepo(manualReversalVO.getTransactionCode());

		if(wsrm != null) {
			wsrm.setIsComplete(1L);
			wsrm.setTransactionStatus(SupplierProcessingStatusConstants.REVERSE_COMPLETED_NAME);
			walletSafRepoDAO.updateWalletSafRepo(wsrm);
		}
	}

	private void prepareAndSaveSettlementTransaction(Long transactionId,Long productId,Double amount,Long fromAccountInfoId, Long toAccountInfoId, Boolean isAgent, boolean debitPoolAcc) throws Exception{
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		SettlementTransactionModel settlementModel = new SettlementTransactionModel();
		settlementModel.setTransactionID(transactionId);
		settlementModel.setProductID(productId);
		settlementModel.setCreatedBy(appUserModel.getAppUserId());
		settlementModel.setUpdatedBy(appUserModel.getAppUserId());
		settlementModel.setCreatedOn(new Date());
		settlementModel.setUpdatedOn(new Date());
		settlementModel.setStatus(0L);	  
		settlementModel.setFromBankInfoID(fromAccountInfoId);
		settlementModel.setToBankInfoID(toAccountInfoId);
		settlementModel.setAmount(amount);
		
		this.settlementManager.saveSettlementTransactionModel(settlementModel);
		
		if(isAgent != null){
			Long poolBankInfoId = null;
			
			settlementModel = new SettlementTransactionModel();
			if(isAgent){
				poolBankInfoId = PoolAccountConstantsInterface.AGENT_POOL_ACCOUNT_ID;
			}else{
				poolBankInfoId = PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID;
			}
			
			settlementModel.setTransactionID(transactionId);
			settlementModel.setProductID(productId);
			settlementModel.setCreatedBy(appUserModel.getAppUserId());
			settlementModel.setUpdatedBy(appUserModel.getAppUserId());
			settlementModel.setCreatedOn(new Date());
			settlementModel.setUpdatedOn(new Date());
			settlementModel.setStatus(0L);	  
			settlementModel.setFromBankInfoID((debitPoolAcc)?poolBankInfoId:fromAccountInfoId);
			settlementModel.setToBankInfoID((!debitPoolAcc)?poolBankInfoId:toAccountInfoId);
			settlementModel.setAmount(amount);
			this.settlementManager.saveSettlementTransactionModel(settlementModel);
		}
	}
	
	public TransactionDetailMasterModel getTransactionDetailMasterModel(String trxCode) throws FrameworkCheckedException{
		if(StringUtil.isNullOrEmpty(trxCode)){
			return null;
		}
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		TransactionDetailMasterModel txDetailMaster = new TransactionDetailMasterModel();
		txDetailMaster.setTransactionCode(trxCode);
		baseWrapper.setBasePersistableModel(txDetailMaster);
		baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
		txDetailMaster = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
		return txDetailMaster;
	}

//	@Override
//	public TransactionDetailMasterModel getTransactionDetailMasterModelByTrxIdAndStatusId(String trxCode, Long statusId) throws FrameworkCheckedException {
//		if(StringUtil.isNullOrEmpty(trxCode)){
//			return null;
//		}
//
//		TransactionDetailMasterModel txDetailMaster = transactionDetailMasterManager.loadTransactionDetailMasterModelByTrxIdandStatusId(trxCode, statusId);
//		return txDetailMaster;
//	}

	private WorkFlowWrapper createOrLoadTransaction(ManualReversalVO manualReversalVO) throws FrameworkCheckedException {
		String transactionCode = manualReversalVO.getTransactionCode();
		WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
		
		if(StringUtils.isEmpty(transactionCode)) {
			try{
				//wrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_SENDER, maModel.getFromACNo());
				//wrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_RECIPIENT, maModel.getToACNo());
				//wrapper.setTransactionAmount(maModel.getAmount());
				
				ProductModel productModel = new ProductModel(ProductConstantsInterface.MANUAL_ADJUSTMENT, ProductConstantsInterface.MANUAL_ADJUSTMENT_NAME);
				productModel.setSupplierIdSupplierModel(new SupplierModel(SupplierConstants.BRANCHLESS_BANKING_SUPPLIER, SupplierConstants.BRANCHLESS_BANKING_SUPPLIER_NAME));
				wrapper.setProductModel(productModel);
				wrapper.setDeviceTypeModel(new DeviceTypeModel(DeviceTypeConstantsInterface.WEB));
				wrapper.setTransactionTypeModel(new TransactionTypeModel(TransactionTypeConstantsInterface.MANUAL_ADJUSTMENT_TX));
				wrapper.setPaymentModeModel(new PaymentModeModel(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT));
				wrapper.setAppUserModel(UserUtils.getCurrentUser());
				wrapper.setUserDeviceAccountModel(new UserDeviceAccountsModel());
				wrapper.setTransactionAmount(manualReversalVO.getTotalAmount());
				wrapper.setTotalAmount(manualReversalVO.getTotalAmount());
				transactionModuleManager.generateTransactionCodeRequiresNewTransaction(wrapper);
				transactionModuleManager.createTransactionModel(wrapper);
				manualReversalVO.setTransactionCode(wrapper.getTransactionCodeModel().getCode());
			
			}catch(Exception ex){
				logger.error("Unable to create Transaction, Reason:"+ex.getMessage(),ex);
				throw new FrameworkCheckedException("Unable to create Transaction");
			}
		}else{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			TransactionDetailMasterModel txDetailMaster = new TransactionDetailMasterModel();
			txDetailMaster.setTransactionCode(transactionCode);
			baseWrapper.setBasePersistableModel(txDetailMaster);
			baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
			txDetailMaster = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
			wrapper.setTransactionDetailMasterModel(txDetailMaster);
			wrapper.setProductModel(new ProductModel(txDetailMaster.getProductId(), txDetailMaster.getProductName()));
			TransactionCodeModel transactionCodeModel = new TransactionCodeModel(transactionCode);
			transactionCodeModel.setTransactionCodeId(txDetailMaster.getTransactionCodeId());
			wrapper.setTransactionCodeModel(transactionCodeModel);
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	        TransactionModel trxModel = new TransactionModel();
	        trxModel.setTransactionId(txDetailMaster.getTransactionId());
	        searchBaseWrapper.setBasePersistableModel(trxModel);
	        searchBaseWrapper = this.transactionModuleManager.loadTransaction(searchBaseWrapper);    
	        
	        if(searchBaseWrapper.getBasePersistableModel() != null){
	        	trxModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
				wrapper.setTransactionModel(trxModel);
	        }else{
				logger.error("Unable to load Transaction against transaction.transaction_id:"+txDetailMaster.getTransactionId());
				throw new FrameworkCheckedException("Unable to load Transaction Details");
	        }
		}
		return wrapper;
	}
	
	private void saveSettlementTransactionList(SwitchWrapper switchWrapper) throws Exception{
		Long fromAccountInfoId = null;
		Long toAccountInfoId = null;
		
		TransactionDetailMasterModel txDetailMaster = switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel();
		Long transactionId = txDetailMaster.getTransactionId();
		Long productId = txDetailMaster.getProductId();
		StakeholderBankInfoModel model = new StakeholderBankInfoModel();
		
		for(OLAInfo debitOlaInfo: switchWrapper.getOlavo().getDebitAccountList()){
			if(debitOlaInfo.getCustomerAccountTypeId().longValue() == 3){
				model = new StakeholderBankInfoModel();
				model.setAccountNo(debitOlaInfo.getPayingAccNo());
				model = stakeholderBankInfoManager.loadStakeholderBankInfoModel(model);
				fromAccountInfoId = null;
				if(null != model){
					fromAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
				}
				if(null != fromAccountInfoId){
					prepareAndSaveSettlementTransaction(transactionId, productId, debitOlaInfo.getBalance(),fromAccountInfoId,PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, null, false);		
				}else{
					throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + debitOlaInfo.getPayingAccNo());
				}
			}else{	
				fromAccountInfoId = settlementManager.getStakeholderBankInfoId(debitOlaInfo.getCustomerAccountTypeId());
				prepareAndSaveSettlementTransaction(transactionId, productId, debitOlaInfo.getBalance(),fromAccountInfoId,PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,debitOlaInfo.getIsAgent(), true);		
			}
		}
		
		for(OLAInfo creditOlaInfo: switchWrapper.getOlavo().getCreditAccountList()){
			if(creditOlaInfo.getCustomerAccountTypeId().longValue() == 3){
				model = new StakeholderBankInfoModel();
				model.setAccountNo(creditOlaInfo.getPayingAccNo());
				model = stakeholderBankInfoManager.loadStakeholderBankInfoModel(model);
				toAccountInfoId = null;
				if(null != model){
					toAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
				}
				if(null != toAccountInfoId){
					prepareAndSaveSettlementTransaction(transactionId, productId, creditOlaInfo.getBalance(), PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, toAccountInfoId, null, false);		
				}else{
					throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + creditOlaInfo.getPayingAccNo());
				}
			}else{	
				toAccountInfoId = settlementManager.getStakeholderBankInfoId(creditOlaInfo.getCustomerAccountTypeId());
				prepareAndSaveSettlementTransaction(transactionId,productId, creditOlaInfo.getBalance(), PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,toAccountInfoId,creditOlaInfo.getIsAgent(), false);		
			}
		}
		
	}
	
	private void checkClosedAccount(BBAccountsViewModel model) throws Exception{
		if(model != null ){
			if(model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT){
				logger.info("checkClosedAccount - Passed - Settlement Acc Type");
			}else{
				String accNumber = EncryptionUtil.decryptWithDES(model.getAccountNumber());
				if( model.getIsActive()== null || !model.getIsActive() ){
					throw new FrameworkCheckedException("Account is not active against account number " + accNumber);
				}else if(model.getStatusId() == null || model.getStatusId().longValue() != 1){
					throw new FrameworkCheckedException("Account Status is not active against account number " + accNumber);
				}else if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")){
					throw new FrameworkCheckedException("Account is CLOSED against account number " + accNumber);
				}
				logger.info("checkClosedAccount - Passed");
			}
		}
	}
	
	public void setManualAdjustmentDAO(ManualAdjustmentDAO manualAdjustmentDAO) {
		this.manualAdjustmentDAO = manualAdjustmentDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setBBAccountsViewDao(BBAccountsViewDao bBAccountsViewDao) {
		this.bBAccountsViewDao = bBAccountsViewDao;
	}

	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}

	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}

	public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}
	
	public void setTransactionModuleManager(TransactionModuleManager transactionModuleManager) {
		this.transactionModuleManager = transactionModuleManager;
	}
	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}

	public void setWalletSafRepoDAO(WalletSafRepoDAO walletSafRepoDAO) {
		this.walletSafRepoDAO = walletSafRepoDAO;
	}

	@Override
	public void createBulkReversal(List<BulkAutoReversalModel> dis) throws IOException, FrameworkCheckedException {
		bulkReversalDAO.saveOrUpdateCollection(dis);

		BulkAutoReversalModel bulkAutoReversalModel = new BulkAutoReversalModel();
		if(dis.size() > 0 && dis !=null){
			bulkAutoReversalModel = dis.get(0);
		}

		if(bulkAutoReversalModel.getApproved() == true){
			for (BulkAutoReversalModel bulkModel : dis) {
				BulkReversalRefDataModel bulkReversalRefDataModel = new BulkReversalRefDataModel();
//				bulkManualAdjustmentRefDataModel.setBulkAdjustmentId(bulkModel.getBulkAdjustmentId());
				bulkReversalRefDataModel.setTrxnId(bulkModel.getTrxnId());
//				bulkManualAdjustmentRefDataModel.setAdjustmentType(bulkModel.getAdjustmentType());
//				bulkManualAdjustmentRefDataModel.setFromAccount(bulkModel.getFromAccount());
//				bulkManualAdjustmentRefDataModel.setFromAccountTitle(bulkModel.getFromAccountTitle());
//				bulkManualAdjustmentRefDataModel.setToAccount(bulkModel.getToAccount());
//				bulkManualAdjustmentRefDataModel.setToAccountTitle(bulkModel.getToAccountTitle());
//				bulkManualAdjustmentRefDataModel.setAmount(bulkModel.getAmount());
//				bulkManualAdjustmentRefDataModel.setComments(bulkModel.getDescription());
				bulkReversalRefDataModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				bulkReversalRefDataModel.setCreatedOn(new Date());
				bulkReversalRefDataModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());
				//manualAdjustmentManager.pushBulkManualAdjustmentToQueue(bulkManualAdjustmentRefDataModel);

				String xml = bulkAutoReversalModelXmlMarshaller.marshal(bulkReversalRefDataModel);
				jmsProducer.produce(xml , DestinationConstants.BULK_REVERSAL_DESTINATION);
			}
		}
	}

	@Override
	public void validateTransactionCode(String trxCode) throws FrameworkCheckedException {
		if(StringUtil.isNullOrEmpty(trxCode)){
			return;
		}

		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		exampleHolder.setMatchMode(MatchMode.EXACT);
		exampleHolder.setIgnoreCase(false);

		TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
		transactionCodeModel.setCode(trxCode);

		CustomList<TransactionCodeModel> txCodeList = transactionCodeDAO.findByExample(transactionCodeModel,null,null,exampleHolder);
		if(txCodeList == null || txCodeList.getResultsetList().size() == 0){
			throw new FrameworkCheckedException("INVALID_TRX_ID");
		}
	}

	@Override
	public String accountNumberHealthCheck(BBAccountsViewModel model, String fromTo) {
		String errMsg = "";
		if(model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT){
			logger.info("[bulk]Settlement Acc Type loaded against accNumber:"+model.getAccountNumber()+" ... so SKIPPING account status/active check");
		}else{
			// Agent / Customer scenario
			if( model.getIsActive()== null || !model.getIsActive() ){
				errMsg = fromTo + "Account is not active against provided account number";
			}else if(model.getStatusId() == null || model.getStatusId().longValue() != 1){
				errMsg = fromTo + "Account Status is not active against provided account number";
			}else if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")){
				errMsg = fromTo + "Account is CLOSED against provided account number";
			}
		}
		return errMsg;
	}

	public void setBulkReversalDAO(BulkReversalDAO bulkReversalDAO) {
		this.bulkReversalDAO = bulkReversalDAO;
	}



	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}

	public void setBulkAutoReversalModelXmlMarshaller(XmlMarshaller<BulkReversalRefDataModel> bulkAutoReversalModelXmlMarshaller) {
		this.bulkAutoReversalModelXmlMarshaller = bulkAutoReversalModelXmlMarshaller;
	}

	@Override
	public List<BulkAutoReversalModel> loadBulkReversalModelList(SearchBaseWrapper searchBaseWrapper) throws Exception {
		BulkAutoReversalModel bulkAutoReversalModel = (BulkAutoReversalModel) searchBaseWrapper.getBasePersistableModel();
		CustomList<BulkAutoReversalModel> customList = bulkReversalDAO.findByExample(bulkAutoReversalModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		List<BulkAutoReversalModel> resultList = new ArrayList<>();
		if (customList != null && CollectionUtils.isNotEmpty(customList.getResultsetList())) {
			resultList = customList.getResultsetList();
		}

		return resultList;
	}

	@Override
	public void updateIsApprovedForBatch(Long batchId, String[] bulkAdjustmentId) throws Exception {

		BulkAutoReversalModel model = new BulkAutoReversalModel();
		List<BulkAutoReversalModel> updatedBulkReversalList = new ArrayList<>();
		model.setBatchId(batchId);
		CustomList<BulkAutoReversalModel> bulkAutoReversalModelList = (CustomList<BulkAutoReversalModel>) bulkReversalDAO.findByExample(model, null);

//		if(bulkAdjustmentIds != null && bulkAdjustmentIds.length > 0){
//			HashSet<String> hs = new HashSet<String>(Arrays.asList(bulkAdjustmentIds));
			for(BulkAutoReversalModel bulkAutoReversalModel : bulkAutoReversalModelList.getResultsetList()){
//				if(! hs.contains(bulkManualAdjustmentModel.getBulkAdjustmentId().toString())){
//					bulkManualAdjustmentModel.setIsSkipped(true);
//				}
				bulkAutoReversalModel.setApproved(true);
				bulkAutoReversalModel.setUpdatedOn(new Date());
				bulkAutoReversalModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

				updatedBulkReversalList.add(bulkAutoReversalModel);

			}
		bulkReversalDAO.saveOrUpdateCollection(updatedBulkReversalList);
//		}
		//bulkManualAdjustmentDAO.updateIsApprovedForBatch(batchId , bulkAdjustmentId);
	}

	@Override
	public void pushBulkReversalToQueue(BulkReversalRefDataModel bulkReversalRefDataModel) throws Exception {

		String xml = bulkAutoReversalModelXmlMarshaller.marshal(bulkReversalRefDataModel);
		jmsProducer.produce(xml , DestinationConstants.BULK_REVERSAL_DESTINATION);

	}

	@Override
	public SearchBaseWrapper loadBulkAutoReversals(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<BulkAutoReversalModel>
				list = this.bulkReversalDAO.findByExample( (BulkAutoReversalModel)
						searchBaseWrapper.
								getBasePersistableModel(),
				searchBaseWrapper.
						getPagingHelperModel(),
				searchBaseWrapper.
						getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		if(list != null){
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}
}
