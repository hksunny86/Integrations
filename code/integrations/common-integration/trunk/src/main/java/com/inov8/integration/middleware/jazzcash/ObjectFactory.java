
package com.inov8.integration.middleware.jazzcash;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.server package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ResultMsg_QNAME = new QName("http://cps.huawei.com/cpsinterface/result", "ResultMsg");
    private final static QName _ResponseMsg_QNAME = new QName("http://cps.huawei.com/cpsinterface/result", "ResponseMsg");
    private final static QName _RequestMsg_QNAME = new QName("http://cps.huawei.com/cpsinterface/request", "RequestMsg");
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.server
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cps.huawei.com/cpsinterface/result", name = "ResultMsg")
    public JAXBElement<String> createResultMsg(String value) {
        return new JAXBElement<String>(_ResultMsg_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */

	  @XmlElementDecl(namespace = "http://cps.huawei.com/cpsinterface/request", name = "RequestMsg")
    public JAXBElement<String> createRequestMsg(String value) {
        return new JAXBElement<String>(_RequestMsg_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cps.huawei.com/cpsinterface/request", name = "ResponseMsg")
    public JAXBElement<String> createResponseMsg(String value) {
        return new JAXBElement<String>(_ResponseMsg_QNAME, String.class, null, value);
    }

}
