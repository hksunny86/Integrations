
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingCompany complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BillingCompany"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BillingMonthLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CategoryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CompanyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CompanyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CompanyNameLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerNameLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerNumberLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerNumberLength" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerReferenceExample" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerReferenceLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Limit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PaymentRule" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}PaymentRule" minOccurs="0"/&gt;
 *         &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillingCompany", propOrder = {
    "billingMonthLabel",
    "categoryName",
    "companyId",
    "companyName",
    "companyNameLabel",
    "consumerNameLabel",
    "consumerNumberLabel",
    "consumerNumberLength",
    "consumerReferenceExample",
    "consumerReferenceLabel",
    "limit",
    "paymentRule",
    "paymentType",
    "typeId",
    "typeName"
})
public class BillingCompany
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "BillingMonthLabel", nillable = true)
    protected String billingMonthLabel;
    @XmlElement(name = "CategoryName", nillable = true)
    protected String categoryName;
    @XmlElement(name = "CompanyId", nillable = true)
    protected String companyId;
    @XmlElement(name = "CompanyName", nillable = true)
    protected String companyName;
    @XmlElement(name = "CompanyNameLabel", nillable = true)
    protected String companyNameLabel;
    @XmlElement(name = "ConsumerNameLabel", nillable = true)
    protected String consumerNameLabel;
    @XmlElement(name = "ConsumerNumberLabel", nillable = true)
    protected String consumerNumberLabel;
    @XmlElement(name = "ConsumerNumberLength", nillable = true)
    protected String consumerNumberLength;
    @XmlElement(name = "ConsumerReferenceExample", nillable = true)
    protected String consumerReferenceExample;
    @XmlElement(name = "ConsumerReferenceLabel", nillable = true)
    protected String consumerReferenceLabel;
    @XmlElement(name = "Limit", nillable = true)
    protected String limit;
    @XmlElement(name = "PaymentRule", nillable = true)
    protected PaymentRule paymentRule;
    @XmlElement(name = "PaymentType", nillable = true)
    protected String paymentType;
    @XmlElement(name = "TypeId", nillable = true)
    protected String typeId;
    @XmlElement(name = "TypeName", nillable = true)
    protected String typeName;

    /**
     * Gets the value of the billingMonthLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingMonthLabel() {
        return billingMonthLabel;
    }

    /**
     * Sets the value of the billingMonthLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingMonthLabel(String value) {
        this.billingMonthLabel = value;
    }

    /**
     * Gets the value of the categoryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the value of the categoryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryName(String value) {
        this.categoryName = value;
    }

    /**
     * Gets the value of the companyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Sets the value of the companyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyId(String value) {
        this.companyId = value;
    }

    /**
     * Gets the value of the companyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the value of the companyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyName(String value) {
        this.companyName = value;
    }

    /**
     * Gets the value of the companyNameLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyNameLabel() {
        return companyNameLabel;
    }

    /**
     * Sets the value of the companyNameLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyNameLabel(String value) {
        this.companyNameLabel = value;
    }

    /**
     * Gets the value of the consumerNameLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerNameLabel() {
        return consumerNameLabel;
    }

    /**
     * Sets the value of the consumerNameLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerNameLabel(String value) {
        this.consumerNameLabel = value;
    }

    /**
     * Gets the value of the consumerNumberLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerNumberLabel() {
        return consumerNumberLabel;
    }

    /**
     * Sets the value of the consumerNumberLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerNumberLabel(String value) {
        this.consumerNumberLabel = value;
    }

    /**
     * Gets the value of the consumerNumberLength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerNumberLength() {
        return consumerNumberLength;
    }

    /**
     * Sets the value of the consumerNumberLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerNumberLength(String value) {
        this.consumerNumberLength = value;
    }

    /**
     * Gets the value of the consumerReferenceExample property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerReferenceExample() {
        return consumerReferenceExample;
    }

    /**
     * Sets the value of the consumerReferenceExample property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerReferenceExample(String value) {
        this.consumerReferenceExample = value;
    }

    /**
     * Gets the value of the consumerReferenceLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerReferenceLabel() {
        return consumerReferenceLabel;
    }

    /**
     * Sets the value of the consumerReferenceLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerReferenceLabel(String value) {
        this.consumerReferenceLabel = value;
    }

    /**
     * Gets the value of the limit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLimit(String value) {
        this.limit = value;
    }

    /**
     * Gets the value of the paymentRule property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentRule }
     *     
     */
    public PaymentRule getPaymentRule() {
        return paymentRule;
    }

    /**
     * Sets the value of the paymentRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentRule }
     *     
     */
    public void setPaymentRule(PaymentRule value) {
        this.paymentRule = value;
    }

    /**
     * Gets the value of the paymentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * Sets the value of the paymentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentType(String value) {
        this.paymentType = value;
    }

    /**
     * Gets the value of the typeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeId(String value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the typeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of the typeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeName(String value) {
        this.typeName = value;
    }

}
