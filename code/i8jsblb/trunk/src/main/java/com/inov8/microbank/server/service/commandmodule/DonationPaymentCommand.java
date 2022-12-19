package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.DonationPaymentVO;
import com.inov8.verifly.common.model.AccountInfoModel;

public class DonationPaymentCommand extends BaseCommand {
	
	private String accountId = null;
	private String commissionAmount = null;
	private String consumerNumber = null;
	private String billAmount = null;
	private String productId = null;
	private String mobileNumber = null;
	private String bankPIN = null;
	private String deviceTypeId = null;
	private String bankId = null;
	private Double balance = 0D;
	private Double discountAmount = 0d;
	private Long reasonId = null;
	
	private TransactionModel transactionModel = null;
	private ProductModel productModel = null;
	private AppUserModel appUserModel = null;
	private BaseWrapper baseWrapper = null;
	private SmartMoneyAccountModel smartMoneyAccountModel = null;
	private UserDeviceAccountsModel userDeviceAccountsModel = null; 
	private WorkFlowWrapper workFlowWrapper = null;
	private SegmentModel segmentModel = null;
	
	private DateTimeFormatter dtf = null;
	private DateTimeFormatter tf = null;
	private String successMessage = null;
	
	protected final Log logger = LogFactory.getLog(DonationPaymentCommand.class);
	
	public DonationPaymentCommand() {

		this.dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
		this.tf = DateTimeFormat.forPattern("h:mm a");
	}
	
	
	@Override
	public void execute() throws CommandException {
		
		

		logger.info(":- Started Donation Payment Command -:");
		
		ValidationErrors validationErrors = null;
		
		try {
		
			validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
		
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		if(validationErrors.hasValidationErrors()) {
			
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, new Throwable());
		}
		
		try {
		
			getCommonCommandManager().checkProductLimit(null, Long.valueOf(productId), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.parseDouble(billAmount), null, null, handlerModel);
		
		}  catch (Exception e) {
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		if(appUserModel.getRetailerContactId() != null && 
				appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER)	{

			logger.debug("executeAgentWorkFlow()");
			executeAgentWorkFlow();
			
		} else if(appUserModel != null && 
				appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER) {

			logger.debug("executeCustomerWorkFlow()");
			executeCustomerWorkFlow();
		}
	}



	/**
	 * @throws CommandException
	 */
	public void executeAgentWorkFlow() throws CommandException {
		
		try {
			
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));
				
//			DonationPaymentVO donationPaymentVO = (DonationPaymentVO) getCommonCommandManager().loadProductVO(baseWrapper);
			baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNumber);
			BillPaymentVO donationPaymentVO = (BillPaymentVO)getCommonCommandManager().loadProductVO(baseWrapper);	
			if(donationPaymentVO == null) {
				
				throw new CommandException(this.getMessageSource().getMessage("DonationPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setOldPin(bankPIN);
			
			AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
			
			BankModel bankModel = new BankModel();
			bankModel.setBankId(Long.valueOf(bankId));
			
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DONATION_PAYMENT_TX);
									
			workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setHandlerModel(handlerModel);
			workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
				
			workFlowWrapper.setProductModel(productModel);
//			workFlowWrapper.setCustomerAppUserModel(appUserModel);
			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
			workFlowWrapper.setAccountInfoModel(accountInfoModel);
			workFlowWrapper.setProductVO(donationPaymentVO);
			workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
			workFlowWrapper.setTotalAmount(Double.parseDouble(billAmount));
			workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
			workFlowWrapper.setAppUserModel(appUserModel);
			workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
			workFlowWrapper.setWalkInCustomerMob(mobileNumber);
			workFlowWrapper.setBankModel(bankModel);
			
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CSCD, this.consumerNumber);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
				
			workFlowWrapper.setTxProcessingAmount(Double.parseDouble("0"));
			workFlowWrapper.setTotalAmount(Double.parseDouble("0"));		
			workFlowWrapper.putObject("reasonId", reasonId);

			WorkFlowWrapper _workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);

			SmartMoneyAccountModel _smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
			transactionModel = workFlowWrapper.getTransactionModel();	
			productModel = workFlowWrapper.getProductModel();
			userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
			discountAmount = workFlowWrapper.getDiscountAmount();
			successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();

			balance = workFlowWrapper.getSwitchWrapper().getBalance();
			
			consumerNumber = ((BillPaymentVO)workFlowWrapper.getProductVO()).getConsumerNo();
			
		} catch (WorkFlowException | FrameworkCheckedException ex) {

			logger.error(ex);
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			
		} catch (Exception ex) {

			logger.error(ex);
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
	}
	
	/**
	 * @throws CommandException
	 */
	public void executeCustomerWorkFlow() throws CommandException {
		
		try {
			
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));
				
			DonationPaymentVO donationPaymentVO = (DonationPaymentVO) getCommonCommandManager().loadProductVO(baseWrapper);
				
			if(donationPaymentVO == null) {
				
				throw new CommandException(this.getMessageSource().getMessage("DonationPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setOldPin(bankPIN);
			
			AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
			
			CustomerModel customerModel = new CustomerModel();
			customerModel.setCustomerId(appUserModel.getCustomerId());
			
			BankModel bankModel = new BankModel();
			bankModel.setBankId(Long.valueOf(bankId));
			
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DONATION_PAYMENT_TX);
									
			workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setHandlerModel(handlerModel);
			workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
				
			workFlowWrapper.setProductModel(productModel);
			workFlowWrapper.setCustomerAppUserModel(appUserModel);
			workFlowWrapper.setCustomerModel(customerModel);
			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
			workFlowWrapper.setAccountInfoModel(accountInfoModel);
			workFlowWrapper.setProductVO(donationPaymentVO);
			workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
			workFlowWrapper.setTotalAmount(Double.parseDouble(billAmount));
			workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
			workFlowWrapper.setAppUserModel(appUserModel);
			workFlowWrapper.setBankModel(bankModel);
			
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CSCD, this.consumerNumber);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
				
			workFlowWrapper.setTxProcessingAmount(Double.parseDouble("0"));
			workFlowWrapper.setTotalAmount(Double.parseDouble("0"));		
			workFlowWrapper.putObject("reasonId", reasonId);

			WorkFlowWrapper _workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);

			transactionModel = workFlowWrapper.getTransactionModel();	
			productModel = workFlowWrapper.getProductModel();
			userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
			discountAmount = workFlowWrapper.getDiscountAmount();
			successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
		
			balance = workFlowWrapper.getSwitchWrapper().getBalance();
		
			consumerNumber = ((DonationPaymentVO) workFlowWrapper.getProductVO()).getConsumerNo();
			
		} catch (WorkFlowException | FrameworkCheckedException ex) {

			logger.error(ex);
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			
		} catch (Exception ex) {

			logger.error(ex);
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}		
	}
	
	
	/**
	 * @param _smartMoneyAccountModel
	 * @return
	 */
	private SmartMoneyAccountModel getSmartMoneyAccountModel(Long criteria) throws CommandException {

		SmartMoneyAccountModel _smartMoneyAccountModel = null;
		
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		smartMoneyAccountModel.setDeleted(Boolean.FALSE);
	    smartMoneyAccountModel.setActive(Boolean.TRUE);
	    smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		
		if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
		
			smartMoneyAccountModel.setRetailerContactId(criteria);
		
		} else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
			
			smartMoneyAccountModel.setCustomerId(criteria);
		}
	    
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(smartMoneyAccountModel);
		
		try {
			
			SearchBaseWrapper searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(wrapper);
			
			if(searchBaseWrapper != null) {
				
				List<SmartMoneyAccountModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();
				
				if(resultsetList != null && !resultsetList.isEmpty()) {
					
					_smartMoneyAccountModel = resultsetList.get(0);
				}
			}
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		logger.debug("Found Smart Money BB Account "+ _smartMoneyAccountModel.getName());
		
		return _smartMoneyAccountModel;
	}
			
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUST_CODE);
		
		mobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		bankPIN = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		bankPIN = StringUtil.replaceSpacesWithPlus(bankPIN);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAMT);
		billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
	
    	if(UserTypeConstantsInterface.CUSTOMER.longValue() == appUserModel.getAppUserTypeId().longValue()) {
    		
    		CustomerModel customerModel = new CustomerModel();
    		customerModel.setCustomerId(appUserModel.getCustomerId());
    		
        	BaseWrapper _baseWrapper = new BaseWrapperImpl();
    		_baseWrapper.setBasePersistableModel(customerModel);
    		
    		try {
    		
    			_baseWrapper = getCommonCommandManager().loadCustomer(_baseWrapper);
    			customerModel = (CustomerModel) _baseWrapper.getBasePersistableModel();	
    			segmentModel = customerModel.getSegmentIdSegmentModel();
    			
    		} catch (FrameworkCheckedException e) {
    		
    			logger.error(e);
    		}	
    	}    	

		reasonId = getReasonType(Long.valueOf(productId), Double.valueOf(billAmount), segmentModel);
		
		if(StringUtil.isNullOrEmpty(commissionAmount)) {
			ProductModel productModel = new ProductModel();
			productModel.setPrimaryKey(Long.valueOf(productId));
			Double _commissionAmount = getCommonCommandManager().getCommissionRate(productModel);
			commissionAmount = _commissionAmount.toString();		
		}
		
		try {
			
			SmartMoneyAccountModel _smartMoneyAccountModel = null;
			
			if(!StringUtil.isNullOrEmpty(bankId)) {
								
				if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
				
					_smartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel.getCustomerId());
										
				} else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
					
					_smartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel.getRetailerContactId());
				}
				
				ValidationErrors validationErrors = checkSmartMoneyAccount(_smartMoneyAccountModel);
				
				if(!validationErrors.hasValidationErrors()) {
					
					smartMoneyAccountModel = _smartMoneyAccountModel;
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
					
				} else {
					
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			
		} catch(Exception ex) {
			logger.error(ex);
			if(logger.isErrorEnabled()) {
				logger.error("DonationPaymentCommand.prepare] Exception occured for ProductID: " + productId + " AppUserID: " + appUserModel.getAppUserId() + " Exception Details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
		}	
	}
	
	
	/**
	 * @param _smartMoneyAccountModel
	 * @return
	 * @throws FrameworkCheckedException
	 */
	private ValidationErrors checkSmartMoneyAccount(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException {

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setCustomList(new CustomList(new ArrayList<>(0)));
		searchBaseWrapper.getCustomList().getResultsetList().add(_smartMoneyAccountModel);
		searchBaseWrapper.setBasePersistableModel(_smartMoneyAccountModel);
									
		return getCommonCommandManager().checkSmartMoneyAccount(searchBaseWrapper);
	}

	@Override
	public String response() {

		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
		validationErrors = ValidatorWrapper.doRequired(billAmount,validationErrors,"Bill Amount");
				
		if(!validationErrors.hasValidationErrors()) {
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		
		return validationErrors;
	}
	
	private String toXML() {		
		
		String productName = productModel.getName();
		String transactionAmount = Formatter.formatNumbers(transactionModel.getTransactionAmount());
		String transactionCode = replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
		String transactionCharges = "00.0";
		String dateTime = dtf.print(new DateTime()) +" "+ tf.print(new LocalTime());
		String balance = Formatter.formatNumbers(workFlowWrapper.getSwitchWrapper().getBalance());
		
		if(reasonId != null && reasonId.longValue() == 5) {
			transactionCharges = Formatter.formatNumbers(transactionModel.getTotalCommissionAmount());
		}
		
		StringBuilder response = new StringBuilder();

		if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue() &&
				
			appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
			
			Object[] arguments = new Object[] {productName, mobileNumber, transactionAmount, transactionCharges, transactionCode, dateTime, balance};

			response.append(this.getMessageSource().getMessage("USSD.RETAILER.DonationPaymentNotification", arguments, null));
			
		}  else if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue() &&
				
			appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

			Object[] arguments = new Object[] {productName, transactionAmount, transactionCharges, transactionCode, dateTime, balance};

			response.append(this.getMessageSource().getMessage("USSD.CUSTOMER.DonationPaymentNotification", arguments, null));
			
		} else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue() &&
				
			appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

			Object[] arguments = new Object[] {productName, transactionAmount, transactionCharges, transactionCode, dateTime, balance};

			response.append(createParameterTag(CommandFieldConstants.KEY_PROD_ID, productName));
			response.append(createParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, transactionAmount));
			response.append(createParameterTag(CommandFieldConstants.KEY_TX_CODE, transactionCode));
			response.append(createParameterTag(CommandFieldConstants.KEY_TX_DATE, dateTime));
			response.append(createParameterTag(CommandFieldConstants.KEY_FORMATED_BAL, balance));
			
			if(reasonId != null && reasonId.longValue() == 5) {
				
				response.append(createParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, transactionCharges));	
			}
		}  
		
		
		return response.toString();
	}
	
	public String toString() {
		
		return org.apache.commons.lang.builder.ReflectionToStringBuilder.toString(this);
	}
	
	private String createParameterTag(String name, String value) {
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		
		.append(name)
		
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		
		.append(value)
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE);		
		
		return strBuilder.toString();
	}


	/**
	 * @param productModel
	 * @return
	 * @throws FrameworkCheckedException
	 */
	private Long getReasonType(Long productId, Double billAmount, SegmentModel segmentModel) {
		
		Long reasonId = -1L;
		
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionAmount(billAmount);
		
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setProductModel(new ProductModel(productId));
		workFlowWrapper.setTransactionModel(transactionModel);
		workFlowWrapper.setSegmentModel(segmentModel);

		Double commissionRate = super.getCommonCommandManager().getCommissionAmount(workFlowWrapper);	
		reasonId = (Long) workFlowWrapper.getObject("REASON_ID");
		
		return reasonId;
	}
	
}