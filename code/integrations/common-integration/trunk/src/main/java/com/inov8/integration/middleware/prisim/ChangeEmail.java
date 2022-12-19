
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
 *         &lt;element name="SecretQuestion" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}SecretQuestion" minOccurs="0"/&gt;
 *         &lt;element name="SecretAnswer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OldEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NewEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "secretQuestion",
    "secretAnswer",
    "oldEmail",
    "newEmail"
})
@XmlRootElement(name = "ChangeEmail", namespace = "http://tempuri.org/")
public class ChangeEmail
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "InputHeader", namespace = "http://tempuri.org/", nillable = true)
    protected InputHeader inputHeader;
    @XmlElement(name = "ChannelUserIdentifier", namespace = "http://tempuri.org/", nillable = true)
    protected String channelUserIdentifier;
    @XmlElement(name = "CustomerUserName", namespace = "http://tempuri.org/", nillable = true)
    protected String customerUserName;
    @XmlElement(name = "SecretQuestion", namespace = "http://tempuri.org/", nillable = true)
    protected SecretQuestion secretQuestion;
    @XmlElement(name = "SecretAnswer", namespace = "http://tempuri.org/", nillable = true)
    protected String secretAnswer;
    @XmlElement(name = "OldEmail", namespace = "http://tempuri.org/", nillable = true)
    protected String oldEmail;
    @XmlElement(name = "NewEmail", namespace = "http://tempuri.org/", nillable = true)
    protected String newEmail;

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
     * Gets the value of the secretQuestion property.
     * 
     * @return
     *     possible object is
     *     {@link SecretQuestion }
     *     
     */
    public SecretQuestion getSecretQuestion() {
        return secretQuestion;
    }

    /**
     * Sets the value of the secretQuestion property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretQuestion }
     *     
     */
    public void setSecretQuestion(SecretQuestion value) {
        this.secretQuestion = value;
    }

    /**
     * Gets the value of the secretAnswer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretAnswer() {
        return secretAnswer;
    }

    /**
     * Sets the value of the secretAnswer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretAnswer(String value) {
        this.secretAnswer = value;
    }

    /**
     * Gets the value of the oldEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldEmail() {
        return oldEmail;
    }

    /**
     * Sets the value of the oldEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldEmail(String value) {
        this.oldEmail = value;
    }

    /**
     * Gets the value of the newEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewEmail() {
        return newEmail;
    }

    /**
     * Sets the value of the newEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewEmail(String value) {
        this.newEmail = value;
    }

}
