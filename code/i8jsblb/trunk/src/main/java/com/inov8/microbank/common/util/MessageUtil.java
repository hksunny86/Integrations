package com.inov8.microbank.common.util;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

public class MessageUtil
{
	private static Logger logger =  Logger.getLogger(MessageUtil.class);
	private static MessageSource messageSource;
	public static String GenericUpdateSuccessMessage =  "genericUpdateSuccessMessage";

	public static String getMessage(Long key) {
		if(key == null)
			return "";
		return getMessage(key.toString());
	}	
	
	public static String getMessage(String key)
	{
		 String returnString = "";
		  if(key == null || key.length() == 0)
		   return returnString;
		  
		  try{
		   returnString = messageSource.getMessage(key, null, null);
		  }
		  catch(NoSuchMessageException e) {
		   logger.error("ERROR : No message found against given key : " + key);
		  }
		return returnString;
	}
	
	public static String getMessage(String key, Object... args)
	{
		String returnString = "";
		if(key != null && key.length() > 0)
		{
			returnString = messageSource.getMessage(key, args, null);	
		}
		return returnString;
	}

	public static boolean getBooleanMessage(String key) {
		String returnString = getMessage(key); 
		return CommonUtils.convertToBoolean(returnString);
	}
		   
	public static int getIntMessage(String key) {
		String returnString = getMessage(key); 
		try {
			return Integer.parseInt(returnString);
		}catch(Exception e) {
			logger.error("ERROR: unable to parse int " + key);
		}
		return 1;
	}


	public static String getTermsAndConditions(){
		String url = getMessage("server.url");
		url=url+"/terms-and-conditions.jsp";
		return url;
	}



	public static long getLongMessage(String key) {
		String returnString = getMessage(key);
		try {
			return Long.parseLong(returnString);
		}catch(Exception e) {
			logger.error("ERROR: unable to parse long " + key);
		}
		return 1;
	}

	public static long getCWOTPValidityInMin() {
		return getLongMessage("cw.otp.validity.min");
	}

	public static int getOTPRetryLimit() {
		return getIntMessage("otp.retry.limit");
	}

	public static long getOTPValidityInMin() {
		return getLongMessage("otp.validity.min");
	}

	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
	
	public static String getBrandName() {
		return getMessage("brand.name");
	}
	
	public static String getPortalLink() {
		  return getMessage("server.url");
	}
	
	public static Integer getRemotingConnectionTimeout() {
		return getIntMessage("remoting.connection.timeout");
	}

	public static Integer getRemotingReadTimeout() {
		return getIntMessage("remoting.read.timeout");
	}
	

}