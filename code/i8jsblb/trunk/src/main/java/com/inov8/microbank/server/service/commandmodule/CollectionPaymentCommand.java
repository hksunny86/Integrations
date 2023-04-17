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
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CollectionPaymentCommand extends BaseCommand {

	protected final Log logger = LogFactory.getLog(CollectionPaymentCommand.class);
	private BaseWrapper baseWrapper;
	private ProductModel productModel;
	protected AppUserModel appUserModel;
	private TransactionModel transactionModel;
	private SmartMoneyAccountModel smartMoneyAccountModel;
	
	private WorkFlowWrapper workFlowWrapper;
	private BaseWrapper preparedBaseWrapper;
	private DateTimeFormatter dateTimeFormatter;
	private UserDeviceAccountsModel userDeviceAccountsModel;

	private Long transactionTypeId = null;
	private Boolean isInclusiveCharges;
	private Double balance = 0D;
	protected String commissionAmount;
	protected String productId;	
	protected String agentMobileNo;
	protected String paymentType = "0";
	private Boolean isPayByAccount = Boolean.FALSE;
	protected String bankId;
	protected String customerMobileNo;
	protected String consumerNumber;
	protected String deviceTypeId;
	private String channelId;
	protected String billAmount;
	protected String totalProcessingAmount;
	protected String totalAmount;
	protected String billStatus;
	
	Double exclusiveCharges = 0D;
	
	String txAmount;
	protected String pin;
	private Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
	RetailerContactModel fromRetailerContactModel;
	
	
	public CollectionPaymentCommand() {		
		dateTimeFormatter =  DateTimeFormat.forPattern("dd/MM/yyyy");
	}
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		logger.info("Start of CollectionPaymentCommand.prepare()");
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
		consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER);
		billStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_STATUS);
		billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
		totalProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
		paymentType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_TYPE);

		preparedBaseWrapper = baseWrapper;
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		
		
		 try{
	        	RetailerContactModel retContactModel = new RetailerContactModel();
	        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
	        	
	        	BaseWrapper bWrapper = new BaseWrapperImpl();
	        	bWrapper.setBasePersistableModel(retContactModel);
	        	CommonCommandManager commonCommandManager = this.getCommonCommandManager();
	        	bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

	        	this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

	        }catch(Exception ex){
	        	logger.error("[CollectionPaymentCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
	        }
		
		 	logger.info("[CollectionPaymentCommand.prepare()] \nLogged In AppUserModel: " + appUserModel.getAppUserId() + 
     			"\n Product ID:" + productId +
     			"\n deviceTypeId: " + deviceTypeId + 
     			"\n AgentMobileNo: " + agentMobileNo );

	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors)
			throws CommandException {
		
		logger.info("Start of CollectionPaymentCommand.validate()");
		validationErrors = ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile No");		
		validationErrors = ValidatorWrapper.doRequired(consumerNumber, validationErrors, "Consumer No");	
		validationErrors = ValidatorWrapper.doRequired(billAmount, validationErrors, "Collection Amount");	
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		
		if(!validationErrors.hasValidationErrors()){
			validationErrors = ValidatorWrapper.doNumeric(agentMobileNo,validationErrors,"Agent Mobile No");
			if(!productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION.toString()))
				validationErrors = ValidatorWrapper.doNumeric(consumerNumber, validationErrors, "Consumer Code");
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			isPayByAccount = paymentType.equalsIgnoreCase("0") ? Boolean.TRUE : Boolean.FALSE;
		}
		logger.info("End of CollectionPaymentCommand.validate()");
		return validationErrors;
		
	}

	@Override
	public void execute() throws CommandException {

		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		
		try {
			
			if(appUserModel.getRetailerContactId() != null)
			{
				String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
				// Velocity validation - start
				BaseWrapper bWrapper = new BaseWrapperImpl();
				bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
				bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
				bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, fromRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
				bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, fromRetailerContactModel.getDistributorLevelId());
				bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(billAmount));

				bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segmentId));
				bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, fromRetailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
				bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//				bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);

				boolean result = commonCommandManager.checkVelocityCondition(bWrapper);
				// Velocity validation - end
				
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);				
				if(!validationErrors.hasValidationErrors())
				{
					SwitchWrapper switchWrapper = new SwitchWrapperImpl();
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					baseWrapper.setBasePersistableModel(productModel);
					baseWrapper = commonCommandManager.loadProduct(baseWrapper);
					productModel = (ProductModel)baseWrapper.getBasePersistableModel();
					if(productModel == null){
						throw new CommandException("Product not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}

					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);

					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("CollectionPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					String productCode = productModel.getProductCode();
					if(consumerNumber!=null && productId != null && productCode !=null)
					{
						logger.info("Validating Challan for Consumer No : " +consumerNumber+" product Code: "+productCode);
						boolean flag =this.getCommonCommandManager().validateChallanParams(consumerNumber,productCode);
						//Params are valid check for challan status
						if (flag){
							boolean billInQueue = this.getCommonCommandManager().getChallanStatus(consumerNumber,productCode);
							if(billInQueue)
								throw new CommandException("Transaction is already Processing.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
							/*//Check challan payment in TransactionDetailMaster and throws exception if exists
							long paidCount = this.getCommonCommandManager().getPaidChallan(consumerNumber,productCode);
							// throws exception according to the count of paidCount, if paidCount >0
							if(paidCount>0)
								this.getCommonCommandManager().throwsChallanException(paidCount);*/

							//challan is not in process or paid add to bill_status
							this.getCommonCommandManager().addChallanToQueue(consumerNumber,productCode);

						}

					}
					AppUserModel retailerAppUserModel = new AppUserModel();
					retailerAppUserModel.setMobileNo(agentMobileNo);
					retailerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.COLLECTION_PAYMENT_TX);
				
					//SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					smartMoneyAccountModel.setDeleted(false);
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					
					
					
					baseWrapper = commonCommandManager.loadOLASmartMoneyAccount(baseWrapper);
					smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
			
					UtilityBillVO utilityBillVO = new UtilityBillVO();
					utilityBillVO.setConsumerNo(consumerNumber);
					utilityBillVO.setBillAmount(Double.parseDouble(billAmount));
					
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));


					AppUserModel customerAppUserModel = null;
					CustomerModel customerModel = null;
					AccountInfoModel customerAccountInfoModel = null;

					if(isPayByAccount) {
						if (!StringUtil.isNullOrEmpty(customerMobileNo)) {
							customerAppUserModel = this.getCustomerAppUserModel(customerMobileNo);
							if (customerAppUserModel != null) {

								customerModel = new CustomerModel();
								customerModel.setCustomerId(customerAppUserModel.getCustomerId());

								BaseWrapper baseWrapper = new BaseWrapperImpl();
								baseWrapper.setBasePersistableModel(customerModel);

								baseWrapper = this.getCommonCommandManager().loadCustomer(baseWrapper);

								if (baseWrapper.getBasePersistableModel() != null) {
									customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
									if (isPayByAccount) {//done
										if (customerModel != null) {

											checkCustomerCredentials(customerAppUserModel);
											segmentId = customerModel.getSegmentId();

											customerAccountInfoModel = getCommonCommandManager().getAccountInfoModel(customerAppUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

										} else {
											throw new CommandException("Customer does not exist against mobile number " + customerMobileNo, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
										}
									}
								}
							}
						}

						SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
						sma.setCustomerId(appUserModel.getCustomerId());

						sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

						SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);

						commonCommandManager.validateBalance(customerAppUserModel, smartMoneyAccountModel, Double.valueOf(billAmount), true);
					}

					workFlowWrapper.setSwitchWrapper(switchWrapper);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setRetailerAppUserModel(retailerAppUserModel);
					
					transactionModel = new TransactionModel();
					transactionModel.setTransactionAmount(Double.parseDouble(billAmount));	
					transactionModel.setTotalAmount(Double.parseDouble(this.totalAmount));
					transactionModel.setTotalCommissionAmount(Double.parseDouble(totalProcessingAmount));
					transactionModel.setNotificationMobileNo(customerMobileNo);
					workFlowWrapper.setTransactionModel(transactionModel);
					workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
					workFlowWrapper.setCustomerModel(customerModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					Long customerId = null;
					if(appUserModel.getCustomerId() != null)
						customerId = appUserModel.getCustomerId();
					else
						customerId = appUserModel.getAppUserId();
					AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(customerId,smartMoneyAccountModel.getName());
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
					workFlowWrapper.setFromRetailerContactModel(fromRetailerContactModel);
					workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
					workFlowWrapper.setToSegmentId(segmentId);
					workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
					workFlowWrapper.setTotalCommissionAmount(0.0D);
					workFlowWrapper.setTxProcessingAmount(0.0D);
					workFlowWrapper.setCustomField(customerMobileNo);
					workFlowWrapper.setProductVO(utilityBillVO);
					
					
					SegmentModel segmentModel = new SegmentModel();
					segmentModel.setSegmentId(segmentId);
					workFlowWrapper.setSegmentModel(segmentModel);
					
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());

					logger.info("[CollectionPaymentCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() +
								" Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);

					
					workFlowWrapper.setCustomField(0);
					//*************************************************************************************************************
					//*************************************************************************************************************
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
					//*************************************************************************************************************
					//*************************************************************************************************************				
					isInclusiveCharges = workFlowWrapper.getCommissionAmountsHolder().getIsInclusiveCharges();
					exclusiveCharges = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount();
					transactionModel = workFlowWrapper.getTransactionModel();						
					productModel = workFlowWrapper.getProductModel();
							
					
					//Customer Balance
					if(workFlowWrapper.getCustomerAppUserModel() != null) {
						balance = workFlowWrapper.getOLASwitchWrapper().getOlavo().getAgentBalanceAfterTransaction();
					}
					else{
						balance = workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction();
					}
					//consumerNumber = ((UtilityBillVO) workFlowWrapper.getProductVO()).getConsumerNo();
	
					commonCommandManager.sendSMS(workFlowWrapper);
					//Remove challan row from billStatus
					//Data will be delete by CLEAN_BILL_STATUS_JOB
                	/*if(consumerNumber!=null && productCode != null)
                    this.getCommonCommandManager().removeChallanFromQueue(consumerNumber,productCode);*/
				
				
				}else
				{
					logger.error("[CollectionPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + txAmount + " Commission: " + commissionAmount);
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
				
				
				
			}else {
				
				throw new CommandException("Agent does not exist against mobile number: "+agentMobileNo ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
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


	private AppUserModel getCustomerAppUserModel(String mobileNumber) throws FrameworkCheckedException {

		AppUserModel customerAppUserModel = new AppUserModel();
		customerAppUserModel.setMobileNo(mobileNumber);
		customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		sBaseWrapper.setBasePersistableModel(customerAppUserModel);
		sBaseWrapper = getCommonCommandManager().loadAppUserByMobileNumberAndType(sBaseWrapper);

		customerAppUserModel = (AppUserModel) sBaseWrapper.getBasePersistableModel();

		if (customerAppUserModel != null && customerAppUserModel.getAccountEnabled() != null && isPayByAccount) {

			if (!customerAppUserModel.getAccountEnabled() || customerAppUserModel.getAccountLocked() || customerAppUserModel.getAccountExpired()) {
				throw new CommandException("Transaction cannot be processed. The customer account is locked / deactivated.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			}
		}

		return customerAppUserModel;
	}


	private void checkCustomerCredentials(AppUserModel senderAppUserModel) throws FrameworkCheckedException {

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.setBasePersistableModel(senderAppUserModel);

		validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

		if (validationErrors.hasValidationErrors()) {

			throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
		}
	}

	@Override
	public String response() {
		
		 return toXML();
	}

	
	
	private String toXML(){

        TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
        params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(workFlowWrapper.getTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(replaceNullWithZero((balance)))));
        params.add(new LabelValueBean(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNumber));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_BILL_AMOUNT, billAmount));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, Formatter.formatNumbers(Double.parseDouble(billAmount))));
        params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(workFlowWrapper.getTransactionDetailMasterModel().getExclusiveCharges() + "")));
        params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(workFlowWrapper.getTransactionDetailMasterModel().getExclusiveCharges())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_AGENT_MOBILE, agentMobileNo));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, appUserModel.getNic()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT_NAME,  replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
        params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));

        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));

        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));

        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));


        return MiniXMLUtil.createResponseXMLByParams(params);
    }

	
	
}
