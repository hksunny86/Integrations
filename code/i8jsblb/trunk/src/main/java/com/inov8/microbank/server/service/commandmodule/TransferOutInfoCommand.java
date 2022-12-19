package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;

public class TransferOutInfoCommand extends BaseCommand {
	
	
	/** PARAMETERS  **/
	private String productId = null;
	private String amount = null;
	
	private String commissionAmount = null;
	private ProductModel productModel = null;
	private AppUserModel appUserModel = null;
	private String coreAccountNick = null;
	private String coreAccountNumber = null;
	private String bbAccountNick = null;
	private String bankId = null;
	private String deviceTypeId = null;
	private String agentMobileNumber = null;
	
	protected final Log logger = LogFactory.getLog(TransferOutInfoCommand.class);
	
	
	@Override
	public void execute() throws CommandException {
		
		logger.info(":- Started Transfer Out Command -:");
		
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
		

		if(appUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.RETAILER.longValue()) {

			throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, new Throwable());
		}
		
		RetailerContactModel retailerContactModel = null;
		
		try {
			retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(retailerContactModel);
			baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
			retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
			
			DistributorModel distributorModel = getCommonCommandManager().loadDistributor(appUserModel);
			
			getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.valueOf(amount), productModel, distributorModel, handlerModel);

			String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

			checkVelocityCondition(distributorModel, retailerContactModel, null, userId);
			
		} catch (Exception e) {
			
			throw new CommandException(e.getLocalizedMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
		}			

		SmartMoneyAccountModel _smartMoneyAccountModelBB = getSmartMoneyAccountModel(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		SmartMoneyAccountModel _smartMoneyAccountModelCore = getSmartMoneyAccountModel(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

		
		
		AccountInfoModel accountInfoModelCore = null;
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		
		try {
			accountInfoModelCore = this.getCommonCommandManager().getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

			workFlowWrapper.setAccountInfoModel(accountInfoModelCore);
			workFlowWrapper.setSmartMoneyAccountModel(_smartMoneyAccountModelCore);
			CustomerAccount customerAccount = new CustomerAccount();
			customerAccount.setNumber(accountInfoModelCore.getAccountNo());
			workFlowWrapper.setCustomerAccount(customerAccount);
			this.getCommonCommandManager().fetchAccountTitle(workFlowWrapper);
			
			if(workFlowWrapper.getSwitchWrapper() != null && workFlowWrapper.getSwitchWrapper().getMiddlewareIntegrationMessageVO() != null) {

				coreAccountNick = workFlowWrapper.getSwitchWrapper().getMiddlewareIntegrationMessageVO().getAccountTitle();
				coreAccountNumber = workFlowWrapper.getSwitchWrapper().getMiddlewareIntegrationMessageVO().getAccountNo1();
			}
			
		} catch (Exception e) {
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		} 
		
		bbAccountNick = _smartMoneyAccountModelBB.getName();
		
		WorkFlowWrapper _workFlowWrapper = new WorkFlowWrapperImpl();
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionAmount(Double.valueOf(amount));

		TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
		transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.TRANSFER_OUT_TX);
		
		_workFlowWrapper.setTransactionModel(transactionModel);
		_workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
		_workFlowWrapper.setProductModel(productModel);
		
		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
		_workFlowWrapper.setSegmentModel(segmentModel);
		
		
		Double _commissionAmount = 0.0D;
		
		try {
			
			_workFlowWrapper.setRetailerContactModel(retailerContactModel);
			_workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());
			
			CommissionWrapper commissionWrapper = getCommonCommandManager().calculateCommission(_workFlowWrapper);
			
			CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
					CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			
			commissionAmount = commissionAmounts.getTransactionProcessingAmount().toString();	
			
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
	}
	
	
	void checkVelocityCondition(DistributorModel distributorModel, RetailerContactModel retailerContactModel, Long segmentId,
								String userId) throws FrameworkCheckedException {
		
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
	    bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
	    bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, distributorModel.getDistributorId());
	    bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
		bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(amount));
		bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
		bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//		bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
	    if(segmentId != null && CommissionConstantsInterface.DEFAULT_SEGMENT_ID.longValue() != segmentId.longValue()) {
	    	bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, segmentId);
	    }
	    
	    getCommonCommandManager().checkVelocityCondition(bWrapper);
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
		
		logger.debug("Found Smart Money Account "+ _smartMoneyAccountModel.getName());
		
		return smartMoneyAccountModel;
	}
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		amount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		agentMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOB_NO);
		
		if(StringUtil.isNullOrEmpty(commissionAmount)) {
			
			ProductModel productModel = new ProductModel();
			productModel.setPrimaryKey(Long.valueOf(productId));
			
			BaseWrapper _baseWrapper = new BaseWrapperImpl();
			_baseWrapper.setBasePersistableModel(productModel);
			
			try {
				_baseWrapper = getCommonCommandManager().loadProduct(_baseWrapper);
				this.productModel = (ProductModel)_baseWrapper.getBasePersistableModel();
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}	
		}
		
	}
	

	@Override
	public String response() {

		return toXML();
	}

	
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
				
		if(!validationErrors.hasValidationErrors()) {
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		
		return validationErrors;
	}
	

	private String toMobileXMLResponse() {
		
		StringBuilder responseBuilder = new StringBuilder();
			
		responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);		

		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER_AGNETMATE, this.coreAccountNumber));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, StringEscapeUtils.escapeXml(this.coreAccountNick)));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB_AGNETMATE, this.bbAccountNick));
		
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, this.amount));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, Formatter.formatDoubleByPattern(Double.valueOf(amount), "#,###.00")));

		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAM, this.commissionAmount));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAMF, Formatter.formatDoubleByPattern(Double.valueOf(commissionAmount), "#,###.00")));

		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, this.commissionAmount));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMTF, Formatter.formatDoubleByPattern(Double.valueOf(commissionAmount), "#,###.00")));

		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMT, String.valueOf(Double.parseDouble(amount) + Double.parseDouble(commissionAmount))));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMTF, Formatter.formatDoubleByPattern(Double.valueOf(Double.parseDouble(amount) + Double.parseDouble(commissionAmount)), "#,###.00")));
					
		responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);	
		
		logger.info(responseBuilder.toString());
		return responseBuilder.toString();
	}
		

		private String toXML() {		
			
			String productName = productModel.getName();
			
			StringBuilder response = new StringBuilder();
					
			if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {

				response.append(toMobileXMLResponse());
				
			} else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {

				response.append("<msg id='105'><params>");

				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, productModel.getProductId().toString()));
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_NAME, productName));
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER, this.coreAccountNick));
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB, this.bbAccountNick));
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, this.amount));
				response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, this.commissionAmount));

				response.append("</params></msg>");
			} else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
				
				response.append(toMobileXMLResponse());
			}
			
			return response.toString();
	}
	
	
	public String toString() {
		
		return org.apache.commons.lang.builder.ReflectionToStringBuilder.toString(this);
	}
}