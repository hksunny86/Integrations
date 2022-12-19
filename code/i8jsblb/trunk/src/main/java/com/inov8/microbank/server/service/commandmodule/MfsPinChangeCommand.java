package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL_ONE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
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
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;

public class MfsPinChangeCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String pin;
	protected String newPin;
	protected String confirmPin;
	protected String userId;
	protected String deviceTypeId;
	protected String encryption_type;
	UserDeviceAccountsModel userDeviceAccountsModel;
	protected String accountId;
	protected String bankId;
	boolean errorMessagesFlag;

	
	protected final Log logger = LogFactory.getLog(MfsPinChangeCommand.class);
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MFSPinChangeCommand.prepare()");
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
		
		/*
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsPinChangeCommand.prepare()");
		}
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		if(deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALL_PAY.toString())){
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			UserDeviceAccountsModel devmodel = new UserDeviceAccountsModel();
			devmodel.setAppUserId(appUserModel.getAppUserId());
			devmodel.setDeviceTypeId(5L);
			searchBaseWrapper.setBasePersistableModel(devmodel );
			try {				
				searchBaseWrapper = getCommonCommandManager().loadUserDeviceAccounts(searchBaseWrapper);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
				devmodel = (UserDeviceAccountsModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
			}
			userId = devmodel.getUserId();
		}else{			
			userId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
		}
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		if(!DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId))
		{
//			pin = this.decryptPin(pin);
		}
		newPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NEW_PIN);
		if(!DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId))
		{
//			newPin = this.decryptPin(newPin);
		}
		confirmPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CONF_PIN);
		if(!DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId))
		{
//			confirmPin = this.decryptPin(confirmPin);
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsPinChangeCommand.prepare()");
		}
		
		*/
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
		
		/*
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsPinChangeCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(userId,validationErrors,"User Id");
		validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"PIN");
		validationErrors = ValidatorWrapper.doRequired(newPin,validationErrors,"New PIN");
		validationErrors = ValidatorWrapper.doRequired(confirmPin,validationErrors,"Confirm PIN");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		
		validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");
        
        if(!validationErrors.hasValidationErrors())
        {
             byte enc_type = new Byte(encryption_type).byteValue();
             ThreadLocalEncryptionType.setEncryptionType(enc_type);
        }

		
		if(!validationErrors.hasValidationErrors())
		{            
			validationErrors = ValidatorWrapper.doNumeric(userId,validationErrors,"User Id");
			if(!DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId)
					&& !DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId))
			{
				validationErrors = ValidatorWrapper.doNumeric(pin,validationErrors,"PIN");
				validationErrors = ValidatorWrapper.doNumeric(newPin,validationErrors,"New PIN");
				validationErrors = ValidatorWrapper.doNumeric(confirmPin,validationErrors,"Confirm PIN");
			}
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		
		if(!validationErrors.hasValidationErrors())
		{
			if( this.deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALL_PAY.toString()) || this.deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALLPAY_WEB.toString()))
				validationErrors = ValidatorWrapper.checkLength(userId,CommandFieldConstants.ALLPAY_USERID_LENGTH,validationErrors,"User Id");
			else
				validationErrors = ValidatorWrapper.checkLength(userId,CommandFieldConstants.KEY_MFS_USER_ID_LENGTH,validationErrors,"User Id");
//			if(DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId))
//			{
//				validationErrors = ValidatorWrapper.checkLength(pin,CommandFieldConstants.KEY_MFS_PASSWORD_LENGTH,validationErrors,"Password");
//				validationErrors = ValidatorWrapper.checkLength(newPin,CommandFieldConstants.KEY_MFS_PASSWORD_LENGTH,validationErrors,"New Password");
//				validationErrors = ValidatorWrapper.checkLength(confirmPin,CommandFieldConstants.KEY_MFS_PASSWORD_LENGTH,validationErrors,"Confirm Password");
//				validationErrors = ValidatorWrapper.doPassword(pin,validationErrors,"Password");
//				validationErrors = ValidatorWrapper.doPassword(newPin,validationErrors,"New Password");
//				validationErrors = ValidatorWrapper.doPassword(confirmPin,validationErrors,"Confirm Password");
//				
//				
//				
//			}
//			else
			if(!DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId) 
					&& !DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId) && !DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId))
			{
				validationErrors = ValidatorWrapper.checkLength(pin,CommandFieldConstants.KEY_MFS_PIN_LENGTH,validationErrors,"PIN");
				validationErrors = ValidatorWrapper.checkLength(newPin,CommandFieldConstants.KEY_MFS_PIN_LENGTH,validationErrors,"New PIN");
				validationErrors = ValidatorWrapper.checkLength(confirmPin,CommandFieldConstants.KEY_MFS_PIN_LENGTH,validationErrors,"Confirm PIN");
			}
				
		}

		if(!validationErrors.hasValidationErrors())
		{
			if(DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) || DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId))
			{
				validationErrors = ValidatorWrapper.matchPassword(newPin, confirmPin, validationErrors, "New", "Confirm Password");
			}
			else
			{
				validationErrors = ValidatorWrapper.doEqual(newPin, confirmPin, validationErrors, "New PIN", "Confirm PIN");
			}
			
			
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsPinChangeCommand.validate()");
		}
		return validationErrors;
		*/
	}
	
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
		try
		{
			logger.info("\n\nMfsPinChangeCommand");
			   logger.info(
			      "\nApp User Id = "+appUserModel.getAppUserId() +
			      "\nApp User name = "+appUserModel.getUsername() 
			     +"\nAppUser Type Id "+ appUserModel.getAppUserTypeId()
			     +"\nAppUser Retailer Id "+appUserModel.getRetailerContactId()
			     +"\nAppUser Handler Id "+appUserModel.getHandlerId()
			     +"\nAppUser Customer Id "+appUserModel.getCustomerId()
			     );
			
			if(appUserModel.getCustomerId() != null || appUserModel.getRetailerContactId() != null || appUserModel.getHandlerId() != null)
			{
				ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationError.hasValidationErrors())
				{					
					
					if(StringUtil.isNullOrEmpty(accountId) && ( appUserModel.getHandlerId() != null)) {

						smartMoneyAccountModel.setHandlerId(appUserModel.getHandlerId());
						
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel); 
						searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
						List<SmartMoneyAccountModel> smartMoneyAccounts = searchBaseWrapper.getCustomList().getResultsetList() ;
						
						if( smartMoneyAccounts.size() == 1) {	
							smartMoneyAccountModel = (SmartMoneyAccountModel)smartMoneyAccounts.get(0) ;
							accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
						}						
					}
					
					if( accountId != null && !accountId.equals("") )
						smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
					else if( bankId != null && !bankId.equals("") )
					{
						smartMoneyAccountModel.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
						smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
						smartMoneyAccountModel.setActive(true);
						
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel); 
						
						searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
						
						List<SmartMoneyAccountModel> smartMoneyAccounts = searchBaseWrapper.getCustomList().getResultsetList() ;
						
						if( smartMoneyAccounts.size() > 0 )
						{	
							smartMoneyAccountModel = (SmartMoneyAccountModel)smartMoneyAccounts.get(0) ;
						}
						else
						{
							throw new CommandException(this.getMessageSource().getMessage("command.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());	
						}
					}else{
						if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()){
							smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
						}else{
							smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
						}
						smartMoneyAccountModel.setDeleted(Boolean.FALSE);
						smartMoneyAccountModel.setActive(Boolean.TRUE);
						smartMoneyAccountModel.setBankId(BankConstantsInterface.OLA_BANK_ID);

						SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
						wrapper.setBasePersistableModel(smartMoneyAccountModel);
						
						try {
							
							SearchBaseWrapper searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(wrapper);
							
							if(searchBaseWrapper != null) {
								
								List<SmartMoneyAccountModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();
								
								if(resultsetList != null && !resultsetList.isEmpty()) {
									
									smartMoneyAccountModel = resultsetList.get(0);
								}
							}
							
						} catch (FrameworkCheckedException e) {
							
							throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
						}
						
						logger.debug("Found Smart Money Account "+ smartMoneyAccountModel.getName());
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
										(null != smartMoneyAccountModel.getHandlerId() && smartMoneyAccountModel.getHandlerId().toString().equals(appUserModel.getHandlerId().toString())))	{
									
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
									veriflyBaseWrapper = commonCommandManager.changePIN(veriflyBaseWrapper);																		
									errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();															
									if(!errorMessagesFlag)
									{					
										throw new CommandException(veriflyBaseWrapper.getErrorMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									String smsText = MessageUtil.getMessage("createdPin", new String[]{String.valueOf(new Date())});
									SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), smsText);
									veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, smsMessage);
									this.getCommonCommandManager().getSmsSender().send(smsMessage);
								}
								else
								{
									if(null != smartMoneyAccountModel.getHandlerId() && smartMoneyAccountModel.getHandlerId().longValue() != appUserModel.getHandlerId().longValue())	{
										
									
										throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
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
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(Exception ex)
		{	ex.printStackTrace();
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VeriflyPinChangeCommand.execute()");
		}
		
		/*
		userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsPinChangeCommand.execute()");
		}
		if(!DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) && ! DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId)
				&&  !(DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId)))
		{
			pin = generateEncodedPin(pin);
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		try
		{
			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationError.hasValidationErrors() && !(DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId)) && !(DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId)
					))
			{
				UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
				userDeviceAccountsModel.setPin(pin);
				userDeviceAccountsModel.setUserId(userId); 
				userDeviceAccountsModel.setDeviceTypeId(new Long(deviceTypeId).longValue());
				if(appUserModel.getPrimaryKey() != null)
				{
					userDeviceAccountsModel.setAppUserId(appUserModel.getPrimaryKey());
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
					baseWrapper = commonCommandManager.loadMfs(baseWrapper);
					userDeviceAccountsModel = (UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
												
					if(userDeviceAccountsModel.getUserDeviceAccountsId() != null)
					{
						validationError = commonCommandManager.checkUserCredentials(userDeviceAccountsModel);
						if(!validationError.hasValidationErrors())
						{
							newPin = generateEncodedPin(newPin);
							userDeviceAccountsModel.setPin(newPin);
							userDeviceAccountsModel.setUpdatedOn(new Date());
							userDeviceAccountsModel.setUpdatedByAppUserModel(appUserModel);
							
							userDeviceAccountsModel.setPinChangeRequired(Boolean.FALSE);
							baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
							
							commonCommandManager.updateMfsPin(baseWrapper);
						}
						else
						{
							throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					else
					{
						if( this.deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALL_PAY.toString()) )
							throw new CommandException(this.getMessageSource().getMessage("mfsPinChangeCommand.invalidAllPayPin", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						else
							throw new CommandException(this.getMessageSource().getMessage("mfsPinChangeCommand.invalidMfsPin", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}	
			}
			else if(DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId) || DeviceTypeConstantsInterface.ALLPAY_WEB.toString().equals(deviceTypeId))
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationErrors.hasValidationErrors())
				{
					UserDeviceAccountsModel deviceAccountsModel = new UserDeviceAccountsModel();
//					userDeviceAccountsModel.setPin(pin);
					deviceAccountsModel.setUserId(userId);
					if(appUserModel.getPrimaryKey() != null)
					{
						deviceAccountsModel.setAppUserId(appUserModel.getPrimaryKey());
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						baseWrapper.setBasePersistableModel(deviceAccountsModel);
						baseWrapper = commonCommandManager.loadMfs(baseWrapper);
						deviceAccountsModel = (UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
													
						if(deviceAccountsModel.getPassword().equals(pin))
						{
							validationError = commonCommandManager.checkUserCredentials(deviceAccountsModel);
							if(!validationError.hasValidationErrors())
							{
//								newPin = generateEncodedPin(newPin);
//								userDeviceAccountsModel.setPin(newPin);
								deviceAccountsModel.setUpdatedOn(new Date());
								deviceAccountsModel.setUpdatedByAppUserModel(appUserModel);
								deviceAccountsModel.setPasswordChangeRequired(Boolean.FALSE);
								
								
//								baseWrapper.setBasePersistableModel(deviceAccountsModel);
//								
//								commonCommandManager.updateMfsPin(baseWrapper);
								baseWrapper = new BaseWrapperImpl();
								
//								appUserModel.setPassword(newPin);
								deviceAccountsModel.setPassword(newPin);
								deviceAccountsModel.setUpdatedBy(appUserModel.getAppUserId());
								deviceAccountsModel.setUpdatedOn(new Date());
								baseWrapper.setBasePersistableModel(deviceAccountsModel);
								commonCommandManager.updateMfsPin(baseWrapper);
							}
							else
							{
								throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
						}
						else
						{
							if(DeviceTypeConstantsInterface.MFS_WEB.toString().equals(deviceTypeId))
							{
								throw new CommandException(this.getMessageSource().getMessage("mfsPinChangeCommand.invalidPassword", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
							else
							{
								throw new CommandException(this.getMessageSource().getMessage("allPayPinChangeCommand.invalidPassword", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
						}
					}
				}
				
			}
			else
			{
				throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsPinChangeCommand.execute()");
		}
		
		*/
	}
	
	@Override
	public String response()
	{
		return toXML();
	}
	
	private String toXML()
	{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		if(errorMessagesFlag && Long.parseLong(deviceTypeId) != DeviceTypeConstantsInterface.USSD.longValue())
		{
			
			
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
			if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
			{
				strBuilder.append(MessageParsingUtils.parseMessageForIpos(this.getMessageSource().getMessage("veriflyPinChangeCommand.loginPinChangedSuccessfully", null,null)));
			}
			else
			{
				strBuilder.append(this.getMessageSource().getMessage("veriflyPinChangeCommand.loginPinChangedSuccessfully", null,null));	
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
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.toXML()");
		}
		return strBuilder.toString();
		/*
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsPinChangeCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
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
//		if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
//		{
//			
//			strBuilder.append(MessageParsingUtils.parseMessageForIpos(this.getMessageSource().getMessage("mfsPinChangeCommand.mfsPinChangedSuccessfully", null,null)));
//		}
		if( this.deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALL_PAY.toString()))
		{
			strBuilder.append(this.getMessageSource().getMessage("mfsPinChangeCommand.allPayPinChangedSuccessfully", null,null));
		}
		else if (this.deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.MFS_WEB.toString()))
		{
			strBuilder.append(this.getMessageSource().getMessage("mfsPinChangeCommand.mfsPasswordChangedSuccessfully", null,null));
		}
		else if (this.deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALLPAY_WEB.toString()))
		{
			strBuilder.append(this.getMessageSource().getMessage("mfsPinChangeCommand.allPayPasswordChangedSuccessfully", null,null));
		}
		else
		{
			strBuilder.append(this.getMessageSource().getMessage("mfsPinChangeCommand.mfsPinChangedSuccessfully", null,null));
		}
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_MESG)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_MESGS)
			.append(TAG_SYMBOL_CLOSE);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsPinChangeCommand.toXML()");
		}
		return strBuilder.toString();
		*/
	}
		
}
