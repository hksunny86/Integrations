package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;


public class MfsLoginCommand extends BaseCommand
{
	protected String userId;
	protected String pin;
	protected String appVersionNo;
	protected String catVersionNo;
	protected String deviceTypeId;
	String tickerString;
	String appVersion;
	String bankAccInfo;
	String catalogDetail;
	String loginResponse;
	String applicationName;

	protected final Log logger = LogFactory.getLog(MfsLoginCommand.class);

	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLoginCommand.execute()");
		}
		try
		{
			CommandManager commandManager = this.getCommandManager();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(CommandFieldConstants.KEY_U_ID, userId);
			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			
			// Don't call the Check Application Version command in case of MWallet Web
			if( !deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) )
			{
				baseWrapper.putObject(CommandFieldConstants.KEY_APP_VER, appVersionNo);
				baseWrapper.putObject(CommandFieldConstants.KEY_APP_NAME, applicationName);
				appVersion = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CHK_APP_VER);
				appVersion = removeParams(appVersion);
			}
			
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
			
//			if(deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()))
//				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
//			else	
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			
			loginResponse = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_LOGIN);
			loginResponse = removeParams(loginResponse);
		
			if(deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()))
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
			else	
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			
			tickerString = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_GT_TK_STR);
			tickerString = removeParams(tickerString);
			
			if(deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()))
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
			else	
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			
			bankAccInfo = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_GT_ACC_INFO);
			
			// Don't call the retrieve catalog command in case of MWallet Web
			if( !deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) )
			{
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
				baseWrapper.putObject(CommandFieldConstants.KEY_CAT_VER, catVersionNo);
				catalogDetail = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_UPD_CAT);
			}
			
		}
		catch(FrameworkCheckedException ex)
		{
			
			if(ex instanceof CommandException)
			{
				CommandException cmdEx = (CommandException)ex;
				throw cmdEx;
			}
			else
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLoginCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLoginCommand.prepare()");
		}
		userId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		//pin = this.decryptPin(pin);
		appVersionNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_VER);
		catVersionNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAT_VER);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		applicationName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_NAME);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLoginCommand.prepare()");
		}
	}

	@Override
	public String response()
	{
		return toXML();
	}

	@Override
	public void doValidate() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLoginCommand.doValidate()");
		}
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors = ValidatorWrapper.doRequired(userId,validationErrors,"User Id");
		validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
		
		if( !deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) )
		{
			validationErrors = ValidatorWrapper.doRequired(catVersionNo,validationErrors,"Catalog Version No");
			validationErrors = ValidatorWrapper.doRequired(appVersionNo,validationErrors,"Application Version No");
		}
		
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");

		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(userId,validationErrors,"User Id");
		}
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.checkLength(userId,CommandFieldConstants.KEY_MFS_USER_ID_LENGTH,validationErrors,"User Id");
		}
		if(!validationErrors.hasValidationErrors() && !deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) )
		{
			validationErrors = ValidatorWrapper.doInteger(catVersionNo, validationErrors, "Catalog Version No");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}

		if(validationErrors.hasValidationErrors())
		{
			
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLoginCommand.doValidate()");
		}
	}


	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLoginCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			.append(loginResponse)
			.append(tickerString);
			
			if( !deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) )
			{
				strBuilder.append(appVersion);
			}
			
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE);
			
			if( !deviceTypeId.equals(DeviceTypeConstantsInterface.MFS_WEB.toString()) )
			{
				strBuilder.append(catalogDetail);
			}
			
			strBuilder.append(bankAccInfo);
			
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLoginCommand.toXML()");
		}
		return strBuilder.toString();
	}

	
	private String removeParams(String xmlStr)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLoginCommand.removeParams()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE);
		if(xmlStr != null && !xmlStr.equals("") && xmlStr.contains(strBuilder.toString()))
		{
			xmlStr = xmlStr.replaceAll(strBuilder.toString(), "");
			StringBuilder strBuilderParams = new StringBuilder();
			strBuilderParams.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE);
			xmlStr = xmlStr.replaceAll(strBuilderParams.toString(), "");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLoginCommand.removeParams()");
		}
		return xmlStr;
	}

	
}
