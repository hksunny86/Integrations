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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.NoSuchMessageException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.FinancialInstitutionConstants;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;


public class CoreBankMiniStatementCommand extends BaseCommand
{
	protected AppUserModel appUserModel;
	protected String accountId;
	protected String pin;
	protected double accBalance;
	protected double creditLimit;
	protected double amountDue;
	protected double minAmountDue;
	protected Date dueDate;
	protected String deviceTypeId;
	protected String tPin;
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	private Long paymentModeId = null;
	ArrayList transactionList;
	
	private SmartMoneyAccountModel coreSmartMoneyAccountModel;
	
	boolean errorMessagesFlag;
	
	String veriflyErrorMessage;

	protected final Log logger = LogFactory.getLog(CoreBankMiniStatementCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CoreBankMiniStatementCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
//		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		try
		{
//			if(appUserModel.getCustomerId() != null)
			{
				ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationError.hasValidationErrors())
				{
//					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
//					BaseWrapper baseWrapper = new BaseWrapperImpl();
//					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
//					
//					baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
//					smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
//					
//					if(null != smartMoneyAccountModel && smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue()){
						
					AbstractFinancialInstitution abstractFinancialInstitution = commonCommandManager.loadFinancialInstitutionByClassName(FinancialInstitutionConstants.PHOENIX_FINANCIAL_INSTITUTION_CLASS_NAME);
//					
//					}else{
//						
//						logger.error("[CoreBankMiniStatementCommand.execute] Throwing Exception for Logged in AppUserID:" + appUserModel.getAppUserId() + ". Exception Message:" + this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null));
//                 		throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//					
//					}
					
					if(abstractFinancialInstitution != null ){

//						ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

//						if(!validationErrors.hasValidationErrors())
//						{
//							if(smartMoneyAccountModel.getName() != null)
//							{
								switchWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
								
								
								CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
								switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
								switchWrapper.setBasePersistableModel(coreSmartMoneyAccountModel);
								
								switchWrapper.setBankId( coreSmartMoneyAccountModel.getBankId() );
                                switchWrapper.setPaymentModeId( coreSmartMoneyAccountModel.getPaymentModeId() );
                                paymentModeId = coreSmartMoneyAccountModel.getPaymentModeId() ;
                                
                                WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
                                workFlowWrapper.setCustomerAccount(customerAccount);
                                workFlowWrapper.setTransactionModel(new TransactionModel());
                                workFlowWrapper.setMPin(this.tPin);
                                
                                switchWrapper.setWorkFlowWrapper(workFlowWrapper);
                                
//                                    if(DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() == Long.valueOf(deviceTypeId).longValue()) {                                    
//                                    	switchWrapper.getWorkFlowWrapper().setCheckBalance(Boolean.FALSE);
//                                    } else {
//                                    	switchWrapper.getWorkFlowWrapper().setCheckBalance(Boolean.TRUE);//Added by mudassir - Reducing Verifly Calls
//                                    }
								
//                                if(DeviceTypeConstantsInterface.ALL_PAY.longValue() == Long.valueOf(deviceTypeId).longValue()) {
//                                    switchWrapper = abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapper);    
//                                } else {                                    
//                                    switchWrapper = abstractFinancialInstitution.checkBalance(switchWrapper);
//                                }
                                
                                switchWrapper = abstractFinancialInstitution.miniStatement(switchWrapper);
                                
								if(switchWrapper.getWorkFlowWrapper().getTransactionModel().getBankResponseCode() != null)
								{
									transactionList = (ArrayList) switchWrapper.getObject(CommandFieldConstants.KEY_STATEMENTS);
//									accBalance = switchWrapper.getBalance();
								}
//								accBalance = switchWrapper.getBalance();
//								creditLimit = switchWrapper.getCreditLimit();
//								amountDue = switchWrapper.getAmountDue();
//								minAmountDue = switchWrapper.getMinAmountDue();
//								dueDate = switchWrapper.getDueDate();
//								
								logger.info("[CoreBankMiniStatementCommand.execute] Logged in AppUserID:" + appUserModel.getAppUserId());
                        		
//							}
//							else
//							{
//								logger.error("[CoreBankMiniStatementCommand.execute] Throwing Exception for Logged in AppUserID:" + appUserModel.getAppUserId() + ". Exception Message:" + this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null));
//                         		throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//							}
//						}
//						else
//						{
//							logger.error("[CoreBankMiniStatementCommand.execute] Validation errors occured. Throwing Exception for Logged in AppUserID:" + appUserModel.getAppUserId() + ". Errors:" + validationErrors.getErrors());
//                     		throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//						}
					}
					else
					{
						logger.error("[CoreBankMiniStatementCommand.execute] Throwing Exception for Logged in AppUserID:" + appUserModel.getAppUserId() + ". Exception Message:" + this.getMessageSource().getMessage("checkAccountBalanceCommand.financialInstitutionDoesnotExist", null,null));
                 		throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.financialInstitutionDoesnotExist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else
				{
					logger.error("[CoreBankMiniStatementCommand.execute] Validation errors occured. Throwing Exception for Logged in AppUserID:" + appUserModel.getAppUserId() + ". Errors:" + validationError.getErrors());
             		throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
//			else
//			{
//				throw new CommandException(this.getMessageSource().getMessage("command.invalidAppUserTyp", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//			}
		}
		catch(FrameworkCheckedException ex)
		{			
			if(logger.isDebugEnabled())
			{
				logger.debug(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			
			logger.error("[CoreBankMiniStatementCommand.execute] Throwing Exception for Logged in AppUserID:" + appUserModel.getAppUserId() + ". Exception Message:" + ex.getMessage());
     		throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(WorkFlowException ex)
		{			
			if(logger.isDebugEnabled())
			{
				logger.debug(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
//			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		
			String message = "" ;
			try
			{
				message = this.getMessageSource().getMessage(ex.getMessage(), null,null);
			}
			catch (NoSuchMessageException e)
			{
				message = ex.getMessage() ;
			} 
			
			logger.error("[CoreBankMiniStatementCommand.execute] Throwing Exception for Logged in AppUserID:" + appUserModel.getAppUserId() + ". Exception Message:" + message);
     		throw new CommandException(message,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		
		catch(Exception ex)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			
			logger.error("[CoreBankMiniStatementCommand.execute] Throwing Exception for Logged in AppUserID:" + appUserModel.getAppUserId() + ". Exception Message:" + ex.getMessage());
     		
			throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CoreBankMiniStatementCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CoreBankMiniStatementCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		
		logger.info("[CoreBankMiniStatementCommand.prepare] Logged in AppUserID:" + appUserModel.getAppUserId());
		
		try {
			
			if(appUserModel != null && appUserModel.getRetailerContactId() != null) {
				this.coreSmartMoneyAccountModel = getSmartMoneyAccountModel(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
			}else{
				logger.error("TransferInCommand.prepare] Exception occured for Core Bank Mini Statement AppUserID: " + appUserModel.getAppUserId() + " Exception Details: Null or Invalid App User Type");
			}
		} catch(Exception ex) {
			logger.error("TransferInCommand.prepare] Exception occured for Core Bank Mini Statement AppUserID: " + appUserModel.getAppUserId() + " Exception Details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			logger.error(ex);
		}

		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		tPin = this.decryptPin(tPin);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CoreBankMiniStatementCommand.prepare()");
		}
	}

	@Override
	public String response()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CoreBankMiniStatementCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		// checking whether core bank account linked or not
		if(coreSmartMoneyAccountModel == null ||  StringUtil.isNullOrEmpty(coreSmartMoneyAccountModel.getName())){
			throw new CommandException(this.getMessageSource().getMessage("accountNotLinked", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
	
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_ACC_ID, accountId);
		
		if(commonCommandManager.isAccountValidationRequired(baseWrapper)){
			validationErrors = ValidatorWrapper.doRequired(accountNumber, validationErrors, "Account Number");
			validationErrors = ValidatorWrapper.doRequired(accountType, validationErrors, "Account Type");
			validationErrors = ValidatorWrapper.doRequired(accountCurrency, validationErrors, "Account Currency");
			validationErrors = ValidatorWrapper.doRequired(accountStatus, validationErrors, "Account Status");
		}else if( tPin != null && tPin.equals("") && deviceTypeId != null && !deviceTypeId.equals(DeviceTypeConstantsInterface.ALL_PAY.toString()) && !deviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString())){
			validationErrors = ValidatorWrapper.doRequired(pin, validationErrors, "Pin");		
		}
		
		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		
		if(!validationErrors.hasValidationErrors()){
			validationErrors = ValidatorWrapper.doNumeric(accountId,validationErrors,"Account Id");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		
		return validationErrors;
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CoreBankMiniStatementCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		int count = 1;
		
		if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue() || Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.MOBILE.longValue())
		{
			if(transactionList != null && transactionList.size() > 0){
				for(Object transaction : transactionList){
					if(transaction instanceof Map){
						Map<String,String> singleTransaction = ( Map<String,String> ) transaction;
						//TODO format the statement
						
						if(count < transactionList.size()){
							String trxDate = singleTransaction.get("date");
							String desc = singleTransaction.get("desc");
							String stan = singleTransaction.get("stan");
							String amount = singleTransaction.get("amount");
							System.out.println(trxDate + " " + desc + " " + stan + " " + amount);
						}else{
							String desc = singleTransaction.get("desc");
							String amount = singleTransaction.get("balance");
							System.out.println(desc + " " + amount);
						}
						count++;
					}
				}
				
//				strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
//				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, productModel.getProductId().toString()));
//				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CREDIT_CARD_NO,this.creditCardNumber));
//				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_DUE_DATE, dtf.print(p.getDueDate().getTime())));
//				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_AMOUNT,replaceNullWithZero(p.getDueAmount()).toString()));
//				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_MINIMUM_AMOUNT_DUE,replaceNullWithZero(p.getMinimumDueAmount()).toString()));
//				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CREDIT_CARD_OUTSTANDING_BAL,replaceNullWithZero(p.getOutstandingDueAmount()).toString()));
//				strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

			}
			
//			strBuilder.append("Current balance of your account is Rs. "+Formatter.formatDouble(accBalance));
		}
		
		else if( this.paymentModeId != null )
		{
			if( this.paymentModeId.longValue() == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue() || this.paymentModeId.longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue() )
			{
				if(Double.toString(accBalance) != null && Double.toString(accBalance).length() > 0)
				{
					strBuilder.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAMS)
						.append(TAG_SYMBOL_CLOSE)
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_BAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(replaceNullWithZero(accBalance))
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
						.append(CommandFieldConstants.KEY_FORMATED_BAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatNumbers(accBalance))
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE)

						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAMS)
						.append(TAG_SYMBOL_CLOSE);
				}

			}
			else if( this.paymentModeId.longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue() )
			{
					strBuilder.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAMS)
						.append(TAG_SYMBOL_CLOSE)
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_SPACE)
						.append(ATTR_PARAM_NAME)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(CommandFieldConstants.KEY_FORMATED_CREDIT_LIMIT)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatNumbers(this.creditLimit))
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
						.append(Formatter.formatNumbers(this.amountDue))
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
						.append(CommandFieldConstants.KEY_FORMATED_MINIMUM_AMOUNT_DUE)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatNumbers(this.minAmountDue))
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
						.append(CommandFieldConstants.KEY_FORMATED_LATE_BILL_DATE)
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_CLOSE)
						.append(Formatter.formatDate(this.dueDate))
						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAM)
						.append(TAG_SYMBOL_CLOSE)

						.append(TAG_SYMBOL_OPEN)
						.append(TAG_SYMBOL_SLASH)
						.append(TAG_PARAMS)
						.append(TAG_SYMBOL_CLOSE);

			}
			
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CoreBankMiniStatementCommand.toXML()");
		}
		return strBuilder.toString();
	}

	/**
	 * @param _smartMoneyAccountModel
	 * @return
	 */
	private SmartMoneyAccountModel getSmartMoneyAccountModel(Long paymentModeId) throws CommandException {

		SmartMoneyAccountModel smartMoneyAccountModel = null;
		
		SmartMoneyAccountModel _smartMoneyAccountModel = new SmartMoneyAccountModel();
		_smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
		_smartMoneyAccountModel.setDeleted(Boolean.FALSE);
		_smartMoneyAccountModel.setActive(Boolean.TRUE);
		_smartMoneyAccountModel.setPaymentModeId(paymentModeId);
		
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(_smartMoneyAccountModel);
		
		try {
			SearchBaseWrapper searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(wrapper);
			if(searchBaseWrapper != null) {
				List<SmartMoneyAccountModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();
				if(resultsetList != null && !resultsetList.isEmpty()) {
					smartMoneyAccountModel = resultsetList.get(0);
				}
			}
		} catch (FrameworkCheckedException e) {
			logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return smartMoneyAccountModel;
	}
	
}
