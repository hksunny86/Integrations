package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.PortalConstants.SCHEDULER_APP_USER_ID;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.PaymentModeConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.MiniBaseCommand;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.LogModel;



public class BulkPaymentCommand extends MiniBaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected String txProcessingAmount;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String totalAmount;
	protected String txAmount;

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
	
	String recepientWalkinCNIC;
	String recepientWalkinMobile;
	Boolean isLimitApplicable;
	private String paymentType;
	private String bulkDisbursementsId;
	
	protected final Log logger = LogFactory.getLog(BulkPaymentCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of BulkPaymentCommand.execute()");
		}
		
		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);		
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		
		try
		{
			productModel = new ProductModel();
			productModel.setProductId(Long.parseLong(productId));
			ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
			if(productVo == null){
				throw new CommandException(this.getMessageSource().getMessage("BulkPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
				
			workFlowWrapper.setWalkInCustomerCNIC(recepientWalkinCNIC);
			workFlowWrapper.setWalkInCustomerMob(recepientWalkinMobile);
			workFlowWrapper.setWalkinLimitApplicable(isLimitApplicable);
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BULK_PAYMENT_TX);
				
			DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
			deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
			
			workFlowWrapper.setProductModel(productModel);
			workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
			workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
			workFlowWrapper.setProductVO(productVo);
			workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
			workFlowWrapper.setAppUserModel(appUserModel);
			workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
			workFlowWrapper.setSegmentModel(segmentModel);
			workFlowWrapper.setCommissionSettledOnLeg2(true);

//			put walkin customer details in wrapper to populate in BulkPaymentTransaction.doPreStart() from DB.
			workFlowWrapper.setRecipientWalkinCustomerModel(new WalkinCustomerModel(recepientWalkinCNIC));
			workFlowWrapper.setRecipientWalkinSmartMoneyAccountModel(new SmartMoneyAccountModel());
			workFlowWrapper.setBulkDisbursmentsId(Long.parseLong(bulkDisbursementsId));
			
			workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
		
			transactionModel = workFlowWrapper.getTransactionModel();
			smartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
			
			productModel = workFlowWrapper.getProductModel();
			userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
			discountAmount = workFlowWrapper.getDiscountAmount();
			successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
			
			BaseWrapper tempBaseWrapper = new BaseWrapperImpl();
			tempBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

			LogModel logModel = new LogModel();
			logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
			logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

			String pin = CommonUtils.generateOneTimePin(5);//RandomUtils.generateRandom(4, Boolean.FALSE, Boolean.TRUE);
			String encryptedPin = EncoderUtils.encodeToSha(pin);
			MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
			
			miniTransactionModel.setTransactionCodeIdTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
			miniTransactionModel.setTransactionCodeId(transactionModel.getTransactionCodeIdTransactionCodeModel().getTransactionCodeId());
			miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_BULK_PAYMENT)) ;
			miniTransactionModel.setAppUserId( ThreadLocalAppUser.getAppUserModel().getAppUserId() ) ;
			miniTransactionModel.setTimeDate(new Date()) ;
			miniTransactionModel.setMobileNo( this.recepientWalkinMobile );
			miniTransactionModel.setSmsText( recepientWalkinMobile + " " + txAmount ) ;
			miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT) ;
			miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
			miniTransactionModel.setOneTimePin(encryptedPin);
			try
			{
				miniTransactionModel.setCAMT(transactionModel.getTotalCommissionAmount()) ;
				miniTransactionModel.setBAMT(transactionModel.getTotalAmount()) ;
				miniTransactionModel.setTAMT(Double.parseDouble(txAmount)) ;
				miniTransactionModel.setTPAM(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()) ;
				baseWrapper.setBasePersistableModel( miniTransactionModel ) ;
				baseWrapper = this.getCommonCommandManager().saveMiniTransaction(baseWrapper) ;
			}
			catch (Exception e)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[BulkPaymentCommand.execute()] Exception occured during saving MiniTransactionModel for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId());
					logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
				}
			}

			String brandName = MessageUtil.getMessage("jsbl.brandName");
			//*************************************************end miniTransaction**************************************************/
			//bulkPayment.recipient.Leg1SMS={0}\nTrx Id {1}\nTrx Code {2}\nRs. {3} received as Payment of {4} at {5} on {6}. Pls. collect from Brand Agent and bring original CNIC and copy.
			
			String recipientSmsText = this.getMessageSource().getMessage(
					"bulkPayment.recipient.Leg1SMS",
					new Object[] {
							brandName,
							workFlowWrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(transactionModel.getTransactionAmount() - workFlowWrapper.getOLASwitchWrapper().getInclusiveChargesApplied()),
							//Formatter.formatDouble(workFlowWrapper.getOLASwitchWrapper().getInclusiveChargesApplied()),
							paymentType,
							PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							brandName,
					},
					null);

			try{
				sendSMSToUser(recepientWalkinMobile, recipientSmsText); // SMS 2 sent to Walkin Customer Recipient
			}catch (Exception ex) {
				logger.error("[BulkPaymentCommand.execute] Exception occured while sending SMS to Mobile No: " + recepientWalkinMobile + ". Logged in AppUserId:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Exception: " + ex.getMessage());
			}
				
		}catch(Exception ex){
			if(logger.isErrorEnabled()){
				logger.error("[BulkPaymentCommand.execute()] Exception occured for smartMoneyAccountId:" + smartMoneyAccountModel.getSmartMoneyAccountId() + " \n" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			}
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		
	}

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setAppUserId(SCHEDULER_APP_USER_ID);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.SCHEDULER);
		ThreadLocalAppUser.setAppUserModel(appUserModel);

		this.baseWrapper = baseWrapper;
		this.appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		recepientWalkinCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC);
		recepientWalkinMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN);
		String walkinLimitStr =  this.getCommandParameter(baseWrapper, CommandFieldConstants.WALK_IN_LIMIT_APPLIED);
		if ( ! StringUtils.isEmpty(walkinLimitStr)) {
			isLimitApplicable = Boolean.valueOf(walkinLimitStr);
		}
		
		paymentType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_TYPE);
		bulkDisbursementsId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BULK_DISBURSMENTS_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		
		logger.info("[BulkPaymentCommand.prepare] Recipient CNIC:" + recepientWalkinCNIC + " RecepientMobileNo:"  + recepientWalkinMobile + ". Limit Applicable:" + walkinLimitStr +"  bulkDisbursementsId:"+bulkDisbursementsId);
		
	}

	@Override
	public String response() 
	{
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");
		validationErrors = ValidatorWrapper.doRequired(bulkDisbursementsId,validationErrors,"Bulk Disbursements ID");
		
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(bulkDisbursementsId,validationErrors,"Bulk Disbursements ID");	
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		StringBuilder strBuilder = new StringBuilder();
		try {
			strBuilder.append(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
		} catch (Exception e) {
			logger.error("[BulkPaymentCommand.toXML] Error in populaing transaction Code.");
			e.printStackTrace();
		}
		
		return strBuilder.toString();
	}

}