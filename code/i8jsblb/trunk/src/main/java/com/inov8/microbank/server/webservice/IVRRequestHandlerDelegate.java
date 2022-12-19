package com.inov8.microbank.server.webservice;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.inov8.microbank.ivr.IvrRequestHandler;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.webservice.bean.IvrResponseDTO;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;

@WebService(targetNamespace = "http://service.microbank.inov8.com/", serviceName = "IVRRequestHandlerService", portName = "IVRRequestHandlerPort")
@javax.jws.soap.SOAPBinding(parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
public class IVRRequestHandlerDelegate {
	

	@Resource
	private WebServiceContext wsContext;
	protected final Log logger = LogFactory.getLog(getClass());
	private IvrRequestHandler ivrRequestHandler;
	
	@WebMethod
	public IvrRequestDTO handleIVRRequest(@WebParam(name="ivrResponseDTO")IvrResponseDTO ivrResponseDTO){
		//load IvrRequestHandler from Context
		ivrRequestHandler = getIvrRequestHandler();
		
		IvrRequestDTO requestDTO = ivrRequestHandler.handleIvrResponse(ivrResponseDTO);
		
		/*IVROutputDTO ivrOutputDTO = new IVROutputDTO();
		ivrOutputDTO.setAgentMobileNo("03214487845");
		ivrInputDTO.setAmount(333.4);
		ivrInputDTO.setCharges(32.4);
		ivrInputDTO.setTransactionId("3243535436455");*/
		
		
		return requestDTO;
	}
	
	private HttpSession getSession() {
		MessageContext mc = wsContext.getMessageContext();
		HttpSession session = ((javax.servlet.http.HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getSession();
		return session;
	}

	// Added by Maqsood Shahzad

	private HttpServletRequest getRequest() {
		MessageContext mc = wsContext.getMessageContext();
		HttpServletRequest request = (javax.servlet.http.HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
		return request;
	}

	
	private IvrRequestHandler getIvrRequestHandler() {
		WebApplicationContext webApplicationContext = getWebApplicationContext();
		IvrRequestHandler ivrRequestHandler = (IvrRequestHandler) webApplicationContext.getBean("ivrRequestHandler");
		return ivrRequestHandler;
	}

	private WebApplicationContext getWebApplicationContext() {
		ServletContext servletContext = (ServletContext) wsContext.getMessageContext().get("javax.xml.ws.servlet.context");
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		return webApplicationContext;
	}
	
	private ActionLogManager getActionLogManager()
	{
		WebApplicationContext webApplicationContext = getWebApplicationContext();
		ActionLogManager actionLogManager = (ActionLogManager) webApplicationContext.getBean("actionLogManager");
		return actionLogManager;
	}

}
