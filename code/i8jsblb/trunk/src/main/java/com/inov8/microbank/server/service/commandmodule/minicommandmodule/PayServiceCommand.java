
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


public class PayServiceCommand extends MiniBaseCommand
{
	protected String mobileNo;
	protected HashMap parameters = null ;
	protected String firstParam ;
	protected String productCode ;
	protected Boolean doPayBill ;
	protected String pin ;
	String smsMessage ;
	String bankPIN ;
	private MiniTransactionModel miniTransactionModel ;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		parameters = (HashMap)((HashMap)baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone() ;
		doPayBill = ((String)this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DO_PAY_BILL)).equalsIgnoreCase("") ? false : true ;
		smsMessage = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM_SMS_TEXT) ;
		miniTransactionModel = (MiniTransactionModel) baseWrapper.getObject( CommandFieldConstants.KEY_MINI_TX_MODEL ) ;
		
		if( !doPayBill )
		{
			firstParam = (String)parameters.get( CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL ) ;
			productCode = (String)parameters.get( CommandFieldConstants.KEY_PARAM + (Integer.parseInt(CommandFieldConstants.PARAM_COUNTER_FIRST_VAL)+1) ) ;
			bankPIN = (String)parameters.get( CommandFieldConstants.KEY_PARAM + (Integer.parseInt(CommandFieldConstants.PARAM_COUNTER_FIRST_VAL)+2) ) ;
		}
		else
		{
			smsMessage = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM_SMS_TEXT) ;
			StringTokenizer st = new StringTokenizer( smsMessage , " ");
			
			if( st.hasMoreTokens() )
				firstParam = st.nextToken();
			if( st.hasMoreTokens() )
				productCode = st.nextToken();		
//			firstParam = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM_SMS_TEXT);
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
			response = this.getMessageSource().getMessage("MINI.InvalidBillPaymentFormat", new Object[]{}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if(  !doPayBill && (bankPIN == null || bankPIN.equalsIgnoreCase("") ) )
		{
			response = this.getMessageSource().getMessage("MINI.InvalidBillPaymentFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if(  (productCode == null || productCode.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidBillPaymentFormat", new Object[]{}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if( doPayBill == true && (pin == null || pin.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidPIN", new Object[]{firstParam}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
//		if( doPayBill == true && (smsText == null || smsText.equalsIgnoreCase("") ))
//		{
//			response = this.getMessageSource().getMessage("MINI.NoSuchServiceNick", new Object[]{smsText}, null) ;
//			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
//		}		
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
			
			// Load Product model to check whether the product is active or not
			ProductModel productModel = new ProductModel() ;
			productModel.setProductCode(productCode) ;
			baseWrapper.setBasePersistableModel( productModel ) ;
			productModel = (ProductModel)this.getCommonCommandManager().searchProduct( baseWrapper ).getBasePersistableModel() ;
			
			if( productModel == null  )
				{
					logger.error("Exception thrown by PayServiceCommand.execute(). Service Nock not found");
					response = this.getMessageSource().getMessage("MINI.ServiceNickNotFound", new Object[]{productCode}, null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
				}
				
			if(  !productModel.getActive() )
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
			
			
			/**
			 * Load the user_service info against this nick
			 */
			
			
			{
				
				// Load Product model to check whether the product is active or not
				
				if( !productModel.getActive() )
				{
					logger.error("Exception thrown by PayServiceCommand.execute(). Product not active");
					response = this.getMessageSource().getMessage("MINI.ServiceInActive", new Object[]{ productModel.getName() } , null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
				}
				
				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel() ;				
				
				smartMoneyAccountModel.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
//				smartMoneyAccountModel.setDefPayAccount( true ) ;
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper) ;
				
				
				if( searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 ) 
				{
					smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0) ;
					
					if( !smartMoneyAccountModel.getActive() )
					{
						logger.error("Exception thrown by PayServiceCommand.execute(). Smart Money Account not active");
						response = this.getMessageSource().getMessage("MINI.InActiveDefAcc", new Object[]{ smartMoneyAccountModel.getName() } , null) ;
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);						
					}
//					if( !smartMoneyAccountModel.getMiniEnable() )
//					{
//						logger.error("Exception thrown by PayServiceCommand.execute(). Smart Money Account not Mini account");
//						response = this.getMessageSource().getMessage("MINI.DefAccNotLinkedWithMini", new Object[]{ smartMoneyAccountModel.getName() } , null) ;
//						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);						
//					}
					
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					if( this.getCommonCommandManager().isCVVReqForSMA(baseWrapper) )
					{
						logger.error("Exception thrown by PayServiceCommand.execute(). CVV Account is not supported with Mini");
						response = this.getMessageSource().getMessage("MINI.InvalidSMAForGL", new Object[]{ smartMoneyAccountModel.getName() } , null) ;
						//response = "CVV wala account nahi challay ga" ;
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);						
					}
					
					// All pre-reqs are checked at this point 
					if( doPayBill ) // Call the BillPaymentCommand
					{
						baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, productModel.getProductId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, mobileNo ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_CUST_CODE, firstParam ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_PIN, pin ) ;
						
						baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT, miniTransactionModel.getBAMT() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_TOTAL_AMOUNT, miniTransactionModel.getTAMT() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_TX_PROCESS_AMNT, miniTransactionModel.getTPAM() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_COMM_AMOUNT, miniTransactionModel.getCAMT() ) ;
						
						saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_BILL_PAY_TRANS));
						
						// populate all the custom parameters
						/*baseWrapper.putObject( userServiceCustFldViewModel.getKeyName1(), userServiceCustFldViewModel.getCustomFieldValue1() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName2(), userServiceCustFldViewModel.getCustomFieldValue2() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName3(), userServiceCustFldViewModel.getCustomFieldValue3() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName4(), userServiceCustFldViewModel.getCustomFieldValue4() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName5(), userServiceCustFldViewModel.getCustomFieldValue5() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName6(), userServiceCustFldViewModel.getCustomFieldValue6() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName7(), userServiceCustFldViewModel.getCustomFieldValue7() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName8(), userServiceCustFldViewModel.getCustomFieldValue8() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName9(), userServiceCustFldViewModel.getCustomFieldValue9() ) ;
						baseWrapper.putObject( userServiceCustFldViewModel.getKeyName10(), userServiceCustFldViewModel.getCustomFieldValue10() ) ;*/
						
						response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_BILL_PAY_TRANS);
						
						if( response.indexOf("trn code=") != -1 )
							response = "" ;
						
					}
					else // Send the temporary Verifly Pin 
					{
						
						MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
						
						// Mark all previous transactions as expired........
						miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT ) ;
						miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
						baseWrapper.setBasePersistableModel(miniTransactionModel);
						this.getCommonCommandManager().modifyPINSentMiniTransToExpired(baseWrapper);
						
						
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
						baseWrapper.putObject( CommandFieldConstants.KEY_CUST_CODE, firstParam ) ;
//						baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT, txAmount ) ;
						
						saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_GET_BILL_INFO));
						response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_GET_BILL_INFO);
						
						System.out.println( response );
						
						
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
						
						String PIN = this.generateOneTimeVeriflyPIN(smartMoneyAccountModel);
						
						// Saves the current GL Transaction
						miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_MINI_PAY_SERVICE)) ;
						miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
						miniTransactionModel.setTimeDate(new Date()) ;
						miniTransactionModel.setMobileNo( this.mobileNo );
						miniTransactionModel.setSmsText( firstParam + " " + productCode ) ;
						miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT ) ;
						miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
						
						baseWrapper.setBasePersistableModel( miniTransactionModel ) ;
						baseWrapper = this.getCommonCommandManager().saveMiniTransaction(baseWrapper) ;
						
						
						if( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.BILL_PAID_STATUS_NODEREF) != null 
								&& MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.BILL_PAID_STATUS_NODEREF).equals("1") )
						{
							response = this.getMessageSource().getMessage("MINI.LateBillInfo", new Object[]{firstParam, miniTransactionModel.getBAMT(), 
									MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.LATE_BILL_DATE_NODEREF),}
									, null) ; ;
							miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.UNSUCCESSFUL ) ;
						}
						else
						response = this.getMessageSource().getMessage("MINI.BillInfo", new Object[]{firstParam, miniTransactionModel.getBAMT(), 
								MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.LATE_BILL_DATE_NODEREF),
								miniTransactionModel.getCAMT(), miniTransactionModel.getTAMT(),PIN,
								this.getMessageSource().getMessage("MINI.OneTimeVeriflyPINValidityInMins",null,null)}
								, null) ; ;
					}
				}
				else //If there is no smart money account linked with Mini 
				{
					logger.error("Exception thrown by PayServiceCommand.execute(). No Smart Money Account linked.");
					response = this.getMessageSource().getMessage("MINI.NoMiniAccLinked", null, null) ;
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
				}
			}
//			else // In-Valid Service Nick
//			{
//				logger.error("Exception thrown by PayServiceCommand.execute(). Service Nock not found");
//				response = this.getMessageSource().getMessage("MINI.ServiceNickNotFound", new Object[]{firstParam}, null) ;
//				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
//			}
		}
		catch (FrameworkCheckedException ex)
		{
			ex.printStackTrace();
			logger.error("Exception thrown by PayServiceCommand.checkLogin()");			
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
		catch (Exception ex)
		{
			ex.printStackTrace() ;
			logger.error("Exception thrown by TopupCommand.()");			
			response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[]{firstParam}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, ex);
		}		
		
	}


	@Override
	public String response()
	{		
//		if( doPayBill )
//		{
//		try
//		{
//			String values[] = new String[8];
//			
//			values[0] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_CODE_NODEREF ) ;
//			values[1] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_DATE_NODEREF ) ;
//						
//				try
//				{
//					DateFormat df = new SimpleDateFormat("EEE, MMM dd, yyyy");
//					Date date = df.parse(values[1]) ;
//					
//					df = new SimpleDateFormat("dd/MM/yy");
//					values[1] = df.format(date) ;
//				}
//				catch (java.text.ParseException e)
//				{
//					e.printStackTrace();
//				}
//
//			
//			values[2] = firstParam ;
//			values[3] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_SMACCOUNT_NODEREF ) ;
//			values[4] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_AUTH_CODE_NODEREF ) ;
//			values[5] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_BILL_AMT_NODEREF ) ;
//			values[6] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF ) ;
//			values[7] = MiniXMLUtil.getTagTextValue( response, MiniXMLUtil.TRANS_TOTAL_AMT_NODEREF ) ;
//			
//			response = this.getMessageSource().getMessage("MINI.BillPaymentSMS", values, null) ;
//		}
//		catch (XPathExpressionException e1)
//		{
//			e1.printStackTrace();
//		}
//		}
		
		return response;
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

}


