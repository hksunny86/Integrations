package com.inov8.integration.middleware.translitration.IdeaService;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.5
 * 2017-04-11T16:20:12.048+05:00
 * Generated source version: 3.1.5
 * 
 */
@WebServiceClient(name = "IdeaUToEService", 
                  wsdlLocation = "file:translation.wsdl",
                  targetNamespace = "http://softwarebydefault.com") 
public class IdeaUToEService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://softwarebydefault.com", "IdeaUToEService");
    public final static QName IdeaUToEServiceSoap = new QName("http://softwarebydefault.com", "IdeaUToEServiceSoap");
    public final static QName IdeaUToEServiceSoap12 = new QName("http://softwarebydefault.com", "IdeaUToEServiceSoap12");
    static {
        URL url = null;
        try {
            url = new URL("file:translation.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(IdeaUToEService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:translation.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public IdeaUToEService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public IdeaUToEService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IdeaUToEService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public IdeaUToEService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public IdeaUToEService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public IdeaUToEService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns IdeaUToEServiceSoap
     */
    @WebEndpoint(name = "IdeaUToEServiceSoap")
    public IdeaUToEServiceSoap getIdeaUToEServiceSoap() {
        return super.getPort(IdeaUToEServiceSoap, IdeaUToEServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IdeaUToEServiceSoap
     */
    @WebEndpoint(name = "IdeaUToEServiceSoap")
    public IdeaUToEServiceSoap getIdeaUToEServiceSoap(WebServiceFeature... features) {
        return super.getPort(IdeaUToEServiceSoap, IdeaUToEServiceSoap.class, features);
    }


    /**
     *
     * @return
     *     returns IdeaUToEServiceSoap
     */
    @WebEndpoint(name = "IdeaUToEServiceSoap12")
    public IdeaUToEServiceSoap getIdeaUToEServiceSoap12() {
        return super.getPort(IdeaUToEServiceSoap12, IdeaUToEServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IdeaUToEServiceSoap
     */
    @WebEndpoint(name = "IdeaUToEServiceSoap12")
    public IdeaUToEServiceSoap getIdeaUToEServiceSoap12(WebServiceFeature... features) {
        return super.getPort(IdeaUToEServiceSoap12, IdeaUToEServiceSoap.class, features);
    }

}
