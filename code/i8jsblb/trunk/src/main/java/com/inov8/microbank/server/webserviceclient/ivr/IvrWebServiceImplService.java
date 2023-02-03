package com.inov8.microbank.server.webserviceclient.ivr;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * <p>
 * An example of how this class may be used:
 * 
 * <pre>
 * IvrWebServiceImplService service = new IvrWebServiceImplService();
 * IvrWebService portType = service.getIvrWebServicePort();
 * portType.handleRequest(...);
 * </pre>
 * 
 * </p>
 * 
 */
//@WebServiceClient(name = "ivrwebservice", targetNamespace = "http://server.ws.microbank.inov8.com/", wsdlLocation = "http://ivrservice:8585/ivrwebservice?wsdl")
//@org.springframework.stereotype.Service
public class IvrWebServiceImplService extends Service {

	private final static URL IVRWEBSERVICEIMPLSERVICE_WSDL_LOCATION;
	private final static Logger logger = Logger
			.getLogger(com.inov8.microbank.server.webserviceclient.ivr.IvrWebServiceImplService.class
					.getName());

	static {
		URL url = null;
		/*String dynamicUrl = MessageUtil.getMessage("ivrClientURL");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ " + dynamicUrl);*/
		try {
			URL baseUrl;
			baseUrl = com.inov8.microbank.server.webserviceclient.ivr.IvrWebServiceImplService.class
					.getResource(".");
			url = new URL(baseUrl, "http://ivrservice:8585/ivrwebservice?wsdl");
		} catch (MalformedURLException e) {
			logger.warning("Failed to create URL for the wsdl Location: 'http://ivrservice:8585/ivrwebservice?wsdl', retrying as a local file");
			logger.warning(e.getMessage());
		}
		IVRWEBSERVICEIMPLSERVICE_WSDL_LOCATION = url;
	}

	public IvrWebServiceImplService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public IvrWebServiceImplService() {
		super(IVRWEBSERVICEIMPLSERVICE_WSDL_LOCATION, new QName(
				"http://server.ws.microbank.inov8.com/",
				"ivrwebservice"));
	}

	/**
	 * 
	 * @return returns IvrWebService
	 */
	@WebEndpoint(name = "ivrwebservicePort")
	public IvrWebService getIvrWebServicePort() {
		return super.getPort(new QName("http://server.ws.microbank.inov8.com/",
				"ivrwebservicePort"), IvrWebService.class);
	}

}