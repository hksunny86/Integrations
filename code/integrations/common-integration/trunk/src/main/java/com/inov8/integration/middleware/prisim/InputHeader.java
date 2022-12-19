
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InputHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InputHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ChannelRelationshipId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HashValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IpAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MessageVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MobileIpAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RetrievalReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InputHeader", propOrder = {
    "channelRelationshipId",
    "hashValue",
    "ipAddress",
    "messageVersion",
    "mobileIpAddress",
    "password",
    "retrievalReferenceNumber",
    "transactionCode",
    "transactionDateTime",
    "username"
})
public class InputHeader
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ChannelRelationshipId", nillable = true)
    protected String channelRelationshipId;
    @XmlElement(name = "HashValue", nillable = true)
    protected String hashValue;
    @XmlElement(name = "IpAddress", nillable = true)
    protected String ipAddress;
    @XmlElement(name = "MessageVersion", nillable = true)
    protected String messageVersion;
    @XmlElement(name = "MobileIpAddress", nillable = true)
    protected String mobileIpAddress;
    @XmlElement(name = "Password", nillable = true)
    protected String password;
    @XmlElement(name = "RetrievalReferenceNumber", nillable = true)
    protected String retrievalReferenceNumber;
    @XmlElement(name = "TransactionCode", nillable = true)
    protected String transactionCode;
    @XmlElement(name = "TransactionDateTime", nillable = true)
    protected String transactionDateTime;
    @XmlElement(name = "Username", nillable = true)
    protected String username;

    /**
     * Gets the value of the channelRelationshipId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelRelationshipId() {
        return channelRelationshipId;
    }

    /**
     * Sets the value of the channelRelationshipId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelRelationshipId(String value) {
        this.channelRelationshipId = value;
    }

    /**
     * Gets the value of the hashValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashValue() {
        return hashValue;
    }

    /**
     * Sets the value of the hashValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashValue(String value) {
        this.hashValue = value;
    }

    /**
     * Gets the value of the ipAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAddress() {
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
    public void setIpAddress(String value) {
        this.ipAddress = value;
    }

    /**
     * Gets the value of the messageVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageVersion() {
        return messageVersion;
    }

    /**
     * Sets the value of the messageVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageVersion(String value) {
        this.messageVersion = value;
    }

    /**
     * Gets the value of the mobileIpAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileIpAddress() {
        return mobileIpAddress;
    }

    /**
     * Sets the value of the mobileIpAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileIpAddress(String value) {
        this.mobileIpAddress = value;
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
     * Gets the value of the retrievalReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    /**
     * Sets the value of the retrievalReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetrievalReferenceNumber(String value) {
        this.retrievalReferenceNumber = value;
    }

    /**
     * Gets the value of the transactionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionCode() {
        return transactionCode;
    }

    /**
     * Sets the value of the transactionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionCode(String value) {
        this.transactionCode = value;
    }

    /**
     * Gets the value of the transactionDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    /**
     * Sets the value of the transactionDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionDateTime(String value) {
        this.transactionDateTime = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

}
