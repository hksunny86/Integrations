package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.StringUtil.isNullOrEmpty;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
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
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.CreditCardPaymentVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;



public class AgentCreditCardPaymentCommand extends BaseCommand 
{

	protected final Log logger = LogFactory.getLog(AgentCreditCardPaymentCommand.class);

	private AppUserModel appUserModel;
	private String productId;
	private String accountId;
	private String mobileNo;
	private String txProcessingAmount;
	private String pin;
	private String deviceTypeId;
	private String commissionAmount;
	private String totalAmount;
	private String billAmount;
	private String cvv;
	private String tPin;
	protected String creditCardNo;
	protected String txAmount;

	private String accountType;
	private String accountCurrency;
	private String accountStatus;
	private String accountNumber;
	private Double discountAmount = 0D;
	private Long transactionTypeId = null;
	
	private TransactionModel transactionModel;
	private ProductModel productModel;
	private SmartMoneyAccountModel smartMoneyAccountModel;
	private BaseWrapper baseWrapper;
	private WorkFlowWrapper workFlowWrapper;
	private String walkInCustomerCNIC;
	private String walkInCustomerMobileNumber;
	private UserDeviceAccountsModel userDeviceAccountsModel; 
	private String successMessage;
	private String ussdPin;
	private Double balance = 0D;
	private String consumerNo;
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");

	
	@Override
	public void execute() throws CommandException {

		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
					
		try {
				
			ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
			if(validationErrors.hasValidationErrors()) {
				logger.error("[AgentCreditCardPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo);
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
				
			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setOldPin(pin);
								
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));

			commonCommandManager.checkProductLimit(null, Long.parseLong(productId), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.parseDouble(totalAmount), null, null, workFlowWrapper.getHandlerModel());					

			ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
			if(productVo == null) {
				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
					
			transactionTypeId = TransactionTypeConstantsInterface.AGENT_CREDIT_CARD_PAYMENT_TX;
			
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(transactionTypeId);
			
			logger.info(smartMoneyAccountModel.getPaymentModeId()+" - "+smartMoneyAccountModel.getPrimaryKey());	
			
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
				
			AppUserModel customerAppUserModel = new AppUserModel();
			customerAppUserModel.setMobileNo(mobileNo);
//			customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
				
			CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
				
			workFlowWrapper.setCustomerAccount(customerAccount);
			workFlowWrapper.setProductModel(productModel);
			workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
			workFlowWrapper.setAccountInfoModel(accountInfoModel);
			workFlowWrapper.setProductVO(productVo);
			workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
			workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
			workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
			workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
			workFlowWrapper.setAppUserModel(appUserModel);
			workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
			workFlowWrapper.setCcCVV(this.cvv);
			workFlowWrapper.setMPin(this.tPin);
			workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					
			if(!isNullOrEmpty(walkInCustomerCNIC)) {
				workFlowWrapper.setWalkInCustomerCNIC(walkInCustomerCNIC);
			}
						
			if(!isNullOrEmpty(walkInCustomerMobileNumber)) {
				workFlowWrapper.setWalkInCustomerMob(walkInCustomerMobileNumber);
			}
					
			logger.info("[AgentCreditCardPaymentCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + 
						" Customer Mobile No:" + mobileNo + " WalkInCustomerMobileNumber:" + walkInCustomerMobileNumber);

			workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
			transactionModel = workFlowWrapper.getTransactionModel();
			smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
			productModel = workFlowWrapper.getProductModel();
			userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
			discountAmount = workFlowWrapper.getDiscountAmount();
			successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					
//			if(UtilityCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {
//						
//				balance = workFlowWrapper.getSwitchWrapper().getBalance();
//						
//				if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {
//							
//					balance = workFlowWrapper.getSwitchWrapper().getAgentBalance();
//				}						
//						
//				consumerNo = ((UtilityBillVO) workFlowWrapper.getProductVO()).getConsumerNo();
//					
//			} else if(InternetCompanyEnum.contains(String.valueOf(productModel.getProductId()))){
//						
//				if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {
//							
//					balance = workFlowWrapper.getSwitchWrapper().getAgentBalance();
//						
//				} else if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {
//						
//					balance = workFlowWrapper.getSwitchWrapper().getBalance();
//				}  
//						
//				consumerNo = ((UtilityBillVO)workFlowWrapper.getProductVO()).getConsumerNo();
//			}
			
				
		} catch(FrameworkCheckedException ex) {
				
			if(logger.isErrorEnabled()) {
				logger.error("[AgentCreditCardPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
					
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
			
		catch(WorkFlowException wex) {
						
			if(logger.isErrorEnabled()) {
				logger.error("[AgentCreditCardPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
			}
						
			throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
				
		} catch(Exception ex) {
					
			ex.printStackTrace();
					
			if(logger.isErrorEnabled()) {
				logger.error("[AgentCreditCardPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}

		
		if(logger.isDebugEnabled()) {
			logger.debug("End of AgentCreditCardPaymentCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AgentCreditCardPaymentCommand.prepare()");
		}
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		
		/**
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
		
		try {
		
			if(appUserModel == null || appUserModel.getRetailerContactId() == null) {

				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
			if(!isNullOrEmpty(accountId) && !isNullOrEmpty(bankId))
			{
				CommonCommandManager commonCommandManager = this.getCommonCommandManager();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
				smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
				smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				smartMoneyAccountModel.setActive(Boolean.TRUE);
				smartMoneyAccountModel.setDeleted(Boolean.FALSE);
				
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
				smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
			
				ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
				
				if(!validationErrors.hasValidationErrors()) {
				
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
					this.smartMoneyAccountModel = smartMoneyAccountModel;
				
				} else {
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			
			
		}
		catch(Exception ex)
		{
			if(logger.isErrorEnabled())
			{
				logger.error("AllPayBillInfoCommand.prepare] Exception occured for ProductID: " + productId + " AppUserID: " + appUserModel.getAppUserId() + " Exception Details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
//			ex.printStackTrace();
		}

		/**
		 * ------------------------End of Change------------------------------
		 */

		
		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		creditCardNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CREDIT_CARD_NO);

		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		walkInCustomerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		
		txProcessingAmount = StringUtil.isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT))?"0.0":this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		commissionAmount = StringUtil.isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT))?"0.0":this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		
		totalAmount = txAmount;
		billAmount = txAmount;

		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
//		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
//		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
//		billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
		
//		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
//		cvv = this.decryptPin(cvv);
//		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
//		tPin = StringUtil.replaceSpacesWithPlus(tPin);
//		tPin = this.decryptPin(tPin);
		
		//Maqsood Shahzad for USSD cash deposit.
		
//		ussdPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
//		walkInCustomerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
//		walkInCustomerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE);
		//End changes for USSD cash deposit
		
		
		logger.info("[AgentCreditCardPaymentCommand.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Walkin Customer Mobile No:" + walkInCustomerMobileNumber);

		if(!isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)) && 
				!isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)))
		{
			discountAmount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT));
		}
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AgentCreditCardPaymentCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of AgentCreditCardPaymentCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AgentCreditCardPaymentCommand.validate()");
		}
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(creditCardNo,validationErrors,"Credit Card No");
		validationErrors = ValidatorWrapper.doRequired(mobileNo,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Tx Amount");

//		validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Amount");
//		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
//		validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
//		validationErrors = ValidatorWrapper.doRequired(billAmount,validationErrors,"Bill Amount");
				
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");	
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");
			validationErrors = ValidatorWrapper.doInteger(creditCardNo,validationErrors,"Credit Card No");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AgentCreditCardPaymentCommand.validate()");
		}
		
		if(smartMoneyAccountModel == null) {
			
			throw new CommandException("Branchless Banking Account is not defined" ,ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, new Throwable());

		}
		
		if(appUserModel == null || appUserModel.getRetailerContactId() == null) {

			throw new CommandException("Missing Retailer Contact Identity",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

		}
		
		return validationErrors;
	}
	
	private String toXML()
	{
		
		CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();
//		String notification = null;
//		String date=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
//		String time=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
//		String agentBalance = Formatter.formatDouble(workFlowWrapper.getSwitchWrapper().getAgentBalance());
//		Double transactionAmount = transactionModel.getTransactionAmount();		
//		String transactionCode = transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();
		
		Date billDueDate = ((CreditCardPaymentVO) workFlowWrapper.getProductVO()).getDueDate();
		Double billAmount = ((CreditCardPaymentVO) workFlowWrapper.getProductVO()).getDueAmount();
		Double minimumDueAmount = ((CreditCardPaymentVO) workFlowWrapper.getProductVO()).getMinimumDueAmount();
		Double osDueAmount = ((CreditCardPaymentVO) workFlowWrapper.getProductVO()).getOutstandingDueAmount();
		
		StringBuilder strBuilder = new StringBuilder();
		
		if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {		
			
			String KEY_TX_CODE = "";
		
			if(workFlowWrapper.getTransactionCodeModel() != null) {
				KEY_TX_CODE = workFlowWrapper.getTransactionCodeModel().getCode();
			}
		
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, this.productId));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CREDIT_CARD_NO, this.creditCardNo));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_DUE_DATE, dtf.print(new Date().getTime())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_AMOUNT, replaceNullWithZero(billAmount).toString()));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_MINIMUM_AMOUNT_DUE, replaceNullWithZero(minimumDueAmount).toString()));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CREDIT_CARD_OUTSTANDING_BAL, replaceNullWithZero(osDueAmount).toString()));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_CODE, KEY_TX_CODE));
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		
		} else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {
			
		}
		
		return strBuilder.toString();
	}

}
