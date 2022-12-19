package com.inov8.integration.inbound.sms;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.integration.inbound.sms.bean.InboundSMSServiceBean;

@javax.jws.WebService(targetNamespace = "http://sms.inbound.integration.inov8.com/", serviceName = "InboundSmsServiceService", portName = "InboundSmsServicePort")
public class InboundSmsServiceDelegate {
	private static Logger logger = LoggerFactory.getLogger(InboundSmsServiceDelegate.class.getSimpleName());
	com.inov8.integration.inbound.sms.InboundSmsService inboundSmsService = new com.inov8.integration.inbound.sms.InboundSmsService();

	@Resource
	WebServiceContext wsContext;
	
	public InboundSMSServiceBean receiveSMS(InboundSMSServiceBean inboundSMSServiceBean) {

		MessageContext mc = wsContext.getMessageContext();
	    HttpServletRequest req = (HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST); 

		logger.debug("An incoming msg recieved from ..........."+req.getRemoteAddr());
		logger.debug("Calling inbound sms service ...........");
		return inboundSmsService.receiveSMS(inboundSMSServiceBean);
	}

}