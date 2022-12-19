
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;


/**
 * <p>Java class for RequestHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="authentication" type="{http://soap.api.novatti.com/types}Authentication" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentTransactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentTerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentTimeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="languageCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestHeader", propOrder = {
    "authentication",
    "version",
    "agentVersion",
    "agentTransactionId",
    "agentTerminalId",
    "agentTimeStamp",
    "languageCode"
})
public class RequestHeader implements Serializable {

    protected Authentication authentication;
    @XmlElement(defaultValue = "1.0")
    protected String version;
    protected String agentVersion;
    protected String agentTransactionId;
    protected String agentTerminalId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar agentTimeStamp;
    protected String languageCode;

    /**
     * Gets the value of the authentication property.
     * 
     * @return
     *     possible object is
     *     {@link Authentication }
     *     
     */
    public Authentication getAuthentication() {
        return authentication;
    }

    /**
     * Sets the value of the authentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link Authentication }
     *     
     */
    public void setAuthentication(Authentication value) {
        this.authentication = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the agentVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentVersion() {
        return agentVersion;
    }

    /**
     * Sets the value of the agentVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentVersion(String value) {
        this.agentVersion = value;
    }

    /**
     * Gets the value of the agentTransactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentTransactionId() {
        return agentTransactionId;
    }

    /**
     * Sets the value of the agentTransactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentTransactionId(String value) {
        this.agentTransactionId = value;
    }

    /**
     * Gets the value of the agentTerminalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentTerminalId() {
        return agentTerminalId;
    }

    /**
     * Sets the value of the agentTerminalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentTerminalId(String value) {
        this.agentTerminalId = value;
    }

    /**
     * Gets the value of the agentTimeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAgentTimeStamp() {
        return agentTimeStamp;
    }

    /**
     * Sets the value of the agentTimeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAgentTimeStamp(XMLGregorianCalendar value) {
        this.agentTimeStamp = value;
    }

    /**
     * Gets the value of the languageCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Sets the value of the languageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguageCode(String value) {
        this.languageCode = value;
    }

}
