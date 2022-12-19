package com.inov8.microbank.server.service.commandmodule;


import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.util.*;
import com.inov8.verifly.common.constants.FailureReasonConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class VeriflyPinChangeCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String accountId;
	protected String bankId;
	protected String pin;
	protected String newPin;
	protected String confirmPin;
	protected String deviceTypeId;
	String veriflyErrorMessage;
	boolean errorMessagesFlag;
	UserDeviceAccountsModel userDeviceAccountsModel; 
	protected String encryption_type;
	private Boolean incorrectMPIN = Boolean.FALSE;

	protected final Log logger = LogFactory.getLog(VeriflyPinChangeCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.execute()");
		}
		userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();

		logger.info("\n\nVeriflyPinChangeCommand");
		logger.info(
				 "\nApp User Id = "+appUserModel.getAppUserId() 
				+"\nAppUser Type Id "+ appUserModel.getAppUserTypeId()
				+"\nAppUser Retailer Id "+appUserModel.getRetailerContactId()
				+"\nAppUser Handler Id "+appUserModel.getHandlerId()
				+"\nAppUser Customer Id "+appUserModel.getCustomerId()
				);
		
		try
		{
				if(appUserModel.getCustomerId() != null || appUserModel.getRetailerContactId() != null || 
						(appUserModel.getHandlerId() != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue())) {
				
				ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationError.hasValidationErrors()) {	
					
					if(appUserModel.getHandlerId() != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {

						smartMoneyAccountModel.setHandlerId(appUserModel.getHandlerId());
						smartMoneyAccountModel = this.getSmartMoneyAccountModel(smartMoneyAccountModel);
						
					} else if(appUserModel.getRetailerContactId() != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

						smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());	
						smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
						smartMoneyAccountModel = this.getSmartMoneyAccountModel(smartMoneyAccountModel);			
					
					} else if(appUserModel.getCustomerId() != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

						smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
						smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
						smartMoneyAccountModel = this.getSmartMoneyAccountModel(smartMoneyAccountModel);
					}
					
					if(smartMoneyAccountModel == null) {
						logger.error("smart money account not found");
						throw new CommandException(this.getMessageSource().getMessage("phoenix.trans.02", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());	
					}					
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					AbstractFinancialInstitution abstractFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);

					if(abstractFinancialInstitution != null)
					{
						baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper); 
						smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

//						ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
						
						if(smartMoneyAccountModel.getActive())
						{
							if(smartMoneyAccountModel.getName() != null)
							{
								if((null != smartMoneyAccountModel.getCustomerId() && smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString())) || 
										(null != smartMoneyAccountModel.getRetailerContactId() && smartMoneyAccountModel.getRetailerContactId().toString().equals(appUserModel.getRetailerContactId().toString())) || 
										(null != smartMoneyAccountModel.getHandlerId() && smartMoneyAccountModel.getHandlerId().longValue() == appUserModel.getHandlerId().longValue()))
								{
									AccountInfoModel accountInfoModel = new AccountInfoModel();
									if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue())
									{
										accountInfoModel.setCustomerId(appUserModel.getCustomerId());
									}
									else
									{
										accountInfoModel.setCustomerId(appUserModel.getAppUserId());
									}
									accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
									accountInfoModel.setOldPin(pin);
									accountInfoModel.setNewPin(newPin);
									accountInfoModel.setConfirmNewPin(confirmPin);
									LogModel logModel = new LogModel();
									logModel.setCreatedBy(appUserModel.getUsername());
									logModel.setCreatdByUserId(appUserModel.getAppUserId());
									veriflyBaseWrapper.setLogModel(logModel);
									veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
									
									SwitchWrapper switchWrapper = new SwitchWrapperImpl();
									switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
									switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
									switchWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN,newPin);
									switchWrapper.putObject(CommandFieldConstants.KEY_PIN,pin);
									switchWrapper.putObject(CommandFieldConstants.KEY_CNIC,appUserModel.getNic());
									switchWrapper.putObject(CommandFieldConstants.KEY_OPERATOR_MODEL,this.getOperatorModel());
									veriflyBaseWrapper.putObject(SwitchConstants.KEY_SWITCHWRAPPER,switchWrapper);
									
																		
									veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
									
									veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId) );
									
									veriflyBaseWrapper = abstractFinancialInstitution.changePin(veriflyBaseWrapper);
									

									logger.info("\n----------------------------------------------------------------------");

									logger.info(appUserModel.getUsername());
									logger.info(appUserModel.getAppUserId());
									logger.info(smartMoneyAccountModel.getSmartMoneyAccountId());
									logger.info(smartMoneyAccountModel.getHandlerId());
									
									logger.info("\n----------------------------------------------------------------------");
									
									errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
															
									if(errorMessagesFlag)
									{						
										accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
										smartMoneyAccountModel.setChangePinRequired(Boolean.FALSE);
										smartMoneyAccountModel.setUpdatedOn(new Date());
										smartMoneyAccountModel.setUpdatedByAppUserModel(appUserModel);
										baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
										commonCommandManager.updateSmartMoneyAccount(baseWrapper);
									}
									else
									{
										veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
										incorrectMPIN = Boolean.TRUE;
										if(!deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()))
											throw new CommandException(veriflyErrorMessage,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
										else
											return;
									}
								}
								else
								{
									throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
								}
							}
							else
							{
								throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
						}
						else
						{
							throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					else
					{
						throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.veriflyManagerNotExists", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else
				{
					throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			else
			{
				throw new CommandException(this.getMessageSource().getMessage("command.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
//		catch(FrameworkCheckedException ex)
//		{
//			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
//		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VeriflyPinChangeCommand.execute()");
		}
	}

	private SmartMoneyAccountModel getSmartMoneyAccountModel(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException {
		
		SmartMoneyAccountModel smartMoneyAccountModel = null;
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(_smartMoneyAccountModel); 
		searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
		List<SmartMoneyAccountModel> smartMoneyAccounts = searchBaseWrapper.getCustomList().getResultsetList() ;
		
		if( smartMoneyAccounts.size() == 1) {
			smartMoneyAccountModel = (SmartMoneyAccountModel)smartMoneyAccounts.get(0) ;
		}
		
		return smartMoneyAccountModel;
	}
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		
				
		if( pin == null || pin.equals("") )
			pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
			
		pin = StringUtil.replaceSpacesWithPlus(pin);		
		newPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NEW_PIN);
		newPin = StringUtil.replaceSpacesWithPlus(newPin);
		confirmPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CONF_PIN);
		confirmPin = StringUtil.replaceSpacesWithPlus(confirmPin);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VeriflyPinChangeCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
		validationErrors = ValidatorWrapper.doRequired(newPin,validationErrors,"New Pin");
		validationErrors = ValidatorWrapper.doRequired(confirmPin,validationErrors,"Confirm Pin");
		//validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");
        
        if(!validationErrors.hasValidationErrors())
        {
             byte enc_type = new Byte(encryption_type).byteValue();
             ThreadLocalEncryptionType.setEncryptionType(enc_type);
        }
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VeriflyPinChangeCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		if(!deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()) || !incorrectMPIN) {
			if (errorMessagesFlag && Long.parseLong(deviceTypeId) != DeviceTypeConstantsInterface.USSD.longValue()) {


				strBuilder.append(TAG_SYMBOL_OPEN)
						.append(TAG_MESGS)
						.append(TAG_SYMBOL_CLOSE)
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_MESG)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_LEVEL)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(ATTR_LEVEL_ONE)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE);
				if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
					strBuilder.append(MessageParsingUtils.parseMessageForIpos(this.getMessageSource().getMessage("veriflyPinChangeCommand.veriflyPinChangedSuccessfully", null, null)));
				} else {
					strBuilder.append(this.getMessageSource().getMessage("veriflyPinChangeCommand.veriflyPinChangedSuccessfully", null, null));
				}

				strBuilder.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_MESG)
						.append(TAG_SYMBOL_CLOSE)
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_MESGS)
						.append(TAG_SYMBOL_CLOSE);

			}
		}
		else{
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
					.append(TAG_SYMBOL_CLOSE);
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("CODE")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(Long.valueOf("143")).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
					.append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

			return  strBuilder.toString();
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.toXML()");
		}
		return strBuilder.toString();
	}
}