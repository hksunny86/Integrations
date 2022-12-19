package com.inov8.microbank.fonepay.commands;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FonepayPaymentCommand extends BaseCommand {

	protected final Log logger = LogFactory.getLog(FonepayPaymentInfoCommand.class);
	private AppUserModel appUserModel = null;
	private String mobileNo;
	private String dateTime;
	private String amount;
	private String rrn;
	protected String cnic;
	protected String transactionType;
	protected String paymentType;
	protected String settelmentType;
	protected String fonepayTransactionCode;
	private String mpin;

	protected String transactionId;
	protected String channelId;
	protected String accNo;
	private Long deviceTypeId;
	protected BaseWrapper preparedBaseWrapper;
	private FonePayManager fonepayManager;
	private String vcn;
	private String transactionCode;
	private String terminalId;
	private String stan;

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		if (logger.isDebugEnabled()){
			logger.debug("Start of FonepayPaymentCommand.prepare()");
		}
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		dateTime = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DATE);
		amount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
		rrn = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RRN);
		deviceTypeId = DeviceTypeConstantsInterface.WEB_SERVICE;
		mpin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		transactionType = this.getCommandParameter(baseWrapper, FonePayConstants.TRANSACTION_TYPE);
		paymentType = this.getCommandParameter(baseWrapper, FonePayConstants.PAYMENT_TYPE);
		settelmentType = this.getCommandParameter(baseWrapper, FonePayConstants.KEY_FONEPAY_SETTLEMENT_TYPE);
		fonepayTransactionCode = this.getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
		stan=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_STAN);
		channelId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID);
		if(channelId.equals(FonePayConstants.APIGEE_CHANNEL)){
			terminalId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TERMINAL_ID);
		}

		if(paymentType.equals(FonePayConstants.VIRTUAL_CARD_PAYMENT)){
			vcn = this.getCommandParameter(baseWrapper, FonePayConstants.VIRTUAL_CARD_NO);
		}
		
		
		try {
			appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNo);			
			if(appUserModel != null){
				logger.debug("[FonepayPaymentCommand.execute] AppUserModel loader wil AppUserId:"+appUserModel.getAppUserId());
				ThreadLocalAppUser.setAppUserModel(appUserModel);
				cnic = appUserModel.getNic();
			}
		} catch (Exception e) {
			logger.error("[FonepayPaymentCommand.execute] Unable to Load AppUserModel by mobile: " + mobileNo + "\nSetting Default appUserModel in ThreadLocal. appUserId:" + PortalConstants.SCHEDULER_APP_USER_ID+"\n",e);
		}
		if (logger.isDebugEnabled()){
			logger.debug("End of FonepayPaymentCommand.prepare()");
		}
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		
		validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
		validationErrors = ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
		validationErrors = ValidatorWrapper.doRequired(amount, validationErrors, "Amount");	
		validationErrors = ValidatorWrapper.doRequired(paymentType, validationErrors, "Payment Type");
		if(paymentType.equals(FonePayConstants.VIRTUAL_CARD_PAYMENT)){
			validationErrors = ValidatorWrapper.doRequired(vcn, validationErrors, "Virtual Card No");
		}
				
		if(!validationErrors.hasValidationErrors()){
			validationErrors = ValidatorWrapper.doNumeric(mobileNo,validationErrors,"Mobile No");
			validationErrors = ValidatorWrapper.doNumeric(cnic, validationErrors, "CNIC");
			validationErrors = ValidatorWrapper.doNumeric(amount, validationErrors, "Amount");
		}
		
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		logger.info("Start of FonepayPaymentCommand.execute()");
		
		Double _commissionAmount = 0.0D;
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try {
			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
			
			//Always customer Id will be set in case of Fonepay transaction.				
			smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());			
			searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
			searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
			smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
		
			ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
			if(validationErrors.hasValidationErrors()){
					throw new CommandException(validationErrors.getErrors(), FonePayResponseCodes.PIN_CHANGE_REQUIRED,ErrorLevel.MEDIUM,new Throwable());
			}
			//*******************************************************************************
			//All Validations goes here 
			ProductModel productModel = new ProductModel();
			if(!(channelId.equals(FonePayConstants.APIGEE_CHANNEL))) {

				if (paymentType.equals(FonePayConstants.WEB_SERVICE_PAYMENT)) {
					productModel.setProductId(ProductConstantsInterface.WEB_SERVICE_PAYMENT);
				} else {
					productModel.setProductId(ProductConstantsInterface.VIRTUAL_CARD_PAYMENT);
				}
			}
			else{
				productModel.setProductId(ProductConstantsInterface.APIGEE_PAYMENT);
			}
			
			baseWrapper.setBasePersistableModel(productModel);
			baseWrapper = commonCommandManager.loadProduct(baseWrapper);
			
			productModel = (ProductModel) baseWrapper.getBasePersistableModel();
			if(productModel == null){
				logger.error("[FonepayPaymentCommand.execute()] Unable to load productModel. mobileNo:" + mobileNo);
				throw new CommandException("Unable to load productModel", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
			}
			workFlowWrapper.setProductModel(productModel);
			
			AppUserModel customerAppUserModel = new AppUserModel();
			customerAppUserModel.setMobileNo(mobileNo);
			customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			customerAppUserModel.setCustomerId(appUserModel.getCustomerId());
					
			
			CustomerModel custModel = new CustomerModel();
	        custModel.setCustomerId(customerAppUserModel.getCustomerId());
	        baseWrapper.setBasePersistableModel(custModel);
	        baseWrapper = this.getCommonCommandManager().loadCustomer(baseWrapper);
	        workFlowWrapper.setSegmentModel(((CustomerModel)baseWrapper.getBasePersistableModel()).getSegmentIdSegmentModel());	
	        workFlowWrapper.setTaxRegimeModel(((CustomerModel)baseWrapper.getBasePersistableModel()).getTaxRegimeIdTaxRegimeModel());
	
			Long segmentId = workFlowWrapper.getSegmentModel().getSegmentId();

			DistributorModel distributorModel=new DistributorModel();
			distributorModel.setDistributorId(FonePayConstants.FONEPAY_AGENT_NETWORK);
			//Product Limit Check
			getCommonCommandManager().checkProductLimit(segmentId, productModel.getProductId(), appUserModel.getMobileNo(), 
				     deviceTypeId, Double.parseDouble(amount), productModel, distributorModel, null);
			
			//*****************************************************************************************************
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, new Long(productModel.getProductId()));
			bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,new Long(deviceTypeId));
//			bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, fromRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
//			bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, fromRetailerContactModel.getDistributorLevelId());
			bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(amount));

			bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segmentId));
			//boolean result = commonCommandManager.checkVelocityCondition(bWrapper);

			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);					
			workFlowWrapper.setAccountInfoModel(new AccountInfoModel());
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setTransactionAmount(Double.valueOf(amount));
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
			switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			
			
			baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
			baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, amount);
			
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();

            if(paymentType.equals(FonePayConstants.AGENT_SETTLEMENT_PAYMENT)) {
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.AGENT_SATTELMENT_PAYMENT_TX);
            }else if(paymentType.equals(FonePayConstants.VIRTUAL_CARD_PAYMENT)){
				transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.VIRTUAL_CARD_PAYMENT_TX);
			}else{
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.WEB_SERVICE_PAYMENT_TX);
            }
			
			TransactionModel trxnModel = new TransactionModel();
			trxnModel.setTransactionAmount(Double.parseDouble(amount));			
			
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(deviceTypeId);
			workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setTransactionModel(trxnModel);
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
			//workFlowWrapper.setProductVO(productVo);
			workFlowWrapper.setTxProcessingAmount(Double.parseDouble(amount));
			workFlowWrapper.setTotalAmount(Double.parseDouble(amount));
			workFlowWrapper.setTransactionAmount(Double.parseDouble(amount));
			
			workFlowWrapper.setAppUserModel(appUserModel);
			
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
			workFlowWrapper.setSwitchWrapper(switchWrapper);
			workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
			
			workFlowWrapper.putObject(CommandFieldConstants.KEY_DATE, dateTime);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_RRN, rrn);
			workFlowWrapper.setIsCustomerInitiatedTransaction(true);


			workFlowWrapper.putObject(FonePayConstants.KEY_FONEPAY_SETTLEMENT_TYPE,settelmentType);
			workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE,fonepayTransactionCode);
            workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME,transactionType);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,channelId);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN,stan);

			if(channelId.equals(FonePayConstants.APIGEE_CHANNEL)) {
				workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID,terminalId);
			}

			//**************************************************************************************//
			//						Calling of Transaction											//
			//**************************************************************************************//
			
			 workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
			transactionCode = workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode();
if(MessageUtil.getMessage("WEBSERVICE.SENDSMS.CHANNEL").contains(channelId)) {
	DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
	DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");
	String smsText = MessageUtil.getMessage("webservice.trx.sms", new String[]{
			workFlowWrapper.getTransactionDetailMasterModel().getTransactionAmount().toString(),
			workFlowWrapper.getTransactionDetailMasterModel().getExclusiveCharges().toString(),
			dtf.print(new DateTime()),
			workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode()});
	SmsMessage smsMessage = new SmsMessage(workFlowWrapper.getCustomerAppUserModel().getMobileNo(), smsText);
	this.getCommonCommandManager().getSmsSender().send(smsMessage);
}

			//**************************************************************************************//
			
			
		}catch (Exception e) {
			logger.error("[FonepayPaymentCommand.execute()] Exception occured for mobileNo:" + mobileNo + "\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		
	} 

	@Override
	public String response() {
		
		return transactionCode;
	}

	public FonePayManager getFonepayManager() {
		return fonepayManager;
	}

	public void setFonepayManager(FonePayManager fonepayManager) {
		this.fonepayManager = fonepayManager;
	}

	
	
	
}
