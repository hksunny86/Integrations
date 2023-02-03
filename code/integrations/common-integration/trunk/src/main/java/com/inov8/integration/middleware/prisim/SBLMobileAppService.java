package com.inov8.integration.middleware.prisim;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2016-04-22T15:52:22.520+05:00
 * Generated source version: 3.1.6
 * 
 */
@WebServiceClient(name = "SBLMobileAppService", 
                  wsdlLocation = "file:SBLMobileAppService.wsdl",
                  targetNamespace = "http://tempuri.org/") 
public class SBLMobileAppService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tempuri.org/", "SBLMobileAppService");
    public final static QName BasicHttpBindingISBLMobileAppService = new QName("http://tempuri.org/", "BasicHttpBinding_ISBLMobileAppService");
    public final static QName BasicHttpsBindingISBLMobileAppService = new QName("http://tempuri.org/", "BasicHttpsBinding_ISBLMobileAppService");
    static {
        URL url = null;
        try {
            url = new URL("file:SBLMobileAppService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(SBLMobileAppService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:SBLMobileAppService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public SBLMobileAppService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SBLMobileAppService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SBLMobileAppService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public SBLMobileAppService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public SBLMobileAppService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public SBLMobileAppService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns ISBLMobileAppService
     */
    @WebEndpoint(name = "BasicHttpBinding_ISBLMobileAppService")
    public ISBLMobileAppService getBasicHttpBindingISBLMobileAppService() {
        return super.getPort(BasicHttpBindingISBLMobileAppService, ISBLMobileAppService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ISBLMobileAppService
     */
    @WebEndpoint(name = "BasicHttpBinding_ISBLMobileAppService")
    public ISBLMobileAppService getBasicHttpBindingISBLMobileAppService(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingISBLMobileAppService, ISBLMobileAppService.class, features);
    }


    /**
     *
     * @return
     *     returns ISBLMobileAppService
     */
    @WebEndpoint(name = "BasicHttpsBinding_ISBLMobileAppService")
    public ISBLMobileAppService getBasicHttpsBindingISBLMobileAppService() {
        return super.getPort(BasicHttpsBindingISBLMobileAppService, ISBLMobileAppService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ISBLMobileAppService
     */
    @WebEndpoint(name = "BasicHttpsBinding_ISBLMobileAppService")
    public ISBLMobileAppService getBasicHttpsBindingISBLMobileAppService(WebServiceFeature... features) {
        return super.getPort(BasicHttpsBindingISBLMobileAppService, ISBLMobileAppService.class, features);
    }

}