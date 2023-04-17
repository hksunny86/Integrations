package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.AgentToAgentTransferVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class RetailerToRetailerTransactionCommand extends BaseCommand 
{
	protected AppUserModel appUserModel;
	protected String txAmount;
	protected String deviceTypeId;
	protected String allPayId;
	protected String productId;
	protected ProductModel productModel;
	protected BaseWrapper preparedBaseWrapper;
	protected String pin;
	
	protected String txProcessingAmount;

	String senderAgentMobile;
	String recipientAgentMobile;
	String recipientAgentCNIC;

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
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		
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
					receiverAppUserModel.setMobileNo(recipientAgentMobile);
					receiverAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
					bWrapper.setBasePersistableModel(receiverAppUserModel);
					bWrapper = this.getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
					
					if(null != bWrapper.getBasePersistableModel())
					{
						UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
						allPayId = userDeviceAccountsModel.getUserId();
						receiverAppUserModel.setAppUserId(userDeviceAccountsModel.getAppUserId());
					}
					else
					{
						throw new CommandException(this.getMessageSource().getMessage("allpay.user.not.exist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					
					// Loading product Module and VO for use in the transaction class
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					baseWrapper.setBasePersistableModel(productModel);
					baseWrapper = commonCommandManager.loadProduct(baseWrapper);
					productModel = (ProductModel)baseWrapper.getBasePersistableModel();
					
					commonCommandManager.checkProductLimit(null,productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.parseDouble(txAmount), productModel, null, workFlowWrapper.getHandlerModel());					
					Long senderAppUserId = appUserModel.getAppUserId(); //Agent appUserId
					if(handlerAppUserModel != null && handlerAppUserModel.getAppUserId() != null){
						senderAppUserId = handlerAppUserModel.getAppUserId(); //Handler appUserId
					}
					AgentTransferRuleModel ruleModel = commonCommandManager.findAgentTransferRule(Long.valueOf(deviceTypeId),Double.parseDouble(txAmount), senderAppUserId, receiverAppUserModel.getAppUserId());
					workFlowWrapper.setAgentTransferRuleModel(ruleModel);

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
								". Receiver Mobile No:" + recipientAgentMobile + ". Product ID:" + productId);

						throw new CommandException(this.getMessageSource().getMessage("allpay.user.not.exist", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}
					
					toRetailerAppUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();
				
					recipientAgentCNIC = toRetailerAppUserModel.getNic();
					
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.RET_TO_RET_TX);
					
					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
					
					ProductModel productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
//					toRetailerAppUserModel.setMobileNo(distributorOrRetailer.getMobileNo());
					SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
					smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
					SmartMoneyAccountModel receivingSmartMoneyAccountModel = new SmartMoneyAccountModel();
					receivingSmartMoneyAccountModel.setRetailerContactId(toRetailerAppUserModel.getRetailerContactId());


					RetailerContactModel retailerContactModel = loadRetailerContactModel(appUserModel.getRetailerContactId());
					RetailerModel retailerModel = loadRetailerModel(retailerContactModel.getRetailerId());
					DistributorModel distributorModel = loadDistributorModel(retailerModel.getDistributorId());


					workFlowWrapper.setRetailerContactModel(retailerContactModel);
					workFlowWrapper.setDistributorModel(distributorModel);
					workFlowWrapper.setRetailerModel(retailerModel);
					
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
								". Receiver Mobile No:" + recipientAgentMobile + ". Product ID:" + productId);

					commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
					
					txProcessingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount().toString();
					
					transactionModel = workFlowWrapper.getTransactionModel();
					successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
					this.workFlowWrapper = workFlowWrapper;
					
					commonCommandManager.sendSMS(workFlowWrapper);
				}
				else
				{
					logger.info("[RetailerToRetailerTransactionCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							". Validation Errors:" + validationErrors.getErrors() + 
							". Receiver Mobile No:" + recipientAgentMobile + ". Product ID:" + productId);

					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(Exception ex)
			{
				logger.info("[RetailerToRetailerTransactionCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
						". Exception Msg:" + ex.getMessage() + 
						". Receiver Mobile No:" + recipientAgentMobile + ". Product ID:" + productId);

				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.info("[RetailerToRetailerTransactionCommand.execute] Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
					". Exception Msg:" + this.getMessageSource().getMessage("retailerToRetailerTransactionCommand.invalidAppUserType", null,null) + 
					". Receiver Mobile No:" + recipientAgentMobile + ". Product ID:" + productId);

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
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		senderAgentMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
		recipientAgentMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECIPIENT_AGENT_MOBILE);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		
		logger.info("[RetailerToRetailerTransactionCommand.prepare] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Receiver Mobile No:" + recipientAgentMobile + ". Product ID:" + productId);
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
		validationErrors = ValidatorWrapper.doRequired(senderAgentMobile,validationErrors,"Sender agent mobile no");
		validationErrors = ValidatorWrapper.doRequired(recipientAgentMobile,validationErrors,"Agent mobile no");
		validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doInteger(recipientAgentMobile, validationErrors, "Agent mobile no");
			validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
		params.add(new LabelValueBean(ATTR_RAMOB, replaceNullWithEmpty(recipientAgentMobile)));
		params.add(new LabelValueBean(ATTR_RACNIC, replaceNullWithEmpty(recipientAgentCNIC)));
		params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
		params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.LONG_DATE_FORMAT)));
		params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
		params.add(new LabelValueBean(ATTR_PROD, replaceNullWithEmpty(productModel.getName())));
		params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount()+"")));
		params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
		params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(txProcessingAmount)));
		params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(Double.parseDouble(txProcessingAmount))));
		params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
		params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
		params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
		params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
		params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(workFlowWrapper.getOLASwitchWrapper().getAgentBalance())));
		
		return MiniXMLUtil.createResponseXMLByParams(params);
	
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
		BaseWrapper baseWrapper=new BaseWrapperImpl();
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




	public DistributorModel loadDistributorModel(Long distributorId) throws CommandException {

		BaseWrapper baseWrapper=new BaseWrapperImpl();
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


}
