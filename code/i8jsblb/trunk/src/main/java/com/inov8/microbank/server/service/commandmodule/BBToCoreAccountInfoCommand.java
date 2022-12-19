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
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class BBToCoreAccountInfoCommand extends BaseCommand
{
	protected AppUserModel appUserModel;
	protected AppUserModel senderWalkinAppUserModel;
	protected String productId;
	protected BaseWrapper peparedBaseWrapper;
	protected String accountId;
	double discountAmount;
	
	protected UserDeviceAccountsModel userDeviceAccountsModel;
	ProductModel productModel;
	BaseWrapper baseWrapper;
	String successMessage;
	double billAmount;
	CommissionAmountsHolder commissionAmountsHolder;
	BBToCoreVO bbToCoreVO ;
	protected String pin;
	protected AbstractFinancialInstitution abstractFinancialInstitution;
	protected final Log logger = LogFactory.getLog(BBToCoreAccountInfoCommand.class);
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private String senderMobileNo;
	private String coreAccountNo;
	RetailerContactModel fromRetailerContactModel;
	private String accountTitle;
	
	private String senderWalkinMobileNo;
	private String senderWalkinCnic;
	protected List<AppUserModel> senderAppUserModelList;
	private String logClassName = "BBToCoreAccountInfoCommand";
	
	private String bvsRequired = "0";
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of "+logClassName+".execute()");
		}
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		CommissionWrapper commissionWrapper;
		baseWrapper = new BaseWrapperImpl();
		
		
		if(appUserModel.getAppUserId() != null)//Agent AppUser ID
		{
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					AppUserModel senderAppUserModel = null;
					
					if(productId.equals(ProductConstantsInterface.BB_TO_CORE_ACCOUNT.toString())){
						
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						senderAppUserModel = this.getCommonCommandManager().loadAppUserByQuery(senderMobileNo, UserTypeConstantsInterface.CUSTOMER);
						
						if (senderAppUserModel == null || senderAppUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.CUSTOMER){
							logger.error("["+logClassName+".execute] Invalid Customer Account. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + senderMobileNo); 
							throw new CommandException(MessageUtil.getMessage("6078"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						
						}else{//BB Customer case
							
							validationErrors = commonCommandManager.checkActiveAppUser(senderAppUserModel);
							if(validationErrors.hasValidationErrors()){
								logger.error("["+logClassName+".execute] Sender App User Validation Failed. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + senderMobileNo); 
								throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
							
							//Check User Device Accounts health
							BaseWrapper baseWrapper = new BaseWrapperImpl();
							baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
							baseWrapper.setBasePersistableModel(senderAppUserModel);
							validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);
							
							if(validationErrors.hasValidationErrors()) {
								throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
							
							//check Customer SMA
							searchBaseWrapper = new SearchBaseWrapperImpl();
							SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
							smartMoneyAccountModel.setCustomerId(senderAppUserModel.getCustomerId());
							searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
							
							searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
							if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
								smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
							}
							
							baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
							validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);
						
							if(!validationErrors.hasValidationErrors())
							{
								if(smartMoneyAccountModel.getName() != null)
								{
									if(smartMoneyAccountModel.getCustomerId().toString().equals(senderAppUserModel.getCustomerId().toString()))
									{
										workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
									
									}else {
										logger.error("["+logClassName+".execute] Invalid Smart Money account. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + senderMobileNo); 
										throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
									}
								
								}else {
									logger.error("["+logClassName+".execute] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + senderMobileNo); 
									throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
								}
						
							}else {
								logger.error("["+logClassName+".execute] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ " Mobile No:" + senderMobileNo); 
								throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
						}
					
					}else if(productId.equals(ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT.toString())){

						//Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
						commonCommandManager.createOrUpdateWalkinCustomer(senderWalkinCnic, senderWalkinMobileNo, null);
						/*if(senderAppUserModelList!=null && senderAppUserModelList.size()==0) {
							getCommonCommandManager().createNewWalkinCustomer(senderWalkinCnic, senderWalkinMobileNo, null);
						}
				*/
					}
					

						//Common steps for BB and Walkin Customer
						TransactionModel transactionModel = new TransactionModel();
						productModel = new ProductModel();
						productModel.setProductId(Long.parseLong(productId));
						baseWrapper.setBasePersistableModel(productModel);
						baseWrapper = commonCommandManager.loadProduct(baseWrapper);
						productModel = (ProductModel)baseWrapper.getBasePersistableModel();
							
						//load/Populate VO
						if(productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())) 
						{
							ProductVO productVO = commonCommandManager.loadProductVO(peparedBaseWrapper);
							if(productVO == null)
							{
								throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
							}
							
							productVO.populateVO(productVO, peparedBaseWrapper);
							
							workFlowWrapper.setProductModel(productModel);
							workFlowWrapper.setProductVO(productVO);
							
							//titleFetch
							CustomerAccount customerAccount = new CustomerAccount(coreAccountNo,null,null,null);
							workFlowWrapper.setCustomerAccount(customerAccount);
							LoadAgentSmartMoneyAccountModel(workFlowWrapper);
							workFlowWrapper = commonCommandManager.getBBToCoreAccInfo(workFlowWrapper);
							
							logger.info("["+logClassName+".execute] Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
						
						}else{
							logger.error("["+logClassName+".execute] Unable to load Product VO. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
							throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
						}
						

									
						TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
						transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BB_TO_CORE_ACCOUNT_TX);
						workFlowWrapper.setProductModel(productModel);
						workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
						workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
						workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(Long.parseLong(deviceTypeId)));

						BBToCoreVO vo = (BBToCoreVO)workFlowWrapper.getProductVO();
						transactionModel.setTransactionAmount(vo.getCurrentBillAmount());
						workFlowWrapper.setTransactionModel(transactionModel);

						if (productId.equals(ProductConstantsInterface.BB_TO_CORE_ACCOUNT.toString())) {
							
							loadCustomerSegment(workFlowWrapper, senderAppUserModel.getCustomerId());
							
						}else{
							workFlowWrapper.setSegmentModel(new SegmentModel());
							workFlowWrapper.getSegmentModel().setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
						}
							
						if(productId.equals(ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT.toString())) {		//CNIC based transaction
							workFlowWrapper.setTransactionAmount(billAmount);
							commonCommandManager.verifyWalkinCustomerThroughputLimits(workFlowWrapper, TransactionTypeConstantsInterface.OLA_DEBIT, senderWalkinCnic);
							bvsRequired = (String)workFlowWrapper.getObject(CommandFieldConstants.IS_SENDER_BVS_REQUIRED);
						}

						commonCommandManager.checkProductLimit(workFlowWrapper.getSegmentModel().getSegmentId(), productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), billAmount, productModel, null, workFlowWrapper.getHandlerModel());								

					    workFlowWrapper.setTaxRegimeModel(fromRetailerContactModel.getTaxRegimeIdTaxRegimeModel());
						
						commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
						commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

						SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
						sma.setCustomerId(senderAppUserModel.getCustomerId());

						sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

						SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);
						commonCommandManager.validateBalance(senderAppUserModel, smartMoneyAccountModel, commissionAmountsHolder.getTotalAmount(), true);

						if (productId.equals(ProductConstantsInterface.BB_TO_CORE_ACCOUNT.toString())) {
							commonCommandManager.checkCustomerBalance(senderMobileNo, commissionAmountsHolder.getTotalAmount());
						}
						
						billAmount = vo.getBillAmount();
						bbToCoreVO = vo;
						accountTitle = vo.getAccountTitle();
						userDeviceAccountsModel = (UserDeviceAccountsModel)ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
						

				}
				else
				{
					logger.error("["+logClassName+".execute] User Validation Failed. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()); 
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(CommandException e)
			{
				logger.error("["+logClassName+".execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Exception Message:" + e.getMessage()); 
				throw e;
			}
			catch(WorkFlowException wex)
			{
				logger.error("["+logClassName+".execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId+ " Exception Message:" + wex.getMessage()); 
                if(StringUtil.isFailureReasonId(wex.getMessage())){
                	throw new CommandException(MessageUtil.getMessage(wex.getMessage()),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
                }else{
                	throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
                }
			}
			catch (ClassCastException e)
			{							
				logger.error("["+logClassName+".execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Exception Message:" + e.getMessage()); 
				throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
			}
			catch(Exception ex)
			{ex.printStackTrace();
				logger.error("["+logClassName+".execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId +" Exception Message:" + ex.getMessage()); 
				if(ex.getMessage() != null && ex.getMessage().indexOf("JTA") != -1)
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
			logger.error("["+logClassName+".execute] App User not found. Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId); 
			throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of "+logClassName+".execute()");
		}
	}

	private void loadCustomerSegment(WorkFlowWrapper workFlowWrapper, Long customerId) throws Exception{
		CustomerModel custModel = new CustomerModel();
		custModel.setCustomerId(customerId);
		BaseWrapper bWrapper = new BaseWrapperImpl();
		bWrapper.setBasePersistableModel(custModel);
		bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
		if(null != bWrapper.getBasePersistableModel())
		{
			custModel = (CustomerModel) bWrapper.getBasePersistableModel();
			workFlowWrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
		}
	}
	
	private void LoadAgentSmartMoneyAccountModel(WorkFlowWrapper workFlowWrapper) throws Exception{
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();
		smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
		searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		
		searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
		if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
			smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
			workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
		}
		
	}
	
	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		peparedBaseWrapper = baseWrapper;
		accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		coreAccountNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CORE_ACC_NO);
		
		if(productId != null && productId.equals(ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT.toString())){
			senderWalkinMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE);
			senderWalkinCnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_S_W_CNIC);
			logClassName = "BBToCoreAccountInfoCommand[CNIC Case]";
			try {
				//no registered user should exists with sender mobile no
				senderWalkinAppUserModel = this.getCommonCommandManager().checkAppUserTypeAsWalkinCustoemr(senderWalkinMobileNo);
			}catch(Exception e){
				logger.error("["+logClassName+".prepare] Unable to load senderAppUserModel");
			}
		}
		
		String billAmountStr = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		if (StringUtils.isNotEmpty(billAmountStr) && StringUtils.isNumeric(billAmountStr)) {
			billAmount = Double.valueOf(billAmountStr);
		}
		try{
        	RetailerContactModel retContactModel = new RetailerContactModel();
        	retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
        	
        	BaseWrapper bWrapper = new BaseWrapperImpl();
        	bWrapper.setBasePersistableModel(retContactModel);
        	CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        	bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

        	this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

        }catch(Exception ex){
        	logger.error("["+logClassName+".prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
        }
		//senderAppUserModelList = this.getCommonCommandManager().getAppUserManager().findAppUserByCnicAndMobile(senderWalkinMobileNo , senderWalkinCnic);
		logger.info("["+logClassName+".prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + 
    			" Product ID:" + productId + " AccountId: " + accountId + 
    			" deviceTypeId: " + deviceTypeId);
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException{
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		
		String classNMethodName = getClass().getName() + ".validate():";
		try{
			if(this.getCommonCommandManager().loadAppUserByCnicAndType(senderWalkinCnic) != null){
				String error=this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_NIC_ALREADY_REG", null,null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
			
		}catch(FrameworkCheckedException fe){
				fe.printStackTrace();
		}
		
		if(productId.equals(ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT.toString())){
			validationErrors = ValidatorWrapper.doRequired(senderWalkinCnic,validationErrors,"Sender CNIC");
			validationErrors = ValidatorWrapper.doValidateCNIC(senderWalkinCnic,validationErrors,"Sender CNIC");
			validationErrors = ValidatorWrapper.doRequired(senderWalkinMobileNo,validationErrors,"Sender Mobile Number");
		}else{
			validationErrors = ValidatorWrapper.doRequired(senderMobileNo,validationErrors,"Sender Mobile Number");
		}
		
		validationErrors = ValidatorWrapper.doRequired(coreAccountNo,validationErrors,"Account Number");
		
		if(!validationErrors.hasValidationErrors()){
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		if(productId.equals(ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT.toString())){
			if(null != senderWalkinAppUserModel){
				String error = this.getMessageSource().getMessage("MONEYTRANSFER.SENDER_MOB_ALREADY_REG", null, null);
				logger.error(classNMethodName + error);
				ValidatorWrapper.addError(validationErrors, error);
			}
		}


		return validationErrors;
	}
	
	@Override
	public String response() 
	{
		return toXML();
	}
	
	private String toXML()
	{
		
		StringBuilder strBuilder = new StringBuilder();
		
		if(commissionAmountsHolder != null && bbToCoreVO != null){
			
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
			
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, productId));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, StringEscapeUtils.escapeXml(accountTitle)));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CORE_ACC_NO, bbToCoreVO.getAccountNumber()));
			
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_IS_BVS_REQUIRED, "0"));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
			strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
			
			if(productId.equals(ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT.toString())){
				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE, senderWalkinMobileNo));
				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_S_W_CNIC, senderWalkinCnic));
			}else{
				strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, senderMobileNo));
			}
			
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		}
		
		return strBuilder.toString();

	}
}