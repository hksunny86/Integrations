
package com.inov8.integration.middleware.daewoo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.integration.middleware.daewoo package. 
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

    private final static QName _String_QNAME = new QName("http://daewoowsdl.com/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.daewoo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetDepartures }
     * 
     */
    public GetDepartures createGetDepartures() {
        return new GetDepartures();
    }

    /**
     * Create an instance of {@link GetDeparturesResponse }
     * 
     */
    public GetDeparturesResponse createGetDeparturesResponse() {
        return new GetDeparturesResponse();
    }

    /**
     * Create an instance of {@link GetArrivals }
     * 
     */
    public GetArrivals createGetArrivals() {
        return new GetArrivals();
    }

    /**
     * Create an instance of {@link GetArrivalsResponse }
     * 
     */
    public GetArrivalsResponse createGetArrivalsResponse() {
        return new GetArrivalsResponse();
    }

    /**
     * Create an instance of {@link GetRoutes }
     * 
     */
    public GetRoutes createGetRoutes() {
        return new GetRoutes();
    }

    /**
     * Create an instance of {@link GetRoutesResponse }
     * 
     */
    public GetRoutesResponse createGetRoutesResponse() {
        return new GetRoutesResponse();
    }

    /**
     * Create an instance of {@link GetSeats }
     * 
     */
    public GetSeats createGetSeats() {
        return new GetSeats();
    }

    /**
     * Create an instance of {@link GetSeatsResponse }
     * 
     */
    public GetSeatsResponse createGetSeatsResponse() {
        return new GetSeatsResponse();
    }

    /**
     * Create an instance of {@link GetBooking }
     * 
     */
    public GetBooking createGetBooking() {
        return new GetBooking();
    }

    /**
     * Create an instance of {@link GetBookingResponse }
     * 
     */
    public GetBookingResponse createGetBookingResponse() {
        return new GetBookingResponse();
    }

    /**
     * Create an instance of {@link GetTicket }
     * 
     */
    public GetTicket createGetTicket() {
        return new GetTicket();
    }

    /**
     * Create an instance of {@link GetTicketResponse }
     * 
     */
    public GetTicketResponse createGetTicketResponse() {
        return new GetTicketResponse();
    }

    /**
     * Create an instance of {@link GetSeatQuota }
     * 
     */
    public GetSeatQuota createGetSeatQuota() {
        return new GetSeatQuota();
    }

    /**
     * Create an instance of {@link GetSeatQuotaResponse }
     * 
     */
    public GetSeatQuotaResponse createGetSeatQuotaResponse() {
        return new GetSeatQuotaResponse();
    }

    /**
     * Create an instance of {@link GetBookingDetails }
     * 
     */
    public GetBookingDetails createGetBookingDetails() {
        return new GetBookingDetails();
    }

    /**
     * Create an instance of {@link GetBookingDetailsResponse }
     * 
     */
    public GetBookingDetailsResponse createGetBookingDetailsResponse() {
        return new GetBookingDetailsResponse();
    }

    /**
     * Create an instance of {@link CTR }
     * 
     */
    public CTR createCTR() {
        return new CTR();
    }

    /**
     * Create an instance of {@link CTRResponse }
     * 
     */
    public CTRResponse createCTRResponse() {
        return new CTRResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://daewoowsdl.com/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
