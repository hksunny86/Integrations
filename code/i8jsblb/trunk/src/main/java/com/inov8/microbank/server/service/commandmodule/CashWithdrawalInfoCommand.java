/**
 *
 */
package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * @author Atieq Rehman
 *
 * <p>Cash Withdrawal Info Command</p>
 *
 */
public class CashWithdrawalInfoCommand extends BaseCommand {
	private AppUserModel appUserModel, customerAppUserModel;
	private ProductModel productModel;
	private CommissionAmountsHolder commissionAmountsHolder;
	private String customerMobileNo, agentMobileNo, withdrawalAmount;
	private String cnic;
	private Long appUserTypeId;
	private String productId;
	private RetailerContactModel retailerContactModel;
	private String paymentMode;
	private String isHRALinkedRequest;
	private String isOtpRequired;
	protected final Log logger = LogFactory.getLog(CashWithdrawalInfoCommand.class);
	/**
	 * prepare() function extracts information from baseWrapper and load models to initialize properties
	 */
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		String classNMethodName = "[CashWithdrwalInfoCommand.prepare] ";
		if(logger.isDebugEnabled()){
			logger.debug("Start of " + classNMethodName);
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		isOtpRequired = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_IS_OTP_REQUIRED);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		customerMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		cnic = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CNIC);
		agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
		withdrawalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		paymentMode = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE) == null || this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE) == ""
				? "BLB":"HRA";
		isHRALinkedRequest = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_HRA_LINKED_REQUEST);
		try{
			RetailerContactModel retContactModel = new RetailerContactModel();
			retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel(retContactModel);
			bWrapper = getCommonCommandManager().loadRetailerContact(bWrapper);
			retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
		}
		catch(Exception ex){
			logger.error(classNMethodName + "Customer App user/retailer contact model not found: " + ex.getStackTrace());
		}
		customerAppUserModel = new AppUserModel();
		customerAppUserModel.setMobileNo(customerMobileNo);
		try {
			customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(customerMobileNo);
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		try{
			BaseWrapper bWrapper = new BaseWrapperImpl();
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));
			bWrapper.setBasePersistableModel(productModel);
			bWrapper = getCommonCommandManager().loadProduct(bWrapper);
			productModel = (ProductModel)bWrapper.getBasePersistableModel();
		}
		catch(Exception ex){
			logger.error(classNMethodName +" Product model not found: " + ex.getStackTrace());
		}
		if(logger.isDebugEnabled()){
			logger.debug("End of " + classNMethodName);
		}
	}

	/**
	 * validate() validates required inputs and properties
	 */
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)
			throws CommandException {
		String classNMethodName = "[CashWithdrwalInfoCommand.validate] ";
		String inputParams = "Logged in AppUserID: " +
				ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Customer Mobile No:" + customerMobileNo;

		if(logger.isDebugEnabled()) {
			logger.debug("Start of "+ classNMethodName);
		}
		validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Customer Mobile No");
		validationErrors = ValidatorWrapper.doRequired(agentMobileNo,validationErrors,"Agent Mobile No");
		validationErrors = ValidatorWrapper.doRequired(withdrawalAmount,validationErrors,"Withdrawal Amount");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product Id");
		if(!validationErrors.hasValidationErrors()) {
			validationErrors = ValidatorWrapper.doNumeric(withdrawalAmount,validationErrors,"Withdrawal Amount");
		}
		if(customerMobileNo.equals(agentMobileNo)){
			logger.error("Agent & Customer mobile nos. cannot be same.");
			validationErrors = ValidatorWrapper.addError(validationErrors, "Agent and Customer mobile nos. cannot be same.");
		}
		try {
			ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(appUserModel);
			if(userValidation.hasValidationErrors()) {
				logger.error(classNMethodName + " User validation failed.");
				validationErrors = ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
			}
		} catch (FrameworkCheckedException e) {
			logger.error(classNMethodName + e.getMessage());
			validationErrors = ValidatorWrapper.addError(validationErrors, e.getMessage());
		}
		if(null == retailerContactModel) {
			logger.error("[CashWithdrawalInfoCommand.execute] Retailer Contact Model is null. " + inputParams);
			validationErrors = ValidatorWrapper.addError(validationErrors, "Retailer Contact not found.");
		}
		if(null== productModel) {
			logger.error(classNMethodName + "Product model not found: " + inputParams);
			validationErrors = ValidatorWrapper.addError(validationErrors, "Product not found.");
		}
		if(logger.isDebugEnabled()) {
			logger.debug("End of " + classNMethodName);
		}
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		String classNMethodName = "[CashWithdrwalInfoCommand.execute] ";
		String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Mobile No:" + customerMobileNo ;
		Long level1ErrorCode = null;
		if(cnic.equals("") && paymentMode.equals("HRA"))
		    cnic = customerAppUserModel.getNic();
		if(paymentMode.equals("HRA"))
		    level1ErrorCode = ErrorCodes.HRA_CW_LEVEL_1_NOT_FOUND;
		else
		    level1ErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
		if(null == customerAppUserModel) {
		    if(paymentMode.equals("BLB"))
		        level1ErrorCode = Long.valueOf(WorkFlowErrorCodeConstants.CUSTOMER_NOT_REGISTERED);
			logger.error(classNMethodName + "Customer App User not found: " + inputParams);
			throw new CommandException(this.getMessageSource().getMessage(WorkFlowErrorCodeConstants.CUSTOMER_NOT_REGISTERED, null,null),level1ErrorCode,ErrorLevel.MEDIUM,null);
		}else if(customerAppUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.CUSTOMER){
			logger.error("[CashDepositInfoCommand.execute] Invalid Customer Account. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			throw new CommandException(MessageUtil.getMessage("invalid.mobile"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		else if(customerAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
			CustomerModel customerModel = new CustomerModel();
			try {
				customerModel = commonCommandManager.getCustomerModelById(customerAppUserModel.getCustomerId());
			} catch (CommandException e) {
				e.printStackTrace();
			}

			if (customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
				logger.error("[CashDepositInfoCommand.execute] Upgrade Account L0 to L1 to perform Transaction.: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + customerMobileNo);
				throw new CommandException(MessageUtil.getMessage("upgrade.customer.L1.account"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

			}
		}

		else if(paymentMode.equals("HRA") && customerAppUserModel != null && customerAppUserModel.getNic() != null && !customerAppUserModel.getNic().equals(cnic))
		{
			throw new CommandException("Invalid CNIC",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		CommissionWrapper commissionWrapper;
		BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
		String exceptionMessage = "Exception occurred ";
		String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
		Boolean isBlackListed = getCommonCommandManager().isCnicBlacklisted(customerAppUserModel.getNic());
		if(isBlackListed)
			throw new CommandException("CNIC is BlackListed.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		if(paymentMode.equals("HRA") && (isHRALinkedRequest != null && isHRALinkedRequest.equals(""))) {
			try {
				String customerCity = null;
				I8SBSwitchControllerRequestVO requestVO = ESBAdapter.preparePayMTNCRequest(customerAppUserModel.getNic(),customerAppUserModel.getMobileNo(),null,
						customerAppUserModel.getFirstName(),customerAppUserModel.getLastName(),customerCity);
				SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
				i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
				i8sbSwitchWrapper = this.commonCommandManager.getEsbAdapter().makeI8SBCall(i8sbSwitchWrapper);
				I8SBSwitchControllerResponseVO responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
				ESBAdapter.processI8sbResponseCode(responseVO, false);

			}/*catch (WorkFlowException wfe){
		logger.error("Exception occured while parsing i8sb response");
	}*/catch (Exception e) {
				if (e instanceof NullPointerException
						|| e instanceof HibernateException
						|| e instanceof SQLException
						|| e instanceof DataAccessException
						|| (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

					logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
				} else {
					this.logger.error("[FonePaySwitchController.CashIn] Error occured: " + e.getMessage(), e);
				}

			}
		}
		try{
			//Added by Sheheryaar Nawaz
			ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(customerAppUserModel);
			if(userValidation.hasValidationErrors()) {
				logger.error(classNMethodName + " Customer validation failed");
				throw new CommandException(userValidation.getErrors(),Long.valueOf(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT),ErrorLevel.MEDIUM,new Throwable());
			}
			if(!paymentMode.equals("HRA")) {
				//Check User Device Accounts health
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
				baseWrapper.setBasePersistableModel(customerAppUserModel);
				userValidation = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

				if (userValidation.hasValidationErrors()) {
					logger.error(classNMethodName + " User Device Accounts failed");
					throw new CommandException(userValidation.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
				}
			}
			CustomerModel customerModel = loadCustomerSegment(workFlowWrapper, customerAppUserModel.getCustomerId(),paymentMode);
			if(paymentMode.equals("HRA"))
			{
				if(customerModel != null && customerModel.getCustomerAccountTypeId() != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0))
					throw new CommandException("Please Upgrade Level 0 to Level 1.",ErrorCodes.UPGRADE_LEVEL_0_TO_LEVEL_1,ErrorLevel.MEDIUM,null);
			}
			//FIXME need to finalize which parameters are required for checkProductLimit
			getCommonCommandManager().checkProductLimit(workFlowWrapper.getSegmentModel().getSegmentId(), productModel.getProductId(), appUserModel.getMobileNo(),
					Long.valueOf(deviceTypeId), Double.parseDouble(withdrawalAmount), productModel, null, workFlowWrapper.getHandlerModel());
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
			customerSMAModel.setCustomerId(customerAppUserModel.getCustomerId());
			customerSMAModel.setActive(Boolean.TRUE);
			if(paymentMode.equals("HRA"))
				customerSMAModel.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
			else
				customerSMAModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			searchBaseWrapper.setBasePersistableModel(customerSMAModel);
			searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
			String errorMessage = null;
			Long errorCode = null;
			if(paymentMode.equals("HRA"))
			{
				errorMessage = "HRA Account does not exist";
				errorCode = ErrorCodes.NO_HRA_EXISTS;
			}
			else
			{
				errorMessage = "BLB Account does not exist";
				errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
			}
			@SuppressWarnings("rawtypes")
			CustomList smaList = searchBaseWrapper.getCustomList();
			if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0)
				customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
			if(customerSMAModel.getSmartMoneyAccountId() == null)
				throw new CommandException(errorMessage,errorCode,ErrorLevel.MEDIUM,new Throwable());
			baseWrapperTemp.setBasePersistableModel(customerSMAModel);
			validationErrors = getCommonCommandManager().checkSmartMoneyAccount(baseWrapperTemp);
			if(validationErrors.hasValidationErrors()) {
				logger.error(genericExceptionMessage);
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			if(null == customerSMAModel.getName()) {
				logger.error(genericExceptionMessage);
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			if(!customerSMAModel.getCustomerId().toString().equals(customerAppUserModel.getCustomerId().toString())) {
				logger.error(classNMethodName + " Invalid Smart Money account."+inputParams + exceptionMessage);
				throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);
			TransactionModel transactionModel = new TransactionModel();
			// add to baseWrapperTemp so populate vo
			baseWrapperTemp.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
			baseWrapperTemp.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
			baseWrapperTemp.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo);
			baseWrapperTemp.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, agentMobileNo);
			baseWrapperTemp.putObject(CommandFieldConstants.KEY_TAMT, withdrawalAmount);
			Long pimInfoId = productModel.getProductIntgModuleInfoId();
			Long piVOId = productModel.getProductIntgVoId();
			if(pimInfoId == null || "".equals(pimInfoId) || piVOId == null || "".equals(piVOId)){
				logger.error(classNMethodName + " Unable to load Product VO. " + inputParams + exceptionMessage);
				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			ProductVO productVO = getCommonCommandManager().loadProductVO(baseWrapperTemp);
			if(productVO == null) {
				throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			productVO.populateVO(productVO, baseWrapperTemp);
			workFlowWrapper.setProductModel(productModel);
			workFlowWrapper.setProductVO(productVO);
			logger.info(classNMethodName + inputParams);
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_WITHDRAWAL_TX);
			workFlowWrapper.setProductModel(productModel);
			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setRetailerContactModel(retailerContactModel);
			workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
			workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));
			transactionModel.setTransactionAmount(Double.valueOf(withdrawalAmount));
			workFlowWrapper.setTransactionModel(transactionModel);
			workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());
			commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
			commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			if(paymentMode.equals("HRA") && (isHRALinkedRequest != null && (isHRALinkedRequest.equals("1" ) || isHRALinkedRequest.equals(""))))
				getCommonCommandManager().checkCustomerBalanceForHra(customerMobileNo, commissionAmountsHolder.getTotalAmount());
			else if(paymentMode.equals("BLB"))
				getCommonCommandManager().checkCustomerBalance(customerMobileNo, commissionAmountsHolder.getTotalAmount());


			commonCommandManager.validateBalance(customerAppUserModel, customerSMAModel, commissionAmountsHolder.getTotalAmount(), true);
		}
		catch(CommandException e) {
			logger.error(genericExceptionMessage + e.getMessage());
			throw e;
		}
		catch(WorkFlowException we) {
			logger.error(genericExceptionMessage + we.getMessage());
			throw new CommandException(we.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,we);
		}
		catch(Exception ex) {
			logger.error(genericExceptionMessage + ex.getMessage());
			if(ex.getMessage() != null && ex.getMessage().indexOf("JTA") != -1) {
				throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			else{
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
//		if (!deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()))
		if(deviceTypeId.equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString()) || (isOtpRequired !=null && isOtpRequired.equals("1")) || paymentMode.equals("HRA"))
		{
			String otp = CommonUtils.generateOneTimePin(4);
			String encryptedPin = EncoderUtils.encodeToSha(otp);
			String productName = "HRA Cash Withdrawal";
			if(isOtpRequired !=null && isOtpRequired.equals("1"))
			    productName = "Cash Out";
			try {
				commonCommandManager.getFonePayManager().createMiniTransactionModel(encryptedPin, this.customerMobileNo,"",CommandFieldConstants.CMD_CASH_OUT_INFO);
				//Send OTP via SMS to Customer
				String smsText = MessageUtil.getMessage("otpSms", new String[]{productName, otp});
				SmsMessage smsMessage = new SmsMessage(this.customerMobileNo, smsText);
				logger.info("CNIC : "+ this.customerAppUserModel.getNic() +"Mobile # : "+ this.customerMobileNo+" : " +smsText);
				commonCommandManager.getSmsSender().send(smsMessage);
			} catch (Exception e) {
				logger.error("Error While generating OTP in CashWithDrawalInfoCommand.execute() :: " + e.getMessage() + " :: " + e);
				throw new CommandException("Could not Generate OTP for Cash Withdrawal",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
			}
		}
	}

	@Override
	public String response() {

		StringBuilder responseXML = new StringBuilder();
		responseXML.append(TAG_SYMBOL_OPEN)
				.append(TAG_PARAMS)
				.append(TAG_SYMBOL_CLOSE);

		responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
		responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_AGENT_MOBILE, agentMobileNo));

		if(customerAppUserModel != null){
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_NAME, customerAppUserModel.getFirstName() +" "+ customerAppUserModel.getLastName()));
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, customerAppUserModel.getNic()));
		}

		if(commissionAmountsHolder != null) {
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TXAM, replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()).toString()));
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TXAMF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()))));
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMT, replaceNullWithZero(commissionAmountsHolder.getTotalAmount()).toString()));
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTotalAmount()))));
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()).toString()));
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()))));
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAM, replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()).toString()));
			responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()))));
		}
		responseXML.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		return responseXML.toString();
	}

	private CustomerModel loadCustomerSegment(WorkFlowWrapper workFlowWrapper, Long customerId,String paymentMode) throws Exception{
		CustomerModel custModel = new CustomerModel();
		custModel.setCustomerId(customerId);
		BaseWrapper bWrapper = new BaseWrapperImpl();
		bWrapper.setBasePersistableModel(custModel);
		bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
		if(null != bWrapper.getBasePersistableModel()) {
			custModel = (CustomerModel) bWrapper.getBasePersistableModel();
			workFlowWrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
		}
		return custModel;
	}

}
