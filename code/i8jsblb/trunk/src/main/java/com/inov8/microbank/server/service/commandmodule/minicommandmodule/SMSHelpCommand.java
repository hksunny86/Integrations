
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;


import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.NoSuchMessageException;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MiniHelpKeywordModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;

/**
 * 
 * @author Jawwad Farooq
 * July, 2007
 * 
 */

public class SMSHelpCommand extends MiniBaseCommand
{
	protected String mobileNo;
	protected HashMap parameters = null ;
	protected String firstParam ;
	protected String secondParam ;
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		parameters = (HashMap)((HashMap)baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone() ;
		
		firstParam = (String)parameters.get( CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL ) ; 
		if( parameters.get( CommandFieldConstants.KEY_PARAM + (Integer.valueOf(CommandFieldConstants.PARAM_COUNTER_FIRST_VAL) + 1) ) != null )
			secondParam = " " + (String)parameters.get( CommandFieldConstants.KEY_PARAM + (Integer.valueOf(CommandFieldConstants.PARAM_COUNTER_FIRST_VAL) + 1) ) ;
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		mobileNo = this.removeCountryCode(mobileNo);		
	}

	
	@Override
	public void doValidate() throws CommandException
	{
		ValidationErrors validationErrors = new ValidationErrors();		
		validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");

		if (validationErrors.hasValidationErrors())
		{
			logger.error(validationErrors.getErrors());
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR,
					ErrorLevel.HIGH, new Throwable());
		}
	}

	@Override
	public void execute() throws CommandException
	{		
		logger.info( "*********************************** AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey()  ) ;
		logger.info( "*********************************** UserDeviceId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey()  ) ;
		
		try
		{
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;
			BaseWrapper baseWrapper = new BaseWrapperImpl() ;
			MiniHelpKeywordModel miniHelpKeywordModel = new MiniHelpKeywordModel() ;
			
			if( firstParam == null || firstParam.equalsIgnoreCase("") )
			{				
				response = this.getMessageSource().getMessage("MINI.Help", null, null) ;			
			}
			else 
			{
				String helpKeyword = firstParam == null ? "" : firstParam ;
				helpKeyword +=  secondParam == null ? "" : secondParam ;
				miniHelpKeywordModel.setName( helpKeyword ) ;
				searchBaseWrapper.setBasePersistableModel(miniHelpKeywordModel) ;
				searchBaseWrapper = this.getCommonCommandManager().loadMiniHelpKeyword(searchBaseWrapper) ; 
				
				if( searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 )
				{
					miniHelpKeywordModel = (MiniHelpKeywordModel)searchBaseWrapper.getCustomList().getResultsetList().get(0) ;
					response = this.getMessageSource().getMessage( miniHelpKeywordModel.getHelpMessageId() , null, null) ;
				}
				else
					response = this.getMessageSource().getMessage("MINI.GeneralHelp", null, null) ;
				
			}	
		}
		catch( NoSuchMessageException ex )
		{
			response = this.getMessageSource().getMessage("MINI.GeneralError", null, null) ;			
		}

		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error("Exception thrown by LoginCommand.checkLogin()");
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, ex);
		}		
		
	}


	@Override
	public String response()
	{		
		return response;
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

}

