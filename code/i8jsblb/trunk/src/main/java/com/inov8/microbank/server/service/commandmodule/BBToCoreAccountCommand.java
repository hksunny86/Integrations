package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_CAMT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CAMTF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CMOB;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_COREACID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PROD;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TAMT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TAMTF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TPAM;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TPAMF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRXID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TXAM;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TXAMF;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;

import com.inov8.microbank.common.util.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;



public class BBToCoreAccountCommand extends BaseCommand {
	protected AppUserModel appUserModel;
	protected String productId;
	protected String accountId;
	protected String mobileNo;
	protected String txProcessingAmount;
	protected String pin;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String totalAmount;
	protected String billAmount;
	protected String cvv;
	protected String tPin;
	
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	private String accountTitle;
	
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel smartMoneyAccountModel;
	UserDeviceAccountsModel userDeviceAccountsModel;
	
	private double customer1Balance;
	RetailerContactModel fromRetailerContactModel;
	private String senderMobileNo;
	private String coreAccountNo;
	private Boolean isIvrResponse;
	private String transactionId;
	private String encryption_type;
	
	private String receiverMobileNo; //turab
	
	protected final Log logger = LogFactory.getLog(BBToCoreAccountCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BBToCoreAccountCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		if(appUserModel.getAppUserId() != null)//Agent AppUser ID
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					smartMoneyAccountModel =  new SmartMoneyAccountModel();
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
					if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
						smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
					}
					
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
					accountInfoModel.setCustomerId(appUserModel.getAppUserId());
					accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
					workFlowWrapper.setCustomerAccount(customerAccount);
								
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setAppUserModel(appUserModel);
					
						
					
					if(isIvrResponse){
						TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
						transactionCodeModel.setCode(transactionId);
						workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
						TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel();
						txDetailMasterModel.setTransactionCode(transactionId);
						searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(txDetailMasterModel);
						searchBaseWrapper = this.getCommonCommandManager().loadTransactionDetailMaster(searchBaseWrapper);
						txDetailMasterModel = (TransactionDetailMasterModel)searchBaseWrapper.getBasePersistableModel();
						workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);
						senderMobileNo = txDetailMasterModel.getSaleMobileNo();//
						billAmount = txDetailMasterModel.getTransactionAmount().toString();
						baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT,  txDetailMasterModel.getTransactionAmount());
						
						if(txDetailMasterModel.getHandlerId() != null){
							this.getCommonCommandManager().loadHandlerData(txDetailMasterModel.getHandlerId(), workFlowWrapper);
						}

						//Following check is to avoid duplicate processing of transaction(duplicate IVR calls) [11 July 2017]
						if(txDetailMasterModel.getSupProcessingStatusId() != SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING.longValue()){
							logger.error("This transaction is already processed via IVR. trxId:"+txDetailMasterModel.getTransactionCode()+" existing status:"+txDetailMasterModel.getSupProcessingStatusId());
							throw new CommandException("Transaction already processed",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}

					}else{
						
						workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
						workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
						workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
						workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
						workFlowWrapper.setHandlerModel(handlerModel);
						workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
						
					}
					

					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					
					((BBToCoreVO)productVo).setAccountNumber(accountNumber);
					workFlowWrapper.setProductVO(productVo);
					
//					if(!isIvrResponse) {
//						//title fetch and load vo with customer data
//						workFlowWrapper.setTransactionDetailMasterModel(new TransactionDetailMasterModel());
//						workFlowWrapper = commonCommandManager.getBBToCoreAccInfo(workFlowWrapper);
//					}
					
					AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel = this.getCommonCommandManager().loadAppUserByQuery(senderMobileNo, UserTypeConstantsInterface.CUSTOMER);
					
					CustomerModel customerModel = new CustomerModel();
					customerModel.setCustomerId(customerAppUserModel.getCustomerId());
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.setBasePersistableModel(customerModel);
					bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
					customerModel = (CustomerModel)bWrapper.getBasePersistableModel();
					Long segId = customerModel.getSegmentId();
					
					workFlowWrapper.setCustomerModel(customerModel);
					workFlowWrapper.setSegmentModel(new SegmentModel());
					workFlowWrapper.getSegmentModel().setSegmentId(segId);
					workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
					workFlowWrapper.setWalkInCustomerMob(receiverMobileNo);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BB_TO_CORE_ACCOUNT_TX);
					workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
	
					/*smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());*/

					String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

					// Velocity validation - start
				    bWrapper = new BaseWrapperImpl();
				    bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
				    bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
				    bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, fromRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
				    bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, fromRetailerContactModel.getDistributorLevelId());
				    bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(billAmount));
					bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, fromRetailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
					bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//					bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
					bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segId));
				    commonCommandManager.checkVelocityCondition(bWrapper);
				    // Velocity validation - end

					
					
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.setIsIvrResponse(isIvrResponse);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					
					logger.info("[BBToCoreAccountCommand.execute] Going to execute Transaction flow. Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + 
							" Customer Mobile No:" + mobileNo);

					SmartMoneyAccountModel custmerSmartMoneyAccountModel =  new SmartMoneyAccountModel();
					custmerSmartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
					custmerSmartMoneyAccountModel.setDeleted(Boolean.FALSE);
					custmerSmartMoneyAccountModel.setActive(Boolean.TRUE);
					custmerSmartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
					searchBaseWrapper.setBasePersistableModel(custmerSmartMoneyAccountModel);					
					searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
					
					if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
						custmerSmartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
						workFlowWrapper.setOlaSmartMoneyAccountModel(custmerSmartMoneyAccountModel);
					} else {
						logger.info("Unable to load Smart Money Account for Customer.");
					}			
					
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					txProcessingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount().toString();
					
					if (((BBToCoreVO)workFlowWrapper.getProductVO()).getSenderBalance() != null) {
						customer1Balance = ((BBToCoreVO)workFlowWrapper.getProductVO()).getSenderBalance();
					}

					commonCommandManager.sendSMS(workFlowWrapper);
				}
				else
				{
					logger.error("[BBToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount);
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				
				if(logger.isErrorEnabled())
				{
					logger.error("[BBToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				
				if(logger.isErrorEnabled())
				{
					logger.error("[BBToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				throw new CommandException(wex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				
				if(logger.isErrorEnabled())
				{
					logger.error("[BBToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.error("[BBToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount);
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of BBToCoreAccountCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BBToCoreAccountCommand.prepare()");
		}
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		/*if (appUserModel == null) {
			appUserModel = new AppUserModel();
			appUserModel.setMobileNo(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE));
			try {
				appUserModel = this.getCommonCommandManager().loadAppUserByMobileByQuery(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE));
				ThreadLocalAppUser.setAppUserModel(appUserModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		
		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CORE_ACC_NO);
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		cvv = this.decryptPin(cvv);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		tPin = this.decryptPin(tPin);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		coreAccountNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CORE_ACC_NO);
		isIvrResponse = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE) == null ? false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE));
		transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
		accountTitle = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE);
		receiverMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		
		try{
        	RetailerContactModel retContactModel = new RetailerContactModel();
        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
        	
        	BaseWrapper bWrapper = new BaseWrapperImpl();
        	bWrapper.setBasePersistableModel(retContactModel);
        	CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        	bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

        	this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

        }catch(Exception ex){
        	logger.error("[CashDepositInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
        }
		
		logger.info("[BBToCoreAccountCommand.prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + 
    			" Product ID:" + productId + " AccountId: " + accountId + 
    			" deviceTypeId: " + deviceTypeId + 
    			(productId.equals("50000") ? " Receiver" : "") + " MobileNumber: " + mobileNo);
    
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of BBToCoreAccountCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of BBToCoreAccountCommand.response()");
		}
		
		if(isIvrResponse){
			return ivrErrorCode;
		}
		
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BBToCoreAccountCommand.validate()");
		}
		
		if (isIvrResponse) {
			validationErrors = ValidatorWrapper.doRequired(transactionId,validationErrors,"Transaction ID");
			if(!validationErrors.hasValidationErrors())
			{
				validationErrors = ValidatorWrapper.doNumeric(transactionId,validationErrors,"Transaction ID");
			}
		}else{
			
			validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doRequired(accountNumber,validationErrors,"Account Number");
			validationErrors = ValidatorWrapper.doRequired(senderMobileNo,validationErrors," Sender Mobile Number");
			validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
			validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Amount");
			validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
			validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
			validationErrors = ValidatorWrapper.doRequired(billAmount,validationErrors,"Bill Amount");
			validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");

			if(!validationErrors.hasValidationErrors())
			{
				byte enc_type = new Byte(encryption_type).byteValue();
				ThreadLocalEncryptionType.setEncryptionType(enc_type);
			}
		
			if(!validationErrors.hasValidationErrors())
			{
				validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
				validationErrors = ValidatorWrapper.doInteger(accountNumber,validationErrors,"Account Number");	
				validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
				validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Amount");
			}
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of BBToCoreAccountCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BBToCoreAccountCommand.toXML()");
		}
		
		StringBuilder strBuilder = new StringBuilder();

		String BAMT=Formatter.formatDouble(transactionModel.getTransactionAmount());
		String txID=transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();
		String date=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
		String time=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
		String CAMT=Formatter.formatDouble(transactionModel.getTotalCommissionAmount());
		
		String TPAM=txProcessingAmount;
		String cust1Balance=Formatter.formatDouble(customer1Balance);

		if (isIvrResponse == false) {
			
			strBuilder.append(TAG_SYMBOL_OPEN)
			
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRXID)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_CMOB)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getNotificationMobileNo()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_COREACID)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(coreAccountNo)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_DATE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(transactionModel.getCreatedOn())
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_DATEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TIMEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatTime(transactionModel.getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PROD)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(productModel.getName()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_CAMT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CAMT)
			.append(TAG_SYMBOL_QUOTE)
					
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_CAMTF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(transactionModel.getTotalCommissionAmount()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TPAM)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(TPAM)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TPAMF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(Double.parseDouble(txProcessingAmount)))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TAMT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithZero(transactionModel.getTotalAmount()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TAMTF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(transactionModel.getTotalAmount()))
			.append(TAG_SYMBOL_QUOTE)		
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TXAM)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithZero(transactionModel.getTransactionAmount()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TXAMF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(transactionModel.getTransactionAmount()))
			.append(TAG_SYMBOL_QUOTE)		
//			.append(TAG_SYMBOL_CLOSE)
			//balance not required before ivr call.
			/*.append(TAG_SYMBOL_SPACE)
			.append(ATTR_BALF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(cust1Balance)
			.append(TAG_SYMBOL_QUOTE)		
			.append(TAG_SYMBOL_CLOSE)*/
		
			
			.append(TAG_SYMBOL_SPACE)
			.append(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(StringEscapeUtils.escapeXml(accountTitle))
			.append(TAG_SYMBOL_QUOTE)		
			.append(TAG_SYMBOL_CLOSE)				
			
			
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE);
			
		}

		return strBuilder.toString();
	}
}