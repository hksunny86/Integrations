package com.inov8.microbank.server.service.workflow.sales;

import java.util.ArrayList;
import java.util.List;

import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.dispenser.TransferInDispenser;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.workflow.credittransfer.CreditTransferTransaction;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.ReasonConstants;
import com.inov8.verifly.common.model.AccountInfoModel;

public class TransferInTransaction extends CreditTransferTransaction {

	private final String OLA_TRANSFER_IN_SWITCH = "OLA_TRANSFER_IN_SWITCH";
	private final String OLA_TRANSFER_IN_RECON_SWITCH = "OLA_TRANSFER_IN_RECON_SWITCH";
	private final String CORE_TRANSFER_IN_SWITCH = "CORE_TRANSFER_IN_SWITCH";
	
	protected final Log log = LogFactory.getLog(getClass());
	private DateTimeFormatter dateTimeFormat =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private DateTimeFormatter timeFormat =  DateTimeFormat.forPattern("h:mm a");

	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private FinancialIntegrationManager financialIntegrationManager;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private ProductDispenseController productDispenseController;
	private CommissionManager commissionManager;
	private MessageSource messageSource;
	private GenericDao genericDAO;
	private String rrnPrefix;
	private RetailerContactManager retailerContactManager;	
	private TransferInDispenser transferInDispenser = null;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;


	@Override
	protected WorkFlowWrapper doValidate(WorkFlowWrapper workFlowWrapper) throws Exception {
		
		if (workFlowWrapper.getUserDeviceAccountModel() == null) {
			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}	
		
		if (workFlowWrapper.getProductModel() != null) {
			
			if (!workFlowWrapper.getProductModel().getActive()) {
		
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
			}
		} else {
		
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
		}	
		
		return workFlowWrapper;
	}
	

	/**
	 * This method calls the commission module to calculate the commission on this product and transaction.
	 * The wrapper should have product,payment mode and principal amount that is passed onto the commission module
	 * 
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public void calculateCommission(WorkFlowWrapper workFlowWrapper) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of TransferInTransaction....");
		}

		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
		workFlowWrapper.setSegmentModel(segmentModel);
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(workFlowWrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(workFlowWrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(workFlowWrapper.getProductModel());
		
	    workFlowWrapper.setTaxRegimeModel(workFlowWrapper.getFromRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(workFlowWrapper);

		workFlowWrapper.setCommissionWrapper(commissionWrapper);		
	}	
	
	
	/**
	 * 
	 * @param commissionHolder CommissionAmountsHolder
	 * @param calculatedCommissionHolder CommissionAmountsHolder
	 * @throws FrameworkCheckedException
	 */
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		ProductVO productVO = (ProductVO) workFlowWrapper.getProductVO();

		if (commissionHolder.getTotalCommissionAmount().doubleValue() != workFlowWrapper.getTotalCommissionAmount().doubleValue()) {

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		
		if (commissionHolder.getTotalAmount().doubleValue() != workFlowWrapper.getTotalAmount().doubleValue()){

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
		}
		
		if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()){
			
			if (commissionHolder.getTotalAmount().doubleValue() < workFlowWrapper.getDiscountAmount()) {

				throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
			}
		}

		if (this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue() != workFlowWrapper.getTxProcessingAmount().doubleValue()) {

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}
		if (logger.isDebugEnabled())
		{

			logger.debug("Ending validateCommission of TransferInTransaction...");
		}

	}
	
	
	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		
		if(logger.isDebugEnabled())	{
			logger.debug("Inside getTransactionProcessingCharges of TransferInTransaction....");
		}
		
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList) {
			
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue()) {

				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue()) {
				
					transProcessingAmount += commissionRateModel.getRate();
					
				} else {
					
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
				}
			}
		}
		
		if(logger.isDebugEnabled()) {
			
			logger.debug("Ending getTransactionProcessingCharges of TransferInTransaction....");
		}
		
		return transProcessingAmount;
	}	
	
	
	@Override
	public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper _workFlowWrapper) throws Exception {
 		
 		TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();
		transactionModel.setConfirmationMessage(" _ ");
		transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
		transactionModel.setDiscountAmount(0.0D);
		transactionModel.setNotificationMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionModel.setDeviceTypeId(_workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
		transactionModel.setTransactionTypeId(_workFlowWrapper.getTransactionTypeModel().getTransactionTypeId());
		transactionModel.setFromRetContactId(_workFlowWrapper.getAppUserModel().getRetailerContactId());
//		transactionModel.setToRetContactId(_workFlowWrapper.getAppUserModel().getRetailerContactId());
		transactionModel.setMfsId(_workFlowWrapper.getUserDeviceAccountModel().getUserId());
		transactionModel.setFromRetContactName(_workFlowWrapper.getAppUserModel().getFirstName() +" "+ _workFlowWrapper.getAppUserModel().getLastName());
		transactionModel.setFromRetContactMobNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionModel.setCustomerMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionModel.setSaleMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionModel.setProcessingBankId(_workFlowWrapper.getBankModel().getBankId());
		transactionModel.setFromDistContactId(_workFlowWrapper.getSmartMoneyAccountModel().getDistributorContactId());
		transactionModel.setToDistContactId(_workFlowWrapper.getSmartMoneyAccountModel().getDistributorContactId());		
		transactionModel.setTransactionAmount(_workFlowWrapper.getBillAmount());
		transactionModel.setDistributorId(_workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		
		transactionModel.setRetailerId(_workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getRetailerId());
		transactionModel.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		transactionModel.setSmartMoneyAccountId(_workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		
		
 		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
		transactionDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
		transactionDetailModel.setSettled(Boolean.TRUE);
		transactionDetailModel.setConsumerNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionDetailModel.setSettled(Boolean.TRUE);
		transactionDetailModel.setCustomField1(String.valueOf(_workFlowWrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId()));
		transactionDetailModel.setCustomField3("8");
		
		_workFlowWrapper.setTransactionDetailModel(transactionDetailModel);

		computeCommission(_workFlowWrapper);

		CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();
		
		transactionModel.setTotalCommissionAmount(commissionAmountsHolder.getTotalCommissionAmount() );
		_workFlowWrapper.getTransactionDetailModel().setActualBillableAmount(commissionAmountsHolder.getBillingOrganizationAmount());
		
		
		Double totalCommissionAmount = 0.0D;
		
		if(commissionAmountsHolder.getTotalCommissionAmount() != null) {
			totalCommissionAmount = commissionAmountsHolder.getTotalCommissionAmount();
		}
		
		transactionModel.setTransactionAmount(commissionAmountsHolder.getTransactionAmount());
		transactionModel.setTotalAmount(transactionModel.getTransactionAmount() +commissionAmountsHolder.getExclusiveFixAmount()+commissionAmountsHolder.getExclusivePercentAmount());
		transactionModel.setTotalCommissionAmount(totalCommissionAmount);	
		
		_workFlowWrapper.setTransactionModel(transactionModel);

		_workFlowWrapper.getTransactionDetailMasterModel().setSendingRegion(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionId());
		_workFlowWrapper.getTransactionDetailMasterModel().setSendingRegionName(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionName());

		_workFlowWrapper.getTransactionDetailMasterModel().setSenderAreaId(_workFlowWrapper.getRetailerContactModel().getAreaId());
		_workFlowWrapper.getTransactionDetailMasterModel().setSenderAreaName(_workFlowWrapper.getRetailerContactModel().getAreaName());

		_workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorId(_workFlowWrapper.getDistributorModel().getDistributorId());
		_workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorName(_workFlowWrapper.getDistributorModel().getName());
		if (null != _workFlowWrapper.getDistributorModel().getMnoId()) {
			_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPId(_workFlowWrapper.getDistributorModel().getMnoId());
			_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPName(_workFlowWrapper.getDistributorModel().getMnoModel().getName());
			//		_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPName(_workFlowWrapper.getDistributorModel().getMnoModel().getName());
		}
		
		// Set Handler Detail in Transaction and Transaction Detail Master
		if(_workFlowWrapper.getHandlerModel() != null){
			_workFlowWrapper.getTransactionModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
				_workFlowWrapper.getTransactionModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
				_workFlowWrapper.getTransactionDetailMasterModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
				_workFlowWrapper.getTransactionDetailMasterModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
		}
			
		
		if(transferInDispenser == null) {
			
			transferInDispenser = (TransferInDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
		}
		
		Double coreBalance = 0.0D ;
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());
		
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		_workFlowWrapper.setTransactionModel(transactionModel);
		
		this.transferOLA(_workFlowWrapper);			
		
		this.transferCore(_workFlowWrapper);

		_workFlowWrapper.putObject("CORE_BALANCE", coreBalance.toString());
		
		this.sendSMS(_workFlowWrapper);

		_workFlowWrapper.getTransactionModel().addTransactionIdTransactionDetailModel(transactionDetailModel);
		this.saveTransaction(_workFlowWrapper); // save the transaction
		
		
		SETTLE_COMMISSION_BLOCK : {
		
			if (logger.isDebugEnabled()) {				
				logger.debug("Going to settle commissions using SettlementManager....");
			}
			
			this.settlementManager.settleCommission(_workFlowWrapper.getCommissionWrapper(), _workFlowWrapper);
		}
			
		
		if (logger.isDebugEnabled()) {
			
			logger.debug("Ending doSale of TransferInTransaction.");
		}

		return _workFlowWrapper;
	}

	
	/**
	 * @param _workFlowWrapper
	 */
	void sendSMS(WorkFlowWrapper _workFlowWrapper) {

		String brandName = null;

		if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
			brandName = MessageUtil.getMessage("sco.brandName");
		}
		else {
			brandName = MessageUtil.getMessage("jsbl.brandName");
		}

		Double agentBalance = (Double) _workFlowWrapper.getObject("OLA_BALANCE");
		String olaBalance = Formatter.formatDoubleByPattern(agentBalance, "#,###.00");
		String coreAccountNumber = (String) _workFlowWrapper.getObject("CORE_ACCOUNT_NO");
		
		TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();
		
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
		
		if(_workFlowWrapper.getHandlerModel() == null  ||
				(_workFlowWrapper.getHandlerModel() != null && _workFlowWrapper.getHandlerModel().getSmsToAgent())){

			Object[] smsParams = new Object[] {brandName, 
					transactionModel.getTransactionCodeIdTransactionCodeModel().getCode(),
					transactionModel.getTransactionAmount().toString(),
					timeFormat.print(new LocalTime()),
					dateTimeFormat.print(new DateTime()),
					coreAccountNumber,
					brandName,
					olaBalance
					};

			String agentSMS = this.messageSource.getMessage("TransferInPayment.SMS.Notification", smsParams, null);
								
			transactionModel.setNotificationMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
			transactionModel.setConfirmationMessage(agentSMS);
			_workFlowWrapper.getTransactionDetailModel().setCustomField4(agentSMS);

			messageList.add(new SmsMessage(ThreadLocalAppUser.getAppUserModel().getMobileNo(), agentSMS));
		}
		
		if(_workFlowWrapper.getHandlerModel() != null && _workFlowWrapper.getHandlerModel().getSmsToHandler()){

			Object[] smsParams = new Object[] {brandName, 
					transactionModel.getTransactionCodeIdTransactionCodeModel().getCode(),
					transactionModel.getTransactionAmount().toString(),
					timeFormat.print(new LocalTime()),
					dateTimeFormat.print(new DateTime()),
					coreAccountNumber,
					brandName,
					olaBalance
					};

			String handlerSMS = this.messageSource.getMessage("TransferInPayment.SMS.Notification", smsParams, null);
			messageList.add(new SmsMessage(_workFlowWrapper.getHandlerAppUserModel().getMobileNo(), handlerSMS));
		}

		_workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
	}	
	
	
	
	/**
	 * @param _workFlowWrapper
	 * @param transactionModel
	 * @param olaAccountInfoModel
	 * @throws Exception
	 */
	void transferOLA(WorkFlowWrapper _workFlowWrapper) throws Exception {

		OLAVO olaVO = new OLAVO();
		
		List<OLAInfo> debitList = new ArrayList<OLAInfo>(0);
		List<OLAInfo> creditList = new ArrayList<OLAInfo>(0);

		CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();
		
		Double agent1CommissionAmount = commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);
		
		StakeholderBankInfoModel iftPoolAccountStakeHolder = this.getStakeholderBankInfoModel(PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID);

		Boolean isThirdPartyIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
		isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;

		String iftPoolAccount = iftPoolAccountStakeHolder.getAccountNo();		
		String agentBBAccount = _workFlowWrapper.getAccountInfoModel().getAccountNo();
		Double olaBalance = 0.0D;
		
		Double iftPoolAccountCreditAmount = commissionAmountsHolder.getTransactionAmount() + 
											commissionAmountsHolder.getExclusiveFixAmount() + 
											commissionAmountsHolder.getExclusivePercentAmount();

		iftPoolAccountCreditAmount = iftPoolAccountCreditAmount == null ? 0.0D : iftPoolAccountCreditAmount;	
		
		OLAInfo iftPoolAccountDebitFT = new OLAInfo();													//	FT 1
		iftPoolAccountDebitFT.setReasonId(ReasonConstants.TRANSFER_IN);
		iftPoolAccountDebitFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		iftPoolAccountDebitFT.setIsAgent(Boolean.TRUE);
		iftPoolAccountDebitFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
		iftPoolAccountDebitFT.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
		iftPoolAccountDebitFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
		iftPoolAccountDebitFT.setReceivingAccNo(iftPoolAccount);
		iftPoolAccountDebitFT.setPayingAccNo(iftPoolAccount);
		iftPoolAccountDebitFT.setBalance(iftPoolAccountCreditAmount);	
		
		if(!StringUtil.isNullOrEmpty(iftPoolAccount)) {

			debitList.add(iftPoolAccountDebitFT);	
		}
		
		Double inclusiveCharges = (commissionAmountsHolder.getInclusiveFixAmount() + commissionAmountsHolder.getInclusivePercentAmount());
		inclusiveCharges = inclusiveCharges == null ? 0.0D : inclusiveCharges;
		
		Double agentCreditAmount = (commissionAmountsHolder.getTransactionAmount() + agent1CommissionAmount);
		agentCreditAmount = agentCreditAmount == null ? 0.0D : agentCreditAmount;
		
		if(!isThirdPartyIncluded ) {

			agentCreditAmount -= inclusiveCharges;
		}
		
		agentCreditAmount = agentCreditAmount == null ? 0.0D : agentCreditAmount;
		
		OLAInfo agentAccountCreditFT = new OLAInfo();													//	FT 2 
		agentAccountCreditFT.setReasonId(ReasonConstants.TRANSFER_IN);
		agentAccountCreditFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
		agentAccountCreditFT.setCustomerAccountTypeId(_workFlowWrapper.getRetailerContactModel().getOlaCustomerAccountTypeModelId());
		agentAccountCreditFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
		agentAccountCreditFT.setPayingAccNo(agentBBAccount);
		agentAccountCreditFT.setReceivingAccNo(agentBBAccount);
		agentAccountCreditFT.setBalance(agentCreditAmount);	
		agentAccountCreditFT.setAgentBalanceAfterTrxRequired(Boolean.TRUE);
		agentAccountCreditFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		agentAccountCreditFT.setIsAgent(Boolean.TRUE);
		
        if((_workFlowWrapper.getHandlerModel() != null)){
        	agentAccountCreditFT.setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
        	agentAccountCreditFT.setHandlerAccountTypeId(_workFlowWrapper.getHandlerModel().getAccountTypeId());
        }
		
		if(!StringUtil.isNullOrEmpty(agentBBAccount)) {

			creditList.add(agentAccountCreditFT);	
		}
		
		olaVO.setCreditAccountList(creditList);
		olaVO.setDebitAccountList(debitList);			
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();			
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());	
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setTransactionAmount(_workFlowWrapper.getTransactionModel().getTransactionAmount() + _workFlowWrapper.getTransactionModel().getTotalCommissionAmount());
		switchWrapper.setOlavo(olaVO);
		
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		
		/*
		 *** 	SENDING TRANSFER IN TO OLA BANKING.
		 */				
		logger.info("OLA > SENDING TRANSFER-IN TO OLA BANKING.");
					
		if(transferInDispenser == null) {
			
			transferInDispenser = (TransferInDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
		}
		
		_workFlowWrapper = transferInDispenser.doSale(_workFlowWrapper);
				
		olaBalance = _workFlowWrapper.getSwitchWrapper().getOlavo().getAgentBalanceAfterTransaction();
		
		_workFlowWrapper.getTransactionDetailModel().setCustomField2(agentBBAccount);
		_workFlowWrapper.putObject("OLA_BALANCE", olaBalance);
	}	
	
	
	/**
	 * @param _workFlowWrapper
	 * @throws Exception
	 */
	void transferCore(WorkFlowWrapper _workFlowWrapper) throws Exception {

		Boolean isThirdPartyIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
		isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;		
		
		AccountInfoModel accountInfoModelCore = 
				transferInDispenser.getAccountInfoModelBySmartMoneyAccount(_workFlowWrapper.getSmartMoneyAccountModel(), 
						ThreadLocalAppUser.getAppUserModel().getAppUserId(), _workFlowWrapper.getTransactionModel().getTransactionCodeId());
		
		AccountInfoModel olaAccountInfoModel = _workFlowWrapper.getAccountInfoModel();
		
		accountInfoModelCore.setOldPin(olaAccountInfoModel.getOldPin());
		
		String coreAccountNumber = accountInfoModelCore.getAccountNo();
		
		StakeholderBankInfoModel inwardFundTransferStakeholderBankInfoModel = new StakeholderBankInfoModel();
		inwardFundTransferStakeholderBankInfoModel.setStakeholderBankInfoId(PoolAccountConstantsInterface.INWARD_FUND_TRANSFER_ACCOUNT_ID);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(inwardFundTransferStakeholderBankInfoModel);
		
		searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
		
		inwardFundTransferStakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
					
		CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();
		Double amount = commissionAmountsHolder.getTransactionAmount();
		Double exclusiveAmount = commissionAmountsHolder.getExclusiveFixAmount() + commissionAmountsHolder.getExclusivePercentAmount();

		Boolean isExclusiveAmountApplied = (exclusiveAmount != null && exclusiveAmount > 0D) ? Boolean.TRUE : Boolean.FALSE;
		
		if(isExclusiveAmountApplied || isThirdPartyIncluded) {

			amount += exclusiveAmount;
		}
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);		
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
		switchWrapper.setMiddlewareIntegrationMessageVO(new MiddlewareMessageVO());
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setAccountInfoModel(accountInfoModelCore);
		switchWrapper.setTransactionAmount(amount);
		
		switchWrapper.setFromAccountNo(accountInfoModelCore.getAccountNo());	
		switchWrapper.setFromAccountType("20");
		switchWrapper.setFromCurrencyCode("586");			
		
		switchWrapper.setToAccountNo(inwardFundTransferStakeholderBankInfoModel.getAccountNo());
		switchWrapper.setToAccountType("20");
		switchWrapper.setToCurrencyCode("586");

		_workFlowWrapper.setAccountInfoModel(accountInfoModelCore);
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setSmartMoneyAccountModel(_workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapper.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE);
		/*
		 *** 	SENDING TRANSFER OUT TO CORE BANKING.
		 */				
		logger.info("JS Core > SENDING TRANSFER IN TO PHOENIX.");
		logger.info("JS Core > FROM ACCOUNT : "+switchWrapper.getFromAccountNo());
		logger.info("JS Core > TO ACCOUNT : "+switchWrapper.getToAccountNo());
		logger.info("JS Core > AMOUNT : "+switchWrapper.getTransactionAmount());
				
		switchWrapper = phoenixFinancialInstitution.debitAccount(switchWrapper);

		String bankResponseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();
		
		_workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper); // for day end O.F. settlement of Core FT
					
		_workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
		_workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		_workFlowWrapper.putObject("CORE_ACCOUNT_NO", coreAccountNumber);
		_workFlowWrapper.getTransactionModel().setBankAccountNo(accountInfoModelCore.getAccountNo());

	}
	
	
	/**
	 * @param _workFlowWrapper
	 * @throws Exception
	 */
	void computeCommission(WorkFlowWrapper _workFlowWrapper) throws Exception {

		this.calculateCommission(_workFlowWrapper);
		
		Double franchise1CommissionAmount = 0.0D;
		Double agent1CommissionAmount = 0.0D;
		
		CommissionWrapper commissionWrapper = _workFlowWrapper.getCommissionWrapper();
				
		CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		
		List<CommissionRateModel> commissionRateModelArray = (ArrayList<CommissionRateModel>) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);
		
		Double _commissionRate = 0.0D;
		
		if(commissionRateModelArray != null && !commissionRateModelArray.isEmpty()) {
			_commissionRate = commissionRateModelArray.get(0).getRate();
		} else {	
			logger.error("No Commission Amount Defined or Check Transaction Amount vs Commission Rate's range");
		}			
		
		//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
		
		if(commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID) != null) {

			agent1CommissionAmount = commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);
		}
		
		if(commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID) != null) {

			franchise1CommissionAmount = commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID);
		}
		
		if(_workFlowWrapper.getFromRetailerContactModel() != null && _workFlowWrapper.getFromRetailerContactModel().getHead()) {
			
			agent1CommissionAmount += franchise1CommissionAmount;
			franchise1CommissionAmount = 0.0D;
		}
		
		if(_commissionRate != null && _commissionRate > 0) {
			commissionAmountsHolder.setTotalCommissionAmount(_commissionRate);
		}
	
		/* Check removed as it never executed ( no record against productDeviceFlow.DeviceTypeId = 1)
		ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
		productDeviceFlowModel.setProductId(_workFlowWrapper.getProductModel().getPrimaryKey());
		productDeviceFlowModel.setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );
			
		List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

		if(list != null && list.size() > 0) {
			productDeviceFlowModel = list.get(0);	
			_workFlowWrapper.setProductDeviceFlowModel(productDeviceFlowModel);
		}
			
		if ( productDeviceFlowModel != null && 	productDeviceFlowModel.getDeviceFlowId() != null && productDeviceFlowModel.getDeviceFlowId().longValue() != 17 ) {
		
			this.validateCommission(commissionWrapper, _workFlowWrapper); // validate
		}
		*/

		commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID, agent1CommissionAmount);
		
		_workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);			
	}	
	
	/*
	CHECK_CORE_ACCOUNT_BALANCE : {

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
		
		AbstractFinancialInstitution abstractFinancialInstitution = phoenixFinancialInstitution;//this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setAccountInfoModel(coreAccountInfoModel);
		switchWrapper.setFromAccountNo(coreAccountInfoModel.getAccountNo());
		switchWrapper.setToAccountNo(coreAccountInfoModel.getAccountNo());
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapper.putObject(CommandFieldConstants.KEY_PIN, _workFlowWrapper.getObject(CommandFieldConstants.KEY_PIN));
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setTransactionTransactionModel(transactionModel);
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel() ) ;		

		abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapper);

		coreBalance = switchWrapper.getAgentBalance();
		
		if(coreBalance < transactionModel.getTotalAmount()) {
		
			throw new CommandException("Core Bank Balance > "+messageSource.getMessage("MINI.InsufficientBalance", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}		
		
		coreAccountInfoModel = switchWrapper.getAccountInfoModel();
		coreAccountInfoModel.setOldPin(_workFlowWrapper.getAccountInfoModel().getOldPin());
	}
	*/
	
	
	@Override
	protected WorkFlowWrapper logTransaction(WorkFlowWrapper workFlowWrapper) throws Exception {

		return workFlowWrapper;
	}

	
	private StakeholderBankInfoModel getStakeholderBankInfoModel(Long stakeholderBankInfoId) throws FrameworkCheckedException {
	
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setStakeholderBankInfoId(stakeholderBankInfoId);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		
		this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
		
		stakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
		
		return stakeholderBankInfoModel;
	}

	


	
	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws Exception {
	
		SwitchWrapper switchWrapper = (SwitchWrapper) workFlowWrapper.getObject(this.OLA_TRANSFER_IN_SWITCH);
		SwitchWrapper switchWrapperRecon = (SwitchWrapper) workFlowWrapper.getObject(this.OLA_TRANSFER_IN_RECON_SWITCH);
		SwitchWrapper switchWrapperCore = (SwitchWrapper) workFlowWrapper.getObject(this.CORE_TRANSFER_IN_SWITCH);

		SmartMoneyAccountModel coreSmartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
		SmartMoneyAccountModel olaSmartMoneyAccountModel = workFlowWrapper.getOlaSmartMoneyAccountModel();
		
		workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
		
		if(switchWrapper != null) {
			logger.info("****************** ROLL BACK FOR OLA_TRANSFER_IN ******************");
			saveTransaction(workFlowWrapper);
			switchWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
			rollback(switchWrapper, workFlowWrapper);
		}
		
		if(switchWrapperRecon != null) {
			logger.info("****************** ROLL BACK FOR OLA_TRANSFER_IN_RECON ******************");
			saveTransaction(workFlowWrapper);
			switchWrapperRecon.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
			rollback(switchWrapperRecon, workFlowWrapper);
		}
		
		if(switchWrapperCore != null) {
			logger.info("****************** ROLL BACK FOR TRANSFER_IN_CORE ******************");
			saveTransaction(workFlowWrapper);
			switchWrapperCore.setSmartMoneyAccountModel(coreSmartMoneyAccountModel);
			rollback(switchWrapperCore, workFlowWrapper);
		}
		
		return workFlowWrapper;
	}	
	
	
	
	private void rollback(SwitchWrapper switchWrapper, WorkFlowWrapper workFlowWrapper) throws Exception {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(switchWrapper.getSmartMoneyAccountModel());
				
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);		
		
		logger.info("Financial Institution Class = "+abstractFinancialInstitution.getClass().getName());
		
		String olaFromAccount = switchWrapper.getToAccountNo();
		String olaToAccount = switchWrapper.getFromAccountNo();

		if(!StringUtil.isNullOrEmpty(olaFromAccount) && !StringUtil.isNullOrEmpty(olaToAccount)) {
			
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
			switchWrapper.setBasePersistableModel(workFlowWrapper.getOlaSmartMoneyAccountModel());
			switchWrapper.setBankId(switchWrapper.getSmartMoneyAccountModel().getBankId());	
			switchWrapper.setTransactionAmount(switchWrapper.getTransactionAmount());		
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setFromAccountNo(olaFromAccount);
			switchWrapper.setToAccountNo(olaToAccount);		
			
			OLAVO olaVO = switchWrapper.getOlavo();
			
			if(olaVO == null) {
				olaVO = new OLAVO();
				olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
				switchWrapper.setOlavo(olaVO);
			}
			
			switchWrapper.getOlavo().setPayingAccNo(olaFromAccount);
			switchWrapper.getOlavo().setReceivingAccNo(olaToAccount);
			switchWrapper.getOlavo().setBalance(switchWrapper.getTransactionAmount());

			logger.info("Rollback. From Account : "+olaFromAccount);
			logger.info("Rollback. To Account   : "+olaToAccount);
			logger.info("Rollback. Amount       : "+switchWrapper.getTransactionAmount());
			
			abstractFinancialInstitution.rollback(switchWrapper);
		}
	}
	
	
	private void saveTransaction(WorkFlowWrapper _workFlowWrapper) throws FrameworkCheckedException {
		
		txManager.transactionRequiresNewTransaction(_workFlowWrapper);		
	}
	
	/*
	 * 	DI/IOC Methods
	 */

	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}
	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}
	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)	{
		this.financialIntegrationManager = financialIntegrationManager;
	}	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	public void setProductDispenseController(ProductDispenseController productDispenseController) {
		this.productDispenseController = productDispenseController;
	}
	public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}
	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
	public void setRetailerContactManager(RetailerContactManager retailerContactManager){
		this.retailerContactManager = retailerContactManager;
	}
	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}
	
	
	  /**
	   * Populates the transaction object with all the necessary data to save it in the db.
	   * @param wrapper WorkFlowWrapper
	   * @return WorkFlowWrapper
	   */
	  @Override
	  protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper) throws Exception {
		  
		_workFlowWrapper = super.doPreStart(_workFlowWrapper);
		  
		  // Populate Retailer Contact model from DB
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		RetailerContactModel retailerContact = new RetailerContactModel();
		retailerContact.setRetailerContactId( _workFlowWrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
		searchBaseWrapper.setBasePersistableModel( retailerContact );
		searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
		_workFlowWrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());
						
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(_workFlowWrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
			
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			
		_workFlowWrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());	
		
		// Populate Handler's Retailer Contact model from DB
		if(_workFlowWrapper.getHandlerModel() != null){
//			searchBaseWrapper = new SearchBaseWrapperImpl();
//			retailerContact = new RetailerContactModel();
//			retailerContact.setRetailerContactId( _workFlowWrapper.getHandlerModel().getRetailerContactId() );
//			searchBaseWrapper.setBasePersistableModel( retailerContact );
//			searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
//			_workFlowWrapper.setHandlerRetContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());

			// Populate the Handler OLA Smart Money Account from DB
			SmartMoneyAccountModel sma = smartMoneyAccountManager.getSMAccountByHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
			_workFlowWrapper.setHandlerSMAModel(sma);

			// Set Handler User Device Account Model
			UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
			handlerUserDeviceAccountsModel.setAppUserId(_workFlowWrapper.getHandlerAppUserModel().getAppUserId());
			handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
			baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			_workFlowWrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		}
		  
		return _workFlowWrapper;
	  }
		
	  public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		  this.smartMoneyAccountManager = smartMoneyAccountManager;
	  }

}