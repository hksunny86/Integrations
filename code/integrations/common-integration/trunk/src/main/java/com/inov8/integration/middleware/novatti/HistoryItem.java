
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for HistoryItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HistoryItem"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="inTransNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="inTransRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyValues" type="{http://soap.api.novatti.com/types}Map" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HistoryItem", propOrder = {
    "inTransNo",
    "inTransRef",
    "type",
    "description",
    "amount",
    "dateTime",
    "keyValues"
})
public class HistoryItem implements Serializable {

    protected String inTransNo;
    protected String inTransRef;
    protected String type;
    protected String description;
    protected String amount;
    protected String dateTime;
    protected Map keyValues;

    /**
     * Gets the value of the inTransNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInTransNo() {
        return inTransNo;
    }

    /**
     * Sets the value of the inTransNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInTransNo(String value) {
        this.inTransNo = value;
    }

    /**
     * Gets the value of the inTransRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInTransRef() {
        return inTransRef;
    }

    /**
     * Sets the value of the inTransRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInTransRef(String value) {
        this.inTransRef = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
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
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the dateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of the dateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTime(String value) {
        this.dateTime = value;
    }

    /**
     * Gets the value of the keyValues property.
     * 
     * @return
     *     possible object is
     *     {@link Map }
     *     
     */
    public Map getKeyValues() {
        return keyValues;
    }

    /**
     * Sets the value of the keyValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link Map }
     *     
     */
    public void setKeyValues(Map value) {
        this.keyValues = value;
    }

}
