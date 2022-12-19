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
 * 2015-10-16T11:28:49.847+05:00
 * Generated source version: 3.1.3
 * 
 */
@WebServiceClient(name = "PurchaseService", 
                  wsdlLocation = "file:purchase.wsdl",
                  targetNamespace = "http://soap.api.novatti.com/service") 
public class PurchaseService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://soap.api.novatti.com/service", "PurchaseService");
    public final static QName PurchaseServiceIFPort = new QName("http://soap.api.novatti.com/service", "PurchaseServiceIFPort");
    static {
        URL url = null;
        try {
            url = new URL("file:purchase.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(PurchaseService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:purchase.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public PurchaseService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PurchaseService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PurchaseService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public PurchaseService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public PurchaseService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public PurchaseService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns PurchaseServiceIF
     */
    @WebEndpoint(name = "PurchaseServiceIFPort")
    public PurchaseServiceIF getPurchaseServiceIFPort() {
        return super.getPort(PurchaseServiceIFPort, PurchaseServiceIF.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PurchaseServiceIF
     */
    @WebEndpoint(name = "PurchaseServiceIFPort")
    public PurchaseServiceIF getPurchaseServiceIFPort(WebServiceFeature... features) {
        return super.getPort(PurchaseServiceIFPort, PurchaseServiceIF.class, features);
    }

}
