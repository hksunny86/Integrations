
package com.inov8.integration.middleware.daewoo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CLIENT_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CLIENT_PASSWORD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SCHEDULE_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="QUOTA_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="QUOTA_DEPARTURE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="QUOTA_ARRIVAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "clientid",
    "clientpassword",
    "schedulecode",
    "quotadate",
    "quotadeparture",
    "quotaarrival"
})
@XmlRootElement(name = "getSeatQuota")
public class GetSeatQuota
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CLIENT_ID")
    protected String clientid;
    @XmlElement(name = "CLIENT_PASSWORD")
    protected String clientpassword;
    @XmlElement(name = "SCHEDULE_CODE")
    protected String schedulecode;
    @XmlElement(name = "QUOTA_DATE")
    protected String quotadate;
    @XmlElement(name = "QUOTA_DEPARTURE")
    protected String quotadeparture;
    @XmlElement(name = "QUOTA_ARRIVAL")
    protected String quotaarrival;

    /**
     * Gets the value of the clientid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLIENTID() {
        return clientid;
    }

    /**
     * Sets the value of the clientid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLIENTID(String value) {
        this.clientid = value;
    }

    /**
     * Gets the value of the clientpassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLIENTPASSWORD() {
        return clientpassword;
    }

    /**
     * Sets the value of the clientpassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLIENTPASSWORD(String value) {
        this.clientpassword = value;
    }

    /**
     * Gets the value of the schedulecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCHEDULECODE() {
        return schedulecode;
    }

    /**
     * Sets the value of the schedulecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCHEDULECODE(String value) {
        this.schedulecode = value;
    }

    /**
     * Gets the value of the quotadate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQUOTADATE() {
        return quotadate;
    }

    /**
     * Sets the value of the quotadate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQUOTADATE(String value) {
        this.quotadate = value;
    }

    /**
     * Gets the value of the quotadeparture property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQUOTADEPARTURE() {
        return quotadeparture;
    }

    /**
     * Sets the value of the quotadeparture property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQUOTADEPARTURE(String value) {
        this.quotadeparture = value;
    }

    /**
     * Gets the value of the quotaarrival property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQUOTAARRIVAL() {
        return quotaarrival;
    }

    /**
     * Sets the value of the quotaarrival property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQUOTAARRIVAL(String value) {
        this.quotaarrival = value;
    }

}
