package com.inov8.microbank.server.webserviceclient.ivr;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * com.inov8.microbank.server.webserviceclient.ivr package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _HandleRequestResponse_QNAME = new QName(
			"http://server.microbank.inov8.com/", "handleRequestResponse");
	private final static QName _HandleRequest_QNAME = new QName(
			"http://server.microbank.inov8.com/", "handleRequest");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * com.inov8.microbank.server.webserviceclient.ivr
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link HandleRequestResponse }
	 * 
	 */
	public HandleRequestResponse createHandleRequestResponse() {
		return new HandleRequestResponse();
	}

	/**
	 * Create an instance of {@link IvrRequestDTO }
	 * 
	 */
	public IvrRequestDTO createIvrRequestDTO() {
		return new IvrRequestDTO();
	}

	/**
	 * Create an instance of {@link HandleRequest }
	 * 
	 */
	public HandleRequest createHandleRequest() {
		return new HandleRequest();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link HandleRequestResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://server.microbank.inov8.com/", name = "handleRequestResponse")
	public JAXBElement<HandleRequestResponse> createHandleRequestResponse(
			HandleRequestResponse value) {
		return new JAXBElement<HandleRequestResponse>(
				_HandleRequestResponse_QNAME, HandleRequestResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link HandleRequest }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://server.microbank.inov8.com/", name = "handleRequest")
	public JAXBElement<HandleRequest> createHandleRequest(HandleRequest value) {
		return new JAXBElement<HandleRequest>(_HandleRequest_QNAME,
				HandleRequest.class, null, value);
	}

}
