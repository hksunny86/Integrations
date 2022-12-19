
package com.inov8.integration.channel.CLSJS.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.integration.channel.CLSJS.client package. 
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

    private final static QName _CNIC_QNAME = new QName("http://tempuri.org/Screen_MS", "CNIC");
    private final static QName _CustomerName_QNAME = new QName("http://tempuri.org/Screen_MS", "CustomerName");
    private final static QName _DateOfBirth_QNAME = new QName("http://tempuri.org/Screen_MS", "DateOfBirth");
    private final static QName _Nationality_QNAME = new QName("http://tempuri.org/Screen_MS", "Nationality");
    private final static QName _Residence_QNAME = new QName("http://tempuri.org/Screen_MS", "Residence");
    private final static QName _CustomerNumber_QNAME = new QName("http://tempuri.org/Screen_MS", "CustomerNumber");
    private final static QName _AccountNumber_QNAME = new QName("http://tempuri.org/Screen_MS", "AccountNumber");
    private final static QName _IsIndividual_QNAME = new QName("http://tempuri.org/Screen_MS", "IsIndividual");
    private final static QName _BranchCode_QNAME = new QName("http://tempuri.org/Screen_MS", "BranchCode");
    private final static QName _UserId_QNAME = new QName("http://tempuri.org/Screen_MS", "UserId");
    private final static QName _CompanyNTN_QNAME = new QName("http://tempuri.org/Screen_MS", "CompanyNTN");
    private final static QName _CaseId_QNAME = new QName("http://tempuri.org/Screen_MS", "CaseId");
    private final static QName _CaseStatus_QNAME = new QName("http://tempuri.org/Screen_MS", "CaseStatus");
    private final static QName _ImportStatus_QNAME = new QName("http://tempuri.org/Screen_MS", "ImportStatus");
    private final static QName _IsHit_QNAME = new QName("http://tempuri.org/Screen_MS", "IsHit");
    private final static QName _ScreeningStatus_QNAME = new QName("http://tempuri.org/Screen_MS", "ScreeningStatus");
    private final static QName _TotalCWL_QNAME = new QName("http://tempuri.org/Screen_MS", "TotalCWL");
    private final static QName _TotalGWL_QNAME = new QName("http://tempuri.org/Screen_MS", "TotalGWL");
    private final static QName _TotalPEPEDD_QNAME = new QName("http://tempuri.org/Screen_MS", "TotalPEPEDD");
    private final static QName _TotalPrivate_QNAME = new QName("http://tempuri.org/Screen_MS", "TotalPrivate");
    private final static QName _ImportScreening_QNAME = new QName("http://tempuri.org/Screen_MS", "ImportScreening");
    private final static QName _ImportScreeningResponse_QNAME = new QName("http://tempuri.org/Screen_MS", "ImportScreeningResponse");
    private final static QName _City_QNAME = new QName("http://tempuri.org/Screen_MS", "City");
    private final static QName _RequestID_QNAME = new QName("http://tempuri.org/Screen_MS", "RequestID");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.channel.CLSJS.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ImportScreening }
     * 
     */
    public ImportScreening createImportScreening() {
        return new ImportScreening();
    }

    /**
     * Create an instance of {@link ImportScreeningResponse }
     * 
     */
    public ImportScreeningResponse createImportScreeningResponse() {
        return new ImportScreeningResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "CNIC")
    public JAXBElement<String> createCNIC(String value) {
        return new JAXBElement<String>(_CNIC_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "CustomerName")
    public JAXBElement<String> createCustomerName(String value) {
        return new JAXBElement<String>(_CustomerName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "DateOfBirth")
    public JAXBElement<String> createDateOfBirth(String value) {
        return new JAXBElement<String>(_DateOfBirth_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "Nationality")
    public JAXBElement<String> createNationality(String value) {
        return new JAXBElement<String>(_Nationality_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "Residence")
    public JAXBElement<String> createResidence(String value) {
        return new JAXBElement<String>(_Residence_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "CustomerNumber")
    public JAXBElement<String> createCustomerNumber(String value) {
        return new JAXBElement<String>(_CustomerNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "AccountNumber")
    public JAXBElement<String> createAccountNumber(String value) {
        return new JAXBElement<String>(_AccountNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "IsIndividual")
    public JAXBElement<String> createIsIndividual(String value) {
        return new JAXBElement<String>(_IsIndividual_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "BranchCode")
    public JAXBElement<String> createBranchCode(String value) {
        return new JAXBElement<String>(_BranchCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "UserId")
    public JAXBElement<String> createUserId(String value) {
        return new JAXBElement<String>(_UserId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "CompanyNTN")
    public JAXBElement<String> createCompanyNTN(String value) {
        return new JAXBElement<String>(_CompanyNTN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "CaseId")
    public JAXBElement<String> createCaseId(String value) {
        return new JAXBElement<String>(_CaseId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "CaseStatus")
    public JAXBElement<String> createCaseStatus(String value) {
        return new JAXBElement<String>(_CaseStatus_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "ImportStatus")
    public JAXBElement<String> createImportStatus(String value) {
        return new JAXBElement<String>(_ImportStatus_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "IsHit")
    public JAXBElement<String> createIsHit(String value) {
        return new JAXBElement<String>(_IsHit_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "ScreeningStatus")
    public JAXBElement<String> createScreeningStatus(String value) {
        return new JAXBElement<String>(_ScreeningStatus_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "TotalCWL")
    public JAXBElement<String> createTotalCWL(String value) {
        return new JAXBElement<String>(_TotalCWL_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "TotalGWL")
    public JAXBElement<String> createTotalGWL(String value) {
        return new JAXBElement<String>(_TotalGWL_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "TotalPEPEDD")
    public JAXBElement<String> createTotalPEPEDD(String value) {
        return new JAXBElement<String>(_TotalPEPEDD_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "TotalPrivate")
    public JAXBElement<String> createTotalPrivate(String value) {
        return new JAXBElement<String>(_TotalPrivate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportScreening }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "ImportScreening")
    public JAXBElement<ImportScreening> createImportScreening(ImportScreening value) {
        return new JAXBElement<ImportScreening>(_ImportScreening_QNAME, ImportScreening.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportScreeningResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "ImportScreeningResponse")
    public JAXBElement<ImportScreeningResponse> createImportScreeningResponse(ImportScreeningResponse value) {
        return new JAXBElement<ImportScreeningResponse>(_ImportScreeningResponse_QNAME, ImportScreeningResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "City")
    public JAXBElement<String> createCity(String value) {
        return new JAXBElement<String>(_City_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/Screen_MS", name = "RequestID")
    public JAXBElement<String> createRequestID(String value) {
        return new JAXBElement<String>(_RequestID_QNAME, String.class, null, value);
    }

}
