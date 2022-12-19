
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBaseWithSession"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="agentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="parentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="stateCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="timezone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billingInvoiceEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billAcn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billAbn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="categoryId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="agentComments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="addresses" type="{http://soap.api.novatti.com/types}AddressUpdates" minOccurs="0"/&gt;
 *         &lt;element name="profileOptions" type="{http://soap.api.novatti.com/types}Options" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "agentCode",
    "agentDescription",
    "agentType",
    "parentCode",
    "countryCode",
    "stateCode",
    "timezone",
    "billingInvoiceEmail",
    "billAcn",
    "billAbn",
    "categoryId",
    "agentComments",
    "addresses",
    "profileOptions"
})
@XmlRootElement(name = "SoapAgentUpdateRequest")
public class SoapAgentUpdateRequest
    extends SoapRequestBaseWithSession
{

    protected String agentCode;
    protected String agentDescription;
    protected String agentType;
    protected String parentCode;
    protected String countryCode;
    protected String stateCode;
    protected String timezone;
    protected String billingInvoiceEmail;
    protected String billAcn;
    protected String billAbn;
    protected Integer categoryId;
    protected String agentComments;
    protected AddressUpdates addresses;
    protected Options profileOptions;

    /**
     * Gets the value of the agentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentCode() {
        return agentCode;
    }

    /**
     * Sets the value of the agentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentCode(String value) {
        this.agentCode = value;
    }

    /**
     * Gets the value of the agentDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentDescription() {
        return agentDescription;
    }

    /**
     * Sets the value of the agentDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentDescription(String value) {
        this.agentDescription = value;
    }

    /**
     * Gets the value of the agentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentType() {
        return agentType;
    }

    /**
     * Sets the value of the agentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentType(String value) {
        this.agentType = value;
    }

    /**
     * Gets the value of the parentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * Sets the value of the parentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentCode(String value) {
        this.parentCode = value;
    }

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the stateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * Sets the value of the stateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateCode(String value) {
        this.stateCode = value;
    }

    /**
     * Gets the value of the timezone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets the value of the timezone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimezone(String value) {
        this.timezone = value;
    }

    /**
     * Gets the value of the billingInvoiceEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingInvoiceEmail() {
        return billingInvoiceEmail;
    }

    /**
     * Sets the value of the billingInvoiceEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingInvoiceEmail(String value) {
        this.billingInvoiceEmail = value;
    }

    /**
     * Gets the value of the billAcn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAcn() {
        return billAcn;
    }

    /**
     * Sets the value of the billAcn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAcn(String value) {
        this.billAcn = value;
    }

    /**
     * Gets the value of the billAbn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAbn() {
        return billAbn;
    }

    /**
     * Sets the value of the billAbn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAbn(String value) {
        this.billAbn = value;
    }

    /**
     * Gets the value of the categoryId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the value of the categoryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCategoryId(Integer value) {
        this.categoryId = value;
    }

    /**
     * Gets the value of the agentComments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentComments() {
        return agentComments;
    }

    /**
     * Sets the value of the agentComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentComments(String value) {
        this.agentComments = value;
    }

    /**
     * Gets the value of the addresses property.
     * 
     * @return
     *     possible object is
     *     {@link AddressUpdates }
     *     
     */
    public AddressUpdates getAddresses() {
        return addresses;
    }

    /**
     * Sets the value of the addresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressUpdates }
     *     
     */
    public void setAddresses(AddressUpdates value) {
        this.addresses = value;
    }

    /**
     * Gets the value of the profileOptions property.
     * 
     * @return
     *     possible object is
     *     {@link Options }
     *     
     */
    public Options getProfileOptions() {
        return profileOptions;
    }

    /**
     * Sets the value of the profileOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Options }
     *     
     */
    public void setProfileOptions(Options value) {
        this.profileOptions = value;
    }

}
