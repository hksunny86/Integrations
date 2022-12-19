
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for AddressUpdate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressUpdate"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="addressTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contactName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="addressLine1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="addressLine2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="postCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="stateCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fax1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fax2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="phone1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="phone2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mobile1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mobile2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="gps" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressUpdate", propOrder = {
    "addressTypeCode",
    "contactName",
    "addressLine1",
    "addressLine2",
    "city",
    "postCode",
    "stateCode",
    "countryCode",
    "fax1",
    "fax2",
    "phone1",
    "phone2",
    "mobile1",
    "mobile2",
    "email",
    "url",
    "gps"
})
public class AddressUpdate implements Serializable {

    protected String addressTypeCode;
    protected String contactName;
    protected String addressLine1;
    protected String addressLine2;
    protected String city;
    protected String postCode;
    protected String stateCode;
    protected String countryCode;
    protected String fax1;
    protected String fax2;
    protected String phone1;
    protected String phone2;
    protected String mobile1;
    protected String mobile2;
    protected String email;
    protected String url;
    protected String gps;

    /**
     * Gets the value of the addressTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressTypeCode() {
        return addressTypeCode;
    }

    /**
     * Sets the value of the addressTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressTypeCode(String value) {
        this.addressTypeCode = value;
    }

    /**
     * Gets the value of the contactName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the value of the contactName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactName(String value) {
        this.contactName = value;
    }

    /**
     * Gets the value of the addressLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Sets the value of the addressLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine1(String value) {
        this.addressLine1 = value;
    }

    /**
     * Gets the value of the addressLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * Sets the value of the addressLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine2(String value) {
        this.addressLine2 = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the postCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Sets the value of the postCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostCode(String value) {
        this.postCode = value;
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
     * Gets the value of the fax1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax1() {
        return fax1;
    }

    /**
     * Sets the value of the fax1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax1(String value) {
        this.fax1 = value;
    }

    /**
     * Gets the value of the fax2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax2() {
        return fax2;
    }

    /**
     * Sets the value of the fax2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax2(String value) {
        this.fax2 = value;
    }

    /**
     * Gets the value of the phone1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone1() {
        return phone1;
    }

    /**
     * Sets the value of the phone1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone1(String value) {
        this.phone1 = value;
    }

    /**
     * Gets the value of the phone2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone2() {
        return phone2;
    }

    /**
     * Sets the value of the phone2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone2(String value) {
        this.phone2 = value;
    }

    /**
     * Gets the value of the mobile1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobile1() {
        return mobile1;
    }

    /**
     * Sets the value of the mobile1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobile1(String value) {
        this.mobile1 = value;
    }

    /**
     * Gets the value of the mobile2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobile2() {
        return mobile2;
    }

    /**
     * Sets the value of the mobile2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobile2(String value) {
        this.mobile2 = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the gps property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGps() {
        return gps;
    }

    /**
     * Sets the value of the gps property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGps(String value) {
        this.gps = value;
    }

}
