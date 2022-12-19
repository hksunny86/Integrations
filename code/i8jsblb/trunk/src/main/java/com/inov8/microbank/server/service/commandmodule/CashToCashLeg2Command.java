package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_BANK_RESPONSE_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PAYMENT_MODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_HELPLINE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_MOB_NO;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_SUPPLIER;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;
import static org.apache.commons.lang.StringEscapeUtils.escapeXml;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.MiniBaseCommand;
import com.inov8.microbank.server.service.integration.vo.CashToCashVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;


public class CashToCashLeg2Command extends MiniBaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected String accountId;
	protected String mobileNo;
	protected String txProcessingAmount;
	protected String pin;
	private String plainTextPin;
	protected String deviceTypeId;
	protected String commissionAmount;
//	protected String totalAmount;
	protected String txAmount;
	protected String cvv;
	protected String tPin;
	
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
	
	private String customer2Name=null;;
	private String customer2MSISN=null;
	private double customer1Balance;
	private double balance;
	private String consumerNo;
	private String recepientWalkinCNIC;
	private String recepientWalkinMobile;
	private String transactionCode;
	private String transactionId;
	
	private String walkinSenderCNIC;
	private String walkinSenderMobile;
	private boolean isTxCodeInvalid;
	private MiniTransactionModel miniTransactionModel;
	
	protected final Log logger = LogFactory.getLog(AccountToCashLeg2Command.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CashToCashLeg2Command.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		
		if(appUserModel.getCustomerId() != null || appUserModel.getRetailerContactId() != null) //retailer condition added by Maqsood - for USSD CW flow
		{	
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					////////////////////////////////////////////////////////////////////////////////////
								///////////////////Expire Transaction Code\\\\\\\\\\\\\\\\\
					////////////////////////////////////////////////////////////////////////////////////
					TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
					transactionCodeModel.setCode(transactionId);
					
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.setBasePersistableModel(transactionCodeModel);
					bWrapper = commonCommandManager.loadTransactionCodeByCode(bWrapper);
					transactionCodeModel = ((TransactionCodeModel)(bWrapper.getBasePersistableModel()));
			
					miniTransactionModel = new MiniTransactionModel();
					miniTransactionModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
			
					SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
					wrapper.setBasePersistableModel(miniTransactionModel);
					wrapper = commonCommandManager.loadMiniTransaction(wrapper);
					
					if(null != wrapper.getCustomList() && null != wrapper.getCustomList().getResultsetList()){
			
						miniTransactionModel = (MiniTransactionModel)wrapper.getCustomList().getResultsetList().get(0);
			
						if(miniTransactionModel.getMiniTransactionStateId().longValue() != MiniTransactionStateConstant.PIN_SENT) {
							isTxCodeInvalid = true;//to avoid updating miniTransactionModel with PIN_SENT status again in exception block.
							throw new CommandException("This transaction is either already claimed or expired.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
						 }
						
						String encryptedPin = miniTransactionModel.getOneTimePin();
						String encryptedTxCode = EncoderUtils.encodeToSha(transactionCode);//TODO decode pin again
						
						Boolean isExpired = !(encryptedPin.equals(encryptedTxCode));
						
						if (isExpired) {//use entered Pin does not match the one sent to BBC 
							
							logger.info("[CashToCashLeg2Command.execute] Pin code not matched. " + 
									"Agent SmartMoneyAccountId : " + accountId + 
									" Agent appUserId: " + appUserModel.getAppUserId() + 
									" Trx ID:" + transactionCodeModel.getCode());
			
							miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
							isTxCodeInvalid = Boolean.TRUE;
							
						} else {
							
							miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.COMPLETED);
						}
						
						bWrapper.setBasePersistableModel(miniTransactionModel);
						bWrapper = this.getCommonCommandManager().updateMiniTransactionRequiresNewTransaction(bWrapper) ;
						
						if(isTxCodeInvalid) {
							
							throw new CommandException(this.getMessageSource().getMessage("CashToCashCommand.invalidTransaction", null, null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
						}					
			
					} else {
			
						logger.info("[CashToCashLeg2Command.execute] GeniLiteTransaction not found. " + 
								"Agent SmartMoneyAccountId : " + accountId + 
								" Agent appUserId: " + appUserModel.getAppUserId() + 
								" Trx ID:" + transactionCodeModel.getCode());
		
						throw new CommandException("Error in loading MiniTransaction",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
					}
					
					///////////////////////////////////////////////////////////////////////////////////
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
								
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("CashToCashLeg2Command.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
						
					AppUserModel customerAppUserModel = new AppUserModel();
					customerAppUserModel.setMobileNo(mobileNo);
					
					/*TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
					transactionCodeModel.setCode(transactionCode);*/
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(transactionCodeModel);
					baseWrapper = commonCommandManager.loadTransactionCodeByCode(baseWrapper);
					transactionCodeModel = ((TransactionCodeModel)(baseWrapper.getBasePersistableModel()));
					
					SearchBaseWrapper sBaesWrapper = new SearchBaseWrapperImpl();
					TransactionModel txModel = new TransactionModel();
					txModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
					sBaesWrapper.setBasePersistableModel(transactionCodeModel);
					sBaesWrapper = commonCommandManager.loadTransactionByTransactionCode(sBaesWrapper);
					txModel = (TransactionModel) sBaesWrapper.getBasePersistableModel();

					sBaesWrapper = new SearchBaseWrapperImpl();
					sBaesWrapper.setBasePersistableModel(txModel);
					sBaesWrapper = commonCommandManager.loadTransactionDetail(sBaesWrapper);
					TransactionDetailModel txDetailModel = (TransactionDetailModel) sBaesWrapper.getBasePersistableModel();
					
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_TO_CASH_LEG_2_TX);
						
					smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
					
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
					
					AppUserModel senderAppUser = new AppUserModel();
					senderAppUser.setNic(txDetailModel.getCustomField7());
					senderAppUser.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
					
					SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
					sBaseWrapper.setBasePersistableModel(senderAppUser);
					sBaseWrapper = (getCommonCommandManager().searchAppUserByExample(sBaseWrapper));
					
					workFlowWrapper.setSenderAppUserModel((AppUserModel)sBaseWrapper.getBasePersistableModel());
					workFlowWrapper.setCustomerAccount(customerAccount);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setProductVO(productVo);
//					workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
//					workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
//					workFlowWrapper.setBillAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
					
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
					
//					put walkin customer details in wrapper to populate in AccountToCashTransaction.doPreStart() from DB.
					workFlowWrapper.setRecipientWalkinCustomerModel(new WalkinCustomerModel(recepientWalkinCNIC));
					workFlowWrapper.setRecipientWalkinSmartMoneyAccountModel(new SmartMoneyAccountModel());
					
					workFlowWrapper.setSenderWalkinCustomerModel(new WalkinCustomerModel());
					workFlowWrapper.getSenderWalkinCustomerModel().setMobileNumber(walkinSenderMobile);
					workFlowWrapper.getSenderWalkinCustomerModel().setCnic(walkinSenderCNIC);
					
					workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
					workFlowWrapper.setTransactionModel(txModel);
					workFlowWrapper.setTransactionDetailModel(txDetailModel);
					workFlowWrapper.setSenderSmartMoneyAccountModel(new SmartMoneyAccountModel());
					workFlowWrapper.setRetailerContactModel(new RetailerContactModel());
					
					StringBuilder logString = new StringBuilder();
					logString.append("[CashToCashLeg2Command.execute] ")
								.append(" Agent SmartMoneyAccountId : " + smartMoneyAccountModel.getSmartMoneyAccountId())
								.append(" Agent appUserId: " + appUserModel.getAppUserId())
								.append(" Trx ID:" + transactionCodeModel.getCode());
					
					logger.info(logString.toString());
					
					//Transaction Detail Master Updates 
					TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel(false);
					transactionDetailMasterModel.setTransactionId(txModel.getTransactionId());
					transactionDetailMasterModel.setTransactionCodeId(txModel.getTransactionCodeId());
					sBaesWrapper = new SearchBaseWrapperImpl();
					sBaesWrapper.setBasePersistableModel(transactionDetailMasterModel);
					sBaesWrapper = commonCommandManager.loadTransactionDetailMaster(sBaesWrapper);
					transactionDetailMasterModel = (TransactionDetailMasterModel)sBaesWrapper.getBasePersistableModel();
					
					transactionDetailMasterModel.setSaleMobileNo(miniTransactionModel.getMobileNo());
					transactionDetailMasterModel.setRecipientMobileNo(recepientWalkinMobile);
					transactionDetailMasterModel.setIsManualOTPin(miniTransactionModel.getIsManualOTPin());
					transactionDetailMasterModel.setTransactionAmount(miniTransactionModel.getTAMT());
					transactionDetailMasterModel.setTotalAmount(miniTransactionModel.getTAMT() + miniTransactionModel.getTPAM());
					transactionDetailMasterModel.setExclusiveCharges(miniTransactionModel.getTPAM());
					transactionDetailMasterModel.setProductName(ProductConstantsInterface.CASH_TRANSFER_NAME);
					workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
					
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
				
					transactionModel = workFlowWrapper.getTransactionModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					discountAmount = workFlowWrapper.getDiscountAmount();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					
					if(((CashToCashVO)workFlowWrapper.getProductVO()).getBalance() !=null && ((CashToCashVO)workFlowWrapper.getProductVO()).getBalance().doubleValue() != 0.0d){
						customer1Balance= ((CashToCashVO)workFlowWrapper.getProductVO()).getBalance();
					}
				
					this.getCommonCommandManager().saveTransactionDetailModel(workFlowWrapper.getTransactionDetailModel());
					this.getCommonCommandManager().saveTransactionModel(workFlowWrapper.getTransactionModel());					
					
				}
				else
				{
					updateMiniTransactionToPinSent(workFlowWrapper);
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("Exception Occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + transactionId + "Exception Details:\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				if(isTxCodeInvalid){
					isTxCodeInvalid = false;
				}else{
					updateMiniTransactionToPinSent(workFlowWrapper);
					
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("Exception Occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + transactionId + "Exception Details:\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				updateMiniTransactionToPinSent(workFlowWrapper);
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("Exception Occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + transactionId + " Exception Details:\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				updateMiniTransactionToPinSent(workFlowWrapper);
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			updateMiniTransactionToPinSent(workFlowWrapper);
			throw new CommandException(this.getMessageSource().getMessage("CashToCashLeg2Command.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CashToCashLeg2Command.execute()");
		}
	}

	private void updateMiniTransactionToPinSent(WorkFlowWrapper wrapper){
		try {
			
			StringBuilder logString = new StringBuilder();
			logString.append("[CashToCashLeg2Command.execute] ")
			.append(" Exception Occured. Going to updateMiniTransactionModel status to PIN_SENT again.")
			.append(" Agent appUserId: " + appUserModel.getAppUserId())
			.append(" Trx ID:" + transactionId);
			logger.info(logString.toString());

			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);//change status to pin sent
			
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel( miniTransactionModel ) ;
			bWrapper = this.getCommonCommandManager().updateMiniTransactionRequiresNewTransaction(bWrapper) ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CashToCashLeg2Command.prepare()");
		}
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		
		recepientWalkinCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC);
		recepientWalkinMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN);
		
		walkinSenderCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_CNIC);
		walkinSenderMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN);
		
		String bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
		
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		
		transactionId= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_ID);
		transactionCode= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
		
		

		plainTextPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN+"_");
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
//		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		cvv = this.decryptPin(cvv);
				
		logger.info("[CashToCashLeg2Command.prepare] Receiving Agent AppUserID : "+ appUserModel.getAppUserId() + 
				"productId : "+productId + 
				"accountId : "+accountId + 
				"deviceTypeId : "+deviceTypeId + 
				"agentMobileNumber : "+mobileNo + 
				"walkinSenderCNIC : "+walkinSenderCNIC + 
				"walkinSenderMobile : "+walkinSenderMobile + 
				"recepientWalkinCNIC : "+recepientWalkinCNIC + 
				"recepientWalkinMobile : "+recepientWalkinMobile + 
				"bankId : "+bankId);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CashToCashLeg2Command.prepare()");
		}

	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of AccountToCashLeg2Command.response()");
		}	
				
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CashToCashLeg2Command.validate()");
		}
		
		if(accountId != null && accountId.length() > 0)
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(CommandFieldConstants.KEY_ACC_ID, accountId);
			CommonCommandManager commonCommandManager = this.getCommonCommandManager();
			
					
			
		}
			
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
		validationErrors = ValidatorWrapper.doRequired(mobileNo,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
				
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(accountId,validationErrors,"Account Id");	
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Amount");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CashToCashLeg2Command.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CashToCashLeg2Command.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		if(UtilityCompanyEnum.contains(String.valueOf(productModel.getProductId()))){
			if(appUserModel!=null && appUserModel.getAppUserTypeId().longValue() == 2L){
				String date=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
				String time=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
				String companyName = UtilityCompanyEnum.lookup(String.valueOf(productModel.getProductId())).name();
				companyName = companyName.replace("_", " ");
				strBuilder.append(this.getMessageSource().getMessage(
						"USSD.CustomerBillPaymentNotification",
						new Object[] { 
								companyName,
								consumerNo,
								transactionModel.getTransactionAmount(),
								transactionModel.getTransactionCodeIdTransactionCodeModel().getCode(),
								date,
								time,
								balance
								}, null));
			}
			
		}
		
		else if(productModel.getProductId().longValue()== 50000L){
			
			String BAMT=Formatter.formatDouble(transactionModel.getTransactionAmount());
			String txID=transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();
			String date=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
			String time=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
			
			String TPAM=txProcessingAmount;
			String cust1Balance=Formatter.formatDouble(customer1Balance);
			/*if(customer2Name!=null && customer2Name.contains(" ")){
				customer2Name=customer2Name.split(" ")[0];
			}*/
			customer2Name=StringUtil.trimNameForNotification(customer2Name,Integer.parseInt(getMessageSource().getMessage("USSD.NotificationNameLength",null,null)));
			String cust1Notification = this.getMessageSource().getMessage(
					"USSD.Customer1A2ANotification",
					new Object[] { 
							customer2Name,
							customer2MSISN,
							BAMT,
							txID,
							date,
							time,
							TPAM,
							cust1Balance
							
							}, null);
			strBuilder.append(cust1Notification);
		}else if(productModel.getProductId().longValue()== 50011L){// customer A/C to Cash transaction
			
			String BAMT=Formatter.formatDouble(transactionModel.getTransactionAmount());
			String txID=transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();
			String date=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
			String time=PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
			
			String TPAM=txProcessingAmount;
			String cust1Balance=Formatter.formatDouble(customer1Balance);
			
			String cust1Notification = this.getMessageSource().getMessage(
					"USSD.Customer1A2CNotification",
					new Object[] { 
							recepientWalkinCNIC,
							BAMT,
							txID,
							date,
							time,
							TPAM,
							cust1Balance
							
							}, null);
			strBuilder.append(cust1Notification);
		}else{
				strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_TRANS)
					.append(TAG_SYMBOL_CLOSE)
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_TRN)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_CODE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_MOB_NO)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(transactionModel.getNotificationMobileNo()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_TYPE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(transactionModel.getTransactionTypeId())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_DATE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(transactionModel.getCreatedOn())
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_DATEF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT))
					//.append(Formatter.formatDate(transactionModel.getCreatedOn()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_TIMEF)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatTime(transactionModel.getCreatedOn()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_PAYMENT_MODE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(escapeXml(replaceNullWithEmpty(smartMoneyAccountModel.getName())))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_PRODUCT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(productModel.getName()))
					.append(TAG_SYMBOL_QUOTE)
							
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_TRN_SUPPLIER)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(productModel.getSupplierIdSupplierModel().getName()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_BANK_RESPONSE_CODE)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(transactionModel.getBankResponseCode()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_FORMATED_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatNumbers(transactionModel.getTotalAmount()))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithZero(transactionModel.getTotalAmount()))
					.append(TAG_SYMBOL_QUOTE);
					
				if(productModel != null && productModel.getHelpLineNotificationMessageModel().getSmsMessageText() != "") 
				{
					strBuilder.append(TAG_SYMBOL_SPACE)
						.append(ATTR_TRN_HELPLINE)
						.append(TAG_SYMBOL_EQUAL)
						.append(TAG_SYMBOL_QUOTE)
						.append(replaceNullWithEmpty(productModel.getHelpLineNotificationMessageModel().getSmsMessageText()))
						.append(TAG_SYMBOL_QUOTE);		
				}
				if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
				{
					strBuilder.append(TAG_SYMBOL_SPACE)
					.append(XMLConstants.ATTR_FORMATTED_DISCOUNT_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(Formatter.formatNumbers(discountAmount))
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)
					.append(XMLConstants.ATTR_DISCOUNT_AMOUNT)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(replaceNullWithEmpty(String.valueOf(discountAmount)))
					.append(TAG_SYMBOL_QUOTE);
					
				}
		
							
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
					logger.debug("End of CashToCashLeg2Command.toXML()");
				}
		}
		return strBuilder.toString();
	}
	
	
	/**
	 * @param _smartMoneyAccountModel
	 * @return
	 */
	private SmartMoneyAccountModel getSmartMoneyAccountModel(SmartMoneyAccountModel _smartMoneyAccountModel) throws CommandException {

		SmartMoneyAccountModel smartMoneyAccountModel = null;
		
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
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return smartMoneyAccountModel;
	}		
	

	public static void main(String[] ar){
		
		/*String encryptedPin = EncoderUtils.encodeToSha("0461");
		System.out.println(encryptedPin);
		*/

		
		
		for (int i = 0; i < 2; i++) {
			String pin = RandomUtils.generateRandom(4, false, true);
			//Encode PIN
			String encryptedPin = EncoderUtils.encodeToSha(pin);
			
			System.out.println(pin + "\t" + encryptedPin);
			
		}
	}
}