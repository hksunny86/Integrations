package com.inov8.microbank.server.service.commandmodule;

/**
 * Project Name: 			i8Microbank	
 * @author 					Soofia Faruq
 * Creation Date: 			October 2012  			
 * Description:				Pay Cash Info Command
 */

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

public class PayCashInfoCommand extends BaseCommand {
	protected AppUserModel appUserModel;
	private String transactionId;
	protected BaseWrapper preparedBaseWrapper;
	BaseWrapper baseWrapper;
	CommissionAmountsHolder commissionAmountsHolder;
	protected String trxId, trxDate, trxTime, msisdn, full_name, MOBN, CMOBN;
	protected Double BAMT, TPAM, TAMT;
	
	protected final Log logger = LogFactory.getLog(PayCashInfoCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of PayCashInfoCommand.execute()");
		}

		String retVal = null;
		DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		CommissionWrapper commissionWrapper;
		baseWrapper = new BaseWrapperImpl();
		
		try
		{
			 TransactionModel txModel = new TransactionModel();
			 SearchBaseWrapper sBaesWrapper = new SearchBaseWrapperImpl();
			 TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
			 transactionCodeModel.setCode(transactionId);
			 BaseWrapper baseWrapper = new BaseWrapperImpl();
			 baseWrapper.setBasePersistableModel(transactionCodeModel);
			 baseWrapper = commonCommandManager.loadTransactionCodeByCode(baseWrapper);
			 MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
			 transactionCodeModel = ((TransactionCodeModel)(baseWrapper.getBasePersistableModel()));
			 
			 miniTransactionModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
			 
			 SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			 wrapper.setBasePersistableModel(miniTransactionModel);
			 wrapper = commonCommandManager.loadMiniTransaction(wrapper);
			 if(null != wrapper.getCustomList() && null != wrapper.getCustomList().getResultsetList())
			 {
				 miniTransactionModel = (MiniTransactionModel)wrapper.getCustomList().getResultsetList().get(0);
				 
				 if(miniTransactionModel.getMiniTransactionStateId().longValue() != MiniTransactionStateConstant.PIN_SENT)
				 {
					//check if this is Account to cash transaction
					 txModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
					 sBaesWrapper.setBasePersistableModel(transactionCodeModel);
					 sBaesWrapper = commonCommandManager.loadTransactionByTransactionCode(sBaesWrapper);
					 txModel = (TransactionModel) sBaesWrapper.getBasePersistableModel();
					
					 if(txModel != null && 
							 (txModel.getTransactionTypeId().equals(TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_TX)
									 || txModel.getTransactionTypeId().equals(TransactionTypeConstantsInterface.CASH_TO_CASH))) {			 
	
						 retVal = "This transaction is either already claimed or expired.\n";						 
					 }
					 
					 //other transactions i.e. cash withdrawal etc.
					 retVal = "The Transaction ID is already used.\n";					 
				 }
				 
				 ////////////////////////////////////////
				 //check if this is Account to cash transaction
	//			 SearchBaseWrapper sBaesWrapper = new SearchBaseWrapperImpl();
	//			 TransactionModel txModel = new TransactionModel();
				 txModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
				 sBaesWrapper.setBasePersistableModel(transactionCodeModel);
				 sBaesWrapper = commonCommandManager.loadTransactionByTransactionCode(sBaesWrapper);
				 txModel = (TransactionModel) sBaesWrapper.getBasePersistableModel();
				
				 if(txModel != null && 
						 (txModel.getTransactionTypeId().equals(TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_TX)
								 || txModel.getTransactionTypeId().equals(TransactionTypeConstantsInterface.CASH_TO_CASH))) {			 
					 
					 sBaesWrapper = new SearchBaseWrapperImpl();
					 sBaesWrapper.setBasePersistableModel(txModel);
					 sBaesWrapper = commonCommandManager.loadTransactionDetail(sBaesWrapper);
					 TransactionDetailModel txDetailModel = (TransactionDetailModel) sBaesWrapper.getBasePersistableModel();
					 
//					 if(txModel.getTransactionTypeId().equals(TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_TX)){
//						 userState.setIsAccountToCashLeg2(Boolean.TRUE);
//						 userState.setWalkinReceiverCNIC(txDetailModel.getCustomField9());
//						 return validateA2CTransctionId(ussdInputDTO, commonCommandManager, userState, miniTransactionModel);
//					 }
//	
//					 // Cash To Cash
//					 if(txModel.getTransactionTypeId().equals(TransactionTypeConstantsInterface.CASH_TO_CASH)){
//						 userState.setIsCashToCashLeg2(Boolean.TRUE);
//						 userState.setWalkinReceiverMSISDN(txDetailModel.getCustomField5());
//						 userState.setWalkinSenderMSISDN(txDetailModel.getCustomField6());
//						 userState.setWalkinSenderCNIC(txDetailModel.getCustomField7());
//						 userState.setWalkinReceiverCNIC(txDetailModel.getCustomField9());
//						 return validateC2CTransctionId(ussdInputDTO, commonCommandManager, userState, miniTransactionModel);
//					 }
//					 // End Cash To Cash
						 
				 }
				 ///////////////////////////////////////////////////////////////////
				 
				 String sms = miniTransactionModel.getSmsText();
				 
				 StringTokenizer tokenizer = new StringTokenizer(sms, " ");
				 String mobileNo = tokenizer.nextToken();
				 
				 if(!ThreadLocalAppUser.getAppUserModel().getMobileNo().equals(mobileNo.trim()))
				 {
					 retVal = "Transaction ID invalid for this agent.\n";					 
				 }
				 
				 /* Checking Customer Account Health  */
				 AppUserModel appUserModel = miniTransactionModel.getAppUserIdAppUserModel();
				 
				 UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
					try
					{
						userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
						userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);
					
						searchBaseWrapper = commonCommandManager.loadUserDeviceAccounts(searchBaseWrapper);
						if(null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
						{
							
							userDeviceAccountsModel = (UserDeviceAccountsModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
							if(userDeviceAccountsModel.getAccountEnabled() == Boolean.FALSE)
							{
								retVal = "Transaction cannot be processed.\nCustomer account is deactivated.\n";
							}
							else if(userDeviceAccountsModel.getAccountLocked() == Boolean.TRUE)
							{
								retVal = "Transaction cannot be processed.\nCustomer account is locked.\n";
							}
							else if(userDeviceAccountsModel.getCredentialsExpired() == Boolean.TRUE)
							{
								retVal = "Transaction cannot be processed.\nCustomer account credentails are expired.\n";
							}
						}
						else
						{
							retVal = "No Customer found against this transaction.\n";
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						retVal = "No Customer found against this transaction.\n";
					}
				 
				 /* Done Checking Customer Account Health  */
				 //incorporate all the following attributes in return XML
				MOBN = mobileNo;
				trxId = miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode();
				trxDate = dtf.print(miniTransactionModel.getCreatedOn().getTime());
				trxTime = tf.print(miniTransactionModel.getCreatedOn().getTime());
				BAMT = miniTransactionModel.getBAMT().doubleValue();
				TPAM = miniTransactionModel.getTPAM().doubleValue();
				TAMT = miniTransactionModel.getTAMT().doubleValue();
				msisdn = miniTransactionModel.getMobileNo();
								
				AppUserModel cusModel = new AppUserModel();
					cusModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
					cusModel.setMobileNo(miniTransactionModel.getMobileNo());
					
					SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
					sBaseWrapper.setBasePersistableModel(cusModel);
					sBaseWrapper = commonCommandManager.loadAppUserByMobileNumberAndType(sBaseWrapper);
					if (sBaseWrapper!=null && sBaseWrapper.getBasePersistableModel()!=null) {
						cusModel = (AppUserModel) sBaseWrapper
								.getBasePersistableModel();
						
						full_name = cusModel.getFirstName() + " " + cusModel.getLastName();
						CMOBN = cusModel.getMobileNo();
				}
			 }
			 else
			 {
				 retVal = "The transaction could not be fetched\n";
			 }		 	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			retVal = "The transaction could not be fetched\n";
		}

		if( retVal != null )
		 {
			 throw new CommandException(retVal,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		 }
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of PayCashInfoCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of PayCashInfoCommand.prepare()");
		}
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_ID);
		preparedBaseWrapper = baseWrapper;
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        
        if(logger.isDebugEnabled())
		{
			logger.debug("End of PayCashInfoCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of PayCashInfoCommand.response()");
		}
		
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of PayCashInfoCommand.validate()");
		}
		
		validationErrors = ValidatorWrapper.doRequired(transactionId,validationErrors,"Transaction ID");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
			
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(transactionId,validationErrors,"Transaction ID");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");			
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of PayCashInfoCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of PayCashInfoCommand.toXML()");
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
			.append(CommandFieldConstants.KEY_TX_ID)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(trxId)
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
			.append(CommandFieldConstants.KEY_TX_DATE)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(trxDate)
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
			.append(CommandFieldConstants.KEY_TX_TIME)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(trxTime)
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
			.append(CommandFieldConstants.KEY_BILL_AMOUNT)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(BAMT)
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
			.append(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(Formatter.formatNumbers(BAMT))
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
			.append(CommandFieldConstants.KEY_TPAM)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(TPAM)
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
			.append(CommandFieldConstants.KEY_TPAMF)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(Formatter.formatNumbers(TPAM))
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
			.append(CommandFieldConstants.KEY_TAMT)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAMT)
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
			.append(CommandFieldConstants.KEY_TAMTF)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(Formatter.formatNumbers(TAMT))
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
			.append(CommandFieldConstants.KEY_MSISDN)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(msisdn)
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
			.append(CommandFieldConstants.KEY_RP_NAME)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(full_name)
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
			.append(CommandFieldConstants.KEY_MOB_NO)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(MOBN)
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
			.append(CommandFieldConstants.KEY_CW_CUSTOMER_MOBILE )
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(CMOBN)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE);
		
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE);
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of PayCashInfoCommand.toXML()");
		}		
		return strBuilder.toString();
	}
}
