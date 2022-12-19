package com.inov8.microbank.server.service.commandmodule;

/**
 * Project Name: 			i8Mircobank	
 * @author 					Kashif Bashir
 * Creation Date: 			June 2012  			
 * Description:				
 */

import static com.inov8.microbank.common.util.XMLConstants.ATTR_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_COMMISSION;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FORMATED_AMOUNT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_LEVEL_ONE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRN_PRODUCT_ID;
import static com.inov8.microbank.common.util.XMLConstants.EXCLUSIVE_CHARGES;
import static com.inov8.microbank.common.util.XMLConstants.INCLUSIVE_CHARGES;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MESGS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;
import static com.inov8.microbank.common.util.XMLConstants.TRANSACTION_ID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.MiniStatementListViewModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

public class MiniStatementAgentCommand extends BaseCommand {
	
	protected AppUserModel appUserModel;
	protected String recipientMfsId;
	protected String mfsId;
	protected List<MiniStatementListViewModel> list;
	protected long userType;
	protected String pin;
	private String isHRA;
	protected final Log logger = LogFactory.getLog(MiniStatementAgentCommand.class);
	WorkFlowWrapper workFlowWrapper;
	
	@Override
	public void execute() throws CommandException 
	{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of MiniStatementAgentCommand.execute()");
		}
		CommonCommandManager commonCommandManager = this.getCommonCommandManager();
		workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setHandlerModel(handlerModel);
		workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
		
		try
		{
			ValidationErrors validationError = commonCommandManager.checkActiveAppUser(appUserModel);
			if(!validationError.hasValidationErrors()) {
				Long paymentModeId = null;
				if(isHRA.equals("1"))
					paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
				else
					paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

				SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,paymentModeId);
				if(smartMoneyAccountModel == null && isHRA.equals("1"))
					throw new CommandException("HRA Account does not exist.",ErrorCodes.NO_HRA_EXISTS,ErrorLevel.MEDIUM,new Throwable());
				else if(smartMoneyAccountModel == null && !isHRA.equals("1"))
					paymentModeId = PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT;

				AccountInfoModel accountInfoModel = null;

				MiniStatementListViewModel viewModel = new MiniStatementListViewModel();
		        viewModel.setUserType(userType);
				
			    if(userType == UserTypeConstantsInterface.CUSTOMER) {
					mfsId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
			        viewModel.setRecipientMfsId(mfsId);
			        viewModel.setMfsId(mfsId);
					accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getCustomerId(),paymentModeId);
					if(accountInfoModel != null && accountInfoModel.getAccountNick() != null)
						viewModel.setRecipientAccountNick(accountInfoModel.getAccountNick());
			    }
			       
			    if(userType == UserTypeConstantsInterface.RETAILER) {
			        viewModel.setAgent1Id(recipientMfsId);
			        viewModel.setAgent2Id(mfsId);
					accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getAppUserId(),paymentModeId);
					if(accountInfoModel != null && accountInfoModel.getAccountNick() != null)
						viewModel.setSenderAccountNick(accountInfoModel.getAccountNick());
			    }
				viewModel.setPaymentModeId(paymentModeId);

				list =	commonCommandManager.getMiniStatementListViewModelList(viewModel, Integer.valueOf(5));
								
			} else {
				
				throw new CommandException(validationError.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}
		}
		catch(FrameworkCheckedException ex)
		{
			throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		catch(Exception ex)
		{
			throw new CommandException(this.getMessageSource().getMessage("MiniStatementAgentCommand.generalErrorOccured", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("End of MiniStatementAgentCommand.execute()");
		}
	}
	

	@Override
	public void prepare(BaseWrapper baseWrapper) 
	{
		if(logger.isDebugEnabled()) {
			logger.debug("Start of MiniStatementAgentCommand.prepare()");
		}
		appUserModel = ThreadLocalAppUser.getAppUserModel();
		deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
		if(deviceTypeId.equalsIgnoreCase(DeviceTypeConstantsInterface.ALL_PAY.toString())){
			mfsId = this.getCommandParameter(baseWrapper, "APID");
			recipientMfsId = this.getCommandParameter(baseWrapper, "APID");
				
		}else{
			mfsId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
			recipientMfsId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_U_ID);
			
		}

		isHRA = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_MODE);

		userType = appUserModel.getAppUserTypeId().longValue();
		if(logger.isDebugEnabled())	{
			logger.debug("End of MiniStatementAgentCommand.prepare() \n"+
					"appUserModel : "+appUserModel+
					"\nmfsId : "+mfsId+
					"\nrecipientMfsId : "+recipientMfsId+
					"\ndeviceTypeId : "+deviceTypeId);
		}
	}

	@Override
	public String response()
	{
		return toXML();
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		if(logger.isDebugEnabled()) {
			logger.debug("Start of MiniStatementAgentCommand.validate()");
		}

        if(userType != UserTypeConstantsInterface.CUSTOMER) {
            if (!isValidString(mfsId)) {
                validationErrors = ValidatorWrapper.doRequired(mfsId, validationErrors, "MFS Id (UID)");
            }

            if (!isValidString(recipientMfsId)) {
                validationErrors = ValidatorWrapper.doRequired(recipientMfsId, validationErrors, "Recipient MFS Id (UID)");
            }
        }

		return validationErrors;
	}
	
	private String createAttribute(String name, String value) {
	
		StringBuilder strBuilder = new StringBuilder();
	
		strBuilder.append(TAG_SYMBOL_SPACE)
					.append(name)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(value)
					.append(TAG_SYMBOL_QUOTE);
		
		return strBuilder.toString();
	}
	
	private String toXML() {
		StringBuilder strBuilder = new StringBuilder();

		if(new Long(deviceTypeId).longValue() == DeviceTypeConstantsInterface.USSD)
		{
			sendMiniStatementSMS(list);

		}

		else
		{


			if(list != null && !list.isEmpty()) {
			
				strBuilder.append(TAG_SYMBOL_OPEN);
				strBuilder.append(TAG_TRANS);
				strBuilder.append(TAG_SYMBOL_CLOSE);
				
				if(new Long(deviceTypeId).longValue() == DeviceTypeConstantsInterface.ALL_PAY)
				{
					for(MiniStatementListViewModel tranModel : list) {
						
						strBuilder.append(TAG_SYMBOL_OPEN);
						strBuilder.append(TAG_TRN);
					
						SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMMM yyyy");
						
						strBuilder.append(createAttribute(ATTR_TRN_DATE.toUpperCase(), 	Formatter.formatDate(tranModel.getCreatedOn())));
						strBuilder.append(createAttribute(ATTR_TRN_DATEF.toUpperCase(), sdf.format(tranModel.getCreatedOn())));
						strBuilder.append(createAttribute("description".toUpperCase(), replaceNullWithEmpty(tranModel.getProductName())));
						strBuilder.append(createAttribute("TAMT", replaceNullWithEmpty(getString(tranModel.getTransactionAmount()))));
						strBuilder.append(createAttribute("TAMTF", Formatter.formatNumbers(tranModel.getTransactionAmount())));
						
						
						strBuilder.append(TAG_SYMBOL_CLOSE);				
						strBuilder.append(TAG_SYMBOL_OPEN);
						strBuilder.append(TAG_SYMBOL_SLASH);
						strBuilder.append(TAG_TRN);
						strBuilder.append(TAG_SYMBOL_CLOSE);
					}
				}else{
					for(MiniStatementListViewModel tranModel : list) {
						
						strBuilder.append(TAG_SYMBOL_OPEN);
						strBuilder.append(TAG_TRN);
		
						strBuilder.append(createAttribute(TRANSACTION_ID, replaceNullWithEmpty(getString(tranModel.getTransactionCode()))));
						strBuilder.append(createAttribute(ATTR_TRN_PRODUCT, replaceNullWithEmpty(tranModel.getProductName())));
						strBuilder.append(createAttribute(ATTR_TRN_PRODUCT_ID, replaceNullWithEmpty(getString(tranModel.getProductId()))));
						strBuilder.append(createAttribute(ATTR_AMOUNT, replaceNullWithEmpty(getString(tranModel.getTransactionAmount()))));
						strBuilder.append(createAttribute(ATTR_COMMISSION, replaceNullWithEmpty(getString(tranModel.getAgentCommission()))));
						strBuilder.append(createAttribute(EXCLUSIVE_CHARGES, replaceNullWithEmpty(getString(tranModel.getExclusiveCharges()))));
						strBuilder.append(createAttribute(INCLUSIVE_CHARGES, replaceNullWithEmpty(getString(tranModel.getInclusiveCharges()))));
						
						strBuilder.append(createAttribute(ATTR_TRN_DATE, replaceNullWithEmpty(getStringDate(tranModel.getCreatedOn()))));
						strBuilder.append(createAttribute("AGENT_1_ID", replaceNullWithEmpty(getString(tranModel.getAgent1Id()))));
						strBuilder.append(createAttribute("AGENT_2_ID", replaceNullWithEmpty(getString(tranModel.getAgent2Id()))));
						strBuilder.append(createAttribute("AGENT_1_COMM", replaceNullWithEmpty(getString(tranModel.getAgentCommission()))));
						strBuilder.append(createAttribute("AGENT_2_COMM", replaceNullWithEmpty(getString(tranModel.getAgent2Commission()))));
						strBuilder.append(createAttribute("TAMT", replaceNullWithEmpty(getString(tranModel.getTotalAmount()))));
						
						strBuilder.append(TAG_SYMBOL_CLOSE);				
						strBuilder.append(TAG_SYMBOL_OPEN);
						strBuilder.append(TAG_SYMBOL_SLASH);
						strBuilder.append(TAG_TRN);
						strBuilder.append(TAG_SYMBOL_CLOSE);
					}
				}

				strBuilder.append(TAG_SYMBOL_OPEN);
				strBuilder.append(TAG_SYMBOL_SLASH);
				strBuilder.append(TAG_TRANS);
				strBuilder.append(TAG_SYMBOL_CLOSE);
			
			} else {
				
				strBuilder.append(TAG_SYMBOL_OPEN)
					.append(TAG_MESGS)
					.append(TAG_SYMBOL_CLOSE)
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_MESG)
					.append(TAG_SYMBOL_SPACE)
					.append(ATTR_LEVEL)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(ATTR_LEVEL_ONE)
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_CLOSE)
					.append(this.getMessageSource().getMessage("fetchTransactionsCommand.noTransactionAvailable", null,null))
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_MESG)
					.append(TAG_SYMBOL_CLOSE)
					.append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH)
					.append(TAG_MESGS)
					.append(TAG_SYMBOL_CLOSE);
			}
		
		
		}
		return strBuilder.toString();
	}
	/**
	 * @param string
	 * @return
	 */
	public static Boolean isValidString(String string) {
		
		Boolean isValidString = Boolean.FALSE;
		
		if(null != string && 
				!"".equals(string)) {
			
			isValidString = Boolean.TRUE;
		}
		
		return isValidString;
	}
	
	
	/**
	 * @param value
	 * @return
	 */
	private String getString(Object value) {
		
		if(value != null) {
			
			return value.toString();
		}
		
		return null;
	}
	
	
	/**
	 * @param date
	 * @return String
	 */
	private String getStringDate(Date date) {
		
		String dateString = null;
		
		if(date != null) {
			
			dateString = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
		}
		
		return dateString;
	}
	
	private void sendMiniStatementSMS(List<MiniStatementListViewModel> list)
	{
		BaseWrapper baseWrapper;
		int counter = 0;
		String message;
		for(MiniStatementListViewModel tranModel : list) {
		counter ++;	
		
		message = "Trx "+counter+" of "+list.size()+"\n\nTrx ID : "+tranModel.getTransactionCode()+"\nProduct Name : "+tranModel.getProductName()+"\n" +
				/*"Trx Amount : "+tranModel.getTransactionAmount()+*/"\nTrx Date : "+PortalDateUtils.formatDate(tranModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_FORMAT);
		if(tranModel.getProductId().longValue() == 50006L && userType == UserTypeConstantsInterface.CUSTOMER)
		{
			message += "\nDebit Amount : "+tranModel.getTotalAmount();
		}
		else if (tranModel.getProductId().longValue() == 50006L && userType == UserTypeConstantsInterface.RETAILER)
		{
			message += "\nCredit Amount : "+tranModel.getTransactionAmount()+tranModel.getAgentCommission();
		}
		else if(tranModel.getProductId().longValue() == 50002L && userType == UserTypeConstantsInterface.CUSTOMER)
		{
			message += "\nCredit Amount : "+tranModel.getTransactionAmount();
		}
		else if(tranModel.getProductId().longValue() == 50002L && userType == UserTypeConstantsInterface.RETAILER)
		{
			if(null==tranModel.getAgentCommission()){
				tranModel.setAgentCommission(0d);
			}
			message += "\nDebit Amount : "+(tranModel.getTotalAmount()-tranModel.getAgentCommission());
		}
		else if(tranModel.getProductId().longValue() == 50013L && userType == UserTypeConstantsInterface.RETAILER)
		{
			
			if(tranModel.getAgent1Id().equals(mfsId))
			{
				message += "\nDebit Amount : "+tranModel.getTotalAmount();
			}
			else if(tranModel.getAgent2Id().equals(mfsId))
			{
				message += "\nCredit Amount : "+tranModel.getTransactionAmount();
			}
		}

		else if(tranModel.getProductId().longValue() == 50010L) {
			
			if(userType == UserTypeConstantsInterface.RETAILER) {
				
				message += "\nCredit Amount : "+(tranModel.getTransactionAmount()+tranModel.getAgent2Commission());
				
			} else if(userType == UserTypeConstantsInterface.CUSTOMER) 	{
				
				String status = tranModel.getProcessingStatusName();
				
				if(SupplierProcessingStatusConstants.REVERSED_NAME.equalsIgnoreCase(status)) {

					message += "\nCredit Amount : "+(tranModel.getTransactionAmount()+tranModel.getAgent2Commission());
					
				} else if(SupplierProcessingStatusConstants.IN_PROCESS.equalsIgnoreCase(status)) {

					message += "\nDebit Amount : "+(tranModel.getTotalAmount());
					
				} else if(SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase(status)) {

					message += "\nDebit Amount : "+(tranModel.getTransactionAmount()+tranModel.getAgent2Commission());
					
				}				
			}
		}

		else if(tranModel.getProductId().longValue() == 50011L && userType == UserTypeConstantsInterface.RETAILER)
		{
			if(mfsId.equals(tranModel.getAgent1Id())) {
				
				message += "\nDebit Amount : "+(tranModel.getTotalAmount()-tranModel.getAgentCommission());
				
			} 
			if(mfsId.equals(tranModel.getAgent2Id())) {
				
				message += "\nCredit Amount : "+(tranModel.getTransactionAmount()+tranModel.getAgent2Commission());
			}
		}
		
		else if(tranModel.getProductId().longValue() == 50000L && userType == UserTypeConstantsInterface.CUSTOMER)
		{
			if(tranModel.getMfsId().equals(mfsId))
			{
				message += "\nDebit Amount : "+tranModel.getTotalAmount();
			}
			else if(tranModel.getRecipientMfsId().equals(mfsId))
			{
				message += "\nCredit Amount : "+tranModel.getTransactionAmount();
			}
		}
		else if(UtilityCompanyEnum.contains(String.valueOf(tranModel.getProductId())) 
				|| InternetCompanyEnum.contains(String.valueOf(tranModel.getProductId()))
				|| DonationCompanyEnum.contains(String.valueOf(tranModel.getProductId())))
		{
			message += "\nDebit Amount : "+tranModel.getTransactionAmount();
		
		}else if(tranModel.getProductId().longValue() == 50018L && userType == UserTypeConstantsInterface.CUSTOMER) {
			
			message += "\nDebit Amount : "+tranModel.getTotalAmount();
		
		}else if(tranModel.getProductId().longValue() == 50018L && userType == UserTypeConstantsInterface.RETAILER) {
			
			if(null==tranModel.getAgentCommission()){
				tranModel.setAgentCommission(0d);
			}
			message += "\nCredit Amount : "+(tranModel.getTransactionAmount() + tranModel.getAgentCommission());
		}
			
	    baseWrapper = new BaseWrapperImpl();
		SmsMessage smsMessage = new SmsMessage(ThreadLocalAppUser.getAppUserModel().getMobileNo(), message, this.getMessageSource().getMessage("MINI.shortCode", null, null));
		baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, smsMessage);
		try
		{
			this.getCommonCommandManager().sendSMSToUser(baseWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			ex.printStackTrace();
		}
	}
   }
}