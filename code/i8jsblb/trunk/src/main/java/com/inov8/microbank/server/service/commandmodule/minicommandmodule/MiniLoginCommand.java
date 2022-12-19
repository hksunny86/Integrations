
package com.inov8.microbank.server.service.commandmodule.minicommandmodule;


import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.userdeviceaccountmodule.UserDeviceAccountListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;


/**
 * 
 * @author Jawwad Farooq
 * July, 2007
 * 
 */

public class MiniLoginCommand extends MiniBaseCommand
{

	protected String userId;
	protected String deviceTypeId;
	protected String mobileNo;
	protected final Log logger = LogFactory.getLog(getClass());
	protected int tillBalanceRequired;

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{		
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		mobileNo = this.removeCountryCode(mobileNo);
	}

	@Override
	public void doValidate() throws CommandException
	{
		ValidationErrors validationErrors = new ValidationErrors();		
		validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");

		if (!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}

		if (validationErrors.hasValidationErrors())
		{
			logger.error(validationErrors.getErrors());
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR,
					ErrorLevel.HIGH, new Throwable());
		}
	}

	@Override
	public void execute() throws CommandException
	{
		try {
			
			checkLogin(this.getCommonCommandManager());
			
		} catch (Exception e) {
			e.printStackTrace();
			if(!StringUtil.isNullOrEmpty(this.deviceTypeId) && Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.SMS_GATEWAY.longValue() && e instanceof CommandException) {
				
				sendErrorSMS(e.getMessage());
			}
			
			throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, e);
		}
		
		userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
		if(Long.valueOf(deviceTypeId) != DeviceTypeConstantsInterface.SMS_GATEWAY){			
			checkTillBalanceRequired(this.getCommonCommandManager());
		}
		
	}
	
	private void sendErrorSMS(String msgToText) {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(this.mobileNo , msgToText));
		
		try {
			
			this.getCommonCommandManager().sendSMSToUser(baseWrapper);
		
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public String response()
	{
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAMS)
		.append(TAG_SYMBOL_CLOSE)
		/*Adding parameter for till balance report functionality*/
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_TILL_BALANCE_AMOUNT)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(tillBalanceRequired)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_PARAM)
			.append(TAG_SYMBOL_CLOSE)
			/*End Adding parameter for till balance functionality*/ 
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAMS)
		.append(TAG_SYMBOL_CLOSE);
		return strBuilder.toString();
	}

	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}
	
	private BaseWrapper checkTillBalanceRequired(CommonCommandManager commonCommandManager) throws CommandException
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.AM_PM, Calendar.AM);
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		AgentOpeningBalModel agentOpeningBalModel = new AgentOpeningBalModel();
		agentOpeningBalModel.setAgentId(userId);
		agentOpeningBalModel.setBalDate(calendar.getTime());
		searchBaseWrapper.setBasePersistableModel(agentOpeningBalModel);
		try
		{
		searchBaseWrapper = commonCommandManager.checkTillBalanceRequired(searchBaseWrapper);
		if(null != searchBaseWrapper.getCustomList() && null != searchBaseWrapper.getCustomList().getResultsetList() && searchBaseWrapper.getCustomList().getResultsetList().size() >0)
		{
			List<AgentOpeningBalModel> list = searchBaseWrapper.getCustomList().getResultsetList();
			agentOpeningBalModel = list.get(0);
			if(null == agentOpeningBalModel.getOpeningAccountBalance())
			{
				switchWrapper = commonCommandManager.checkAgentBalance();
				double balance = switchWrapper.getBalance();
				agentOpeningBalModel.setOpeningAccountBalance(balance);
				agentOpeningBalModel.setRunningAccountBalance(balance);
				if(null != switchWrapper.getAccountInfoModel() && null == agentOpeningBalModel.getAccountNumber())
				{
					agentOpeningBalModel.setAccountNumber(switchWrapper.getAccountInfoModel().getAccountNo());
				}
				baseWrapper.setBasePersistableModel(agentOpeningBalModel);
				baseWrapper = commonCommandManager.saveTillBalance(baseWrapper);
				tillBalanceRequired = 0;
				
			}
			else
			{
				tillBalanceRequired = 0;
			}
			
		}
		else
		{
			tillBalanceRequired = 1;
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CommandException("Your Core Banking Account cannot be accessed at the moment. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, e);
		}
		return baseWrapper;
	}

	private BaseWrapper checkLogin(CommonCommandManager commonCommandManager) throws CommandException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<UserDeviceAccountListViewModel> list;
		UserDeviceAccountsModel userDeviceAccountsModel = null;

		try
		{
			UserDeviceAccountListViewModel example = new UserDeviceAccountListViewModel();
			example.setMobileNo(mobileNo);
			searchBaseWrapper.setBasePersistableModel(example);
			searchBaseWrapper = commonCommandManager.loadMiniUserDeviceAccountsListView(searchBaseWrapper);
			list = searchBaseWrapper.getCustomList().getResultsetList();

			UserDeviceAccountListViewModel userDeviceAccountListViewModel= null;
			
			if (null != list && !list.isEmpty()) {
				
				for(UserDeviceAccountListViewModel _userDeviceAccountListViewModel : list) {
					
					long appUserTypeId = _userDeviceAccountListViewModel.getAppUserTypeId().longValue();
					
					if(appUserTypeId == UserTypeConstantsInterface.CUSTOMER.longValue() || 
							appUserTypeId == UserTypeConstantsInterface.RETAILER.longValue()) {
						
						userDeviceAccountListViewModel = _userDeviceAccountListViewModel;

						break;
						
					} else {
						
						continue;
					}
				}				
			}
			
			if (null != userDeviceAccountListViewModel)
			{

				// Update UserDeviceAccount of this mini user
				baseWrapper = this.updateUserDeviceAccountLoginTime(userDeviceAccountListViewModel);
				userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();

				
				boolean messageSeparatorFlag = false;
				StringBuilder strBuilder = new StringBuilder();

				if (userDeviceAccountListViewModel.getAccountExpired())
				{
					strBuilder.append(this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.EXPIRED", null,
							null));
					messageSeparatorFlag = true;
					
					throw new CommandException(this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.EXPIRED", null, null),
							ErrorCodes.ACCOUNT_EXPIRED_ERROR, ErrorLevel.MEDIUM, new Throwable());
			
				}
				if (userDeviceAccountListViewModel.getAccountLocked())
				{
					if (messageSeparatorFlag)
					{
						strBuilder.append(";");
						strBuilder.append(this.getMessageSource().getMessage("CUSTOMER.accountLocked",
								null, null));
					}
					else
					{
						strBuilder.append(this.getMessageSource().getMessage("CUSTOMER.accountLocked",
								null, null));
						messageSeparatorFlag = true;
					}
					
					throw new CommandException(this.getMessageSource().getMessage("CUSTOMER.accountLocked", null, null),
							ErrorCodes.ACCOUNT_LOCKED, ErrorLevel.MEDIUM, new Throwable());

				}
				if (userDeviceAccountListViewModel.getCredentialsExpired())
				{
					if (messageSeparatorFlag)
					{
						strBuilder.append(";");
						strBuilder.append(this.getMessageSource().getMessage(
								"LoginCommand.credentialsExpired", null, null));
					}
					else
					{
						strBuilder.append(this.getMessageSource().getMessage(
								"LoginCommand.credentialsExpired", null, null));
						messageSeparatorFlag = true;
					}
					
					throw new CommandException(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null, null),
							ErrorCodes.CREDENTIALS_EXPIRED, ErrorLevel.MEDIUM, new Throwable());
					
				}
				if (!userDeviceAccountListViewModel.getAccountEnabled())
				{
					if (messageSeparatorFlag)
					{
						strBuilder.append(";");
						strBuilder.append(this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.DEACTIVATED",
								null, null));
					}
					else
					{
						strBuilder.append(this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.DEACTIVATED",
								null, null));
					}
					
					throw new CommandException(this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.DEACTIVATED", null, null),
							ErrorCodes.ACCOUNT_DISABLED_ERROR, ErrorLevel.MEDIUM, new Throwable());
				}

				if (strBuilder.length() == 0)
				{
					AppUserModel appUserModel = new AppUserModel();
					appUserModel.setAppUserId(userDeviceAccountListViewModel.getAppUserId());
					baseWrapper.setBasePersistableModel(appUserModel);
					baseWrapper = commonCommandManager.loadAppUser(baseWrapper);
					appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
					

					if (appUserModel != null)
					{
						if (appUserModel.getCustomerId() != null)
						{
							CustomerModel customerModel = new CustomerModel();
							customerModel.setCustomerId(appUserModel.getCustomerId());
							baseWrapper.setBasePersistableModel(customerModel);
							baseWrapper = commonCommandManager.loadCustomer(baseWrapper);
							customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
							if (customerModel.getRegister())
							{
								
							}
							else
							{
								logger.error("Exception thrown by LoginCommand.check that Customer is Registered");
								throw new CommandException(this.getMessageSource().getMessage(
										"LoginCommand.customerInactive", null, null),
										ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
							}
						}						
						
						if(appUserModel.getAccountClosedUnsettled()) {

							throw new CommandException(this.getMessageSource().getMessage("CUSTOMER.ACCOUNT.CLOSED", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, new Exception());
						}			
						
						baseWrapper.setBasePersistableModel(appUserModel);
						baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.SMS_GATEWAY.toString());
						validationErrors = commonCommandManager.checkUserCredentials(baseWrapper);
						
						if(validationErrors.hasValidationErrors()) {
							throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
						}						
						
						ThreadLocalAppUser.setAppUserModel(appUserModel);
						ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountsModel);
					}
				}
				else
				{
					logger.error("Exception thrown by LoginCommand.checkLogin()");
					strBuilder.append(this.getMessageSource().getMessage(
							"LoginCommand.contactServiceProvider", null, null));
					throw new CommandException(strBuilder.toString(), ErrorCodes.INVALID_USER_ACCOUNT,
							ErrorLevel.MEDIUM, new Throwable());
				}
			}
			else
			{
				logger.error("Exception thrown by LoginCommand.checkLogin()");
				if(Long.valueOf(deviceTypeId) == 4){
					response = "07";
				}else{
					response = this.getMessageSource().getMessage("MINI.NotMiniUser", null, null) ;					
				}
				throw new CommandException(response, ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
			}

		}
		catch (FrameworkCheckedException ex)
		{
			if(ex.getErrorCode() == ErrorCodes.ACCOUNT_EXPIRED_ERROR.longValue()
					|| ex.getErrorCode() == ErrorCodes.ACCOUNT_DISABLED_ERROR.longValue()
					|| ex.getErrorCode() == ErrorCodes.PIN_CHANGE_ERROR.longValue()
					|| ex.getErrorCode() == ErrorCodes.CREDENTIALS_EXPIRED.longValue()
					|| ex.getErrorCode() == ErrorCodes.ACCOUNT_BLOCKED.longValue()|| ex.getErrorCode() == ErrorCodes.ACCOUNT_LOCKED.longValue()){
				logger.error("Exception thrown by LoginCommand.checkLogin()");
				throw new CommandException(ex.getMessage(), ex.getErrorCode(),ErrorLevel.MEDIUM, ex);
			}
			ex.printStackTrace();
			logger.error("Exception thrown by LoginCommand.checkLogin()");
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, ex);
		}
		return baseWrapper;
	}


	public BaseWrapper updateUserDeviceAccount(UserDeviceAccountsModel userDeviceAccountModel,
			CommonCommandManager commonCommandManager) throws FrameworkCheckedException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(userDeviceAccountModel);
		baseWrapper = commonCommandManager.updateMfsPin(baseWrapper);
		return baseWrapper;
	}

	public BaseWrapper updateUserDeviceAccountLoginTime(
			UserDeviceAccountListViewModel userDeviceAccountListViewModel) throws FrameworkCheckedException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(userDeviceAccountListViewModel);

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setPrimaryKey(userDeviceAccountListViewModel.getUserDeviceAccountsId());
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.getCommonCommandManager().loadUserDeviceAccount(baseWrapper);

		userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
		userDeviceAccountsModel.setLastLoginAttemptTime(new Timestamp(System.currentTimeMillis()));
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);

		/**
		 * @TODO Refactor the updateMfsPin method of commonCommandManager
		 */
		baseWrapper = this.getCommonCommandManager().updateMfsPin(baseWrapper);
		return baseWrapper;
	}
}
