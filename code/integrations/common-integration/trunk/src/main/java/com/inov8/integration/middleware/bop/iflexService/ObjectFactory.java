
package com.inov8.integration.middleware.bop.iflexService;

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
 * generated in the com.inov8.integration.middleware.bop.iflexService package. 
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

    private final static QName _MinExclusive_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "minExclusive");
    private final static QName _MinInclusive_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "minInclusive");
    private final static QName _MaxExclusive_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "maxExclusive");
    private final static QName _MaxInclusive_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "maxInclusive");
    private final static QName _FractionDigits_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "fractionDigits");
    private final static QName _Length_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "length");
    private final static QName _MinLength_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "minLength");
    private final static QName _MaxLength_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "maxLength");
    private final static QName _Enumeration_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "enumeration");
    private final static QName _Sequence_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "sequence");
    private final static QName _Choice_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "choice");
    private final static QName _All_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "all");
    private final static QName _AnyAttribute_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "anyAttribute");
    private final static QName _Unique_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "unique");
    private final static QName _Key_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "key");
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
    private final static QName _IChannelRequest_QNAME = new QName("http://schemas.datacontract.org/2004/07/CoExistance.FlexConsumer", "IChannelRequest");
    private final static QName _DataSet_QNAME = new QName("", "DataSet");
    private final static QName _GroupTypeElement_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "element");
    private final static QName _GroupTypeGroup_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "group");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.bop.iflexService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAccountStatementResponse }
     * 
     */
    public GetAccountStatementResponse createGetAccountStatementResponse() {
        return new GetAccountStatementResponse();
    }

    /**
     * Create an instance of {@link GetGLDescriptionResponse }
     * 
     */
    public GetGLDescriptionResponse createGetGLDescriptionResponse() {
        return new GetGLDescriptionResponse();
    }

    /**
     * Create an instance of {@link Schema }
     * 
     */
    public Schema createSchema() {
        return new Schema();
    }

    /**
     * Create an instance of {@link OpenAttrs }
     * 
     */
    public OpenAttrs createOpenAttrs() {
        return new OpenAttrs();
    }

    /**
     * Create an instance of {@link Include }
     * 
     */
    public Include createInclude() {
        return new Include();
    }

    /**
     * Create an instance of {@link Annotated }
     * 
     */
    public Annotated createAnnotated() {
        return new Annotated();
    }

    /**
     * Create an instance of {@link Annotation }
     * 
     */
    public Annotation createAnnotation() {
        return new Annotation();
    }

    /**
     * Create an instance of {@link Appinfo }
     * 
     */
    public Appinfo createAppinfo() {
        return new Appinfo();
    }

    /**
     * Create an instance of {@link Documentation }
     * 
     */
    public Documentation createDocumentation() {
        return new Documentation();
    }

    /**
     * Create an instance of {@link Import }
     * 
     */
    public Import createImport() {
        return new Import();
    }

    /**
     * Create an instance of {@link Redefine }
     * 
     */
    public Redefine createRedefine() {
        return new Redefine();
    }

    /**
     * Create an instance of {@link SimpleType }
     * 
     */
    public SimpleType createSimpleType() {
        return new SimpleType();
    }

    /**
     * Create an instance of {@link Union }
     * 
     */
    public Union createUnion() {
        return new Union();
    }

    /**
     * Create an instance of {@link LocalSimpleType }
     * 
     */
    public LocalSimpleType createLocalSimpleType() {
        return new LocalSimpleType();
    }

    /**
     * Create an instance of {@link List }
     * 
     */
    public List createList() {
        return new List();
    }

    /**
     * Create an instance of {@link Restriction }
     * 
     */
    public Restriction createRestriction() {
        return new Restriction();
    }

    /**
     * Create an instance of {@link Facet }
     * 
     */
    public Facet createFacet() {
        return new Facet();
    }

    /**
     * Create an instance of {@link TotalDigits }
     * 
     */
    public TotalDigits createTotalDigits() {
        return new TotalDigits();
    }

    /**
     * Create an instance of {@link NumFacet }
     * 
     */
    public NumFacet createNumFacet() {
        return new NumFacet();
    }

    /**
     * Create an instance of {@link NoFixedFacet }
     * 
     */
    public NoFixedFacet createNoFixedFacet() {
        return new NoFixedFacet();
    }

    /**
     * Create an instance of {@link WhiteSpace }
     * 
     */
    public WhiteSpace createWhiteSpace() {
        return new WhiteSpace();
    }

    /**
     * Create an instance of {@link Pattern }
     * 
     */
    public Pattern createPattern() {
        return new Pattern();
    }

    /**
     * Create an instance of {@link ComplexType }
     * 
     */
    public ComplexType createComplexType() {
        return new ComplexType();
    }

    /**
     * Create an instance of {@link ExplicitGroup }
     * 
     */
    public ExplicitGroup createExplicitGroup() {
        return new ExplicitGroup();
    }

    /**
     * Create an instance of {@link All }
     * 
     */
    public All createAll() {
        return new All();
    }

    /**
     * Create an instance of {@link GroupRef }
     * 
     */
    public GroupRef createGroupRef() {
        return new GroupRef();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link AttributeGroupRef }
     * 
     */
    public AttributeGroupRef createAttributeGroupRef() {
        return new AttributeGroupRef();
    }

    /**
     * Create an instance of {@link Wildcard }
     * 
     */
    public Wildcard createWildcard() {
        return new Wildcard();
    }

    /**
     * Create an instance of {@link ComplexContent }
     * 
     */
    public ComplexContent createComplexContent() {
        return new ComplexContent();
    }

    /**
     * Create an instance of {@link ExtensionType }
     * 
     */
    public ExtensionType createExtensionType() {
        return new ExtensionType();
    }

    /**
     * Create an instance of {@link ComplexRestrictionType }
     * 
     */
    public ComplexRestrictionType createComplexRestrictionType() {
        return new ComplexRestrictionType();
    }

    /**
     * Create an instance of {@link SimpleContent }
     * 
     */
    public SimpleContent createSimpleContent() {
        return new SimpleContent();
    }

    /**
     * Create an instance of {@link SimpleExtensionType }
     * 
     */
    public SimpleExtensionType createSimpleExtensionType() {
        return new SimpleExtensionType();
    }

    /**
     * Create an instance of {@link SimpleRestrictionType }
     * 
     */
    public SimpleRestrictionType createSimpleRestrictionType() {
        return new SimpleRestrictionType();
    }

    /**
     * Create an instance of {@link Group }
     * 
     */
    public Group createGroup() {
        return new Group();
    }

    /**
     * Create an instance of {@link RealGroup }
     * 
     */
    public RealGroup createRealGroup() {
        return new RealGroup();
    }

    /**
     * Create an instance of {@link LocalElement }
     * 
     */
    public LocalElement createLocalElement() {
        return new LocalElement();
    }

    /**
     * Create an instance of {@link Any }
     * 
     */
    public Any createAny() {
        return new Any();
    }

    /**
     * Create an instance of {@link AttributeGroup }
     * 
     */
    public AttributeGroup createAttributeGroup() {
        return new AttributeGroup();
    }

    /**
     * Create an instance of {@link Element }
     * 
     */
    public Element createElement() {
        return new Element();
    }

    /**
     * Create an instance of {@link LocalComplexType }
     * 
     */
    public LocalComplexType createLocalComplexType() {
        return new LocalComplexType();
    }

    /**
     * Create an instance of {@link Keybase }
     * 
     */
    public Keybase createKeybase() {
        return new Keybase();
    }

    /**
     * Create an instance of {@link Keyref }
     * 
     */
    public Keyref createKeyref() {
        return new Keyref();
    }

    /**
     * Create an instance of {@link Selector }
     * 
     */
    public Selector createSelector() {
        return new Selector();
    }

    /**
     * Create an instance of {@link Field }
     * 
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link Notation }
     * 
     */
    public Notation createNotation() {
        return new Notation();
    }

    /**
     * Create an instance of {@link RestrictionType }
     * 
     */
    public RestrictionType createRestrictionType() {
        return new RestrictionType();
    }

    /**
     * Create an instance of {@link SimpleExplicitGroup }
     * 
     */
    public SimpleExplicitGroup createSimpleExplicitGroup() {
        return new SimpleExplicitGroup();
    }

    /**
     * Create an instance of {@link NarrowMaxMin }
     * 
     */
    public NarrowMaxMin createNarrowMaxMin() {
        return new NarrowMaxMin();
    }

    /**
     * Create an instance of {@link GetData }
     * 
     */
    public GetData createGetData() {
        return new GetData();
    }

    /**
     * Create an instance of {@link IChannelRequest }
     * 
     */
    public IChannelRequest createIChannelRequest() {
        return new IChannelRequest();
    }

    /**
     * Create an instance of {@link GetDataResponse }
     * 
     */
    public GetDataResponse createGetDataResponse() {
        return new GetDataResponse();
    }

    /**
     * Create an instance of {@link GetAccountMapping }
     * 
     */
    public GetAccountMapping createGetAccountMapping() {
        return new GetAccountMapping();
    }

    /**
     * Create an instance of {@link GetAccountMappingResponse }
     * 
     */
    public GetAccountMappingResponse createGetAccountMappingResponse() {
        return new GetAccountMappingResponse();
    }

    /**
     * Create an instance of {@link ChannelActivation }
     * 
     */
    public ChannelActivation createChannelActivation() {
        return new ChannelActivation();
    }

    /**
     * Create an instance of {@link ChannelActivationResponse }
     * 
     */
    public ChannelActivationResponse createChannelActivationResponse() {
        return new ChannelActivationResponse();
    }

    /**
     * Create an instance of {@link GetAccountStatement }
     * 
     */
    public GetAccountStatement createGetAccountStatement() {
        return new GetAccountStatement();
    }

    /**
     * Create an instance of {@link GetAccountStatementResponse.GetAccountStatementResult }
     * 
     */
    public GetAccountStatementResponse.GetAccountStatementResult createGetAccountStatementResponseGetAccountStatementResult() {
        return new GetAccountStatementResponse.GetAccountStatementResult();
    }

    /**
     * Create an instance of {@link GetGLDescription }
     * 
     */
    public GetGLDescription createGetGLDescription() {
        return new GetGLDescription();
    }

    /**
     * Create an instance of {@link GetGLDescriptionResponse.GetGLDescriptionResult }
     * 
     */
    public GetGLDescriptionResponse.GetGLDescriptionResult createGetGLDescriptionResponseGetGLDescriptionResult() {
        return new GetGLDescriptionResponse.GetGLDescriptionResult();
    }

    /**
     * Create an instance of {@link IsFiler }
     * 
     */
    public IsFiler createIsFiler() {
        return new IsFiler();
    }

    /**
     * Create an instance of {@link IsFilerResponse }
     * 
     */
    public IsFilerResponse createIsFilerResponse() {
        return new IsFilerResponse();
    }

    /**
     * Create an instance of {@link DataSet }
     * 
     */
    public DataSet createDataSet() {
        return new DataSet();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Facet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "minExclusive")
    public JAXBElement<Facet> createMinExclusive(Facet value) {
        return new JAXBElement<Facet>(_MinExclusive_QNAME, Facet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Facet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "minInclusive")
    public JAXBElement<Facet> createMinInclusive(Facet value) {
        return new JAXBElement<Facet>(_MinInclusive_QNAME, Facet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Facet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "maxExclusive")
    public JAXBElement<Facet> createMaxExclusive(Facet value) {
        return new JAXBElement<Facet>(_MaxExclusive_QNAME, Facet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Facet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "maxInclusive")
    public JAXBElement<Facet> createMaxInclusive(Facet value) {
        return new JAXBElement<Facet>(_MaxInclusive_QNAME, Facet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NumFacet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "fractionDigits")
    public JAXBElement<NumFacet> createFractionDigits(NumFacet value) {
        return new JAXBElement<NumFacet>(_FractionDigits_QNAME, NumFacet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NumFacet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "length")
    public JAXBElement<NumFacet> createLength(NumFacet value) {
        return new JAXBElement<NumFacet>(_Length_QNAME, NumFacet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NumFacet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "minLength")
    public JAXBElement<NumFacet> createMinLength(NumFacet value) {
        return new JAXBElement<NumFacet>(_MinLength_QNAME, NumFacet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NumFacet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "maxLength")
    public JAXBElement<NumFacet> createMaxLength(NumFacet value) {
        return new JAXBElement<NumFacet>(_MaxLength_QNAME, NumFacet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoFixedFacet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "enumeration")
    public JAXBElement<NoFixedFacet> createEnumeration(NoFixedFacet value) {
        return new JAXBElement<NoFixedFacet>(_Enumeration_QNAME, NoFixedFacet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExplicitGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "sequence")
    public JAXBElement<ExplicitGroup> createSequence(ExplicitGroup value) {
        return new JAXBElement<ExplicitGroup>(_Sequence_QNAME, ExplicitGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExplicitGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "choice")
    public JAXBElement<ExplicitGroup> createChoice(ExplicitGroup value) {
        return new JAXBElement<ExplicitGroup>(_Choice_QNAME, ExplicitGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link All }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "all")
    public JAXBElement<All> createAll(All value) {
        return new JAXBElement<All>(_All_QNAME, All.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Wildcard }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "anyAttribute")
    public JAXBElement<Wildcard> createAnyAttribute(Wildcard value) {
        return new JAXBElement<Wildcard>(_AnyAttribute_QNAME, Wildcard.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Keybase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "unique")
    public JAXBElement<Keybase> createUnique(Keybase value) {
        return new JAXBElement<Keybase>(_Unique_QNAME, Keybase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Keybase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "key")
    public JAXBElement<Keybase> createKey(Keybase value) {
        return new JAXBElement<Keybase>(_Key_QNAME, Keybase.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link IChannelRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/CoExistance.FlexConsumer", name = "IChannelRequest")
    public JAXBElement<IChannelRequest> createIChannelRequest(IChannelRequest value) {
        return new JAXBElement<IChannelRequest>(_IChannelRequest_QNAME, IChannelRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DataSet")
    public JAXBElement<DataSet> createDataSet(DataSet value) {
        return new JAXBElement<DataSet>(_DataSet_QNAME, DataSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LocalElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "element", scope = GroupType.class)
    public JAXBElement<LocalElement> createGroupTypeElement(LocalElement value) {
        return new JAXBElement<LocalElement>(_GroupTypeElement_QNAME, LocalElement.class, GroupType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GroupRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "group", scope = GroupType.class)
    public JAXBElement<GroupRef> createGroupTypeGroup(GroupRef value) {
        return new JAXBElement<GroupRef>(_GroupTypeGroup_QNAME, GroupRef.class, GroupType.class, value);
    }

}
