package com.inov8.integration.m3.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inov8.integration.m3.client.ServiceSoap;
import com.inov8.integration.util.StringUtil;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

@Service
public class M3SmsService {
	
	
	private static Logger logger = LoggerFactory.getLogger(M3SmsService.class.getSimpleName());
	
	private String username = "JsBank";
	private String password = "j@$123B#";
	
	@Resource(name="serviceSoap")
	private ServiceSoap serviceSoap;
	
	public PhoenixIntegrationMessageVO sendSms(PhoenixIntegrationMessageVO messageVO){	
		try {
			
			logger.debug("sendSms method > Request reciever");
			
			messageVO.setUsername(username);
			messageVO.setPassword(password);
			
			boolean result = this.validateRequest(messageVO);
			if(result){
				if (messageVO.getMobileNumber().startsWith("00")) {
					logger.debug("Truncating Mobile Number...."+messageVO.getMobileNumber());
					String subString = messageVO.getMobileNumber().substring(2,messageVO.getMobileNumber().length());			
					messageVO.setMobileNumber(subString);
				}
				
				logger.debug("inboundSMSServiceBean parameters >");
				logger.debug(" - Mobile Number:"+messageVO.getMobileNumber());
				logger.debug(" - Transaction ID:"+messageVO.getTransactionId());
				logger.debug(" - Message:"+messageVO.getMessageType());
						
				try {
					logger.debug("Sending request to M3 WebService......");
					String response = serviceSoap.sendSMS(messageVO.getUsername(), messageVO.getPassword(), messageVO.getMobileNumber(), 
							messageVO.getTransactionId(), messageVO.getMessageType(), "JSBank");
					if(!StringUtil.isEmpty(response)){						
						messageVO.setResponseCode(response);
					}else{
						messageVO.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
					}
				} catch (Exception e) {
					e.printStackTrace();
					messageVO.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
					logger.error("Error Occured: "+e.getMessage());
				}
			}
			logger.debug("Returning response .. Response Code is "+messageVO.getResponseCode() != null ? messageVO.getResponseCode() : ResponseMessagesConstant.SYSTERM_ERROR_CODE);
			return messageVO;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageVO;
	}
	
	private Boolean validateRequest(PhoenixIntegrationMessageVO messageVO) {
		
		logger.debug("In Validating request....");
		
		if(StringUtil.isEmpty(messageVO.getUsername()) && StringUtil.isEmpty(messageVO.getPassword()) && StringUtil.isEmpty(messageVO.getMobileNumber())
				&& StringUtil.isEmpty(messageVO.getTransactionId()) && StringUtil.isEmpty(messageVO.getMessageType())){
			messageVO.setResponseCode(ResponseMessagesConstant.REQUEST_IS_NULL_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUEST_IS_NULL_MSG);
			return false;
		}else if(StringUtil.isEmpty(messageVO.getMobileNumber())){
			messageVO.setResponseCode(ResponseMessagesConstant.REQUIRED_MOBILE_NO_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_MOBILE_NO_IS_MISSING_MSG);
			return false;
		}/*
		else if(StringUtil.isEmpty(messageVO.getUsername())){
			messageVO.setResponseCode(ResponseMessagesConstant.REQUIRED_USERNAME_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_USERNAME_IS_MISSING_MSG);
			return false;
		}
		else if(StringUtil.isEmpty(messageVO.getPassword())){
			messageVO.setResponseCode(ResponseMessagesConstant.REQUIRED_PASSWORD_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_PASSWORD_IS_MISSING_MSG);
			return false;
		}*/
		else if(StringUtil.isEmpty(messageVO.getTransactionId())){
			messageVO.setResponseCode(ResponseMessagesConstant.REQUIRED_TRX_ID_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_TRX_ID_IS_MISSING_MSG);
			return false;
		}else if(StringUtil.isEmpty(messageVO.getMessageType())){
			messageVO.setResponseCode(ResponseMessagesConstant.REQUIRED_SMS_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_SMS_IS_MISSING_MSG);
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		PhoenixIntegrationMessageVO messageVO = new PhoenixIntegrationMessageVO();
		messageVO.setMobileNumber("00923214084099");
		
		
		String mobile = null;
		if (messageVO.getMobileNumber().startsWith("00")) {
			String subString = messageVO.getMobileNumber().substring(2,messageVO.getMobileNumber().length());
			
				logger.debug("Subscriber Number	after truncate : --------------- {} ", subString);
			
		}else{
			mobile = messageVO.getMobileNumber();
		}
		
		System.out.println(mobile);
		
		
	}

}
