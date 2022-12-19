package com.inov8.microbank.server.service.commandmodule.minicommandmodule;


import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MiniCommandKeywordModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
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

public class PINCommand extends MiniBaseCommand
{

	protected String mobileNo;
	protected HashMap parameters = null;
	protected String firstParam;
	protected final Log logger = LogFactory.getLog(getClass());
	protected String transactionValidity;

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		parameters = (HashMap) ((HashMap) baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone();

		firstParam = (String) parameters.get(CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL);
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		mobileNo = this.removeCountryCode(mobileNo);
		
		transactionValidity = this.getMessageSource().getMessage("MINI.OneTimeVeriflyPINValidityInMins", null, null);
		
	}

	@Override
	public void doValidate() throws CommandException
	{
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");

		if (validationErrors.hasValidationErrors())
		{
			logger.error(validationErrors.getErrors());
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,
					new Throwable());
		}

		if (firstParam == null || firstParam.equalsIgnoreCase(""))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidPIN", new Object[] { firstParam }, null);
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
		}
	}

	@Override
	public void execute() throws CommandException
	{
		logger.info("*********************************** AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey());
		logger.info("*********************************** UserDeviceId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey());

		MiniTransactionModel miniTransactionModel = null;
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		BaseWrapper baseWrapper = new BaseWrapperImpl();

		try
		{
			/**
			 * Load the Mini transactions within 15mins
			 */
			miniTransactionModel = new MiniTransactionModel();

			// Mark all previous transactions as expired........
			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
			miniTransactionModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			searchBaseWrapper.setBasePersistableModel(miniTransactionModel);
			searchBaseWrapper = this.getCommonCommandManager().loadMiniTransaction(searchBaseWrapper);

			List<MiniTransactionModel> miniTransactionModelList = searchBaseWrapper.getCustomList().getResultsetList();
			if (miniTransactionModelList.size() > 0)
			{
				miniTransactionModel = miniTransactionModelList.get(0);

				//Check the 15 mins validity
				long transValidityInMilliSecs = Integer.valueOf(transactionValidity) * 60 * 1000;
				long timeDiff = System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime();

				if (timeDiff <= transValidityInMilliSecs)
				{
					MiniCommandKeywordModel miniCommandKeywordModel = new MiniCommandKeywordModel() ;
					miniCommandKeywordModel.setCommandId(miniTransactionModel.getCommandId());
					searchBaseWrapper.setBasePersistableModel(miniCommandKeywordModel) ;
					searchBaseWrapper = this.getCommonCommandManager().loadMiniCommand(searchBaseWrapper) ;
					
					if( searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 )
					{
						miniCommandKeywordModel = (MiniCommandKeywordModel)searchBaseWrapper.getCustomList().getResultsetList().get(0) ;
						
						saveMiniCommandLog( miniTransactionModel.getCommandId() );
						
						// Resume the transaction 
						baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
						baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, miniCommandKeywordModel.getProductId());
						baseWrapper.putObject(CommandFieldConstants.KEY_PARAM_HASHMAP, parameters);
						baseWrapper.putObject(CommandFieldConstants.KEY_PARAM_SMS_TEXT, miniTransactionModel.getSmsText());
						baseWrapper.putObject(CommandFieldConstants.KEY_PARAM_PIN, firstParam);
						baseWrapper.putObject(CommandFieldConstants.KEY_DO_PAY_BILL, true);
						baseWrapper.putObject(CommandFieldConstants.KEY_MINI_TX_MODEL, miniTransactionModel);
						response = this.getCommandManager().executeCommand(baseWrapper,miniTransactionModel.getCommandId().toString());

						miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.COMPLETED);
					}
					else
					{
						logger.info(" PINCommand.execute() Line 126 MiniCommandKeyword with PK:" + miniTransactionModel.getCommandId() + " not found " );
						response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[] { firstParam }, null);
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
					}
				}
				else
				{
					miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.EXPIRED);
					
					logger.error("Exception thrown by PINCommand.execute()");
					response = this.getMessageSource().getMessage("MINI.ExpiredPIN", null, null);
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}
			}
			else
			{
				logger.error("Exception thrown by PINCommand.execute()");
				response = this.getMessageSource().getMessage("MINI.NoTransInProcess", null, null);
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);				
			}
		}
		catch (FrameworkCheckedException ex)
		{
			if( ex instanceof CommandException )
			{
				if( !ex.getMessage().equalsIgnoreCase( this.getMessageSource().getMessage("MINI.IncorrectTransPIN", null, null) ) )
					miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.UNSUCCESSFUL);
			}
			else
				miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.UNSUCCESSFUL);
			
			logger.error("Exception thrown by LoginCommand.checkLogin()");			

			if (ex.getMessage().equalsIgnoreCase(""))
			{
				response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[] { firstParam }, null);
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
			}
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}
		catch( Exception ex )
		{
			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.UNSUCCESSFUL);
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}
		finally
		{
			if( miniTransactionModel != null && miniTransactionModel.getPrimaryKey() != null )
			{
				miniTransactionModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
				baseWrapper.setBasePersistableModel(miniTransactionModel);
				try
				{
					this.getCommonCommandManager().updateMiniTransactionRequiresNewTransaction(baseWrapper);
				}
				catch (FrameworkCheckedException e)
				{				
					e.printStackTrace();
				}
			}
			
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
