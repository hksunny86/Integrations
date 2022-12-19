package com.inov8.microbank.server.service.orphantransaction;

import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.OLATransactionReasonsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ReasonConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionConstantsInterface;
import com.inov8.microbank.common.util.TransactionProductEnum;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.dailyjob.OrphanTransactionDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.thoughtworks.xstream.XStream;

public class OrphanTransactionManagerImpl implements OrphanTransactionManager {
	
	private static Logger logger = Logger.getLogger(OrphanTransactionManagerImpl.class.getSimpleName());

	private SwitchController switchController;
	private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
	private AppUserManager appUserManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private OrphanTransactionDAO orphanTransactionDAO;
	private ActionLogManager actionLogManager;
	private MessageSource messageSource;
	private SmsSender smsSender;
	private SettlementManager settlementManager;
    private TransactionDetailMasterManager transactionDetailMasterManager;
    private CommissionManager commissionManager;

	
	public boolean reverseOrphanTransaction(TransactionModel transaction,StakeholderBankInfoModel olaSundaryAccount, Double txAmount) throws FrameworkCheckedException, WorkFlowException{
		
		String receiverCNIC = null;
		Double customerBalance = 0D;
		TransactionDetailModel transactionDetailModel = null;
		List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transaction.getTransactionIdTransactionDetailModelList();
		if (txdetails != null) {
			transactionDetailModel = txdetails.get(0);
			receiverCNIC = transactionDetailModel.getCustomField9();
		}

		ActionLogModel actionLogModel = new ActionLogModel();
		XStream xstream = new XStream();
		xstream.toXML(new String(" "));
		actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
		actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String(" ")), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogBeforeStart(actionLogModel);

		logger.info("[OrphanTransactionManagerImpl] Loading customer data. Mobile: " + transaction.getCustomerMobileNo() + " Transction ID:" + transaction.getTransactionCodeIdTransactionCodeModel().getCode());
		/*
		AppUserModel customerAppUserModel = new AppUserModel();
		customerAppUserModel.setMobileNo(transaction.getCustomerMobileNo());
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		sBaseWrapper.setBasePersistableModel(customerAppUserModel);
		sBaseWrapper = appUserManager.searchAppUserByMobile(sBaseWrapper);
		if (null != sBaseWrapper.getCustomList() && null != sBaseWrapper.getCustomList().getResultsetList()
				&& sBaseWrapper.getCustomList().getResultsetList().size() > 0) {
			customerAppUserModel = (AppUserModel) sBaseWrapper.getCustomList().getResultsetList().get(0);
		}
		*/
		
		AppUserModel customerAppUserModel = appUserManager.loadAppUserByQuery(transaction.getCustomerMobileNo(), UserTypeConstantsInterface.CUSTOMER);

		if(customerAppUserModel == null) {
			throw new FrameworkCheckedException(transaction.getCustomerMobileNo() +" Not Registered agaist any Customer");
		}		
		
		ThreadLocalAppUser.setAppUserModel(customerAppUserModel);

		SmartMoneyAccountModel customerSMA = new SmartMoneyAccountModel();
		customerSMA.setCustomerId(customerAppUserModel.getCustomerId());

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(customerSMA);
		baseWrapper = smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
		if (null != baseWrapper.getBasePersistableModel()) {
			customerSMA = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
		}

		AccountInfoModel customerAccountInfo;
		try {
			customerAccountInfo = olaVeriflyFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(customerSMA, customerAppUserModel.getCustomerId(), transaction.getTransactionCodeId());
		} catch (Exception e1) {
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to load customer Acc Info. TransactionCodeId:"+transaction.getTransactionCodeId());
		}

		
		// Update Transaction Model STATUS to Reversed
		boolean status = this.orphanTransactionDAO.reverseTransaction(transaction.getTransactionId(), SupplierProcessingStatusConstants.UNCLAIMED, SCHEDULER_APP_USER_ID, new Date() );
		if (!status){
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to update transaction status - TransactionCodeId: " + transaction.getTransactionCodeId());
		}
		
		SwitchWrapper switchWrapperOLA = olaFundsTransfer(txAmount, olaSundaryAccount.getAccountNo(), customerAccountInfo.getAccountNo(), customerAppUserModel.getCustomerIdCustomerModel().getCustomerAccountTypeId(), transaction);
		
		if (switchWrapperOLA.getOlavo().getResponseCode().equals("00")){
			customerBalance = switchWrapperOLA.getOlavo().getToBalanceAfterTransaction();
		}else{
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to transfer funds from Acc2Cash Sundry to customer account - TransactionCodeId: " + transaction.getTransactionCodeId());
		}
		
		Long customerAccountTypeBankInfoId = settlementManager.getStakeholderBankInfoId(customerAppUserModel.getCustomerIdCustomerModel().getCustomerAccountTypeId());

		makeSettlementTransactionEntry(txAmount, PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID, customerAccountTypeBankInfoId, ProductConstantsInterface.ACCOUNT_TO_CASH, transaction.getTransactionId());
		makeSettlementTransactionEntry(txAmount, PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID, ProductConstantsInterface.ACCOUNT_TO_CASH, transaction.getTransactionId());
		
		if (StringUtils.isNotEmpty(transaction.getCustomerMobileNo())) {
			String date = PortalDateUtils.formatDate(transaction.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
			String time = PortalDateUtils.formatDate(transaction.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
			String amount = Formatter.formatDouble(txAmount);
			String notification = messageSource.getMessage("ACC2CASH.NOTIFICATION", new Object[] { receiverCNIC, date, time, amount, customerBalance },null);
			SmsMessage message = new SmsMessage(transaction.getCustomerMobileNo(), notification);
			try {
				smsSender.send(message);
			} catch (Exception e) {
				logger.error("OrphanTransactionManagerImpl - error while sending sms to customer:", e);
			}
		}

		actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String("dumy")), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogAfterEnd(actionLogModel);

		return true;
	}
	
	public boolean reverseC2COrphanTransaction(TransactionDetailMasterModel transactionDetailMasterModel, TransactionModel transaction,StakeholderBankInfoModel fundTransferOLASundaryAccount, StakeholderBankInfoModel olaUnclaimedSundaryAccount) throws FrameworkCheckedException, WorkFlowException{
		
		String receiverCNIC = null;
		String receiverMobileNo = null;
		Double txAmount = 0d;
		TransactionDetailModel transactionDetailModel = null;
		List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transaction.getTransactionIdTransactionDetailModelList();
		if (txdetails != null) {
			transactionDetailModel = txdetails.get(0);
			receiverCNIC = transactionDetailModel.getCustomField9();
			receiverMobileNo = transactionDetailModel.getCustomField6();
		}

		if(transactionDetailMasterModel != null) {
			txAmount = transactionDetailMasterModel.getSundryAmount();
		}

		ActionLogModel actionLogModel = new ActionLogModel();
		XStream xstream = new XStream();
		xstream.toXML(new String(" "));
		actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
		actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String(" ")), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogBeforeStart(actionLogModel);

		// Update Transaction Model STATUS to Reversed
		boolean status = this.orphanTransactionDAO.reverseTransaction(transaction.getTransactionId(), SupplierProcessingStatusConstants.UNCLAIMED, SCHEDULER_APP_USER_ID, new Date() );
		if (!status){
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to update transaction status - TransactionCodeId: " + transaction.getTransactionCodeId());
		}
		
		SwitchWrapper switchWrapperOLA = olaFundsTransfer(txAmount, fundTransferOLASundaryAccount.getAccountNo(), olaUnclaimedSundaryAccount.getAccountNo(), CustomerAccountTypeConstants.SETTLEMENT, transaction);
		
		if ( ! switchWrapperOLA.getOlavo().getResponseCode().equals("00")){
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to transfer funds from Acc2Cash Sundry to customer account - TransactionCodeId: " + transaction.getTransactionCodeId());
		}

		makeSettlementTransactionEntry(txAmount, PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID, PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_ACCOUNT, ProductConstantsInterface.CASH_TRANSFER, transaction.getTransactionId());
		
		if (StringUtils.isNotEmpty(receiverMobileNo)) {
			String date = PortalDateUtils.formatDate(transaction.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
			String time=PortalDateUtils.formatDate(transaction.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
			String amount =  Formatter.formatDouble(txAmount);
			String notification = messageSource.getMessage("CASH2CASHREVERSAL.NOTIFICATION", new Object[] {
					receiverCNIC,
					date,
					time,
					amount
					},
					null);
			logger.info("NOTIFICATION: "+notification);
			SmsMessage message = new SmsMessage(receiverMobileNo, notification);
			try {
				smsSender.send(message);
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}
		}
		
		actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String("dumy")), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogAfterEnd(actionLogModel);

		return true;
	}

	public boolean reverseBulkPaymentOrphanTransaction(TransactionModel transaction,StakeholderBankInfoModel fundTransferOLASundaryAccount, StakeholderBankInfoModel olaUnclaimedSundaryAccount) throws FrameworkCheckedException, WorkFlowException{
		
		String receiverCNIC = null;
		String receiverMobileNo = null;
		Double txAmount = 0d;
		TransactionDetailModel transactionDetailModel = null;
		List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transaction.getTransactionIdTransactionDetailModelList();
		if (txdetails != null) {
			transactionDetailModel = txdetails.get(0);
			txAmount = transactionDetailModel.getActualBillableAmount();
			receiverCNIC = transactionDetailModel.getCustomField9();
			receiverMobileNo = transactionDetailModel.getCustomField6();
		}

		ActionLogModel actionLogModel = new ActionLogModel();
		XStream xstream = new XStream();
		xstream.toXML(new String(" "));
		actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
		actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String(" ")), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogBeforeStart(actionLogModel);

		// Update Transaction Model STATUS to Reversed
		boolean status = this.orphanTransactionDAO.reverseTransaction(transaction.getTransactionId(), SupplierProcessingStatusConstants.UNCLAIMED, SCHEDULER_APP_USER_ID, new Date() );
		if (!status){
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to update transaction status - TransactionCodeId: " + transaction.getTransactionCodeId());
		}
		
		SwitchWrapper switchWrapperOLA = olaFundsTransfer(txAmount, fundTransferOLASundaryAccount.getAccountNo(), olaUnclaimedSundaryAccount.getAccountNo(), CustomerAccountTypeConstants.SETTLEMENT, transaction);
		
		if ( ! switchWrapperOLA.getOlavo().getResponseCode().equals("00")){
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to transfer funds from Acc2Cash Sundry to customer account - TransactionCodeId: " + transaction.getTransactionCodeId());
		}

		makeSettlementTransactionEntry(txAmount, PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID, PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_ACCOUNT, ProductConstantsInterface.BULK_PAYMENT,transaction.getTransactionId());
		
		//TODO: check this message if needs removeal
		if (StringUtils.isNotEmpty(receiverMobileNo)) {
			String date = PortalDateUtils.formatDate(transaction.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
			String time=PortalDateUtils.formatDate(transaction.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
			String amount =  Formatter.formatDouble(txAmount);
			String notification = messageSource.getMessage("CASH2CASHREVERSAL.NOTIFICATION", new Object[] {
					receiverCNIC,
					date,
					time,
					amount
					},
					null);
			logger.info("NOTIFICATION: "+notification);
			SmsMessage message = new SmsMessage(receiverMobileNo, notification);
			try {
				smsSender.send(message);
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}
		}
		
		actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String("dumy")), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogAfterEnd(actionLogModel);

		return true;
	}
	public SwitchWrapper olaFundsTransfer(Double amount, String senderAccNo, String recipientAccNo, Long customerAccountTypeId, TransactionModel transactionModel) throws WorkFlowException, FrameworkCheckedException {
		
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		ThreadLocalAppUser.setAppUserModel(appUserModel);
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		TransactionCodeModel transactionCodeModel = transactionModel.getTransactionCodeIdTransactionCodeModel();
		transactionCodeModel.setTransactionCodeId(transactionModel.getTransactionCodeId());
		switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
		switchWrapper.getWorkFlowWrapper().setTransactionCodeModel(transactionCodeModel);
		switchWrapper.setOlavo(new OLAVO());
		switchWrapper.getOlavo().setCategory(TransactionConstantsInterface.UNCLAIMED_CATEGORY_ID); // 3 -> Unclaimed
		addOLAInfo(switchWrapper, senderAccNo, amount, CustomerAccountTypeConstants.SETTLEMENT,  false);
		addOLAInfo(switchWrapper, recipientAccNo, amount, customerAccountTypeId, true);
			
		switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			
		switchWrapper = switchController.debitCreditAccount(switchWrapper);

		return switchWrapper;
	}
	
	private void addOLAInfo(SwitchWrapper switchWrapper, String accountNo, Double amount, Long customerAccountTypeId, boolean isCredit) {
		OLAVO olaVO = switchWrapper.getOlavo();
		
		
		OLAInfo olaInfo = new OLAInfo();				
		olaInfo.setBalance(amount);
		olaInfo.setReasonId(ReasonConstants.REVERSAL);
		olaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);
		olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
		olaInfo.setCustomerAccountTypeId(customerAccountTypeId);
		olaInfo.setPayingAccNo(accountNo);
		
		if (isCredit) {
			if (null == olaVO.getCreditAccountList()) {
				List<OLAInfo> creditList = new ArrayList<OLAInfo>();
				olaVO.setCreditAccountList(creditList);		
			}
			
			olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
			olaInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
			olaVO.getCreditAccountList().add(olaInfo);	
			
		}else{
			
			if (null == olaVO.getDebitAccountList()) {
				List<OLAInfo> debitList = new ArrayList<OLAInfo>();
				olaVO.setDebitAccountList(debitList);
			}
			
			olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
			olaVO.getDebitAccountList().add(olaInfo);	
		}
	}
	
	private void makeSettlementTransactionEntry(Double txAmount, Long fromBankInfoId, Long toBankInfoId, Long productId, Long transactionId){
		logger.info("Saving settlemnt Transaction Entry. From Bank Info ID: " + fromBankInfoId + ". To Bank Info ID: " + toBankInfoId + " Product ID:" + productId);
		// using FT Settlement Scheduler to perform Reversal FT
		SettlementTransactionModel settlementModel = new SettlementTransactionModel();
		settlementModel.setTransactionID(transactionId);
		settlementModel.setProductID(productId);
		settlementModel.setCreatedBy(SCHEDULER_APP_USER_ID);
		settlementModel.setUpdatedBy(SCHEDULER_APP_USER_ID);
		settlementModel.setCreatedOn(new Date());
		settlementModel.setUpdatedOn(new Date());
		settlementModel.setStatus(0L);
		
		settlementModel.setFromBankInfoID(fromBankInfoId);
		settlementModel.setToBankInfoID(toBankInfoId);
		settlementModel.setAmount(txAmount);
		try {
			this.settlementManager.saveSettlementTransactionModel(settlementModel);
		} catch (Exception e) {
			logger.error("[OrphanTransactionReversalScheduler.execute] Unable to Save Settlement Transaction Model. Total Amount which is missed is: "+txAmount, e);
		}
	}
	private void actionLogBeforeStart(ActionLogModel actionLogModel)  throws FrameworkCheckedException{

		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
		if (actionLogModel.getActionLogId() != null) {
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}

	private void actionLogAfterEnd(ActionLogModel actionLogModel) throws FrameworkCheckedException {
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
		actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
		insertActionLogRequiresNewTransaction(actionLogModel);
	}

	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		return actionLogModel;
	}

	public SwitchController getSwitchController() {
		return switchController;
	}

	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}

	public AbstractFinancialInstitution getOlaVeriflyFinancialInstitution() {
		return olaVeriflyFinancialInstitution;
	}

	public void setOlaVeriflyFinancialInstitution(
			AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
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

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
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

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public SettlementManager getSettlementManager() {
		return settlementManager;
	}

	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}

	@Override
	public boolean makeTransactionsUnclaimed(TransactionModel transaction, StakeholderBankInfoModel olaSundaryAccount, StakeholderBankInfoModel olaUnclaimedSundaryAccount) throws FrameworkCheckedException, WorkFlowException {

		String transactionCode = transaction.getTransactionCodeIdTransactionCodeModel().getCode();
		String receiverMobileNo = null;
		Double txAmount = 0d;
		TransactionDetailModel transactionDetailModel = null;
		List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transaction.getTransactionIdTransactionDetailModelList();
		if (txdetails != null) {
			transactionDetailModel = txdetails.get(0);
			txAmount = transaction.getTotalAmount();
			if (transactionDetailModel.getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH) {
				receiverMobileNo =  transaction.getCustomerMobileNo();
			}else{
				receiverMobileNo = transactionDetailModel.getCustomField6();
			}
		}else{
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to load TransactionDetailModel for TransactionCodeId: " + transaction.getTransactionCodeId());
		}
		
		// sundry debit amount calculation - start
		BaseWrapper baseWrapper = new BaseWrapperImpl();
        TransactionDetailMasterModel trxDetailMasterModel = new TransactionDetailMasterModel();
        trxDetailMasterModel.setTransactionCodeId(transaction.getTransactionCodeId());
        baseWrapper.setBasePersistableModel(trxDetailMasterModel);
        baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
        trxDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
        if(trxDetailMasterModel == null){
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to load TransactionDetailMasterModel for TransactionCodeId: " + transaction.getTransactionCodeId());
		}
		Boolean isThirdPartyIncluded = trxDetailMasterModel.getThirdPartyCheck();
		isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;
		Double inclusiveChargesApplied = CommonUtils.getDoubleOrDefaultValue(trxDetailMasterModel.getInclusiveCharges());
				
		CommissionAmountsHolder commissionAmountsHolder = commissionManager.loadCommissionDetailsUnsettled(transactionDetailModel.getTransactionDetailId());

		double totalUnsettledCommission = CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getTotalCommissionAmountUnsettled());
		
		Double totalAmount = Double.valueOf(Formatter.formatDouble(trxDetailMasterModel.getTransactionAmount() 
								+ totalUnsettledCommission
								- inclusiveChargesApplied));
        
    	logger.info("Scheduler makeTransactionsUnclaimed Details - Transaction ID: " + trxDetailMasterModel.getTransactionCode()
				+"\nProduct: " + trxDetailMasterModel.getProductName()
				+"\nsundryDebitAmount: " + totalAmount
				+"\ntrxAmount: "+ trxDetailMasterModel.getTransactionAmount() 
				+"\ninclChargesApplied: " + inclusiveChargesApplied
				+"\ntotalUnsettledComm: " + totalUnsettledCommission
				);
		// sundry debit amount calculation - end
		
		ActionLogModel actionLogModel = new ActionLogModel();
		XStream xstream = new XStream();
		xstream.toXML(new String(" "));
		actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
		actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String(" ")), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogBeforeStart(actionLogModel);

		// Update Transaction Model STATUS to Reversed
		boolean status = this.orphanTransactionDAO.reverseTransaction(transaction.getTransactionId(), SupplierProcessingStatusConstants.UNCLAIMED, SCHEDULER_APP_USER_ID, new Date() );
		if (!status){
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to update transaction status - TransactionCodeId: " + transaction.getTransactionCodeId());
		}
		
		SwitchWrapper switchWrapperOLA = olaFundsTransfer(totalAmount, olaSundaryAccount.getAccountNo(), olaUnclaimedSundaryAccount.getAccountNo(), CustomerAccountTypeConstants.SETTLEMENT, transaction);
		
		if ( ! switchWrapperOLA.getOlavo().getResponseCode().equals("00")){
			throw new FrameworkCheckedException("[OrphanTransactionManagerImpl.reverseOrphanTransaction] Unable to transfer funds from Acc2Cash Sundry to customer account - TransactionCodeId: " + transaction.getTransactionCodeId());
		}

		makeSettlementTransactionEntry(totalAmount, PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID, PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_ACCOUNT, transactionDetailModel.getProductId(), transaction.getTransactionId());
		
		logger.info("Receier Mobile No: " + receiverMobileNo);
		if (StringUtils.isNotEmpty(receiverMobileNo)) {
			String notification = messageSource.getMessage("REVERSALREDEMPTION.NOTIFICATION", new Object[] {transactionCode}, null);
			logger.info("NOTIFICATION: "+notification);
			SmsMessage message = new SmsMessage(receiverMobileNo, notification);
			try {
				smsSender.send(message);
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}
		}
		
		actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String("dumy")), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogAfterEnd(actionLogModel);

		return true;
	}

	public void setTransactionDetailMasterManager(
			TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}

	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}
}
