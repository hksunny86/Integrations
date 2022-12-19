package com.inov8.integration.channel.BOPBLB.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2020-10-27T20:17:16.518+05:00
 * Generated source version: 3.1.6
 * 
 */
@WebServiceClient(name = "ProcessEchallan", 
                  wsdlLocation = "file:proofoflife.wsdl",
                  targetNamespace = "http://tempuri.org/") 
public class ProcessEchallan extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tempuri.org/", "ProcessEchallan");
    public final static QName ProcessEchallanSoap = new QName("http://tempuri.org/", "ProcessEchallanSoap");
    public final static QName ProcessEchallanSoap12 = new QName("http://tempuri.org/", "ProcessEchallanSoap12");
    static {
        URL url = null;
        try {
            url = new URL("file:proofoflife.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(ProcessEchallan.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:proofoflife.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public ProcessEchallan(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ProcessEchallan(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ProcessEchallan() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public ProcessEchallan(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public ProcessEchallan(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public ProcessEchallan(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns ProcessEchallanSoap
     */
    @WebEndpoint(name = "ProcessEchallanSoap")
    public ProcessEchallanSoap getProcessEchallanSoap() {
        return super.getPort(ProcessEchallanSoap, ProcessEchallanSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ProcessEchallanSoap
     */
    @WebEndpoint(name = "ProcessEchallanSoap")
    public ProcessEchallanSoap getProcessEchallanSoap(WebServiceFeature... features) {
        return super.getPort(ProcessEchallanSoap, ProcessEchallanSoap.class, features);
    }


    /**
     *
     * @return
     *     returns ProcessEchallanSoap
     */
    @WebEndpoint(name = "ProcessEchallanSoap12")
    public ProcessEchallanSoap getProcessEchallanSoap12() {
        return super.getPort(ProcessEchallanSoap12, ProcessEchallanSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ProcessEchallanSoap
     */
    @WebEndpoint(name = "ProcessEchallanSoap12")
    public ProcessEchallanSoap getProcessEchallanSoap12(WebServiceFeature... features) {
        return super.getPort(ProcessEchallanSoap12, ProcessEchallanSoap.class, features);
    }

}
