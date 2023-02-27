package com.inov8.microbank.server.service.commandmodule;

import java.text.SimpleDateFormat;
import java.util.List;

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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.TellerCashInVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

public class TellerCashInCommand extends BaseCommand  {
	
	protected final Log logger = LogFactory.getLog(TellerCashInCommand.class);

	private String productId;
	private String amount;	
	private String mobileNo;
	private String senderMobileNo;
	private String totalAmount;
	private String deviceTypeId;
	private String commissionAmount;
	private String successMessage;
	private String walkInCustomerCNIC;
	private String walkInCustomerMobileNumber;
	private String accountType;
	private String accountNumber;
	private Double discountAmount = 0D;
	private Double balance=0D;
	private String consumerNo;
	private String transactionAmount;

	private SmartMoneyAccountModel olaSmartMoneyAccountModel;
	private AccountInfoModel accountInfoModel;
	private AppUserModel bankAppUserModel;
	private AppUserModel appUserModel;
	private TransactionModel transactionModel;
	private ProductModel productModel;
	private BaseWrapper baseWrapper;
	private UserDeviceAccountsModel userDeviceAccountsModel; 
	private RetailerContactModel retailerContactModel = null;
	private CustomerModel customerModel = null;
	private WorkFlowWrapper workFlowWrapper;
	
	private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");	
	
	@Override
	public void execute() throws CommandException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of TellerCashInCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		
		try {
			
			if(bankAppUserModel == null && bankAppUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.BANK.longValue()) {

				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		
			Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER};
			
			appUserModel = commonCommandManager.loadAppUserByMobileAndType(mobileNo, appUserTypeIds);
			
			if(appUserModel != null) {

				this.olaSmartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				
				if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

					accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

					retailerContactModel = getRetailerContactModel(appUserModel);
					
				} else if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

					accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(new CustomerModel(appUserModel.getCustomerId()));
					commonCommandManager.loadCustomer(baseWrapper);
					customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
				}
				
			} else {

				throw new CommandException("Invalid Customer/Retailer Mobile Number" ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
		} catch(Exception ex) {
			
			logger.error(ex);

			throw new CommandException("Invalid Customer/Retailer Mobile Number" ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}		
		
		workFlowWrapper = new WorkFlowWrapperImpl();
					
		try {
			
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));
					
			TellerCashInVO tellerCashInVO = (TellerCashInVO) commonCommandManager.loadProductVO(baseWrapper);
					
			if(tellerCashInVO == null) {
					
				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			} else {
				productModel = (ProductModel)baseWrapper.getBasePersistableModel();
				tellerCashInVO.setProductModel(productModel);
			}
	
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.Teller_CASH_IN_TX);
				
			transactionModel = new TransactionModel();
			transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
			BankModel bankModel = new BankModel(BankConstantsInterface.ASKARI_BANK_ID);

				
			WORK_FLOW_WRAPPER_PARAMS : {
	
				workFlowWrapper.setRetailerContactModel(retailerContactModel);
				workFlowWrapper.setProductModel(productModel);
				workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
				workFlowWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
				workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
				workFlowWrapper.setAccountInfoModel(accountInfoModel);
				workFlowWrapper.setProductVO(tellerCashInVO);
				workFlowWrapper.setTransactionAmount(Double.parseDouble(transactionAmount));
				workFlowWrapper.setAppUserModel(bankAppUserModel);
				workFlowWrapper.setReceiverAppUserModel(appUserModel);
				workFlowWrapper.setCustomerModel(customerModel);
				workFlowWrapper.setTransactionModel(transactionModel);
				workFlowWrapper.setBankModel(bankModel);
				
				if(productModel.getProductId().longValue() == ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue()) {
					
					workFlowWrapper.setSenderWalkinCustomerModel(new WalkinCustomerModel(walkInCustomerCNIC, senderMobileNo));
				}
				
				
				logger.info("[TellerCashInCommand.execute] \nProduct ID: " + productId + " \nLogged In AppUserID:" + bankAppUserModel.getAppUserId() + 
							"\n Trx Amount: " + Double.parseDouble(amount) + "\n Commission: " + commissionAmount);
			}
				
			workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
			transactionModel = workFlowWrapper.getTransactionModel();
			productModel = workFlowWrapper.getProductModel();
			userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
			discountAmount = workFlowWrapper.getDiscountAmount();
			successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					
			logger.info(workFlowWrapper.getOLASwitchWrapper().getAgentBalance()+"-"+workFlowWrapper.getOLASwitchWrapper().getBalance());
	
			balance = workFlowWrapper.getOLASwitchWrapper().getBalance();
			balance = workFlowWrapper.getOLASwitchWrapper().getAgentBalance();
			workFlowWrapper.putObject("productTile",productModel.getName());
			commonCommandManager.sendSMS(workFlowWrapper);
				
		} catch(FrameworkCheckedException ex) {
				
			if(logger.isErrorEnabled()) {
				logger.error("[TellerCashInCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + bankAppUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + amount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
				
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
			
		catch(WorkFlowException wex) {
			wex.printStackTrace();
			if(logger.isErrorEnabled()) {
					
				logger.error("[TellerCashInCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + bankAppUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + amount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
			}
			
			throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			if(logger.isErrorEnabled()) {
				logger.error("[TellerCashInCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + bankAppUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Trx Amount: " + amount + " Commission: " + commissionAmount + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}

		
		if(logger.isDebugEnabled()) {
			logger.debug("End of TellerCashInCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of TellerCashInCommand.prepare()");
		}
		
		this.baseWrapper = baseWrapper;
		bankAppUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN);
		walkInCustomerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_CNIC);
		amount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		
		if(logger.isDebugEnabled()) {
			logger.debug("End of TellerCashInCommand.prepare()");
		}
	}
	

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Start of TellerCashInCommand.validate()");
		}
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(amount,validationErrors,"Amount");
				
		if(!validationErrors.hasValidationErrors()) {
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(amount, validationErrors, "Tx Amount");
		}
		

		if(!validationErrors.hasValidationErrors()) {
			
			try {
				
				validationErrors = getCommonCommandManager().checkActiveAppUser(bankAppUserModel);

				if(validationErrors.hasValidationErrors()) {
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				
			} catch (FrameworkCheckedException e) {
				
				throw new CommandException(e.getLocalizedMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		if(logger.isDebugEnabled()) {
			logger.debug("End of TellerCashInCommand.validate()");
		}
		
		return validationErrors;
	}


	@Override
	public String response() {
		
		String date = dtf.print(new DateTime());
		String time = tf.print(new LocalTime());
		
		StringBuilder response = new StringBuilder();

		if(Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {
			
			Object[] arguments = new Object[] {
					(String) workFlowWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT), 
					(String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER),
					(String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB),
					date +" "+ time, 
					(String) workFlowWrapper.getObject(CommandFieldConstants.KEY_TAMT),
					(String) workFlowWrapper.getObject("CORE_BALANCE"),
					(String) workFlowWrapper.getObject("OLA_BALANCE")
					
			};

			response.append(this.getMessageSource().getMessage("TransferInPayment.USSD.Notification", arguments, null));

		} else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {

			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_PROD_ID)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_NAME, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_NAME)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_CAMT)));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_CODE, workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode()));

			response.append(MiniXMLUtil.createXMLParameterTag("OLA_BALANCE", (String) workFlowWrapper.getObject("OLA_BALANCE")));
			response.append(MiniXMLUtil.createXMLParameterTag("CORE_BALANCE", (String) workFlowWrapper.getObject("CORE_BALANCE")));
			
		}  else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
			
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
		
		String transactionCode = _transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();	
		Double TPAM = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount();
		totalAmount = Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTotalAmount());

		StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append("<trans>");	
		responseBuilder.append("<trn ");
		responseBuilder.append(CommandFieldConstants.KEY_PRODUCT_ID+"='"+productId+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_PRODUCT+"='"+productModel.getName()+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TX_ID+"='"+transactionCode+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_DATEF+"='"+sdf.format(_transactionModel.getCreatedOn())+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TIMEF+"='"+timef.format(_transactionModel.getCreatedOn())+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TPAMF+"='"+Formatter.formatDoubleByPattern(TPAM, "#,###.00")+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_CAMTF+"='"+Formatter.formatDoubleByPattern(_transactionModel.getTotalCommissionAmount(), "#,###.00")+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TXAMF+"='"+Formatter.formatDoubleByPattern(Double.valueOf(transactionModel.getTransactionAmount()), "#,###.00")+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_FORMATED_BAL+"='"+Formatter.formatDoubleByPattern(Double.valueOf((String)workFlowWrapper.getObject("OLA_BALANCE")), "#,###.00")+"' ");
		responseBuilder.append(CommandFieldConstants.KEY_TOTAL_AMOUNT+"='"+Formatter.formatDoubleByPattern(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount() + workFlowWrapper.getCommissionAmountsHolder().getTotalCommissionAmount(), "#,###.00")+"' ");
		
		if(workFlowWrapper.getSenderWalkinCustomerModel() != null) {
			responseBuilder.append(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN+"='"+workFlowWrapper.getSenderWalkinCustomerModel().getMobileNumber()+"' ");
			responseBuilder.append(CommandFieldConstants.KEY_WALKIN_SENDER_CNIC+"='"+workFlowWrapper.getSenderWalkinCustomerModel().getCnic()+"' ");		
			responseBuilder.append(CommandFieldConstants.KEY_NAME+"='Walkin Customer' ");

		} else {
			responseBuilder.append(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN+"='"+appUserModel.getMobileNo()+"' ");
			responseBuilder.append(CommandFieldConstants.KEY_WALKIN_SENDER_CNIC+"='"+appUserModel.getNic()+"' ");			
			responseBuilder.append(CommandFieldConstants.KEY_NAME+"='"+appUserModel.getFirstName()+" "+appUserModel.getLastName()+"' ");		
		}

		responseBuilder.append(">");
		responseBuilder.append("</trn>");
		responseBuilder.append("</trans>");
		
		logger.info("\n"+responseBuilder.toString());
		return responseBuilder.toString();
	}	
	

	private RetailerContactModel getRetailerContactModel(AppUserModel appUserModel) throws FrameworkCheckedException {

		
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(retailerContactModel);
		baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
		retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();	
		
		return (retailerContactModel != null && retailerContactModel.getPrimaryKey() != null ? retailerContactModel : null);
	}
	
	/**
	 * @param _smartMoneyAccountModel
	 * @return
	 */
	private SmartMoneyAccountModel getSmartMoneyAccountModel(AppUserModel appUserModel , Long paymentModeId) throws CommandException {

		SmartMoneyAccountModel smartMoneyAccountModel = null;
		
		SmartMoneyAccountModel _smartMoneyAccountModel = new SmartMoneyAccountModel();
		
		if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

			_smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
			
		} else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

			_smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
		}
		
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
}