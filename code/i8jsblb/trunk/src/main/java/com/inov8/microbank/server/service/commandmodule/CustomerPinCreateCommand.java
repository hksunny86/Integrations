package com.inov8.microbank.server.service.commandmodule;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.constants.VeriflyConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;


public class CustomerPinCreateCommand extends BaseCommand 
{
	protected AppUserModel customerAppUserModel;
	protected String accountId;
	protected String bankId;
	protected String pin;
	protected String newPin;
	protected String confirmPin;
	protected String deviceTypeId;
	protected String customerMobileNumber;
	String veriflyErrorMessage;
	boolean errorMessagesFlag;
	UserDeviceAccountsModel userDeviceAccountsModel; 
	protected String encryption_type;
	
	protected final Log logger = LogFactory.getLog(VeriflyPinChangeCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
		try
		{
			if(customerAppUserModel.getCustomerId() != null)
			{
				String brandName = MessageUtil.getMessage("jsbl.brandName");
				ValidationErrors validationError = commonCommandManager.checkActiveAppUser(customerAppUserModel);
				if(!validationError.hasValidationErrors())
				{					
					
					smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
					smartMoneyAccountModel.setDeleted(Boolean.FALSE);
					smartMoneyAccountModel.setActive(Boolean.TRUE); //check this if we need to skip this check in case of new customer??
					smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
					SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
					wrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					try {
						
						SearchBaseWrapper searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(wrapper);
						
						if(searchBaseWrapper != null) {
							
							List<SmartMoneyAccountModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();
							
							if(resultsetList != null && !resultsetList.isEmpty()) {
								
								smartMoneyAccountModel = resultsetList.get(0);
							}
						}
						
					} catch (FrameworkCheckedException e) {
						
						throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
					}
					
					logger.info("Found Smart Money Account "+ smartMoneyAccountModel.getName());
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
										
					AbstractFinancialInstitution abstractFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);

					if(abstractFinancialInstitution != null)
					{
						baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper); 
						smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

						
						if(smartMoneyAccountModel.getActive())
						{
							if(smartMoneyAccountModel.getName() != null)
							{
								if((null != smartMoneyAccountModel.getCustomerId() && smartMoneyAccountModel.getCustomerId().toString().equals(customerAppUserModel.getCustomerId().toString())) || 
										(null != smartMoneyAccountModel.getRetailerContactId() && smartMoneyAccountModel.getRetailerContactId().toString().equals(customerAppUserModel.getRetailerContactId().toString())))
								{
									AccountInfoModel accountInfoModel = new AccountInfoModel();
									veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
								     LogModel logModel = new LogModel();

								       
								          accountInfoModel.setCustomerId(smartMoneyAccountModel.getCustomerId());


								        
								          accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
								        

								        
								          logModel.setCreatedBy(customerAppUserModel.getUsername());
								        

								        
								          logModel.setCreatdByUserId(customerAppUserModel.getAppUserId());
								        

								        //create wrapper object and populate with AccountInfoModel
								          veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
								          veriflyBaseWrapper.setLogModel(logModel);
								          veriflyBaseWrapper.putObject(VeriflyConstants.IVR_USER_PIN, newPin);
								          veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
								        try {
								            baseWrapper =
								            		abstractFinancialInstitution.resetPin(veriflyBaseWrapper);
								        } catch (Exception ex) {
								            ex.printStackTrace();
								        }
									
																		
									errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
															
									if(errorMessagesFlag)
									{						
										accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
										smartMoneyAccountModel.setChangePinRequired(Boolean.FALSE);
										smartMoneyAccountModel.setUpdatedOn(new Date());
										smartMoneyAccountModel.setUpdatedByAppUserModel(customerAppUserModel);
										baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
										commonCommandManager.updateSmartMoneyAccount(baseWrapper);
										
										BaseWrapper _baseWrapper = new BaseWrapperImpl();
										_baseWrapper.setBasePersistableModel(new CustomerModel(customerAppUserModel.getCustomerId()));
										this.getCommonCommandManager().loadCustomer(_baseWrapper);
										CustomerModel customerModel = (CustomerModel) _baseWrapper.getBasePersistableModel();
										customerModel.setIsMPINGenerated(Boolean.TRUE);
										this.getCommonCommandManager().saveOrUpdateCustomerModel(customerModel);
										
										String receiverSMS = this.getMessageSource().getMessage("CustomerNewPIN_IVR",null,null);
										messageList.add(new SmsMessage(customerAppUserModel.getMobileNo(), receiverSMS));
										veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);	
										commonCommandManager.sendSMS(veriflyBaseWrapper);
									}
									else
									{
										veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
										/*String receiverSMS = this.getMessageSource().getMessage(
												"RecipientBBToT24SMS",
												new Object[] { 
														brandName,
														wrapper.getTransactionCodeModel().getCode(),
														Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
														PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
														PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
														wrapper.getTransactionDetailModel().getCustomField11(),
														wrapper.getTransactionModel().getNotificationMobileNo()
													}, null);
										messageList.add(new SmsMessage(wrapper.getTransactionDetailModel().getCustomField5(), receiverSMS));*/
										throw new CommandException(veriflyErrorMessage,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
								}
								else
								{
									throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
								}
							}
							else
							{
								throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
						}
						else
						{
							throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					else
					{
						throw new CommandException(this.getMessageSource().getMessage("veriflyPinChangeCommand.veriflyManagerNotExists", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else
				{
					throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			else
			{
				throw new CommandException(this.getMessageSource().getMessage("command.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(Exception ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VeriflyPinChangeCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.prepare()");
		}
		customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
//		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		
				
		pin = StringUtil.replaceSpacesWithPlus(pin);		
		newPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NEW_PIN);
		newPin = StringUtil.replaceSpacesWithPlus(newPin);
		confirmPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CONF_PIN);
		confirmPin = StringUtil.replaceSpacesWithPlus(confirmPin);
		try{
			
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setMobileNo(customerMobileNumber);
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(appUserModel);
			searchBaseWrapper = this.getCommonCommandManager().loadAppUserByMobileNumberAndType(searchBaseWrapper);
			customerAppUserModel = (AppUserModel)searchBaseWrapper.getBasePersistableModel();

			
//			customerAppUserModel = this.getCommonCommandManager().loadAppUserByMobileByQuery(customerMobileNumber);
		baseWrapper.setBasePersistableModel(customerAppUserModel);
		baseWrapper = this.getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
		userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
		baseWrapper.setBasePersistableModel(customerAppUserModel);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VeriflyPinChangeCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.validate()");
		}
		//validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
		validationErrors = ValidatorWrapper.doRequired(newPin,validationErrors,"New Pin");
		validationErrors = ValidatorWrapper.doRequired(confirmPin,validationErrors,"Confirm Pin");
		//validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");
        
        if(!validationErrors.hasValidationErrors())
        {
             byte enc_type = new Byte(encryption_type).byteValue();
             ThreadLocalEncryptionType.setEncryptionType(enc_type);
        }
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of VeriflyPinChangeCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.toXML()");
		}
		
		StringBuilder strBuilder = new StringBuilder();
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of VeriflyPinChangeCommand.toXML()");
		}
		return strBuilder.toString();
	}
}
