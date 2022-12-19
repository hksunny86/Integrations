
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingCompanyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BillingCompanyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PaymentCompanyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PaymentCompanyTypeLable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PaymentTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PaymentTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillingCompanyType", propOrder = {
    "paymentCompanyId",
    "paymentCompanyTypeLable",
    "paymentTypeId",
    "paymentTypeName"
})
public class BillingCompanyType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "PaymentCompanyId", nillable = true)
    protected String paymentCompanyId;
    @XmlElement(name = "PaymentCompanyTypeLable", nillable = true)
    protected String paymentCompanyTypeLable;
    @XmlElement(name = "PaymentTypeId", nillable = true)
    protected String paymentTypeId;
    @XmlElement(name = "PaymentTypeName", nillable = true)
    protected String paymentTypeName;

    /**
     * Gets the value of the paymentCompanyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentCompanyId() {
        return paymentCompanyId;
    }

    /**
     * Sets the value of the paymentCompanyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentCompanyId(String value) {
        this.paymentCompanyId = value;
    }

    /**
     * Gets the value of the paymentCompanyTypeLable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentCompanyTypeLable() {
        return paymentCompanyTypeLable;
    }

    /**
     * Sets the value of the paymentCompanyTypeLable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentCompanyTypeLable(String value) {
        this.paymentCompanyTypeLable = value;
    }

    /**
     * Gets the value of the paymentTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    /**
     * Sets the value of the paymentTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentTypeId(String value) {
        this.paymentTypeId = value;
    }

    /**
     * Gets the value of the paymentTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    /**
     * Sets the value of the paymentTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentTypeName(String value) {
        this.paymentTypeName = value;
    }

}
