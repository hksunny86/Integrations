
package com.inov8.integration.middleware.jsnadra;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.integration.middleware.jsnadra package. 
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

    private final static QName _VerifyFingerPrints_QNAME = new QName("http://JSNadraWebService", "VerifyFingerPrints");
    private final static QName _VerifyFingerPrintsResponse_QNAME = new QName("http://JSNadraWebService", "VerifyFingerPrintsResponse");
    private final static QName _GetCitizenData_QNAME = new QName("http://JSNadraWebService", "GetCitizenData");
    private final static QName _GetCitizenDataResponse_QNAME = new QName("http://JSNadraWebService", "GetCitizenDataResponse");
    private final static QName _VerifyFingerPrintsOTC_QNAME = new QName("http://JSNadraWebService", "VerifyFingerPrintsOTC");
    private final static QName _VerifyFingerPrintsOTCResponse_QNAME = new QName("http://JSNadraWebService", "VerifyFingerPrintsOTCResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.jsnadra
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link VerifyFingerPrints }
     * 
     */
    public VerifyFingerPrints createVerifyFingerPrints() {
        return new VerifyFingerPrints();
    }

    /**
     * Create an instance of {@link VerifyFingerPrintsResponse }
     * 
     */
    public VerifyFingerPrintsResponse createVerifyFingerPrintsResponse() {
        return new VerifyFingerPrintsResponse();
    }

    /**
     * Create an instance of {@link GetCitizenData }
     * 
     */
    public GetCitizenData createGetCitizenData() {
        return new GetCitizenData();
    }

    /**
     * Create an instance of {@link GetCitizenDataResponse }
     * 
     */
    public GetCitizenDataResponse createGetCitizenDataResponse() {
        return new GetCitizenDataResponse();
    }

    /**
     * Create an instance of {@link VerifyFingerPrintsOTC }
     * 
     */
    public VerifyFingerPrintsOTC createVerifyFingerPrintsOTC() {
        return new VerifyFingerPrintsOTC();
    }

    /**
     * Create an instance of {@link VerifyFingerPrintsOTCResponse }
     * 
     */
    public VerifyFingerPrintsOTCResponse createVerifyFingerPrintsOTCResponse() {
        return new VerifyFingerPrintsOTCResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyFingerPrints }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://JSNadraWebService", name = "VerifyFingerPrints")
    public JAXBElement<VerifyFingerPrints> createVerifyFingerPrints(VerifyFingerPrints value) {
        return new JAXBElement<VerifyFingerPrints>(_VerifyFingerPrints_QNAME, VerifyFingerPrints.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyFingerPrintsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://JSNadraWebService", name = "VerifyFingerPrintsResponse")
    public JAXBElement<VerifyFingerPrintsResponse> createVerifyFingerPrintsResponse(VerifyFingerPrintsResponse value) {
        return new JAXBElement<VerifyFingerPrintsResponse>(_VerifyFingerPrintsResponse_QNAME, VerifyFingerPrintsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCitizenData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://JSNadraWebService", name = "GetCitizenData")
    public JAXBElement<GetCitizenData> createGetCitizenData(GetCitizenData value) {
        return new JAXBElement<GetCitizenData>(_GetCitizenData_QNAME, GetCitizenData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCitizenDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://JSNadraWebService", name = "GetCitizenDataResponse")
    public JAXBElement<GetCitizenDataResponse> createGetCitizenDataResponse(GetCitizenDataResponse value) {
        return new JAXBElement<GetCitizenDataResponse>(_GetCitizenDataResponse_QNAME, GetCitizenDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyFingerPrintsOTC }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://JSNadraWebService", name = "VerifyFingerPrintsOTC")
    public JAXBElement<VerifyFingerPrintsOTC> createVerifyFingerPrintsOTC(VerifyFingerPrintsOTC value) {
        return new JAXBElement<VerifyFingerPrintsOTC>(_VerifyFingerPrintsOTC_QNAME, VerifyFingerPrintsOTC.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyFingerPrintsOTCResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://JSNadraWebService", name = "VerifyFingerPrintsOTCResponse")
    public JAXBElement<VerifyFingerPrintsOTCResponse> createVerifyFingerPrintsOTCResponse(VerifyFingerPrintsOTCResponse value) {
        return new JAXBElement<VerifyFingerPrintsOTCResponse>(_VerifyFingerPrintsOTCResponse_QNAME, VerifyFingerPrintsOTCResponse.class, null, value);
    }

}
