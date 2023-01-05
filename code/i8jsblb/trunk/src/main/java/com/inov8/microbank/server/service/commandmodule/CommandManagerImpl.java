package com.inov8.microbank.server.service.commandmodule;

import com.inov8.microbank.server.service.consumercommandmodule.CustomerSelfRegistrationBaseCommand;
import com.inov8.microbank.server.service.consumercommandmodule.VerifyCustomerSelfRegistetrationCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.CommandModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

public class CommandManagerImpl implements CommandManager
{
	private CommonCommandManager commonCommandManager;
	private MessageSource messageSource;
	private int numberOfAttempts;
	private int maxTimeForAttempts;
	private static OperatorModel operatorModel;


	protected final Log logger = LogFactory.getLog(CommandManagerImpl.class);

	public String executeCommand(BaseWrapper baseWrapper) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CommandManagerImpl.executeCommand(BaseWrapper baseWrapper)");
		}
		BaseCommand baseCommand;
		String action = baseWrapper.getObject(CommandFieldConstants.KEY_CURR_COMMAND).toString();
		CommandModel commandModel = new CommandModel();
		commandModel.setCommandId(Long.parseLong(action));
		commandModel = loadCommand(commandModel);
		baseCommand = createObjectThroughReflection(commandModel, Long.parseLong(action));
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CommandManagerImpl.executeCommand(BaseWrapper baseWrapper)");
		}
		if(!commandModel.getActive()){
			throw new CommandException("Command is not active",ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
		}
		return runCommand(baseCommand,baseWrapper);
	}

	public String executeCommand(BaseWrapper baseWrapper, String action) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CommandManagerImpl.executeCommand(BaseWrapper baseWrapper, String action)");
		}
		BaseCommand baseCommand;
		CommandModel commandModel = new CommandModel();
		commandModel.setCommandId(Long.parseLong(action));
		commandModel = loadCommand(commandModel);
		baseCommand = createObjectThroughReflection(commandModel,Long.parseLong(action));
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CommandManagerImpl.executeCommand(BaseWrapper baseWrapper, String action)");
		}
		return runCommand(baseCommand,baseWrapper);

	}

	private String runCommand(BaseCommand baseCommand, BaseWrapper baseWrapper) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CommandManagerImpl.runCommand()");
		}
		String resultedCommandString = null;
		baseCommand.doPrepare(baseWrapper);
		baseCommand.doValidate();
		baseCommand.doExecute();
		resultedCommandString = baseCommand.doResponse();
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CommandManagerImpl.runCommand()");
		}
		return resultedCommandString;
	}

	private CommandModel loadCommand(CommandModel commandModel) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CommandManagerImpl.loadCommand()");
		}
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(commandModel);
		try
		{
			baseWrapper = this.getCommonCommandManager().loadCommand(baseWrapper);
			if(logger.isDebugEnabled())
			{
				logger.debug("End of CommandManagerImpl.loadCommand()");
			}
			return (CommandModel)baseWrapper.getBasePersistableModel();
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}

	}

	private BaseCommand createObjectThroughReflection(CommandModel commandModel, long commandId) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CommandManagerImpl.createObjectThroughReflection()");
		}
		BaseCommand commandObject = null;
		try
		{
			commandObject = (BaseCommand) Class.forName(commandModel.getClassName()).newInstance();
			commandObject.setCommandManager(this);
			commandObject.setCommonCommandManager(getCommonCommandManager());
			commandObject.setMessageSource(this.messageSource);
			commandObject.setMaxTimeForAttempts(maxTimeForAttempts);
			commandObject.setNumberOfAttempts(numberOfAttempts);
			loadOperator();
			commandObject.setOperatorModel(this.operatorModel);
			if(logger.isDebugEnabled())
			{
				logger.debug("End of CommandManagerImpl.createObjectThroughReflection()");
			}
			return commandObject;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}
	}

	private OperatorModel loadOperator() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CommandManagerImpl.loadOperator()");
		}
		/*synchronized(this)
		{
			if(this.operatorModel == null)
			{
				operatorModel = new OperatorModel();
				operatorModel.setOperatorId(1L);
				try
				{
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(operatorModel);
					baseWrapper = getCommonCommandManager().loadOperator(baseWrapper);
					operatorModel = (OperatorModel)baseWrapper.getBasePersistableModel();
				}
				catch(FrameworkCheckedException ex)
				{
					this.operatorModel = null;
					throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
				}
			}
		}*/
		operatorModel = new OperatorModel();
		operatorModel.setOperatorId(1L);
		try
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(operatorModel);
			baseWrapper = commonCommandManager.loadOperator(baseWrapper);
			operatorModel = (OperatorModel)baseWrapper.getBasePersistableModel();
		}
		catch(FrameworkCheckedException ex)
		{
			this.operatorModel = null;
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CommandManagerImpl.loadOperator()");
		}
		return operatorModel;
	}


	public void setCommonCommandManager(CommonCommandManager commonCommandManager)
	{
		this.commonCommandManager = commonCommandManager;
	}

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public void setMaxTimeForAttempts(int maxTimeForAttempts)
	{
		this.maxTimeForAttempts = maxTimeForAttempts;
	}

	public void setNumberOfAttempts(int numberOfAttempts)
	{
		this.numberOfAttempts = numberOfAttempts;
	}

	public CommonCommandManager getCommonCommandManager() {
		return commonCommandManager;
	}
}
