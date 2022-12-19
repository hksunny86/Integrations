package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

public class SaveTillBalanceCommand extends BaseCommand{
	
	protected String agentMSISDN;
	protected AppUserModel appUserModel;
	protected String deviceTypeID;
	protected String userID;
	protected String tillBalanceAmount;
	protected final Log logger = LogFactory.getLog(SaveTillBalanceCommand.class);
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SaveTillBalanceCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		userID = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
		agentMSISDN = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		tillBalanceAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TILL_BALANCE_AMOUNT);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SaveTillBalanceCommand.prepare()");
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)
			throws CommandException {
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SaveTillBalanceCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doRequired(userID, validationErrors, "User ID");
		}
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doRequired(agentMSISDN, validationErrors, "Agent MSISDN");
		}
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(tillBalanceAmount, validationErrors, "Till Balance Amount");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SaveTillBalanceCommand.validate()");
		}
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SaveTillBalanceCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		Double balance=null;
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		try{
			switchWrapper = this.getCommonCommandManager().checkAgentBalance(); 
			double bal = switchWrapper.getBalance();
			balance = new Double(bal);
		}
		catch(Exception e)
		{
			//Eat the exception because we have to save the following record no matter what.
		}
		
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.AM_PM,Calendar.AM);
		try
			{
			
			AgentOpeningBalModel agentOpeningBalModel = new AgentOpeningBalModel();
			agentOpeningBalModel.setMsisdn(agentMSISDN);
			agentOpeningBalModel.setAgentId(userID);
			agentOpeningBalModel.setOpeningTillBalance(Double.parseDouble(tillBalanceAmount));
			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(tillBalanceAmount));
			agentOpeningBalModel.setBalDate(calendar.getTime());
			agentOpeningBalModel.setCreatedOn(new Date());
			agentOpeningBalModel.setUpdatedOn(new Date());
			
			agentOpeningBalModel.setCreatedBy(appUserModel.getAppUserId());
			agentOpeningBalModel.setUpdatedBy(appUserModel.getAppUserId());
			if(null != switchWrapper.getAccountInfoModel())
			{
				agentOpeningBalModel.setAccountNumber(switchWrapper.getAccountInfoModel().getAccountNo());
			}
			if(null != balance)
			{
				agentOpeningBalModel.setOpeningAccountBalance(balance);
				agentOpeningBalModel.setRunningAccountBalance(balance);
			}
			baseWrapper.setBasePersistableModel(agentOpeningBalModel);
			baseWrapper = commonCommandManager.saveTillBalance(baseWrapper);
			if(null == balance)
			{
				throw new CommandException("Your Core Banking Account cannot be accessed at the moment. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR,
						ErrorLevel.MEDIUM);
			}
		}
		catch(CommandException ce)
		{
			if(logger.isErrorEnabled())
			{
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(ce));
			}
			throw new CommandException(ce.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ce);
		}
		catch(FrameworkCheckedException ex)
		{
			if(logger.isErrorEnabled())
			{
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		
		catch(WorkFlowException wex)
		{
			if(logger.isErrorEnabled())
			{
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
			}
			throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
		}
		catch(Exception ex)
		{
			if(logger.isErrorEnabled())
			{
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		
		if(!validationErrors.hasValidationErrors())
		{	
		}
		else
		{
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SaveTillBalanceCommand.execute()");
		}
		
	}

	@Override
	public String response() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAMS)
		.append(TAG_SYMBOL_CLOSE)
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAMS)
		.append(TAG_SYMBOL_CLOSE);
		return strBuilder.toString();
	}

}
