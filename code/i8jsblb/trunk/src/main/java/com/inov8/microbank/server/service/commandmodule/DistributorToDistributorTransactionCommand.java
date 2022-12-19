package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_ALLPAY_ID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_SUPPLIER;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.CREDIT_TRANSFER_NAME;
import static com.inov8.microbank.common.util.XMLConstants.INOV8_SUPPLIER;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;


public class DistributorToDistributorTransactionCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String mobileNo;
	protected String txAmount;
	protected String deviceTypeId;
	protected String allPayId;
	
	TransactionModel transactionModel;
	String successMessage;

	protected final Log logger = LogFactory.getLog(DistributorToDistributorTransactionCommand.class);
	
	@Override
	public void execute() throws CommandException 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of DistributorToDistributorTransactionCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		if(appUserModel.getDistributorContactId() != null)
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationErrors.hasValidationErrors())
				{
					UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
					userDeviceAccountsModel.setUserId(allPayId);
					searchBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);
					searchBaseWrapper = commonCommandManager.loadUserDeviceAccounts(searchBaseWrapper);
					CustomList<UserDeviceAccountsModel> list = searchBaseWrapper.getCustomList();
					if(null != list.getResultsetList() && ! list.getResultsetList().isEmpty())
					{
						userDeviceAccountsModel = list.getResultsetList().get(0);
					}
					else
					{
						throw new CommandException(this.getMessageSource().getMessage("allpay.user.not.exist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					AppUserModel distributorOrRetailer = userDeviceAccountsModel.getAppUserIdAppUserModel();
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					workFlowWrapper.setDistOrRetAppUserModel(distributorOrRetailer);
					if(distributorOrRetailer.getRetailerContactId() != null && distributorOrRetailer.getDistributorContactId() == null)
					{
						// funds transfer is to a retailer so the retailer flow will get executed
						AppUserModel toRetailerAppUserModel = new AppUserModel();
						transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DIST_TO_RET_TX);
						DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
						deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
						toRetailerAppUserModel.setMobileNo(distributorOrRetailer.getMobileNo());
						SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
						smartMoneyAccountModel.setDistributorContactId(appUserModel.getDistributorContactId());
						SmartMoneyAccountModel receivingSmartMoneyAccountModel = new SmartMoneyAccountModel();
						receivingSmartMoneyAccountModel.setRetailerContactId(distributorOrRetailer.getRetailerContactId());
						workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
						workFlowWrapper.setReceivingSmartMoneyAccountModel(receivingSmartMoneyAccountModel);
						workFlowWrapper.setToRetailerContactAppUserModel(toRetailerAppUserModel);
						workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
						workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
						workFlowWrapper.setFromDistributorContactAppUserModel(appUserModel);
					
					}
					else if(distributorOrRetailer.getRetailerContactId() == null && distributorOrRetailer.getDistributorContactId() != null)
					{
//						 funds transfer is to a distributor so the distributor flow will get executed
						transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DIST_TO_DIST_TX);
						AppUserModel toDistributorAppUserModel = new AppUserModel();
						toDistributorAppUserModel.setMobileNo(distributorOrRetailer.getMobileNo());
						DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
						deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
						SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
						smartMoneyAccountModel.setDistributorContactId(appUserModel.getDistributorContactId());
						SmartMoneyAccountModel receivingSmartMoneyAccountModel = new SmartMoneyAccountModel();
						receivingSmartMoneyAccountModel.setDistributorContactId(distributorOrRetailer.getDistributorContactId());
						workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
						workFlowWrapper.setReceivingSmartMoneyAccountModel(receivingSmartMoneyAccountModel);
						workFlowWrapper.setToDistributorContactAppUserModel(distributorOrRetailer);
						workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
						workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
						workFlowWrapper.setFromDistributorContactAppUserModel(appUserModel);
					}
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
						
					commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
					transactionModel = workFlowWrapper.getTransactionModel();
					
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
				}
				else
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(Exception ex)
			{
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}	
		}
		else
		{
			throw new CommandException(this.getMessageSource().getMessage("distributorTransactionCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of DistributorToDistributorTransactionCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of DistributorToDistributorTransactionCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		allPayId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ALLPAY_ID);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of DistributorToDistributorTransactionCommand.prepare()");
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
			logger.debug("Start of DistributorToDistributorTransactionCommand.validate()");
		}
//		validationErrors = ValidatorWrapper.doRequired(mobileNo,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(allPayId,validationErrors,"AllPay ID");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(allPayId,validationErrors,"AllPay ID");
			validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of DistributorToDistributorTransactionCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of DistributorToDistributorTransactionCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_ALLPAY_ID)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(allPayId)
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TYPE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(transactionModel.getTransactionTypeId())
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_DATE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(transactionModel.getCreatedOn())
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_DATEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT))
//			.append(Formatter.formatDate(transactionModel.getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_PRODUCT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(CREDIT_TRANSFER_NAME)
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_SUPPLIER)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(INOV8_SUPPLIER)
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TIMEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatTime(transactionModel.getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_FORMATED_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(transactionModel.getTotalAmount()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithZero(transactionModel.getTotalAmount()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of DistributorToDistributorTransactionCommand.toXML()");
		}
		return strBuilder.toString();
	}
}
