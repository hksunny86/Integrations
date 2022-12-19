
package com.inov8.integration.middleware.prisim;

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
 *         &lt;element name="InputHeader" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}InputHeader" minOccurs="0"/&gt;
 *         &lt;element name="ChannelUserIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CustomerUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AuditLogging" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}AuditLoggingRequest" minOccurs="0"/&gt;
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
    "inputHeader",
    "channelUserIdentifier",
    "customerUserName",
    "transactionType",
    "auditLogging"
})
@XmlRootElement(name = "AuditLoggging", namespace = "http://tempuri.org/")
public class AuditLoggging
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "InputHeader", namespace = "http://tempuri.org/", nillable = true)
    protected InputHeader inputHeader;
    @XmlElement(name = "ChannelUserIdentifier", namespace = "http://tempuri.org/", nillable = true)
    protected String channelUserIdentifier;
    @XmlElement(name = "CustomerUserName", namespace = "http://tempuri.org/", nillable = true)
    protected String customerUserName;
    @XmlElement(name = "TransactionType", namespace = "http://tempuri.org/", nillable = true)
    protected String transactionType;
    @XmlElement(name = "AuditLogging", namespace = "http://tempuri.org/", nillable = true)
    protected AuditLoggingRequest auditLogging;

    /**
     * Gets the value of the inputHeader property.
     * 
     * @return
     *     possible object is
     *     {@link InputHeader }
     *     
     */
    public InputHeader getInputHeader() {
        return inputHeader;
    }

    /**
     * Sets the value of the inputHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link InputHeader }
     *     
     */
    public void setInputHeader(InputHeader value) {
        this.inputHeader = value;
    }

    /**
     * Gets the value of the channelUserIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelUserIdentifier() {
        return channelUserIdentifier;
    }

    /**
     * Sets the value of the channelUserIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelUserIdentifier(String value) {
        this.channelUserIdentifier = value;
    }

    /**
     * Gets the value of the customerUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerUserName() {
        return customerUserName;
    }

    /**
     * Sets the value of the customerUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerUserName(String value) {
        this.customerUserName = value;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionType(String value) {
        this.transactionType = value;
    }

    /**
     * Gets the value of the auditLogging property.
     * 
     * @return
     *     possible object is
     *     {@link AuditLoggingRequest }
     *     
     */
    public AuditLoggingRequest getAuditLogging() {
        return auditLogging;
    }

    /**
     * Sets the value of the auditLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuditLoggingRequest }
     *     
     */
    public void setAuditLogging(AuditLoggingRequest value) {
        this.auditLogging = value;
    }

}
