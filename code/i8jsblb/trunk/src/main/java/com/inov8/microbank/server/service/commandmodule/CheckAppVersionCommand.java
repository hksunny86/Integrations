package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppVersionModel;
import com.inov8.microbank.common.model.appversionmodule.AppVersionListViewModel;
import com.inov8.microbank.common.util.ApplicationNamesConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;


public class CheckAppVersionCommand extends BaseCommand 
{
	protected String appVersionNo;
	protected AppUserModel appUserModel;
	protected String appUsageLevel;
	protected String deviceTypeId;
	String latestAppVersionNo;
	String mfsId;
	String applicationName = "";
	
	protected final Log logger = LogFactory.getLog(CheckAppVersionCommand.class);
	
	@Override
	public void execute() throws CommandException 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAppVersionCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		try
		{
			AppVersionModel appVersionModel = new AppVersionModel();
			appVersionModel.setAppVersionNumber(appVersionNo);
			appVersionModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(appVersionModel);
			baseWrapper = commonCommandManager.checkAppVersion(baseWrapper);
			appVersionModel = (AppVersionModel)baseWrapper.getBasePersistableModel();
			if(appVersionModel.getAppVersionId() != null)
			{
				AppVersionListViewModel appVersionListViewModel = new AppVersionListViewModel();
				appVersionListViewModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
				baseWrapper.setBasePersistableModel(appVersionListViewModel);
				baseWrapper = commonCommandManager.getLatestAppVersion(baseWrapper);
				appVersionListViewModel = (AppVersionListViewModel)baseWrapper.getBasePersistableModel();
				latestAppVersionNo = appVersionListViewModel.getAppVersionNumber();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//				UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
//				userDeviceAccountsModel.setUserId(mfsId);
//				searchBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);
//				searchBaseWrapper = commonCommandManager.loadUserDeviceAccounts(searchBaseWrapper);
//				if(null != searchBaseWrapper.getCustomList() && null != searchBaseWrapper.getCustomList().getResultsetList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
//				{
//					userDeviceAccountsModel = (UserDeviceAccountsModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
//				}
				if(!appVersionModel.getBlackListed())
				{
					if(Double.parseDouble(appVersionNo) == Double.parseDouble(appVersionListViewModel.getAppVersionNumber()))
					{
						appUsageLevel = XMLConstants.TAG_APP_LEVEL_NORMAL;
					}
					else if((Double.parseDouble(appVersionNo)) >= (Double.parseDouble(appVersionListViewModel.getToCompatibleVersion())) 
							&& (Double.parseDouble(appVersionNo)) <= (Double.parseDouble(appVersionListViewModel.getFromCompatibleVersion())))
					{
						appUsageLevel = XMLConstants.TAG_APP_LEVEL_CRITICAL;
					}
					else
					{
						appUsageLevel = XMLConstants.TAG_APP_LEVEL_Obsolete;
						
						if(applicationName.equalsIgnoreCase(ApplicationNamesConstantsInterface.ALLPAY_APPLICATION_NAME) || 
								deviceTypeId.equals(DeviceTypeConstantsInterface.ALL_PAY.toString()) )
						{																   
							throw new CommandException(this.getMessageSource().getMessage("checkAppVersionCommand.blackListObsoleteAGENTmateVersionNumber", null,null),ErrorCodes.APP_VERSION_BLACKLIST_OBSOLETE,ErrorLevel.MEDIUM,new Throwable());
						}
						throw new CommandException(this.getMessageSource().getMessage("checkAppVersionCommand.blackListObsoleteAGENTmateVersionNumber", null,null),ErrorCodes.APP_VERSION_BLACKLIST_OBSOLETE,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else 
				{
					appUsageLevel = XMLConstants.TAG_APP_LEVEL_BLOCK;
					
					if(applicationName.equalsIgnoreCase(ApplicationNamesConstantsInterface.ALLPAY_APPLICATION_NAME) || 
							deviceTypeId.equals(DeviceTypeConstantsInterface.ALL_PAY.toString()) )
					{
						throw new CommandException(this.getMessageSource().getMessage("checkAppVersionCommand.blackListObsoleteAGENTmateVersionNumber", null,null),ErrorCodes.APP_VERSION_BLACKLIST_OBSOLETE,ErrorLevel.MEDIUM,new Throwable());
					}
					throw new CommandException(this.getMessageSource().getMessage("checkAppVersionCommand.blackListObsoleteAGENTmateVersionNumber", null,null),ErrorCodes.APP_VERSION_BLACKLIST_OBSOLETE,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			else
			{
				throw new CommandException(this.getMessageSource().getMessage("checkAppVersionCommand.invalidVersionNumber", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
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
			logger.debug("End of CheckAppVersionCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAppVersionCommand.prepare()");
		}
		appVersionNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_VER);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		mfsId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
		applicationName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_APP_NAME);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAppVersionCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CheckAppVersionCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAppVersionCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(appVersionNo,validationErrors,"AppVersionNo");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAppVersionCommand.validate()");
		}
		return validationErrors;
	}
	@Override
	public void doValidate() throws CommandException
	{
		
	}
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAppVersionCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_APP_VER)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(latestAppVersionNo)
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
			.append(CommandFieldConstants.KEY_APP_USAGE_LVL)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(appUsageLevel)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAppVersionCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
