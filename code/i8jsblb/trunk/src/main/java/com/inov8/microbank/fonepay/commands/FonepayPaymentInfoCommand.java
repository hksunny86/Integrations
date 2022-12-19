package com.inov8.microbank.fonepay.commands;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import java.util.Date;
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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

public class FonepayPaymentInfoCommand extends BaseCommand{

	protected final Log logger = LogFactory.getLog(FonepayPaymentInfoCommand.class);
	private AppUserModel appUserModel = null;
	private String mobileNo;
	private String dateTime;
	private String amount;
	private String rrn;
	protected String cnic;
	protected String transactionId;
	protected String accNo;
	private Long deviceTypeId;
	private String commissionAmount = null;
	protected BaseWrapper preparedBaseWrapper;
	private Double TPAM = 0.0D;
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		if (logger.isDebugEnabled()){
			logger.debug("Start of FonepayPaymentInfoCommand.prepare()");
		}
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		dateTime = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DATE);
		amount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
		rrn = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RRN);
		deviceTypeId = DeviceTypeConstantsInterface.WEB_SERVICE;
		
		try {
			appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNo);			
			if(appUserModel != null){
				logger.debug("[FonepayPaymentInfoCommand.execute] AppUserModel loader wil AppUserId:"+appUserModel.getAppUserId());
				ThreadLocalAppUser.setAppUserModel(appUserModel);
				cnic = appUserModel.getNic();
			}
		} catch (Exception e) {
			logger.error("[FonepayPaymentInfoCommand.execute] Unable to Load AppUserModel by mobile: " + mobileNo + "\nSetting Default appUserModel in ThreadLocal. appUserId:" + PortalConstants.SCHEDULER_APP_USER_ID+"\n",e);
		}
		if (logger.isDebugEnabled()){
			logger.debug("End of FonepayPaymentInfoCommand.prepare()");
		}
		
	}
	
	
	
	
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		
		validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
		validationErrors = ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
		validationErrors = ValidatorWrapper.doRequired(amount, validationErrors, "Amount");
		
		
		if(!validationErrors.hasValidationErrors()){
			validationErrors = ValidatorWrapper.doNumeric(mobileNo,validationErrors,"Mobile No");
			validationErrors = ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
			validationErrors = ValidatorWrapper.doNumeric(amount, validationErrors, "Amount");
		}
		
		return validationErrors;
	}
	
	
	
	
	@Override
	public void execute() throws CommandException {
		
		logger.info("Start of FonepayPaymentInfoCommand.execute()");
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
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			//*******************************************************************************
			//All Validations goes here
			ProductModel productModel = new ProductModel();
			productModel.setProductId(ProductConstantsInterface.APIGEE_PAYMENT);
			baseWrapper.setBasePersistableModel(productModel);
			baseWrapper = commonCommandManager.loadProduct(baseWrapper);
			
			productModel = (ProductModel) baseWrapper.getBasePersistableModel();
			if(productModel == null){
				logger.error("[FonepayPaymentInfoCommand.execute()] Unable to load productModel. mobileNo:" + mobileNo);
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
		
			//Product Limit Check
			getCommonCommandManager().checkProductLimit(segmentId, productModel.getProductId(), appUserModel.getMobileNo(), 
				     deviceTypeId, Double.parseDouble(amount), productModel, null, null);
			
			//*****************************************************************************************************
			
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
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.WEB_SERVICE_PAYMENT_TX);

			
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
			
			CommissionWrapper commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);			
			CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			_commissionAmount = commissionAmountsHolder.getTotalCommissionAmount();
			TPAM = commissionAmountsHolder.getTransactionProcessingAmount();
			commissionAmount = _commissionAmount.toString();	
			
			//**************************************************************************************//
			//						Calling of Transaction											//
			//**************************************************************************************//
			
			//workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
			
			//**************************************************************************************//
			
			//commonCommandManager.sendSMS(workFlowWrapper);
			

		} catch (Exception e) {
		logger.error("[FonepayPaymentInfoCommand.execute()] Exception occured for mobileNo:" + mobileNo + "\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
		throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
	}
		
			
	}
	

	/**
	 * @param _smartMoneyAccountModel
	 * @return
	 */
	private SmartMoneyAccountModel getSmartMoneyAccountModel(Long paymentModeId) throws CommandException {

		SmartMoneyAccountModel smartMoneyAccountModel = null;
		
		SmartMoneyAccountModel _smartMoneyAccountModel = new SmartMoneyAccountModel();
		_smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
		_smartMoneyAccountModel.setDeleted(Boolean.FALSE);
		_smartMoneyAccountModel.setActive(Boolean.TRUE);
		_smartMoneyAccountModel.setPaymentModeId(paymentModeId);
	    
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(_smartMoneyAccountModel);
		
		try {
			
			SearchBaseWrapper searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(wrapper);
			
			if(searchBaseWrapper != null) {
				
				List<SmartMoneyAccountModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();
				
				if(resultsetList != null && !resultsetList.isEmpty()) {
					
					smartMoneyAccountModel = resultsetList.get(0);
				}
			}
			
			if(smartMoneyAccountModel == null) {

				if(paymentModeId.longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT.longValue()) {

					throw new CommandException("T24 Account is not linked with your Branchless Bank Account", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
					
				} else if(paymentModeId.longValue() == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue()) {

					throw new CommandException("Branchless Bank Account not found", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
				}				
			}
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		logger.debug("Found Smart Money Account "+ smartMoneyAccountModel.getName());
		
		return smartMoneyAccountModel;
	}
	

	@Override
	public String response() {
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append(this.TPAM);
		return strBuilder.toString();
		//return toXML();
	}

	private String toXML(){
		StringBuilder strBuilder = new StringBuilder();
		if(commissionAmount != null){
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RRN, rrn));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, this.amount));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+this.TPAM));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, this.commissionAmount));

			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, replaceNullWithEmpty(cnic)));
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		}
		
		
		return strBuilder.toString();
	}
	
	
	
	
}
