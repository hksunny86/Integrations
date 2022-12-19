
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
 *         &lt;element name="CLIENT_TOKEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="USER_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="USER_PASS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_TIME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_SCHEDULE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_TIMECODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_ROUTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_DEPARTURE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_ARRIVAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_DEPARTURE_SEQ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_ARRIVAL_SEQ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_FARE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_SEATS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_QTY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_GENDER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_NAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_PHONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_CNIC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_REMARK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="P_SEAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "clienttoken",
    "userid",
    "userpass",
    "pdate",
    "ptime",
    "pschedule",
    "ptimecode",
    "proute",
    "pdeparture",
    "parrival",
    "pdepartureseq",
    "parrivalseq",
    "pfare",
    "pseats",
    "pqty",
    "pgender",
    "pname",
    "pphone",
    "pcnic",
    "premark",
    "pseat"
})
@XmlRootElement(name = "getBooking")
public class GetBooking
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CLIENT_TOKEN")
    protected String clienttoken;
    @XmlElement(name = "USER_ID")
    protected String userid;
    @XmlElement(name = "USER_PASS")
    protected String userpass;
    @XmlElement(name = "P_DATE")
    protected String pdate;
    @XmlElement(name = "P_TIME")
    protected String ptime;
    @XmlElement(name = "P_SCHEDULE")
    protected String pschedule;
    @XmlElement(name = "P_TIMECODE")
    protected String ptimecode;
    @XmlElement(name = "P_ROUTE")
    protected String proute;
    @XmlElement(name = "P_DEPARTURE")
    protected String pdeparture;
    @XmlElement(name = "P_ARRIVAL")
    protected String parrival;
    @XmlElement(name = "P_DEPARTURE_SEQ")
    protected String pdepartureseq;
    @XmlElement(name = "P_ARRIVAL_SEQ")
    protected String parrivalseq;
    @XmlElement(name = "P_FARE")
    protected String pfare;
    @XmlElement(name = "P_SEATS")
    protected String pseats;
    @XmlElement(name = "P_QTY")
    protected String pqty;
    @XmlElement(name = "P_GENDER")
    protected String pgender;
    @XmlElement(name = "P_NAME")
    protected String pname;
    @XmlElement(name = "P_PHONE")
    protected String pphone;
    @XmlElement(name = "P_CNIC")
    protected String pcnic;
    @XmlElement(name = "P_REMARK")
    protected String premark;
    @XmlElement(name = "P_SEAT")
    protected String pseat;

    /**
     * Gets the value of the clienttoken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLIENTTOKEN() {
        return clienttoken;
    }

    /**
     * Sets the value of the clienttoken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLIENTTOKEN(String value) {
        this.clienttoken = value;
    }

    /**
     * Gets the value of the userid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERID() {
        return userid;
    }

    /**
     * Sets the value of the userid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERID(String value) {
        this.userid = value;
    }

    /**
     * Gets the value of the userpass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERPASS() {
        return userpass;
    }

    /**
     * Sets the value of the userpass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERPASS(String value) {
        this.userpass = value;
    }

    /**
     * Gets the value of the pdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDATE() {
        return pdate;
    }

    /**
     * Sets the value of the pdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDATE(String value) {
        this.pdate = value;
    }

    /**
     * Gets the value of the ptime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPTIME() {
        return ptime;
    }

    /**
     * Sets the value of the ptime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPTIME(String value) {
        this.ptime = value;
    }

    /**
     * Gets the value of the pschedule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPSCHEDULE() {
        return pschedule;
    }

    /**
     * Sets the value of the pschedule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPSCHEDULE(String value) {
        this.pschedule = value;
    }

    /**
     * Gets the value of the ptimecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPTIMECODE() {
        return ptimecode;
    }

    /**
     * Sets the value of the ptimecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPTIMECODE(String value) {
        this.ptimecode = value;
    }

    /**
     * Gets the value of the proute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROUTE() {
        return proute;
    }

    /**
     * Sets the value of the proute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROUTE(String value) {
        this.proute = value;
    }

    /**
     * Gets the value of the pdeparture property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDEPARTURE() {
        return pdeparture;
    }

    /**
     * Sets the value of the pdeparture property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDEPARTURE(String value) {
        this.pdeparture = value;
    }

    /**
     * Gets the value of the parrival property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPARRIVAL() {
        return parrival;
    }

    /**
     * Sets the value of the parrival property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPARRIVAL(String value) {
        this.parrival = value;
    }

    /**
     * Gets the value of the pdepartureseq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDEPARTURESEQ() {
        return pdepartureseq;
    }

    /**
     * Sets the value of the pdepartureseq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDEPARTURESEQ(String value) {
        this.pdepartureseq = value;
    }

    /**
     * Gets the value of the parrivalseq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPARRIVALSEQ() {
        return parrivalseq;
    }

    /**
     * Sets the value of the parrivalseq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPARRIVALSEQ(String value) {
        this.parrivalseq = value;
    }

    /**
     * Gets the value of the pfare property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPFARE() {
        return pfare;
    }

    /**
     * Sets the value of the pfare property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPFARE(String value) {
        this.pfare = value;
    }

    /**
     * Gets the value of the pseats property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPSEATS() {
        return pseats;
    }

    /**
     * Sets the value of the pseats property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPSEATS(String value) {
        this.pseats = value;
    }

    /**
     * Gets the value of the pqty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPQTY() {
        return pqty;
    }

    /**
     * Sets the value of the pqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPQTY(String value) {
        this.pqty = value;
    }

    /**
     * Gets the value of the pgender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPGENDER() {
        return pgender;
    }

    /**
     * Sets the value of the pgender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPGENDER(String value) {
        this.pgender = value;
    }

    /**
     * Gets the value of the pname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPNAME() {
        return pname;
    }

    /**
     * Sets the value of the pname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPNAME(String value) {
        this.pname = value;
    }

    /**
     * Gets the value of the pphone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPPHONE() {
        return pphone;
    }

    /**
     * Sets the value of the pphone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPPHONE(String value) {
        this.pphone = value;
    }

    /**
     * Gets the value of the pcnic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPCNIC() {
        return pcnic;
    }

    /**
     * Sets the value of the pcnic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPCNIC(String value) {
        this.pcnic = value;
    }

    /**
     * Gets the value of the premark property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPREMARK() {
        return premark;
    }

    /**
     * Sets the value of the premark property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPREMARK(String value) {
        this.premark = value;
    }

    /**
     * Gets the value of the pseat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPSEAT() {
        return pseat;
    }

    /**
     * Sets the value of the pseat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPSEAT(String value) {
        this.pseat = value;
    }

}
