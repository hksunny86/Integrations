package com.inov8.integration.remoting.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean;
import com.inov8.integration.inbound.sms.bean.ResponseMessagesConstant;
import com.inov8.integration.remoting.I8RemotingService;
import com.inov8.integration.util.StringUtil;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

@SuppressWarnings("all")
public class i8RemotingServiceImpl implements I8RemotingService {
	
	private static Logger logger = LoggerFactory.getLogger(i8RemotingServiceImpl.class.getSimpleName());
	
	private SwitchController switchController = null;
	
	private static i8RemotingServiceImpl instance;

	private i8RemotingServiceImpl() {

	}

	public static i8RemotingServiceImpl getInstance() {
		if (instance == null)
			instance = new i8RemotingServiceImpl();
		return instance;
	}
	
	public InboundSMSServiceBean checkBalanceXML(InboundSMSServiceBean inboundSMSServiceBean){
		
		logger.debug("> In checkBalance method ......");
		logger.debug("KEYWORD:BAL");
		CloseableHttpResponse closeableHttpResponse = null;
		
		PhoenixIntegrationMessageVO messageVO = new PhoenixIntegrationMessageVO();
		String MOBN  = inboundSMSServiceBean.getSenderMSISDN();
		String SMS = inboundSMSServiceBean.getSmsText();
		String userId = inboundSMSServiceBean.getUsername();
		String pin = inboundSMSServiceBean.getPassword();
		
	    try
	    {
			String loginXML = "<msg id=\"44\" reqTime=\"" + System.currentTimeMillis() + "\"><params><param name=\"UID\">" + userId + "</param><param name=\"PIN\">" + pin + "</param><param name=\"DTID\">12</param><param name=\"MOBN\">" + MOBN + "</param><param name=\"SMS\">" + "BAL" + "</param></params></msg>";
		    List<NameValuePair> params = new LinkedList();
			params.add(new BasicNameValuePair("message", loginXML));
		    
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String serviceUrl = this.getProperty("i8-scheme")+"://"+ this.getProperty("ip") + ":" + this.getProperty("port")+ "/i8Microbank/kannel.sms";
		    HttpPost httpPost = new HttpPost(serviceUrl);
		    httpPost.setEntity(new UrlEncodedFormEntity(params));
		    
			logger.debug("> HTTP Request to i8 for Check Balance Transaction ......");
			closeableHttpResponse = httpclient.execute(httpPost);
		
		    HttpEntity entity = closeableHttpResponse.getEntity();
		    logger.debug("HTTP Response: "+EntityUtils.toString(entity));
		    String response = parseXML(EntityUtils.toString(entity));
		    if(StringUtil.isEmpty(response)){
		    	logger.debug("> Response is empty recieved from i8 Remoting service ... ");
		    	inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
		    	return inboundSMSServiceBean;
		    }
		    
		    logger.debug("Parsed Response from XML ... "+response);
		    inboundSMSServiceBean.setResponseCode(response);
			      
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	try {				
	    		closeableHttpResponse.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
	    }
	    
	    return inboundSMSServiceBean;
	}
	
	
 public InboundSMSServiceBean checkMiniStatementXML(InboundSMSServiceBean inboundSMSServiceBean){
		
		logger.debug("> In checkMiniStatementXML method ......");
		logger.debug("KEYWORD:MS");
		CloseableHttpResponse closeableHttpResponse = null;
		
		PhoenixIntegrationMessageVO messageVO = new PhoenixIntegrationMessageVO();
		String MOBN  = inboundSMSServiceBean.getSenderMSISDN();
		String SMS = inboundSMSServiceBean.getSmsText();
		String userId = inboundSMSServiceBean.getUsername();
		String pin = inboundSMSServiceBean.getPassword();
		
	    try
	    {
			String loginXML = "<msg id=\"44\" reqTime=\"" + System.currentTimeMillis() + "\"><params><param name=\"UID\">" + userId + "</param><param name=\"PIN\">" + pin + "</param><param name=\"DTID\">12</param><param name=\"MOBN\">" + MOBN + "</param><param name=\"SMS\">" + "MS" + "</param></params></msg>";
		    List<NameValuePair> params = new LinkedList();
			params.add(new BasicNameValuePair("message", loginXML));
		    
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String serviceUrl = this.getProperty("i8-scheme")+"://"+ this.getProperty("ip") + ":" + this.getProperty("port")+ "/i8Microbank/kannel.sms";
		    HttpPost httpPost = new HttpPost(serviceUrl);
		    httpPost.setEntity(new UrlEncodedFormEntity(params));
		    
			logger.debug("> HTTP Request to i8 for Mini Statement Transaction ......");
			closeableHttpResponse = httpclient.execute(httpPost);
		
		    HttpEntity entity = closeableHttpResponse.getEntity();
		    logger.debug("HTTP Response: "+EntityUtils.toString(entity));
		    String response = parseXML(EntityUtils.toString(entity));
		    if(StringUtil.isEmpty(response)){
		    	logger.debug("> Response is empty recieved from i8 Remoting service ... ");
		    	inboundSMSServiceBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
		    	return inboundSMSServiceBean;
		    }
		    inboundSMSServiceBean.setResponseCode(response);
			      
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	try {				
	    		closeableHttpResponse.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
	    }
	    
	    return inboundSMSServiceBean;
	}

	@Override
	@Deprecated
	public InboundSMSServiceBean checkBalance(InboundSMSServiceBean inboundSmsBean) {
		this.getProxy();
		logger.debug("> In checkBalance method ......");

		
		PhoenixIntegrationMessageVO messageVO = new PhoenixIntegrationMessageVO();
		messageVO.setMobileNumber(inboundSmsBean.getSenderMSISDN());
		messageVO.setMessageType(inboundSmsBean.getSmsText());
		messageVO.setTransactionId(inboundSmsBean.getTransactionID());

		logger.debug("> Calling i8 remoting service to update transaction status ......");
		
		try {			
			messageVO = (PhoenixIntegrationMessageVO) switchController.checkBalance(messageVO);
			if(messageVO != null && !StringUtil.isEmpty(messageVO.getResponseCode())){
				inboundSmsBean.setResponseCode(ResponseMessagesConstant.SUCCESS_CODE);
			}else{
				logger.debug("> Response is empty recieved from i8 Remoting service ... ");
				inboundSmsBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
			}
		}catch (RemoteAccessException e) {
			inboundSmsBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
			logger.error("UNABLE_TO_CONNECT_WITH_I8_MICROBANK_REMOTING_SERVICE");
			logger.error("ERROR:" , e.getMessage());
			return inboundSmsBean;
		}catch (Exception e) {
			inboundSmsBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
			logger.error("ERROR:" , e.getMessage());
			return inboundSmsBean;
		}
		logger.debug("> Response code recieved from i8 Remoting service ......" +messageVO.getResponseCode());
		inboundSmsBean.setResponseCode(ResponseMessagesConstant.SUCCESS_CODE);
		
		return inboundSmsBean;
	}

	@Override
	@Deprecated
	public InboundSMSServiceBean miniStatement(InboundSMSServiceBean inboundSmsBean) {
		this.getProxy();
		logger.debug("> In checkBalance method ......");

		
		PhoenixIntegrationMessageVO messageVO = new PhoenixIntegrationMessageVO();
		messageVO.setMobileNumber(inboundSmsBean.getSenderMSISDN());
		messageVO.setMessageType(inboundSmsBean.getSmsText());
		messageVO.setTransactionId(inboundSmsBean.getTransactionID());

		logger.debug("> Calling i8 remoting service to update transaction status ......");
		
		try {			
			messageVO = (PhoenixIntegrationMessageVO) switchController.getMiniStatement(messageVO);
			if(messageVO != null && !StringUtil.isEmpty(messageVO.getResponseCode())){
				inboundSmsBean.setResponseCode(ResponseMessagesConstant.SUCCESS_CODE);
			}else{
				logger.debug("> Response is empty recieved from i8 Remoting service ... ");
				inboundSmsBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
			}
		}catch (RemoteAccessException e) {
			inboundSmsBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
			logger.error("UNABLE_TO_CONNECT_WITH_I8_MICROBANK_REMOTING_SERVICE");
			logger.error("ERROR:" , e.getMessage());
			return inboundSmsBean;
		}catch (Exception e) {
			inboundSmsBean.setResponseCode(ResponseMessagesConstant.SYSTERM_ERROR_CODE);
			logger.error("ERROR:" , e.getMessage());
			return inboundSmsBean;
		}
		logger.debug("> Response code recieved from i8 Remoting service ......" +messageVO.getResponseCode());
		inboundSmsBean.setResponseCode(ResponseMessagesConstant.SUCCESS_CODE);
		
		return inboundSmsBean;
	}
	
	public String parseXML(String xml){
		String response = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
			Document doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("msg");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				System.out.println(node.getNodeValue());

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					response = element.getTextContent();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}
	
	public void getProxy(){
		
		String serviceUrl = this.getProperty("i8-scheme")+"://"+ this.getProperty("ip") + ":" + this.getProperty("port")+ "/i8Microbank/ws/smsService";
		
		HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
		httpInvokerProxyFactoryBean.setServiceInterface(SwitchController.class);
		httpInvokerProxyFactoryBean.setServiceUrl(serviceUrl);
		httpInvokerProxyFactoryBean.afterPropertiesSet();
		
		switchController = (SwitchController) httpInvokerProxyFactoryBean.getObject();
	}
	
	public String getProperty(String key) {
		InputStream inputStream = null;
		String value = null;
		try {
			// Get the inputStream
			inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
			Properties properties = new Properties();
			// load the inputStream using the Properties
			properties.load(inputStream);

			value = properties.getProperty(key);
			if(value != null)
				value.trim();

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

}
