package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CollectionPaymentInfoCommand extends BaseCommand {

	protected final Log logger = LogFactory.getLog(CollectionPaymentInfoCommand.class);
	private BaseWrapper baseWrapper;
	private ProductModel productModel;
	protected AppUserModel appUserModel;
	private DateTimeFormatter dateTimeFormatter;
	private UserDeviceAccountsModel userDeviceAccountsModel;
	private CommissionAmountsHolder commissionAmountsHolder;
	private I8SBSwitchControllerResponseVO responseVO;
	private FonePayManager fonePayManager;


	protected String productId;
	
	protected String agentMobileNo;
	
	protected String paymentType = "0";
	private Boolean isPayByAccount = Boolean.FALSE;
	protected String bankId;
	protected String customerMobileNo;
	protected String consumerNumber;
	protected String deviceTypeId;
	protected String billAmount;
	protected AppUserModel receiverAppUserModel;

	RetailerContactModel fromRetailerContactModel;
	
	public CollectionPaymentInfoCommand() {
		
		dateTimeFormatter =  DateTimeFormat.forPattern("dd/MM/yyyy");
	}
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		
		logger.info("Start of CollectionPaymentInfoCommand.prepare()");
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
		consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER);
		paymentType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_TYPE);

		try {
			receiverAppUserModel = commonCommandManager.loadAppUserByQuery(customerMobileNo, UserTypeConstantsInterface.CUSTOMER);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

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
	        	logger.error("[CashDepositInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
	        }
		
		 	logger.info("[CollectionPaymentInfoCommand.prepare()] \nLogged In AppUserModel: " + appUserModel.getAppUserId() + 
     			"\n Product ID:" + productId +
     			"\n deviceTypeId: " + deviceTypeId + 
     			"\n customerMobileNumber: " + customerMobileNo );

		
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		logger.info("Start of CollectionPaymentInfoCommand.validate()");
		validationErrors = ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile No");
		validationErrors = ValidatorWrapper.doRequired(consumerNumber, validationErrors, "Consumer No");
		validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");

		if (!validationErrors.hasValidationErrors()) {
			validationErrors = ValidatorWrapper.doNumeric(agentMobileNo, validationErrors, "Agent Mobile No");
			if (!productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION.toString()))
				validationErrors = ValidatorWrapper.doNumeric(consumerNumber, validationErrors, "Consumer Code");
			validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			isPayByAccount = paymentType.equalsIgnoreCase("0") ? Boolean.TRUE : Boolean.FALSE;
		}

		String error = "";
		if (isPayByAccount){
			if (null == receiverAppUserModel) {
				error = this.getMessageSource().getMessage("MONEYTRANSFER.REC_MOB_NOT_REG", null, null);
//			logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			} else {
				try {
					validateBBCustomer(receiverAppUserModel, "Receiver - ");
					validsateSmartMoneyAccount(receiverAppUserModel, "Receiver - ");
				} catch (FrameworkCheckedException e) {
//				logger.error(classNMethodName + e.getMessage());
					ValidatorWrapper.addError(validationErrors, e.getMessage());
				}

				if (!validationErrors.hasValidationErrors()) {
					commonCommandManager.validateHRA(customerMobileNo);
				}
			}
	}

		logger.info("End of CollectionPaymentInfoCommand.validate()");
		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {
		
		logger.info("Start of CollectionPaymentInfoCommand.execute()");
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		fonePayManager = this.getCommonCommandManager().getFonePayManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		
		CommissionWrapper commissionWrapper;
		baseWrapper = new BaseWrapperImpl();
		
		if(appUserModel.getRetailerContactId() != null) {
			
			try {
				ValidationErrors validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors()) {
					
					SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
					
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					smartMoneyAccountModel.setDeleted(false);
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					baseWrapper = commonCommandManager.loadOLASmartMoneyAccount(baseWrapper);
					smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

					baseWrapper.putObject(CommandFieldConstants.KEY_HANDLER_MODEL, handlerModel);
					validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

					if(smartMoneyAccountModel.getName() != null){
						if(smartMoneyAccountModel.getRetailerContactId().toString().equals(appUserModel.getRetailerContactId().toString()))
						{
							productModel = new ProductModel();
							TransactionModel transactionModel = null;
							productModel.setProductId(Long.parseLong(productId));
							baseWrapper.setBasePersistableModel(productModel);
							baseWrapper = commonCommandManager.loadProduct(baseWrapper);
							productModel = (ProductModel)baseWrapper.getBasePersistableModel();
							if(productModel == null){
		                            throw new CommandException("Product not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		                    }												
							workFlowWrapper.setProductModel(productModel);
							String productCode = productModel.getProductCode();
							if(consumerNumber!=null && productId != null && productCode!=null)
							{
								logger.info("Validating Challan for Consumer No : " +consumerNumber+" product Code: "+productCode);
								boolean flag =this.getCommonCommandManager().validateChallanParams(consumerNumber,productCode);
								//Params are valid check for challan status
								if (flag){
									boolean billInQueue = this.getCommonCommandManager().getChallanStatus(consumerNumber,productCode);
									if (billInQueue)
										throw new CommandException("Transaction is already Processing.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
									//Check challan payment in TransactionDetailMaster and throws exception if exists
									/*long paidCount = this.getCommonCommandManager().getPaidChallan(consumerNumber,productId);
									// throws exception according to the count of paidCount, if paidCount >0
									if(paidCount>0)
										this.getCommonCommandManager().throwsChallanException(paidCount);*/

								}

							}

//							if(isPayByAccount) {
//								String otp = CommonUtils.generateOneTimePin(4);
//								String encryptedPin = EncoderUtils.encodeToSha(otp);
//								logger.info("Otp for Kpk Challan is:" + otp);
//								try {
//									fonePayManager.createMiniTransactionModel(encryptedPin, this.customerMobileNo, "", CommandFieldConstants.CMD_OTP_VERIFICATION);
//
//									String smsText = MessageUtil.getMessage("otpSms", new String[]{productModel.getName(), otp});
//									SmsMessage smsMessage = new SmsMessage(this.customerMobileNo, smsText);
//									logger.info("Mobile # : "+ this.customerMobileNo+" : " +smsText);
//									commonCommandManager.getSmsSender().send(smsMessage);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}

//							CustomerModel customerModel = null;

//							customerModel.setCustomerId(appUserModel.getCustomerId());
							//********************************************* Fetch Challan Details - Start
	                        I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareCollectionInquiryRequest(consumerNumber,productModel);
	                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	                        switchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

	                        switchWrapper = commonCommandManager.makeI8SBCall(switchWrapper);

	                        responseVO = switchWrapper.getI8SBSwitchControllerResponseVO();

	                        ESBAdapter.processI8sbResponseCode(responseVO, true); //throws WorkFlowException incase of error response code

							if(responseVO.getBillStatus().equals("U")) {
								if (isPayByAccount) {
									String otp = CommonUtils.generateOneTimePin(4);
									String encryptedPin = EncoderUtils.encodeToSha(otp);
									logger.info("Otp for Kpk Challan is:" + otp);
									try {
										fonePayManager.createMiniTransactionModel(encryptedPin, this.customerMobileNo, "", CommandFieldConstants.CMD_OTP_VERIFICATION);

										String smsText = MessageUtil.getMessage("otpSms", new String[]{productModel.getName(), otp});
										SmsMessage smsMessage = new SmsMessage(this.customerMobileNo, smsText);
										logger.info("Mobile # : " + this.customerMobileNo + " : " + smsText);
										commonCommandManager.getSmsSender().send(smsMessage);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
	                        
	                        String billAmount = responseVO.getBillAmount() == null ? "0.0" : responseVO.getBillAmount();
	                     
	                        //********************************************* Fetch Challan Details - End
	                        
	                    	//*****To calculate the commission******//
							transactionModel = new TransactionModel();
							transactionModel.setTransactionAmount(Double.valueOf(billAmount));

							TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
							transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.COLLECTION_PAYMENT_TX);
							workFlowWrapper.setProductModel(productModel);
							workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
							workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
							workFlowWrapper.setTransactionModel(transactionModel);
							workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
							workFlowWrapper.setFromRetailerContactAppUserModel( appUserModel );
							
							
							//pulling the default segment for commission module changes
							Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
							SegmentModel segmentModel = new SegmentModel();
							segmentModel.setSegmentId(segmentId);
							workFlowWrapper.setSegmentModel(segmentModel);
							workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
							workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));
							
							
							
							
							
	                        //********************************************* Product Limit after bill inquiry- Start
	                        DistributorModel distributorModel = new DistributorModel();
	                        distributorModel.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);

	                        commonCommandManager.checkProductLimit(segmentModel.getSegmentId(), productModel.getProductId(), "abc",
	                                Long.valueOf(deviceTypeId), Double.parseDouble(billAmount), productModel, distributorModel, workFlowWrapper.getHandlerModel());
	                        //********************************************* Product Limit after bill inquiry- End
	                        
							
							
							// Setting Tax Regime Model....
							workFlowWrapper.setTaxRegimeModel((TaxRegimeModel)fromRetailerContactModel.getTaxRegimeIdTaxRegimeModel().clone());
							logger.info("Going to Calculate Commission for Product-ID :: " + productId + " Mobile NO. :: " + customerMobileNo  + " at Time :: " + new Date());
							commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
							logger.info("Commission Calculated for Product-ID :: " + productId + " Mobile NO. :: " + customerMobileNo + " at Time :: " + new Date());
							commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);


							if(responseVO.getBillStatus().equals("U")) {
								if (isPayByAccount) {
//								SmartMoneyAccountModel customerSMA = getSmartMoneyAccountModel(appUserModel.getCustomerId());
									SmartMoneyAccountModel customerSMA = new SmartMoneyAccountModel();
									customerSMA.setCustomerId(receiverAppUserModel.getCustomerId());
									customerSMA.setActive(true);
									customerSMA.setAccountClosedUnsetteled(0L);
									SmartMoneyAccountModel sma1 = getCommonCommandManager().getSmartMoneyAccountByCustomerIdAndPaymentModeId(customerSMA);
//									commonCommandManager.validateBalance(receiverAppUserModel, sma1, commissionAmountsHolder.getTotalAmount(), true);
								}
							}
							userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
						
						}
						else
						{
							throw new CommandException(this.getMessageSource().getMessage("CollectionPaymentInfoCommand.execute()", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}else{						
						throw new CommandException(this.getMessageSource().getMessage("CollectionPaymentInfoCommand.execute()", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}					
					
					
				}else {
					logger.error("[CollectionPaymentInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			
				
			}
			catch (NullPointerException n)
			{
				n.printStackTrace();
				logger.error("NullPointerException :: " );
			}
			catch(WorkFlowException wex) {
				wex.printStackTrace();
				logger.error("[CollectionPaymentInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch (ClassCastException e) {							
				logger.error("[CollectionPaymentInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
				throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
			} catch(Exception ex) {
				ex.printStackTrace();
				if(ex.getMessage() != null && !"".equals(ex.getMessage()) && ex.getMessage().indexOf("JTA") != -1) {
					logger.error("[CollectionPaymentInfoCommand.execute] Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
					throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
				} else {
					logger.error("[CollectionPaymentInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
					throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
				}
			}
			
		} else {
			logger.error("[CollectionPaymentInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
			throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
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

	protected void validateBBCustomer(AppUserModel customerAppUserModel, String prefix) throws FrameworkCheckedException{
		ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(customerAppUserModel);
		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(prefix + validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}

		//Check User Device Accounts health
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.putObject(CommandFieldConstants.KEY_CUST_ERROR_PREFIX, prefix.substring(0, prefix.lastIndexOf(" -")));
		baseWrapper.setBasePersistableModel(customerAppUserModel);
		validationErrors = commonCommandManager.checkCustomerCredentials(baseWrapper);

		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
	}

	protected void validsateSmartMoneyAccount(AppUserModel customerAppUserModel, String prefix) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
		smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
		searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);

		if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null &&
				searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
			smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
		}

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
		if(validationErrors.hasValidationErrors()) {
			throw new CommandException(prefix + validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM,new Throwable());
		}

		if(smartMoneyAccountModel.getName() == null){
			throw new CommandException(prefix + "SMA Model name is null", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM,new Throwable());
		}

		if(!smartMoneyAccountModel.getCustomerId().toString().equals(customerAppUserModel.getCustomerId().toString())){
			throw new CommandException(prefix + this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
	}


	private SmartMoneyAccountModel getSmartMoneyAccountModel(Long customerId) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
		customerSMAModel.setCustomerId(appUserModel.getCustomerId());
		customerSMAModel.setActive(Boolean.TRUE);
		customerSMAModel.setDeleted(Boolean.FALSE);
		customerSMAModel.setDefAccount(Boolean.TRUE);
		searchBaseWrapper.setBasePersistableModel(customerSMAModel);

		searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

		@SuppressWarnings("rawtypes")
		CustomList smaList = searchBaseWrapper.getCustomList();
		if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
			customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
		}

		return customerSMAModel;
	}

	@Override
	public String response() {		
		return toXMLResponse();
	}
	
	private String toXMLResponse() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMMM yyyy hh:mm aaa");

        String dueDateStr = responseVO.getDueDate();
        String billStatusStr = responseVO.getBillStatus();
        String billAmountStr = responseVO.getBillAmount();
        //String lateBillAmountStr = responseVO.getAmountAfterDueDate();

        Date dueDate = null;
        try {
            dueDate = PortalDateUtils.parseStringAsDate(dueDateStr, "yyyyMMdd");
        } catch (ParseException e) {
            logger.error("Unparseable date in response:" + dueDateStr);
        }

        double billAmount = StringUtil.isNullOrEmpty(billAmountStr) ? 0.0D : Double.parseDouble(billAmountStr);
        //double lateBillAmount = StringUtil.isNullOrEmpty(lateBillAmountStr) ? 0.0D : Double.parseDouble(lateBillAmountStr);

        String billPaid = (billStatusStr.equalsIgnoreCase("U") ? "0" : "1");

		StringBuilder responseBuilder = new StringBuilder();

		responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_AGENT_MOBILE, agentMobileNo));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PRODUCT_NAME, productModel.getName()));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, productModel.getProductId()+""));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNumber));

        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_AMOUNT,  String.valueOf(replaceNullWithZero(billAmount))));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, Formatter.formatDoubleByPattern(billAmount, "#,###.00")));
		//responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_LATE_BILL_AMT, String.valueOf(replaceNullWithZero(lateBillAmount))));
		//responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_LATE_BILL_AMT, Formatter.formatDoubleByPattern(lateBillAmount, "#,###.00")));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_PAID, billPaid));

		if(dueDate != null) {
			DateTime nowDate = new DateTime();
			DateTime _dueDate = new DateTime(dueDate).withTime(23, 59, 59, 0);
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, dateTimeFormatter.print(_dueDate)));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATEF, sdf.format(_dueDate.toDate())));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", nowDate.isAfter(_dueDate) ? "1" : "0"));
		} else {
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, "N/A"));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATEF, "N/A"));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", "1"));
		}

        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, replaceNullWithZero(commissionAmountsHolder.getTotalAmount())+""));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())+""));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())+""));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));

        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		return responseBuilder.toString();
	}
}
