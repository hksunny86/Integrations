
package com.inov8.integration.middleware.translitration.TacitService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.integration.middleware.translitration.TacitService package. 
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

    private final static QName _String_QNAME = new QName("http://translator.tecthis/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.translitration.TacitService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TestFunc }
     * 
     */
    public TestFunc createTestFunc() {
        return new TestFunc();
    }

    /**
     * Create an instance of {@link TestFuncResponse }
     * 
     */
    public TestFuncResponse createTestFuncResponse() {
        return new TestFuncResponse();
    }

    /**
     * Create an instance of {@link UtoEService }
     * 
     */
    public UtoEService createUtoEService() {
        return new UtoEService();
    }

    /**
     * Create an instance of {@link UtoEServiceResponse }
     * 
     */
    public UtoEServiceResponse createUtoEServiceResponse() {
        return new UtoEServiceResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://translator.tecthis/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
