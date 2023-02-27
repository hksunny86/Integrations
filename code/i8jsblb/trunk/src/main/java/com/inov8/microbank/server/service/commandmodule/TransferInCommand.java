package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.StringUtil.isNullOrEmpty;

import java.text.SimpleDateFormat;
import java.util.List;

import com.inov8.microbank.common.model.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalEncryptionType;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.integration.vo.TransferInVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

public class TransferInCommand extends BaseCommand  {
	
	protected final Log logger = LogFactory.getLog(TransferInCommand.class);

	private String pin;
	private String encryption_type;
	private String productId;
	private String amount;

	private SmartMoneyAccountModel coreSmartMoneyAccountModel;
	private SmartMoneyAccountModel olaSmartMoneyAccountModel;
	
	private String accountId;
	private String mobileNo;
	private String totalAmount;
	private String deviceTypeId;
	private String commissionAmount;
	private String txProcessingAmount;
	private String cvv;
	private String tPin;
	private String ussdPin;
	private String successMessage;
	private String walkInCustomerCNIC;
	private String walkInCustomerMobileNumber;
	private String accountType;
	private String accountCurrency;
	private String accountStatus;
	private String accountNumber;
	private Double discountAmount = 0D;
	private Double balance=0D;
	private String consumerNo;
	private String transactionAmount;
	
	private AppUserModel appUserModel;
	private TransactionModel transactionModel;
	private ProductModel productModel;
	private BaseWrapper baseWrapper;
	private UserDeviceAccountsModel userDeviceAccountsModel; 
	private WorkFlowWrapper workFlowWrapper;
	
	private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");	
	private Long reasonId = 5L;
	private String bankId = null;
	
	@Override
	public void execute() throws CommandException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of TransferInCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
					
		try {
				
			ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
			
			if(!validationErrors.hasValidationErrors()) {

				AccountInfoModel accountInfoModel = new AccountInfoModel();
				accountInfoModel.setOldPin(pin);
								
				productModel = new ProductModel();
				productModel.setProductId(Long.parseLong(productId));
					
				TransferInVO transferInVO = (TransferInVO) commonCommandManager.loadProductVO(baseWrapper);
					
				if(transferInVO == null) {
					
					throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				} else {
//					transferInVO.setProductModel((ProductModel)baseWrapper.getBasePersistableModel());
					productModel = (ProductModel)baseWrapper.getBasePersistableModel();
				}

				TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
				transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.TRANSFER_IN_TX);
					
				DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
				deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
				BankModel bankModel = new BankModel();
				bankModel.setBankId(Long.valueOf(bankId));
								
//				Double commissionAmount = getCommonCommandManager().getCommissionRate(productModel);
//				Double totalAmount = Double.parseDouble(amount) + commissionAmount.doubleValue();
				
				RetailerContactModel retailerContactModel = new RetailerContactModel();
				retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(retailerContactModel);
				baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
				retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
				RetailerModel retailerModel = loadRetailerModel(retailerContactModel.getRetailerId());
				DistributorModel distributorModel = loadDistributorModel(retailerModel.getDistributorId());

				accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				
				WORK_FLOW_WRAPPER_PARAMS : {

					workFlowWrapper.setRetailerContactModel(retailerContactModel);
					workFlowWrapper.setDistributorModel(distributorModel);
					workFlowWrapper.setRetailerModel(retailerModel);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(coreSmartMoneyAccountModel);
					workFlowWrapper.setOlaSmartMoneyAccountModel(olaSmartMoneyAccountModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setProductVO(transferInVO);
//					workFlowWrapper.setTxProcessingAmount(Double.parseDouble(amount));
					workFlowWrapper.setTransactionAmount(Double.parseDouble(transactionAmount));
//					workFlowWrapper.setTotalCommissionAmount(commissionAmount);
					workFlowWrapper.setBillAmount(Double.parseDouble(amount));
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
					workFlowWrapper.setCcCVV(this.cvv);
					workFlowWrapper.setMPin(this.tPin);
					workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					workFlowWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
					workFlowWrapper.putObject("reasonId", reasonId);
					workFlowWrapper.setBankModel(bankModel);

						
					logger.info("[TransferInCommand.execute] \nProduct ID: " + productId + " \nLogged In AppUserID:" + appUserModel.getAppUserId() + 
								"\n Trx Amount: " + Double.parseDouble(amount) + "\n Commission: " + commissionAmount);
				}

					
				workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
				transactionModel = workFlowWrapper.getTransactionModel();
				productModel = workFlowWrapper.getProductModel();
				userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
				discountAmount = workFlowWrapper.getDiscountAmount();
				successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
//				smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
				logger.info(workFlowWrapper.getSwitchWrapper().getAgentBalance()+"-"+workFlowWrapper.getSwitchWrapper().getBalance());

				balance = workFlowWrapper.getSwitchWrapper().getBalance();
				balance = workFlowWrapper.getSwitchWrapper().getAgentBalance();
					
				} else {
					logger.error("[TransferInCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + amount + " Commission: " + commissionAmount);
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			workFlowWrapper.putObject("productTile",productModel.getName());
				commonCommandManager.sendSMS(workFlowWrapper);
				
			} catch(FrameworkCheckedException ex) {
				
				if(logger.isErrorEnabled()) {
					logger.error("[TransferInCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + amount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex) {
				wex.printStackTrace();
				if(logger.isErrorEnabled()) {
					
					logger.error("[TransferInCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + amount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			
			} catch(Exception ex) {
				ex.printStackTrace();
				if(logger.isErrorEnabled()) {
					logger.error("[TransferInCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + amount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}

		
		if(logger.isDebugEnabled()) {
			logger.debug("End of TransferInCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of TransferInCommand.prepare()");
		}
		
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);	
		reasonId = super.getCommonCommandManager().getReasonType(Long.valueOf(productId));
		bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		
		try {
			
			if(appUserModel == null || appUserModel.getRetailerContactId() == null) {

				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		
			this.olaSmartMoneyAccountModel = getSmartMoneyAccountModel(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			
			this.coreSmartMoneyAccountModel = getSmartMoneyAccountModel(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
			
		} catch(Exception ex) {
			
			if(logger.isErrorEnabled()) {
				
				logger.error("TransferInCommand.prepare] Exception occured for ProductID: " + productId + " AppUserID: " + appUserModel.getAppUserId() + " Exception Details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			
			logger.error(ex);
		}
		
		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
		
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
//		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		amount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		cvv = this.decryptPin(cvv);
		tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
		tPin = StringUtil.replaceSpacesWithPlus(tPin);
		tPin = this.decryptPin(tPin);
		
		//Maqsood Shahzad for USSD cash deposit.
		
		ussdPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		walkInCustomerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
		walkInCustomerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE);
		//End changes for USSD cash deposit
		
		logger.info("[TransferInCommand.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Walkin Customer Mobile No:" + walkInCustomerMobileNumber + " Trx Amount: " + amount + " Commission: " + commissionAmount);

		if(!isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)) && 
				!isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT))) {
			
			discountAmount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("End of TransferInCommand.prepare()");
		}
	}
	

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of TransferInCommand.validate()");
		}
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(amount,validationErrors,"Amount");
				
		if(!validationErrors.hasValidationErrors()) {
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(amount, validationErrors, "Tx Amount");
		}
		
		validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");
        
        if(!validationErrors.hasValidationErrors()) {
             byte enc_type = new Byte(encryption_type).byteValue();
             ThreadLocalEncryptionType.setEncryptionType(enc_type);
        }
		
		if(logger.isDebugEnabled()) {
			logger.debug("End of TransferInCommand.validate()");
		}
		
		return validationErrors;
	}
	
	
	/**
	 * @param appUserModel
	 * @param bankPin
	 * @param transactionCodeModel
	 * @param productModel
	 * @param fetchTitle
	 * @throws FrameworkCheckedException
	 */
	/*protected void verifyPIN(AppUserModel appUserModel, String bankPin) throws FrameworkCheckedException {
		
		logger.info("AllPayBillPaymentCommand.Verify Bank PIN > APP USER " + appUserModel.getUsername() + ". RetailerContactId:" + appUserModel.getRetailerContactId());
	    
		SwitchWrapper switchWrapper = new SwitchWrapperImpl() ;
	    switchWrapper.setSmartMoneyAccountModel(this.olaSmartMoneyAccountModel);
	    
		try {

		    BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
		    baseWrapperTemp.setBasePersistableModel(this.olaSmartMoneyAccountModel);
		    
			AbstractFinancialInstitution abstractFinancialInstitution = getCommonCommandManager().loadFinancialInstitution(baseWrapperTemp);
		    switchWrapper.putObject(CommandFieldConstants.KEY_PIN , bankPin) ;
//		    switchWrapper.putObject(CommandFieldConstants.KEY_PIN , MfsWebUtil.encryptPin(bankPin)) ;
		    
			switchWrapper = abstractFinancialInstitution.verifyPIN(switchWrapper) ;			

			logger.info("Bank PIN Verified" + (switchWrapper != null));
			
			AccountInfoModel accountInfoModel = switchWrapper.getAccountInfoModel();
			accountInfoModel.setOldPin(bankPin);
			
		} catch (FrameworkCheckedException e) {
	
			throw new FrameworkCheckedException(e.getMessage());
			
		} catch (Exception e) {
	
			throw new FrameworkCheckedException(e.getMessage());
		}	
	}
*/

	@Override
	public String response() {
		
		String date = dtf.print(new DateTime());
		String time = tf.print(new LocalTime());
		
		StringBuilder response = new StringBuilder();

//		if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {
//
//			Object[] arguments = new Object[] {
//					(String) workFlowWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT),
//					(String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER),
//					(String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB),
//					date +" "+ time,
//					(String) workFlowWrapper.getObject(CommandFieldConstants.KEY_TAMT),
//					(String) workFlowWrapper.getObject("CORE_BALANCE"),
//					(String) workFlowWrapper.getObject("OLA_BALANCE")
//
//			};
//
//			response.append(this.getMessageSource().getMessage("TransferInPayment.USSD.Notification", arguments, null));
//
//		}
		if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {

			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_PROD_ID)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_NAME, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_NAME)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_CAMT)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_CODE, workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode()));

			response.append(MiniXMLUtil.createXMLParameterTag("OLA_BALANCE", (String) workFlowWrapper.getObject("OLA_BALANCE")));
			response.append(MiniXMLUtil.createXMLParameterTag("CORE_BALANCE", (String) workFlowWrapper.getObject("CORE_BALANCE")));
			
		}  else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue() || Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {

			logger.info("[TransferIn Command] Going to push back the response");
			response.append(toMobileXMLResponse());
		}  
		
		return response.toString();
	}
	

	private String toMobileXMLResponse() {

		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMMM yyyy hh:mm aaa");
		SimpleDateFormat timef = new SimpleDateFormat("hh:mm:ss a");
		TransactionModel _transactionModel = workFlowWrapper.getTransactionModel();
				
		String date=PortalDateUtils.formatDate(_transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
		String time=PortalDateUtils.formatDate(_transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
		
		String agentBalance = Formatter.formatDouble(workFlowWrapper.getSwitchWrapper().getOlavo().getAgentBalanceAfterTransaction());
		
		if(agentBalance == null) {
			agentBalance = Formatter.formatDouble(0D);
		}		
		
		String transactionCode = _transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();	
		String coreAccountNumber = StringEscapeUtils.escapeXml(workFlowWrapper.getAccountInfoModel().getAccountNick());
		String bbAccountNumber = workFlowWrapper.getSwitchWrapper().getAccountInfoModel().getAccountNick();	
		Double TPAM = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount();
		totalAmount = Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTotalAmount());

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<trans>");		

		responseBuilder.append(
			    "<trn "+
			     " TRXID='"+transactionCode+"'"+
			     " AMOB='"+appUserModel.getMobileNo()+"'"+
			     " BBACID='"+bbAccountNumber+"'"+
			     " COREACID='"+coreAccountNumber+"'"+
			     " PROD='"+workFlowWrapper.getProductModel().getName()+"'"+
			     " CAMT='"+_transactionModel.getTotalCommissionAmount()+"'"+
			     " CAMTF='"+Formatter.formatDoubleByPattern(_transactionModel.getTotalCommissionAmount(), "#,###.00")+"'"+
			     " TPAM='"+TPAM+"'"+
			     " TPAMF='"+Formatter.formatDoubleByPattern(TPAM, "#,###.00")+"'"+
			     " TAMT='"+totalAmount+"'"+
			     " TAMTF='"+Formatter.formatDoubleByPattern(Double.valueOf(totalAmount), "#,###.00")+"'"+
			     " TXAM='"+transactionModel.getTransactionAmount()+"'"+
			     " TXAMF='"+Formatter.formatDoubleByPattern(Double.valueOf(transactionModel.getTransactionAmount()), "#,###.00")+"'"+
			     " DATE='"+date+"'"+
			     " DATEF='"+sdf.format(_transactionModel.getCreatedOn())+"'"+
			     " TIMEF='"+timef.format(_transactionModel.getCreatedOn())+"'"+
			     " BALF='"+Formatter.formatDoubleByPattern(Double.valueOf((Double)workFlowWrapper.getObject("OLA_BALANCE")), "#,###.00")+"'>"+
			    "</trn>");
		
		responseBuilder.append("</trans>");
		
		logger.info("\n"+responseBuilder.toString());
		return responseBuilder.toString();
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
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		logger.debug("Found Smart Money Account "+ smartMoneyAccountModel.getName());
		
		return smartMoneyAccountModel;
	}

	public RetailerModel loadRetailerModel(Long retailerId) throws CommandException {

		RetailerModel retailerModel = new RetailerModel();
		retailerModel.setRetailerId(retailerId);

		baseWrapper.setBasePersistableModel(retailerModel);

		try {

			baseWrapper = getCommonCommandManager().loadRetailer(baseWrapper);

		} catch (FrameworkCheckedException e) {

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}

		return (baseWrapper != null ? ((RetailerModel) baseWrapper.getBasePersistableModel()) : null);
	}

	public DistributorModel loadDistributorModel(Long distributorId) throws CommandException {

		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setDistributorId(distributorId);

		baseWrapper.setBasePersistableModel(distributorModel);

		try {

			baseWrapper = getCommonCommandManager().loadDistributor(baseWrapper);

		} catch (FrameworkCheckedException e) {

			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}

		return (baseWrapper != null ? ((DistributorModel) baseWrapper.getBasePersistableModel()) : null);
	}

}