package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_BANK_RESPONSE_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PAYMENT_MODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_SUPPLIER;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;
import static org.apache.commons.lang.StringEscapeUtils.escapeXml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;




public class CustomerDiscreteProductSaleCommand extends BaseCommand
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected String accountId;
	protected String mobileNo;
	protected String deviceTypeId;
	protected String pin;
	protected String cvv;
	protected String tPin;
	
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	protected double discountAmount;
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	BaseWrapper baseWrapper;
	
	
	
	protected final Log logger = LogFactory.getLog(CustomerDiscreteProductSaleCommand.class);
	
	TransactionModel transactionModel;
	SmartMoneyAccountModel smartMoneyAccountModel;
	ProductModel productModel;
	ProductVO productVO = null;
	String successMessage;

	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerDiscreteProductSaleCommand.execute()");
		}
	
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
//		BaseWrapper baseWrapper1 = new BaseWrapperImpl();
		
		if(appUserModel.getCustomerId() != null)
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationErrors.hasValidationErrors())
				{
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
								
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					baseWrapper.putObject((CommandFieldConstants.KEY_PROD_ID).toString() , productId );
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					System.out.println(" Class of Product VO " + productVo.getClass().getName());
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("customerVariableProductSaleCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					
					AppUserModel customerAppUserModel = new AppUserModel();
					if(mobileNo == null)
						customerAppUserModel.setMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
					else
						customerAppUserModel.setMobileNo(mobileNo);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUST_DISC_PRODUCT_SALE_TX);
						
					smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
			
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					
					CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
					workFlowWrapper.setCustomerAccount(customerAccount);
					
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
					
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
					discountAmount = productModel.getFixedDiscount() + (productModel.getPercentDiscount()/100) * workFlowWrapper.getTransactionModel().getTotalAmount();
					productVO = workFlowWrapper.getProductVO() ;
				}
				else
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(Exception ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			throw new CommandException(this.getMessageSource().getMessage("customerProductSaleCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerDiscreteProductSaleCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerDiscreteProductSaleCommand.prepare()");
		}
		
		this.baseWrapper = baseWrapper ;
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
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
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
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

		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		cvv = this.decryptPin(cvv);
		tPin = this.decryptPin(tPin);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerDiscreteProductSaleCommand.prepare()");
		}
	}

	@Override
	public String response()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CustomerDiscreteProductSaleCommand.prepare()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerDiscreteProductSaleCommand.validate()");
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
					valErrors.getStringBuilder().append("Transaction cannot be processed on this version of Microbank. Please download the new version from http://www.microbank.inov8.com.pk to be able to make transactions.");
					return valErrors;	
				}
			}
			
			if(commonCommandManager.isAccountValidationRequired(baseWrapper))
			{
				validationErrors = ValidatorWrapper.doRequired(accountNumber, validationErrors, "Account Number");
				validationErrors = ValidatorWrapper.doRequired(accountType, validationErrors, "Account Type");
				validationErrors = ValidatorWrapper.doRequired(accountCurrency, validationErrors, "Account Currency");
				validationErrors = ValidatorWrapper.doRequired(accountStatus, validationErrors, "Account Status");
			}
			else if( tPin != null && tPin.equals("") )
			{
				validationErrors = ValidatorWrapper.doRequired(pin, validationErrors, "Pin");		
			}
		}
		
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		//validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
		if(mobileNo != null && mobileNo.length() > 0)
		{
			throw new CommandException(this.getMessageSource().getMessage("customerDiscreteProductSaleCommand.mobileNoExistError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		

		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
			
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(accountId,validationErrors,"Account Id");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerDiscreteProductSaleCommand.validate()");
		}
		return validationErrors;
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerDiscreteProductSaleCommand.toXML()");
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
//			.append(TAG_SYMBOL_SPACE)
//			.append(ATTR_TRN_MOB_NO)
//			.append(TAG_SYMBOL_EQUAL)
//			.append(TAG_SYMBOL_QUOTE)
//			.append(replaceNullWithEmpty(transactionModel.getNotificationMobileNo()))
//			.append(TAG_SYMBOL_QUOTE)
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
			//.append(Formatter.formatDate(transactionModel.getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TIMEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatTime(transactionModel.getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PAYMENT_MODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(escapeXml(replaceNullWithEmpty(smartMoneyAccountModel.getName())))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_PRODUCT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(productModel.getName()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_SUPPLIER)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(productModel.getSupplierIdSupplierModel().getName()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_BANK_RESPONSE_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getBankResponseCode()))
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
			.append(TAG_SYMBOL_QUOTE);
			
//			if(productModel != null && productModel.getHelpLineNotificationMessageModel().getSmsMessageText() != "") 
//			{
//				strBuilder.append(TAG_SYMBOL_SPACE)
//					.append(ATTR_TRN_HELPLINE)
//					.append(TAG_SYMBOL_EQUAL)
//					.append(TAG_SYMBOL_QUOTE)
//					.append(replaceNullWithEmpty(productModel.getHelpLineNotificationMessageModel().getSmsMessageText()))
//					.append(TAG_SYMBOL_QUOTE);		
//			}
			if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
			{
				strBuilder.append(TAG_SYMBOL_SPACE)
				.append(XMLConstants.ATTR_FORMATTED_DISCOUNT_AMOUNT)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(Formatter.formatNumbers(discountAmount))
				.append(TAG_SYMBOL_QUOTE)
				.append(TAG_SYMBOL_SPACE)
				.append(XMLConstants.ATTR_DISCOUNT_AMOUNT)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(replaceNullWithEmpty(String.valueOf(discountAmount)))
				.append(TAG_SYMBOL_QUOTE);
				
			}
			
//			if(null != productVO && productVO.getResponseCode() != null )
//			{
//				strBuilder.append(TAG_SYMBOL_SPACE)				
//				.append(XMLConstants.ATTR_TRACKING_ID)
//				.append(TAG_SYMBOL_EQUAL)
//				.append(TAG_SYMBOL_QUOTE)
//				.append(replaceNullWithEmpty(productVO.getResponseCode()))
//				.append(TAG_SYMBOL_QUOTE);				
//			}
		
			
			
			strBuilder.append(TAG_SYMBOL_CLOSE)
			
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
			logger.debug("End of CustomerDiscreteProductSaleCommand.toXML()");
		}
		return strBuilder.toString();
	}
}
