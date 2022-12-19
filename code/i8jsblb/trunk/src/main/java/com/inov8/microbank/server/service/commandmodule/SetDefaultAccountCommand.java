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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;


public class SetDefaultAccountCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String accountId;
	protected String deviceTypeId;
	String returnMessage;
	
	protected final Log logger = LogFactory.getLog(SetDefaultAccountCommand.class);
	
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SetDefaultAccountCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager(); 
		try
		{
			ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationErrors.hasValidationErrors())
			{
				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();	
				if(appUserModel.getCustomerId() != null)
				{
					smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
					smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
					if(smartMoneyAccountModel.getName() != null && smartMoneyAccountModel.getCustomerId().equals(appUserModel.getCustomerId()))
					{
						if(smartMoneyAccountModel.getActive())
						{
							if(smartMoneyAccountModel.getDefAccount() == false)
							{
								smartMoneyAccountModel.setUpdatedOn(new Date());
								smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
								baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
								commonCommandManager.setDefaultSmartMoneyAccount(baseWrapper);
								returnMessage = this.getMessageSource().getMessage("setDefaultAccountCommand.defaultAccountSuccessMessage", null,null);
							}
							else
							{
								returnMessage = this.getMessageSource().getMessage("setDefaultAccountCommand.alreadyDefaultAccount", null,null);
							}
						}
						else
						{
							returnMessage = this.getMessageSource().getMessage("setDefaultAccountCommand.inaciveAccount", null,null);
						}
					}
					else
					{
						returnMessage = this.getMessageSource().getMessage("setDefaultAccountCommand.accountIdNotExistsAgainstCustomer", null,null);
					}
				}
				else
				{
					throw new CommandException(this.getMessageSource().getMessage("setDefaultAccountCommand.commandForCustomerOnly", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			else
			{
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(Exception ex)
		{
			returnMessage = this.getMessageSource().getMessage("setDefaultAccountCommand.accountIdNotExists", null,null);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SetDefaultAccountCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SetDefaultAccountCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SetDefaultAccountCommand.prepare()");
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
			logger.debug("Start of SetDefaultAccountCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
			
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SetDefaultAccountCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SetDefaultAccountCommand.toXML()");
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
			.append(TAG_SYMBOL_CLOSE)
			.append(returnMessage)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_MESG)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_MESGS)
			.append(TAG_SYMBOL_CLOSE);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SetDefaultAccountCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
