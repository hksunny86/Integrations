package com.inov8.microbank.server.service.commandmodule;

import java.text.SimpleDateFormat;
import java.util.List;

import com.inov8.microbank.common.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.RemittanceTypeConstant;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

public class ReceiveCashCommand extends BaseCommand  {
	
	protected final Log logger = LogFactory.getLog(ReceiveCashCommand.class);

	private String productId;
	private String mobileNo;
	private String deviceTypeId;
	private String walkInCustomerCNIC;
	private String walkInReceiverCNIC;
	private String walkInCustomerMobileNumber;
	private String transactionAmount;
	private String transactionCode;
	private String transactionPin;
	private String pin;
	private String encryption_type;

	private SmartMoneyAccountModel olaSmartMoneyAccountModel;
	private AccountInfoModel accountInfoModel;
	private AppUserModel appUserModel;
	private TransactionModel transactionModel;
	private ProductModel productModel;
	private UserDeviceAccountsModel userDeviceAccountsModel; 
	private RetailerContactModel retailerContactModel = null;
	private CustomerModel customerModel = null;
	private WorkFlowWrapper workFlowWrapper;
	private CommonCommandManager commonCommandManager = null;



	private TransactionCodeModel transactionCodeModel = null;
	private MiniTransactionModel miniTransactionModel = null;	
	private TransactionDetailMasterModel transactionDetailMasterModel = null;
	
	private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");	
	
	private BaseWrapper preparedBaseWrapper = null;
	private String isReceiverBvsRequired;

	
	@Override
	public void execute() throws CommandException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of ReceiveMoneyCommand.execute()");
		}
		
		commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();
		
		try {

			this.olaSmartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

			accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

			retailerContactModel = getRetailerContactModel(appUserModel);
			
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel(new TransactionCodeModel(transactionCode));
			bWrapper = commonCommandManager.loadTransactionCodeByCode(bWrapper);
			transactionCodeModel = ((TransactionCodeModel)(bWrapper.getBasePersistableModel()));			
				
			transactionModel = new TransactionModel();

			transactionDetailMasterModel = new TransactionDetailMasterModel();
			transactionDetailMasterModel.setTransactionCodeId(this.transactionCodeModel.getTransactionCodeId());
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(transactionDetailMasterModel);
			
			commonCommandManager.loadTransactionDetailMaster(searchBaseWrapper);		
			
			transactionDetailMasterModel = (TransactionDetailMasterModel) searchBaseWrapper.getBasePersistableModel();
			
//			if(transactionDetailMasterModel == null) {
//				throw new CommandException("Transaction ID does not exists against the Recipient CNIC/mobile number.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
//			}
//			if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.COMPLETED.longValue()){
//				throw new CommandException("Transaction ID already claimed.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
//			}
//			if(transactionDetailMasterModel.getSupProcessingStatusId()==SupplierProcessingStatusConstants.FAILED.longValue()){
//				throw new CommandException("Transaction ID is invalid.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
//			}
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(new ProductModel(transactionDetailMasterModel.getProductId()));			
			commonCommandManager.loadProduct(baseWrapper);
			productModel = (ProductModel)baseWrapper.getBasePersistableModel();
			
			//*********************************** Bulk Payment 2 CNIC limit check - START
			Boolean limitApplicableForBulkdisbursement = true;
			if(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() == productModel.getServiceId().longValue()) {
				BulkDisbursementsModel bulkDisbursementsModel = new BulkDisbursementsModel();
				bulkDisbursementsModel.setTransactionCode(transactionDetailMasterModel.getTransactionCode());
				List<BasePersistableModel> list = commonCommandManager.findBasePersistableModel(bulkDisbursementsModel);
				if (list != null && !list.isEmpty()) {
					bulkDisbursementsModel = (BulkDisbursementsModel) list.get(0);
					limitApplicableForBulkdisbursement = bulkDisbursementsModel.getLimitApplicable();
					limitApplicableForBulkdisbursement = limitApplicableForBulkdisbursement == null ? Boolean.FALSE : limitApplicableForBulkdisbursement;
				}
			}
			//*********************************** Bulk Payment 2 CNIC limit check - END
			
			
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
			BankModel bankModel = new BankModel(BankConstantsInterface.ASKARI_BANK_ID);

			userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
						
			if(userDeviceAccountsModel == null) {
				userDeviceAccountsModel = new UserDeviceAccountsModel();
				userDeviceAccountsModel.setDeviceTypeIdDeviceTypeModel(deviceTypeModel);
				userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
				searchBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);		
				
				commonCommandManager.checkLogin(searchBaseWrapper);
				
				CustomList<UserDeviceAccountsModel> customList = searchBaseWrapper.getCustomList();
				
				if(customList.getResultsetList() != null && customList.getResultsetList().size()>0) {

					userDeviceAccountsModel = customList.getResultsetList().get(0);
				}
			}

			transactionDetailMasterModel.setAgent2Id(userDeviceAccountsModel.getUserId());
			RetailerModel retailerModel = loadRetailerModel(retailerContactModel.getRetailerId());
			DistributorModel distributorModel = loadDistributorModel(retailerModel.getDistributorId());

			WORK_FLOW_WRAPPER_PARAMS : {
	
				workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
				workFlowWrapper.setTransactionCodeModel(this.transactionCodeModel);
				workFlowWrapper.setRetailerContactModel(retailerContactModel);
				workFlowWrapper.setDistributorModel(distributorModel);
				workFlowWrapper.setRetailerModel(retailerModel);
				workFlowWrapper.setProductModel(productModel);
				workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel(TransactionTypeConstantsInterface.RECEIVE_MONEY_TX));
				workFlowWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
				workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
				workFlowWrapper.setAccountInfoModel(accountInfoModel);
				workFlowWrapper.setTransactionAmount(Double.parseDouble(transactionAmount));
				workFlowWrapper.setAppUserModel(appUserModel);
				workFlowWrapper.setReceiverAppUserModel(appUserModel);
				workFlowWrapper.setCustomerModel(customerModel);
				workFlowWrapper.setTransactionModel(transactionModel);
				workFlowWrapper.setBankModel(bankModel);
				workFlowWrapper.setSenderWalkinCustomerModel(new WalkinCustomerModel(walkInCustomerCNIC, walkInCustomerMobileNumber));
				// Following two lines are used to restore status in case of Failure (in WorkflowFacadeImpl)
				workFlowWrapper.setCurrentSupProcessingStatusId(transactionDetailMasterModel.getSupProcessingStatusId());
				workFlowWrapper.setLeg2Transaction(Boolean.TRUE);
				workFlowWrapper.setMPin(transactionPin);
				workFlowWrapper.setUserDeviceAccountModel(userDeviceAccountsModel);
			}

			boolean isBvs = isReceiverBvsRequired.equals("1") ? true : false;
			workFlowWrapper.setReceiverBvs(isBvs);

//			SwitchWrapper switchWrapper = commonCommandManager.verifyPIN(appUserModel, pin, workFlowWrapper);
			
			AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setAccountInfoModel(accountInfoModel);			
			
			workFlowWrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());
				
			workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
			transactionModel = workFlowWrapper.getTransactionModel();
			productModel = workFlowWrapper.getProductModel();
			userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
			workFlowWrapper.putObject("productTile",productModel.getName());
			commonCommandManager.sendSMS(workFlowWrapper);
				
		} catch(FrameworkCheckedException ex) {
			ex.printStackTrace();
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
			
		catch(WorkFlowException wex) {
			wex.printStackTrace();
			
			throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}

		
		if(logger.isDebugEnabled()) {
			logger.debug("End of ReceiveMoneyCommand.execute()");
		}
	}
	
	

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of ReceiveMoneyCommand.prepare()");
		}

		this.preparedBaseWrapper = baseWrapper;

		appUserModel = ThreadLocalAppUser.getAppUserModel();
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		transactionPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ONE_TIME_PIN);
		transactionCode = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_ID);
		walkInCustomerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
		walkInCustomerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
		walkInReceiverCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
		
		transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		isReceiverBvsRequired = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_BVS_REQUIRED);

		mobileNo = appUserModel.getMobileNo();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		
		if(logger.isDebugEnabled()) {
			logger.debug("End of ReceiveMoneyCommand.prepare()");
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of ReceiveMoneyCommand.validate()");
		}
			
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(isReceiverBvsRequired,validationErrors,"BVS Flag");
				
		if(!validationErrors.hasValidationErrors()) {
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		
		//***************************************************************************************************************************
		//									Check if sender or receiver cnic is blacklisted
		//***************************************************************************************************************************
		if (!validationErrors.hasValidationErrors()) {
			if (this.getCommonCommandManager().isCnicBlacklisted(walkInReceiverCNIC)) {
				validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
				throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
			}
		}
		//***************************************************************************************************************************

		if(!validationErrors.hasValidationErrors()) {
			
			try {
				
				validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);

				if(validationErrors.hasValidationErrors()) {
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				
			} catch (FrameworkCheckedException e) {
				
				throw new CommandException(e.getLocalizedMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		if(logger.isDebugEnabled()) {
			logger.debug("End of ReceiveMoneyCommand.validate()");
		}
		
		return validationErrors;
	}


	
	@Override
	public String response() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMMM yyyy");
		DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
		TransactionModel _transactionModel = workFlowWrapper.getTransactionModel();
				
		String date=PortalDateUtils.formatDate(_transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
		String time=PortalDateUtils.formatDate(_transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);	
		
		String transactionCode = _transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();	
		String agentBalance = Formatter.formatDouble(workFlowWrapper.getSwitchWrapper().getOlavo().getAgentBalanceAfterTransaction());
		Boolean isThirdPartyIncluded = transactionDetailMasterModel.getThirdPartyCheck();
		Double inclusiveCharges = transactionDetailMasterModel.getInclusiveCharges();

		isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;
		
		if(isThirdPartyIncluded) {
			inclusiveCharges = null;
		}
		
		inclusiveCharges = (inclusiveCharges == null) ? 0D : inclusiveCharges;
		
		Double TXAM = transactionDetailMasterModel.getTransactionAmount();
		TXAM -= inclusiveCharges;
		
		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<trans>");	
		responseBuilder.append("<trn ");

		responseBuilder.append(CommandFieldConstants.KEY_TX_ID+"='"+transactionCode+"' ");
		
		responseBuilder.append(CommandFieldConstants.KEY_R_W_CNIC+"='"+this.transactionDetailMasterModel.getRecipientCnic()+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_S_W_CNIC+"='"+this.transactionDetailMasterModel.getSenderCnic()+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE+"='"+this.walkInCustomerMobileNumber+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE+"='"+this.transactionDetailMasterModel.getRecipientMobileNo()+"' ");

		responseBuilder.append(CommandFieldConstants.KEY_DATE+"='"+date+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_DATEF+"='"+sdf.format(_transactionModel.getCreatedOn())+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TIMEF+"='"+tf.print(new LocalTime())+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_PRODUCT+"='"+this.transactionDetailMasterModel.getProductName()+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_CAMT+"='"+_transactionModel.getTotalCommissionAmount()+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_CAMTF+"='"+Formatter.formatDoubleByPattern(_transactionModel.getTotalCommissionAmount(), "#,###.00")+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TPAM+"='"+inclusiveCharges+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TPAMF+"='"+Formatter.formatDoubleByPattern(inclusiveCharges, "0.00")+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TXAM+"='"+TXAM+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TXAMF+"='"+Formatter.formatDoubleByPattern(TXAM, "#,###.00")+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TOTAL_AMOUNT+"='"+TXAM+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT+"='"+Formatter.formatDoubleByPattern(TXAM, "#,###.00")+"' ");
		
		responseBuilder.append(CommandFieldConstants.KEY_FORMATED_BAL+"='"+agentBalance+"' ");

		responseBuilder.append(">");
		responseBuilder.append("</trn>");
		responseBuilder.append("</trans>");
		
//		logger.info("\n"+responseBuilder.toString());
		return responseBuilder.toString();
	}	
	

	/**
	 * @param appUserModel
	 * @return
	 * @throws FrameworkCheckedException
	 */
	private RetailerContactModel getRetailerContactModel(AppUserModel appUserModel) throws FrameworkCheckedException {

		
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(retailerContactModel);
		baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
		retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();	
		
		return (retailerContactModel != null && retailerContactModel.getPrimaryKey() != null ? retailerContactModel : null);
	}
	
	/**
	 * @param _smartMoneyAccountModel
	 * @return
	 */
	private SmartMoneyAccountModel getSmartMoneyAccountModel(AppUserModel appUserModel , Long paymentModeId) throws CommandException {

		SmartMoneyAccountModel smartMoneyAccountModel = null;
		
		SmartMoneyAccountModel _smartMoneyAccountModel = new SmartMoneyAccountModel();
		
		if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

			_smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
			
		} else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

			_smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
		}
		
		_smartMoneyAccountModel.setDeleted(Boolean.FALSE);
		_smartMoneyAccountModel.setActive(Boolean.TRUE);
		_smartMoneyAccountModel.setPaymentModeId(paymentModeId);
	    
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(_smartMoneyAccountModel);
		
		try {
			
			SearchBaseWrapper searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(wrapper);
			
			if(searchBaseWrapper != null) {
				
				List<SmartMoneyAccountModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();
				
				if(resultsetList != null && !resultsetList.isEmpty()) {
					
					smartMoneyAccountModel = resultsetList.get(0);
				}
			}
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		logger.debug("Found Smart Money Account "+ smartMoneyAccountModel.getName());
		
		return smartMoneyAccountModel;
	}


	public RetailerModel loadRetailerModel(Long retailerId) throws CommandException {

		RetailerModel retailerModel = new RetailerModel();
		retailerModel.setRetailerId(retailerId);

		preparedBaseWrapper.setBasePersistableModel(retailerModel);

		try {

			preparedBaseWrapper = getCommonCommandManager().loadRetailer(preparedBaseWrapper);

		} catch (FrameworkCheckedException e) {

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}

		return (preparedBaseWrapper != null ? ((RetailerModel) preparedBaseWrapper.getBasePersistableModel()) : null);
	}

	public DistributorModel loadDistributorModel(Long distributorId) throws CommandException {

		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setDistributorId(distributorId);

		preparedBaseWrapper.setBasePersistableModel(distributorModel);

		try {

			preparedBaseWrapper = getCommonCommandManager().loadDistributor(preparedBaseWrapper);

		} catch (FrameworkCheckedException e) {

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}

		return (preparedBaseWrapper != null ? ((DistributorModel) preparedBaseWrapper.getBasePersistableModel()) : null);
	}

}