package com.inov8.microbank.server.service.commandmodule;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.MiniBaseCommand;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

/**
 * @author Abu Turab
 * @Description Sender Redeem Payment Command, Cash2Cash only
 * @date		27-04-2015
 *
 */

public class SenderRedeemPaymentCommand extends MiniBaseCommand 
{
	protected AppUserModel appUserModel;
	protected RetailerContactModel retailerContactModel;
	protected String productId;
	protected String accountId;
	protected String mobileNo;
	protected String txProcessingAmount;
	protected String pin;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String txAmount;
	protected String cvv;
	
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
	
	private String customer2Name=null;;
	private String customer2MSISN=null;
	private double customer1Balance;
	private double balance;
	private double agentBalance;
	protected double effectiveAmount;
	private String consumerNo;
	
//	added by mudassir
	private String recepientWalkinCNIC;
	private String recepientWalkinMobile;
	private String senderWalkinCNIC;
	private String senderWalkinMobile;
	private String transactionCode;
	private String transactionId;
	private String agentMobile;
	private MiniTransactionModel miniTransactionModel;
	
	private String	oneTimePin, trxDate, trxTime, currentDate, currentTime;

	protected final Log logger = LogFactory.getLog(SenderRedeemPaymentCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SenderRedeemPaymentCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		
		DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
		
		if(appUserModel.getCustomerId() != null || appUserModel.getRetailerContactId() != null) 
		{		
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
					transactionCodeModel.setCode(transactionId);
					
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.setBasePersistableModel(transactionCodeModel);
					bWrapper = commonCommandManager.loadTransactionCodeByCode(bWrapper);
					transactionCodeModel = ((TransactionCodeModel)(bWrapper.getBasePersistableModel()));

					/*miniTransactionModel = new MiniTransactionModel();
					miniTransactionModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
					miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);

					SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
					wrapper.setBasePersistableModel(miniTransactionModel);
					wrapper = commonCommandManager.loadMiniTransaction(wrapper);
					
					if(null != wrapper.getCustomList() && null != wrapper.getCustomList().getResultsetList() && wrapper.getCustomList().getResultsetList().size() > 0){

						miniTransactionModel = (MiniTransactionModel)wrapper.getCustomList().getResultsetList().get(0);
						
						if(miniTransactionModel.getMiniTransactionStateId().longValue() != MiniTransactionStateConstant.PIN_SENT) {
							throw new CommandException("This transaction is either already claimed or expired.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
						}
						if(!miniTransactionModel.getOneTimePin().equals(EncoderUtils.encodeToSha(oneTimePin))){
							throw new CommandException("Invalid transaction code provided.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
						}


					}else{

						logger.info("[SenderRedeemPaymentCommand.execute] MiniTransaction not found. " + 
								"Agent SmartMoneyAccountId : " + accountId + 
								" Agent appUserId: " + appUserModel.getAppUserId() + 
								" Trx ID:" + transactionCodeModel.getCode());
		
						throw new CommandException("This transaction is either already claimed or expired.\n",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
					}*/
					
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin(pin);
								
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("SenderRedeemPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
						

					//Removing duplicate loading of transactionCodeModel
//					baseWrapper.setBasePersistableModel(transactionCodeModel);
//					baseWrapper = commonCommandManager.loadTransactionCodeByCode(baseWrapper);
//					transactionCodeModel = ((TransactionCodeModel)(baseWrapper.getBasePersistableModel()));
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
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.SENDER_REDEEM_TX);
					
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

					workFlowWrapper.setProductModel(productModel);

					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					workFlowWrapper.setRetailerContactModel(retailerContactModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setAppUserModel(appUserModel);

					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
					
					workFlowWrapper.setWalkInCustomerMob(senderWalkinMobile);
					
					workFlowWrapper.setRecipientWalkinSmartMoneyAccountModel(new SmartMoneyAccountModel());
					
					workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
					workFlowWrapper.setTransactionModel(txModel);
					workFlowWrapper.setTransactionDetailModel(txDetailModel);

//					workFlowWrapper.setRetailerContactModel(new RetailerContactModel());
					
					StringBuilder logString = new StringBuilder();
					logString.append("[SenderRedeemPaymentCommand.execute] ")
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

					workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
					/*workFlowWrapper.setMiniTransactionModel(miniTransactionModel);*/
					
					// Following two lines are used to restore status in case of Failure (in WorkflowFacadeImpl)
					workFlowWrapper.setCurrentSupProcessingStatusId(transactionDetailMasterModel.getSupProcessingStatusId());
					workFlowWrapper.setLeg2Transaction(true);

					
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

					effectiveAmount = (Double) workFlowWrapper.getObject(CommandFieldConstants.KEY_TOTAL_AMOUNT);
					transactionModel = workFlowWrapper.getTransactionModel();
					transactionDetailMasterModel = workFlowWrapper.getTransactionDetailMasterModel();
					smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
					transactionCode = workFlowWrapper.getTransactionCodeModel().getCode();
					productModel = workFlowWrapper.getProductModel();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
					agentBalance = workFlowWrapper.getOLASwitchWrapper().getOlavo().getAgentBalanceAfterTransaction();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					
					trxDate = dtf.print(txModel.getCreatedOn().getTime());
					trxTime = tf.print(txModel.getCreatedOn().getTime());
					
					currentDate = dtf.print(new Date().getTime());
					currentTime = tf.print(new Date().getTime());
					
					commonCommandManager.sendSMS(workFlowWrapper);
					
					this.getCommonCommandManager().saveTransactionDetailModel(workFlowWrapper.getTransactionDetailModel());
					this.getCommonCommandManager().saveTransactionModel(workFlowWrapper.getTransactionModel());					
					
				}
				else
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("Exception Occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + transactionId + "Exception Details:\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("Exception Occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + transactionId + "Exception Details:\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("Exception Occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + transactionId + "Exception Details:\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}

				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			throw new CommandException(this.getMessageSource().getMessage("SenderRedeemPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SenderRedeemPaymentCommand.execute()");
		}
	}
	
	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SenderRedeemPaymentCommand.prepare()");
		}
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		recepientWalkinCNIC 	= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
		recepientWalkinMobile 	= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE);
		senderWalkinCNIC		= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
		senderWalkinMobile		= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
		agentMobile				= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOB_NO);
		transactionCode 		= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
		transactionId 			= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_ID);
		oneTimePin 				= this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ONE_TIME_PIN);
		
		logger.info("[SenderRedeemPaymentCommand.prepare] Recipient CNIC:" + recepientWalkinCNIC + " RecepientMobileNo:"  +recepientWalkinMobile + " transaction ID:" + transactionId);
		
		try{
		
			
				CommonCommandManager commonCommandManager = this.getCommonCommandManager();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				RetailerContactModel retailerContactModel = new RetailerContactModel();
				retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
				searchBaseWrapper.setBasePersistableModel(retailerContactModel);
				searchBaseWrapper = (SearchBaseWrapper) this.getCommonCommandManager().loadRetailerContact(searchBaseWrapper);
				
				this.retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();

				smartMoneyAccountModel = new SmartMoneyAccountModel();
				
				searchBaseWrapper = new SearchBaseWrapperImpl();
				smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
				searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				
				searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
				smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
			
				ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
				
				
				if(!validationErrors.hasValidationErrors())
				{
					accountId = smartMoneyAccountModel.getSmartMoneyAccountId().toString();
				}
				else
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			
			
			if( ( accountId == null || "".equals(accountId) ))
			{
				throw new CommandException("AccountId is null",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
			
		}
		catch(Exception ex)
		{
			logger.error("[SenderRedeemPaymentCommand.prepare] Exception: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex) + "\nFor Recipient CNIC:" + recepientWalkinCNIC + " RecepientMobileNo:"  +recepientWalkinMobile + " transaction ID:" + transactionId);
		}

		mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, txAmount); // added coz cash2cashVO populate method using it
		cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
		cvv = this.decryptPin(cvv);


		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SenderRedeemPaymentCommand.prepare()");
		}
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of SenderRedeemPaymentCommand.response()");
		}

		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SenderRedeemPaymentCommand.validate()");
		}
		
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");

		/*validationErrors = ValidatorWrapper.doRequired(oneTimePin,validationErrors,"OTP");*/

		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
				
		if(logger.isDebugEnabled())
		{
			logger.debug("End of SenderRedeemPaymentCommand.validate()");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of SenderRedeemPaymentCommand.toXML()");
		}
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		params.add(new LabelValueBean(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE, replaceNullWithEmpty(senderWalkinMobile)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_S_W_CNIC, replaceNullWithEmpty(senderWalkinCNIC)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE, replaceNullWithEmpty(recepientWalkinMobile)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_R_W_CNIC, replaceNullWithEmpty(recepientWalkinCNIC)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, replaceNullWithEmpty(transactionCode)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, replaceNullWithEmpty(currentDate)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, replaceNullWithEmpty(currentTime)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(effectiveAmount)));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BAL, Formatter.formatNumbers(agentBalance)));
		
		return MiniXMLUtil.createResponseXMLByParams(params);
	}

}