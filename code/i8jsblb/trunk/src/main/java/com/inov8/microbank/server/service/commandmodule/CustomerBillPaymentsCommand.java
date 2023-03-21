package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CustomerBillPaymentsCommand extends BaseCommand {

	
	protected final Log logger = LogFactory.getLog(CustomerBillPaymentsCommand.class);

	private AppUserModel appUserModel;
	private String productId;

	

	private String txProcessingAmount;

	private String deviceTypeId;
	private String commissionAmount;
	private String totalAmount;
	private String billAmount;

	private Long transactionTypeId = null;
	
	private TransactionModel transactionModel;
	private ProductModel productModel;
	private BaseWrapper baseWrapper;
	private WorkFlowWrapper workFlowWrapper;
	private String channelId;
	private String thirdPartyTransactionId;
	
	private Double balance = 0D;
	private String accountTitle = "";
	
	
	private String accountId;
	private UserDeviceAccountsModel userDeviceAccountsModel; 
	private String consumerNumber;
	private Boolean isInclusiveCharges;
	private String custMobileNo;
	private ProductVO billPaymentVO = null;
	private Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
	
	private Date billDueDate;
	private Double billingAmount;
	private Double lateBillAmount;
	//
	private String terminalId;

	private String isAirTimeTopUp;
	private String stan;
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {

		appUserModel = ThreadLocalAppUser.getAppUserModel();	
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		custMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);		
		consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CSCD);
		billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
		stan=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_STAN);
		this.baseWrapper = baseWrapper;
		SmartMoneyAccountModel smartMoneyAccountModel = null;	
  		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		channelId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID);
		terminalId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TERMINAL_ID);
		isAirTimeTopUp = this.getCommandParameter(baseWrapper,"IS_AIR_TIME_TOP_UP");
		logger.info("[CustomerBillPaymentsCommand.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + custMobileNo);
		
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {

		if(!StringUtil.isNullOrEmpty(deviceTypeId) && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())){
			BaseWrapper bWrapper = new BaseWrapperImpl();
			ProductModel productModel = new ProductModel();
			productModel.setProductId(Long.valueOf(productId));
			bWrapper.setBasePersistableModel(productModel);

			try {
				bWrapper = getCommonCommandManager().loadProduct(bWrapper);
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
				throw new CommandException("Product not found against the given the productId" + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			}
			productModel = (ProductModel) bWrapper.getBasePersistableModel();
			if (productModel != null && productModel.getAppUserTypeId() != null && !productModel.getAppUserTypeId().equals(UserTypeConstantsInterface.CUSTOMER)) {
				throw new CommandException("Product not found against given product id for Customer " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			}

		}
		else {
			validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
			validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doRequired(billAmount, validationErrors, "Bill Amount");
			validationErrors = ValidatorWrapper.doRequired(consumerNumber, validationErrors, "Consumer Number");
			validationErrors = ValidatorWrapper.doRequired(custMobileNo, validationErrors, "Customer Mobile Number");

			if (!validationErrors.hasValidationErrors()) {
				validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
				validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			}
		}
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException { 
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();
		
		try {
			ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);			
			if(validationErrors.hasValidationErrors()) {
				logger.error("[CustomerBillPaymentsCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + custMobileNo);
				throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));

			CustomerModel customerModel = null;						
			//AppUserModel customerAppUserModel = this.getAppUserModel(custMobileNo);			
			if(appUserModel != null) {
					
				customerModel = new CustomerModel();
				customerModel.setCustomerId(appUserModel.getCustomerId());
				
				BaseWrapper bWrapper = new BaseWrapperImpl(); 
				bWrapper.setBasePersistableModel(customerModel);				
				bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
				
				if(bWrapper.getBasePersistableModel() != null) {					
					customerModel = (CustomerModel) bWrapper.getBasePersistableModel();
					segmentId = customerModel.getSegmentId();					
				} 
				
				workFlowWrapper.setTransactionAmount(Double.parseDouble(billAmount));
				
				if(!StringUtil.isNullOrEmpty(txProcessingAmount)) {
					workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
				} else {
					workFlowWrapper.setTxProcessingAmount(0.0D);
				}
				
				if(!StringUtil.isNullOrEmpty(billAmount)) {
					workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
				} else {
					workFlowWrapper.setBillAmount(0.0D);
				}
				
				if(!StringUtil.isNullOrEmpty(commissionAmount)) {
					workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
				} else {
					workFlowWrapper.setTotalCommissionAmount(0.0D);
				}
				// load UtilityBillVO from session which was saved at info command
				UtilityBillVO billInfoFromSession = ThreadLocalBillInfo.getBillInfo();
				if(billInfoFromSession != null){
					billingAmount = billInfoFromSession.getBillAmount();
					lateBillAmount = billInfoFromSession.getLateBillAmount();
					billDueDate = billInfoFromSession.getDueDate();
				}
				baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID,productId);
				billPaymentVO = commonCommandManager.loadProductVO(baseWrapper);
				productModel = (ProductModel) baseWrapper.getBasePersistableModel();						
				if(billPaymentVO == null) {
					throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				else if(productModel != null && Double.parseDouble(billAmount) < productModel.getMinLimit())
					throw new CommandException("Bill Amount must be Greater/Equal to " + productModel.getMinLimit().toString(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				else if(productModel != null && Double.parseDouble(billAmount) > productModel.getMaxLimit())
					throw new CommandException("Bill Amount must be Lesser/Equal to " + productModel.getMaxLimit().toString(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				
				UtilityBillVO utilityBillVO = (UtilityBillVO) billPaymentVO;
				utilityBillVO.setBillAmount(billingAmount);
				utilityBillVO.setLateBillAmount(lateBillAmount);
				utilityBillVO.setDueDate(billDueDate);
				utilityBillVO.setBillingMonth(CommonUtils.getBillingMonthFromDueDate(billDueDate));
				utilityBillVO.setBillPaid(Boolean.FALSE);
				
				workFlowWrapper.setCustomerAppUserModel(appUserModel);
				
				SegmentModel segmentModel = new SegmentModel();
				segmentModel.setSegmentId(segmentId);
				workFlowWrapper.setSegmentModel(segmentModel);				
				workFlowWrapper.setIsCustomerInitiatedTransaction(true);
						
				if(InternetCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {							
					transactionTypeId = TransactionTypeConstantsInterface.AGENT_INTERNET_BILL_SALE_TX;
							
				} else if(NadraCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {
					transactionTypeId = TransactionTypeConstantsInterface.AGENT_NADRA_BILL_SALE_TX;
						
				} else if (UtilityCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {
					transactionTypeId = TransactionTypeConstantsInterface.CUSTOMER_BILL_PAYMENT_TX;
				}
				else if (OneBillProductEnum.contains(String.valueOf(productModel.getProductId())) || OtherThanOneBillProductEnum.contains(String.valueOf(productModel.getProductId()))) {

					transactionTypeId = TransactionTypeConstantsInterface.CUSTOMER_BILL_PAYMENT_TX;
				}
				else{
					transactionTypeId = TransactionTypeConstantsInterface.OFFLINE_BILL_PAYMENT_TX;
				}

				//**********************************************************************
				TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
				transactionTypeModel.setTransactionTypeId(transactionTypeId);
				//*********************************************************************
				DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
				deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
				
				
								
				//DistributorModel distributorModel = commonCommandManager.loadDistributor(appUserModel);			
				
				//commonCommandManager.checkProductLimit(segmentId, productModel.getProductId(), appUserModel.getMobileNo(), deviceTypeModel.getDeviceTypeId(), Double.parseDouble(billAmount), productModel, distributorModel, workFlowWrapper.getHandlerModel());
		
				SmartMoneyAccountModel olaSmartMoneyAccountModel = getsmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);	
				AccountInfoModel accountInfoModel = null;				
				accountInfoModel = getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				
				
				if(olaSmartMoneyAccountModel == null) {				
					throw new CommandException("Branchless Banking Account is not defined" ,ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, new Throwable());
				} else {
					accountId = olaSmartMoneyAccountModel.getSmartMoneyAccountId().toString();
					accountTitle = olaSmartMoneyAccountModel.getName();
				}

				commonCommandManager.validateBalance(appUserModel, olaSmartMoneyAccountModel, Double.valueOf(billAmount), true);

				SwitchWrapper switchWrapper = new SwitchWrapperImpl();
				if(isAirTimeTopUp != null && isAirTimeTopUp.equals("1"))
				{
					switchWrapper.setFromAccountNo("10121934709");
				}
				else if(accountInfoModel != null) {
					switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
				}

				long accountType = customerModel.getCustomerAccountTypeId();
				String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
				this.checkVelocityCondition(segmentId, accountType, userId);
				
				workFlowWrapper.setSwitchWrapper(switchWrapper);
				workFlowWrapper.setProductModel(productModel);
				workFlowWrapper.setCustomerAppUserModel(appUserModel);
				workFlowWrapper.setCustomerModel(customerModel);
				workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
				workFlowWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
				workFlowWrapper.setOlaSmartMoneyAccountModel(olaSmartMoneyAccountModel);
				workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
				workFlowWrapper.setAccountInfoModel(accountInfoModel);
				workFlowWrapper.setProductVO(utilityBillVO);
				workFlowWrapper.setAppUserModel(appUserModel);
				workFlowWrapper.setConsumerNumber(consumerNumber);
					
				workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
				
				workFlowWrapper.putObject("IS_AIR_TIME_TOP_UP",isAirTimeTopUp);
				
				transactionModel = new TransactionModel();
				transactionModel.setTransactionAmount(Double.parseDouble(billAmount));	
				workFlowWrapper.setTransactionModel(transactionModel);
				
				logger.info("[CustomerBillPaymentsCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + 
							" Customer Mobile No:" + custMobileNo );
				
				
				//Boolean isPayByCustomerAccount = ((Integer)workFlowWrapper.getCustomField()).intValue() == 0;
				
				workFlowWrapper.setCustomField(0);
				workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID,terminalId);
				workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,channelId);
				workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN,stan);
				workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE,thirdPartyTransactionId);
				//*************************************************************************************************************
				//*************************************************************************************************************
				workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				//*************************************************************************************************************
				//*************************************************************************************************************				
				isInclusiveCharges = workFlowWrapper.getCommissionAmountsHolder().getIsInclusiveCharges();					
				transactionModel = workFlowWrapper.getTransactionModel();						
				productModel = workFlowWrapper.getProductModel();
						
				
						
				
				
				//Customer Balance
				balance = workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction();
				//consumerNumber = ((UtilityBillVO) workFlowWrapper.getProductVO()).getConsumerNo();


				workFlowWrapper.putObject("productTile",productModel.getName());
				commonCommandManager.sendSMS(workFlowWrapper);
				commonCommandManager.novaAlertMessage(workFlowWrapper);
				
			} else {
				
				throw new CommandException("Customer does not exist against mobile number "+custMobileNo ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
			}			
		}catch(FrameworkCheckedException ex) {
			ex.printStackTrace();				
		throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
	}	
	catch(WorkFlowException wex) {			
		throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			
	}catch(Exception ex) {				
		ex.printStackTrace();
		throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
	}

	
		
		
	}


	
	@Override
	public String response() {
		
		return toXML();
	}
	
	
	
	
	private String toXML()
	{
		
		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(workFlowWrapper.getTransactionCodeModel().getCode())));
		params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(replaceNullWithZero(( balance)))));
		params.add(new LabelValueBean(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, replaceNullWithEmpty(consumerNumber)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_BILL_AMOUNT, replaceNullWithEmpty(billAmount)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, Formatter.formatNumbers(replaceNullWithZero(Double.parseDouble(billAmount)))));
		params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount()+"")));
		params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(replaceNullWithZero(transactionModel.getTotalCommissionAmount()))));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(custMobileNo)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, appUserModel.getNic()));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_LATE_BILL_AMT, replaceNullWithZero(lateBillAmount).toString()));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_LATE_BILL_AMT, Formatter.formatNumbers(replaceNullWithZero(lateBillAmount))));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, replaceNullWithEmpty(productModel.getDescription())));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_PESSENGER_NAME,  replaceNullWithEmpty(productModel.getName())));
		params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
		params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
		params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()+"")));
		params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount())));

		params.add(new LabelValueBean(ATTR_TRN_TYPE,  replaceNullWithEmpty(transactionModel.getTransactionTypeId().toString())));
		params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));
		
		params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
		params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
		params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
		
		params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
		
		
		return MiniXMLUtil.createResponseXMLByParams(params);
		

	}
	

	
	private SmartMoneyAccountModel getsmartMoneyAccountModel(AppUserModel _appUserModel, Long paymentModeId) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
        customerSMAModel.setCustomerId(appUserModel.getCustomerId());
        customerSMAModel.setActive(Boolean.TRUE);
        customerSMAModel.setDeleted(Boolean.FALSE);
        customerSMAModel.setDefAccount(Boolean.TRUE);
        customerSMAModel.setPaymentModeId(paymentModeId);
        searchBaseWrapper.setBasePersistableModel(customerSMAModel);

        searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);
		
        @SuppressWarnings("rawtypes")
        CustomList smaList = searchBaseWrapper.getCustomList();
        if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
            customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
        }
		
		return customerSMAModel;
	}
	
	
	void checkVelocityCondition( Long segmentId, Long accountType, String userId) throws FrameworkCheckedException {
		
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
	    bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
	    bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, -1L);
	    bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, -1L);
		bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(billAmount));
//		bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, -1L);

	    if(segmentId != null && CommissionConstantsInterface.DEFAULT_SEGMENT_ID.longValue() != segmentId.longValue()) {
	    	bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, segmentId);
	    }

		if(accountType != null) {
	    	bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, accountType);
		}
		if(userId != null) {
			bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
		}
		getCommonCommandManager().checkVelocityCondition(bWrapper);
	}
	

}
