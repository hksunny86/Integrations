package com.inov8.microbank.server.service.commandmodule.minicommandmodule;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniCommandKeywordModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;

/**
 * 
 * @author Jawwad Farooq
 * July, 2007
 * 
 */

public class USSDPayCashCommand extends MiniBaseCommand
{

	protected String mobileNo;
	protected HashMap parameters = null;
	protected String firstParam;
	protected String secondParam;
	protected String thirdParam;
	protected final Log logger = LogFactory.getLog(getClass());
	protected String transactionValidity;
	protected AppUserModel customerModel; // For Customer who performed the first leg of CW
	String webappResponse;
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
	protected String tempCwDeviceTypeId;
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
	      /**
         * Check added by Soofia Faruq
         * to handle the Cash Withdrawal request from Mobile App with DTID 5
         */
        if( Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue() )
        {
            firstParam = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM + "1");
            secondParam = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM + "2");
            thirdParam = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM + "3");
            
            parameters = new HashMap();
            baseWrapper.putObject(CommandFieldConstants.KEY_PARAM_HASHMAP, parameters);     
            
            parameters.put( CommandFieldConstants.KEY_PARAM + "1", firstParam ) ;
            parameters.put( CommandFieldConstants.KEY_PARAM + "2", secondParam ) ;
            parameters.put( CommandFieldConstants.KEY_PARAM + "3", thirdParam ) ;
        }
        else
        {
            parameters = (HashMap) ((HashMap) baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone();
    
            firstParam = (String) parameters.get(CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL);
            secondParam = (String) parameters.get(CommandFieldConstants.KEY_PARAM + "2");
            thirdParam = (String) parameters.get(CommandFieldConstants.KEY_PARAM + "3");
        }
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		mobileNo = this.removeCountryCode(mobileNo);
		
		tempCwDeviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AW_DTID);
		
		transactionValidity = this.getMessageSource().getMessage("MINI.OneTimeUSSDVeriflyPINValidityInMins", null, null);
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		customerModel = new AppUserModel();
		customerModel.setMobileNo(thirdParam);
		customerModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		wrapper.setBasePersistableModel(customerModel);
		try
		{
			wrapper = this.getCommonCommandManager().loadAppUserByMobileNumberAndType(wrapper);
		}
		catch(Exception e)
		{
			logger.error(e.getStackTrace());
			throw new WorkFlowException("Invalid Customer Mobile Provided.");
		}
		customerModel = (AppUserModel) wrapper.getBasePersistableModel();
		
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
		logger.info("[UssdPayCashCommand.execute] Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + 
					" UserDeviceId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey() + 
//					" TransactionID:" +
					" Mobile No:" + mobileNo);
  
		
		MiniTransactionModel miniTransactionModel = new MiniTransactionModel();;
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		BaseWrapper baseWrapper = new BaseWrapperImpl();

		
		
		try
		{
			/**
			 * Verify Agent Pin
			 */
			
		      SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
				SmartMoneyAccountModel smacc = new SmartMoneyAccountModel();
				smacc.setRetailerContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
				smacc.setDefAccount(true);
				smacc.setDeleted(false);
				sBaseWrapper.setBasePersistableModel(smacc);
				sBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(sBaseWrapper) ;
				if( sBaseWrapper.getCustomList().getResultsetList() != null && sBaseWrapper.getCustomList().getResultsetList().size() > 0 ) 
				{
					smacc = (SmartMoneyAccountModel)sBaseWrapper.getCustomList().getResultsetList().get(0) ;
					
					if( !smacc.getActive() )
					{
						logger.error("Exception thrown by USSDPayCashCommand.()");
						response = this.getMessageSource().getMessage("MINI.InActiveDefAcc", new Object[]{ smacc.getName() } , null) ;
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);						
					}
				}
				 /**
		         * Check added by Soofia Faruq
		         * to handle the Cash Withdrawal request from Mobile App with DTID 5
		         */
		        if( Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue() )
		        {
                    BaseWrapper wrapper = new BaseWrapperImpl();
    
                    wrapper.setBasePersistableModel(smacc);
                    AbstractFinancialInstitution abstractFinancialInstitution = this.getCommonCommandManager().loadFinancialInstitution(wrapper);
                    SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                    switchWrapper.setSmartMoneyAccountModel(smacc);
                    switchWrapper.putObject(CommandFieldConstants.KEY_PIN, firstParam);
                    switchWrapper = abstractFinancialInstitution.verifyPIN(switchWrapper);
		        }
			
			
			
			/**
			 * Load the Mini transactions within 15mins
			 */
//			miniTransactionModel = new MiniTransactionModel();

			// Mark all previous transactions as expired........
			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
			miniTransactionModel.setAppUserId(customerModel.getAppUserId());
			miniTransactionModel.setMobileNo(thirdParam);
			miniTransactionModel.setCommandId(Long.parseLong(CommandFieldConstants.CMD_MINI_CASHOUT));
			searchBaseWrapper.setBasePersistableModel(miniTransactionModel);
			searchBaseWrapper = this.getCommonCommandManager().loadMiniTransaction(searchBaseWrapper);

			List<MiniTransactionModel> miniTransactionModelList = searchBaseWrapper.getCustomList().getResultsetList();
			if (miniTransactionModelList.size() > 0)
			{
				logger.info("[UssdPayCashCommand.execute] Fetched MiniTransactionList Size: " + miniTransactionModelList.size() + " Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + 
						" UserDeviceId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey() + 
						" transactionValidity:" + transactionValidity +
						" Mobile No:" + mobileNo);
				miniTransactionModel = miniTransactionModelList.get(0);

				//Check the 15 mins validity
				long transValidityInMilliSecs = Integer.valueOf(transactionValidity) * 60 * 1000;
				long timeDiff = System.currentTimeMillis() - miniTransactionModel.getTimeDate().getTime();
				
				logger.info("[UssdPayCashCommand.execute] Fetched MiniTransactionList Size: " + miniTransactionModelList.size() + " Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + 
						" UserDeviceId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey() + 
						" TransactionValidity:" + transactionValidity + " Minutes." +
						" Time Elapsed:" + (timeDiff / 60000) + " Minutes." +
						" Mobile No:" + mobileNo);

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
						//baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo); // pass the mobile number of the customer - USSD
						baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, thirdParam);
						baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, miniCommandKeywordModel.getProductId());
						baseWrapper.putObject(CommandFieldConstants.KEY_PARAM_HASHMAP, parameters);
						baseWrapper.putObject(CommandFieldConstants.KEY_PARAM_SMS_TEXT, miniTransactionModel.getSmsText());
						baseWrapper.putObject(CommandFieldConstants.KEY_PARAM_PIN, firstParam);
						baseWrapper.putObject(CommandFieldConstants.KEY_ONE_TIME_PIN, secondParam);
						baseWrapper.putObject(CommandFieldConstants.KEY_AW_DTID, tempCwDeviceTypeId);
						
						
						baseWrapper.putObject(CommandFieldConstants.KEY_DO_PAY_BILL, true);
						baseWrapper.putObject(CommandFieldConstants.KEY_MINI_TX_MODEL, miniTransactionModel);
						//add tx code - for USSD - Maqsood Shahzad
						baseWrapper.putObject(CommandFieldConstants.KEY_TX_CODE_MODEL, miniTransactionModel.getTransactionCodeIdTransactionCodeModel());
						
						response = this.getCommandManager().executeCommand(baseWrapper,miniTransactionModel.getCommandId().toString());
						webappResponse = response;
						
						if(response!=null){
							
							 String customerName=customerModel.getFirstName(); 
							 String customerMSISDN=MiniXMLUtil.getTagTextValue(response, "//trans/trn/@mNo");
							 String billAmount= Formatter.formatDouble( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, "//trans/trn/@BAMT")));
							 String totalAmount= Formatter.formatDouble( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, "//trans/trn/@TAMT")));
							 String date=dtf.print(new DateTime());
							 String time=tf.print(new LocalTime());
							 String balance=Formatter.formatDouble( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, "//trans/trn/@A1BAL")));
							 String Agentcommission=Formatter.formatDouble( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, "//trans/trn/@A1CAMT")));
							 String transactionID=MiniXMLUtil.getTagTextValue(response, "//trans/trn/@code");
							 
							 response=this.getMessageSource().getMessage(
										"USSD.AgentPayCashNotification",
										new Object[] { 
												customerName,
												customerMSISDN,
												Formatter.formatDouble(miniTransactionModel.getBAMT()),
												Formatter.formatDouble(miniTransactionModel.getCAMT()),
												miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode(), 
												dtf.print(new DateTime()),
												tf.print(new LocalTime()),
												balance},
												null);
							 
							 
							/* response="Successfully completed a\nC W Trx of Rs. "+billAmount+" to\n"+customerMSISDN+"\n on "+date+" at "+time+
							 "\n Your Commission: Rs. "+Agentcommission+"\nTrx ID "+transactionID ;*/
						}

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

					response = this.getMessageSource().getMessage("MINI.ExpiredPIN", null, null);
					logger.error("[UssdPayCashCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}
			}
			else
			{
				response = this.getMessageSource().getMessage("MINI.NoTransInProcess", null, null);
				logger.error("[UssdPayCashCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
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
			
			logger.error("[UssdPayCashCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + ex.getMessage());
			
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
			logger.error("[UssdPayCashCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + ex.getMessage());
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
					logger.error("[UssdPayCashCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + e.getMessage());
				}
			}
			
		}

	}

	@Override
	public String response()
	{
		if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue())
		{
			return response;
		}
		else
		{
			if(null != webappResponse && !"".equals(webappResponse))
			{
				return webappResponse;
			}
			else 
			{
				return response;
			}
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

}
