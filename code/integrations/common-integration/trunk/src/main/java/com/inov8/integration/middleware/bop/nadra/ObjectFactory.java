
package com.inov8.integration.middleware.bop.nadra;

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
 * generated in the com.inov8.integration.middleware.bop.nadra package. 
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

    private final static QName _UserCredentials_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification", "UserCredentials");
    private final static QName _NADRADTO_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "NADRA_DTO");
    private final static QName _EnumsTemplateFormat_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "Enums.Template_Format");
    private final static QName _NADRADTOContextData_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "NADRA_DTO.ContextData");
    private final static QName _CitizenVerificationDTO_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "CitizenVerificationDTO");
    private final static QName _OTCNADRADTO_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "OTC_NADRA_DTO");
    private final static QName _ContextData_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "ContextData");
    private final static QName _EnumsREMITTANCETYPE_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "Enums.REMITTANCE_TYPE");
    private final static QName _OTCVerificationDTO_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "OTCVerificationDTO");
    private final static QName _CitizenVerificationLocalDTO_QNAME = new QName("http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", "CitizenVerificationLocalDTO");
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
    private final static QName _ArrayOfint_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfint");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.bop.nadra
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UserCredentials }
     * 
     */
    public UserCredentials createUserCredentials() {
        return new UserCredentials();
    }

    /**
     * Create an instance of {@link VerifyCitizen }
     * 
     */
    public VerifyCitizen createVerifyCitizen() {
        return new VerifyCitizen();
    }

    /**
     * Create an instance of {@link NADRADTO }
     * 
     */
    public NADRADTO createNADRADTO() {
        return new NADRADTO();
    }

    /**
     * Create an instance of {@link VerifyCitizenResponse }
     * 
     */
    public VerifyCitizenResponse createVerifyCitizenResponse() {
        return new VerifyCitizenResponse();
    }

    /**
     * Create an instance of {@link CitizenVerificationDTO }
     * 
     */
    public CitizenVerificationDTO createCitizenVerificationDTO() {
        return new CitizenVerificationDTO();
    }

    /**
     * Create an instance of {@link VerifyOTCTransaction }
     * 
     */
    public VerifyOTCTransaction createVerifyOTCTransaction() {
        return new VerifyOTCTransaction();
    }

    /**
     * Create an instance of {@link OTCNADRADTO }
     * 
     */
    public OTCNADRADTO createOTCNADRADTO() {
        return new OTCNADRADTO();
    }

    /**
     * Create an instance of {@link VerifyOTCTransactionResponse }
     * 
     */
    public VerifyOTCTransactionResponse createVerifyOTCTransactionResponse() {
        return new VerifyOTCTransactionResponse();
    }

    /**
     * Create an instance of {@link OTCVerificationDTO }
     * 
     */
    public OTCVerificationDTO createOTCVerificationDTO() {
        return new OTCVerificationDTO();
    }

    /**
     * Create an instance of {@link VerifyCitizenWithSegment }
     * 
     */
    public VerifyCitizenWithSegment createVerifyCitizenWithSegment() {
        return new VerifyCitizenWithSegment();
    }

    /**
     * Create an instance of {@link VerifyCitizenWithSegmentResponse }
     * 
     */
    public VerifyCitizenWithSegmentResponse createVerifyCitizenWithSegmentResponse() {
        return new VerifyCitizenWithSegmentResponse();
    }

    /**
     * Create an instance of {@link VerifyCitizenLocally }
     * 
     */
    public VerifyCitizenLocally createVerifyCitizenLocally() {
        return new VerifyCitizenLocally();
    }

    /**
     * Create an instance of {@link VerifyCitizenLocallyResponse }
     * 
     */
    public VerifyCitizenLocallyResponse createVerifyCitizenLocallyResponse() {
        return new VerifyCitizenLocallyResponse();
    }

    /**
     * Create an instance of {@link CitizenVerificationLocalDTO }
     * 
     */
    public CitizenVerificationLocalDTO createCitizenVerificationLocalDTO() {
        return new CitizenVerificationLocalDTO();
    }

    /**
     * Create an instance of {@link CheckVerisysBYSegement }
     * 
     */
    public CheckVerisysBYSegement createCheckVerisysBYSegement() {
        return new CheckVerisysBYSegement();
    }

    /**
     * Create an instance of {@link CheckVerisysBYSegementResponse }
     * 
     */
    public CheckVerisysBYSegementResponse createCheckVerisysBYSegementResponse() {
        return new CheckVerisysBYSegementResponse();
    }

    /**
     * Create an instance of {@link GetExistingVerisys }
     * 
     */
    public GetExistingVerisys createGetExistingVerisys() {
        return new GetExistingVerisys();
    }

    /**
     * Create an instance of {@link GetExistingVerisysResponse }
     * 
     */
    public GetExistingVerisysResponse createGetExistingVerisysResponse() {
        return new GetExistingVerisysResponse();
    }

    /**
     * Create an instance of {@link VerifyUser }
     * 
     */
    public VerifyUser createVerifyUser() {
        return new VerifyUser();
    }

    /**
     * Create an instance of {@link VerifyUserResponse }
     * 
     */
    public VerifyUserResponse createVerifyUserResponse() {
        return new VerifyUserResponse();
    }

    /**
     * Create an instance of {@link NADRADTOContextData }
     * 
     */
    public NADRADTOContextData createNADRADTOContextData() {
        return new NADRADTOContextData();
    }

    /**
     * Create an instance of {@link ContextData }
     * 
     */
    public ContextData createContextData() {
        return new ContextData();
    }

    /**
     * Create an instance of {@link ArrayOfint }
     * 
     */
    public ArrayOfint createArrayOfint() {
        return new ArrayOfint();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserCredentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification", name = "UserCredentials")
    public JAXBElement<UserCredentials> createUserCredentials(UserCredentials value) {
        return new JAXBElement<UserCredentials>(_UserCredentials_QNAME, UserCredentials.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NADRADTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "NADRA_DTO")
    public JAXBElement<NADRADTO> createNADRADTO(NADRADTO value) {
        return new JAXBElement<NADRADTO>(_NADRADTO_QNAME, NADRADTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnumsTemplateFormat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "Enums.Template_Format")
    public JAXBElement<EnumsTemplateFormat> createEnumsTemplateFormat(EnumsTemplateFormat value) {
        return new JAXBElement<EnumsTemplateFormat>(_EnumsTemplateFormat_QNAME, EnumsTemplateFormat.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NADRADTOContextData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "NADRA_DTO.ContextData")
    public JAXBElement<NADRADTOContextData> createNADRADTOContextData(NADRADTOContextData value) {
        return new JAXBElement<NADRADTOContextData>(_NADRADTOContextData_QNAME, NADRADTOContextData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CitizenVerificationDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "CitizenVerificationDTO")
    public JAXBElement<CitizenVerificationDTO> createCitizenVerificationDTO(CitizenVerificationDTO value) {
        return new JAXBElement<CitizenVerificationDTO>(_CitizenVerificationDTO_QNAME, CitizenVerificationDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OTCNADRADTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "OTC_NADRA_DTO")
    public JAXBElement<OTCNADRADTO> createOTCNADRADTO(OTCNADRADTO value) {
        return new JAXBElement<OTCNADRADTO>(_OTCNADRADTO_QNAME, OTCNADRADTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContextData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "ContextData")
    public JAXBElement<ContextData> createContextData(ContextData value) {
        return new JAXBElement<ContextData>(_ContextData_QNAME, ContextData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnumsREMITTANCETYPE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "Enums.REMITTANCE_TYPE")
    public JAXBElement<EnumsREMITTANCETYPE> createEnumsREMITTANCETYPE(EnumsREMITTANCETYPE value) {
        return new JAXBElement<EnumsREMITTANCETYPE>(_EnumsREMITTANCETYPE_QNAME, EnumsREMITTANCETYPE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OTCVerificationDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "OTCVerificationDTO")
    public JAXBElement<OTCVerificationDTO> createOTCVerificationDTO(OTCVerificationDTO value) {
        return new JAXBElement<OTCVerificationDTO>(_OTCVerificationDTO_QNAME, OTCVerificationDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CitizenVerificationLocalDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", name = "CitizenVerificationLocalDTO")
    public JAXBElement<CitizenVerificationLocalDTO> createCitizenVerificationLocalDTO(CitizenVerificationLocalDTO value) {
        return new JAXBElement<CitizenVerificationLocalDTO>(_CitizenVerificationLocalDTO_QNAME, CitizenVerificationLocalDTO.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfint")
    public JAXBElement<ArrayOfint> createArrayOfint(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_ArrayOfint_QNAME, ArrayOfint.class, null, value);
    }

}
