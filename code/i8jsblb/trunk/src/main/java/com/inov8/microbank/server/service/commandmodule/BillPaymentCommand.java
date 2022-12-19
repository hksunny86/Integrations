package com.inov8.microbank.server.service.commandmodule;

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
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.InternetCompanyEnum;
import com.inov8.microbank.common.util.NadraCompanyEnum;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.P2PVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;



public class BillPaymentCommand extends BaseCommand 
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
	
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	protected double discountAmount = 0d;
	private Long transactionTypeId = null;
	
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel smartMoneyAccountModel;
	UserDeviceAccountsModel userDeviceAccountsModel;
	
	private String customer2Name=null;;
	private String customer2MSISN=null;
	private double customer1Balance;
	private double balance;
	private String consumerNo;
	private Boolean isInclusiveCharges;
	
	protected final Log logger = LogFactory.getLog(BillPaymentCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BillPaymentCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		if(appUserModel.getCustomerId() != null || appUserModel.getRetailerContactId() != null) //retailer condition added by Maqsood - for USSD CW flow
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
					
					if(productVo == null) {
						throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}

					productModel = (ProductModel) baseWrapper.getBasePersistableModel();					
						
					AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel.setMobileNo(mobileNo);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					
					if(InternetCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {
						
						transactionTypeId = TransactionTypeConstantsInterface.CUSTOMER_INTERNET_BILL_SALE_TX;
							
					} else if(NadraCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {

						transactionTypeId = TransactionTypeConstantsInterface.CUSTOMER_NADRA_BILL_SALE_TX;
						
					} else if (UtilityCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {
							
						transactionTypeId = TransactionTypeConstantsInterface.BILL_SALE_TX;
					}

					transactionTypeModel.setTransactionTypeId(transactionTypeId);
					
					commonCommandManager.checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.parseDouble(totalAmount), productModel, null, workFlowWrapper.getHandlerModel());
//					
//					smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
			
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
					workFlowWrapper.setCustomerAccount(customerAccount);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
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
					workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
					workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
					
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					
					logger.info("[BillPaymentCommand.execute] Going to execute Transaction flow. Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + 
							" Customer Mobile No:" + mobileNo + " ConsumerNo:" + ((BillPaymentVO)productVo).getConsumerNo());

					
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

					isInclusiveCharges = workFlowWrapper.getCommissionAmountsHolder().getIsInclusiveCharges();
					
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					discountAmount = workFlowWrapper.getDiscountAmount();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();					
					balance=workFlowWrapper.getSwitchWrapper().getAgentBalance();
					consumerNo=((UtilityBillVO)workFlowWrapper.getProductVO()).getConsumerNo();
					
					if(UtilityCompanyEnum.contains(String.valueOf(productModel.getProductId()))){
						balance=workFlowWrapper.getSwitchWrapper().getBalance();
						consumerNo=((UtilityBillVO)workFlowWrapper.getProductVO()).getConsumerNo();
					}else if(InternetCompanyEnum.contains(String.valueOf(productModel.getProductId()))){
						balance=workFlowWrapper.getSwitchWrapper().getBalance();
						consumerNo=((UtilityBillVO)workFlowWrapper.getProductVO()).getConsumerNo();
					}
					else if(productModel.getProductId().longValue() == 50000){
						customer2Name= ((P2PVO)workFlowWrapper.getProductVO()).getCustomerName();
						customer2MSISN= ((P2PVO)workFlowWrapper.getProductVO()).getMobileNo();
						if(workFlowWrapper.getSwitchWrapper()!=null){
							customer1Balance=workFlowWrapper.getSwitchWrapper().getBalance();
						}
					}
				}
				else
				{
					logger.error("[BillPaymentCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo);
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[BillPaymentCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[BillPaymentCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[BillPaymentCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.error("[BillPaymentCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo);
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of BillPaymentCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BillPaymentCommand.prepare()");
		}
		this.baseWrapper = baseWrapper;
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
		accountId = null;
		try{
		
			if(StringUtil.isNullOrEmpty(accountId) && !StringUtil.isNullOrEmpty(bankId)) {
				
				smartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel.getCustomerId());
				
				accountId = this.smartMoneyAccountModel.getSmartMoneyAccountId().toString();
			
			} else {
				throw new CommandException("AccountId is null and BankId is also null",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
		} catch(Exception ex) {

			if(logger.isErrorEnabled()) {
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
		
		logger.info("[BillPaymentCommand.prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + 
    			" Product ID:" + productId + " AccountId: " + accountId + 
    			" deviceTypeId: " + deviceTypeId + 
    			(productId.equals("50000") ? " Receiver" : "") + " MobileNumber: " + mobileNo);
    
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of BillPaymentCommand.prepare()");
		}
	}	
	/**
	 * @param _smartMoneyAccountModel
	 * @return
	 */
	private SmartMoneyAccountModel getSmartMoneyAccountModel(Long customerId) throws CommandException {

		SmartMoneyAccountModel _smartMoneyAccountModel = null;
		
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		smartMoneyAccountModel.setCustomerId(customerId);
	    smartMoneyAccountModel.setActive(Boolean.TRUE);	
		smartMoneyAccountModel.setDeleted(Boolean.FALSE);
	    smartMoneyAccountModel.setChangePinRequired(Boolean.FALSE);
//		smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
	    
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(smartMoneyAccountModel);
		
		try {
			
			SearchBaseWrapper searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(wrapper);
			
			if(searchBaseWrapper != null) {
				
				List<SmartMoneyAccountModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();
				
				if(resultsetList != null && !resultsetList.isEmpty()) {
					
					_smartMoneyAccountModel = resultsetList.get(0);
					
					logger.debug("Found Smart Money Account "+ _smartMoneyAccountModel.getName());
				}
			}
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return _smartMoneyAccountModel;
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of BillPaymentCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BillPaymentCommand.validate()");
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
			logger.debug("End of BillPaymentCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BillPaymentCommand.toXML()");
		}		
		
		String amount = Formatter.formatDouble(transactionModel.getTotalAmount());
		
		if(isInclusiveCharges) {
			amount = Formatter.formatDouble(transactionModel.getTransactionAmount());
		}
		
		StringBuilder strBuilder = new StringBuilder();
		if(UtilityCompanyEnum.contains(String.valueOf(productModel.getProductId()))){
			if(appUserModel!=null && appUserModel.getAppUserTypeId().longValue() == 2L){
				String date=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
				String time=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
				String companyName = UtilityCompanyEnum.lookup(String.valueOf(productModel.getProductId())).name();
				companyName = companyName.replace("_BILL", "");
				strBuilder.append(this.getMessageSource().getMessage(
						"USSD.CustomerBillPaymentNotification",
						new Object[] { 
								companyName,
								consumerNo,
								amount,
								transactionModel.getTransactionCodeIdTransactionCodeModel().getCode(),
								date,
								time,
								balance
								}, null));
			}
			
		} else if(InternetCompanyEnum.contains(String.valueOf(productModel.getProductId()))){
			if(appUserModel!=null && appUserModel.getAppUserTypeId().longValue() == 2L){
				String date=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
				String time=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
				String companyName = InternetCompanyEnum.lookup(String.valueOf(productModel.getProductId())).name();
				companyName = companyName.replace("_BILL", "");
				companyName = companyName.replace("_", " ");
				strBuilder.append(this.getMessageSource().getMessage(
						"USSD.CustomerBillPaymentNotification",
						new Object[] { 
								companyName,
								consumerNo,
								amount,
								transactionModel.getTransactionCodeIdTransactionCodeModel().getCode(),
								date,
								time,
								balance
								}, null));
			}
			
		} else {
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
					.append(amount)
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(amount)
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
					logger.debug("End of BillPaymentCommand.toXML()");
				}
		}
		return strBuilder.toString();
	}

}
