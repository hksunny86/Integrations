package com.inov8.integration.middleware.novatti;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 3.1.3
 * 2015-10-16T11:28:16.182+05:00
 * Generated source version: 3.1.3
 * 
 */
@WebServiceClient(name = "AgentService", 
                  wsdlLocation = "file:agent.wsdl",
                  targetNamespace = "http://soap.api.novatti.com/service") 
public class AgentService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://soap.api.novatti.com/service", "AgentService");
    public final static QName AgentServiceIFPort = new QName("http://soap.api.novatti.com/service", "AgentServiceIFPort");
    static {
        URL url = null;
        try {
            url = new URL("file:agent.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(AgentService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:agent.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public AgentService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AgentService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AgentService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public AgentService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public AgentService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public AgentService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns AgentServiceIF
     */
    @WebEndpoint(name = "AgentServiceIFPort")
    public AgentServiceIF getAgentServiceIFPort() {
        return super.getPort(AgentServiceIFPort, AgentServiceIF.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AgentServiceIF
     */
    @WebEndpoint(name = "AgentServiceIFPort")
    public AgentServiceIF getAgentServiceIFPort(WebServiceFeature... features) {
        return super.getPort(AgentServiceIFPort, AgentServiceIF.class, features);
    }

}
