
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
 *         &lt;element name="DEPARTURE_SEQ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ARRIVAL_SEQ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DATEA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DATEB" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "departureseq",
    "arrivalseq",
    "datea",
    "dateb"
})
@XmlRootElement(name = "getSeats")
public class GetSeats
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CLIENT_ID")
    protected String clientid;
    @XmlElement(name = "CLIENT_PASSWORD")
    protected String clientpassword;
    @XmlElement(name = "SCHEDULE_CODE")
    protected String schedulecode;
    @XmlElement(name = "DEPARTURE_SEQ")
    protected String departureseq;
    @XmlElement(name = "ARRIVAL_SEQ")
    protected String arrivalseq;
    @XmlElement(name = "DATEA")
    protected String datea;
    @XmlElement(name = "DATEB")
    protected String dateb;

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
     * Gets the value of the departureseq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPARTURESEQ() {
        return departureseq;
    }

    /**
     * Sets the value of the departureseq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPARTURESEQ(String value) {
        this.departureseq = value;
    }

    /**
     * Gets the value of the arrivalseq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getARRIVALSEQ() {
        return arrivalseq;
    }

    /**
     * Sets the value of the arrivalseq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setARRIVALSEQ(String value) {
        this.arrivalseq = value;
    }

    /**
     * Gets the value of the datea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATEA() {
        return datea;
    }

    /**
     * Sets the value of the datea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATEA(String value) {
        this.datea = value;
    }

    /**
     * Gets the value of the dateb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATEB() {
        return dateb;
    }

    /**
     * Sets the value of the dateb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATEB(String value) {
        this.dateb = value;
    }

}
