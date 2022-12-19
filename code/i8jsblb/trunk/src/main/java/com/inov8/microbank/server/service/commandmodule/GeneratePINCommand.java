package com.inov8.microbank.server.service.commandmodule;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			February 2008  			
 * Description:				
 */

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

import java.util.List;

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
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MessageParsingUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;


public class GeneratePINCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String bankId;
	protected String pin;
	protected String newPin;
	protected String confirmPin;
	protected String deviceTypeId;
	String veriflyErrorMessage;
	boolean errorMessagesFlag;
	UserDeviceAccountsModel userDeviceAccountsModel;
	
	protected final Log logger = LogFactory.getLog(VeriflyPinChangeCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.execute()");
		}
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
					if( bankId != null && !bankId.equals("") )
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
								if(smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString()))
								{
//									LogModel logModel = new LogModel();
//									logModel.setCreatedBy(appUserModel.getUsername());
//									logModel.setCreatdByUserId(appUserModel.getAppUserId());
									
									SwitchWrapper switchWrapper = new SwitchWrapperImpl();
									veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId) ) ;
									switchWrapper.setBankId( smartMoneyAccountModel.getBankId() ) ;
									switchWrapper.setPaymentModeId( smartMoneyAccountModel.getPaymentModeId() ) ;
									switchWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, newPin);
									//switchWrapper.putObject(CommandFieldConstants.KEY_PIN, confirmPin);
									switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
									veriflyBaseWrapper.putObject( SwitchConstants.KEY_SWITCHWRAPPER, switchWrapper ) ;
									veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
									userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
									
									veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);									
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
		bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
//		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
//		pin = StringUtil.replaceSpacesWithPlus(pin);
		newPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NEW_PIN);
		newPin = StringUtil.replaceSpacesWithPlus(newPin);
		confirmPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CONF_PIN);
		confirmPin = StringUtil.replaceSpacesWithPlus(confirmPin);
		newPin = this.decryptPin(newPin);
		confirmPin = this.decryptPin(confirmPin);
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
		//validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
		validationErrors = ValidatorWrapper.doRequired(newPin,validationErrors,"New Pin");
		validationErrors = ValidatorWrapper.doRequired(confirmPin,validationErrors,"Confirm Pin");
		validationErrors = ValidatorWrapper.doRequired(bankId,validationErrors,"Bank Id");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		
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
					strBuilder.append(MessageParsingUtils.parseMessageForIpos(this.getMessageSource().getMessage("generatePIN", null,null)));
				}
				else
				{
					strBuilder.append(this.getMessageSource().getMessage("generatePIN", null,null));
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
			logger.debug("Start of VeriflyPinChangeCommand.toXML()");
		}
		return strBuilder.toString();
	}
}
