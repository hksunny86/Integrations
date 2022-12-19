
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
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

public class CancelTransactionCommand extends MiniBaseCommand
{

	protected String mobileNo;
	protected HashMap parameters = null;
	protected String firstParam;
	protected final Log logger = LogFactory.getLog(getClass());
	protected String transactionValidity = "15";

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		parameters = (HashMap) ((HashMap) baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone();
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
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,
					new Throwable());
		}
	}

	@Override
	public void execute() throws CommandException
	{
		logger.info("[CancelTransactionCommand.execute] Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + 
					" UserDeviceId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey());

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
			miniTransactionModel.setCommandId(Long.parseLong(CommandFieldConstants.CMD_MINI_CASHOUT));
			searchBaseWrapper.setBasePersistableModel(miniTransactionModel);
			searchBaseWrapper = this.getCommonCommandManager().loadMiniTransaction(searchBaseWrapper);

			List<MiniTransactionModel> miniTransactionModelList = searchBaseWrapper.getCustomList().getResultsetList();
			if (miniTransactionModelList.size() > 0)
			{
				miniTransactionModel = miniTransactionModelList.get(0);

				Iterator<MiniTransactionModel> i = miniTransactionModelList.iterator();
				while (i.hasNext()) {
					MiniTransactionModel model = i.next(); // must be called before you can call i.remove()
				   
					if(model.getCommandId().longValue() == Long.parseLong(CommandFieldConstants.CMD_ACCOUNT_TO_CASH)
							|| model.getCommandId().longValue() == Long.parseLong(CommandFieldConstants.CMD_CASH_TO_CASH_INFO)){
						
						   i.remove();//remove account to cash/cash to cash minitransation objects. 
					}
				}
				
				if (miniTransactionModelList.size() > 0)
				{
					miniTransactionModel = miniTransactionModelList.get(0);

					//Check the 15 mins validity
					long transValidityInMilliSecs = Integer.valueOf(transactionValidity) * 60 * 1000;
					long timeDiff = System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime();
	
					if (timeDiff <= transValidityInMilliSecs)
					{
						miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.CANCELLED);
						baseWrapper.setBasePersistableModel(miniTransactionModel);
						this.getCommonCommandManager().updateMiniTransactionRequiresNewTransaction(baseWrapper);
						response = this.getMessageSource().getMessage("MINI.TransCancelled", null, null);					
						logger.info("[CancelTransactionCommand.execute] Transaction cancelled Successfully. Going to Update Transaction Detail Master for reporting. Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey());
						
						TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
						transactionDetailMasterModel.setTransactionCodeId(miniTransactionModel.getTransactionCodeId());
						transactionDetailMasterModel.setTransactionCode(miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
						searchBaseWrapper.setBasePersistableModel(transactionDetailMasterModel);
						searchBaseWrapper = this.getCommonCommandManager().loadTransactionDetailMaster(searchBaseWrapper);
						transactionDetailMasterModel = (TransactionDetailMasterModel)searchBaseWrapper.getBasePersistableModel();
						if(StringUtils.isNotEmpty(transactionDetailMasterModel.getProcessingStatusName()) && transactionDetailMasterModel.getProcessingStatusName().equals(MiniTransactionStateConstant.PIN_SENT_NAME)){
							transactionDetailMasterModel.setProcessingStatusName(MiniTransactionStateConstant.miniStateNamesMap.get(MiniTransactionStateConstant.CANCELLED));
							this.getCommonCommandManager().saveTransactionDetailMasterModel(transactionDetailMasterModel);
							logger.info("[CancelTransactionCommand.execute] Transaction cancelled Successfully in Transaction Detail Master. Sendig response for Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + ". Response:" + response);
						}
					}
					else
					{
						response = this.getMessageSource().getMessage("MINI.NoTransInProcess", null, null);
						logger.error("[CancelTransactionCommand.execute] Error in cancelling MiniTransction. Throwing exception for Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + ". Response:" + response);
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
					}
				}
				
			}
			else
			{
				response = this.getMessageSource().getMessage("MINI.NoTransInProcess", null, null);				
				logger.error("[CancelTransactionCommand.execute] Throwing exception for Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + ". Response:" + response);
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);				
			}
		}
		catch (FrameworkCheckedException ex)
		{
			if (ex.getMessage().equalsIgnoreCase(""))
			{
				response = this.getMessageSource().getMessage("MINI.TransNotCancelled", new Object[] { firstParam }, null);
				logger.error("[CancelTransactionCommand.execute] Throwing exception for Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + ". Response:" + response);
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
			}
			
			logger.error("[CancelTransactionCommand.execute] Throwing exception for Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + ". Exception Msg:" + ex.getMessage());
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
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
