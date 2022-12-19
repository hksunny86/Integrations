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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.FavoriteNumbersModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;


public class EditFavoriteNumbersCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String deviceTypeId;
	protected String favoriteNumberId;
	protected String favoriteNumber;
	protected String favoriteNumberTag;
	
	
	protected final Log logger = LogFactory.getLog(EditFavoriteNumbersCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SetFavoriteNumbersCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		try
		{
			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationError.hasValidationErrors())
			{
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				FavoriteNumbersModel favoriteNumbersModel = new FavoriteNumbersModel();
				favoriteNumbersModel.setAppUserId(appUserModel.getAppUserId());
				favoriteNumbersModel.setFavoriteNumbersId(Long.parseLong(favoriteNumberId));
				favoriteNumbersModel.setFavoriteNumber(favoriteNumber);
				favoriteNumbersModel.setName(favoriteNumberTag);
				baseWrapper.setBasePersistableModel(favoriteNumbersModel);
				
				baseWrapper = commonCommandManager.updateFavoriteNumber(baseWrapper);
			}
			else
			{
				throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{	
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.HIGH,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SetFavoriteNumbersCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SetFavoriteNumbersCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		favoriteNumberId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FAV_NUM_ID);
		favoriteNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FAV_NUM);
		favoriteNumberTag = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FAV_NUM_TAG);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SetFavoriteNumbersCommand.prepare()");
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
			logger.debug("Start of SetFavoriteNumbersCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		validationErrors = ValidatorWrapper.doRequired(favoriteNumberId, validationErrors, "Favorite Number Id");
		validationErrors = ValidatorWrapper.doRequired(favoriteNumber, validationErrors, "Favorite Number");
		validationErrors = ValidatorWrapper.doRequired(favoriteNumberTag, validationErrors, "Favorite Number Tag");
			
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(favoriteNumberId, validationErrors, "Favorite Number Id");
		}
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(favoriteNumber, validationErrors, "Favorite Number");
		}
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = checkFields(validationErrors);
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SetFavoriteNumbersCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SetFavoriteNumbersCommand.toXML()");
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
			.append(this.getMessageSource().getMessage("favoriteNumbersCommand.favoriteNumberEditedSuccessfully", null,null))
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
			logger.debug("Start of SetFavoriteNumbersCommand.toXML()");
		}
		return strBuilder.toString();
	}

	
	private ValidationErrors checkFields(ValidationErrors valErrors)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SetFavoriteNumbersCommand.checkFields()");
		}

		if(favoriteNumber == null || "".equals(favoriteNumber) || favoriteNumberTag == null || "".equals(favoriteNumberTag))
		{
			valErrors.getStringBuilder().append("Any Name or Number or Type cannot be Empty");
		}
		else
		{
			if(!(favoriteNumber.length() <= CommandFieldConstants.KEY_FAVORITE_NUMBER_LENGTH))
			{
				valErrors.getStringBuilder().append("Any Name or Number Length should be less than 50 Characters");
			}
			else if(!(favoriteNumberTag.length() <= CommandFieldConstants.KEY_FAVORITE_NAME_LENGTH))
			{
				valErrors.getStringBuilder().append("Any Name or Number Length should be less than 50 Characters");
			}
		}
	
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SetFavoriteNumbersCommand.checkFields()");
		}
		return valErrors;
	}
	
	
	 
	
	
	
	
}
