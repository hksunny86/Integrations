
package com.inov8.integration.middleware.nadra.otc;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.integration.middleware.nadra.otc package. 
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

    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _TemplateType_QNAME = new QName("http://schemas.datacontract.org/2004/07/NADRA.Biometric.Verification", "TemplateType");
    private final static QName _RemittanceType_QNAME = new QName("http://schemas.datacontract.org/2004/07/NADRA.Biometric.Verification", "RemittanceType");
    private final static QName _VerifyFingerPrintsFranchizeID_QNAME = new QName("http://NADRA.Biometric.Verification", "franchizeID");
    private final static QName _VerifyFingerPrintsXmlRequestData_QNAME = new QName("http://NADRA.Biometric.Verification", "xml_request_data");
    private final static QName _VerifyFingerPrintsResponseVerifyFingerPrintsResult_QNAME = new QName("http://NADRA.Biometric.Verification", "VerifyFingerPrintsResult");
    private final static QName _VerifyPhotographResponseVerifyPhotographResult_QNAME = new QName("http://NADRA.Biometric.Verification", "VerifyPhotographResult");
    private final static QName _GetCitizenIdentityDemographicsDataResponseGetCitizenIdentityDemographicsDataResult_QNAME = new QName("http://NADRA.Biometric.Verification", "GetCitizenIdentityDemographicsDataResult");
    private final static QName _GetLastVerificationResultsResponseGetLastVerificationResultsResult_QNAME = new QName("http://NADRA.Biometric.Verification", "GetLastVerificationResultsResult");
    private final static QName _SubmitManualVerificationResultsResponseSubmitManualVerificationResultsResult_QNAME = new QName("http://NADRA.Biometric.Verification", "SubmitManualVerificationResultsResult");
    private final static QName _TestServiceTempType_QNAME = new QName("http://NADRA.Biometric.Verification", "tempType");
    private final static QName _TestServiceResponseTestServiceResult_QNAME = new QName("http://NADRA.Biometric.Verification", "TestServiceResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.nadra.otc
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
     * Create an instance of {@link VerifyPhotograph }
     * 
     */
    public VerifyPhotograph createVerifyPhotograph() {
        return new VerifyPhotograph();
    }

    /**
     * Create an instance of {@link VerifyPhotographResponse }
     * 
     */
    public VerifyPhotographResponse createVerifyPhotographResponse() {
        return new VerifyPhotographResponse();
    }

    /**
     * Create an instance of {@link GetCitizenIdentityDemographicsData }
     * 
     */
    public GetCitizenIdentityDemographicsData createGetCitizenIdentityDemographicsData() {
        return new GetCitizenIdentityDemographicsData();
    }

    /**
     * Create an instance of {@link GetCitizenIdentityDemographicsDataResponse }
     * 
     */
    public GetCitizenIdentityDemographicsDataResponse createGetCitizenIdentityDemographicsDataResponse() {
        return new GetCitizenIdentityDemographicsDataResponse();
    }

    /**
     * Create an instance of {@link GetLastVerificationResults }
     * 
     */
    public GetLastVerificationResults createGetLastVerificationResults() {
        return new GetLastVerificationResults();
    }

    /**
     * Create an instance of {@link GetLastVerificationResultsResponse }
     * 
     */
    public GetLastVerificationResultsResponse createGetLastVerificationResultsResponse() {
        return new GetLastVerificationResultsResponse();
    }

    /**
     * Create an instance of {@link SubmitManualVerificationResults }
     * 
     */
    public SubmitManualVerificationResults createSubmitManualVerificationResults() {
        return new SubmitManualVerificationResults();
    }

    /**
     * Create an instance of {@link SubmitManualVerificationResultsResponse }
     * 
     */
    public SubmitManualVerificationResultsResponse createSubmitManualVerificationResultsResponse() {
        return new SubmitManualVerificationResultsResponse();
    }

    /**
     * Create an instance of {@link TestService }
     * 
     */
    public TestService createTestService() {
        return new TestService();
    }

    /**
     * Create an instance of {@link TestServiceResponse }
     * 
     */
    public TestServiceResponse createTestServiceResponse() {
        return new TestServiceResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TemplateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/NADRA.Biometric.Verification", name = "TemplateType")
    public JAXBElement<TemplateType> createTemplateType(TemplateType value) {
        return new JAXBElement<TemplateType>(_TemplateType_QNAME, TemplateType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemittanceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/NADRA.Biometric.Verification", name = "RemittanceType")
    public JAXBElement<RemittanceType> createRemittanceType(RemittanceType value) {
        return new JAXBElement<RemittanceType>(_RemittanceType_QNAME, RemittanceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "franchizeID", scope = VerifyFingerPrints.class)
    public JAXBElement<String> createVerifyFingerPrintsFranchizeID(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsFranchizeID_QNAME, String.class, VerifyFingerPrints.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "xml_request_data", scope = VerifyFingerPrints.class)
    public JAXBElement<String> createVerifyFingerPrintsXmlRequestData(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsXmlRequestData_QNAME, String.class, VerifyFingerPrints.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "VerifyFingerPrintsResult", scope = VerifyFingerPrintsResponse.class)
    public JAXBElement<String> createVerifyFingerPrintsResponseVerifyFingerPrintsResult(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsResponseVerifyFingerPrintsResult_QNAME, String.class, VerifyFingerPrintsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "franchizeID", scope = VerifyPhotograph.class)
    public JAXBElement<String> createVerifyPhotographFranchizeID(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsFranchizeID_QNAME, String.class, VerifyPhotograph.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "xml_request_data", scope = VerifyPhotograph.class)
    public JAXBElement<String> createVerifyPhotographXmlRequestData(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsXmlRequestData_QNAME, String.class, VerifyPhotograph.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "VerifyPhotographResult", scope = VerifyPhotographResponse.class)
    public JAXBElement<String> createVerifyPhotographResponseVerifyPhotographResult(String value) {
        return new JAXBElement<String>(_VerifyPhotographResponseVerifyPhotographResult_QNAME, String.class, VerifyPhotographResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "franchizeID", scope = GetCitizenIdentityDemographicsData.class)
    public JAXBElement<String> createGetCitizenIdentityDemographicsDataFranchizeID(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsFranchizeID_QNAME, String.class, GetCitizenIdentityDemographicsData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "xml_request_data", scope = GetCitizenIdentityDemographicsData.class)
    public JAXBElement<String> createGetCitizenIdentityDemographicsDataXmlRequestData(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsXmlRequestData_QNAME, String.class, GetCitizenIdentityDemographicsData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "GetCitizenIdentityDemographicsDataResult", scope = GetCitizenIdentityDemographicsDataResponse.class)
    public JAXBElement<String> createGetCitizenIdentityDemographicsDataResponseGetCitizenIdentityDemographicsDataResult(String value) {
        return new JAXBElement<String>(_GetCitizenIdentityDemographicsDataResponseGetCitizenIdentityDemographicsDataResult_QNAME, String.class, GetCitizenIdentityDemographicsDataResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "franchizeID", scope = GetLastVerificationResults.class)
    public JAXBElement<String> createGetLastVerificationResultsFranchizeID(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsFranchizeID_QNAME, String.class, GetLastVerificationResults.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "xml_request_data", scope = GetLastVerificationResults.class)
    public JAXBElement<String> createGetLastVerificationResultsXmlRequestData(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsXmlRequestData_QNAME, String.class, GetLastVerificationResults.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "GetLastVerificationResultsResult", scope = GetLastVerificationResultsResponse.class)
    public JAXBElement<String> createGetLastVerificationResultsResponseGetLastVerificationResultsResult(String value) {
        return new JAXBElement<String>(_GetLastVerificationResultsResponseGetLastVerificationResultsResult_QNAME, String.class, GetLastVerificationResultsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "franchizeID", scope = SubmitManualVerificationResults.class)
    public JAXBElement<String> createSubmitManualVerificationResultsFranchizeID(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsFranchizeID_QNAME, String.class, SubmitManualVerificationResults.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "xml_request_data", scope = SubmitManualVerificationResults.class)
    public JAXBElement<String> createSubmitManualVerificationResultsXmlRequestData(String value) {
        return new JAXBElement<String>(_VerifyFingerPrintsXmlRequestData_QNAME, String.class, SubmitManualVerificationResults.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "SubmitManualVerificationResultsResult", scope = SubmitManualVerificationResultsResponse.class)
    public JAXBElement<String> createSubmitManualVerificationResultsResponseSubmitManualVerificationResultsResult(String value) {
        return new JAXBElement<String>(_SubmitManualVerificationResultsResponseSubmitManualVerificationResultsResult_QNAME, String.class, SubmitManualVerificationResultsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "tempType", scope = TestService.class)
    public JAXBElement<String> createTestServiceTempType(String value) {
        return new JAXBElement<String>(_TestServiceTempType_QNAME, String.class, TestService.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NADRA.Biometric.Verification", name = "TestServiceResult", scope = TestServiceResponse.class)
    public JAXBElement<String> createTestServiceResponseTestServiceResult(String value) {
        return new JAXBElement<String>(_TestServiceResponseTestServiceResult_QNAME, String.class, TestServiceResponse.class, value);
    }

}
