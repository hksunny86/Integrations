
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;


import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SupplierConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;


/**
 * 
 * @author Jawwad Farooq
 * November, 2010
 * 
 */

public class CashoutCommand extends MiniBaseCommand
{
	protected String mobileNo;
	protected HashMap parameters = null ;
	protected String agentMSISDN ;
	protected String txAmount ;
	protected Boolean doPayBill ;
	protected String pin ;
	protected String bankPIN ;
	protected String productId ;
	private MiniTransactionModel miniTransactionModel ;
	private TransactionCodeModel transactionCodeModel; //Added by Maqsood Shahzad for USSD CW transaction
//	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
//	SimpleDateFormat stf=new SimpleDateFormat("h:mm a");
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
	protected final Log logger = LogFactory.getLog(getClass());
	protected String smsText;
	String oneTimePin;
	Double balance;
	protected String tempCwDeviceTypeId;
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		parameters = (HashMap)((HashMap)baseWrapper.getObject(CommandFieldConstants.KEY_PARAM_HASHMAP)).clone() ;
		doPayBill = ((String)this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DO_PAY_BILL)).equalsIgnoreCase("") ? false : true ;
		miniTransactionModel = (MiniTransactionModel) baseWrapper.getObject( CommandFieldConstants.KEY_MINI_TX_MODEL ) ;
		if(null != baseWrapper.getObject(CommandFieldConstants.KEY_TX_CODE_MODEL)) // Added by Maqsood Shahzad for USSD CW transaction
		{
			transactionCodeModel = (TransactionCodeModel) baseWrapper.getObject(CommandFieldConstants.KEY_TX_CODE_MODEL);
		}
		
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID) ;
		
		if( !doPayBill )
		{
			agentMSISDN = (String)parameters.get( CommandFieldConstants.KEY_PARAM + CommandFieldConstants.PARAM_COUNTER_FIRST_VAL ) ;
			txAmount = (String)parameters.get( CommandFieldConstants.KEY_PARAM + (Integer.parseInt(CommandFieldConstants.PARAM_COUNTER_FIRST_VAL)+1) ) ;
			bankPIN = (String)parameters.get( CommandFieldConstants.KEY_PARAM + (Integer.parseInt(CommandFieldConstants.PARAM_COUNTER_FIRST_VAL)+2) ) ;
		}
		else
		{
			String smsMessage = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM_SMS_TEXT) ;
			StringTokenizer st = new StringTokenizer( smsMessage , " ");
			
			if( st.hasMoreTokens() )
				agentMSISDN = st.nextToken();
			if( st.hasMoreTokens() )
				txAmount = st.nextToken();			
		}		
		
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PARAM_PIN);
		oneTimePin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ONE_TIME_PIN);
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		mobileNo = this.removeCountryCode(mobileNo);	
		tempCwDeviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AW_DTID);
		
		logger.info("[CashoutCommand.prepare] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Mobile No:" + mobileNo + ". Amount:" + txAmount + ". TransactionCode:" + (transactionCodeModel == null ? " is NULL" : transactionCodeModel.getCode())) ;
		
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
			logger.error("[CashOutCommand.validate] Exception thrown for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if(  (agentMSISDN == null || agentMSISDN.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidCashoutFormat", new Object[]{}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if(  (txAmount == null || txAmount.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidCashoutFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if(  !doPayBill && (bankPIN == null || bankPIN.equalsIgnoreCase("") ) )
		{
			response = this.getMessageSource().getMessage("MINI.InvalidCashoutFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		try
		{
			Double.parseDouble(txAmount);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			response = this.getMessageSource().getMessage("MINI.InvalidCashoutFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
		
		ValidationErrors validationError = ValidatorWrapper.doNumeric( txAmount , new ValidationErrors(), "");
		if( validationError.hasValidationErrors() )
		{
			response = this.getMessageSource().getMessage("MINI.InvalidCashoutFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
		
		validationError = ValidatorWrapper.doNumeric( txAmount , new ValidationErrors(), "");
		if( !validationError.getStringBuilder().toString().equals("") )
		{
			response = this.getMessageSource().getMessage("MINI.InvalidCashoutFormat", null, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}
		
		
		if( doPayBill && (pin == null || pin.equalsIgnoreCase("") ))
		{
			response = this.getMessageSource().getMessage("MINI.InvalidPIN", new Object[]{agentMSISDN}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);					
		}
		
		if( productId == null || productId.equalsIgnoreCase("") )
		{
			logger.info("$$$$$$ CashoutCommand.doValidate() Line:127 Product Id in MiniCommandKeyword table is not mentioned.");
			response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[]{agentMSISDN}, null) ;
			throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);		
		}
		
	}

	@Override
	public void execute() throws CommandException
	{
			logger.info( "[CashOutCommand.execute] Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + 
						 " UserDeviceAccountId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey() +
						 " Mobile No:" + mobileNo + " Amount:" + txAmount + " Product ID:" + productId ) ;
		
		try
		{
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;
			BaseWrapper baseWrapper = new BaseWrapperImpl() ;
//			AppUserPeerModel appUserPeerModel = new AppUserPeerModel() ;
			String topupMobileNo = "" ;
			AppUserModel custModel = new AppUserModel();
			custModel.setMobileNo(mobileNo);
			custModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			searchBaseWrapper.setBasePersistableModel(custModel);
			custModel = (AppUserModel) this.getCommonCommandManager().loadAppUserByMobileNumberAndType(searchBaseWrapper).getBasePersistableModel();
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
					response = this.getMessageSource().getMessage("MINI.ServiceInActive", new Object[]{ productModel.getName() } , null) ;
					logger.error("[CashOutCommand.execute] Exception thrown for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
				}
				
				if( productModel.getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue() )
				{
					response = this.getMessageSource().getMessage("MINI.GeneralError", null , null) ;
					logger.error("[CashOutCommand.execute] Exception thrown for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Not a Variable Product. Response:" + response);
					throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);				
				}
				
				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel() ;
				if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) //CW 2nd leg will be performed by agent..checking
				{
					smartMoneyAccountModel.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
				}
				else
				{
					
					smartMoneyAccountModel.setCustomerId( custModel.getCustomerId() );
				}
				smartMoneyAccountModel.setDefAccount( true ) ;
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper) ;
				
				
				if( searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 ) 
				{
					smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0) ;
					
					if( !smartMoneyAccountModel.getActive() )
					{
						response = this.getMessageSource().getMessage("MINI.InActiveDefAcc", new Object[]{ smartMoneyAccountModel.getName() } , null) ;
						logger.error("[CashOutCommand.execute] Exception thrown for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
						throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);						
					}
						topupMobileNo = mobileNo ;
					
					// All pre-reqs are checked at this point 
					if( doPayBill ) // Call the BillPaymentCommand
					{
						baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, productModel.getProductId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, ThreadLocalAppUser.getAppUserModel().getMobileNo() /*custModel.getMobileNo()*/ ) ;// pass the mobile number of the customer - USSD
						baseWrapper.putObject( CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE, custModel.getMobileNo()) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_PIN, pin ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_ONE_TIME_PIN, oneTimePin ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_TX_AMOUNT, txAmount ) ;
						
						baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT, miniTransactionModel.getBAMT() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_TOTAL_AMOUNT, (miniTransactionModel.getTAMT() +  miniTransactionModel.getTPAM())) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_TX_PROCESS_AMNT, miniTransactionModel.getTPAM() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_COMM_AMOUNT, miniTransactionModel.getCAMT() ) ;
						baseWrapper.putObject(CommandFieldConstants.KEY_AW_DTID, tempCwDeviceTypeId);
						if(transactionCodeModel != null) // Added by Maqsood Shahzad for USSD CW transaction
						{
							baseWrapper.putObject( CommandFieldConstants.KEY_TX_CODE_MODEL, transactionCodeModel ) ; 
						}
						
						//Cash Withdrawal Leg 2 - Populate Transaction Detail Master data from MiniTransctionModel
						if(miniTransactionModel.getCommandId() != null && miniTransactionModel.getCommandId().toString().equals(CommandFieldConstants.CMD_MINI_CASHOUT)){
							TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
							transactionDetailMasterModel.setTransactionCodeId(miniTransactionModel.getTransactionCodeId());
							transactionDetailMasterModel.setTransactionCode(miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
							searchBaseWrapper.setBasePersistableModel(transactionDetailMasterModel);
							searchBaseWrapper = this.getCommonCommandManager().loadTransactionDetailMaster(searchBaseWrapper);
							transactionDetailMasterModel = (TransactionDetailMasterModel)searchBaseWrapper.getBasePersistableModel();
							transactionDetailMasterModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
							transactionDetailMasterModel.setRecipientMobileNo(miniTransactionModel.getMobileNo());
							transactionDetailMasterModel.setIsManualOTPin(miniTransactionModel.getIsManualOTPin());
							transactionDetailMasterModel.setTransactionAmount(miniTransactionModel.getTAMT());
							transactionDetailMasterModel.setTotalAmount(miniTransactionModel.getTAMT() + miniTransactionModel.getTPAM());
							transactionDetailMasterModel.setExclusiveCharges(0d);
							transactionDetailMasterModel.setProductName(ProductConstantsInterface.CASH_WITHDRAWAL_NAME);
							//populate rest of the fields in TransactionProcessor.postUpdate by checking transction status ( update in case of failed)
							baseWrapper.putObject(CommandFieldConstants.KEY_TX_DTL_MASTER_MODEL, transactionDetailMasterModel);
						}
						
//						saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_BILL_PAY_TRANS));
						saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CW_BILL_PAYMENT_TRANS));
						
						response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CW_BILL_PAYMENT_TRANS);
						
						/*commented the following 2 lines for ussd */
//						if( response.indexOf("trn code=") != -1 )
//							response = "" ;
					}
					else // Send the temporary Verifly Pin 
					{
						
						// Fetch the Name of Receiver ......
						
						String receiverName = "" ;
						String agentFname="";
						AppUserModel appUserModel = new AppUserModel();
						appUserModel.setMobileNo(agentMSISDN) ;
						appUserModel.setAppUserTypeId( UserTypeConstantsInterface.RETAILER ) ;
						searchBaseWrapper.setBasePersistableModel(appUserModel) ;
						searchBaseWrapper = this.getCommonCommandManager().searchAppUserByMobile(searchBaseWrapper) ;
						
						if( searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 ){
							receiverName = ((AppUserModel)searchBaseWrapper.getCustomList().getResultsetList().get(0)).getFirstName() + " " +
								((AppUserModel)searchBaseWrapper.getCustomList().getResultsetList().get(0)).getLastName() ;
							agentFname=((AppUserModel)searchBaseWrapper.getCustomList().getResultsetList().get(0)).getFirstName();
						}
						else
						{
							response = this.getMessageSource().getMessage("MINI.InvalidFundTransferAccount", null, null) ;
							logger.error("[CashOutCommand.execute] Exception thrown for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
							throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
						}
							
						
						
						//Check BankPIN -- looks like this code should be removed . Maqsood
						SwitchWrapper switchWrapper = new SwitchWrapperImpl() ;
						BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
						baseWrapperTemp.setBasePersistableModel(smartMoneyAccountModel);
						switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
						AbstractFinancialInstitution abstractFinancialInstitution = this.getCommonCommandManager().loadFinancialInstitution(baseWrapperTemp);
						/*switchWrapper.putObject( CommandFieldConstants.KEY_PIN , bankPIN) ;
						switchWrapper = abstractFinancialInstitution.verifyPIN(switchWrapper) ;*/
						//TODO: what should be status of this code - the above verifyPin() call is removed as it seems necessary (Mudassir Hanif)
						
						//Maqsood Shahzad -- need to check the balance of customer in order to carry out the transaction
						
						SwitchWrapper tempWrapper = new SwitchWrapperImpl();
						tempWrapper.putObject(CommandFieldConstants.KEY_PIN , bankPIN);
						BaseWrapper wrapper = this.getCommonCommandManager().generateTransactionCode(); // generate transaction code
						TransactionCodeModel codeModel = (TransactionCodeModel) wrapper.getBasePersistableModel();
						WorkFlowWrapper flowWrapperTemp = new WorkFlowWrapperImpl();
						TransactionModel transactionModel = new TransactionModel();
						transactionModel.setTransactionCodeId(codeModel.getTransactionCodeId());
						flowWrapperTemp.setTransactionModel(transactionModel);
						// Change by Rashid -------Start
						flowWrapperTemp.setTransactionCodeModel(codeModel);
						flowWrapperTemp.setProductModel(productModel);
						// Change by Rashid -------End
						tempWrapper.setWorkFlowWrapper(flowWrapperTemp);
						tempWrapper.setBasePersistableModel(smartMoneyAccountModel);
						tempWrapper.setTransactionTransactionModel(transactionModel);
						logger.info("[CashOutCommand.execute] Going to check balance for SmartMoneyAccountID:" + smartMoneyAccountModel.getSmartMoneyAccountId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
						switchWrapper = abstractFinancialInstitution.checkBalance(tempWrapper) ;
						
						/*Set logged in customer accountInfoModel in ThreadLocalAccountInfo to reduce verifly calls*/
//						ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(switchWrapper.getAccountInfoModel());
						
						balance = switchWrapper.getBalance();
						
						if(balance < new Double(txAmount).doubleValue() )
						{
							response = this.getMessageSource().getMessage("MINI.InsufficientBalance", null, null) ; 
							logger.error("[CashOutCommand.execute] Exception thrown for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Response:" + response);
							throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);		
						}
						
						
						
						baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
						baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, productModel.getProductId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, agentMSISDN ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT, txAmount ) ;
						baseWrapper.putObject(CommandFieldConstants.KEY_PIN, bankPIN);
						baseWrapper.putObject(CommandFieldConstants.KEY_FINANCIAL_INSTITUTION, abstractFinancialInstitution);
						saveMiniCommandLog( Long.parseLong(CommandFieldConstants.CMD_GET_BILL_INFO));
						response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_GET_BILL_INFO);
						
						//************************Generate one time verifly pin called -
//						
						String PIN = this.generateOneTimeVeriflyPIN(smartMoneyAccountModel);
						
						
						MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
						
						// Mark all previous transactions as expired........
						miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT/*MiniTransactionStateConstant.PENDING*/ ) ;
						miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
						miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_MINI_CASHOUT) ) ;
						baseWrapper.setBasePersistableModel(miniTransactionModel);
						this.getCommonCommandManager().modifyPINSentMiniTransToExpired(baseWrapper);
						
						
						// Saves the current GL Transaction
						miniTransactionModel.setTransactionCodeIdTransactionCodeModel((TransactionCodeModel)wrapper.getBasePersistableModel());
						miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_MINI_CASHOUT)) ;
						miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
						miniTransactionModel.setTimeDate(new Date()) ;
						miniTransactionModel.setMobileNo( this.mobileNo );
						miniTransactionModel.setSmsText( agentMSISDN + " " + txAmount ) ;
						miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT/*MiniTransactionStateConstant.PENDING*/ ) ;
						miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
						
						try
						{
							miniTransactionModel.setCAMT( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.CAMTF_NODEREF) )) ;
							miniTransactionModel.setBAMT( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.BILL_AMOUNT_NODEREF) )) ;
							miniTransactionModel.setTAMT( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.BILL_AMOUNT_NODEREF) )) ;
							miniTransactionModel.setTPAM( Double.parseDouble( MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.SERVICE_CHARGES_NODEREF)) ) ;
						}
						catch (Exception e)
						{
							logger.error("[CashOutCommand.execute] Exception occured in populating MiniTransactionModel for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Message:" + e.getMessage());
						}
						
						baseWrapper.setBasePersistableModel( miniTransactionModel ) ;
						baseWrapper = this.getCommonCommandManager().saveMiniTransaction(baseWrapper) ;
						
						logger.info( "[CashOutCommand.execute] Saved MiniTransaction for Logged in AppUserId : " + ThreadLocalAppUser.getAppUserModel().getPrimaryKey() + 
									 " MiniTransactionID:" + miniTransactionModel.getMiniTransactionId() + 
									 " UserDeviceAccountId : " + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getPrimaryKey() +
								 	 " Mobile No:" + mobileNo + " Amount:" + txAmount + " Product ID:" + productId ) ;
				
						//Cash Withdrawal Leg 1 - Populate Transaction Detail Master data from MiniTransctionModel
						if(miniTransactionModel.getCommandId() != null && miniTransactionModel.getCommandId().toString().equals(CommandFieldConstants.CMD_MINI_CASHOUT)){
							TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel(true);
							transactionDetailMasterModel.setSaleMobileNo(agentMSISDN);
							transactionDetailMasterModel.setRecipientMobileNo(miniTransactionModel.getMobileNo());
							transactionDetailMasterModel.setIsManualOTPin(miniTransactionModel.getIsManualOTPin());
							transactionDetailMasterModel.setTransactionAmount(miniTransactionModel.getTAMT());
							transactionDetailMasterModel.setTotalAmount(miniTransactionModel.getTAMT() + miniTransactionModel.getTPAM());
							transactionDetailMasterModel.setExclusiveCharges(miniTransactionModel.getTPAM());
							transactionDetailMasterModel.setProductName(ProductConstantsInterface.CASH_WITHDRAWAL_NAME);
							transactionDetailMasterModel.setTransactionCode(miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
							transactionDetailMasterModel.setTransactionCodeId(miniTransactionModel.getTransactionCodeId());
							transactionDetailMasterModel.setProcessingStatusName(MiniTransactionStateConstant.miniStateNamesMap.get(miniTransactionModel.getMiniTransactionStateId()));
							transactionDetailMasterModel.setCreatedOn(new Date());
							transactionDetailMasterModel.setUpdatedOn(new Date());
							transactionDetailMasterModel.setSupplierId(productModel.getSupplierId());
							transactionDetailMasterModel.setSupplierName(productModel.getSupplierIdSupplierModel().getName());
							this.getCommonCommandManager().saveTransactionDetailMasterModel(transactionDetailMasterModel) ;
						}
						
//						response = this.getMessageSource().getMessage("MINI.CashoutPINSMS", new Object[]{receiverName,firstParam, miniTransactionModel.getBAMT(), miniTransactionModel.getCAMT(), miniTransactionModel.getTAMT(),PIN,
//								this.getMessageSource().getMessage("MINI.OneTimeVeriflyPINValidityInMins",null,null)}, null) ; ;
						
						
/*						smsText = this.getMessageSource().getMessage("USSD.CustomerCashoutSMS", new Object[]{Formatter.formatDouble(miniTransactionModel.getBAMT()),Formatter.formatDouble(miniTransactionModel.getTPAM()),Formatter.formatDouble(miniTransactionModel.getTAMT()),firstParam, dtf.print(new DateTime())
								
								,tf.print(new LocalTime()), miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode() , PIN ,tf.print(new LocalTime().plusHours(24)), dtf.print(new LocalDate().plusDays(1))  },null) ; ;
								
*/								
					smsText = this.getMessageSource().getMessage(
							"USSD.CustomerCashoutSMS",
							new Object[] { 
									Formatter.formatDouble(miniTransactionModel.getBAMT()),
									agentMSISDN,
									Formatter.formatDouble(miniTransactionModel.getTPAM()),
									dtf.print(new DateTime()),
									tf.print(new LocalTime()),
									miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode(),
									PIN
									 
									
									
									
									},
									null);
					
					
					response = this.getMessageSource().getMessage(
							"USSD.CustomerCashoutNotification",
							new Object[] { 
									Formatter.formatDouble(miniTransactionModel.getBAMT()),
									agentFname,
									agentMSISDN,									
									miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode(),
									dtf.print(new DateTime()),
									tf.print(new LocalTime()),
									Formatter.formatDouble(miniTransactionModel.getTPAM())},
									null);
					
								
	/*					response = "Cash Withdrawal\nAgent Mobile\n"+firstParam+"\nAmount : Rs."+Formatter.formatDouble(miniTransactionModel.getBAMT())+"\nCharges and Tax : Rs."+Formatter.formatDouble(miniTransactionModel.getTPAM())+
						"\nTotal Amount : "+Formatter.formatDouble(miniTransactionModel.getTAMT())+"\nTx ID : "+miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode()
						+"\n"+dtf.print(new DateTime())+" at "+tf.print(new LocalTime());
	*/					
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
				response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[]{agentMSISDN}, null) ;
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
			if(ex instanceof CommandException)
			{
				throw (CommandException)ex;
			}
			ex.printStackTrace() ;
			logger.error("Exception thrown by CashoutCommand()");			
			response = this.getMessageSource().getMessage("MINI.GeneralError", new Object[]{agentMSISDN}, null) ;
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
		try
		{
			if(null != smsText && !"".equals(smsText))
			{
				sendSMSToUser(mobileNo, smsText);
			}
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		
		// for cashout from USSD
		
		return response;
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

}



