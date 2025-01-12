package com.inov8.integration.middleware.bop.iflexService;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.5
 * 2016-12-07T10:12:52.079+05:00
 * Generated source version: 3.1.5
 * 
 */
@WebServiceClient(name = "FlexService", 
                  wsdlLocation = "file:BOP.wsdl",
                  targetNamespace = "http://tempuri.org/") 
public class FlexService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tempuri.org/", "FlexService");
    public final static QName ChannelServiceEndPoint = new QName("http://tempuri.org/", "ChannelServiceEndPoint");
    static {
        URL url = null;
        try {
            url = new URL("file:BOP.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(FlexService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:BOP.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public FlexService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FlexService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FlexService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public FlexService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public FlexService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public FlexService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns IFlexService
     */
    @WebEndpoint(name = "ChannelServiceEndPoint")
    public IFlexService getChannelServiceEndPoint() {
        return super.getPort(ChannelServiceEndPoint, IFlexService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IFlexService
     */
    @WebEndpoint(name = "ChannelServiceEndPoint")
    public IFlexService getChannelServiceEndPoint(WebServiceFeature... features) {
        return super.getPort(ChannelServiceEndPoint, IFlexService.class, features);
    }

}
