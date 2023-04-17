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
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;



public class CnicToBBAccountCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected String accountId;
	protected String txProcessingAmount;
	protected String pin;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String totalAmount;
	protected String cvv;
	protected String tPin;
	
	protected String accountType;
	protected String accountCurrency;
	protected String accountStatus;
	protected String accountNumber;
	protected double discountAmount = 0d;
	
	
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel smartMoneyAccountModel;
	UserDeviceAccountsModel userDeviceAccountsModel; 
	WorkFlowWrapper workFlowWrapper;
	String ussdPin;
	String walkInCustomerCNIC;
	
	RetailerContactModel fromRetailerContactModel;
	long segmentId;

	protected String senderMobileNo;
	protected String recipientMobileNo;
	protected String agentMobileNo;
	protected String txAmount;
	private String coreAccountTitle;

    private String isBVSRequired;
	
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
	double balance=0D;
	private String transactionPurposeCode;
	
	protected final Log logger = LogFactory.getLog(CnicToBBAccountCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CnicToBBAccountCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		if(appUserModel.getRetailerContactId() != null)
		{
			try
			{
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
//				bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
				bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segmentId));
				boolean result = commonCommandManager.checkVelocityCondition(bWrapper);
				// Velocity validation - end

				
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
								
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));

					//Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
					//this.getCommonCommandManager().createOrUpdateWalkinCustomer(walkInCustomerCNIC, senderMobileNo, null);
					//this.getCommonCommandManager().createNewWalkinCustomer(walkInCustomerCNIC, senderMobileNo, null);

					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
						
					AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel.setMobileNo(recipientMobileNo);
					customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CNIC_TO_BB_ACCOUNT_TX);
						
					smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
			
					
					
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
					workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
					workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
					workFlowWrapper.setFromRetailerContactModel(fromRetailerContactModel);
					workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
					workFlowWrapper.setToSegmentId(segmentId);
					
					
					
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					workFlowWrapper.setWalkInCustomerCNIC(walkInCustomerCNIC);
					workFlowWrapper.setWalkInCustomerMob(senderMobileNo);
					
					logger.info("[CnicToBBAccountCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + 
								" Customer Mobile No:" + recipientMobileNo + " WalkInCustomerMobileNumber:" + senderMobileNo + 
								" Trx Amount: " + txAmount + " Commission: " + commissionAmount);

                    boolean isBVS = isBVSRequired.equals("1") ? true : false;
                    workFlowWrapper.setSenderBvs(isBVS);
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
					
					logger.info(workFlowWrapper.getSwitchWrapper().getAgentBalance()+"-"+workFlowWrapper.getSwitchWrapper().getBalance());

					commonCommandManager.sendSMS(workFlowWrapper);
				}
				else
				{
					logger.error("[CnicToBBAccountCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + recipientMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CnicToBBAccountCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + recipientMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CnicToBBAccountCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + recipientMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				
				ex.printStackTrace();
				
				if(logger.isErrorEnabled())
				{
					logger.error("[CnicToBBAccountCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + recipientMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.error("[CnicToBBAccountCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + recipientMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CnicToBBAccountCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{

		this.baseWrapper = baseWrapper;
		transactionPurposeCode = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TRANS_PURPOSE_CODE);
        isBVSRequired = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_BVS_REQUIRED);
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		
		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		
		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
		walkInCustomerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
		recipientMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		agentMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		cvv = this.decryptPin(cvv);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		tPin = this.decryptPin(tPin);
		
		//Maqsood Shahzad for USSD cash deposit.
		
		ussdPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		//End changes for USSD cash deposit
		
		
		logger.info("[CnicToBBAccountCommand.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + recipientMobileNo + " Walkin Customer Mobile No:" + senderMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);

		if(null != this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT) && !"".equals(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)))
		{
			discountAmount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT));
		}
		
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
		bWrapper.setBasePersistableModel(retailerContactModel);
		
		try{
			bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
			
			this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
			
		}catch(Exception ex){
			logger.error("[CnicToBBAccountCommand.prepare] Unable to load RetailerContact info... ",ex);
		}
		
		try {
			segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(recipientMobileNo);
		} catch (Exception e) {
			logger.error("[CnicToBBAccountCommand.prepare] Unable to load Customer Segment info... ",e);
		}
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CnicToBBAccountCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CnicToBBAccountCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(com.inov8.microbank.common.util.ValidationErrors validationErrors) throws CommandException
	{
//		if(accountId != null && accountId.length() > 0)
//		{
//			BaseWrapper baseWrapper = new BaseWrapperImpl();
//			baseWrapper.putObject(CommandFieldConstants.KEY_ACC_ID, accountId);
//			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
//			
//			if(commonCommandManager.checkTPin(baseWrapper))
//			{
//				validationErrors = ValidatorWrapper.doRequired(tPin, validationErrors, "TPIN");
//				if(validationErrors.hasValidationErrors())
//				{
//					ValidationErrors valErrors=new ValidationErrors();
//					valErrors.getStringBuilder().append("Transaction cannot be processed on this version of Microbank. Please download the new version from http://www.microbank.inov8.com.pk to be able to make transactions.");
//					return valErrors;	
//				}
//			}
			
			
//			if(commonCommandManager.isAccountValidationRequired(baseWrapper))
//			{
//				validationErrors = ValidatorWrapper.doRequired(accountNumber, validationErrors, "Account Number");
//				validationErrors = ValidatorWrapper.doRequired(accountType, validationErrors, "Account Type");
//				validationErrors = ValidatorWrapper.doRequired(accountCurrency, validationErrors, "Account Currency");
//				validationErrors = ValidatorWrapper.doRequired(accountStatus, validationErrors, "Account Status");
//			}
//			else if( tPin != null && tPin.equals("") )
//			{
//				validationErrors = ValidatorWrapper.doRequired(pin, validationErrors, "Pin");		
//			}
//			else if( pin != null && pin.equals("") )
//			{
//				validationErrors = ValidatorWrapper.doRequired(tPin, validationErrors, "T-Pin");		
//			}
//			
//			
//		}
		//validationErrors = ValidatorWrapper.doRequired(transactionPurposeCode,validationErrors,"Transaction Purpose");
		validationErrors = ValidatorWrapper.doValidateCNIC(walkInCustomerCNIC, validationErrors, "Walkin Customer CNIC");
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
//		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
//		validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Pin");
//		validationErrors = ValidatorWrapper.doRequired(mobileNo,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Processing Amount");
		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
		validationErrors = ValidatorWrapper.doRequired(commissionAmount,validationErrors,"Commission Amount");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");
		validationErrors = ValidatorWrapper.doRequired(recipientMobileNo,validationErrors,"Recipient Mobile No");
		validationErrors = ValidatorWrapper.doRequired(agentMobileNo,validationErrors,"Agent Mobile No");

		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
//			validationErrors = ValidatorWrapper.doInteger(accountId,validationErrors,"Account Id");	
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Processing Amount");
		}
		//********************************************************************************************
		//						Check if receiver cnic is blacklisted								//			
		//********************************************************************************************
        if (!validationErrors.hasValidationErrors()) {	
            if (this.getCommonCommandManager().isCnicBlacklisted(walkInCustomerCNIC)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
		//********************************************************************************************
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CnicToBBAccountCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
		params.add(new LabelValueBean(ATTR_SWCNIC, replaceNullWithEmpty(walkInCustomerCNIC)));
		params.add(new LabelValueBean(ATTR_SWMOB, replaceNullWithEmpty(senderMobileNo)));
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
		params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(workFlowWrapper.getSwitchWrapper().getAgentBalance())));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, StringEscapeUtils.escapeXml(this.coreAccountTitle)));
		
		return MiniXMLUtil.createResponseXMLByParams(params);
	
	}
    
}