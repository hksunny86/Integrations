package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.StringUtil.isNullOrEmpty;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
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
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.integration.vo.BulkBillPaymentVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;



public class BulkBillPaymentCommand extends BaseCommand 
{

	protected final Log logger = LogFactory.getLog(AllPayBillPaymentCommand.class);

	private AppUserModel appUserModel;
	private String productId;
	private String accountId;
	private String mobileNo;
	private String txProcessingAmount;
	private String pin;
	private String deviceTypeId;
	private String commissionAmount;
	private String totalAmount;
	private String billAmount;
	private String cvv;
	private String tPin;
	
	private String accountType;
	private String accountCurrency;
	private String accountStatus;
	private String accountNumber;
	private Double discountAmount = 0D;
	private Long transactionTypeId = null;
	
	private TransactionModel transactionModel;
	private ProductModel productModel;
	private SmartMoneyAccountModel smartMoneyAccountModel;
	private BaseWrapper baseWrapper;
	private String walkInCustomerCNIC;
	private String walkInCustomerMobileNumber;
	private UserDeviceAccountsModel userDeviceAccountsModel; 
	private String successMessage;
	private String ussdPin;
	private Double balance = 0D;
	private String consumerNo;
	private CopyOnWriteArrayList<BulkBillPaymentVO> bulkBillPaymentVOList = new CopyOnWriteArrayList<BulkBillPaymentVO>();
	private WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

	
	@Override
	public void execute() throws CommandException {

		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
					
		try {

			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setOldPin(pin);

			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.AGENT_BULK_BILL_SALE_TX);
						
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
				
			accountInfoModel = verifyPIN(appUserModel, pin);
			
			WorkFlowWrapper _workFlowWrapper = new WorkFlowWrapperImpl();
			_workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			_workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
			_workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			_workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
			_workFlowWrapper.setAccountInfoModel(accountInfoModel);
			_workFlowWrapper.setAppUserModel(appUserModel);
			_workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
			
			_workFlowWrapper.putObject(BulkBillPaymentVO.BulkBillPaymentVO, bulkBillPaymentVOList);
					
			workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(_workFlowWrapper);
			
			balance = _workFlowWrapper.getSwitchWrapper().getAgentBalance();			
				
		} catch(FrameworkCheckedException ex) {
				
			if(logger.isErrorEnabled()) {
				logger.error("[AllPayBillPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
					
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
			
		catch(WorkFlowException wex) {
						
			if(logger.isErrorEnabled()) {
				logger.error("[AllPayBillPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
			}
						
			throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
				
		} catch(Exception ex) {
					
			ex.printStackTrace();
					
			if(logger.isErrorEnabled()) {
				logger.error("[AllPayBillPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}

		
		if(logger.isDebugEnabled()) {
			logger.debug("End of AllPayBillPaymentCommand.execute()");
		}
	}
	
	
	/**
	 * @param appUserModel
	 * @param bankPin
	 * @return
	 * @throws FrameworkCheckedException
	 */
	private AccountInfoModel verifyPIN(AppUserModel appUserModel, String bankPin) throws FrameworkCheckedException {
		
		logger.info("Agent Web > Verify Bank PIN > APP USER " + appUserModel.getUsername());
		
	    BaseWrapper baseWrapper = new BaseWrapperImpl();
	    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
	    
		SwitchWrapper switchWrapper = new SwitchWrapperImpl() ;
	    switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
	    	    
		try {
			
			AbstractFinancialInstitution abstractFinancialInstitution = getCommonCommandManager().loadFinancialInstitution(baseWrapper);
		    switchWrapper.putObject(CommandFieldConstants.KEY_PIN , bankPin) ;
			switchWrapper = abstractFinancialInstitution.verifyPIN(switchWrapper) ;

			logger.info("Agent Web > Bank PIN Verified" + (switchWrapper != null));
			
		} catch (FrameworkCheckedException e) {
	
			throw new FrameworkCheckedException(e.getMessage());
			
		} catch (Exception e) {

			throw new FrameworkCheckedException(e.getMessage());
		}
		
		return switchWrapper.getAccountInfoModel();
	}
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AllPayBillPaymentCommand.prepare()");
		}
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		
		Integer totalRows = Integer.parseInt((String)baseWrapper.getObject("totalRows"));
		
		
		for(int rowNum = 1; rowNum<=totalRows; rowNum++) {

			String consumerNumber = (String)baseWrapper.getObject(("CONSUMER_NUMBER_"+rowNum));
			String mobileNumber = (String)baseWrapper.getObject(("MOBILE_NUMBER_"+rowNum));
			String productId = (String)baseWrapper.getObject(("PRODUCT_ID_"+rowNum));
			String billAmount = (String)baseWrapper.getObject(("AMOUNT_"+rowNum));
			
			logger.info("CONSUMER_NUMBER : "+consumerNumber+"\n"+ "MOBILE_NUMBER : "+mobileNumber+"\n"+	"PRODUCT_ID : "+productId+"\n"+	"AMOUNT : "+billAmount);

			BulkBillPaymentVO bulkBillPaymentVO = new BulkBillPaymentVO(consumerNumber, mobileNumber, Long.valueOf(productId), Double.valueOf(billAmount));
			
			bulkBillPaymentVOList.add(bulkBillPaymentVO);
		}		
		
		String bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		
		try {
		
			if(appUserModel == null || appUserModel.getRetailerContactId() == null) {

				throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
			if(!isNullOrEmpty(accountId) && !isNullOrEmpty(bankId))
			{
				CommonCommandManager commonCommandManager = this.getCommonCommandManager();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
				smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
				smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				smartMoneyAccountModel.setActive(Boolean.TRUE);
				smartMoneyAccountModel.setDeleted(Boolean.FALSE);
				
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
				smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
			
				ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
				
				if(!validationErrors.hasValidationErrors()) {
				
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
					this.smartMoneyAccountModel = smartMoneyAccountModel;
				
				} else {
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			
			
		}
		catch(Exception ex)
		{
			if(logger.isErrorEnabled())
			{
				logger.error("AllPayBillInfoCommand.prepare] Exception occured for ProductID: " + productId + " AppUserID: " + appUserModel.getAppUserId() + " Exception Details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
		}

		/**
		 * ------------------------End of Change------------------------------
		 */

		
		accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
		accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
		accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
		accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
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
		
		
		logger.info("[AllPayBillPaymentCommand.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Walkin Customer Mobile No:" + walkInCustomerMobileNumber);

		if(!isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)) && 
				!isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)))
		{
			discountAmount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT));
		}
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AllPayBillPaymentCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of AllPayBillPaymentCommand.response()");
		}
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
			
		if(smartMoneyAccountModel == null) {
			
			throw new CommandException("Branchless Banking Account is not defined" ,ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, new Throwable());

		}
		
		if(appUserModel == null || appUserModel.getRetailerContactId() == null) {

			throw new CommandException("Missing Retailer Contact Identity",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
		return validationErrors;
	}
	
	private String toXML() {

		int i = 0;
		
		StringBuilder response = new StringBuilder();

		response.append("<msg id='37'><params>");
		response.append("<length>"+ bulkBillPaymentVOList.size() +"</length>");
		
		for(BulkBillPaymentVO bulkBillPaymentVO : bulkBillPaymentVOList) {

			response.append("<bill name='"+(i)+"'>");

			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_NAME, bulkBillPaymentVO.getProductName()));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUST_CODE, bulkBillPaymentVO.getConsumerNumber()));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CW_CUSTOMER_MOBILE, bulkBillPaymentVO.getMobileNumber()));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, String.valueOf(bulkBillPaymentVO.getBillAmount())));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_STATUS, bulkBillPaymentVO.getSupProcessingStatus()));
			response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_CODE, bulkBillPaymentVO.getTransactionCode()));
			
			response.append("</bill>");
			i++;
		}

		response.append("</params></msg>");
		
		logger.info(response.toString());
		
		return response.toString();
	}

}
