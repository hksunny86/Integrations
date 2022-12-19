package com.inov8.microbank.server.service.mfssubscriptionremindermodule;


import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.mfssubscriptionremindermodule.DeviceSubReminderViewModel;
import com.inov8.microbank.common.util.SmsSender;



public class DeviceSubscriptionReminderSchedular extends QuartzJobBean
{
	private MessageSource messageSource;	
	private SmsSender smsSender;	
	private String reminderInterval;
	private DeviceSubscriptionReminderManager deviceSubscriptionReminderManager;
	protected static Log logger = LogFactory.getLog(DeviceSubscriptionReminderSchedular.class);
	
	public void setReminderInterval(String pReminderInterval) {
		this.reminderInterval = pReminderInterval;
	}	
	
	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException
	{
			
		logger.info( ".............DeviceSubscriptionReminder called..................." ) ;
		sendReminders();
		lockUnpaidAccounts();
	}
	
	/**
	 * This method locks the user accounts whose expiry dates have been passed 
	 * with out payment
	 * 
	 * */
	public void lockUnpaidAccounts(){
		
		try {
			logger.info( ".............locking accounts..................." ) ;
			deviceSubscriptionReminderManager.lockUnpaidAccounts();
		}
		catch (Exception e){
			logger.error(e.getMessage());
		}
	}	
	
	/*
	 *  This method send reminder to users about MWallet expiry date
	 *  
	 * **/
	public void sendReminders (){
		
		try
		{
			CustomList<DeviceSubReminderViewModel> userDeviceAccountModelList = null;
			logger.info( ".............checking for reminders..................." ) ;
			try{
				SearchBaseWrapper sWrapper = new SearchBaseWrapperImpl(); 
				
				
				Vector reminderIntervalVec = this.getIntervals(reminderInterval);
				Double days =new Double(getMaximumInterval(reminderIntervalVec));
				
				logger.info(" ...... Max Day for reminder:" + days.doubleValue());
				
				sWrapper.putObject("days", days);
				userDeviceAccountModelList = deviceSubscriptionReminderManager.getNoOfDays(sWrapper).getCustomList() ;
				
				logger.info("the size of the list is "+userDeviceAccountModelList.getResultsetList().size());
								
				for( DeviceSubReminderViewModel miniSubReminderViewModel : userDeviceAccountModelList.getResultsetList() )
				{						
					Date currentDate = new Date(); // system date
					double diff = ((miniSubReminderViewModel.getExpiryDate().getTime()+ 24*3600*1000)-currentDate.getTime());
					
					// Calculate the remaining days
					int intDays = (int)Math.ceil(diff/(1000*3600*24)); 
					checkandsendSMS(miniSubReminderViewModel.getMobileNo() , intDays, reminderIntervalVec);					
				}
			}
			catch (Exception e)
			{
				logger.error(e.getStackTrace());
			}
		}
		catch (NoSuchMessageException e)
		{			
			logger.error(e.getStackTrace());
		}
		
	}
	/**
	 * send out sms
	 * */
	public void checkandsendSMS(String mobileNo , int days, Vector intervals)
	{		
		logger.info(" Checking for SMS at " + mobileNo + " -- " + days );
		
		if ( intervals.contains(days+"") == false){
			return;
		}		
		
		String messageString = this.getMessageSource().getMessage("DeviceSubsriptionReminder_Sms", new Object[]{(days-1)+""},null); 
		SmsMessage smsMessage = new SmsMessage(mobileNo, messageString);
		try
		{
			smsSender.send(smsMessage);
		}
		catch (FrameworkCheckedException e)
		{			
			logger.error(e.getMessage());
		}		
	}
	
	/**
	 * seperating reminder intervals from single string
	 * */
	public Vector getIntervals (String pCombined){
		
		Vector rVec = new Vector();
		StringTokenizer st = new StringTokenizer(pCombined, ",");
		for ( ;st.hasMoreTokens(); ){
			rVec.add(st.nextToken());
		}
		return rVec;
	}
	
	/**
	 * Retreive maximum
	 * **/
	public int getMaximumInterval (Vector pVec){
		
		int maxInterval = 0 ;
		for (int i = 0 ;i < pVec.size(); i ++){
			
			int temp =  Integer.parseInt((String)pVec.elementAt(i));
			if (temp > maxInterval )
				maxInterval = temp;
		}
		
		return maxInterval;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public String getReminderInterval() {
		return reminderInterval;
	}

	
	public void setDeviceSubscriptionReminderManager(
			DeviceSubscriptionReminderManager deviceSubscriptionReminderManager)
	{
		this.deviceSubscriptionReminderManager = deviceSubscriptionReminderManager;
	}
}
