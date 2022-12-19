package com.inov8.integration.inbound.sms;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean;
import com.inov8.integration.inbound.sms.bean.ResponseMessagesConstant;
import com.inov8.integration.remoting.impl.i8RemotingServiceImpl;
import com.inov8.integration.util.StringUtil;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

@WebService
public class InboundSmsService {
	
	private static Logger logger = LoggerFactory.getLogger(InboundSmsService.class.getSimpleName());
	

	@WebMethod
	public InboundSMSServiceBean receiveSMS(InboundSMSServiceBean inboundSMSServiceBean){
		
		logger.debug("receiveSMS method > inboundSmsRequest Received");
		
		logger.debug("inboundSMSServiceBean parameters >");
		logger.debug(" - Username:"+inboundSMSServiceBean.getUsername() == null ||inboundSMSServiceBean.getUsername().equalsIgnoreCase("")  ? "NULL" : inboundSMSServiceBean.getUsername());
		logger.debug(" - Sender Mobile Number:"+inboundSMSServiceBean.getSenderMSISDN() == null ||inboundSMSServiceBean.getSenderMSISDN().equalsIgnoreCase("")  ? "NULL" : inboundSMSServiceBean.getSenderMSISDN());
		logger.debug(" - Transaction ID:"+inboundSMSServiceBean.getTransactionID() == null ||inboundSMSServiceBean.getTransactionID().equalsIgnoreCase("")  ? "NULL" : inboundSMSServiceBean.getTransactionID());
		logger.debug(" - SMS Text:"+inboundSMSServiceBean.getSmsText() == null ||inboundSMSServiceBean.getSmsText().equalsIgnoreCase("")  ? "NULL" : inboundSMSServiceBean.getSmsText());
		
		
//		pushSMS(inboundSMSServiceBean.getSmsText(), inboundSMSServiceBean.getSenderMSISDN(), inboundSMSServiceBean.getTransactionID());
		boolean result = this.validateRequest(inboundSMSServiceBean);
	
		if(result){
//			inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.SUCCESS_CODE);
		
			try {
				if(inboundSMSServiceBean.getSenderMSISDN().startsWith("92")){					
					String mobile = inboundSMSServiceBean.getSenderMSISDN().substring(2, inboundSMSServiceBean.getSenderMSISDN().length());
					StringBuilder mobileStr = new StringBuilder("0");
					mobileStr.append(mobile);
					inboundSMSServiceBean.setSenderMSISDN(mobileStr.toString());
				}
				
				if(inboundSMSServiceBean.getSmsText().equalsIgnoreCase("BAL")){
					logger.debug("Executing checkBalance ......... ");
//					pushSMS(inboundSMSServiceBean.getSmsText(), inboundSMSServiceBean.getSenderMSISDN(), inboundSMSServiceBean.getTransactionID());
					inboundSMSServiceBean = i8RemotingServiceImpl.getInstance().checkBalanceXML(inboundSMSServiceBean);
//					inboundSMSServiceBean.setResponseCode(inboundSMSServiceBean.getTransactionID() == null || inboundSMSServiceBean.getTransactionID().equalsIgnoreCase("") ? null : inboundSMSServiceBean.getTransactionID());
				}else if(inboundSMSServiceBean.getSmsText().equalsIgnoreCase("MINI")){
//					pushSMS(inboundSMSServiceBean.getSmsText(), inboundSMSServiceBean.getSenderMSISDN(), inboundSMSServiceBean.getTransactionID());
					logger.debug("Executing mini statement ......... ");
					inboundSMSServiceBean = i8RemotingServiceImpl.getInstance().checkMiniStatementXML(inboundSMSServiceBean);
//					inboundSMSServiceBean.setResponseCode(inboundSMSServiceBean.getTransactionID() == null || inboundSMSServiceBean.getTransactionID().equalsIgnoreCase("") ? null : inboundSMSServiceBean.getTransactionID());
				}else{
					logger.debug("Invalid SMS Command ......... ");
					inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
				logger.error("Error Occured: "+e.getMessage());
			}
		}
	
		logger.debug("Returning response .. Response Code is "+inboundSMSServiceBean.getResponseCode() != null ? inboundSMSServiceBean.getResponseCode() : ResponseMessagesConstant.SYSTERM_ERROR_CODE);
		return inboundSMSServiceBean;
	}
	
	private void pushSMS(String msg, String number, String msgId){
		try{			
			String url = "http://10.0.1.40:9090/M3SmsIntegrationClient/ws/m3Sms";
//		String url = "http://127.0.0.1:8080/M3SmsIntegrationClient/ws/m3Sms";
			HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
			httpInvokerProxyFactoryBean.setServiceInterface(SwitchController.class);
			httpInvokerProxyFactoryBean.setServiceUrl(url);
			httpInvokerProxyFactoryBean.afterPropertiesSet();
			
			SwitchController switchController = (SwitchController) httpInvokerProxyFactoryBean.getObject();
			PhoenixIntegrationMessageVO messageVO = new PhoenixIntegrationMessageVO();
			try {			
				messageVO.setMessageType("Msg Recieved:"+msg);
				messageVO.setMobileNumber("923214084099");
				messageVO.setTransactionId(msgId);
				messageVO = (PhoenixIntegrationMessageVO)switchController.transaction(messageVO);
				System.out.println(messageVO.getResponseCode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e){
			
		}
	}
	
	private Boolean validateRequest(InboundSMSServiceBean inboundSMSServiceBean) {
		
		logger.debug("In Validating request....");
		
		if(StringUtil.isEmpty(inboundSMSServiceBean.getUsername()) && StringUtil.isEmpty(inboundSMSServiceBean.getPassword()) && StringUtil.isEmpty(inboundSMSServiceBean.getSenderMSISDN())
				&& StringUtil.isEmpty(inboundSMSServiceBean.getTransactionID()) && StringUtil.isEmpty(inboundSMSServiceBean.getSmsText())){
			inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.REQUEST_IS_NULL_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUEST_IS_NULL_MSG);
			return false;
		}else if(StringUtil.isEmpty(inboundSMSServiceBean.getSenderMSISDN())){
			inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.REQUIRED_MOBILE_NO_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_MOBILE_NO_IS_MISSING_MSG);
			return false;
		}else if(StringUtil.isEmpty(inboundSMSServiceBean.getUsername())){
			inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.REQUIRED_USERNAME_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_USERNAME_IS_MISSING_MSG);
			return false;
		}else if(StringUtil.isEmpty(inboundSMSServiceBean.getPassword())){
			inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.REQUIRED_PASSWORD_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_PASSWORD_IS_MISSING_MSG);
			return false;
		}else if(StringUtil.isEmpty(inboundSMSServiceBean.getTransactionID())){
			inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.REQUIRED_TRX_ID_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_TRX_ID_IS_MISSING_MSG);
			return false;
		}else if(StringUtil.isEmpty(inboundSMSServiceBean.getSmsText())){
			inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.REQUIRED_SMS_IS_MISSING_CODE);
			logger.debug("Validation Failed: " +ResponseMessagesConstant.REQUIRED_SMS_IS_MISSING_MSG);
			return false;
		}
		
		return true;
	}


	 /**
	 * 
	 */
	private static void disableSslVerification() {
	
		try {
	         // Create a trust manager that does not validate certificate chains
	         TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	        	 
	             public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                 return null;
	             }
	             
	             public void checkClientTrusted(X509Certificate[] certs, String authType) {
	             }
	             
	             public void checkServerTrusted(X509Certificate[] certs, String authType) {
	             }
	         }
	         };

	         // Install the all-trusting trust manager
	         SSLContext sc = SSLContext.getInstance("SSL");
	         sc.init(null, trustAllCerts, new java.security.SecureRandom());
	         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	         // Create all-trusting host name verifier
	         HostnameVerifier allHostsValid = new HostnameVerifier() {
	             public boolean verify(String hostname, SSLSession session) {
	                 return true;
	             }
	         };

	         // Install the all-trusting host verifier
	         HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	         
	     } catch (NoSuchAlgorithmException e) {
	         e.printStackTrace();
	     } catch (KeyManagementException e) {
	         e.printStackTrace();
	     }
	 }
	
	 static {
	     disableSslVerification();
	 }		
	
}
