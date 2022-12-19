package com.inov8.microbank.server.service.commandmodule.minicommandmodule;

import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MiniCommandKeywordModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;


/**
 * 
 * @author Jawwad Farooq
 * July, 2007
 * 
 */

public class MiniCommandManager extends MiniBaseCommand
{

	protected String userId;
	protected String pin;
	protected String deviceTypeId;
	protected String mobileNo;
	protected String smsMessage;
	protected String cmdKeyword;
	protected Date updatedExpiryDate = null;
	protected HashMap parameters = null ;
	protected String response ;
	protected String tempCwDeviceTypeId;
	
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		userId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		mobileNo = this.removeCountryCode(mobileNo);
		smsMessage = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_KENNEL_SMS_DATA);
		parameters = new HashMap();
		tempCwDeviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AW_DTID);
		
		logger.info("[MiniCommandManager.prepare] SENDER MOBILE:" + mobileNo);
		logger.info("[MiniCommandManager.prepare] SMS MESSAGE:" + smsMessage);
		
//		logger.info( "[MiniCommandManager.prepare] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()) ;
	}
	

	@Override
	public void doValidate() throws CommandException
	{
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(userId,validationErrors,"User Id");
		validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(validationErrors.hasValidationErrors())
		{
			logger.error(validationErrors.getErrors());
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
		}
	}
	
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}
	
	
	@Override
	public void execute() throws CommandException
	{
		logger.info("In execute Mini Command Manager.....");
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		CommandManager commandManager = this.getCommandManager();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		
				
		try
		{
			saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_LOGIN ));
			baseWrapper.putObject(CommandFieldConstants.KEY_U_ID, userId);
			baseWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
			baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			baseWrapper.putObject(CommandFieldConstants.KEY_AW_DTID, tempCwDeviceTypeId);
			String loginResponse = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_LOGIN);
			
			/**
			 * Condition get true when Kannel successfully logged-on
			 */
			if(loginResponse != null && loginResponse.length() > 0)
			{
				
				
				saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_MINI_LOGIN ));
				baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId );
				response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_MINI_LOGIN);
				
				//Parse the message at this stage
				this.parseMessage();
				
				// Load mini command from MINI_COMMAND_KEYWORD table
				MiniCommandKeywordModel miniCommandKeywordModel = new MiniCommandKeywordModel();
				if(cmdKeyword.equalsIgnoreCase("BAL")){
					cmdKeyword = "BI";
				}
				miniCommandKeywordModel.setKeyword( cmdKeyword.toUpperCase() ) ;
				logger.info("Executing command against command keyword........."+cmdKeyword);
				searchBaseWrapper.setBasePersistableModel( miniCommandKeywordModel ) ;
				searchBaseWrapper = this.getCommonCommandManager().loadMiniCommand(searchBaseWrapper) ;
				
				if( searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 )
				{
					miniCommandKeywordModel = (MiniCommandKeywordModel)searchBaseWrapper.getCustomList().getResultsetList().get(0) ;
					
					if( miniCommandKeywordModel != null )
					{
						logger.info(miniCommandKeywordModel.getCommandId() + " Command Found against keyword "+cmdKeyword);
						saveMiniCommandLog( miniCommandKeywordModel.getCommandId() );
						baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, miniCommandKeywordModel.getProductId());
						baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);	
						baseWrapper.putObject(CommandFieldConstants.KEY_PARAM_HASHMAP, parameters);					
						response = commandManager.executeCommand(baseWrapper, miniCommandKeywordModel.getCommandId().toString());
					}
				}
				// Invalid Keyword
				else
				{
					logger.info(cmdKeyword + " Command Not Found.");
					response = this.getMessageSource().getMessage("MINI.InValidKeyword", null, null);
					sendSMSToUser(mobileNo, response);
					logger.error("[MiniCommandManager.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
				}				
			}
		}
		catch(Exception ex)
		{
			if (ex instanceof CommandException)
			{
//				logger.error("[MiniCommandManager.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + ex.getMessage());
				
				if(((CommandException) ex).getErrorCode() == ErrorCodes.ACCOUNT_EXPIRED_ERROR.longValue()){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.EXPIRED", null, null));
				}else if(((CommandException) ex).getErrorCode() == ErrorCodes.ACCOUNT_DISABLED_ERROR.longValue()){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.INACTIVE", null, null));
				}else if(((CommandException) ex).getMessage().equalsIgnoreCase(this.getMessageSource().getMessage("CUSTOMER.PIN.CHANGE", null, null))){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.PIN.CHANGE", null, null));
				}else if(((CommandException) ex).getMessage().equalsIgnoreCase(this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.INACTIVE", null, null))){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.INACTIVE", null, null));
				}else if(((CommandException) ex).getMessage().equalsIgnoreCase(this.getMessageSource().getMessage("MINI.InActiveSMA", null, null))){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.INACTIVE", null, null));
				}else if(((CommandException) ex).getMessage().equalsIgnoreCase(	this.getMessageSource().getMessage("MINI.InActiveSMA", null, null))){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.INACTIVE", null, null));
				}else if(((CommandException) ex).getErrorCode() == ErrorCodes.CREDENTIALS_EXPIRED.longValue()){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.credentialsExpired", null, null));
				}else if(((CommandException) ex).getErrorCode() == ErrorCodes.ACCOUNT_BLOCKED.longValue()){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.accountLocked", null, null));
				}else if(((CommandException) ex).getErrorCode() == ErrorCodes.ACCOUNT_LOCKED.longValue()){
					sendSMSToUser(mobileNo, this.getMessageSource().getMessage("CUSTOMER.accountLocked", null, null));
				}
		
			
				throw new CommandException(ex.getMessage(),ErrorCodes.UNKNOWN_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			else if(ex instanceof FrameworkCheckedException)
			{
//				sendSMSToUser(mobileNo, ex.getMessage());
				logger.error("[MiniCommandManager.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + ex.getMessage());
				throw new CommandException(ex.getMessage(),ErrorCodes.UNKNOWN_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			else
			{
//				sendSMSToUser(mobileNo, this.getMessageSource().getMessage("smsCommand.act_sms5", null,null));				
				logger.error("[MiniCommandManager.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + this.getMessageSource().getMessage("smsCommand.act_sms5", null,null));
				throw new CommandException(this.getMessageSource().getMessage("smsCommand.act_sms5", null,null),ErrorCodes.UNKNOWN_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
	}
	
	
	@Override
	public String response()
	{		
//		try
//		{
//			if( response != null && !response.equals("") )
//				
//				sendSMSToUser(mobileNo, response);
//		}
//		catch (CommandException e)
//		{
//			logger.error(e) ;
//		}		
		return response;
	}
	
	
	private void parseMessage()
	{
		smsMessage = smsMessage.trim();
		StringTokenizer st = new StringTokenizer( smsMessage , " ");
		
				
		//Command Keyword
		if( st.hasMoreTokens() )
			cmdKeyword = st.nextToken();
		
		for( int count=1; st.hasMoreTokens(); count++ )			
		{
			parameters.put( CommandFieldConstants.KEY_PARAM + count, st.nextToken() ) ;
		}
		
	}
	
	private String formatNumberWith92(String mobNum)
	{
		if(null != mobNum && !"".equalsIgnoreCase(mobNum))
		{
			mobNum = mobNum.replaceFirst("0", "0092");
			
		}
		
		return mobNum;
	}
	
	private String removePipeSignsforUSSD(String string)
	{
		if(null != string && !"".equalsIgnoreCase(string))
		{
			string = string.replaceAll("|", " ");
			
		}
		return string;
	}
	
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
	
	
	
	

}
