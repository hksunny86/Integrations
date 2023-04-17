package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.MiniBaseCommand;
import com.inov8.microbank.server.service.integration.vo.AgentRetailPaymentVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class CustomerRetailPaymentCommand extends MiniBaseCommand
{
	protected AppUserModel appUserModel;
	protected String productId;
	protected String customerMobile;
	protected String txProcessingAmount;
	protected String deviceTypeId;
	protected String commissionAmount;
	protected String totalAmount;
	protected String txAmount;
	private Double balance;
	TransactionModel transactionModel;
	ProductModel productModel;
	String successMessage;
	BaseWrapper baseWrapper;
	SmartMoneyAccountModel customerSMAModel;
	UserDeviceAccountsModel userDeviceAccountsModel;
	private String agentMobile;
	long segmentId;
	RetailerContactModel receiverRetailerContactModel;
	AppUserModel retailerAppUserModel;

	protected final Log logger = LogFactory.getLog(CustomerRetailPaymentCommand.class);
	
	@Override
	public void execute() throws CommandException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerRetailPaymentCommand.execute()");
		}

		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);		
		
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

		if(appUserModel.getCustomerId() != null) {//validate sender (Customer) 
			try
			{
				ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
				
				if(!validationErrors.hasValidationErrors())
				{
					workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
					workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
					workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setBillAmount(Double.parseDouble(txAmount));
					workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
					AppUserModel agentAppUserModel=new AppUserModel();
					AppUserModel retailerAppModel=new AppUserModel();
					retailerAppModel.setMobileNo(agentMobile);
					retailerAppModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
					workFlowWrapper.setRetailerAppUserModel(getCommonCommandManager().getAppUserManager().getAppUserModel(retailerAppModel));


					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setOldPin("");
								
					productModel = new ProductModel();
					productModel.setProductId(Long.parseLong(productId));
					
					ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					customerSMAModel =  new SmartMoneyAccountModel();
					customerSMAModel.setCustomerId(appUserModel.getCustomerId());
					customerSMAModel.setActive(Boolean.TRUE);
					customerSMAModel.setDeleted(Boolean.FALSE);
					customerSMAModel.setDefAccount(Boolean.TRUE);
					searchBaseWrapper.setBasePersistableModel(customerSMAModel);

					searchBaseWrapper = commonCommandManager.loadSMAExactMatch(searchBaseWrapper);
					if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
						customerSMAModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
					}
					if(productVo == null)
					{
						throw new CommandException(this.getMessageSource().getMessage("CustomerRetailPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
					}

					String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

					// Velocity validation - start
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
					bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
					bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, receiverRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
					bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, receiverRetailerContactModel.getDistributorLevelId());
					bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(txAmount));
					bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(segmentId));
					bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, receiverRetailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
					bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//					bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
					boolean result = commonCommandManager.checkVelocityCondition(bWrapper);
					// Velocity validation - end

					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_RET_PAYMENT_TX);
						

					DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
					deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
//					CustomerAccount customerAccount = new CustomerAccount(accountNumber,accountType,accountCurrency,accountStatus);
//					workFlowWrapper.setCustomerAccount(customerAccount);
					workFlowWrapper.setProductModel(productModel);
					workFlowWrapper.setCustomerAppUserModel(appUserModel);
					workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
					workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);
					workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
					workFlowWrapper.setAccountInfoModel(accountInfoModel);
					workFlowWrapper.setProductVO(productVo);
					workFlowWrapper.setAppUserModel(appUserModel);
					workFlowWrapper.setSegmentModel(segmentModel);
					workFlowWrapper.setRetailerContactModel(receiverRetailerContactModel);
					workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
					
					StringBuilder logString = new StringBuilder();
					logString.append("[CustomerRetailPaymentCommand.execute] ")
								.append(" Customer SmartMoneyAccountId : " + customerSMAModel.getSmartMoneyAccountId())
								.append(" Customer appUserId: " + appUserModel.getAppUserId());
								
					logger.info(logString.toString());
					workFlowWrapper.setIsCustomerInitiatedTransaction(true);
					workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
					transactionModel = workFlowWrapper.getTransactionModel();
					customerSMAModel = workFlowWrapper.getSmartMoneyAccountModel();
					productModel = workFlowWrapper.getProductModel();
					AgentRetailPaymentVO avo=(AgentRetailPaymentVO) workFlowWrapper.getProductVO();
					balance=avo.getBalance();
					userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();

					commonCommandManager.sendSMS(workFlowWrapper);
				}
				else
				{
					logger.error("[CustomerRetailPaymentCommand.execute()] Exception occured in Validation for appUserID:" + appUserModel.getAppUserId());
					
					throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
				}
			}
			catch(FrameworkCheckedException ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CustomerRetailPaymentCommand.execute()] Exception occured for smartMoneyAccountId:" + customerSMAModel.getSmartMoneyAccountId() + " \n"  + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
			
			catch(WorkFlowException wex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CustomerRetailPaymentCommand.execute()]  Exception occured for smartMoneyAccountId:" + customerSMAModel.getSmartMoneyAccountId() + " \n"  + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
				}
				throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
			}
			catch(Exception ex)
			{
				if(logger.isErrorEnabled())
				{
					logger.error("[CustomerRetailPaymentCommand.execute()] Exception occured for smartMoneyAccountId:" + customerSMAModel.getSmartMoneyAccountId() + " \n" + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				}
				throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
			}
		}
		else
		{
			logger.info("[CustomerRetailPaymentCommand.execute()] Invalid User Type. Throwing Exception for smartMoneyAccountId:" + customerSMAModel.getSmartMoneyAccountId());
			throw new CommandException(this.getMessageSource().getMessage("CustomerRetailPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerRetailPaymentCommand.execute()");
		}
	}

	@Override
	public void prepare(BaseWrapper baseWrapper)
	{
		
		
		this.baseWrapper = baseWrapper;
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		customerMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		agentMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOB_NO);
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
		commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
		txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
		AppUserModel retailerAppUser=new AppUserModel();
		try {
			retailerAppUser=getCommonCommandManager().loadAppUserByMobileAndType(agentMobile);
		} catch (Exception e) {
			logger.error("[CnicToBBAccountCommand.prepare] Unable to load Customer Segment info... ",e);
		}
		if(retailerAppUser.getAppUserTypeId().equals(UserTypeConstantsInterface.RETAILER)) {
			BaseWrapper bWrapper = new BaseWrapperImpl();
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(retailerAppUser.getRetailerContactId());
			bWrapper.setBasePersistableModel(retailerContactModel);
			try{
				bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);

				receiverRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

			}catch(Exception ex){
				logger.error("[CnicToBBAccountCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
			}
		}

		try {
			segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(customerMobile);
		} catch (Exception e) {
			logger.error("[CnicToBBAccountCommand.prepare] Unable to load Customer Segment info... ", e);
		}


		logger.info("[CustomerRetailPaymentCommand.prepare] Sender AppUserID:" + appUserModel.getAppUserId() + ". Receiver Mobile:" + agentMobile);
	}

	@Override
	public String response() 
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Start/End of CustomerRetailPaymentCommand.response()");
		}
		
		try
		{
			/*if(null != smsText && !"".equals(smsText))
			{
				sendSMSToUser(customerMobile, smsText);
			}*/
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
		validationErrors = ValidatorWrapper.doRequired(customerMobile,validationErrors,"Customer Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(agentMobile,validationErrors,"Agent Mobile Number");
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
		validationErrors = ValidatorWrapper.doRequired(txProcessingAmount,validationErrors,"Tx Amount");
		validationErrors = ValidatorWrapper.doRequired(totalAmount,validationErrors,"Total Amount");

		if(!validationErrors.hasValidationErrors())
		{
			validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
			validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Amount");
		}
		return validationErrors;
	}
	
	private String toXML()
	{
		ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
		/*strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_AGENT_MOBILE, replaceNullWithEmpty(receiverRetailerContactModel.getName())));
		strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RECIPIENT_AGENT_NAME, replaceNullWithEmpty(receiverRetailerContactModel.getMobileNo())));*/
		params.add(new LabelValueBean(CommandFieldConstants.KEY_RECIPIENT_AGENT_NAME,replaceNullWithEmpty(transactionModel.getToRetContactName())));
		params.add(new LabelValueBean(CommandFieldConstants.KEY_AGENT_MOBILE, replaceNullWithEmpty(transactionModel.getToRetContactMobNo())));
		params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
		params.add(new LabelValueBean(ATTR_CMOB, replaceNullWithEmpty(customerMobile)));
		params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
		params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));
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
		params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(balance)));

		return MiniXMLUtil.createResponseXMLByParams(params);
	}

}