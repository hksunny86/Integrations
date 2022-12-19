package com.inov8.microbank.server.service.dailyjob;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import com.inov8.microbank.common.model.FailedSmsModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.server.dao.messagemodule.FailedSmsDAO;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;

public class FailedSmsSender {

	private static final Logger LOGGER = Logger.getLogger( FailedSmsSender.class );
	private FailedSmsDAO failedSmsDAO;
    private SmsSenderService smsSenderService;
    
	public FailedSmsSender(){
	}
	
	public void init(){
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("FailedSmsSender Scheduler Process");
		String chunkSize = MessageUtil.getMessage("sms.retry.chunk.size");
		String currentTime = getCurrentTimeStamp();

        LOGGER.info("**********************************************************");
        LOGGER.info("*********** Executing FailedSmsSender chunkSize="+chunkSize+"*************");
        LOGGER.info("**********************************************************");
        try{
        	List<FailedSmsModel> smsList = failedSmsDAO.getFailedSmsList(Long.parseLong(chunkSize));
    		if(smsList != null && smsList.size() > 0){
    			LOGGER.info("!!!! Ooops !!!! Found Failed SMS (" + smsList.size() + ")");
    			boolean serviceUp = false;
    			boolean smsSent = false;
    			int failedSmsCount = 0;
    			int iteration = 1;
    			
				for(FailedSmsModel smsModel : smsList){
    				
					smsSent = this.sendSMS(smsModel);
    				
					if(smsSent){
    					serviceUp = true;
    				}
					
					if(iteration == 5 && !serviceUp){
    					LOGGER.error("First 5 try to send failed-SMS is Unsuccessful, so going to stop the process.");
    					break;
    				}
    				
    				if(!smsSent){
    					failedSmsCount++;	
    				}
    				
    				iteration++;
    			}
    			
    			if(serviceUp){
        			LOGGER.info("Completed delivery of (" + smsList.size() + ") failed SMS. No of Failed SMS=" + failedSmsCount);
    			}
    		
    		}else{
    			LOGGER.info("!!!! Hurrayyy !!!! Nothing wrong with SMS (Size of failed SMS = 0 )");
    		}
    	
        }catch(Exception e){
        		LOGGER.error("Exception @ init : " + e.getMessage());
        		LOGGER.error(e);
    	}

        LOGGER.info("*********** Finished Executing FailedSmsSender ***********");
        
        stopWatch.stop();
        LOGGER.info(stopWatch.prettyPrint());
    }

	private boolean sendSMS(FailedSmsModel failedSmsModel){
		boolean result = false;
		try {
			smsSenderService.makeFailedSmsRetry(failedSmsModel);
			result = true;
		} catch (Exception e) {
			LOGGER.error("Failed to send SMS for failed_sms_id:"+failedSmsModel.getFailedSmsId()+ " , mob:"+failedSmsModel.getMobileNumber()+ ", created_on:"+failedSmsModel.getCreatedOn());
			LOGGER.error(e);
		}
		return result;
	}
	
	private String getCurrentTimeStamp() {
		Calendar c1 = Calendar.getInstance();
		return c1.get(Calendar.HOUR_OF_DAY) + ":" +  c1.get(Calendar.MINUTE);
	}

	public void setFailedSmsDAO(FailedSmsDAO failedSmsDAO) {
		this.failedSmsDAO = failedSmsDAO;
	}
	
	public void setSmsSenderService(SmsSenderService smsSenderService) {
		this.smsSenderService = smsSenderService;
	}
	
}
