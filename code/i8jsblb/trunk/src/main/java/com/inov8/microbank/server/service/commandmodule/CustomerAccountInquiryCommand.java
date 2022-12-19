package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_CURRENCY;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_CVV;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_IS_DEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_MPIN;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_NO;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_PN_CH_REQ;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_STATUS;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_TPIN;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_ACC_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_IS_BANK_PIN_REQ;
import static com.inov8.microbank.common.util.XMLConstants.TAG_BANK_ACCOUNT;
import static com.inov8.microbank.common.util.XMLConstants.TAG_BANK_ACCOUNTS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.inov8.microbank.common.model.smartmoneymodule.SmAcctInfoListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;



public class CustomerAccountInquiryCommand extends BaseCommand
{
	
	protected final Log logger = LogFactory.getLog(CustomerAccountInquiryCommand.class);
	
	protected AppUserModel appUserModel;
	//protected String accountId;
	protected String deviceTypeId;
	protected String bankId;
	List<SmAcctInfoListViewModel> smAcctInfoList;
//	protected String mPin;
	protected IntegrationMessageVO integrationMessageVO;
	protected List<CustomerAccount> customerAccountList;
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerAccountInquiryCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		//accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		bankId= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
//		mPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MPIN);
//		mPin = StringUtil.replaceSpacesWithPlus(mPin);
//		mPin = this.decryptPin(mPin);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerAccountInquiryCommand.prepare()");
		}
		
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerAccountInquiryCommand.validate()");
		}
		
//		if(accountId != null && accountId.length() > 0)
//		{
//			BaseWrapper baseWrapper = new BaseWrapperImpl();
//			baseWrapper.putObject(CommandFieldConstants.KEY_ACC_ID, accountId);
//			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
//			
//			if(commonCommandManager.checkTPin(baseWrapper))
//			{
//				validationErrors = ValidatorWrapper.doRequired(mPin, validationErrors, "TPIN");
//			}
//		}
		
		validationErrors = ValidatorWrapper.doRequired(bankId,validationErrors,"Bank Id");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(bankId,validationErrors,"Bank Id");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerAccountInquiryCommand.validate()");
		}
		return validationErrors;
	}	
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerAccountInquiryCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		
		try
		{
			//if(appUserModel.getCustomerId() != null)
			{
				ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationError.hasValidationErrors())
				{
					//smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
					smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
					
					if( appUserModel.getCustomerId() != null )
						smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
					else if( appUserModel.getRetailerContactId() != null )
						smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					else if( appUserModel.getDistributorContactId() != null )
						smartMoneyAccountModel.setDistributorContactId(appUserModel.getDistributorContactId());
					
					smartMoneyAccountModel.setActive(true);
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
					
					if( searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null &&
							searchBaseWrapper.getCustomList().getResultsetList().size() > 0 )
						smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
					else
						throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					AbstractFinancialInstitution abstractFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);
					
					if(abstractFinancialInstitution != null )
					{
						baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
						smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
						
						
						SmAcctInfoListViewModel sMAcctInfoListViewModel = new SmAcctInfoListViewModel();
						
						if( appUserModel.getCustomerId() != null )
							sMAcctInfoListViewModel.setCustomerId(appUserModel.getCustomerId());
						else if( appUserModel.getRetailerContactId() != null )
							sMAcctInfoListViewModel.setRetailerContactId(appUserModel.getRetailerContactId());
						else if( appUserModel.getDistributorContactId() != null )
							sMAcctInfoListViewModel.setDistributorContactId(appUserModel.getDistributorContactId());
						
						sMAcctInfoListViewModel.setBankId(Long.valueOf(bankId));
						
						searchBaseWrapper.setBasePersistableModel(sMAcctInfoListViewModel);
						searchBaseWrapper = commonCommandManager.loadSmartMoneyAccountInfo(searchBaseWrapper);
						smAcctInfoList = searchBaseWrapper.getCustomList().getResultsetList();	

						ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

						if(!validationErrors.hasValidationErrors())
						{
							if(smartMoneyAccountModel.getName() != null)
							{
//								if(smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString()))
								{
									WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
									workFlowWrapper.setTransactionModel(new TransactionModel());
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
//									workFlowWrapper.setMPin(mPin);
									switchWrapper.setWorkFlowWrapper(workFlowWrapper);
									switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
									switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
									switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
									
									switchWrapper = abstractFinancialInstitution.customerAccountRelationshipInquiry(switchWrapper);
									if(switchWrapper != null && switchWrapper.getIntegrationMessageVO() != null)
									{
										integrationMessageVO = switchWrapper.getIntegrationMessageVO();
										if(integrationMessageVO != null && integrationMessageVO.getCustomerAccounts() != null)
										{
											customerAccountList = integrationMessageVO.getCustomerAccounts();
										}
										else
										{
											throw new CommandException(MessageUtil.getMessage("CustomerAccountInquiryCommand.CustomerAccountListEmpty"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
										}
									}
									else
									{
										throw new CommandException(MessageUtil.getMessage("CustomerAccountInquiryCommand.CustomerAccountListEmpty"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
								}
//								else
//								{
//									throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//								}
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
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(WorkFlowException ex)
		{
			logger.error(ex);
			ex.printStackTrace();
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex); 
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
			logger.debug("End of CustomerAccountInquiryCommand.execute()");
		}
	}
	
	@Override
	public String response()
	{
		return toXML();
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerAccountInquiryCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
	
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_BANK_ACCOUNTS)
			.append(TAG_SYMBOL_CLOSE);
			
		if(customerAccountList != null)
		{
			
			SmAcctInfoListViewModel localSmAcctInfoListViewModel= (SmAcctInfoListViewModel)smAcctInfoList.get(0);
			for(CustomerAccount customerAccount:customerAccountList)
			{
				strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_BANK_ACCOUNT)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_ACC_NO)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(customerAccount.getNumber())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_ACC_TYPE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(customerAccount.getType())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_ACC_CURRENCY)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(customerAccount.getCurrency())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE);
					
					if(localSmAcctInfoListViewModel.getVeriflyId() != null)
					{
					
					strBuilder.append(ATTR_IS_BANK_PIN_REQ)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(this.convertBooleanToBit(true))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE);
					}
					
					else
					{
						strBuilder.append(ATTR_IS_BANK_PIN_REQ)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(this.convertBooleanToBit(false))
						.append(TAG_SYMBOL_QUOTE)
						.append(TAG_SYMBOL_SPACE);
					}
					strBuilder.append(ATTR_ACC_CVV)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getIsCvvRequired()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					
					.append(ATTR_ACC_PN_CH_REQ)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getChangePinRequired()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					
					.append(ATTR_ACC_IS_DEF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getDefAccount()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					
					
					.append(ATTR_ACC_TPIN)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getIsTpinRequired()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					
					.append(ATTR_ACC_MPIN)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(this.convertBooleanToBit(localSmAcctInfoListViewModel.getIsMpinRequired()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					
					
					
					
					
					.append(ATTR_ACC_STATUS)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(customerAccount.getStatus())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_CLOSE)
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_BANK_ACCOUNT)
					.append(TAG_SYMBOL_CLOSE);	
			}
		}	
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_BANK_ACCOUNTS)
			.append(TAG_SYMBOL_CLOSE);
	
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerAccountInquiryCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
