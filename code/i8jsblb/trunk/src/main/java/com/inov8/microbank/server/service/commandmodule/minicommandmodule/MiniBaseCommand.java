package com.inov8.microbank.server.service.commandmodule.minicommandmodule;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MiniCommandLogModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;


/**
 * 
 * @author Jawwad Farooq
 * July, 2007
 * 
 */

public abstract class MiniBaseCommand extends BaseCommand
{

	protected String response = "";
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected void sendSMSToUser(String mobileNo, String message) throws CommandException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SmsMessage smsMessage = new SmsMessage(mobileNo, message, this.getMessageSource().getMessage("MINI.shortCode", null, null));
		baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, smsMessage);
		try
		{
			this.getCommonCommandManager().sendSMSToUser(baseWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			throw new CommandException(this.getMessageSource().getMessage("mfsRequestHandler.unknownError", null, null),
					ErrorCodes.UNKNOWN_ERROR, ErrorLevel.MEDIUM, new Throwable());
		}
	}
	
	protected void saveMiniCommandLog(Long commandId) throws CommandException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		MiniCommandLogModel miniCommandLogModel = new MiniCommandLogModel() ;
		miniCommandLogModel.setCommandId(commandId) ;
		miniCommandLogModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
		
		baseWrapper.setBasePersistableModel( miniCommandLogModel ) ;

		try
		{
			this.getCommonCommandManager().saveMiniCommandLogRequiresNewTransaction(baseWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			throw new CommandException(this.getMessageSource().getMessage("mfsRequestHandler.unknownError", null, null),
					ErrorCodes.UNKNOWN_ERROR, ErrorLevel.MEDIUM, new Throwable());
		}
	}

	protected String generateOneTimeVeriflyPIN(SmartMoneyAccountModel smartMoneyAccountModel) throws CommandException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		boolean errorMessagesFlag;

		try
		{
			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			VeriflyManager veriflyManager = this.getCommonCommandManager().loadVeriflyManagerByAccountId(baseWrapper);

			if (veriflyManager != null)
			{
				AccountInfoModel accountInfoModel = new AccountInfoModel();
				accountInfoModel.setCustomerId(smartMoneyAccountModel.getCustomerId());
				accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());

				LogModel logModel = new LogModel();
				logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
				logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

				veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
				veriflyBaseWrapper.setLogModel(logModel);

				veriflyBaseWrapper = this.getCommonCommandManager().generateOneTimeVeriflyPIN(veriflyManager, veriflyBaseWrapper);
				errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

				if (errorMessagesFlag)
				{
					return veriflyBaseWrapper.getAccountInfoModel().getOtPin();
				}				
			}
		}
		catch (FrameworkCheckedException e)
		{
			e.printStackTrace();
			throw new CommandException("", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new CommandException("", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		throw new CommandException("", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
	}

	protected boolean isMiniTransInProcess() throws CommandException
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		MiniTransactionModel miniTransactionModel = new MiniTransactionModel();

		try
		{
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

				/************************************************************************************************************
				 * ********************************************Bug 1092 Fix**************************************************
				 ***********************************************************************************************************/
				
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
					long transValidityInMilliSecs = Integer.valueOf(this.getMessageSource().getMessage(
							"MINI.OneTimeUSSDVeriflyPINValidityInMins", null, null)) * 60 * 1000;
					long timeDiff = System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime();
	
					if (timeDiff <= transValidityInMilliSecs)
						return true;
					else
						return false;
				}
			}
		}
		catch (FrameworkCheckedException e)
		{
			logger.error("[MiniBaseCommand.isMiniTransInProcess] Exception occured for validating MiniTransactionModel. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + e.getMessage());
		}

		return false;
	}

}
