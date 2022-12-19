
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;


import java.util.Date;
import java.util.HashMap;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;


/**
 * 
 * @author Jawwad Farooq
 * November, 2010
 * 
 */

public class CheckBalanceCommand extends MiniBaseCommand
{
	protected String deviceTypeId;
	protected String mobileNo;
	protected HashMap parameters = null ;
	protected String firstParam ;
	protected Boolean doReqTransaction ;
	protected String pin ;
	private Boolean isIvrResponse;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{

		isIvrResponse = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE) == null ? false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE));
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		parameters = (HashMap)((HashMap)baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone() ;
		doReqTransaction = ((String)this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DO_PAY_BILL)).equalsIgnoreCase("") ? false : true ;
		
		if( !doReqTransaction )
			firstParam = (String)parameters.get( CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL ) ;
		else
			firstParam = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM_SMS_TEXT);
		
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM_PIN);
				
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
		
		if( !doReqTransaction && isMiniTransInProcess() )
		{
			response = this.getMessageSource().getMessage("MINI.MultipleTrans", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
//		if(  (firstParam == null || firstParam.equalsIgnoreCase("") ))
//		{
//			response = this.getMessageSource().getMessage("MINI.WrongAccSerOrNick", new Object[]{firstParam}, null) ;
//			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
//		}
		
		if( doReqTransaction == true && (pin == null || pin.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidPIN", new Object[]{firstParam}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
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
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel() ;
			
			smartMoneyAccountModel.setDefAccount(Boolean.TRUE) ;
			
			logger.info("Loading Smart Money Account.");
			
			if(UserTypeConstantsInterface.CUSTOMER.longValue() == ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue()) {
				
				smartMoneyAccountModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
				
			} else if(UserTypeConstantsInterface.RETAILER.longValue() == ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue()) {

				smartMoneyAccountModel.setRetailerContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
			}
			
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
				if( smartMoneyAccountModel.getActive() )
				{
					if(Long.valueOf(deviceTypeId) == (DeviceTypeConstantsInterface.SMS_GATEWAY.longValue())){
						baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.SMS_GATEWAY ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, mobileNo ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_PIN, pin ) ;
						baseWrapper.putObject( "ACCTYPE", "1");
						
						firstParam = smartMoneyAccountModel.getName() ;
						
						saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_CHK_ACC_BAL));
						
						response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CHK_ACC_BAL);
						if(response == null || response.equalsIgnoreCase(""))
							response = "00";
					}else{
				
//							All pre-reqs are checked at this point 
							if( doReqTransaction ) // Call the BillPaymentCommand
							{
								//	Delegate the request to the CheckBalanceCommand						
								baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId() ) ;
								baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
								baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, mobileNo ) ;
								baseWrapper.putObject( CommandFieldConstants.KEY_PIN, pin ) ;
								
								firstParam = smartMoneyAccountModel.getName() ;
								
								saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_CHK_ACC_BAL));
								
								response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CHK_ACC_BAL);
							}
							else // Send the temporary Verifly Pin 
							{
								
								String PIN = this.generateOneTimeVeriflyPIN(smartMoneyAccountModel);
								MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
								
								// Mark all previous transactions as expired........
								miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT ) ;
								miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
								baseWrapper.setBasePersistableModel(miniTransactionModel);
								this.getCommonCommandManager().modifyPINSentMiniTransToExpired(baseWrapper);
								
								
								// Saves the current GL Transaction
								miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_MINI_CHECK_BALANCE)) ;
								miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
								miniTransactionModel.setTimeDate(new Date()) ;
								miniTransactionModel.setMobileNo( this.mobileNo );
								miniTransactionModel.setSmsText( "" ) ;
								miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT ) ;
								miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
								
								baseWrapper.setBasePersistableModel( miniTransactionModel ) ;
								baseWrapper = this.getCommonCommandManager().saveMiniTransaction(baseWrapper) ;
								
								response = this.getMessageSource().getMessage("MINI.BalancePINSMS", new Object[]{PIN,
										this.getMessageSource().getMessage("MINI.OneTimeVeriflyPINValidityInMins",null,null)}, null) ; ;
										
							}
						}
					
					
					
					
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
			
			//response = this.getMessageSource().getMessage("MINI.SMAPayAccChanged", new Object[]{smartMoneyAccountModel.getName()}, null) ;
		}
		catch (FrameworkCheckedException ex)
		{
			ex.printStackTrace() ;
			logger.error("Exception thrown by LoginCommand.checkLogin()");			
			if( ex.getMessage().equalsIgnoreCase("") )
			{
				response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[]{firstParam}, null) ;
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, ex);
			}
			else if( ex.getMessage().equalsIgnoreCase( this.getMessageSource().getMessage( "6033", null, null) ) )
			{
				response = this.getMessageSource().getMessage("MINI.IncorrectTransPIN", null, null) ;
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, ex);
			}
			
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, ex);
		}		
	}


	@Override
	public String response()
	{	
		if( doReqTransaction )
		{
		try
		{
			String values[] = new String[3];
			
			values[0] = ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ;
			values[1] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.BALANCE_AMOUNT_NODEREF ) ;
			values[2] = PortalDateUtils.formatDate(new Date(), PortalDateUtils.SHORT_TIME_FORMAT) 
			+ " " + PortalDateUtils.formatDate(new Date(), PortalDateUtils.SHORT_DATE_FORMAT) ;
			
			response = this.getMessageSource().getMessage("MINI.CheckBalSMS", values, null) ;			
		}
		catch (XPathExpressionException e1)
		{
			e1.printStackTrace();
			response = this.getMessageSource().getMessage("MINI.GeneralError", null, null) ;
		}
		}

		return response ;
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}


	public String getDeviceTypeId() {
		return deviceTypeId;
	}


	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

}



