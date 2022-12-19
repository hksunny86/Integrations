package com.inov8.microbank.server.service.dailyjob;

import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.OlaStatusConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.ledger.LedgerManager;
import com.inov8.ola.util.TransactionTypeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.*;
import org.springframework.util.StopWatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MarkUserDormantScheduler {


	private static final Logger LOGGER = Logger.getLogger( MarkUserDormantScheduler.class );
	private AppUserManager	appUserManager;
	private LedgerManager	ledgerManager;
	private AccountManager	accountManager;
	private SmsSenderService smsSenderService;
	private ArrayList<SmsMessage> smsMessageList = new ArrayList<>();
	private SmsMessage smsMessage;
	//no argument default constructor
	public MarkUserDormantScheduler(){
	}

	@SuppressWarnings("unchecked")
	public void init() {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start("MarkUserDormant Scheduler init");
			int ORACLE_LIMIT = 1000;
	        LOGGER.info("*********** Executing MarkUserDormant Scheduler ***********");
	        int dormantMarkedSize = 0;
			int dormantAlertDays = MessageUtil.getIntMessage("job.dormant.expiry.alert.days");
			String dormantAlertMessage = MessageUtil.getMessage("markUserDormantScheduler.beforeDormancyAlert",dormantAlertDays);
			//int noOfDaysToMarkDormant;
			/*int noOfDaysToMarkDormant= MessageUtil.getIntMessage("job.dormant.activation.tx.days");
			int noOfYearsToSkipDormant = MessageUtil.getIntMessage("job.cnic.expiry.alert.years");*/
	        try
	        {
	        	Calendar cal = GregorianCalendar.getInstance();
	    		Date currentDate = new Date();
	    		List<AccountModel> dormantAccountModelList = new ArrayList<AccountModel>();
	    		List<AccountModel> accountModelList = new ArrayList<AccountModel>();
	    		List<Object[]> cnicListObjects = appUserManager.getAppUserCNICsToMarkDormant();
	    		List<String> cnicList = new ArrayList<String>(cnicListObjects.size());
	    		for(Object[] a : cnicListObjects){ //add all CNIC in list for in clause
	    			cnicList.add(a[0].toString());
	    		}

	    		if(CollectionUtils.isNotEmpty(cnicList))
	    		{
		    		if(cnicList.size() <= ORACLE_LIMIT){
		    			accountModelList = accountManager.getAccountModelListByCNICs(cnicList);
		    		}else{//handle oracle limit of 1000 IN CLAUSE
		    			List<String>[] chunks = this.chunks(cnicList, ORACLE_LIMIT);
		    			for (List<String> inClauseByThousands : chunks) {
		    				accountModelList.addAll(accountManager.getAccountModelListByCNICs(inClauseByThousands));

		    			}
		    		}
	    		}

	    		int noOfMonths=0;
	    		for(Object[] a : cnicListObjects){
	    			String cnic = a[0].toString();
					/*if(cnic.equals("3430114151923"))
					{
						System.out.println("Done: " + cnic);
					}*/
	    			String strMonth = a[1].toString();
	    			String strDormancyRemovedOn = a[2] == null ? null : a[2].toString();
					String mobileNo =  a[3] == null ? null : a[3].toString();
					//String dobString = a[4] == null ? null : a[4].toString();
		    		for(AccountModel accountModel : accountModelList){
		        		String decryptedCNIC = accountModel.getAccountHolderIdAccountHolderModel().getCnic();//EncryptionUtil.decryptPin(accountModel.getAccountHolderIdAccountHolderModel().getCnic());
		        		if(cnic.equals(decryptedCNIC))
		        		{

							// if user's age has gone beyond {job.cnic.expiry.alert.years}
							// the account is not to be marked as dormant
							/*DateFormat dobDf = new SimpleDateFormat("yyyy-MM-dd");
							Date dob = null;
							if(dobString != null)
								dob = dobDf.parse(dobString);
							Calendar dobCalender = Calendar.getInstance();
							dobCalender.setTime(dob);
							DateTime dobStart = new DateTime(dobCalender.getTime());
							DateTime currentDateTime = new DateTime(currentDate.getTime());
							int years = Years.yearsBetween(dobStart,currentDateTime).getYears();*/
							/*if(years >= noOfYearsToSkipDormant){
								continue;
							}*/

							// account has not been marked dormant yet
							// the limit is number of months defined in DB.
							if(strDormancyRemovedOn == null)
							{
								/*if(cnic.equals("3430114151923"))
								{
									if(decryptedCNIC.equals("3430114151923"))
										System.out.println("Done: " + cnic);
								}
								noOfDaysToMarkDormant = MessageUtil.getIntMessage("job.dormant.mark.days");*/
								noOfMonths = (int) Double.parseDouble(strMonth);
								cal.setTime(accountModel.getCreatedOn());
								DateTime start = new DateTime(cal.getTime());
								DateTime end= new DateTime(currentDate.getTime());
								int months = Months.monthsBetween(start,end).getMonths();
								if (isDormantRequired(accountModel,cal,currentDate,strDormancyRemovedOn)) {
									dormantAccountModelList.add(accountModel);
								}
								else if(months >= noOfMonths){
									if(isDormantRequired(accountModel,cal,currentDate,strDormancyRemovedOn)){
										dormantAccountModelList.add(accountModel);
									}
								}
								// send sms alert if applicable
								else {
									Calendar dormantCalender = Calendar.getInstance();
									dormantCalender.setTime(accountModel.getCreatedOn());
									dormantCalender.add(Calendar.MONTH, noOfMonths);
									dormantCalender.add(Calendar.DAY_OF_WEEK, -dormantAlertDays);
									int dateComparison = DateTimeComparator.getDateOnlyInstance().compare(dormantCalender.getTime(),currentDate);
									if(dateComparison == 0){
										if(isDormantRequired(accountModel,dormantCalender,currentDate,strDormancyRemovedOn)){
											addToSmsList(mobileNo,dormantAlertMessage);
										}
									}
								}

							}
							// account has marked dormant and revoked before
							// now the limit is number of days only {noOfDaysToMarkDormant}
		        			else
		        			{
								int noOfDaysToMarkDormant = MessageUtil.getIntMessage("job.dormant.activation.tx.days");
								if(decryptedCNIC.equals("3520253851081"))
									System.out.println("Done: " + cnic + strDormancyRemovedOn);
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
								Date dormancyRemovedOn = df.parse(strDormancyRemovedOn);
								cal.setTime(dormancyRemovedOn);
								cal.add(Calendar.DAY_OF_WEEK, noOfDaysToMarkDormant);
								Date dateToMarkDormant = cal.getTime();
								int comparisonToMarkDormant = DateTimeComparator.getDateOnlyInstance().compare(currentDate,dateToMarkDormant);
								// dates are equal, mark the account as dormant
								if(comparisonToMarkDormant == 1 || comparisonToMarkDormant == 0){
									cal.setTime(dormancyRemovedOn);
									if(isDormantRequired(accountModel,cal,currentDate,dormancyRemovedOn.toString())){
										dormantAccountModelList.add(accountModel);
									}
								}
								// send sms alert if applicable
								else{
									if(comparisonToMarkDormant > 0){
										DateTime start = new DateTime(currentDate);
										DateTime end = new DateTime(dateToMarkDormant);
										int days = Days.daysBetween(start,end).getDays();
										if(days == dormantAlertDays){
											addToSmsList(mobileNo,dormantAlertMessage);
										}
									}
								}

		        			}
		        		}
		        	}

	    		}//loop through all cnic and mark dormant as per time provided in OlaCustomerAccountTypeModel

	        	if(dormantAccountModelList.size() > 0){
					List<AppUserModel> dormantMarkedList = appUserManager.markAppUserDormant(dormantAccountModelList);
	        		dormantMarkedSize = dormantMarkedList.size();
	        	}
				if(smsMessageList != null && CollectionUtils.isNotEmpty(smsMessageList)){
					smsSenderService.sendSmsList(smsMessageList);
				}
	        }
	        catch(Exception e)
	        {
	        	LOGGER.error( e.getMessage(), e );
	        }

	        LOGGER.info("Total " + dormantMarkedSize + " accounts were marked as Dormant");
	        LOGGER.info("*********** Finished Executing MarkUserDormant Scheduler ***********");

	        stopWatch.stop();
	        LOGGER.info(stopWatch.prettyPrint());
	    }

	private void addToSmsList(String mobileNo, String msg){
		if (!StringUtil.isNullOrEmpty(mobileNo)) {
			smsMessage = new SmsMessage(mobileNo, msg);
			smsMessageList.add(smsMessage);
		}
	}

	private Boolean isDormantRequired(AccountModel accountModel, Calendar cal, Date currentDate,String strDormancyRemovedOn){
		Date startDate = null;
		Date endDate = null;
		Boolean isDormantRequired = Boolean.FALSE;
		try {
			if(accountModel.getCustomerAccountTypeId() == 3L || accountModel.getCustomerAccountTypeId() == 9L)
				return false;
			if(accountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_CLOSED))
				return false;
			DateFormat tempFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			Calendar debitCredCalendar = GregorianCalendar.getInstance();
			int noOfDaysToMarkDormant = 1;

			if(strDormancyRemovedOn == null)
			{
				noOfDaysToMarkDormant = MessageUtil.getIntMessage("job.dormant.mark.days");
				debitCredCalendar.setTime(currentDate);
				debitCredCalendar.add(Calendar.DAY_OF_WEEK, -noOfDaysToMarkDormant);
				startDate = debitCredCalendar.getTime();
				endDate = currentDate;

			}
			else
			{
				noOfDaysToMarkDormant = MessageUtil.getIntMessage("job.dormant.activation.tx.days");
				startDate = tempFormat.parse(strDormancyRemovedOn);
				debitCredCalendar.setTime(startDate);
				debitCredCalendar.add(Calendar.DAY_OF_WEEK, noOfDaysToMarkDormant);
				endDate = debitCredCalendar.getTime();
			}
			Double monthlyCreditConsumed = ledgerManager.getConsumedBalanceByDateRangeForDormancy(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, startDate, endDate);
			Double monthlyDebitConsumed = ledgerManager.getConsumedBalanceByDateRangeForDormancy(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, startDate, endDate);

			if((monthlyCreditConsumed !=null && monthlyCreditConsumed > 0) || (monthlyDebitConsumed != null && monthlyDebitConsumed > 0))
			{
				LOGGER.info("MarkUserDormantScheduler.isDormantRequired():: Monthly Debit Consumed > 0 or MonthlyCreditConsumed > 0:: ACCOUNT_ID: " + accountModel.getAccountId()
						+ ",START_DATE: " + startDate + ",END_DATE: " + endDate + ",CURRENT_DATE: " + currentDate );

				return isDormantRequired;
			}
			else if(strDormancyRemovedOn == null)
			{
				if (Days.daysBetween(new DateTime(accountModel.getCreatedOn().getTime()),new DateTime(currentDate.getTime())).getDays() > MessageUtil.getIntMessage("job.dormant.mark.days")){
					LOGGER.info("MarkUserDormantScheduler.isDormantRequired():Account is older than "+MessageUtil.getIntMessage("job.dormant.mark.days")+": ACCOUNT_ID: " + accountModel.getAccountId()
							+ ",Account Created ON: " + accountModel.getCreatedOn()+ ",CURRENT_DATE: " + currentDate );
					isDormantRequired = Boolean.TRUE;
					return isDormantRequired;
				}
			}
			else {
				if (Days.daysBetween(new DateTime(endDate),new DateTime(currentDate)).getDays() > 0){
					LOGGER.info("MarkUserDormantScheduler.isDormantRequired():ACCOUNT_ID: " + accountModel.getAccountId()
							+ "endDate " + endDate+ ",CURRENT_DATE: " + currentDate );
					isDormantRequired = Boolean.TRUE;
					return isDormantRequired;
				}
			}
		} catch(Exception e){
			LOGGER.error("Account ID: " + accountModel.getAccountId() + " Exception: " + e.getMessage(),e);
		}
		finally {
			LOGGER.info("MarkUserDormantScheduler.isDormantRequired():: ACCOUNT_ID: " + accountModel.getAccountId()
					+ ",START_DATE: " + startDate + ",END_DATE: " + endDate + ",CURRENT_DATE: " + currentDate + "\nDORMANT_REQUIRED = " + isDormantRequired);
		}
		return false;
	}
	private List[] chunks(final List<String> pList, final int pSize)
    {
        if(pList == null || pList.size() == 0 || pSize == 0) return new List[] {};
        if(pSize < 0) return new List[] { pList };

        // Calculate the number of batches
        int numBatches = (pList.size() / pSize) + 1;

        // Create a new array of Lists to hold the return value
        List[] batches = new List[numBatches];

        for(int index = 0; index < numBatches; index++)
        {
            int count = index + 1;
            int fromIndex = Math.max(((count - 1) * pSize), 0);
            int toIndex = Math.min((count * pSize), pList.size());
            batches[index] = pList.subList(fromIndex, toIndex);
        }

        return batches;
    }

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}


	public void setLedgerManager(LedgerManager ledgerManager) {
		this.ledgerManager = ledgerManager;
	}


	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setSmsSenderService(SmsSenderService smsSenderService) {
		this.smsSenderService = smsSenderService;
	}


}
