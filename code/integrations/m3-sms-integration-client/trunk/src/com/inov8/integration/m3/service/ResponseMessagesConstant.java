package com.inov8.integration.m3.service;

@SuppressWarnings("all")
public class ResponseMessagesConstant {
		
	public static final String SUCCESS_CODE = "00";
	public static final String SUCCESS_CODE_MSG = "Success";
	
	public static final String REQUIRED_MOBILE_NO_IS_MISSING_CODE = "01";
	public static final String REQUIRED_MOBILE_NO_IS_MISSING_MSG = "Required data (sender mobile number) is missing";
	
	public static final String REQUIRED_USERNAME_IS_MISSING_CODE = "02";
	public static final String REQUIRED_USERNAME_IS_MISSING_MSG = "Required data (username) is missing";
	
	public static final String REQUIRED_PASSWORD_IS_MISSING_CODE = "03";
	public static final String REQUIRED_PASSWORD_IS_MISSING_MSG = "Required data (password) is missing";
	
	public static final String REQUIRED_TRX_ID_IS_MISSING_CODE = "04";
	public static final String REQUIRED_TRX_ID_IS_MISSING_MSG = "Required data (transaction id) is missing";
	
	public static final String REQUIRED_SMS_IS_MISSING_CODE = "05";
	public static final String REQUIRED_SMS_IS_MISSING_MSG = "Required data (SMS Text) is missing";
	
	public static final String REQUEST_IS_NULL_CODE = "06";
	public static final String REQUEST_IS_NULL_MSG = "InboundSMSServiceBean (request bean) is null";
	
	public static final String INVALID_SENDER_MOBILE_CODE = "07";
	public static final String INVALID_SENDER_MOBILE_MSG = "Invalid sender mobile number";
	
	public static final String INVALID_USERNAME_PASSWORD_CODE = "08";
	public static final String INVALID_USERNAME_PASSWORD_MSG = "Invalid username or password";
	
	public static final String DUPLICATE_TRX_CODE = "10";
	public static final String DUPLICATE_TRX_MSG = "Duplicate transaction Id";
	
	public static final String INVALID_TRX_CODE = "12";
	public static final String INVALID_TRX_MSG = "Invalid transaction Id";
	
	public static final String SYSTERM_ERROR_CODE = "99";
	public static final String SYSTEM_ERROR_MSG = "System error/Unknown error";
}
