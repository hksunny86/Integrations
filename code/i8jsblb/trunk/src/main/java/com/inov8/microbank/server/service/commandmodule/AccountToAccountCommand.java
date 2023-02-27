package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_CAMT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CAMTF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CMOB;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PROD;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_RCMOB;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TAMT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TAMTF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TPAM;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TPAMF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRXID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TXAM;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TXAMF;

import java.util.ArrayList;

import com.inov8.microbank.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.fop.viewer.Command;


public class AccountToAccountCommand extends BaseCommand {
	protected AppUserModel appUserModel;
	protected String productId;
	protected String txProcessingAmount;
	protected String pin;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String totalAmount;
	
	protected String senderMobileNo;
	protected String recipientMobileNo;
	protected String agentMobileNo;
	protected String txAmount;
	
	protected String cvv;
	protected String tPin;
	
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	private Boolean isIvrResponse;
	private String transactionId;
	
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel smartMoneyAccountModel;
	UserDeviceAccountsModel userDeviceAccountsModel;
	
	RetailerContactModel fromRetailerContactModel;
	long fromSegmentId;
	long toSegmentId;
	
	WorkFlowWrapper workFlowWrapper;
	private String encryption_type;
	
	protected final Log logger = LogFactory.getLog(AccountToAccountCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AccountToAccountCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();

		if(appUserModel.getRetailerContactId() != null)
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
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
						senderMobileNo = txDetailMasterModel.getSaleMobileNo();
						recipientMobileNo = txDetailMasterModel.getRecipientMobileNo();
						toSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(recipientMobileNo);
						txAmount = txDetailMasterModel.getTransactionAmount().toString();
						baseWrapper.putObject( CommandFieldConstants.KEY_TX_AMOUNT,  txDetailMasterModel.getTransactionAmount());
						baseWrapper.putObject( CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE,  recipientMobileNo);
						
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
						workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
						workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
						workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));

						workFlowWrapper.setHandlerModel(handlerModel);
						workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
					}
					
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));

					String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

					// Velocity validation - start
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
					bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
					bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, fromRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
					bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, fromRetailerContactModel.getDistributorLevelId());
					bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(txAmount));
					bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, fromRetailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
					bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//					bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);

					bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(fromSegmentId));
					commonCommandManager.checkVelocityCondition(bWrapper);
					// Velocity validation - end

					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
						
					AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel.setMobileNo(senderMobileNo);
					
					AppUserModel receiverAppUserModel = new AppUserModel();
					receiverAppUserModel.setMobileNo(recipientMobileNo);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.ACCOUT_TO_ACCOUNT_TX);
						
					smartMoneyAccountModel = new SmartMoneyAccountModel();
//					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
			
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
					workFlowWrapper.setReceiverAppUserModel(receiverAppUserModel);
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.setFromRetailerContactModel(fromRetailerContactModel);
					workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
					workFlowWrapper.setFromSegmentId(fromSegmentId);
					workFlowWrapper.setToSegmentId(toSegmentId);
					
					SegmentModel segmentModel = new SegmentModel();
					segmentModel.setSegmentId((fromSegmentId==0)?CommissionConstantsInterface.DEFAULT_SEGMENT_ID:fromSegmentId);
					workFlowWrapper.setSegmentModel(segmentModel);
					workFlowWrapper.setIsIvrResponse(isIvrResponse);

					
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());

					workFlowWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
					workFlowWrapper = commonCommandManager.getAccToAccInfo(workFlowWrapper);
					
					logger.info("[AccountToAccountCommand.execute] Going to execute Transaction flow. Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + 
							" Customer Mobile No:" + senderMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);

					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					txProcessingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount().toString();
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();

					workFlowWrapper.putObject("productTile",productModel.getName());
					commonCommandManager.sendSMS(workFlowWrapper);
				}
				else
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex){
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}catch(WorkFlowException wex){
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				throw new CommandException(wex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,wex);
			}catch(Exception ex){
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
		}else{
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}

	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		/*if (appUserModel == null) {
			appUserModel = new AppUserModel();
			appUserModel.setMobileNo(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE));
			try {
				appUserModel = this.getCommonCommandManager().loadAppUserByMobileByQuery(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE));
				ThreadLocalAppUser.setAppUserModel(appUserModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		
//		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
//		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
//		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
//		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
//		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		
		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		recipientMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		agentMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);

		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		isIvrResponse = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE) == null 
				? false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE));
		transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		
//		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
//		cvv = this.decryptPin(cvv);
//		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
//		tPin = StringUtil.replaceSpacesWithPlus(tPin);
//		tPin = this.decryptPin(tPin);
		
		logger.info("[AccountToAccountCommand.prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + 
    			" Product ID:" + productId + 
    			" deviceTypeId: " + deviceTypeId + 
    			(productId.equals("50000") ? " Receiver" : ""));
    
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
		bWrapper.setBasePersistableModel(retailerContactModel);
		
		try{
			bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
			
			this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
			
		}catch(Exception ex){
			logger.error("[AccountToAccountCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
		}
		
		try {
			fromSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(senderMobileNo);
		} catch (Exception e) {
			logger.error("[AccountToAccountCommand.prepare] Unable to load Sender Customer Segment info... ", e);
		}

		if ( ! isIvrResponse) {
			try {
				toSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(recipientMobileNo);
			} catch (Exception e) {
				logger.error("[AccountToAccountCommand.prepare] Unable to load Recipient Customer Segment info... ",e);
			}
		}

		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AccountToAccountCommand.prepare()");
		}
	}

	@Override
	public String response() {
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AccountToAccountCommand.validate()");
		}
		
		if (isIvrResponse) {
			validationErrors = ValidatorWrapper.doRequired(transactionId,validationErrors,"Transaction ID");
			if(!validationErrors.hasValidationErrors())
				validationErrors = ValidatorWrapper.doNumeric(transactionId,validationErrors,"Transaction ID");
			
			return validationErrors;
		}else{
			
			validationErrors = ValidatorWrapper.doRequired(recipientMobileNo,validationErrors,"Recipient Mobile No");//load from transaction model in execute
		}
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Processing Amount");
		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
		validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");
		validationErrors = ValidatorWrapper.doRequired(agentMobileNo,validationErrors,"Agent Mobile No");

		validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");

		if(!validationErrors.hasValidationErrors())
		{
			byte enc_type = new Byte(encryption_type).byteValue();
			ThreadLocalEncryptionType.setEncryptionType(enc_type);
		}	
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Processing Amount");
			validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AccountToAccountCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML(){
		
		// when isIvrResponse = true no response is needed
		if (isIvrResponse) {
			return "";
		}
		
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
		params.add(new LabelValueBean(ATTR_CMOB, replaceNullWithEmpty(senderMobileNo)));
		params.add(new LabelValueBean(ATTR_RCMOB, replaceNullWithEmpty(recipientMobileNo)));
		params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
		params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.LONG_DATE_FORMAT)));
		params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
		params.add(new LabelValueBean(ATTR_PROD, replaceNullWithEmpty(productModel.getName())));
		params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount()+"")));
		params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
		params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(txProcessingAmount)));
		params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(Double.parseDouble(txProcessingAmount))));
		params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
		params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
		params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
		params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
//		params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(workFlowWrapper.getSwitchWrapper().getAgentBalance())));
		
		return MiniXMLUtil.createResponseXMLByParams(params);
	
	}

}
