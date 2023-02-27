/**
 * 
 */
package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.integration.vo.CashWithdrawalVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Atieq Rehman
 * 
 * <p>Cash Withdrawal Command</p>
 *
 */
public class CashWithdrawalCommand extends BaseCommand {

	private AppUserModel appUserModel, customerAppUserModel;
	private BaseWrapper baseWrapper;
	
	private String customerMobileNo, agentMobileNo, withdrawalAmount;
	private String productId, encryptionType, CNIC, agentPIN;
	private String commissionAmount, processingAmount, totalAmount;
	private Boolean isIvrResponse;
	private String transactionId;
	private RetailerContactModel retailerContactModel;
	private ProductModel productModel;
	private CustomerModel customerModel;
	private Double customerBalance;
	private boolean isUSSDCashWirhdrawal;
	private String appId;
	private String isOtpRequired;
	private String otp;
	private String terminalId;
	private String channelId;
	private String thirdPartyTransactionId;
	private TransactionModel transactionModel;
	private String stan;
	
	protected final Log logger = LogFactory.getLog(CashWithdrawalCommand.class);
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		String classNMethodName = "[CashWithdrawalCommand.prepare] ";
		if(logger.isDebugEnabled()){
			logger.debug("Start of " + classNMethodName);
		}

		this.baseWrapper = baseWrapper;  
		appUserModel = ThreadLocalAppUser.getAppUserModel();

		otp = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_ONE_TIME_PIN);
		appId = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_APP_ID);
		isOtpRequired = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_IS_OTP_REQUIRED);
		deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
	    
		customerMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        withdrawalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);

        encryptionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        CNIC= getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        agentPIN = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
    	commissionAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAMT);
    	processingAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
    	totalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
		channelId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID);
		terminalId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TERMINAL_ID);
		thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
		stan=getCommandParameter(baseWrapper,CommandFieldConstants.KEY_STAN);


		isIvrResponse = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE) == null 
				? false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE));

		isUSSDCashWirhdrawal= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_USSD) == null
				? false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_USSD));
		transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
		
		try{
        	BaseWrapper bWrapper = new BaseWrapperImpl();
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));
			bWrapper.setBasePersistableModel(productModel);
			bWrapper = getCommonCommandManager().loadProduct(bWrapper);
			productModel = (ProductModel)bWrapper.getBasePersistableModel();
        }
        catch(Exception ex){
        	logger.error(classNMethodName +" Product model not found: " + ex.getStackTrace().toString());
        }
		
        try{
        	RetailerContactModel retContactModel = new RetailerContactModel();
        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
        	
        	BaseWrapper bWrapper = new BaseWrapperImpl();
        	bWrapper.setBasePersistableModel(retContactModel);
        	CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        	bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

        	retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
        	
        	customerAppUserModel = new AppUserModel();
			customerAppUserModel.setMobileNo(customerMobileNo);
			customerAppUserModel = getCommonCommandManager().loadAppUserByQuery(customerMobileNo, UserTypeConstantsInterface.CUSTOMER);
			
			if(customerAppUserModel != null) {
				customerModel = new CustomerModel();
				customerModel.setCustomerId(customerAppUserModel.getCustomerId());
				bWrapper = new BaseWrapperImpl();
				bWrapper.setBasePersistableModel(customerModel);
				bWrapper = getCommonCommandManager().loadCustomer(bWrapper);
				customerModel = (CustomerModel)bWrapper.getBasePersistableModel();
        
			}
        }
        catch(Exception ex){
        	logger.error(classNMethodName + "Customer App user/retailer contact model not found: " + ex.getStackTrace());
        }
        
        if(logger.isDebugEnabled()) {
			logger.debug("End of " + classNMethodName);
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)
			throws CommandException {
		String classNMethodName = "[CashWithdrwalInfoCommand.validate] ";
		String inputParams = "Logged in AppUserID: " + 
				ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Customer Mobile No:" + customerMobileNo;
	
		if(logger.isDebugEnabled()) {
			logger.debug("Start of " + classNMethodName);
		}
		
		if (isIvrResponse) {
			validationErrors = ValidatorWrapper.doRequired(transactionId,validationErrors,"Transaction ID");
			if(!validationErrors.hasValidationErrors())
				validationErrors = ValidatorWrapper.doNumeric(transactionId,validationErrors,"Transaction ID");
			
			return validationErrors;
		}
		if(isOtpRequired != null && isOtpRequired.equals("1")){
			validationErrors = ValidatorWrapper.doRequired(otp,validationErrors,"One Time Pin");
		}
		
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
		validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product Id");
		validationErrors = ValidatorWrapper.doRequired(encryptionType, validationErrors, "Encryption Type");
		validationErrors = ValidatorWrapper.doRequired(CNIC, validationErrors, "CNIC");
		
		validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");
		validationErrors = ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile No");
		
		validationErrors = ValidatorWrapper.doRequired(agentPIN, validationErrors, "Product Id");
		
		validationErrors = ValidatorWrapper.doRequired(withdrawalAmount, validationErrors, "Withdrawal Amount");
		validationErrors = ValidatorWrapper.doRequired(commissionAmount, validationErrors, "Commission Amount");
		validationErrors = ValidatorWrapper.doRequired(processingAmount, validationErrors, "Processing Amount");
		validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");
			
		if(!validationErrors.hasValidationErrors()) {
			validationErrors = ValidatorWrapper.doNumeric(withdrawalAmount,validationErrors,"Withdrawal Amount");
			validationErrors = ValidatorWrapper.doNumeric(commissionAmount, validationErrors, "Commission Amount");
			validationErrors = ValidatorWrapper.doNumeric(processingAmount, validationErrors, "Processing Amount");
			validationErrors = ValidatorWrapper.doNumeric(totalAmount, validationErrors, "Total Amount");
		}
		
		if(agentMobileNo.equals(customerMobileNo )) {
			validationErrors = ValidatorWrapper.addError(validationErrors, "Agent and Customer mobile nos. cannot be same.");
		}
		
		if(null== productModel) {
			logger.error(classNMethodName + "Product model not found: " + inputParams);
			
			validationErrors = ValidatorWrapper.addError(validationErrors, "Product not found.");
		}
		
		if(null == retailerContactModel) {
			logger.error(classNMethodName + "Retailer Contact Model is null. " + inputParams);
			
			validationErrors = ValidatorWrapper.addError(validationErrors, "Retailer Contact not found.");
		}
		
		if(null == customerAppUserModel) {
			logger.error(classNMethodName + "Customer App User not found: " + inputParams);

			validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
		}
		
		if(null == customerModel) {
			logger.error(classNMethodName + "Customer model not found: " + inputParams);

			validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
		}

		try {
			ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(appUserModel);
		
			if(userValidation.hasValidationErrors()) {
				logger.error(classNMethodName + " User validation failed."); 

				validationErrors = ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
			}
			
			userValidation = getCommonCommandManager().checkActiveAppUser(customerAppUserModel);
			
			if(userValidation.hasValidationErrors()) {
				logger.error(classNMethodName + " Customer validation failed"); 

				validationErrors = ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
			}
			
		} catch (FrameworkCheckedException e) {
			logger.error(classNMethodName + e.getMessage()); 

			validationErrors = ValidatorWrapper.addError(validationErrors, e.getMessage());
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("End of PayCashInfoCommand.validate()");
		}
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		
		String classNMethodName = "[CashWithdrwalCommand.execute] ";
		String exceptionMessage = "Exception Occured for Logged in AppUser";
		String inputParams = " ID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Mobile No:" + customerMobileNo ;
		String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
		
		try{
			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setOldPin(agentPIN);
			workFlowWrapper.setAccountInfoModel(accountInfoModel);
			if(isOtpRequired != null && isOtpRequired.equals("1")){
				logger.info("One Time Pin validation in CashWithdrawalCommand.execute()");
				baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO,customerMobileNo);
				baseWrapper.putObject(CommandFieldConstants.KEY_ACTION,"0");
				baseWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID,CommandFieldConstants.CMD_CASH_OUT_INFO);
				baseWrapper.putObject(CommandFieldConstants.KEY_PIN,(String)baseWrapper.getObject("OTPIN"));
				getCommandManager().executeCommand(baseWrapper,CommandFieldConstants.CMD_OTP_VERIFICATION);
			}

			if(isIvrResponse) {
				TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
				transactionCodeModel.setCode(transactionId);
				workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
				TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel();
				txDetailMasterModel.setTransactionCode(transactionId);
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(txDetailMasterModel);
				searchBaseWrapper = getCommonCommandManager().loadTransactionDetailMaster(searchBaseWrapper);
				txDetailMasterModel = (TransactionDetailMasterModel)searchBaseWrapper.getBasePersistableModel();
				workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);
				customerMobileNo = txDetailMasterModel.getSaleMobileNo();
				baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT,  txDetailMasterModel.getTransactionAmount());
				withdrawalAmount = txDetailMasterModel.getTransactionAmount().toString();

				if(txDetailMasterModel.getHandlerId() != null){
					this.getCommonCommandManager().loadHandlerData(txDetailMasterModel.getHandlerId(), workFlowWrapper);
				}

				//Following check is to avoid duplicate processing of transaction(duplicate IVR calls) [11 July 2017]
				if(txDetailMasterModel.getSupProcessingStatusId() != SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING.longValue()){
					logger.error("This transaction is already processed via IVR. trxId:"+txDetailMasterModel.getTransactionCode()+" existing status:"+txDetailMasterModel.getSupProcessingStatusId());
					throw new CommandException("Transaction already processed",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}

			}
			else{
				workFlowWrapper.setTxProcessingAmount(Double.parseDouble(processingAmount));
				workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
				workFlowWrapper.setTransactionAmount(Double.parseDouble(withdrawalAmount));
				workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
				workFlowWrapper.setHandlerModel(handlerModel);
				workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
			}

			// check product limit
			getCommonCommandManager().checkProductLimit(null,productModel.getProductId(), appUserModel.getMobileNo(),
					Long.valueOf(deviceTypeId), Double.parseDouble(withdrawalAmount), productModel, null, workFlowWrapper.getHandlerModel());

			String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

			// Velocity validation
			BaseWrapper vWrapper = new BaseWrapperImpl();
			vWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
			vWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
			vWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
			vWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
			vWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.valueOf(withdrawalAmount));
			vWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, customerModel.getSegmentId());
			vWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
			vWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//			vWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);

			getCommonCommandManager().checkVelocityCondition(vWrapper);

			ProductVO productVo = getCommonCommandManager().loadProductVO(baseWrapper);

			if(productVo == null) {
				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}

			workFlowWrapper.setCustomerModel(customerModel);
			workFlowWrapper.setSegmentModel(new SegmentModel());
			workFlowWrapper.getSegmentModel().setSegmentId(customerModel.getSegmentId());

			workFlowWrapper.setProductVO(productVo);
			workFlowWrapper.setProductModel(productModel);
			workFlowWrapper.setAppUserModel(appUserModel);
			workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);


			//Agent smart money account
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			SmartMoneyAccountModel agentSMAModel =  new SmartMoneyAccountModel();
			agentSMAModel.setRetailerContactId(appUserModel.getRetailerContactId());
			agentSMAModel.setActive(Boolean.TRUE);
			searchBaseWrapper.setBasePersistableModel(agentSMAModel);

			searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);

			@SuppressWarnings("rawtypes")
			CustomList smaList = searchBaseWrapper.getCustomList();
			if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
				agentSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
			}

			workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);
//			already checked above
//			if( ! isIvrResponse) {
//				verifyPIN(agentSMAModel, agentPIN);
//			}

			//Customer smart money account,
			searchBaseWrapper = new SearchBaseWrapperImpl();
			SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
			customerSMAModel.setCustomerId(customerAppUserModel.getCustomerId());
			customerSMAModel.setActive(Boolean.TRUE);
			customerSMAModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			searchBaseWrapper.setBasePersistableModel(customerSMAModel);

			searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);

			smaList = searchBaseWrapper.getCustomList();
			if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
				customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
			}

			workFlowWrapper.setOlaSmartMoneyAccountModel(customerSMAModel);


			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_WITHDRAWAL_TX);
			workFlowWrapper.setRetailerContactModel(retailerContactModel);

			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setDeviceTypeModel(deviceTypeModel);

			workFlowWrapper.putObject(CommandFieldConstants.KEY_APP_ID,appId);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED,isOtpRequired);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID,terminalId);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,channelId);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN,stan);
			workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, thirdPartyTransactionId);

			if(isOtpRequired != null && isOtpRequired.equals("1"))
				isIvrResponse = Boolean.TRUE;

			workFlowWrapper.setIsIvrResponse(isIvrResponse);
			workFlowWrapper.setUSSDCashWithdrawal(isUSSDCashWirhdrawal);

			logger.info(classNMethodName + " Going to execute Transaction flow. " + inputParams);

			workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);

			transactionModel = workFlowWrapper.getTransactionModel();
			productModel = workFlowWrapper.getProductModel();

			if (((CashWithdrawalVO)workFlowWrapper.getProductVO()).getCustomerBalance() != null) {
				customerBalance = ((CashWithdrawalVO)workFlowWrapper.getProductVO()).getCustomerBalance();
			}
			workFlowWrapper.putObject("productTile",productModel.getName());
			getCommonCommandManager().sendSMS(workFlowWrapper);

			/*if (((CashWithdrawalVO)workFlowWrapper.getProductVO()).getAgentBalance() != null) {
				agentBalance = ((CashWithdrawalVO)workFlowWrapper.getProductVO()).getAgentBalance();
			}*/
		}
		catch(CommandException e) {
			if(isIvrResponse){
				ivrErrorCode = workFlowWrapper.getErrorCode();
				if (StringUtils.isEmpty(ivrErrorCode)) {
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
			}else{
				ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
			}

			logger.error(genericExceptionMessage + e.getMessage());
			throw e;
		}
		catch(WorkFlowException we) {
			if(isIvrResponse){
				ivrErrorCode = workFlowWrapper.getErrorCode();
				if (StringUtils.isEmpty(ivrErrorCode)) {
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
			}else{
				ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
			}

			logger.error(genericExceptionMessage + we.getMessage());
			throw new CommandException(we.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,we);
		}
		catch (ClassCastException cce) {
			logger.error(genericExceptionMessage + cce.getMessage());
			throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,cce);
		}
		catch(Exception ex) {
			if(isIvrResponse){
				ivrErrorCode = workFlowWrapper.getErrorCode();
				if (StringUtils.isEmpty(ivrErrorCode)) {
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
			}else{
				ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
			}

			logger.error(genericExceptionMessage + ex.getMessage());

			if(ex.getMessage() != null && ex.getMessage().indexOf("JTA") != -1) {
				throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
			else{
				throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
		}
	}

	@Override
	public String response() {
		// when isIvrResponse = true no response is needed
		if(isIvrResponse && (isOtpRequired != null && !isOtpRequired.equals("1"))){
			return ivrErrorCode;
		}
		
		List<LabelValueBean> lvbs = new ArrayList<LabelValueBean>();
		
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode().toString()));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, CNIC));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, transactionModel.getCreatedOn().toString()));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(transactionModel.getCreatedOn())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, productModel.getName()));

		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TXAM, replaceNullWithZero(transactionModel.getTransactionAmount()).toString()));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TXAMF, Formatter.formatNumbers(replaceNullWithZero(transactionModel.getTransactionAmount()))));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount().toString())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAM, processingAmount));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(Double.parseDouble(processingAmount))));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMT, String.valueOf(transactionModel.getTotalAmount().doubleValue())));
		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));

		lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BAL, Formatter.formatNumbers(customerBalance)));

		return MiniXMLUtil.createResponseXMLByParams(lvbs);
	}
	
}
