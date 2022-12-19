package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.NoSuchMessageException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

public class ATMCheckBalanceCommand extends BaseCommand {
	protected AppUserModel appUserModel;
	protected String deviceTypeId;
	private String allPayId;
	boolean errorMessagesFlag;
	private double accBalance;
	private String balanceResponse = "";
	private String pin;
	private String customerId;
	

	String veriflyErrorMessage;

	protected final Log logger = LogFactory.getLog(ATMCheckBalanceCommand.class);

	@Override
	public void execute() throws CommandException {
		logger.debug("Start of ATMCheckBalanceCommand.execute()");
		CommandManager commandManager = this.getCommandManager();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try {
			SmartMoneyAccountModel smartMoneyAccountModel = getDefaultOLAAccount();
			logger.debug("SMA account id : " + smartMoneyAccountModel.getPrimaryKey());

			if (smartMoneyAccountModel != null) {
				try {
					baseWrapper.putObject(CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId());
					baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
					baseWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
					baseWrapper.putObject(CommandFieldConstants.KEY_U_ID, customerId);
					balanceResponse = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CHK_ACC_BAL);
					balanceResponse = removeParams(balanceResponse);

				} catch (FrameworkCheckedException ex) {
					ex.printStackTrace();
					throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
				}
			}

		} catch (FrameworkCheckedException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		} catch (WorkFlowException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			// throw new
			// CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);

			String message = "";
			try {
				message = this.getMessageSource().getMessage(ex.getMessage(), null, null);
			} catch (NoSuchMessageException e) {
				message = ex.getMessage();
			}

			throw new CommandException(message, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
		}

		catch (Exception ex) {
			ex.printStackTrace();
			if (logger.isDebugEnabled()) {
				logger.debug(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR,
					ErrorLevel.MEDIUM, ex);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("End of ATMCheckBalanceCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		logger.debug("Start of ATMCheckBalanceCommand.prepare()");

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setUserId(baseWrapper.getObject("UID").toString());
		customerId = baseWrapper.getObject("UID").toString();
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		BaseWrapper wrapper = new BaseWrapperImpl();
		wrapper.setBasePersistableModel(userDeviceAccountsModel);
		try {
			wrapper = commonCommandManager.loadMfs(wrapper);
			userDeviceAccountsModel = (UserDeviceAccountsModel) wrapper.getBasePersistableModel();
			appUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();
			
			ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountsModel);
			ThreadLocalAppUser.setAppUserModel(appUserModel);
			
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		allPayId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ALLPAY_ID);
//		pin = "011c945f30ce2cbafc452f39840f025693339c42";
		pin = userDeviceAccountsModel.getPin();

		logger.debug("End of ATMCheckBalanceCommand.prepare()");
	}

	@Override
	public String response() {
		if (logger.isDebugEnabled()) {
			logger.debug("Start/End of ATMCheckBalanceCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		if (logger.isDebugEnabled()) {
			logger.debug("Start of ATMCheckBalanceCommand.validate()");
		}
		// validationErrors =
		// ValidatorWrapper.doRequired(allPayId,validationErrors,"AllPay ID");
		// validationErrors =
		// ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");

		return validationErrors;
	}

	private String toXML() {
		if (logger.isDebugEnabled()) {
			logger.debug("Start of ATMCheckBalanceCommand.toXML()");
		}
		/*
		 * StringBuilder strBuilder = new StringBuilder();
		 * 
		 * strBuilder.append(TAG_SYMBOL_OPEN) .append(TAG_PARAMS)
		 * .append(TAG_SYMBOL_CLOSE) .append(TAG_SYMBOL_OPEN) .append(TAG_PARAM)
		 * .append(TAG_SYMBOL_SPACE) .append(ATTR_PARAM_NAME)
		 * .append(TAG_SYMBOL_EQUAL) .append(TAG_SYMBOL_QUOTE)
		 * .append(CommandFieldConstants.KEY_BAL) .append(TAG_SYMBOL_QUOTE)
		 * .append(TAG_SYMBOL_CLOSE) .append(replaceNullWithZero(accBalance))
		 * .append(TAG_SYMBOL_OPEN) .append(TAG_SYMBOL_SLASH) .append(TAG_PARAM)
		 * .append(TAG_SYMBOL_CLOSE)
		 * 
		 * .append(TAG_SYMBOL_OPEN) .append(TAG_PARAM) .append(TAG_SYMBOL_SPACE)
		 * .append(ATTR_PARAM_NAME) .append(TAG_SYMBOL_EQUAL)
		 * .append(TAG_SYMBOL_QUOTE)
		 * .append(CommandFieldConstants.KEY_FORMATED_BAL)
		 * .append(TAG_SYMBOL_QUOTE) .append(TAG_SYMBOL_CLOSE)
		 * .append(Formatter.formatNumbers(accBalance)) .append(TAG_SYMBOL_OPEN)
		 * .append(TAG_SYMBOL_SLASH) .append(TAG_PARAM)
		 * .append(TAG_SYMBOL_CLOSE)
		 * 
		 * .append(TAG_SYMBOL_OPEN) .append(TAG_SYMBOL_SLASH)
		 * .append(TAG_PARAMS) .append(TAG_SYMBOL_CLOSE);
		 * 
		 * if(logger.isDebugEnabled()) {
		 * logger.debug("End of ATMCheckBalanceCommand.toXML()"); } return
		 * strBuilder.toString();
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("End of ATMCheckBalanceCommand.toXML()");
		}
		return balanceResponse;
	}

	private SmartMoneyAccountModel getDefaultOLAAccount() throws FrameworkCheckedException {
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
		
		/*
		Long retailerContactId = ThreadLocalAppUser.getAppUserModel().getRetailerContactId();
		Long distributorContactId = ThreadLocalAppUser.getAppUserModel().getDistributorContactId();

		if (retailerContactId != null)
			sma.setRetailerContactId(retailerContactId);
		else if (distributorContactId != null)
			sma.setDistributorContactId(distributorContactId);
			
			*/
		
		Long customerId = ThreadLocalAppUser.getAppUserModel().getCustomerId();

		if (customerId != null)
			sma.setCustomerId(customerId);

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(sma);

		sma = (SmartMoneyAccountModel) this.getCommonCommandManager().loadOLAAccount(baseWrapper).getBasePersistableModel();

		return sma;
	}

	private String removeParams(String xmlStr) {
		if (logger.isDebugEnabled()) {
			logger.debug("Start of AllPayLoginCommand.removeParams()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		if (xmlStr != null && !xmlStr.equals("") && xmlStr.contains(strBuilder.toString())) {
			xmlStr = xmlStr.replaceAll(strBuilder.toString(), "");
			StringBuilder strBuilderParams = new StringBuilder();
			strBuilderParams.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
			xmlStr = xmlStr.replaceAll(strBuilderParams.toString(), "");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("End of AllPayLoginCommand.removeParams()");
		}
		return xmlStr;
	}

}
