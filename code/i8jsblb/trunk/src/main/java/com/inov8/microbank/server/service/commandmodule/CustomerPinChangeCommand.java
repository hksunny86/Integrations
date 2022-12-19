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
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;


public class CustomerPinChangeCommand extends BaseCommand 
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
		try
		{
			if(appUserModel.getCustomerId() != null)
			{
				ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationError.hasValidationErrors())
				{					
					
					smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
					smartMoneyAccountModel.setDeleted(Boolean.FALSE);
					smartMoneyAccountModel.setActive(Boolean.TRUE);
					smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				    
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
					
					logger.info("Found Smart Money Account "+ smartMoneyAccountModel.getName());
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel); 
		
					
					AbstractFinancialInstitution abstractFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);

					if(abstractFinancialInstitution != null)
					{
						baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper); 
						smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

						
						if(smartMoneyAccountModel.getActive())
						{
							if(smartMoneyAccountModel.getName() != null)
							{
								if((null != smartMoneyAccountModel.getCustomerId() && smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString())) || 
										(null != smartMoneyAccountModel.getRetailerContactId() && smartMoneyAccountModel.getRetailerContactId().toString().equals(appUserModel.getRetailerContactId().toString())))
								{
									AccountInfoModel accountInfoModel = new AccountInfoModel();
									accountInfoModel.setCustomerId(appUserModel.getCustomerId());
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
									veriflyBaseWrapper.setSkipPanCheck(Boolean.TRUE);
									veriflyBaseWrapper.putObject(SwitchConstants.KEY_SWITCHWRAPPER,switchWrapper);
									
																		
									veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
									
									veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId) );
									
									veriflyBaseWrapper = abstractFinancialInstitution.changePin(veriflyBaseWrapper);
									
																		
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
										throw new CommandException(veriflyErrorMessage,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
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
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(Exception ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VeriflyPinChangeCommand.execute()");
		}
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
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		
				
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
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.toXML()");
		}
		return strBuilder.toString();
	}
}
