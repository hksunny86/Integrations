
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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
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
 * December, 2010
 * 
 */

public class RetailPaymentCommand extends MiniBaseCommand
{
	protected String mobileNo;
	protected HashMap parameters = null ;
	protected String firstParam ;
	protected String txAmount ;
	protected Boolean doPayBill ;
	protected String pin ;
	protected String bankPIN ;
	protected String productId ;
	private MiniTransactionModel miniTransactionModel ;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		logger.info("CashoutCommand.prepare Started...........") ;
		
		parameters = (HashMap)((HashMap)baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone() ;
		doPayBill = ((String)this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DO_PAY_BILL)).equalsIgnoreCase("") ? false : true ;
		miniTransactionModel = (MiniTransactionModel) baseWrapper.getObject( CommandFieldConstants.KEY_MINI_TX_MODEL ) ;
		
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID) ;
		
		if( !doPayBill )
		{
			firstParam = (String)parameters.get( CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL ) ;
			txAmount = (String)parameters.get( CommandFieldConstants.KEY_PARAM + (Integer.parseInt(CommandFieldConstants.PARAM_COUNTER_FIRST_VAL)+1) ) ;
			bankPIN = (String)parameters.get( CommandFieldConstants.KEY_PARAM + (Integer.parseInt(CommandFieldConstants.PARAM_COUNTER_FIRST_VAL)+2) ) ;
		}
		else
		{
			String smsMessage = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM_SMS_TEXT) ;
			StringTokenizer st = new StringTokenizer( smsMessage , " ");
			
			if( st.hasMoreTokens() )
				firstParam = st.nextToken();
			if( st.hasMoreTokens() )
				txAmount = st.nextToken();			
		}		
		
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
		
		if( !doPayBill && isMiniTransInProcess() )
		{
			response = this.getMessageSource().getMessage("MINI.MultipleTrans", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if(  (firstParam == null || firstParam.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidRetailPaymentFormat", new Object[]{}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if(  (txAmount == null || txAmount.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidRetailPaymentFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if(  !doPayBill && (bankPIN == null || bankPIN.equalsIgnoreCase("") ) )
		{
			response = this.getMessageSource().getMessage("MINI.InvalidRetailPaymentFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		try
		{
			Double.parseDouble(txAmount);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			response = this.getMessageSource().getMessage("MINI.InvalidRetailPaymentFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
		
		ValidationErrors validationError = ValidatorWrapper.doInteger( txAmount , new ValidationErrors(), "");
		if( validationError.hasValidationErrors() )
		{
			response = this.getMessageSource().getMessage("MINI.InvalidRetailPaymentFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
		
		validationError = ValidatorWrapper.doNumeric( txAmount , new ValidationErrors(), "");
		if( !validationError.getStringBuilder().toString().equals("") )
		{
			response = this.getMessageSource().getMessage("MINI.InvalidRetailPaymentFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
		
		
		if( doPayBill && (pin == null || pin.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidPIN", new Object[]{firstParam}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if( productId == null || productId.equalsIgnoreCase("") )
		{
			logger.info("$$$$$$ CashoutCommand.doValidate() Line:127 Product Id in MiniCommandKeyword table is not mentioned.");
			response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[]{firstParam}, null) ;
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
//			AppUserPeerModel appUserPeerModel = new AppUserPeerModel() ;
			String topupMobileNo = "" ;
			
			/**
			 * Load the user_service info against this nick
			 */
			
				// Load Product model to check whether the product is active or not
				ProductModel productModel = new ProductModel() ;
				productModel.setProductId( Long.valueOf(productId) ) ;
				baseWrapper.setBasePersistableModel( productModel ) ;
				productModel = (ProductModel)this.getCommonCommandManager().loadProduct( baseWrapper ).getBasePersistableModel() ;
				
				if( !productModel.getActive() )
				{
					logger.error("Exception thrown by CashoutCommand.");
					response = this.getMessageSource().getMessage("MINI.ServiceInActive", new Object[]{ productModel.getName() } , null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
				}
				
				if( productModel.getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue() )
				{
					logger.error("Exception thrown by CashoutCommand.execute() Line:174 --- Not a Variable Product ");
					response = this.getMessageSource().getMessage("MINI.GeneralError", null , null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
				}
				
				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel() ;				
				
				smartMoneyAccountModel.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
				smartMoneyAccountModel.setDefAccount( true ) ;
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper) ;
				
				
				if( searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 ) 
				{
					smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0) ;
					
					if( !smartMoneyAccountModel.getActive() )
					{
						logger.error("Exception thrown by CashoutCommand.()");
						response = this.getMessageSource().getMessage("MINI.InActiveDefAcc", new Object[]{ smartMoneyAccountModel.getName() } , null) ;
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);						
					}
						topupMobileNo = mobileNo ;
					
					// All pre-reqs are checked at this point 
					if( doPayBill ) // Call the BillPaymentCommand
					{
						baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, productModel.getProductId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, firstParam ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_PIN, pin ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_TX_AMOUNT, txAmount ) ;
						
						baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT, miniTransactionModel.getBAMT() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_TOTAL_AMOUNT, miniTransactionModel.getTAMT() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_TX_PROCESS_AMNT, miniTransactionModel.getTPAM() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_COMM_AMOUNT, miniTransactionModel.getCAMT() ) ;
						
						
						saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_BILL_PAY_TRANS));
						
						response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_BILL_PAY_TRANS);
						
						if( response.indexOf("trn code=") != -1 )
							response = "" ;
					}
					else // Send the temporary Verifly Pin 
					{
						
						// Fetch the Name of Receiver ......
						
						String receiverName = "" ;
						AppUserModel appUserModel = new AppUserModel();
						appUserModel.setMobileNo(firstParam) ;
						appUserModel.setAppUserTypeId( UserTypeConstantsInterface.RETAILER ) ;
						searchBaseWrapper.setBasePersistableModel(appUserModel) ;
						searchBaseWrapper = this.getCommonCommandManager().searchAppUserByMobile(searchBaseWrapper) ;
						
						if( searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 )
							receiverName = ((AppUserModel)searchBaseWrapper.getCustomList().getResultsetList().get(0)).getFirstName() + " " +
								((AppUserModel)searchBaseWrapper.getCustomList().getResultsetList().get(0)).getLastName() ;
						else
						{
							response = this.getMessageSource().getMessage("MINI.InvalidFundTransferAccount", null, null) ;
							throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
						}
							
						
						
						//Check BankPIN
						SwitchWrapper switchWrapper = new SwitchWrapperImpl() ;
						BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
						baseWrapperTemp.setBasePersistableModel(smartMoneyAccountModel);
						switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
						AbstractFinancialInstitution abstractFinancialInstitution = this.getCommonCommandManager().loadFinancialInstitution(baseWrapperTemp);
						switchWrapper.putObject( CommandFieldConstants.KEY_PIN , bankPIN) ;
						switchWrapper = abstractFinancialInstitution.verifyPIN(switchWrapper) ;
						
						baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
						baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, productModel.getProductId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, firstParam ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT, txAmount ) ;
						
						saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_GET_BILL_INFO));
						response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_GET_BILL_INFO);
						
						System.out.println( response );
						
						String PIN = this.generateOneTimeVeriflyPIN(smartMoneyAccountModel);
						MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
						
						// Mark all previous transactions as expired........
						miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT ) ;
						miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
						baseWrapper.setBasePersistableModel(miniTransactionModel);
						this.getCommonCommandManager().modifyPINSentMiniTransToExpired(baseWrapper);
						
						
						// Saves the current GL Transaction
						miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_MINI_RETAIL_PAYMENT)) ;
						miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
						miniTransactionModel.setTimeDate(new Date()) ;
						miniTransactionModel.setMobileNo( this.mobileNo );
						miniTransactionModel.setSmsText( firstParam + " " + txAmount ) ;
						miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT ) ;
						miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
						
						try
						{
							miniTransactionModel.setCAMT( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.CAMTF_NODEREF) )) ;
							miniTransactionModel.setBAMT( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.BILL_AMOUNT_NODEREF) )) ;
							miniTransactionModel.setTAMT( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.TAMTF_NODEREF) )) ;
							miniTransactionModel.setTPAM( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.SERVICE_CHARGES_NODEREF)) ) ;
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						
						baseWrapper.setBasePersistableModel( miniTransactionModel ) ;
						baseWrapper = this.getCommonCommandManager().saveMiniTransaction(baseWrapper) ;
						
						response = this.getMessageSource().getMessage("MINI.CashoutPINSMS", new Object[]{receiverName,firstParam, miniTransactionModel.getBAMT(), miniTransactionModel.getCAMT(), miniTransactionModel.getTAMT(),PIN,
								this.getMessageSource().getMessage("MINI.OneTimeVeriflyPINValidityInMins",null,null)}, null) ; ;
					}
				}
				else //If there is no smart money account linked with Mini 
				{
					logger.error("Exception thrown by CashoutCommand.()");
					response = this.getMessageSource().getMessage("MINI.NoMiniAccLinked", null, null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
				}
		}
		catch (FrameworkCheckedException ex)
		{
			ex.printStackTrace() ;
			logger.error("Exception thrown by CashoutCommand.()");			
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
			else if( ex.getMessage().equalsIgnoreCase( "One Time PIN and PIN not matched" ))
			{
				response = this.getMessageSource().getMessage("MINI.IncorrectTransPIN", null, null) ;
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, ex);
			}
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, ex);
		}		
		catch (Exception ex)
		{
			ex.printStackTrace() ;
			logger.error("Exception thrown by CashoutCommand()");			
			response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[]{firstParam}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, ex);
		}		
	}


	@Override
	public String response()
	{		
		if( doPayBill )
		{
//		try
//		{
//			String values[] = new String[8];
//			
//			values[0] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_CODE_NODEREF ) ;
//			values[1] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_DATE_NODEREF ) ;
//			
//			try
//			{
//				DateFormat df = new SimpleDateFormat("EEE, MMM dd, yyyy");
//				Date date = df.parse(values[1]) ;
//				
//				df = new SimpleDateFormat("dd/MM/yy");
//				values[1] = df.format(date) ;
//			}
//			catch (java.text.ParseException e)
//			{
//				e.printStackTrace();
//			}
//			
//			values[2] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_SERVICE_NODEREF ) ;
//			values[3] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_SMACCOUNT_NODEREF ) ;
//			values[4] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_AUTH_CODE_NODEREF ) ;
//			values[5] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_AMT_NODEREF ) ;
//			values[6] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF ) ;
//			values[7] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_TOTAL_AMT_NODEREF ) ;
//			
//			response = this.getMessageSource().getMessage("MINI.TopupSMS", values, null) ;
//		}
//		catch (XPathExpressionException e1)
//		{
//			e1.printStackTrace();
//		}
		}
		
		return response;
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

}



