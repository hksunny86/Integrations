package com.inov8.microbank.server.service.dailyjob;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

/** 
 * Created By    : Abu Turab <br>
 * Creation Date : May 26, 2015 5:13 PM<p>
 * Purpose       : <p>Purpose of this scheduler is to generate the HSM based PIN through IVR call with in given time intervals.</p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class OneTimeHsmPinGenerator {

	private static final Logger LOGGER = Logger.getLogger( OneTimeHsmPinGenerator.class );
	private AppUserManager	appUserManager;
	private static Long	CUSTOMER = 2L;
	private static Long	RETAILER = 3L;
	private static Long	HANDLER = 12L;
	
	//no argument default constructor
	public OneTimeHsmPinGenerator(){
	}
	
	public void init()
	    {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start("OneTimeHsmPinGenerator Scheduler Process");
			String strDate1 , strDate2, strLimit, time1, time2;
			int recordLimit = 25;
			
			String currentTime = getCurrentTimeStamp();
			time1	 = MessageUtil.getMessage("ONETIME_HSM_HOURS");
			String [] hours = time1.split("-");
			String [] validTimestamps = new String[hours.length * 60];
			
			int cnt=0;
			for(int h=0; h<hours.length;h++){
				for(int m=0; m<=59; m++){
					validTimestamps[cnt]=hours[h]+":"+m;
					cnt++;
				}
			}
			
			strDate1 = MessageUtil.getMessage("ONETIME_HSM_DATE1");
			strDate2 = MessageUtil.getMessage("ONETIME_HSM_DATE2");
			strLimit = MessageUtil.getMessage("ONETIME_HSM_REC_LIMIT");
			
			if(strLimit != null && !"".equals(strLimit)){
				recordLimit = Integer.parseInt(strLimit);
			}
			
			List<AppUserModel> appUsers = new ArrayList<AppUserModel>();
	        LOGGER.info("*********** Executing OneTimeHsmPinGenerator at " + currentTime + "*************");
	        LOGGER.info("********************** BWETEEN DATES *********************");
	        LOGGER.info("*********** " + strDate1 + " AND " + strDate2 + " ********");
	        LOGGER.info("******************* For Time Intervals ********************");
	        LOGGER.info("*********** " + time1 + " ********");
	        LOGGER.info("**********************************************************");
	        try{
	        	if( isValidTimeStamp(currentTime, validTimestamps) ){
		        	appUsers = appUserManager.getAppUserOneTimeByTypeForHsmCall(CUSTOMER, strDate1, strDate2, recordLimit);
		        	if(CollectionUtils.isNotEmpty(appUsers)){
		        		appUsers = appUserManager.oneTimeHsmPinGenerator(appUsers, CUSTOMER);
		        	}else{
	        				LOGGER.info("*********** IVR CALLS HAS BEEN ROUTED TO ALL CUSTOMERS, TIME TO STOP SCHEDULER ***********");
		        	}
	        	}else{
	        		LOGGER.info("***********************************************");
	        		LOGGER.info("*** " + currentTime + " is not in range of " + time1);
	        		LOGGER.info("***********************************************");
	        	}
        	}catch(Exception e){
	        		LOGGER.debug(e.getMessage());
        	}
	     

	        LOGGER.info("*********** Finished Executing OneTimeHsmPinGenerator ***********");
	        
	        stopWatch.stop();
	        LOGGER.info(stopWatch.prettyPrint());
	    }
	
	private String getCurrentTimeStamp() {
	
		Calendar c1 = Calendar.getInstance();
		return c1.get(Calendar.HOUR_OF_DAY) + ":" +  c1.get(Calendar.MINUTE);
	}

	private boolean isValidTimeStamp(String currentTimeStamp, String[] validTimeStamps){
		boolean isValid=false;
		
		for(int i=0; i<validTimeStamps.length; i++){
			if(null != validTimeStamps[i] && !"".equals(validTimeStamps[i]) && currentTimeStamp.equals(validTimeStamps[i])){
				isValid=true;
				break;
			}
		}
		
		return isValid;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	
}
