package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_FAV_NUM;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FAV_NUM_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL_ONE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_REQ_ID;
import static com.inov8.microbank.common.util.XMLConstants.TAG_FAVS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_FAV_NUM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.favoritenumbermodule.FavoriteNumberListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MessageParsingUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

public class GetFavoriteNumbersCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String deviceTypeId;
	List<FavoriteNumberListViewModel> list;
	UserDeviceAccountsModel userDeviceAccountsModel;
	
	protected final Log logger = LogFactory.getLog(GetFavoriteNumbersCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetFavoriteNumbersCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		try 
		{
			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationError.hasValidationErrors())
			{
				LinkedHashMap<String,SortingOrder> sortingOrderMap = new LinkedHashMap();
				//FavoriteNumbersModel favoriteNumbersModel = new FavoriteNumbersModel();
				FavoriteNumberListViewModel favoriteNumbersModel = new FavoriteNumberListViewModel();
				
				favoriteNumbersModel.setAppUserId(appUserModel.getAppUserId());
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(favoriteNumbersModel);
				sortingOrderMap.put("name", SortingOrder.ASC);
				searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
				searchBaseWrapper = commonCommandManager.getFavoriteNumbers(searchBaseWrapper);
				list = searchBaseWrapper.getCustomList().getResultsetList();
				
			}
			else
			{
				throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch (FrameworkCheckedException ex) 
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex); 
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetFavoriteNumbersCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetFavoriteNumbersCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetFavoriteNumbersCommand.prepare()");
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
			logger.debug("Start of GetFavoriteNumbersCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
			
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of GetFavoriteNumbersCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of GetFavoriteNumbersCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		if(list != null && list.size() > 0)
		{
			strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_FAVS)
				.append(TAG_SYMBOL_CLOSE);
			for(FavoriteNumberListViewModel localFavoriteNumberModel:list)
			{
				strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_FAV_NUM)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_FAV_NUM)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(localFavoriteNumberModel.getFavoriteNumber())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_FAV_NUM_TYPE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(localFavoriteNumberModel.getFavoriteType())
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_REQ_ID)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(localFavoriteNumberModel.getFavoriteNumbersId())
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_CLOSE)
					.append(localFavoriteNumberModel.getName())
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_FAV_NUM)
					.append(TAG_SYMBOL_CLOSE);
			}
			strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_FAVS)
				.append(TAG_SYMBOL_CLOSE);
		}
		else
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
			
				strBuilder.append(MessageParsingUtils.parseMessageForIpos(this.getMessageSource().getMessage("getFavoriteNumbersCommand.favoriteNumbersNotExist", null,null)));
			}
			else
			{
				strBuilder.append(this.getMessageSource().getMessage("getFavoriteNumbersCommand.favoriteNumbersNotExist", null,null));
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
			logger.debug("End of GetFavoriteNumbersCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
