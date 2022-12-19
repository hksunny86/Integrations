
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
 *         &lt;element name="DEPARTURE_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DEPARTURE_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ARRIVAL_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "departuredate",
    "departureid",
    "arrivalid"
})
@XmlRootElement(name = "getRoutes")
public class GetRoutes
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CLIENT_ID")
    protected String clientid;
    @XmlElement(name = "CLIENT_PASSWORD")
    protected String clientpassword;
    @XmlElement(name = "DEPARTURE_DATE")
    protected String departuredate;
    @XmlElement(name = "DEPARTURE_ID")
    protected String departureid;
    @XmlElement(name = "ARRIVAL_ID")
    protected String arrivalid;

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
     * Gets the value of the departuredate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPARTUREDATE() {
        return departuredate;
    }

    /**
     * Sets the value of the departuredate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPARTUREDATE(String value) {
        this.departuredate = value;
    }

    /**
     * Gets the value of the departureid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPARTUREID() {
        return departureid;
    }

    /**
     * Sets the value of the departureid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPARTUREID(String value) {
        this.departureid = value;
    }

    /**
     * Gets the value of the arrivalid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getARRIVALID() {
        return arrivalid;
    }

    /**
     * Sets the value of the arrivalid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setARRIVALID(String value) {
        this.arrivalid = value;
    }

}
