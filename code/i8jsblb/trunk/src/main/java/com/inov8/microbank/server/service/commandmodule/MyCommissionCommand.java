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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AgentEarnedComSummaryModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MyCommissionEnum;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

public class MyCommissionCommand extends BaseCommand{
	
	protected AppUserModel appUserModel;
	String commissionOption;
	String deviceTypeId;
	Double agentCommission;
	Date commissionStartDate=null;
	Date commissionEndDate=null;
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	protected final Log logger = LogFactory.getLog(MyCommissionCommand.class);
	

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MyCommissionCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		commissionOption = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMMISSION_OPTION);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MyCommissionCommand.prepare()");
		}
		
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)
			throws CommandException {
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MyCommissionCommand.validate()");
		}
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MyCommissionCommand.validate()");
		}
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MyCommissionCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		try
		{
			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationError.hasValidationErrors())
			{
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				
				if(appUserModel.getRetailerContactId() != null)
				{
					UserDeviceAccountsModel agentUserDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
					RetailerContactModel retailerContactModel = new RetailerContactModel();
					retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
					baseWrapper.setBasePersistableModel(retailerContactModel);
					baseWrapper = commonCommandManager.loadRetailerContact(baseWrapper);
					retailerContactModel = (RetailerContactModel)baseWrapper.getBasePersistableModel();
					AgentEarnedComSummaryModel agentCommissionModel = new AgentEarnedComSummaryModel();
					agentCommissionModel.setAgentId(agentUserDeviceAccountsModel.getUserId());
					if(MyCommissionEnum.contains(commissionOption))
					{
						baseWrapper.setBasePersistableModel(agentCommissionModel);
						try
						{
							baseWrapper = commonCommandManager.loadAgentCommission(baseWrapper);
						}
						catch(Exception e)
						{e.printStackTrace();
							throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e); 
						}
						if(null != baseWrapper.getBasePersistableModel())
						{
							agentCommissionModel = (AgentEarnedComSummaryModel)baseWrapper.getBasePersistableModel();
							
							switch(commissionOption)
							{
							
							case "1":{
										agentCommission = agentCommissionModel.getComToday();
										commissionStartDate = agentCommissionModel.getDateToday();
										break;
							}
							case "2":{
										agentCommission = agentCommissionModel.getComYesterday();
										commissionStartDate = agentCommissionModel.getDateYesterday();
										break;
							}
							case "3":{
										agentCommission = agentCommissionModel.getComThisWeek();
										commissionStartDate = agentCommissionModel.getDateStartOfWeek();
										commissionEndDate = new Date();
										break;
							}
							case "4":{
										agentCommission = agentCommissionModel.getComLastWeek();
										commissionStartDate = agentCommissionModel.getDateStartOfLastweek();
										commissionEndDate = agentCommissionModel.getDateEndOfLastweek();
										break;
							}
							case "5":{
										agentCommission = agentCommissionModel.getComThisMonth();
										commissionStartDate = agentCommissionModel.getDateStartOfMonth();
										commissionEndDate = new Date();
										break;
							}
							case "6":{
										agentCommission = agentCommissionModel.getComLastMonth();
										commissionStartDate = agentCommissionModel.getDateStartOfLastmonth();
										commissionEndDate = agentCommissionModel.getDateEndOfLastmonth();
										break;
							}
								
							}
							
							if(agentCommission == null) {
								throw new CommandException(this.getMessageSource().getMessage("myCommissionCommand.agentCommissionNotFound", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}							
						}
						else
						{
							throw new CommandException(this.getMessageSource().getMessage("myCommissionCommand.agentCommissionNotFound", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					baseWrapper.setBasePersistableModel(agentCommissionModel);
					if(retailerContactModel == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("checkBalanceCommand.RetailerDoesNotExists", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else
				{
					throw new CommandException(this.getMessageSource().getMessage("command.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			else
			{
				throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MyCommissionCommand.execute()");
		}
		
	}

	@Override
	public String response() {
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of MyCommissionCommand.response()");
		}
		return toXML();
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MyCommissionCommand.toXML()");
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
			.append(CommandFieldConstants.KEY_COMM_AMOUNT)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(replaceNullWithZero(agentCommission))
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
			.append(CommandFieldConstants.KEY_COMM_START_DATE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(dtf.print(commissionStartDate.getTime()))
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE);
			if(null!=commissionEndDate){
			
				strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_COMM_END_DATE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(dtf.print(commissionEndDate.getTime()))
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE);
			}
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(Formatter.formatNumbers(agentCommission))
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
			logger.debug("End of MyCommissionCommand.toXML()");
		}		
		return strBuilder.toString();
	}
}
