
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
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

public class ChangeDefaultRecAccCommand extends MiniBaseCommand
{
	protected String mobileNo;
	protected HashMap parameters = null ;
	protected String firstParam ;
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		parameters = (HashMap)((HashMap)baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone() ;
		
		firstParam = (String)parameters.get( CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL ) ; 
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
		
		if( firstParam == null || firstParam.equalsIgnoreCase("") )
		{
			response = this.getMessageSource().getMessage("MINI.WrongAccSerOrNick", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
	}

	@Override
	public void execute() throws CommandException
	{
		
		
		logger.info( "*********************************** AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey()  ) ;
		logger.info( "*********************************** UserDeviceId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey()  ) ;
		
		/*
		try
		{
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;
			BaseWrapper baseWrapper = new BaseWrapperImpl() ;
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel() ;
			
			ValidationErrors validationErrors = ValidatorWrapper.doNumeric( firstParam , new ValidationErrors(), "");
			
			if( !validationErrors.getStringBuilder().toString().equals("") )
				smartMoneyAccountModel.setName( firstParam );		
			
			else			
				smartMoneyAccountModel.setMiniSerialNo( Integer.valueOf( firstParam ) ) ;				
						
			
			smartMoneyAccountModel.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
			searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
			searchBaseWrapper = this.getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper)  ;
			CustomList<SmartMoneyAccountModel> smartMoneyAccountModelList = searchBaseWrapper.getCustomList() ;
			
			if( smartMoneyAccountModelList.getResultsetList() != null && smartMoneyAccountModelList.getResultsetList().size() != 0 )
			{
				smartMoneyAccountModel = smartMoneyAccountModelList.getResultsetList().get(0) ;
				
				//Checking if the SMA is not active
				if( !smartMoneyAccountModel.getActive() )
				{
					logger.error("Exception thrown by LoginCommand.checkLogin()");
					response = this.getMessageSource().getMessage("MINI.InActiveSMA", null, null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
				}
				
				//Checking if SMA linked with Mini
				if( smartMoneyAccountModel.getMiniEnable() )
				{
					SmartMoneyAccountModel tempSMA = new SmartMoneyAccountModel() ;
					tempSMA.setCustomerId( smartMoneyAccountModel.getCustomerId() ) ;
					tempSMA.setDefReceiveAccount(true);
					searchBaseWrapper.setBasePersistableModel(tempSMA);
					
					searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper)  ;
					smartMoneyAccountModelList = searchBaseWrapper.getCustomList() ;
					
					// Change the flag of all others default paying account
					for( int counter=0; smartMoneyAccountModelList.getResultsetList() != null && smartMoneyAccountModelList.getResultsetList().size() > counter ; counter++ )
					{
						tempSMA = smartMoneyAccountModelList.getResultsetList().get(counter) ;
						
						if( tempSMA.getPrimaryKey().longValue() != smartMoneyAccountModel.getPrimaryKey().longValue() )
						{
							tempSMA.setDefReceiveAccount(false) ;
							baseWrapper.setBasePersistableModel(tempSMA) ;
							this.getCommonCommandManager().updateSmartMoneyAccount(baseWrapper);
						}						
					}

					
					smartMoneyAccountModel.setDefReceiveAccount( true ) ;
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					baseWrapper = this.getCommonCommandManager().updateSmartMoneyAccount(baseWrapper) ;
					smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel() ;
				}
				//Not linked with Mini
				else
				{
					logger.error("Exception thrown by LoginCommand.checkLogin()");
					response = this.getMessageSource().getMessage("MINI.NotAMiniAcc", new Object[]{ firstParam }, null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
				}				
			}
			else // If no smart money account with the param, found
			{
				logger.error("Exception thrown by LoginCommand.checkLogin()");
				response = this.getMessageSource().getMessage("MINI.WrongAccSerOrNick", null, null) ;
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
			}	
			
			response = this.getMessageSource().getMessage("MINI.SMARecAccChanged", new Object[]{smartMoneyAccountModel.getName()}, null) ;
			
		}
		catch (FrameworkCheckedException ex)
		{
			logger.error("Exception thrown by LoginCommand.checkLogin()");
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, ex);
		}		
		*/
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

