package com.inov8.microbank.server.service.commandmodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
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
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;



public class CnicToCoreAccountCommand extends BaseCommand {
	protected AppUserModel appUserModel;
	protected String productId;
	protected String accountId;
	protected String mobileNo;
	protected String txProcessingAmount;
	protected String pin;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String totalAmount;
	protected String billAmount;
	protected String cvv;
	protected String tPin;
	
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel smartMoneyAccountModel;
	UserDeviceAccountsModel userDeviceAccountsModel;
	
	private double agentBalance;
	RetailerContactModel fromRetailerContactModel;
	private String senderMobileNo;
	private String coreAccountNo;
	private String senderCnic;
	private String encryption_type;
	private String accountTitle;
	private String receiverMobileNo; //turab
	
	private String isBVSRequired;
	
	protected final Log logger = LogFactory.getLog(CnicToCoreAccountCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CnicToCoreAccountCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		if(appUserModel.getAppUserId() != null)//Agent AppUser ID
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					smartMoneyAccountModel =  new SmartMoneyAccountModel();
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					smartMoneyAccountModel.setDeleted(Boolean.FALSE);
					smartMoneyAccountModel.setActive(Boolean.TRUE);
					searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
					if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
						smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
					}
					
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
					accountInfoModel.setCustomerId(appUserModel.getAppUserId());
					accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
								
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					
						
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}

					logger.info("[CnicToCoreAccountCommand.execute] Checking if Walkin Customer is registered or not. Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " CNIC: " + senderCnic + " Customer Mobile No:" + senderMobileNo);

					//Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
					//this.getCommonCommandManager().createOrUpdateWalkinCustomer(senderCnic, senderMobileNo, null);
					//this.getCommonCommandManager().createNewWalkinCustomer(senderCnic, senderMobileNo, null);

					WalkinCustomerModel walkinSenderModel = new WalkinCustomerModel();
					walkinSenderModel.setMobileNumber(senderMobileNo);
						walkinSenderModel.setCnic(senderCnic);
					walkinSenderModel = this.getCommonCommandManager().getWalkinCustomerModel(walkinSenderModel);

					AppUserModel walkinCustomerAppUserModel = this.getCommonCommandManager().loadWalkinCustomerAppUserByCnic(senderCnic);

					Long segId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
					workFlowWrapper.setSegmentModel(new SegmentModel());
					workFlowWrapper.getSegmentModel().setSegmentId(segId);
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setSenderAppUserModel(walkinCustomerAppUserModel);
					workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
					workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
					workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
					workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
					workFlowWrapper.setSenderWalkinCustomerModel(walkinSenderModel);
				
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CNIC_TO_CORE_ACCOUNT_TX);
					workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
	
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
					workFlowWrapper.setCustomerAccount(customerAccount);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.setWalkInCustomerCNIC(senderCnic);
					workFlowWrapper.setWalkInCustomerMob(receiverMobileNo);
					
					boolean isBVS= isBVSRequired.equals("1")? true : false;
					workFlowWrapper.setSenderBvs(isBVS);
					
					logger.info("[CnicToCoreAccountCommand.execute] Going to execute Transaction flow. Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + 
							" Customer Mobile No:" + mobileNo);

					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();

					if (((BBToCoreVO)workFlowWrapper.getProductVO()).getSenderBalance() != null) {
						agentBalance = ((BBToCoreVO)workFlowWrapper.getProductVO()).getSenderBalance();
					}
					workFlowWrapper.putObject("productTile",productModel.getName());
					commonCommandManager.sendSMS(workFlowWrapper);
				}
				else
				{
					logger.error("[CnicToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount);
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CnicToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CnicToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CnicToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.error("[CnicToCoreAccountCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + billAmount + " Commission: " + commissionAmount);
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CnicToCoreAccountCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CnicToCoreAccountCommand.prepare()");
		}
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		
		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CORE_ACC_NO);
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		cvv = this.decryptPin(cvv);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		tPin = this.decryptPin(tPin);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
		coreAccountNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CORE_ACC_NO);
		senderCnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);

		accountTitle = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE);
		receiverMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		isBVSRequired = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_BVS_REQUIRED);

		try{
        	RetailerContactModel retContactModel = new RetailerContactModel();
        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
        	
        	BaseWrapper bWrapper = new BaseWrapperImpl();
        	bWrapper.setBasePersistableModel(retContactModel);
        	CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        	bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

        	this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

        }catch(Exception ex){
        	logger.error("[CnicToCoreAccountCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
        }
		
		logger.info("[CnicToCoreAccountCommand.prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + 
    			" Product ID:" + productId + " AccountId: " + accountId + 
    			" deviceTypeId: " + deviceTypeId + 
    			(productId.equals("50000") ? " Receiver" : "") + " MobileNumber: " + mobileNo);
    
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CnicToCoreAccountCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CnicToCoreAccountCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CnicToCoreAccountCommand.validate()");
		}
		
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(accountNumber,validationErrors,"Account Number");
		validationErrors = ValidatorWrapper.doRequired(senderMobileNo,validationErrors," Sender Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
		validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
		validationErrors = ValidatorWrapper.doRequired(billAmount,validationErrors,"Bill Amount");
		validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");
		validationErrors = ValidatorWrapper.doValidateCNIC(senderCnic , validationErrors, "Sender CNIC");

		if(!validationErrors.hasValidationErrors())
		{
			byte enc_type = new Byte(encryption_type).byteValue();
			ThreadLocalEncryptionType.setEncryptionType(enc_type);
		}
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(accountNumber,validationErrors,"Account Number");	
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Amount");
		}
		//**********************************************************************************************************************	
		//									Check if receiver cnic is blacklisted
		//**********************************************************************************************************************
		if(!validationErrors.hasValidationErrors()) {
			if(this.getCommonCommandManager().isCnicBlacklisted(senderCnic)) {
				validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.TERMINATE_EXECUTION_FLOW,ErrorLevel.MEDIUM,new Throwable());
			}
		}		
		//**********************************************************************************************************************

		if(logger.isDebugEnabled())
		{
			logger.debug("End of CnicToCoreAccountCommand.validate()");
		}
		return validationErrors;
	}

	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CnicToCoreAccountCommand.toXML()");
		}
		
		StringBuilder strBuilder = new StringBuilder();

		String CAMT=Formatter.formatDouble(transactionModel.getTotalCommissionAmount());
		
		String TPAM=txProcessingAmount;
		String agent1Balance=Formatter.formatDouble(agentBalance);

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
		.append(ATTR_SWMOB)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(replaceNullWithEmpty(transactionModel.getNotificationMobileNo()))
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_COREACID)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(coreAccountNo)
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
		.append(ATTR_CAMT)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(CAMT)
		.append(TAG_SYMBOL_QUOTE)
				
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_CAMTF)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(Formatter.formatNumbers(transactionModel.getTotalCommissionAmount()))
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_TPAM)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(TPAM)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_TPAMF)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(Formatter.formatNumbers(Double.parseDouble(txProcessingAmount)))
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
		.append(ATTR_SWCNIC)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(replaceNullWithEmpty(senderCnic))
		.append(TAG_SYMBOL_QUOTE)

		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_TXAMF)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(Formatter.formatNumbers(transactionModel.getTransactionAmount()))
		.append(TAG_SYMBOL_QUOTE)		
		
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_BALF)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(agent1Balance)
		.append(TAG_SYMBOL_QUOTE)		
//		.append(TAG_SYMBOL_CLOSE)	
		
		.append(TAG_SYMBOL_SPACE)
		.append(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(StringEscapeUtils.escapeXml(accountTitle))
		.append(TAG_SYMBOL_QUOTE)		
		.append(TAG_SYMBOL_CLOSE)		
		
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_TRN)
		.append(TAG_SYMBOL_CLOSE)
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_TRANS)
		.append(TAG_SYMBOL_CLOSE);
		

		return strBuilder.toString();
	}
}