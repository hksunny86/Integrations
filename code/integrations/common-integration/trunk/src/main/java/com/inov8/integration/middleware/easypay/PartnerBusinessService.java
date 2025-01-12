package com.inov8.integration.middleware.easypay;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.3
 * 2016-01-29T12:13:39.369+05:00
 * Generated source version: 3.1.3
 * 
 */
@WebServiceClient(name = "PartnerBusinessService", 
                  wsdlLocation = "https://easypay.easypaisa.com.pk/easypay-service/PartnerBusinessService/META-INF/wsdl/partner/transaction/PartnerBusinessService.wsdl",
                  targetNamespace = "http://transaction.partner.pg.systems.com/") 
public class PartnerBusinessService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://transaction.partner.pg.systems.com/", "PartnerBusinessService");
    public final static QName PartnerBusinessServicePort = new QName("http://transaction.partner.pg.systems.com/", "PartnerBusinessServicePort");
    static {
        URL url = null;
        try {
            url = new URL("https://easypay.easypaisa.com.pk/easypay-service/PartnerBusinessService/META-INF/wsdl/partner/transaction/PartnerBusinessService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(PartnerBusinessService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "https://easypay.easypaisa.com.pk/easypay-service/PartnerBusinessService/META-INF/wsdl/partner/transaction/PartnerBusinessService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public PartnerBusinessService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PartnerBusinessService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PartnerBusinessService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public PartnerBusinessService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public PartnerBusinessService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public PartnerBusinessService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns IPartnerBusinessService
     */
    @WebEndpoint(name = "PartnerBusinessServicePort")
    public IPartnerBusinessService getPartnerBusinessServicePort() {
        return super.getPort(PartnerBusinessServicePort, IPartnerBusinessService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IPartnerBusinessService
     */
    @WebEndpoint(name = "PartnerBusinessServicePort")
    public IPartnerBusinessService getPartnerBusinessServicePort(WebServiceFeature... features) {
        return super.getPort(PartnerBusinessServicePort, IPartnerBusinessService.class, features);
    }

}
