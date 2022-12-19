package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.middleware.MiddlewareSwitchImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.web.context.ContextLoader;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class CheckAccountBalanceCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String accountId;
	protected String pin;
	protected Double accBalance;
	protected double creditLimit;
	protected double amountDue;
	protected double minAmountDue;
	protected Date dueDate;
	protected String deviceTypeId;
	protected String tPin;
	protected String appId;
	private boolean isConsumerAppRequest = false;
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	private Long paymentModeId = null;
	protected String encryptionType;
	protected String actType;
	boolean errorMessagesFlag;
	private Boolean isIvrResponse;
	protected Boolean isPinChangeRequired = false;
	protected Boolean isAccountDisabled = false;
	String veriflyErrorMessage;
	protected Boolean isAccountLinked = false;
	protected Boolean isAccountActive = false;
	private SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
	private BaseWrapper _baseWrapper = null;
	private String paymentMode;

	protected final Log logger = LogFactory.getLog(CheckAccountBalanceCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.info("Start of CheckAccountBalanceCommand.execute()");
		}	
		
		if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
			accountType = "1";
		}
		
		if(accountType.equals("1")) {
			
			if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
				if(isConsumerAppRequest){

					accBalance = getOlaBalance();

				}else {

					if (!isIvrResponse) {

						logger.info("Calling for IVR service....");
						try {

							IvrRequestDTO ivrDTO = new IvrRequestDTO();
							ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
							ivrDTO.setProductId(6L);
							ivrDTO.setRetryCount(0);
							getIvrRequestHandler().sentAuthenticationRequest(ivrDTO);

						} catch (FrameworkCheckedException e) {

							e.printStackTrace();
						}

						return;

					} else if (isIvrResponse) {

						accBalance = getOlaBalance();

						if (accBalance != null) {

							sendSMSCustomer(accBalance);
						}

					}
				}
            } else if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.RETAILER.longValue()) {
            	
				
    			accBalance = getOlaBalance();
    			
    			if(accBalance != null) {
    				
//    				sendSMSRetailer(accBalance);
    			}
    			
    			
            }
			
		} else if(accountType.equals("0")) {
			
			accBalance = getCoreBalance();
		}
	}
	
	
	private void sendSMSCustomer(Double accBalance) {
		
		String smsText = this.getMessageSource().getMessage("MINI.BalanceMS", new Object[] {Formatter.formatDouble(accBalance)}, null);			
		
		try {
		
			sendSMSToUser(appUserModel.getMobileNo(), smsText);
		
		} catch (Exception ex) {
			logger.error("[AccountToCashCommand.execute] Exception occured while sending SMS to Mobile No: " + appUserModel.getMobileNo() + ". Logged in AppUserId:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Exception: " + ex.getMessage());
		}
	}
	
	
	private void sendSMSRetailer(Double accBalance) {
		
    	logger.info("Functionality to check Agent Balance via SMS is disabled, will enable this on JS/Client request.");
		/*
		String smsText = this.getMessageSource().getMessage("MINI.BalanceMSAgent", new Object[] {Formatter.formatDouble(accBalance)}, null);			
		
		try {
		
			sendSMSToUser(appUserModel.getMobileNo(), smsText);
		
		} catch (Exception ex) {
			logger.error("[AccountToCashCommand.execute] Exception occured while sending SMS to Mobile No: " + appUserModel.getMobileNo() + ". Logged in AppUserId:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Exception: " + ex.getMessage());
		}
		*/
	}
	
	
	/**
	 * @return
	 * @throws CommandException 
	 */
	private Double getOlaBalance() throws CommandException {
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();

		Double balance = 0.0D;
		
		try {
	
			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
			
			if(!validationError.hasValidationErrors()) {
				Long paymentId=3L;
				if(paymentMode.equals("HRA"))
					paymentId=7L;
				SmartMoneyAccountModel bbSmartMoneyAccountModel =  this.getSmartMoneyAccountModel(appUserModel, paymentId) ;
				
				if(bbSmartMoneyAccountModel != null){
					isAccountLinked = true;
					SearchBaseWrapperImpl searchBaseWrapper = new SearchBaseWrapperImpl();
					
					CustomList customList = new CustomList();
					ArrayList arrList = new ArrayList();
					arrList.add(bbSmartMoneyAccountModel);
					customList.setResultsetList(arrList);
					searchBaseWrapper.setCustomList(customList);
					validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
					isAccountActive = true;
				}			
				
				if(!isAccountLinked){
					throw new CommandException("HRA Account does not exist.", ErrorCodes.NO_HRA_EXISTS, ErrorLevel.CRITICAL,new Throwable());
					/*if(paymentId.equals(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT))
						throw new CommandException("HRA Account does not exist.", ErrorCodes.NO_HRA_EXISTS, ErrorLevel.CRITICAL,new Throwable());
					else
						throw new CommandException(this.getMessageSource().getMessage("accountNotLinked", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());*/
				}

				if(!isAccountActive){
					throw new CommandException(this.getMessageSource().getMessage("ACCOUNT.INACTIVE", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}					
				
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(bbSmartMoneyAccountModel);					
				AbstractFinancialInstitution olaFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);
										
				TransactionModuleManager transactionModuleManager = (TransactionModuleManager) getBean("transactionModuleManager");
				
				WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
				workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());		
				workFlowWrapper = transactionModuleManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);					
				workFlowWrapper.setSmartMoneyAccountModel(bbSmartMoneyAccountModel);
										
				TransactionModel transactionModel = new TransactionModel();
				transactionModel.setTransactionCodeIdTransactionCodeModel(workFlowWrapper.getTransactionCodeModel());
					
				workFlowWrapper.setTransactionModel(transactionModel);

				Long customerId = null;
				if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()){
					customerId = appUserModel.getCustomerId();
				}else{
					customerId = appUserModel.getAppUserId();
				}
				AccountInfoModel olaAccountInfoModel = olaFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(bbSmartMoneyAccountModel, customerId, workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());

				SwitchWrapper _switchWrapper = new SwitchWrapperImpl();
				_switchWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
				_switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
				_switchWrapper.setWorkFlowWrapper(workFlowWrapper);
				_switchWrapper.setAccountInfoModel(olaAccountInfoModel);
				_switchWrapper.setTransactionTransactionModel(transactionModel);
				_switchWrapper.setBasePersistableModel(bbSmartMoneyAccountModel) ;	
						
				olaFinancialInstitution.checkBalanceWithoutPin(_switchWrapper);

				paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
				balance = _switchWrapper.getBalance();
			}
				
		} catch(Exception e) {
			if(!isAccountLinked)
				throw new CommandException("HRA Account does not exist.", ErrorCodes.NO_HRA_EXISTS, ErrorLevel.CRITICAL,new Throwable());

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
		}
		
		return balance;
	}
	
	
	
	/**
	 * @return
	 * @throws CommandException 
	 */
	private Double getCoreBalance() throws CommandException {

		if(logger.isDebugEnabled()) {
			logger.debug("Start of CheckAccountBalanceCommand.getCoreBalance()");
		}

		Double agentBalance = 0.0D;
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		
		try {
		

			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
		
			if(!validationError.hasValidationErrors()) {
					
				SmartMoneyAccountModel coreSmartMoneyAccountModel = this.getSmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT) ;
				SmartMoneyAccountModel bbSmartMoneyAccountModel =  this.getSmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) ;
					
				if(coreSmartMoneyAccountModel != null){
					isAccountLinked = true;
					SearchBaseWrapperImpl searchBaseWrapper = new SearchBaseWrapperImpl();
					
					CustomList customList = new CustomList();
					ArrayList arrList = new ArrayList();
					arrList.add(coreSmartMoneyAccountModel);
					customList.setResultsetList(arrList);
					searchBaseWrapper.setCustomList(customList);
					validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
					isAccountActive = true;
				}			
				
				if(!isAccountLinked){
					throw new CommandException(this.getMessageSource().getMessage("accountNotLinked", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}

				if(!isAccountActive){
					throw new CommandException(this.getMessageSource().getMessage("ACCOUNT.INACTIVE", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}	
				
				//this.verifyPIN(appUserModel, pin, bbSmartMoneyAccountModel);
				
				AbstractFinancialInstitution olaFinancialInstitution = null;

				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(bbSmartMoneyAccountModel);					
				olaFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);
					
				AbstractFinancialInstitution coreFinancialInstitution = commonCommandManager.loadFinancialInstitutionByClassName(FinancialInstitutionConstants.PHOENIX_FINANCIAL_INSTITUTION_CLASS_NAME);
					
				TransactionModuleManager transactionModuleManager = (TransactionModuleManager) getBean("transactionModuleManager");
				WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
				workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());		
				workFlowWrapper = transactionModuleManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);					
				workFlowWrapper.setSmartMoneyAccountModel(coreSmartMoneyAccountModel);
					
				AccountInfoModel coreAccountInfoModel = olaFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(coreSmartMoneyAccountModel, appUserModel.getAppUserId(), workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());

				paymentModeId = PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT;
				
				TransactionModel transactionModel = new TransactionModel();
				transactionModel.setTransactionCodeIdTransactionCodeModel(workFlowWrapper.getTransactionCodeModel());
									
				DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
				deviceTypeModel.setDeviceTypeId(Long.valueOf(deviceTypeId));
				
				workFlowWrapper.setTransactionModel(transactionModel);
				workFlowWrapper.setDeviceTypeModel(deviceTypeModel);	
				
				SwitchWrapper switchWrapper = new SwitchWrapperImpl();

				switchWrapper.setAccountInfoModel(coreAccountInfoModel);
				switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
				switchWrapper.putObject(CommandFieldConstants.KEY_PIN, coreAccountInfoModel.getOldPin());
				switchWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
				switchWrapper.setWorkFlowWrapper(workFlowWrapper);
				switchWrapper.setTransactionTransactionModel(new TransactionModel());
				switchWrapper.setBasePersistableModel(coreSmartMoneyAccountModel) ;		
				switchWrapper.setPaymentModeId(paymentModeId);
				switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
				
				switchWrapper = coreFinancialInstitution.checkBalanceWithoutPin(switchWrapper);

				agentBalance = switchWrapper.getAgentBalance();
			}
				
		} catch(Exception e) {
			logger.error(e);
			if(e instanceof NullPointerException){
				throw new CommandException(this.getMessageSource().getMessage("7001", null,null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			} else if(e instanceof RemoteAccessException || e instanceof ConnectException) {
				throw new WorkFlowException(new MiddlewareSwitchImpl().getResponseCodeDetail("404"));	
			} else {
				throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			}
		}
			
		return agentBalance;
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAccountBalanceCommand.prepare()");
		}
		
		_baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		
		isIvrResponse = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE) == null ? 
				false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE));
		
		accountId = this.getCommandParameter(baseWrapper, "BBACID");

		appId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_ID);
		if(!StringUtil.isNullOrEmpty(appId) && appId.equals(CommandConstants.CONSUMER_APP)){
			isConsumerAppRequest = true;
		}

		logger.info("[CheckAccountBalanceCommand.prepare] Logged in AppUserID:" + appUserModel.getAppUserId() + ", isConsumerAppRequest:"+isConsumerAppRequest);
		
		encryptionType = this.getCommandParameter(baseWrapper, "ENCT");
		accountType = this.getCommandParameter(baseWrapper, "ACCTYPE");
		
		String bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);

		accountType = this.getCommandParameter(baseWrapper, "ACCTYPE");
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		paymentMode = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE) == null || this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE).equals("")
				? "BLB":"HRA";
		if(StringUtil.isNullOrEmpty(tPin)){
			if(!StringUtil.isNullOrEmpty(pin)){				
				tPin = pin;
			}
		}
		pin = StringUtil.replaceSpacesWithPlus(pin);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		if(appUserModel.getAppUserTypeId() != 3 && Long.valueOf(deviceTypeId) != 5L){			
			tPin = this.decryptPin(tPin);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAccountBalanceCommand.prepare()");
		}
	}
	
	
	
	private void validate(BaseWrapper baseWrapper) throws CommandException {
		
		if(logger.isDebugEnabled()) {
			logger.info("Start of CheckAccountBalanceCommand.validate()");
		}
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		
		try {
			
			smartMoneyAccountModel = this.getSmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		
		} catch (FrameworkCheckedException e) {

			throw new CommandException(e.getLocalizedMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
		if(( accountId == null || "".equals(accountId) ) && !isIvrResponse && appUserModel.getAppUserTypeId() == 2L) {
				
				ValidationErrors validationErrors = checkSmartMoneyAccount(searchBaseWrapper);
				
				if(!validationErrors.hasValidationErrors()) {
					
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
				
				} else {
				
					if(validationErrors.getErrors().equalsIgnoreCase(this.getMessageSource().getMessage("ACCOUNT.PIN.CHANGE.REQ", null, null)))
					{
						isPinChangeRequired = true;
						isAccountDisabled = false;
					}else if(validationErrors.getErrors().equalsIgnoreCase(this.getMessageSource().getMessage("ACCOUNT.INACTIVE", null, null)))
					{
						isPinChangeRequired = false;
						isAccountDisabled = true;
					}else{
						isPinChangeRequired = false;
						isAccountDisabled = false;
					}
					
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				
			} else if( ( accountId == null || "".equals(accountId) ) && isIvrResponse ) {
			
				ValidationErrors validationErrors = checkSmartMoneyAccount(searchBaseWrapper);
				
				if(!validationErrors.hasValidationErrors()) {
				
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
				} else {
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				
			}else if(isIvrResponse && appUserModel.getAppUserTypeId() == 2L) {
			
				ValidationErrors validationErrors = checkSmartMoneyAccount(searchBaseWrapper);
				
				if(!validationErrors.hasValidationErrors())	{
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
				} else {
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				
			}else if(appUserModel.getAppUserTypeId() == 3L) {
								
				ValidationErrors validationErrors = checkSmartMoneyAccount(searchBaseWrapper);
			
				if(!validationErrors.hasValidationErrors())
				{
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
					isAccountActive = true;
				}else{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
	}	
	
	
	private ValidationErrors checkSmartMoneyAccount(SearchBaseWrapper searchBaseWrapper) {
		
		ValidationErrors validationErrors = new ValidationErrors();
		
		try {
			
			java.util.List<SmartMoneyAccountModel> resultSetList = new ArrayList<SmartMoneyAccountModel>(0);
			resultSetList.add((SmartMoneyAccountModel)searchBaseWrapper.getBasePersistableModel());
			
			searchBaseWrapper.setCustomList(new CustomList<SmartMoneyAccountModel>(resultSetList));

			validationErrors = this.getCommonCommandManager().checkSmartMoneyAccount(searchBaseWrapper);
			
		} catch (FrameworkCheckedException e) {
			
			e.printStackTrace();
		}
		
		return validationErrors;
	}
	

	@Override
	public String response()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CheckAccountBalanceCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAccountBalanceCommand.validate()");
		}
		
		if(Long.valueOf(deviceTypeId) != DeviceTypeConstantsInterface.SMS_GATEWAY.longValue() && !isIvrResponse){			
			//validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		}
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		if(isPinChangeRequired && appUserModel.getAppUserTypeId() == 2L){			
			ValidatorWrapper.addError(validationErrors, this.getMessageSource().getMessage("CUSTOMER.PIN.CHANGE",null, null));
		}else if(isAccountDisabled && appUserModel.getAppUserTypeId() == 2L){
			ValidatorWrapper.addError(validationErrors, this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.INACTIVE",null, null));
		}
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(accountId,validationErrors,"Account Id");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAccountBalanceCommand.validate()");
		}		
		
		validate(_baseWrapper);	
		return validationErrors;
	}

	private String toXML() {
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAccountBalanceCommand.toXML()");
		}
		
		
	
		// when isIvrResponse = true no response is needed
		if (isIvrResponse && appUserModel.getAppUserTypeId() == 2) 
			return "";
		
		StringBuilder strBuilder = new StringBuilder();
		if(isConsumerAppRequest){
			if(Double.toString(accBalance) != null)
			{
				strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_PARAM).append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_BAL).append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE).append(replaceNullWithZero(accBalance))
						.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAM).append(TAG_SYMBOL_CLOSE)
						.append(TAG_SYMBOL_OPEN).append(TAG_PARAM).append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE).append(CommandFieldConstants.KEY_FORMATED_BAL)
						.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE).append(Formatter.formatNumbers(accBalance))
						.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAM).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
			}

		}
//		else if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue())
//		{
//			strBuilder.append("Current balance of your account is Rs. "+Formatter.formatDouble(accBalance));
//		}
//
		else if( this.paymentModeId != null )
		{
			if( this.paymentModeId.longValue() == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue() || 
					this.paymentModeId.longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue() )
			{
				if(Double.toString(accBalance) != null)
				{
					strBuilder.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAMS)
						.append(TAG_SYMBOL_CLOSE)
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_BAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(replaceNullWithZero(accBalance))
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE)

						.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_FORMATED_BAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatNumbers(accBalance))
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE)

						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAMS)
						.append(TAG_SYMBOL_CLOSE);
				}

			}
			else if( this.paymentModeId.longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue() )
			{
					strBuilder.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAMS)
						.append(TAG_SYMBOL_CLOSE)
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_FORMATED_CREDIT_LIMIT)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatNumbers(this.creditLimit))
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE)

						.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatNumbers(this.amountDue))
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE)
						
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_FORMATED_MINIMUM_AMOUNT_DUE)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatNumbers(this.minAmountDue))
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE)
						
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_FORMATED_LATE_BILL_DATE)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatDate(this.dueDate))
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE)

						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAMS)
						.append(TAG_SYMBOL_CLOSE);

			}
			
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAccountBalanceCommand.toXML()");
		}
		return strBuilder.toString();
	}
	
	protected void sendSMSToUser(String mobileNo, String message) throws CommandException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SmsMessage smsMessage = new SmsMessage(mobileNo, message, this.getMessageSource().getMessage("MINI.shortCode", null, null));
		baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, smsMessage);
		try
		{
			this.getCommonCommandManager().sendSMSToUser(baseWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			throw new CommandException(this.getMessageSource().getMessage("mfsRequestHandler.unknownError", null, null),
					ErrorCodes.UNKNOWN_ERROR, ErrorLevel.MEDIUM, new Throwable());
		}
	}	
	
		private SmartMoneyAccountModel getSmartMoneyAccountModel(AppUserModel appUserModel, Long paymentModeId) throws FrameworkCheckedException {

			SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
			if(appUserModel.getAppUserTypeId() == 2L){
				smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
			}else{				
				smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
			}
			smartMoneyAccountModel.setPaymentModeId(paymentModeId);
			smartMoneyAccountModel.setDeleted(Boolean.FALSE);
			smartMoneyAccountModel.setActive(Boolean.TRUE);
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl(); 
			searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);					
			
			if(searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
				return (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
			}
			
			return null;
		}
	
	
		public Object getBean(String beanName) {
			return ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
		}	
}
