package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.workflow.credittransfer.CreditTransferTransaction;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.util.VeriflyKeyConstantInterface;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiveCashTransaction extends CreditTransferTransaction {
	
	protected final Log log = LogFactory.getLog(getClass());
	private DateTimeFormatter dateFormat =  DateTimeFormat.forPattern("dd-MM-yyyy");
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
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private MiniTransactionDAO miniTransactionDAO;
	

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
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of ....");
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
		workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel(TransactionTypeConstantsInterface.RECEIVE_MONEY_TX));
		
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

			logger.debug("Ending validateCommission of ...");
		}

	}
	
	
	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		
		if(logger.isDebugEnabled())	{
			logger.debug("Inside getTransactionProcessingCharges of ....");
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
			
			logger.debug("Ending getTransactionProcessingCharges of ....");
		}
		
		return transProcessingAmount;
	}	
	
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.workflow.credittransfer.CreditTransferTransaction#doCreditTransfer(com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper)
	 */
	@Override
	public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper _workFlowWrapper) throws Exception {

		//Removed after otp validation removed by Sheheryaar
		//this.verifyOneTimePIN(_workFlowWrapper);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(_workFlowWrapper.getTransactionCodeModel());
		
		this.txManager.loadTransactionByTransactionCode(searchBaseWrapper);
		
 		TransactionModel transactionModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
 		TransactionDetailModel transactionDetailModel = null;
		
		_workFlowWrapper.setTransactionModel(transactionModel);						
		_workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
		_workFlowWrapper.getTransactionDetailMasterModel().setTransactionCodeId(_workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());


		_workFlowWrapper.getTransactionDetailMasterModel().setReceivingRegion(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionId());
		_workFlowWrapper.getTransactionDetailMasterModel().setReceivingRegionName(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionName());

		_workFlowWrapper.getTransactionDetailMasterModel().setReceiverAreaId(_workFlowWrapper.getRetailerContactModel().getAreaId());
		_workFlowWrapper.getTransactionDetailMasterModel().setReceiverAreaName(_workFlowWrapper.getRetailerContactModel().getAreaName());

		_workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorId(_workFlowWrapper.getDistributorModel().getDistributorId());
		_workFlowWrapper.getTransactionDetailMasterModel().setReceiverDistributorName(_workFlowWrapper.getDistributorModel().getName());
		if (null != _workFlowWrapper.getDistributorModel().getMnoId()) {
			_workFlowWrapper.getTransactionDetailMasterModel().setReceiverServiceOPId(_workFlowWrapper.getDistributorModel().getMnoId());
			_workFlowWrapper.getTransactionDetailMasterModel().setReceiverServiceOPName(_workFlowWrapper.getDistributorModel().getMnoModel().getName());
	//		_workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPName(_workFlowWrapper.getDistributorModel().getMnoModel().getName());
		}



		List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(transactionModel.getTransactionIdTransactionDetailModelList());
		transactionDetailModel = (transactionDetailModelList.get(0));
		_workFlowWrapper.setTransactionAmount(transactionModel.getTransactionAmount());	
		
		List<TransactionDetailModel> list = new ArrayList<TransactionDetailModel>(transactionModel.getTransactionIdTransactionDetailModelList());
		list.clear();

		transactionDetailModel.setSettled(Boolean.TRUE);
		transactionDetailModel.setActualBillableAmount(transactionModel.getTransactionAmount());
		list.add(transactionDetailModel);
		
		_workFlowWrapper.setTransactionDetailModel(transactionDetailModel);
		transactionModel.setTransactionIdTransactionDetailModelList(null);
		transactionModel.setTransactionIdTransactionDetailModelList(list);
		transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		transactionModel.setUpdatedOn(new Date());
		transactionModel.setToRetContactId(_workFlowWrapper.getAppUserModel().getRetailerContactId());

		_workFlowWrapper.setTransactionModel(transactionModel);
		_workFlowWrapper.setTransactionAmount(transactionModel.getTransactionAmount());
		_workFlowWrapper.setTransactionModel(transactionModel);
		
        createSMS(_workFlowWrapper, transactionDetailModel);
        createFundTransferEntries(_workFlowWrapper);
		
        BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(transactionModel);
		
		this.txManager.updateTransaction(baseWrapper);
		        
		_workFlowWrapper.setTransactionModel((TransactionModel)baseWrapper.getBasePersistableModel());	        
        
		if (logger.isDebugEnabled()) {
			
			logger.debug("Ending doSale of ReceiveCashTransaction.");
		}				
	
		return _workFlowWrapper;
	}
	
	

	private OLAVeriflyFinancialInstitutionImpl olaFinancialInstitution = null;
	
	private void doOLAFundTransfer(WorkFlowWrapper workFlowWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
		
		SwitchWrapper switchWrapper = workFlowWrapper.getSwitchWrapper();
		
		if(olaFinancialInstitution == null) {

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());
			
			olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		}
		
		olaFinancialInstitution.performLeg2(switchWrapper,
									ReasonConstants.RECEIVE_CASH,
									TransactionConstantsInterface.RECIEVE_CASH_CATEGORY_ID,
									Boolean.TRUE);
		
		String accountNumber = StringUtil.replaceString(switchWrapper.getAccountInfoModel().getAccountNo(), 5, "*");
		
		switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(accountNumber);
		switchWrapper.getWorkFlowWrapper().getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
//		List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionIdTransactionDetailModelList());
		
//		if(transactionDetailModelList != null && transactionDetailModelList.size() > 0) {
//
//			transactionDetailModelList.get(0).setCustomField2(accountNumber);
//		}
		
		workFlowWrapper.setOLASwitchWrapper(switchWrapper);			
	}
	

	/**
	 * @throws CommandException 
	 * @throws FrameworkCheckedException
	 */
	private MiniTransactionModel verifyOneTimePIN(WorkFlowWrapper _workFlowWrapper) throws CommandException {
				
		TransactionCodeModel transactionCodeModel = _workFlowWrapper.getTransactionCodeModel();
		
		MiniTransactionModel miniTransactionModel = null;
		
		try {
			
			miniTransactionModel = loadMiniTransaction(transactionCodeModel);
			
			if(miniTransactionModel != null) {
				
				long transactionStateId = miniTransactionModel.getMiniTransactionStateId().longValue();
				
				if(transactionStateId != MiniTransactionStateConstant.PIN_SENT.longValue()) {
					throw new CommandException(messageSource.getMessage("RECEIVE_CASH_FRAUD_DETECTED",null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
				}
				
				String inRecordOTP = miniTransactionModel.getOneTimePin();
				String enteredOTP = EncoderUtils.encodeToSha(_workFlowWrapper.getMPin());//TODO decode pin again;
				
				Boolean isEqualOTP = (inRecordOTP.equals(enteredOTP));
				
				if (!isEqualOTP) {//use entered Pin does not match the one sent to BBC

					throw new CommandException("Invalid transaction code provided.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
					
				} else {		
					
					miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.COMPLETED);
				}	

			} else {

				throw new CommandException(messageSource.getMessage("RECEIVE_CASH_FRAUD_DETECTED",null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
			}
			
		} catch (Exception e) {		
			e.printStackTrace();	
			throw new CommandException(e.getLocalizedMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
		} finally {
			
			if(miniTransactionModel != null) {

				miniTransactionModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId()) ;
				miniTransactionModel.setUpdatedOn(new Date()) ;
				this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);	
			}			
		}			
		
		return miniTransactionModel;
	}
	
	public MiniTransactionModel loadMiniTransaction(TransactionCodeModel transactionCodeModel) throws FrameworkCheckedException {
		
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);

		MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
		miniTransactionModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
		miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);

		List<MiniTransactionModel> resultList = miniTransactionDAO.searchMiniTransactionModel(miniTransactionModel);
		
		MiniTransactionModel _miniTransactionModel = null;
		
		if(resultList.size() == 1) {
			try{
				_miniTransactionModel = this.miniTransactionDAO.loadAccountAndLock(resultList.get(0));
			}catch(Exception ex){
				logger.error("Failed to loadAccountAndLock MiniTransaction",ex);
				throw new CommandException(messageSource.getMessage("RECEIVE_CASH_FRAUD_DETECTED",null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
			}
		}
		
		return _miniTransactionModel;
	}	
	
	
	/**
	 * @param _workFlowWrapper
	 * @throws FrameworkCheckedException
	 * @throws Exception
	 */
	private void checkBalance(WorkFlowWrapper _workFlowWrapper) throws FrameworkCheckedException, Exception {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
		
		OLAVeriflyFinancialInstitutionImpl olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		AccountInfoModel accountInfoModel = _workFlowWrapper.getAccountInfoModel();		
		
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

		CommissionAmountsHolder commissionAmountsHolder = commissionManager.loadCommissionDetailsUnsettled(_workFlowWrapper.getTransactionDetailMasterModel().getTransactionDetailId());
		_workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
		
		this.commissionManager.makeAgent2CommissionSettlement(_workFlowWrapper);
		settlementManager.updateCommissionTransactionSettled(_workFlowWrapper.getTransactionDetailMasterModel().getTransactionDetailId());

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());	
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setTransactionAmount(_workFlowWrapper.getTransactionModel().getTransactionAmount());
		switchWrapper.setAccountInfoModel(_workFlowWrapper.getAccountInfoModel());
		switchWrapper.setTransactionTypeId(_workFlowWrapper.getTransactionTypeModel().getTransactionTypeId());			
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		
		/*
		 *** 	SENDING TRANSFER IN TO OLA BANKING.
		 */				
		logger.info("OLA > SENDING RECEIVE CASH TO OLA BANKING.");
		
		this.doOLAFundTransfer(_workFlowWrapper);
		
		_workFlowWrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString("", 5, "*"));
	
		return _workFlowWrapper;
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

		String cnicReceiver = _workFlowWrapper.getTransactionDetailMasterModel().getRecipientCnic();
		String cnicSender = _workFlowWrapper.getTransactionDetailMasterModel().getSenderCnic();
		String recipientMobileNo = _workFlowWrapper.getTransactionDetailMasterModel().getRecipientMobileNo();
		String senderMobileNo = _workFlowWrapper.getSenderWalkinCustomerModel().getMobileNumber();
		String agent2MobileNo = _workFlowWrapper.getAppUserModel().getMobileNo();

		Boolean inclChargesCheck = _workFlowWrapper.getTransactionDetailMasterModel().getThirdPartyCheck();
		Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(_workFlowWrapper.getTransactionDetailMasterModel().getInclusiveCharges());
		if(inclChargesCheck != null && inclChargesCheck){
			totalInclusiveCharges = 0D;
		}

		Object[] smsParamsSender = new Object[] {brandName, 
				_workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode(), 
				Formatter.formatDouble(_workFlowWrapper.getTransactionDetailMasterModel().getTransactionAmount() - totalInclusiveCharges),
				(StringUtil.isNullOrEmpty(cnicReceiver) ? "" : cnicReceiver), 
				dateFormat.print(new DateTime()) , timeFormat.print(new LocalTime())};
		
		Object[] smsParamsAgent2 = new Object[] {brandName, 
				Formatter.formatDouble(_workFlowWrapper.getTransactionDetailMasterModel().getTransactionAmount() - totalInclusiveCharges),
				dateFormat.print(new DateTime()), timeFormat.print(new LocalTime()),
				_workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode()};

		String smsTextSender = this.messageSource.getMessage("RECEIVE_CASH_SENDER_LEG_2", smsParamsSender, null);
		String smsTextAgent2 = this.messageSource.getMessage("RECEIVE_CASH_AGENT2_LEG_2", smsParamsAgent2, null);
		messageList.add(new SmsMessage(senderMobileNo, smsTextSender));
		messageList.add(new SmsMessage(agent2MobileNo, smsTextAgent2));
		
		Object[] smsParamsReceiver = null;
		String smsTextReceiver = null;

		if(_workFlowWrapper.getProductModel().getServiceId() == ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() || 
				_workFlowWrapper.getProductModel().getServiceId() == ServiceConstantsInterface.BULK_DISB_ACC_HOLDER.longValue() ) {	
			
			smsParamsReceiver = new Object[] {brandName, 
					_workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode(), 
					Formatter.formatDouble(_workFlowWrapper.getTransactionDetailMasterModel().getTransactionAmount() - totalInclusiveCharges),
					dateFormat.print(new DateTime()) , timeFormat.print(new LocalTime())};
			
			smsTextReceiver = this.messageSource.getMessage("RECEIVE_CASH_RECEIVER_BULK_LEG_2", smsParamsReceiver, null);	
		
		} else {			
			
			smsParamsReceiver = new Object[] {brandName, 
					_workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode(), 
					Formatter.formatDouble(_workFlowWrapper.getTransactionDetailMasterModel().getTransactionAmount() - totalInclusiveCharges),
					(StringUtil.isNullOrEmpty(cnicSender) ? "" : cnicSender), 
					dateFormat.print(new DateTime()) , timeFormat.print(new LocalTime())};	
			
			smsTextReceiver = this.messageSource.getMessage("RECEIVE_CASH_RECEIVER_LEG_2", smsParamsReceiver, null);
		}
		
		messageList.add(new SmsMessage(recipientMobileNo, smsTextReceiver));
		
		_workFlowWrapper.getTransactionModel().setNotificationMobileNo(recipientMobileNo);
		_workFlowWrapper.getTransactionModel().setConfirmationMessage(smsTextSender);
		transactionDetailModel.setCustomField8(smsTextSender);
		transactionDetailModel.setCustomField4(smsTextAgent2);
		transactionDetailModel.setCustomField10(smsTextReceiver);
		if(_workFlowWrapper.getDeviceTypeModel() != null){
			transactionDetailModel.setCustomField13( "" + _workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
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
						
		return _workFlowWrapper;
	  }
		
	  public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		  this.smartMoneyAccountManager = smartMoneyAccountManager;
	  }
		public void setMiniTransactionDAO(MiniTransactionDAO miniTransactionDAO) {
			this.miniTransactionDAO = miniTransactionDAO;
		}

}