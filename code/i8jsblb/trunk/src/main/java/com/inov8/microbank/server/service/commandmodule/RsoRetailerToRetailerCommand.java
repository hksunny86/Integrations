package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.AGENT_TO_AGENT_TRANSFER_NAME;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_BANK_RESPONSE_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_CODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PAYMENT_MODE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_MOB_NO;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_SUPPLIER;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.INOV8_SUPPLIER;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.AgentToAgentTransferVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;


public class RsoRetailerToRetailerCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String mobileNo;
	protected String txAmount;
	protected String deviceTypeId;
	protected String allPayId;
	protected String productId;
	protected ProductModel productModel;
	protected BaseWrapper preparedBaseWrapper;
	protected String pin;
	
	
	TransactionModel transactionModel;
	String successMessage;
	WorkFlowWrapper workFlowWrapper;
	
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
	double balance=0D;
	
	protected final Log logger = LogFactory.getLog(RetailerToRetailerTransactionCommand.class);
	
	@Override
	public void execute() throws CommandException 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of RetailerToRetailerTransactionCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		if(appUserModel.getRetailerContactId() != null)
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				if(!validationErrors.hasValidationErrors())
				{
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
					
					
					BaseWrapper bWrapper = new BaseWrapperImpl();
					AppUserModel receiverAppUserModel = new AppUserModel();
					receiverAppUserModel.setMobileNo(mobileNo);
					receiverAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
					bWrapper.setBasePersistableModel(receiverAppUserModel);
					bWrapper = this.getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
					
					if(null != bWrapper.getBasePersistableModel())
					{
						UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
						allPayId = userDeviceAccountsModel.getUserId();
					}
					else
					{
						throw new CommandException(this.getMessageSource().getMessage("allpay.user.not.exist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					
					// Loading product Module and VO for use in the transaction class
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					productModel = new ProductModel();
					TransactionModel transactionModel = null;
					productModel.setProductId(Long.parseLong(productId));
					baseWrapper.setBasePersistableModel(productModel);
					baseWrapper = commonCommandManager.loadProduct(baseWrapper);
					productModel = (ProductModel)baseWrapper.getBasePersistableModel();
					
					if(productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())) 
					{
						ProductVO productVO = commonCommandManager.loadProductVO(preparedBaseWrapper);
						if(productVO == null)
						{
							throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
						
						
						productVO.populateVO(productVO, preparedBaseWrapper);
						
						if(null != bWrapper.getBasePersistableModel())
						{
							UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
							((AgentToAgentTransferVO)productVO).setAgentId(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId());
						}
						
						workFlowWrapper.setProductModel(productModel);
						BillPaymentProductDispenser billSale = (BillPaymentProductDispenser)commonCommandManager.loadProductDispense(workFlowWrapper);
						workFlowWrapper.setProductDispenser(billSale);
						workFlowWrapper.setAccountInfoModel(accountInfoModel);
						workFlowWrapper.setProductVO(productVO);
//						workFlowWrapper = commonCommandManager.getBillInfo(billSale, workFlowWrapper);
						
					}
					
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					AppUserModel toRetailerAppUserModel = new AppUserModel();
//					toRetailerAppUserModel.setMobileNo(mobileNo);
					UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
					userDeviceAccountsModel.setUserId(allPayId);
					searchBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);
					searchBaseWrapper = commonCommandManager.loadUserDeviceAccounts(searchBaseWrapper);
					CustomList<UserDeviceAccountsModel> list = searchBaseWrapper.getCustomList();
					if(null != list.getResultsetList() && ! list.getResultsetList().isEmpty())
					{
						userDeviceAccountsModel = list.getResultsetList().get(0);
					}
					else
					{
						logger.info("[RetailerToRetailerTransactionCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
								". Exception Msg:" + this.getMessageSource().getMessage("allpay.user.not.exist", null,null) + 
								". Receiver Mobile No:" + mobileNo + ". Product ID:" + productId);

						throw new CommandException(this.getMessageSource().getMessage("allpay.user.not.exist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					
					toRetailerAppUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();
				
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.RSO_TO_RET_TX);
					
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					
					ProductModel productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
//					toRetailerAppUserModel.setMobileNo(distributorOrRetailer.getMobileNo());
					SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					SmartMoneyAccountModel receivingSmartMoneyAccountModel = new SmartMoneyAccountModel();
					receivingSmartMoneyAccountModel.setRetailerContactId(toRetailerAppUserModel.getRetailerContactId());
					
					
					
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setReceivingSmartMoneyAccountModel(receivingSmartMoneyAccountModel);
								
					workFlowWrapper.setToRetailerContactAppUserModel(toRetailerAppUserModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setDistOrRetAppUserModel(toRetailerAppUserModel);
					workFlowWrapper.setProductModel(productModel);
					
					logger.info("[RetailerToRetailerTransactionCommand.execute] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
								". Receiver Mobile No:" + mobileNo + ". Product ID:" + productId);

					commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
					transactionModel = workFlowWrapper.getTransactionModel();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					this.workFlowWrapper = workFlowWrapper;
				}
				else
				{
					logger.info("[RetailerToRetailerTransactionCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							". Validation Errors:" + validationErrors.getErrors() + 
							". Receiver Mobile No:" + mobileNo + ". Product ID:" + productId);

					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(Exception ex)
			{
				logger.info("[RetailerToRetailerTransactionCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
						". Exception Msg:" + ex.getMessage() + 
						". Receiver Mobile No:" + mobileNo + ". Product ID:" + productId);

				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.info("[RetailerToRetailerTransactionCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
					". Exception Msg:" + this.getMessageSource().getMessage("retailerToRetailerTransactionCommand.invalidAppUserType", null,null) + 
					". Receiver Mobile No:" + mobileNo + ". Product ID:" + productId);

			throw new CommandException(this.getMessageSource().getMessage("retailerToRetailerTransactionCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of RetailerToRetailerTransactionCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of RetailerToRetailerTransactionCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		if(logger.isDebugEnabled())
		{
			logger.debug("End of RetailerToRetailerTransactionCommand.prepare()");
		}
		
		logger.info("[RetailerToRetailerTransactionCommand.prepare] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Receiver Mobile No:" + mobileNo + ". Product ID:" + productId);
		preparedBaseWrapper = baseWrapper;
	}

	@Override
	public String response() 
	{
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of RetailerToRetailerTransactionCommand.validate()");
		}
//		validationErrors = ValidatorWrapper.doRequired(mobileNo,validationErrors,"Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of RetailerToRetailerTransactionCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of RetailerToRetailerTransactionCommand.toXML()");
		}
		StringBuilder strBuilder = new StringBuilder();
		
		if (50014L==(workFlowWrapper.getProductModel().getProductId()).longValue() && Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue() ) {

//			String customerName= ((P2PVO)workFlowWrapper.getProductVO()).getCustomerName();
//			customerName=StringUtil.trimNameForNotification(customerName,Integer.parseInt(getMessageSource().getMessage("USSD.NotificationNameLength",null,null)));
			
			String TAMT=Formatter.formatDouble(workFlowWrapper.getTransactionModel().getTotalAmount());
//			String txID=replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
			String agentMSISDN = workFlowWrapper.getToRetailerContactAppUserModel().getMobileNo();
			String date=dtf.print(new DateTime());
			String time=tf.print(new LocalTime());
//			String agentBalance=Formatter.formatDouble(workFlowWrapper.getSwitchWrapper().getAgentBalance());
			strBuilder.append(this.getMessageSource().getMessage(
					"USSD.AgentToAgentTransferNotification",
					new Object[] { 
							TAMT,
							agentMSISDN,
							date,
							time,
							"0.00",
							workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(),
							((AgentToAgentTransferVO)workFlowWrapper.getProductVO()).getBalance()
							},
							null));
		}
		else
		{
		
		
		
		
		strBuilder.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_MOB_NO)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithEmpty(workFlowWrapper.getTransactionModel().getNotificationMobileNo()))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TYPE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(workFlowWrapper.getTransactionModel().getTransactionTypeId())
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_DATE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(workFlowWrapper.getTransactionModel().getCreatedOn())
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_DATEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.LONG_DATE_FORMAT))
			//.append(Formatter.formatDate(transactionModel.getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_PRODUCT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(AGENT_TO_AGENT_TRANSFER_NAME)
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_SUPPLIER)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(INOV8_SUPPLIER)
			.append(TAG_SYMBOL_QUOTE)
						
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_TRN_TIMEF)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatTime(workFlowWrapper.getTransactionModel().getCreatedOn()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_PAYMENT_MODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT)			
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_BANK_RESPONSE_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append("00")
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_FORMATED_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(Formatter.formatNumbers(workFlowWrapper.getTransactionModel().getTotalAmount()))
			.append(TAG_SYMBOL_QUOTE)
		
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(replaceNullWithZero(workFlowWrapper.getTransactionModel().getTotalAmount()))
			.append(TAG_SYMBOL_QUOTE)
			
			.append(TAG_SYMBOL_CLOSE)
			
			.append(TAG_SYMBOL_SPACE)
			.append(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append("0.00")
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_SPACE)
			
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRN)
			.append(TAG_SYMBOL_CLOSE)
			
			
			
//			.append(TAG_SYMBOL_OPEN)
//			.append(TAG_PARAM)
//			.append(TAG_SYMBOL_SPACE)
//			.append(ATTR_PARAM_NAME)
//			.append(TAG_SYMBOL_EQUAL)
//			.append(TAG_SYMBOL_QUOTE)
//			.append(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT)
//			.append(TAG_SYMBOL_QUOTE)
//			.append(TAG_SYMBOL_CLOSE)
//			
//			.append("0.00")
//			
//			.append(TAG_SYMBOL_OPEN)
//			.append(TAG_SYMBOL_SLASH)
//			.append(TAG_PARAM)
//			.append(TAG_SYMBOL_CLOSE)
			
			
			
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH)
			.append(TAG_TRANS)
			.append(TAG_SYMBOL_CLOSE);
		
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of RetailerToRetailerTransactionCommand.toXML()");
		}
		return strBuilder.toString();
	}


}
