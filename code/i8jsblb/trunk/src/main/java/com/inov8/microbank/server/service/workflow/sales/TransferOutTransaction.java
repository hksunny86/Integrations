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
import com.inov8.microbank.common.exception.CommandException;
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
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
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
import com.inov8.verifly.common.util.VeriflyKeyConstantInterface;

public class TransferOutTransaction extends CreditTransferTransaction {

	private final String CORE_TRANSFER_OUT_SWITCH = "CORE_TRANSFER_OUT_SWITCH";
	
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
	public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of TransferOutTransaction.");
		}

		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
		workFlowWrapper.setSegmentModel(segmentModel);
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(workFlowWrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(workFlowWrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(workFlowWrapper.getProductModel());
		
		
	    workFlowWrapper.setTaxRegimeModel(workFlowWrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
		
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

			logger.debug("Ending validateCommission of TransferOutTransaction...");
		}

	}
	
	
	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		
		if(logger.isDebugEnabled())	{
			logger.debug("Inside getTransactionProcessingCharges of TransferOutTransaction....");
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
			
			logger.debug("Ending getTransactionProcessingCharges of TransferOutTransaction....");
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
		transactionModel.setPaymentModeId(_workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
		transactionModel.setTransactionTypeId(_workFlowWrapper.getTransactionTypeModel().getTransactionTypeId());
 		transactionModel.setFromRetContactId(_workFlowWrapper.getAppUserModel().getRetailerContactId());
//		transactionModel.setToRetContactId(_workFlowWrapper.getAppUserModel().getRetailerContactId());
		transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		transactionModel.setMfsId(_workFlowWrapper.getUserDeviceAccountModel().getUserId());
		transactionModel.setSmartMoneyAccountId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPrimaryKey());
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

 		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
		transactionDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
		transactionDetailModel.setSettled(Boolean.TRUE);
		transactionDetailModel.setConsumerNo(_workFlowWrapper.getAppUserModel().getMobileNo());
		transactionDetailModel.setSettled(Boolean.TRUE);
		transactionDetailModel.setCustomField1(String.valueOf(_workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId()));
		transactionDetailModel.setCustomField3("8");	
		
		this.computeCommission(_workFlowWrapper);
		
		transactionModel.setTotalCommissionAmount(_workFlowWrapper.getCommissionAmountsHolder().getTotalCommissionAmount());
		transactionDetailModel.setActualBillableAmount(_workFlowWrapper.getCommissionAmountsHolder().getBillingOrganizationAmount());
		_workFlowWrapper.setTransactionDetailModel(transactionDetailModel);

		_workFlowWrapper.getTransactionDetailMasterModel().setSendingRegion(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionId());
		_workFlowWrapper.getTransactionDetailMasterModel().setSendingRegionName(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionName());

		_workFlowWrapper.getTransactionDetailMasterModel().setSenderAreaId(_workFlowWrapper.getRetailerContactModel().getAreaId());
		_workFlowWrapper.getTransactionDetailMasterModel().setSenderAreaName(_workFlowWrapper.getRetailerContactModel().getAreaName());

		_workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorId(_workFlowWrapper.getDistributorModel().getDistributorId());
		_workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorName(_workFlowWrapper.getDistributorModel().getName());
		if (null != _workFlowWrapper.getDistributorModel().getMnoId()) {
			_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPId(_workFlowWrapper.getDistributorModel().getMnoId());
			_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPName(_workFlowWrapper.getDistributorModel().getMnoModel().getName());
		}
		
		Double totalCommissionAmount = 0.0D;
		
		if(_workFlowWrapper.getCommissionAmountsHolder().getTotalCommissionAmount() != null) {
			totalCommissionAmount = _workFlowWrapper.getCommissionAmountsHolder().getTotalCommissionAmount();
		}

		transactionModel.setTotalAmount(_workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount() + _workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() + _workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount());	
		transactionModel.setTotalCommissionAmount(totalCommissionAmount);
		_workFlowWrapper.setTransactionModel(transactionModel);
		
		// Set Handler Detail in Transaction and Transaction Detail Master
		if(_workFlowWrapper.getHandlerModel() != null) {
			_workFlowWrapper.getTransactionModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
			_workFlowWrapper.getTransactionModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
			_workFlowWrapper.getTransactionDetailMasterModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
			_workFlowWrapper.getTransactionDetailMasterModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
		}
		
		if(transferInDispenser == null) {
			
			transferInDispenser = (TransferInDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
		}		
			
		AccountInfoModel accountInfoModelCore = 
				transferInDispenser.getAccountInfoModelBySmartMoneyAccount(_workFlowWrapper.getSmartMoneyAccountModel(), 
							ThreadLocalAppUser.getAppUserModel().getAppUserId(), transactionModel.getTransactionCodeId());
		
		_workFlowWrapper.getTransactionDetailModel().setCustomField2(accountInfoModelCore.getAccountNo());

//		this.titleFetch(_workFlowWrapper, accountInfoModelCore);
		
		this.checkOLABalance(_workFlowWrapper);
		
		this.transferOLA(_workFlowWrapper);	
						
		this.transferCore(_workFlowWrapper, accountInfoModelCore);
		
		this.sendSMS(_workFlowWrapper, accountInfoModelCore);
		
		transactionModel.addTransactionIdTransactionDetailModel(transactionDetailModel);
		_workFlowWrapper.setTransactionModel(transactionModel);
			
		transactionModel = saveTransaction(_workFlowWrapper, transactionModel.getSupProcessingStatusId()); // save the transaction
		
		
		SETTLE_COMMISSION_BLOCK : {
		
			if (logger.isDebugEnabled()) {				
				logger.debug("Going to settle commissions using SettlementManager....");
			}
			
			this.settlementManager.settleCommission(_workFlowWrapper.getCommissionWrapper(), _workFlowWrapper);
		}
		
		if (logger.isDebugEnabled()) {
			
			logger.debug("Ending doSale of TransferOutTransaction.");
		}

		_workFlowWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, _workFlowWrapper.getProductModel().getProductId().toString());
		_workFlowWrapper.putObject(CommandFieldConstants.KEY_NAME, _workFlowWrapper.getProductModel().getName());
		_workFlowWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, Formatter.formatDouble(transactionModel.getTransactionAmount()));
		_workFlowWrapper.putObject(CommandFieldConstants.KEY_CAMT, Formatter.formatDouble(transactionModel.getTotalCommissionAmount()));
		_workFlowWrapper.putObject(CommandFieldConstants.KEY_TAMT, Formatter.formatDouble(transactionModel.getTotalAmount()));

		return _workFlowWrapper;
	}

	
	/**
	 * @param _workFlowWrapper
	 * @param accountInfoModelCore
	 */
	void sendSMS(WorkFlowWrapper _workFlowWrapper, AccountInfoModel accountInfoModelCore) {

		Double olaBalance = (Double) _workFlowWrapper.getObject("OLA_BALANCE");
		
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);


		String brandName=null;
		if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
			brandName=MessageUtil.getMessage("sco.brandName");

		}else {

			brandName= MessageUtil.getMessage("jsbl.brandName");
		}

		
		if(_workFlowWrapper.getHandlerModel() == null  ||
				(_workFlowWrapper.getHandlerModel() != null && _workFlowWrapper.getHandlerModel().getSmsToAgent())){
			
			Object[] smsParams = new Object[] {brandName, 
					_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(),
					Formatter.formatDouble(_workFlowWrapper.getTransactionModel().getTransactionAmount()),
					timeFormat.print(new LocalTime()),
					dateTimeFormat.print(new DateTime()),
					accountInfoModelCore.getAccountNo(),
					olaBalance
					};		

			String agentSMS = this.messageSource.getMessage("TransferOutPayment.SMS.Notification", smsParams, null);
								
			_workFlowWrapper.getTransactionModel().setNotificationMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
			_workFlowWrapper.getTransactionModel().setConfirmationMessage(agentSMS);
			_workFlowWrapper.getTransactionDetailModel().setCustomField4(agentSMS);

			messageList.add(new SmsMessage(ThreadLocalAppUser.getAppUserModel().getMobileNo(), agentSMS));
			
		}
		
		if(_workFlowWrapper.getHandlerModel() != null && _workFlowWrapper.getHandlerModel().getSmsToHandler()){

			Object[] smsParams = new Object[] {
									(String) _workFlowWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT),
									_workFlowWrapper.getAccountInfoModel().getAccountNick().toString(),
									accountInfoModelCore.getAccountNick().toString(),
									dateTimeFormat.print(new DateTime()) +" "+ timeFormat.print(new LocalTime()), 
									(_workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() + _workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount()),
									_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode()};			

			String handlerSMS = this.messageSource.getMessage("TransferOutPayment.SMS.Notification", smsParams, null);
							
			messageList.add(new SmsMessage(_workFlowWrapper.getHandlerAppUserModel().getMobileNo(), handlerSMS));				
		}

		_workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);		
	}
	
	
	/**
	 * @param _workFlowWrapper
	 * @throws Exception
	 */
	void computeCommission(WorkFlowWrapper _workFlowWrapper) throws Exception {

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
		_workFlowWrapper.setCommissionWrapper(commissionWrapper);
	}	
	

	/**
	 * @param _workFlowWrapper
	 * @param accountInfoModelCore
	 * @throws WorkFlowException
	 * @throws FrameworkCheckedException
	 */
	void titleFetch(WorkFlowWrapper _workFlowWrapper, AccountInfoModel accountInfoModelCore) throws WorkFlowException, FrameworkCheckedException {		

		SwitchWrapper switchWrapperFetchTitle = new SwitchWrapperImpl();

		WorkFlowWrapper workFlowWrapperFetchTitle = new WorkFlowWrapperImpl();
		workFlowWrapperFetchTitle.setTransactionCodeModel(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel());
		workFlowWrapperFetchTitle.setProductModel(_workFlowWrapper.getProductModel());
		workFlowWrapperFetchTitle.setTransactionDetailMasterModel(_workFlowWrapper.getTransactionDetailMasterModel());
		
		switchWrapperFetchTitle.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapperFetchTitle.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		switchWrapperFetchTitle.setAccountInfoModel(accountInfoModelCore);
		switchWrapperFetchTitle.setFromAccountNo(accountInfoModelCore.getAccountNo());
		switchWrapperFetchTitle.setBankId(_workFlowWrapper.getSmartMoneyAccountModel().getBankId());
		switchWrapperFetchTitle.setWorkFlowWrapper(workFlowWrapperFetchTitle);
		switchWrapperFetchTitle = phoenixFinancialInstitution.titleFetch(switchWrapperFetchTitle);
		_workFlowWrapper.getTransactionDetailModel().setCustomField2(accountInfoModelCore.getAccountNo());
	}
	
	/**
	 * @param _workFlowWrapper
	 * @throws Exception 
	 */
	void checkOLABalance(WorkFlowWrapper _workFlowWrapper) throws Exception {

		AccountInfoModel accountInfoModel = _workFlowWrapper.getAccountInfoModel();
		
		Double olaBalance = 0.0D ;
		AccountInfoModel olaAccountInfoModel = null;
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());
		
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		SwitchWrapper _switchWrapper = new SwitchWrapperImpl();

		_switchWrapper.putObject(CommandFieldConstants.KEY_PIN, accountInfoModel.getOldPin());
		_switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		_switchWrapper.setAccountInfoModel(accountInfoModel);;
		_switchWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
		_switchWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel() ) ;	
		_switchWrapper.putObject(VeriflyKeyConstantInterface.skipCheckPin, Boolean.TRUE);
		
		abstractFinancialInstitution.checkBalance(_switchWrapper);
		olaBalance = _switchWrapper.getBalance();
				
		if(olaBalance < _workFlowWrapper.getTransactionModel().getTotalAmount()) {
			
			throw new CommandException("Branchless Bank Balance > "+messageSource.getMessage("MINI.InsufficientBalance", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
		
		olaAccountInfoModel = _switchWrapper.getAccountInfoModel();
		olaAccountInfoModel.setOldPin(_workFlowWrapper.getAccountInfoModel().getOldPin());
		
		_workFlowWrapper.setAccountInfoModel(olaAccountInfoModel);
		_workFlowWrapper.getTransactionModel().setBankAccountNo(_switchWrapper.getAccountInfoModel().getAccountNo());
	}	
	
	
	
	/**
	 * @param _workFlowWrapper
	 * @throws Exception
	 */
	void transferOLA(WorkFlowWrapper _workFlowWrapper) throws Exception {
		
		StakeholderBankInfoModel iftPoolAccountStakeHolder = this.getStakeholderBankInfoModel(PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID);
		
		String agentBBAccount = _workFlowWrapper.getAccountInfoModel().getAccountNo();
		String iftPoolAccount = iftPoolAccountStakeHolder.getAccountNo();
        
		OLAVO olaVO = new OLAVO();
		List<OLAInfo> debitList = new ArrayList<OLAInfo>(0);
        List<OLAInfo> creditList = new ArrayList<OLAInfo>(0);		

		Boolean isThirdPartyIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
		isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;	
		
		Double agent1CommissionAmount = _workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);		

		OLAInfo agentAccountDebitFT = new OLAInfo();													//	FT 1 
		agentAccountDebitFT.setReasonId(ReasonConstants.TRANSFER_OUT);
		agentAccountDebitFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		agentAccountDebitFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
		agentAccountDebitFT.setCustomerAccountTypeId(_workFlowWrapper.getRetailerContactModel().getOlaCustomerAccountTypeModelId());
		agentAccountDebitFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
		agentAccountDebitFT.setPayingAccNo(agentBBAccount);
		agentAccountDebitFT.setReceivingAccNo(agentBBAccount);
		agentAccountDebitFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		agentAccountDebitFT.setIsAgent(Boolean.TRUE);
			
	    if((_workFlowWrapper.getHandlerModel() != null)){
	       	agentAccountDebitFT.setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
	       	agentAccountDebitFT.setHandlerAccountTypeId(_workFlowWrapper.getHandlerModel().getAccountTypeId());
	    }			
	        
		agentAccountDebitFT.setBalance(
					(_workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount() + _workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() + 
							_workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount()) - agent1CommissionAmount);	

			
		if(!StringUtil.isNullOrEmpty(agentBBAccount)) {

			debitList.add(agentAccountDebitFT);	
		}
			
		Double iftAmount = _workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount();
		Double inclusiveAmount = _workFlowWrapper.getCommissionAmountsHolder().getInclusiveFixAmount() + _workFlowWrapper.getCommissionAmountsHolder().getInclusivePercentAmount();			

		Boolean isInclusiveAmountApplied = (inclusiveAmount != null && inclusiveAmount > 0D) ? Boolean.TRUE : Boolean.FALSE;

		if(!isThirdPartyIncluded && isInclusiveAmountApplied) {
				
			iftAmount -= (inclusiveAmount);
			
		}					
					
		OLAInfo iftPoolAccountCreditFT = new OLAInfo();													//	FT 2
		iftPoolAccountCreditFT.setReasonId(ReasonConstants.TRANSFER_OUT);
		iftPoolAccountCreditFT.setBalanceAfterTrxRequired(Boolean.TRUE);
		iftPoolAccountCreditFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
		iftPoolAccountCreditFT.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
		iftPoolAccountCreditFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
		iftPoolAccountCreditFT.setReceivingAccNo(iftPoolAccount);
		iftPoolAccountCreditFT.setPayingAccNo(iftPoolAccount);
		iftPoolAccountCreditFT.setBalance(iftAmount);
			
		if(!StringUtil.isNullOrEmpty(iftPoolAccount) && iftPoolAccountCreditFT.getBalance() != null) {

			creditList.add(iftPoolAccountCreditFT);	
		}
						
		olaVO.setCreditAccountList(creditList);
		olaVO.setDebitAccountList(debitList);			
		
		_workFlowWrapper.setTransactionAmount(iftAmount);
			
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();			
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());	
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setTransactionAmount(_workFlowWrapper.getTransactionModel().getTransactionAmount() + _workFlowWrapper.getTransactionModel().getTotalCommissionAmount());
		switchWrapper.setOlavo(olaVO);
			
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		
		/*
			 *** 	SENDING TRANSFER OUT TO OLA BANKING.
		 */				
		logger.info("OLA > SENDING TRANSFER OUT TO OLA BANKING.");
						
		_workFlowWrapper = transferInDispenser.doSale(_workFlowWrapper);
			
		Double olaBalance = _workFlowWrapper.getSwitchWrapper().getOlavo().getFromBalanceAfterTransaction();
		_workFlowWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER, agentBBAccount);
		_workFlowWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB, iftPoolAccount);
		_workFlowWrapper.putObject("OLA_BALANCE", olaBalance);
	}
	

	/**
	 * @param _workFlowWrapper
	 * @throws Exception
	 */
	void transferCore(WorkFlowWrapper _workFlowWrapper, AccountInfoModel accountInfoModelCore) throws Exception {

		Double amount = _workFlowWrapper.getTransactionModel().getTransactionAmount();
		
		Boolean isThirdPartyIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
		isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;			
		
		CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();
		Double inclusiveAmount = commissionAmountsHolder.getInclusiveFixAmount() + commissionAmountsHolder.getInclusivePercentAmount();

		Boolean isInclusiveAmountApplied = (inclusiveAmount != null && inclusiveAmount > 0D) ? Boolean.TRUE : Boolean.FALSE;		

		if(isInclusiveAmountApplied || isThirdPartyIncluded) {
			
			amount -= inclusiveAmount;
		}
		
		amount = _workFlowWrapper.getTransactionAmount();
				
		accountInfoModelCore.setOldPin(_workFlowWrapper.getAccountInfoModel().getOldPin());
		
		StakeholderBankInfoModel inwardFundTransferStakeholderBankInfoModel = new StakeholderBankInfoModel();
		inwardFundTransferStakeholderBankInfoModel.setStakeholderBankInfoId(PoolAccountConstantsInterface.INWARD_FUND_TRANSFER_ACCOUNT_ID);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(inwardFundTransferStakeholderBankInfoModel);
		
		searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
		
		inwardFundTransferStakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
					
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();

		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		switchWrapper.setAccountInfoModel(accountInfoModelCore);
		switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);		
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
		switchWrapper.setMiddlewareIntegrationMessageVO(new MiddlewareMessageVO());
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setTransactionAmount(amount);
		
		switchWrapper.setFromAccountNo(inwardFundTransferStakeholderBankInfoModel.getAccountNo());	
		switchWrapper.setFromAccountType("20");
		switchWrapper.setFromCurrencyCode("586");			
		
		switchWrapper.setToAccountNo(accountInfoModelCore.getAccountNo());
		switchWrapper.setToAccountType("20");
		switchWrapper.setToCurrencyCode("586");

		_workFlowWrapper.setAccountInfoModel(accountInfoModelCore);
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.TRANSFER_OUT_CORE);

		/*
		 *** 	SENDING TRANSFER OUT TO CORE BANKING.
		 */				
		logger.info("JS Core > SENDING TRANSFER OUT TO PHOENIX.");
		logger.info("JS Core > FROM ACCOUNT : "+switchWrapper.getFromAccountNo());
		logger.info("JS Core > TO ACCOUNT : "+switchWrapper.getToAccountNo());
		logger.info("JS Core > AMOUNT : "+switchWrapper.getTransactionAmount());
		
		_workFlowWrapper.putObject(CORE_TRANSFER_OUT_SWITCH, switchWrapper);
		
		switchWrapper = phoenixFinancialInstitution.creditAccountAdvice(switchWrapper);
		
		String bankResponseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();
		
		_workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper); // for day end O.F. settlement of Core FT
						
		_workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
		_workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
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

		return workFlowWrapper;
	}
	
	
	private TransactionModel saveTransaction(WorkFlowWrapper _workFlowWrapper, Long supplierProcessingStatusId) throws FrameworkCheckedException {
		
		_workFlowWrapper.getTransactionModel().setSupProcessingStatusId(supplierProcessingStatusId);

		txManager.transactionRequiresNewTransaction(_workFlowWrapper);	
		
		return _workFlowWrapper.getTransactionModel();
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
	  protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper)throws Exception {
		  
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
//				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//				RetailerContactModel retailerContact = new RetailerContactModel();
//				retailerContact.setRetailerContactId( _workFlowWrapper.getHandlerModel().getRetailerContactId() );
//				searchBaseWrapper.setBasePersistableModel( retailerContact );
//				searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
//				_workFlowWrapper.setHandlerRetContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());

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

		public void setSmartMoneyAccountManager(
				SmartMoneyAccountManager smartMoneyAccountManager) {
			this.smartMoneyAccountManager = smartMoneyAccountManager;
		}

}