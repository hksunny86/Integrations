package com.inov8.microbank.server.service.workflow.sales;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

import java.util.ArrayList;
import java.util.List;

import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
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
import com.inov8.microbank.server.service.integration.dispenser.TellerCashInDispenser;
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

public class TellerCashInTransaction extends CreditTransferTransaction {

	private final String OLA_CASH_IN_SWITCH = "OLA_CASH_IN_SWITCH";
	private final String OLA_CASH_IN_RECON_SWITCH = "OLA_CASH_IN_RECON_SWITCH";
	private final String CORE_CASH_IN_SWITCH = "CORE_CASH_IN_SWITCH";
	
	protected final Log log = LogFactory.getLog(getClass());
	private DateTimeFormatter dateFormat =  DateTimeFormat.forPattern("dd-MM-yyyy");
	private DateTimeFormatter timeFormat =  DateTimeFormat.forPattern("HH:MM a");

	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private FinancialIntegrationManager financialIntegrationManager;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private ProductDispenseController productDispenseController;
	private CommissionManager commissionManager;
	private MessageSource messageSource;
	private GenericDao genericDAO;
	private String rrnPrefix;
	private RetailerContactManager retailerContactManager;	
	private TellerCashInDispenser tellerCashInDispenser = null;
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
	public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of TransferInTransaction....");
		}

		Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
				
		AppUserModel userAppUserModel = workFlowWrapper.getReceiverAppUserModel();
					
		if(userAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {				
				
			CustomerModel customerModel = null;
				
			List<CustomerModel> list = this.genericDAO.findEntityByExample(new CustomerModel(userAppUserModel.getCustomerId()), null);

			if(list != null && list.size() > 0) {
				
				customerModel = list.get(0);
				segmentId = customerModel.getSegmentId();	
				workFlowWrapper.setCustomerModel(customerModel);	
			}
		}
					
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionAmount(workFlowWrapper.getTransactionAmount());

		workFlowWrapper.setSegmentModel(new SegmentModel(segmentId));
		workFlowWrapper.setTransactionModel(transactionModel);
		workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel(TransactionTypeConstantsInterface.Teller_CASH_IN_TX));
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(workFlowWrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(workFlowWrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(workFlowWrapper.getProductModel());
		
		if(ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() == workFlowWrapper.getProductModel().getProductId().longValue() && 
				userAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()|| 
						userAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
			
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerIdRetailerModel(new RetailerModel());		
			workFlowWrapper.setRetailerContactModel(retailerContactModel);
		}		
		
		
		RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	    workFlowWrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(workFlowWrapper);

		return commissionWrapper;
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
		transactionModel.setToRetContactId(_workFlowWrapper.getAppUserModel().getRetailerContactId());
		transactionModel.setMfsId(_workFlowWrapper.getUserDeviceAccountModel().getUserId());
		transactionModel.setFromRetContactName(_workFlowWrapper.getAppUserModel().getFirstName() +" "+ _workFlowWrapper.getAppUserModel().getLastName());
		transactionModel.setFromRetContactMobNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionModel.setCustomerMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionModel.setProcessingBankId(_workFlowWrapper.getBankModel().getBankId());
		transactionModel.setFromDistContactId(_workFlowWrapper.getSmartMoneyAccountModel().getDistributorContactId());
		transactionModel.setToDistContactId(_workFlowWrapper.getSmartMoneyAccountModel().getDistributorContactId());		
		transactionModel.setTransactionAmount(_workFlowWrapper.getTransactionAmount());
		
		transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		transactionModel.setSmartMoneyAccountId(_workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		
		
 		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
		transactionDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
		transactionDetailModel.setSettled(Boolean.TRUE);
		transactionDetailModel.setConsumerNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionDetailModel.setSettled(Boolean.TRUE);
		transactionDetailModel.setCustomField1(String.valueOf(_workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId()));
		transactionDetailModel.setCustomField3("8");

		if(_workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue()) {

			transactionModel.setSaleMobileNo(_workFlowWrapper.getSenderWalkinCustomerModel().getMobileNumber());
			transactionDetailModel.setCustomField6(_workFlowWrapper.getSenderWalkinCustomerModel().getMobileNumber());
			transactionDetailModel.setCustomField7(_workFlowWrapper.getSenderWalkinCustomerModel().getCnic());
			
		}
		
		transactionDetailModel.setCustomField5(_workFlowWrapper.getReceiverAppUserModel().getNic());
		transactionDetailModel.setCustomField8(_workFlowWrapper.getReceiverAppUserModel().getMobileNo());
		
		_workFlowWrapper.setTransactionDetailModel(transactionDetailModel);

		CommissionAmountsHolder commissionAmountsHolder = null;
		CommissionWrapper commissionWrapper = null;
		Double agent1CommissionAmount = 0.0D;
		Double franchise1CommissionAmount = 0.0D;
		
		CALC_COMMISSION : {

			commissionWrapper = this.calculateCommission(_workFlowWrapper);
			
			commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			
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

			_workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);			
		}

		transactionModel.setTotalCommissionAmount(commissionAmountsHolder.getTotalCommissionAmount() );
		transactionDetailModel.setActualBillableAmount(commissionAmountsHolder.getBillingOrganizationAmount());
		transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);
				
		Double totalCommissionAmount = 0.0D;
		
		if(commissionAmountsHolder.getTotalCommissionAmount() != null) {
			totalCommissionAmount = commissionAmountsHolder.getTotalCommissionAmount();
		}
		
		transactionModel.setTransactionAmount(commissionAmountsHolder.getTransactionAmount());
		transactionModel.setTotalAmount(transactionModel.getTransactionAmount() +commissionAmountsHolder.getExclusiveFixAmount()+commissionAmountsHolder.getExclusivePercentAmount());
		transactionModel.setTotalCommissionAmount(totalCommissionAmount);	
		
		_workFlowWrapper.setTransactionModel(transactionModel);
				
		saveTransaction(_workFlowWrapper, transactionModel.getSupProcessingStatusId());	
		
		if(tellerCashInDispenser == null) {
			
			tellerCashInDispenser = (TellerCashInDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
		}
		/*
		Double coreBalance = 0.0D ;
		
		AccountInfoModel coreAccountInfoModel = 
				tellerCashInDispenser.getAccountInfoModelBySmartMoneyAccount(_workFlowWrapper.getSmartMoneyAccountModel(), ThreadLocalAppUser.getAppUserModel().getAppUserId(), transactionModel.getTransactionCodeId());
		
		coreAccountInfoModel.setOldPin(_workFlowWrapper.getAccountInfoModel().getOldPin());
		*/
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
		Double olaBalance = 0.0D ;
		
		AccountInfoModel accountInfoModel = _workFlowWrapper.getAccountInfoModel();
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
		/*
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		
		CHECK_BB_ACCOUNT_BALANCE : {
			
			SwitchWrapper _switchWrapper = new SwitchWrapperImpl();
			_switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
			_switchWrapper.setAccountInfoModel(accountInfoModel);
			_switchWrapper.setTransactionTransactionModel(transactionModel);
			_switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel() ) ;	
			_switchWrapper.putObject(VeriflyKeyConstantInterface.skipCheckPin, Boolean.TRUE);
			
			abstractFinancialInstitution.checkBalance(_switchWrapper);
			olaBalance = _switchWrapper.getBalance();
			*/
			/*	No Need To Check Existing Balance Validation
			
			log.info("OLA Balance : "+ olaBalance);
			
			if(olaBalance < transactionModel.getTotalAmount()) {
				
				throw new CommandException("Branchless Bank Balance > "+messageSource.getMessage("MINI.InsufficientBalance", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
			}
			
			*/
			/*
			olaAccountInfoModel = _switchWrapper.getAccountInfoModel();
			olaAccountInfoModel.setOldPin(_workFlowWrapper.getAccountInfoModel().getOldPin());
		}	
		*/	       

		
        logger.info(commissionAmountsHolder.toString());	
		
        createFundTransferEntries(_workFlowWrapper, commissionAmountsHolder);
        createCoreTransferEntries(_workFlowWrapper);
        createSMS(_workFlowWrapper, transactionDetailModel);
        
        /**********************************************************************************************************************************/        
								
		_workFlowWrapper.setTransactionModel(transactionModel);
		saveTransaction(_workFlowWrapper, SupplierProcessingStatusConstants.COMPLETED );
        
		_workFlowWrapper.setTransactionModel(transactionModel);
			
		saveTransaction(_workFlowWrapper, transactionModel.getSupProcessingStatusId()); // save the transaction
		
		
		SETTLE_COMMISSION_BLOCK : {
		
			if (logger.isDebugEnabled()) {				
				logger.debug("Going to settle commissions using SettlementManager....");
			}
			
			this.settlementManager.settleCommission(commissionWrapper, _workFlowWrapper);
		}
			
		
		if (logger.isDebugEnabled()) {
			
			logger.debug("Ending doSale of TransferInTransaction.");
		}

//		_workFlowWrapper.putObject("CORE_BALANCE", coreBalance.toString());
		_workFlowWrapper.putObject("OLA_BALANCE", olaBalance.toString());

		return _workFlowWrapper;
	}
	
	

	/**
	 * @param _workFlowWrapper
	 * @param commissionAmountsHolder
	 * @return
	 * @throws Exception
	 */
	private WorkFlowWrapper createFundTransferEntries(WorkFlowWrapper _workFlowWrapper, CommissionAmountsHolder commissionAmountsHolder) throws Exception {
        
		OLAVO olaVO = new OLAVO();
		List<OLAInfo> debitList = new ArrayList<OLAInfo>(0);
        List<OLAInfo> creditList = new ArrayList<OLAInfo>(0);			
		
		rrnPrefix = buildRRNPrefix();

		AccountInfoModel accountInfoModel = _workFlowWrapper.getAccountInfoModel();
		StakeholderBankInfoModel stakeholderBankInfoModel_T24SetlementAccount = this.getStakeholderBankInfoModel(PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID);
		
		String customerAgentBBAccount = accountInfoModel.getAccountNo();
		String iftPoolAccount = stakeholderBankInfoModel_T24SetlementAccount.getAccountNo();
		Long accountTypeId = null;
		
		if(_workFlowWrapper.getReceiverAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
			
			accountTypeId = _workFlowWrapper.getRetailerContactModel().getOlaCustomerAccountTypeModelId();
			
		} else if(_workFlowWrapper.getReceiverAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

			accountTypeId = _workFlowWrapper.getCustomerModel().getCustomerAccountTypeId();
		}
		
		Double totalExclusiveCharges = commissionAmountsHolder.getExclusiveFixAmount() + commissionAmountsHolder.getExclusivePercentAmount();
		Double totalInclusiveCharges = commissionAmountsHolder.getInclusiveFixAmount()+commissionAmountsHolder.getInclusivePercentAmount();
		
		OLAInfo settlementAccountDebitFT = new OLAInfo();													//	FT 1
		settlementAccountDebitFT.setReasonId(ReasonConstants.TELLER_CASH_IN);
		settlementAccountDebitFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		settlementAccountDebitFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
		settlementAccountDebitFT.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
		settlementAccountDebitFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
		settlementAccountDebitFT.setReceivingAccNo(iftPoolAccount);
		settlementAccountDebitFT.setPayingAccNo(iftPoolAccount);
		settlementAccountDebitFT.setBalance(commissionAmountsHolder.getTransactionAmount() + totalExclusiveCharges);	
		
		if(!StringUtil.isNullOrEmpty(iftPoolAccount) && settlementAccountDebitFT.getBalance() != null && settlementAccountDebitFT.getBalance() > 0 ) {

			debitList.add(settlementAccountDebitFT);	
		}

		OLAInfo agentCustomerAccountCreditFT = new OLAInfo();													//	FT 2 
		agentCustomerAccountCreditFT.setReasonId(ReasonConstants.TELLER_CASH_IN);
		agentCustomerAccountCreditFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
		agentCustomerAccountCreditFT.setCustomerAccountTypeId(accountTypeId);
		agentCustomerAccountCreditFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
		agentCustomerAccountCreditFT.setPayingAccNo(customerAgentBBAccount);
		agentCustomerAccountCreditFT.setReceivingAccNo(customerAgentBBAccount);
		agentCustomerAccountCreditFT.setBalance((commissionAmountsHolder.getTransactionAmount())-(totalInclusiveCharges));	
		agentCustomerAccountCreditFT.setAgentBalanceAfterTrxRequired(Boolean.TRUE);
		agentCustomerAccountCreditFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		agentCustomerAccountCreditFT.setIsAgent(Boolean.TRUE);
		
		if(!StringUtil.isNullOrEmpty(customerAgentBBAccount) && agentCustomerAccountCreditFT.getBalance() != null && agentCustomerAccountCreditFT.getBalance() > 0 ) {

			creditList.add(agentCustomerAccountCreditFT);	
		}
		
		olaVO.setCreditAccountList(creditList);
		olaVO.setDebitAccountList(debitList);			

		_workFlowWrapper.setAccountInfoModel(accountInfoModel);			
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();			
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());	
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setTransactionAmount(_workFlowWrapper.getTransactionModel().getTransactionAmount() + _workFlowWrapper.getTransactionModel().getTotalCommissionAmount());
		switchWrapper.setOlavo(olaVO);
		switchWrapper.setAccountInfoModel(accountInfoModel);
		switchWrapper.setTransactionTypeId(_workFlowWrapper.getTransactionTypeModel().getTransactionTypeId());
		
//		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		_workFlowWrapper.setOLASwitchWrapper(switchWrapper);
		
		/*
		 *** 	SENDING TRANSFER IN TO OLA BANKING.
		 */				
		logger.info("OLA > SENDING TRANSFER-IN TO OLA BANKING.");
					
		if(tellerCashInDispenser == null) {
			
			tellerCashInDispenser = (TellerCashInDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
		}
		
		_workFlowWrapper = tellerCashInDispenser.doSale(_workFlowWrapper);
		
		_workFlowWrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(iftPoolAccount, 5, "*"));
	
		return _workFlowWrapper;
	}
	
	
	/**
	 * @param _workFlowWrapper
	 * @throws Exception
	 */
	private void createCoreTransferEntries(WorkFlowWrapper _workFlowWrapper) throws Exception {
		
		String tellerIdentityNumber = _workFlowWrapper.getAppUserModel().getTellerId();
		
		StakeholderBankInfoModel inwardFundTransferStakeholderBankInfoModel = new StakeholderBankInfoModel();
		inwardFundTransferStakeholderBankInfoModel.setStakeholderBankInfoId(PoolAccountConstantsInterface.INWARD_FUND_TRANSFER_ACCOUNT_ID);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(inwardFundTransferStakeholderBankInfoModel);
		
		searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
		
		inwardFundTransferStakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
					
		SwitchWrapper switchWrapper = _workFlowWrapper.getOLASwitchWrapper();

		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);		
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
		switchWrapper.setMiddlewareIntegrationMessageVO(new MiddlewareMessageVO());
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setTransactionAmount(_workFlowWrapper.getTransactionModel().getTransactionAmount());
		
		switchWrapper.setFromAccountNo(tellerIdentityNumber);	
		switchWrapper.setFromAccountType("20");
		switchWrapper.setFromCurrencyCode("586");			
		
		switchWrapper.setToAccountNo(inwardFundTransferStakeholderBankInfoModel.getAccountNo());
		switchWrapper.setToAccountType("20");
		switchWrapper.setToCurrencyCode("586");

		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setSmartMoneyAccountModel(_workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapper.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.TELLER_CASH_IN_CORE_REVERSAL);
		/*
		 *** 	SENDING TRANSFER OUT TO CORE BANKING.
		 */				
		logger.info("JS Core > SENDING CASH IN (TELLER) TO PHOENIX.");
		logger.info("JS Core > FROM TELLER : "+switchWrapper.getFromAccountNo());
		logger.info("JS Core > TO ACCOUNT : "+switchWrapper.getToAccountNo());
		logger.info("JS Core > AMOUNT : "+switchWrapper.getTransactionAmount());
				
		switchWrapper = phoenixFinancialInstitution.debitAccount(switchWrapper);

		String bankResponseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();
		
		_workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper);
					
		_workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
		_workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);	
	}	
	
	
	private void createSMS(WorkFlowWrapper _workFlowWrapper, TransactionDetailModel transactionDetailModel) {
		

		String brandName = null;

		if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
			brandName = MessageUtil.getMessage("sco.brandName");
		}
		else {
			brandName = MessageUtil.getMessage("jsbl.brandName");
		}
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
		
		String smsText = null;
		String notificationMobileNo = null;
		Object[] smsParams = null;

		if(_workFlowWrapper.getAppUserModel() != null && 
				(_workFlowWrapper.getReceiverAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue() || 
				 _workFlowWrapper.getReceiverAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue())
				 && ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN.longValue() == _workFlowWrapper.getProductModel().getProductId().longValue()) {
			
			smsParams = new Object[] {brandName, 
					_workFlowWrapper.getTransactionCodeModel().getCode(), 
					Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()),
					_workFlowWrapper.getReceiverAppUserModel().getMobileNo(), 
					Formatter.formatNumbers(_workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
					timeFormat.print(new DateTime()), 
					dateFormat.print(new DateTime())};			
			
			smsText = this.messageSource.getMessage("TELLER.TRNAS_IN_CUSTOMER_RETALER", smsParams, null);
			notificationMobileNo = _workFlowWrapper.getReceiverAppUserModel().getMobileNo();


			_workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);
			_workFlowWrapper.getTransactionModel().setConfirmationMessage(smsText);
			transactionDetailModel.setCustomField4(smsText);

			messageList.add(new SmsMessage(notificationMobileNo, smsText));

		} else if(ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() == _workFlowWrapper.getProductModel().getProductId().longValue()) {

			
		smsParams = new Object[] {brandName,
				_workFlowWrapper.getTransactionCodeModel().getCode(), 
				Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()), 
				_workFlowWrapper.getReceiverAppUserModel().getMobileNo(),
				Formatter.formatNumbers(_workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
				timeFormat.print(new DateTime()), 
				dateFormat.print(new DateTime())};
			
		smsText = this.messageSource.getMessage("TELLER.TRNAS_IN_WALKING_RECIPIENT", smsParams, null);
			notificationMobileNo = _workFlowWrapper.getAppUserModel().getMobileNo();
			messageList.add(new SmsMessage(notificationMobileNo, smsText));

			_workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);
			_workFlowWrapper.getTransactionModel().setConfirmationMessage(smsText);
			transactionDetailModel.setCustomField4(smsText);			
			
		smsParams = new Object[] {brandName,
				_workFlowWrapper.getTransactionCodeModel().getCode(), 
				Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()), 
				_workFlowWrapper.getReceiverAppUserModel().getMobileNo(), 
				Formatter.formatNumbers(_workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
				timeFormat.print(new DateTime()), 
				dateFormat.print(new DateTime())};
		
			smsText = this.messageSource.getMessage("TELLER.TRNAS_IN_WALKING_DEPOSITOR", smsParams, null);
			notificationMobileNo = _workFlowWrapper.getSenderWalkinCustomerModel().getMobileNumber();
			messageList.add(new SmsMessage(notificationMobileNo, smsText));
		}

		_workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);	
	}
	
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
	
		SwitchWrapper switchWrapper = (SwitchWrapper) workFlowWrapper.getObject(this.OLA_CASH_IN_SWITCH);
		SwitchWrapper switchWrapperRecon = (SwitchWrapper) workFlowWrapper.getObject(this.OLA_CASH_IN_RECON_SWITCH);
		SwitchWrapper switchWrapperCore = (SwitchWrapper) workFlowWrapper.getObject(this.CORE_CASH_IN_SWITCH);

		SmartMoneyAccountModel coreSmartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
		SmartMoneyAccountModel olaSmartMoneyAccountModel = workFlowWrapper.getOlaSmartMoneyAccountModel();
		
		if(switchWrapper != null) {
			logger.info("****************** ROLL BACK FOR OLA_CASH_IN ******************");
			saveTransaction(workFlowWrapper, SupplierProcessingStatusConstants.FAILED);
			switchWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
			rollback(switchWrapper, workFlowWrapper);
		}
		
		if(switchWrapperRecon != null) {
			logger.info("****************** ROLL BACK FOR OLA_CASH_IN_RECON ******************");
			saveTransaction(workFlowWrapper, SupplierProcessingStatusConstants.FAILED);
			switchWrapperRecon.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
			rollback(switchWrapperRecon, workFlowWrapper);
		}
		
		if(switchWrapperCore != null) {
			logger.info("****************** ROLL BACK FOR CASH_IN_CORE ******************");
			saveTransaction(workFlowWrapper, SupplierProcessingStatusConstants.FAILED);
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
	
	
	private void saveTransaction(WorkFlowWrapper _workFlowWrapper, Long supplierProcessingStatusId) throws FrameworkCheckedException {
		
		_workFlowWrapper.getTransactionModel().setSupProcessingStatusId(supplierProcessingStatusId);

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
						
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(_workFlowWrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			
		_workFlowWrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		  
		return _workFlowWrapper;
	  }
		
	  public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		  this.smartMoneyAccountManager = smartMoneyAccountManager;
	  }

}