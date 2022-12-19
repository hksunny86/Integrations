package com.inov8.microbank.server.service.workflow.sales;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

import java.util.ArrayList;
import java.util.Date;
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
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SegmentModel;
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
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
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
import com.inov8.verifly.common.util.VeriflyKeyConstantInterface;

public class TellerCashOutTransaction extends CreditTransferTransaction {

	private final String TELLER_TRANSFER_OUT_SWITCH = "TELLER_TRANSFER_OUT_SWITCH";
	
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
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of TellerCashOutCommand....");
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
					
		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
		transactionModel.setTransactionAmount(workFlowWrapper.getTransactionAmount());

		workFlowWrapper.setSegmentModel(new SegmentModel(segmentId));
		workFlowWrapper.setTransactionModel(transactionModel);
		workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel(TransactionTypeConstantsInterface.Teller_CASH_IN_TX));
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(workFlowWrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(workFlowWrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(workFlowWrapper.getProductModel());
		/*
		if(ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() == workFlowWrapper.getProductModel().getProductId().longValue() && 
				userAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()|| 
						userAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
			
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerIdRetailerModel(new RetailerModel());		
			workFlowWrapper.setRetailerContactModel(retailerContactModel);
		}		
		*/
		
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

			logger.debug("Ending validateCommission of TellerCashOutCommand...");
		}

	}
	
	
	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		
		if(logger.isDebugEnabled())	{
			logger.debug("Inside getTransactionProcessingCharges of TellerCashOutCommand....");
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
			
			logger.debug("Ending getTransactionProcessingCharges of TellerCashOutCommand....");
		}
		
		return transProcessingAmount;
	}	
	
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.workflow.credittransfer.CreditTransferTransaction#doCreditTransfer(com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper)
	 */
	@Override
	public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper _workFlowWrapper) throws Exception {
 		
		Boolean isIvrResponse = _workFlowWrapper.getIsIvrResponse();

		if(!isIvrResponse) {
			
			_workFlowWrapper = preIVRCreditTransfer(_workFlowWrapper);
		
		} else {
			
			postIVRCreditTransfer(_workFlowWrapper);
		}
		
		return _workFlowWrapper;
	}
	

	
	/**
	 * @param _workFlowWrapper
	 * @throws Exception
	 */
	public WorkFlowWrapper preIVRCreditTransfer(WorkFlowWrapper _workFlowWrapper) throws Exception {

		CommissionWrapper commissionWrapper =  this.getCommissionWrapper(_workFlowWrapper);

		CommissionAmountsHolder commissionAmountsHolder = 
				(CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		
 		TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();
		transactionModel.setConfirmationMessage(" _ ");
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
		transactionModel.setCreatedBy(_workFlowWrapper.getAppUserModel().getAppUserId());
		
 		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
		transactionDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
		transactionDetailModel.setConsumerNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionDetailModel.setCustomField1(String.valueOf(_workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId()));
		transactionDetailModel.setCustomField3("8");		
		transactionDetailModel.setSettled(Boolean.FALSE);
		transactionDetailModel.setCustomField5(_workFlowWrapper.getReceiverAppUserModel().getNic());
		transactionDetailModel.setCustomField8(_workFlowWrapper.getReceiverAppUserModel().getMobileNo());
		
		transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING);
		transactionModel.setTotalAmount(0d);
		transactionModel.setTotalCommissionAmount(0d);
		transactionModel.setTransactionCodeIdTransactionCodeModel(_workFlowWrapper.getTransactionCodeModel());

		transactionModel.setTransactionAmount(commissionAmountsHolder.getTransactionAmount());
		transactionModel.setTotalAmount(transactionModel.getTransactionAmount() + commissionAmountsHolder.getExclusiveFixAmount() + commissionAmountsHolder.getExclusivePercentAmount());
		transactionModel.setTotalCommissionAmount(commissionAmountsHolder.getTotalCommissionAmount());
		
		_workFlowWrapper.setTransactionDetailModel(transactionDetailModel);
		
		transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);
		
		_workFlowWrapper.setTransactionModel(transactionModel);
		
		txManager.saveTransaction(_workFlowWrapper);	
		
		checkBalance(_workFlowWrapper);
		
		UserDeviceAccountsModel deviceAccountsModel = new UserDeviceAccountsModel();
		deviceAccountsModel.setUserDeviceAccountsId(-1L);
		ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(deviceAccountsModel);	
		
		logger.info(_workFlowWrapper.getTransactionCodeModel().getCode());
		
//		return makeIvrRequest(_workFlowWrapper);
		return _workFlowWrapper;
	}
	
	

	/**
	 * @param _workFlowWrapper
	 * @throws Exception
	 */
	public void postIVRCreditTransfer(WorkFlowWrapper _workFlowWrapper) throws Exception {
		
		if(tellerCashInDispenser == null) {
			
			tellerCashInDispenser = (TellerCashInDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
		}
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(_workFlowWrapper.getTransactionCodeModel());
		
		this.txManager.loadTransactionByTransactionCode(searchBaseWrapper);
		
		TransactionModel transactionModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
		
		_workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
		_workFlowWrapper.getTransactionDetailMasterModel().setTransactionCodeId(_workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
		
		List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(transactionModel.getTransactionIdTransactionDetailModelList());
		TransactionDetailModel transactionDetailModel = (transactionDetailModelList.get(0));
		_workFlowWrapper.setTransactionAmount(transactionModel.getTransactionAmount());	
		
		List<TransactionDetailModel> list = new ArrayList<TransactionDetailModel>(transactionModel.getTransactionIdTransactionDetailModelList());
		list.clear();
//		transactionModel.setTransactionCodeId(_workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
		transactionDetailModel.setSettled(Boolean.TRUE);
		transactionDetailModel.setActualBillableAmount(transactionModel.getTransactionAmount());
		list.add(transactionDetailModel);

		_workFlowWrapper.setTransactionDetailModel(transactionDetailModel);
		transactionModel.setTransactionIdTransactionDetailModelList(null);
		transactionModel.setTransactionIdTransactionDetailModelList(list);
		transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		transactionModel.setCreatedOn(new Date());
		transactionModel.setDiscountAmount(0D);
		transactionModel.setProcessingBankId(_workFlowWrapper.getBankModel().getBankId());
		transactionModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);

		_workFlowWrapper.setTransactionModel(transactionModel);
		
		CommissionWrapper commissionWrapper = this.getCommissionWrapper(_workFlowWrapper);
		CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		
		_workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);	
		_workFlowWrapper.setTransactionModel(transactionModel);
		
        createSMS(_workFlowWrapper, transactionDetailModel);
        createFundTransferEntries(_workFlowWrapper);
        createCoreTransferEntries(_workFlowWrapper);
		
		SETTLE_COMMISSION_BLOCK : {
		
			if (logger.isDebugEnabled()) {				
				logger.debug("Going to settle commissions using SettlementManager....");
			}
			
			this.settlementManager.settleCommission(commissionWrapper, _workFlowWrapper);
		}

		
        BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(transactionModel);
		
		this.txManager.updateTransaction(baseWrapper);
        
		_workFlowWrapper.setTransactionModel((TransactionModel)baseWrapper.getBasePersistableModel());	        
        
		if (logger.isDebugEnabled()) {
			
			logger.debug("Ending doSale of TellerCashOutCommand.");
		}				
	}

	
	
	/**
	 * @param _workFlowWrapper
	 * @return
	 * @throws Exception
	 */
	private CommissionWrapper getCommissionWrapper(WorkFlowWrapper _workFlowWrapper) throws Exception {
		
	CommissionAmountsHolder commissionAmountsHolder = null;
	CommissionWrapper commissionWrapper = null;
	Double agent1CommissionAmount = 0.0D;
	Double franchise1CommissionAmount = 0.0D;

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
		
		return commissionWrapper;
	}	
	
	
	
	/**
	 * @param _workFlowWrapper
	 * @throws FrameworkCheckedException
	 * @throws Exception
	 */
	private void checkBalance(WorkFlowWrapper _workFlowWrapper) throws FrameworkCheckedException, Exception {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());

		AccountInfoModel accountInfoModel = _workFlowWrapper.getAccountInfoModel();	
		
		OLAVeriflyFinancialInstitutionImpl olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);	
		
		SwitchWrapper _switchWrapper = new SwitchWrapperImpl();
		_switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		_switchWrapper.setAccountInfoModel(accountInfoModel);
		_switchWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
		_switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel() ) ;	
		_switchWrapper.putObject(VeriflyKeyConstantInterface.skipCheckPin, Boolean.TRUE);
		_switchWrapper.putObject(CommandFieldConstants.KEY_PIN, accountInfoModel.getPin());

		olaFinancialInstitution.checkBalanceWithoutPin(_switchWrapper);	
			
		Double olaBalance = _switchWrapper.getBalance();
			
		log.info("OLAB : "+ olaBalance);
			
		if(olaBalance < _workFlowWrapper.getTransactionModel().getTotalAmount()) {
				
			throw new CommandException("Branchless Bank Balance > "+messageSource.getMessage("MINI.InsufficientBalance", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
	}
	
	
	/**
	 * @param _workFlowWrapper
	 * @param commissionAmountsHolder
	 * @return
	 * @throws Exception
	 */
	private WorkFlowWrapper createFundTransferEntries(WorkFlowWrapper _workFlowWrapper) throws Exception {
        
		OLAVO olaVO = new OLAVO();
		List<OLAInfo> debitList = new ArrayList<OLAInfo>(0);
        List<OLAInfo> creditList = new ArrayList<OLAInfo>(0);			
		
        CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();
        
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
		settlementAccountDebitFT.setReasonId(ReasonConstants.TELLER_CASH_OUT);
		settlementAccountDebitFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		settlementAccountDebitFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionCodeModel().getCode());
		settlementAccountDebitFT.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
		settlementAccountDebitFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
		settlementAccountDebitFT.setReceivingAccNo(iftPoolAccount);
		settlementAccountDebitFT.setPayingAccNo(iftPoolAccount);
		settlementAccountDebitFT.setBalance(commissionAmountsHolder.getTransactionAmount() + totalExclusiveCharges);	
		
		if(!StringUtil.isNullOrEmpty(iftPoolAccount) && settlementAccountDebitFT.getBalance() != null && settlementAccountDebitFT.getBalance() > 0 ) {

			creditList.add(settlementAccountDebitFT);	
		}

		OLAInfo agentCustomerAccountCreditFT = new OLAInfo();													//	FT 2 
		agentCustomerAccountCreditFT.setReasonId(ReasonConstants.TELLER_CASH_OUT);
		agentCustomerAccountCreditFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionCodeModel().getCode());
		agentCustomerAccountCreditFT.setCustomerAccountTypeId(accountTypeId);
		agentCustomerAccountCreditFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
		agentCustomerAccountCreditFT.setPayingAccNo(customerAgentBBAccount);
		agentCustomerAccountCreditFT.setReceivingAccNo(customerAgentBBAccount);
		agentCustomerAccountCreditFT.setBalance((commissionAmountsHolder.getTransactionAmount())+(totalInclusiveCharges));	
		agentCustomerAccountCreditFT.setAgentBalanceAfterTrxRequired(Boolean.TRUE);
		agentCustomerAccountCreditFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		agentCustomerAccountCreditFT.setIsAgent(Boolean.TRUE);
		
		if(!StringUtil.isNullOrEmpty(customerAgentBBAccount) && agentCustomerAccountCreditFT.getBalance() != null && agentCustomerAccountCreditFT.getBalance() > 0 ) {

			debitList.add(agentCustomerAccountCreditFT);	
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
		
		_workFlowWrapper.setOLASwitchWrapper(switchWrapper);
		
		/*
		 *** 	SENDING TRANSFER IN TO OLA BANKING.
		 */				
		logger.info("OLA > SENDING TELLER CASH OUT TO OLA BANKING.");
					
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
		
		TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();

        CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();

		String tellerIdentityNumber = _workFlowWrapper.getAppUserModel().getTellerId();
		
		StakeholderBankInfoModel inwardFundTransferStakeholderBankInfoModel = new StakeholderBankInfoModel();
		inwardFundTransferStakeholderBankInfoModel.setStakeholderBankInfoId(PoolAccountConstantsInterface.INWARD_FUND_TRANSFER_ACCOUNT_ID);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(inwardFundTransferStakeholderBankInfoModel);
		
		searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
		
		inwardFundTransferStakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
					
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.putObject(VeriflyKeyConstantInterface.skipCheckPin, Boolean.TRUE);
		switchWrapper.putObject(CommandFieldConstants.KEY_PIN, _workFlowWrapper.getAccountInfoModel().getPin());
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		switchWrapper.setAccountInfoModel(_workFlowWrapper.getAccountInfoModel());
		switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);		
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
		switchWrapper.setMiddlewareIntegrationMessageVO(new MiddlewareMessageVO());
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setTransactionAmount(transactionModel.getTransactionAmount() - (commissionAmountsHolder.getInclusiveFixAmount() + commissionAmountsHolder.getInclusivePercentAmount()));
		
		switchWrapper.setFromAccountNo(inwardFundTransferStakeholderBankInfoModel.getAccountNo());	
		switchWrapper.setFromAccountType("20");
		switchWrapper.setFromCurrencyCode("586");			
		
		switchWrapper.setToAccountNo(tellerIdentityNumber);
		switchWrapper.setToAccountType("20");
		switchWrapper.setToCurrencyCode("586");

		AppUserModel bankAppUserModel = _workFlowWrapper.getAppUserModel();
		
		WorkFlowWrapper workFlowWrapper = _workFlowWrapper;
		workFlowWrapper.setAppUserModel(_workFlowWrapper.getReceiverAppUserModel());
		workFlowWrapper.setTransactionModel(_workFlowWrapper.getTransactionModel());
		workFlowWrapper.setTransactionCodeModel(_workFlowWrapper.getTransactionCodeModel());
		workFlowWrapper.setProductModel(_workFlowWrapper.getProductModel());
		
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.TRANSFER_OUT_CORE);

		/*
		 *** 	SENDING TELLER CASH OUT TO CORE BANKING.
		 */				
		logger.info("JS Core > SENDING TELLER CASH OUT TO CORE BANKING.");
		logger.info("JS Core > FROM ACCOUNT : "+switchWrapper.getFromAccountNo());
		logger.info("JS Core > TO ACCOUNT : "+switchWrapper.getToAccountNo());
		logger.info("JS Core > AMOUNT : "+switchWrapper.getTransactionAmount());
		
		_workFlowWrapper.putObject(TELLER_TRANSFER_OUT_SWITCH, switchWrapper);
		
		switchWrapper = phoenixFinancialInstitution.creditAccountAdvice(switchWrapper);
		
		String bankResponseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();
		
		workFlowWrapper.setAppUserModel(bankAppUserModel);
		
		_workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper); // for day end O.F. settlement of Core FT
		_workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
	}	
	
	
	/**
	 * @param _workFlowWrapper
	 * @param transactionDetailModel
	 */
	private void createSMS(WorkFlowWrapper _workFlowWrapper, TransactionDetailModel transactionDetailModel) {
		

		String brandName = null;

		if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
			brandName = MessageUtil.getMessage("sco.brandName");
		}
		else {
			brandName = MessageUtil.getMessage("jsbl.brandName");
		}
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
					
		Object[] smsParams = new Object[] {brandName, 
				_workFlowWrapper.getTransactionCodeModel().getCode(), 
				Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()),
				_workFlowWrapper.getReceiverAppUserModel().getMobileNo(), 
				Formatter.formatNumbers(_workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
				timeFormat.print(new DateTime()), 
				dateFormat.print(new DateTime())};			
			
		String smsText = this.messageSource.getMessage("TELLER.TRNAS_OUT_SMS", smsParams, null);
		String notificationMobileNo = _workFlowWrapper.getReceiverAppUserModel().getMobileNo();

		messageList.add(new SmsMessage(notificationMobileNo, smsText));

		_workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);
		_workFlowWrapper.getTransactionModel().setConfirmationMessage(smsText);
		transactionDetailModel.setCustomField4(smsText);
		
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