package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_BALF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CAMT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CAMTF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CMOB;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CNIC;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PROD;
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
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
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
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.MiniBaseCommand;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.RetailPaymentVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;



public class AgentRetailPaymentCommand extends MiniBaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected String deviceTypeId;
	protected String customerMobileNo;
	protected String txProcessingAmount;
	protected String commissionAmount;
	protected String totalAmount;
	protected String txAmount;
	protected String pin;
	protected String cvv;
	protected String tPin;
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel smartMoneyAccountModel;
	UserDeviceAccountsModel userDeviceAccountsModel;
	
	RetailerContactModel fromRetailerContactModel;
	long segmentId;
	double agentBalance = 0.0;
	private Boolean isIvrResponse;
	private String transactionId;
	private String encryption_type;
	
	protected final Log logger = LogFactory.getLog(AgentRetailPaymentCommand.class);
	
	@Override
	public void execute() throws CommandException
	{

		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		smartMoneyAccountModel = new SmartMoneyAccountModel();

		if(appUserModel.getRetailerContactId() != null) {
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					if(isIvrResponse){
						TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
						transactionCodeModel.setCode(transactionId);
						workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
						TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel();
						txDetailMasterModel.setTransactionCode(transactionId);
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(txDetailMasterModel);
						searchBaseWrapper = this.getCommonCommandManager().loadTransactionDetailMaster(searchBaseWrapper);
						txDetailMasterModel = (TransactionDetailMasterModel)searchBaseWrapper.getBasePersistableModel();
						workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);
						// TODO need to verify senderMobileNo = txDetailMasterModel.getSaleMobileNo();//
						txAmount = txDetailMasterModel.getTransactionAmount().toString();
						baseWrapper.putObject( CommandFieldConstants.KEY_TX_AMOUNT,  txDetailMasterModel.getTransactionAmount());

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
						workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
						workFlowWrapper.setBillAmount(Double.parseDouble(txAmount));
						workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
						workFlowWrapper.setHandlerModel(handlerModel);
						workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

					}

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

					bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segmentId));
					boolean result = commonCommandManager.checkVelocityCondition(bWrapper);
					// Velocity validation - end

					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);

					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("AgentRetailPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
						
					AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel.setMobileNo(customerMobileNo);
					
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.AGENT_RETAIL_PAYMENT_TX);
						
					//Agent smart money account
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					smartMoneyAccountModel.setActive(Boolean.TRUE);
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
					
					@SuppressWarnings("rawtypes")
					CustomList smaList = searchBaseWrapper.getCustomList();
					if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
						smartMoneyAccountModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
					}
			
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
//					already checked above
//					if( ! isIvrResponse) {
//						verifyPIN(smartMoneyAccountModel, pin);
//					}
					
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setBillAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.setIsIvrResponse(isIvrResponse);
					
					workFlowWrapper.setFromRetailerContactModel(fromRetailerContactModel);
					workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
					workFlowWrapper.setToSegmentId(segmentId);
					
					SegmentModel segmentModel = new SegmentModel();
					segmentModel.setSegmentId(segmentId);
					workFlowWrapper.setSegmentModel(segmentModel);

					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					
					StringBuilder logString = new StringBuilder();
					logString.append("[AgentRetailPaymentCommand.execute] Agent appUserId: " + appUserModel.getAppUserId());
								
					logger.info(logString.toString());
					
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();

					commonCommandManager.sendSMS(workFlowWrapper);
				}
				else
				{
					logger.error("[AgentRetailPaymentCommand.execute()] Exception occured in Validation for appUserID:" + appUserModel.getAppUserId());
					
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				
				if(logger.isErrorEnabled())
				{
					logger.error("[AgentRetailPaymentCommand.execute()] Exception occured for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId() + " \n"  + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				
				if(logger.isErrorEnabled())
				{
					logger.error("[AgentRetailPaymentCommand.execute()]  Exception occured for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId() + " \n"  + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				throw new CommandException(wex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				if(isIvrResponse){
					ivrErrorCode = workFlowWrapper.getErrorCode();
					if (StringUtils.isEmpty(ivrErrorCode)) {
						ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
					}
				}else{
					ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
				}
				
				if(logger.isErrorEnabled())
				{
					logger.error("[AgentRetailPaymentCommand.execute()] Exception occured for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId() + " \n" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.info("[AgentRetailPaymentCommand.execute()] Invalid User Type. Throwing Exception for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId());
			throw new CommandException(this.getMessageSource().getMessage("AgentRetailPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AgentRetailPaymentCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
	
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		
		customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		cvv = this.decryptPin(cvv);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		tPin = this.decryptPin(tPin);
		isIvrResponse = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE) == null ? false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE));
		transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		BaseWrapper bWrapper = new BaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
		bWrapper.setBasePersistableModel(retailerContactModel);
		
		try{
			bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
			
			this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
			
		}catch(Exception ex){
			logger.error("[CnicToBBAccountCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
		}
		
		try {
			segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(customerMobileNo);
		} catch (Exception e) {
			logger.error("[CnicToBBAccountCommand.prepare] Unable to load Customer Segment info... ",e);
		}
		
	}

	@Override
	public String response() 
	{
	
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if (isIvrResponse) {
			validationErrors = ValidatorWrapper.doRequired(transactionId,validationErrors,"Transaction ID");
			if(!validationErrors.hasValidationErrors())
				validationErrors = ValidatorWrapper.doNumeric(transactionId,validationErrors,"Transaction ID");
			
			return validationErrors;
		}
		
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
		validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
		if(!validationErrors.hasValidationErrors())
		{
			byte enc_type = new Byte(encryption_type).byteValue();
			ThreadLocalEncryptionType.setEncryptionType(enc_type);
		}
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Amount");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(isIvrResponse){
			return "";
		}
		
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
		params.add(new LabelValueBean(ATTR_CMOB, replaceNullWithEmpty(customerMobileNo)));
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
		
		return MiniXMLUtil.createResponseXMLByParams(params);
	
	}

}