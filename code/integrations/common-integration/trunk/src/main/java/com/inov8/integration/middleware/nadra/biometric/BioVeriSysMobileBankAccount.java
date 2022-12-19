package com.inov8.integration.middleware.nadra.biometric;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.6
 * 2019-12-19T12:55:54.488+05:00
 * Generated source version: 3.1.6
 * 
 */
@WebServiceClient(name = "BioVeriSysMobileBankAccount", 
                  wsdlLocation = "file:BioVeriSysMobileBankAccount.wsdl",
                  targetNamespace = "http://NADRA.Biometric.Verification") 
public class BioVeriSysMobileBankAccount extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://NADRA.Biometric.Verification", "BioVeriSysMobileBankAccount");
    public final static QName BasicHttpBindingIBioVeriSysMobileBankAccount = new QName("http://NADRA.Biometric.Verification", "BasicHttpBinding_IBioVeriSysMobileBankAccount");
    static {
        URL url = null;
        try {
            url = new URL("file:BioVeriSysMobileBankAccount.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(BioVeriSysMobileBankAccount.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:BioVeriSysMobileBankAccount.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public BioVeriSysMobileBankAccount(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BioVeriSysMobileBankAccount(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BioVeriSysMobileBankAccount() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public BioVeriSysMobileBankAccount(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public BioVeriSysMobileBankAccount(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public BioVeriSysMobileBankAccount(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns IBioVeriSysMobileBankAccount
     */
    @WebEndpoint(name = "BasicHttpBinding_IBioVeriSysMobileBankAccount")
    public IBioVeriSysMobileBankAccount getBasicHttpBindingIBioVeriSysMobileBankAccount() {
        return super.getPort(BasicHttpBindingIBioVeriSysMobileBankAccount, IBioVeriSysMobileBankAccount.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IBioVeriSysMobileBankAccount
     */
    @WebEndpoint(name = "BasicHttpBinding_IBioVeriSysMobileBankAccount")
    public IBioVeriSysMobileBankAccount getBasicHttpBindingIBioVeriSysMobileBankAccount(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingIBioVeriSysMobileBankAccount, IBioVeriSysMobileBankAccount.class, features);
    }

}
