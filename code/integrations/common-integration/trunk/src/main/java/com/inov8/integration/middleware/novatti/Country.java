
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for Country complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Country"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="iso2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="iso3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="currencyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dialcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Country", propOrder = {
    "id",
    "iso2",
    "iso3",
    "description",
    "currencyId",
    "dialcode"
})
public class Country implements Serializable {

    protected Integer id;
    protected String iso2;
    protected String iso3;
    protected String description;
    protected String currencyId;
    protected String dialcode;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the iso2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIso2() {
        return iso2;
    }

    /**
     * Sets the value of the iso2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIso2(String value) {
        this.iso2 = value;
    }

    /**
     * Gets the value of the iso3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIso3() {
        return iso3;
    }

    /**
     * Sets the value of the iso3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIso3(String value) {
        this.iso3 = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the currencyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Sets the value of the currencyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyId(String value) {
        this.currencyId = value;
    }

    /**
     * Gets the value of the dialcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDialcode() {
        return dialcode;
    }

    /**
     * Sets the value of the dialcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDialcode(String value) {
        this.dialcode = value;
    }

}
