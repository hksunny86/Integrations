package com.inov8.integration.middleware.bop.nadra;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2020-02-21T18:56:01.715+05:00
 * Generated source version: 3.1.6
 * 
 */
@WebServiceClient(name = "RelayService", 
                  wsdlLocation = "file:nadra.wsdl",
                  targetNamespace = "http://tempuri.org/") 
public class RelayService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tempuri.org/", "RelayService");
    public final static QName BasicHttpBindingIBioVeriSysBranchBankAccount = new QName("http://tempuri.org/", "BasicHttpBinding_IBioVeriSysBranchBankAccount");
    static {
        URL url = null;
        try {
            url = new URL("file:nadra.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RelayService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:nadra.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RelayService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RelayService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RelayService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public RelayService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public RelayService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public RelayService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns IRelayService
     */
    @WebEndpoint(name = "BasicHttpBinding_IBioVeriSysBranchBankAccount")
    public IRelayService getBasicHttpBindingIBioVeriSysBranchBankAccount() {
        return super.getPort(BasicHttpBindingIBioVeriSysBranchBankAccount, IRelayService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IRelayService
     */
    @WebEndpoint(name = "BasicHttpBinding_IBioVeriSysBranchBankAccount")
    public IRelayService getBasicHttpBindingIBioVeriSysBranchBankAccount(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingIBioVeriSysBranchBankAccount, IRelayService.class, features);
    }

}
