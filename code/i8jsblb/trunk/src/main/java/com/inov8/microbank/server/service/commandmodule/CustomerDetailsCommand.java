package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.*;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * @author Abu Turab Munir
 * @Dated  April 12, 2016
 * @Description: purpose of the document is to check whether customer's mobile number and CNIC already exists in MicroBank or not, 
 *               and fetch the details from NADRA regarding the customer's CNIC, Mobile and Thumb Impression
 * 
 */

public class CustomerDetailsCommand extends BaseCommand {

	protected final Log logger = LogFactory.getLog(CustomerDetailsCommand.class);
	protected BaseWrapper preparedBaseWrapper;
	
	private String cMsisdn;
	private String isHRA;
	private Double dailyDebitConsumed=0.0,monthlyDebitConsumed=0.0,yearlyDebitConsumed=0.0;
	private Double dailyCreditConsumed=0.0,monthlyCreditConsumed=0.0,yearlyCreditConsumed=0.0;
	private Double maxDebitLimit,maxCreditLimit;
	private List<Double> remainingLimits;
	//
	protected AppUserModel appUserModel;
	protected List customerDetailList;

	public void prepare(BaseWrapper baseWrapper) {
		if (this.logger.isDebugEnabled())
			this.logger.debug("Start of CustomerNadraVerificationCommand.prepare()");
		
		this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
		this.isHRA = getCommandParameter(baseWrapper, "PAYMENT_MODE");
	}

	@Override
	public void doValidate() throws CommandException
	{
		if (this.logger.isDebugEnabled())
			this.logger.debug("Start of CustomerNadraVerificationCommand.validate()");
		ValidationErrors validationErrors = new ValidationErrors();

		
		validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "MSISDN");
		validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");
		
		if(validationErrors.hasValidationErrors())
		{	
			throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
		}
		
		if (this.logger.isDebugEnabled())
			this.logger.debug("End of CustomerNadraVerificationCommand.validate()");
	}


	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
	{
		return new ValidationErrors();
	}

	@Override
	public void execute() throws CommandException
	{
		try
		{
			Long[] appUserTypes = new Long[]{UserTypeConstantsInterface.CUSTOMER};
			appUserModel = this.commonCommandManager.loadAppUserByMobileAndType(this.cMsisdn,appUserTypes);
			if(appUserModel == null)
				throw new CommandException("No customer exists against this Mobile No.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

			Long paymentModeId = null;
			Long customerAccountType= null;
			if(isHRA.equals("1"))
			{
				paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
				customerAccountType = CustomerAccountTypeConstants.HRA;
			}
			else
			{
				paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
				customerAccountType = CustomerAccountTypeConstants.LEVEL_1;
			}

			SmartMoneyAccountModel smartMoneyAccountModel = this.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,paymentModeId);
			if(smartMoneyAccountModel == null && !isHRA.equals("1"))
				throw new CommandException("Account is closed.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			if(smartMoneyAccountModel == null && isHRA.equals("1"))
				throw new CommandException("HRA Account does not exist.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
			Long statusId = OlaStatusConstants.ACCOUNT_STATUS_ACTIVE;
			AccountModel accountModel = null;
			customerDetailList=this.getCommonCommandManager().getCustomerDetails(appUserModel);
			accountModel = getCommonCommandManager().getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(),customerAccountType,statusId);
			if(accountModel != null)
			{
				Long accountId = accountModel.getAccountId();
				Date currentDate = new Date();
				Calendar cal = GregorianCalendar.getInstance();
				Date startDate;
				//daily debit consumed
				dailyDebitConsumed = getCommonCommandManager().getDailyConsumedBalance(accountId, TransactionTypeConstants.DEBIT, currentDate,null);
				//daily credit consumed
				dailyCreditConsumed = getCommonCommandManager().getDailyConsumedBalance(accountId, TransactionTypeConstants.CREDIT, currentDate,null);
				cal.setTime(new Date());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				startDate = cal.getTime();

				monthlyDebitConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);
				//monthly credit consumed
				monthlyCreditConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, currentDate);
				//yearly debit consumed
				cal.setTime(new Date());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.MONTH,0);
				startDate = cal.getTime();

				yearlyDebitConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);
				//yearly credit consumed
				yearlyCreditConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, currentDate);

				remainingLimits=calculateLimits(dailyDebitConsumed,dailyCreditConsumed,monthlyDebitConsumed,monthlyCreditConsumed,
						yearlyDebitConsumed,yearlyCreditConsumed,customerAccountType);
			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
			throw new CommandException(MessageUtil.getMessage(ex.getMessage()),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
		}
	}

	@Override
	public String response() {
		StringBuilder strBuilder = new StringBuilder();
		if(!customerDetailList.isEmpty())
		{
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("CNAME")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(0)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("FNAME")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(1)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("MOTHER_MAIDEN_NAME")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(2)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("CNIC")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(appUserModel.getNic()).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("CMOB")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(this.cMsisdn).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("GENDER")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(3)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("BIRTH_PLACE")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(4)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("ACCOUNT_TYPE")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(5)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("SEGMENT")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(6)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("CITY_NAME")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(7)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("TAX_REGIME")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(8)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("FED")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(9)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("CNIC_Expiry")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(10).toString().split(" ")[0]).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("DOB")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(11).toString().split(" ")[0]).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("REG_STATE")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(12)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("ACCOUNT_STATE")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(customerDetailList.get(13)).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("Used_Daily_Debit_Limit")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(dailyDebitConsumed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("Used_Daily_Credit_Limit")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(dailyCreditConsumed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("Used_Monthly_Debit_Limit")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(monthlyDebitConsumed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("Used_Monthly_Credit_Limit")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(monthlyCreditConsumed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("Used_Yearly_Debit_Limit")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(yearlyDebitConsumed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("Used_Yearly_Credit_Limit")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(yearlyCreditConsumed).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
					.append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		}
		else
		{
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append("Empty")
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append("No Record found against this mobile No.").append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
					.append(TAG_SYMBOL_CLOSE);

			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
					.append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		}

		return strBuilder.toString();
	}
	private List<Double> calculateLimits(Double dailyDebitConsumed,Double dailyCreditConsumed,Double monthlyDebitConsumed,Double monthlyCreditConsumed,
										 Double yearlyDebitConsumed,Double yearlyCreditConsumed,Long customerAccountTypeId) throws FrameworkCheckedException
	{
		List<LimitModel> limits = getCommonCommandManager().getLimitsByCustomerAccountType(customerAccountTypeId);
		List<Double> remainingLimits=new ArrayList<>();

		Double remainingDailyCreditLimit 	= 0.0;
		Double remainingDailyDebitLimit 	= 0.0;
		Double remainingMonthlyCreditLimit 	= 0.0;
		Double remainingMonthlyDebitLimit 	= 0.0;
		Double remainingYearlyCreditLimit 	= 0.0;
		Double remainingYearlyDebitLimit 	= 0.0;

		for (LimitModel limitModel : limits) {
			if( limitModel.getLimitTypeId().equals(LimitTypeConstants.DAILY) ){
				if(limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)){
					remainingDailyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - dailyCreditConsumed;
					remainingDailyCreditLimit = remainingDailyCreditLimit < 0 ? 0 : remainingDailyCreditLimit;
				}else{
					remainingDailyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - dailyDebitConsumed;
					remainingDailyDebitLimit = (remainingDailyDebitLimit < 0 ? 0 : remainingDailyDebitLimit);
				}
			}else if(limitModel.getLimitTypeId().equals(LimitTypeConstants.MONTHLY)){
				if(limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)){
					remainingMonthlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - monthlyCreditConsumed;
					remainingMonthlyCreditLimit = remainingMonthlyCreditLimit < 0 ? 0 : remainingMonthlyCreditLimit;
				}else{
					remainingMonthlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - monthlyDebitConsumed;
					remainingMonthlyDebitLimit = remainingMonthlyDebitLimit < 0 ? 0 : remainingMonthlyDebitLimit;
				}
			}else if(limitModel.getLimitTypeId().equals(LimitTypeConstants.YEARLY)){
				if(limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)){
					remainingYearlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - yearlyCreditConsumed;
					remainingYearlyCreditLimit = remainingYearlyCreditLimit < 0 ? 0 : remainingYearlyCreditLimit;
				}else{
					remainingYearlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum() ) - yearlyDebitConsumed;
					remainingYearlyDebitLimit = remainingYearlyDebitLimit < 0 ? 0 : remainingYearlyDebitLimit;
				}
			}
		}
		remainingLimits.add(remainingDailyCreditLimit);
		remainingLimits.add(remainingDailyDebitLimit);
		remainingLimits.add(remainingMonthlyCreditLimit);
		remainingLimits.add(remainingMonthlyDebitLimit);
		remainingLimits.add(remainingYearlyCreditLimit);
		remainingLimits.add(remainingYearlyDebitLimit);
		return remainingLimits;
	}
}


