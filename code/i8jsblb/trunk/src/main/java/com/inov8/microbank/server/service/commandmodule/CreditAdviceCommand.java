package com.inov8.microbank.server.service.commandmodule;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

public class CreditAdviceCommand extends BaseCommand {

	protected final Log logger = LogFactory.getLog(CreditAdviceCommand.class);
	private AppUserModel appUserModel = null;
	private String mobileNo;
	private String senderBankAccountNo;
	private String senderBankId;
	private String amount;
	private String deviceTypeId;
	private Date requestTime;
	private String stan;
	private String rrn;
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		logger.debug("Start of CreditAdviceCommand.prepare()");
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		senderBankAccountNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		senderBankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		amount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		requestTime = (Date) baseWrapper.getObject(CommandFieldConstants.KEY_TX_DATE);
		stan = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ST_TX_NO);
		rrn = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RRN);
		
		try {
			appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNo);
			if(appUserModel != null){
				logger.debug("[CreditAdviceCommand.execute] AppUserModel loader wil AppUserId:"+appUserModel.getAppUserId());
				ThreadLocalAppUser.setAppUserModel(appUserModel);				
			}
		} catch (Exception e) {
			logger.error("[CreditAdviceCommand.execute] Unable to Load AppUserModel by mobile: " + mobileNo + "\nSetting Default appUserModel in ThreadLocal. appUserId:" + PortalConstants.SCHEDULER_APP_USER_ID+"\n",e);
		}
		
		logger.debug("End of CreditAdviceCommand.prepare()");
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)throws CommandException {
		
		logger.debug("Start of CreditAdviceCommand.validate()");
		
		validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
		validationErrors = ValidatorWrapper.doRequired(senderBankAccountNo, validationErrors, "Sender Account No");
		validationErrors = ValidatorWrapper.doRequired(amount, validationErrors, "Amount");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(mobileNo,validationErrors,"Mobile No");
			validationErrors = ValidatorWrapper.doNumeric(amount, validationErrors, "Amount");
		}
		
		logger.debug("End of CreditAdviceCommand.validate()");
		
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		ActionLogModel actionLogModel = new ActionLogModel();

		logger.debug("Start of CreditAdviceCommand.execute()");
		
		try {
			this.actionLogBeforeStart(actionLogModel, mobileNo);

			validateAppUserType(appUserModel);
			
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			if(appUserModel.getAppUserTypeId() != UserTypeConstantsInterface.CUSTOMER.longValue()){				
				BaseWrapper bWrapper = new BaseWrapperImpl();
				retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
				bWrapper.setBasePersistableModel(retailerContactModel);
				bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
				retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
			}
				
			
			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
			if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()){				
				smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
			}else{
				smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
			}
			searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
			searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
			smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
		
			ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
			
			if(validationErrors.hasValidationErrors()){
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);					
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setAccountInfoModel(new AccountInfoModel());
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setTransactionAmount(Double.valueOf(amount));
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
			switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			
			baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.IBFT.longValue());
			baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
			baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, amount);
			ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
			if(productVo == null){
				logger.error("[CreditAdviceCommand.execute()] Unable to load productVo. mobileNo:" + mobileNo);
				throw new CommandException("Unable to load productVo", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
			}
			
			
			ProductModel productModel = new ProductModel();
			productModel.setProductId(ProductConstantsInterface.IBFT);
			baseWrapper.setBasePersistableModel(productModel);
			baseWrapper = commonCommandManager.loadProduct(baseWrapper);
			
			productModel = (ProductModel) baseWrapper.getBasePersistableModel();
			if(productModel == null){
				logger.error("[CreditAdviceCommand.execute()] Unable to load productModel. mobileNo:" + mobileNo);
				throw new CommandException("Unable to load productModel", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
			}
			workFlowWrapper.setProductModel(productModel);
			
			
			
			AppUserModel customerAppUserModel = new AppUserModel();
			customerAppUserModel.setMobileNo(mobileNo);
			customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			customerAppUserModel.setCustomerId(appUserModel.getCustomerId());
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.IBFT_TX);

			
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
			workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
			workFlowWrapper.setProductVO(productVo);
			workFlowWrapper.setTxProcessingAmount(Double.parseDouble(amount));
			workFlowWrapper.setTotalAmount(Double.parseDouble(amount));
			workFlowWrapper.setTransactionAmount(Double.parseDouble(amount));
			workFlowWrapper.setAppUserModel(appUserModel);
			if(appUserModel.getAppUserTypeId() != UserTypeConstantsInterface.CUSTOMER.longValue()){					
				workFlowWrapper.setRetailerContactModel(retailerContactModel);
				workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
				workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());
			}
			workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
			workFlowWrapper.setSwitchWrapper(switchWrapper);
			workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
			
			workFlowWrapper.putObject(CommandFieldConstants.KEY_TX_DATE, requestTime);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_RRN, rrn);
			
			workFlowWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_SENDER, senderBankAccountNo);
			workFlowWrapper.putObject(CommandFieldConstants.KEY_BANK_ID, senderBankId);
			
			workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
			
			commonCommandManager.sendSMS(workFlowWrapper);
			
			this.actionLogAfterEnd(actionLogModel);

		} catch (Exception e) {
			logger.error("[CreditAdviceCommand.execute()] Exception occured for mobileNo:" + mobileNo + "\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		logger.debug("End of CreditAdviceCommand.execute()");
	}

	private void validateAppUserType(AppUserModel appUserModel) throws CommandException{
		if(appUserModel == null  || appUserModel.getAppUserTypeId() == null 
				|| (appUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.CUSTOMER
						&& appUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.RETAILER)){
			
			logger.error("[CreditAdviceCommand.validateAppUserType] Invalid User... mobileNo:" + mobileNo);
			throw new CommandException("Invalid User", ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, null);
			
		}
	}
	
	
	
	private void actionLogBeforeStart(ActionLogModel actionLogModel, String mobileNo){
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.longValue());
		actionLogModel.setStartTime(new Timestamp( new java.util.Date().getTime() ));
		
		Long appUserId = PortalConstants.SCHEDULER_APP_USER_ID;
		if(ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserId() != null){
			appUserId = ThreadLocalAppUser.getAppUserModel().getAppUserId();
		}
		
		actionLogModel.setAppUserId(appUserId);
		actionLogModel.setCommandId(Long.valueOf(CommandFieldConstants.CREDIT_ADVICE_COMMAND));
		actionLogModel.setCustomField1(mobileNo);
		
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
		
		if(actionLogModel.getActionLogId() != null){
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}

	private void actionLogAfterEnd(ActionLogModel actionLogModel){
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		try{
			actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
			actionLogModel.setEndTime(new Timestamp( new java.util.Date().getTime() ));
			
			baseWrapper.setBasePersistableModel(actionLogModel);
			
			this.getActionLogManager().createOrUpdateActionLog(baseWrapper);
			
		}catch(Exception e){
			logger.error("IBFTSwitchController.actionLogAfterEnd - Error occured: ", e);
		}
	}
	
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel){
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try{
			baseWrapper = this.getActionLogManager().createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel)baseWrapper.getBasePersistableModel();
		}catch(Exception ex){
			logger.error("[IBFTSwitchController]Exception occurred while insertActionLogRequiresNewTransaction() ",ex);
		}
		return actionLogModel;
	}

	public ActionLogManager getActionLogManager() {
		ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		return (ActionLogManager) applicationContext.getBean("actionLogManager");
	}

	@Override
	public String response() {
		return "";
	}

}
