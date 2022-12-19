package com.inov8.microbank.server.service.commandmodule;


import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
/*import com.inov8.microbank.common.model.transactionmodule.LastTransactionDetailViewModel;*/
import com.inov8.microbank.common.util.CommandConstants;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.XMLConstants;

public class MfsLogoutCommand extends BaseCommand
{

	protected final Log logger = LogFactory.getLog(MfsLogoutCommand.class);
	
	DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
	DateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
	
	String loginDate;
	String loginTime;
	String logoutDate;
	String logoutTime;
	long hours;
	long minutes;
	long seconds;

	/*LastTransactionDetailViewModel lastTransactionDetailViewModel = null;*/
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLogoutCommand.execute()");
		}
		
		try
		{
			/*AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
			
			Timestamp loginTime = appUserModel.getLastLoginAttemptTime();
			Timestamp logoutTime = new Timestamp(System.currentTimeMillis());
			
			this.loginDate = PortalDateUtils.formatDate(new Date(loginTime.getTime()), CommandFieldConstants.DATE_FORMAT);
			this.loginTime = PortalDateUtils.formatDate(new Date(loginTime.getTime()), CommandFieldConstants.TIME_FORMAT);
			this.logoutDate = PortalDateUtils.formatDate(new Date(logoutTime.getTime()), CommandFieldConstants.DATE_FORMAT);
			this.logoutTime = PortalDateUtils.formatDate(new Date(logoutTime.getTime()), CommandFieldConstants.TIME_FORMAT);
			
			long diff = logoutTime.getTime() - loginTime.getTime();
			
			this.seconds = diff / 1000 % 60;
			this.minutes = diff / (60 * 1000) % 60;
			this.hours = diff / (60 * 60 * 1000) % 24;
			
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			LastTransactionDetailViewModel lastTransaction = new LastTransactionDetailViewModel();
			lastTransaction.setAppUserId(appUserModel.getAppUserId());
			searchBaseWrapper = this.getCommonCommandManager().fetchLastTransactinoByAppUserId(lastTransaction);
			if(searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				
				LastTransactionDetailViewModel model = (LastTransactionDetailViewModel)searchBaseWrapper.getCustomList().getResultsetList().get(0); 
				
				if(loginTime.before(model.getTransactionDate()))
				{
					this.lastTransactionDetailViewModel = model;
				}
				*/
				
				/*List<LastTransactionDetailViewModel> lastTransactionDetailViewModelList = searchBaseWrapper.getCustomList().getResultsetList(); 
				for(LastTransactionDetailViewModel model: lastTransactionDetailViewModelList)
				{
					if(loginTime.before(model.getTransactionDate()))
					{
						if(this.lastTransactionDetailViewModel == null)
						{
							this.lastTransactionDetailViewModel = model;
						}
						else if(this.lastTransactionDetailViewModel.getTransactionDate().before(model.getTransactionDate()))
						{
							this.lastTransactionDetailViewModel = model;
						}
					}
				}*/
			/*}*/
			
			ThreadLocalAppUser.setAppUserModel(null);
			ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(null);
		}
		/*catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),(ex.getErrorCode() == 0)?ErrorCodes.COMMAND_EXECUTION_ERROR:ex.getErrorCode(),ErrorLevel.MEDIUM,ex);
		}*/
		catch(WorkFlowException wex)
		{
			throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
		}
		catch(Exception ex)
		{
			throw new CommandException(CommandConstants.GENERAL_ERROR_MSG,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.HIGH,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLogoutCommand.execute()");
		}
	}
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLogoutCommand.prepare()");
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLogoutCommand.prepare()");
		}
	}

	@Override
	public String response()
	{
		return toXML();
	}

	@Override
	public void doValidate() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLogoutCommand.doValidate()");
		}
		
		ValidationErrors validationErrors = new ValidationErrors();
		
		if(validationErrors.hasValidationErrors())
		{
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLogoutCommand.doValidate()");
		}
	}


	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MfsLogoutCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_PARAMS)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_PARAM_NAME)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.LOGOUT_SUCCESS_MSG)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		/*.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_PARAM_NAME)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_LOGIN_DATE_FORMAT)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.TAG_SYMBOL_CLOSE);
		try{
			strBuilder.append(PortalDateUtils.formatDate(this.loginDate, CommandFieldConstants.DATE_FORMAT,CommandFieldConstants.DATE_FORMATE));	
		}
		catch(ParseException pe)
		{
			strBuilder.append(this.loginDate);
		}
		strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_PARAM_NAME)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_LOGIN_TIME)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		.append(this.loginTime)
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_PARAM_NAME)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_LOGOUT_DATE)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		.append(this.logoutDate)
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_PARAM_NAME)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_LOGOUT_DATE_FORMAT)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.TAG_SYMBOL_CLOSE);
		try{
			strBuilder.append(PortalDateUtils.formatDate(this.logoutDate, CommandFieldConstants.DATE_FORMAT,CommandFieldConstants.DATE_FORMATE));	
		}
		catch(ParseException pe)
		{
			strBuilder.append(this.logoutDate);
		}
		strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_PARAM_NAME)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_LOGOUT_TIME)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		.append(this.logoutTime)
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_PARAM_NAME)
		.append(XMLConstants.TAG_SYMBOL_EQUAL)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(CommandFieldConstants.KEY_DURATION)
		.append(XMLConstants.TAG_SYMBOL_QUOTE)
		.append(XMLConstants.TAG_SYMBOL_CLOSE)
		.append(this.hours + " hrs " + this.minutes + " mins " + this.seconds + " secs")
		.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_PARAM)
		.append(XMLConstants.TAG_SYMBOL_CLOSE);
		*/
		strBuilder.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAMS)
		.append(TAG_SYMBOL_CLOSE);
			
		/*StringBuilder xmlBuilder = new StringBuilder();

		
		
		xmlBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_TRANS)
		.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		if(lastTransactionDetailViewModel != null)
		{
			xmlBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
			.append(XMLConstants.TAG_TRN)
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_DATE)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(inputFormat.format(this.lastTransactionDetailViewModel.getTransactionDate()))
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_DATE_FORMAT)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(outputFormat.format(this.lastTransactionDetailViewModel.getTransactionDate()))
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_COMPANY)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(StringEscapeUtils.escapeXml(this.lastTransactionDetailViewModel.getCompany()))
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_DESCRIPTION)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(StringEscapeUtils.escapeXml(this.lastTransactionDetailViewModel.getDescription()))
			.append(XMLConstants.TAG_SYMBOL_QUOTE);
	
			xmlBuilder.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_AMOUNT)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(this.lastTransactionDetailViewModel.getTransactionAmount())
			.append(XMLConstants.TAG_SYMBOL_QUOTE)
			
			.append(XMLConstants.TAG_SYMBOL_SPACE)
			.append(XMLConstants.ATTR_FORMATED_AMOUNT)
			.append(XMLConstants.TAG_SYMBOL_EQUAL)
			.append(XMLConstants.TAG_SYMBOL_QUOTE);
			if(this.lastTransactionDetailViewModel.getTransactionAmount() != null )
			{
				xmlBuilder.append(NumberFormat.getNumberInstance(Locale.US).format(this.lastTransactionDetailViewModel.getTransactionAmount()));
			}
			
			xmlBuilder.append(XMLConstants.TAG_SYMBOL_QUOTE)
			.append(XMLConstants.TAG_SYMBOL_CLOSE)
			.append(XMLConstants.TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(XMLConstants.TAG_TRN)
			.append(XMLConstants.TAG_SYMBOL_CLOSE);	
		}
		xmlBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_TRANS)
		.append(XMLConstants.TAG_SYMBOL_CLOSE);
		
		strBuilder = strBuilder.append(xmlBuilder.toString());
		*/
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MfsLogoutCommand.toXML()");
		}
		return strBuilder.toString();
	}
	
}
