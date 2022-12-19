package com.inov8.microbank.server.service.commandmodule;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			February 2008  			
 * Description:				
 */

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_STMTS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

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


public class MiniStatementCommand extends BaseCommand
{
	protected AppUserModel appUserModel;
	protected String accountId;
	protected String pin;
	protected double accBalance;
	protected String deviceTypeId;
	protected String tPin;
	protected String statements;
	
	boolean errorMessagesFlag;
	
	String veriflyErrorMessage;

	protected final Log logger = LogFactory.getLog(CheckAccountBalanceCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAccountBalanceCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		try
		{
			if(appUserModel.getCustomerId() != null)
			{
				ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationError.hasValidationErrors())
				{
					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		
					AbstractFinancialInstitution abstractFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);
					
					if(abstractFinancialInstitution != null )
					{
						baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
						smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

						ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

						if(!validationErrors.hasValidationErrors())
						{
							if(smartMoneyAccountModel.getName() != null)
							{
								if(smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString()))
								{
									switchWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
									
									
//									CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
									switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
									switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
									
									switchWrapper.setBankId( smartMoneyAccountModel.getBankId() );
                                    switchWrapper.setPaymentModeId( smartMoneyAccountModel.getPaymentModeId() );
                                    WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
//                                    workFlowWrapper.setCustomerAccount(customerAccount);
                                    workFlowWrapper.setTransactionModel(new TransactionModel());
                                    workFlowWrapper.setMPin(this.tPin);
                                    switchWrapper.setWorkFlowWrapper(workFlowWrapper);
									
									switchWrapper = abstractFinancialInstitution.miniStatement(switchWrapper);
																		
									//accBalance = switchWrapper.getBalance();
									statements = (String)switchWrapper.getObject("STMTS");
								}
								else
								{
									throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
								}
							}
							else
							{
								throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
						}
						else
						{
							throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					else
					{
						throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.financialInstitutionDoesnotExist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else
				{
					throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			else
			{
				throw new CommandException(this.getMessageSource().getMessage("command.invalidAppUserTyp", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{			
			if(logger.isDebugEnabled())
			{
				logger.debug(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
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
		
			throw new CommandException(message,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		
		catch(Exception ex)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAccountBalanceCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAccountBalanceCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		/**
		 * 
		 *   Change by Sheraz Ahmed on June 30th, 2008
		 *   
		 *   To get the accountId according to CustomerId and BankId basis in case of BANK
		 *   Because
		 *         in BANK case only BankId is available not accountId.
		 *         
		 */
		
		/**
		 * ------------------------Start of Change------------------------------
		 */
		
		String bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		
		try{
		
			if( ( accountId == null || "".equals(accountId) ) 
	                && 
					         ( bankId != null && !("".equals(bankId)) ) )
			{
				CommonCommandManager commonCommandManager = this.getCommonCommandManager();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
				smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
				smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
			
				ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
				
				if(!validationErrors.hasValidationErrors())
				{
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
				}
				else
				{
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			else
				if( ( accountId == null || "".equals(accountId) ) 
		                && 
				         ( bankId == null || "".equals(bankId) ) )
			{
				throw new CommandException("AccountId is null and BankId is also null",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
		}
		catch(Exception ex)
		{
			if(logger.isErrorEnabled())
			{
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			ex.printStackTrace();
		}

		/**
		 * ------------------------End of Change------------------------------
		 */
		
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		tPin = this.decryptPin(tPin);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAccountBalanceCommand.prepare()");
		}
	}

	@Override
	public String response()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CheckAccountBalanceCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAccountBalanceCommand.validate()");
		}
		
		if(accountId != null && accountId.length() > 0)
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(CommandFieldConstants.KEY_ACC_ID, accountId);
			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
			
			if(commonCommandManager.checkTPin(baseWrapper))
			{
				validationErrors = ValidatorWrapper.doRequired(tPin, validationErrors, "TPIN");
				if(validationErrors.hasValidationErrors())
				{
					ValidationErrors valErrors=new ValidationErrors();

					if( Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.MFS_WEB.longValue() )
					{
						valErrors.getStringBuilder().append("Please enter TPIN");
					}
					else
						valErrors.getStringBuilder().append("Transaction cannot be processed on this version of Microbank. Please download the new version from http://www.microbank.inov8.com.pk to be able to make transactions.");

					return valErrors;	
				}
			}
			
			if( tPin != null && tPin.equals("") ) 
				validationErrors = ValidatorWrapper.doRequired(pin, validationErrors, "Pin");		
			else if( pin != null && pin.equals("") ) 
				validationErrors = ValidatorWrapper.doRequired(tPin, validationErrors, "TPin");

		}
		
		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(accountId,validationErrors,"Account Id");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAccountBalanceCommand.validate()");
		}
		return validationErrors;
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CheckAccountBalanceCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		
		
		int totalStatements=10;
		int beginIndex=0;
		int endIndex=40;
		
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
				
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_STMTS)
			.append(TAG_SYMBOL_CLOSE);
			  
		String result = new String();
		
		
		if( statements != null )
		if(this.statements.length()==400)
		{
			for (int x=0; x<totalStatements; x++)
			{	
				result=this.statements.substring(beginIndex, endIndex);
				
				result =result.trim();
				if(result!="")
				{
					strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_STMT)
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_CLOSE)
					.append(result)
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);
				}
				beginIndex=beginIndex+40;
				endIndex=endIndex+40;
				
			}
		}
		
		strBuilder.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_STMTS)
		.append(TAG_SYMBOL_CLOSE)
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAMS)
		.append(TAG_SYMBOL_CLOSE);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CheckAccountBalanceCommand.toXML()");
		}
		
		return strBuilder.toString();
	}

}
