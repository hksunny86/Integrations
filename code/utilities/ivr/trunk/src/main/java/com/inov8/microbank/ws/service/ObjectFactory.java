
package com.inov8.microbank.ws.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.microbank.service package. 
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
@Deprecated
public class ObjectFactory {

    private final static QName _IvrResponseDTO_QNAME = new QName("http://service.microbank.inov8.com/", "ivrResponseDTO");
    private final static QName _HandleIVRRequestResponse_QNAME = new QName("http://service.microbank.inov8.com/", "handleIVRRequestResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.microbank.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IvrResponseDTO }
     * 
     */
    public IvrResponseDTO createIvrResponseDTO() {
        return new IvrResponseDTO();
    }
    
    /**
     * Create an instance of {@link IvrRequestDTO }
     * 
     */
    public IvrRequestDTO createIvrRequestDTO() {
        return new IvrRequestDTO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IvrResponseDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.microbank.inov8.com/", name = "ivrResponseDTO")
    public JAXBElement<IvrResponseDTO> createIvrResponseDTO(IvrResponseDTO value) {
        return new JAXBElement<IvrResponseDTO>(_IvrResponseDTO_QNAME, IvrResponseDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IvrRequestDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.microbank.inov8.com/", name = "handleIVRRequestResponse")
    public JAXBElement<IvrRequestDTO> createHandleIVRRequestResponse(IvrRequestDTO value) {
        return new JAXBElement<IvrRequestDTO>(_HandleIVRRequestResponse_QNAME, IvrRequestDTO.class, null, value);
    }

}
