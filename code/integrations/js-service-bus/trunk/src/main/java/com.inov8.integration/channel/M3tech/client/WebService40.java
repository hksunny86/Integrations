package com.inov8.integration.channel.M3tech.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.5
 * 2022-06-07T11:46:16.934+05:00
 * Generated source version: 3.1.5
 * 
 */
@WebServiceClient(name = "WebService_4_0", 
                  wsdlLocation = "file:M3tech.wsdl",
                  targetNamespace = "http://tempuri.org/") 
public class WebService40 extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tempuri.org/", "WebService_4_0");
    public final static QName WebService40HttpGet = new QName("http://tempuri.org/", "WebService_4_0HttpGet");
    public final static QName WebService40Soap12 = new QName("http://tempuri.org/", "WebService_4_0Soap12");
    public final static QName WebService40Soap = new QName("http://tempuri.org/", "WebService_4_0Soap");
    public final static QName WebService40HttpPost = new QName("http://tempuri.org/", "WebService_4_0HttpPost");
    static {
        URL url = null;
        try {
            url = new URL("file:M3tech.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(WebService40.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:M3tech.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public WebService40(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public WebService40(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WebService40() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public WebService40(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public WebService40(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public WebService40(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns WebService40HttpGet
     */
    @WebEndpoint(name = "WebService_4_0HttpGet")
    public WebService40HttpGet getWebService40HttpGet() {
        return super.getPort(WebService40HttpGet, WebService40HttpGet.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WebService40HttpGet
     */
    @WebEndpoint(name = "WebService_4_0HttpGet")
    public WebService40HttpGet getWebService40HttpGet(WebServiceFeature... features) {
        return super.getPort(WebService40HttpGet, WebService40HttpGet.class, features);
    }


    /**
     *
     * @return
     *     returns WebService40Soap
     */
    @WebEndpoint(name = "WebService_4_0Soap12")
    public WebService40Soap getWebService40Soap12() {
        return super.getPort(WebService40Soap12, WebService40Soap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WebService40Soap
     */
    @WebEndpoint(name = "WebService_4_0Soap12")
    public WebService40Soap getWebService40Soap12(WebServiceFeature... features) {
        return super.getPort(WebService40Soap12, WebService40Soap.class, features);
    }


    /**
     *
     * @return
     *     returns WebService40Soap
     */
    @WebEndpoint(name = "WebService_4_0Soap")
    public WebService40Soap getWebService40Soap() {
        return super.getPort(WebService40Soap, WebService40Soap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WebService40Soap
     */
    @WebEndpoint(name = "WebService_4_0Soap")
    public WebService40Soap getWebService40Soap(WebServiceFeature... features) {
        return super.getPort(WebService40Soap, WebService40Soap.class, features);
    }


    /**
     *
     * @return
     *     returns WebService40HttpPost
     */
    @WebEndpoint(name = "WebService_4_0HttpPost")
    public WebService40HttpPost getWebService40HttpPost() {
        return super.getPort(WebService40HttpPost, WebService40HttpPost.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WebService40HttpPost
     */
    @WebEndpoint(name = "WebService_4_0HttpPost")
    public WebService40HttpPost getWebService40HttpPost(WebServiceFeature... features) {
        return super.getPort(WebService40HttpPost, WebService40HttpPost.class, features);
    }

}
