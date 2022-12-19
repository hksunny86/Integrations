
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
 *         &lt;element name="NewSecretQuestion" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}SecretQuestion" minOccurs="0"/&gt;
 *         &lt;element name="NewSecretAnswer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MobilePin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "newSecretQuestion",
    "newSecretAnswer",
    "mobilePin"
})
@XmlRootElement(name = "ForgotSecretAnswer", namespace = "http://tempuri.org/")
public class ForgotSecretAnswer
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "InputHeader", namespace = "http://tempuri.org/", nillable = true)
    protected InputHeader inputHeader;
    @XmlElement(name = "ChannelUserIdentifier", namespace = "http://tempuri.org/", nillable = true)
    protected String channelUserIdentifier;
    @XmlElement(name = "CustomerUserName", namespace = "http://tempuri.org/", nillable = true)
    protected String customerUserName;
    @XmlElement(name = "NewSecretQuestion", namespace = "http://tempuri.org/", nillable = true)
    protected SecretQuestion newSecretQuestion;
    @XmlElement(name = "NewSecretAnswer", namespace = "http://tempuri.org/", nillable = true)
    protected String newSecretAnswer;
    @XmlElement(name = "MobilePin", namespace = "http://tempuri.org/", nillable = true)
    protected String mobilePin;

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
     * Gets the value of the newSecretQuestion property.
     * 
     * @return
     *     possible object is
     *     {@link SecretQuestion }
     *     
     */
    public SecretQuestion getNewSecretQuestion() {
        return newSecretQuestion;
    }

    /**
     * Sets the value of the newSecretQuestion property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretQuestion }
     *     
     */
    public void setNewSecretQuestion(SecretQuestion value) {
        this.newSecretQuestion = value;
    }

    /**
     * Gets the value of the newSecretAnswer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewSecretAnswer() {
        return newSecretAnswer;
    }

    /**
     * Sets the value of the newSecretAnswer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewSecretAnswer(String value) {
        this.newSecretAnswer = value;
    }

    /**
     * Gets the value of the mobilePin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePin() {
        return mobilePin;
    }

    /**
     * Sets the value of the mobilePin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePin(String value) {
        this.mobilePin = value;
    }

}
