
package com.inov8.integration.channel.BOPBLB.client;

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
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="bankMnemonic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Mobile_No" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Cnic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SegmentID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RRN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AgentLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AgentCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AgentID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MachineName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IP_Address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MACAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UDID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Acknowledgedflag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "userName",
    "password",
    "bankMnemonic",
    "mobileNo",
    "cnic",
    "segmentID",
    "rrn",
    "agentLocation",
    "agentCity",
    "agentID",
    "machineName",
    "ipAddress",
    "macAddress",
    "udid",
    "acknowledgedflag"
})
@XmlRootElement(name = "ProofOfLifeInquiry")
public class ProofOfLifeInquiry
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String userName;
    protected String password;
    protected String bankMnemonic;
    @XmlElement(name = "Mobile_No")
    protected String mobileNo;
    @XmlElement(name = "Cnic")
    protected String cnic;
    @XmlElement(name = "SegmentID")
    protected String segmentID;
    @XmlElement(name = "RRN")
    protected String rrn;
    @XmlElement(name = "AgentLocation")
    protected String agentLocation;
    @XmlElement(name = "AgentCity")
    protected String agentCity;
    @XmlElement(name = "AgentID")
    protected String agentID;
    @XmlElement(name = "MachineName")
    protected String machineName;
    @XmlElement(name = "IP_Address")
    protected String ipAddress;
    @XmlElement(name = "MACAddress")
    protected String macAddress;
    @XmlElement(name = "UDID")
    protected String udid;
    @XmlElement(name = "Acknowledgedflag")
    protected String acknowledgedflag;

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the bankMnemonic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankMnemonic() {
        return bankMnemonic;
    }

    /**
     * Sets the value of the bankMnemonic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankMnemonic(String value) {
        this.bankMnemonic = value;
    }

    /**
     * Gets the value of the mobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the value of the mobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNo(String value) {
        this.mobileNo = value;
    }

    /**
     * Gets the value of the cnic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCnic() {
        return cnic;
    }

    /**
     * Sets the value of the cnic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCnic(String value) {
        this.cnic = value;
    }

    /**
     * Gets the value of the segmentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegmentID() {
        return segmentID;
    }

    /**
     * Sets the value of the segmentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegmentID(String value) {
        this.segmentID = value;
    }

    /**
     * Gets the value of the rrn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRRN() {
        return rrn;
    }

    /**
     * Sets the value of the rrn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRRN(String value) {
        this.rrn = value;
    }

    /**
     * Gets the value of the agentLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentLocation() {
        return agentLocation;
    }

    /**
     * Sets the value of the agentLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentLocation(String value) {
        this.agentLocation = value;
    }

    /**
     * Gets the value of the agentCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentCity() {
        return agentCity;
    }

    /**
     * Sets the value of the agentCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentCity(String value) {
        this.agentCity = value;
    }

    /**
     * Gets the value of the agentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentID() {
        return agentID;
    }

    /**
     * Sets the value of the agentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentID(String value) {
        this.agentID = value;
    }

    /**
     * Gets the value of the machineName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * Sets the value of the machineName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMachineName(String value) {
        this.machineName = value;
    }

    /**
     * Gets the value of the ipAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPAddress() {
        return ipAddress;
    }

    /**
     * Sets the value of the ipAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPAddress(String value) {
        this.ipAddress = value;
    }

    /**
     * Gets the value of the macAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMACAddress() {
        return macAddress;
    }

    /**
     * Sets the value of the macAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMACAddress(String value) {
        this.macAddress = value;
    }

    /**
     * Gets the value of the udid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUDID() {
        return udid;
    }

    /**
     * Sets the value of the udid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUDID(String value) {
        this.udid = value;
    }

    /**
     * Gets the value of the acknowledgedflag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcknowledgedflag() {
        return acknowledgedflag;
    }

    /**
     * Sets the value of the acknowledgedflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcknowledgedflag(String value) {
        this.acknowledgedflag = value;
    }

}
