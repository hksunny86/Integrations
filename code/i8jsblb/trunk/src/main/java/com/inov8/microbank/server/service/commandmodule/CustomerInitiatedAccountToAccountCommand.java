package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
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
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class CustomerInitiatedAccountToAccountCommand extends BaseCommand  {
	private AppUserModel appUserModel;
	private String productId;
	private String txProcessingAmount;
	private String deviceTypeId;
	private String commissionAmount;
	private String totalAmount;
	
	private String senderMobileNo;
	private String recipientMobileNo, recipientTitle;
	private String txAmount;
	private double senderCustomerBalance = 0.0D;
	
	private TransactionModel transactionModel;
	private ProductModel productModel;
	private BaseWrapper baseWrapper;
	private SmartMoneyAccountModel smartMoneyAccountModel;
	
	private long fromSegmentId;
	private long toSegmentId;
	private String thirdPartyTransactionId;
	private String terminalId;
	private String channelId;
	private String stan;
	private AppUserModel receiverAppUserModel,senderAppUserModel;

	WorkFlowWrapper workFlowWrapper;
	
	private final Log logger = LogFactory.getLog(CustomerInitiatedAccountToAccountCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		logger.info("Start of CustomerInitiatedAccountToAccountCommand.execute()");
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();

		if(appUserModel.getCustomerId() != null)
		{
			try
			{
				CustomerModel customerModel = new CustomerModel();
				customerModel = commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationErrors.hasValidationErrors())
				{
					String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
					// Velocity validation - start
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
					bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
					bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, -1L);
					bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, -1L);
					bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(txAmount));
					bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(fromSegmentId));
					bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, customerModel.getCustomerAccountTypeId());
					bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//					bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
					commonCommandManager.checkVelocityCondition(bWrapper);
					// Velocity validation - end

					AccountInfoModel accountInfoModel = new AccountInfoModel();
					//accountInfoModel.setOldPin(pin);
					
					
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					
					AppUserModel receiverAppUserModel = new AppUserModel();
					receiverAppUserModel.setMobileNo(recipientMobileNo);
					receiverAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(recipientMobileNo, UserTypeConstantsInterface.CUSTOMER);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_ACCOUT_TO_ACCOUNT_TX);
					
					SegmentModel segmentModel = new SegmentModel();
					segmentModel.setSegmentId((fromSegmentId==0)?CommissionConstantsInterface.DEFAULT_SEGMENT_ID:fromSegmentId);

					smartMoneyAccountModel = new SmartMoneyAccountModel();
			
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(appUserModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setReceiverAppUserModel(receiverAppUserModel);
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setFromSegmentId(fromSegmentId);
					workFlowWrapper.setToSegmentId(toSegmentId);
					workFlowWrapper.setSegmentModel(segmentModel);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
					workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
					workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setIsCustomerInitiatedTransaction(true);
					workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE,thirdPartyTransactionId);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,channelId);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID,terminalId);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.WEB_SERVICE);
					workFlowWrapper = commonCommandManager.getAccToAccInfo(workFlowWrapper);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, channelId);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, recipientMobileNo);
					if(deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
						workFlowWrapper.setCommissionSettledOnLeg2(true);
					}
					if(!deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                        workFlowWrapper.putObject(CommandFieldConstants.KEY_RECIEVER_ACCOUNT_TITLE, receiverAppUserModel.getFirstName() + " " + receiverAppUserModel.getLastName());
					}
					logger.info("[CustomerInitiatedAccountToAccountCommand.execute] Going to execute Transaction flow. Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + 
							"Sender Customer Mobile No:" + senderMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);


					SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
					sma.setCustomerId(appUserModel.getCustomerId());

					sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

					SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);
					commonCommandManager.validateBalance(appUserModel, smartMoneyAccountModel, Double.valueOf(totalAmount), true);

					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

					transactionModel = workFlowWrapper.getTransactionModel();
					txProcessingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount().toString();
					productModel = workFlowWrapper.getProductModel();
					senderCustomerBalance = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction()); // Sender balance
					
					workFlowWrapper.putObject("RCMobileNo",true);
					commonCommandManager.sendSMS(workFlowWrapper);
				}
				else
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex){
				if (ex.getMessage() != null && ex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.RECIPIENT_MOBILE_NUMBER_MESSAGE)) {
					ivrErrorCode = String.valueOf(8090L);
					throw new CommandException(ex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, ex);
				}
				else {
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					throw new CommandException(ex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, ex);
				}
			}catch(WorkFlowException wex){
				if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.DAILY_DEBIT_LIMIT_MESSAGE)) {
					ivrErrorCode = String.valueOf(8062L);
					throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
				}
				else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.MONTHLY_DEBIT_LIMIT_MESSAGE)) {
					ivrErrorCode = String.valueOf(8064L);
					throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
				}
				else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.MONTHLY_CREDIT_LIMIT_MESSAGE)) {
					ivrErrorCode = String.valueOf(8063L);
					throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
				}
				else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.DAILY_CREDIT_LIMIT_MESSAGE)) {
					ivrErrorCode = String.valueOf(8061L);
					throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
				}
				else {
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
				}
			}catch(Exception ex){
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
		}else{
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
			logger.info("End of CustomerInitiatedAccountToAccountCommand.execute()");
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		
		logger.info("Start of CustomerInitiatedAccountToAccountCommand.prepare()");
		
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
		channelId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID);
		terminalId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TERMINAL_ID);
		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		recipientMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		recipientTitle = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECIEVER_ACCOUNT_TITLE);

		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		stan=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_STAN);
		
		logger.info("[CustomerInitiatedAccountToAccountCommand.prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + 
    			" Product ID:" + productId + 
    			" deviceTypeId: " + deviceTypeId);
    
		
		try {
			fromSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(senderMobileNo);
			receiverAppUserModel = commonCommandManager.loadAppUserByQuery(recipientMobileNo, UserTypeConstantsInterface.RETAILER);

		} catch (Exception e) {
			logger.error("[CustomerInitiatedAccountToAccountCommand.prepare] Unable to load Sender Customer Segment info... ", e);
		}

			try {
				if(!deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
					toSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(recipientMobileNo);
				}
			} catch (Exception e) {
				logger.error("[CustomerInitiatedAccountToAccountCommand.prepare] Unable to load Recipient Customer Segment info... ",e);
			}
		
			logger.info("End of CustomerInitiatedAccountToAccountCommand.prepare()");
	}

	@Override
	public String response() {
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		logger.info("Start of CustomerInitiatedAccountToAccountCommand.validate()");

		String classNMethodName = getClass().getSimpleName() + ".validate(): ";
		logger.info("Start of "+classNMethodName);

		validationErrors = ValidatorWrapper.doRequired(recipientMobileNo,validationErrors,"Recipient Mobile No");//load from transaction model in execute
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Processing Amount");
		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
		validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");

		String error = "";
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Processing Amount");
			validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");

			if(!validationErrors.hasValidationErrors()){
				if(null != receiverAppUserModel){
					error = this.getMessageSource().getMessage("MONEYTRANSFER.AGENT_MOB_REGISTERED", null,null);
					logger.error(classNMethodName + error);
					ValidatorWrapper.addError(validationErrors, error);
				}
			}
		}
		
		logger.info("End of CustomerInitiatedAccountToAccountCommand.validate()");
		
		return validationErrors;
	}
	
	private String toXML(){

		logger.info("Start of CustomerInitiatedAccountToAccountCommand.toXML()");
		
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
		params.add(new LabelValueBean(ATTR_CMOB, replaceNullWithEmpty(senderMobileNo)));
		params.add(new LabelValueBean(ATTR_RCMOB, replaceNullWithEmpty(recipientMobileNo)));
		params.add(new LabelValueBean(ATTR_RCNAME, replaceNullWithEmpty(recipientTitle)));
		params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
		params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));
		params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
		params.add(new LabelValueBean(ATTR_PROD, replaceNullWithEmpty(productModel.getDescription())));
//		params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount()+"")));
//		params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
		params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(workFlowWrapper.getCommissionAmountsHolder().getTotalCommissionAmount()+"")));
		params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(workFlowWrapper.getCommissionAmountsHolder().getTotalCommissionAmount())));
		params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(txProcessingAmount)));
		params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(Double.parseDouble(txProcessingAmount))));
		params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
		params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
		params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
		params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
		params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(senderCustomerBalance)));
		
		logger.info("End of CustomerInitiatedAccountToAccountCommand.toXML()");
		
		return MiniXMLUtil.createResponseXMLByParams(params);
	
	}

}
