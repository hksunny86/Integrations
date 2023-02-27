package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.MiniBaseCommand;
import com.inov8.microbank.server.service.integration.vo.AccountToCashVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;



public class AccountToCashCommand extends MiniBaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected String accountId;
	protected String mobileNo;
	protected String txProcessingAmount;
	protected String pin;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String totalAmount;
	protected String txAmount;
	protected String cvv;
	protected String tPin;
	private String manualOTPin;

	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	protected double discountAmount = 0d;

	private String smsText = "";
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel smartMoneyAccountModel;
	UserDeviceAccountsModel userDeviceAccountsModel;
	
	private double customer1Balance;
	private String senderMobileNo;
	private Boolean isIvrResponse;
	private String transactionId;
	
//	added by mudassir
	String recepientWalkinCNIC;
	String recepientWalkinMobile;
	RetailerContactModel fromRetailerContactModel;
	private String encryption_type;
	private String transactionPurposeCode;
	
	protected final Log logger = LogFactory.getLog(AccountToCashCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AccountToCashCommand.execute()");
		}

		try{
        	RetailerContactModel retContactModel = new RetailerContactModel();
        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
        	
        	BaseWrapper bWrapper = new BaseWrapperImpl();
        	bWrapper.setBasePersistableModel(retContactModel);
        	CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        	bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

        	this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

        }catch(Exception ex){
        	logger.error("[CashDepositInfoCommand.execute] Unable to load RetailerContact info... " + ex.getStackTrace());
			throw new CommandException(this.getMessageSource().getMessage("6082", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        }
		
		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);		
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		
		if(appUserModel.getRetailerContactId() != null) //retailer condition added by Maqsood - for USSD CW flow
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
								
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
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
						senderMobileNo = txDetailMasterModel.getSaleMobileNo();//
						recepientWalkinCNIC = txDetailMasterModel.getRecipientCnic();
						recepientWalkinMobile = txDetailMasterModel.getRecipientMobileNo();
						baseWrapper.putObject( CommandFieldConstants.KEY_BILL_AMOUNT,  txDetailMasterModel.getTransactionAmount());
						baseWrapper.putObject( CommandFieldConstants.KEY_R_W_CNIC,  txDetailMasterModel.getRecipientCnic());
						baseWrapper.putObject( CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE,  txDetailMasterModel.getRecipientMobileNo());
						baseWrapper.putObject( CommandFieldConstants.KEY_TX_AMOUNT,  txDetailMasterModel.getTransactionAmount());

						if(txDetailMasterModel.getHandlerId() != null){
							this.getCommonCommandManager().loadHandlerData(txDetailMasterModel.getHandlerId(), workFlowWrapper);
						}

						txAmount = replaceNullWithZero(txDetailMasterModel.getTransactionAmount()).toString();
						totalAmount = replaceNullWithZero(txDetailMasterModel.getTotalAmount()).toString();
						txProcessingAmount = replaceNullWithZero(txDetailMasterModel.getExclusiveCharges()).toString();
						/*Generate Pin and store in MiniTransaction*/
						String pin = null;
						if(manualOTPin != null && ! "".equals(manualOTPin)){//if Pin is manually set by customer, use this.
							pin = manualOTPin;
						}else{//generate random pin.
							pin = CommonUtils.generateOneTimePin(5);
						}
						workFlowWrapper.setOneTimePin(pin);
						//Encode PIN
						String encryptedPin = EncoderUtils.encodeToSha(pin);
						workFlowWrapper.setMiniTransactionModel(populateMiniTransactionModel(encryptedPin));

						//Following check is to avoid duplicate processing of transaction(duplicate IVR calls) [11 July 2017]
						if(txDetailMasterModel.getSupProcessingStatusId() != SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING.longValue()){
							logger.error("This transaction is already processed via IVR. trxId:"+txDetailMasterModel.getTransactionCode()+" existing status:"+txDetailMasterModel.getSupProcessingStatusId());
							throw new CommandException("Transaction already processed",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}

					}else{
						//Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
						this.getCommonCommandManager().createOrUpdateWalkinCustomer(recepientWalkinCNIC, recepientWalkinMobile, null);
						List<AppUserModel> appUserModelList=new ArrayList<>();
						//appUserModelList=this.getCommonCommandManager().getAppUserManager().findAppUserByCnicAndMobile( recepientWalkinMobile,recepientWalkinCNIC);

						/*if(appUserModelList!=null && appUserModelList.size()==0) {
							this.getCommonCommandManager().createNewWalkinCustomer(recepientWalkinCNIC, recepientWalkinMobile, null);
						}*/
						workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
						workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
						workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
						workFlowWrapper.setBillAmount(Double.parseDouble(txAmount));
						workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
						workFlowWrapper.setHandlerModel(handlerModel);
						workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

					}
					
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("AccountToCashCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
						
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					
					AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel.setMobileNo(senderMobileNo);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_TX);
						
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
					workFlowWrapper.setCustomerAccount(customerAccount);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.setIsIvrResponse(isIvrResponse);
					workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
					workFlowWrapper.setSegmentModel(segmentModel);
					workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
					
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					
//					put walkin customer details in wrapper to populate in AccountToCashTransaction.doPreStart() from DB.
					workFlowWrapper.setRecipientWalkinCustomerModel(new WalkinCustomerModel(recepientWalkinCNIC));
					workFlowWrapper.setRecipientWalkinSmartMoneyAccountModel(new SmartMoneyAccountModel());
					//validate Agent PIN
/*					if( ! isIvrResponse){
						SwitchWrapper switchWrapper = commonCommandManager.verifyPIN(appUserModel, pin, workFlowWrapper);
//						workFlowWrapper = commonCommandManager.getAccountToCashInfo(workFlowWrapper);
					}
*/					
					StringBuilder logString = new StringBuilder();
					logString.append("[AccountToCashCommand.execute] ")
								.append(" Sender SmartMoneyAccountId : " + smartMoneyAccountModel.getSmartMoneyAccountId())
								.append(" Sender appUserId: " + appUserModel.getAppUserId());
								
					logger.info(logString.toString());
					workFlowWrapper.setCommissionSettledOnLeg2(true);
					TransactionPurposeModel transactionPurposeModel = new TransactionPurposeModel();
					transactionPurposeModel.setCode(transactionPurposeCode);
					List<TransactionPurposeModel> list = null;
					if(transactionPurposeCode != null && !transactionPurposeCode.equals(""))
						list = commonCommandManager.getTransactionPurposeDao().findByExample(transactionPurposeModel).getResultsetList();
					if(list != null && !list.isEmpty()){
						workFlowWrapper.putObject("TRANS_PURPOSE_MODEL",list.get(0));
					}

					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					discountAmount = workFlowWrapper.getDiscountAmount();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					txProcessingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount().toString();
					
					if(((AccountToCashVO)workFlowWrapper.getProductVO()).getBalance() !=null && ((AccountToCashVO)workFlowWrapper.getProductVO()).getBalance().doubleValue() != 0.0d){
						customer1Balance= ((AccountToCashVO)workFlowWrapper.getProductVO()).getBalance();
					}
					
					Double charges = workFlowWrapper.getTransactionModel().getTotalCommissionAmount();
					workFlowWrapper.putObject("productTile",productModel.getName());
					commonCommandManager.sendSMS(workFlowWrapper);
					
				}
				else
				{
					logger.error("[AccountToCashCommand.execute()] Exception occured in Validation for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId());
					
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
					logger.error("[AccountToCashCommand.execute()] Exception occured for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId() + " \n"  + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
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
					logger.error("[AccountToCashCommand.execute()] Exception occured for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId() + " \n"  + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
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
					logger.error("[AccountToCashCommand.execute()] Exception occured for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId() + " \n" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),Long.valueOf(ivrErrorCode),ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.info("[AccountToCashCommand.execute()] Invalid User Type. Throwing Exception for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId());
			throw new CommandException(this.getMessageSource().getMessage("AccountToCashCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AccountToCashCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AccountToCashCommand.prepare()");
		}
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
		transactionPurposeCode = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TRANS_PURPOSE_CODE);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);

		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		isIvrResponse = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE) == null ? false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE));
		transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		recepientWalkinCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
		recepientWalkinMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		
		try{
		
			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

			SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
//			smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
			smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
			searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
			searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
			smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
		
			searchBaseWrapper.putObject(CommandFieldConstants.KEY_HANDLER_MODEL, handlerModel);
			ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
			
			
			if(!validationErrors.hasValidationErrors())
			{
				accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
				this.smartMoneyAccountModel = smartMoneyAccountModel;
			}
			else{
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		
		}
		catch(Exception ex)
		{
			logger.error("[AccountToCashCommand.prepare] Exception: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex) + "\nFor Recipient CNIC:" + recepientWalkinCNIC + " RecepientMobileNo:"  +recepientWalkinMobile + " AppUSerID:" + appUserModel.getAppUserId());
			
			/*if(logger.isErrorEnabled())
			{
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}*/
//			ex.printStackTrace();
		}

		/**
		 * ------------------------End of Change------------------------------
		 */

		/*test by mudassir
		 try{
			
			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
			walkinCustomerModel = commonCommandManager.getWalkinCustomerModel(new WalkinCustomerModel(walkinCustomerCNIC));
		}
			catch(Exception ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				ex.printStackTrace();
			}
			
		wakinSmartMoneyAccountModel.setWalkinCustomerModel(walkinCustomerModel);
		
		searchBaseWrapper.setBasePersistableModel(wakinSmartMoneyAccountModel);
		
		searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
		wakinSmartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
	
		*/
		
		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		
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
		if(null != this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT) && !"".equals(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)))
		{
			discountAmount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT));
		}
		
		logger.info("[AccountToCashCommand.prepare] Recipient CNIC:" + recepientWalkinCNIC + " RecepientMobileNo:"  +recepientWalkinMobile + " Sender AppUserID:" + appUserModel.getAppUserId());
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AccountToCashCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of AccountToCashCommand.response()");
		}
		
		try
		{
			/*if(null != smsText && !"".equals(smsText))
			{
				sendSMSToUser(mobileNo, smsText);
			}*/
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AccountToCashCommand.validate()");
		}
		
		if (isIvrResponse) {
			validationErrors = ValidatorWrapper.doRequired(transactionId,validationErrors,"Transaction ID");
			if(!validationErrors.hasValidationErrors())
			{
				validationErrors = ValidatorWrapper.doNumeric(transactionId,validationErrors,"Transaction ID");
			}
		
		}else{
			
			if(accountId != null && accountId.length() > 0)
			{
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject(CommandFieldConstants.KEY_ACC_ID, accountId);
				CommonCommandManager commonCommandManager = this.getCommonCommandManager();

				if(commonCommandManager.isAccountValidationRequired(baseWrapper))
				{
					validationErrors = ValidatorWrapper.doRequired(accountNumber, validationErrors, "Account Number");
					validationErrors = ValidatorWrapper.doRequired(accountType, validationErrors, "Account Type");
					validationErrors = ValidatorWrapper.doRequired(accountCurrency, validationErrors, "Account Currency");
					validationErrors = ValidatorWrapper.doRequired(accountStatus, validationErrors, "Account Status");
				}

				if( pin != null && pin.equals("") )
				{
					validationErrors = ValidatorWrapper.doRequired(tPin, validationErrors, "T-Pin");		
				}


			}
			//validationErrors = ValidatorWrapper.doRequired(transactionPurposeCode,validationErrors,"Transaction Purpose");
			validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
			validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
			validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Amount");
			validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
			validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
			validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");

			if(!validationErrors.hasValidationErrors())
			{
				byte enc_type = new Byte(encryption_type).byteValue();
				ThreadLocalEncryptionType.setEncryptionType(enc_type);
			}
			
			if(!validationErrors.hasValidationErrors())
			{
				validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
				validationErrors = ValidatorWrapper.doInteger(accountId,validationErrors,"Account Id");	
				validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
				validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Amount");
			}
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AccountToCashCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AccountToCashCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		
		if (isIvrResponse == false) {
			Double totalCommission = Double.valueOf(txProcessingAmount);
			strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_TRANS)
					.append(TAG_SYMBOL_CLOSE)
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_TRN)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRXID)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_RWCNIC)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(recepientWalkinCNIC))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_CMOB)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(transactionModel.getSaleMobileNo())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					
					.append(ATTR_RWMOB)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(recepientWalkinMobile)
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					
					.append(ATTR_DATE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(transactionModel.getCreatedOn())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_DATEF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TIMEF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatTime(transactionModel.getCreatedOn()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_PROD)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(productModel.getName()))
					.append(TAG_SYMBOL_QUOTE)
						
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TPAM)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithZero(totalCommission))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TPAMF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatNumbers(totalCommission))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TAMT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithZero(transactionModel.getTotalAmount()))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TAMTF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatNumbers(transactionModel.getTotalAmount()))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TXAM)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithZero(transactionModel.getTransactionAmount()))
					.append(TAG_SYMBOL_QUOTE)
					
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TXAMF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatNumbers(transactionModel.getTransactionAmount()))
					.append(TAG_SYMBOL_QUOTE)
					;
					
				strBuilder.append(TAG_SYMBOL_CLOSE);
				
				strBuilder.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_TRN)
				.append(TAG_SYMBOL_CLOSE)
				.append(TAG_SYMBOL_OPEN)
				.append(TAG_SYMBOL_SLASH)
				.append(TAG_TRANS)
				.append(TAG_SYMBOL_CLOSE);
				if(logger.isDebugEnabled())
				{
					logger.debug("End of AccountToCashCommand.toXML()");
				}
		}
		
		return strBuilder.toString();
	}
	
	/**
	 * @param workFlowWrapper
	 */
	private MiniTransactionModel populateMiniTransactionModel(String encryptedPin) throws CommandException {

		MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
		
		try {
			miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_ACCOUNT_TO_CASH)) ;
			miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
			miniTransactionModel.setTimeDate(new Date()) ;
			miniTransactionModel.setMobileNo( recepientWalkinMobile );
			miniTransactionModel.setSmsText( recepientWalkinMobile + " " + txAmount ) ;
			miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT) ;
			miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
			miniTransactionModel.setOneTimePin(encryptedPin);//save pin in miniTransaction.
			miniTransactionModel.setCAMT(Double.parseDouble(StringUtil.isNullOrEmpty(commissionAmount)?"0":commissionAmount));
			miniTransactionModel.setBAMT(Double.parseDouble(StringUtil.isNullOrEmpty(txAmount)?"0":txAmount));
			miniTransactionModel.setTAMT(Double.parseDouble(StringUtil.isNullOrEmpty(totalAmount)?"0":totalAmount));
			miniTransactionModel.setTPAM(Double.parseDouble(StringUtil.isNullOrEmpty(txProcessingAmount)?"0":txProcessingAmount));
			
			if(manualOTPin != null && ! "".equals(manualOTPin)){//if Pin is manually set by customer, use this.
				miniTransactionModel.setIsManualOTPin(Boolean.TRUE);
			}
			
			
		} catch (Exception e) {
			
			if(logger.isErrorEnabled()){
				logger.error("[CashToCashCommand.populateMiniTransactionModel] Exception in populating MiniTransactionModel. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +". Details\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return miniTransactionModel;

	}

}