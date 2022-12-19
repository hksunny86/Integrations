
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBaseWithSession"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="agentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="walletTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="destinationAgentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="destinationTerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="destinationAgentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="reasonId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sourceMsisdn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="destinationMsisdn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "agentId",
    "agentCode",
    "walletTypeId",
    "destinationAgentId",
    "destinationTerminalId",
    "destinationAgentCode",
    "user",
    "password",
    "reasonId",
    "comments",
    "sourceMsisdn",
    "destinationMsisdn",
    "value"
})
@XmlRootElement(name = "SoapAgentWalletTransferRequest")
public class SoapAgentWalletTransferRequest
    extends SoapRequestBaseWithSession
{

    protected String agentId;
    protected String agentCode;
    protected String walletTypeId;
    protected String destinationAgentId;
    protected String destinationTerminalId;
    protected String destinationAgentCode;
    protected String user;
    protected String password;
    protected String reasonId;
    protected String comments;
    protected String sourceMsisdn;
    protected String destinationMsisdn;
    protected Double value;

    /**
     * Gets the value of the agentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentId() {
        return agentId;
    }

    /**
     * Sets the value of the agentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentId(String value) {
        this.agentId = value;
    }

    /**
     * Gets the value of the agentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentCode() {
        return agentCode;
    }

    /**
     * Sets the value of the agentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentCode(String value) {
        this.agentCode = value;
    }

    /**
     * Gets the value of the walletTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWalletTypeId() {
        return walletTypeId;
    }

    /**
     * Sets the value of the walletTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWalletTypeId(String value) {
        this.walletTypeId = value;
    }

    /**
     * Gets the value of the destinationAgentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationAgentId() {
        return destinationAgentId;
    }

    /**
     * Sets the value of the destinationAgentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationAgentId(String value) {
        this.destinationAgentId = value;
    }

    /**
     * Gets the value of the destinationTerminalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationTerminalId() {
        return destinationTerminalId;
    }

    /**
     * Sets the value of the destinationTerminalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationTerminalId(String value) {
        this.destinationTerminalId = value;
    }

    /**
     * Gets the value of the destinationAgentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationAgentCode() {
        return destinationAgentCode;
    }

    /**
     * Sets the value of the destinationAgentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationAgentCode(String value) {
        this.destinationAgentCode = value;
    }

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
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
     * Gets the value of the reasonId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonId() {
        return reasonId;
    }

    /**
     * Sets the value of the reasonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonId(String value) {
        this.reasonId = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    /**
     * Gets the value of the sourceMsisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceMsisdn() {
        return sourceMsisdn;
    }

    /**
     * Sets the value of the sourceMsisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceMsisdn(String value) {
        this.sourceMsisdn = value;
    }

    /**
     * Gets the value of the destinationMsisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationMsisdn() {
        return destinationMsisdn;
    }

    /**
     * Sets the value of the destinationMsisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationMsisdn(String value) {
        this.destinationMsisdn = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValue(Double value) {
        this.value = value;
    }

}
