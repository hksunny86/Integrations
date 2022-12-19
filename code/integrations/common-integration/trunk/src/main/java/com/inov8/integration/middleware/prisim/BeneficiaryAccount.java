
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BeneficiaryAccount complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BeneficiaryAccount"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BeneficiaryAccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryAccountTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryBankIMD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryBankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryEmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryMobileNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryNickName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryRelationshipId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryRelationshipwithCustomer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BeneficiaryAccount", propOrder = {
    "beneficiaryAccountNumber",
    "beneficiaryAccountTitle",
    "beneficiaryBankIMD",
    "beneficiaryBankName",
    "beneficiaryEmailAddress",
    "beneficiaryMobileNumber",
    "beneficiaryNickName",
    "beneficiaryReferenceNumber",
    "beneficiaryRelationshipId",
    "beneficiaryRelationshipwithCustomer",
    "beneficiaryType"
})
public class BeneficiaryAccount
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "BeneficiaryAccountNumber", nillable = true)
    protected String beneficiaryAccountNumber;
    @XmlElement(name = "BeneficiaryAccountTitle", nillable = true)
    protected String beneficiaryAccountTitle;
    @XmlElement(name = "BeneficiaryBankIMD", nillable = true)
    protected String beneficiaryBankIMD;
    @XmlElement(name = "BeneficiaryBankName", nillable = true)
    protected String beneficiaryBankName;
    @XmlElement(name = "BeneficiaryEmailAddress", nillable = true)
    protected String beneficiaryEmailAddress;
    @XmlElement(name = "BeneficiaryMobileNumber", nillable = true)
    protected String beneficiaryMobileNumber;
    @XmlElement(name = "BeneficiaryNickName", nillable = true)
    protected String beneficiaryNickName;
    @XmlElement(name = "BeneficiaryReferenceNumber", nillable = true)
    protected String beneficiaryReferenceNumber;
    @XmlElement(name = "BeneficiaryRelationshipId", nillable = true)
    protected String beneficiaryRelationshipId;
    @XmlElement(name = "BeneficiaryRelationshipwithCustomer", nillable = true)
    protected String beneficiaryRelationshipwithCustomer;
    @XmlElement(name = "BeneficiaryType", nillable = true)
    protected String beneficiaryType;

    /**
     * Gets the value of the beneficiaryAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    /**
     * Sets the value of the beneficiaryAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryAccountNumber(String value) {
        this.beneficiaryAccountNumber = value;
    }

    /**
     * Gets the value of the beneficiaryAccountTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryAccountTitle() {
        return beneficiaryAccountTitle;
    }

    /**
     * Sets the value of the beneficiaryAccountTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryAccountTitle(String value) {
        this.beneficiaryAccountTitle = value;
    }

    /**
     * Gets the value of the beneficiaryBankIMD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryBankIMD() {
        return beneficiaryBankIMD;
    }

    /**
     * Sets the value of the beneficiaryBankIMD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryBankIMD(String value) {
        this.beneficiaryBankIMD = value;
    }

    /**
     * Gets the value of the beneficiaryBankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    /**
     * Sets the value of the beneficiaryBankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryBankName(String value) {
        this.beneficiaryBankName = value;
    }

    /**
     * Gets the value of the beneficiaryEmailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryEmailAddress() {
        return beneficiaryEmailAddress;
    }

    /**
     * Sets the value of the beneficiaryEmailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryEmailAddress(String value) {
        this.beneficiaryEmailAddress = value;
    }

    /**
     * Gets the value of the beneficiaryMobileNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryMobileNumber() {
        return beneficiaryMobileNumber;
    }

    /**
     * Sets the value of the beneficiaryMobileNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryMobileNumber(String value) {
        this.beneficiaryMobileNumber = value;
    }

    /**
     * Gets the value of the beneficiaryNickName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryNickName() {
        return beneficiaryNickName;
    }

    /**
     * Sets the value of the beneficiaryNickName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryNickName(String value) {
        this.beneficiaryNickName = value;
    }

    /**
     * Gets the value of the beneficiaryReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryReferenceNumber() {
        return beneficiaryReferenceNumber;
    }

    /**
     * Sets the value of the beneficiaryReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryReferenceNumber(String value) {
        this.beneficiaryReferenceNumber = value;
    }

    /**
     * Gets the value of the beneficiaryRelationshipId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryRelationshipId() {
        return beneficiaryRelationshipId;
    }

    /**
     * Sets the value of the beneficiaryRelationshipId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryRelationshipId(String value) {
        this.beneficiaryRelationshipId = value;
    }

    /**
     * Gets the value of the beneficiaryRelationshipwithCustomer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryRelationshipwithCustomer() {
        return beneficiaryRelationshipwithCustomer;
    }

    /**
     * Sets the value of the beneficiaryRelationshipwithCustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryRelationshipwithCustomer(String value) {
        this.beneficiaryRelationshipwithCustomer = value;
    }

    /**
     * Gets the value of the beneficiaryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    /**
     * Sets the value of the beneficiaryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryType(String value) {
        this.beneficiaryType = value;
    }

}
