package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.disbursement.job.AcHolderBulkDisbursementsThread;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.MiniBaseCommand;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.dispenser.CashToCashDispenser;
import com.inov8.microbank.server.service.integration.vo.CashToCashVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManagerImpl;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class CashToCashCommand extends MiniBaseCommand {

	private final Log logger = LogFactory.getLog(CashToCashCommand.class);	
	
	private AppUserModel appUserModel = null;
	private BaseWrapper preparedBaseWrapper = null;

	private String productId = null;
	private String walkinSenderCNIC = null;
	private String walkinSenderMobile = null;
	private String recepientWalkinCNIC = null;
	private String recepientWalkinMobile = null;
	private String pin;
	private String senderCity,receiverCity;
	
	private BaseWrapper baseWrapper = null;
	private CashToCashVO cashToCashVO = null;
	private CommissionAmountsHolder commissionAmountsHolder;
	private CommissionWrapper commissionWrapper;	
	private Double txAmount = 0.0;
	private Double commissionAmount = 0.0D;
	private Double txProcessingAmount = 0.0D;

	private String strTxAmount;
	private String strCommissionAmount;
	private String strTxProcessingAmount;

	private WorkFlowWrapper workFlowWrapper = null;
	private ProductModel productModel = null;
	private String manualOTPin;
    private String isBVSRequired;
    private String transactionPurposeCode;

	RetailerContactModel fromRetailerContactModel;

	public CashToCashCommand() {		
		baseWrapper = new BaseWrapperImpl();
		workFlowWrapper = new WorkFlowWrapperImpl();
	}
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {
		transactionPurposeCode = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TRANS_PURPOSE_CODE);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		preparedBaseWrapper = baseWrapper;
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		walkinSenderCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
		walkinSenderMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
        recepientWalkinCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
        recepientWalkinMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE);
		appUserModel = ThreadLocalAppUser.getAppUserModel();
        isBVSRequired = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_BVS_REQUIRED);
		senderCity = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SENDER_CITY);
		receiverCity = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVER_CITY);



		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		pin = StringUtil.replaceSpacesWithPlus(pin);    	
    	
		strTxAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		if(strTxAmount != null && !strTxAmount.isEmpty()) {
			txAmount = Double.valueOf(strTxAmount);
		}
		
		strTxProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
    	
		if(strTxProcessingAmount != null && !strTxProcessingAmount.isEmpty()) {
			txProcessingAmount = Double.valueOf(strTxProcessingAmount);
    	}
		
		strCommissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
		bWrapper.setBasePersistableModel(retailerContactModel);
		
		try{
			bWrapper = getCommonCommandManager().loadRetailerContact(bWrapper);
			
			this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
			
		}catch(Exception ex){
			logger.error("[CashToCashCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
		}
		
		logger.info("[CashToCashCommand.prepare] Sender CNIC:" + walkinSenderCNIC + " Sender Mobile:" + walkinSenderMobile+ " Recipient CNIC:" + recepientWalkinCNIC + " RecepientMobileNo:"  + recepientWalkinMobile + " Agent AppUserID:" + appUserModel.getAppUserId());
		
	}

	
	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		//validationErrors = ValidatorWrapper.doRequired(transactionPurposeCode,validationErrors,"Transaction Purpose");
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(strTxProcessingAmount,validationErrors,"Tx Processing Amount");
		validationErrors = ValidatorWrapper.doRequired(strCommissionAmount,validationErrors,"Commission Amount");
		validationErrors = ValidatorWrapper.doRequired(strTxAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(walkinSenderMobile,validationErrors,"Sender Mobile No");
		validationErrors = ValidatorWrapper.doRequired(walkinSenderCNIC,validationErrors,"Sender CNIC");
        validationErrors = ValidatorWrapper.doValidateCNIC(walkinSenderCNIC,validationErrors,"Sender CNIC");
		validationErrors = ValidatorWrapper.doRequired(recepientWalkinMobile,validationErrors,"Recipient Mobile No");
		validationErrors = ValidatorWrapper.doRequired(recepientWalkinCNIC,validationErrors,"Recipient CNIC");
        validationErrors = ValidatorWrapper.doValidateCNIC(recepientWalkinCNIC,validationErrors,"Recipient CNIC");

		validationErrors = ValidatorWrapper.doRequired(receiverCity,validationErrors,"Recipient City");


		if(!validationErrors.hasValidationErrors())	{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		
		if(!validationErrors.hasValidationErrors())	{
			if(walkinSenderCNIC.equals(recepientWalkinCNIC)){
				logger.error("!!!!!!!! [CashToCashCommand.validate] Sender & Recipient CNIC must be different.");
				validationErrors = ValidatorWrapper.addError(validationErrors, "Sender & Recipient CNIC must be different.");
			}
		}
		
		//***************************************************************************************************************************
		//									Check if sender or receiver cnic is blacklisted
		//***************************************************************************************************************************
        if(!validationErrors.hasValidationErrors()) {
            if(this.getCommonCommandManager().isCnicBlacklisted(walkinSenderCNIC)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(),ErrorCodes.TERMINATE_EXECUTION_FLOW,ErrorLevel.MEDIUM,new Throwable());
                
            }
            if(this.getCommonCommandManager().isCnicBlacklisted(recepientWalkinCNIC)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(),ErrorCodes.TERMINATE_EXECUTION_FLOW,ErrorLevel.MEDIUM,new Throwable());
            }
        }
		//***************************************************************************************************************************
		
		try{
			if(!validationErrors.hasValidationErrors() && getCommonCommandManager().checkP2PTransactionsOnCNIC(recepientWalkinCNIC,recepientWalkinMobile)){
				String error = this.getMessageSource().getMessage("MONEYTRANSFER.EXISTING_P2P_WITH_DIFF_MOB", null,null);
				logger.error("[CashToCashCommand.validate] validation failed due to Reason: " + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
		}catch(FrameworkCheckedException fe){
			fe.printStackTrace();
		}

		return validationErrors;
	}
	
	
	@Override
	public void execute() throws CommandException {		
		
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		if(appUserModel.getRetailerContactId() != null)
		{
			try{
				String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
				// Velocity validation - start
				BaseWrapper bWrapper = new BaseWrapperImpl();
				bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
				bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
				bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, fromRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
				bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, fromRetailerContactModel.getDistributorLevelId());
				bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, txAmount);
				bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, fromRetailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
				bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//				bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
				boolean result = getCommonCommandManager().checkVelocityCondition(bWrapper);
				// Velocity validation - end
		
				
				SegmentModel segmentModel = new SegmentModel();
				segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);		
				
				AccountInfoModel accountInfoModel = new AccountInfoModel();
				accountInfoModel.setOldPin(pin);
				
				DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
				deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
		
				TransactionModel transactionModel = new TransactionModel();
				transactionModel.setTransactionAmount(txAmount);
				
				TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
				transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_TO_CASH);
				
				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
				smartMoneyAccountModel.setActive(Boolean.TRUE);
				smartMoneyAccountModel = getSmartMoneyAccountModel(smartMoneyAccountModel);
						
				if(!isActiveAppUser(appUserModel)) {
					
					if(smartMoneyAccountModel != null &&
							appUserModel.getRetailerContactId().longValue() == 
									smartMoneyAccountModel.getRetailerContactId().longValue()) {					
							
						productModel = loadProduct(productId);								
							
						if(productModel != null) {

							//Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
							//this.getCommonCommandManager().createOrUpdateWalkinCustomer(walkinSenderCNIC, walkinSenderMobile, null);
							//this.getCommonCommandManager().createOrUpdateWalkinCustomer(recepientWalkinCNIC, recepientWalkinMobile, null);

							//Commited for small instance
							/*this.getCommonCommandManager().createNewWalkinCustomer(walkinSenderCNIC, walkinSenderMobile, null);
							this.getCommonCommandManager().createNewWalkinCustomer(recepientWalkinCNIC, recepientWalkinMobile, null);*/

							RetailerContactModel retailerContactModel = loadRetailerContactModel(appUserModel.getRetailerContactId());
							RetailerModel retailerModel = loadRetailerModel(retailerContactModel.getRetailerId());
							DistributorModel distributorModel = loadDistributorModel(retailerModel.getDistributorId());
							//AgentMerchantDetailModel agentMerchantDetailModel= loadAgentMerchant(retailerModel.getDistributorId(),retailerModel.getRetailerId());
								
							transactionModel.setFromRetContactName(retailerContactModel.getBusinessName());
							
							workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
							workFlowWrapper.setWalkInCustomerMob(walkinSenderMobile);
							workFlowWrapper.setWalkInCustomerCNIC(walkinSenderCNIC);
							workFlowWrapper.setProductModel(productModel);
							workFlowWrapper.setAppUserModel(appUserModel);					
							workFlowWrapper.setRetailerContactModel(retailerContactModel);
							workFlowWrapper.setDistributorModel(distributorModel);
							workFlowWrapper.setRetailerModel(retailerModel);

							
							workFlowWrapper.setTxProcessingAmount(txProcessingAmount);
							workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
							workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
							workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
							workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);//added by mudassir for rollback() in CashToCashTransactin.java
							workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
							workFlowWrapper.setAccountInfoModel(accountInfoModel);
							workFlowWrapper.setRecipientWalkinCustomerModel(getWalkinCustomerModel(new WalkinCustomerModel(recepientWalkinCNIC)));
							workFlowWrapper.setSenderWalkinCustomerModel(getWalkinCustomerModel(new WalkinCustomerModel(walkinSenderCNIC)));
							workFlowWrapper.setRecipientWalkinSmartMoneyAccountModel(getSmartMoneyAccountModel(new WalkinCustomerModel(recepientWalkinCNIC)));
		//					workFlowWrapper.setSenderWalkinSmartMoneyAccountModel(getSmartMoneyAccountModel(new WalkinCustomerModel(walkinSenderCNIC)));
							workFlowWrapper.setTransactionModel(transactionModel);		
							workFlowWrapper.setTransactionTypeModel(transactionTypeModel);	
							workFlowWrapper.setSegmentModel(segmentModel);
		
							commissionAmount = getCommissionAmount(workFlowWrapper);						
								
							workFlowWrapper.setTransactionAmount(txAmount);
							workFlowWrapper.setTotalCommissionAmount(commissionAmount);
							workFlowWrapper.setTotalAmount(commissionAmount + txAmount);							
							workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
							workFlowWrapper.setCommissionWrapper(commissionWrapper);
		
							cashToCashVO = (CashToCashVO) loadProductVO(preparedBaseWrapper);
		
							if(cashToCashVO != null) {
									
								cashToCashVO.setWalkInCNIC(this.recepientWalkinCNIC);
								cashToCashVO.setCommissionAmount(commissionAmount);
								cashToCashVO.setTxAmount(txAmount);
								cashToCashVO.setTotalAmount(txAmount + commissionAmount);
							}												
		
							workFlowWrapper.setProductVO(cashToCashVO);
							
		//					if (StringUtils.isNotEmpty(manualOTPin)) {//checking if One Time Pin is given by Walkin Customer (for using in TransactionDetailMaster reports) 
		//						workFlowWrapper.putObject(CommandFieldConstants.KEY_MAN_OT_PIN, Boolean.TRUE);
		//					}
							
							getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(), deviceTypeModel.getDeviceTypeId(), txAmount, productModel, null, workFlowWrapper.getHandlerModel());
							
							StringBuilder logString = new StringBuilder();
							logString.append("[CashToCashCommand.execute] ")
										.append(" Sender Agent SmartMoneyAccountId : " + smartMoneyAccountModel.getSmartMoneyAccountId())
										.append(" Sender Agent appUserId: " + appUserModel.getAppUserId());
										
							logger.info(logString.toString());
							
							/*Generate Pin and store in MiniTransaction*/
							String pin = null;
							if(manualOTPin != null && ! "".equals(manualOTPin)){//if Pin is manually set by customer, use this.
								pin = manualOTPin;
							}else{//generate random pin.
								pin = CommonUtils.generateOneTimePin(5);
							}
							//Encode PIN
							String encryptedPin = EncoderUtils.encodeToSha(pin);
							workFlowWrapper.setOneTimePin(pin);
							workFlowWrapper.setMiniTransactionModel(populateMiniTransactionModel(encryptedPin));
							workFlowWrapper.setCommissionSettledOnLeg2(true);
							
							//************** BVS Check - Start
							boolean bvsReq = isBVSRequired.equals("1")? true : false;
                            workFlowWrapper.setSenderBvs(bvsReq);
                            //************* BVS Check - End

							workFlowWrapper.putObject(CommandFieldConstants.KEY_SENDER_CITY,senderCity);
							workFlowWrapper.putObject(CommandFieldConstants.KEY_RECEIVER_CITY,receiverCity);
							TransactionPurposeModel transactionPurposeModel = new TransactionPurposeModel();
							transactionPurposeModel.setCode(transactionPurposeCode);
							List<TransactionPurposeModel> list = null;
							if(transactionPurposeCode != null && !transactionPurposeCode.equals(""))
								list = commonCommandManager.getTransactionPurposeDao().findByExample(transactionPurposeModel).getResultsetList();
							if(list != null && !list.isEmpty()){
								workFlowWrapper.putObject("TRANS_PURPOSE_MODEL",list.get(0));
							}
							workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);
								
							if(workFlowWrapper.getTransactionCodeModel() != null) {
									
								workFlowWrapper.getTransactionModel().setTransactionCodeIdTransactionCodeModel(workFlowWrapper.getTransactionCodeModel());
									
								workFlowWrapper.getTransactionModel().setTransactionCodeId(workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
							}
							
							txProcessingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount();
							strTxProcessingAmount = txProcessingAmount.toString();
								
							// already sent in transaction
//							this.sendSMS(workFlowWrapper);
							getCommonCommandManager().sendSMS(workFlowWrapper);
//							getCommonCommandManager().intimateAppInSnapForSendMoneyRequiresNewTransaction(workFlowWrapper);

						}					
			
					}
						
				}
				
			}catch(FrameworkCheckedException ex){
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}catch(WorkFlowException wex){
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}catch(Exception ex){
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}else{
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
	}
	
/*    private void verifyWalkinCustomerBiometric(WorkFlowWrapper workFlowWrapper) throws Exception {
        CommandManager commandManager = this.getCommandManager();
        preparedBaseWrapper.putObject(CommandFieldConstants.KEY_BVS_CNIC, walkinSenderCNIC);
        preparedBaseWrapper.putObject(CommandFieldConstants.KEY_BVS_MSISDN, walkinSenderMobile);
        preparedBaseWrapper.putObject(CommandFieldConstants.KEY_CNIC, recepientWalkinCNIC);
        preparedBaseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, recepientWalkinMobile);
        preparedBaseWrapper.putObject(CommandFieldConstants.KEY_AREA_NAME, "**** Area ****"); //workFlowWrapper.getTaxRegimeModel().getName());
        preparedBaseWrapper.putObject(CommandFieldConstants.KEY_REMITTANCE_TYPE, RemittanceTypeConstant.MONEY_TRANSFER_SENDER);
        preparedBaseWrapper.putObject(CommandFieldConstants.KEY_TEMPLATE_TYPE, MessageUtil.getMessage("TEMPLATE_FORMAT"));
        commandManager.executeCommand(preparedBaseWrapper, CommandFieldConstants.CMD_BIOMETRIC_VERIFICATION);
    }
*/
	/**
	 * @param workFlowWrapper
	 * @param pin
	 */
	private void sendSMS(WorkFlowWrapper workFlowWrapper) throws CommandException {

//		logger.debug(workFlowWrapper.getTransactionCodeModel().getCode()+transactionPin);	

		Double trxAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount();
		Double commissoinAmount = ((CashToCashVO)workFlowWrapper.getProductVO()).getCommissionAmount();
//		Raqam: Rs. {0}\nCharges: Rs. {1} Dukaan per mukamal adaige: Rs. {2} Trx ID: {3} Raqam nikalwane ka code: {4}\n{5} {6}\nShukriya - Timepey!
		Object[] customerSmsArguments = new Object[] {
				Formatter.formatDouble(trxAmount),//{0}
				Formatter.formatDouble(commissoinAmount),//{1}
				Formatter.formatDouble(trxAmount + commissoinAmount),//{2}
				workFlowWrapper.getTransactionCodeModel().getCode(),//{3}						
//				transactionPin, //{4}
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),//{5}
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),//{6}
			};
		
		/*Cash Transfer Rs\: {0}\nthrough ({1})\nto CNIC ({2}), on {3} {4}\nCharges Rs\: {5}\nTx Code\: {6}\nTrx ID\: {7}
		  Object[] customerSmsArguments = new Object[] {
				Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()),//{0}
				workFlowWrapper.getAppUserModel().getMobileNo(),//{1}
				workFlowWrapper.getRecipientWalkinCustomerModel().getCnic(),//{2}
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),//{3}
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),//{4}
				Formatter.formatDouble(((CashToCashVO)workFlowWrapper.getProductVO()).getCommissionAmount()),//{5}
				transactionPin, //{6}
				workFlowWrapper.getTransactionCodeModel().getCode()//{7}						
			};*/

//		Cash Transfer\: Rs {0}*\nfor CNIC ({1})\n{2} {3}\n Trx ID\:{4}\n*Charges applied
		Object[] agentSmsArguments = new Object[] {
				Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()),
				walkinSenderCNIC, 
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),//{3}
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),//{4}
				workFlowWrapper.getTransactionCodeModel().getCode(), Formatter.formatDouble(workFlowWrapper.getTransactionModel().getTotalCommissionAmount())		
			};
		
//		Please collect Rs {0} from the merchant. This amount was sent by CNIC {1}
		Object[] recipientWalkinSmsArguments = new Object[] {
				Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount()),
				walkinSenderCNIC,
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),//{3}
				PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),//{4}
				workFlowWrapper.getTransactionCodeModel().getCode()
			};

		try {

			//sending SMS to Walkin Customer Recepient.			
			String smsText = this.getMessageSource().getMessage("USSD.CustomerCashToCashSMS", customerSmsArguments, null);
			this.sendSMSToUser(walkinSenderMobile, smsText);
			
			//sending SMS to Agent.
			smsText = this.getMessageSource().getMessage("USSD.AgentCashToCashSMS", agentSmsArguments, null);	
			this.sendSMSToUser(workFlowWrapper.getAppUserModel().getMobileNo(), smsText);

			//sending SMS to Walkin Customer Recipient.
			smsText = this.getMessageSource().getMessage("USSD.WalkinRecipientCashToCashLeg1SMS", recipientWalkinSmsArguments, null);	
			this.sendSMSToUser(recepientWalkinMobile, smsText);

			
		} catch (CommandException e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
//			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
	}
		
	
	/**
	 * @param mobileNumber
	 * @return
	 */
	private AppUserModel loadAppUserByCNICAndType(String walkinSenderCNIC) {
		logger.info("[CashToCashCommand.loadAppUserByCNICAndType] Loading Walkin Customer with CNIC:" + walkinSenderCNIC + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setNic(walkinSenderCNIC);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(appUserModel);
		
		try {

			searchBaseWrapper = getCommonCommandManager().searchAppUserByExample(searchBaseWrapper);
			
		} catch (FrameworkCheckedException e) {
			logger.error("[CashToCashCommand.loadAppUserByCNICAndType] Exception in Loading Walkin Customer with CNIC:" + walkinSenderCNIC + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + e.getMessage());
			
		}	
		
		appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();		
		
		this.appUserModel = appUserModel;
		
		return (appUserModel != null && appUserModel.getAppUserId() != null) ? appUserModel : null;
	}
	
	
	/**
	 * @param workFlowWrapper
	 */
	private MiniTransactionModel populateMiniTransactionModel(String encryptedPin) throws CommandException {

		MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
		
		try {
			miniTransactionModel.setCommandId( Long.valueOf(CommandFieldConstants.CMD_CASH_TO_CASH_INFO)) ;
			miniTransactionModel.setMobileNo(walkinSenderMobile);
			miniTransactionModel.setSmsText( recepientWalkinMobile + " " + txAmount ) ;
			miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.PIN_SENT) ;
			miniTransactionModel.setActionLogId( ThreadLocalActionLog.getActionLogId() ) ;
			miniTransactionModel.setOneTimePin(encryptedPin);
			miniTransactionModel.setTAMT(txAmount) ;
			if(manualOTPin != null && ! "".equals(manualOTPin)){//if Pin is manually set by agent, use this.
				miniTransactionModel.setIsManualOTPin(Boolean.TRUE);
			}

		} catch (Exception e) {
			
			if(logger.isErrorEnabled()){
				logger.error("[CashToCashCommand.populateMiniTransactionModel] Exception in populating MiniTransactionModel. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +". Details\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return miniTransactionModel;

	}
	

	/**
	 * @param workFlowWrapper
	 * @return
	 */
	private Double getCommissionAmount(WorkFlowWrapper workFlowWrapper) throws CommandException {
		
		try {
			
			commissionWrapper = getCommissionWrapper(workFlowWrapper);
			
			commissionAmountsHolder = (CommissionAmountsHolder)
				commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			
		} catch (CommandException e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
		
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);		
		}
		
		return commissionAmountsHolder.getTotalCommissionAmount();	
	}
	
	
	/**
	 * @param workFlowWrapper
	 * @return
	 */
	private CommissionWrapper getCommissionWrapper(WorkFlowWrapper workFlowWrapper) throws CommandException {
		
		CommissionWrapper commissionWrapper = null;
		
		try {
			
			workFlowWrapper.setTaxRegimeModel((TaxRegimeModel)fromRetailerContactModel.getTaxRegimeIdTaxRegimeModel().clone());
			commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return commissionWrapper;
	}
	
	
	/**
	 * @param workFlowWrapper
	 * @return
	 */
	private CashToCashDispenser loadCashToCashDispenser(WorkFlowWrapper workFlowWrapper) throws CommandException {

		CashToCashDispenser cashToCashDispenser = null;
		
		try {

			cashToCashDispenser = (CashToCashDispenser) getCommonCommandManager().loadProductDispense(workFlowWrapper);
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);			
		}
		
		return cashToCashDispenser;
	}
	
	
	/**
	 * @param preparedBaseWrapper
	 * @return
	 */
	private ProductVO loadProductVO(BaseWrapper preparedBaseWrapper) throws CommandException {
		
		ProductVO productVO = null;
		
		try {
			
			productVO = getCommonCommandManager().loadProductVO(preparedBaseWrapper);
			
			if(productVO != null) {				
				
//				productVO.populateVO(productVO, preparedBaseWrapper);
				
			} else {				
				throw new CommandException("ProductVo not Loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());						
			}
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return productVO;
	}
	
	
	private RetailerContactModel loadRetailerContactModel(Long retailerContactId) throws CommandException {

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		try {

			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(retailerContactId);		
			baseWrapper.setBasePersistableModel(retailerContactModel);
			
			baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
						
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return (baseWrapper != null ? ((RetailerContactModel) baseWrapper.getBasePersistableModel()) : null);
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
	

	public AgentMerchantDetailModel loadAgentMerchant(Long distributorId,Long retailerId) throws CommandException {

		AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
		agentMerchantDetailModel.setDistributorId(distributorId);
		agentMerchantDetailModel.setRetailerId(retailerId);
		
		baseWrapper.setBasePersistableModel(agentMerchantDetailModel);

/*		try {
			
*//*			baseWrapper = getCommonCommandManager().loadAgentMerchant(baseWrapper);*//*
			
		} catch (FrameworkCheckedException e) {
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}*/
		
		return (baseWrapper != null ? ((AgentMerchantDetailModel) baseWrapper.getBasePersistableModel()) : null);
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

	
	/**
	 * @param productId
	 * @return
	 */
	private ProductModel loadProduct(String productId) throws CommandException {

		BaseWrapper productBaseWrapper = new BaseWrapperImpl();
		
		try {

			ProductModel productModel = new ProductModel();
			productModel.setProductId(Long.valueOf(productId));			
			productBaseWrapper.setBasePersistableModel(productModel);
			
			productBaseWrapper = getCommonCommandManager().loadProduct(productBaseWrapper);
						
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return (productBaseWrapper != null ? ((ProductModel) productBaseWrapper.getBasePersistableModel()) : null);
	}
	
	
	/**
	 * @param appUserModel
	 * @return
	 */
	private Boolean isActiveAppUser(AppUserModel appUserModel) throws CommandException {
		
		Boolean hasValidationErrors = Boolean.FALSE;
		
		try {

			ValidationErrors validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
			hasValidationErrors = validationErrors.hasValidationErrors();
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()) {
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return hasValidationErrors;
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

	
	
	/**
	 * @param _walkinCustomerModel
	 * @return
	 */
	private WalkinCustomerModel getWalkinCustomerModel(WalkinCustomerModel _walkinCustomerModel) throws CommandException {

		WalkinCustomerModel walkinCustomerModel = null;
		
    	try {
    		
			walkinCustomerModel = getCommonCommandManager().getWalkinCustomerModel(_walkinCustomerModel);
			
		} catch (FrameworkCheckedException e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
    	
    	return walkinCustomerModel;
		
	}

	/**
	 * @param _walkinCustomerModel
	 * @return
	 */
	private SmartMoneyAccountModel getSmartMoneyAccountModel(WalkinCustomerModel _walkinCustomerModel) throws CommandException {

		SmartMoneyAccountModel _smartMoneyAccountModel = null;
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
	    smartMoneyAccountModel.setDeleted(Boolean.FALSE);
	    smartMoneyAccountModel.setActive(Boolean.TRUE);
	    	     
	    try {
			
	    	WalkinCustomerModel walkinCustomerModel = this.getWalkinCustomerModel(_walkinCustomerModel);
	    	
		    smartMoneyAccountModel.setWalkinCustomerModel(walkinCustomerModel);
	    	
	    	_smartMoneyAccountModel = getCommonCommandManager().getSmartMoneyAccountByWalkinCustomerId(smartMoneyAccountModel);
			
		} catch (Exception e) {
			
			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}
		
		return _smartMoneyAccountModel;
	}
		
	
	@Override
	public String response() {
		return toXML();
	}

	
	private String toText() {
		
		CashToCashVO cashToCashVO = (CashToCashVO) workFlowWrapper.getProductVO();
		
		String BAMT = Formatter.formatDouble(cashToCashVO.getTxAmount());
		String date = PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
		String time = PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
		String TPAM = Formatter.formatDouble(cashToCashVO.getCommissionAmount());
		String balance = Formatter.formatDouble(cashToCashVO.getBalance());
		String cnic = recepientWalkinCNIC;
		String mobile = recepientWalkinMobile;
		String txId= workFlowWrapper.getTransactionCodeModel().getCode();

		String textNotification = this.getMessageSource().getMessage("USSD.AgentC2CNotification", 
							new Object[] {cnic, mobile, BAMT, date, time, TPAM,txId, balance}, null);	
		
		return textNotification;		
	}

	
	private String toXML() {

		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(workFlowWrapper.getTransactionCodeModel().getCode())));
		params.add(new LabelValueBean(ATTR_SWCNIC, replaceNullWithEmpty(walkinSenderCNIC)));
		params.add(new LabelValueBean(ATTR_RWCNIC, replaceNullWithEmpty(recepientWalkinCNIC)));
		params.add(new LabelValueBean(ATTR_SWMOB, replaceNullWithEmpty(walkinSenderMobile)));
		params.add(new LabelValueBean(ATTR_RWMOB, replaceNullWithEmpty(recepientWalkinMobile)));
		
		params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
		params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_FORMAT)));
		params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
		params.add(new LabelValueBean(ATTR_PROD, replaceNullWithEmpty(productModel.getName())));
		params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount()+"")));
		params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
		params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(strTxProcessingAmount)));
		params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(Double.parseDouble(strTxProcessingAmount))));
		params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
		params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
		params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
		params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
		params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(replaceNullWithZero(((CashToCashVO) workFlowWrapper.getProductVO()).getBalance()))));
		
		return MiniXMLUtil.createResponseXMLByParams(params);
	
	}
	
	
	/**
	 * @param PIN
	 * @return
	 * @throws CommandException
	 */
	private void fetchTitle(String PIN) throws CommandException {
				
   	  	try {

   	  		AccountInfoModel accountInfoModel = null;
			
			if(accountInfoModel == null || StringUtil.isNullOrEmpty(accountInfoModel.getAccountNo())) {
				
				throw new FrameworkCheckedException("Could Not Fetch Title for "+appUserModel.getFirstName());
			}
			
		} catch (WorkFlowException e) {

 			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
			
		} catch (FrameworkCheckedException e) {

 			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
			
		} catch (Exception e) {

 			if(logger.isErrorEnabled()){
				logger.error(ExceptionProcessorUtility.prepareExceptionStackTrace(e));
			}
			throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
		}	
	}
	
}