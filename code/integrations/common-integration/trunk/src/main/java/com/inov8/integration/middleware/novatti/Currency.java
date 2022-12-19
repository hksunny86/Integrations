
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for Currency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Currency"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="major" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="majors" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="minor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="minors" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="units" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="leftsign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rightsign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Currency", propOrder = {
    "id",
    "currencyCode",
    "description",
    "major",
    "majors",
    "minor",
    "minors",
    "units",
    "leftsign",
    "rightsign"
})
public class Currency implements Serializable {

    protected Integer id;
    protected String currencyCode;
    protected String description;
    protected String major;
    protected String majors;
    protected String minor;
    protected String minors;
    protected Integer units;
    protected String leftsign;
    protected String rightsign;

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
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
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
     * Gets the value of the major property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMajor() {
        return major;
    }

    /**
     * Sets the value of the major property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMajor(String value) {
        this.major = value;
    }

    /**
     * Gets the value of the majors property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMajors() {
        return majors;
    }

    /**
     * Sets the value of the majors property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMajors(String value) {
        this.majors = value;
    }

    /**
     * Gets the value of the minor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinor() {
        return minor;
    }

    /**
     * Sets the value of the minor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinor(String value) {
        this.minor = value;
    }

    /**
     * Gets the value of the minors property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinors() {
        return minors;
    }

    /**
     * Sets the value of the minors property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinors(String value) {
        this.minors = value;
    }

    /**
     * Gets the value of the units property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUnits() {
        return units;
    }

    /**
     * Sets the value of the units property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUnits(Integer value) {
        this.units = value;
    }

    /**
     * Gets the value of the leftsign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeftsign() {
        return leftsign;
    }

    /**
     * Sets the value of the leftsign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeftsign(String value) {
        this.leftsign = value;
    }

    /**
     * Gets the value of the rightsign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRightsign() {
        return rightsign;
    }

    /**
     * Sets the value of the rightsign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRightsign(String value) {
        this.rightsign = value;
    }

}
