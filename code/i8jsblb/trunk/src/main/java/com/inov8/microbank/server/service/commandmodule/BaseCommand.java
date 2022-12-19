package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.commands.FonePayOTPVerificationCommand;
import com.inov8.microbank.server.service.consumercommandmodule.CustomerSelfRegistrationBaseCommand;
import com.inov8.microbank.server.service.consumercommandmodule.VerifyCustomerSelfRegistetrationCommand;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.hibernate.JDBCException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ContextLoader;

import java.sql.SQLException;

public abstract class BaseCommand implements CommandInterface 
{

	private MessageSource messageSource;
	protected CommonCommandManager commonCommandManager;
	private ESBAdapter esbAdapter;

	private CommandManager commandManager;
	
	public String deviceTypeId;
	public ValidationErrors validationErrors; 
	
	private int numberOfAttempts;
	private int maxTimeForAttempts;
	protected String ivrErrorCode;
	private OperatorModel operatorModel;
	protected HandlerModel handlerModel;
	protected AppUserModel handlerAppUserModel;
		
	//Against Bug#58
	
	public String getCommandParameter(BaseWrapper baseWrapper,String fieldName)
	{
		String fieldValue;
		if(baseWrapper.getObject(fieldName) != null)
		{
			fieldValue = baseWrapper.getObject(fieldName).toString();
			fieldValue = fieldValue.trim();
		}
		else
		{
			fieldValue = "";
		}
		return fieldValue;
	}
	
	public void doPrepare(BaseWrapper baseWrapper)
	{
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();

		if (appUserModel != null && 
				!(this instanceof CheckAppVersionCommand 
						|| this instanceof LoginCommand 
						|| this instanceof GetTickerStringCommand 
						|| this instanceof GetBankAccountInfoCommand
						|| this instanceof UpdateCatalogCommand
						|| this instanceof MfsPinChangeCommand
						|| this instanceof MfsLogoutCommand
						|| this instanceof VerifyPINCommand
						|| this instanceof VeriflyPinChangeCommand || this instanceof VerifyCustomerSelfRegistetrationCommand)) {
			
			try {

				if (appUserModel.getHandlerId() != null) {
					BaseWrapper bWrapper = new BaseWrapperImpl();
					HandlerModel handlerModel = new HandlerModel();
					handlerModel.setHandlerId(appUserModel.getHandlerId());
					bWrapper.setBasePersistableModel(handlerModel);
					bWrapper = this.getCommonCommandManager().loadHandler(bWrapper);
					handlerModel = (HandlerModel)bWrapper.getBasePersistableModel();
					if (handlerModel != null) {
						this.handlerModel = handlerModel;
						baseWrapper.putObject(CommandFieldConstants.KEY_HANDLER_MODEL, handlerModel);
					}
					
					//Preserve handler AppUser to check account health in validate()
					handlerAppUserModel = appUserModel;
					//Now Load Retailer AppUserModel and set in ThreadLocalAppUser
					AppUserModel retAppUserModel = new AppUserModel();
					retAppUserModel = this.getCommonCommandManager().loadAppUserByRetailerContractId(handlerModel.getRetailerContactId());
					ThreadLocalAppUser.setAppUserModel(retAppUserModel);
					
					bWrapper = new BaseWrapperImpl();
					UserDeviceAccountsModel udaModel = new UserDeviceAccountsModel();
					udaModel.setAppUserId(retAppUserModel.getAppUserId());
                    //Load userDeviceAccount of Agent for deviceTypeId=5 instead of 8
                    long deviceTypeIdLong = Long.valueOf(deviceTypeId == null ? "0" : deviceTypeId);
                    deviceTypeIdLong = (deviceTypeIdLong == DeviceTypeConstantsInterface.ALLPAY_WEB) ? DeviceTypeConstantsInterface.ALL_PAY : deviceTypeIdLong;
					udaModel.setDeviceTypeId(deviceTypeIdLong);
					bWrapper.setBasePersistableModel(udaModel);
					bWrapper = this.getCommonCommandManager().loadMfs(bWrapper);
					udaModel = (UserDeviceAccountsModel)bWrapper.getBasePersistableModel();
					if(udaModel != null){
						ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(udaModel);
					}
					
				}else if (appUserModel.getRetailerContactId() != null) {
					RetailerContactModel retContactModel = new RetailerContactModel();
					retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
					
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.setBasePersistableModel(retContactModel);
					CommonCommandManager commonCommandManager = this.getCommonCommandManager();
					bWrapper = commonCommandManager.loadRetailerContact(bWrapper);
					if (retContactModel != null) {
						baseWrapper.putObject(CommandFieldConstants.KEY_RETAILER_CONTACT_MODEL, retContactModel);
					}
				}
				
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}
			
		}
		
		prepare(baseWrapper);
	}
	
	public void doValidate() throws CommandException
	{
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		
		// skip credentials check in case of scheduler is performing transactions
		if(appUserModel != null 
				&& (appUserModel.getAppUserId() == PortalConstants.SCHEDULER_APP_USER_ID || appUserModel.getAppUserId() == PortalConstants.WEB_SERVICE_APP_USER_ID)){
			return;
		}
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
		try
		{
			
			//validate handler account health
			if (handlerAppUserModel != null) {
				
				baseWrapper.setBasePersistableModel(handlerAppUserModel);
				validationErrors = commonCommandManager.checkUserCredentials(baseWrapper);
				
				if(!validationErrors.hasValidationErrors())
				{
					validationErrors = validate(validationErrors);
				}
				
				if(validationErrors.hasValidationErrors())
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
				}
			}
			
			
			//validate Retailer/Agent account health
			baseWrapper.setBasePersistableModel(appUserModel);
			if(!(this instanceof CustomerPinCreateCommand) && !(this instanceof TellerCashInInfoCommand) && !(this instanceof TellerCashInCommand)
					&& !(this instanceof TellerCashOutCommand) && !(this instanceof VerifyCustomerSelfRegistetrationCommand) && !(this instanceof FonePayOTPVerificationCommand)
					&& !(this instanceof CustomerSelfRegistrationBaseCommand) && !(this instanceof ForgotPinInfoCommand) && !(this instanceof ForgotPinCommand)
					&& !(this instanceof ChangeLoginPinCommand) ){ //if IVR pin create/regenerate then no need to check credentials; Turab Feb 25, 2015
				validationErrors = commonCommandManager.checkUserCredentials(baseWrapper);
			}else{
				//in case of CustomerPinCreateCommand we need to initialize validationErrors to avoid NULL Pointer 
				validationErrors = new ValidationErrors();
			}
			
			if(!validationErrors.hasValidationErrors())
			{
				validationErrors = validate(validationErrors);
			}
			
			if(validationErrors.hasValidationErrors())
			{
				if(validationErrors.getErrors().equals("Receiver Mobile No. is registered as Agent.")){
					throw new CommandException(validationErrors.getErrors(),9024,ErrorLevel.HIGH,new Throwable());
				}
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			//Resetting threadlocal if found some error on validate method of command.
			if (handlerAppUserModel != null && handlerAppUserModel.getAppUserId() != null) {
				System.out.println("Resetting ThreadLocalAppUser with handlerAppUser - after command execute ===== appUserId:" + handlerAppUserModel.getAppUserId() + " Type:"+ handlerAppUserModel.getAppUserTypeId());
				ThreadLocalAppUser.setAppUserModel(handlerAppUserModel);

				try {
					BaseWrapper bWrapper = new BaseWrapperImpl();
					UserDeviceAccountsModel udaModel = new UserDeviceAccountsModel();
					udaModel.setAppUserId(handlerAppUserModel.getAppUserId());

					//Load userDeviceAccount of Agent for deviceTypeId=5 instead of 8
					long deviceTypeIdLong = Long.valueOf(deviceTypeId == null ? "0" : deviceTypeId);
					deviceTypeIdLong = (deviceTypeIdLong == DeviceTypeConstantsInterface.ALLPAY_WEB) ? DeviceTypeConstantsInterface.ALL_PAY : deviceTypeIdLong;
					udaModel.setDeviceTypeId(deviceTypeIdLong);
					bWrapper.setBasePersistableModel(udaModel);
					bWrapper = this.getCommonCommandManager().loadMfs(bWrapper);
					udaModel = (UserDeviceAccountsModel)bWrapper.getBasePersistableModel();
					if(udaModel != null){
						ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(udaModel);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if(ex.getErrorCode()==9015){
				throw new CommandException(null,ErrorCodes.CREDENTIALS_EXPIRED,ErrorLevel.HIGH,new Throwable());
			}
			else if(ex.getErrorCode()==9024){
				throw new CommandException(validationErrors.getErrors(),9024,ErrorLevel.HIGH,new Throwable());
			}
			else
				throw new CommandException(ex.getMessage(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
		}
	}
	
	public void doExecute() throws CommandException
	{
		try{
			execute();
		}finally{
			if (handlerAppUserModel != null && handlerAppUserModel.getAppUserId() != null) {
				System.out.println("Resetting ThreadLocalAppUser with handlerAppUser - after command execute ===== appUserId:" + handlerAppUserModel.getAppUserId() + " Type:"+ handlerAppUserModel.getAppUserTypeId());
				ThreadLocalAppUser.setAppUserModel(handlerAppUserModel);
				
				try {
					BaseWrapper bWrapper = new BaseWrapperImpl();
					UserDeviceAccountsModel udaModel = new UserDeviceAccountsModel();
					udaModel.setAppUserId(handlerAppUserModel.getAppUserId());

                    //Load userDeviceAccount of Agent for deviceTypeId=5 instead of 8
                    long deviceTypeIdLong = Long.valueOf(deviceTypeId == null ? "0" : deviceTypeId);
                    deviceTypeIdLong = (deviceTypeIdLong == DeviceTypeConstantsInterface.ALLPAY_WEB) ? DeviceTypeConstantsInterface.ALL_PAY : deviceTypeIdLong;
                    udaModel.setDeviceTypeId(deviceTypeIdLong);
					bWrapper.setBasePersistableModel(udaModel);
					bWrapper = this.getCommonCommandManager().loadMfs(bWrapper);
					udaModel = (UserDeviceAccountsModel)bWrapper.getBasePersistableModel();
					if(udaModel != null){
						ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(udaModel);
					}
				} catch (CommandException ex) {
						if(ex.getErrorCode() == 9098){
						throw new CommandException("Email Address Already Exists", ErrorCodes.EMAIL_ADDRESS_ALREADY_EXISTS, ErrorLevel.MEDIUM, new Throwable());
					}
				} catch (Exception ex) {
					if(ex.getMessage().equals("Debit Card Re-issuance request already exists")){
						throw new CommandException("Debit Card Re-issuance request already exists", ErrorCodes.DEBIT_CARD_REISSUANCE_EXISTS, ErrorLevel.MEDIUM, new Throwable());
					}
					ex.printStackTrace();
				}

			}
		}
	}

	public String doResponse() 
	{
		String response = response();
		
		if (handlerAppUserModel != null && handlerAppUserModel.getAppUserId() != null) {
			ThreadLocalAppUser.setAppUserModel(handlerAppUserModel);
		}
		
		return response;
	}


	
	public String generateEncodedPin(String pin)
	{
		pin = EncoderUtils.encodeToSha(pin);
		return pin;
	}
	
	public Double replaceNullWithZero(Double value)
	{
		if(value == null)
		{
			value = 0.00;
		}
		return value;
	}
	
	public Long replaceNullWithOne(Long value)
	{
		if(value == null)
		{
			value = 1L;
		}
		return value;
	}

	
	
	public String replaceNullWithEmpty(String value)
	{
		String fieldValue;
		if(value != null)
		{
			fieldValue = value;
			fieldValue = fieldValue.trim();
		}
		else
		{
			fieldValue = "";
		}
		return fieldValue;
	}
	
	public String convertBooleanToBit(boolean convertableFlag)
	{
		String convertedFlag;
		if(convertableFlag)
		{
			convertedFlag = "1";
		}
		else
		{
			convertedFlag = "0";
		}
		return convertedFlag;
	}
	
	public String decryptPin(String pin, byte encryption_type)
	{
		switch(encryption_type) {
			case EncryptionUtil.ENCRYPTION_TYPE_RSA:
				if(null != pin && null != getOperatorModel().getKey() && !pin.equals(""))
				{
					pin = StringUtil.replaceSpacesWithPlus(pin);
					pin = EncryptionUtil.doDecrypt(getOperatorModel().getKey(),pin);
				}
				break;
			case EncryptionUtil.ENCRYPTION_TYPE_AES:
				if(null != pin && null != getOperatorModel().getAESKey() && !pin.equals(""))
				{
					pin = StringUtil.replaceSpacesWithPlus(pin);
					pin = EncryptionUtil.decryptWithAES(getOperatorModel().getAESKey(),pin);
				}
				break;				
		}
		
		return new String(pin);
	}
	
	public String encryptPin(String pin, byte encryption_type)
	{
		switch(encryption_type) {
			case EncryptionUtil.ENCRYPTION_TYPE_RSA:
				if(null != pin && null != getOperatorModel().getKey() && !pin.equals(""))
				{
					pin = StringUtil.replaceSpacesWithPlus(pin);
					pin = EncryptionUtil.doEncrypt(getOperatorModel().getKey(),pin);
				}
				break;
			case EncryptionUtil.ENCRYPTION_TYPE_AES:
				if(null != pin && null != getOperatorModel().getAESKey() && !pin.equals(""))
				{
					pin = StringUtil.replaceSpacesWithPlus(pin);
					pin = EncryptionUtil.encryptWithAES(getOperatorModel().getAESKey(),pin);
				}
				break;				
		}
		
		return new String(pin);
	}
	
	public String decryptPin(String pin)
	{
		if(null != pin && null != getOperatorModel().getKey() && !pin.equals(""))
		{
			pin = StringUtil.replaceSpacesWithPlus(pin);
			pin = EncryptionUtil.doDecrypt(getOperatorModel().getKey(),pin);
		}
		return pin;
	}
	
	
	public String encryptPin(String pin)
	{
		if(pin != null && getOperatorModel().getKey() != null)
		{
			pin = EncryptionUtil.doEncrypt(getOperatorModel().getKey(), pin);
		}
		return pin;
	}
	
	public String removeCountryCode(String mobileNumber)
	{
		if(mobileNumber != null && !mobileNumber.equals(""))
		{
			if(mobileNumber.indexOf("+") != -1)
			{
				mobileNumber = "0"+mobileNumber.substring(3,mobileNumber.length());
			}
		}
		return mobileNumber;
	}
	
	
	
	public abstract void prepare(BaseWrapper baseWrapper);
	public abstract ValidationErrors validate(ValidationErrors validationErrors) throws CommandException;
	public abstract void execute() throws CommandException;
	public abstract String response();

	public IvrAuthenticationRequestQueue getIvrRequestHandler() {
		ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		return (IvrAuthenticationRequestQueue) applicationContext.getBean("ivrAuthenticationRequestQueueSender");
	}

	public CommonCommandManager getCommonCommandManager()
	{
		return commonCommandManager;
	}

	public void setCommonCommandManager(CommonCommandManager commonCommandManager)
	{
		this.commonCommandManager = commonCommandManager;
	}
	public void setEsbAdapter(ESBAdapter esbAdapter) {
		this.esbAdapter = esbAdapter;
	}
	public ESBAdapter getEsbAdapter() {
		return this.esbAdapter ;
	}
	
	public void setCommandManager(CommandManager commandManager)
	{
		this.commandManager = commandManager;
	}
	
	protected CommandManager getCommandManager()
	{
		return commandManager;		
	}
	
	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public int getMaxTimeForAttempts()
	{
		return maxTimeForAttempts;
	}

	public void setMaxTimeForAttempts(int maxTimeForAttempts)
	{
		this.maxTimeForAttempts = maxTimeForAttempts;
	}

	public int getNumberOfAttempts()
	{
		return numberOfAttempts;
	}

	public void setNumberOfAttempts(int numberOfAttempts)
	{
		this.numberOfAttempts = numberOfAttempts;
	}
	
	public OperatorModel getOperatorModel()
	{
		return operatorModel;
	}

	public void setOperatorModel(OperatorModel operatorModel)
	{
		this.operatorModel = operatorModel;
	}

	protected void handleException(Exception e) throws CommandException {
		Long errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
		if (e instanceof FrameworkCheckedException) {
			FrameworkCheckedException ex = (FrameworkCheckedException)e;
			if (ex.getErrorCode() == ErrorCodes.TERMINATE_EXECUTION_FLOW.longValue()) {
				errorCode = ex.getErrorCode();
			}
		}
		handleException(e, errorCode);
	}

	protected void handleException(Exception e, String errorCode) throws CommandException {
		handleException(e, Long.parseLong(errorCode));
	}

	protected void handleException(Exception e, Long errorCode) throws CommandException {

		long exCode = errorCode;
		if(e instanceof FrameworkCheckedException)
			exCode = ((FrameworkCheckedException) e).getErrorCode();

		String message = e.getMessage();
		if(StringUtil.isNullOrEmpty(message)|| e instanceof SQLException || e instanceof JDBCException || !(e instanceof FrameworkCheckedException || e instanceof WorkFlowException)){
			message = MessageUtil.getMessage("7001");
			exCode = ErrorCodes.TERMINATE_EXECUTION_FLOW;
		}

		throw new CommandException(message, exCode, ErrorLevel.HIGH, e);
	}

}
