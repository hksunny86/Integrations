package com.inov8.microbank.server.service.commandmodule.minicommandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_BANK_RESPONSE_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PAYMENT_MODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_HELPLINE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_MOB_NO;
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

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;


public class CWBillPaymentCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected String accountId;
	protected String mobileNo;
	protected String txProcessingAmount;
	protected String pin;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String totalAmount;
	protected String billAmount;
	protected String cvv;
	protected String tPin;
	protected String oneTimePin;
	protected String tempCwDeviceTypeId;
	
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	protected double discountAmount = 0d;
	
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel smartMoneyAccountModel;
	UserDeviceAccountsModel userDeviceAccountsModel; 
	TransactionCodeModel transactionCodeModel;
	Double agent1Commission;
	Double agentBalance;
	private TransactionDetailMasterModel transactionDetailMasterModel; 
	
	protected final Log logger = LogFactory.getLog(CWBillPaymentCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CWBillPaymentCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		
		if(appUserModel.getCustomerId() != null ) 
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
					
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
						
					/*AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel.setMobileNo(mobileNo);
					customerAppUserModel.setCustomerId(appUserModel.getCustomerId());*/
					
					CustomerModel cModel = new CustomerModel();
					cModel.setCustomerId(appUserModel.getCustomerId());
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.setBasePersistableModel(cModel);
					bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
					if(null != bWrapper.getBasePersistableModel())
					{
						cModel = (CustomerModel) bWrapper.getBasePersistableModel();
					}
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_WITHDRAWAL_TX);
						
					smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
			
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
					if(null != transactionCodeModel)
					{
						workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
					}
					workFlowWrapper.setCustomerModel(cModel);
					workFlowWrapper.setSegmentModel(cModel.getSegmentIdSegmentModel());
					workFlowWrapper.setCustomerAccount(customerAccount);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(appUserModel/*customerAppUserModel*/);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
					workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
					workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
					workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.setOneTimePin(oneTimePin); // For CW
					workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
					
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					workFlowWrapper.putObject(CommandFieldConstants.KEY_AW_DTID, tempCwDeviceTypeId);
//					workFlowWrapper.putObject(CommandFieldConstants.KEY_PIN, )
					workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
					logger.info("[CWBillPaymentComand.execute] Goning to execute transactio flow. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Mobile No:" + mobileNo + 
							" Transaction ID:" + (transactionCodeModel == null ? " is NULL" : transactionCodeModel.getCode()) + 
							" Transaction Amount:" + billAmount);
				
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					discountAmount = workFlowWrapper.getDiscountAmount();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					
					agent1Commission=workFlowWrapper.getCommissionAmountsHolder().getAgent1CommissionAmount();
					agentBalance=workFlowWrapper.getSwitchWrapper().getAgentBalance();
				}
				else
				{
					logger.error("[CWBillPaymentComand.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Errors:" + validationErrors.getErrors());
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CWBillPaymentComand.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CWBillPaymentComand.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CWBillPaymentComand.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.error("[CWBillPaymentComand.execute] Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null));
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CWBillPaymentCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		SearchBaseWrapper sWrapper = new SearchBaseWrapperImpl();
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CWBillPaymentCommand.prepare()");
		}
		this.baseWrapper = baseWrapper;
//		appUserModel = ThreadLocalAppUser.getAppUserModel();
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE);
		appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		sWrapper.setBasePersistableModel(appUserModel);
		try
		{
			sWrapper = this.getCommonCommandManager().loadAppUserByMobileNumberAndType(sWrapper);
		}
		catch(Exception e)
		{
			logger.error(e.getStackTrace());
		}
		appUserModel = (AppUserModel) sWrapper.getBasePersistableModel();
		if(null != baseWrapper.getObject(CommandFieldConstants.KEY_TX_CODE_MODEL)) // Added by Maqsood Shahzad for USSD CW transaction
		{
			transactionCodeModel = (TransactionCodeModel) baseWrapper.getObject(CommandFieldConstants.KEY_TX_CODE_MODEL);
		}
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		
		if(null != baseWrapper.getObject(CommandFieldConstants.KEY_TX_DTL_MASTER_MODEL)) {
			transactionDetailMasterModel = (TransactionDetailMasterModel)baseWrapper.getObject(CommandFieldConstants.KEY_TX_DTL_MASTER_MODEL);
		}
		
		logger.info("[CWBillPaymentComand.prepare] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Mobile No:" + mobileNo + 
					" Transaction ID:" + (transactionCodeModel == null ? " is NULL" : transactionCodeModel.getCode()));
		
		/**
		 * 
		 *   Change by Sheraz Ahmed on June 30th, 2008
		 *   
		 *   To get the accountId according to CustomerId and BankId basis in case of BANK
		 *   Because in Bank case only BankId is available not accountId.
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
		
		
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		cvv = this.decryptPin(cvv);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		tPin = this.decryptPin(tPin);
		if(null != this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT) && !"".equals(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)))
		{
			discountAmount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT));
		}
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CWBillPaymentCommand.prepare()");
		}
		
		oneTimePin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ONE_TIME_PIN);
		tempCwDeviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AW_DTID);
		
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CWBillPaymentCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CWBillPaymentCommand.validate()");
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
						valErrors.getStringBuilder().append("Please enter T-PIN/M-PIN");
					}
					else
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
			else if( pin != null && pin.equals("") )
			{
				validationErrors = ValidatorWrapper.doRequired(tPin, validationErrors, "T-Pin");		
			}
			
			
		}
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
//		validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
		validationErrors = ValidatorWrapper.doRequired(mobileNo,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
		validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
		validationErrors = ValidatorWrapper.doRequired(billAmount,validationErrors,"Bill Amount");
				
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(accountId,validationErrors,"Account Id");	
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Amount");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CWBillPaymentCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CWBillPaymentCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRN)
			
			.append(TAG_SYMBOL_SPACE)
			.append(CommandFieldConstants.KEY_BILL_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(transactionModel.getTransactionAmount())
			.append(TAG_SYMBOL_QUOTE)
			
			
			
			.append(TAG_SYMBOL_SPACE)
			.append(CommandFieldConstants.KEY_A1_COMM_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(agent1Commission)
			.append(TAG_SYMBOL_QUOTE)
			
			
			.append(TAG_SYMBOL_SPACE)
			.append(CommandFieldConstants.KEY_A1_BAL)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(agentBalance)
			.append(TAG_SYMBOL_QUOTE)
			
			
			.append(TAG_SYMBOL_SPACE)
			.append(CommandFieldConstants.KEY_TX_PROCESS_AMNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(transactionModel.getTotalCommissionAmount())
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(CommandFieldConstants.KEY_TOTAL_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(transactionModel.getTotalAmount())
			.append(TAG_SYMBOL_QUOTE)
			
			
			
			.append(TAG_SYMBOL_SPACE)
			.append(CommandFieldConstants.KEY_COMM_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(commissionAmount)
			.append(TAG_SYMBOL_QUOTE)
			
			
			
			
			
			
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_MOB_NO)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(transactionModel.getNotificationMobileNo()))
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
			
		if(productModel != null && productModel.getHelpLineNotificationMessageModel().getSmsMessageText() != "") 
		{
			strBuilder.append(TAG_SYMBOL_SPACE)
				.append(ATTR_TRN_HELPLINE)
				.append(TAG_SYMBOL_EQUAL)
				.append(TAG_SYMBOL_QUOTE)
				.append(replaceNullWithEmpty(productModel.getHelpLineNotificationMessageModel().getSmsMessageText()))
				.append(TAG_SYMBOL_QUOTE);		
		}
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

					
		strBuilder.append(TAG_SYMBOL_CLOSE);
		
		
		
			strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CWBillPaymentCommand.toXML()");
		}
		return strBuilder.toString();
	}

}
