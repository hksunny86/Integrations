package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.integration.vo.AccountToAccountVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;



public class AccountToAccountInfoCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected BaseWrapper preparedBaseWrapper;
	protected String accountId;
	double discountAmount;
	
	protected String senderMobileNo;
	protected String recipientMobileNo;
	protected String agentMobileNo;
	
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	ProductModel productModel;
	BaseWrapper baseWrapper;
	String successMessage;
	String txAmount;
	CommissionAmountsHolder commissionAmountsHolder;
	AccountToAccountVO p ;
	protected String pin;
	protected AbstractFinancialInstitution abstractFinancialInstitution;
	protected final Log logger = LogFactory.getLog(AccountToAccountInfoCommand.class);
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	
	RetailerContactModel fromRetailerContactModel;
	long senderSegmentId;
	long recipientSegmentId;
	
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of AccountToAccountInfoCommand.execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		
		CommissionWrapper commissionWrapper;
		baseWrapper = new BaseWrapperImpl();
		
		//Agent Assisted Acc to Acc
		if(appUserModel.getRetailerContactId() != null)
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{

					if(senderMobileNo.equals(recipientMobileNo )) {
						logger.error("[AccountToAccountInfoCommand.execute] Sender and Reciever Mobile No must be different. Throwing Exception - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Sender Mobile No:" + senderMobileNo); 
						throw new CommandException("Sender and Reciever Mobile No must be different.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					if(agentMobileNo.equals(senderMobileNo) || agentMobileNo.equals(recipientMobileNo)) {
						logger.error("[AccountToAccountInfoCommand.execute] Your Mobile No cannot be used as Sender or Reciever. Throwing Exception - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Sender Mobile No:" + senderMobileNo); 
						throw new CommandException("Your Mobile No cannot be used as Sender or Reciever.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}

					//*** Dynamically deciding product - start
					boolean isSenderWalkIn = false;
					boolean isRecipientWalkIn = false;
					String senderPrefix = "Sender - ";
					String recipientPrefix = "Recipient - ";
					
					//Sender Customer Validation
//					AppUserModel senderAppUserModel = this.getCommonCommandManager().loadAppUserByQuery(senderMobileNo, UserTypeConstantsInterface.CUSTOMER);
					AppUserModel senderAppUserModel = this.getCommonCommandManager().checkAppUserTypeAsWalkinCustoemr(senderMobileNo);
					if (senderAppUserModel == null) {
						//Walkin customer case
						isSenderWalkIn = true;
					}else if(senderAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
						//BB Customer Case
						validateBBCustomer(senderAppUserModel, true, senderPrefix);
					}else{
						// If senderMobileNo is registered as other than walk-in  and customer then it will be not allowed to perform transaction.
						logger.error("[AccountToAccountInfoCommand.execute] Invalid Customer Account. Throwing Exception - Logged in AppUserID: " + senderAppUserModel.getAppUserId() + " Mobile No:" + senderMobileNo); 
						throw new CommandException(MessageUtil.getMessage("6078"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}

					//Recipient Customer Validation
//					AppUserModel recipientAppUserModel = this.getCommonCommandManager().loadAppUserByQuery(recipientMobileNo, UserTypeConstantsInterface.CUSTOMER);
					AppUserModel recipientAppUserModel = this.getCommonCommandManager().checkAppUserTypeAsWalkinCustoemr(recipientMobileNo);
					if (recipientAppUserModel == null ) {
						//Walkin customer case
						logger.info("[AccountToAccountInfoCommand.execute] recipientAppUserModel:" + recipientAppUserModel + " is Walkin customer - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + recipientMobileNo); 
						isRecipientWalkIn = true;
					}else if(recipientAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
						//BB Customer Case
						logger.info("[AccountToAccountInfoCommand.execute] recipientAppUserModel ID:" + recipientAppUserModel.getAppUserId() + " is BB customer - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + recipientMobileNo); 
						validateBBCustomer(recipientAppUserModel, false, recipientPrefix);
					}else{
						// If recieverMobileNo is registered as other than walk-in  and customer then it will be not allowed to perform transaction.
						logger.error("[AccountToAccountInfoCommand.execute] Invalid Customer Account. Throwing Exception - Logged in AppUserID: " + recipientAppUserModel.getAppUserId() + " Mobile No:" + recipientMobileNo); 
						throw new CommandException(MessageUtil.getMessage("6078"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}

					//Calculating Product ID
					if(isSenderWalkIn && isRecipientWalkIn){
						productId = ProductConstantsInterface.CASH_TRANSFER.toString();
					}else if(isSenderWalkIn && !isRecipientWalkIn){
						productId = ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.toString();
					}else if(!isSenderWalkIn && isRecipientWalkIn){
						productId = ProductConstantsInterface.ACCOUNT_TO_CASH.toString();
					}else if(!isSenderWalkIn && !isRecipientWalkIn){
						productId = ProductConstantsInterface.ACT_TO_ACT.toString();
					}
					
					//Calculating Segment Details for commission calculations
					if(productId.equals(ProductConstantsInterface.ACCOUNT_TO_CASH.toString())){
						try {
							senderSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(senderMobileNo);
							
							SegmentModel segmentModel = new SegmentModel();
							segmentModel.setSegmentId(senderSegmentId);
							workFlowWrapper.setSegmentModel(segmentModel);
							
						} catch (Exception e) {
							logger.error("[AccountToAccountInfoCommand.execute] Unable to load Sender Customer Segment info... ",e);
						}
					}

					if(productId.equals(ProductConstantsInterface.ACT_TO_ACT.toString())){
						try {
							senderSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(senderMobileNo);
							
							SegmentModel segmentModel = new SegmentModel();
							segmentModel.setSegmentId(senderSegmentId);
							workFlowWrapper.setSegmentModel(segmentModel);
							
						} catch (Exception e) {
							logger.error("[AccountToAccountInfoCommand.execute] Unable to load Sender Customer Segment info... ",e);
						}
						try {
							recipientSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(recipientMobileNo);
						} catch (Exception e) {
							logger.error("[AccountToAccountInfoCommand.execute] Unable to load Recipient Customer Segment info... ",e);
						}
					}


					if(productId.equals(ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.toString())){
						try {
							recipientSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(recipientMobileNo);
							
							SegmentModel segmentModel = new SegmentModel();
							segmentModel.setSegmentId(recipientSegmentId);
							workFlowWrapper.setSegmentModel(segmentModel);
							
						} catch (Exception e) {
							logger.error("[AccountToAccountInfoCommand.prepare] Unable to load Recipient Customer Segment info... ",e);
						}
					}

					if(productId.equals(ProductConstantsInterface.CASH_TRANSFER.toString())){ 
						SegmentModel segmentModel = new SegmentModel();
						segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);		
						workFlowWrapper.setSegmentModel(segmentModel);
					}

					//In case segment Model is null load default segment
					if(workFlowWrapper.getSegmentModel() == null){ 
						SegmentModel segmentModel = new SegmentModel();
						segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);		
						workFlowWrapper.setSegmentModel(segmentModel);
					}

					
					//*** Dynamically deciding product - end
					
					SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
					
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					smartMoneyAccountModel.setDeleted(false);
					baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					
					baseWrapper = commonCommandManager.loadOLASmartMoneyAccount(baseWrapper);
					smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
					
					baseWrapper.putObject(CommandFieldConstants.KEY_HANDLER_MODEL, handlerModel);
					validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

					if(!validationErrors.hasValidationErrors())
					{
						if(smartMoneyAccountModel.getName() != null)
						{
//							if(smartMoneyAccountModel.getCustomerId().toString().equals(appUserModel.getCustomerId().toString()))
							{
					
								productModel = new ProductModel();
								TransactionModel transactionModel = null;
								productModel.setProductId(Long.parseLong(productId));
								baseWrapper.setBasePersistableModel(productModel);
								baseWrapper = commonCommandManager.loadProduct(baseWrapper);
								productModel = (ProductModel)baseWrapper.getBasePersistableModel();
								
								commonCommandManager.checkProductLimit(workFlowWrapper.getSegmentModel().getSegmentId(),productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.parseDouble(txAmount), productModel, null,workFlowWrapper.getHandlerModel());								
								
								if(productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())) 
								{
									ProductVO productVO = commonCommandManager.loadProductVO(preparedBaseWrapper);
									if(productVO == null)
									{
										throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
									
									productVO.populateVO(productVO, preparedBaseWrapper);
									
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setProductVO(productVO);
//									workFlowWrapper = commonCommandManager.getAccToAccInfo(workFlowWrapper);
									
									AccountToAccountVO a2aVO = (AccountToAccountVO)workFlowWrapper.getProductVO();
									
									if(senderAppUserModel != null){
//										a2aVO.setsendName(replaceNullWithEmpty(senderAppUserModel.getFirstName()) + " " + replaceNullWithEmpty(senderAppUserModel.getLastName()) ) ;
										a2aVO.setSenderCustomerId(senderAppUserModel.getCustomerId() ) ;
										a2aVO.setSenderAppUserId(senderAppUserModel.getAppUserId() ) ;
									}

									if(recipientAppUserModel != null){
										a2aVO.setRecipientName(replaceNullWithEmpty(recipientAppUserModel.getFirstName()) + " " + replaceNullWithEmpty(recipientAppUserModel.getLastName()) ) ;
										a2aVO.setRecipientCustomerId(recipientAppUserModel.getCustomerId() ) ;
										a2aVO.setRecipientAppUserId(recipientAppUserModel.getAppUserId() ) ;
									}
									
//									To calculate the commission
									transactionModel = new TransactionModel();
									transactionModel.setTransactionAmount(a2aVO.getTransactionAmount());					
									
//									smartMoneyAccountModel.setSmartMoneyAccountId(Long.parseLong(accountId));
//									baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
//									baseWrapper = commonCommandManager.loadSmartMoneyAccount(baseWrapper);
//									smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
									
									if(smartMoneyAccountModel.getPaymentModeId() != null)
									{
										transactionModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
									}
												
									TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
									transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.ACCOUT_TO_ACCOUNT_TX);
									workFlowWrapper.setProductModel(productModel);
									workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
									workFlowWrapper.setTransactionModel(transactionModel);
									workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									workFlowWrapper.setFromRetailerContactModel(fromRetailerContactModel);
									workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
									workFlowWrapper.setFromSegmentId(senderSegmentId);
									workFlowWrapper.setToSegmentId(recipientSegmentId);
									
									//Maqsood Shahzad - due to commission changes loading customer along with the segment
//									CustomerModel custModel = new CustomerModel();
//									custModel.setCustomerId(a2aVO.getSenderCustomerId());
//									BaseWrapper bWrapper = new BaseWrapperImpl();
//									bWrapper.setBasePersistableModel(custModel);
//									bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
//									if(null != bWrapper.getBasePersistableModel())
//									{
//										custModel = (CustomerModel) bWrapper.getBasePersistableModel();
//										workFlowWrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
//									}
									
									workFlowWrapper.setTaxRegimeModel((TaxRegimeModel)fromRetailerContactModel.getTaxRegimeIdTaxRegimeModel().clone());
									commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
									commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

									commonCommandManager.validateBalance(appUserModel, smartMoneyAccountModel, commissionAmountsHolder.getTotalAmount(), true);

									if(productId.equals(ProductConstantsInterface.ACT_TO_ACT.toString()) ||
											productId.equals(ProductConstantsInterface.ACCOUNT_TO_CASH.toString()) ){
										
										commonCommandManager.checkCustomerBalance(senderMobileNo, commissionAmountsHolder.getTotalAmount());
									
									}
									userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
//									txAmount = a2aVO.getBillAmount();
									p = a2aVO;
									
									logger.info("[AccountToAccountInfoCommand.execute] Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction Amount: " + p.getTransactionAmount());
								}							
							}
//							else
//							{
//								logger.error("[AccountToAccountInfoCommand.execute] Throwing Exception - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Bill Amount: " + p.getBillAmount()  + " ConsumerNo:" + p.getConsumerNo()); 
//								throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//							}
						}
						else
						{
							throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
					}
					else
					{
						throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}
				else
				{
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(WorkFlowException wex)
			{
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch (ClassCastException e)
			{							
				throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
			}
			catch(Exception ex)
			{
				logger.error(ex.getMessage(),ex);
				if(ex.getMessage().indexOf("JTA") != -1)
				{
					throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
				}
				else
				{
					throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
				}
			}
		}
		else
		{
			logger.error("[AccountToAccountInfoCommand.execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Transaction Amount: " + p.getTransactionAmount()); 
			throw new CommandException(this.getMessageSource().getMessage("AccountToAccountInfoCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of AccountToAccountInfoCommand.execute()");
		}
	}

	private void validateBBCustomer(AppUserModel customerAppUserModel, boolean checkSMA, String prefix) throws FrameworkCheckedException{
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		
		validationErrors = commonCommandManager.checkActiveAppUser(customerAppUserModel);
		if(validationErrors.hasValidationErrors()){
			logger.error("[AccountToAccountInfoCommand.validateBBCustomer] Sender Customer App User Validation Failed. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + senderMobileNo); 
			throw new CommandException(prefix + validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
		//Check User Device Accounts health
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.putObject(CommandFieldConstants.KEY_CUST_ERROR_PREFIX, prefix.substring(0, prefix.lastIndexOf(" -")));
		baseWrapper.setBasePersistableModel(customerAppUserModel);
		validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);
		
		if(validationErrors.hasValidationErrors()) {

			throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		
		if(checkSMA){
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
			smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
			searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			
			searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
			
			if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
				smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
			}
			
			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
		
			if(!validationErrors.hasValidationErrors()){
				if(smartMoneyAccountModel.getName() != null){
					if(smartMoneyAccountModel.getCustomerId().toString().equals(customerAppUserModel.getCustomerId().toString())){
	//					TODO FIXME set SMA for sender / recipient customer
	//					workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
					}else {
						logger.error("[AccountToAccountInfoCommand.validateBBCustomer] Invalid Smart Money account. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + customerAppUserModel.getMobileNo()); 
						throw new CommandException(prefix + this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
				}else {
					logger.error("[AccountToAccountInfoCommand.validateBBCustomer] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + customerAppUserModel.getMobileNo()); 
					throw new CommandException(prefix + this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}else {
				logger.error("[AccountToAccountInfoCommand.validateBBCustomer] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ " Mobile No:" + customerAppUserModel.getMobileNo()); 
				throw new CommandException(prefix + validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		
	}
	
	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		preparedBaseWrapper = baseWrapper;
//		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		recipientMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
		agentMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);

		
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId( appUserModel.getRetailerContactId() );
		bWrapper.setBasePersistableModel(retailerContactModel);
		
		try{
			bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
			
			this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
			
		}catch(Exception ex){
			logger.error("[AccountToAccountInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
		}
		
		logger.info("[AccountToAccountInfoCommand.prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + 
    			" Product ID:" + productId + " deviceTypeId: " + deviceTypeId);
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException{
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Amount");
		validationErrors = ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile No");
		validationErrors = ValidatorWrapper.doRequired(recipientMobileNo,validationErrors,"Recipient Mobile No");
		validationErrors = ValidatorWrapper.doRequired(agentMobileNo,validationErrors,"Agent Mobile No");
		
		if(!validationErrors.hasValidationErrors()){
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txAmount,validationErrors,"Amount");
		}
		return validationErrors;
	}
	
	@Override
	public String response() 
	{
		return toXML();
	}
	
	private String toXML(){
		StringBuilder strBuilder = new StringBuilder();
		
		if(commissionAmountsHolder != null){
			
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, p.getSenderCustomerMobileNo()));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, p.getRecipientCustomerMobileNo()));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, productId));
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		}
		
		return strBuilder.toString();
	}
	
}
